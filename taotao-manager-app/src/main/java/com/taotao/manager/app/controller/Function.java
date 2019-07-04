package com.taotao.manager.app.controller;

import java.io.Serializable;

public class Function implements Serializable {
	private String id;
	private String name;//菜单名称
	private String code;//简称
	private String description;//描述
	private String page;//访问url
	private String zindex;//排序序号
	private String pId;//父类id
	private Integer isMenu;//是否为菜单
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
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getZindex() {
		return zindex;
	}
	public void setZindex(String zindex) {
		this.zindex = zindex;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public Integer getIsMenu() {
		return isMenu;
	}
	public void setIsMenu(Integer isMenu) {
		this.isMenu = isMenu;
	}
	@Override
	public String toString() {
		return "Function [id=" + id + ", name=" + name + ", code=" + code
				+ ", description=" + description + ", page=" + page
				+ ", zindex=" + zindex + ", pId=" + pId + ", isMenu=" + isMenu
				+ "]";
	}
	
}
