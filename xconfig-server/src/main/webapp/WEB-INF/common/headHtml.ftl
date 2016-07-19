<#--定义html文件中head标签里的内容 <#nested/>为附加内容-->
<#--
	jsFiles从服务器引入的js文件，
	localJsFiles从当前项目引入的文件,
	remoteJsFiles是从远程引入的js文件，例如百度地图,
	cssFiles服务器引入的css文件,
	localCssFiles从当前项目引入的css文件,
	headTag是否在宏内添加<head标签>默认为false不添加
-->
<#macro headHtml localJsFiles=[] remoteJsFiles=[] localCssFiles=[] headTag = false>
	<#if headTag>
		<head>
	</#if>
	<#-- 	响应布局 -->
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<#-- 	ie兼容 -->
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	
	<#-- 最新 Bootstrap 核心 CSS 文件 -->
	<#--<link rel="stylesheet" href="http://cdn.bootcss.com/twitter-bootstrap/3.0.3/css/bootstrap.min.css">-->
    <link rel="stylesheet" href="${basepath}/resources/bootstrap3.3.5/css/bootstrap.min.css">
	<#-- 可选的Bootstrap主题文件（一般不用引入） -->
	<#--<link rel="stylesheet" href="http://cdn.bootcss.com/twitter-bootstrap/3.0.3/css/bootstrap-theme.min.css">-->
    <link rel="stylesheet" href="${basepath}/resources/bootstrap3.3.5/css/bootstrap-theme.min.css">
	<#-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
	<#--<script src="http://cdn.bootcss.com/jquery/2.0.3/jquery.min.js" type="text/javascript"></script>-->
    <script src="${basepath}/resources/plugin/jquery/jquery2.0.3.min.js" type="text/javascript"></script>

	<#-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<#--<script src="http://cdn.bootcss.com/twitter-bootstrap/3.0.3/js/bootstrap.min.js" type="text/javascript"></script>-->
    <script src="${basepath}/resources/bootstrap3.3.5/js/bootstrap.min.js" type="text/javascript"></script>

    <!--[if lte IE 8]>
    <!--<script src="http://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>-->
    <script src="${basepath}/resources/plugin/Respond1.4.2.min.js" type="text/javascript"></script>
    <![endif]-->

	<#-- jquery cookie文件 -->
	<script src="${basepath}/resources/plugin/jquery/jquery.cookie.js" type="text/javascript"></script>
    <#--jquery form-->
    <script src="${basepath}/resources/plugin/jquery/jquery.form.js" type="text/javascript"></script>

    <#--表单验证插件-->
    <link rel="stylesheet" href="${basepath}/resources/plugin/bootstrapValidator/css/bootstrapValidator.min.css"/>
    <script type="text/javascript" src="${basepath}/resources/plugin/bootstrapValidator/js/bootstrapValidator.min.js"></script>

    <#--自定义的jquery方法-->
    <script src="${basepath}/resources/plugin/myJqueryPlugin.js" type="text/javascript"></script>
	<#list localCssFiles as localCss>
		<link rel="stylesheet" href="${basepath}/resources/css/${localCss}"/>
	</#list>

	<#list localJsFiles as localJs>
		<script type="text/javascript" src="${basepath}/resources/${localJs}"></script>
	</#list>
	
	<#list remoteJsFiles as remoteJs>
		<script type="text/javascript" src="${remoteJs}"></script>
	</#list>
	
	<#nested/>
	<#if headTag>
		</head>
	</#if>
</#macro>
