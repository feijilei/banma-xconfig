<#include "/common/baseHtml.ftl" />
<@baseHtml localJsFiles=["plugin/jquery-ui-1.12.0.custom/jquery-ui.min.js","javascripts/page/index.js"] remoteJsFiles=[] localCssFiles=["plugin/jquery-ui-1.12.0.custom/jquery-ui.min.css","css/page/index.css"] bodyContainer=false>
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

    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <h3 style="text-align: center">projects <@dataRight role=role url="/main/addProject"><small><a href="javascript:void(0)"><span class="glyphicon glyphicon-plus-sign" aria-hidden="true" data-toggle="modal" data-target="#addProjectModal"></span></a></small></@dataRight></h3>
                <hr>
                <div style="font-size: x-large">
                    <#list projects as project>
                        <a class="label <#if project_index%3 == 0>label-primary<#elseif project_index%3 == 1>label-success<#elseif project_index%3 == 2>label-info</#if>" href="${basepath}/main/project?project=${project?html}">${project?html}</a>
                    </#list>
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