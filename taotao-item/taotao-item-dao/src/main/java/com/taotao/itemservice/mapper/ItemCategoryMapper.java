package com.taotao.itemservice.mapper;

import java.util.List;

import com.taotao.itemservice.model.ItemCategory;

public interface ItemCategoryMapper {
	public void addItemCategory(ItemCategory itemCategory);
	
	public void deleteItemCategory(long id);
	
	public void updateItemCategory(ItemCategory itemCategory);
	
	public ItemCategory findItemCategoryById(long id);
	
	public List<ItemCategory> findItemCategoryByParentId(long parentId);
	
}
