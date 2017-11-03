<!DOCTYPE html>
<html class="not-ie" lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <title>订单列表</title>
<#include "/WEB-INF/pages/base/css.ftl">
    <link rel="stylesheet" href="/admins/assets/plugins/data-tables/DT_bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="/admins/assets/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css"/>
    <link rel="stylesheet" href="/admins/assets/css/order/base.css">
    <link rel="stylesheet" href="/admins/assets/css/order/vipCenter.css">
    <link rel="stylesheet" href="/admins/assets/css/order/orderQuery.css">
    <link rel="stylesheet" href="/admins/assets/plugins/loading/loading.css">

<#include "/WEB-INF/pages/base/js.ftl">
<#import "/WEB-INF/pages/base/base.ftl"  as b>
</head>
<body>
<div class="main-r fl">
    <ul class="clearfix query-head">
        <li class="current">全部订单</li>
        <li>待审核</li>
        <li>待提交</li>
        <li>待付款</li>
        <li>待发货</li>
        <li>待收货</li>
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
        <li>货柜批次号</li>
        <li>货柜名称</li>
        <li>货柜状态</li>
        <li>贷款申请金额</li>
        <li>贷款状态</li>
        <li>订单状态</li>
        <li>操作</li>
    </ul>
    <div class="ctrl-btn">
        <button id="btn-last">上一页</button>
        <button id="btn-next">下一页</button>
    </div>
    <div class="order-list clearfix" id="orderList" style="">

    </div>
    <div class="pageDiv">
    </div>
    <div class="noData">
        <i class="iconfont icon-order order-icon1"></i>

        <p>没有相关的订单哦</p>

        <div class="noData-btn">
            <input type="button" id="allOr" value="查看全部订单"/>
            <#--<input type="button" value="去下单"/>-->
        </div>
    </div>
    <!--<div id="Searchresult">分页初始化完成后这里的内容会被替换。1</div>-->

</div>
<div class="mask">
    <div id="inform-cancle" style="display: none" >
        <div class="info">是否取消订单</div>
        <div class="can-btn">
            <button id="btn-cancle-confirm">确认</button>
            <button id="btn-cancle-cancle">取消</button>
        </div>
    </div>
    <div id="inform-insured" style="border-radius: 20px;display: none">
        <div class="head">
            <h4>货柜批次号</h4>
            <h4>保险合同号</h4>
        </div>

        <div class="can-btn">
            <button id="btn-insured-confirm">确认</button>
            <button id="btn-insured-cancle">取消</button>
        </div>
    </div>
    <div id="inform-3" style="display: none">
        <div class="info">是否确认收款</div>
        <div class="can-btn">
            <button id="btn-3">确认</button>
            <button id="btn-4">取消</button>
        </div>
    </div>
    <div id="inform-remark" style="display: none">
        <span><span class="orderNo">订单号：</span><h2></h2></span>

        <textarea rows="5" cols="40" placeholder="请输入备注信息">
</textarea>

        <div class="can-btn">
            <button id="btn-remark-confirm">确认</button>
            <button id="btn-remark-cancle">取消</button>
        </div>
    </div>
</div>

<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" style="width: 550px;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">请选择导出条件</h4>

            </div>
            <div class="modal-body">
                <div class="col-md-12">
                    <div class="row">

                        <div style="padding-top: 10px">
                            <span>用户类型：</span>
                        <#list user_type_list as item>
                            <#if item.id gt -1>
                                <input type="checkbox" name="type" id="type" value="${item.id}"
                                       <#if item.id == 1 ||item.id ==2>checked="checked"</#if>/> ${item.value}
                            </#if>
                        </#list>
                        </div>


                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary export-btn" data-id="1">确认</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                </div>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal-dialog -->
    </div>
</div>


<#-- 引用moment -->
<script src="/admins/assets/plugins/moment/moment.min.js"></script>
<script src="/admins/assets/plugins/date/laydate.dev.js"></script>
<script src="/admins/assets/plugins/loading/loading.js"></script>
<script src="/admins/assets/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
<script src="/admins/assets/plugins/jQueryPage/jquery.page.js"></script>
<script src="/admins/js/form.js" type="text/javascript"></script>
<script src="/admins/js/order/orderQuery/orderCreateTable.js"></script>
<script src="/admins/js/order/orderQuery/orderDetailHandler.js"></script>
<script src="/admins/js/order/orderQuery/queryOrderList.js"></script>

<script>

    laydate({

        elem: '#day_1'

    });

    laydate({

        elem: '#day_2'

    });


    <#--function query(pageNo) {-->
        <#--$('#pageNo').val(pageNo);-->
        <#--var form = $("#thisForm");-->
        <#--form.attr('action', form.attr('action'));-->
        <#--form.submit();-->
    <#--}-->

    <#--$(".export-btn").on('click', function (e) {-->
        <#--e.preventDefault();-->
        <#--var checkstr = "";-->
        <#--$("input:checkbox[name='type']:checked").each(function (i) {-->
            <#--if (checkstr != "") {-->
                <#--checkstr += ",";-->
            <#--}-->
            <#--checkstr += $(this).val();-->
        <#--});-->
        <#--var url = "${BASEPATH}/user/export?type="+checkstr;-->

        <#--var d = dialog({-->
            <#--title: '导出用户数据',-->
            <#--url: url,-->
            <#--iframe_width: 300,-->
            <#--iframe_height: 150,-->
            <#--zIndex: 9996,-->
            <#--quickClose: false,-->
        <#--});-->
        <#--top.openDialog = d;   //保存后如果需要关闭窗口-->
        <#--d.show();-->

    <#--});-->
</script>
</body>
</html>