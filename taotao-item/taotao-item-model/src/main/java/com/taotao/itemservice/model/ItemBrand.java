package com.taotao.itemservice.model;

import java.io.Serializable;
/**
 * 商品品牌
 * @author Administrator
 *
 */
public class ItemBrand implements Serializable {
	
	public long id;
	
	public String brand;
	
	public long itemId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	@Override
	public String toString() {
		return "ItemBrand [id=" + id + ", brand=" + brand + ", itemId="
				+ itemId + "]";
	}
	
}
