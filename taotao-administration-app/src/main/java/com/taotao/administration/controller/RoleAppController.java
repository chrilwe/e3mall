package com.taotao.administration.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.administration.utils.TaotaoUtils;
import com.taotao.user.model.Role;
import com.taotao.user.model.UserPageBean;
import com.taotao.user.service.RoleService;

/**
 * 角色管理
 * @author Administrator
 *
 */
@Controller
public class RoleAppController {
	
	@Autowired
	private RoleService roleService;
	
	/**
	 * 查询所有角色
	 * @return
	 */
	@RequestMapping("/manager/role/list")
	@ResponseBody
	public List<Role> findRoles() {
		
		return roleService.findRoles();
	}
	
	/**
	 * 角色关联权限
	 * @param roleId 角色id
	 * @param ids 权限id串
	 * @return
	 */
	@RequestMapping("/manager/role-function")
	@ResponseBody
	public TaotaoUtils roleFunction(String roleId, String ids) {
		try {
			/**
			 * 幂等性判断 ：判断当前的角色是否已经关联了当前的权限
			 */
			roleService.roleFunctions(roleId, ids);
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoUtils.build(501, "角色关联权限失败");
		}
		
		return TaotaoUtils.ok();
	}
	
	/**
	 * 用户关联角色
	 */
	@RequestMapping("/manager/user-role")
	@ResponseBody
	public TaotaoUtils userRole(String roleId, String userId) {
		try {
			roleService.userRole(userId, roleId);
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoUtils.build(502, "用户关联角色失败");
		}
		
		return TaotaoUtils.ok();
	}
	
	/**
	 * 角色分页查询
	 */
	@RequestMapping("/manager/getRoleList")
	@ResponseBody
	public UserPageBean queryRoles(int page, int rows) {
		
		return roleService.queryRoles((page - 1) * rows, rows);
	}
	
	/**
	 * 添加角色
	 * @param role
	 * @return
	 */
	@RequestMapping("/manager/role/add")
	@ResponseBody
	public TaotaoUtils addRole(Role role) {
		role.setId(UUID.randomUUID().toString());
		try {
			roleService.addRole(role);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return TaotaoUtils.ok();
	}
	
	/**
	 * 批量删除角色
	 * @param ids
	 * @return
	 */
	@RequestMapping("/manager/role/delete")
	@ResponseBody
	public TaotaoUtils deleteRoles(String ids) {
		roleService.deleteRoles(ids);
		return TaotaoUtils.ok();
	}
}
