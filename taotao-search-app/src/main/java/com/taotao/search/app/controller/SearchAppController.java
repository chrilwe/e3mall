package com.taotao.search.app.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.es.search.model.EsSearchResultModel;
import com.taotao.es.search.model.ResultModel;
import com.taotao.es.search.service.EsSearchService;
import com.taotao.search.model.SearchModel;
import com.taotao.search.model.SearchResultModel;
import com.taotao.search.service.SearchService;

/**
 * 搜索服务
 * @author Administrator
 *
 */
@Controller
public class SearchAppController {
	
	/*@Autowired
	private SearchService searchService;*/
	@Autowired
	private EsSearchService esSearchService;
	
	@Value("${SEARCH_PAGE_SIZE}")
	private Integer SEARCH_PAGE_SIZE;
	@Value("${ITEM_STATUS}")
	private Integer ITEM_STATUS;
	@Value("${ITEM_INDEX}")
	private String ITEM_INDEX;
	
	/**
	 * 搜索商品(solr搜索引擎)
	 * @return
	 */
	/*@RequestMapping("/search")
	public String search(String keyword, Integer page, Model model) {
		
		*//**
		 * GET请求乱码解决
		 *//*
		try {
			keyword = new String(keyword.getBytes("iso-8859-1"),"UTF-8");
			System.out.println("日志============》搜索商品" + keyword);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			System.out.println("异常============》搜索关键字转码异常");
		}
		if(keyword == null) {
			return "error";
		}
		
		SearchResultModel result = searchService.findItemBySearchEnginer(keyword, page, SEARCH_PAGE_SIZE);
		List<SearchModel> itemList = result.getItemList();
		Long recordCount = result.getRecordCount();
		int totalPages = result.getTotalPages();
		
		*//**
		 * 多个图片，只需要展现一张图片
		 *//*
		for(SearchModel searchModel : itemList) {
			String images = searchModel.getImage();
			String[] imagesArray = images.split(",");
			searchModel.setImage(imagesArray[0]);
		} 
		
		model.addAttribute("itemList", itemList);
		model.addAttribute("recordCount", recordCount);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("query", keyword);
		
		return "search";
	}*/
	
	/**
	 * 搜索商品(es搜索引擎)
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/search")
	public String searchItem(String keyword, Integer page, Model model) throws Exception {
		keyword = new String(keyword.getBytes("iso-8859-1"), "UTF-8");
		//商品状态，0-未上架，1-上架，2-下架，3-删除
		ResultModel result = esSearchService.queryString(keyword, (page - 1) * SEARCH_PAGE_SIZE, SEARCH_PAGE_SIZE, ITEM_STATUS, ITEM_INDEX);
		
		//多张图片，取一张图片即可
		List<EsSearchResultModel> itemList = result.getItemList();
		for (EsSearchResultModel esSearchResultModel : itemList) {
			String images = esSearchResultModel.getImage();
			if(images != null && !images.equals("")) {
				String[] imagesArray = images.split(",");
				esSearchResultModel.setImage(imagesArray[0]);
			}
		}
		
		model.addAttribute("itemList", itemList);
		model.addAttribute("recordCount", result.getRecordCount());
		model.addAttribute("totalPages", result.getTotalPages());
		model.addAttribute("query", keyword);
		
		return "search";
	}
}
