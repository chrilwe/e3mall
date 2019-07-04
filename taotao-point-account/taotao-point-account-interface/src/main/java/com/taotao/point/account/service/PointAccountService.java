package com.taotao.point.account.service;

import java.util.List;

import org.mengyun.tcctransaction.api.TransactionContext;

import com.taotao.point.account.model.PointAccount;
import com.taotao.point.account.model.PointAccountHistory;
import com.taotao.point.account.model.PointPageBean;

public interface PointAccountService {
	/**
	 * 提供try接口,TCC事务入口
	 */
	public void creditToPointAccountTcc(TransactionContext transactionContext,
			String userNo, Integer pointAmount, 
			String requestNo, String bankTrxNo, String trxType, String remark);
	
	/**
	 * 查询积分记录
	 */
	public PointPageBean queryPointHistory(int page, int size, String userId);
	
	/**
	 * 根据用户查询积分
	 */
	public PointAccount findPointAmountByUserId(String userId);
	
}
