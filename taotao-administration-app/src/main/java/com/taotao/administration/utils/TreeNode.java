package com.taotao.administration.utils;
/**
 * 封装节点
 * @author Administrator
 *
 */
public class TreeNode {
	private String state;
	
	private String text;
	
	private long id;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
