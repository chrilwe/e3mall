package com.taotao.iteminventory.dao;

import com.taotao.iteminventory.model.ItemInventory;

public interface RedisDao {
	public void setItemInventory2Redis(long itemId, String itemInventory);
	
	public void deleteItemInventoryFromRedisByItemId(long itemId);
	
	public String getItemInventoryFromRedisByItemId(long itemId);
}
