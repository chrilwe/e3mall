package com.taotao.pay.account.model;

import java.io.Serializable;
import java.util.Date;

public class Account implements Serializable {
	private String id;
	private Date createTime;
	private Date editTime;
	private Integer version;
	private String remark;
	private String accountNo;
	private Double banlance;
	private Double unbanlance;
	private Double securityMoney;
	private String status;
	private Double totalIncome;
	private Double totalExpend;
	private Double todayIncome;
	private Double todayExpend;
	private String accountType;
	private Double settAmount;
	private String userNo;
	
	
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
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public Double getBanlance() {
		return banlance;
	}
	public void setBanlance(Double banlance) {
		this.banlance = banlance;
	}
	public Double getUnbanlance() {
		return unbanlance;
	}
	public void setUnbanlance(Double unbanlance) {
		this.unbanlance = unbanlance;
	}
	public Double getSecurityMoney() {
		return securityMoney;
	}
	public void setSecurityMoney(Double securityMoney) {
		this.securityMoney = securityMoney;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Double getTotalIncome() {
		return totalIncome;
	}
	public void setTotalIncome(Double totalIncome) {
		this.totalIncome = totalIncome;
	}
	public Double getTotalExpend() {
		return totalExpend;
	}
	public void setTotalExpend(Double totalExpend) {
		this.totalExpend = totalExpend;
	}
	public Double getTodayIncome() {
		return todayIncome;
	}
	public void setTodayIncome(Double todayIncome) {
		this.todayIncome = todayIncome;
	}
	public Double getTodayExpend() {
		return todayExpend;
	}
	public void setTodayExpend(Double todayExpend) {
		this.todayExpend = todayExpend;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public Double getSettAmount() {
		return settAmount;
	}
	public void setSettAmount(Double settAmount) {
		this.settAmount = settAmount;
	}
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	@Override
	public String toString() {
		return "Account [id=" + id + ", createTime=" + createTime
				+ ", editTime=" + editTime + ", version=" + version
				+ ", remark=" + remark + ", accountNo=" + accountNo
				+ ", banlance=" + banlance + ", unbanlance=" + unbanlance
				+ ", securityMoney=" + securityMoney + ", status=" + status
				+ ", totalIncome=" + totalIncome + ", totalExpend="
				+ totalExpend + ", todayIncome=" + todayIncome
				+ ", todayExpend=" + todayExpend + ", accountType="
				+ accountType + ", settAmount=" + settAmount + ", userNo="
				+ userNo + "]";
	}
	
	
}
