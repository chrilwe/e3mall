package com.taotao.cache.test;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;


public class TestRedisCluster {
	public static void main(String[] args) {
		ApplicationContext context = new  ClassPathXmlApplicationContext("classpath:spring/applicationContext-rediscluster.xml");
		JedisCluster jedisCluster = context.getBean(JedisCluster.class);
		System.out.println(jedisCluster.get("itemId_536563"));
	}
}
