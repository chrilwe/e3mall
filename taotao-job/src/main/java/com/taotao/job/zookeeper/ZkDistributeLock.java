package com.taotao.job.zookeeper;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

/**
 * zookeeper分布式锁
 * @author Administrator
 *
 */
public class ZkDistributeLock {
	
	private ZooKeeper zooKeeper;
	
	/**
	 * 多线程同步类
	 */
	private CountDownLatch count = new CountDownLatch(1);
	
	private ZkDistributeLock() {
		try {
			//初始化的时候创建一个zk连接
			zooKeeper = new ZooKeeper("192.168.43.126:2181", 60 * 1000, new ZkWatcher());
			//等待连接
			count.await();
			
			System.out.println("connect to zookeeper succccessful");//连接成功
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 监听zookeeper的连接状态 
	 */
	private class ZkWatcher implements Watcher {

		@Override
		public void process(WatchedEvent event) {
			if(KeeperState.SyncConnected == event.getState()) {
				//连接成功，并发线程count减一
				count.countDown();
			}
		}
		
	}
	
	/**
	 * 内部静态类
	 */
	private static class Singleton {
		private static ZkDistributeLock lock = null;
		static {
			lock = new ZkDistributeLock();
		}
		
		private static ZkDistributeLock getInstance() {
			
			return lock;
		}
	}
	
	/**
	 * 获取单例实例
	 */
	public static ZkDistributeLock getInstance() {
		
		return Singleton.getInstance();
	}
	
	/**
	 * 获得锁
	 */
	public Boolean requireLock(String path) {
		try {
			zooKeeper.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		} catch (Exception e) {
			//创建临时节点失败
			return false;
		}
		return true;
	}
	
	/**
	 * 释放锁
	 */
	public void releaseLock(String path) {
		try {
			zooKeeper.delete(path, -1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
