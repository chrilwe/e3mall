package com.taotao.es.search.test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class TestRedis {
	public static void main(String[] args) {
		JedisPool pool = new JedisPool("192.168.43.104", 1111);
		Jedis jedis = pool.getResource();
		System.out.println(jedis.get("itemInfoAggr_536563"));
	}
}
