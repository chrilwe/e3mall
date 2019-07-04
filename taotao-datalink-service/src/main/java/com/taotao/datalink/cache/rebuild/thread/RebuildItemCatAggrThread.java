package com.taotao.datalink.cache.rebuild.thread;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoader;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.taotao.datalink.cache.rebuild.queue.RebuildItemCatAggrCacheQueue;
import com.taotao.datalink.model.ItemCategoryAggr;
import com.taotao.datalink.service.DatalinkService;
import com.taotao.datalink.utils.JsonUtils;
import com.taotao.datalink.zksession.ZooKeeperSession;

/**
 * 重建缓存后台线程
 * @author Administrator
 *
 */
public class RebuildItemCatAggrThread implements Runnable {

	@Override
	public void run() {
		while(true) {
			ItemCategoryAggr itemCategoryAggr = RebuildItemCatAggrCacheQueue.getInstance().take();
			
			/**
			 * 
			 * 更新本地缓存
			 */
			DatalinkService datalinkService = ContextLoader.getCurrentWebApplicationContext().getBean(DatalinkService.class);
			datalinkService.setItemCategoryAggr2Ehecache(itemCategoryAggr);
			System.out.println("日志=======》将商品分类聚合数据更新到本地内存当中");
			
			/**
			 * 获得分布式锁
			 */
			ZooKeeperSession zkSession = ZooKeeperSession.getInstance();
			String path = "/itemCategoryAggr_" + itemCategoryAggr.getItemId();
			zkSession.getZooKeeperSessionLock(path);
			System.out.println("日志=======》更新商品分类聚合数据获得锁");
			
			/**
			 * 更新redis缓存
			 */
			//从redis中查找
			JedisPool jedisPool = ContextLoader.getCurrentWebApplicationContext().getBean(JedisPool.class);
			Jedis jedis = jedisPool.getResource();
			String itemCategoryAggrStr = jedis.get("itemCategoryAggr_" + itemCategoryAggr.getItemId());
			
			if(itemCategoryAggrStr == null) {
				//没有找到，说明redis缓存中没有该数据，直接更新到redis中
				System.out.println("日志=========》redis缓存没有数据，直接更新数据到redis缓存");
				jedis.set("itemCategoryAggr_" + itemCategoryAggr.getItemId(), JsonUtils.objectToJson(itemCategoryAggr));
			} else {
				//如果redis缓存中已经有数据了，需要判断当前更新的数据是否为最新的数据
				ItemCategoryAggr categoryAggr = JsonUtils.jsonToPojo(itemCategoryAggrStr, ItemCategoryAggr.class);
				
				if(categoryAggr.getUpdated().before(itemCategoryAggr.getUpdated())) {
					System.out.println("日志=======》redis缓存中原有的数据是旧版本的数据");
					jedis.set("itemCategoryAggr_" + itemCategoryAggr.getItemId(), JsonUtils.objectToJson(itemCategoryAggr));
				} else {
					System.out.println("日志=======》redis缓存中的数据是最新版本数据，忽略此更新操作");
				}
			}
			
			/**
			 * 释放分布式锁
			 */
			zkSession.realeaseZooKeeperSessionLock(path);
			System.out.println("日志=======》释放分布式锁");
		}
	}

}
