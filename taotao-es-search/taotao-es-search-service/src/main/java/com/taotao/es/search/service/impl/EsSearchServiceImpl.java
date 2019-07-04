package com.taotao.es.search.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.es.search.dao.EsSearchDao;
import com.taotao.es.search.model.Comment;
import com.taotao.es.search.model.EsSearchResultModel;
import com.taotao.es.search.model.ResultModel;
import com.taotao.es.search.service.EsSearchService;
/**
 * es搜索服务
 * @author Administrator
 *
 */
@Service
public class EsSearchServiceImpl implements EsSearchService {
	
	@Autowired
	private EsSearchDao esSearchDao;

	@Override
	public void add(EsSearchResultModel model, String itemType, String itemIndex) throws Exception {
		esSearchDao.addDocument(model, itemType, itemIndex);
	}

	@Override
	public void delete(long id, String itemType, String itemIndex) throws Exception {
		esSearchDao.deleteDocumentById(id, itemType, itemIndex);
	}

	@Override
	public void update(EsSearchResultModel model, String itemType, String itemIndex) throws Exception {
		esSearchDao.updateDocument(model, itemType, itemIndex);
	}

	@Override
	public EsSearchResultModel findById(long id, String itemType, String itemIndex) throws Exception {
		
		return esSearchDao.findDocumentById(id, itemType, itemIndex);
	}

	@Override
	public ResultModel queryString(String keyword, int from, int size, int status, String itemIndex)
			throws Exception {
		
		return esSearchDao.queryString(keyword, from, size, status, itemIndex);
	}

	@Override
	public ResultModel queryType(String itemType, int from, int size, int status, String itemIndex)
			throws Exception {
		
		return esSearchDao.queryType(itemType, from, size, status, itemIndex);
	}

	@Override
	public void updateItemStatus(String itemIndex, String itemType, int status,
			long itemId) throws Exception {
		esSearchDao.updateItemStatus(itemIndex, itemType, status, itemId);
	}

	@Override
	public void add(Comment comment, String commentType, String commentIndex)
			throws Exception {
		esSearchDao.addDocument(comment, commentType, commentIndex);
	}
	
	/**
	 * 降序排序
	 */
	@Override
	public List<Comment> findCommentsOrderByDate(String commentIndex,
			String commentType) throws Exception {
		
		return esSearchDao.findCommentsOrderByDate(commentIndex, commentType);
	}

}
