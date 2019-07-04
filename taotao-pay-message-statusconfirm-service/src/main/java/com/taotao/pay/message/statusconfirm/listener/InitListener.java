package com.taotao.pay.message.statusconfirm.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.taotao.pay.message.statusconfirm.thread.HandlerTradeSendingTimeOutMessageThread;
import com.taotao.pay.message.statusconfirm.thread.HandlerTradeWaitingConfirmTimeOutMessageThread;

public class InitListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		new Thread(new HandlerTradeSendingTimeOutMessageThread()).start();
		new Thread(new HandlerTradeWaitingConfirmTimeOutMessageThread()).start();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub

	}

}
