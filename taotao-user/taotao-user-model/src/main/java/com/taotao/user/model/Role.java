package com.taotao.user.model;

import java.io.Serializable;

public class Role implements Serializable {
	private String id;
	private String name;
	private String code;
	private String description;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "Role [id=" + id + ", name=" + name + ", code=" + code
				+ ", description=" + description + "]";
	}
	
}
