package com.taotao.request.receiver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.request.receiver.activemq.MessageSender;

/**
 * 接收访问请求
 * @author Administrator
 *
 */
@Controller
public class ReceiverRequestController {
	
	@RequestMapping("/count")
	@ResponseBody
	public String receiveRequest(long itemId) {
		//发送请求信息到mq
		MessageSender sender = new MessageSender();
		sender.sendMessage(itemId);
		return null;
	}
}
