package com.taotao.coupon.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.taotao.coupon.model.Coupon;

public interface CouponMapper {
	public void addCoupon(Coupon coupon);
	public void deleteCouponById(int id);
	public void updateCoupon(Coupon coupon);
	public List<Coupon> findCouponsByUserId(@Param("userId")long userId, @Param("status")String status);
}
