package com.taotao.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.taotao.user.model.RoleFunction;

public interface RoleFunctionMapper {
	public void add(RoleFunction roleFunction);
	public void delete(String roleId);
	public void deleteByFunctionId(String functionId);
	public List<String> findFunctionIdsByRoleId(String roleId);
	public List<String> findRoleIdsByFunctionId(String functionId);
	public void roleFinctions(@Param("roleId")String roleId, @Param("functionId")String functionId);//角色关联权限
}
