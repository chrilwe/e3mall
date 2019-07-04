package com.taotao.pay.account.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.taotao.pay.account.model.AccountRecord;

public interface AccountRecordMapper {
	public void addAccountRecord(AccountRecord accountRecord);
	
	public void deleteAccountRecordById(String id);
	
	public void updateAccountRecord(AccountRecord accountRecord);
	
	public AccountRecord findAccountRecordByMessageId(String messageId);
	
	public List<AccountRecord> queryAccountRecordList(@Param("page")int page, @Param("size")int size);
	
	public int account();
}
