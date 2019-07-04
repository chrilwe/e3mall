package com.taotao.datalink.cache.rebuild.queue;

import java.util.concurrent.ArrayBlockingQueue;




import org.springframework.beans.factory.annotation.Value;

import com.taotao.datalink.model.ItemInfoAggr;
import com.taotao.itemservice.model.ItemBrand;

public class RebuildItemInfoAggrCacheQueue {
	private ArrayBlockingQueue<ItemInfoAggr> arrayBlockingQueue = null;
	
	private RebuildItemInfoAggrCacheQueue() {
		arrayBlockingQueue = new ArrayBlockingQueue<>(1000);
	}
	
	/**
	 * 添加到内存队列
	 */
	public void put(ItemInfoAggr itemInfoAggr) {
		try {
			arrayBlockingQueue.put(itemInfoAggr);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("异常========》重建缓存添加到内存队列发生异常");
		}
	}
	
	/**
	 * 从队列中取出
	 */
	public ItemInfoAggr take() {
		ItemInfoAggr itemInfoAggr = null;
		try {
			itemInfoAggr = arrayBlockingQueue.take();
		} catch (Exception e) {
			e.printStackTrace();System.out.println("异常========》重建缓存从内存队列取出数据发生异常");
		}
		return itemInfoAggr;
	}
	
	/**
	 * 静态内部类
	 */
	private static class Singleton {
		private static RebuildItemInfoAggrCacheQueue queue = null;
		
		static {
			queue = new RebuildItemInfoAggrCacheQueue();
		}
		
		private static RebuildItemInfoAggrCacheQueue getInstance() {
			
			return queue;
		}
	} 
	
	/**
	 * 获取单例实例
	 */
	public static RebuildItemInfoAggrCacheQueue getInstance() {
		
		return Singleton.getInstance();
	}
}