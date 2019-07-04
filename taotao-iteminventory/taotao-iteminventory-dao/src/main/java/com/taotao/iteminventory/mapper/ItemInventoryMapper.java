package com.taotao.iteminventory.mapper;

import com.taotao.iteminventory.model.ItemInventory;

public interface ItemInventoryMapper {
	public void addItemInventory(ItemInventory itemInventory);
	
	public void deleteItemInventory(long id);
	
	public void updateItemInventory(ItemInventory itemInventory);
	
	public ItemInventory findItemInventoryById(long id);
}
