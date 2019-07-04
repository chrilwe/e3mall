package com.taotao.order.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.mengyun.tcctransaction.api.TransactionContext;

import com.taotao.order.model.Order;
import com.taotao.order.model.OrderItem;
import com.taotao.order.model.OrderPageBean;
import com.taotao.order.model.OrderRecord;
import com.taotao.order.model.OrderShipping;

public interface OrderService {
	
	public Order findOrderById(String orderId);
	
	public OrderRecord findOrderRecordById(String id);
	
	public void addOrderRecord(OrderRecord orderRecord);
	
	public void addOrder(Order order, OrderRecord orderRecord, List<OrderItem> orderItems);
	
	public Order findOrderByUserId(long userId, String status);
	
	public OrderRecord findOrderRecordByTrxNo(String messageId);
	
	public List<OrderItem> findOrderItemByOrderId(String orderId);
	
	public void updateOrderStatusByOrderId(String orderId, Date editTime, String status);
	
	public void updateOrderStatusByOrderId(String orderId, Date editTime, String status, String cancelReason);
	
	public void updateOrderStatusAndUpdatePointAccountByTcc(Map<String, String> params);
	
	public OrderPageBean findOrdersByPageAndSize(int page, int size);
	
	public void deleteOrders(String ids);
	
	public OrderPageBean findOrderRecordsByPageAndSize(int page, int size);
	
	public int countNoPayOrder(String status);
	
	public List<Order> findNoPayOrderByPageAndSize(String status, int page, int size);
	
	public void refundOrder(String cancelReason, String orderId, String status, Date editTime, String isRefund, int refundTimes, double refuundAmount);
	
}
