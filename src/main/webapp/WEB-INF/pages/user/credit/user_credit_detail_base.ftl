
<!DOCTYPE html>

<html class="not-ie" lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="utf-8"/>
    <title>授信详情-贷款申请信息</title>
<#include "/WEB-INF/pages/base/css.ftl">
    <link rel="stylesheet" href="/admins/assets/plugins/data-tables/DT_bootstrap.css"/>
<#include "/WEB-INF/pages/base/js.ftl">
<#import "/WEB-INF/pages/base/base.ftl"  as b>
</head>

<div class="page-content extended ">
    <div class="tabbable-custom">
        <form id="user-loan-info-form" name="user-loan-info-form" class="base-info-form form-horizontal">
            <input type="hidden" name="id" id="id" value="${(userCredit.id)!}"/>
            <table cellpadding="5px">

                <tr class="form-group" >
                    <td align="right" class="control-label">手机号：</td>
                    <td >${userCredit.mobile}</td>
                    <td align="right" class="control-label">客户名称：</td>
                    <td>${userCredit.username}</td>
                </tr>

                <tr class="form-group">
                    <td align="right" class="control-label">身份证号码：</td>
                    <td>${userCredit.identity}</td>
                    <td align="right" class="control-label">信贷编号：</td>
                    <td>${userCredit.crCstNo}</td>
                </tr>

                <tr class="form-group">
                    <td align="right" class="control-label">合同号：</td>
                    <td>${userCredit.ctrNo}</td>
                    <td align="right" class="control-label">合同状态：</td>
                    <td>
                    <#list credit_status_list as item>
                        <#if userCredit.status = item.id><span > ${item.value}</span></#if>
                    </#list>
                    </td>
                </tr>

                <tr class="form-group">
                    <td align="right" class="control-label">合同金额：</td>
                    <td>${userCredit.creditLine?string('0.00')}</td>
                    <td align="right" class="control-label">合同已用金额：</td>
                    <td>${userCredit.expenditure?string('0.00')}</td>
                </tr>

                <tr class="form-group" >
                    <td align="right" class="control-label">起始日期：</td>
                    <td >${userCredit.addTimeString}</td>
                    <td align="right" class="control-label">终止日期：</td>
                    <td >${userCredit.expireTimeString}</td>
                </tr>

                <tr class="form-group">
                    <td align="right" class="control-label">合同循环标识：</td>
                    <td>是</td>
                    <td align="right" class="control-label">业务品种：</td>
                    <td>质押贷款</td>
                </tr>

            </table>

            </br>
            </br>
            </br>
            </br>
            </br>

            <div>
                <tr>
                    <h5><strong>合同补充内容:</strong></strong></h5>
                </tr>
                <tr class="form-group">
                    <td align="right" class="control-label">贷款合同银行账号(九江)：</td>
                    <td class="input-group"><input id = "ctrBankNo" name="ctrBankNo" type="text" maxlength="20" value="${userCredit.ctrBankNo}"/></td>
                    <td align="right" class="control-label">保证合同编号：</td>
                    <td class="input-group"><input id = "insureCtrNo" name="insureCtrNo" type="text" maxlength="32" value="${userCredit.insureCtrNo}"/></td>
                    <a class="btn btn-sm btn-primary" id ="updateBtn" href="#" onclick="updateInfo()">修 改</a>
                    <#--<button type="button" id="saveCreditDetail"  onclick="updateInfo(${userCredit.id},${userCredit.ctrBankNo},${userCredit.insureCtrNo})" > 修 改</button>-->
                </tr>
            </div>
        </form>


    </div>

</div>


<!-- 页面JS类库引入 st-->
<!-- 表格 -->
<script type="text/javascript" src="/admins/assets/plugins/data-tables/jquery.dataTables.min.js"></script>
<!-- 表格添加水平滚动，内容不会换行
<script type="text/javascript" src="/admins/assets/plugins/data-tables/DT_bootstrap.js"></script>
-->
<!-- 页面所需组件的js文件 -->
<script src="/admins/js/form.js" type="text/javascript"></script>
<!-- 页面JS类库引入 ed-->

<script>
    $(document).ready(function(){
        // 初始化内容
        if("${displayFlag}"){
            document.getElementById("ctrBankNo").setAttribute("readOnly","true");
            document.getElementById("insureCtrNo").setAttribute("readOnly","true");
            document.getElementById("updateBtn").style.display = "none";
        }
    });

    function  updateInfo() {
        var id = document.getElementById("id");
        var ctrBankNo = document.getElementById("ctrBankNo");
        var insureCtrNo = document.getElementById("insureCtrNo");

        $.ajax("${update_contract_credit_info_ajax}", {
            data: {id:id.value,
                ctrBankNo:ctrBankNo.value,
                insureCtrNo:insureCtrNo.value },
            type: "POST",
            dataType:"json"
        }).always(function () {
        }).done(function (data) {
            if (data.result) {
                bootbox.alert("操作成功!", function () {

                    document.getElementById("ctrBankNo").setAttribute("readOnly","true");
                    document.getElementById("insureCtrNo").setAttribute("readOnly","true");
                    document.getElementById("updateBtn").style.display = "none";
                });

            } else {
                bootbox.alert(data.msg);
            }
        }).fail(function () {
        });
    }

</script>
</body>
</html>