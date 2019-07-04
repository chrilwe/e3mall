package com.taotao.iteminventory.model;

import java.io.Serializable;
import java.util.Date;
/**
 * 商品库存
 * @author Administrator
 *
 */
public class ItemInventory implements Serializable {
	private long id;
	
	private long inventory;
	
	private long itemId;
	
	private Date updated;
	
	private Date created;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getInventory() {
		return inventory;
	}

	public void setInventory(long inventory) {
		this.inventory = inventory;
	}

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Override
	public String toString() {
		return "ItemInventory [id=" + id + ", inventory=" + inventory
				+ ", itemId=" + itemId + ", updated=" + updated + ", created="
				+ created + "]";
	}
	
	
}
