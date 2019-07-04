package com.taotao.cache.thread;

import org.springframework.beans.factory.annotation.Autowired;

import com.taotao.cache.ZkSession.ZkSession;
import com.taotao.cache.model.Content;
import com.taotao.cache.queue.ContentInfoCacheQueue;
import com.taotao.cache.service.CacheService;
import com.taotao.cache.utils.JsonUtils;

/**
 * 广告信息缓存重建
 * @author Administrator
 *
 */
public class RebuildContentInfoCacheThread implements Runnable {
	
	@Autowired
	private CacheService cacheService;

	@Override
	public void run() {
		while(true) {
			
			ContentInfoCacheQueue queue = ContentInfoCacheQueue.getInstance();
			Content content = queue.take();
			System.out.println("========日志======广告信息缓存重建");
			
			if(content != null) {
				//更新本地缓存中的缓存数据
				cacheService.setContentInfo2Ehcache(content);
				
				//更新redis缓存中 的数据
				//获得分布式锁，解决多个服务实例并发更新下，导致数据更新不是最新版本的问题
				ZkSession zkSession = ZkSession.getInstance();
				String path = "/contentId-lock-" + content.getId();
				zkSession.acquireZkSessionLock(path);
				
				//从redis中尝试取出旧版本的数据
				Content contentFromRedis = cacheService.getContentInfoFromRedis(content.getId());
				if(contentFromRedis != null) {
					if(contentFromRedis.getUpdated().before(content.getUpdated())) {
						cacheService.setContentInfo2Redis(content);
					}
				} else {
					cacheService.setContentInfo2Redis(content);
				}
				
				//释放分布式锁
				zkSession.realeaseLock(path);
			}
		}
	}

}
