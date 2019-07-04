package com.taotao.search.model;

import java.io.Serializable;
/**
 * 搜索引擎的实体类
 * @author Administrator
 *
 */
public class SearchModel implements Serializable {
	private static final long serialVersionUID = 9045430196427678782L;
	
	private String id;
	private String title;
	private String sellPoint;
	@Override
	public String toString() {
		return "SearchModel [id=" + id + ", title=" + title + ", sellPoint="
				+ sellPoint + ", price=" + price + ", image=" + image
				+ ", categoryName=" + categoryName + "]";
	}
	private long price;
	private String image;
	private String categoryName;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSellPoint() {
		return sellPoint;
	}
	public void setSellPoint(String sellPoint) {
		this.sellPoint = sellPoint;
	}
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
}
