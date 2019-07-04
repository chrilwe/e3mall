package com.taotao.itemservice.mapper;

import com.taotao.itemservice.model.ItemProperty;

public interface ItemPropertyMapper {
	
	public void addItemProperty(ItemProperty itemProperty);
	
	public void deleteItemProperty(long id);
	
	public void updateItemProperty(ItemProperty itemProperty);
	
	public ItemProperty findItemPropertyById(long id);
	
	public ItemProperty findItemPropertyByItemId(long itemId);
}
