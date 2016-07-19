jQuery(document).ready(function($){

    function submitLogin(){
        var username = $("#j_username").val();
        if(username == undefined || username.length == 0){
            $("#errmsg").text("请输入用户名");
            $(".error_message").removeClass("hide");
            return ;
        }

        var password = $("#j_password").val();
        if(password == undefined || password.length == 0){
            $("#errmsg").text("请输入密码");
            $(".error_message").removeClass("hide");
            return ;
        }
        $("#login_form").submit();
    }

    $("#login_submit").bind("click",function(){
        submitLogin();
    });

    $("#j_password").bind("keydown",function(event){
        if(event.keyCode == 13){
            submitLogin();
        }
    });
});
