<!DOCTYPE html>
<html class="not-ie" lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <title>订单信贷服务费(周)</title>
<#include "/WEB-INF/pages/base/css.ftl">
    <link rel="stylesheet" href="/admins/assets/plugins/data-tables/DT_bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="/admins/assets/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css"/>
<#include "/WEB-INF/pages/base/js.ftl">
<#import "/WEB-INF/pages/base/base.ftl"  as b>

    <style>
        #main{margin:0 auto;width: 950px;height:600px;}
    </style>
</head>
<body>

<div class="page-content extended ">

    <div id="main"></div>

</div>




<script src="/admins/assets/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
<script src="/admins/js/form.js" type="text/javascript"></script>
<!-- 引入 ECharts 文件 -->
<script src="/admins/assets/plugins/echarts/echarts.min.js"></script>



<script>

    $(document).ready(function(){
        //这个方法需要放在ready里
        initChart();
        getStatOrderLoanWeekModelList();
    });

</script>

<script type="text/javascript">
    var colors = ['#1F6ED4','#F78500','#d14a61', '#5793f3', '#675bba'];
    var myChart = echarts.init(document.getElementById('main'));
    var maxJine = 163*1.2;
    var maxHuanbi = 100*1.2;
    var minHuanbi = -15*1.2;
    option = {
        color: colors,

        tooltip: {
            trigger: 'axis',
            axisPointer: {type: 'cross'},
            formatter: '{b} <br/> {a0} : {c0} (万)<br/> {a1} : {c1} (万) <br/> {a2} : {c2} (万) '
        },
        grid: {
            right: '1%'
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
            data:['订单金额','贷款金额','服务费金额']
        },
        xAxis: [
            {
                type: 'category',
                axisLabel: {
                    formatter: function (value, index) {
                        var retValue = value.replace("-","\n - \n")
                        return retValue;
                    }
                },
                data: ['20170724-20170730','20170731-20170806','20170807-20170813','20170814-20170820','20170821-20170827','20170828-20170903']
            }
        ],
        yAxis: [
            {
                type: 'value',
                name: '金额',
                max:maxJine,
                position: 'left',
                axisLabel: {
                    formatter: '{value} 万'
                }
            }

        ],
        series: [
            {
                name:'订单金额',
                type:'bar',
                itemStyle:{
                    normal:{
                        label:{
                            show: true,
                            position: 'top',
                            formatter: '{c} 万',
                            textStyle:{
                                fontSize: 10
                            }
                        }
                    }
                },
                data:[21.81,26.17, 31.72,162.92,161.87,0.00]
            },
            {
                name:'贷款金额',
                type:'bar',
                itemStyle:{
                    normal:{
                        label:{
                            show: true,
                            position: 'top',
                            formatter: '{c} 万',
                            textStyle:{
                                fontSize: 10
                            }
                        }
                    }
                },
                data:[9.00,14.00, 12.00,98.50,67.10,0.00]
            },
            {
                name:'服务费金额',
                type:'bar',
                itemStyle:{
                    normal:{
                        label:{
                            show: true,
                            position: 'top',
                            formatter: '{c} 万',
                            textStyle:{
                                fontSize: 10
                            }
                        }
                    }
                },
                data:[0.90,1.40,1.28,9.85,6.71,0.00]
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
        var chartHeight = totalHeight-30;
        var totalWidth = $(window).width();
        var chartWidth = totalWidth-50;
        $("#main").height(chartHeight);
        $("#main").width(chartWidth);
        myChart.resize();
    }

</script>


<script>
    var statOrderLoanWeekModelList = null;//数据源
    var statistics_cycle = 6;//统计周期  -----只能展示6周，否则显示不全
    var statistics_end_time = '';//统计最终时间

    /**
     * 通过ajax获取周统计数据
     */
    function getStatOrderLoanWeekModelList(){
        $.ajax("${BASEPATH}/statistics/listStatServiceFeeOfWeeks", {
            data: {cycleNum:statistics_cycle,
                endTime:statistics_end_time
            },
            type: "POST"
//            async:false,
        }).always(function () {
        }).done(function (data) {
            if (data.code==200) {
                var dataSay = data.data;
                if(dataSay){
                    var statList = dataSay.statOrderLoanWeekModelList;
                    var cycleDatas = new Array();
                    var orderAmountDatas = new Array();
                    var loanAmountDatas = new Array();
                    var loanServiceFeeDatas = new Array();

                    if(statList && statList.length>0){
                        for(var i=0;i<statList.length;i++){
                            cycleDatas.push(statList[i].statCycle);
                            orderAmountDatas.push(formatterWanValue(statList[i].orderAmount));
                            loanAmountDatas.push(formatterWanValue(statList[i].loanAmount));
                            loanServiceFeeDatas.push(formatterWanValue(statList[i].loanServiceFee));
                        }
                    }
                    var maxOrderAmount = formatterWanValue(dataSay.maxOrderAmount*1.2);//最大订单总额

                    refreshChartByData(cycleDatas,orderAmountDatas,loanAmountDatas,loanServiceFeeDatas,
                            maxOrderAmount);//将数据灌入图形
                }
            } else {
                bootbox.alert(data.msg);
            }
        }).fail(function () {
            bootbox.alert("系统错误！");
        });
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

    /**
     * 格式化数字，单位为万
     * @param value
     * @param fixNum 保留小数位数
     */
    function formatterPercentValue(value,fixNum){
        var chuNum = value*100;
        var retValue = 0;
        if(fixNum != null && fixNum != undefined){
            retValue = chuNum.toFixed(fixNum);
        }else{
            retValue = chuNum.toFixed(2);
        }
        return retValue;
    }


    /**
     *根据数据刷新图形
     */
    function refreshChartByData(cycle,orderAmountDatas,loanAmountDatas,loanServiceFeeDatas,
                                maxOrderAmount){
        //xAxis 的data值
        option.xAxis[0].data = cycle;

        //yAxis max
        option.yAxis[0].max = maxOrderAmount;

        //series 的data
        option.series[0].data = orderAmountDatas;//订单金额

        option.series[1].data = loanAmountDatas;//贷款金额

        option.series[2].data = loanServiceFeeDatas;//服务费金额

        myChart.setOption(option,true);
    }


    /**
     *格式化数据显示
     **/
    function formatterDataList(dataList){
        var retDataList = dataList.map(function (data) {
            return formatterWanValue(data);
        });
        return retDataList;
    }

    /**
     *格式化数据显示
     **/
    function formatterRatioList(dataList){
        var retDataList = dataList.map(function (data) {
            return formatterPercentValue(data);
        });
        return retDataList;
    }

</script>




<#--此页面不需要登录即可访问   http://www.fruit.com/admin/statistics/statLoanServiceChargeWeeks-->



</body>
</html>