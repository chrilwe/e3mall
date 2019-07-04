package com.taotao.search.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.taotao.search.dao.SearchDao;
import com.taotao.search.model.SearchModel;
import com.taotao.search.model.SearchResultModel;
/**
 * 搜索引擎数据层操作
 * @author Administrator
 *
 */
@Repository
public class SearchDaoImpl implements SearchDao {
	
	@Autowired
	private SolrServer solrServer;
	
	@Value("${ID}")
	private String ID;
	@Value("${ITEM_TITLE}")
	private String ITEM_TITLE;
	@Value("${ITEM_SELL_POINT}")
	private String ITEM_SELL_POINT;
	@Value("${ITEM_CATEGORY_NAME}")
	private String ITEM_CATEGORY_NAME;
	@Value("${ITEM_PRICE}")
	private String ITEM_PRICE;
	@Value("${ITEM_IMAGE}")
	private String ITEM_IMAGE;
	
	
	/**
	 * 商品添加到搜索库中
	 */
	@Override
	public void addItem(SearchModel searchModel) {
		try {
			SolrInputDocument document = new SolrInputDocument();
			document.addField(ID,searchModel.getId());
			document.addField(ITEM_TITLE, searchModel.getTitle());
			document.addField(ITEM_SELL_POINT, searchModel.getSellPoint());
			document.addField(ITEM_CATEGORY_NAME, searchModel.getCategoryName());
			document.addField(ITEM_IMAGE, searchModel.getImage());
			document.addField(ITEM_PRICE, searchModel.getPrice());
			
			solrServer.add(document);
			solrServer.commit();
			
			System.out.println("日志============》将商品添加到搜索库中，" + searchModel);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("异常===========》添加商品到搜索库异常:" + e);
		}
	}
	
	/**
	 * 删除搜索库中的商品
	 */
	@Override
	public void deleteItem(String id) {
		try {
			solrServer.deleteById(id);
			solrServer.commit();
			System.out.println("日志===============》从搜索库中删除商品,商品id=" + id);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("异常=========》从搜索库中删除商品异常:" + e);
		}
	}
	
	/**
	 * 根据查询条件查找搜索库中的商品
	 */
	@Override
	public SearchResultModel findItemBySearchEnginer(SolrQuery solrQuery) {
		List<SearchModel> searchModels = new ArrayList<SearchModel>();
		SearchResultModel resultModel = new SearchResultModel();
		try {
			QueryResponse response = solrServer.query(solrQuery);
			
			//普通查询
			SolrDocumentList documentList = response.getResults();
			
			System.out.println("日志===============》查询到相关记录" + documentList.getNumFound() + "条");
			if(documentList.getNumFound() > 0) {
				//高亮查询显示结果
				Map<String, Map<String, List<String>>> highlightingResult = response.getHighlighting();
				for (SolrDocument solrDocument : documentList) {
					String id = (String) solrDocument.getFieldValue(ID);
					
					//标题高亮
					String itemTitle = "";
					List<String> result = highlightingResult.get(solrDocument.get(ID)).get(ITEM_TITLE);
					if(result != null && result.size() > 0) {
						itemTitle = result.get(0);
					} else {
						itemTitle = (String) solrDocument.getFieldValue(ITEM_TITLE);
					}
					
					String itemSellPoint = (String) solrDocument.getFieldValue(ITEM_SELL_POINT);
					String itemCategoryName = (String) solrDocument.getFieldValue(ITEM_CATEGORY_NAME);
					String itemImage = (String) solrDocument.getFieldValue(ITEM_IMAGE);
					Long itemPrice = (Long) solrDocument.getFieldValue(ITEM_PRICE);
					
					SearchModel searchModel = new SearchModel();
					searchModel.setId(id);
					searchModel.setCategoryName(itemCategoryName);
					searchModel.setImage(itemImage);
					searchModel.setPrice(itemPrice);
					searchModel.setSellPoint(itemSellPoint);
					searchModel.setTitle(itemTitle);
					
					searchModels.add(searchModel);
				}	
			}
			resultModel.setItemList(searchModels);
			resultModel.setRecordCount(documentList.getNumFound());
		} catch (SolrServerException e) {
			e.printStackTrace();
			System.out.println("异常=======》查询搜索库异常");
			return null;
		}
		return resultModel;
	}

}
