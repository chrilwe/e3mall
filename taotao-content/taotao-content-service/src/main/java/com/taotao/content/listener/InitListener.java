package com.taotao.content.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.taotao.content.thread.ContentThreadPool;

public class InitListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ContentThreadPool.init();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}

}
