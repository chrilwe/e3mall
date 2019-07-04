package com.taotao.cache.service;

import com.taotao.cache.model.Content;
import com.taotao.cache.model.Item;

public interface CacheService {
	Item getItemInfoFromRedis(long itemId);//从redis缓存中获取商品信息
	
	void setItemInfo2Redis(Item item);//将商品信息存入redis缓存中
	
	Item getItemInfoFromEhcache(long itemId);//从本地缓存取出商品信息
	
	Item setItemInfo2Ehcache(Item item);//将商品信息存放到本地内存中
	
	Content getContentInfoFromRedis(long contentId);//从redis中获取广告内容
	
	void setContentInfo2Redis(Content content);//将广告内容更新到redis
	
	Content getContentInfoFromEhcache(long contentId);//从本地缓存获取广告信息
	
	Content setContentInfo2Ehcache(Content content);//将广告信息更新到本地内存当中
	
}
