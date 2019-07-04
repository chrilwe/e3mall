package com.taotao.cart.model;

import java.io.Serializable;

public class Cart implements Serializable {
	private static final long serialVersionUID = 1211075112044381821L;

	private Long id;
	
	private String title;
	
	private Long price;
	
	private Integer num;
	
	private String image;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "Cart [id=" + id + ", title=" + title + ", price=" + price
				+ ", num=" + num + ", image=" + image + "]";
	}
	
}
