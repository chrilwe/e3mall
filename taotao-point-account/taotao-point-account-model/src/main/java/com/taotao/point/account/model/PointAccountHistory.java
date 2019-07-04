package com.taotao.point.account.model;

import java.io.Serializable;
import java.util.Date;

public class PointAccountHistory implements Serializable {
	private static final long serialVersionUID = 7771953399419098388L;
	
	private String id;
	
	private Date createTime;
	
	private Date editTime;
	
	private Integer version;
	
	private String remark;
	
	private Integer amount;
	
	private Integer balance;
	
	private String foundDirection;
	
	private String requestNo;
	
	private String bankTrxNo;
	
	private String trxType;
	
	private String userNo;
	
	private String status;

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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	public String getFoundDirection() {
		return foundDirection;
	}

	public void setFoundDirection(String foundDirection) {
		this.foundDirection = foundDirection;
	}

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public String getBankTrxNo() {
		return bankTrxNo;
	}

	public void setBankTrxNo(String bankTrxNo) {
		this.bankTrxNo = bankTrxNo;
	}

	public String getTrxType() {
		return trxType;
	}

	public void setTrxType(String trxType) {
		this.trxType = trxType;
	}

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "PointAccountHistory [id=" + id + ", createTime=" + createTime
				+ ", editTime=" + editTime + ", version=" + version
				+ ", remark=" + remark + ", amount=" + amount + ", balance="
				+ balance + ", foundDirection=" + foundDirection
				+ ", requestNo=" + requestNo + ", bankTrxNo=" + bankTrxNo
				+ ", trxType=" + trxType + ", userNo=" + userNo + ", status="
				+ status + "]";
	}
	
}
