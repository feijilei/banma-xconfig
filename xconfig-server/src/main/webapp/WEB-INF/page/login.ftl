<#include "/common/baseHtml.ftl" />
<@baseHtml localJsFiles=["javascripts/page/login.js"] remoteJsFiles=[] localCssFiles=["css/page/index.css"] bodyContainer=false>
<div class="my_header my_header_img">
    <div class="overlay">
        <h1>xConfig</h1>
        <form class="form-horizontal" style="text-align: left;width: 25%" action="${basepath}/login" method="post">
            <div class="form-group">
                <label for="email" class="col-sm-3 control-label">邮箱</label>
                <div class="col-sm-9">
                    <input type="email" class="form-control" id="email" name="email" placeholder="Email">
                </div>
            </div>
            <div class="form-group">
                <label for="password" class="col-sm-3 control-label">密码</label>
                <div class="col-sm-9">
                    <input type="password" class="form-control" id="password" name="password" placeholder="Password">
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-3 col-sm-9">
                    <div class="checkbox">
                        <label>
                            <input type="checkbox" name="rememberMe"> 记住我
                        </label>
                    </div>
                </div>
            </div>
            <#if errMsg??>
                <div class="form-group">
                    <div class="col-sm-offset-3 col-sm-9">
                        <p><strong>${errMsg!"未知错误"}</strong></p>
                    </div>
                </div>
            </#if>
            <div class="form-group">
                <div class="col-sm-offset-3 col-sm-9">
                    <button type="submit" class="btn btn-default">登录</button>
                </div>
            </div>
        </form>
    </div>
</div>
<#--body end-->

<script type="text/javascript">
</script>
</@baseHtml>