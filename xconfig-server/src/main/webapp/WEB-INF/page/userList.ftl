<#include "/common/baseHtml.ftl" />
<#include "/common/paging.ftl" />
<#assign roleMap = {"10":"guest","30":"master"} />
<@baseHtml localJsFiles=[
        "javascripts/page/userList.js"
    ] remoteJsFiles=[] localCssFiles=[] curMenu="user">
    <script type="application/javascript">
    </script>

    <h1 class="row">
        <div class="col-sm-11">
            用户管理
        </div>
        <div class="col-sm-1">
            <div class="pull-right">
                <@dataRight role=role url="/user/addUser">
                <a href="javascript:void(0)"><span class="glyphicon glyphicon-plus-sign" aria-hidden="true" data-toggle="modal" data-target="#addUserModal"></span></a>
                </@dataRight>
            </div>
        </div>
    </h1>
    <div>
        <table id="kvTable" class="table table-condensed table-hover" cellspacing="0" width="100%">
            <thead>
            <tr>
                <th>用户名</th>
                <th>昵称</th>
                <th>角色</th>
                <th>创建时间</th>
                <th>操作</th>
            </tr>
            </thead>

            <tbody>
                <#list users.t as user>
                    <tr data-key=${user.userName?html}>
                        <td>${user.userName?html}</td>
                        <td>${user.userNike?html}</td>
                        <td>${roleMap[user.role?string]}</td>
                        <td>${user.createTime}</td>
                        <td>
                            <@dataRight role=role url="/user/removeUser">
                            <a href="javascript:void(0)"><span class="glyphicon glyphicon-remove" aria-hidden="true" data-toggle="modal" data-target="#removeUserModal"></span></a>
                            </@dataRight>
                        </td>
                    </tr>
                </#list>
            </tbody>
        </table>
        <@paging pageIndex=users.pageNum+1 pageCount=users.pageCount path="/user/userList?un=${un?html}&pn="/>
    </div>

    <#--Modal-->
    <div id="addUserModal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title"><span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span> 新增用户</h4>
                </div>
                <div class="modal-body">
                    <form id="addUserForm" class="form-horizontal">
                        <div class="form-group">
                            <label for="userName" class="col-sm-2 control-label">email</label>
                            <div class="col-sm-10">
                                <input type="email" class="form-control" id="userName" name="userName" placeholder="email">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="password" class="col-sm-2 control-label">密码</label>
                            <div class="col-sm-10">
                                <input type="password" class="form-control" id="password" name="password">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="userNike" class="col-sm-2 control-label">昵称</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="userNike" name="userNike" placeholder="昵称">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <div class="checkbox">
                                    <label>
                                        <input name="role" type="radio" value="10" aria-describedby="helpBlock" checked> guest
                                    </label>
                                    <label>
                                        <input name="role" type="radio" value="30" aria-describedby="helpBlock"> master
                                    </label>
                                    <span id="helpBlock" class="help-block">请注意，这里选择的是用户默认的角色，选择master将会使当前用户拥有所有项目修改权限，请慎重！</span>
                                </div>
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
                    <button type="button" id="addUserButton" class="btn btn-primary">新增</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

    <div id="removeUserModal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title text-danger"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span> 删除用户</h4>
                </div>
                <div class="modal-body">
                    <div>
                        <p class="text-danger"><span class="glyphicon glyphicon-warning-sign" aria-hidden="true"></span> 警告：您正在删除用户！</p>
                    </div>
                    <div class="alert alert-danger alert-dismissible hidden errMsgDiv" role="alert">
                        <strong>Error! </strong><span class="errMsg"></span>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" id="removeUserButton" class="btn btn-primary">确认</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

</@baseHtml>