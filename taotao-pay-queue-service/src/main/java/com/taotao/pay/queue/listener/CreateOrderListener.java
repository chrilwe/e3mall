package com.taotao.pay.queue.listener;

import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.web.context.ContextLoader;

import com.taotao.order.model.Order;
import com.taotao.order.model.OrderItem;
import com.taotao.order.model.OrderRecord;
import com.taotao.order.model.OrderShipping;
import com.taotao.order.service.OrderService;
import com.taotao.pay.message.model.TransactionMessage;
import com.taotao.pay.message.service.TransactionMessageService;
import com.taotao.pay.queue.model.OrderAndShippingMessage;
import com.taotao.pay.queue.proxy.QueueProxy;
import com.taotao.pay.queue.service.QueueProxyService;
import com.taotao.pay.queue.service.impl.QueueProxyServiceImpl;
import com.taotao.pay.queue.utils.JsonUtils;
/**
 * 减少库存后，创建订单分布式事务的监听
 * @author Administrator
 *
 */
public class CreateOrderListener implements MessageListener {

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
		proxyService.createOrder(messageId);
	}

}
