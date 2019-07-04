package com.taotao.item.price.service;

import com.taotao.item.price.model.ItemPrice;

public interface ItemPriceService {
	public void addItemPrice(ItemPrice itemPrice);
	
	public void deleteItemPriceByItemId(long itemId);
	
	public void updateItemPrice(ItemPrice itemPrice);
	
	public void updateItemPriceByItemId(long itemId, long price);
	
	public ItemPrice findItemPriceByItemId(long itemId);
	
	public void setItemPrice2Redis(ItemPrice itemPrice);
	
	public void delItemPriceFromRedis(long itemId);
	
	public String findItemPriceFromRedis(long itemId);
}
