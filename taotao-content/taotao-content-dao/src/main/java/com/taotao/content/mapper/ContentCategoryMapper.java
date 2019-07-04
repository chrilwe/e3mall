package com.taotao.content.mapper;

import com.taotao.content.model.ContentCategory;

public interface ContentCategoryMapper {
	public void addContentCategory(ContentCategory contentCategory);
	
	public void deleteContentCategory(long id);
	
	public void updateContentCategory(ContentCategory contentCategory);
	
	public ContentCategory findContentCategoryById(long id);
}
