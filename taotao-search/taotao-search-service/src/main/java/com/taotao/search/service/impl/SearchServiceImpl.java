package com.taotao.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.search.dao.SearchDao;
import com.taotao.search.model.SearchModel;
import com.taotao.search.model.SearchResultModel;
import com.taotao.search.service.SearchService;
/**
 * 搜索服务
 * @author Administrator
 *
 */
@Service
public class SearchServiceImpl implements SearchService {
	
	@Autowired
	private SearchDao searchDao;
	
	@Value("${DEFAULT_FIELD}")
	private String DEFAULT_FIELD;
	
	/**
	 * 添加商品到搜索库中
	 */
	@Override
	public void addItem(SearchModel searchModel) {
		searchDao.addItem(searchModel);
	}

	/**
	 * 从搜索库中删除商品
	 */
	@Override
	public void deleteItem(String id) {
		searchDao.deleteItem(id);
	}
	
	/**
	 * 从搜索库中查询商品
	 */
	@Override
	public SearchResultModel findItemBySearchEnginer(String keywords, Integer page, Integer rows) {
		//封装查询条件
		SolrQuery solrQuery = createSolrQuery(keywords, page, rows);
		
		//从搜索库中查询
		SearchResultModel result = searchDao.findItemBySearchEnginer(solrQuery);
		result.setTotalPages(Integer.parseInt(String.valueOf(result.getRecordCount() % rows == 0 ? result.getRecordCount()/rows : result.getRecordCount()/rows + 1)));
		return result;
	}
	
	/**
	 * 封装查询条件
	 * @param keywords
	 * @return
	 */
	private SolrQuery createSolrQuery(String keywords, Integer page, Integer rows) {
		//创建一个solrquery对象
		SolrQuery solrQuery = new SolrQuery();
		
		//添加查询条件
		solrQuery.setQuery(keywords);
		//从第几条记录开始
		solrQuery.setStart((page - 1) * rows);
		//分页每页的大小
		solrQuery.setRows(rows);
		//设置默认域
		solrQuery.set("df", DEFAULT_FIELD);
		//设置高亮显示
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("item_title");
		solrQuery.setHighlightSimplePre("<em style=\"color:red\">");
		solrQuery.setHighlightSimplePost("</em>");

		return solrQuery;
	}
	
	/**
	 * 批量删除
	 */
	@Override
	public void deleteItems(String ids) {
		String[] split = ids.split(",");
		for (String id : split) {
			searchDao.deleteItem(id);
		}
	}
	
	/**
	 * 批量添加接口
	 */
	@Override
	public void addItems(List<SearchModel> searchModels) {
		for (SearchModel searchModel : searchModels) {
			addItem(searchModel);
		}
	}

}
