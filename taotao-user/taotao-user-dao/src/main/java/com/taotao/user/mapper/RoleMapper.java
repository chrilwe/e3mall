package com.taotao.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.taotao.user.model.Role;

public interface RoleMapper {
	public void add(Role role);
	public void delete(String roleId);
	public void update(Role role);
	public Role findById(String roleId);
	public List<Role> findRoles();
	public List<Role> queryRoles(@Param("page")int page, @Param("size")int size);
	public int count();
}
