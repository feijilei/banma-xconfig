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

    //增加ower
    //$("#addOwer").autocomplete({
    //    minLength:2,
    //    source:basepath+"/project/queryGuestUser"
    //});
    $('#addOwnerModel').on('show.bs.modal', function (event) {
        $("#addOwnerModel .errMsgDiv").addClass("hidden");
    });
    var addOwnerForm = $("#addOwnerForm").ajaxForm({
        url:basepath+"/project/addOwner",
        type:"POST",
        success:function(data){
            if(data.code == 0){
                window.location = basepath+"/project/setting?project="+project;
            }else{
                $("#addOwnerModal .errMsgDiv").removeClass("hidden");
                $("#addOwnerModal .errMsg").text(data.msg);
            }
            $("#addOwnerButton").prop("disabled",false);
        }
    });
    $("#addOwnerButton").on("click",function(){
        $(this).prop("disabled",true);
        addOwnerForm.submit();
    });

    //删除owner
    $('#removeOwnerModal').on('show.bs.modal', function (event) {
        $("#removeOwnerModal .errMsgDiv").addClass("hidden");
        //$("#editButton").prop("disabled",false);
        var button = $(event.relatedTarget) // Button that triggered the modal
        var userName = button.parents("tr").first().attr("data-key");

        $("#removeOwnerButton").unbind();
        $("#removeOwnerButton").bind("click",function(){
            $.post(basepath+"/project/removeOwner",{"userName":userName,"project":project},function(data){
                if(data.code == 0){
                    window.location = basepath+"/project/setting?project="+project;
                }else{
                    //console.log(data.msg);
                    $("#removeOwnerModal .errMsgDiv").removeClass("hidden");
                    $("#removeOwnerModal .errMsg").text(data.msg);
                }
            })
        });
    });
})