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
});