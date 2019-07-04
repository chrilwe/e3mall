package com.taotao.item.price.service.request.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoader;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.taotao.item.price.model.ItemPrice;
import com.taotao.item.price.service.ItemPriceService;
import com.taotao.item.price.service.request.Request;
/**
 * 商品读请求的封装
 * @author Administrator
 *
 */
public class ItemPriceReadRequestImpl implements Request {
	
	private long itemId;
	
	public ItemPriceReadRequestImpl(long itemId) {
		this.itemId = itemId;
	}

	@Override
	public void process() {
		/**
		 * 先从redis中读取数据，看是否前面有价格更新
		 * 如果前面操作是更新操作，从源头服务拉取商品价格数据并更新到redis缓存中
		 * 如果前面操作时读取操作，不做处理
		 */
		System.out.println("日志===============》开始后台线程执行的逻辑操作,itemPrice_"+itemId);
		ItemPriceService itemPriceService = ContextLoader.getCurrentWebApplicationContext().getBean(ItemPriceService.class);
		String itemPriceStr = itemPriceService.findItemPriceFromRedis(itemId);
		System.out.println("日志===============》读取商品价格"+itemId+"数据:" + itemPriceStr);
		if(itemPriceStr == null) {
			//调用商品价格服务，查询
			ItemPrice itemPrice = itemPriceService.findItemPriceByItemId(itemId);
			System.out.println("日志================》后台线程查询商品价格数据:" + itemPrice);
			if(itemPrice != null) {
				itemPriceService.setItemPrice2Redis(itemPrice);
			}
		}
		
	}

	@Override
	public long getItemId() {
		
		return itemId;
	}

}
