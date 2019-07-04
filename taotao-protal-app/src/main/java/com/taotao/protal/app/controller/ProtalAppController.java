package com.taotao.protal.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.content.model.Content;
import com.taotao.content.service.ContentRequestService;
import com.taotao.protal.app.utils.TaotaoUtils;
/**
 * 淘淘商城首页服务
 * @author Administrator
 *
 */
@Controller
public class ProtalAppController {
	
	@Value("${CONTENT_CATEGORY_ID}")
	private long CONTENT_CATEGORY_ID;
	
	@Autowired
	private ContentRequestService contentRequestService;
	
	/**
	 * 展示动态首页接口
	 * @return
	 */
	@RequestMapping("/protal")
	public String showProtalPage(Model model) {
		List<Content> contents = contentRequestService.readContentsRequest(CONTENT_CATEGORY_ID);
		model.addAttribute("contents", contents);
		return "index";
	}
	
	
	/**
	 * 获取大广告数据
	 */
	@RequestMapping("/content/list")
	@ResponseBody
	public TaotaoUtils getContents() {
		
		return TaotaoUtils.ok(contentRequestService.readContentsRequest(CONTENT_CATEGORY_ID));
	}

}
