package com.taotao.notice.pojo;

import java.io.Serializable;

public class Advise implements Serializable {
	private String id;
	private long userId;
	private String advise;
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
	public String getAdvise() {
		return advise;
	}
	public void setAdvise(String advise) {
		this.advise = advise;
	}
	@Override
	public String toString() {
		return "Advise [id=" + id + ", userId=" + userId + ", advise=" + advise
				+ "]";
	}
	
}
