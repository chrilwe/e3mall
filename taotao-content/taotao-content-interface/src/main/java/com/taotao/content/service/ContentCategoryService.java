package com.taotao.content.service;

import com.taotao.content.model.ContentCategory;

public interface ContentCategoryService {
	public void addContentCategory(ContentCategory contentCategory);
	
	public void deleteContentCategory(long id);
	
	public void updateContentCategory(ContentCategory contentCategory);
	
	public ContentCategory findContentCategoryById(long id);
}
