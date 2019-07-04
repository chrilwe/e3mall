package com.taotao.iteminventory.service;

import com.taotao.iteminventory.model.ItemInventory;

public interface ItemInventoryService {
	public void addItemInventory(ItemInventory itemInventory);
	
	public void deleteItemInventory(long id);
	
	public void updateItemInventory(ItemInventory itemInventory);
	
	public ItemInventory findItemInventoryById(long id);
	
}
