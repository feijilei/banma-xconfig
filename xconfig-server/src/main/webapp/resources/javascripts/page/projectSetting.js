jQuery(document).ready(function ($) {
    $('#removeProjectModal').on('show.bs.modal', function (event) {
        $("#removeProjectModal .errMsgDiv").addClass("hidden");
    });

    $("#removeProjectButton").bind("click",function(){
        $.post(basepath+"/main/removeProject",{"project":project},function(data){
            if(data.code == 0){
                window.location = basepath + "/main/index";
            }else{
                //console.log(data.msg);
                $("#removeProjectModal .errMsgDiv").removeClass("hidden");
                $("#removeProjectModal .errMsg").text(data.msg);
            }
        })
    });
})