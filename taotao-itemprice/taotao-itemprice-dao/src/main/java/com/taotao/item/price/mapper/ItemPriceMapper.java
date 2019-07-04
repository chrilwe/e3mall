package com.taotao.item.price.mapper;

import org.apache.ibatis.annotations.Param;

import com.taotao.item.price.model.ItemPrice;

public interface ItemPriceMapper {
	public void addItemPrice(ItemPrice itemPrice);
	
	public void deleteItemPriceByItemId(long itemId);
	
	public void updateItemPrice(ItemPrice itemPrice);
	
	public void updateItemPriceByItemId(@Param("itemId")long itemId, @Param("price")long price);
	
	public ItemPrice findItemPriceByItemId(long itemId);
}
