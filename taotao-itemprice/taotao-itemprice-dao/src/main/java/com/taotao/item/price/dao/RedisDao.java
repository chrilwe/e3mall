package com.taotao.item.price.dao;

import com.taotao.item.price.model.ItemPrice;

public interface RedisDao {
	public void setItemPrice2Redis(ItemPrice itemPrice);
	
	public String getItemPriceFromRedis(long itemId);
	
	public void delItemPriceByItemId(long itemId);
}
