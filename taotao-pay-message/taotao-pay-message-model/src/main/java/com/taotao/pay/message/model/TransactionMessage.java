package com.taotao.pay.message.model;

import java.io.Serializable;
import java.util.Date;
/**
 * 消息服务实体类
 * @author Administrator
 *
 */
public class TransactionMessage implements Serializable {
	private int version;
	
	private String editor;
	
	private String creater;
	
	private Date editTime;
	
	private Date createTime;
	
	private String messageId;

	private String messageBody;

	private String messageDataType;

	private String consumerQueue;

	private Integer messageSendTimes;

	private String areadlyDead;
	
	private String status;
	
	private String remark;

	private String field1;

	private String field2;

	private String field3;

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}

	public String getMessageDataType() {
		return messageDataType;
	}

	public void setMessageDataType(String messageDataType) {
		this.messageDataType = messageDataType;
	}

	public String getConsumerQueue() {
		return consumerQueue;
	}

	public void setConsumerQueue(String consumerQueue) {
		this.consumerQueue = consumerQueue;
	}

	public Integer getMessageSendTimes() {
		return messageSendTimes;
	}

	public void setMessageSendTimes(Integer messageSendTimes) {
		this.messageSendTimes = messageSendTimes;
	}

	public String getAreadlyDead() {
		return areadlyDead;
	}

	public void setAreadlyDead(String areadlyDead) {
		this.areadlyDead = areadlyDead;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	@Override
	public String toString() {
		return "TransactionMessage [version=" + version + ", editor=" + editor
				+ ", creater=" + creater + ", editTime=" + editTime
				+ ", createTime=" + createTime + ", messageId=" + messageId
				+ ", messageBody=" + messageBody + ", messageDataType="
				+ messageDataType + ", consumerQueue=" + consumerQueue
				+ ", messageSendTimes=" + messageSendTimes + ", areadlyDead="
				+ areadlyDead + ", status=" + status + ", remark=" + remark
				+ ", field1=" + field1 + ", field2=" + field2 + ", field3="
				+ field3 + "]";
	}
	
}
