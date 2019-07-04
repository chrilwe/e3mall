<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'north',split:true" style="height:400px;">
		<table class="easyui-datagrid" id="functionList" title="权限列表" 
	       data-options="singleSelect:false,collapsible:true,pagination:true,url:'/manager/getFunctionList.action',method:'get',pageSize:30,toolbar:toolbar1,fit:true">
	    	<thead>
	        	<tr>
		        	<th data-options="field:'ck',checkbox:true"></th>
		        	<th data-options="field:'id',width:60">权限ID</th>
		            <th data-options="field:'name',width:200">菜单名称</th>
		            <th data-options="field:'code',width:200">菜单简称</th>
		            <th data-options="field:'page',width:200">权限访问地址</th>
		            <th data-options="field:'isMenu',width:200">是否为菜单</th>
		            <th data-options="field:'description',width:200">权限描述</th>
	        	</tr>
	    	</thead>
		</table>
	</div> 
	<div data-options="region:'center',split:true" style="height:320px;">
		<table class="easyui-datagrid" id="roleList" title="角色列表" 
	       data-options="singleSelect:false,collapsible:true,pagination:true,url:'/manager/getRoleList.action',method:'get',pageSize:30,toolbar:toolbar3,fit:true">
	    	<thead>
	        	<tr>
		        	<th data-options="field:'ck',checkbox:true"></th>
		        	<th data-options="field:'id',width:60">角色ID</th>
		            <th data-options="field:'name',width:200">角色名称</th>
		            <th data-options="field:'code',width:200">角色简称</th>
		            <th data-options="field:'description',width:200">角色描述</th>
	        	</tr>
	    	</thead>
		</table>
	</div>
	<div data-options="region:'south',split:true" style="height:500px;">
		<table class="easyui-datagrid" id="userList" title="用户列表" 
	       data-options="singleSelect:true,collapsible:true,pagination:true,url:'/manager/getUserList.action',method:'get',pageSize:30,toolbar:toolbar2,fit:true">
	    	<thead>
	        	<tr>
		        	<th data-options="field:'ck',checkbox:true"></th>
		        	<th data-options="field:'id',width:200">用户ID</th>
		            <th data-options="field:'username',width:200">用户账号</th>
		            <th data-options="field:'phone',width:200">手机号码</th>
		            <th data-options="field:'email',width:200">邮件地址</th>
	        	</tr>
	    	</thead>
		</table>
	</div> 
</div>
<!-- ===============添加权限窗口================================== -->
<div id="addFunctionWindow" class="easyui-window" title="添加菜单/权限" style="width:520px;height:300px" data-options="iconCls:'icon-save',modal:true,closed:true"> 
	<div class="easyui-layout" data-options="fit:true"> 
		<form id="addFunctionForm" method="post">
			<table>
				<tr>
					<td>菜单名称</td>
					<td><input id="" name="name" class="easyui-validatebox" style="height: 25px;width: 300px" data-options="required:true"></td>	
				</tr>
				<tr>
					<td>菜单简称</td>
					<td><input id="" name="code" class="easyui-validatebox" style="height: 25px;width: 300px" data-options="required:true"></td>
				</tr>
				<tr>
					<td>权限访问地址</td>
					<td><input id="" name="page" class="easyui-validatebox" style="height: 25px;width: 300px" data-options="required:true"></td>
				</tr>
				<tr>
					<td>是否为菜单</td>
					<td>是<input name="isMenu" class="easyui-validatebox" type="checkbox" value="1" alt="是" data-options="required:true"/>否<input class="easyui-validatebox" type="checkbox" value="0" alt="否" data-options="required:true"/></td>
				</tr>
				<tr>
					<td>权限描述</td>
					<td><input name="description" class="easyui-validatebox" id="" style="height: 25px;width: 300px" data-options="required:true"></td>
				</tr>
				<tr>
					<td>所属分支(不选则默认为主菜单)</td>
					<td><input id="pId" name="pId"></td>
				</tr>
				<tr>
					<td><a id="btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a></td>
				</tr>
			</table>
		 </form>
		</div> 
	</div> 
</div>

<!-- ===============添加权限窗口================================== -->

<!-- ===============关联角色窗口================================== -->
<div id="roleFunctionWindow" class="easyui-window" title="角色赋予权限" style="width:450px;height:300px" data-options="iconCls:'icon-save',modal:true,closed:true"> 
	<input id="roleFunction" name="roleId">
</div>
<!-- ===============关联角色窗口================================== -->


<!-- 用户添加窗口 -->
<div id="addUserWindow" class="easyui-window" title="添加用户" style="width:430px;height:300px" data-options="iconCls:'icon-save',modal:true,closed:true"> 
	<div class="easyui-layout" data-options="fit:true"> 
		<form id="addUserForm" method="post">
			<table>
				<tr>
					<td>用户账号</td>
					<td><input name="username" type="text" class="easyui-validatebox" style="height: 25px;width: 300px" data-options="required:true"/></td>
				</tr>
				<tr>
					<td>用户密码</td>
					<td><input name="password" type="password" class="easyui-validatebox" style="height: 25px;width: 300px" data-options="required:true"></td>
				</tr>
				<tr>
					<td>手机号</td>
					<td><input name="phone" type="text" class="easyui-validatebox" style="height: 25px;width: 300px" data-options="required:true"/></td>
				</tr>
				<tr>
					<td>邮箱地址</td>
					<td><input name="phone" type="text" class="easyui-validatebox" style="height: 25px;width: 300px" data-options="required:true"/></td>
				</tr>
				<tr>
					<td><a id="userBtn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a></td>
				</tr>
			</table>
		</form>
	</div>
</div>


<!-- 用户关联角色窗口 -->
<div id="userRoleWindow" class="easyui-window" title="用户关联角色" style="width:450px;height:300px" data-options="iconCls:'icon-save',modal:true,closed:true"> 
	<input id="UserRole" name="roleId">
</div>


<!-- 角色添加窗口 -->
<div id="addRoleWindow" class="easyui-window" title="添加角色" style="width:520px;height:300px" data-options="iconCls:'icon-save',modal:true,closed:true"> 
	<div class="easyui-layout" data-options="fit:true"> 
		<form id="addRoleForm" method="post">
			<table>
				<tr>
					<td>角色名称</td>
					<td><input id="" name="name" class="easyui-validatebox" style="height: 25px;width: 300px" data-options="required:true"></td>	
				</tr>
				<tr>
					<td>角色简称</td>
					<td><input id="" name="code" class="easyui-validatebox" style="height: 25px;width: 300px" data-options="required:true"></td>
				</tr>
				<tr>
					<td>角色描述</td>
					<td><input id="" name="page" class="easyui-validatebox" style="height: 25px;width: 300px" data-options="required:true"></td>
				</tr>
				<tr>
					<td><a id="roleBtn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a></td>
				</tr>
			</table>
		 </form>
		</div> 
	</div> 
</div>
<script>

    function getSelectionsIds(){
    	var itemList = $("#functionList");
    	var sels = itemList.datagrid("getSelections");
    	var ids = [];
    	for(var i in sels){
    		ids.push(sels[i].id);
    	}
    	ids = ids.join(",");
    	return ids;
    }
    
    var toolbar1 = [{
        text:'添加菜单/权限',
        iconCls:'icon-add',
        handler:function(){
        	$("#addFunctionForm").form('clear');//清空表单数据
        	$("#addFunctionWindow").window('open');//打开窗口
        	//初始化checkbox获取主菜单id
        	$('#pId').combobox({ 
					url:'/manager/parentId/list.action', 
					valueField:'id', 
					textField:'name',
					onSelect : function(param){
						$('#pId').combobox('setValue', param.id); //将下拉列表值设置为pId
					}
			});
        }
    },{
    	text : '权限关联角色',
    	iconCls : 'icon-edit',
    	handler : function() {
    		//关联之前，先要选定至少条数据
    		var functionIds = $("#functionList").datagrid('getSelections');
    		var ids = [];
	    	for(var i in functionIds){
	    		ids.push(functionIds[i].id);
	    	}
	    	ids = ids.join(",");
    		if(functionIds.length > 0) {
    			//打开角色窗口
    			$("#roleFunctionWindow").window('open');
    			//角色下拉列表
    			var roleId = "";
		    	$('#roleFunction').combobox({ 
					url:'/manager/role/list.action', 
					valueField:'id', 
					textField:'name',
					onSelect : function(param) {
						roleId = param.id;
						//将roleId和functionIds发送到后台
						$.post('/manager/role-function.action',{'roleId':roleId,'ids':ids}, function(data) {
							if(data.status == 200) {
								$("#roleFunctionWindow").window('close');
								$.messager.alert('消息','角色关联权限成功','info');
							}
						});
					} 
				});
				
    		} else {
    			$.messager.alert('信息','至少要选择一条数据','info');
    		}
    	}
    },{
    	text : '删除',
    	iconCls : 'icon-cancel',
    	handler : function() {
    		//关联之前，先要选定至少条数据
    		var functionIds = $("#functionList").datagrid('getSelections');
    		
	    	if(functionIds.length > 0) {
	    		$.messager.confirm('Confirm','是否删除',function(r) {
	    			if(r) {
	    				var ids = [];
				    	for(var i in functionIds){
				    		ids.push(functionIds[i].id);
				    	}
				    	ids = ids.join(",");
	    				$.ajax({
							url : '/manager/function/delete.action',
							dataType : 'json',
							type : "GET",
							data : {'ids':ids},
							success : function(data){
								if(data.status == 200) {
			    					$.messager.alert('消息','删除成功','info');
			    				} else {
			    					$.messager.alert('消息','删除失败','info');
			    				}
							},
							error : function() {
								$.messager.alert('消息','系统异常，删除失败','info');
							}
						});
	    			}
	    		});
	    	} else {
	    		$.messager.alert('消息','请选择你要删除的数据','info');
	    	}
    	}
    }];
    
    var toolbar2 = [{
    	text:'添加用户',
        iconCls:'icon-add',
        handler:function(){
        	$("#addUserForm").form('clear');
        	$("#addUserWindow").window('open');
        	
        }
    },{
    	text:'用户关联角色',
        iconCls:'icon-add',
        handler:function(){
        	//关联之前，先要选定至少条数据
    		var userId = $("#userList").datagrid('getSelections');
    		
        	if(userId.length > 0) {
        		$("#userRoleWindow").Window('open');
	        	$('#userRole').combobox({ 
						url:'/manager/role/list.action', 
						valueField:'id', 
						textField:'name',
						onSelect : function(param) {
							roleId = param.id;
							
							$.post('/manager/user-role.action',{'roleId':roleId,'userId':userId[0]}, function(data) {
								if(data.status == 200) {
									$("#userRoleWindow").window('close');
									$.messager.alert('消息','用户关联角色成功','info');
								}
							});
						} 
					});
        	} else {
        		$.messager.alert('消息','请选择一条数据关联角色', 'info');
        	}
        }
    },{
    	text : '删除',
    	iconCls : 'icon-cancel',
    	handler : function() {
    		//关联之前，先要选定至少条数据
    		var userIds = $("#userList").datagrid('getSelections');
    		
	    	if(userIds.length > 0) {
	    		$.messager.confirm('Confirm','是否删除',function(r) {
	    			if(r) {
	    				var ids = [];
				    	for(var i in userIds){
				    		ids.push(userIds[i].id);
				    	}
				    	ids = ids.join(",");
	    				$.ajax({
							url : '/manager/user/delete.action',
							dataType : 'json',
							type : "GET",
							data : {'ids':ids},
							success : function(data){
								if(data.status == 200) {
			    					$.messager.alert('消息','删除成功','info');
			    				} else {
			    					$.messager.alert('消息','删除失败','info');
			    				}
							},
							error : function() {
								$.messager.alert('消息','系统异常，删除失败','info');
							}
						});
	    			}
	    		});
	    	} else {
	    		$.messager.alert('消息','请选择你要删除的数据','info');
	    	}
    	}
    }];
    
    var toolbar3 = [{
    	text:'添加角色',
        iconCls:'icon-add',
        handler:function(){
        	$("#addRoleForm").form('clear');
        	$("#addRoleWindow").window('open');
        	
        }
    },{
    	text : '删除',
    	iconCls : 'icon-cancel',
    	handler : function() {
    		//关联之前，先要选定至少条数据
    		var roleIds = $("#roleList").datagrid('getSelections');
    		
	    	if(roleIds.length > 0) {
	    		$.messager.confirm('Confirm','是否删除',function(r) {
	    			if(r) {
	    				var ids = [];
				    	for(var i in roleIds){
				    		ids.push(roleIds[i].id);
				    	}
				    	ids = ids.join(",");
	    				$.ajax({
							url : '/manager/role/delete.action',
							dataType : 'json',
							type : "GET",
							data : {'ids':ids},
							success : function(data){
								if(data.status == 200) {
			    					$.messager.alert('消息','删除成功','info');
			    				} else {
			    					$.messager.alert('消息','删除失败','info');
			    				}
							},
							error : function() {
								$.messager.alert('消息','系统异常，删除失败','info');
							}
						});
	    			}
	    		});
	    	} else {
	    		$.messager.alert('消息','请选择你要删除的数据','info');
	    	}
    	}
    }]
    
    $(function() {
    	//保存按钮
    	$("#btn").click(function() {
    		//校验表单
    		var isValidate = $("#addFunctionForm").form('validate');
    		if(isValidate) {
    			//校验通过，提交表单
    			$.ajax({
					url : '/manager/function/add.action',
					dataType : 'json',
					type : "POST",
					data : $("#addFunctionForm").serialize(),
					success : function(data){
						if(data.status == 200) {
	    					//并关闭窗口，弹出添加成功
	    					$("#addFunctionWindow").window('close');
	    					$.messager.alert('添加成功','添加权限成功','info');
	    					//重新加载datagrid
	    					$("#functionList").datagrid('reload');
	    				}
					},
					error : function() {
						$.messager.alert('添加失败','系统异常，添加权限失败','info');
					}
				});
    			
    		}
    	});
    	
    	
		//添加用户按钮
		$("#userBtn").click(function() {
			var validate = $("#addUserForm").form('validate');
			if(validate) {
				$.ajax({
					url : '/manager/user/add.action',
					dataType : 'json',
					type : "POST",
					data : $("#addUserForm").serialize(),
					success : function(data){
						if(data.status == 200) {
	    					//并关闭窗口，弹出添加成功
	    					$("#addUserWindow").window('close');
	    					$.messager.alert('添加成功','添加用户成功','info');
	    					//重新加载datagrid
	    					$("#userList").datagrid('reload');
	    				}
					},
					error : function() {
						$.messager.alert('添加失败','系统异常，添加用户失败','info');
					}
				});
			}
		});
		
		
		//添加角色按钮
		$("#roleBtn").click(function() {
			var validate = $("#addRoleForm").form('validate');
			if(validate) {
				$.ajax({
					url : '/manager/role/add.action',
					dataType : 'json',
					type : "POST",
					data : $("#addRoleForm").serialize(),
					success : function(data){
						if(data.status == 200) {
	    					//并关闭窗口，弹出添加成功
	    					$("#addRoleWindow").window('close');
	    					$.messager.alert('添加成功','添加角色成功','info');
	    					//重新加载datagrid
	    					$("#roleList").datagrid('reload');
	    				}
					},
					error : function() {
						$.messager.alert('添加失败','系统异常，添加角色失败','info');
					}
				});
			}
		});
    	
    });
</script>