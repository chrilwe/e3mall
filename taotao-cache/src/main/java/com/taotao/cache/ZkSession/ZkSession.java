package com.taotao.cache.ZkSession;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * zookeeper分布式锁，保证并发情况下只创建一个node
 * @author Administrator
 *
 */
public class ZkSession {
	
	private ZooKeeper zooKeeper;
	//多线程并发同步工具
	private static CountDownLatch countDownLatch = new CountDownLatch(1);
	
	private ZkSession() {
		try {
			zooKeeper = new ZooKeeper("192.168.43.126:2181,192.168.43.51:2181,192.168.43.51:2181",
					5000, new ZkSessionWatcher());
			System.out.println("连接状态:" + zooKeeper.getState());
			
			/**
			 * 并发同步工具，调用await,线程阻塞，直到其他线程调用countDown
			 */
			countDownLatch.await();
			
			System.out.println("连接成功" + zooKeeper.getState());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * zookeeper分布式锁：获得锁
	 */
	public void acquireZkLock(long itemId) {
		String path = "/item-lock" + itemId;
		try {
			zooKeeper.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		} catch (Exception e) {
			/**
			 * 没有获取到锁，尝试重新获取到锁，直到获得锁
			 */
			while(true) {
				try {
					zooKeeper.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
				} catch (Exception e2) {
					continue;
				}
				break;
			}
		}
	}
	
	public boolean acquireZkSessionLock(String path) {
		try {
			zooKeeper.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		} catch (Exception e) {
			//没有获取锁，直接返回失败
			return false;
		}
		return true;
	}
	
	public void acruireZkSessionLock(String path) {
		try {
			zooKeeper.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		} catch (Exception e) {
			/**
			 * 没有获取到锁，尝试重新获取到锁，直到获得锁
			 */
			while(true) {
				try {
					zooKeeper.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
				} catch (Exception e2) {
					continue;
				}
				break;
			}
		}
	}
	
	/**
	 * zookeeper分布式锁：释放锁
	 */
	public void realeaseZkLock(long itemId) {
		String path = "/item-lock" + itemId;
		try {
			zooKeeper.delete(path, -1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void realeaseLock(String path) {
		try {
			zooKeeper.delete(path, -1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建节点
	 */
	public void createNode(String path) {
		try {
			zooKeeper.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将数据放入节点中
	 * 
	 */
	public void setDataNode(String path, String data) {
		try {
			zooKeeper.setData(path, data.getBytes(), -1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将数据从节点中取出来
	 */
	public String getDataNode(String path) {
		try {
			return new String(zooKeeper.getData(path, false, new Stat()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * zookeeper连接状态监控
	 */
	private class ZkSessionWatcher implements Watcher {

		@Override
		public void process(WatchedEvent event) {
			if(KeeperState.SyncConnected == event.getState()) {
				countDownLatch.countDown();
			}
		}
		
	}
	
	/**
	 * 静态内部类
	 * @author Administrator
	 *
	 */
	private static class Singleton {
		
		private static ZkSession zkSession = null;
		
		static {
			zkSession = new ZkSession();
		}
		
		private static ZkSession getInstance() {
			
			return zkSession;
		}
	}
	
	/**
	 * 获取单例对象 
	 */
	public static ZkSession getInstance() {
		
		return Singleton.getInstance();
	}
	
}
