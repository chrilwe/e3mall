package com.taotao.user.app.utils;

public class UserAndRole {
	private String username;
	private String rolename;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	@Override
	public String toString() {
		return "UserAndRole [username=" + username + ", rolename=" + rolename
				+ "]";
	}
	
}
