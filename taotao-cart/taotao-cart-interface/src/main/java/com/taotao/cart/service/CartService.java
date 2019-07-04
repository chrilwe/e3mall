package com.taotao.cart.service;

import java.util.List;

import com.taotao.cart.model.Cart;

public interface CartService {
	public void addCart2Redis(Cart cart, long userId);//将购物车添加到redis中
	
	public List<Cart> findCartsByUserId(long userId);//查找全部购物车清单
	
	public void deleteAllCartByUserId(long userId);//清空购物车
	
	public void deleteCartByUserIdAndItemId(long userId, long itemId);//删除指定的宝贝
	
	public void deleteCartsByUserIdAndItemIds(String itemIds, long userId);//批量删除购物车中的宝贝
}
