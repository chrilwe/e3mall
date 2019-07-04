package com.taotao.order.model;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable {
	 private String id;
	 
	 private Integer version;
	 
	 private Date createTime;
	 
	 private String editor;
	 
	 private String creater;
	 
	 private Date editTime;
	 
	 private String status;
	 
	 private String prouctName;
	 
	 private String merchantOrderNo;
	 
	 private Long orderAmount;
	 
	 private String orderForm;
	 
	 private String merchantName;
	 
	 private String merchantNo;
	 
	 private Date orderTime;
	 
	 private Date orderDate;
	 
	 private String orderIp;
	 
	 private String orderRefererUrl;
	 
	 private String returnUrl;
	 
	 private String notifyUrl;
	 
	 private String cancelReason;
	 
	 private Integer orderPeriod;
	 
	 private Date expireTime;
	 
	 private String payWayCode;
	 
	 private String payWayName;
	 
	 private String remark;
	 
	 private String trxType;
	 
	 private String payTypeCode;
	 
	 private String payTypeName;
	 
	 private String fundIntoType;
	 
	 private String isRefund;
	 
	 private Integer refundTimes;
	 
	 private Long successRefundAmount;
	 
	 private String field1;
	 
	 private String field2;
	 
	 private String field3;
	 
	 private String field4;
	 
	 private String field5;
	 
	 private String trxNo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public Date getEditTime() {
		return editTime;
	}

	public void setEditTime(Date editTime) {
		this.editTime = editTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProuctName() {
		return prouctName;
	}

	public void setProuctName(String prouctName) {
		this.prouctName = prouctName;
	}

	public String getMerchantOrderNo() {
		return merchantOrderNo;
	}

	public void setMerchantOrderNo(String merchantOrderNo) {
		this.merchantOrderNo = merchantOrderNo;
	}

	public Long getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Long orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getOrderForm() {
		return orderForm;
	}

	public void setOrderForm(String orderForm) {
		this.orderForm = orderForm;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderIp() {
		return orderIp;
	}

	public void setOrderIp(String orderIp) {
		this.orderIp = orderIp;
	}

	public String getOrderRefererUrl() {
		return orderRefererUrl;
	}

	public void setOrderRefererUrl(String orderRefererUrl) {
		this.orderRefererUrl = orderRefererUrl;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	public Integer getOrderPeriod() {
		return orderPeriod;
	}

	public void setOrderPeriod(Integer orderPeriod) {
		this.orderPeriod = orderPeriod;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public String getPayWayCode() {
		return payWayCode;
	}

	public void setPayWayCode(String payWayCode) {
		this.payWayCode = payWayCode;
	}

	public String getPayWayName() {
		return payWayName;
	}

	public void setPayWayName(String payWayName) {
		this.payWayName = payWayName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTrxType() {
		return trxType;
	}

	public void setTrxType(String trxType) {
		this.trxType = trxType;
	}

	public String getPayTypeCode() {
		return payTypeCode;
	}

	public void setPayTypeCode(String payTypeCode) {
		this.payTypeCode = payTypeCode;
	}

	public String getPayTypeName() {
		return payTypeName;
	}

	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}

	public String getFundIntoType() {
		return fundIntoType;
	}

	public void setFundIntoType(String fundIntoType) {
		this.fundIntoType = fundIntoType;
	}

	public String getIsRefund() {
		return isRefund;
	}

	public void setIsRefund(String isRefund) {
		this.isRefund = isRefund;
	}

	public Integer getRefundTimes() {
		return refundTimes;
	}

	public void setRefundTimes(Integer refundTimes) {
		this.refundTimes = refundTimes;
	}

	public Long getSuccessRefundAmount() {
		return successRefundAmount;
	}

	public void setSuccessRefundAmount(Long successRefundAmount) {
		this.successRefundAmount = successRefundAmount;
	}

	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}

	public String getField2() {
		return field2;
	}

	public void setField2(String field2) {
		this.field2 = field2;
	}

	public String getField3() {
		return field3;
	}

	public void setField3(String field3) {
		this.field3 = field3;
	}

	public String getField4() {
		return field4;
	}

	public void setField4(String field4) {
		this.field4 = field4;
	}

	public String getField5() {
		return field5;
	}

	public void setField5(String field5) {
		this.field5 = field5;
	}

	public String getTrxNo() {
		return trxNo;
	}

	public void setTrxNo(String trxNo) {
		this.trxNo = trxNo;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", version=" + version + ", createTime="
				+ createTime + ", editor=" + editor + ", creater=" + creater
				+ ", editTime=" + editTime + ", status=" + status
				+ ", prouctName=" + prouctName + ", merchantOrderNo="
				+ merchantOrderNo + ", orderAmount=" + orderAmount
				+ ", orderForm=" + orderForm + ", merchantName=" + merchantName
				+ ", merchantNo=" + merchantNo + ", orderTime=" + orderTime
				+ ", orderDate=" + orderDate + ", orderIp=" + orderIp
				+ ", orderRefererUrl=" + orderRefererUrl + ", returnUrl="
				+ returnUrl + ", notifyUrl=" + notifyUrl + ", cancelReason="
				+ cancelReason + ", orderPeriod=" + orderPeriod
				+ ", expireTime=" + expireTime + ", payWayCode=" + payWayCode
				+ ", payWayName=" + payWayName + ", remark=" + remark
				+ ", trxType=" + trxType + ", payTypeCode=" + payTypeCode
				+ ", payTypeName=" + payTypeName + ", fundIntoType="
				+ fundIntoType + ", isRefund=" + isRefund + ", refundTimes="
				+ refundTimes + ", successRefundAmount=" + successRefundAmount
				+ ", field1=" + field1 + ", field2=" + field2 + ", field3="
				+ field3 + ", field4=" + field4 + ", field5=" + field5
				+ ", trxNo=" + trxNo + "]";
	}
	
}
