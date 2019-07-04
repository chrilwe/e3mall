package com.taotao.iteminventory.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.taotao.iteminventory.dao.RedisDao;
import com.taotao.iteminventory.mapper.ItemInventoryMapper;
import com.taotao.iteminventory.model.ItemInventory;
import com.taotao.iteminventory.service.ItemInventoryService;
import com.taotao.iteminventory.utils.JsonUtils;
/**
 * 商品库存服务
 * @author Administrator
 *
 */
@Service
public class ItemInventoryServiceImpl implements ItemInventoryService {
	
	@Autowired
	private ItemInventoryMapper itemInventoryMapper;
	
	@Autowired
	private RedisDao redisDao;
	
	/**
	 * 添加库存
	 */
	@Override
	@Transactional
	public void addItemInventory(ItemInventory itemInventory) {
		
		itemInventoryMapper.addItemInventory(itemInventory);
		//更新缓存主集群的缓存数据
		redisDao.setItemInventory2Redis(itemInventory.getItemId(), JsonUtils.objectToJson(itemInventory));
	}
	
	/**
	 * 删除库存
	 */
	@Override
	@Transactional
	public void deleteItemInventory(long id) {
		itemInventoryMapper.deleteItemInventory(id);
		
		//更新redis缓存主集群的缓存数据
		redisDao.deleteItemInventoryFromRedisByItemId(id);
	}
	
	/**
	 * 更新库存
	 */
	@Override
	@Transactional
	public void updateItemInventory(ItemInventory itemInventory) {
		redisDao.deleteItemInventoryFromRedisByItemId(itemInventory.getItemId());
		itemInventoryMapper.updateItemInventory(itemInventory);
	}
	
	/**
	 * id查找库存
	 */
	@Override
	public ItemInventory findItemInventoryById(long id) {
		//从数据库中读取
		ItemInventory itemInventory = itemInventoryMapper.findItemInventoryById(id);
		return itemInventory;
	}

}
