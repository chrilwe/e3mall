package com.taotao.order.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.taotao.order.model.Order;

public interface OrderMapper {
	public void insertOrder(Order order);
	
	public void deleteOrder(@Param("orderId")String orderId, @Param("status")String status);
	
	public void updateOrder(Order order);
	
	public void updateOrderStatusByOrderId(@Param("orderId")String orderId,
			@Param("editTime")Date editTime,@Param("status")String status);
	
	
	public void updateOrderStatusAndCancelReasonByOrderId(@Param("orderId")String orderId,
			@Param("editTime")Date editTime,@Param("status")String status, @Param("cancelReason")String cancelReason);
	
	public void updateRefundMessage(@Param("cancelReason")String cancelReason,@Param("orderId")String orderId,@Param("status")String status,@Param("editTime")Date editTime,@Param("isRefund")String isRefund,@Param("refundTimes")int refundTimes,@Param("refundAmount")double refuundAmount);
	
	public Order findOrderById(String orderId);
	
	public Order findOrderByUserId(@Param("userId")long userId, @Param("status")String status);
	
	public List<Order> findOrdersByPageAndSize(@Param("page")int page, @Param("size")int size);
	
	public int count();
	
	public int countNoPayOrder(String status);
	
	public List<Order> findNoPayOrderByPageAndSize(@Param("status")String status, @Param("page")int page, @Param("size")int size);
}
