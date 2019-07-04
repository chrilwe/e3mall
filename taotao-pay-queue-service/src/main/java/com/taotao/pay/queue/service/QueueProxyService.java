package com.taotao.pay.queue.service;
/**
 * 消息队列系统动态代理接口
 * 执行主业务逻辑
 * @author Administrator
 *
 */
public interface QueueProxyService {
	public void createAccount(String messageId);//会计记账业务逻辑
	public void createOrder(String messageId);//创建订单业务逻辑
	public void addItemToEs(String messageId) throws NumberFormatException, Exception;//添加商品到搜索库中
	public void updateItemStatusFromEs(String messageId, int status) throws NumberFormatException, Exception;//更新搜索库中的商品状态
}
