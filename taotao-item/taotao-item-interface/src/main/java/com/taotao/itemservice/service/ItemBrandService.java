package com.taotao.itemservice.service;

import com.taotao.itemservice.model.ItemBrand;

public interface ItemBrandService {
	public void addItemBrand(ItemBrand itemBrand ,String queueType);
	
	public void deleteItemBrand(long id ,String queueType);
	
	public void updateItemBrand(ItemBrand itemBrand ,String queueType);
	
	public ItemBrand findItemBrandById(long id);
	
	public ItemBrand findItemBrandByItemId(long itemId);
}
