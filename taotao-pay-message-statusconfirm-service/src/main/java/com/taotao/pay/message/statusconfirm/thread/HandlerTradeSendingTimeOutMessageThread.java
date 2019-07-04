package com.taotao.pay.message.statusconfirm.thread;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoader;

import com.taotao.pay.message.statusconfirm.service.MessageStatusConfirmService;

/**
 * 订单支付发送中消息处理
 * @author Administrator
 *
 */
public class HandlerTradeSendingTimeOutMessageThread implements Runnable {

	@Override
	public void run() {
		while(true) {
			try {
				MessageStatusConfirmService messageStatusConfirmService = ContextLoader.getCurrentWebApplicationContext().getBean(MessageStatusConfirmService.class);
				messageStatusConfirmService.handlerTradeSendingTimeOutMessage();
				Thread.sleep(60 * 1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
