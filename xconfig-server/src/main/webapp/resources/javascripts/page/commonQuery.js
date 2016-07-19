jQuery(document).ready(function($){

    $('#chart').highcharts({
        chart: {
            type: 'line'
        },
        title: {
            text: line.title
        },
        subtitle: {
            text: line.subTitle
        },
        xAxis: {
            categories: line.xAxisCategories
        },
        yAxis: {
            title: {
                text: line.yAxisTitle
            }
        },
        plotOptions: {
            line: {
                dataLabels: {
                    enabled: true
                },
                enableMouseTracking: true
            }
        },
        series: line.series
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

    $('#queryForm').bootstrapValidator({
        message: '请注意数据的格式',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        //group: '.form-group',
        fields: {
            startYear: {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '年份不能为空'
                    },
                    digits:{
                        message:'只能输入数字'
                    }
                }
            },
            startQuarter: {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '季度不能为空'
                    },
                    digits:{
                        message:'只能输入数字'
                    }
                }
            },
            startMonth: {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '月份不能为空'
                    },
                    digits:{
                        message:'只能输入数字'
                    }
                }
            },
            startWeek: {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '星期不能为空'
                    },
                    digits:{
                        message:'只能输入数字'
                    }
                }
            },
            startDay: {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '日期不能为空'
                    },
                    digits:{
                        message:'只能输入数字'
                    }
                }
            },
            endYear: {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '年份不能为空'
                    },
                    digits:{
                        message:'只能输入数字'
                    }
                }
            },
            endQuarter: {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '季度不能为空'
                    },
                    digits:{
                        message:'只能输入数字'
                    }
                }
            },
            endMonth: {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '月份不能为空'
                    },
                    digits:{
                        message:'只能输入数字'
                    }
                }
            },
            endWeek: {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '月份不能为空'
                    },
                    digits:{
                        message:'只能输入数字'
                    }
                }
            },
            endDay: {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '日期不能为空'
                    },
                    digits:{
                        message:'只能输入数字'
                    }
                }
            }
        }
    });

    $("#saveAnalysis").bind("click",function(){
        var groupId = $("#id").val();
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

    $(".addBtn").bind("click",function(){
        $("#dataType").val($(this).attr("my-dataType"));

        $("#myModal1").modal();
    })
});