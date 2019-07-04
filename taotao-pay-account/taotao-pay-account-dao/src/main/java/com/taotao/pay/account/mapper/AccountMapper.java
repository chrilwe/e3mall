package com.taotao.pay.account.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.taotao.pay.account.model.Account;

public interface AccountMapper {
	public void addAccount(Account account);
	
	public void deleteAccountById(String id);
	
	public void updateAccount(Account account);
	
	public Account findAccountByUserNo(String userNo);
	
	public List<Account> queryAccountList(@Param("page")int page, @Param("size")int size);
	
	public int account();
}
