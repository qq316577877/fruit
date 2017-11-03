<!DOCTYPE html>

<html class="not-ie" lang="en">
<head>
    <meta charset="utf-8"/>
    <title>用户认证审核-会员认证信息</title>
<#include "/WEB-INF/pages/base/css.ftl">
    <link rel="stylesheet" href="/admins/assets/plugins/data-tables/DT_bootstrap.css"/>
<#include "/WEB-INF/pages/base/js.ftl">
<#import "/WEB-INF/pages/base/base.ftl"  as b>
</head>
<body>
<div class="page-content extended ">
    <div class="tabbable-custom">
        <div class="enterprise-info-box">
            <div class="">
                <#if user.enterpriseVerifyStatus?? && user.enterpriseVerifyStatusDesc??>状态：<span id="view-enterpriseVerifyStatus" style="color: #e02222;">${user.enterpriseVerifyStatusDesc}</span></#if>
            </div>

            <hr>

            <div class="">
                <#if user.enterprise==null>(无认证信息)</#if>
            </div>



            <#if user.enterprise??>

                <#if user.enterprise.type==1>
                    <form action="" id="personalForm" name="personalForm" class="form-inline" method="post">

                        <input name="edit-personal-userid" name="edit-personal-userid" type="hidden" value="${id}"/>
                        <input name="edit-personal-id" name="edit-personal-id" type="hidden" value="${user.enterprise.id}"/>

                        <div id="enterprise-info-personal" name="enterprise-info-personal"
                             class="enterprise-info">
                            <ul style="list-style: none">


                                <#if user.enterprise?? && user.enterprise.memberIdentification??>
                                    <li style="margin-top: 1px;">
                                        <div style="padding-top: 10px">
                                            <span>新老客户标识<span class="required">*</span>： </span>
                                                <#list user_identification_list as item>
                                                    <input type="radio"  name="edit-personal-identification" value="${item.id}" validate="{required:true}"
                                                           <#if user.enterprise.memberIdentification == item.id>checked="checked"</#if> /> ${item.value}
                                                </#list>
                                            <span style="color: #e02222;">（说明：审核时请标记该客户是新客户还是老客户！）</span>
                                        </div>
                                    </li>
                                </#if>




                                <li style="margin-top: 10px;font-weight:bolder;font-size:16px">
                                    <span>企业类型：</span><span id="view-personal-type">${user.enterprise.typeDesc}</span>
                                </li>
                                <li style="margin-top: 10px">
                                    <span>姓名：</span><span id="view-personal-name">${user.enterprise.name}</span>
                                </li>
                                <li style="margin-top: 10px">
                                    <span>身份证号：</span><span id="view-personal-identity">${user.enterprise.identity}</span>
                                </li>
                                <li style="margin-top: 10px">
                                    <span>所在地区：</span><span
                                        id="view-personal-area">${user.enterprise.countryName} - ${user.enterprise.provinceName} - ${user.enterprise.cityName} - ${user.enterprise.districtName}</span>
                                </li>
                                <li style="margin-top: 10px">
                                    <span>详细地址：</span><span id="view-personal-address">${user.enterprise.address}</span>
                                </li>
                                <li style="margin-top: 10px">
                                    <span>联系电话：</span><span id="view-personal-phoneNum">${user.enterprise.phoneNum}</span>
                                </li>

                                <#if user.enterprise.attachmentOne?? && user.enterprise.attachmentOne?length &gt; 0>
                                    <li style="margin-top: 10px">
                                        <span>业务合同或凭证1：</span>
                                        <span id="view-personal-attachmentOne">
                                            <img src="${user.enterprise.attachmentOneUrl}" height="100" width="150">
                                            <a href="${user.enterprise.attachmentOneUrl}" target="_blank">点击查看原图</a>
                                        </span>
                                    </li>
                                </#if>

                                <#if user.enterprise.attachmentTwo?? && user.enterprise.attachmentTwo?length &gt; 0>
                                    <li style="margin-top: 10px">
                                        <span>业务合同或凭证2：</span>
                                        <span id="view-personal-attachmentTwo">
                                            <img src="${user.enterprise.attachmentTwoUrl}" height="100" width="150">
                                            <a href="${user.enterprise.attachmentTwoUrl}" target="_blank">点击查看原图</a>
                                        </span>
                                    </li>
                                </#if>

                                <#if user.enterprise.identityFront?? && user.enterprise.identityFront?length &gt; 0>
                                    <li style="margin-top: 10px">
                                        <span>个人身份证正面：</span>
                                        <span id="view-personal-identityFront">
                                            <img src="${user.enterprise.identityFrontUrl}" height="100" width="150">
                                            <a href="${user.enterprise.identityFrontUrl}" target="_blank">点击查看原图</a>
                                        </span>
                                    </li>
                                </#if>

                                <#if user.enterprise.identityBack?? && user.enterprise.identityBack?length &gt; 0>
                                    <li style="margin-top: 10px">
                                        <span>个人身份证反面：</span>
                                        <span id="view-personal-identityBack">
                                            <img src="${user.enterprise.identityBackUrl}" height="100" width="150">
                                            <a href="${user.enterprise.identityBackUrl}" target="_blank">点击查看原图</a>
                                        </span>
                                    </li>
                                </#if>


                                <li style="margin-top: 10px">
                                    <span>审核反馈：</span>
                                    <span id="view-personal-rejectNote">
                                        <textarea id="edit-personal-rejectNote" name="edit-personal-rejectNote" rows="3" cols="50"
                                                  placeholder="如若用户会员认证信息未通过审核，请在此栏填写未通过详细理由，方便用户修正企业信息重新提交审核"
                                                   maxlength="64"  validate="{required:false}"></textarea>
                                    </span>
                                </li>



                                <li style="margin-top: 10px">
                                    <span>审核备注：</span>
                                    <span id="view-personal-description">
                                        <textarea id="edit-personal-description" name="edit-personal-description" rows="3" cols="50"
                                                  placeholder="审核过程中的备注信息，方便下一审核人"
                                                  maxlength="256"  validate="{required:false}"></textarea>
                                    </span>
                                </li>


                            </ul>
                        </div>
                    </form>

                    <div style="padding-left: 100px">
                        <a class="btn btn-sm btn-primary" href="#" onclick="personalVerifyPass()">认证通过</a>
                        <a class="btn btn-sm btn-primary" href="#" onclick="personalVerifyUnPass()">认证未通过</a>
                    </div>
                </#if>


                <#if user.enterprise.type==2>
                    <form action="" id="enterpriseForm" name="enterpriseForm" class="form-inline" method="post">

                        <input name="edit-enterprise-userid" name="edit-enterprise-userid" type="hidden" value="${id}"/>
                        <input name="edit-enterprise-id" name="edit-enterprise-id" type="hidden" value="${user.enterprise.id}"/>

                        <div id="enterprise-info-enterprise" name="enterprise-info-enterprise"
                             class="enterprise-info">
                            <ul style="list-style: none">

                                <#if user.enterprise?? && user.enterprise.memberIdentification??>
                                    <li style="margin-top: 1px;">
                                        <div style="padding-top: 10px">
                                            <span>新老客户标识<span style="color: #e02222;">*</span>： </span>
                                            <#list user_identification_list as item>
                                                <input type="radio"  name="edit-enterprise-identification" value="${item.id}" validate="{required:true}"
                                                       <#if user.enterprise.memberIdentification == item.id>checked="checked"</#if> /> ${item.value}
                                            </#list>
                                            <span style="color: #e02222;">（说明：审核时请标记该客户是新客户还是老客户！）</span>
                                        </div>
                                    </li>
                                </#if>

                                <li style="margin-top: 10px;font-weight:bolder;font-size:16px">
                                    <span>企业类型：</span><span id="view-enterprise-type">${user.enterprise.typeDesc}</span>
                                </li>
                                <li style="margin-top: 10px">
                                    <span>企业名称：</span><span id="view-enterprise-enterpriseName">${user.enterprise.enterpriseName}</span>
                                </li>
                                <li style="margin-top: 10px">
                                    <span>证件号：</span><span id="view-enterprise-credential">${user.enterprise.credential}</span>
                                </li>
                                <li style="margin-top: 10px">
                                    <span>法人姓名：</span><span id="view-enterprise-name">${user.enterprise.name}</span>
                                </li>
                                <li style="margin-top: 10px">
                                    <span>法人身份证号：</span><span id="view-enterprise-identity">${user.enterprise.identity}</span>
                                </li>
                                <li style="margin-top: 10px">
                                    <span>所在地区：</span><span
                                        id="view-enterprise-area">${user.enterprise.countryName} - ${user.enterprise.provinceName} - ${user.enterprise.cityName} - ${user.enterprise.districtName}</span>
                                </li>
                                <li style="margin-top: 10px">
                                    <span>详细地址：</span><span id="view-enterprise-address">${user.enterprise.address}</span>
                                </li>
                                <li style="margin-top: 10px">
                                    <span>联系电话：</span><span id="view-enterprise-phone">${user.enterprise.phoneNum}</span>
                                </li>

                                <#if user.enterprise.licence?? && user.enterprise.licence?length &gt; 0>
                                    <li style="margin-top: 10px">
                                        <span>营业执照/社会信用代码证：</span>
                                        <span id="view-enterprise-licence">
                                            <img src="${user.enterprise.licenceUrl}" height="100" width="150">
                                            <a href="${user.enterprise.licenceUrl}" target="_blank">点击查看原图</a>
                                        </span>
                                    </li>
                                </#if>

                                <#if user.enterprise.identityFront?? && user.enterprise.identityFront?length &gt; 0>
                                    <li style="margin-top: 10px">
                                        <span>法人身份证正面：</span>
                                        <span id="view-enterprise-identityFront">
                                            <img src="${user.enterprise.identityFrontUrl}" height="100" width="150">
                                            <a href="${user.enterprise.identityFrontUrl}" target="_blank">点击查看原图</a>
                                        </span>
                                    </li>
                                </#if>

                                <#if user.enterprise.identityBack?? && user.enterprise.identityBack?length &gt; 0>
                                    <li style="margin-top: 10px">
                                        <span>法人身份证反面：</span>
                                        <span id="view-enterprise-identityBack">
                                            <img src="${user.enterprise.identityBackUrl}" height="100" width="150">
                                            <a href="${user.enterprise.identityBackUrl}" target="_blank">点击查看原图</a>
                                        </span>
                                    </li>
                                </#if>

                                <li style="margin-top: 10px">
                                    <span>审核反馈：</span>
                                    <span id="view-enterprise-rejectNote">
                                        <textarea id="edit-enterprise-rejectNote" name="edit-enterprise-rejectNote" rows="3" cols="50"
                                                  placeholder="如若用户会员认证信息未通过审核，请在此栏填写未通过详细理由，方便用户修正企业信息重新提交审核"
                                                  maxlength="64"  validate="{required:false}"></textarea>
                                    </span>
                                </li>



                                <li style="margin-top: 10px">
                                    <span>审核备注：</span>
                                    <span id="view-enterprise-description">
                                        <textarea id="edit-enterprise-description" name="edit-enterprise-description" rows="3" cols="50"
                                                  placeholder="审核过程中的备注信息，方便下一审核人"
                                                  maxlength="256"  validate="{required:false}"></textarea>
                                    </span>
                                </li>
                            </ul>
                        </div>
                    </form>

                    <div style="padding-left: 100px">
                        <a class="btn btn-sm btn-primary" href="#" onclick="enterpriseVerifyPass()">认证通过</a>
                        <a class="btn btn-sm btn-primary" href="#" onclick="enterpriseVerifyUnPass()">认证未通过</a>
                    </div>
                </#if>




            </#if>
        </div>
    </div>
</div>
</div>
<!--div class="page-content extended"-->


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




    /**
     * 检查个人认证中新老客户标识不为空
     *
     * @return true(不为空) false（空）
     * */
    function checkPersonFormIdentificationNotNull(){
        var identification =  $("input[name='edit-personal-identification']:checked").val();
        if(identification && identification!=""){
            return true;
        }else{
            bootbox.alert("请标记该客户是新客户还是老客户！");
            return false;
        }
    }


    /**
     * 企业认证--个人（认证通过）
     */
    function personalVerifyPass() {
        if(!checkPersonFormIdentificationNotNull()){
            return;
        }

        bootbox.confirm("确定提交认证通过吗？",function(result) {
            if (result) {
                $.ajax("${auth_enterprise_person_pass_ajax}", {
                    data: $("#personalForm").serialize(),
                    type: "POST"
                }).always(function () {
                }).done(function (data) {
                    if (data.result) {
                        bootbox.alert("操作成功,返回列表!", function () {
                            //返回列表
                            window.location.href = "${user_auth_list_url}";
                        });
                    } else {
                        bootbox.alert(data.msg);
                    }
                }).fail(function () {});
            }
        });

    }


    /**
     *检查认证未通过的必填项
     *
     * 如果验证不通过，返回false
     */
    function checkFormOfPersonalUnpass(){

        var rejectNote = $("#edit-personal-rejectNote").val();
        var input  = /^[\s]*$/;//检测是否输入的全是空格

        if(rejectNote && rejectNote!=""){
            if(input.test(rejectNote)){
                return false;
            }
        }else{
            return false;
        }

        return true;
    }

    /**
     * 企业认证--个人（认证未通过）
     */
    function personalVerifyUnPass() {
        if(!checkPersonFormIdentificationNotNull()){
            return;
        }
        if (!checkFormOfPersonalUnpass()) {
            bootbox.alert("审核反馈信息不能为空，请填写审核反馈信息");
        }else{
            $.ajax("${auth_enterprise_person_unpass_ajax}", {
                data: $("#personalForm").serialize(),
                type: "POST"
            }).always(function () {
            }).done(function (data) {
                if (data.result) {
                    bootbox.alert("操作成功,返回列表!", function () {
                        //返回列表
                        window.location.href = "${user_auth_list_url}";
                    });
                } else {
                    bootbox.alert(data.msg);
                }
            }).fail(function () {});
        }
    }



</script>



<script>


    /**
     * 检查企业认证中新老客户标识不为空
     *
     * @return true(不为空) false（空）
     * */
    function checkEnterpriseFormIdentificationNotNull(){
        var identification =  $("input[name='edit-enterprise-identification']:checked").val();
        if(identification && identification!=""){
            return true;
        }else{
            bootbox.alert("请标记该客户是新客户还是老客户！");
            return false;
        }
    }


    /**
     * 企业认证--企业（认证通过）
     */
    function enterpriseVerifyPass() {
        if(!checkEnterpriseFormIdentificationNotNull()){
            return;
        }

        bootbox.confirm("确定提交认证通过吗？",function(result) {
            if (result) {
                $.ajax("${auth_enterprise_enterprise_pass_ajax}", {
                    data: $("#enterpriseForm").serialize(),
                    type: "POST"
                }).always(function () {
                }).done(function (data) {
                    if (data.result) {
                        bootbox.alert("操作成功,返回列表!", function () {
                            //返回列表
                            window.location.href = "${user_auth_list_url}";
                        });
                    } else {
                        bootbox.alert(data.msg);
                    }
                }).fail(function () {});
            }
        });


    }


    /**
     *检查认证未通过的必填项
     *
     * 如果验证不通过，返回false
     */
    function checkFormOfEnterpriseUnpass(){
        var rejectNote = $("#edit-enterprise-rejectNote").val();
        var input  = /^[\s]*$/;//检测是否输入的全是空格

        if(rejectNote && rejectNote!=""){
            if(input.test(rejectNote)){
                return false;
            }
        }else{
            return false;
        }

        return true;
    }

    /**
     * 企业认证--企业（认证未通过）
     */
    function enterpriseVerifyUnPass() {
        if(!checkEnterpriseFormIdentificationNotNull()){
            return;
        }

        if (!checkFormOfEnterpriseUnpass()) {
            bootbox.alert("审核反馈信息不能为空，请填写审核反馈信息");
        }else{
            $.ajax("${auth_enterprise_enterprise_unpass_ajax}", {
                data: $("#enterpriseForm").serialize(),
                type: "POST"
            }).always(function () {
            }).done(function (data) {
                if (data.result) {
                    bootbox.alert("操作成功,返回列表!", function () {
                        //返回列表
                        window.location.href = "${user_auth_list_url}";
                    });
                } else {
                    bootbox.alert(data.msg);
                }
            }).fail(function () {});
        }
    }



</script>

</body>
</html>