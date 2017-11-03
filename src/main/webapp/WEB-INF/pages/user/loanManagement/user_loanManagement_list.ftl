<!DOCTYPE html>
<html class="not-ie" lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <title>贷款管理</title>
<#include "/WEB-INF/pages/base/css.ftl">
    <link rel="stylesheet" href="/admins/assets/plugins/data-tables/DT_bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="/admins/assets/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css"/>
    <link rel="stylesheet" type="text/css" href="/admins/assets/loan/loan.css"/>

<#include "/WEB-INF/pages/base/js.ftl">
<#import "/WEB-INF/pages/base/base.ftl"  as b>
</head>
<body>
<div class="page-content extended ">
    <div class="row">
        <div class="col-xs-12">
            <div style="padding-top: 10px"  >
                <table id="loanCollect"  >
                    <tr>
                        <td>当前申请总额：</td>
                        <td>${loan_collect_model.applyAmount?string('0.00')}</td>
                        <td>当前服务费总额：</td>
                        <td>${loan_collect_model.applyFee?string('0.00')}</td>
                        <td>当前申请贷款笔数:</td>
                        <td>${loan_collect_model.applyCount}</td>
                    </tr>

                    <tr>
                        <td>当前待放款总额：</td>
                        <td>${loan_collect_model.pendingAmount?string('0.00')}</td>
                        <td>当前待放款服务费：</td>
                        <td>${loan_collect_model.pendingFee?string('0.00')}</td>
                        <td>当前待放款笔数：</td>
                        <td>${loan_collect_model.pendingCount}</td>
                    </tr>

                    <#--<tr>-->
                        <#--<td>已放款总额：</td>-->
                        <#--<td>${loan_collect_model.repaymentAmount?string('0.00')}</td>-->
                        <#--<td>已放款服务费：</td>-->
                        <#--<td>${loan_collect_model.repaymentFee?string('0.00')}</td>-->
                        <#--<td>已放款笔数：</td>-->
                        <#--<td>${loan_collect_model.repaymentCount}</td>-->
                    <#--</tr>-->

                </table>

                <form action="${user_loanManagement_list_url}" id="thisForm" name="thisForm" class="form-inline" method="post">

                <#list loanManagement_status_list as item>
                    <input type="radio" name="status" value="${item.id}"
                           <#if status == item.id>checked="checked"</#if> onclick="query(1)"/> ${item.value}
                </#list>

                <input type="hidden" id="pageNo" name="pageNo" value="1"/>
                <input style="margin-left:20px;" type="text"  id="keyword" name="keyword"
                       placeholder="请输入订单号"  value="${keyword!}"></input>
                <input type="submit" hidden tabindex="-1"/>
                <button type="button" class="btn yellow btn-sm" onclick="query(1)">
                    <i class="fa fa-search"></i> 查 询
                </button>

            </div>

            </form>
        </div>

        <div style="padding-top: 10px">
            <table id="table_1" table-adv="true"
                   class="table table-striped table-bordered table-hover table-full-width text-center">
                <colgroup>
                    <col width='70px'>
                    <col width='120px'>
                    <col width='100px'>
                    <col width='100px'>
                    <col width='100px'>
                    <col width='100px'>
                    <col width='100px'>
                    <col width='100px'>
                    <col width='100px'>
                </colgroup>
                <thead>
                <tr>
                    <th class="text-center">序号</th>
                    <th class="text-center">银行预留手机号</th>
                    <th class="text-center">姓名</th>
                    <th class="text-center">订单号</th>
                    <th class="text-center">货柜号</th>
                    <th class="text-center">申请时间</th>
                    <th class="text-center">贷款金额</th>
                    <th class="text-center">服务费</th>
                    <th class="text-center">贷款状态</th>
                    <th class="text-center">操作</th>
                </tr>
                </thead>

                <tbody>
                <#list user_loanMangement_list as loanMangement>
                <tr>
                    <td>
                    ${loanMangement.id}
                    </td>

                    <td>
                    ${loanMangement.mobile}
                    </td>

                    <td>
                    ${loanMangement.username}
                    </td>

                    <td>
                    ${loanMangement.orderNo}
                    </td>

                    <td>
                    ${loanMangement.containerId}
                    </td>

                    <td>
                    ${loanMangement.addTimeString}
                    </td>
                    <td>
                    ${loanMangement.appliyLoan?string('0.00')}
                    </td>
                    <td>
                    ${loanMangement.serviceFee?string('0.00')}
                    </td>
                    <td>
                        <#list loanManagement_status_list as item>
                            <#if loanMangement.status = item.id&&item.id!=999>
                                <span >${item.value}</span>
                            </#if>
                            <#if loanMangement.status = item.id&&item.id==999>
                                <span style="color: red">${item.value}</span>
                            </#if>
                        </#list>
                    </td>

                    <td>
                        <a href="${user_loanManagement_info_detail_url}?id=${loanMangement.id}">查看</a>
                    </td>

                </tr>
                </#list>
                </tbody>
            </table>


        <@b.pageSimple pageDto 'userList'/>


        </div>
    </div>
</div>
</div>
</div>

<script src="/admins/assets/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
<script src="/admins/assets/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>

<script src="/admins/js/form.js" type="text/javascript"></script>

<script>

    function query(pageNo) {
        $('#pageNo').val(pageNo);
        var form = $("#thisForm");
        form.attr('action', form.attr('action'));
        form.submit();
    }

    function logistics(){
        //自定页
        layer.open({
            type: 1,
            skin: 'layui-layer-demo', //样式类名
            closeBtn: 0, //不显示关闭按钮
            anim: 2,
            shadeClose: true, //开启遮罩关闭
            content: '内容'
        });
    }


</script>
</body>
</html>