<#include "/common/baseHtml.ftl" />
<@baseHtml localJsFiles=["plugin/datatables/js/jquery.dataTables.min.js","plugin/doT1.0.3.min.js","javascripts/page/projectKv.js"] remoteJsFiles=[] localCssFiles=["plugin/datatables/css/jquery.dataTables.min.css"]>
    <style type="text/css">
        .gridToolbar{
            float: right;
        }
        .dataTables_wrapper .dataTables_filter{
            float: left;
        }
    </style>

    <script type="application/javascript">
        var project = "${project?html}";
        var profile = "${profile?html}";
        var kvs = ${kvmap};
    </script>

    <div class="page-header">
        <h1>${project?html}<small>Subtext for header</small></h1>
    </div>

    <ol class="breadcrumb">
        <#list profiles as pf>
            <#if pf == profile>
                <li class="active" >${pf?html}</li>
            <#else>
                <li><a href="${basepath}/main/project?project=${project?html}&profile=${pf?html}" >${pf?html}</a></li>
            </#if>
        </#list>
    </ol>

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
            <tr data-key=${kvVo.key?html} >
                <td>${kvVo.key?html}</td>
                <td>${kvVo.value?html}</td>
                <td>
                    <a style="display: block;text-decoration:none;width: 20em;text-overflow:ellipsis;white-space:nowrap;overflow:hidden;" data-toggle="tooltip" title="${kvVo.description?html}">
                        ${kvVo.description?html}
                    </a>
                </td>

                <td>
                    <a href="#"><span class="glyphicon glyphicon-search" aria-hidden="true" data-toggle="modal" data-target="#detailModal"></span></a>
                    <a href="#"><span class="glyphicon glyphicon-edit" aria-hidden="true"></span></a>
                    <a href="#"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></a>
                </td>
            </tr>
            </#list>
        </tbody>
    </table>

<#--modal and template-->
    <div id="detailModal" class="modal fade">
    </div><!-- /.modal -->

    <script id="detailModalTemplate" type="text/x-dot-template">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">详情</h4>
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
                                <textarea class="form-control" id="xvalue" name="xvalue" rows="2" placeholder="value">{{=it.value}}</textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="description" class="col-sm-2 control-label">描述</label>
                            <div class="col-sm-10">
                                <textarea class="form-control" id="description" name="description" rows="3" placeholder="描述信息">{{=it.description}}</textarea>
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
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary">Save changes</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </script>

    <div id="addModal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">新增配置</h4>
                </div>
                <div class="modal-body">
                    <form id="addForm" class="form-horizontal">
                        <input type="text" class="hide" name="project" value="${project?html}">
                        <input type="text" class="hide" name="profile" value="${profile?html}">

                        <div class="form-group">
                            <label for="xkey" class="col-sm-2 control-label">key</label>
                            <div class="col-sm-10">
                                <div class="input-group">
                                    <span class="input-group-addon">${project?html}.${profile?html}.</span>
                                    <input type="text" class="form-control" id="xkey" name="xkey" placeholder="jdbc.drive">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="xvalue" class="col-sm-2 control-label">value</label>
                            <div class="col-sm-10">
                                <textarea class="form-control" id="xvalue" name="xvalue" rows="2" placeholder="com.mysql.jdbc.Driver"></textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="description" class="col-sm-2 control-label">描述</label>
                            <div class="col-sm-10">
                                <textarea class="form-control" id="description" name="description" rows="3" placeholder="配置jdbc的驱动程序"></textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <div class="checkbox">
                                    <label>
                                        <input id="security" name="security" type="radio" value="N" checked> 普通
                                    </label>
                                    <label>
                                        <input id="security" name="security" type="radio" value="Y"> 高密
                                    </label>
                                </div>
                            </div>
                        </div>
                        <#--<div class="form-group">-->
                            <#--<div class="col-sm-offset-2 col-sm-10">-->
                                <#--<button type="submit" class="btn btn-primary">增加</button>-->
                            <#--</div>-->
                        <#--</div>-->
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" id="addButton" class="btn btn-primary">新增</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</@baseHtml>