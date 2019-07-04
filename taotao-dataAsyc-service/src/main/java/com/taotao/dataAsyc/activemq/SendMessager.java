package com.taotao.dataAsyc.activemq;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 * 发送变更消息到消息队列，数据聚合服务接收到消息，更新该维度的数据
 * @param dataType 维度的类型
 * @param eventType 增删改的内容类型
 * @param dimId 聚合数据的id
 * @Param sendType 发送到对应的消息队列类型
 */
public class SendMessager {
	private String dataType;
	
	private String eventType;
	
	private long dimId;
	
	private JmsTemplate jmsTemplate;
	
	private Destination destination;
	
	public SendMessager(String dataType, String eventType, 
			long dimId, JmsTemplate jmsTemplate, Destination destination) {
		this.dataType = dataType;
		this.eventType = eventType;
		this.dimId = dimId;
		this.destination = destination;
		this.jmsTemplate = jmsTemplate;
	}
	
	public void sendMessage() {
		jmsTemplate.send(destination, new MyMessageCreator());
	}
	
	private class MyMessageCreator implements MessageCreator {

		@Override
		public Message createMessage(Session session) throws JMSException {
			TextMessage message = session.createTextMessage(dataType + "," + eventType + "," + dimId);
			return message;
		}
		
	}
}
