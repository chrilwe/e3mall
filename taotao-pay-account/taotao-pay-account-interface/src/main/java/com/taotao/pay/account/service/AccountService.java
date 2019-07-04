package com.taotao.pay.account.service;

import com.taotao.pay.account.model.Account;
import com.taotao.pay.account.model.AccountPageBean;
import com.taotao.pay.account.model.AccountRecord;

public interface AccountService {
	public void addAccount(Account account, AccountRecord accountRecord);//插入会计记账信息
	
	public AccountRecord findAccountRecordByMessageId(String messageId);//根据消息系统的id查询会计记账历史记录
	
	public Account findAccountByUserNo(String userNo);//根据用户id查询会计记账
	
	public AccountPageBean queryAccountList(int page, int size);//分页查询
	
	public AccountPageBean queryAccountRecordList(int page, int size);
}
