package com.taotao.user.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taotao.user.mapper.RoleFunctionMapper;
import com.taotao.user.mapper.RoleMapper;
import com.taotao.user.mapper.UserRoleMapper;
import com.taotao.user.model.Role;
import com.taotao.user.model.UserPageBean;
import com.taotao.user.service.RoleService;
/**
 * 角色管理服务
 * @author Administrator
 *
 */
@Service
public class RoleServiceImpl implements RoleService {
	
	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private RoleFunctionMapper roleFunctionMapper;
	@Autowired
	private UserRoleMapper userRoleMapper;

	/**
	 * 查询所有角色
	 */
	@Override
	public List<Role> findRoles() {
		
		return roleMapper.findRoles();
	}
	
	/**
	 * 角色关联权限
	 */
	@Override
	@Transactional
	public void roleFunctions(String roleId, String functionIds) {
		//解析权限id
		String[] functionIdArray = functionIds.split(",");
		for (String functionId : functionIdArray) {
			roleFunctionMapper.roleFinctions(roleId, functionId);
		}
	}
	
	/**
	 * 用户关联角色
	 */
	@Override
	@Transactional
	public void userRole(String userId, String roleId) {
		userRoleMapper.userRole(userId, roleId);
	}
	
	/**
	 * 分页查询角色
	 */
	@Override
	public UserPageBean queryRoles(int page, int rows) {
		List<Role> roles = roleMapper.queryRoles(page, rows);
		int total = roleMapper.count();
		
		UserPageBean userPageBean = new UserPageBean();
		userPageBean.setRows(roles);
		userPageBean.setTotal(total);
		return userPageBean;
	}
	
	/**
	 * 添加角色
	 */
	@Override
	@Transactional
	public void addRole(Role role) {
		roleMapper.add(role);
	}
	
	/**
	 * 删除角色
	 */
	@Override
	@Transactional
	public void deleteRole(String roleId) {
		//删除角色
		//删除角色权限关联
		//删除角色用户关联
		roleMapper.delete(roleId);
		roleFunctionMapper.delete(roleId);
		userRoleMapper.deleteByRoleId(roleId);
	}

	@Override
	public void deleteRoles(String ids) {
		String[] idsArray = ids.split(",");
		for (String roleId : idsArray) {
			deleteRole(roleId);
		}
	}
	
	/**
	 * 用户id查询角色
	 */
	@Override
	public List<Role> findRoleByUserId(String userId) {
		List<Role> roles = new ArrayList<Role>();
		List<String> roleIds = userRoleMapper.findRoleIdsByUserId(userId);
		for (String roleId : roleIds) {
			Role role = roleMapper.findById(roleId);
			roles.add(role);
		}
		return roles;
	}


}
