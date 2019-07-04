package com.taotao.es.search.service;

import java.util.List;

import com.taotao.es.search.model.Comment;
import com.taotao.es.search.model.EsSearchResultModel;
import com.taotao.es.search.model.ResultModel;

public interface EsSearchService {
	public void add(EsSearchResultModel model, String itemType, String itemIndex) throws Exception;
	public void delete(long id, String itemType, String itemIndex) throws Exception;
	public void update(EsSearchResultModel model, String itemType, String itemIndex) throws Exception;
	public EsSearchResultModel findById(long id, String itemType, String itemIndex) throws Exception;
	public ResultModel queryString(String keyword, int from, int size, int status, String itemIndex) throws Exception;
	public ResultModel queryType(String itemType, int from, int size, int status, String itemIndex) throws Exception;
	public void updateItemStatus(String itemIndex, String itemType, int status, long itemId) throws Exception;
	public void add(Comment comment, String commentType, String commentIndex) throws Exception;
	public List<Comment> findCommentsOrderByDate(String commentIndex, String commentType) throws Exception;
}
