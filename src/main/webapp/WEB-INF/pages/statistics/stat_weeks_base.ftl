<!DOCTYPE html>
<html class="not-ie" lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <title>基本信息统计(周)</title>
<#include "/WEB-INF/pages/base/css.ftl">
    <link rel="stylesheet" href="/admins/assets/plugins/data-tables/DT_bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="/admins/assets/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css"/>
<#include "/WEB-INF/pages/base/js.ftl">
<#import "/WEB-INF/pages/base/base.ftl"  as b>

    <style>
        #main{margin:0 auto;width: 900px;height:400px;}
        .baseShow{font-size: 15px; text-align: center;display:none;}
        .showTime{font-size: 20px;font-weight:bold;text-align: center;}
        .showValue{font-size: 25px;color:#FF0000;}

        .showDivs{text-align: center;}
        .showOrderCountDiv{display:inline-block;border:1px solid #F78500; width:19%;padding: 5px;}
        .showOrderAmountDiv{display:inline-block;border:1px solid #F78500;width:19%;padding: 5px;margin-left: 5px;}
        .showLoanCountDiv{display:inline-block;border:1px solid #1F6ED4;width:19%;padding: 5px;margin-left: 5px;}
        .showLoanAmountDiv{display:inline-block;border:1px solid #1F6ED4;width:19%;padding: 5px;margin-left: 5px;}
        .showLoanInterestDiv{display:inline-block;border:1px solid #1F6ED4;width:19%;padding: 5px;margin-left: 5px;}
    </style>


</head>
<body>

<div class="page-content extended " style="align:center">

    <div id="show_huashu" class="baseShow">

        <div style="margin-bottom:10px;">
            <span id="show_stat_cycle" class="showTime"></span>
        </div>

        <div class="showDivs">
            <div class="showOrderCountDiv">
                累计订单数（笔）
                <p><span id="show_order_count" class="showValue">0</span></p>
            </div>
            <div class="showOrderAmountDiv">
                累计订单总额（万）
                <p>¥<span id="show_order_amount" class="showValue">0</span></p>
            </div>
            <div class="showLoanCountDiv">
                累计发放贷款数（笔）
                <p><span id="show_loan_count" class="showValue">0</span></p>
            </div>
            <div class="showLoanAmountDiv">
                累计发放贷款总额（万）
                <p>¥<span id="show_loan_amount" class="showValue">0</span></p>
            </div>
            <div class="showLoanInterestDiv">
                累计利息总额（万）
                <p>¥<span id="show_loan_interest" class="showValue">0</span></p>
            </div>
        </div>
    </div>

    <hr>



    <div id="main"></div>

</div>




<script src="/admins/assets/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
<script src="/admins/js/form.js" type="text/javascript"></script>
<!-- 引入 ECharts 文件 -->
<script src="/admins/assets/plugins/echarts/echarts.min.js"></script>


<script type="text/javascript">
    $(document).ready(function(){
        initChart();
        getStatOrderLoanWeekModelList();
    });
</script>


<script type="text/javascript">
    var colors = ['#1F6ED4', '#F78500', '#d14a61'];
    var myChart = echarts.init(document.getElementById('main'));

    var maxDingdan = 100*1.2;
    var xAxisDataList = [];
    var orderAmountDataList = [];
    var loanAmountDataList = [];

    var option = {
        color: colors,

        tooltip: {
            trigger: 'axis',
            axisPointer: {type: 'cross'},
            formatter: '{b} <br/> {a1} : {c1} (万) <br/> {a0} : {c0} (万) '
        },
        grid: {
            right: '10%'
        },
        toolbox: {
            show : true,
            feature : {
                mark : {show: true},
                dataView : {show: true, readOnly: false},
                magicType : {show: true, type: ['line', 'bar']},
                restore : {show: true},
                saveAsImage : {show: true}
            }
        },
        legend: {
            data:['订单金额','贷款金额']
        },
        xAxis: [
            {
                type: 'category',
                axisTick: {
                    alignWithLabel: true
                },
                axisLabel: {
                    formatter: function (value, index) {
                        var retValue = value.replace("-","\n - \n");
                        return retValue;
                    }
                },
                data:xAxisDataList
            }
        ],
        yAxis: [
            {
                type: 'value',
                name: '贷款金额',
                min:0,
                max:maxDingdan,
                position: 'right',
                axisLine: {
                    lineStyle: {
                        color: colors[0]
                    }
                },
                axisLabel: {
                    formatter: '{value} 万'
                }
            },

            {
                type: 'value',
                name: '订单金额',
                min:0,
                max:maxDingdan,
                position: 'left',
                axisLine: {
                    lineStyle: {
                        color: colors[1]
                    }
                },
                axisLabel: {
                    formatter: '{value} 万'
                }
            }
        ],
        series: [
            {
                name:'贷款金额',
                type:'bar',
                label: {
                    normal: {
                        show: true,
                        position: 'top',
//                        formatter: '{a}:\n {c} 万'
                        formatter: '{c} 万'
                    }
                },
                data:loanAmountDataList
            },

            {
                name:'订单金额',
                type:'line',
                label: {
                    normal: {
                        show: true,
                        position: 'top',
//                        formatter: '{a}:\n {c} 万'
                        formatter: '{c} 万',
                        offset:[0,-8]//是否对文字进行偏移。默认不偏移。例如：[30, 40] 表示文字在横向上偏移 30，纵向上偏移 40。
                    }
                },
                yAxisIndex: 1,
                data:orderAmountDataList
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
        var chartHeight = totalHeight-160;
        var totalWidth = $(window).width();
        var chartWidth = totalWidth-50;
        $("#main").height(chartHeight);
        $("#main").width(chartWidth);
        myChart.resize();
    }


</script>

<script>
    var statOrderLoanWeekModelList = null;//数据源
    var statistics_cycle = 8;//统计周期
    var date = new Date();
    var lastDate = moment(date).subtract(1, 'd');//一天之前
    var statistics_end_time = moment(lastDate).format("YYYY-MM-DD");//统计最终时间


    /**
     * 通过ajax获取周统计数据
     */
    function getStatOrderLoanWeekModelList(){
        $.ajax("${BASEPATH}/statistics/listStatOrderLoanOfWeeks", {
            data: {cycleNum:statistics_cycle,
                endTime:statistics_end_time
            },
            type: "POST",
//            async:false,
        }).always(function () {
        }).done(function (data) {
            if (data.code==200) {
                var dataSay = data.data;
                if(dataSay){
                    var statList = dataSay.statOrderLoanWeekModelList;
                    var maxOrderAmount = dataSay.maxOrderAmount;//最大订单总额
                    var showStatHuashu = dataSay.statOrderLoanWeekModel;//截止endTime的总订单统计情况
                    refreshChartWithData(statList,maxOrderAmount);
                    refreshHuaShuWithData(showStatHuashu);
                }
            } else {
                bootbox.alert(data.msg);
            }
        }).fail(function () {
            bootbox.alert("系统错误！");
        });
    }


    /**
     *根据查询到的数据，刷新chart
     */
    function refreshChartWithData(statList,maxOrderAmount){
        if(statList && statList.length>0){
            var xAxisDatas = new Array();
            var orderAmountDatas = new Array();
            var loanAmountDatas = new Array();
            for(var i=0;i<statList.length;i++){
                xAxisDatas.push(statList[i].statCycle);
                orderAmountDatas.push(formatterWanValue(statList[i].orderAmount));
                loanAmountDatas.push(formatterWanValue(statList[i].loanAmount));
            }
            option.xAxis[0].data = xAxisDatas;
            option.series[0].data = loanAmountDatas;
            option.series[1].data = orderAmountDatas;

        }


        if(maxOrderAmount>0){
            option.yAxis[0].max = formatterWanValue(maxOrderAmount*1.2,0);
            option.yAxis[1].max = formatterWanValue(maxOrderAmount*1.2,0);
        }

        myChart.setOption(option,true);
    }

    /**
     * 根据查询数据，刷新话术
     */
    function refreshHuaShuWithData(showStatHuashu){
        var showStatCycle = moment(lastDate).format("YYYY年MM月DD日");//统计最终时间

        $("#show_stat_cycle").text(showStatCycle);
        if(showStatHuashu!=null){
            $("#show_order_count").text(showStatHuashu.orderCount);
            $("#show_order_amount").text(formatterWanValue(showStatHuashu.orderAmount));
            $("#show_loan_count").text(showStatHuashu.loanCount);
            $("#show_loan_amount").text(formatterWanValue(showStatHuashu.loanAmount));
            $("#show_loan_interest").text(formatterWanValue(showStatHuashu.loanInterest));
        }

        $("#show_huashu").show();

    }


    /**
     * 格式化数字，单位为万
     * @param value
     * @param fixNum 保留小数位数
     */
    function formatterWanValue(value,fixNum){
        var chuNum = value/10000;
        var retValue = 0;
        if(fixNum != null && fixNum != undefined){
            retValue = chuNum.toFixed(fixNum);
        }else{
            retValue = chuNum.toFixed(2);
        }
        return retValue;
    }

</script>


<#--此页面不需要登录即可访问   http://www.fruit.com/admin/statistics/statBaseWeeks-->



</body>
</html>