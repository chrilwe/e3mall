package com.taotao.order.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taotao.order.mapper.OrderShippingMapper;
import com.taotao.order.model.OrderShipping;
import com.taotao.order.service.OrderShippingService;
/**
 * 物流配送服务系统
 * @author Administrator
 *
 */
@Service
public class OrderShippingServiceImpl implements OrderShippingService {
	
	@Autowired
	private  OrderShippingMapper orderShippingMapper;
	
	/**
	 * 添加物流信息
	 */
	@Override
	@Transactional
	public void addOrderShipping(OrderShipping orderShipping) {
		orderShippingMapper.addOrderShipping(orderShipping);
	}
	
	/**
	 * 删除 物流信息
	 */
	@Override
	@Transactional
	public void deleteOrderShippingById(long orderId) {
		orderShippingMapper.deleteOrderShippingById(orderId);
	}
	
	/**
	 * 更新后物流信息
	 */
	@Override
	@Transactional
	public void updateOrderShipping(OrderShipping orderShipping) {
		orderShippingMapper.updateOrderShipping(orderShipping);
	}
	
	/**
	 * 根据orderId查找物流信息
	 */
	@Override
	public OrderShipping findOrderShippingById(long orderId) {
		
		return orderShippingMapper.findOrderShippingById(orderId);
	}

}
