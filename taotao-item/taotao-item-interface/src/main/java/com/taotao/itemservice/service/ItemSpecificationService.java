package com.taotao.itemservice.service;

import com.taotao.itemservice.model.ItemSpecification;

public interface ItemSpecificationService {
	public void addItemSpecification(ItemSpecification itemSpecifiction ,String queueType);
	
	public void deleteItemSpecification(long id ,String queueType);
	
	public void updateItemSpecification(ItemSpecification itemSpecification ,String queueType);
	
	public ItemSpecification findItemSpecificationById(long id);
}
