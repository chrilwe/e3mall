<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<table class="easyui-datagrid" id="orderList" title="订单列表" 
       data-options="singleSelect:false,collapsible:true,pagination:true,url:'/manager/getOrderList.action',method:'get',pageSize:30,toolbar:toolbar">
    <thead>
        <tr>
        	<th data-options="field:'ck',checkbox:true"></th>
        	<th data-options="field:'id',width:60">商品ID</th>
            <th data-options="field:'editor',width:200">修改人</th>
            <th data-options="field:'creater',width:100">下订单人</th>
            <th data-options="field:'merchantOrderNo',width:100">商家订单号</th>
            <th data-options="field:'orderAmount',width:100">支付金额</th>
            <th data-options="field:'cancelReason',width:100">取消订单原因</th>
            <th data-options="field:'remark',width:100">备注</th>
            <th data-options="field:'status',width:60,align:'center',formatter:E3.formatItemStatus">订单状态</th>
            <th data-options="field:'createTime',width:130,align:'center',formatter:E3.formatDateTime">创建日期</th>
            <th data-options="field:'editTine',width:130,align:'center',formatter:E3.formatDateTime">更新日期</th>
        </tr>
    </thead>
</table>

</div>
<script>

    function getSelectionsIds(){
    	var itemList = $("#orderList");
    	var sels = itemList.datagrid("getSelections");
    	var ids = [];
    	for(var i in sels){
    		ids.push(sels[i].id);
    	}
    	ids = ids.join(",");
    	return ids;
    }
    
    var toolbar = [{
        text:'删除',
        iconCls:'icon-cancel',
        handler:function(){
        	var ids = getSelectionsIds();
        	if(ids.length == 0){
        		$.messager.alert('提示','未选中订单!');
        		return ;
        	}
        	$.messager.confirm('确认','确定删除ID为 '+ids+' 的订单吗？',function(r){
        	    if (r){
        	    	var params = {"ids":ids};
                	$.post("/order/delete.action",params, function(data){
            			if(data.status == 200){
            				$.messager.alert('提示','删除成功!',undefined,function(){
            					$("#orderList").datagrid("reload");
            				});
            			}
            		});
        	    }
        	});
        }
    }];
</script>