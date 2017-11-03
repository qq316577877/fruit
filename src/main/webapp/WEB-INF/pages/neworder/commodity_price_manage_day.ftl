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
    <link rel="stylesheet" href="/admins/assets/css/neworder/connodityPrice.css">

<#include "/WEB-INF/pages/base/js.ftl">
<#import "/WEB-INF/pages/base/base.ftl"  as b>
</head>
<body>
<div class="commodityList" id="productCommodityList">
    <div id="header">
        <select name="" id="selector" style="display: none">
        </select>
        <h5 id="productName">火龙果</h5>
        <div class="fr">
            <input type="text" id="day_1" disabled>
            <#--<input type="button" id="btn" value="查询">-->
            <input type="button" id="audit" value="编辑">
        </div>
    </div>
    <div id="body">
        <#--<table>-->
            <#--<thead>-->
            <#--<th>商品名称</th>-->
            <#--<th>低位报价</th>-->
            <#--<th>高位报价</th>-->
            <#--<th>成交价</th>-->
            <#--</thead>-->
            <#--<tr>-->
                <#--<td>白心 一级 大</td>-->
                <#--<td>-->
                    <#--<input type="text" value="55">-->
                <#--</td>-->
                <#--<td>-->
                    <#--<input type="text" value="77">-->
                <#--</td>-->
                <#--<td>-->
                    <#--<input type="text" value="50">-->
                <#--</td>-->



            <#--</tr>-->
            <#--<tr>-->
                <#--<td>白心 一级 大</td>-->
                <#--<td>-->
                    <#--<input type="text" value="55">-->
                <#--</td>-->
                <#--<td>-->
                    <#--<input type="text" value="77">-->
                <#--</td>-->
                <#--<td>-->
                    <#--<input type="text" value="50">-->
                <#--</td>-->
            <#--</tr>-->
            <#--<tr>-->
                <#--<td>白心 一级 大</td>-->
                <#--<td>-->
                    <#--<input type="text" value="55">-->
                <#--</td>-->
                <#--<td>-->
                    <#--<input type="text" value="77">-->
                <#--</td>-->
                <#--<td>-->
                    <#--<input type="text" value="50">-->
                <#--</td>-->
            <#--</tr>-->
            <#--<tr>-->
                <#--<td>白心 一级 大</td>-->
                <#--<td>-->
                    <#--<input type="text" value="55">-->
                <#--</td>-->
                <#--<td>-->
                    <#--<input type="text" value="77">-->
                <#--</td>-->
                <#--<td>-->
                    <#--<input type="text" value="50">-->
                <#--</td>-->
            <#--</tr>-->
            <#--<tr>-->
                <#--<td>白心 一级 大</td>-->
                <#--<td>-->
                    <#--<input type="text" value="55">-->
                <#--</td>-->
                <#--<td>-->
                    <#--<input type="text" value="77">-->
                <#--</td>-->
                <#--<td>-->
                    <#--<input type="text" value="50">-->
                <#--</td>-->
            <#--</tr>-->
            <#--<tr>-->
                <#--<td>白心 一级 大</td>-->
                <#--<td>-->
                    <#--<input type="text" value="55">-->
                <#--</td>-->
                <#--<td>-->
                    <#--<input type="text" value="77">-->
                <#--</td>-->
                <#--<td>-->
                    <#--<input type="text" value="50">-->
                <#--</td>-->
            <#--</tr>-->
            <#--<tr>-->
                <#--<td>白心 一级 大</td>-->
                <#--<td>-->
                    <#--<input type="text" value="55">-->
                <#--</td>-->
                <#--<td>-->
                    <#--<input type="text" value="77">-->
                <#--</td>-->
                <#--<td>-->
                    <#--<input type="text" value="50">-->
                <#--</td>-->
            <#--</tr>-->
        <#--</table>-->
    </div>
</div>

<#--<script src="/admins/assets/plugins/moment/moment.min.js"></script>-->
<script src="/admins/assets/plugins/date/laydate.dev.js"></script>
<script src="/admins/assets/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
<script src="/admins/js/form.js" type="text/javascript"></script>
<script src="/admins/js/neworder/newCommodityPrice/createTable.js"></script>
<script src="/admins/js/neworder/newCommodityPrice/newCommodityPrice.js"></script>
<script>

//    laydate({
//
//        elem: '#day_1'
//
//    });

</script>
</body>
</html>