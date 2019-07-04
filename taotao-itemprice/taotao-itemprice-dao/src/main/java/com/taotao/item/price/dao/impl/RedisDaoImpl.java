package com.taotao.item.price.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.ContextLoader;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import com.taotao.item.price.dao.RedisDao;
import com.taotao.item.price.model.ItemPrice;
import com.taotao.item.price.utils.JsonUtils;
/**
 * redisCluster操作DAO层
 * @author Administrator
 *
 */
@Repository
public class RedisDaoImpl implements RedisDao {
	
	/*@Autowired
	private JedisCluster jedisCluster;*/

	@Override
	public void setItemPrice2Redis(ItemPrice itemPrice) {
		/*jedisCluster.set("itemPrice_" + itemPrice.getItemId(), JsonUtils.objectToJson(itemPrice));*/
		JedisPool jedisPool = ContextLoader.getCurrentWebApplicationContext().getBean(JedisPool.class);
		Jedis jedis = jedisPool.getResource();
		jedis.set("itemPrice_" + itemPrice.getItemId(), JsonUtils.objectToJson(itemPrice));
	}

	@Override
	public String getItemPriceFromRedis(long itemId) {
		JedisPool jedisPool = ContextLoader.getCurrentWebApplicationContext().getBean(JedisPool.class);
		Jedis jedis = jedisPool.getResource();
		return /*jedisCluster.get("itemPrice_" + itemId)*/jedis.get("itemPrice_" + itemId);
	}

	@Override
	public void delItemPriceByItemId(long itemId) {
		/*jedisCluster.del("itemPrice_" + itemId);*/
		JedisPool jedisPool = ContextLoader.getCurrentWebApplicationContext().getBean(JedisPool.class);
		Jedis jedis = jedisPool.getResource();
		jedis.del("itemPrice_" + itemId);
	}
	
}
