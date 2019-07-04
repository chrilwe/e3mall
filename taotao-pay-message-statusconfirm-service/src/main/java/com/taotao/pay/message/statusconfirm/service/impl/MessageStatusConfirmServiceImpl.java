package com.taotao.pay.message.statusconfirm.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;

import com.taotao.order.model.OrderRecord;
import com.taotao.order.service.OrderService;
import com.taotao.pay.message.model.TransactionMessage;
import com.taotao.pay.message.service.TransactionMessageService;
import com.taotao.pay.message.statusconfirm.service.MessageStatusConfirmService;
/**
 * 消息状态确认子系统服务
 * @author Administrator
 *
 */
@Service
public class MessageStatusConfirmServiceImpl implements
		MessageStatusConfirmService {
	
	/**
	 * 处理状态为待发送状态超时的消息
	 * @throws Exception 
	 */
	@Override
	public void handlerTradeWaitingConfirmTimeOutMessage() throws Exception {
		
		/**
		 * 将所有的状态为待发送状态并且超时状态的消息查询出来
		 */
		//调用分页查询接口
		TransactionMessageService transactionMessageService = ContextLoader.getCurrentWebApplicationContext().getBean(TransactionMessageService.class);
		//查询条件
		Map<String, String> paramsMap = createFindParams(String.valueOf(10), String.valueOf(1), "待发送", "NO");
		System.out.println("日志===================》分页查询条件:"+paramsMap);
						
		//调用分页查询接口
		List<TransactionMessage> messages = transactionMessageService.getMessagePageList(paramsMap);
		System.out.println("日志=================》调用消息系统服务，查询待发送状态并且未进入死亡队列的消息:"+messages);
		
		//处理待发送状态消息的逻辑
		//如果订单更新成功，重新确认并发送消息发送消息，未更新成功，删除消息
		if(messages != null && messages.size() > 0) {
			for(TransactionMessage transactionMessage : messages) {
				String messageId = transactionMessage.getMessageId();
				OrderService orderService = ContextLoader.getCurrentWebApplicationContext().getBean(OrderService.class);
				//调用订单系统服务，查询订单状态
				OrderRecord orderRecord = orderService.findOrderRecordByTrxNo(messageId);
				System.out.println("日志=================》调用订单系统服务，查询订单状态:"+orderRecord.getStatus());
				if(orderRecord.getStatus().equals("未支付")) {
					transactionMessageService.deleteMessageById(messageId);
				} else {
					transactionMessageService.confirmAndSendMessage(messageId);
				}
			}
		}
	}
	
	/**
	 * 处理状态为发送中状态超时没有消费的消息
	 * @throws Exception 
	 */
	@Override
	public void handlerTradeSendingTimeOutMessage() throws Exception {
		//调用分页查询接口
		TransactionMessageService transactionMessageService = ContextLoader.getCurrentWebApplicationContext().getBean(TransactionMessageService.class);
		//查询条件
		Map<String, String> paramsMap = createFindParams(String.valueOf(10), String.valueOf(1), "发送中", "NO");
		System.out.println("日志===================》分页查询条件:"+paramsMap);
						
		//调用分页查询接口
		List<TransactionMessage> messages = transactionMessageService.getMessagePageList(paramsMap);
		System.out.println("日志=================》调用消息系统服务，查询发送中状态并且未进入死亡队列的消息:"+messages);
		
		//先过滤超过规定重发次数的消息，让它进入死亡队列
		//再过滤未超时的消息
		if(messages != null && messages.size() > 0) {
			for(TransactionMessage transactionMessage : messages) {
				Integer messageSendTimes = transactionMessage.getMessageSendTimes();
				if(messageSendTimes > 5) {
					//调用消息系统服务，让消息进入死亡
					transactionMessageService.updateMessageToAlreadyDead("YES");
					continue;
				}
				
				//判断消息是否超时
				long createTime = transactionMessage.getCreateTime().getTime();
				long currentTime = System.currentTimeMillis();
				if((currentTime - createTime) > 5 * 60 * 1000) {
					System.out.println("日志===============》消息messageId="+transactionMessage.getMessageId()+"发送状态为：发送中超时，重新发送消息");
					//调用消息系统，重新发送消息
					transactionMessageService.reSendMessage(transactionMessage);
				} else {
					System.out.println("日志===============》消息messageId="+transactionMessage.getMessageId()+"发送状态为：发送中没有超时，消息恢复子系统暂时不处理消息");
					continue;
				}
			}
		}
	}
	
	/**
	 * 查询条件的封装
	 */
	private Map<String, String> createFindParams(String pageSize, String currentPage, 
			String status, String isAlReadlyDead) {
		//查询条件
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("pageSize", pageSize);
		paramsMap.put("page", currentPage);
		paramsMap.put("status", status);
		paramsMap.put("isAlReadlyDead", isAlReadlyDead);
		
		return paramsMap;
	}
	
}
