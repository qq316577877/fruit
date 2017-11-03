<!DOCTYPE html>
<html class="not-ie" lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <title>贷后管理</title>
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
            <div style="padding-top: 10px">
                <table id="loanCollect">
                    <tr>
                        <td>借据总额：</td>
                        <td>${repayment_collect_model.offerLoanAmount?string('0.00')}</td>
                        <td>总借据数：</td>
                        <td>${repayment_collect_model.offerLoanCount}</td>
                    </tr>

                    <tr>
                        <td>待还款总额：</td>
                        <td>${repayment_collect_model.unpayAmount?string('0.00')}</td>
                        <td>待还款笔数：</td>
                        <td>${repayment_collect_model.unpayCount}</td>
                    </tr>

                    <tr>
                        <td>已还款总额：</td>
                        <td>${repayment_collect_model.repaymentAmount?string('0.00')}</td>
                        <td>已还款笔数：</td>
                        <td>${repayment_collect_model.repaymentCount}</td>
                    </tr>

                    <tr>
                        <td>代还款总额：</td>
                        <td>${repayment_collect_model.depositAmount?string('0.00')}</td>
                        <td>代还款笔数：</td>
                        <td>${repayment_collect_model.depositCount}</td>
                    </tr>

                </table>

                <form action="${user_repayment_list_url}" id="thisForm" name="thisForm" class="form-inline"
                      method="post">

                <#list loanManagement_status_list as item>
                    <input type="radio" name="status" value="${item.id}"
                           <#if status == item.id>checked="checked"</#if> onclick="query(1)"/> ${item.value}
                </#list>

                    <input type="hidden" id="pageNo" name="pageNo" value="1"/>
                    <input style="width:200px;margin-left:20px;" type="text" id="keyword" name="keyword"
                           placeholder="请输入订单号、借据号 查询" value="${keyword!}"></input>
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
                    <col width='80px'>
                    <col width='100px'>
                    <col width='100px'>
                    <col width='100px'>
                    <col width='120px'>
                    <col width='120px'>
                    <col width='100px'>
                    <col width='70px'>
                </colgroup>
                <thead>
                <tr>
                    <th class="text-center">序号</th>
                    <th class="text-center">银行预留手机号</th>
                    <th class="text-center">姓名</th>
                    <th class="text-center">货柜号</th>
                    <th class="text-center">借据号</th>
                    <th class="text-center">借据金额</th>
                    <th class="text-center">借据状态</th>
                    <th class="text-center">强制还款日期</th>
                    <th class="text-center">借款凭证</th>
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
                    ${loanMangement.containerId}
                    </td>

                    <td>
                    ${loanMangement.dbtNo}
                    </td>

                    <td>
                    ${loanMangement.offerLoan?string('0.00')}
                    </td>
                    <td>
                        <#list loanManagement_status_list as item>
                            <#if loanMangement.status = item.id><span> ${item.value}</span></#if>
                        </#list>
                    </td>
                    <td>
                    ${loanMangement.expiresTimeString}
                    </td>

                    <td>
                        <#if loanMangement.contractUrl !='' >
                            <a onclick="contracShow('${loanMangement.contractUrl}')"  href="javascript:;">查看凭证</a>
                        <#else>
                            <a href="javascript:;"></a>
                        </#if>
                    </td>

                    <td>
                        <a href="${user_repayment_info_detail_url}?id=${loanMangement.id}">查看</a>
                    </td>

                </tr>
                </#list>
                </tbody>
            </table>


        <@b.pageSimple pageDto 'userList'/>


        </div>
    </div>
</div>

<div id="mask">
    <div id="pdf1" style="width:700px; height:600px;margin-left: -350px; display: none">
        1It appears you don't have Adobe Reader or PDF support in this web browser.
        <a href="~/pdf/CGVET22-08-2011V2P.pdf">Click here to download the PDF</a>

    </div>
</div>

</div>
</div>


<script src="/admins/assets/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
<script src="/admins/js/form.js" type="text/javascript"></script>
<script src="/admins/assets/plugins/pdfobject.min.js"></script>
<script>

    function query(pageNo) {
        $('#pageNo').val(pageNo);
        var form = $("#thisForm");
        form.attr('action', form.attr('action'));
        form.submit();
    }

    function contracShow(url) {
        $('#btnCan').remove();
        $('#pdf1').show();
        $('#mask').show();
        $('#pdf1 embed').css({
            'width': '650px',
            'height': '550px'
        });
        $('#mask').append('<button id="btnCan">关闭</button>');
        var options = {
            fallbackLink: "<p>该浏览器不支持pdf预览，请点击<a href='[" + url + "]'>此处</a>下载预览</p>"
        };
        PDFObject.embed(url, "#pdf1", options);
    }

    $('#mask').on('click', '#btnCan', function () {
        $('#mask').hide();
    })


</script>
</body>
</html>