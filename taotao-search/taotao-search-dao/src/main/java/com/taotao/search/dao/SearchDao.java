package com.taotao.search.dao;

import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;

import com.taotao.search.model.SearchModel;
import com.taotao.search.model.SearchResultModel;

public interface SearchDao {
	public void addItem(SearchModel searchModel);//商品上架
	
	public void deleteItem(String id);//商品下架
	
	public SearchResultModel findItemBySearchEnginer(SolrQuery solrQuery);//搜索引擎查找商品
}
