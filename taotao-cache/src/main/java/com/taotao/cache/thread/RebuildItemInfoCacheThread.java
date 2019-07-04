package com.taotao.cache.thread;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.taotao.cache.ZkSession.ZkSession;
import com.taotao.cache.model.Item;
import com.taotao.cache.queue.ItemInfoCacheQueue;
import com.taotao.cache.service.CacheService;

/**
 * 重建缓存的异步重建工作线程
 * @author Administrator
 *
 */
public class RebuildItemInfoCacheThread implements Runnable {
	
	@Autowired
	private CacheService cacheService;

	@Override
	public void run() {
		ItemInfoCacheQueue queue = ItemInfoCacheQueue.getInstance();
		while(true) {
			try {
				Item item = queue.take();
				//将商品信息更新到本地内存中
				System.out.println("========日志======商品信息缓存重建");
				cacheService.setItemInfo2Ehcache(item);
				
				//将上商品信息更新到redis中,用zookeeper分布式锁解决缓存重建并发问题
				ZkSession zkSession = ZkSession.getInstance();
				zkSession.acquireZkLock(item.getId());
				
				//从Redis缓存尝试获取到该商品信息的旧版本数据
				//如果当前数据是最新的，就更新Redis中的缓存，否则不做任何处理
				Item itemInfo = cacheService.getItemInfoFromRedis(item.getId());
				if(itemInfo != null) {
					Date updatedTime = itemInfo.getUpdated();
					if(updatedTime.before(item.getUpdated())) {
						cacheService.setItemInfo2Redis(item);
					}
				}
				
				zkSession.realeaseZkLock(item.getId());
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
