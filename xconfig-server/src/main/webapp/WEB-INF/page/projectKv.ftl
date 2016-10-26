<#include "/common/baseHtml.ftl" />
<@baseHtml localJsFiles=[
        "plugin/tokenfield/bootstrap-tokenfield.min.js",
        "plugin/tokenfield/typeahead0.10.1.js",
        "plugin/datatables/js/jquery.dataTables.min.js",
        "plugin/doT1.0.3.min.js",
        "javascripts/page/projectKv.js"
    ] remoteJsFiles=[] localCssFiles=[
        "plugin/datatables/css/jquery.dataTables.min.css",
        "plugin/tokenfield/css/tokenfield-typeahead.min.css",
        "plugin/tokenfield/css/bootstrap-tokenfield.min.css"
    ]>
    <style type="text/css">
        .gridToolbar{
            float: right;
        }
        .dataTables_wrapper .dataTables_filter{
            float: left;
        }

        /*能够解决bootstrap弹出框太窄的问题*/
        .popover {
            max-width: 700px;
        }
    </style>

    <script type="application/javascript">
        var project = "${project?html}";
        var profile = "${profile?html}";
        var kvs = ${kvmap};
        <#--var lostKeyStr = "${lostKeyStr}";-->
    </script>

    <#--<div class="page-header">-->
        <#--<h1>${project?html}<small>Subtext for header</small></h1>-->
    <#--</div>-->

    <h1 class="row">
        <div class="col-sm-10">
            ${project?html}
            <span style="font-size: x-small">依赖项目：
                <span>
                    <#if dependencies?size gt 0>
                        <#list dependencies as dep>
                            <#if dep_index%3 = 0>
                                <a class="label label-primary" href="${basepath}/main/project?project=${dep?html}&profile=${profile?html}">${dep?html}</a>
                            <#elseif dep_index%3 = 1>
                                <a class="label label-success" href="${basepath}/main/project?project=${dep?html}&profile=${profile?html}">${dep?html}</a>
                            <#elseif dep_index%3 = 2>
                                <a class="label label-info" href="${basepath}/main/project?project=${dep?html}&profile=${profile?html}">${dep?html}</a>
                            </#if>
                        </#list>
                        <@dataRight role=role url="/main/updateProjectDeps">
                            <a href="javascript:void(0)"><span class="glyphicon glyphicon-cog" aria-hidden="true" data-toggle="modal" data-target="#editDepModal"></span></a>
                        </@dataRight>
                        &nbsp;
                        <label>
                            <input type="checkbox" id="allDep" <#if allDep?? && allDep == "true">checked</#if>> 加载所有依赖
                        </label>
                    <#else>
                        空&nbsp;<@dataRight role=role url="/main/updateProjectDeps"><a href="javascript:void(0)"><span class="glyphicon glyphicon-cog" aria-hidden="true" data-toggle="modal" data-target="#editDepModal"></span></a></@dataRight>
                    </#if>
                </span>
            </span>
        </div>
        <div class="col-sm-2">
            <div class="pull-right">
                <#if lostKeyStr?size &gt; 0>
                    <a href="javascript:void(0)" style="color: red" id="lostKeyCheck"><span class="glyphicon glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span></a>
                </#if>
                <@dataRight role=role url="/project/setting">
                    <a href="${basepath}/project/setting?project=${project?html}"><span class="glyphicon glyphicon-cog" aria-hidden="true"></span></a>
                </@dataRight>
            </div>
        </div>
    </h1>
    <h6>client列表：<#if clientIps?? && clientIps?size gt 0><#list clientIps as ip><span class="label label-info" style="display: inline-block;margin-top: 1px;">${ip?html}</span>&nbsp;</#list><#else>无</#if></h6>
    <ul class="nav nav-tabs">
        <#list profiles as pf>
            <#if pf == profile>
                <li role="presentation" class="active"><a href="#">${pf?html}</a></li>
            <#else>
                <li role="presentation"><a href="${basepath}/main/project?project=${project?html}&profile=${pf?html}">${pf?html}</a></li>
            </#if>
        </#list>
        <li role="presentation" class="dropdown">
            <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">
                <span class="glyphicon glyphicon-cog" aria-hidden="true"></span> <span class="caret"></span>
            </a>
            <ul class="dropdown-menu">
                <@dataRight role=role url="/main/addProfile">
                    <li data-toggle="modal" data-target="#addProfileModal"><a href="javascript:void(0)"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> profile</a></li>
                </@dataRight>
                <@dataRight role=role url="/main/removeProfile">
                    <li data-toggle="modal" data-target="#removeProfileModal"><a href="javascript:void(0)"><span class="glyphicon glyphicon-minus" aria-hidden="true"></span> profile</a></li>
                </@dataRight>
            </ul>
        </li>
    </ul>
    <div style="margin-top: 10px">
        <table id="kvTable" class="display" cellspacing="0" width="100%">
            <thead>
                <tr>
                    <th>键</th>
                    <th>值</th>
                    <th>描述</th>
                    <th>操作</th>
                </tr>
            </thead>

            <tbody>
                <#list kvVos as kvVo>
                <tr data-key=${kvVo.key} >
                    <td style="word-break: break-all">${kvVo.key}</td>
                    <td style="word-break: break-all">${kvVo.value}</td>
                    <td>
                        <a style="display: block;text-decoration:none;width: 20em;text-overflow:ellipsis;white-space:nowrap;overflow:hidden;" data-toggle="tooltip" title="${kvVo.description}">
                            ${kvVo.description}
                        </a>
                    </td>

                    <td style="text-align: center">
                        <a href="javascript:void(0)"><span class="glyphicon glyphicon-search" aria-hidden="true" data-toggle="modal" data-target="#detailModal"></span></a>
                        <#if project == kvVo.project>
                            <@dataRight role=role url="/main/editKv">
                                <a href="javascript:void(0)"><span class="glyphicon glyphicon-edit" aria-hidden="true" data-toggle="modal" data-target="#editModal"></span></a>
                            </@dataRight>

                            <@dataRight role=role url="/main/removeKv">
                                <a href="javascript:void(0)"><span class="glyphicon glyphicon-remove" aria-hidden="true" data-toggle="modal" data-target="#removeModal"></span></a>
                            </@dataRight>
                        <#else>
                            <@dataRight role=role url="/main/editKv">
                                <span class="glyphicon glyphicon-edit" aria-hidden="true"></span>
                            </@dataRight>

                            <@dataRight role=role url="/main/removeKv">
                                <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                            </@dataRight>
                        </#if>
                    </td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>

<#--modal and template-->
    <#--表格右上角工具条-->
    <script type="text/html" id="gridToolbar">
        <@dataRight role=role url="/main/addKvs">
            <button type="button" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#addKvModal"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> key </button>
        </@dataRight>
    </script>

    <div id="detailModal" class="modal fade">
    </div><!-- /.modal -->

    <script id="detailModalTemplate" type="text/x-dot-template">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title"><span class="glyphicon glyphicon-search" aria-hidden="true"></span> 详情</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal">
                        <div class="form-group">
                            <label for="xkey" class="col-sm-2 control-label">key</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="xkey" name="xkey" placeholder="key" value="{{=it.key}}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="xvalue" class="col-sm-2 control-label">value</label>
                            <div class="col-sm-10">
                                <textarea class="form-control" id="xvalue" name="xvalue" rows="3" placeholder="value">{{=it.value}}</textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="description" class="col-sm-2 control-label">描述</label>
                            <div class="col-sm-10">
                                <textarea class="form-control" id="description" name="description" rows="5" placeholder="描述信息">{{=it.description}}</textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="security" class="col-sm-2 control-label">是否高密</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="security" name="security" placeholder="key" value="{{=it.security}}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="createTime" class="col-sm-2 control-label">创建时间</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="createTime" name="createTime" value="{{=it.createTime}}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="updateTime" class="col-sm-2 control-label">更新时间</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="updateTime" name="updateTime" value="{{=it.updateTime}}">
                            </div>
                        </div>
                    </form>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </script>

    <div id="editModal" class="modal fade">
    </div><!-- /.modal -->

    <script id="editModalTemplate" type="text/x-dot-template">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title"><span class="glyphicon glyphicon-edit" aria-hidden="true"></span> 编辑</h4>
                </div>
                <div class="modal-body">
                    <form id="editForm" class="form-horizontal">
                        <input type="text" class="hide" name="project" value="${project?html}">
                        <input type="text" class="hide" name="profile" value="${profile?html}">

                        <div class="form-group">
                            <label for="key" class="col-sm-2 control-label">key</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="key" name="key"  placeholder="key" readonly value="{{=it.key}}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="xvalue" class="col-sm-2 control-label">value</label>
                            <div class="col-sm-10">
                                <textarea class="form-control" id="xvalue" name="xvalue" rows="3" placeholder="value">{{=it.value}}</textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="description" class="col-sm-2 control-label">描述</label>
                            <div class="col-sm-10">
                                <textarea class="form-control" id="description" name="description" rows="5" placeholder="描述信息">{{=it.description}}</textarea>
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
                    <button type="button" id="editButton" class="btn btn-primary">更新</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </script>

    <div id="addKvModal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> 新增配置</h4>
                </div>
                <div class="modal-body">
                    <form id="addKvForm" class="form-horizontal">
                        <input type="text" class="hide" name="project" value="${project?html}">
                        <input type="text" class="hide" name="profile" value="${profile?html}">

                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <div class="checkbox">
                                    <#list profiles as pf>
                                        <label>
                                            <input  name="profiles" type="checkbox" value="${pf?html}" checked <#if profile == pf> disabled </#if>> ${pf?html}
                                        </label>
                                    </#list>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="xkey" class="col-sm-2 control-label">key</label>
                            <div class="col-sm-10">
                                <div class="input-group">
                                    <span class="input-group-addon">${project?html}.</span>
                                    <input type="text" class="form-control" id="xkey" name="xkey" placeholder="jdbc.drive">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="xvalue" class="col-sm-2 control-label">value</label>
                            <div class="col-sm-10">
                                <textarea class="form-control" id="xvalue" name="xvalue" rows="3" placeholder="com.mysql.jdbc.Driver"></textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="description" class="col-sm-2 control-label">描述</label>
                            <div class="col-sm-10">
                                <textarea class="form-control" id="description" name="description" rows="5" placeholder="配置jdbc的驱动程序"></textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <div class="checkbox">
                                    <label>
                                        <input name="security" type="radio" value="N" checked> 普通
                                    </label>
                                    <label>
                                        <input name="security" type="radio" value="Y"> 高密
                                    </label>
                                </div>
                            </div>
                        </div>
                        <#--<div class="form-group">-->
                            <#--<div class="col-sm-offset-2 col-sm-10">-->
                                <#--<button type="submit" class="btn btn-primary">增加</button>-->
                            <#--</div>-->
                        <#--</div>-->
                        <div class="form-group">
                            <div class="alert alert-danger alert-dismissible hidden col-sm-offset-2 col-sm-10 errMsgDiv" role="alert">
                                <strong>Error! </strong><span class="errMsg"></span>
                            </div>
                        </div>
                    </form>


                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" id="addKvButton" class="btn btn-primary">新增</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

    <div id="removeModal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title text-danger"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span> 删除</h4>
                </div>
                <div class="modal-body">
                    <div>
                        <p class="text-danger"><span class="glyphicon glyphicon-warning-sign" aria-hidden="true"></span> 警告：删除配置可能会导致严重错误，请确认！</p>
                    </div>
                    <div class="alert alert-danger alert-dismissible hidden errMsgDiv" role="alert">
                        <strong>Error! </strong><span class="errMsg"></span>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" id="removeButton" class="btn btn-primary">确认</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

    <div id="editDepModal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title text-danger"><span class="glyphicon glyphicon-cog" aria-hidden="true"></span> 编辑依赖</h4>
                </div>
                <div class="modal-body">
                    <div>
                        <p class="text-danger"><span class="glyphicon glyphicon-warning-sign" aria-hidden="true"></span> 警告：修改依赖可能会导致项目配置发生变化，请慎重！</p>
                    </div>
                    <form id="editDepForm">
                        <input type="text" class="hide" name="project" value="${project?html}">
                        <input type="text" class="form-control" id="deps" name="deps" value="<#list dependencies as pj>${pj?html}<#if pj_has_next>,</#if></#list>" />
                    </form>
                    <div class="alert alert-danger alert-dismissible hidden errMsgDiv" role="alert">
                        <strong>Error! </strong><span class="errMsg"></span>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" id="editDepButton" class="btn btn-primary">更新</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

    <div id="addProfileModal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> 新增profile</h4>
                </div>
                <div class="modal-body">
                    <form id="addProfileForm" class="form-horizontal">
                        <input type="text" class="hide" name="project" value="${project?html}">

                        <div class="form-group">
                            <label for="addProfileName" class="col-sm-2 control-label">profile名称</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="addProfileName" name="addProfileName" placeholder="dev">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">复制源</label>
                            <div class="col-sm-10">
                                <div class="checkbox">
                                    <label>
                                        <input name="cpSource" type="radio" value="none" checked > none
                                    </label>
                                    <#list profiles as pf>
                                        <label>
                                            <input name="cpSource" type="radio" value="${pf?html}"> ${pf?html}
                                        </label>
                                    </#list>
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
                    <button type="button" id="addProfileButton" class="btn btn-primary">新增</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

    <div id="removeProfileModal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title text-danger"><span class="glyphicon glyphicon-minus" aria-hidden="true"></span> 删除环境</h4>
                </div>
                <div class="modal-body">
                    <div>
                        <p class="text-danger"><span class="glyphicon glyphicon-warning-sign" aria-hidden="true"></span> 警告：删除环境将不可恢复，请慎重！</p>
                    </div>
                    <form id="removeProfileForm" class="form-horizontal">
                        <input type="text" class="hide" name="project" value="${project?html}">

                        <div class="form-group">
                            <label class="col-sm-2 control-label">环境名</label>
                            <div class="col-sm-10">
                                <div class="checkbox">
                                    <#list profiles as pf>
                                        <label>
                                            <input name="removeProfile" type="radio" value="${pf?html}"> ${pf?html}
                                        </label>
                                    </#list>
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
                    <button type="button" id="removeProfileButton" class="btn btn-primary">删除</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

    <script id="lostKeysContent" type="text/html" >
        <ul class="list-unstyled">
            <#list lostKeyStr as key>
                <li>${key?html}</li>
            </#list>
        </ul>
        <small style="color:red">缺失配置可能会导致项目错误。</small>
    </script>
</@baseHtml>