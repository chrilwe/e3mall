<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>我的积分</title>
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
	<div data-options="region:'north',border:false" style="height:85px; padding:5px; background:url('/images/456.jpg');"> 
		<span class="northTitle">用户积分明细</span>
	    <span class="loginInfo" ><h3>当前登录用户：${user.username} &nbsp;&nbsp; 积分余额 : <span id="pointAmount">${pointAmount}</span></h3></span>
	    
	</div>
   
    <div data-options="region:'center',title:''">
    	<div id="tabs" class="easyui-tabs">
		    <div title="用户积分明细记录" style="padding:10px;background:#fafafa; height: 360px">
		        <table class="easyui-datagrid" id="itemList" 
				       data-options="singleSelect:false,collapsible:true,pagination:true,url:'/point/getPointList.action?userId='+${u.id},method:'get',pageSize:30,fit:true">
				    <thead>
				        <tr>
				        	<th data-options="field:'amount',width:150,align:'center'">变动后积分总额</th>
				            <th data-options="field:'fundDirection',width:150,align:'center',formatter:E3.formatPointType">积分变更类型</th>
				            <th data-options="field:'balance',width:150,align:'center'">积分变更数额</th>
				            <th data-options="field:'editTime',width:130,align:'center',formatter:E3.formatDateTime">更新日期</th>
				            <th data-options="field:'remark',width:150,align:'center'">备注</th>
				        </tr>
				    </thead>
				</table>
		    </div>
		</div>
    </div>
    <!-- 页脚信息 -->
	<div data-options="region:'south',border:false" style="height:85px; background:url('/images/456.jpg'); padding:2px; vertical-align:middle;">
		
	</div>
	
</body>
</html>