package com.taotao.search.service.impl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.taotao.search.model.SearchModel;
import com.taotao.search.service.SearchService;

public class Test {
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		SearchService bean = context.getBean(SearchService.class);
		
		/*SearchModel searchModel = new SearchModel();
		searchModel.setId("2");
		searchModel.setCategoryName("分类");
		searchModel.setImage("图片");
		searchModel.setPrice(1l);
		searchModel.setSellPoint("卖点");
		searchModel.setTitle("标题");
		bean.addItem(searchModel );*/
		
		System.out.println(bean.findItemBySearchEnginer("标题", 1, 10));
		
	}
}
