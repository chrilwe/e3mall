package com.taotao.es.search.model;

import java.io.Serializable;

public class Comment implements Serializable {
	private long itemId;//评论关联的商品
	private String id;
	private String message;//评论信息
	private long userId;//评论者
	private String created;
	private String updated;
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getUpdated() {
		return updated;
	}
	public void setUpdated(String updated) {
		this.updated = updated;
	}
	public long getItemId() {
		return itemId;
	}
	public void setItemId(long itemId) {
		this.itemId = itemId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		return "Comment [itemId=" + itemId + ", id=" + id + ", message="
				+ message + ", userId=" + userId + "]";
	}
	
}
