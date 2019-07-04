package com.taotao.itemservice.mapper;

import com.taotao.itemservice.model.ItemBrand;

public interface ItemBrandMapper {
	
	public void addItemBrand(ItemBrand itemBrand);
	
	public void deleteItemBrand(long id);
	
	public void updateItemBrand(ItemBrand itemBrand);
	
	public ItemBrand findItemBrandById(long id);
	
	public ItemBrand findItemBrandByItemId(long itemId);
	
}
