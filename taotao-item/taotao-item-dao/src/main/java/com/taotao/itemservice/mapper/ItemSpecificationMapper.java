package com.taotao.itemservice.mapper;

import com.taotao.itemservice.model.ItemSpecification;

public interface ItemSpecificationMapper {
	public void addItemSpecification(ItemSpecification itemSpecifiction);
	
	public void deleteItemSpecification(long id);
	
	public void updateItemSpecification(ItemSpecification itemSpecification);
	
	public ItemSpecification findItemSpecificationById(long id);
	
}
