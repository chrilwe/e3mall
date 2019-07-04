package com.taotao.dataaggr.model;

import com.taotao.itemservice.model.ItemDescription;

/**
 * 商品介绍维度的聚合类
 * @author Administrator
 *
 */
public class ItemDescriptionAggr {
	private ItemDescription itemDescription;
	
	private long itemId;

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	public ItemDescription getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(ItemDescription itemDescription) {
		this.itemDescription = itemDescription;
	}

	@Override
	public String toString() {
		return "ItemDescriptionAggr [itemDescription=" + itemDescription + "]";
	}
	
}
