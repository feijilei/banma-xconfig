<#include "/common/baseHtml.ftl" />
<@baseHtml localJsFiles=["plugin/jquery-ui-1.12.0.custom/jquery-ui.min.js"] remoteJsFiles=[] localCssFiles=["plugin/jquery-ui-1.12.0.custom/jquery-ui.min.css"] bodyContainer=false>
    <style type="text/css">
        .my_header{
            display: -webkit-box;
            display: -webkit-flex;
            display: -ms-flexbox;
            display: flex;
            -webkit-box-pack: center;
            -ms-flex-pack: center;
            -webkit-justify-content: center;
            justify-content: center;
            -webkit-box-align: center;
            -webkit-align-items: center;
            -ms-flex-align: center;
            align-items: center;
            background-size: cover;
            min-height: 520px;
            height: 520px;
            padding-top: 0;
            padding-bottom: 0;
            color: #ffffff;
            margin: 0 auto;
            width: 100%;
            text-align: center;
            background-image: 8121991;
            background-image: -webkit-radial-gradient(center 800px, 70vw 40vw, #e0e4f3, rgba(255, 255, 255, 0));
            background-image: radial-gradient(center 800px, 70vw 40vw, #e0e4f3, rgba(255, 255, 255, 0));
            background-repeat: no-repeat;
            background-color: #f2f4fa;
        }
        .my_header_img{
            background-image: url(https://reactioncommerce.com/images/home/hero4.jpg);
        }

        .my_header .overlay{
            display: -webkit-box;
            display: -webkit-flex;
            display: -ms-flexbox;
            display: flex;
            -webkit-box-pack: center;
            -webkit-justify-content: center;
            -ms-flex-pack: center;
            justify-content: center;
            -webkit-box-align: center;
            -webkit-align-items: center;
            -ms-flex-align: center;
            align-items: center;
            -webkit-box-orient: vertical;
            -webkit-box-direction: normal;
            -webkit-flex-direction: column;
            -ms-flex-direction: column;
            flex-direction: column;
            width: 100%;
            height: 100%;
            padding: 20px 30px;
            background-color: rgba(5, 24, 33, 0.6);

        }
    </style>

    <div class="my_header my_header_img">
        <div class="overlay">
            <div class="container">
                <h1>xConfig</h1>
                <p>程序员有三种美德:懒惰,急躁和傲慢...</p>
                <p>程序员有三种美德:懒惰,急躁和傲慢...</p>
                <p>程序员有三种美德:懒惰,急躁和傲慢...</p>
                <p>程序员有三种美德:懒惰,急躁和傲慢...</p>
                <span class="input-group input-group-lg">
                    <span class="input-group-btn">
                        <button class="btn btn-default" type="button">Search</button>
                    </span>
                    <input id="search" type="text" class="form-control" placeholder="Search for...">
                </span>
            </div>
        </div>
    </div>


    <#--<div style="font-size: x-large">-->
        <#--<#list projects as project>-->
        <#--&lt;#&ndash;todo 为这些标签增加不同的颜色 &ndash;&gt;-->
            <#--<a class="label label-info" href="${basepath}/main/project?project=${project?html}">${project?html}</a>-->
        <#--</#list>-->
    <#--</div>-->

    <script type="text/javascript">
        var availableTags = ${projects};
        $("#search").autocomplete({
            source: availableTags,
            select:function(event,ui){
                window.location = basepath + "/main/project?project="+ui.item.value;
            }
        });
    </script>
</@baseHtml>