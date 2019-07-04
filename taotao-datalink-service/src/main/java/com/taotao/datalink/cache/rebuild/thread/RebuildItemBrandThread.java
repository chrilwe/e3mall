package com.taotao.datalink.cache.rebuild.thread;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoader;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.taotao.datalink.cache.rebuild.queue.RebuildItemBrandAggrCacheQueue;
import com.taotao.datalink.model.ItemBrandAggr;
import com.taotao.datalink.service.DatalinkService;
import com.taotao.datalink.utils.JsonUtils;
import com.taotao.datalink.zksession.ZooKeeperSession;
import com.taotao.itemservice.model.ItemBrand;

public class RebuildItemBrandThread implements Runnable {
	
	@Autowired
	private DatalinkService datalinkService;
	
	@Autowired
	private JedisPool jedisPool;

	@Override
	public void run() {
		while(true) {
			RebuildItemBrandAggrCacheQueue queue = RebuildItemBrandAggrCacheQueue.getInstance();		
			ItemBrandAggr itemBrandAggr = queue.take();
			
			DatalinkService datalinkService = ContextLoader.getCurrentWebApplicationContext().getBean(DatalinkService.class);
			datalinkService.setItemBrandAggr2Ehecache(itemBrandAggr);
			System.out.println("日志=======》将商品标题聚合数据更新到本地内存当中");
			
			//获得锁,因为部署了多个实例，对于相同的redis资源进行操作，需要防止并发问题
			ZooKeeperSession zkSession = ZooKeeperSession.getInstance();
			String path = "/itemBrandAggr_" + itemBrandAggr.getItemId();
			zkSession.getZooKeeperSessionLock(path);
			System.out.println("日志=======》更新商品标题聚合数据获得锁");
			
			//更新redis资源信息
			JedisPool jedisPool = ContextLoader.getCurrentWebApplicationContext().getBean(JedisPool.class);
			Jedis jedis = jedisPool.getResource();
			String value = jedis.get("itemBrandAggr_" + itemBrandAggr.getItemId());
			if(value == null) {
				jedis.set("itemBrandAggr_" + itemBrandAggr.getItemId(), 
						JsonUtils.objectToJson(itemBrandAggr));
				System.out.println("日志========》重建缓存，redis缓存没有商品标题聚合数据，直接更新");
			} else {
				if(JsonUtils.jsonToPojo(value, ItemBrandAggr.class).getEditTime().before(itemBrandAggr.getEditTime())) {
					jedis.set("itemBrandAggr_" + itemBrandAggr.getItemId(), 
							JsonUtils.objectToJson(itemBrandAggr));
					System.out.println("日志========》将最新版本的商品标题聚合数据更新到redis");
				} else {
					System.out.println("日志========》当前版本的数据过旧，忽略更新到redis中的操作");
				}
			}
			
			//释放锁
			zkSession.realeaseZooKeeperSessionLock(path);
		}
	}

}
