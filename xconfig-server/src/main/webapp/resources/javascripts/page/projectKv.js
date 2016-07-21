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

    $("div.gridToolbar").html('<button type="button" class="btn btn-default" data-toggle="modal" data-target="#addModal"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> 新增 </button>');

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
    //$('#addModal').on('show.bs.modal', function (event) {
    //    var button = $(event.relatedTarget) // Button that triggered the modal
    //    var key = button.parents("tr").first().attr("data-key");
    //    var modal = $(this);
    //
    //    var data = kvs[key];
    //    var template = doT.template($("#addModalTemplate").text());
    //    modal.html(template(data));
    //})

    //新增按钮注册事件
    $("#addButton").on("click",function(){
        $("#addForm").submit();
    })
})