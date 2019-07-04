package com.taotao.content.service;

import java.util.List;

import com.taotao.content.model.Content;
import com.taotao.content.model.ContentPageBean;

public interface ContentService {
	public void addContent(Content content);
	
	public void deleteContent(long id);
	
	public void updateContent(Content content);
	
	public Content findContentById(long id);
	
	public ContentPageBean queryContents(int from, int to);
	
	public void deleteContents(String ids);
	
	public List<Content> findContentsByCid(long contentCategoryId);
}
