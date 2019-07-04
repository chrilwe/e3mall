package com.taotao.iteminventory.service.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.taotao.iteminventory.service.thread.RequestThreadPool;

/**
 * spring初始化容器
 * @author Administrator
 *
 */
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
