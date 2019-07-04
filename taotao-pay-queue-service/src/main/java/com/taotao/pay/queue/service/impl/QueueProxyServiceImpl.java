package com.taotao.pay.queue.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.web.context.ContextLoader;

import com.taotao.es.search.model.EsSearchResultModel;
import com.taotao.es.search.service.EsSearchService;
import com.taotao.item.price.service.ItemPriceRequestService;
import com.taotao.itemservice.model.Item;
import com.taotao.itemservice.model.ItemCategory;
import com.taotao.itemservice.service.ItemCategoryService;
import com.taotao.itemservice.service.ItemInfoService;
import com.taotao.order.model.Order;
import com.taotao.order.model.OrderItem;
import com.taotao.order.model.OrderRecord;
import com.taotao.order.model.OrderShipping;
import com.taotao.order.service.OrderService;
import com.taotao.pay.account.model.Account;
import com.taotao.pay.account.model.AccountRecord;
import com.taotao.pay.account.service.AccountService;
import com.taotao.pay.message.model.TransactionMessage;
import com.taotao.pay.message.service.TransactionMessageService;
import com.taotao.pay.queue.model.OrderAndShippingMessage;
import com.taotao.pay.queue.service.QueueProxyService;
import com.taotao.pay.queue.utils.JsonUtils;
/**
 * 
 * @author Administrator
 *
 */
public class QueueProxyServiceImpl implements QueueProxyService {
	
	/**
	 * 会计记账
	 */
	@Override
	public void createAccount(String messageId) {
		//调用会计系统，根据事务messageId查询会计记录
		AccountService accountService = ContextLoader.getCurrentWebApplicationContext().getBean(AccountService.class);
		AccountRecord accountRecord = accountService.findAccountRecordByMessageId(messageId);
		if(accountRecord == null) {
			//调用消息系统服务，获取订单信息
			TransactionMessageService transactionMessageService = ContextLoader.getCurrentWebApplicationContext().getBean(TransactionMessageService.class);
			TransactionMessage transactionMessage = transactionMessageService.getMessageByMessageId(messageId);
			Order order = JsonUtils.jsonToPojo(transactionMessage.getMessageBody(), Order.class);
			
			//调用会计系统，查询会计信息 
			String userNo = transactionMessage.getField1();
			Account selectAccount = accountService.findAccountByUserNo(userNo);
			System.out.println("日志============》调用会计系统，根据userNo:"+userNo+"获取account:"+selectAccount);
			
			String accountNo = UUID.randomUUID().toString();
			Account account = getAccount(userNo, selectAccount, order, accountNo);
			AccountRecord record = getAccountRecord(accountNo, order, messageId, userNo);
			System.out.println("日志============》封装要入账的account:"+account+"accountRecord:"+record);
			
			//创建会计记账信息
			accountService.addAccount(account , record);
		}
	}
	
	/**
	 * 创建订单
	 */
	@Override
	public void createOrder(String messageId) {
		//调用订单系统服务，查询订单记录，保证创建订单的幂等性
		OrderService orderService = ContextLoader.getCurrentWebApplicationContext().getBean(OrderService.class);
		OrderRecord or = orderService.findOrderRecordByTrxNo(messageId);
		
		TransactionMessageService transactionMessageService = ContextLoader.getCurrentWebApplicationContext().getBean(TransactionMessageService.class);
		if(or == null) {
			//调用消息系统服务，查询消息记录，获取业务所需的消息messageBody
			TransactionMessage transactionMessage = transactionMessageService.getMessageByMessageId(messageId);
			String messageBody = transactionMessage.getMessageBody();
			System.out.println("日志===============》调用消息系统服务，获取messageId_"+messageId+"的messageBody:"+messageBody);
			
			//解析messageBody
			OrderAndShippingMessage orderAndShippingMessage = JsonUtils.jsonToPojo(messageBody, OrderAndShippingMessage.class);
			Order order = orderAndShippingMessage.getOrder();//订单信息
			OrderShipping orderShipping = orderAndShippingMessage.getOrderShipping();//物流信息
			List<OrderItem> orderItems = orderAndShippingMessage.getOrderItems();//订单商品联系表
			
			//调用订单服务系统，创建订单(主业务逻辑)
			OrderRecord orderRecord = new OrderRecord();
			orderRecord.setBankOrderNo(order.getId());
			orderRecord.setBankReturnMsg(messageId);
			orderRecord.setId(order.getId());
			orderRecord.setVersion(1);
			orderRecord.setTrxNo(messageId);
			orderRecord.setMerchantOrderNo(order.getMerchantOrderNo());
			orderRecord.setMerchantNo(order.getMerchantNo());
			orderService.addOrder(order, orderRecord, orderItems);
		}
	}
	
	/**
	 * 封装account
	 */
	private Account getAccount(String userNo, Account selectAccount, Order order, String accountNo) {
		Account account = new Account();
		account.setId(UUID.randomUUID().toString());
		account.setAccountNo(accountNo);
		account.setAccountType(order.getTrxType());
		account.setBanlance(0.00d);
		account.setCreateTime(new Date());
		account.setId(accountNo);
		account.setSecurityMoney(0.01d);
		account.setSettAmount(0.00d);
		account.setStatus("已记账");
		account.setTodayExpend(Double.parseDouble(String.valueOf(order.getOrderAmount())));
		account.setTodayIncome(0.00d);
		account.setTotalExpend(selectAccount == null ? Double.parseDouble(String.valueOf(order.getOrderAmount())) : Double.parseDouble(String.valueOf(order.getOrderAmount())) + selectAccount.getTodayExpend());
		account.setTotalIncome(0.00d);
		account.setUnbanlance(0.00d);
		account.setUserNo(userNo);
		account.setVersion(0);
		
		return account;
	}
	
	/**
	 * 封装accountRecord
	 */
	private AccountRecord getAccountRecord(String accountNo, Order order, String messageId, String userNo) {
		AccountRecord accountRecord = new AccountRecord();
		accountRecord.setAccountNo(accountNo);
		accountRecord.setAmount(Double.parseDouble(String.valueOf(order.getOrderAmount())));
		accountRecord.setBankTrxNo(messageId);
		accountRecord.setBanlance(0.00d);
		accountRecord.setCreateTime(new Date());
		accountRecord.setId(UUID.randomUUID().toString());
		accountRecord.setIsAllowSett("NO");
		accountRecord.setIsCompleteSett("NO");
		accountRecord.setRequestNo(order.getId());
		accountRecord.setRiskDay(0);
		accountRecord.setStatus("已记账");
		accountRecord.setTrxType("可靠消息");
		accountRecord.setUserNo(userNo);
		accountRecord.setVersion(0);
		accountRecord.setFundDirection("....");
		
		return accountRecord;
	}
	
	/**
	 * 添加商品到搜索库中(TODO 异常流程还没有实现)
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	@Override
	public void addItemToEs(String messageId) throws NumberFormatException, Exception {
		/**
		 * 幂等判断，调用搜索服务，查询搜索库中是否已经存在该商品了
		 */
		TransactionMessageService transactionMessageService = ContextLoader.getCurrentWebApplicationContext().getBean(TransactionMessageService.class);
		TransactionMessage message = transactionMessageService.getMessageByMessageId(messageId);
		String itemId = message.getMessageBody();
		
		//调用商品类型服务，查询搜索索引type的别名
		ItemInfoService itemInfoService = ContextLoader.getCurrentWebApplicationContext().getBean(ItemInfoService.class);
		ItemCategoryService itemCategoryService = ContextLoader.getCurrentWebApplicationContext().getBean(ItemCategoryService.class);
		
		Item item = itemInfoService.getItemInfoById(Long.parseLong(itemId));
		String itemType = itemInfoService.findItemTypeCid(item.getCid());
		
		ItemCategory itemCategory = itemCategoryService.findItemCategoryById(Long.parseLong(itemId));
		String itemCategoryName = itemCategory.getName();
		
		//调用es搜索服务，根据商品id查询
		EsSearchService esSearchService = ContextLoader.getCurrentWebApplicationContext().getBean(EsSearchService.class);
		EsSearchResultModel result = esSearchService.findById(Long.parseLong(itemId), itemType, "TAOTAO");
		if(result == null) {
			EsSearchResultModel model = new EsSearchResultModel();
			model.setCategoryName(itemCategoryName);
			model.setId(itemId);
			model.setImage(item.getImage());
			model.setPrice(item.getPrice());
			model.setSellPoint(item.getSellPoint());
			model.setStatus(item.getStatus());
			model.setTitle(item.getTitle());
			esSearchService.add(model , itemType, "taotao");
		}
	}
	
	/**
	 * 更新搜索库中商品状态
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	@Override
	public void updateItemStatusFromEs(String messageId, int status) throws NumberFormatException, Exception {
		//更新本身是幂等
		//调用消息服务，查询要更新的商品ids
		TransactionMessageService transactionMessageService = ContextLoader.getCurrentWebApplicationContext().getBean(TransactionMessageService.class);
		TransactionMessage message = transactionMessageService.getMessageByMessageId(messageId);
		String ids = message.getMessageBody();
		
		//调用商品类型服务，查询搜索索引type的别名(这里需要优化，做个批量接口)
		String[] idsArray = ids.split(",");
		for (String itemId : idsArray) {
			ItemInfoService itemInfoService = ContextLoader.getCurrentWebApplicationContext().getBean(ItemInfoService.class);
			Item item = itemInfoService.getItemInfoById(Long.parseLong(itemId));
			String itemType = itemInfoService.findItemTypeCid(item.getCid());
			
			//调用搜索服务更新搜索库
			EsSearchService esSearchService = ContextLoader.getCurrentWebApplicationContext().getBean(EsSearchService.class);
			esSearchService.updateItemStatus("taotao", itemType, status, Long.parseLong(itemId));
		}					
	}
}
