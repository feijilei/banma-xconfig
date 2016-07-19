<#--定义html文件中head标签里的内容 <#nested/>为附加内容-->
<#--
	jsFiles从服务器引入的js文件，
	localJsFiles从当前项目引入的文件,
	remoteJsFiles是从远程引入的js文件，例如百度地图,
	cssFiles服务器引入的css文件,
	localCssFiles从当前项目引入的css文件,
	headTag是否在宏内添加<head标签>默认为false不添加
-->
<#macro headIncludeMacro localJsFiles=[] remoteJsFiles=[] localCssFiles=[] headTag = false>
	<#if headTag>
		<head>
	</#if>
	<link rel="stylesheet" type="text/css" href="${basepath}/bootstrap2.3.2/css/bootstrap.css" />
	<link rel="stylesheet" type="text/css" href="${basepath}/bootstrap2.3.2/css/font-awesome.min.css">
	<link rel="stylesheet" type="text/css" href="${basepath}/page/common/myCss.css" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link href="${basepath}/bootstrap2.3.2/css/bootstrap-responsive.css" rel="stylesheet">
	<script type="text/javascript" src="${basepath}/bootstrap2.3.2/js/jquery-1.10.0.min.js"></script>
	<script type="text/javascript" src="${basepath}/bootstrap2.3.2/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="${basepath}/bootstrap2.3.2/js/holder.js"></script>
	
	<#list localCssFiles as localCss>
		<link rel="stylesheet" href="${basepath}/resources/css/${localCss}"/>
	</#list>

	<#list localJsFiles as localJs>
		<script type="text/javascript" src="${basepath}/resources/javascripts/${localJs}"></script>
	</#list>
	
	<#list remoteJsFiles as remoteJs>
		<script type="text/javascript" src="${remoteJs}"></script>
	</#list>
	
	<#nested/>
	<#if headTag>
		</head>
	</#if>
</#macro>
