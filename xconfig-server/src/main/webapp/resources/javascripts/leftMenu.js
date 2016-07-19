jQuery(document).ready(function($){

    $.post(basepath+"/index/leftTree",{},function(data){
        $("#leftMenu").treeview({
            data:data,
            showBorder:false,
            onNodeSelected:function(event,data){
                if("c" == data.urlType || "c2" == data.urlType){
                    window.location.href = basepath+data.url+"&selectNode="+data.nodeId;
                }else if("l" == data.urlType) {
                    window.location.href = basepath + data.url + "?selectNode=" + data.nodeId;
                }else{

                }
            }
        });

        //$('#leftMenu').treeview('expandAll');
        $("#leftMenu").treeview("collapseAll",{silent:true});

        if(typeof(selectNode) != "undefined" && selectNode != -1){
            $('#leftMenu').treeview('revealNode', [ selectNode, { silent: true } ]);
            $("#leftMenu").treeview("selectNode",[ selectNode, { silent: true } ]);
        }else{
            var sNode = $.getUrlParam("selectNode");
            if(sNode != null){
                $('#leftMenu').treeview('revealNode', [ parseInt(sNode), { silent: true } ]);
                $("#leftMenu").treeview("selectNode",[parseInt(sNode), { silent: true } ]);
            }
        }

    })
})