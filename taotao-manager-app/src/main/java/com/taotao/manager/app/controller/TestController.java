package com.taotao.manager.app.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.manager.app.utils.TaotaoUtils;
import com.taotao.order.model.Order;

@Controller
public class TestController {
	
	@RequestMapping("/manager/menu")
	@ResponseBody
	public List<Menu> menu() {
		List<Menu> list = new ArrayList<Menu>();
		Menu menu1 = new Menu();
		menu1.setId("1");
		menu1.setName("商品管理");
		menu1.setpId("0");
		Menu menu2 = new Menu();
		menu2.setId("2");
		menu2.setName("新增商品");
		menu2.setPage("/item-add.action");
		menu2.setpId("1");
		Menu menu3 = new Menu();
		menu3.setId("3");
		menu3.setName("权限管理");
		menu3.setPage("/function-list.action");
		menu3.setpId("1");
		list.add(menu1);
		list.add(menu2);
		list.add(menu3);
		return list;
	}
	
	@RequestMapping("/{url}")
	public String show(@PathVariable("url")String url) {
		
		return url;
	}
	
	@RequestMapping("/manager/getFunctionList")
	@ResponseBody
	public PageBean functionList() {
		PageBean pageBean = new PageBean();
		List<Function> list = new ArrayList<Function>();
		Function function = new Function();
		function.setName("wwwww");
		list.add(function);
		pageBean.setRows(list);
		pageBean.setTotal(1);
		return pageBean;
	}
	
	@RequestMapping("/order/getUserOrderList")
	@ResponseBody
	public PageBean NoPayOrderList(int page, int rows) {
		Order order = new Order();
		order.setCancelReason("无理由");
		order.setCreater("小白");
		order.setCreateTime(new Date());
		order.setEditTime(new Date());
		order.setMerchantOrderNo("123");
		order.setOrderAmount(100l);
		List<Order> list = new ArrayList<Order>();
		list.add(order);
		
		PageBean pageBean = new PageBean();
		pageBean.setRows(list);
		pageBean.setTotal(1);
		
		return pageBean;
	}
	
	@RequestMapping("/order/cancelOrder")
	@ResponseBody
	public TaotaoUtils cancelOrder(@RequestParam(name = "reason1", defaultValue = "")String reason1, @RequestParam(name = "reason2", defaultValue = "")String reason2, @RequestParam(name = "reason3", defaultValue = "")String reason3,
			@RequestParam(name = "reason4", defaultValue = "")String reason4, String orderId) {
		//先判断该订单是否为未支付状态（幂等）
		
		//将订单取消
		String reasons = reason1 +"," + reason2 + "," + reason3 + "," + reason4;
		System.out.println("reason="+reasons);
		return TaotaoUtils.ok();
	}
}
