package com.taotao.pay.message.exception;

public class MessageExecption extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String msg;
	
	public MessageExecption(String msg) {
		super(msg);
		this.msg = msg;
	}
	
}
