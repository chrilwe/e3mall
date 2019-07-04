package com.taotao.cache.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.taotao.cache.thread.RebuildContentInfoCacheThread;
import com.taotao.cache.thread.RebuildItemInfoCacheThread;

/**
 * 随着项目的启动初始化
 * @author Administrator
 *
 */
public class InitListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		new Thread(new RebuildItemInfoCacheThread()).start();
		new Thread(new RebuildContentInfoCacheThread()).start();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}

}
