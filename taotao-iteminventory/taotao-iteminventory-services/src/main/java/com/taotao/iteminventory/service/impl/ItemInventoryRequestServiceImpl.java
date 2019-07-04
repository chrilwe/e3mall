package com.taotao.iteminventory.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.iteminventory.dao.RedisDao;
import com.taotao.iteminventory.model.ItemInventory;
import com.taotao.iteminventory.service.ItemInventoryService;
import com.taotao.iteminventory.service.ItemInventoryRequestService;
import com.taotao.iteminventory.service.queue.Asyc2Queue;
import com.taotao.iteminventory.service.request.Request;
import com.taotao.iteminventory.service.request.impl.ItemInventoryReadRequestImpl;
import com.taotao.iteminventory.service.request.impl.ItemInventoryUpdateRequestImpl;
import com.taotao.iteminventory.utils.JsonUtils;
/**
 * 封装库存请求，库存系统服务
 * @author Administrator
 *
 */
@Service
public class ItemInventoryRequestServiceImpl implements ItemInventoryRequestService {
	
	@Value("${WAITTIME}")
	private Long WAITTIME;
	
	@Autowired
	private RedisDao redisDao;
	@Autowired
	private ItemInventoryService itemInventoryService;
	
	/**
	 * 更新库存请求
	 */
	@Override
	public void updateItemInventoryRequest(ItemInventory itemInventory) {
		//封装更新请求
		//将封装的请求路由到内存队列中
		Request request = new ItemInventoryUpdateRequestImpl(itemInventory);
		Asyc2Queue queue = new Asyc2Queue(itemInventory.getItemId());
		queue.process(request);
	}
	
	/**
	 * 读取库存请求
	 */
	@Override
	public ItemInventory readItemInventoryRequest(long itemId) {
		//封装更新请求
		//将封装的请求路由到内存队列中
		Request request = new ItemInventoryReadRequestImpl(itemId);
		Asyc2Queue queue = new Asyc2Queue(itemId);
		queue.process(request);
		
		//从redis中读取数据
		//超时没有从redis获取到数据，直接从数据库中查找
		long waitTime = 0l;
		long startTime = System.currentTimeMillis();
		while(true) {
			if(waitTime > WAITTIME) {
				System.out.println("日志===========》从redis获取数据超过"+ WAITTIME + "毫秒没有获取到，不在从redis获取");
				break;
			}
			//从redis中获取数据
			String itemInventoryStr = redisDao.getItemInventoryFromRedisByItemId(itemId);
			System.out.println("日志============》从redis中获取到库存" + itemInventoryStr);
			if(itemInventoryStr == null) {
				waitTime = System.currentTimeMillis() - startTime;
			} else {
				System.out.println("日志=============》从redis中获取到库存" + itemInventoryStr);
				return JsonUtils.jsonToPojo(itemInventoryStr, ItemInventory.class);
			}
		}
		
		//从数据库获取库存
		ItemInventory itemInventory = itemInventoryService.findItemInventoryById(itemId);
		
		return itemInventory;
	}
	
	/**
	 * 批量读取
	 */
	@Override
	public List<ItemInventory> readItemInventorysRequest(String itemIds) {
		if(itemIds != null && !itemIds.equals("")) {
			List<ItemInventory> result = new ArrayList<ItemInventory>();
			String[] itemIdsArray = itemIds.split(",");
			for (String itemId : itemIdsArray) {
				ItemInventory readItemInventoryRequest = readItemInventoryRequest(Long.parseLong(itemId));
				result.add(readItemInventoryRequest);
			}
			
			return result;
		}
		return null;
	}
	
	/**
	 * 批量更新
	 */
	@Override
	public void updateItemInventorysRequest(List<ItemInventory> itemInventorys) {
		if(itemInventorys != null && itemInventorys.size() > 0) {
			for (ItemInventory itemInventory : itemInventorys) {
				updateItemInventoryRequest(itemInventory);
			}
		}
	}

}
