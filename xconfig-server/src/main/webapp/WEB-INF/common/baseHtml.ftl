<#--定义html文件中head标签里的内容-->
<#include "headHtml.ftl"/>
<#macro baseHtml title="xConfig" localJsFiles=[] remoteJsFiles=[] localCssFiles=[] curMenu="index" bodyContainer=true >
    <#escape x as x?html>
    <!DOCTYPE html>
    <html lang="zh-CN">
    <head>
        <meta http-equiv="pragma" content="no-cache">
        <meta name="description" content="xConfig">
        <meta name="keywords" content="xConfig">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <title>${title}</title>

        <@headHtml localJsFiles = localJsFiles remoteJsFiles = remoteJsFiles localCssFiles = localCssFiles>

        <#-- 	本地全局css -->
            <link rel="stylesheet" href="${basepath}/resources/css/global.css"/>
            <script type="text/javascript">
                var basepath = '${basepath}';

                jQuery(document).ready(function($){
                    $("#logout").bind("click",function(){
                        $.cookie("un","",{path:"/",expires:-1});
                        $.cookie("t","",{path:"/",expires:-1});
                        window.location = basepath + "/logout";
                    })
                })
            </script>
        </@headHtml>
    </head>

    <body>

    <header role="banner" class="navbar navbar-inverse navbar-fixed-top" style="margin-bottom: 0px">
        <div class="container">
        <#-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse"
                        data-target="#bs-example-navbar-collapse-1">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="#">xConfig</a>
            </div>

        <#-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav">
                    <li <#if curMenu == "index">class="active"</#if> ><a href="${basepath}/main/index">index</a></li>
                    <li <#if curMenu == "user">class="active"</#if> ><a href="${basepath}/user/userList">user</a></li>
                    <li <#if curMenu == "other">class="active"</#if> ><a href="#">other</a></li>
                <#--
                <li><a href="#">最新段子</a></li>
                <li class="dropdown">
                  <a href="#" class="dropdown-toggle" data-toggle="dropdown">Dropdown <b class="caret"></b></a>
                  <ul class="dropdown-menu">
                    <li><a href="#">Action</a></li>
                    <li><a href="#">Another action</a></li>
                    <li><a href="#">Something else here</a></li>
                    <li class="divider"></li>
                    <li><a href="#">Separated link</a></li>
                    <li class="divider"></li>
                    <li><a href="#">One more separated link</a></li>
                  </ul>
                </li>
                -->
                </ul>

            <#--
            <form class="navbar-form navbar-left" role="search">
              <div class="form-group">
                <input type="text" class="form-control" placeholder="Search">
              </div>
              <button type="submit" class="btn btn-default">Submit</button>
            </form> -->
                <#if userNike??>
                    <ul class="nav navbar-nav navbar-right">
                    <#--<p class="navbar-text">欢迎:</p>-->
                    <#--<li><a href="/j_spring_security_logout">注销</a></li>-->
                        <li class="dropdown">
                            <a href="javascript:void(0)" class="dropdown-toggle" data-toggle="dropdown"><span class="glyphicon glyphicon-user" aria-hidden="true"></span> ${userNike?html} <b class="caret"></b></a>
                            <ul class="dropdown-menu">
                                <li><a href="${basepath}/user/changePassword"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span> 修改密码</a></li>
                                <li class="divider"></li>
                                <li><a href="javascript:void(0)" id="logout"><span class="glyphicon glyphicon-off" aria-hidden="true"></span> 退出登录</a></li>
                            </ul>
                        </li>
                    </ul>
                </#if>
            </div><#-- /.navbar-collapse -->
        </div>
    </header>

    <#if bodyContainer>
        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <#nested/>
                </div>

            </div>
        </div>
    <#else>
        <#nested/>
    </#if>



    <div id="copyright" >
        <hr/>
        <div class="text-center">
            <small>Copyright©2013 zhengzhichao. All Rights Reserved.</small>
        </div>
    </div>
    </body>
    </html>
    </#escape>
</#macro>
