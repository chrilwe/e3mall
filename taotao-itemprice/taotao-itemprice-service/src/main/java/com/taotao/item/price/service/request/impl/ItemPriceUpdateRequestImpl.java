package com.taotao.item.price.service.request.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoader;

import com.taotao.item.price.service.ItemPriceService;
import com.taotao.item.price.service.request.Request;
/**
 * 封装更新商品请求
 * @author Administrator
 *
 */
public class ItemPriceUpdateRequestImpl implements Request {
	
	private long itemId;
	
	private long price;
	
	
	public ItemPriceUpdateRequestImpl(long itemId, long price) {
		this.itemId = itemId;
		this.price = price;
	}

	@Override
	public void process() {
		ItemPriceService itemPriceService = ContextLoader.getCurrentWebApplicationContext().getBean(ItemPriceService.class);
		//从redis中删除原来的缓存数据
		//更新数据库中的商品价格数据
		itemPriceService.updateItemPriceByItemId(itemId, price);
		System.out.println("日志=============》更新数据库商品价格"+itemId+"数据,并且删除redis缓存中的数据");
	}

	@Override
	public long getItemId() {
		
		return itemId;
	}

}
