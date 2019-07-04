package com.taotao.order.mapper;

import com.taotao.order.model.OrderShipping;

public interface OrderShippingMapper {
	public void addOrderShipping(OrderShipping orderShipping);
	
	public void deleteOrderShippingById(long orderId);
	
	public void updateOrderShipping(OrderShipping orderShipping);
	
	public OrderShipping findOrderShippingById(long orderId);
}
