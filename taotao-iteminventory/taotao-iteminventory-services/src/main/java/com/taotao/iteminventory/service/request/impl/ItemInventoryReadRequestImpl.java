package com.taotao.iteminventory.service.request.impl;

import org.springframework.web.context.ContextLoader;

import com.taotao.iteminventory.dao.RedisDao;
import com.taotao.iteminventory.model.ItemInventory;
import com.taotao.iteminventory.service.ItemInventoryService;
import com.taotao.iteminventory.service.request.Request;
import com.taotao.iteminventory.utils.JsonUtils;
/**
 * 封装库存读请求
 * @author Administrator
 *
 */
public class ItemInventoryReadRequestImpl implements Request {
	
	private long itemId;
	
	public ItemInventoryReadRequestImpl(long itemId) {
		this.itemId = itemId;
	}

	@Override
	public void process() {
		//从redis缓存中读取
		//如果redis缓存中没有数据，从数据库中读取数据,并将数据更新到redis缓存中
		RedisDao redisDao = ContextLoader.getCurrentWebApplicationContext().getBean(RedisDao.class);
		String itemInventoryStr = redisDao.getItemInventoryFromRedisByItemId(itemId);
		System.out.println("日志================》从redis缓存中读取itemInventory_" + itemId + "：" + itemInventoryStr);
		
		if(itemInventoryStr == null) {
			ItemInventoryService itemInventoryService = ContextLoader.getCurrentWebApplicationContext().getBean(ItemInventoryService.class);
			ItemInventory itemInventory = itemInventoryService.findItemInventoryById(itemId);
			System.out.println("日志==============》从数据库中查询商品库存itemInventory_"+ itemId + ":" +itemInventory);
			
			redisDao.setItemInventory2Redis(itemId, JsonUtils.objectToJson(itemInventory));
			System.out.println("日志===============》将商品库存itemInventory_"+ itemId + itemInventory + "更新到redis中");
		}
	}

}
