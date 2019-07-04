package com.taotao.pay.message.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.stereotype.Service;

import com.taotao.pay.message.exception.MessageExecption;
import com.taotao.pay.message.mapper.TransactionMessageMapper;
import com.taotao.pay.message.model.MessagePageBean;
import com.taotao.pay.message.model.TransactionMessage;
import com.taotao.pay.message.service.TransactionMessageService;
/**
 * 消息服务子系统
 * @author Administrator
 *
 */
@Service
public class TransactionMessageServiceImpl implements TransactionMessageService {
	
	@Autowired
	private TransactionMessageMapper transactionMessageMapper;
	
	@Autowired
	private JmsTemplate notifyJmsTemplate;
	
	/**
	 * 预发送消息
	 * @throws MessageExecption 
	 */
	@Override
	public int saveAndWaitingConfirm(TransactionMessage transactionMessage) throws MessageExecption {
		//检验传过来的参数
		checkParams(transactionMessage);
		
		//更新发送状态，将未发送状态改为待发送状态 
		transactionMessage.setStatus("待发送");
		transactionMessage.setEditTime(new Date());
		transactionMessage.setAreadlyDead("NO");
		transactionMessage.setMessageSendTimes(0);
		
		return transactionMessageMapper.insert(transactionMessage);
	}
	
	/**
	 * 确认并发送消息(业务代码执行成功后)
	 * @throws MessageExecption 
	 */
	@Override
	public void confirmAndSendMessage(String messageId) throws MessageExecption {
		//检验传过来的参数
		if(messageId == null) {
			throw new MessageExecption("确认并发送的消息id不能为空");
		}
		
		//根据消息id查询
		TransactionMessage message = transactionMessageMapper.findByMessageId(messageId);
		if(message == null) {
			throw new MessageExecption("根据消息id查询到的消息为空");
		}
		
		//将状态更改为可发送状态 
		transactionMessageMapper.updateStatusAndEditTime(messageId, "发送中", new Date());
		
		//将messageId消息发送到mq队列中
		System.out.println("日志==============》发送消息messageId："+messageId+"到消息通道channel:"+message.getConsumerQueue());
		notifyJmsTemplate.send(message.getConsumerQueue(), new NotifyMessageCreator(messageId));
	}
	
	/**
	 * 存储并发送消息，相当于传统的mq中间件
	 * @throws MessageExecption 
	 */
	@Override
	public void saveAndSendMessage(TransactionMessage transactionMessage) throws MessageExecption {
		//验证参数
		checkParams(transactionMessage);
		
		//将消息存放到消息服务
		transactionMessage.setEditTime(new Date());
		transactionMessage.setAreadlyDead("NO");
		transactionMessage.setStatus("发送中");
		transactionMessageMapper.insert(transactionMessage);
		
		//将messageId消息发送到mq队列中
		String messageId = transactionMessage.getMessageId();
		System.out.println("日志==============》发送消息messageId："+messageId+"到消息通道channel:"+transactionMessage.getConsumerQueue());
		notifyJmsTemplate.send(transactionMessage.getConsumerQueue(), new NotifyMessageCreator(messageId));
	}
	
	/**
	 * 重新发送消息
	 * @throws MessageExecption 
	 */
	@Override
	public void reSendMessage(TransactionMessage transactionMessage) throws MessageExecption {
		//校验参数
		checkParams(transactionMessage);
		
		//更新状态
		transactionMessage.setEditTime(new Date());
		transactionMessage.setMessageSendTimes(transactionMessage.getMessageSendTimes() + 1);
		transactionMessage.setStatus("发送中");
		transactionMessageMapper.update(transactionMessage);
		
		//发送消息到mq队列中
		String messageId = transactionMessage.getMessageId();
		System.out.println("日志==============》发送消息messageId："+messageId+"到消息通道channel:"+transactionMessage.getConsumerQueue());
		notifyJmsTemplate.send(transactionMessage.getConsumerQueue(), new NotifyMessageCreator(messageId));
	}
	
	/**
	 * 删除消息
	 */
	@Override
	public void deleteMessageById(String messageId) {
		transactionMessageMapper.deleteTMByMessageId(messageId);
	}
	
	/**
	 * 消息发送messageCreator
	 */
	private class NotifyMessageCreator implements MessageCreator {
		
		private String messageId;
		
		public NotifyMessageCreator(String messageId) {
			this.messageId = messageId;
		}

		@Override
		public Message createMessage(Session session) throws JMSException {
			
			return session.createTextMessage(messageId);
		}
		
	}
	
	/**
	 * 校验参数
	 */
	private void checkParams(TransactionMessage transactionMessage) throws MessageExecption {
		//验证参数
		if(transactionMessage == null) {
			throw new MessageExecption("存储并发送的消息为空");
		}
		
		if(transactionMessage.getConsumerQueue() == null) {
			throw new MessageExecption("存储并发送的消息队列的队列名为空");
		}
	}
	
	/**
	 * 将更新消息是否已经死亡
	 */
	@Override
	public void updateMessageToAlreadyDead(String isAlReadlyDead) {
		transactionMessageMapper.updateMessageAlreadyDead(isAlReadlyDead);
	}

	/**
	 * 分页查询消息
	 */
	@Override
	public List<TransactionMessage> getMessagePageList(Map<String, String> paramsMap) {
		String status = paramsMap.get("status");
		String isAreadlyDead = paramsMap.get("isAlReadlyDead");
		int page = Integer.parseInt(paramsMap.get("page"));
		int pageSize = Integer.parseInt(paramsMap.get("pageSize"));
		
		List<TransactionMessage> messages = transactionMessageMapper.getMessagePageList(status, isAreadlyDead, (page - 1) * pageSize, pageSize);
		return messages;
	}
	
	/**
	 * 根据消息id查询
	 */
	@Override
	public TransactionMessage getMessageByMessageId(String messageId) {
		
		return transactionMessageMapper.findByMessageId(messageId);
	}

	@Override
	public MessagePageBean queryMessageList(int page, int size) {
		List<TransactionMessage> messageList = transactionMessageMapper.queryMessageList(page, size);
		int total = transactionMessageMapper.account();
		
		MessagePageBean bean = new MessagePageBean();
		bean.setRows(messageList);
		bean.setTotal(total);
		return bean;
	}

	@Override
	public MessagePageBean queryDeadMessageList(int page, int size,
			String areadlyDead) {
		List<TransactionMessage> deadMessageList = transactionMessageMapper.queryDeadMessageList(page, size, areadlyDead);
		int total = transactionMessageMapper.countDeadMessage();
		
		MessagePageBean bean = new MessagePageBean();
		bean.setRows(deadMessageList);
		bean.setTotal(total);
		return bean;
	}

}
