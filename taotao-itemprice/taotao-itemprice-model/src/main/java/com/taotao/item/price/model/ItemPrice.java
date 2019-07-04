package com.taotao.item.price.model;

import java.io.Serializable;

public class ItemPrice implements Serializable {
	private Long id;
	
	private Long price;
	
	private Long itemId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	@Override
	public String toString() {
		return "ItemPrice [id=" + id + ", price=" + price + ", itemId="
				+ itemId + "]";
	}
	
}
