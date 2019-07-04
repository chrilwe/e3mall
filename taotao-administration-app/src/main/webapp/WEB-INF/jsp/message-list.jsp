<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<table class="easyui-datagrid" id="itemList" title="商品列表" 
       data-options="singleSelect:false,collapsible:true,pagination:true,url:'/message/list.action',method:'get',pageSize:30,toolbar:toolbar">
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
