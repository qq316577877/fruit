<!DOCTYPE html>

<html class="not-ie" lang="en">
<head>
    <meta charset="utf-8"/>
    <title>用户详情-基础信息</title>
<#include "/WEB-INF/pages/base/css.ftl">
    <link rel="stylesheet" href="/admins/assets/plugins/data-tables/DT_bootstrap.css"/>
<#include "/WEB-INF/pages/base/js.ftl">
<#import "/WEB-INF/pages/base/base.ftl"  as b>
</head>
<body>
<div class="page-content extended ">
    <div class="tabbable-custom">

            <div class="status-info-box">
                <#--<div>-->
                    <#--<div align="right">-->
                        <#--<a href="${login_as_user_url}?id=${user.userId}" target="_blank">以该用户身份登入水果网前台</a>-->
                    <#--</div>-->
                <#--</div>-->

                <div class="status-info">
                    <ul style="list-style: none">
                        <li style="margin-top: 10px">
                            <span>ID：</span>${user.userId}

                        <#--判断是否展示新老客户标识-->
                            <div style="padding-left: 20px;display: inline-block;">
                                <#if user.enterprise?? && user.enterprise.memberIdentification??>
                                    <#list user_identification_list as item>
                                        <input type="radio" disabled="true" name="identification" value="${item.id}"
                                               <#if user.enterprise.memberIdentification == item.id>checked="checked"</#if> /> ${item.value}
                                    </#list>
                                </#if>
                            </div>


                        </li>

                        <li style="margin-top: 10px">
                            <span>注册日期：</span>${user.addTime?string("yyyy-MM-dd HH:mm:ss")}
                        </li>

                    </ul>
                </div>
            </div>

            <hr>

            <div class="base-info-box">
                <div id="base-info" name="base-info" class="base-info">
                    <ul style="list-style: none">
                        <li style="margin-top: 10px">
                            <span>账号（手机号）：</span><span id="view-mobile">${user.mobile}</span>
                        </li>
                        <li style="margin-top: 10px">
                            <span>邮箱：</span><span id="view-mail">${user.mail}</span>
                        </li>
                        <li style="margin-top: 10px">
                            <span>QQ：</span><span id="view-QQ">${user.QQ}</span>
                        </li>

                    </ul>
                    <div style="padding-left: 100px">
                        <a class="btn btn-sm btn-primary" href="#" onclick="showBaseInfoEditor()">修改基础信息</a>
                    </div>
                </div>
                <div id="base-info-editor" name="base-info-editor" class="" style="display: none; padding-top: 10px">
                    <form id="base-info-form" name="base-info-form" class="base-info-form form-horizontal">

                        <table cellpadding="5px" width="400px">
                            <tr style="display: none">
                                <td align="right">ID：</td>
                                <td class="input-group"><input name="id" type="text" value="${user.userId}"/></td>
                            </tr>

                            <tr class="form-group">
                                <td align="right" class="control-label">账号（手机号）：<span class="required">*</span></td>
                                <td class="input-group"><input id = "mobile" name="mobile" type="text" value="${user.mobile}"
                                                               style="margin-left:0;margin-right:0; width:120%;" validate="{required:true,isMobile:true}" />
                                </td>
                            </tr>

                            <tr class="form-group">
                                <td align="right" class="control-label">邮箱：</td>
                                <td class="input-group"><input id="mail" name="mail" type="text" value="${user.mail}" maxlength="64"
                                           style="margin-left:0;margin-right:0; width:120%;"  validate="{required:false,isEmailCanEmpty:true}"/>
                                </td>
                            </tr>

                            <tr class="form-group">
                                <td align="right" class="control-label">QQ：</td>
                                <td class="input-group"><input id="QQ" name="QQ" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9-]+/,'');}).call(this)" onblur="this.v();"  value="${user.QQ}" minlength="4" maxlength="11" validate="{required:false}"
                                           style="margin-left:0;margin-right:0; width:120%;" /></td>
                            </tr>

                        </table>
                    </form>
                    <div style="padding-left: 100px">
                        <a class="btn btn-sm btn-primary" href="#" onclick="saveBaseInfo()">保存</a>
                        <a class="btn btn-sm btn-primary" href="#" onclick="hideBaseInfoEditor()">取消</a>
                    </div>
                </div>
            </div>

            <hr>



            <div id="password-contorller" name="password-contorller" class="base-info">
                <div style="padding-left: 100px">
                    <a class="btn btn-sm btn-primary" href="#" onclick="showPasswordEditor()">重置密码</a>
                </div>
            </div>

            <div id="password-editor" name="password-editor" class="" style="display: none; padding-top: 10px;">
                <form id="password-form" name="password-form" class="base-info-form form-horizontal">

                    <table cellpadding="5px" width="400px">
                        <tr style="display: none">
                            <td align="right">ID：</td>
                            <td><input name="id" type="text" value="${user.userId}"/></td>
                        </tr>

                        <tr class="form-group">
                            <td align="right" class="control-label">新密码：<span class="required">*</span></td>
                            <td class="input-group"><input id="newPassword" name="newPassword" type="password" minlength="6"  maxlength="18"  validate="{required:true}"
                                       style="margin-left:0;margin-right:0; width:100%;"/>
                            </td>
                        </tr>

                        <tr class="form-group">
                            <td align="right" class="control-label">确认新密码：<span class="required">*</span></td>
                            <td class="input-group"><input id="confirmPassword" name="confirmPassword" type="password" minlength="6"  maxlength="18" validate="{required:true,equalTo:'#newPassword'}"
                                       style="margin-left:0;margin-right:0; width:100%;"/>
                            </td>
                        </tr>
                    </table>
                </form>
                <div style="padding-left: 100px">
                    <a class="btn btn-sm btn-primary" href="#" onclick="updatePassword()">保存</a>
                    <a class="btn btn-sm btn-primary" href="#" onclick="hidePasswordEditor()">取消</a>
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
    var v_base_info;
    var v_password;
    jQuery(document).ready(function () {
        v_base_info = formValidation('base-info-form');
        v_password = formValidation('password-form');
    });
</script>

<script>
    function showBaseInfoEditor() {
        $('#base-info').hide();
        $("#base-info-editor").show();
        $("html,body").animate({scrollTop: $("base-info-editor").offset()}, 300);
    }
    function hideBaseInfoEditor() {
        $("#mobile").val("${user.mobile}");
        $("#mail").val("${user.mail}");
        $("#QQ").val("${user.QQ}");

        $('#base-info').show();
        $("#base-info-editor").hide();
        $("html,body").animate({scrollTop: $("base-info").offset()}, 300);
    }
    function saveBaseInfo() {
        if (!v_base_info.form()) {
            return;
        }else{
            $.ajax("${save_base_info_ajax}", {
                data: $("#base-info-form").serialize(),
                type: "POST"
            }).always(function () {
            }).done(function (data) {
                if (data.result) {
                    bootbox.alert("操作成功!", function () {
                        hideBaseInfoEditor();
                        randerBaseInfo(data.t);
                    });
                } else {
                    bootbox.alert(data.msg);
                }
            }).fail(function () {
            });

        }
    }

    function randerBaseInfo(userinfo) {
        $('#view-username').html(userinfo.userName);
        $('#view-mobile').html(userinfo.mobile);
        $('#view-mail').html(userinfo.mail);
        $('#view-QQ').html(userinfo.qq);
        $('#view-saleName').html(userinfo.saleName);
    }


    /**
     *显示重置密码form
     */
    function showPasswordEditor() {
        $("#password-contorller").hide();
        $("#password-editor").show();
        $("html,body").animate({scrollTop: $("password-editor").offset()}, 300);
    }
    /**
     *隐藏重置密码form
     */
    function hidePasswordEditor() {

        $("#newPassword").val("");
        $("#confirmPassword").val("");

        $("#password-contorller").show();
        $("#password-editor").hide();
        $("html,body").animate({scrollTop: $("password").offset()}, 300);
    }

    /**
     * 重置密码提交form
     */
    function updatePassword() {
        if (!v_password.form()) {
            return;
        }else{
            $.ajax("${update_user_password_ajax}", {
                data: $("#password-form").serialize(),
                type: "POST"
            }).always(function () {
            }).done(function (data) {
                if (data.result) {
                    bootbox.alert("操作成功!", function () {
                        hidePasswordEditor();
                    });
                } else {
                    bootbox.alert(data.msg);
                }
            }).fail(function () {
            });
        }
    }



</script>
</body>
</html>