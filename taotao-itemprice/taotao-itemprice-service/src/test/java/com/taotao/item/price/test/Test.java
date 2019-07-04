package com.taotao.item.price.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.taotao.item.price.model.ItemPrice;
import com.taotao.item.price.service.ItemPriceService;
import com.taotao.item.price.service.ItemPriceRequestService;
import com.taotao.item.price.service.thread.RequestThreadPool;

public class Test {
	public static void main(String[] args) {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		ItemPriceService itemPriceService = context.getBean(ItemPriceService.class);
	
		ItemPrice itemPrice = new ItemPrice();
		itemPrice.setItemId(1l);
		itemPrice.setPrice(100l);
		itemPriceService.setItemPrice2Redis(itemPrice);
		
		System.out.println(itemPriceService.findItemPriceFromRedis(1l));
		
	}
}
