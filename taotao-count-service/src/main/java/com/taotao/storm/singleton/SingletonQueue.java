package com.taotao.storm.singleton;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

public class SingletonQueue {
	
	private ArrayBlockingQueue<Long> queue = null;
	
	private SingletonQueue() {
		queue = new ArrayBlockingQueue<Long>(1000);
	}
	
	/**
	 * 数据入队
	 * @throws InterruptedException 
	 */
	public void put(long itemId) throws InterruptedException {
		queue.put(itemId);
	}
	
	/**
	 * 数据出队
	 * @throws InterruptedException 
	 */
	public Long take() throws InterruptedException {
		return queue.take();
	}
	
	/**
	 * 静态内部类
	 */
	private static class Singleton {
		private static SingletonQueue singletonQueue = null;
		
		static {
			singletonQueue = new SingletonQueue();
		}
		
		private static SingletonQueue getInstance() {
			
			return singletonQueue;
		}
	}
	
	/**
	 * 获取单例
	 */
	public static SingletonQueue getInstance() {
		
		return Singleton.getInstance();
	}
}
