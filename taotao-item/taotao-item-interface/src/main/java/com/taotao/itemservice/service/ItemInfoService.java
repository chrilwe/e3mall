package com.taotao.itemservice.service;

import java.util.List;

import com.taotao.itemservice.model.Item;
import com.taotao.itemservice.model.ItemCategory;
import com.taotao.itemservice.model.ItemDescription;
import com.taotao.itemservice.model.ItemPageBean;

public interface ItemInfoService {
	
	public void addItem(Item item ,String queueType);
	
	public void deleteItem(long itemId ,String queueType);
	
	public void updateItem(Item item ,String queueType);
	
	public Item getItemInfoById(long itemId);
	
	public ItemPageBean findItems(int page, int pageSize);
	
	public void deleteItems(String ids, String queueType);
	
	public void updateItemsStatus(String ids, String queueType);
	
	public List<Item> getItemsByIds(String ids);
	
	public void updateItemInfoAndDesc(Item item, ItemDescription itemDescription);
	
	public List<ItemCategory> findCidByParentId(long parentId);
	
	public String findItemTypeCid(long cid);
}
