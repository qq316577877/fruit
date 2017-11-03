<!DOCTYPE html>
<html class="not-ie" lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <title>用户列表</title>
<#include "/WEB-INF/pages/base/css.ftl">
    <link rel="stylesheet" href="/admins/assets/css/order/upload.css">
    <link rel="stylesheet" href="/admins/assets/plugins/data-tables/DT_bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="/admins/assets/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css"/>
    <link rel="stylesheet" href="/admins/assets/css/order/base.css">
    <link rel="stylesheet" href="/admins/assets/css/neworder/orderInfo/orderInfo.css">
    <link rel="stylesheet" href="/admins/assets/css/neworder/newOrder_audit.css">
    <link rel="stylesheet" href="/admins/assets/css/neworder/orderInfo/orderInfoDetails.css">
    <link rel="stylesheet" href="/admins/assets/css/neworder/newImportStep2.css">
    <link rel="stylesheet" href="/admins/assets/css/neworder/newOrderReview.css">
    <link rel="stylesheet" href="/admins/assets/css/neworder/x/iconfont.css">
<#include "/WEB-INF/pages/base/js.ftl">
<#import "/WEB-INF/pages/base/base.ftl"  as b>
</head>
<body>
<div id="order" style="display: none"></div>
<div class="audit_head">
    <a href="/admin/newOrder/orderList">订单中心</a>
    >审核订单
</div>

<div class="audit_info">
    <#--<div class="infoCloms">-->
        <#--<div><span class="audit_h">订单号：</span>10000120170411001</div>-->
        <#--<div><span class="audit_h">历史成交：</span>10单</div>-->
        <#--<div><span class="audit_h">账号：</span>18664938582</div>-->
        <#--<div><span class="audit_h">订单状态：</span>待审核</div>-->
    <#--</div>-->
    <#--<div class="infoCloms">-->
        <#--<div><span class="audit_h">下单日期：</span>2017-04-11</div>-->
        <#--<div><span class="audit_h">最近交易时间：</span>2017-3-25</div>-->
        <#--<div><span class="audit_h">联系人：</span>刘达</div>-->
    <#--</div>-->
    <#--<div class="infoCloms">-->
        <#--<div>采购商：浙江温州市大东水果批发商</div>-->
        <#--<div><span class="audit_h">最近交易订单号：</span> 10000120170325001</div>-->
        <#--<div>联系电话：13252114152</div>-->
    <#--</div>-->
</div>

<#-- 导航菜单 -->
<div class="orderStep">
    <a id="orderTab" class="clickStep" href="#">订单信息</a>
    <a id="containerTab" href="#">货柜信息</a>
    <a id="logisticsTab" href="javascript:;">物流信息</a>
    <a id="insuranceTab" href="javascript:;">保险信息</a>
    <a id="MoneyTab" href="javascript:;">收款信息</a>
</div>

<#-- 信息展示区域 -->
<div class="orderInfo">
    <#include "/WEB-INF/pages/neworder/new_order_info_detail.ftl">
    <#include "/WEB-INF/pages/neworder/new_order_info_container.ftl">
    <#include "/WEB-INF/pages/neworder/new_order_info_logistics.ftl">
    <#include "/WEB-INF/pages/neworder/new_order_info_insurance.ftl">
    <#include "/WEB-INF/pages/neworder/new_order_info_collection.ftl">
</div>

<script src="/admins/assets/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
<script src="/admins/js/form.js" type="text/javascript"></script>
<script src="/admins/js/neworder/newOrderAudit/newOrderAudit.js"></script>


</body>
</html>