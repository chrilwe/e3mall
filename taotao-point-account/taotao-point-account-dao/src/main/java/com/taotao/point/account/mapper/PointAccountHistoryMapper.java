package com.taotao.point.account.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.taotao.point.account.model.PointAccountHistory;

public interface PointAccountHistoryMapper {
	/**
	 * 增加记录
	 */
	public void addPointAccountHistory(PointAccountHistory pointAccountHistory);
	
	/**
	 * 删除记录
	 */
	public void deletePointAccountHistory(String id);
	
	/**
	 * 更新记录
	 */
	public void updatePointAccountHistory(PointAccountHistory pointAccountHistory);
	
	/**
	 * 根据requestNo更新状态
	 */
	public void updatePointAccountHistoryStatusEditTimeByRequestNo(@Param("requestNo")String requestNo,
			@Param("status")String status,@Param("editTime")Date editTime);
	
	/**
	 * 更具id查询记录
	 */
	public PointAccountHistory findPointAccountHistoryById(String id);
	
	/**
	 * 根据userNo查询记录
	 */
	public PointAccountHistory findPointAccountHistoryByRequestNo(String requestNo);
	
	/**
	 * 根据用户id分页查询积分记录
	 */
	public List<PointAccountHistory> queryByUserId(@Param("page")int page, @Param("size")int size, @Param("userId")String userId);
	
	/**
	 * 统计指定用户的积分历史记录
	 */
	public int countPointHistory(String userId);
}
