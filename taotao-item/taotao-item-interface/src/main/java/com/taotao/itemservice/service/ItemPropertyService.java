package com.taotao.itemservice.service;

import com.taotao.itemservice.model.ItemProperty;

public interface ItemPropertyService {
	public void addItemProperty(ItemProperty itemProperty ,String queueType);
	
	public void deleteItemProperty(long id ,String queueType);
	
	public void updateItemProperty(ItemProperty itemProperty ,String queueType);
	
	public ItemProperty findItemPropertyById(long id);
	
	public ItemProperty findItemPropertyByItemId(long itemId);
}
