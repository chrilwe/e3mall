package com.taotao.search.service;

import java.util.List;

import com.taotao.search.model.SearchModel;
import com.taotao.search.model.SearchResultModel;

public interface SearchService {
	public void addItem(SearchModel searchModel);//添加商品到搜索库
	
	public void addItems(List<SearchModel> searchModel);// 批量添加
	
	public void deleteItem(String id);//从搜索库中删除商品
	
	public void deleteItems(String ids);//批量删除
	
	public SearchResultModel findItemBySearchEnginer(String keywords, Integer page, Integer rows);//从搜索库中查找商品
}
