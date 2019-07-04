package com.taotao.order.app.zookeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

import com.taotao.order.app.utils.TaotaoUtils;

/**
 * zookeeper分布式锁
 * @author Administrator
 *
 */
public class ZooKeeperSession {
	
	private ZooKeeper zooKeeper;
	
	private CountDownLatch cdl = new CountDownLatch(1);
	
	public ZooKeeperSession() {
		try {
			zooKeeper = new ZooKeeper("192.168.43.126:2181", 60 * 1000, new ZooKeeperWatcher());
			System.out.println("日志============》zookeeper连接状态：" + zooKeeper.getState());
			cdl.await();
			
			System.out.println("日志============》zookeeper连接状态:"+zooKeeper.getState());
		} catch (Exception e) {
			System.out.println("异常============》连接zookeeper异常:"+e);
		}
	}
	
	/**
	 * 创建一个监控watcher
	 * @author Administrator
	 *
	 */
	private class ZooKeeperWatcher implements Watcher {

		@Override
		public void process(WatchedEvent event) {
			if(KeeperState.SyncConnected == event.getState()) {
				cdl.countDown();
			}
		}
		
	}
	
	/**
	 * 获得锁
	 */
	public TaotaoUtils acquireLock(String path) {
		try {
			zooKeeper.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			
		} catch (Exception e) {
			long waitTime = 0l;
			long startTime = System.currentTimeMillis();
			while(true) {
				if(waitTime > 5 * 1000) {
					System.out.println("日志===========》获取锁超过5000ms,不再继续获取锁");
					return TaotaoUtils.build(500, "获取锁超时");
				}
				try {
					zooKeeper.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
				} catch (Exception e2) {
					waitTime = System.currentTimeMillis() - startTime;
					continue;
				}
				break;
			}
		}
		return TaotaoUtils.ok();
	}
	
	/**
	 * 释放锁
	 */
	public void releaseLock(String path) {
		try {
			zooKeeper.delete(path, -1);
		} catch (Exception e) {
			System.out.println("异常=============》释放锁失败:"+e);
		} 
	}
	
	/**
	 * 静态内部类
	 */
	private static class Singleton {
		private static ZooKeeperSession zkSession = null;
		
		static {
			zkSession = new ZooKeeperSession();
		}
		
		private static ZooKeeperSession getInstance() {
			
			return zkSession;
		}
	}
	
	/**
	 * 获取单例
	 */
	public static ZooKeeperSession getInstance() {
		
		return Singleton.getInstance();
	}
}
