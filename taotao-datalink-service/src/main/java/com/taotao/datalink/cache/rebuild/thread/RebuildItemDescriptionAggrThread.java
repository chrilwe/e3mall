package com.taotao.datalink.cache.rebuild.thread;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoader;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.taotao.datalink.cache.rebuild.queue.RebuildItemBrandAggrCacheQueue;
import com.taotao.datalink.cache.rebuild.queue.RebuildItemDescriptionAggrCacheQueue;
import com.taotao.datalink.model.ItemBrandAggr;
import com.taotao.datalink.model.ItemDescriptionAggr;
import com.taotao.datalink.service.DatalinkService;
import com.taotao.datalink.utils.JsonUtils;
import com.taotao.datalink.zksession.ZooKeeperSession;

public class RebuildItemDescriptionAggrThread implements Runnable {

	@Override
	public void run() {
		while(true) {
			RebuildItemDescriptionAggrCacheQueue queue = RebuildItemDescriptionAggrCacheQueue.getInstance();		
			ItemDescriptionAggr itemDescriptionAggr = queue.take();
			
			DatalinkService datalinkService = ContextLoader.getCurrentWebApplicationContext().getBean(DatalinkService.class);
			datalinkService.setItemDescriptionAggr2Ehecache(itemDescriptionAggr);
			System.out.println("日志=======》将商品描述聚合数据更新到本地内存当中");
			
			//获得锁,因为部署了多个实例，对于相同的redis资源进行操作，需要防止并发问题
			ZooKeeperSession zkSession = ZooKeeperSession.getInstance();
			String path = "/itemDescriptionAggr_" + itemDescriptionAggr.getItemId();
			zkSession.getZooKeeperSessionLock(path);
			System.out.println("日志=======》更新商品描述聚合数据获得锁");
			
			//更新redis资源信息
			JedisPool jedisPool = ContextLoader.getCurrentWebApplicationContext().getBean(JedisPool.class);
			Jedis jedis = jedisPool.getResource();
			String value = jedis.get("itemDescriptionAggr" + itemDescriptionAggr.getItemId());
			if(value == null) {
				jedis.set("itemDescriptionAggr_" + itemDescriptionAggr.getItemId(), 
						JsonUtils.objectToJson(itemDescriptionAggr));
				System.out.println("日志========》重建缓存，redis缓存没有商品描述聚合数据，直接更新");
			} else {
				if(JsonUtils.jsonToPojo(value, ItemDescriptionAggr.class).getUpdated().before(itemDescriptionAggr.getUpdated())) {
					jedis.set("itemDescriptionAggr_" + itemDescriptionAggr.getItemId(), 
							JsonUtils.objectToJson(itemDescriptionAggr));
					System.out.println("日志========》将最新版本的商品描述聚合数据更新到redis");
				} else {
					System.out.println("日志========》当前版本的数据过旧，忽略更新到redis中的操作");
				}
			}
			
			//释放锁
			zkSession.realeaseZooKeeperSessionLock(path);
			System.out.println("日志=======》释放分布式锁");
		}
	}

}
