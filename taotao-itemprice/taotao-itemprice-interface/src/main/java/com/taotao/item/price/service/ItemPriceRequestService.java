package com.taotao.item.price.service;

import com.taotao.item.price.model.ItemPrice;

/**
 * 商品价格服务
 * @author Administrator
 *
 */
public interface ItemPriceRequestService {
	public void updateItemPrice(long itemId, long price);//更新价格
	
	public ItemPrice findItemPriceByItemId(long itemId);//根据商品id查询商品价格 
}
