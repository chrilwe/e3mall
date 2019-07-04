package com.taotao.point.account.model;

import java.io.Serializable;
import java.util.Date;

public class PointAccount implements Serializable {
	private static final long serialVersionUID = -2353791753912310162L;
	
	private String id;
	
	private Date createTime;
	
	private Date editTime;
	
	private Integer version;
	
	private String userNo;
	
	private Integer balance;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getEditTime() {
		return editTime;
	}

	public void setEditTime(Date editTime) {
		this.editTime = editTime;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "PointAccount [id=" + id + ", createTime=" + createTime
				+ ", editTime=" + editTime + ", version=" + version
				+ ", userNo=" + userNo + ", balance=" + balance + "]";
	}
	
}
