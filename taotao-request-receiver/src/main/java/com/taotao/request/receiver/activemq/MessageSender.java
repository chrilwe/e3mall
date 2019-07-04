package com.taotao.request.receiver.activemq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.context.ContextLoader;

/**
 * 发送消息到mq中
 * @author Administrator
 *
 */
public class MessageSender {
	
	public void sendMessage(long itemId) {
		//获取jmstemplate
		JmsTemplate jmsTemplate = ContextLoader.getCurrentWebApplicationContext().getBean(JmsTemplate.class);
		jmsTemplate.send(new MqMessageCreator(itemId));
	}
	
	private class MqMessageCreator implements MessageCreator {
		
		private long itemId;
		
		public MqMessageCreator(long itemId) {
			this.itemId = itemId;
		}

		@Override
		public Message createMessage(Session session) throws JMSException {
			TextMessage textMessage = session.createTextMessage();
			textMessage.setText(String.valueOf(itemId));
			return textMessage;
		}
		
	}
}
