<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>淘淘商城后台管理系统</title>
<link rel="stylesheet" type="text/css" href="/js/jquery-easyui-1.4.1/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="/js/jquery-easyui-1.4.1/themes/icon.css" />
<link rel="stylesheet" type="text/css" href="/css/e3.css" />
<link rel="stylesheet" type="text/css" href="/css/default.css" />
<script type="text/javascript" src="/js/jquery-easyui-1.4.1/jquery.min.js"></script>
<script type="text/javascript" src="/js/jquery-easyui-1.4.1/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/js/jquery-easyui-1.4.1/locale/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" href="/js/ztree/zTreeStyle.css" type="text/css" />
<script src="/js/ztree/jquery.ztree.all-3.5.js" type="text/javascript"></script>
<script type="text/javascript" src="/js/common.js"></script>

<style type="text/css">
	.content {
		padding: 10px 10px 10px 10px;
	}
</style>
</head>
<body class="easyui-layout">
    <!-- 头部标题 -->
	<div data-options="region:'north',border:false" style="height:85px; padding:5px; background:url('./images/header_bg.png') no-repeat right;"> 
		<span class="northTitle">淘淘商城后台管理系统</span>
	    <span class="loginInfo">登录用户：${usr.username }&nbsp;&nbsp;姓名：${user.username }&nbsp;&nbsp;角色：系统管理员</span>
	    
	    <span style="border: 10px;margin-left: 1000px;"><a href="javascript:void(0)" id="mb" class="easyui-menubutton" data-options="menu:'#mm',iconCls:'icon-edit'">系统设置</a> 
	    	<div id="mm" style="width:200px;"> 
				<div id="logout" data-options="iconCls:'icon-cancel'">注销</div>
				<div id="password" data-options="iconCls:'icon-remove'">修改密码</div> 
				<div class="menu-sep"></div>
				<div id="admin" data-options="iconCls:'icon-help'">联系管理员</div> 
	    	</div>
	    </span>
	    
	</div>
    <div data-options="region:'west',title:'菜单',split:true" style="width:200px;">
    	<ul id="menu" class="ztree" style="margin-top: 10px;margin-left: 5px;"></ul>
         	
    </div>
    <div data-options="region:'center',title:''">
    	<div id="tabs" class="easyui-tabs">
		    <div title="首页" style="padding:20px;background:#fafafa;">
		        <table>
		        	<tr>
		        		<td>
		        			<div id="p1" class="easyui-panel" title="系统公告" style="width:1100px;height:300px;padding:10px;background:#fafafa" data-options="iconCls:'icon-edit',closable:true, collapsible:true,border:true,maximizable:true">
		        				<form action="/manager/notice.action">
		        					<textarea id="notice" rows="15" cols="175"></textarea>
		        				</form>
		        			</div>
		        		</td>
		        		
		        	</tr>
		        	<tr>
		        		<td>
		        			<div id="p1" class="easyui-panel" title="意见反馈" style="width:1100px;height:300px;padding:10px;background:#fafafa" data-options="iconCls:'icon-edit',closable:true, collapsible:true,border:true,maximizable:true">
		        				<form id="bugForm" method="post">
		        					<textarea name="advise" rows="15" cols="175" style="color: gray;">输入你的建议或者系统遇到的bug</textarea><br>
		        					<input id="btn1" type="submit" style="cursor: pointer;">
		        				</form>
		        			</div>
		        		</td>
		        	</tr>
		        </table>
		    </div>
		</div>
    </div>
    <!-- 页脚信息 -->
	<div data-options="region:'south',border:false" style="height:20px; background:#F3F3F3; padding:2px; vertical-align:middle;">
		<span id="sysVersion">系统版本：V3.0</span>
	    <span id="nowTime"></span>
	</div>
	
	<!-- 修改密码窗口 -->
	<div id="passwordWindow" class="easyui-window" title="修改密码" style="width:300px;height:150px" data-options="iconCls:'icon-save',modal:true,closed:true"> 
		<form id="passwordForm">
			<table>
				<tr>
					<td>旧密码</td>
					<td><input id="oldPassword" name="oldPassword" type="password" class="easyui-validatebox" data-options="required:true" /></td>
				</tr>
				<tr>
					<td>新密码</td>
					<td><input id="newPassword" name="newPassword" type="password" class="easyui-validatebox" data-options="required:true" /></td>
				</tr>
				<tr>
					<td>确认密码</td>
					<td><input id="newPassword1" name="newPassword1" type="password" class="easyui-validatebox" required="required" validType="equals['#newPassword']" /></td>
				</tr>
			</table>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<a id="btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'">确认</a>
		</form>
	</div>
	
<script type="text/javascript">
$(function(){
	var setting = {
		data : {
			simpleData : {
				enable : true
			}
		},
		callback : {
			onClick : onClick
		}
	};
	
	$.ajax({
			url : '/manager/menu.action',
			type : 'POST',
			dataType : 'text',
			success : function(data) {
				var zNodes = eval("(" + data + ")");
				$.fn.zTree.init($("#menu"), setting, zNodes);
			},
			error : function(msg) {
				alert('菜单加载异常!');
			}
	});
	
	//--------------------------------选项卡---------------------------------------
	function onClick(event, treeId, treeNode, clickFlag) {
		//返回的节点中，有展示选项卡的地址，才会添加选项卡
		if(treeNode.page != undefined && treeNode.page != "") {
			var content = '<div style="width:100%;height:100%;overflow:hidden;">'
					+ '<iframe src="'
					+ treeNode.page
					+ '" scrolling="auto" style="width:100%;height:100%;border:0;" ></iframe></div>';
			//判断选项卡是否已经存在了
			if($("#tabs").tabs('exists', treeNode.name)) {
				//存在选项卡了，更新选项卡
				$('#tabs').tabs('select', treeNode.name); // 切换tab
				var tab = $('#tabs').tabs('getSelected'); 
				$('#tabs').tabs('update', {
				    tab: tab,
				    options: {
				        title: treeNode.name,
				        content: content
				    }
				});
			} else {
				// 开启一个新的tab页面
				$('#tabs').tabs('add', {
					title : treeNode.name,
					bodyCls:"content",
					closable : true,
					href : treeNode.page
				});
			}
		}
	}
	//--------------------------------选项卡---------------------------------------
	
	//---消息窗口
	$.messager.show({  	
	  title:'消息',  	
	  msg: '欢迎你登录淘淘商城后台管理中心',
	  width : 250,
	  height : 200,  	
	  timeout:10000,  	
	  showType:'slide',
	  showSpeed:1000 
	});
	
	$("#logout").click(function(data){
		$.messager.confirm('Confirm','确定退出?',function(r){
			if(r) {
				$.post('/manager/logout.action',function(data){
					if(data.status == 200) {
						location.href = "http://login.ngrok.xiaomiqiu.cn/user/loginPage";
					}
				});
			}
		});
	});
	
	//公告信息
	$.post('/manager/notice.action',function(data){
		if(data.status == 200) {
			var message = data.data;
			$("#notice").html(message);
		}
	});
	
	//联系管理员
	$("#admin").click(function(){
		$.messager.alert('联系管理员','管理员邮箱地址：1129864619@qq.com');
	});
	
	//修改密码
	$("#password").click(function(){
		//打开窗口
		$("#passwordForm").form('clear');
		$("#passwordWindow").window('open');
		//当确认密码失去焦点时，判断密码是否相同
		$("#newPassword1").blur(function(){
			$.extend($.fn.validatebox.defaults.rules, { 
				equals: { 				
					validator: function(value,param){ 					
					return value == $(param[0]).val(); 			
				}, 				
				message: '输入密码不一致.' 				
				} 		
			}); 		
		});
		$("#btn").click(function(){
			var isValidate = $("#passwordForm").form("validate");
			if(isValidate) {
				$.post('/manager/password/update.action',$("#passwordForm").serialize(),function(data){
					if(data.status == 200) {
						$.messager.confirm('Confirm','修改密码成功',function(r){
							if(r) {
								location.href = "http://login.ngrok.xiaomiqiu.cn/user/loginPage";
							}
						});
					} else {
						$.messager.alert('错误','修改密码失败','info');
					}
				});
			}
			
		});
	});
	
	//提交bug表单
	$("#btn1").click(function(){
		$.post('/manager/advise.action',$("#bugForm").serialize(),function(data){
			if(data.status == 200) {
				$("#bugForm").form('clear');
				$.messager.alert('消息','提交建议成功','info');
			}
		});
	});
});
setInterval("document.getElementById('nowTime').innerHTML=new Date().toLocaleString()+' 星期'+'日一二三四五六'.charAt(new Date().getDay());",1000);
</script>
</body>
</html>