package com.taotao.datalink.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.taotao.datalink.model.ItemBrandAggr;
import com.taotao.datalink.model.ItemCategoryAggr;
import com.taotao.datalink.model.ItemDescriptionAggr;
import com.taotao.datalink.model.ItemInfoAggr;
import com.taotao.datalink.service.DatalinkService;
import com.taotao.datalink.utils.JsonUtils;
import com.taotao.itemservice.model.Item;
import com.taotao.itemservice.model.ItemCategory;

/**
 * 数据直连service
 * @author Administrator
 *
 */
@Service
public class DatalinkServiceImpl implements DatalinkService {
	
	private static final String CACHE_NAME = "local";
	
	@Autowired
	private JedisPool jedisPool;
	
	/**
	 * 将商品基本维度信息更新到ehecache缓存中
	 */
	@Override
	@CachePut(value = CACHE_NAME, key = "'itemInfoAggr_key_'+#itemInfoAggr.getItemId()")
	public ItemInfoAggr setItemInfoAggr2Ehecache(ItemInfoAggr itemInfoAggr) {
		
		return itemInfoAggr;
	}
	
	/**
	 * 从ehecache缓存中取出商品维度的聚合数据
	 */
	@Override
	@Cacheable(value = CACHE_NAME, key = "'itemInfoAggr_key_'+#itemId")
	public ItemInfoAggr getItemInfoAggrFromEhcache(long itemId) {
		
		return null;
	}
	
	/**
	 * 将商品基本维度信息更新到redis缓存主集群中
	 */
	@Override
	public void setItemInfoAggr2Redis(ItemInfoAggr itemInfoAggr) {
		Jedis jedis = jedisPool.getResource();
		jedis.set("itemInfoAggr_" + itemInfoAggr.getItemId(), JsonUtils.objectToJson(itemInfoAggr));
	}
	
	/**
	 * 从缓存主集群中获取商品维度的聚合数据
	 */
	@Override
	public ItemInfoAggr getItemInfoAggrFromRedis(long itemId) {
		Jedis jedis = jedisPool.getResource();
		String value = jedis.get("itemInfoAggr_" + itemId);
		if(value == null || value.equals("")) {
			return null;
		}
		
		return JsonUtils.jsonToPojo(value, ItemInfoAggr.class);
	}
	
	/**
	 * 将商品品牌维度的数据更新到ehecache缓存中
	 */
	@Override
	@CachePut(value = CACHE_NAME, key = "'itemBrandAggr_key_'+#itemBrandAggr.getItemId()")
	public ItemBrandAggr setItemBrandAggr2Ehecache(ItemBrandAggr itemBrandAggr) {
		
		return itemBrandAggr;
	}
	
	/**
	 * 从ehecache缓存中取出商品品牌的聚合数据
	 */
	@Override
	@Cacheable(value = CACHE_NAME, key = "'itemBrandAggr_key_'+#itemId")
	public ItemBrandAggr getItemBrandAggrFromEhcache(long itemId) {
		
		return null;
	}
	
	/**
	 * 将商品品牌维度的聚合数据更新到redis缓存主集群中
	 */
	@Override
	public void setItemBrandAggr2Redis(ItemBrandAggr itemBrandAggr) {
		Jedis jedis = jedisPool.getResource();
		jedis.set("itemBrandAggr_" + itemBrandAggr.getItemId(),
				JsonUtils.objectToJson(ItemBrandAggr.class));
	}
	
	/**
	 * 从redis缓存主集群中获取商品品牌维度的聚合数据
	 */
	@Override
	public ItemBrandAggr getItemBrandAggrFromRedis(long itemId) {
		Jedis jedis = jedisPool.getResource();
		String value = jedis.get("itemBrandAggr_" + itemId);
		if(value == null || value.equals("")) {
			return null;
		}
		return JsonUtils.jsonToPojo(value, ItemBrandAggr.class);
	}
	
	/**
	 * 将商品分类维度的聚合数据更新到ehecache缓存中
	 */
	@Override
	@CachePut(value = CACHE_NAME, key = "'itemCategoryAggr_key_'+#itemBrandAggr.getItemId()")
	public ItemCategoryAggr setItemCategoryAggr2Ehecache(
			ItemCategoryAggr itemCategoryAggr) {
		
		return itemCategoryAggr;
	}
	
	/**
	 * 从ehecahe中获取商品分类维度的聚合数据
	 */
	@Override
	@Cacheable(value = CACHE_NAME, key = "'itemCategoryAggr_key_'+#itemId")
	public ItemCategoryAggr getItemCategoryAggrFromEhcache(long itemId) {
		
		return null;
	}
	
	/**
	 * 更新redis缓存主集群的商品分类维度的聚合数据
	 */
	@Override
	public void setItemCategoryAggr2Redis(ItemCategoryAggr itemCategoryAggr) {
		Jedis jedis = jedisPool.getResource();
		jedis.set("itemCategoryAggr_" + itemCategoryAggr.getItemId(), 
				JsonUtils.objectToJson(itemCategoryAggr));
	}
	
	/**
	 * 从redis缓存主机群中获取商品分类维度的聚合数据
	 */
	@Override
	public ItemCategoryAggr getItemCategoryAggrFromRedis(long itemId) {
		Jedis jedis = jedisPool.getResource();
		String value = jedis.get("itemCategoryAggr_" + itemId);
		if(value == null || value.equals("")) {
			return null;
		}
		
		return JsonUtils.jsonToPojo(value, ItemCategoryAggr.class);
	}
	
	/**
	 * 将商品介绍维度的聚合数据更新到ehecache缓存中
	 */
	@Override
	@CachePut(value = CACHE_NAME, key = "'itemDescriptionAggr_key_'+#itemBrandAggr.getItemId()")
	public ItemDescriptionAggr setItemDescriptionAggr2Ehecache(
			ItemDescriptionAggr itemDescriptionAggr) {
		
		return itemDescriptionAggr;
	}
	
	/**
	 * 从ehecache缓存中获取商品介绍维度的聚合数据
	 */
	@Override
	@Cacheable(value = CACHE_NAME, key = "'itemDescriptionAggr_key_'+#itemId")
	public ItemDescriptionAggr getItemDescriptionAggrFromEhcache(long itemId) {
		
		return null;
	}
	
	/**
	 * 将商品介绍维度的聚合数据更新到redis缓存主机群中
	 */
	@Override
	public void setItemDescriptionAggr2Redis(
			ItemDescriptionAggr itemDescriptionAggr) {
		Jedis jedis = jedisPool.getResource();
		jedis.set("itemDescriptionAggr_" + itemDescriptionAggr.getItemId(), 
				JsonUtils.objectToJson(itemDescriptionAggr));

	}
	
	/**
	 * 从redis缓存主机群中获取商品介绍维度的聚合数据
	 */
	@Override
	public ItemDescriptionAggr getItemDescriptionAggrFromRedis(long itemId) {
		Jedis jedis = jedisPool.getResource();
		String value = jedis.get("itemDescriptionAggr_" + itemId);
		if(value == null || value.equals("")) {
			return null;
		}
		
		return JsonUtils.jsonToPojo(value, ItemDescriptionAggr.class);
	}
}
