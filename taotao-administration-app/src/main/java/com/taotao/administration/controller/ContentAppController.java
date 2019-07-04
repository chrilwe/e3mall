package com.taotao.administration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.administration.utils.TaotaoUtils;
import com.taotao.content.model.Content;
import com.taotao.content.model.ContentPageBean;
import com.taotao.content.service.ContentService;
/**
 * 广告内容管理客户端
 * @author Administrator
 *
 */
@Controller
public class ContentAppController {
	
	@Autowired
	private ContentService contentService;
	
	/**
	 * 添加广告内容接口
	 */
	@RequestMapping("/content/save")
	@ResponseBody
	public TaotaoUtils addContent(Content content) {
		try {
			//调用广告添加记录服务
			contentService.addContent(content);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("异常================》添加广告内容异常：" + e);
			return TaotaoUtils.build(500, "添加广告内容异常");
		}
		return TaotaoUtils.ok();
	}
	
	/**
	 * 分页查询广告内容
	 */
	@RequestMapping("/content/query/list")
	@ResponseBody
	public ContentPageBean getContentList(int page, int rows) {
		int from = (page - 1) * rows;
		return contentService.queryContents(from, rows);
	}
	
	/**
	 * 批量删除广告内容接口
	 */
	@RequestMapping("/content/delete")
	@ResponseBody
	public TaotaoUtils deleteContents(String ids) {
		try {
			contentService.deleteContents(ids);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("异常===============》批量删除广告内容异常:" + e);
			return TaotaoUtils.build(501, "批量删除广告内容异常");
		}
		return TaotaoUtils.ok();
	}
}
