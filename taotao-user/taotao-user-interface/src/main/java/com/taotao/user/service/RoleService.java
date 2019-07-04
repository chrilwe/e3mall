package com.taotao.user.service;

import java.util.List;

import com.taotao.user.model.Role;
import com.taotao.user.model.UserPageBean;

public interface RoleService {
	//查询所有角色信息
	public List<Role> findRoles();
	
	//角色关联权限
	public void roleFunctions(String roleId, String functionIds);
	
	//用户关联角色
	public void userRole(String userId, String roleId);
	
	//分页查询角色
	public UserPageBean queryRoles(int page, int rows);
	
	//添加角色 
	public void addRole(Role role);
	
	//删除角色
	public void deleteRole(String roleId);
	
	//批量删除角色
	public void deleteRoles(String ids);
	
	//根据用户id查询角色
	public List<Role> findRoleByUserId(String userId);
}
