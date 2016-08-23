jQuery(document).ready(function($){
    $( "#sortList" ).sortable();
    $( "#sortList" ).disableSelection();

    $("#addBtn").bind("click",function(){
        var profile = $.trim($("#addProfile").val());
        if(profile != ""){
            $("#sortList").append('<li id='+profile+' class="list-group-item"><span class="glyphicon glyphicon-sort" aria-hidden="true" ></span>&nbsp;'+profile+'<button type="button" class="delBtn close" aria-label="Close"><span aria-hidden="true">&times;</span></button></li>')
        }

        $("#addProfile").val("");
    });

    $("#sortList").delegate(".delBtn","click",function(){
        $(this).parent("li").remove();
    });

    $("#save").bind("click",function(){
        var profileArr = $( "#sortList" ).sortable( "toArray" );
        var profiles = profileArr.join(",");
        $.post(basepath+"/project/saveProfilesOrder",{"profiles":profiles},function(data){
            if(data.code != 0){
                alert(data.msg);
            }else{
                window.location = basepath+"/project/profilesOrder";
            }
        },"json");
    });

    $("#cancel").bind("click",function(){
        window.location = basepath+"/project/profilesOrder";
    })
});