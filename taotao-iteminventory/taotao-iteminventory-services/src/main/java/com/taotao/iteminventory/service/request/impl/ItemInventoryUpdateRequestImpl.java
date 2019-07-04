package com.taotao.iteminventory.service.request.impl;

import org.springframework.web.context.ContextLoader;

import com.taotao.iteminventory.model.ItemInventory;
import com.taotao.iteminventory.service.ItemInventoryService;
import com.taotao.iteminventory.service.request.Request;
/**
 * 封装写请求
 * @author Administrator
 *
 */
public class ItemInventoryUpdateRequestImpl implements Request {
	
	private ItemInventory itemInventory;
	
	public ItemInventoryUpdateRequestImpl(ItemInventory itemInventory) {
		this.itemInventory = itemInventory;
	}

	@Override
	public void process() {
		//删除redis中的库存数据
		//更新数据库中的库存数据
		ItemInventoryService itemInventoryService = ContextLoader.getCurrentWebApplicationContext().getBean(ItemInventoryService.class);
		itemInventoryService.updateItemInventory(itemInventory);
		System.out.println("日志============》" + itemInventory.getItemId() + "删除redis中的库存数据,更新数据库中的库存数据");
	}

}
