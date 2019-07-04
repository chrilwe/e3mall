package com.taotao.order.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.mengyun.tcctransaction.api.Compensable;
import org.mengyun.tcctransaction.api.TransactionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taotao.order.mapper.OrderItemMapper;
import com.taotao.order.mapper.OrderMapper;
import com.taotao.order.mapper.OrderRecordMapper;
import com.taotao.order.mapper.OrderShippingMapper;
import com.taotao.order.model.Order;
import com.taotao.order.model.OrderItem;
import com.taotao.order.model.OrderPageBean;
import com.taotao.order.model.OrderRecord;
import com.taotao.order.model.OrderShipping;
import com.taotao.order.service.OrderService;
import com.taotao.point.account.service.TccPointAccountService;
/**
 * 订单基本信息服务
 * @author Administrator
 *
 */
@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderRecordMapper orderRecordMapper;
	@Autowired
	private OrderShippingMapper orderShippingMapper;
	@Autowired
	private OrderItemMapper orderItemMapper;
	@Autowired
	private TccPointAccountService tccPointAccountService;
	
	
	/**
	 * 根据订单号查询订单
	 */
	@Override
	public Order findOrderById(String orderId) {
		
		return orderMapper.findOrderById(orderId);
	}
	
	/**
	 * 根据id查询订单记录
	 */
	@Override
	public OrderRecord findOrderRecordById(String id) {
		
		return orderRecordMapper.findOrderRecordById(id);
	}
	
	
	/**
	 * 创建订单记录
	 */
	@Override
	@Transactional
	public void addOrderRecord(OrderRecord orderRecord) {
		orderRecordMapper.addOrderRecord(orderRecord);
	}
	
	/**
	 * 创建订单
	 */
	@Override
	@Transactional
	public void addOrder(Order order, OrderRecord orderRecord, List<OrderItem> orderItems) {
		//插入订单数据 
		orderMapper.insertOrder(order);
		//插入订单记录
		orderRecordMapper.addOrderRecord(orderRecord);
		//插入商品订单联系表
		for (OrderItem orderItem : orderItems) {
			orderItemMapper.addOrderItem(orderItem);
		}
	}
	
	/**
	 * 根据userid查询
	 */
	@Override
	public Order findOrderByUserId(long userId, String status) {
		
		return orderMapper.findOrderByUserId(userId, status);
	}
	
	/**
	 * 根据事务编号messageId查询订单记录
	 */
	@Override
	public OrderRecord findOrderRecordByTrxNo(String messageId) {
		
		return orderRecordMapper.findOrderRecordByTrxNo(messageId);
	}
	
	/**
	 * 根据订单号查询订单商品联系表
	 */
	@Override
	public List<OrderItem> findOrderItemByOrderId(String orderId) {
		
		return orderItemMapper.findOrderItemByOrderId(orderId);
	}
	
	/**
	 * 更新订单状态
	 */
	@Override
	public void updateOrderStatusByOrderId(String orderId, Date editTime,
			String status) {
		orderMapper.updateOrderStatusByOrderId(orderId, editTime, status);
		orderRecordMapper.updateOrderRecordStatus(orderId, status, editTime);
	}
	
	/**
	 * TCC事务，更新订单状态和更新积分余额
	 */
	@Override
	@Transactional
	@Compensable(confirmMethod = "confirmCompleteSuccessOrder",cancelMethod = "cancelCompleteSuccessOrder")
	public void updateOrderStatusAndUpdatePointAccountByTcc(Map<String, String> params) {
		System.out.println("日志====================订单状态TCC尝试阶段开始===============");
		String orderId = params.get("orderId");
		//修改支付记录状态并且记录支付流水号
		orderRecordMapper.updateOrderRecordStatus(orderId, "支付中", new Date());
		orderRecordMapper.updateOrderBankOrderNo(orderId, params.get("tradeNo"));
		
		//更新积分
		tccPointAccountService.updateOrCreatePointAccount(null, params);
		System.out.println("日志====================订单状态TCC尝试阶段完成===============");
	}
	
	/**
	 * TCC事务确认阶段
	 */
	@Transactional
	public void confirmCompleteSuccessOrder(Map<String, String> params) {
		System.out.println("日志====================订单状态TCC确认阶段开始===============");
		String orderId = params.get("orderId");
		//修改订单记录状态
		orderMapper.updateOrderStatusByOrderId(orderId, new Date(), "已支付");
		
		//修改订单状态
		orderRecordMapper.updateOrderRecordStatus(orderId, "已支付", new Date());
		System.out.println("日志====================订单状态TCC确认阶段完成===============");
	}
	
	/**
	 * TCC事务取消阶段
	 */
	public void cancelCompleteSuccessOrder(Map<String, String> params) {
		System.out.println("日志====================订单状态TCC取消阶段开始===============");
		String orderId = params.get("orderId");
		//如果订单状态为未支付或者是已支付，就不能进行取消阶段
		OrderRecord orderRecord = orderRecordMapper.findOrderRecordByBankOrderNo(orderId);
		if(orderRecord.getStatus().equals("未支付") || 
				orderRecord.getStatus().equals("已支付") || 
				orderRecord.getStatus().equals("等待支付")) {
			System.out.println("日志===============》订单状态为未支付或者是已支付，就不能进行取消阶段");
			return;
		}
		
		//更新订单状态为CANCEL状态，等待支付
		orderRecordMapper.updateOrderRecordStatus(orderId, "等待支付", new Date());
		orderMapper.updateOrderStatusByOrderId(orderId, new Date(), "等待支付");
		System.out.println("日志================》CANCEL等待支付状态");
		System.out.println("日志====================订单状态TCC取消阶段完成===============");
	}
	
	/**
	 * 订单分页查询
	 */
	@Override
	public OrderPageBean findOrdersByPageAndSize(int page, int size) {
		List<Order> orders = orderMapper.findOrdersByPageAndSize(page, size);
		int total = orderMapper.count();
		OrderPageBean orderPageBean = new OrderPageBean();
		orderPageBean.setTotal(total);
		orderPageBean.setRows(orders);
		return orderPageBean;
	}
	
	/**
	 * 批量删除订单
	 */
	@Override
	@Transactional
	public void deleteOrders(String ids) {
		/**
		 * 解析要删除的订单号
		 */
		String[] idsArray = ids.split(",");
		for (String orderId : idsArray) {
			orderMapper.deleteOrder(orderId, "已删除");
			orderRecordMapper.deleteOrderRecord(orderId, "已删除");
		}
	}
	
	/**
	 * 订单记录分页查询
	 */
	@Override
	public OrderPageBean findOrderRecordsByPageAndSize(int page, int size) {
		int total = orderRecordMapper.count();
		List<OrderRecord> orderRecords = orderRecordMapper.findOrderRecordsByPageAndSize(page, size);
		
		OrderPageBean orderPageBean = new OrderPageBean();
		orderPageBean.setRows(orderRecords);
		orderPageBean.setTotal(total);
		return orderPageBean;
	}
	
	/**
	 * 获取未支付订单的数目
	 */
	@Override
	public int countNoPayOrder(String status) {
		
		return orderMapper.countNoPayOrder(status);
	}
	
	/**
	 * 分页查询未支付订单
	 */
	@Override
	public List<Order> findNoPayOrderByPageAndSize(String status, int page,
			int size) {
		
		return orderMapper.findNoPayOrderByPageAndSize(status, page, size);
	}
	
	/**
	 * 取消订单
	 */
	@Override
	@Transactional
	public void updateOrderStatusByOrderId(String orderId, Date editTime,
			String status, String cancelReason) {
		orderRecordMapper.updateOrderRecordStatus(orderId, status, editTime);
		orderMapper.updateOrderStatusAndCancelReasonByOrderId(orderId, editTime, status, cancelReason);
	}
	
	/**
	 * 退款
	 */
	@Override
	@Transactional
	public void refundOrder(String cancelReason, String orderId, String status, Date editTime, String isRefund, int refundTimes, double refuundAmount) {
		orderMapper.updateRefundMessage(cancelReason, orderId, status, editTime, isRefund, refundTimes, refuundAmount);
		orderRecordMapper.updateRefundMessage(orderId, status, editTime, isRefund, refundTimes, refuundAmount);
	}

}
