package com.taotao.cache.queue;

import java.util.concurrent.ArrayBlockingQueue;

import com.taotao.cache.model.Content;

/**
 *  广告缓存重建单例队列
 * @author Administrator
 *
 */
public class ContentInfoCacheQueue {
	
	private static ArrayBlockingQueue<Content> arrayBlockingQueue = new ArrayBlockingQueue<Content>(1000);
	
	private ContentInfoCacheQueue() {
		
	}
	
	/**
	 * 将广告信息放到内存队列
	 */
	public void put(Content content) {
		try {
			arrayBlockingQueue.put(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *  从内存队列中取出广告信息
	 */
	public Content take() {
		Content content = null;
		try {
			content = arrayBlockingQueue.take();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}
	
	/**
	 * 静态内部类
	 */
	private static class Singleton {
		private static ContentInfoCacheQueue queue = null;
		
		static {
			queue = new ContentInfoCacheQueue();
		}
		
		private static ContentInfoCacheQueue getInstance() {
			
			return queue;
		}
	}
	
	/**
	 * 获取单例对象
	 */
	public static ContentInfoCacheQueue getInstance() {
		
		return Singleton.getInstance();
	}
}
