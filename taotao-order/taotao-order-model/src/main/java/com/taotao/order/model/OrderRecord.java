package com.taotao.order.model;

import java.io.Serializable;
import java.util.Date;

public class OrderRecord implements Serializable {
	private static final long serialVersionUID = -6117678246177026752L;
	
	private String id;
	
	private Integer version;
	
	private Date createTime;
	
	private String status;
	
	private String editor;
	
	private String creater;
	
	private Date editTime;
	
	private String productName;
	
	private String merchantOrderNo;
	
	private String trxNo;
	
	private String bankOrderNo;
	
	private String bankTrxNo;
	
	private String merchantName;
	
	private String merchantNo;
	
	private String payerUserNo;
	
	private String payerName;
	
	private Long payerPayAmount;
	
	private Long payerFee;
	
	private String payerAccountType;
	
	private String receiverUserNo;
	
	private String receiverName;
	
	private Long receiverPayAmount;
	
	private Long receiverFee;
	
	private String receiverAccountType;
	
	private String orderIp;
	
	private String orderRefererUrl;
	
	private Long orderAmount;
	
	private Long platIncome;
	
	private Long feeRate;
	
	private Long platCost;
	
	private Long platProfit;
	
	private String returnUrl;
	
	private String notifyUrl;
	
	private String payWayCode;
	
	private String payWayName;
	
	private Date paySuccessTime;
	
	private Date completeTime;
	
	private String isRefund;
	
	private Integer refundTimes;
	
	private Long successRefundAmount;
	
	private String trxType;
	
	private String orderFrom;
	
	private String payTypeCode;
	
	private String payTypeName;
	
	private String fundIntoType;
	
	private String remark;
	
	private String field1;
	
	private String field2;
	
	private String field3;
	
	private String field4;
	
	private String field5;
	
	private String bankReturnMsg;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getMerchantOrderNo() {
		return merchantOrderNo;
	}

	public void setMerchantOrderNo(String merchantOrderNo) {
		this.merchantOrderNo = merchantOrderNo;
	}

	public String getTrxNo() {
		return trxNo;
	}

	public void setTrxNo(String trxNo) {
		this.trxNo = trxNo;
	}

	public String getBankOrderNo() {
		return bankOrderNo;
	}

	public void setBankOrderNo(String bankOrderNo) {
		this.bankOrderNo = bankOrderNo;
	}

	public String getBankTrxNo() {
		return bankTrxNo;
	}

	public void setBankTrxNo(String bankTrxNo) {
		this.bankTrxNo = bankTrxNo;
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

	public String getPayerUserNo() {
		return payerUserNo;
	}

	public void setPayerUserNo(String payerUserNo) {
		this.payerUserNo = payerUserNo;
	}

	public String getPayerName() {
		return payerName;
	}

	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}

	public Long getPayerPayAmount() {
		return payerPayAmount;
	}

	public void setPayerPayAmount(Long payerPayAmount) {
		this.payerPayAmount = payerPayAmount;
	}

	public Long getPayerFee() {
		return payerFee;
	}

	public void setPayerFee(Long payerFee) {
		this.payerFee = payerFee;
	}

	public String getPayerAccountType() {
		return payerAccountType;
	}

	public void setPayerAccountType(String payerAccountType) {
		this.payerAccountType = payerAccountType;
	}

	public String getReceiverUserNo() {
		return receiverUserNo;
	}

	public void setReceiverUserNo(String receiverUserNo) {
		this.receiverUserNo = receiverUserNo;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public Long getReceiverPayAmount() {
		return receiverPayAmount;
	}

	public void setReceiverPayAmount(Long receiverPayAmount) {
		this.receiverPayAmount = receiverPayAmount;
	}

	public Long getReceiverFee() {
		return receiverFee;
	}

	public void setReceiverFee(Long receiverFee) {
		this.receiverFee = receiverFee;
	}

	public String getReceiverAccountType() {
		return receiverAccountType;
	}

	public void setReceiverAccountType(String receiverAccountType) {
		this.receiverAccountType = receiverAccountType;
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

	public Long getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Long orderAmount) {
		this.orderAmount = orderAmount;
	}

	public Long getPlatIncome() {
		return platIncome;
	}

	public void setPlatIncome(Long platIncome) {
		this.platIncome = platIncome;
	}

	public Long getFeeRate() {
		return feeRate;
	}

	public void setFeeRate(Long feeRate) {
		this.feeRate = feeRate;
	}

	public Long getPlatCost() {
		return platCost;
	}

	public void setPlatCost(Long platCost) {
		this.platCost = platCost;
	}

	public Long getPlatProfit() {
		return platProfit;
	}

	public void setPlatProfit(Long platProfit) {
		this.platProfit = platProfit;
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

	public Date getPaySuccessTime() {
		return paySuccessTime;
	}

	public void setPaySuccessTime(Date paySuccessTime) {
		this.paySuccessTime = paySuccessTime;
	}

	public Date getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
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

	public String getTrxType() {
		return trxType;
	}

	public void setTrxType(String trxType) {
		this.trxType = trxType;
	}

	public String getOrderFrom() {
		return orderFrom;
	}

	public void setOrderFrom(String orderFrom) {
		this.orderFrom = orderFrom;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public String getBankReturnMsg() {
		return bankReturnMsg;
	}

	public void setBankReturnMsg(String bankReturnMsg) {
		this.bankReturnMsg = bankReturnMsg;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "OrderRecord [id=" + id + ", version=" + version
				+ ", createTime=" + createTime + ", status=" + status
				+ ", editor=" + editor + ", creater=" + creater + ", editTime="
				+ editTime + ", productName=" + productName
				+ ", merchantOrderNo=" + merchantOrderNo + ", trxNo=" + trxNo
				+ ", bankOrderNo=" + bankOrderNo + ", bankTrxNo=" + bankTrxNo
				+ ", merchantName=" + merchantName + ", merchantNo="
				+ merchantNo + ", payerUserNo=" + payerUserNo + ", payerName="
				+ payerName + ", payerPayAmount=" + payerPayAmount
				+ ", payerFee=" + payerFee + ", payerAccountType="
				+ payerAccountType + ", receiverUserNo=" + receiverUserNo
				+ ", receiverName=" + receiverName + ", receiverPayAmount="
				+ receiverPayAmount + ", receiverFee=" + receiverFee
				+ ", receiverAccountType=" + receiverAccountType + ", orderIp="
				+ orderIp + ", orderRefererUrl=" + orderRefererUrl
				+ ", orderAmount=" + orderAmount + ", platIncome=" + platIncome
				+ ", feeRate=" + feeRate + ", platCost=" + platCost
				+ ", platProfit=" + platProfit + ", returnUrl=" + returnUrl
				+ ", notifyUrl=" + notifyUrl + ", payWayCode=" + payWayCode
				+ ", payWayName=" + payWayName + ", paySuccessTime="
				+ paySuccessTime + ", completeTime=" + completeTime
				+ ", isRefund=" + isRefund + ", refundTimes=" + refundTimes
				+ ", successRefundAmount=" + successRefundAmount + ", trxType="
				+ trxType + ", orderFrom=" + orderFrom + ", payTypeCode="
				+ payTypeCode + ", payTypeName=" + payTypeName
				+ ", fundIntoType=" + fundIntoType + ", remark=" + remark
				+ ", field1=" + field1 + ", field2=" + field2 + ", field3="
				+ field3 + ", field4=" + field4 + ", field5=" + field5
				+ ", bankReturnMsg=" + bankReturnMsg + "]";
	}
	
}
