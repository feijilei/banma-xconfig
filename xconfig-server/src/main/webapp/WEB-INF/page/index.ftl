<#include "/common/baseHtml.ftl" />
<@baseHtml localJsFiles=[] remoteJsFiles=[] localCssFiles=[]>
    <div class="input-group input-group-lg">
        <input type="text" class="form-control" placeholder="Search for...">
          <span class="input-group-btn">
            <button class="btn btn-default" type="button">Go!</button>
          </span>
    </div>
    <hr/>

    <div style="font-size: x-large">
        <#list projects as project>
            <#--todo 为这些标签增加不同的颜色 -->
            <a class="label label-info" href="${basepath}/main/project?project=${project?html}">${project?html}</a>
        </#list>
    </div>
</@baseHtml>