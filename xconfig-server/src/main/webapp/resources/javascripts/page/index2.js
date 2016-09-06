jQuery(document).ready(function($){

    //滚动条
    var showOffset = 100;
    var isShowIndexBar = false;
    $(window).scroll(function(event){
        if($(window).scrollTop() > showOffset){
            if(!isShowIndexBar){
                $(".letterIndex").removeClass("hidden");
                isShowIndexBar = true;
            }
        }else{
            if(isShowIndexBar){
                $(".letterIndex").addClass("hidden");
                isShowIndexBar = false;
            }
        }
    });

    $("body").scrollspy({target:".letterIndex"});

    //搜索
    $("#search").autocomplete({
        source: availableTags,
        select:function(event,ui){
            window.location = basepath + "/main/project?project="+ui.item.value;
        }
    });

    //增加project
    $('#addProjectModal').on('show.bs.modal', function (event) {
        $("#addProjectModal .errMsgDiv").addClass("hidden");
    });
    var addProjectForm = $("#addProjectForm").ajaxForm({
        url:basepath+"/main/addProject",
        type:"POST",
        success:function(data){
            if(data.code == 0){
                window.location = basepath+"/main/index";
            }else{
                $("#addProjectModal .errMsgDiv").removeClass("hidden");
                $("#addProjectModal .errMsg").text(data.msg);
            }
            $("#addProjectButton").prop("disabled",false);
        }
    });
    $("#addProjectButton").on("click",function(){
        $(this).prop("disabled",true);
        addProjectForm.submit();
    });

    $("#search").focus();
})