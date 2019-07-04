package com.taotao.user.model;

import java.io.Serializable;

public class RoleFunction implements Serializable {
	private String userId;
	private String functionId;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFunctionId() {
		return functionId;
	}
	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}
	@Override
	public String toString() {
		return "RoleFunction [userId=" + userId + ", functionId=" + functionId
				+ "]";
	}
	
}
