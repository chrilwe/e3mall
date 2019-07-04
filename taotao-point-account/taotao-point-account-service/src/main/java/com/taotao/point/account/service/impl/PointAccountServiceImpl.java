package com.taotao.point.account.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.mengyun.tcctransaction.api.Compensable;
import org.mengyun.tcctransaction.api.TransactionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taotao.point.account.mapper.PointAccountHistoryMapper;
import com.taotao.point.account.mapper.PointAccountMapper;
import com.taotao.point.account.model.PointAccount;
import com.taotao.point.account.model.PointAccountHistory;
import com.taotao.point.account.model.PointPageBean;
import com.taotao.point.account.service.PointAccountService;
/**
 * 积分服务
 * @author Administrator
 *
 */
@Service
public class PointAccountServiceImpl implements PointAccountService {
	
	@Autowired
	private PointAccountMapper pointAccountMapper;
	
	@Autowired
	private PointAccountHistoryMapper pointAccountHistoryMapper;

	@Override
	@Transactional
	@Compensable(confirmMethod = "confirmCreditToPointAccountTcc",cancelMethod = "cancelCreditToPointAccountTcc")
	public void creditToPointAccountTcc(TransactionContext transactionContext,
			String userNo, Integer pointAmount, String requestNo,
			String bankTrxNo, String trxType, String remark) {
		
		System.out.println("日志===========》POINT_ACCOUNT TCC事务TRYING阶段开始");
		//幂等操作
		PointAccount pointAccount = pointAccountMapper.findPointAccountByUserNo(userNo);
		if(pointAccount == null) {
			pointAccount = new PointAccount();
			pointAccount.setCreateTime(new Date());
			pointAccount.setEditTime(new Date());
			pointAccount.setId(UUID.randomUUID().toString());
			pointAccount.setUserNo(userNo);
			pointAccount.setVersion(1);
			pointAccount.setBalance(1);
			pointAccountMapper.addPointAccount(pointAccount);
		}
		
		PointAccountHistory pointAccountHistory = pointAccountHistoryMapper.
				findPointAccountHistoryByRequestNo(requestNo);
		if(pointAccountHistory == null) {
			pointAccountHistory = new PointAccountHistory();
			pointAccountHistory.setAmount(pointAmount);
			pointAccountHistory.setBankTrxNo(bankTrxNo);
			pointAccountHistory.setCreateTime(new Date());
			pointAccountHistory.setEditTime(new Date());
			pointAccountHistory.setFoundDirection("ADD");
			pointAccountHistory.setId(UUID.randomUUID().toString());
			pointAccountHistory.setRemark(remark);
			pointAccountHistory.setRequestNo(requestNo);
			pointAccountHistory.setStatus("TRYING");
			pointAccountHistory.setTrxType("TCC");
			pointAccountHistory.setUserNo(userNo);
			pointAccountHistory.setVersion(1);
			pointAccountHistoryMapper.addPointAccountHistory(pointAccountHistory);
		} else if(pointAccountHistory.getStatus().equals("CANCEL")){
			//业务之前出现问题，重试的时候如果是CANCEL状态，需要将状态重新改为TRYING
			System.out.println("日志=========》POINT_ACCOUNT TCC事务重试,将CANCEL阶段改为TRYING阶段");
			pointAccountHistoryMapper.updatePointAccountHistoryStatusEditTimeByRequestNo(requestNo, "TRYING", new Date());
			
		}
		System.out.println("日志========》POINT_ACCOUNT TCC事务TRYING阶段结束");
	}
	
	/**
	 * TCC confirm方法
	 * @param transactionContext
	 * @param userNo
	 * @param pointAmount
	 * @param requestNo
	 * @param bankTrxNo
	 * @param trxType
	 * @param remark
	 */
	@Transactional
	public void confirmCreditToPointAccountTcc(TransactionContext transactionContext,
			String userNo, Integer pointAmount, String requestNo,
			String bankTrxNo, String trxType, String remark) {
		
		System.out.println("日志=========》POINT_ACCOUNT TCC事务COMFIRM阶段开始");
		//幂等操作
		//交易流失已经处理完成，不需要重复处理
		PointAccountHistory pointAccountHistory = pointAccountHistoryMapper.findPointAccountHistoryByRequestNo(requestNo);
		if(pointAccountHistory == null || pointAccountHistory.getStatus().equals("CONFIRMED")) {
			System.out.println("日志=========》//该笔交易流水已处理过,不需再处理");
			return;
		}
		
		/**
		 * 流水号改变状态
		 */
		pointAccountHistoryMapper.updatePointAccountHistoryStatusEditTimeByRequestNo(requestNo, "CONFIRMED", new Date());
		
		/**
		 * 增加积分余额
		 */
		PointAccount pointAccount = pointAccountMapper.findPointAccountByUserNo(userNo);
		Integer balance = pointAmount + pointAccount.getBalance();
		pointAccountMapper.updatePointAccountBalanceEditTimeByUserNo(balance, new Date(), userNo);
		
		System.out.println("日志=========》POINT_ACCOUNT TCC事务CONFIRM阶段结束");
	}
	
	/**
	 * TCC cancel方法
	 * @param transactionContext
	 * @param userNo
	 * @param pointAmount
	 * @param requestNo
	 * @param bankTrxNo
	 * @param trxType
	 * @param remark
	 */
	@Transactional
	public void cancelCreditToPointAccountTcc(TransactionContext transactionContext,
			String userNo, Integer pointAmount, String requestNo,
			String bankTrxNo, String trxType, String remark) {
		
		System.out.println("日志=========》POINT_ACCOUNT TCC事务CANCEL阶段开始");
		//幂等操作
		//交易流失已经处理完成，不需要重复处理
		PointAccountHistory pointAccountHistory = pointAccountHistoryMapper.findPointAccountHistoryByRequestNo(requestNo);
		if(pointAccountHistory == null || pointAccountHistory.getStatus().equals("CONFIRMED")) {
			System.out.println("日志=========》//该笔交易流水已处理过,不需再处理");
			return;
		}
		
		//将状态变更为CANCELED
		pointAccountHistoryMapper.updatePointAccountHistoryStatusEditTimeByRequestNo(requestNo, "CANCELED", new Date());
		
		System.out.println("日志=========》POINT_ACCOUNT TCC事务CANCEL阶段结束");
	}
	
	/**
	 * 查询积分记录 
	 */
	@Override
	public PointPageBean queryPointHistory(int page, int size, String userId) {
		PointPageBean bean = new PointPageBean();
		List<PointAccountHistory> list = pointAccountHistoryMapper.queryByUserId(page, size, userId);
		int total = pointAccountHistoryMapper.countPointHistory(userId);
		bean.setRows(list);
		bean.setTotal(total);
		return bean;
	}
	
	/**
	 * 根据用户查询积分
	 */
	@Override
	public PointAccount findPointAmountByUserId(String userId) {
		
		return pointAccountMapper.findPointAccountByUserNo(userId);
	}

}
