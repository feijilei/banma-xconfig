$(function($){
	/**
	 * 查看cookie的内容,如果点过up或者down按钮的用户不能够再次点击
	 */
	//cookie操作
	var upHistoryList = $.cookie("upList");
	var downHistoryList = $.cookie("downList");
	if(upHistoryList == undefined){//不存在就构建一个
		upHistoryList = [];
		$.cookie("upList","");
	}else{
		upHistoryList = upHistoryList.split(",");
	}
	if(downHistoryList == undefined){
		downHistoryList = [];
		$.cookie("downList","");
	}else{
		downHistoryList = downHistoryList.split(",");
	}
	
	/**
	 * 已经操作过的禁用
	 */
	for(var i = 0 ; i < upHistoryList.length ; i++){
		$('[upId='+upHistoryList[i]+"]").attr("disabled","disabled");
	}
	
	for(var i = 0 ; i < downHistoryList.length ; i++){
		$('[downId='+downHistoryList[i]+"]").attr("disabled","disabled");
	}
	
	/**
	 * up按钮绑定事件
	 */
	$("a[name='upBtn']").bind("click",function(){
		var jokeId = $(this).attr("upId");
		//禁用按钮
		$('[upId='+jokeId+"]").attr("disabled","disabled");
		//up次数加1
		var upCount = $(this).find("#upCount").text();
		upCount++;
		$(this).find("#upCount").text(upCount);
		
		//写入cookie
		if(-1 == $.inArray(jokeId,upHistoryList)){
			upHistoryList.push(jokeId);
		}
		$.cookie("upList",upHistoryList.join(","));
		
		//发送ajax请求到数据库
		var param = {};
		param.jokeId = jokeId;
		$.get(basepath+"/app/up",param);
	});
	
	/**
	 * down按钮绑定事件
	 */
	$("a[name='downBtn']").bind("click",function(){
		var jokeId = $(this).attr("downId");
		//禁用按钮
		$('[downId='+jokeId+"]").attr("disabled","disabled");
		//up次数加1
		var downCount = $(this).find("#downCount").text();
		downCount--;
		$(this).find("#downCount").text(downCount);
		
		//写入cookie
		if(-1 == $.inArray(jokeId,downHistoryList)){
			downHistoryList.push(jokeId);
		}
		$.cookie("downList",downHistoryList.join(","));
		
		//发送ajax请求到数据库
		var param = {};
		param.jokeId = jokeId;
		$.get(basepath+"/app/down",param);
	});
})