<!DOCTYPE html>

<html class="not-ie" lang="en">
<head>
    <meta charset="utf-8"/>
    <title>查看供应商详情-基础信息</title>
<#include "/WEB-INF/pages/base/css.ftl">
    <link rel="stylesheet" href="/admins/assets/plugins/data-tables/DT_bootstrap.css"/>
<#include "/WEB-INF/pages/base/js.ftl">
<#import "/WEB-INF/pages/base/base.ftl"  as b>
</head>
<body>
<div class="page-content extended ">
    <div class="tabbable-custom">

            <div class="status-info-box">
                <div class="status-info">
                    <ul style="list-style: none">
                        <li style="margin-top: 10px;font-weight:bolder;font-size:16px">
                            <span>供应商信息</span>
                        </li>
                    </ul>
                </div>
            </div>

            <hr>

            <div class="base-info-box">
                <div id="base-info" name="base-info" class="base-info">
                    <ul style="list-style: none">
                        <li style="margin-top: 2px">
                            <span>供应商名称：</span><span id="view-supplierName">${supplier.supplierName}</span>
                        </li>
                        <li style="margin-top: 10px">
                            <span>所在地区：</span><span id="view-area">${supplier.countryName} - ${supplier.provinceName} - ${supplier.cityName} - ${supplier.districtName}</span>
                        </li>
                        <li style="margin-top: 10px">
                            <span>详细地址：</span><span id="view-address">${supplier.address}</span>
                        </li>
                        <li style="margin-top: 10px">
                            <span>邮政编码：</span><span id="view-zipCode">${supplier.zipCode}</span>
                        </li>
                        <li style="margin-top: 10px">
                            <span>联系人：</span><span id="view-supplierContact">${supplier.supplierContact}</span>
                        </li>
                        <li style="margin-top: 10px">
                            <span>手机号：</span><span id="view-cellPhone">${supplier.cellPhone}</span>
                        </li>
                        <li style="margin-top: 10px">
                            <span>电话号码：</span><span id="view-phoneNum">${supplier.phoneNum}</span>
                        </li>
                    </ul>
                </div>
            </div>

            <hr>


            <div class="status-info-box">
                <div class="status-info">
                    <ul style="list-style: none">
                        <li style="margin-top: 10px;font-weight:bolder;font-size:16px">
                            <span>
                                交易用户
                                <#if supplier.userInfo?? && supplier.userInfo.mobile?? && supplier.userInfo.mobile?length gt 0>
                                    <span style="color:red">(${supplier.userInfo.mobile})</span>
                                </#if>
                                基本信息
                            </span>
                        </li>
                    </ul>
                </div>
            </div>

            <hr>

            <#if supplier.userInfo??>
                <div class="base-info-box">
                    <div id="user-base-info" name="base-info" class="base-info">
                        <ul style="list-style: none">
                            <#if supplier.userInfo?? && supplier.userInfo.mobile?? && supplier.userInfo.mobile?length gt 0>
                                <li style="margin-top: 2px">
                                    <span>账号：</span><span id="view-user-mobile">${supplier.userInfo.mobile}</span>
                                </li>
                            </#if>

                            <#if supplier.userInfo??>
                                <li style="margin-top: 10px">
                                    <#if supplier.userInfo.mail?? && supplier.userInfo.mail?length gt 0>
                                        <span>邮箱：</span><span id="view-user-mail">${supplier.userInfo.mail}</span>
                                        <#else>
                                            <span>邮箱：</span><span id="view-user-mail">未绑定邮箱</span>
                                    </#if>
                                </li>
                            </#if>

                            <#if supplier.userInfo?? && supplier.userInfo.QQ?? && supplier.userInfo.QQ?length gt 0>
                                <li style="margin-top: 10px">
                                    <span>QQ号：</span><span id="view-user-QQ">${supplier.userInfo.QQ}</span>
                                </li>
                            </#if>


                            <!--企业类型：未认证 -->
                            <#if supplier.userInfo?? && supplier.userInfo.type?? && supplier.userInfo.type==0>
                                    <li style="margin-top: 10px;font-weight:bolder;color:red;">
                                        <span>企业信息：</span>
                                        <span id="view-user-none-type">未认证</span>
                                    </li>
                            </#if>

                            <!--企业类型：个人 -->
                            <#if supplier.userInfo?? && supplier.userInfo.type?? && supplier.userInfo.type==1>
                                <#if supplier.enterprise?? && supplier.enterprise.typeDesc?? && supplier.enterprise.typeDesc?length gt 0>
                                    <li style="margin-top: 10px;font-weight:bolder;">
                                        <span>企业类型：</span><span id="view-user-personal-type">${supplier.enterprise.typeDesc}</span>
                                    </li>
                                </#if>

                                <#if supplier.enterprise?? && supplier.enterprise.name?? && supplier.enterprise.name?length gt 0>
                                    <li style="margin-top: 10px">
                                        <span>姓名：</span><span id="view-user-personnal-name">${supplier.enterprise.name}</span>
                                    </li>
                                </#if>


                                <li style="margin-top: 10px">
                                    <span>单位所在地：</span><span id="view-user-personnal-area">${supplier.enterprise.countryName} - ${supplier.enterprise.provinceName} - ${supplier.enterprise.cityName} - ${supplier.enterprise.districtName}</span>
                                </li>

                            </#if>


                            <!--企业类型：企业 -->
                            <#if supplier.userInfo?? && supplier.userInfo.type?? && supplier.userInfo.type==2>
                                <#if supplier.enterprise?? && supplier.enterprise.typeDesc?? && supplier.enterprise.typeDesc?length gt 0>
                                    <li style="margin-top: 10px;font-weight:bolder;">
                                        <span>企业类型：</span><span id="view-user-enterprise-type">${supplier.enterprise.typeDesc}</span>
                                    </li>
                                </#if>

                                <#if supplier.enterprise?? && supplier.enterprise.enterpriseName?? && supplier.enterprise.enterpriseName?length gt 0>
                                    <li style="margin-top: 10px">
                                        <span>企业名称：</span><span id="view-user-enterprise-enterpriseName">${supplier.enterprise.enterpriseName}</span>
                                    </li>
                                </#if>

                                <li style="margin-top: 10px">
                                    <span>企业证件号：</span><span id="view-user-enterprise-credential">${supplier.enterprise.credential}</span>
                                </li>

                                <#if supplier.enterprise?? && supplier.enterprise.name?? && supplier.enterprise.name?length gt 0>
                                    <li style="margin-top: 10px">
                                        <span>法人姓名：</span><span id="view-user-enterprise-name">${supplier.enterprise.name}</span>
                                    </li>
                                </#if>

                                <li style="margin-top: 10px">
                                    <span>单位所在地：</span><span id="view-user-enterprise-area">${supplier.enterprise.countryName} - ${supplier.enterprise.provinceName} - ${supplier.enterprise.cityName} - ${supplier.enterprise.districtName}</span>
                                </li>

                            </#if>



                        </ul>
                    </div>
                </div>
            </#if>

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