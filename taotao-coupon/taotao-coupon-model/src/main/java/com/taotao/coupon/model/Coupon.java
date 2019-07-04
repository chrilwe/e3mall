package com.taotao.coupon.model;

import java.io.Serializable;
import java.util.Date;

public class Coupon implements Serializable {
	private static final long serialVersionUID = 6229951047265944518L;
	
	private int id;
	private String title;//红包标题
	private double amount;//红包满减
	private int rangeId;//红包使用范围id
	private Date beginTime;//红包生效时间
	private Date endTime;//红包过期时间
	private int allowType;//红包限制使用类型(1.限在线支付,2.线上线下使用)
	private int fullReduction;//满足多少金额才够满减
	private Date createTime;//创建红包的时间
	private String status;//红包使用状态
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public int getRangeId() {
		return rangeId;
	}
	public void setRangeId(int ranngeId) {
		this.rangeId = ranngeId;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public int getAllowType() {
		return allowType;
	}
	public void setAllowType(int allowType) {
		this.allowType = allowType;
	}
	public int getFullReduction() {
		return fullReduction;
	}
	public void setFullReduction(int fullReduction) {
		this.fullReduction = fullReduction;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "Cuopon [id=" + id + ", title=" + title + ", amount=" + amount
				+ ", ranngeId=" + rangeId + ", beginTime=" + beginTime
				+ ", endTime=" + endTime + ", allowType=" + allowType
				+ ", fullReduction=" + fullReduction + ", createTime="
				+ createTime + ", status=" + status + "]";
	}
	
	
}
