package com.taotao.pay.message.service;

import java.util.List;
import java.util.Map;

import com.taotao.pay.message.model.MessagePageBean;
import com.taotao.pay.message.model.TransactionMessage;

public interface TransactionMessageService {
	//预发送消息
	public int saveAndWaitingConfirm(TransactionMessage transactionMessage) throws Exception;
		
	//确认并发送消息
	public void confirmAndSendMessage(String messageId) throws Exception;
		
	//存储并发送消息
	public void saveAndSendMessage(TransactionMessage transactionMessage) throws Exception;
		
	//重新发送消息
	public void reSendMessage(TransactionMessage transactionMessage) throws Exception;
	
	//删除消息
	public void deleteMessageById(String messageId);
	
	//将消息标记为已死亡
	public void updateMessageToAlreadyDead(String isAlReady);
	
	//分页查询待确认或者发送中状态的消息
	public List<TransactionMessage> getMessagePageList(Map<String, String> paramsMap);
	
	//根据消息id查询消息
	public TransactionMessage getMessageByMessageId(String messageId);
	
	public MessagePageBean queryMessageList(int page, int size);
	
	public MessagePageBean queryDeadMessageList(int page, int size, String areadlyDead);
	
}
