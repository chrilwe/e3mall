package com.taotao.content.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taotao.content.mapper.ContentCategoryMapper;
import com.taotao.content.model.ContentCategory;
import com.taotao.content.service.ContentCategoryService;
/**
 * 广告分类服务
 * @author Administrator
 *
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
	
	@Autowired
	private ContentCategoryMapper contentCategoryMapper;
	
	/**
	 * 添加记录
	 */
	@Override
	@Transactional
	public void addContentCategory(ContentCategory contentCategory) {
		contentCategoryMapper.addContentCategory(contentCategory);
	}
	
	/**
	 * 删除记录
	 */
	@Override
	@Transactional
	public void deleteContentCategory(long id) {
		contentCategoryMapper.deleteContentCategory(id);
	}
	
	/**
	 * 更新记录
	 */
	@Override
	@Transactional
	public void updateContentCategory(ContentCategory contentCategory) {
		contentCategoryMapper.updateContentCategory(contentCategory);
	}
	
	/**
	 * 根据id查询记录
	 */
	@Override
	public ContentCategory findContentCategoryById(long id) {
		
		return contentCategoryMapper.findContentCategoryById(id);
	}

}
