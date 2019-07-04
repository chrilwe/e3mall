var E3MALL = {
	checkLogin : function(){
		var _ticket = $.cookie("TAOTAO_TOKEN_");
		if(!_ticket){
			
			return ;
		}
		$.ajax({
			url : "http://login.ngrok.xiaomiqiu.cn/user/token/" + _ticket,
			dataType : "jsonp",
			type : "GET",
			success : function(data){
				if(data.status == 200){
					var username = data.data.username;
					var html = username + "欢迎你登录淘淘商城<a id=\"logout\" href=\"http://login.ngrok.xiaomiqiu.cn:9200/user/logout\" class=\"link-logout\">[注销]</a>";
					$("#loginbar").html(html);
				}
			}
		});
	}
}

$(function(){
	$.post("/content/list", {}, function(data) {
		if(data.status == 200) {
			var html1 = "";
			var html2 = "";
			var contentList = data.data;
			
			for(var i = 0; i < contentList.length; i++) {
				html1 = html1 + '<li><a name="sfbest_hp_hp_focus_' + i + '" class="fore_pic trackref" href="' + contentList[i].url + '" target="_blank"><img id="lunbo_1" alt="' +contentList[i].title+'" src="'+contentList[i].pic+'"></a></li>';
				if(i == 0) {
					html2 = html2 + '<li class="cur">'+(i+1)+'</li>';
				} else {
					html2 = html2 + '<li class="">'+(i+1)+'</li>';
				}	
			}
			/*alert(html1);
			alert(html2);*/
			$("#ad1Pic").html(html1);
			$("#lunboNum").html(html)
		}
	});
	// 判断是否登录
	E3MALL.checkLogin();
	
});