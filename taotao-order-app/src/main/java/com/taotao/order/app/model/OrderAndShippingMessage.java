package com.taotao.order.app.model;

import java.util.List;

import com.taotao.order.model.Order;
import com.taotao.order.model.OrderItem;
import com.taotao.order.model.OrderShipping;

/**
 * 封装order和shipping
 * @author Administrator
 *
 */
public class OrderAndShippingMessage {
	private Order order;
	private OrderShipping orderShipping;
	private List<OrderItem> orderItems;
	
	
	
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public OrderShipping getOrderShipping() {
		return orderShipping;
	}
	public void setOrderShipping(OrderShipping orderShipping) {
		this.orderShipping = orderShipping;
	}
	
}
