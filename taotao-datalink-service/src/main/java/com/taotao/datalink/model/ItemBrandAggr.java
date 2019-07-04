package com.taotao.datalink.model;

import java.io.Serializable;
import java.util.Date;

import com.taotao.itemservice.model.ItemBrand;
/**
 * 商品标题维度聚合类
 * @author Administrator
 *
 */
public class ItemBrandAggr implements Serializable {
	private static final long serialVersionUID = -2174296727727694338L;

	private long itemId;
	
	private Date editTime;
	
	public long brandId;
	
	public String brand;

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	public Date getEditTime() {
		return editTime;
	}

	public void setEditTime(Date editTime) {
		this.editTime = editTime;
	}

	public long getBrandId() {
		return brandId;
	}

	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	@Override
	public String toString() {
		return "ItemBrandAggr [itemId=" + itemId + ", editTime=" + editTime
				+ ", brandId=" + brandId + ", brand=" + brand + "]";
	}
	
}
