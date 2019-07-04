package com.taotao.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.taotao.user.model.UserRole;

public interface UserRoleMapper {
	public void add(UserRole userRole);
	public void delete(String userId);
	public void deleteByRoleId(String roleId);
	public List<String> findRoleIdsByUserId(String userId);
	public void userRole(@Param("userId")String userId, @Param("roleId")String roleId);//用户关联角色
}
