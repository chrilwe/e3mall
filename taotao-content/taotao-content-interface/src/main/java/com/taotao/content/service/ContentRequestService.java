package com.taotao.content.service;

import java.util.List;

import com.taotao.content.model.Content;

public interface ContentRequestService {
	public String updateContentRequest(Content content);
	
	public Content readContentRequest(long contentId);
	
	public List<Content> readContentsRequest(long categoryId);
}
