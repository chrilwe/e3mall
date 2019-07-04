package com.taotao.item.price.service.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.taotao.item.price.service.thread.RequestThreadPool;

public class InitListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		RequestThreadPool.init();
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}

}
