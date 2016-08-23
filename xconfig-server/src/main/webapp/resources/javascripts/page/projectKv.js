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

    $("div.gridToolbar").html($("#gridToolbar").html());

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
    $('#addKvModal').on('show.bs.modal', function (event) {
        $("#addKvModal .errMsgDiv").addClass("hidden");
        //$("#addButton").prop("disabled",false);
    });
    var addKvForm = $("#addKvForm").ajaxForm({
        url:basepath+"/main/addKvs",
        type:"POST",
        success:function(data){
            if(data.code == 0){
                window.location = basepath+"/main/project?project="+project+"&profile="+profile;
            }else{
                //console.log(data.msg);
                $("#addKvModal .errMsgDiv").removeClass("hidden");
                $("#addKvModal .errMsg").text(data.msg);
            }
            $("#addKvButton").prop("disabled",false);
        }
    });
    $("#addKvButton").on("click",function(){
        $(this).prop("disabled",true);
        addKvForm.submit();
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
            $.post(basepath+"/main/removeKv",{"key":key,"profile":profile,"project":project},function(data){
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

    //编辑依赖
    var engine = new Bloodhound({
        remote: basepath+"/main/projectList?q=%QUERY",
        //local:[{value:"red"},{value:"blue"}],
        datumTokenizer: function(d) {
            return Bloodhound.tokenizers.whitespace(d.value);
        },
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        sufficient: 15
    });

    engine.initialize();

    $('#deps').tokenfield({
        //tokens:"red",
        typeahead: [null, {
            source: engine.ttAdapter(),
            limit: 15
        }]
    });

    $('#editDepModal').on('show.bs.modal', function (event) {
        $("#editDepModal .errMsgDiv").addClass("hidden");
    });
    var editDepForm = $("#editDepForm").ajaxForm({
        url:basepath+"/main/updateProjectDeps",
        type:"POST",
        success:function(data){
            if(data.code == 0){
                window.location = basepath+"/main/project?project="+project+"&profile="+profile;
            }else{
                $("#editDepModal .errMsgDiv").removeClass("hidden");
                $("#editDepModal .errMsg").text(data.msg);
            }
            $("#editDepButton").prop("disabled",false);
        }
    });
    $("#editDepButton").on("click",function(){
        $(this).prop("disabled",true);
        editDepForm.submit();
    });

    //增加profile
    $('#addProfileModal').on('show.bs.modal', function (event) {
        $("#addProfileModal .errMsgDiv").addClass("hidden");
    });
    var addProfileForm = $("#addProfileForm").ajaxForm({
        url:basepath+"/main/addProfile",
        type:"POST",
        success:function(data){
            if(data.code == 0){
                window.location = basepath+"/main/project?project="+project+"&profile="+profile;
            }else{
                $("#addProfileModal .errMsgDiv").removeClass("hidden");
                $("#addProfileModal .errMsg").text(data.msg);
            }
            $("#addProfileButton").prop("disabled",false);
        }
    });
    $("#addProfileButton").on("click",function(){
        $(this).prop("disabled",true);
        addProfileForm.submit();
    });

    //删除profile
    $('#removeProfileModal').on('show.bs.modal', function (event) {
        $("#removeProfileModal .errMsgDiv").addClass("hidden");
    });
    var removeProfileForm = $("#removeProfileForm").ajaxForm({
        url:basepath+"/main/removeProfile",
        type:"POST",
        success:function(data){
            if(data.code == 0){
                window.location = basepath+"/main/project?project="+project+"&profile="+profile;
            }else{
                $("#removeProfileModal .errMsgDiv").removeClass("hidden");
                $("#removeProfileModal .errMsg").text(data.msg);
            }
            $("#removeProfileButton").prop("disabled",false);
        }
    });
    $("#removeProfileButton").on("click",function(){
        $(this).prop("disabled",true);
        removeProfileForm.submit();
    });

    $("input[type='search']").focus();
})