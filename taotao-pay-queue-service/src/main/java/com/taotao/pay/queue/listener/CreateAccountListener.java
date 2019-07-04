package com.taotao.pay.queue.listener;

import java.util.Date;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.web.context.ContextLoader;

import com.taotao.order.model.Order;
import com.taotao.pay.account.model.AccountRecord;
import com.taotao.pay.account.service.AccountService;
import com.taotao.pay.message.model.TransactionMessage;
import com.taotao.pay.message.service.TransactionMessageService;
import com.taotao.pay.queue.proxy.QueueProxy;
import com.taotao.pay.queue.service.QueueProxyService;
import com.taotao.pay.queue.service.impl.QueueProxyServiceImpl;
import com.taotao.pay.queue.utils.JsonUtils;

public class CreateAccountListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage)message;
		String messageId = "";
		try {
			messageId = textMessage.getText();
		} catch (JMSException e) {
			e.printStackTrace();
		}
		if(messageId == null) {
			return;
		}
		
		//调用会计记账代理对象，进行记账
		QueueProxyService service = new QueueProxyServiceImpl();
		QueueProxy proxy = new QueueProxy(service, messageId);
		QueueProxyService proxyService = proxy.createProxy();
		proxyService.createAccount(messageId);
	}

}
