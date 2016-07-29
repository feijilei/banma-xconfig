jQuery(document).ready(function($){
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