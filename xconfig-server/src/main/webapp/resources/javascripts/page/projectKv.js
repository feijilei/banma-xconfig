jQuery(document).ready(function ($) {
    var grid = $("#kvTable").DataTable({
        lengthMenu:[10,25,50,-1],//每页可选大小
        pageLength:25,//每页大小

        dom: '<"top"f<"gridToolbar"><"clear">>rt<"bottom"lip<"clear">>',

        //设置列
        columns:[
            null,
            {orderable:false},
            {orderable:false,width:"20em"},
            {orderable:false,width:"40px"}
        ],

        //语言
        language:{
            "decimal":        "",
            "emptyTable":     "空",
            "info":           "从 _START_ 到 _END_ （共 _TOTAL_ 条数据）",
            "infoEmpty":      "从 0 到 0 （共 0 条数据）",
            "infoFiltered":   "(从 _MAX_ 条数据中过滤)",
            "infoPostFix":    "",
            "thousands":      ",",
            "lengthMenu":     "每页 _MENU_ 条",
            "loadingRecords": "Loading...",
            "processing":     "Processing...",
            "search":         "搜索:",
            "zeroRecords":    "没找到匹配的数据",
            "paginate": {
                "first":      "First",
                "last":       "Last",
                "next":       "Next",
                "previous":   "Previous"
            },
            "aria": {
                "sortAscending":  ": activate to sort column ascending",
                "sortDescending": ": activate to sort column descending"
            }
        }
    });

    $("div.gridToolbar").html('<button type="button" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#addModal"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> key </button>');

    //查看
    $('#detailModal').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget) // Button that triggered the modal
        var key = button.parents("tr").first().attr("data-key");
        var modal = $(this);

        var data = kvs[key];
        var template = doT.template($("#detailModalTemplate").text());
        modal.html(template(data));
    });

    //新增
    $('#addModal').on('show.bs.modal', function (event) {
        $("#addModal .errMsgDiv").addClass("hidden");
        //$("#addButton").prop("disabled",false);
    });
    var addForm = $("#addForm").ajaxForm({
        url:basepath+"/main/addKvs",
        type:"POST",
        success:function(data){
            if(data.code == 0){
                window.location = basepath+"/main/project?project="+project+"&profile="+profile;
            }else{
                //console.log(data.msg);
                $("#addModal .errMsgDiv").removeClass("hidden");
                $("#addModal .errMsg").text(data.msg);
            }
            $("#editButton").prop("disabled",false);
        }
    });
    $("#addButton").on("click",function(){
        $(this).prop("disabled",true);
        addForm.submit();
    });

    //编辑
    $('#editModal').on('show.bs.modal', function (event) {
        $("#editModal .errMsgDiv").addClass("hidden");
        //$("#editButton").prop("disabled",false);
        var button = $(event.relatedTarget) // Button that triggered the modal
        var key = button.parents("tr").first().attr("data-key");
        var modal = $(this);

        var data = kvs[key];
        var template = doT.template($("#editModalTemplate").text());
        modal.html(template(data));
    });
    $("body").delegate("#editButton","click",function(){
        $(this).prop("disabled",true);

        $("#editForm").ajaxForm({
            url: basepath + "/main/editKv",
            type: "POST",
            success: function (data) {
                if (data.code == 0) {
                    window.location = basepath + "/main/project?project=" + project + "&profile=" + profile;
                } else {
                    //console.log(data.msg);
                    $("#editModal .errMsgDiv").removeClass("hidden");
                    $("#editModal .errMsg").text(data.msg);
                }

                $("#editButton").prop("disabled",false);
            }
        }).submit();
    });

    //删除
    $('#removeModal').on('show.bs.modal', function (event) {
        $("#removeModal .errMsgDiv").addClass("hidden");
        //$("#editButton").prop("disabled",false);
        var button = $(event.relatedTarget) // Button that triggered the modal
        var key = button.parents("tr").first().attr("data-key");

        $("#removeButton").unbind();
        $("#removeButton").bind("click",function(){
            $.post(basepath+"/main/removeKv",{"mkey":key},function(data){
                if(data.code == 0){
                    window.location = basepath + "/main/project?project=" + project + "&profile=" + profile;
                }else{
                    //console.log(data.msg);
                    $("#removeModal .errMsgDiv").removeClass("hidden");
                    $("#removeModal .errMsg").text(data.msg);
                }
            })
        });
    });

    //查看所有依赖
    $("#allDep").bind("change",function(){
        window.location = basepath+"/main/project?project=" + project + "&profile=" + profile + "&allDep=" +$(this).prop("checked");
    });
})