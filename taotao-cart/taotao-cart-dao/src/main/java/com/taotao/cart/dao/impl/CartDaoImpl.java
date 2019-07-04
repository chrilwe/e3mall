package com.taotao.cart.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.taotao.cart.dao.CartDao;
import com.taotao.cart.dao.utils.JsonUtils;
import com.taotao.cart.model.Cart;
@Repository
public class CartDaoImpl implements CartDao {
	
	@Autowired
	private JedisPool jedisPool;
	
	/**
	 * 将宝贝添加到redis中
	 */
	@Override
	public void addCart2Redis(Cart cart, long userId) {
		Jedis jedis = jedisPool.getResource();
		//查询redis中是否存在
		Cart c = findCartFromRedisByUserIdAndItemId(userId, cart.getId());
		
		if(c != null && !c.equals("")) {
			cart.setNum(c.getNum() + cart.getNum());
		}
		
		jedis.hset(String.valueOf(userId), String.valueOf(cart.getId()), JsonUtils.objectToJson(cart));
		System.out.println("日志=============》将用户" + userId + "的商品" + cart + "添加到redis中");
	}
	
	/**
	 * 根据userid和itemid删除宝贝
	 */
	@Override
	public void deleteCartFromRedis(long userId, long itemId) {
		Jedis jedis = jedisPool.getResource();
		jedis.hdel(String.valueOf(userId), String.valueOf(itemId));
	}
	
	
	@Override
	public void updateCartFromRedis(List<Cart> carts) {
		
	}
	
	/**
	 * 根据userid查找购物车所有清单
	 */
	@Override
	public List<Cart> findCartFromRedis(long userId) {
		Jedis jedis = jedisPool.getResource();
		List<Cart> result = new ArrayList<Cart>();
		Map<String, String> cartStrs = jedis.hgetAll(String.valueOf(userId));
		System.out.println("日志==============》从redis获取用户" + userId + "购物车清单:" + cartStrs);
		
		if(cartStrs != null && cartStrs.size() > 0) {
			for(Map.Entry entry : cartStrs.entrySet()) {
				String cartStr = (String) entry.getValue();
				result.add(JsonUtils.jsonToPojo(cartStr, Cart.class));
			}
		}
		
		return result;
	}
	
	/**
	 * 根据userid和itemid查询宝贝信息
	 */
	@Override
	public Cart findCartFromRedisByUserIdAndItemId(long userId, long itemId) {
		Jedis jedis = jedisPool.getResource();
		String cartStr = jedis.hget(String.valueOf(userId), String.valueOf(itemId));
		if(cartStr == null) {
			return null;
		}
		return JsonUtils.jsonToPojo(cartStr, Cart.class);
	}
	
	/**
	 * 清空购物车的宝贝
	 */
	@Override
	public void deleteAllCartByuserId(long userId) {
		Jedis jedis = jedisPool.getResource();
		List<Cart> carts = findCartFromRedis(userId);
		if(carts != null) {
			for (Cart cart : carts) {
				deleteCartFromRedis(userId, cart.getId());
			}
		}
	}

}
