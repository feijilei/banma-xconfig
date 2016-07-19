<!DOCTYPE html>
<html>
<head>
    <meta content="text/html;charset=utf-8" http-equiv="Content-Type" />
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
    <meta name="renderer" content="webkit" />
    <title>登录 - WeGene个人遗传基因检测解读平台</title>
    <meta name="keywords" content="基因，祖源，基因检测，基因解读，基因测序，遗传基因，基因体检，基因与健康, 23andme" />
    <meta name="description" content="WeGene是一个提供基因检测，遗传基因分析，基因解读的平台，分析包括祖源信息，遗传疾病，健康预测，运动天赋，营养吸收，药物吸收等内容。"  />
    <link href="https://www.wegene.com/static/css/wegene/img/favicon.ico?v=20151025" rel="shortcut icon" type="image/x-icon" />

    <%--<link rel="stylesheet" type="text/css" href="https://www.wegene.com/static/css/bootstrap.css?v=20151025" />--%>
    <%--<link rel="stylesheet" type="text/css" href="https://www.wegene.com/static/css/icon.css?v=20151025" />--%>

    <%--<link href="https://www.wegene.com/static/css/default/common.css?v=20151025" rel="stylesheet" type="text/css" />--%>
    <%--<link href="https://www.wegene.com/static/css/default/link.css?v=20151025" rel="stylesheet" type="text/css" />--%>
    <%--<link href="https://www.wegene.com/static/js/plug_module/style.css?v=20151025" rel="stylesheet" type="text/css" />--%>
    <%--<link href="https://www.wegene.com/static/css/default/login.css?v=20151025" rel="stylesheet" type="text/css" />--%>

    <%--<link href="https://www.wegene.com/static/css/wegene/wegene.css?v=20151025" rel="stylesheet" type="text/css" />--%>



    <link rel="stylesheet" href="http://cdn.bootcss.com/twitter-bootstrap/3.0.3/css/bootstrap.min.css">
    <link rel="stylesheet" href="http://cdn.bootcss.com/twitter-bootstrap/3.0.3/css/bootstrap-theme.min.css">
    <script src="http://cdn.bootcss.com/jquery/2.0.3/jquery.min.js" type="text/javascript"></script>

    <script src="http://cdn.bootcss.com/twitter-bootstrap/3.0.3/js/bootstrap.min.js" type="text/javascript"></script>





    <script type="text/javascript">


        var G_RESEARCH = '';
    </script>
    <!--[if lte IE 8]>
    <script type="text/javascript" src="https://www.wegene.com/static/js/respond.js"></script>
    <![endif]-->
</head>
<noscript unselectable="on" id="noscript">
    <div class="aw-404 aw-404-wrap container">
        <img src="https://www.wegene.com/static/common/no-js.jpg">
        <p>你的浏览器禁用了JavaScript, 请开启后刷新浏览器获得更好的体验!</p>
    </div>
</noscript>
<body class="login">

<div id="wrapper">
    <div class="aw-login-box">
        <div class="mod-body clearfix">
            <div class="content pull-left">
                <h1 class="logo"><a href=""><img width="97" src="https://www.wegene.com/static/css/wegene/img/login_logo.png" /></a></h1>
                <!-- <h2>WeGene个人遗传基因检测解读平台</h2> -->
                <form id="login_form" method="post" onsubmit="return false" action="https://www.wegene.com/account/ajax/login_process/">
                    <input type="hidden" name="return_url" value="" />
                    <ul>
                        <li>
                            <input type="text" id="aw-login-user-name" class="form-control" placeholder="邮箱 / 用户名" name="user_name" />
                        </li>
                        <li>
                            <input type="password" id="aw-login-user-password" class="form-control" placeholder="密码" name="password" />
                        </li>
                        <li class="alert alert-danger hide error_message">
                            <i class="icon icon-delete"></i> <em></em>
                        </li>
                        <li class="last">
                            <a href="javascript:;" onclick="AWS.ajax_post($('#login_form'), AWS.ajax_processer, 'error_message');" id="login_submit" class="pull-right btn btn-normal btn-success">登录</a>
                            <label class="hidden-xs">
                                <input type="checkbox" value="1" name="net_auto_login" checked />
                                记住我							</label>
                            <a class="hidden-xs" href="https://www.wegene.com/account/find_password/">&nbsp;&nbsp;忘记密码</a>
                        </li>
                    </ul>
                </form>
            </div>
        </div>
        <div class="mod-footer">
            <span>还没有账号?</span>&nbsp;&nbsp;
            <a href="https://www.wegene.com/account/register/">立即注册</a>
            <a class="hide forget-password" href="https://www.wegene.com/account/find_password/">• 忘记密码</a>
            <!-- •&nbsp;&nbsp; -->
            <!-- <a href="">游客访问</a>&nbsp;&nbsp;•&nbsp;&nbsp;
            <a href="https://www.wegene.com/reader/">问答阅读</a> -->
        </div>
    </div>
</div>

<script type="text/javascript" src="https://www.wegene.com/static/js/app/login.js"></script>

<!-- DO NOT REMOVE -->
<div id="aw-ajax-box" class="aw-ajax-box"></div>
<div class="aw-mask hide">
    <div class="bg"></div>
    <div class="img"></div>
    <i class="icon icon-delete"></i>
</div>

<!-- Escape time: 0.013078927993774 --><!-- / DO NOT REMOVE -->

</body>
</html>