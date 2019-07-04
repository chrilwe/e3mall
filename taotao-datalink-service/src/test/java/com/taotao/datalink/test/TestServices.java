package com.taotao.datalink.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.taotao.datalink.hystrix.GetItemInfoByIdCommand;
import com.taotao.itemservice.model.Item;
import com.taotao.itemservice.service.ItemInfoService;
import com.taotao.itemservice.service.ItemPropertyService;
import com.taotao.itemservice.service.ItemSpecificationService;

public class TestServices {
	public static void main(String[] args) {
		/*ItemInfoService bean = context.getBean(ItemInfoService.class);
		System.out.println(bean.getItemInfoById(536563l));*/
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/springmvc.xml","classpath:spring/applicationContext-*.xml");
		ItemSpecificationService bean = context.getBean(ItemSpecificationService.class);
		ItemPropertyService bean2 = context.getBean(ItemPropertyService.class);
		System.out.println(bean.findItemSpecificationById(536563l));
		System.out.println(bean2.findItemPropertyById(536563l));
	}
}
