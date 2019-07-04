<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<table class="easyui-datagrid" id="messageList" title="商品列表" 
       data-options="singleSelect:false,collapsible:true,pagination:true,url:'/message/dead/list.action',method:'get',pageSize:30,toolbar:toolbar">
    <thead>
        <tr>
        	<th data-options="field:'ck',checkbox:true"></th>
        	<th data-options="field:'messageId',width:60">消息ID</th>
            <th data-options="field:'editor',width:200">修改者</th>
            <th data-options="field:'creater',width:100">创建者</th>
            <th data-options="field:'createTime',width:130,align:'center',formatter:E3.formatDateTime">创建日期</th>
            <th data-options="field:'editTime',width:130,align:'center',formatter:E3.formatDateTime">修改日期</th>
            <th data-options="field:'messageBody',width:100">消息</th>
            <th data-options="field:'messageDataType',width:100">消息数据类型</th>
            <th data-options="field:'consumerQueue',width:100">消息队列通道名称</th>
            <th data-options="field:'messageSendTimes',width:100">消息发送次数</th>
            <th data-options="field:'areadlyDead',width:100">是否死亡</th>
            <th data-options="field:'status',width:100">状态</th>
            <th data-options="field:'remark',width:100">备注</th>
            <th data-options="field:'field1',width:100">field1</th>
        </tr>
    </thead>
</table>
<div id="itemEditWindow" class="easyui-window" title="编辑商品" data-options="modal:true,closed:true,iconCls:'icon-save',href:'/rest/page/item-edit'" style="width:80%;height:80%;padding:10px;">
</div>
<script>

    function getSelectionsIds(){
    	var messageList = $("#messageList");
    	var sels = messageList.datagrid("getSelections");
    	var ids = [];
    	for(var i in sels){
    		ids.push(sels[i].messageId);
    	}
    	ids = ids.join(",");
    	return ids;
    }
    
    var toolbar = ['-',{
        text:'重新确认并发送处理',
        iconCls:'icon-remove',
        handler:function(){
        	var ids = getSelectionsIds();
        	if(ids.length == 0){
        		$.messager.alert('提示','未选中数据!');
        		return ;
        	}
        	$.messager.confirm('确认','重新确认并发送消息？',function(r){
        	    if (r){
        	    	var params = {"ids":ids};
                	$.post("/message/dead/handler.action",params, function(data){
            			if(data.status == 200){
            				$.messager.alert('提示','下架商品成功!',undefined,function(){
            					$("#messageList").datagrid("reload");
            				});
            			}
            		});
        	    }
        	});
        }
    }];
</script>