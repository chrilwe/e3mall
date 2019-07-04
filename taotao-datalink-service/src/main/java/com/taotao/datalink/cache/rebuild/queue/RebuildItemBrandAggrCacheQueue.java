package com.taotao.datalink.cache.rebuild.queue;

import java.util.concurrent.ArrayBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.taotao.datalink.model.ItemBrandAggr;


/**
 * 重建商品标题内存队列
 * @author Administrator
 *
 */
public class RebuildItemBrandAggrCacheQueue {
	
	private ArrayBlockingQueue<ItemBrandAggr> arrayBlockingQueue = null;
	
	private RebuildItemBrandAggrCacheQueue() {
		arrayBlockingQueue = new ArrayBlockingQueue<>(1000);
	}
	
	/**
	 * 添加到内存队列
	 */
	public void put(ItemBrandAggr itemBrandAggr) {
		try {
			arrayBlockingQueue.put(itemBrandAggr);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("异常========》重建缓存添加到内存队列发生异常");
		}
	}
	
	/**
	 * 从队列中取出
	 */
	public ItemBrandAggr take() {
		ItemBrandAggr itemBrandAggr = null;
		try {
			itemBrandAggr = arrayBlockingQueue.take();
		} catch (Exception e) {
			e.printStackTrace();System.out.println("异常========》重建缓存从内存队列取出数据发生异常");
		}
		return itemBrandAggr;
	}
	
	/**
	 * 静态内部类
	 */
	private static class Singleton {
		private static RebuildItemBrandAggrCacheQueue queue = null;
		
		static {
			queue = new RebuildItemBrandAggrCacheQueue();
		}
		
		private static RebuildItemBrandAggrCacheQueue getInstance() {
			
			return queue;
		}
	} 
	
	/**
	 * 获取单例实例
	 */
	public static RebuildItemBrandAggrCacheQueue getInstance() {
		
		return Singleton.getInstance();
	}
}
