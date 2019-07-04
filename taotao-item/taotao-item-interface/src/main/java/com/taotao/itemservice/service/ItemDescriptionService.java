package com.taotao.itemservice.service;

import com.taotao.itemservice.model.ItemDescription;

public interface ItemDescriptionService {
	public void addItemDescription(ItemDescription itemDescription ,String queueType);
	
	public void deleteItemDescription(long id ,String queueType);
	
	public void updateItemDescription(ItemDescription itemDescription ,String queueType);
	
	public ItemDescription findItemDescriptionById(long id);
}
