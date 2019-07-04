package com.taotao.pay.queue.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.jms.Message;
import javax.jms.TextMessage;

import org.springframework.web.context.ContextLoader;

import com.taotao.pay.message.service.TransactionMessageService;
import com.taotao.pay.queue.service.QueueProxyService;
import com.taotao.pay.queue.service.impl.QueueProxyServiceImpl;
/**
 * 动态代理对象
 * @author Administrator
 *
 */
public class QueueProxy implements InvocationHandler {
	
	private QueueProxyService queueProxyService;
	private String messageId;
	
	public QueueProxy(QueueProxyService queueProxyService, String messageId) {
		this.queueProxyService = queueProxyService;
		this.messageId = messageId;
	}

	/**
	 * 生成动态代理
	 */
	public QueueProxyService createProxy() {
		return (QueueProxyService) Proxy.newProxyInstance(QueueProxyServiceImpl.class.getClassLoader(), QueueProxyServiceImpl.class.getInterfaces(), this);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object result = method.invoke(queueProxyService, args);
		last(messageId);
		return result;
	}
	
	/**
	 * 后置通知
	 */
	private void last(String messageId) {
		TransactionMessageService transactionMessageService = ContextLoader.getCurrentWebApplicationContext().getBean(TransactionMessageService.class);
		transactionMessageService.deleteMessageById(messageId);
	}

}
