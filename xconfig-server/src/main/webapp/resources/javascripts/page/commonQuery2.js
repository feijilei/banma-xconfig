jQuery(document).ready(function($){
    $('#chart').highcharts({
        chart: {
            type: 'column'
        },
        title: {
            text: columnChart.title
        },
        /*subtitle: {
            text: 'Source: WorldClimate.com'
        },*/
        xAxis: {
            categories: columnChart.categories
        },
        yAxis: {
            title: {
                text: columnChart.yTitle
            }
        },
        credits: {
            enabled: false
        },
        tooltip: {
            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
            '<td style="padding:0"><b>{point.y:.1f}</b></td></tr>',
            footerFormat: '</table>',
            shared: true,
            useHTML: true
        },
        plotOptions: {
            column: {
                pointPadding: 0.2,
                borderWidth: 0
            }
        },
        series: columnChart.series
    });
    /*var tableData = {
        "total_rows": "2",
        "page_data": [{
                "id": "111",
                "lastname": "Diaz",
                "firstname": "Kai",
                "email": "odio.Aliquam@Phasellus.org",
                "gender": "female"
            },{
                "id": "112",
                "lastname": "Snider",
                "firstname": "Nelle",
                "email": "vulputate@nonlobortis.org",
                "gender": "female"
            }
        ]
    };

    $("#grid").bs_grid(tableData);*/


    $("#saveAnalysis").bind("click",function(){
        var analysisStr = $("#analysisContext").val();
        var param = {};
        param.groupId = groupId;
        param.analysis = analysisStr;
        $.post(basepath+"/common/updateGroupAnalysis",param,function(data){
            if(data.code == 200){
                window.location.reload(true);
            }else{
                $("#errMsg").text(data.msg);
                $("#errMsg").removeClass("hidden");
            }
        });
    });
});