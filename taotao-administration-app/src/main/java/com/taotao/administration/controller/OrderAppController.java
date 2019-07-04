package com.taotao.administration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.administration.utils.TaotaoUtils;
import com.taotao.order.model.Order;
import com.taotao.order.model.OrderPageBean;
import com.taotao.order.service.OrderService;

/**
 *  订单后台管理系统
 * @author Administrator
 *
 */
@Controller
public class OrderAppController {
	
	@Autowired
	private OrderService orderService;
	
	/**
	 * 分页查询订单信息
	 * @param page 当前页
	 * @param rows 页面大小
	 * @return
	 */
	@RequestMapping("/manager/getOrderList")
	@ResponseBody
	public OrderPageBean getOrderList(int page, int rows) {
		
		return orderService.findOrdersByPageAndSize((page - 1) * rows, rows);
	}
	
	/**
	 * 分页查询订单记录
	 * @param page 当前页
	 * @param size 页面大小
	 * @return
	 */
	@RequestMapping("/manager/getOrderRecordList")
	@ResponseBody
	public OrderPageBean getOrderRecordList(int page, int rows) {
		
		return orderService.findOrderRecordsByPageAndSize((page - 1) * rows, rows);
	}
	
	/**
	 * 删除订单
	 * @param ids 订单号字符串
	 * @return
	 */
	@RequestMapping("/order/delete")
	@ResponseBody
	public TaotaoUtils deleteOrder(String ids) {
		//调用订单服务系统，批量删除订单接口
		System.out.println("日志==================》删除订单号:ids="+ids);
		orderService.deleteOrders(ids);
		return TaotaoUtils.ok();
	}
}
