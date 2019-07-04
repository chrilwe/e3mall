package com.taotao.content.request.impl;

import org.springframework.web.context.ContextLoader;

import com.taotao.content.model.Content;
import com.taotao.content.request.ContentRequest;
import com.taotao.content.service.ContentService;
/**
 * 封装广告内容更新请求
 * @author Administrator
 *
 */
public class UpdateContentRequestImpl implements ContentRequest {
	
	private Content content;
	
	public UpdateContentRequestImpl(Content content) {
		this.content = content;
	}

	@Override
	public void process() {
		/**
		 * 删除缓存中的旧版本数据，更新数据库的旧版本数据
		 */
		System.out.println("------------------------广告内容更新后台线程-----------------");
		ContentService contentService = ContextLoader.getCurrentWebApplicationContext().getBean(ContentService.class);
		contentService.updateContent(content);
		System.out.println("日志---------------》删除缓存并且更新数据库的数据");
		
		System.out.println("------------------------广告内容更新后台线程-----------------");
	}
	
	@Override
	public long getContentId() {
		
		return content.getId();
	}

}
