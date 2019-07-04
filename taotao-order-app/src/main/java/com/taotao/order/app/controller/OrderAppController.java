package com.taotao.order.app.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.cart.model.Cart;
import com.taotao.cart.service.CartService;
import com.taotao.iteminventory.model.ItemInventory;
import com.taotao.iteminventory.service.ItemInventoryRequestService;
import com.taotao.iteminventory.service.ItemInventoryService;
import com.taotao.order.app.constant.OrderConstant;
import com.taotao.order.app.model.OrderAndShippingMessage;
import com.taotao.order.app.utils.JsonUtils;
import com.taotao.order.app.utils.TaotaoUtils;
import com.taotao.order.app.zookeeper.ZooKeeperSession;
import com.taotao.order.model.Order;
import com.taotao.order.model.OrderItem;
import com.taotao.order.model.OrderPageBean;
import com.taotao.order.model.OrderShipping;
import com.taotao.order.service.OrderService;
import com.taotao.pay.message.model.TransactionMessage;
import com.taotao.pay.message.service.TransactionMessageService;
import com.taotao.user.model.User;
/**
 * 订单对外服务
 * @author Administrator
 *
 */
@Controller
public class OrderAppController {
	
	@Autowired
	private CartService cartService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private ItemInventoryRequestService itemInventoryRequestService;
	@Autowired
	private TransactionMessageService transactionMessageService;
	
	/**
	 * 去结算接口，展示订单结算页面
	 */
	@RequestMapping("/order/order-cart")
	public String getCartsFromRedis(HttpServletRequest request, Model model) {
		User user = (User) request.getAttribute("user");
		//调用购物车服务，从redis查询购物车宝贝清单
		List<Cart> carts = cartService.findCartsByUserId(user.getId());
		System.out.println("日志==============》从购物车系统服务查询用户"+user.getUsername()+"购物车清单:" + carts);
		getOneImage(carts);
		System.out.println("日志==============》处理后的购物车:" + carts);
		
		model.addAttribute("cartList", carts);
		return "order-cart";
	}
	
	/**
	 * 将多个图片字符串变成一张
	 * @param carts
	 */
	private void getOneImage(List<Cart> carts) {
		//只返回一张图片
		if(carts != null && carts.size() > 0) {
			for (Cart cart : carts) {
				String images = cart.getImage();
				String[] imagesArray = images.split(",");
				cart.setImage(imagesArray[0]);
			}
		}
	}
	
	/**
	 * 提交订单服务接口(需要确保幂等)
	 * @throws Exception 
	 */
	@RequestMapping("/order/create")
	@ResponseBody
	public TaotaoUtils addOrder(OrderShipping orderShipping, HttpServletRequest request) throws Exception {
		User user = (User) request.getAttribute("user");
		/**
		 * 幂等操作，先查询该用户是否已经有订单创建
		 */
		//调用订单系统服务，根据用户id和订单状态查询
		Order selectOrder = orderService.findOrderByUserId(user.getId(), "未支付");
		System.out.println("日志====================》查询用户"+ user.getUsername() + "订单信息" + selectOrder);
		
		/**
		 * 当用户不存在未支付订单时，调用库存服务，减少库存，减少库存成功后创建订单
		 * 分布式事务采用可靠消息最终一致性方案
		 * 
		 */
		if(selectOrder == null) {
			//校验库存是否充足
			String itemIds = "";
			//调用购物车服务，查询购物车的数量
			List<Cart> carts = cartService.findCartsByUserId(user.getId());
			System.out.println("日志===========》查询redis中购物车:"+carts);
			for (int i = 0; i < carts.size(); i++) {
				if(i < carts.size() - 1) {
					itemIds += carts.get(i).getId() + ",";
				} else {
					itemIds += carts.get(i).getId();
				}
			}
			System.out.println("日志============》拼装itemids:" + itemIds);
			
			//检查库存
			TaotaoUtils checkResult = checkItemInventory(carts, user.getId(), itemIds);
			
			//库存充足
			if(checkResult.getStatus() == 200) {
				//加锁
				ZooKeeperSession zkSession = ZooKeeperSession.getInstance();
				String path = "/updateItemInventory_"+user.getId();
				TaotaoUtils acquireLock = zkSession.acquireLock(path);
				
				//加锁超时，直接返回业务繁忙
				if(acquireLock.getStatus() != 200) {
					System.out.println("日志============》加锁超时");
					return TaotaoUtils.build(500, "当前业务繁忙,请稍后重试");
				}
				
				//加锁成功后，需要再次检查一下库存是否充足，因为库存数量可能被其他线程更新了
				System.out.println("日志=============》更新库存锁加锁成功");
				checkResult = checkItemInventory(carts, user.getId(), itemIds);
				if(checkResult.getStatus() != 200) {
					return TaotaoUtils.build(500, "亲！商品库存不足，请重新下单");
				}
				
				//创建订单号
				String orderId = createOrderId(user.getId());
				
				//调用购物车系统服务，获取购物车信息
				List<Cart> paramsCarts = cartService.findCartsByUserId(user.getId());
				List<OrderItem> orderItems = new ArrayList<OrderItem>();
				for(Cart cart : paramsCarts) {
					OrderItem orderItem = new OrderItem();
					orderItem.setItemId(cart.getId());
					orderItem.setImage(cart.getImage());
					orderItem.setNum(cart.getNum());
					orderItem.setPrice(cart.getPrice());
					orderItem.setProductName(cart.getTitle());
					orderItem.setTotalFee(cart.getPrice() * cart.getNum());
					orderItem.setOrderId(orderId);
					
					orderItems.add(orderItem);
				}
				
				//更新库存
				List<ItemInventory> itemInventorys = (List<ItemInventory>) checkResult.getData();
				updateInventoryAndCreateOrder(orderItems, orderShipping, user.getId(), itemIds, itemInventorys, orderId);
				
				//解锁
				zkSession.releaseLock(path);
				
			} else {
				
				return checkResult;
			}
		} else {
			return TaotaoUtils.build(500, "请支付未完成的订单");
		}
		
		return TaotaoUtils.ok();
	}
	
	/**
	 * 校验库存是否充足，并且封装好要更新的库存
	 */
	private TaotaoUtils checkItemInventory(List<Cart> carts, long userId, String itemIds) {
		List<ItemInventory> list = new ArrayList<ItemInventory>();
		//调用商品库存服务批量查询接口，查询商品库存数量
		List<ItemInventory> inventorys = itemInventoryRequestService.readItemInventorysRequest(itemIds);
		
		//判断是否有库存不足
		for(int i = 0; i < inventorys.size(); i++) {
			if(inventorys.get(i).getInventory() - carts.get(i).getNum() < 0) {
				System.out.println("商品id:"+inventorys.get(i).getItemId()+"库存不足");
				return TaotaoUtils.build(500, "商品id:"+inventorys.get(i).getItemId()+"库存不足");
			}
			
			//全部库存充足，返回封装好的itemInventory
			ItemInventory itemInventory = new ItemInventory();
			itemInventory.setCreated(new Date());
			itemInventory.setId(inventorys.get(i).getId());
			itemInventory.setInventory(inventorys.get(i).getInventory() - carts.get(i).getNum());
			itemInventory.setItemId(inventorys.get(i).getItemId());
			
			list.add(itemInventory);
		}
		
		return TaotaoUtils.ok(list);
	}
	
	/**
	 * 库存充足的情况下，减少库存，采用可靠消息最终一致性方案创建订单
	 * @throws Exception 
	 */
	private void updateInventoryAndCreateOrder(List<OrderItem> orderItems, OrderShipping orderShipping, 
			long userId, String itemIds, List<ItemInventory> itemInventorys, String orderId) throws Exception {
		//预发送消息
		OrderAndShippingMessage oasmessage = new OrderAndShippingMessage();
		oasmessage.setOrder(createOrder(orderItems, orderId, userId));
		oasmessage.setOrderShipping(orderShipping);
		oasmessage.setOrderItems(orderItems);
		
		String messageId = UUID.randomUUID().toString();
		TransactionMessage transactionMessage = new TransactionMessage();
		transactionMessage.setConsumerQueue("createOrderQueue");//消息的queue通道名称
		transactionMessage.setCreateTime(new Date());
		transactionMessage.setMessageBody(JsonUtils.objectToJson(oasmessage));//订单和物流信息消息
		transactionMessage.setMessageDataType("JSON");//消息数据类型
		transactionMessage.setMessageId(messageId);
		transactionMessage.setField1(String.valueOf(userId));//将扩展字段field1作为userId字段
		transactionMessage.setMessageSendTimes(0);//发送次数
		transactionMessage.setVersion(1);
		
		transactionMessageService.saveAndWaitingConfirm(transactionMessage);
		
		//业务逻辑，批量更新库存
		itemInventoryRequestService.updateItemInventorysRequest(itemInventorys);
		
		//确认并发送消息到消息队列系统
		transactionMessageService.confirmAndSendMessage(messageId);
	}
	
	
	/**
	 * 生成订单号
	 */
	private String createOrderId(long userId) {
		//日期加上用户id
		Date date = new Date();
		String orderId = "";
		orderId = String.valueOf(date.getTime()) + userId;
		
		return orderId;
	}
	
	/**
	 * 封装order信息
	 */
	private Order createOrder(List<OrderItem> orderItems, String orderId, long userId) {
		if(orderItems != null && orderItems.size() > 0) {
			long totalFee = 0l;
			for (OrderItem orderItem : orderItems) {
				totalFee += orderItem.getTotalFee();
			}
			
			Order order = new Order();
			order.setId(orderId);
			order.setCreateTime(new Date());
			order.setField1(String.valueOf(userId));
			order.setVersion(1);
			order.setMerchantNo("123");//商家编号
			order.setMerchantOrderNo(orderId);//商户订单号
			order.setStatus("未支付");
			order.setOrderAmount(totalFee);
			
			System.out.println("日志=============封装订单order:" + order);
			return order;
		}
		return null;
	}
	
	/**
	 * 我的订单页面
	 * 获取该用户的订单记录
	 */
	@RequestMapping("/order/getUserOrderList")
	@ResponseBody
	public OrderPageBean getUserOrders(int page, int rows) {
		OrderPageBean orderPageBean = orderService.findOrdersByPageAndSize((page - 1)*rows, rows);
		
		return orderPageBean;
	}
	
	/**
	 * 我的订单页面
	 */
	@RequestMapping("/order/myOrders")
	public String myOrderPage(HttpServletRequest request, Model model) {
		User user = (User) request.getAttribute("user");
		model.addAttribute("user", user);
		return "UserOrder";
	}
	
	/**
	 * 取消订单
	 */
	@RequestMapping("/order/cancelOrder")
	public TaotaoUtils cancelOrder(@RequestParam(name = "reason1", defaultValue = "")String reason1, @RequestParam(name = "reason2", defaultValue = "")String reason2, @RequestParam(name = "reason3", defaultValue = "")String reason3,
			@RequestParam(name = "reason4", defaultValue = "")String reason4, String orderId) {
		try {
			if(orderId.equals("") || orderId == null) {
				return TaotaoUtils.build(502, "订单号不能为空");
			} 
			//先判断该订单是否为未支付状态（幂等）
			Order order = orderService.findOrderById(orderId);
			if(!order.getStatus().equals(OrderConstant.NOPAY)) {
				return TaotaoUtils.build(500, "订单" + order.getStatus());
			}
			
			//将订单取消
			String reasons = reason1 + "" + reason2 + "" + reason3 + "," + reason4;
			orderService.updateOrderStatusByOrderId(orderId, new Date(), OrderConstant.CANCEL, reasons);
		} catch (Exception e) {
			return TaotaoUtils.build(500, "网络异常,取消订单失败");
		}
		
		return TaotaoUtils.ok();
	}
	
}
