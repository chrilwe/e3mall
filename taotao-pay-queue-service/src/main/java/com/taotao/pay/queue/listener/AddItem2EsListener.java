package com.taotao.pay.queue.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import com.taotao.pay.queue.proxy.QueueProxy;
import com.taotao.pay.queue.service.QueueProxyService;
import com.taotao.pay.queue.service.impl.QueueProxyServiceImpl;
/**
 * 将商品 导入搜索库中
 * @author Administrator
 *
 */
public class AddItem2EsListener implements MessageListener {

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
		try {
			proxyService.addItemToEs(messageId);
		} catch (Exception e) {
			
			e.printStackTrace();
		} 
	}

}
