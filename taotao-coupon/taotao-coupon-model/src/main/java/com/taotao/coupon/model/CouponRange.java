package com.taotao.coupon.model;

import java.io.Serializable;
import java.util.Date;

public class CouponRange implements Serializable {
	private static final long serialVersionUID = 4049264064044754569L;
	
	private int id;
	private int shopId;
	private Date createTime;
	private Date editTime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getShopId() {
		return shopId;
	}
	public void setShopId(int shopId) {
		this.shopId = shopId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getEditTime() {
		return editTime;
	}
	public void setEditTime(Date editTime) {
		this.editTime = editTime;
	}
	@Override
	public String toString() {
		return "CuoponRange [id=" + id + ", shopId=" + shopId + ", createTime="
				+ createTime + ", editTime=" + editTime + "]";
	}
	
}
