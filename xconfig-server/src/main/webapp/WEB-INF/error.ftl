<#include "/common/baseHtml.ftl" />
<@baseHtml localJsFiles=[] remoteJsFiles=[] localCssFiles=[]>
    <#if stackTraces??>
        ${stackTraces!""}
    <#else>
        <div class="jumbotron">
            <h1>哎呀，出错了!</h1>
            <p>数据错误或系统异常....</p>
        </div>
    </#if>
</@baseHtml>