package com.taotao.dataaggr.model;

import java.io.Serializable;

import com.taotao.itemservice.model.Item;
import com.taotao.itemservice.model.ItemProperty;
import com.taotao.itemservice.model.ItemSpecification;

/**
 * 商品基本信息维度的聚合类
 * @author Administrator
 *
 */
public class ItemInfoAggr implements Serializable {
	private Item item;
	
	private long itemId;
	
	private ItemSpecification itemSpecification;
	
	private ItemProperty itemProperty;
	

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public ItemSpecification getItemSpecification() {
		return itemSpecification;
	}

	public void setItemSpecification(ItemSpecification itemSpecification) {
		this.itemSpecification = itemSpecification;
	}

	public ItemProperty getItemProperty() {
		return itemProperty;
	}

	public void setItemProperty(ItemProperty itemProperty) {
		this.itemProperty = itemProperty;
	}

	@Override
	public String toString() {
		return "ItemInfoAggr [item=" + item + ", itemSpecification="
				+ itemSpecification + ", itemProperty=" + itemProperty + "]";
	}
	
}
