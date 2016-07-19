<#--定义html文件中head标签里的内容-->
<#include "headIncludeMacro.ftl"/>
<#macro baseHtmlPage title="常用工具" localJsFiles=[] remoteJsFiles=[] localCssFiles=[] curMenu="" >
<#escape x as x?html>
<!DOCTYPE html>
	<html lang="zh-CN">
		<head>
			<meta http-equiv="pragma" content="no-cache">
			<meta name="description" content="">
			<meta name="keywords" content="">
			<meta http-equiv="cache-control" content="no-cache">
			<meta http-equiv="expires" content="0">    
			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
			
			<title>${title}</title>
			
			<@headIncludeMacro localJsFiles = localJsFiles remoteJsFiles = remoteJsFiles localCssFiles = localCssFiles>
				<script type="text/javascript">
					var basepath = '${basepath}';
				</script>
			</@headIncludeMacro>
		</head>
		
		<body>
			<div id="top_navbar" class="navbar navbar-fixed-top navbar-inverse">
		    <div class="navbar-inner">
		      <div class="container">
		        <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
		          <span class="icon-bar"></span>
		          <span class="icon-bar"></span>
		          <span class="icon-bar"></span>
		        </a>
		        <a class="brand" href="#">DEVTOOLS</a>
		        <div class="nav-collapse">
		          <ul class="nav">
		            <li id="header_1" class="active">
		            	<a href="<%=basePath%>">格式化</a>
		           	</li>
		            <li id="header_2"><a href="#">预留1</a></li>
		            <li id="header_3"><a href="#">预留2</a></li>
		          </ul>
		          <ul class="nav pull-right">
		            <li><a href="#">链接</a></li>
		            <li class="divider-vertical"></li>
		            <li class="dropdown">
		              <a href="#" class="dropdown-toggle" data-toggle="dropdown">关于<b class="caret"></b></a>
		              <ul class="dropdown-menu">
						<li><a href="#">关于</a></li>
		                <li><a href="#">另一个动作</a></li>
		                <li><a href="#">其他动作</a></li>
		                <li class="divider"></li>
		                <li class="nav-header">导航头</li>
		                <li><a href="#">被间隔的链接</a></li>
		                <li><a href="#">另一个链接</a></li>
		              </ul>
		            </li>
		          </ul>
		        </div>
		      </div>
		    </div>
		  </div>
		  
		  	<#nested/>
		  
		  	<div id="copyright" class="container">
				<hr/>
				<div class="well well-small text-center">
					<small>Copyright©2013 zhengzhichao-devTool. All Rights Reserved.<a href="http://www.miitbeian.gov.cn/">豫ICP备13010845号</a></small>
				</div>
			</div>
		</body>
	</html>
</#escape>
</#macro>
