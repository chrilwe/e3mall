package com.taotao.datalink.model;

import java.io.Serializable;
import java.util.Date;

import com.taotao.itemservice.model.ItemDescription;

/**
 * 商品介绍维度的聚合类
 * @author Administrator
 *
 */
public class ItemDescriptionAggr implements Serializable {
	 private Date created;

	 private Date updated;

	 private String itemDesc;
	 
	 private long itemId;

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	@Override
	public String toString() {
		return "ItemDescriptionAggr [created=" + created + ", updated="
				+ updated + ", itemDesc=" + itemDesc + "]";
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public String getItemDesc() {
		return itemDesc;
	}

	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
}
