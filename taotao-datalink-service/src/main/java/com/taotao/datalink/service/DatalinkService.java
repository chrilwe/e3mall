package com.taotao.datalink.service;

import com.taotao.datalink.model.ItemBrandAggr;
import com.taotao.datalink.model.ItemCategoryAggr;
import com.taotao.datalink.model.ItemDescriptionAggr;
import com.taotao.datalink.model.ItemInfoAggr;
import com.taotao.itemservice.model.Item;

public interface DatalinkService {
	public ItemInfoAggr setItemInfoAggr2Ehecache(ItemInfoAggr itemInfoAggr);
	
	public ItemInfoAggr getItemInfoAggrFromEhcache(long itemId);
	
	public void setItemInfoAggr2Redis(ItemInfoAggr itemInfoAggr);
	
	public ItemInfoAggr getItemInfoAggrFromRedis(long itemId);
	
	
	public ItemBrandAggr setItemBrandAggr2Ehecache(ItemBrandAggr itemBrandAggr);
	
	public ItemBrandAggr getItemBrandAggrFromEhcache(long itemId);
	
	public void setItemBrandAggr2Redis(ItemBrandAggr itemBrandAggr);
	
	public ItemBrandAggr getItemBrandAggrFromRedis(long itemId);
	
	
	public ItemCategoryAggr setItemCategoryAggr2Ehecache(ItemCategoryAggr itemCategoryAggr);
	
	public ItemCategoryAggr getItemCategoryAggrFromEhcache(long itemId);
	
	public void setItemCategoryAggr2Redis(ItemCategoryAggr itemCategoryAggr);
	
	public ItemCategoryAggr getItemCategoryAggrFromRedis(long itemId);
	
	
	public ItemDescriptionAggr setItemDescriptionAggr2Ehecache(ItemDescriptionAggr itemDescriptionAggr);
	
	public ItemDescriptionAggr getItemDescriptionAggrFromEhcache(long itemId);
	
	public void setItemDescriptionAggr2Redis(ItemDescriptionAggr itemDescriptionAggr);
	
	public ItemDescriptionAggr getItemDescriptionAggrFromRedis(long itemId);
	
}
