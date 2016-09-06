<#include "/common/baseHtml.ftl" />
<#assign color = ["#E0F2F1","#FFEBEE","#EEEEEE","#E8EAF6","#F3E5F5","#E3F2FD","#E0F2F1","#F3E5F5","#EEE0E5","#FFDEAD","#CDAF95","#B4CDCD"] />
<@baseHtml localJsFiles=["plugin/jquery-ui-1.12.0.custom/jquery-ui.min.js","javascripts/page/index2.js"] remoteJsFiles=[] localCssFiles=["plugin/jquery-ui-1.12.0.custom/jquery-ui.min.css"] bodyContainer=false>

    <style>
        .pull-left{
            float:left;
        }
        .circle{
            width:50px;
            height: 50px;
            border-radius: 25px;
            background:#ccc;
            text-align: center;
            line-height: 50px;
        }
        .item{
            padding:10px 0;
            border-bottom: 1px solid #eee;
        }
        .title{
            line-height: 25px;
            padding-left: 10px;
            font-size: large;
        }
        .sub-title{
            line-height: 25px;
            padding-left: 10px;
            color: #7f8fa4;
        }
        .right-title{
            line-height: 25px;
            color: #7f8fa4;
            font-size: smaller;
        }
        .clearfix:before,.clearfix:after{
            display: table;
            content: " ";

        }
        .clearfix:after{
            clear: both;
        }

        body{
            position: relative;
        }
    </style>
    <div class="letterIndex hidden">
        <ul class="nav nav-pills nav-stacked navbar-fixed-top pull-left" style="width: 40px;font-size: small" role="tablist">
            <#list ["A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"] as x>
                <li role="presentation"><a href="#${x}" style="padding:3px 15px;">${x}</a></li>
            </#list>
        </ul>
    </div>

    <div class="container">
        <div class="text-center">
            <h1>projects<@dataRight role=role url="/main/addProject"><small><a href="javascript:void(0)"><span class="glyphicon glyphicon-plus-sign" aria-hidden="true" data-toggle="modal" data-target="#addProjectModal"></span></a></small></@dataRight></h1>
            <p>${wisdom?html}</p>
            <div style="width: 80%;margin-top: 20px;margin-left: auto;margin-right: auto">
                <span class="input-group input-group-lg">
                    <span class="input-group-addon">
                        <span class="glyphicon glyphicon-search" aria-hidden="true"></span>
                    </span>
                    <input id="search" type="text" class="form-control" placeholder="Search Project...">
                </span>
            </div>
        </div>
        <hr>
        <div id="projectList" class="row">
            <#--<div class="letterIndex">-->
                <#--<nav id="letterIndexBar" class="navbar navbar-inverse navbar-fixed-top" role="navigation">-->
                    <#--<div class="container">-->
                        <#--<!-- Collect the nav links, forms, and other content for toggling &ndash;&gt;-->
                        <#--<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">-->
                            <#--<ul class="nav navbar-nav">-->
                                <#--<#list ["A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"] as x>-->
                                    <#--<li><a href="#${x}">${x}</a></li>-->
                                <#--</#list>-->
                            <#--</ul>-->
                        <#--</div><!-- /.navbar-collapse &ndash;&gt;-->
                    <#--</div><!-- /.container-fluid &ndash;&gt;-->
                <#--</nav>-->
            <#--</div>-->

            <div class="col-md-12">
                <div>
                    <#list projectsPo as projectPo>
                        <div id="${projectPo.project?substring(0,1)?upper_case}" class="clearfix item">
                            <div class="pull-left">
                                <div class="circle" style="background-color: ${color[projectPo.project?length%12]}">${projectPo.project?substring(0,1)?upper_case}</div>
                            </div>
                            <div class="pull-left">
                                <div class="title"><strong><a href="${basepath}/main/project?project=${projectPo.project?html}">${projectPo.project?html}</a></strong></div>
                                <div class="sub-title">${projectPo.description?html}</div>
                            </div>
                            <div class="pull-right">
                                <div class="right-title">${projectPo.createTime?string("YYYY-MM-dd HH:mm:ss")}</div>
                            </div>
                        </div>
                    </#list>

                    <#--<#list ["A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"] as x>-->
                        <#--<div id="${x}" class="clearfix item">-->
                            <#--<div class="pull-left">-->
                                <#--<div class="circle">${x?substring(0,1)?upper_case}</div>-->
                            <#--</div>-->
                            <#--<div class="pull-left">-->
                                <#--<div class="title"><strong><a href="${basepath}/main/project?project=${x?html}">${x?html}</a></strong></div>-->
                                <#--<div class="sub-title">位置服务</div>-->
                            <#--</div>-->
                        <#--</div>-->
                    <#--</#list>-->
                </div>
            </div>
        </div>
    </div>
    <#--body end-->

    <#--Modal-->
    <div id="addProjectModal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> 新增project</h4>
                </div>
                <div class="modal-body">
                    <form id="addProjectForm" class="form-horizontal">
                        <div class="form-group">
                            <label for="addProjectName" class="col-sm-3 control-label">project名称</label>
                            <div class="col-sm-9">
                                <input type="text" class="form-control" id="addProjectName" name="addProjectName" placeholder="projectName">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="preProfiles" class="col-sm-3 control-label">预创建profiles</label>
                            <div class="col-sm-9">
                                <input type="text" class="form-control" id="preProfiles" name="preProfiles" placeholder="dev,beta,pre,prd" value="dev,daily,pre,pp,prd" aria-describedby="preProfilesHelpBlock">
                                <span id="preProfilesHelpBlock" class="help-block">请用逗号分隔，如果这里没有输入任何profile，系统将自动为您创建一个默认的profile</span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="description" class="col-sm-3 control-label">描述</label>
                            <div class="col-sm-9">
                                <textarea class="form-control" id="description" name="description" rows="5" placeholder="描述信息"></textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="alert alert-danger alert-dismissible hidden col-sm-offset-3 col-sm-9 errMsgDiv" role="alert">
                                <strong>Error! </strong><span class="errMsg"></span>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" id="addProjectButton" class="btn btn-primary">新增</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->


    <script type="text/javascript">
        var availableTags = ${projectsJson};
    </script>
</@baseHtml>