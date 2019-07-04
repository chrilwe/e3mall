package com.taotao.itemservice.service;

import java.util.List;

import com.taotao.itemservice.model.ItemCategory;

public interface ItemCategoryService {
	public void addItemCategory(ItemCategory itemCategory ,String queueType);
	
	public void deleteItemCategory(long id ,String queueType);
	
	public void updateItemCategory(ItemCategory itemCategory ,String queueType);
	
	public ItemCategory findItemCategoryById(long id);
	
	public List<ItemCategory> findItemCategoryByParentId(long parentId);
	
	public List<ItemCategory> findItemCategorys(String ids);
}
