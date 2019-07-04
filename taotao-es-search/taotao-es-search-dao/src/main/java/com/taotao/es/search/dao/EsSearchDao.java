package com.taotao.es.search.dao;

import java.io.IOException;
import java.util.List;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;

import com.taotao.es.search.model.Comment;
import com.taotao.es.search.model.EsSearchResultModel;
import com.taotao.es.search.model.ResultModel;

public interface EsSearchDao {
	public IndexResponse addDocument(EsSearchResultModel model, String itemType, String itemIndex) throws Exception;
	public DeleteResponse deleteDocumentById(long id, String itemType, String itemIndex) throws Exception;
	public UpdateResponse updateDocument(EsSearchResultModel model, String itemType, String itemIndex) throws Exception;
	public EsSearchResultModel findDocumentById(long id, String itemType, String itemIndex) throws Exception;
	public ResultModel queryString(String keyword, int from, int size, int status, String itemIndex) throws Exception;//关键字搜索
	public ResultModel queryType(String itemType, int from, int size, int status, String itemIndex) throws Exception;//根据商品类别查询
	public void updateItemStatus(String itemIndex, String itemType, int status, long itemId) throws Exception;
	public IndexResponse addDocument(Comment comment, String commentType, String commentIndex) throws Exception;
	public List<Comment> findCommentsOrderByDate(String commentIndex, String commentType) throws Exception;
}
