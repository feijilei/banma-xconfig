<#include "/common/baseHtml.ftl" />
<@baseHtml localJsFiles=[
        "plugin/tokenfield/bootstrap-tokenfield.min.js",
        "plugin/tokenfield/typeahead0.10.1.js",
        "plugin/datatables/js/jquery.dataTables.min.js",
        "plugin/doT1.0.3.min.js",
        "javascripts/page/projectSetting.js"
    ] remoteJsFiles=[] localCssFiles=[
        "plugin/datatables/css/jquery.dataTables.min.css",
        "plugin/tokenfield/css/tokenfield-typeahead.min.css",
        "plugin/tokenfield/css/bootstrap-tokenfield.min.css"
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
    <div>
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
</@baseHtml>