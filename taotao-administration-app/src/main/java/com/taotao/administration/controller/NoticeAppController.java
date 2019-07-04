package com.taotao.administration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.administration.utils.TaotaoUtils;
import com.taotao.notice.pojo.Advise;
import com.taotao.notice.pojo.Notice;
import com.taotao.notice.service.AdviseService;
import com.taotao.notice.service.NoticeService;
/**
 * 公告栏消息
 * @author Administrator
 *
 */
@Controller
public class NoticeAppController {
	
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private AdviseService adviseService;
	
	/**
	 * 展示公告栏消息
	 * @return
	 */
	@RequestMapping("/manager/notice")
	@ResponseBody
	public TaotaoUtils showNotice() {
		Notice notice = noticeService.findNotice();
		return TaotaoUtils.ok(notice.getMessage());
	}
	
	/**
	 * 用户建议反馈
	 */
	@RequestMapping("/manager/advise")
	@ResponseBody
	public TaotaoUtils advise(Advise advise) {
		adviseService.updateAdvise(advise);
		
		return TaotaoUtils.ok();
	}
}
