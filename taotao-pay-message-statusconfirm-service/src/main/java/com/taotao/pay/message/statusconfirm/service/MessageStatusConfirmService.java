package com.taotao.pay.message.statusconfirm.service;

public interface MessageStatusConfirmService {
	public void handlerTradeWaitingConfirmTimeOutMessage() throws Exception;
	
	public void handlerTradeSendingTimeOutMessage() throws Exception;
}
