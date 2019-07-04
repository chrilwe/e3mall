package com.taotao.iteminventory.service;

import java.util.List;

import com.taotao.iteminventory.model.ItemInventory;

public interface ItemInventoryRequestService {
	public void updateItemInventoryRequest(ItemInventory itemInventory);
	
	public ItemInventory readItemInventoryRequest(long itemId);
	
	public List<ItemInventory> readItemInventorysRequest(String itemIds);
	
	public void updateItemInventorysRequest(List<ItemInventory> itemInventorys);
	
}
