<!DOCTYPE html>

<html class="not-ie" lang="en">
<head>
    <meta charset="utf-8"/>
    <title>查看用户详情-会员认证信息</title>
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
                    <div id="enterprise-info-personal" name="enterprise-info-personal"
                         class="enterprise-info">
                        <ul style="list-style: none">
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

                            <#if user.enterprise.rejectNote?? && user.enterprise.rejectNote?length &gt; 0>
                                <li style="margin-top: 10px">
                                    <span>审核反馈：</span>
                                    <span id="view-personal-rejectNote">
                                        <textarea rows="3" cols="50" disabled="true">${user.enterprise.rejectNote}</textarea>
                                    </span>
                                </li>
                            </#if>

                            <#if user.enterprise.description?? && user.enterprise.description?length &gt; 0>
                                <li style="margin-top: 10px">
                                    <span>审核备注：</span>
                                    <span id="view-personal-description">
                                        <textarea rows="3" cols="50" disabled="true">${user.enterprise.description}</textarea>
                                    </span>
                                </li>
                            </#if>
                        </ul>
                    </div>
                </#if>


                <#if user.enterprise.type==2>
                    <div id="enterprise-info-enterprise" name="enterprise-info-enterprise"
                         class="enterprise-info">
                        <ul style="list-style: none">
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

                            <#if user.enterprise.rejectNote?? && user.enterprise.rejectNote?length &gt; 0>
                                <li style="margin-top: 10px">
                                    <span>审核反馈：</span>
                                    <span id="view-enterprise-rejectNote">
                                        <textarea rows="3" cols="50" disabled="true">${user.enterprise.rejectNote}</textarea>
                                    </span>
                                </li>
                            </#if>

                            <#if user.enterprise.description?? && user.enterprise.description?length &gt; 0>
                                <li style="margin-top: 10px">
                                    <span>审核备注：</span>
                                    <span id="view-enterprise-description">
                                        <textarea rows="3" cols="50" disabled="true">${user.enterprise.description}</textarea>
                                    </span>
                                </li>
                            </#if>
                        </ul>
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

</body>
</html>