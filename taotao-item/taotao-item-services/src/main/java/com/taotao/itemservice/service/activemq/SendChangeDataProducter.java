package com.taotao.itemservice.service.activemq;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 * 变更消息发送者
 * @author Administrator
 *
 */
public class SendChangeDataProducter {
	
	@Autowired
	private Destination itemServiceDestination;
	
	@Autowired
	private Destination changeHighProxDestination;
	
	@Autowired
	private Destination changeBetchDestination;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	/**
	 * 将消息发送到消息队列中 
	 * @param dataType
	 * @param eventType 
	 * @param id
	 * @param queueType 队列类型
	 */
	public void sendMessage(String dataType, String eventType, long id, String queueType) {
		if(queueType.equals("waitQueue")) {
			//等待队列
			jmsTemplate.send(itemServiceDestination, new MyMessageCreator(dataType, eventType, id));
		} else if(queueType.equals("hightProxQueue")) {
			//高优先级队列
			jmsTemplate.send(changeHighProxDestination, new MyMessageCreator(dataType, eventType, id));
		} else if(queueType.equals("betchQueue")) {
			//批量数据处理队列
			jmsTemplate.send(changeBetchDestination, new MyMessageCreator(dataType, eventType, id));
		}
	}
	
	/**
	 * 消息制造类
	 */
	private class MyMessageCreator implements MessageCreator {
		
		private String dataType;
		
		private String eventType;
		
		private long id;
		
		public MyMessageCreator(String dataType, String eventType, long id) {
			this.dataType = dataType;
			this.eventType = eventType;
			this.id = id;
		}

		@Override
		public Message createMessage(Session session) throws JMSException {
			TextMessage message = session.createTextMessage(dataType + "," + eventType + "," + id);
			return message;
		}
		
	}
}
