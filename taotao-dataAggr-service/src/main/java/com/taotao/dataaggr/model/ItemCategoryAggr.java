package com.taotao.dataaggr.model;

import java.io.Serializable;

import com.taotao.itemservice.model.ItemCategory;

/**
 * 商品分类维度聚合类
 * @author Administrator
 *
 */
public class ItemCategoryAggr implements Serializable {
	private ItemCategory itemCategory;
	
	private long itemId;

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	public ItemCategory getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(ItemCategory itemCategory) {
		this.itemCategory = itemCategory;
	}

	@Override
	public String toString() {
		return "ItemCategoryAggr [itemCategory=" + itemCategory + "]";
	}
	
}
