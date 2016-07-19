<#include "/common/headHtml.ftl" />
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
    <meta name="description" content="宏观经济">
    <meta name="keywords" content="宏观经济">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <meta name="renderer" content="webkit" />


    <title>DIMGMEI</title>
    <meta name="keywords" content="" />
    <meta name="description" content=""  />

    <@headHtml localJsFiles = localJsFiles remoteJsFiles = remoteJsFiles localCssFiles = localCssFiles>
        <link type="text/css" rel="stylesheet" href="${basepath}/resources/css/login/common.css" ></link>
        <link type="text/css" rel="stylesheet" href="${basepath}/resources/css/login/login.css" ></link>
        <link type="text/css" rel="stylesheet" href="${basepath}/resources/css/login/w1.css" ></link>

        <script src="${basepath}/resources/javascripts/login.js" type="text/javascript"></script>

        <script type="text/javascript">
            var basepath = '${basepath}';
        </script>
    </@headHtml>

</head>
<noscript unselectable="on" id="noscript">
    <div class="aw-404 aw-404-wrap container">
        <p>你的浏览器禁用了JavaScript, 请开启后刷新浏览器获得更好的体验!</p>
    </div>
</noscript>
<body class="login">

<div id="wrapper">
    <div class="aw-login-box">
        <div class="mod-body clearfix">
            <div class="content pull-left">
                <h1 class="logo"><a href=""><img width="100" src="/resources/img/dingmei2_logo.jpg" /></a></h1>
                <#--<h2>欢迎来到</h2>-->
                <form id="login_form" method="post" action="/j_spring_security_check">
                    <input type="hidden" name="return_url" value="" />
                    <ul>
                        <li>
                            <input type="text" id="j_username" class="form-control" placeholder="邮箱 / 用户名" name="j_username" />
                        </li>
                        <li>
                            <input type="password" id="j_password" class="form-control" placeholder="密码" name="j_password" />
                        </li>
                        <li class="alert alert-danger <#if !error??>hide</#if> error_message">
                            <i class="icon icon-delete"></i>
                            <em id="errmsg"><#if error??>用户名或者密码错误</#if></em>
                        </li>
                        <li class="last">
                            <a href="javascript:;" id="login_submit" class="pull-right btn btn-normal btn-success">登录</a>
                            <label class="hidden-xs">
                               <!-- <input type="checkbox" value="1" name="net_auto_login" checked />
                                记住我			-->
                                </label>
                        </li>
                    </ul>
                </form>
            </div>
        </div>
        <div class="mod-footer">
            <span>还没有账号?</span>&nbsp;&nbsp;
            <a href="#">立即注册</a>
        </div>
    </div>
</div>

<!-- DO NOT REMOVE -->
<div id="aw-ajax-box" class="aw-ajax-box"></div>
<div class="aw-mask hide">
    <div class="bg"></div>
    <div class="img"></div>
    <i class="icon icon-delete"></i>
</div>

<!-- Escape time: 0.013461112976074 --><!-- / DO NOT REMOVE -->

</body>
</html>

<!--
<html>
<head>

</head>
<body>
<form action="/j_spring_security_check" method="post">
    userName:<input name="j_spring_security_check" type="text" />
    password:<input name="j_password" type="password" />
    <input type="submit" value="submit" />
</form>
</body>
</html> -->