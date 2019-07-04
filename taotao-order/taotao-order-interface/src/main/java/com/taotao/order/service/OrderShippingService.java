package com.taotao.order.service;

import com.taotao.order.model.OrderShipping;

/**
 * 物流配送服务系统
 * @author Administrator
 *
 */
public interface OrderShippingService {
	public void addOrderShipping(OrderShipping orderShipping);
	
	public void deleteOrderShippingById(long orderId);
	
	public void updateOrderShipping(OrderShipping orderShipping);
	
	public OrderShipping findOrderShippingById(long orderId);
}
