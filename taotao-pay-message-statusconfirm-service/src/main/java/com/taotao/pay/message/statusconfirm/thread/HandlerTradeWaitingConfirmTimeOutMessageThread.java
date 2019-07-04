package com.taotao.pay.message.statusconfirm.thread;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoader;

import com.taotao.pay.message.statusconfirm.service.MessageStatusConfirmService;

/**
 * 订单支付处理待发送消息后台线程
 * @author Administrator
 *
 */
public class HandlerTradeWaitingConfirmTimeOutMessageThread implements Runnable {
	
	@Override
	public void run() {
		while(true) {
			try {
				MessageStatusConfirmService messageStatusConfirmService = ContextLoader.getCurrentWebApplicationContext().getBean(MessageStatusConfirmService.class);
				messageStatusConfirmService.handlerTradeWaitingConfirmTimeOutMessage();
				Thread.sleep(60 * 1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
}
