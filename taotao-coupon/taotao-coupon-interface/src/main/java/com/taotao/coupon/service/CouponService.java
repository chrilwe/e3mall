package com.taotao.coupon.service;

import java.util.List;

import com.taotao.coupon.model.Coupon;

public interface CouponService {
	public void addCoupon(Coupon coupon);
	public void deleteCouponById(int id);
	public void updateCoupon(Coupon coupon);
	public List<Coupon> findCouponsByUserId(long userId, String status);
}
