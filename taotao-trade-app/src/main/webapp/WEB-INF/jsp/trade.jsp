<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>支付二维码</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	     <link rel="stylesheet" type="text/css" href="/css/jquery.alerts.css" />
<link rel="stylesheet" type="text/css" href="/css/headerfooterindex.css" />
<link rel="stylesheet" type="text/css" href="/css/login.css" />
<script type="text/javascript" src="/js/jquery-1.5.1.min.js"></script>
<script type="text/javascript" src="/js/jquery.cookie.js"></script>
<script type="text/javascript" src="/js/jquery.alerts.js"></script>
<script type="text/javascript" src="/js/png.js"></script>
<script type="text/javascript" src="/js/cas.login.js"></script>
<script type="text/javascript" src="/js/capsLock.js"></script>
  </head>
  
  <body>
  	
    <img alt="支付二维码" src="${image}" align="middle">
    
 <!--    <script type="text/javascript">
    
    	$(function() {
    		var orderId = "${orderId}";
    		
    		runPerSec(orderId);
    	});
    	
    	function runPerSec(orderId) {
    		setTimeout(runPerSec,1000);
    		
    		$.post("/trade/pay_status",{"orderId":orderId},function(data){
    			if(data.status == 200) {
    				location.href = "/trade/success";
    				return;
    			} 
    		});
    	}
    </script> -->
    
  </body>
</html>
