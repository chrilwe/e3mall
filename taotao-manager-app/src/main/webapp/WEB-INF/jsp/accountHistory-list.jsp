<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<table class="easyui-datagrid" id="itemList" title="商品列表" 
       data-options="singleSelect:false,collapsible:true,pagination:true,url:'/account/history/list.action',method:'get',pageSize:30,toolbar:toolbar">
    <thead>
        <tr>
        	<th data-options="field:'ck',checkbox:true"></th>
        	<th data-options="field:'accountNo',width:60">记账账号</th>
             <th data-options="field:'createTime',width:130,align:'center',formatter:E3.formatDateTime">创建日期</th>
            <th data-options="field:'editTime',width:130,align:'center',formatter:E3.formatDateTime">修改日期</th>
            <th data-options="field:'amount',width:100">交易总金额</th>
            <th data-options="field:'requestNo',width:100">请求号</th>
            <th data-options="field:'bankTrxNo',width:100">银行事务号</th>
            <th data-options="field:'status',width:100">状态</th>
            <th data-options="field:'userNo',width:100">用户ID</th>
        </tr>
    </thead>
</table>
