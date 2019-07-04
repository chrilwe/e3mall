<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<table class="easyui-datagrid" id="orderRecordList" title="订单列表" 
       data-options="singleSelect:false,collapsible:true,pagination:true,url:'/manager/getOrderRecordList',method:'get',pageSize:30,toolbar:toolbar">
    <thead>
        <tr>
        	<th data-options="field:'ck',checkbox:true"></th>
        	<th data-options="field:'id',width:60">商品ID</th>
            <th data-options="field:'editor',width:200">修改人</th>
            <th data-options="field:'creater',width:100">下订单人</th>
            <th data-options="field:'merchantOrderNo',width:100">商家订单号</th>
            <th data-options="field:'orderAmount',width:100">变更金额</th>
            <th data-options="field:'cancelReason',width:100">取消订单原因</th>
            <th data-options="field:'trxNo',width:100">事务号</th>
            <th data-options="field:'cancelReason',width:100">消费者id</th>
            <th data-options="field:'cancelReason',width:100">消费者名称</th>
            <th data-options="field:'remark',width:100">备注</th>
            <th data-options="field:'status',width:60,align:'center',formatter:E3.formatItemStatus">订单状态</th>
            <th data-options="field:'createTime',width:130,align:'center',formatter:E3.formatDateTime">创建日期</th>
            <th data-options="field:'editTine',width:130,align:'center',formatter:E3.formatDateTime">更新日期</th>
        </tr>
    </thead>
</table>

</div>
