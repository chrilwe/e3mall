package com.taotao.order.mapper;

import java.util.List;

import com.taotao.order.model.OrderItem;

public interface OrderItemMapper {
	public void addOrderItem(OrderItem orderItem);
	
	public void deleteOrderItemById(long id);
	
	public void updateOrderItem(OrderItem orderItem);
	
	public List<OrderItem> findOrderItemByOrderId(String orderId);
}
