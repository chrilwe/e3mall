<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>我的订单</title>
<link rel="stylesheet" type="text/css"
	href="/js/jquery-easyui-1.4.1/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css"
	href="/js/jquery-easyui-1.4.1/themes/icon.css" />
<link rel="stylesheet" type="text/css" href="/css/e3.css" />
<link rel="stylesheet" type="text/css" href="/css/default.css" />
<script type="text/javascript"
	src="/js/jquery-easyui-1.4.1/jquery.min.js"></script>
<script type="text/javascript"
	src="/js/jquery-easyui-1.4.1/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="/js/jquery-easyui-1.4.1/locale/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" href="/js/ztree/zTreeStyle.css" type="text/css" />
<script src="/js/ztree/jquery.ztree.all-3.5.js" type="text/javascript"></script>
<script type="text/javascript" src="/js/common.js"></script>
</head>

<body class="easyui-layout"> 

	<!-- 头部标题 -->
	<div data-options="region:'north',border:false" style="height:85px; padding:5px; background:url('/images/456.jpg');"> 
		<span class="northTitle">我的订单明细表</span>
	    <span class="loginInfo" ><h3>当前登录用户：admin</h3></span>
	    
	</div>
	
	<!-- 页脚信息 -->
	<div data-options="region:'south',border:false" style="height:85px; background:url('/images/456.jpg'); padding:2px; vertical-align:middle;">
		
	</div>
	
	<div data-options="region:'east'" style="width:10px;"></div> 
	
	<div data-options="region:'west'" style="width:10px;"></div> 
	
	<div data-options="region:'center'" style="padding:5px;">
		<table class="easyui-datagrid" id="userOrderList"
       data-options="singleSelect:true,collapsible:true,pagination:true,url:'/order/getUserOrderList.action',method:'get',pageSize:30,fit:true,toolbar:toolbar">
		    <thead>
		        <tr>
		        	<th data-options="field:'ck',checkbox:true"></th>
		        	<th data-options="field:'merchantOrderNo',width:150,align:'center'">商家订单号</th>
		            <th data-options="field:'editor',width:100,align:'center'">修改人</th>
		            <th data-options="field:'creater',width:100,align:'center'">下订单人</th>
		            <th data-options="field:'orderAmount',width:100">支付金额</th>
		            <th data-options="field:'remark',width:60,align:'center'">备注</th>
		            <th data-options="field:'status',width:100,align:'center'">订单状态</th>
		            <th data-options="field:'createTime',width:130,align:'center',formatter:E3.formatDateTime">创建日期</th>
		            <th data-options="field:'editTime',width:130,align:'center',formatter:E3.formatDateTime">更新日期</th>
		            <th data-options="field:'cancelReason',width:300,align:'center'">取消订单原因</th>
		        </tr>
		    </thead>
		</table>
	</div>
	
	<div id="cancelReasonWin" class="easyui-window" title="取消订单理由" style="width:400px;height:300px" data-options="iconCls:'icon-save',modal:true,closed:true"> 
		<form id="cancelReasonForm" method="post">
			<input type="hidden" name="orderId" id="orderId">
			取消订单原因:</br>
			<input name="reason1" type="checkbox" value="timeout" alt="配送时间过长">配送时间过长</br>
			<input name="reason2" type="checkbox" value="noSend">没有送达</br>
			<input name="reason3" type="checkbox" value="errorOrder">下错订单</br>
			其他原因:</br>
			<textarea name="reason4" rows="10" cols="58"></textarea>
			<input id="submitReason" type="button" value="提交">
		</form>
	</div>
	
	
	<div id="refundReasonWin" class="easyui-window" title="退款理由" style="width:400px;height:300px" data-options="iconCls:'icon-save',modal:true,closed:true"> 
		<form id="refundReasonForm" method="post">
			<input type="hidden" name="orderId" id="refundOrderId">
			取消订单原因:</br>
			<input name="reason1" type="checkbox" value="timeout" alt="配送时间过长">配送时间过长</br>
			<input name="reason2" type="checkbox" value="noSend">没有送达</br>
			<input name="reason3" type="checkbox" value="errorOrder">下错订单</br>
			其他原因:</br>
			<textarea name="reason4" rows="10" cols="58"></textarea>
			<input id="btn1" type="button" value="提交申请">
		</form>
	</div>

</body>
<script type="text/javascript">
 function getSelectionsIds(){
    	var orderList = $("#userOrderList");
    	var sels = orderList.datagrid("getSelections");
    	var ids = [];
    	for(var i in sels){
    		ids.push(sels[i].id);
    	}
    	ids = ids.join(",");
    	return ids;
 }
 var toolbar = [{
        text:'取消订单',
        iconCls:'icon-cancel',
        handler:function(){
        	var order = $("#userOrderList").datagrid("getSelections");
        	if(order.length <= 0) {
        		$.messager.alert('消息','确认需要取消的订单','info');
        	} else {
        		$.messager.confirm('Confirm','确定取消订单?', function(r){
        			if(r) {
        				$("#cancelReasonForm").form('clear');
        				$("#cancelReasonWin").window('open');
        				
        				var orderId = order[0].merchantOrderNo;
        				$("#orderId").val(orderId);
        			}
        		});
        	}
        }
    },{
    	text:'去支付',
        iconCls:'icon-edit',
        handler:function(){
        	var r = $("#userOrderList").datagrid("getSelections");
        	if(r.length <= 0) {
        		$.messager.alert('消息','选择支付的订单','info');
        	} else {
        		var orderId = r[0].merchantOrderNo;
        		alert(orderId);
        		location.href = "http://localhost:9900/trade/payment?orderId="+orderId;
        	}
        }
    },{
        text:'申请退款',
        iconCls:'icon-cancel',
        handler:function(){
        	var order = $("#userOrderList").datagrid("getSelections");
        	if(order.length <= 0) {
        		$.messager.alert('消息','选择要退款的订单','info');
        	} else {
        		$("#refundReasonForm").form('clear');
        		$("#refundReasonWin").window('open');
        		var orderId = order[0].merchantOrderNo;
        		$("#refundOrderId").val(orderId);
        	}
        }
    }]
  $(function(){
  	$("#submitReason").click(function(){
  		$.ajax({
			url : '/order/cancelOrder.action',
			type : 'POST',
			data : $("#cancelReasonForm").serialize(),
			success : function(data) {
				if(data.status == 200) {
					$("#cancelReasonWin").window('close');
	  				$.messager.alert("消息","取消订单成功","info");
	  				$("#userOrderList").datagrid("reload");
	  			} else {
	  				$.messager.alert("消息","取消订单失败:"+data.msg,"info");
	  			}
			},
			error : function(msg) {
				$("#cancelReasonWin").window('close');
				$.messager.alert('消息','网络异常，订单取消失败','info');
			}
		});
		
		
  		/* $.post('/order/cancelOrder.action',$("#cancelReasonForm").serialize(),function(data){
  			if(data.status == 200) {
  				$.messager.alert("消息","取消订单成功","info");
  				$("#userOrderList").datagrid("reload");
  			} else {
  				$.messager.alert("消息","取消订单失败:"+data.msg,"info");
  			}
  		}); */
  	});
  	
  	$("#btn1").click(function(){
		
			$.ajax({
				url : '/order/refund.action',
				type : 'POST',
				data : $("#refundReasonForm").serialize(),
				success : function(data) {
					if(data.status == 200) {
						$("#refundReasonWin").window('close');
		  				$.messager.alert("消息","退款申请成功,等待商家同意","info");
		  				$("#userOrderList").datagrid("reload");
		  			} else {
		  				$.messager.alert("消息","退款失败:"+data.msg,"info");
		  			}
				},
				error : function(msg) {
					$("#refundReasonWin").window('close');
					$.messager.alert('消息','网络异常，退款失败','info');
				}
			});
		});
  });
</script>

</html>
