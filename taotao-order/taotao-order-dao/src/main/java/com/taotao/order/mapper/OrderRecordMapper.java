package com.taotao.order.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.taotao.order.model.OrderRecord;

public interface OrderRecordMapper {
	//添加记录
	public void addOrderRecord(OrderRecord orderRecord);
	
	//删除记录
	public void deleteOrderRecord(@Param("id")String id, @Param("status")String status);
	
	//修改记录
	public void updateOrderRecord(OrderRecord orderRecord);
	
	public void updateOrderRecord_(@Param("paySuccessTime")Date paySuccessTime,
			@Param("bankTrxNo")String bankTrxNo,@Param("bankReturnMsg")String bankReturnMsg,
			@Param("status")String status,@Param("bankOrderNo")String bankOrderNo);
	
	public void updateRefundMessage(@Param("orderId")String orderId,@Param("status")String status,@Param("editTime")Date editTime,@Param("isRefund")String isRefund,@Param("refundTimes")int refundTimes,@Param("refundAmount")double refuundAmount);
	
	//根据id查询记录
	public OrderRecord findOrderRecordById(String id);
	
	//根据订单号查询记录
	public OrderRecord findOrderRecordByBankOrderNo(String bankOrderNo);
	
	//根据事务号messageId查询订单记录
	public OrderRecord findOrderRecordByTrxNo(String messageId);
	
	//更新订单记录状态
	public void updateOrderRecordStatus(@Param("orderId")String orderId, 
			@Param("status")String status, @Param("editTime")Date editTime);
	
	//插入支付流水号，退款流程需要交易流水号
	public void updateOrderBankOrderNo(@Param("orderId")String orderId, @Param("bank_order_no")String bankOrderNo);
	
	//分页查询
	public List<OrderRecord> findOrderRecordsByPageAndSize(@Param("page")int page, @Param("size")int size);
	
	public int count();
}
