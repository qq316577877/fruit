<!DOCTYPE html>
<html class="not-ie" lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <title>订单列表</title>
<#include "/WEB-INF/pages/base/css.ftl">
    <link rel="stylesheet" href="/admins/assets/plugins/data-tables/DT_bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="/admins/assets/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css"/>
    <link rel="stylesheet" href="/admins/assets/css/neworder/base.css">
    <link rel="stylesheet" href="/admins/assets/css/neworder/vipCenter.css">
    <link rel="stylesheet" href="/admins/assets/css/neworder/newOrderQuery.css">
    <link rel="stylesheet" href="/admins/assets/plugins/loading/loading.css">

<#include "/WEB-INF/pages/base/js.ftl">
<#import "/WEB-INF/pages/base/base.ftl"  as b>
</head>
<body>
<div class="main-r fl">
    <ul class="clearfix query-head">
        <li class="tabCurrent">全部订单</li>
        <li>待审核</li>
        <li>待提交</li>
        <li>合同已签订</li>
        <li>已预付</li>
        <li>费用结清</li>
        <li>完成</li>
    </ul>
    <div class="order-search">
        <span class="fl">单号：</span>
        <input type="text" id="orderSearch" placeholder="请输入订单号进行搜索"/>
        <button id="orderSea">订单搜索</button>
    </div>
    <div class="filtrate-day clearfix">
        <span class="fl">日期：</span>
        <ul class="fl">
            <li id="today">当天</li>
            <li id="toweek">本周</li>
            <li id="tomonth">本月</li>
            <li id="threeMonth">前三个月</li>
        </ul>
        <div class="custom">
            <span>自定义：</span>
            <input type="text" id="day_1"/> -
            <input type="text" id="day_2"/>
            <input type="button" value="自定义查询" id="day_3"/>
        </div>
    </div>

    <ul class="order-head">
        <li>日期</li>
        <li>订单号</li>
        <li>果品</li>
        <li>商品数量</li>
        <li>采购商</li>
        <li>是否贷款</li>
        <li>订单状态</li>
        <li>操作</li>
    </ul>
<#--<div class="ctrl-btn">-->
<#--<button id="btn-last">上一页</button>-->
<#--<button id="btn-next">下一页</button>-->
<#--</div>-->
    <div class="order-list clearfix" id="orderList" style="">
        <#--<table>-->
            <#--<tr>-->
                <#--<td>2017-04-11</td>-->
                <#--<td>111709077661</td>-->
                <#--<td>火龙果</td>-->
                <#--<td>58000</td>-->
                <#--<td>武汉华南城大发批发代理</td>-->
                <#--<td>是</td>-->
                <#--<td>-->
                    <#--<div class="doubleHeight">-->
                        <#--<div>待审核</div>-->
                        <#--<a class="orderDetails" href="javascript:;">订单详情</a>-->
                    <#--</div>-->
                <#--</td>-->
                <#--<td>-->
                    <#--<div class="doubleHeight">-->
                        <#--<a class="orderDetails" href="javascript:;">审核订单</a>-->
                        <#--<a href="javascript:;">取消订单</a>-->
                    <#--</div>-->
                <#--</td>-->
            <#--</tr>-->
            <#--<tr>-->
                <#--<td>2017-04-11</td>-->
                <#--<td>111709077661</td>-->
                <#--<td>火龙果</td>-->
                <#--<td>58000</td>-->
                <#--<td>武汉华南城大发批发代理</td>-->
                <#--<td>是</td>-->
                <#--<td>-->
                    <#--<div class="doubleHeight">-->
                        <#--<div>待审核</div>-->
                        <#--<a class="orderDetails" href="javascript:;">订单详情</a>-->
                    <#--</div>-->
                <#--</td>-->
                <#--<td>-->
                    <#--<div class="doubleHeight">-->
                        <#--<a class="orderDetails" href="javascript:;">审核订单</a>-->
                        <#--<a href="javascript:;">取消订单</a>-->
                    <#--</div>-->
                <#--</td>-->
            <#--</tr>-->
            <#--<tr>-->
                <#--<td>2017-04-11</td>-->
                <#--<td>111709077661</td>-->
                <#--<td>火龙果</td>-->
                <#--<td>58000</td>-->
                <#--<td>武汉华南城大发批发代理</td>-->
                <#--<td>是</td>-->
                <#--<td>-->
                    <#--<div class="doubleHeight">-->
                        <#--<div>待审核</div>-->
                        <#--<a class="orderDetails" href="javascript:;">订单详情</a>-->
                    <#--</div>-->
                <#--</td>-->
                <#--<td>-->
                    <#--<div class="doubleHeight">-->
                        <#--<a class="orderDetails" href="javascript:;">审核订单</a>-->
                        <#--<a href="javascript:;">取消订单</a>-->
                    <#--</div>-->
                <#--</td>-->
            <#--</tr>-->
            <#--<tr>-->
                <#--<td>2017-04-11</td>-->
                <#--<td>111709077661</td>-->
                <#--<td>火龙果</td>-->
                <#--<td>58000</td>-->
                <#--<td>武汉华南城大发批发代理</td>-->
                <#--<td>是</td>-->
                <#--<td>-->
                    <#--<div class="doubleHeight">-->
                        <#--<div>待审核</div>-->
                        <#--<a class="orderDetails" href="javascript:;">订单详情</a>-->
                    <#--</div>-->
                <#--</td>-->
                <#--<td>-->
                    <#--<div class="doubleHeight">-->
                        <#--<a class="orderDetails" href="javascript:;">审核订单</a>-->
                        <#--<a href="javascript:;">取消订单</a>-->
                    <#--</div>-->
                <#--</td>-->
            <#--</tr>-->
        <#--</table>-->
    <#--<table>-->
    <#--<tr>-->
    <#--<td>2017-04-11</td>-->
    <#--<td>111709077661</td>-->
    <#--<td>火龙果</td>-->
    <#--<td>58000</td>-->
    <#--<td>武汉华南城大发批发代理</td>-->
    <#--<td>是</td>-->
    <#--<td>-->
    <#--<div>待审核</div>-->
    <#--<a class="orderDetails" href="javascript:;">订单详情</a>-->
    <#--</td>-->
    <#--<td>-->
    <#--<a class="orderDetails" href="javascript:;">审核订单</a>-->
    <#--<a href="javascript:;">取消订单</a>-->
    <#--</td>-->
    <#--</tr>-->
    <#--</table>-->
    <#--<table>-->
    <#--<tr>-->
    <#--<td>2017-04-11</td>-->
    <#--<td>111709077661</td>-->
    <#--<td>火龙果</td>-->
    <#--<td>58000</td>-->
    <#--<td>武汉华南城大发批发代理</td>-->
    <#--<td>是</td>-->
    <#--<td>-->
    <#--<div>待审核</div>-->
    <#--<a class="orderDetails" href="javascript:;">订单详情</a>-->
    <#--</td>-->
    <#--<td>-->
    <#--<a class="orderDetails" href="javascript:;">审核订单</a>-->
    <#--<a href="javascript:;">取消订单</a>-->
    <#--</td>-->
    <#--</tr>-->
    <#--</table>-->
    <#--<table>-->
    <#--<tr>-->
    <#--<td>2017-04-11</td>-->
    <#--<td>111709077661</td>-->
    <#--<td>火龙果</td>-->
    <#--<td>58000</td>-->
    <#--<td>武汉华南城大发批发代理</td>-->
    <#--<td>是</td>-->
    <#--<td>-->
    <#--<div>待审核</div>-->
    <#--<a class="orderDetails"  href="javascript:;">订单详情</a>-->
    <#--</td>-->
    <#--<td>-->
    <#--<a class="orderDetails" href="javascript:;">审核订单</a>-->
    <#--<a href="javascript:;">取消订单</a>-->
    <#--</td>-->
    <#--</tr>-->
    <#--</table>-->
    <#--<table>-->
    <#--<tr>-->
    <#--<td>2017-04-11</td>-->
    <#--<td>111709077661</td>-->
    <#--<td>火龙果</td>-->
    <#--<td>58000</td>-->
    <#--<td>武汉华南城大发批发代理</td>-->
    <#--<td>是</td>-->
    <#--<td>-->
    <#--<div>待审核</div>-->
    <#--<a class="orderDetails"  href="javascript:;">订单详情</a>-->
    <#--</td>-->
    <#--<td>-->
    <#--<a class="orderDetails" href="javascript:;">审核订单</a>-->
    <#--<a href="javascript:;">取消订单</a>-->
    <#--</td>-->
    <#--</tr>-->
    <#--</table>-->
    </div>
<div class="pageDiv">
</div>


</div>
<div id="listMask" >
    <div id="advince" style="display: none;">
        <h4>预付款确认</h4>
        <label for="">
            <span>预付款金额（元）</span>
            <input type="text" id="advinceNum">
        </label>
        <label for="">
            <span>备注信息</span>
            <textarea name="" id="advinceText">

            </textarea>
        </label>
        <div class="btnClass">
            <input type="button" id="collectBtn" value="确定">
            <input type="button" id="collectCancel" value="取消">
        </div>
    </div>
    <div id="prepaid" style="display: none;">
        <h4>尾款确认</h4>
        <label for="">
            <span>尾款金额（元）</span>
            <input type="text" id="prepaidNum">
        </label>
        <label for="">
            <span>备注信息</span>
            <textarea name="" id="prepaidText">

            </textarea>
        </label>
        <div class="btnClass">
            <input type="button" id="prepaidBtn" value="确定">
            <input type="button" id="prepaidCancel" value="取消">
        </div>
    </div>
    <div id="cancelPop" style="display: none;">
        <h3>是否取消订单</h3>
        <div class="canBtn">
            <input type="button" id="insureBtn" value="确定">
            <input type="button" id="canBtnOrder" value="取消">
        </div>
    </div>
</div>


<#-- 引用moment -->
<script src="/admins/assets/plugins/moment/moment.min.js"></script>
<script src="/admins/assets/plugins/date/laydate.dev.js"></script>
<script src="/admins/assets/plugins/loading/loading.js"></script>
<script src="/admins/assets/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
<script src="/admins/assets/plugins/jQueryPage/jquery.page.js"></script>
<script src="/admins/js/form.js" type="text/javascript"></script>
<script src="/admins/js/neworder/newOrderQuery/newOrderCreateTable.js"></script>
<script src="/admins/js/neworder/newOrderQuery/newOrderDetailHandler.js"></script>
<script src="/admins/js/neworder/newOrderQuery/newQueryOrderList.js"></script>

<script>

    laydate({

        elem: '#day_1'

    });

    laydate({

        elem: '#day_2'
    });

</script>
</body>
</html>