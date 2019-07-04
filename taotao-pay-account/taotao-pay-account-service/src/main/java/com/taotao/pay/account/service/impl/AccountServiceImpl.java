package com.taotao.pay.account.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taotao.pay.account.mapper.AccountMapper;
import com.taotao.pay.account.mapper.AccountRecordMapper;
import com.taotao.pay.account.model.Account;
import com.taotao.pay.account.model.AccountPageBean;
import com.taotao.pay.account.model.AccountRecord;
import com.taotao.pay.account.service.AccountService;
/**
 * 会计系统服务
 * @author Administrator
 *
 */
@Service
public class AccountServiceImpl implements AccountService {
	
	@Autowired
	private AccountMapper accountMapper;
	@Autowired
	private AccountRecordMapper accountRecordMapper;
	
	/**
	 * 插入会计记账信息
	 */
	@Override
	@Transactional
	public void addAccount(Account account, AccountRecord accountRecord) {
		accountMapper.addAccount(account);
		accountRecordMapper.addAccountRecord(accountRecord);
	}
	
	/**
	 * 根据消息系统id查询会计记录
	 */
	@Override
	public AccountRecord findAccountRecordByMessageId(String messageId) {
		
		return accountRecordMapper.findAccountRecordByMessageId(messageId);
	}
	
	/**
	 * 根据用户id查询会计记账
	 */
	@Override
	public Account findAccountByUserNo(String userNo) {
		
		return accountMapper.findAccountByUserNo(userNo);
	}
	
	/**
	 * 分页查询账单
	 */
	@Override
	public AccountPageBean queryAccountList(int page, int size) {
		List<Account> accountList = accountMapper.queryAccountList(page, size);
		int total = accountMapper.account();
		
		AccountPageBean bean = new AccountPageBean();
		bean.setRows(accountList);
		bean.setTotal(total);
		return bean;
	}
	
	/**
	 * 入账记录分页
	 */
	@Override
	public AccountPageBean queryAccountRecordList(int page, int size) {
		List<AccountRecord> accountRecordList = accountRecordMapper.queryAccountRecordList(page, size);
		int total = accountRecordMapper.account();
		
		AccountPageBean bean = new AccountPageBean();
		bean.setRows(accountRecordList);
		bean.setTotal(total);
		return bean;
	}

}
