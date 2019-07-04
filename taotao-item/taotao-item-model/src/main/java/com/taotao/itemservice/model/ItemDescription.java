package com.taotao.itemservice.model;

import java.io.Serializable;
import java.util.Date;
/**
 * 商品介绍
 * @author Administrator
 *
 */
public class ItemDescription implements Serializable {
	 private Long itemId;

	 private Date created;

	 private Date updated;

	 private String itemDesc;

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
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

	@Override
	public String toString() {
		return "ItemDescription [itemId=" + itemId + ", created=" + created
				+ ", updated=" + updated + ", itemDesc=" + itemDesc + "]";
	}
	 
	 
}
