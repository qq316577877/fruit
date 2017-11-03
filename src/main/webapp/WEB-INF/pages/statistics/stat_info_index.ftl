<!DOCTYPE html>

<html class="not-ie" lang="en">
<head>
    <meta charset="utf-8"/>
    <title>订单资金服务统计</title>
<#include "/WEB-INF/pages/base/css.ftl">
<#include "/WEB-INF/pages/base/js.ftl">
<#import "/WEB-INF/pages/base/base.ftl"  as b>
</head>
<body>
<div class="page-content extended">
    <div class="tabbable-custom">
        <ul class="nav nav-tabs" style="width:80%">
            <li class="active"><a id="tab_1_active" href="#tab_1" data-toggle="tab">基本信息(周)</a></li>
            <li class="" data-type=''><a id="tab_2_active" href="#tab_2" data-toggle="tab">订单总额走向(周)</a></li>
            <li class="" data-type=''><a id="tab_3_active" href="#tab_3" data-toggle="tab">可贷余额(日)</a></li>
            <li class="" data-type=''><a id="tab_4_active" href="#tab_4" data-toggle="tab">订单信贷环比(周)</a></li>
            <li class="" data-type=''><a id="tab_5_active" href="#tab_5" data-toggle="tab">订单信贷服务费(周)</a></li>
            <li class="" data-type=''><a id="tab_6_active" href="#tab_6" data-toggle="tab">订单信贷综合信息(周)</a></li>

        </ul>
        <div class="tab-content">
            <div class="tab-pane active" id="tab_1">
                <iframe id="tab_1_iframe" name="tab_1_iframe" src="" height="450px" frameborder="0" scrolling="auto"
                        marginheight="0" marginwidth="0" width="100%"></iframe>
            </div>

            <div class="tab-pane" id="tab_2">
                <iframe id="tab_2_iframe" name="tab_2_iframe" src="" height="450px" frameborder="0" scrolling="auto"
                        marginheight="0" marginwidth="0" width="100%"></iframe>
            </div>

            <div class="tab-pane" id="tab_3">
                <iframe id="tab_3_iframe" name="tab_3_iframe" src="" height="450px" frameborder="0" scrolling="auto"
                        marginheight="0" marginwidth="0" width="100%"></iframe>
            </div>

            <div class="tab-pane" id="tab_4">
                <iframe id="tab_4_iframe" name="tab_4_iframe" src="" height="450px" frameborder="0" scrolling="auto"
                        marginheight="0" marginwidth="0" width="100%"></iframe>
            </div>

            <div class="tab-pane" id="tab_5">
                <iframe id="tab_5_iframe" name="tab_5_iframe" src="" height="450px" frameborder="0" scrolling="auto"
                        marginheight="0" marginwidth="0" width="100%"></iframe>
            </div>

            <div class="tab-pane" id="tab_6">
                <iframe id="tab_6_iframe" name="tab_6_iframe" src="" height="450px" frameborder="0" scrolling="auto"
                        marginheight="0" marginwidth="0" width="100%"></iframe>
            </div>
        </div>

    </div>
</div>



<!-- 页面JS类库引入 st-->
<!-- 页面JS类库引入 ed-->
<!-- 私有JS st -->
<script src="/admins/js/form.js" type="text/javascript"></script>
<!-- 私有JS ed -->
<script>
    jQuery(document).ready(function () {
        refreshInit();
        initLiClickListener();
    });


    /**
     * 一次性嵌入所有iframe，此处不推荐使用
     */
    function refresh() {
        $('#tab_1_iframe').attr('src', "${BASEPATH}/statistics/statBaseWeeks");
        $('#tab_2_iframe').attr('src', "${BASEPATH}/statistics/statOrderAmountWeeks");
        $('#tab_3_iframe').attr('src', "${BASEPATH}/statistics/statLoanLimitDays");
        $('#tab_4_iframe').attr('src', "${BASEPATH}/statistics/statOrderLoanCycleRingWeeks");
        $('#tab_5_iframe').attr('src', "${BASEPATH}/statistics/statLoanServiceChargeWeeks");
        $('#tab_6_iframe').attr('src', "${BASEPATH}/statistics/statOrderLoanComprehensiveWeeks");
    }

    /**
     * 嵌入第一个iframe
     */
    function refreshInit(){
        var windowsHeigth = $(window).height();
        var frameHeight = windowsHeigth-100;
        $("#tab_1_iframe").height(frameHeight);
        $('#tab_1_iframe').attr('src', "${BASEPATH}/statistics/statBaseWeeks");
    }

    /**
     * tab页点击事件重定义，点击时重新嵌入页面，保证数据查询为新的
     */
    var initLiClickListener = function(){
        var windowsHeigth = $(window).height();
        var frameHeight = windowsHeigth-100;
        $('#tab_1_active').click(function(){
            $("#tab_1_iframe").height(frameHeight);
            $('#tab_1_iframe').attr('src', "${BASEPATH}/statistics/statBaseWeeks");
        });
        $('#tab_2_active').click(function(){
            $("#tab_2_iframe").height(frameHeight);
            $('#tab_2_iframe').attr('src', "${BASEPATH}/statistics/statOrderAmountWeeks");
        });
        $('#tab_3_active').click(function(){
            $("#tab_3_iframe").height(frameHeight);
            $('#tab_3_iframe').attr('src', "${BASEPATH}/statistics/statLoanLimitDays");
        });
        $('#tab_4_active').click(function(){
            $("#tab_4_iframe").height(frameHeight);
            $('#tab_4_iframe').attr('src', "${BASEPATH}/statistics/statOrderLoanCycleRingWeeks");
        });
        $('#tab_5_active').click(function(){
            $("#tab_5_iframe").height(frameHeight);
            $('#tab_5_iframe').attr('src', "${BASEPATH}/statistics/statLoanServiceChargeWeeks");
        });
        $('#tab_6_active').click(function(){
            $("#tab_6_iframe").height(frameHeight);
            $('#tab_6_iframe').attr('src', "${BASEPATH}/statistics/statOrderLoanComprehensiveWeeks");
        });
    };

    var disableTab = function () {
        $("li[data-type]").attr({"class": "disabled"});
        $("li[data-type] a").removeAttr('data-toggle');

    };

    var showTab = function () {
        $("li[data-type]").attr({"class": ""});
        $("li[data-type] a").attr({"data-toggle": "tab"});
    };

</script>

<#--此页面不需要登录即可访问   http://www.fruit.com/admin/statistics/statInfoIndex-->

</body>
</html>