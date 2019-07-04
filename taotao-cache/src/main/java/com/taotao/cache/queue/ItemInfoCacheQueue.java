package com.taotao.cache.queue;

import java.util.concurrent.ArrayBlockingQueue;

import com.taotao.cache.model.Item;

/**
 * 商品信息缓存重建单例内存队列
 * @author Administrator
 *
 */
public class ItemInfoCacheQueue {
	
	private static ArrayBlockingQueue<Item> arrayBlockingQueue = new ArrayBlockingQueue<>(1000);
	
	private ItemInfoCacheQueue() {
		
		
	}
	
	/**
	 * 将item信息放入内存队列中
	 */
	public void put(Item item) {
		try {
			arrayBlockingQueue.put(item);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 从内存队列中取出item
	 */
	public Item take() {
		try {
			Item item = arrayBlockingQueue.take();
			return item;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 静态内部类
	 */
	private static class Singleton {
		private static ItemInfoCacheQueue queue = null;
		
		static {
			queue = new ItemInfoCacheQueue();
		}
		
		private static ItemInfoCacheQueue getInstance() {
			
			return queue;
		}
	}
	
	/**
	 * 获取单例
	 */
	public static ItemInfoCacheQueue getInstance() {
		
		return Singleton.getInstance();
	}
	
	/**
	 * 初始化queue
	 */
	public static void init() {
		getInstance();
	}
}
