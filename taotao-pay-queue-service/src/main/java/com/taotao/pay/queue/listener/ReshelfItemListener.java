package com.taotao.pay.queue.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import com.taotao.pay.queue.proxy.QueueProxy;
import com.taotao.pay.queue.service.QueueProxyService;
import com.taotao.pay.queue.service.impl.QueueProxyServiceImpl;
/**
 * 商品下架消息
 * @author Administrator
 *
 */
public class ReshelfItemListener implements MessageListener {

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
			proxyService.updateItemStatusFromEs(messageId, 1);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
