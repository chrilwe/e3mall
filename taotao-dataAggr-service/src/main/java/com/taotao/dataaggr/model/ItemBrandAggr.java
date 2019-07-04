package com.taotao.dataaggr.model;

import java.io.Serializable;
import java.util.Date;

import com.taotao.itemservice.model.ItemBrand;
/**
 * 商品标题维度聚合类
 * @author Administrator
 *
 */
public class ItemBrandAggr implements Serializable {
	private ItemBrand itemBrand;
	
	private long itemId;
	
	private Date editTime;
	

	public Date getEditTime() {
		return editTime;
	}

	public void setEditTime(Date editTime) {
		this.editTime = editTime;
	}

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	public ItemBrand getItemBrand() {
		return itemBrand;
	}

	public void setItemBrand(ItemBrand itemBrand) {
		this.itemBrand = itemBrand;
	}

	@Override
	public String toString() {
		return "ItemBrandAggr [itemBrand=" + itemBrand + "]";
	}
	
}
