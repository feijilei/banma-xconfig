<#include "/common/baseHtml.ftl" />
<#include "/common/paging.ftl" />
<#assign itemClass = ["list-group-item-success","list-group-item-info","list-group-item-warning","list-group-item-danger"] />
<@baseHtml localJsFiles=[
        "plugin/jquery-ui-1.12.0.custom/jquery-ui.min.js",
        "javascripts/page/profileOrder.js"
    ] remoteJsFiles=[] localCssFiles=[
        "plugin/jquery-ui-1.12.0.custom/jquery-ui.min.css"
    ] curMenu="profiles">
    <script type="application/javascript">
    </script>

    <h1 class="row">
        <div class="col-sm-12">
            profile排序
        </div>
    </h1>
    <hr>

    <div class="row">
        <div class="col-md-8">
            <ul id="sortList" class="list-group">
                <#list profiles as profile>
                    <li id="${profile}" class="list-group-item ${itemClass[profile_index%4]}"><span class="glyphicon glyphicon-sort" aria-hidden="true" ></span>&nbsp;${profile}<button type="button" class="delBtn close" aria-label="Close"><span aria-hidden="true">&times;</span></button></li>
                </#list>
            </ul>
        </div>
        <div class="col-md-4">
            <div class="form-group">
                <label for="addProfile">增加profile</label>
                <input type="text" class="form-control" id="addProfile" placeholder="增加profile">
            </div>
            <button id="addBtn" type="submit" class="btn btn-default">增加</button>
        </div>
    </div>
    <hr>

    <div class="row">
        <div class="text-center">
            <button id="save" type="submit" class="btn btn-primary">应用</button>
            <button id="cancel" type="submit" class="btn btn-default">取消</button>
        </div>
    </div>

</@baseHtml>