<!DOCTYPE html>

<html class="not-ie" lang="en">
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
        <form id="apply-info-form" name="apply-info-form" class="base-info-form form-horizontal">
            <table cellpadding="5px" >
                <tr>
                    <td align="right">企业名称：</td>
                    <td>${(userLoan.enterpriseName)}</td>
                    <td align="right">婚姻状况：</td>
                    <td>
                    <#list loan_marriage_list as item>
                        <#if userLoan.maritalStatus = item.id><span > ${item.value}</span></#if>
                    </#list>
                    </td>
                </tr>
                <tr>
                    <td align="right">证照号：</td>
                    <td>${userLoan.credential}</td>
                    <td align="right">配偶姓名：</td>
                    <td>${userLoan.partnerName}</td>
                </tr>
                <tr >
                    <td align="right">申请人：</td>
                    <td>${userLoan.username}</td>
                    <td align="right">配偶身份证号：</td>
                    <td>${userLoan.partnerIdentity}</td>
                </tr>
                <tr>
                    <td align="right">身份证号：</td>
                    <td>${userLoan.identity}</td>
                    <td align="right">联系电话：</td>
                    <td>${userLoan.mobile}</td>
                </tr>
                <tr>
                    <td align="right">所在地区：</td>
                    <td>
                    ${userLoan.userLoanVO.countryName}
                        ${userLoan.userLoanVO.provinceName}
                        ${userLoan.userLoanVO.cityName}
                        ${userLoan.userLoanVO.districtName}
                    </td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td align="right">详细地址：</td>
                    <td>${userLoan.address}</td>
                    <td></td>
                    <td></td>
                </tr>

            </table>
        </form>

        <br>
        <form id="user-loan-info-form" name="user-loan-info-form" class="base-info-form form-horizontal">

            <table cellpadding="5px" >
                <tr style="display: none">
                    <td >ID：</td>
                    <td><input name="id" type="text" value="${userLoan.id}" validate="{required:true}"/></td>
                    <td><input name="mobile" type="text" value="${userLoan.mobile}" validate="{required:true}"/></td>
                </tr>

                <tr >
                    <td align="right">批复状态：</td>
                    <td >
                    <#if userLoan.status!=2 >
                        <#list loan_status_list as item>
                            <#if userLoan.status = item.id><span > ${item.value}</span></#if>
                        </#list>
                    </#if>
                    <#if userLoan.status==2 >
                        <select id = "status" name="status" validate="{required:true}">
                            <option value="3">拒绝</option>
                        </select>
                    </#if>
                    </td>

                </tr>

                <tr>
                <#if userLoan.status!=1>
                    <td align="right">拒绝原因：</td>
                    <td>
                        <#if userLoan.status ==3 >
                            <#--<#list loan_reject_list as item>-->
                                <#--<#if userLoan.rejectNote == item.id><span > ${item.value}</span></#if>-->
                            <#--</#list>-->
                            <#if userLoan.rejectNote == 0><span > 征信不良</span></#if>
                            <#if userLoan.rejectNote == 1><span > 负债过高</span></#if>
                        </#if>
                        <#if userLoan.status=2 >
                            <select id = "rejectNote" name="rejectNote" validate="{required:true}">
                                <option value="0">征信不良</option>
                                <option value="1">负债过高</option>
                            </select>
                        </#if>
                    </td>

                </#if>

                </tr>

                <tr validate="{required:true}">
                <#if userLoan.status!=1 >
                    <td align="right">备注信息：</td>
                    <td>
                        <#if userLoan.status==3 >
                        ${userLoan.rejectDescription}
                        </#if>
                        <#if userLoan.status=2 >
                            <textarea id ="rejectDescription" name="rejectDescription"></textarea><br>
                                <span>
                                    说明：请根据银行客户经理提供的客户贷款申请结果谨慎操作,该操作一旦提交，不可更改
                                </span>
                        </#if>
                    </td>

                </#if>
                </tr>

            </table>
        </form>

    <#if userLoan.status=2>
        <div style="padding-left: 100px">
            <a class="btn btn-sm btn-primary" href="#" onclick="updateCredit()">提交</a>
            <#--<a class="btn btn-sm btn-primary" href="#" onclick="resetApply()">取消</a>-->
        </div>
    </#if>

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

    function updateCredit(){
        var rejectDescription = $("#rejectDescription").val();
        if(rejectDescription=="")
        {
            $("#rejectDescription").val("没有备注信息");
        }
        bootbox.confirm('确定拒绝申请？',function(result) {
            if (result) {
                $.ajax("${update_user_credit_info_ajax}", {
                    data: $("#user-loan-info-form").serialize(),
                    type: "POST"
                }).always(function () {
                }).done(function (data) {
                    if (data.result) {
                        bootbox.alert("操作成功!", function () {

                        });
                        window.location.href = "${BASEPATH}/user/credit/detail/apply?id=${userLoan.id}";
//                        refresh();
                    } else {
                        bootbox.alert(data.msg);
                    }
                }).fail(function () {
                });
            }
        });
    };





//    function resetApply(){
//        $("#description").val("");
//        $("#status option[value='1']").attr("selected","selected");
//        $("#rejectNote option[value='0']").attr("selected","selected");
//
//    };
//
//    $(function(){
//        $("#selectStatus").ready(function(){
//            $("#status option[value='2']").attr("selected", "selected");
//        });
//
//    });


</script>
</body>
</html>