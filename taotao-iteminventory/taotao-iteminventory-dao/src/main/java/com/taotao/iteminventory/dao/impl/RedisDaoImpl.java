package com.taotao.iteminventory.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.ContextLoader;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.taotao.iteminventory.dao.RedisDao;
@Repository
public class RedisDaoImpl implements RedisDao {

	/**
	 * 将商品库存更新到redis中
	 */
	@Override
	public void setItemInventory2Redis(long itemId, String itemInventory) {
		Jedis jedis = getJedis();
		jedis.set("itemInventory_" + itemId, itemInventory);
	}
	
	/**
	 * 删除redis中的商品库存
	 */
	@Override
	public void deleteItemInventoryFromRedisByItemId(long itemId) {
		Jedis jedis = getJedis();
		jedis.del("itemInventory_" + itemId);
	}
	
	/**
	 * 从redis中取出库存
	 */
	@Override
	public String getItemInventoryFromRedisByItemId(long itemId) {
		Jedis jedis = getJedis();
		return jedis.get("itemInventory_" + itemId);
	}
	
	private Jedis getJedis() {
		JedisPool jedisPool = ContextLoader.getCurrentWebApplicationContext().getBean(JedisPool.class);
		return jedisPool.getResource();
	}

}
