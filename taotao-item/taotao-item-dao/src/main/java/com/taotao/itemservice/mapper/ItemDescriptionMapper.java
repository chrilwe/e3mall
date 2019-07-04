package com.taotao.itemservice.mapper;

import com.taotao.itemservice.model.ItemDescription;

public interface ItemDescriptionMapper {
	public void addItemDescription(ItemDescription itemDescription);
	
	public void deleteItemDescription(long id);
	
	public void updateItemDescription(ItemDescription itemDescription);
	
	public ItemDescription findItemDescriptionById(long id);
}
