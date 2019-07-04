package com.taotao.oneservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.taotao.item.price.model.ItemPrice;
import com.taotao.item.price.service.ItemPriceRequestService;
import com.taotao.iteminventory.model.ItemInventory;
import com.taotao.iteminventory.service.ItemInventoryRequestService;
import com.taotao.iteminventory.service.ItemInventoryService;

/**
 * 一站式服务，主要服务于时效性比较高的数据
 * @author Administrator
 *
 */
@Controller
public class OneServiceController {
	
	@Autowired
	private ItemPriceRequestService requestService;
	@Autowired
	private ItemInventoryRequestService itemInventoryRequestService;
	
	/**
	 * 更新商品价格
	 */
	@RequestMapping("/one-service/updateItemPrice")
	@ResponseBody
	public void updateItemPrice(long itemId, long price) {
		requestService.updateItemPrice(itemId, price);
	}
	
	/**
	 * 读取商品价格
	 */
	@RequestMapping("/one-service/getItemPrice")
	@ResponseBody
	public ItemPrice getItemPrice(long itemId) {
		ItemPrice itemPrice = requestService.findItemPriceByItemId(itemId);
		
		return itemPrice;
	}
	
	/**
	 * 获取商品库存
	 * @param itemId
	 * @return
	 */
	@RequestMapping("/one-service/getItemInventory")
	@ResponseBody
	public ItemInventory getItemInventory(long itemId) {
		
		return itemInventoryRequestService.readItemInventoryRequest(itemId);
	}
	
	/**
	 * 更新库存
	 */
	@RequestMapping("/one-service/updateItemInventory")
	@ResponseBody
	public String updateItemInventory(ItemInventory itemInventory) {
		itemInventoryRequestService.updateItemInventoryRequest(itemInventory);
		return "success";
	}
}
