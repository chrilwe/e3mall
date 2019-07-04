package com.taotao.pay.message.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.taotao.pay.message.model.TransactionMessage;

public interface TransactionMessageMapper {
	public int insert(TransactionMessage transactionMessage);
	
	public int deleteTMByMessageId(String messageId);
	
	public int updateStatusAndEditTime(@Param("messageId")String messageId, 
			@Param("status")String status, @Param("editTime")Date editTime);
	
	public TransactionMessage findByMessageId(String messageId);
	
	public int update(TransactionMessage transactionMessage);
	
	public int updateMessageAlreadyDead(String isAlreadlyDead);
	
	public List<TransactionMessage> getMessagePageList(@Param("status")String status,
			@Param("isAreadlyDead")String isAreadlyDead, @Param("from")int from, @Param("to")int to);
	
	public List<TransactionMessage> queryMessageList(@Param("page")int page, @Param("size")int size);
	
	public int account();
	
	public List<TransactionMessage> queryDeadMessageList(@Param("page")int page, 
			@Param("size")int size, @Param("areadlyDead")String areadlyDead);
	
	public int countDeadMessage();
	
}
