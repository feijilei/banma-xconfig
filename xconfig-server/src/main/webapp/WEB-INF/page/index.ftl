<#include "/common/baseHtml.ftl" />
<@baseHtml localJsFiles=["plugin/jquery-ui-1.12.0.custom/jquery-ui.min.js"] remoteJsFiles=[] localCssFiles=["plugin/jquery-ui-1.12.0.custom/jquery-ui.min.css","css/page/index.css"] bodyContainer=false>
    <div class="my_header my_header_img">
        <div class="overlay">
            <h1>xConfig</h1>
            <p>${wisdom?html}</p>
            <div style="width: 40%;margin-top: 20px">
                <span class="input-group input-group-lg">
                    <span class="input-group-addon">
                        <span class="glyphicon glyphicon-search" aria-hidden="true"></span>
                    </span>
                    <input id="search" type="text" class="form-control" placeholder="Search Project...">
                </span>
            </div>
        </div>
    </div>


    <#--<div style="font-size: x-large">-->
        <#--<#list projects as project>-->
        <#--&lt;#&ndash;todo 为这些标签增加不同的颜色 &ndash;&gt;-->
            <#--<a class="label label-info" href="${basepath}/main/project?project=${project?html}">${project?html}</a>-->
        <#--</#list>-->
    <#--</div>-->

    <script type="text/javascript">
        var availableTags = ${projects};
        $("#search").autocomplete({
            source: availableTags,
            select:function(event,ui){
                window.location = basepath + "/main/project?project="+ui.item.value;
            }
        });
    </script>
</@baseHtml>