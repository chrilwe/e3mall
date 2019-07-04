package com.taotao.item.price.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.taotao.item.price.dao.RedisDao;
import com.taotao.item.price.mapper.ItemPriceMapper;
import com.taotao.item.price.model.ItemPrice;
import com.taotao.item.price.service.ItemPriceService;
import com.taotao.item.price.service.utils.JsonUtils;
/**
 * 商品价格服务系统
 * @author Administrator
 *
 */
@Service
public class ItemPriceServiceImpl implements ItemPriceService {
	
	@Autowired
	private ItemPriceMapper itemPriceMapper;
	@Autowired
	private RedisDao redisDao;
	
	/**
	 * 添加商品价格
	 */
	@Override
	@Transactional
	public void addItemPrice(ItemPrice itemPrice) {
		itemPriceMapper.addItemPrice(itemPrice);
		setItemPrice2Redis(itemPrice);
	}
	
	/**
	 * 删除上商品价格
	 */
	@Override
	@Transactional
	public void deleteItemPriceByItemId(long itemId) {
		itemPriceMapper.deleteItemPriceByItemId(itemId);
		delItemPriceFromRedis(itemId);
	}
	
	/**
	 * 更新商品价格
	 */
	@Override
	@Transactional
	public void updateItemPrice(ItemPrice itemPrice) {
		
		itemPriceMapper.updateItemPrice(itemPrice);	
	}
	
	/**
	 * 根据商品id查询商品价格
	 */
	@Override
	public ItemPrice findItemPriceByItemId(long itemId) {
		ItemPrice itemPrice = itemPriceMapper.findItemPriceByItemId(itemId);
		return itemPrice;
	}
	
	/**
	 * 将商品价格存入redis缓存
	 */
	@Override
	public void setItemPrice2Redis(ItemPrice itemPrice) {
		redisDao.setItemPrice2Redis(itemPrice);
	}
	
	/**
	 * 从redis缓存中删除商品价格
	 */
	@Override
	public void delItemPriceFromRedis(long itemId) {
		redisDao.delItemPriceByItemId(itemId);
	}
	
	/**
	 * 从redis缓存中查找缓存数据
	 */
	@Override
	public String findItemPriceFromRedis(long itemId) {
		System.out.println("日志================》itemId_"+itemId + "从redis缓存中查找" + redisDao.getItemPriceFromRedis(itemId));
		return redisDao.getItemPriceFromRedis(itemId);
	}
	
	/**
	 * 更新商品价格
	 */
	@Override
	public void updateItemPriceByItemId(long itemId, long price) {
		delItemPriceFromRedis(itemId);
		itemPriceMapper.updateItemPriceByItemId(itemId, price);
	}

}
