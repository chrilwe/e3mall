package com.taotao.administration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.pay.account.model.AccountPageBean;
import com.taotao.pay.account.service.AccountService;
/**
 * 会计记账管理客户端
 * @author Administrator
 *
 */
@Controller
public class AccountAppController {
	@Autowired
	private AccountService accountService;
	
	/**
	 * 会计记账分页查询
	 */
	@RequestMapping("/account/list")
	@ResponseBody
	public AccountPageBean queryAccountList(int page, int rows) {
		//调用记账系统接口
		return accountService.queryAccountList((page - 1) * rows, rows);
	}
	
	/**
	 * 入账记录分页查询
	 */
	@RequestMapping("/account/history/list")
	@ResponseBody
	public AccountPageBean queryAccountHistoryList(int page, int rows) {
		
		return accountService.queryAccountRecordList((page - 1) * rows, rows);
	}
	
}
