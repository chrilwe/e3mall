package com.taotao.order.model;

import java.io.Serializable;

/**
 * 接收订单页面传过来的信息
 * @author Administrator
 *
 */
public class OrderItem implements Serializable {
	private long itemId;
	private int num;
	private long price;
	private long totalFee;
	private String productName;
	private String image;
	private String orderId;
	
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public long getItemId() {
		return itemId;
	}
	public void setItemId(long itemId) {
		this.itemId = itemId;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}
	public long getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(long totalFee) {
		this.totalFee = totalFee;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	@Override
	public String toString() {
		return "OrderItem [itemId=" + itemId + ", num=" + num + ", price="
				+ price + ", totalFee=" + totalFee + ", productName="
				+ productName + ", image=" + image + "]";
	}
	
	
}
