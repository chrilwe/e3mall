package com.taotao.notice.pojo;

import java.io.Serializable;

public class Notice implements Serializable {
	private String id;
	private long userId;
	private String message;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "Notice [id=" + id + ", userId=" + userId + ", message="
				+ message + "]";
	}
	
}
