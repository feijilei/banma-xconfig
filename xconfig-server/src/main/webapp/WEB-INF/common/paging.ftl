<!--
	pageIndex 当前页面编号 从1开始
	pageCount 总页数 从1开始
	liNum 滑动的分页色块个数 默认是5
-->
<#macro paging pageIndex=0 pageCount=0 liNum=5 path="/user/userList?un=&pn=" >
	<ul class="pagination">
		<#assign offset = (liNum/2)?floor />
		
	  	<#--向上翻页特殊处理-->
  		<#if pageIndex-1 gte 1>
  			<li><a href="${basepath}${path}${pageIndex-1}">&laquo;</a></li>
  		<#else>
  			<li class="disabled"><a href="javascript:void(0);">&laquo;</a></li>
  		</#if>
  		
  		<#--中间特殊处理-->
  		<#if pageCount lte liNum><#-- 页数小于等于滑块个数，不会出现省略号等 -->
  			<#list 1..pageCount as pg>
  				<li <#if pg == pageIndex>class="active"</#if> >
  					<a href="${basepath}${path}${pg}">${pg}</a>
  				</li>
  			</#list>
  		<#else><#-- 页数大于滑块个数，将会出现省略号 -->
  			<#if pageIndex lt liNum><#-- 当前页面小于滑块数，表示当前页面较靠前，省略号在后 -->
  				<#list 1..liNum as pg>
	  				<li <#if pg == pageIndex>class="active"</#if> >
	  					<a href="${basepath}${path}${pg}">${pg}</a>
	  				</li>
	  			</#list>
  				<li><a href="javascript:void(0)">..</a></li>
	  			<li><a href="${basepath}${path}${pageCount}">${pageCount}</a></li>
			<#elseif (pageCount-pageIndex) lt liNum ><#-- 当前页面比较靠后，省略号在前 -->
				<li><a href="${basepath}${path}1">1</a></li>
				<li><a href="javascript:void(0)">..</a></li>
				<#list (pageCount-liNum)..pageCount as pg>
	  				<li <#if pg == pageIndex>class="active"</#if> >
	  					<a href="${basepath}${path}${pg}">${pg}</a>
	  				</li>
	  			</#list>
	  			
	  		<#else><#-- 两侧都有省略号 -->
	  			<li><a href="${basepath}${path}1">1</a></li>
				<li><a href="javascript:void(0)">..</a></li>
				<#list (pageIndex-offset)..(pageIndex+offset) as pg>
					<li <#if pg == pageIndex>class="active"</#if> >
	  					<a href="${basepath}${path}${pg}">${pg}</a>
	  				</li>
				</#list>
				<li><a href="javascript:void(0)">..</a></li>
	  			<li><a href="${basepath}${path}${pageCount}">${pageCount}</a></li>
			</#if>  			
  		</#if>
		
		<#--向下翻页特殊处理-->
		<#if pageIndex+1 lte pageCount>
  			<li><a href="${basepath}${path}${pageIndex+1}">&raquo;</a></li>
  		<#else>
  			<li class="disabled"><a href="javascript:void(0);">&raquo;</a></li>
  		</#if>
	</ul>
</#macro>