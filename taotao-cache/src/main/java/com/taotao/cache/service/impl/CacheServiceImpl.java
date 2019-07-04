package com.taotao.cache.service.impl;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.taotao.cache.hystrix.GetContentInfoFromRedisCommand;
import com.taotao.cache.hystrix.GetItemInfoFromRedisCommand;
import com.taotao.cache.hystrix.SetContentInfo2RedisCommand;
import com.taotao.cache.hystrix.SetItemInfo2RedisCommand;
import com.taotao.cache.model.Content;
import com.taotao.cache.model.Item;
import com.taotao.cache.service.CacheService;
import com.taotao.cache.utils.JsonUtils;

@Service
public class CacheServiceImpl implements CacheService {
	
	public static final String CACHE_NAME = "local";
	
	/**
	 * 从redis缓存中获取商品信息
	 */
	@Override
	public Item getItemInfoFromRedis(long itemId) {
		String key = "itemId_" + itemId;
		GetItemInfoFromRedisCommand command = new GetItemInfoFromRedisCommand(key);
		String itemInfoStr = command.execute();
		if(itemInfoStr == null) {
			return null;
		}
		return JsonUtils.jsonToPojo(itemInfoStr, Item.class);
	}
	
	/**
	 * 将商品信息更新到redis缓存中
	 */
	@Override
	public void setItemInfo2Redis(Item item) {
		SetItemInfo2RedisCommand command = new SetItemInfo2RedisCommand(item);
		Boolean result = command.execute();
		System.out.println("Redis缓存更新结果: " + result);
	}
	
	/**
	 * 从本地缓存当中获得商品信息
	 */
	@Override
	@Cacheable(value = CACHE_NAME, key = "'key_'+#itemId")
	public Item getItemInfoFromEhcache(long itemId) {
		
		return null;
	}
	
	/**
	 * 将商品信息更新到本地缓存中
	 */
	@Override
	@CachePut(value = CACHE_NAME, key = "'key_'+#item.Id()")
	public Item setItemInfo2Ehcache(Item item) {
		return item;
	}
	
	/**
	 * 从redis缓存中取出广告信息
	 */
	@Override
	public Content getContentInfoFromRedis(long contentId) {
		
		GetContentInfoFromRedisCommand command = new GetContentInfoFromRedisCommand(contentId);
		String contentInfo = command.execute();
		if(contentInfo == null) {
			return null;
		}
		return JsonUtils.jsonToPojo(contentInfo, Content.class);
	}
	
	/**
	 * 将广告信息更新到redis缓存中
	 */
	@Override
	public void setContentInfo2Redis(Content content) {
		SetContentInfo2RedisCommand command = new SetContentInfo2RedisCommand(content);
		Boolean result = command.execute();
	}
	
	/**
	 * 从本地缓存中获取广告信息
	 */
	@Override
	@Cacheable(value = CACHE_NAME, key = "'key_'+#contentId")
	public Content getContentInfoFromEhcache(long contentId) {
		return null;
	}
	
	/**
	 * 将广告信息 更新到本地缓存中
	 */
	@Override
	@CachePut(value = CACHE_NAME, key = "'key_'+#content.Id()")
	public Content setContentInfo2Ehcache(Content content) {
		return content;
	}

}
