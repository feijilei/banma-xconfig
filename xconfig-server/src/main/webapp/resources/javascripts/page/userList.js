jQuery(document).ready(function($){
    //增加user
    $('#addUserModel').on('show.bs.modal', function (event) {
        $("#addUserModel .errMsgDiv").addClass("hidden");
    });
    var addUserForm = $("#addUserForm").ajaxForm({
        url:basepath+"/user/addUser",
        type:"POST",
        success:function(data){
            if(data.code == 0){
                window.location = basepath+"/user/userList";
            }else{
                $("#addUserModal .errMsgDiv").removeClass("hidden");
                $("#addUserModal .errMsg").text(data.msg);
            }
            $("#addUserButton").prop("disabled",false);
        }
    });
    $("#addUserButton").on("click",function(){
        $(this).prop("disabled",true);
        addUserForm.submit();
    });

    //删除user
    $('#removeUserModal').on('show.bs.modal', function (event) {
        $("#removeUserModal .errMsgDiv").addClass("hidden");
        //$("#editButton").prop("disabled",false);
        var button = $(event.relatedTarget) // Button that triggered the modal
        var userName = button.parents("tr").first().attr("data-key");

        $("#removeUserButton").unbind();
        $("#removeUserButton").bind("click",function(){
            $.post(basepath+"/user/removeUser",{"userName":userName},function(data){
                if(data.code == 0){
                    window.location = basepath + "/user/userList";
                }else{
                    //console.log(data.msg);
                    $("#removeUserModal .errMsgDiv").removeClass("hidden");
                    $("#removeUserModal .errMsg").text(data.msg);
                }
            })
        });
    });
});