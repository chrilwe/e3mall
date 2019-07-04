package com.taotao.datalink.cache.rebuild.queue;

import java.util.concurrent.ArrayBlockingQueue;

import org.springframework.beans.factory.annotation.Value;

import com.taotao.datalink.model.ItemCategoryAggr;

/**
 * 缓存重建的内存队列
 * @author Administrator
 *
 */
public class RebuildItemCatAggrCacheQueue {
	
	private ArrayBlockingQueue<ItemCategoryAggr> arrayBlockingQueue;
	
	public RebuildItemCatAggrCacheQueue() {
		arrayBlockingQueue = new ArrayBlockingQueue<ItemCategoryAggr>(1000);
		
	}
	
	/**
	 * 将聚合类放到内存队列
	 * @param <T>
	 */
	public void put(ItemCategoryAggr itemCategoryAggr) {
		try {
			System.out.println("日志==========》将聚合类添加到内存队列中");
			arrayBlockingQueue.put(itemCategoryAggr);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("异常==========》将聚合类放到内存队列发生异常");
		}
	}
	
	/**
	 * 从内存队列中取出聚合类
	 * @param <T>
	 */
	public ItemCategoryAggr take() {
		ItemCategoryAggr itemCategoryAggr = null;
		try {
			System.out.println("日志==========》将聚合类从内存队列中取出来");
			itemCategoryAggr = arrayBlockingQueue.take();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("异常==========》取出聚合类发生异常");
		}
		return itemCategoryAggr;
	}
	
	/**
	 * 静态内部类
	 */
	private static class Singleton {
		private static RebuildItemCatAggrCacheQueue queue = null;
		
		static {
			queue = new RebuildItemCatAggrCacheQueue();
		}
		
		private static RebuildItemCatAggrCacheQueue getInstance() {
			
			return queue;
		}
	}
	
	/**
	 * 获取静态单例
	 */
	public static RebuildItemCatAggrCacheQueue getInstance() {
		
		return Singleton.getInstance();
	}
}
