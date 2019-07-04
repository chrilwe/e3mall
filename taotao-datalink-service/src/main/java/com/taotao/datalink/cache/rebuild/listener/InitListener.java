package com.taotao.datalink.cache.rebuild.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.taotao.datalink.cache.rebuild.thread.RebuildItemBrandThread;
import com.taotao.datalink.cache.rebuild.thread.RebuildItemCatAggrThread;
import com.taotao.datalink.cache.rebuild.thread.RebuildItemDescriptionAggrThread;
import com.taotao.datalink.cache.rebuild.thread.RebuildItemInfoAggrThread;

/**
 * 监听上下文
 * @author Administrator
 *
 */
public class InitListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		RebuildItemBrandThread rebuildItemBrandThread = new RebuildItemBrandThread();
		RebuildItemDescriptionAggrThread rebuildItemDescriptionAggrThread = new RebuildItemDescriptionAggrThread();
		RebuildItemCatAggrThread rebuildItemCatAggrThread = new RebuildItemCatAggrThread();
		RebuildItemInfoAggrThread rebuildItemInfoAggrThread = new RebuildItemInfoAggrThread();
		
		new Thread(rebuildItemBrandThread).start();
		new Thread(rebuildItemDescriptionAggrThread).start();
		new Thread(rebuildItemCatAggrThread).start();
		new Thread(rebuildItemInfoAggrThread).start();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}

}
