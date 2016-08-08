<#include "/common/baseHtml.ftl" />
<@baseHtml localJsFiles=[
        "javascripts/page/projectSetting.js"
    ] remoteJsFiles=[] localCssFiles=[]>
    <style type="text/css">
    </style>

    <script type="application/javascript">
    </script>

    <h1 class="row">
        <div class="col-sm-11">
            修改密码
        </div>
    </h1>
    <hr/>
    <div>
        <#if msg??>
            <#if msg != "ok">
                <div class="alert alert-danger" role="alert">
                    <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                    <span class="sr-only">Error:</span>
                ${msg?html}
                </div>
            <#else >
                <div class="alert alert-info" role="alert">
                    <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                    <span class="sr-only">Success:</span>
                    修改成功！
                </div>
            </#if>
        </#if>


        <form action="${basepath}/user/updateUser" method="post">
            <div class="form-group">
                <label for="userNike">昵称</label>
                <input type="text" class="form-control" id="userNike" name="userNike" placeholder="张三">
            </div>
            <div class="form-group">
                <label for="oldPassword">旧密码</label>
                <input type="password" class="form-control" id="oldPassword" name="oldPassword" placeholder="旧密码">
            </div>
            <div class="form-group">
                <label for="newPassword1">新密码</label>
                <input type="password" class="form-control" id="newPassword1" name="newPassword1" placeholder="新密码">
            </div>
            <div class="form-group">
                <label for="newPassword2">新密码</label>
                <input type="password" class="form-control" id="newPassword2" name="newPassword2" placeholder="请重复新密码">
            </div>
            <button type="submit" class="btn btn-default">修改</button>
        </form>
    </div>
</@baseHtml>