package com.taotao.cart.dao;

import java.util.List;

import com.taotao.cart.model.Cart;

public interface CartDao {
	public void addCart2Redis(Cart cart, long userId);//将购物车添加到redis中
	
	public void deleteCartFromRedis(long userId, long itemId);//将购物车的宝贝从redis中删除
	
	public void deleteAllCartByuserId(long userId);//清空购物车
	
	public void updateCartFromRedis(List<Cart> carts);//更新购物车信息
	
	public List<Cart> findCartFromRedis(long userId);//根据用户id查询购物车
	
	public Cart findCartFromRedisByUserIdAndItemId(long userId, long itemId);//根据 商品id和用户id查询
}
