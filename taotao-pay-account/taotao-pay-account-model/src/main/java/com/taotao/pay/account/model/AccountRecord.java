package com.taotao.pay.account.model;

import java.io.Serializable;
import java.util.Date;

public class AccountRecord implements Serializable {
	private String id;
	private Date createTime;
	private Date editTime;
	private Integer version;
	private String remark;
	private String accountNo;
	private Double amount;
	private Double banlance;
	private String fundDirection;
	private String isAllowSett;
	private String isCompleteSett;
	private String requestNo;
	private String bankTrxNo;
	private String trxType;
	private Integer riskDay;
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
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Double getBanlance() {
		return banlance;
	}
	public void setBanlance(Double banlance) {
		this.banlance = banlance;
	}
	public String getFundDirection() {
		return fundDirection;
	}
	public void setFundDirection(String fundDirection) {
		this.fundDirection = fundDirection;
	}
	public String getIsAllowSett() {
		return isAllowSett;
	}
	public void setIsAllowSett(String isAllowSett) {
		this.isAllowSett = isAllowSett;
	}
	public String getIsCompleteSett() {
		return isCompleteSett;
	}
	public void setIsCompleteSett(String isCompleteSett) {
		this.isCompleteSett = isCompleteSett;
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
	public Integer getRiskDay() {
		return riskDay;
	}
	public void setRiskDay(Integer riskDay) {
		this.riskDay = riskDay;
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
	@Override
	public String toString() {
		return "AccountRecord [id=" + id + ", createTime=" + createTime
				+ ", editTime=" + editTime + ", version=" + version
				+ ", remark=" + remark + ", accountNo=" + accountNo
				+ ", amount=" + amount + ", banlance=" + banlance
				+ ", fundDirection=" + fundDirection + ", isAllowSett="
				+ isAllowSett + ", isCompleteSett=" + isCompleteSett
				+ ", requestNo=" + requestNo + ", bankTrxNo=" + bankTrxNo
				+ ", trxType=" + trxType + ", riskDay=" + riskDay + ", userNo="
				+ userNo + ", status=" + status + "]";
	}
	
	
}
