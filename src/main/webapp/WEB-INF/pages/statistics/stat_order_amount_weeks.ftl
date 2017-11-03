<!DOCTYPE html>
<html class="not-ie" lang="en" xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <title>订单总额走向图(周)</title>
<#include "/WEB-INF/pages/base/css.ftl">
    <link rel="stylesheet" href="/admins/assets/plugins/data-tables/DT_bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="/admins/assets/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css"/>
<#include "/WEB-INF/pages/base/js.ftl">
<#import "/WEB-INF/pages/base/base.ftl"  as b>

    <style>
        #main{margin:0 auto;width: 900px;height:600px;}
        .dayYearInput{width:100px;}
        .marginSel{margin-left:20px;}
        .yearTargetSpan{margin-left:20px;}
        .yearTargetInput{width:100px;}

    </style>
</head>
<body>

<div class="page-content extended ">

    <form  id="searchForm" name="searchForm" class="form-horizontal">
        <div class="form-group" style="display: inline-block">
            <span align="right" class="control-label">年份：</span>
            <div class="input-group" style="display: inline-block;">
                <input class="dayYearInput"  type="text" id="day_year" validate="{required:true"/>
            </div>
        </div>

        <div class="form-group" style="display: inline-block;margin-left: 30px;margin-right: 10px;">
            <span align="right" class="control-label">目标值：</span>
            <div class="input-group" style="display: inline-block">
                <input class="yearTargetInput" type="number" id="year_target" value="50000" validate="{required:true,nonnegativeFloat:true}"/>万
            </div>
        </div>

        <button type="button" class="btn blue btn-sm search-btn marginSel" onclick="searchButtonClick()">
            <i class="fa"></i> 统计
        </button>
    </form>

    <hr>

    <div id="main"></div>

</div>




<script src="/admins/assets/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
<script src="/admins/js/form.js" type="text/javascript"></script>
<!-- 引入 ECharts 文件 -->
<script src="/admins/assets/plugins/echarts/echarts.min.js"></script>
<script src="/admins/assets/plugins/laydate-v5.0.4/laydate.js"></script>
<#--<script src="/admins/assets/plugins/date/laydate.dev.js"></script>-->
<script src="/admins/assets/plugins/moment/moment.min.js"></script>
<script src="/admins/js/form.js" type="text/javascript"></script>


<script>
    var v_search_form;

    $(document).ready(function(){
        v_search_form = formValidation('searchForm');

        //这个方法需要放在ready里
        initChart();
        getValueAndRefreshChart();
    });

    /**
     * 查询按钮点击事件
     */
    function searchButtonClick(){
        var dayYear = $("#day_year").val();
        var yearTarget = $("#year_target").val();
        if((!dayYear) || dayYear==""){
            bootbox.alert("请选择统计年份！");
        }
        if((!yearTarget) || yearTarget==""){
            bootbox.alert("请输入年目标值！");
        }

        getValueAndRefreshChart(dayYear,yearTarget);
    }

</script>

<script type="text/javascript">
    var colors = ['#5793f3', '#d14a61', '#675bba'];
    var myChart = echarts.init(document.getElementById('main'));

    var option = {
        title : {
            text: '订单总额走向图',
            subtext: '按周统计总额',
            x: 'center',
            align: 'right'
        },
        grid: {
            bottom: 90
        },
        toolbox: {
            feature: {
                restore: {},
                saveAsImage: {}
            }
        },
        tooltip : {
            trigger: 'axis',
            formatter: '{b} <br/> {a} : {c} (万)',
            axisPointer: {
                type: 'cross',
//                animation: false,
                label: {
                    backgroundColor: '#505765'
                }
            }
        },
        legend: {
            data:['订单金额'],
            x: 'left'
        },
        dataZoom: [
            {
                show: true,
                realtime: true,
                start: 80,
                end: 100
            },
            {
                type: 'inside',
                realtime: true,
                start: 80,
                end: 100
            }
        ],
        xAxis : [
            {
                type : 'category',
                boundaryGap : false,
                axisLine: {onZero: false},
                data : []
            }
        ],
        yAxis: [

            {
                name: '订单金额',
                type: 'value',
                position: 'left',
                max: 50000,
                axisLabel: {
                    formatter: '{value} 万'
                }
            }
        ],
        series: [
            {
                name:'订单金额',
                type:'line',
                areaStyle: {
                    normal: {}
                },
                lineStyle: {
                    normal: {
                        width: 1
                    }
                },
                markArea: {
                    silent: true,
                    data: [[{
                        xAxis: '第0周'
                    }, {
                        xAxis: '第1周'
                    }]]
                },
                markLine: {
                    silent: false,
                    symbol:'none',
                    itemStyle:{
                        normal:{
                            color:'#675bba',
                            label:{
                                show:true,
                                formatter:'{c} 万',
                                textStyle:{
                                    fontSize:15,
                                    fontWeight:'bold'
                                }
                            }
                        }
                    },
                    data: [{
                        yAxis: 50000
                    }
                    ]
                },
                label: {
                    normal: {
                        show: true,
                        position: 'top',
                        formatter: '{c} 万'
                    }
                },
                data:[

                ]
            }
        ]
    };


    function initChart(){
        resizeMainDivSize();
        myChart.setOption(option);
    }

    /**
     * 将chart的高度，绑定当前窗口的高度
     */
    function resizeMainDivSize(){
        $(window).resize(function(){
            myChart.resize();
        });
        var totalHeight = $(window).height();
        var chartHeight = totalHeight-90;
        var totalWidth = $(window).width();
        var chartWidth = totalWidth-50;
        $("#main").height(chartHeight);
        $("#main").width(chartWidth);
        myChart.resize();
    }


    /**
     *在柱子中间展示数据
     */
    function getValueAndRefreshChart(dayYear,yearTarget){
        if(!dayYear){
            dayYear = thisYear;
        }

        var mubiaoValue = 50000;//年目标
        if(yearTarget>0){
            mubiaoValue = yearTarget;//年目标
        }

//        var cycle = [
//            '第1周', '第2周', '第3周', '第4周', '第5周', '第6周', '第7周', '第8周', '第9周', '第10周',
//            '第11周', '第12周', '第13周', '第14周', '第15周', '第16周', '第17周', '第18周', '第19周', '第20周',
//            '第21周', '第22周', '第23周', '第24周', '第25周', '第26周', '第27周', '第28周', '第29周', '第30周',
//            '第31周', '第32周', '第33周', '第34周', '第35周', '第36周', '第37周', '第38周', '第39周', '第40周',
//            '第41周', '第42周', '第43周', '第44周', '第45周', '第46周', '第47周', '第48周', '第49周', '第50周'
//        ];
//        var dataList = [
//            0.00,1.55*10000,20.88*10000,50.13*10000,60.12*10000,70.25*10000,150.53*10000,200.15*10000,250.33*10000,500.19*10000,
//            600.00*10000,712.55*10000,800.88*10000,912.13*10000,1000.13*10000,1121.13*10000,2132.13*10000,2544.13*10000,2812.13*10000,3122.13*10000,
//            3600.00*10000,3712.55*10000,3800.88*10000,3912.13*10000,4000.13*10000,10121.13*10000,12132.13*10000,12544.13*10000,12812.13*10000,13122.13*10000,
//            10600.00*10000,17012.55*10000,18000.88*10000,19112.13*10000,20000.13*10000,25121.13*10000,28132.13*10000,32544.13*10000,38212.13*10000,43122.13*10000,
//            46000.00*10000,47102.55*10000,48000.88*10000,49012.13*10000,50000.13*10000,51121.13*10000,52132.13*10000,52544.13*10000,52812.13*10000,63122.13*10000
//        ];
//        var dataNowMax = 63122.13*10000;

        getValueListByAjax(dayYear,mubiaoValue);
    }


    /**
     * 通过ajax查询数据内容
     */
    function getValueListByAjax(dayYear,mubiaoValue){
        $.ajax("${BASEPATH}/statistics/listStatOrderAmountOfWeeks", {
            data: {dayYear:dayYear
            },
            type: "POST",
//            async:false,
        }).always(function () {
        }).done(function (data) {
            if (data.code==200) {
                var dataSay = data.data;
                if(dataSay){
                    var statList = dataSay.statOrderLoanAmountList;
                    var cycle = new Array();
                    var dataList = new Array();
                    if(statList && statList.length>0){
                        for(var i=0;i<statList.length;i++){
                            cycle.push(statList[i].statCycle);
                            dataList.push(statList[i].orderAmount);
                        }
                    }
                    var dataNowMax = dataSay.maxOrderAmount;//最大订单总额

                    refreshChartByData(cycle,dataList,dataNowMax,mubiaoValue);
                }
            } else {
                bootbox.alert(data.msg);
            }
        }).fail(function () {
            bootbox.alert("系统错误！");
        });
    }


    /**
     *根据数据刷新图形
     */
    function refreshChartByData(cycle,dataList,dataNowMax,mubiaoValue){
        //xAxis 的data值
        var retCycle = formatterCycle(cycle);
        option.xAxis[0].data = retCycle;

        //yAxis max
        var yMaxValue = formatterDataWanValue(dataNowMax,0);
        if(yMaxValue<mubiaoValue){
            yMaxValue = mubiaoValue;
        }
        option.yAxis[0].max = getYMax(yMaxValue);

        //series 的markArea
        var markAreaBegin = 0;
        var markAreaEnd = 0;
        if(cycle.length>1){
            markAreaBegin = cycle.length-2;
            markAreaEnd = cycle.length-1;
        }
        option.series[0].markArea.data[0][0].xAxis=cycle[markAreaBegin];
        option.series[0].markArea.data[0][1].xAxis=cycle[markAreaEnd];

        //series 的markLine
        option.series[0].markLine.data[0].yAxis = mubiaoValue;

        //series 的data
        var retDataList = formatterDataList(dataList);
        option.series[0].data = retDataList;


        //DataRoom 的start值
        var start = getDataRoomStartValue(cycle.length);
        option.dataZoom[0].start = start;
        option.dataZoom[1].start = start;

        myChart.setOption(option,true);
    }


    /**
     *根据周期数，获取DataRoom 的start数
     **/
    function getDataRoomStartValue(dataSize){
        var start = 0;
        if(dataSize<=10){
            start = 0;
        }else if(dataSize>10){
            start = 100-parseInt((10*100)/dataSize);
        }
        return start;
    }


    /**
     *格式化周期显示
     **/
    function formatterCycle(cycle){
        var retCycle = cycle.map(function (str) {
            return str.replace('-', '\n-\n');
        })
        return retCycle;
    }

    /**
     *格式化数据显示
     **/
    function formatterDataList(dataList){
        var retDataList = dataList.map(function (data) {
            return formatterDataWanValue(data);
        })
        return retDataList;
    }

    /**
     *保留两位小数，四舍五入  单位为万
     * @param value
     * @param fixNum 保留小数位数
     **/
    function formatterDataWanValue(value,fixNum){
        var chuNum = value/10000;
        var retValue = fixNumber(chuNum,fixNum);
        return retValue;
    }

    /**
     *保留两位小数 四舍五入
     */
    function fixNumber(value,fixNum){
        var retValue = 0;
        if(fixNum != null && fixNum != undefined){
            retValue = value.toFixed(fixNum);
        }else{
            retValue = value.toFixed(2);
        }
        return retValue;
    }


    function getYMax(dataNowMax){
        var wanValue = parseInt(dataNowMax/10000)+1;
        return wanValue*10000;
    }

</script>


<script>
    var date = new Date();
    var thisYear = moment(date).format("YYYY");
    laydate.render({
        elem: '#day_year',
        format:'yyyy',
        min:'2017',
        max:thisYear,
        value:thisYear,
        type:'year'
    });
</script>




<#--此页面不需要登录即可访问   http://www.fruit.com/admin/statistics/statOrderAmountWeeks-->



</body>
</html>