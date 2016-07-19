<#include "/common/baseHtml.ftl" />
<#include "/common/paging.ftl" />
<@baseHtml localJsFiles=['javascripts/newJoke.js'] remoteJsFiles=[] localCssFiles=[]>

	<#list jokes as joke>
		<div class="panel panel-success">
		  <div class="panel-body">
		    ${joke.content}
		  </div>
		  <div class="panel-footer" style="padding:5px 15px;background-color:#FFFFFF">
		  	<a href="javascript:void(0)" upId='${joke.jokeId}' name='upBtn' class="btn btn-default btn-sm" role="button">
			  <span class="glyphicon glyphicon-thumbs-up"></span>&nbsp;<span id="upCount">${joke.upCount}</span>
			</a>
			&nbsp;&nbsp;
			<a href="javascript:void(0)" downId='${joke.jokeId}' name='downBtn' class="btn btn-default btn-sm" role="button">
			  <span class="glyphicon glyphicon-thumbs-down"></span>&nbsp;<span id="downCount">${joke.downCount}</span>
			</a>
		  </div>
		</div>
	</#list>
	
	<#-- 分页 -->
	<@paging pageIndex=pageIndex pageCount=pageCount />
</@baseHtml>