package com.taotao.datalink.cache.rebuild.queue;

import java.util.concurrent.ArrayBlockingQueue;

import org.springframework.beans.factory.annotation.Value;

import com.taotao.datalink.model.ItemDescriptionAggr;
import com.taotao.itemservice.model.ItemBrand;

public class RebuildItemDescriptionAggrCacheQueue {
	private ArrayBlockingQueue<ItemDescriptionAggr> arrayBlockingQueue = null;
	
	private RebuildItemDescriptionAggrCacheQueue() {
		arrayBlockingQueue = new ArrayBlockingQueue<>(1000);
	}
	
	/**
	 * 添加到内存队列
	 */
	public void put(ItemDescriptionAggr itemDescriptionAggr) {
		try {
			arrayBlockingQueue.put(itemDescriptionAggr);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("异常========》重建缓存添加到内存队列发生异常");
		}
	}
	
	/**
	 * 从队列中取出
	 */
	public ItemDescriptionAggr take() {
		ItemDescriptionAggr itemDescriptionAggr = null;
		try {
			itemDescriptionAggr = arrayBlockingQueue.take();
		} catch (Exception e) {
			e.printStackTrace();System.out.println("异常========》重建缓存从内存队列取出数据发生异常");
		}
		return itemDescriptionAggr;
	}
	
	/**
	 * 静态内部类
	 */
	private static class Singleton {
		private static RebuildItemDescriptionAggrCacheQueue queue = null;
		
		static {
			queue = new RebuildItemDescriptionAggrCacheQueue();
		}
		
		private static RebuildItemDescriptionAggrCacheQueue getInstance() {
			
			return queue;
		}
	} 
	
	/**
	 * 获取单例实例
	 */
	public static RebuildItemDescriptionAggrCacheQueue getInstance() {
		
		return Singleton.getInstance();
	}
}
