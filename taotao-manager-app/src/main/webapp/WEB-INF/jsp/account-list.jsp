<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<table class="easyui-datagrid" id="itemList" title="商品列表" 
       data-options="singleSelect:false,collapsible:true,pagination:true,url:'/account/list.action',method:'get',pageSize:30">
    <thead>
        <tr>
        	<th data-options="field:'ck',checkbox:true"></th>
        	<th data-options="field:'accountNo',width:60">记账账号</th>
             <th data-options="field:'createTime',width:130,align:'center',formatter:E3.formatDateTime">创建日期</th>
            <th data-options="field:'editTime',width:130,align:'center',formatter:E3.formatDateTime">修改日期</th>
            <th data-options="field:'status',width:100">记账状态</th>
            <th data-options="field:'totalIncome',width:70">总收入</th>
            <th data-options="field:'totalExpend',width:70">总支出</th>
            <th data-options="field:'todayIncome',width:100">今日总收入</th>
            <th data-options="field:'todayExpend',width:100">今日总支出</th>
            <th data-options="field:'userNo',width:100">用户ID</th>
            <th data-options="field:'remark',width:100">备注</th>
        </tr>
    </thead>
</table>
</div>