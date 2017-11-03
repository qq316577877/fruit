<!DOCTYPE html>
<html class="not-ie" lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <title>每日可贷余额统计图</title>
<#include "/WEB-INF/pages/base/css.ftl">
    <link rel="stylesheet" href="/admins/assets/plugins/data-tables/DT_bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="/admins/assets/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css"/>
<#include "/WEB-INF/pages/base/js.ftl">
<#import "/WEB-INF/pages/base/base.ftl"  as b>
</head>

    <style>
        #main{margin:0 auto;width: 900px;height:600px;}

        .dayInput{width:100px;}
        .markInput{width:100px;}
        .marginSel{margin-left:20px;}
    </style>
<body>

<div class="page-content extended ">

    <form  id="searchForm" name="searchForm" class="form-horizontal">
        <div class="form-group" style="display: inline-block">
            <span align="right" class="control-label">开始时间：</span>
            <div class="input-group" style="display: inline-block;">
                <input class="dayInput"  type="text" id="day_begin" validate="{required:true"/>
            </div>
        </div>

        <div class="form-group" style="display: inline-block;margin-left: 20px;">
            <span align="right" class="control-label">结束时间：</span>
            <div class="input-group" style="display: inline-block;">
                <input class="dayInput"  type="text" id="day_end" validate="{required:true"/>
            </div>
        </div>

        <div class="form-group" style="display: inline-block;margin-left: 20px;">
            <span align="right" class="control-label">警戒线：</span>
            <div class="input-group" style="display: inline-block">
                <input class="markInput" type="number" id="mark_value" value="1000" validate="{required:true,nonnegativeFloat:true}"/>万
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
<!-- 引入 ECharts 文件 -->
<script src="/admins/assets/plugins/echarts/echarts.min.js"></script>

<script src="/admins/assets/plugins/laydate-v5.0.4/laydate.js"></script>
<script src="/admins/assets/plugins/moment/moment.min.js"></script>
<script src="/admins/js/form.js" type="text/javascript"></script>



<script>
    var v_search_form;

    $(document).ready(function(){
        v_search_form = formValidation('searchForm');

        //这个方法需要放在ready里
        initChart();
        var markValue = $("#mark_value").val();
        getValueAndRefreshChart(initBeginTime,initEndTime,markValue);
    });

    /**
     * 查询按钮点击事件
     */
    function searchButtonClick(){
        var beginTime = $("#day_begin").val();
        var endTime = $("#day_end").val();
        if((!beginTime) || beginTime==""){
            bootbox.alert("请选择统计开始时间！");
        }
        if((!endTime) || endTime==""){
            bootbox.alert("请选择统计结束时间！");
        }
        var markValue = $("#mark_value").val();
        if((!markValue) || markValue==""){
            bootbox.alert("请输入可贷余额警戒值！");
        }

        getValueAndRefreshChart(beginTime,endTime,markValue);
    }

</script>

<script type="text/javascript">
    var colors = ['#675bba', '#d14a61', '#5793f3'];
    var myChart = echarts.init(document.getElementById('main'));

    var option = {
        title : {
            text: '可贷余额走向图',
            subtext: '按日统计可贷余额',
            x: 'center',
            align: 'right'
        },
        grid: {
            y:80,
            bottom: 80
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
                animation: false,
                label: {
                    backgroundColor: '#505765'
                }
            }
        },
        legend: {
            data:['可贷余额'],
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
//                name: '可贷余额',
                type: 'value',
                position: 'left',
                max: 40000,
                axisLabel: {
                    formatter: '{value} 万'
                }
            }
        ],
        series: [
            {
                name:'可贷余额',
                type:'line',
                areaStyle: {
                    normal: {color:'#675bba'}
                },
                lineStyle: {
                    normal: {
                        width: 1
                    }
                },
                markArea: {
                    silent: true,
                    data: [
                        [{
                            xAxis: '20170808'
                        }, {
                            xAxis: '20170809'
                        }],
                        [{
                            xAxis: '20170807'
                        }, {
                            xAxis: '20170808'
                        }],
                    ]
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
                        yAxis: 40000
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
    function getValueAndRefreshChart(beginTime,endTime,markValue){
//        var maxLoanValue = 40000;//平台最高可贷额度
////        var markValue = 1000;//低于此数告警
//        var cycle = [
//            '20170701', '20170702', '20170703', '20170704', '20170705', '20170706', '20170707', '20170708', '20170709', '20170710',
//            '20170711', '20170712', '20170713', '20170714', '20170715', '20170716', '20170717', '20170718', '20170719', '20170720',
//            '20170721', '20170722', '20170723', '20170724', '20170725', '20170726', '20170727', '20170728', '20170729', '20170730',
//            '20170731', '20170801', '20170802', '20170803', '20170804', '20170805', '20170806', '20170807', '20170808', '20170809'
//        ];
//        var dataList = [
//            40000*10000,3218.34*10000,3222.34*10000,4218.34*10000,2218.34*10000,1218.34*10000,1018.34*10000,3218.34*10000,2218.34*10000,218.34*10000,
//            30000*10000,2218.34*10000,3222.34*10000,4218.34*10000,2218.34*10000,1218.34*10000,1018.34*10000,3218.34*10000,2218.34*10000,218.34*10000,
//            20000*10000,2218.34*10000,1222.34*10000,12218.34*10000,2218.34*10000,1218.34*10000,1018.34*10000,3218.34*10000,2218.34*10000,218.34*10000,
//            10000*10000,1218.34*10000,3222.34*10000,5218.34*10000,1218.34*10000,1218.34*10000,1018.34*10000,3218.34*10000,2218.34*10000,218.34*10000
//        ];

//        //xAxis 的data值
//        var retCycle = formatterCycle(cycle);
//        option.xAxis[0].data = retCycle;
//
//        //yAxis max
//        option.yAxis[0].max = maxLoanValue;
//
//        //series 的markArea
//        var markAreaBegin = 0;
//        var markAreaEnd = 0;
//        if(cycle.length>1){
//            markAreaBegin = cycle.length-2;
//            markAreaEnd = cycle.length-1;
//        }
//        option.series[0].markArea.data[0][0].xAxis=cycle[markAreaBegin];
//        option.series[0].markArea.data[0][1].xAxis=cycle[markAreaEnd];
//
//        var markList = getMarkAreaIndex(cycle,dataList,markValue*10000);
//        option.series[0].markArea.data = markList;
//
//
//
//        //series 的markLine
//        option.series[0].markLine.data[0].yAxis = maxLoanValue;
//
//        //series 的data
//        var retDataList = formatterDataList(dataList);
//        option.series[0].data = retDataList;
//
//
//        //DataRoom 的start值
//        var start = getDataRoomStartValue(cycle.length);
//        option.dataZoom[0].start = start;
//        option.dataZoom[1].start = start;
//
//        myChart.setOption(option,true);
        getValueListByAjax(beginTime,endTime,markValue);
    }


    /**
     * 通过ajax查询数据内容
     */
    function getValueListByAjax(beginTime,endTime,markValue){
        $.ajax("${BASEPATH}/statistics/listStatLoanableBalanceDays", {
            data: {beginTime:beginTime,
                endTime:endTime
            },
            type: "POST",
//            async:false,
        }).always(function () {
        }).done(function (data) {
            if (data.code==200) {
                var dataSay = data.data;
                if(dataSay){
                    var statList = dataSay.statLoanableBalanceList;
                    var cycle = new Array();
                    var dataList = new Array();
                    if(statList && statList.length>0){
                        for(var i=0;i<statList.length;i++){
                            cycle.push(statList[i].statCycle);
                            dataList.push(statList[i].loanableBalance);
                        }
                    }
                    var loanTotalAmount = formatterDataWanValue(dataSay.loanTotalAmount,0);//平台最大可贷额度

                    refreshChartByData(cycle,dataList,loanTotalAmount,markValue);
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
    function refreshChartByData(cycle,dataList,loanTotalAmount,markValue){
        //xAxis 的data值
        var retCycle = formatterCycle(cycle);

        option.xAxis[0].data = retCycle;

        //yAxis max
        option.yAxis[0].max = loanTotalAmount;

        //series 的markArea
//        var markAreaBegin = 0;
//        var markAreaEnd = 0;
//        if(cycle.length>1){
//            markAreaBegin = cycle.length-2;
//            markAreaEnd = cycle.length-1;
//        }
//        alert(markAreaBegin);
//        alert(markAreaEnd);
//
//        alert(option.series[0].markArea.data[0][0].xAxis);
//        option.series[0].markArea.data[0][0].xAxis=cycle[markAreaBegin];
//        alert(option.series[0].markArea.data[0][1].xAxis);
//        option.series[0].markArea.data[0][1].xAxis=cycle[markAreaEnd];

        var markList = getMarkAreaIndex(cycle,dataList,markValue*10000);
        option.series[0].markArea.data = markList;

        //series 的markLine
        option.series[0].markLine.data[0].yAxis = loanTotalAmount;

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


    function getMarkAreaIndex(cycle,dataList,markValue){
        var markList = [];
        if(cycle && cycle.length>0 && dataList && dataList.length>0 && cycle.length==dataList.length){

            for(var i=0;i<dataList.length;i++){
                if(dataList[i]<markValue){
                    var mark = [];
                    if(i>1){
                        mark = [{xAxis:cycle[i-1]},{xAxis:cycle[i]}];
                    }else{
                        mark = [{xAxis:cycle[0]},{xAxis:cycle[1]}];
                    }
                    markList.push(mark);
                }
            }
        }


        return markList;

    }

</script>

<script>
    var date = new Date();
    var monthAgoDate = moment(date).subtract(1, 'M');//一个月之前
    var lastDate = moment(date).subtract(1, 'd');//一天之前
    var initBeginTime = moment(monthAgoDate).format("YYYY-MM-DD");
    var initEndTime = moment(lastDate).format("YYYY-MM-DD");

    laydate.render({
        elem: '#day_begin',
        min:'2017-07-17',
        max:initEndTime,
        value:initBeginTime,
    });

    laydate.render({
        elem: '#day_end',
        min:'2017-07-17',
        max:initEndTime,
        value:initEndTime
    });
</script>


<#--此页面不需要登录即可访问   http://www.fruit.com/admin/statistics/statLoanLimitDays-->



</body>
</html>