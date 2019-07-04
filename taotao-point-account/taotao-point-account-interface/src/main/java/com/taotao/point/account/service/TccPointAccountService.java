package com.taotao.point.account.service;

import java.util.Map;

import org.mengyun.tcctransaction.api.Compensable;
import org.mengyun.tcctransaction.api.TransactionContext;

import com.taotao.point.account.model.PointAccount;

public interface TccPointAccountService {
	@Compensable
	public void updateOrCreatePointAccount(TransactionContext transactionContext, Map<String, String> params);
	
	public void confirmPointAccount(TransactionContext transactionContext, Map<String, String> params);
	
	public void cancelPointAccount(TransactionContext transactionContext, Map<String, String> params);
}
