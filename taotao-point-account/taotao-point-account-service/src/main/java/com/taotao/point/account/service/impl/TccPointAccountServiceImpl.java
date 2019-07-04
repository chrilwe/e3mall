package com.taotao.point.account.service.impl;

import java.util.Date;
import java.util.Map;
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
import com.taotao.point.account.service.TccPointAccountService;
/**
 * 积分TCC事务，积分变更服务
 * @author Administrator
 *
 */
@Service
public class TccPointAccountServiceImpl implements TccPointAccountService {
	
	@Autowired
	private PointAccountMapper pointAccountMapper;
	@Autowired
	private PointAccountHistoryMapper pointAccountHistoryMappper;
	
	/**
	 * try接口，对外提供TCC事务服务
	 */
	@Override
	@Transactional
	@Compensable(confirmMethod = "confirmPointAccount",cancelMethod = "cancelPointAccount")
	public void updateOrCreatePointAccount(
			TransactionContext transactionContext, Map<String, String> params) {
		System.out.println("日志====================积分系统TCC尝试阶段开始===============");
		String userNo = params.get("userNo");
		String requestNo = params.get("requestNo");
		String amount = params.get("amount");
		String balance = params.get("balance");
		String bankTrxNo = params.get("bankTrxNo");
		String remark = params.get("remark");
		/**
		 * 判断用户积分记录是否存在
		 */
		PointAccount pointAccount = pointAccountMapper.findPointAccountByUserNo(userNo);
		if(pointAccount == null) {
			System.out.println("异常==============》用户积分账号不存在");
			throw new RuntimeException("用户积分不存在");
		}
		
		/**
		 * 根据唯一请求号进行幂等判断，因为try方法会尝试多次重试
		 */
		PointAccountHistory pointAccountHistory = pointAccountHistoryMappper.findPointAccountHistoryByRequestNo(requestNo);
		if(pointAccountHistory == null) {
			//历史记录为空，说明没有进行重试操作,插入一条积分操作
			PointAccountHistory pointHistory = new PointAccountHistory();
			pointHistory.setAmount(Integer.parseInt(amount));
			pointHistory.setBalance(Integer.parseInt(balance));
			pointHistory.setBankTrxNo(bankTrxNo);
			pointHistory.setCreateTime(new Date());
			pointHistory.setEditTime(new Date());
			pointHistory.setFoundDirection("ADD");
			pointHistory.setId(UUID.randomUUID().toString());
			pointHistory.setRemark(remark);
			pointHistory.setRequestNo(requestNo);
			pointHistory.setStatus("TRYING");
			pointHistory.setTrxType("TCC");
			pointHistory.setUserNo(userNo);
			pointHistory.setVersion(1);
			
			pointAccountHistoryMappper.addPointAccountHistory(pointHistory);
			System.out.println("日志====================》用户"+userNo+"添加积分记录，TCC事务TRYING阶段");
		} else if(pointAccountHistory.getStatus().equals("CANCEL")) {
			System.out.println("日志==================》用户"+userNo+"添加积分记录过程发生未知异常，重新进入TRYING阶段");
			pointAccountHistoryMappper.updatePointAccountHistoryStatusEditTimeByRequestNo(requestNo, "TRYING", new Date());
		}
		
		System.out.println("日志====================积分系统TCC尝试阶段完成===============");
	}
	
	/**
	 * TCC确认方法
	 */
	@Transactional
	public void confirmPointAccount(
			TransactionContext transactionContext, Map<String, String> params) {
		System.out.println("日志====================积分系统TCC确认阶段开始===============");
		String requestNo = params.get("requestNo");
		String balance = params.get("balance");
		String userNo = params.get("userNo");
		//幂等判断，如果积分历史记录为空，或者积分状态不是TRYING阶段的就不能进入确认方法
		PointAccountHistory pointAccountHistory = pointAccountHistoryMappper.findPointAccountHistoryByRequestNo(requestNo);
		if(pointAccountHistory == null || !pointAccountHistory.getStatus().equals("TRYING")) {
			System.out.println("日志==============》积分记录不是TRYING阶段，不走CONFIRM方法");
			return;
		}
		
		//给用户积分积分，并且将记录状态更改为CONFIRM
		pointAccountMapper.updatePointAccountBalanceEditTimeByUserNo(Integer.parseInt(balance), new Date(), userNo);
		pointAccountHistoryMappper.updatePointAccountHistoryStatusEditTimeByRequestNo(requestNo, "CONFIRM", new Date());
		System.out.println("日志==============》用户"+userNo+"增加积分完成");
		System.out.println("日志====================积分系统TCC确认阶段完成===============");
	}
	
	/**
	 * TCC 取消方法
	 */
	@Transactional
	public void cancelPointAccount(
			TransactionContext transactionContext, Map<String, String> params) {
		System.out.println("日志====================积分系统TCC取消阶段开始===============");
		String requestNo = params.get("requestNo");
		//不是TRYING阶段或者记录为空，就不会进入CANCEL阶段
		PointAccountHistory pointAccountHistory = pointAccountHistoryMappper.findPointAccountHistoryByRequestNo(requestNo);
		if(pointAccountHistory == null || !pointAccountHistory.equals("TRYING")) {
			return;
		}
		
		/**
		 * 更新状态为cancel阶段
		 */
		pointAccountHistoryMappper.updatePointAccountHistoryStatusEditTimeByRequestNo(requestNo, "CANCEL", new Date());
		System.out.println("日志=====================》用户增加积分发生未知异常，执行CANCEL阶段");
		System.out.println("日志====================积分系统TCC取消阶段完成===============");
	}
}
