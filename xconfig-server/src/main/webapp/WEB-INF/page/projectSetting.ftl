<#include "/common/baseHtml.ftl" />
<@baseHtml localJsFiles=[
        "plugin/jquery-ui-1.12.0.custom/jquery-ui.min.js",
        "javascripts/page/projectSetting.js"
    ] remoteJsFiles=[] localCssFiles=[
        "plugin/jquery-ui-1.12.0.custom/jquery-ui.min.css"
    ]>
    <style type="text/css">
    </style>

    <script type="application/javascript">
        var project = "${project?html}";
    </script>

    <h1 class="row">
        <div class="col-sm-11">
            ${project?html}
            <span style="font-size: medium">
                <a href="javascript:void(0)" style="color: red"><span class="glyphicon glyphicon-remove-sign" aria-hidden="true" data-toggle="modal" data-target="#removeProjectModal"></span></a>
            </span>
        </div>
    </h1>
<h6>被依赖项目列表：<#if dProjects?? && dProjects?size gt 0><#list dProjects as dProject><span class="label label-info" style="display: inline-block;margin-top: 1px;">${dProject?html}</span>&nbsp;</#list><#else>无</#if></h6>
    <hr/>
    <h4>项目owner列表: <a href="javascript:void(0)"><span class="glyphicon glyphicon-plus-sign" aria-hidden="true" data-toggle="modal" data-target="#addOwnerModal"></span></a></h4>
    <div>
        <table id="kvTable" class="table table-condensed table-hover" cellspacing="0" width="100%">
            <thead>
            <tr>
                <th>用户名</th>
                <th>昵称</th>
                <th>创建时间</th>
                <th>操作</th>
            </tr>
            </thead>

            <tbody>
                <#list xUserVos as user>
                <tr data-key=${user.userName?html}>
                    <td>${user.userName?html}</td>
                    <td>${user.userNike?html}</td>
                    <td>${user.createTime?html}</td>
                    <td>
                        <a href="javascript:void(0)"><span class="glyphicon glyphicon-remove" aria-hidden="true" data-toggle="modal" data-target="#removeOwnerModal"></span></a>
                    </td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>

    <#--body end-->

    <div id="removeProjectModal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" style="color: red"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span> 删除项目</h4>
                </div>
                <div class="modal-body">
                    <div>
                        <p class="text-danger"><span class="glyphicon glyphicon-warning-sign" aria-hidden="true"></span> 警告：删除项目后将不可恢复，请确认！</p>
                    </div>
                    <div class="alert alert-danger alert-dismissible hidden errMsgDiv" role="alert">
                        <strong>Error! </strong><span class="errMsg"></span>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" id="removeProjectButton" class="btn btn-primary">确认</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

    <#--Modal-->
    <div id="addOwnerModal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title"><span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span> 新增owner</h4>
                </div>
                <div class="modal-body">
                    <form id="addOwnerForm" class="form-horizontal">
                        <input type="text" name="project" class="hidden" value="${project?html}"/>
                        <div class="form-group">
                            <label for="addOwer" class="col-sm-2 control-label">用户名</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="addOwer" name="addOwer" placeholder="admin@xconfig.com">
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="alert alert-danger alert-dismissible hidden col-sm-offset-2 col-sm-10 errMsgDiv" role="alert">
                                <strong>Error! </strong><span class="errMsg"></span>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" id="addOwnerButton" class="btn btn-primary">新增</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

    <div id="removeOwnerModal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title text-danger"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span> 删除owner</h4>
                </div>
                <div class="modal-body">
                    <div>
                        <p class="text-danger"><span class="glyphicon glyphicon-warning-sign" aria-hidden="true"></span> 警告：删除owner将会导致其失去修改当前项目配置的权限！</p>
                    </div>
                    <div class="alert alert-danger alert-dismissible hidden errMsgDiv" role="alert">
                        <strong>Error! </strong><span class="errMsg"></span>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" id="removeOwnerButton" class="btn btn-primary">确认</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</@baseHtml>