var E3MALL = {
	checkLogin : function(){
		var _ticket = $.cookie("TAOTAO_TOKEN_");
		if(!_ticket){
			
			return ;
		}
		$.ajax({
			url : "http://login.e3mall.club:9200/user/token/" + _ticket,
			dataType : "jsonp",
			type : "GET",
			success : function(data){
				if(data.status == 200){
					var username = data.data.username;
					var html = username + "欢迎你登录淘淘商城<a id=\"logout\" href=\"http://login.e3mall.club:9200/user/logout\" class=\"link-logout\">[注销]</a>";
					$("#loginbar").html(html);
				}
			}
		});
	}
}

$(function(){
	// 判断是否登录
	E3MALL.checkLogin();
	
});