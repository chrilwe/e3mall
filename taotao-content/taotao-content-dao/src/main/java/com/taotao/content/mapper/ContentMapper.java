package com.taotao.content.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.taotao.content.model.Content;

public interface ContentMapper {
	public void addContent(Content content);
	
	public void deleteContent(long id);
	
	public void updateContent(Content content);
	
	public Content findContentById(long id);
	
	public List<Content> findContents(@Param("from")int from, @Param("to")int to);
	
	public int count();
	
	public List<Content> findContentsByCId(long contentCategoryId);
	
	public List<Long> findContentIdByCid(long contentCategoryId);
}
