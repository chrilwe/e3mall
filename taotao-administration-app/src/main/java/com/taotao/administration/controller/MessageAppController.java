package com.taotao.administration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.administration.utils.TaotaoUtils;
import com.taotao.pay.message.model.MessagePageBean;
import com.taotao.pay.message.service.TransactionMessageService;
/**
 * 消息管理客户端
 * @author Administrator
 *
 */
@Controller
public class MessageAppController {
	
	@Autowired
	private TransactionMessageService transactionMessageService;
	
	/**
	 * 消息分页查询
	 */
	@RequestMapping("/message/list")
	@ResponseBody
	public MessagePageBean queryMessageList(int page, int rows) {
		
		return transactionMessageService.queryMessageList((page - 1) * rows, rows);
	}
	
	/**
	 * 进入死亡队列的消息
	 */
	@RequestMapping("/message/dead/list")
	@ResponseBody
	public MessagePageBean queryDeadMessageList(int page, int rows) {
		
		return transactionMessageService.queryDeadMessageList((page - 1) * rows, rows, "YES");
	}
	
	/**
	 * 处理死亡的消息
	 * @throws Exception 
	 */
	@RequestMapping("/message/dead/handler")
	@ResponseBody
	public TaotaoUtils handlerDeadMessages(String ids) throws Exception {
		String[] messageIdArray = ids.split(",");
		for (String messageId : messageIdArray) {
			transactionMessageService.confirmAndSendMessage(messageId);
		}
		
		return TaotaoUtils.ok();
	}
}
