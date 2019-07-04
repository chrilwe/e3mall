package com.taotao.datalink.zksession;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;

/**
 * zookeeper分布式锁
 * @author Administrator
 *
 */
public class ZooKeeperSession {
	
	@Value("${zkSessionConnectAddr}")
	private String zkSessionConnectAddr;
	
	@Value("${sessionTimeout}")
	private Integer sessionTimeout;
	
	/**
	 * 并发同步类
	 */
	private CountDownLatch countDownLatch = new CountDownLatch(1);
	
	private ZooKeeper zooKeeper;
	
	public ZooKeeperSession() {
		try {
			zooKeeper = new ZooKeeper("192.168.43.126:2181", 
					60 * 1000, new ZkWatcher());
			System.out.println("日志=========》zookeeper连接状态" + zooKeeper.getState());
			/**
			 * 等待连接
			 */
			countDownLatch.await();
			
			/**
			 * 连接成功
			 */
			System.out.println("日志=========》zookeeper连接状态" + zooKeeper.getState());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("异常=========》zookeeper连接异常");
		}
	}
	
	/**
	 * 获取分布式锁
	 */
	public void getZooKeeperSessionLock(String path) {
		try {
			zooKeeper.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		} catch (Exception e) {
			while(true) {
				try {
					System.out.println("日志========》第一次获取分布式锁失败，尝试重新获取分布式锁");
					zooKeeper.create(path, "".getBytes(), 
							Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
				} catch (Exception e2) {
					System.out.println("日志=======》重新获取分布式锁失败");
					continue;
				}
				System.out.println("日志=========》重新获取到分布式锁");
				break;
			}
		}
		System.out.println("日志=========》获取分布式锁成功");
	}
	
	/**
	 * 释放分布式锁
	 */
	public void realeaseZooKeeperSessionLock(String path) {
		try {
			System.out.println("日志=========》释放分布式锁");
			zooKeeper.delete(path, -1);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("异常=======》分布式锁释放异常");
		}
	}
	
	/**
	 * 监听zookeeper是否已经连接上的类
	 */
	private class ZkWatcher implements Watcher {

		@Override
		public void process(WatchedEvent event) {
			if(KeeperState.SyncConnected == event.getState()) {
				countDownLatch.countDown();
			}
		}
		
	}
	/**
	 * 内部静态类
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
	 * 获取单例实例
	 */
	public static ZooKeeperSession getInstance() {
		
		return Singleton.getInstance();
	}
}
