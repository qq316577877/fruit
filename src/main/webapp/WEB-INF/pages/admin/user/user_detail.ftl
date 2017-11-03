<!DOCTYPE html>

<html class="not-ie" lang="en">
<head>
    <meta charset="utf-8"/>
    <title>菜单</title>
<#include "/WEB-INF/pages/base/css.ftl">
<#include "/WEB-INF/pages/base/js.ftl">
<#import "/WEB-INF/pages/base/base.ftl"  as b>
</head>
<body>
<div class="page-content extended">
    <div class="scroller" data-height="auto">
        <div class="row">
            <div class="col-xs-12">
                <form id="adminUserForm" name="adminUserForm" action="${save_admin_user_ajax}" class="form-horizontal"
                      method="post">
                    <input type="hidden" name="id" id="id" value="${(user.sysId)!}"/>

                    <div class="form-body">
                        <div class="row">
                            <div class="col-xs-6">
                                <div class="form-group">
                                    <label class="control-label col-xs-4">用户名<span class="required">*</span></label>

                                    <div class="col-xs-8">
                                        <div class="input-group">
                                            <input type='text' class="form-control" validate="{required:true}"
                                                   id='userName'
                                                   name='userName' value='${(user.userName)!}'/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-xs-6">
                                <div class="form-group">
                                    <label class="control-label col-xs-4">角色<span class="required">*</span></label>

                                    <div class="col-xs-8">
                                        <div class="input-group">
                                        <#--<@b.select select_id='type' dictcode='AdminType' required='true' defValue='${(data.type)!}' />-->
                                            <select id="type" name="type">
                                                <#list role_list as role>
                                                    <option value="${role.id}">${role.name}</option>
                                                </#list>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-6">
                                <div class="form-group">
                                    <label class="control-label col-xs-4">中文名<span class="required">*</span></label>

                                    <div class="col-xs-8">
                                        <div class="input-group">
                                            <input type='text' class="form-control" validate="{required:true}"
                                                   id='cnName' name='cnName' value='${(user.cnName)!}'/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-xs-6">
                                <div class="form-group">
                                    <label class="control-label col-xs-4">英文名</label>

                                    <div class="col-xs-8">
                                        <div class="input-group">
                                            <input type='text' class="form-control" id='enName' name='enName'
                                                   value='${(user.enName)!}'/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-6">
                                <div class="form-group">
                                    <label class="control-label col-xs-4">邮箱<span class="required">*</span></label>

                                    <div class="col-xs-8">
                                        <div class="input-group">
                                            <input type='text' class="form-control" validate="{required:true}" id='mail'
                                                   name='mail' value='${(user.mail)!}'/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-xs-6">
                                <div class="form-group">
                                    <label class="control-label col-xs-4">手机<span class="required">*</span></label>

                                    <div class="col-xs-8">
                                        <div class="input-group">
                                            <input type='text' class="form-control" validate="{required:true}"
                                                   id='mobile'
                                                   name='mobile' value='${(user.mobile)!}'/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-6">
                                <div class="form-group">
                                    <label class="control-label col-xs-4">部门</label>

                                    <div class="col-xs-8">
                                        <div class="input-group">
                                            <input type='text' class="form-control" id='depart'
                                                   name='depart' value='${(user.depart)!}'/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-xs-6">
                                <div class="form-group">
                                    <label class="control-label col-xs-4">备注</label>

                                    <div class="col-xs-8">
                                        <div class="input-group">
                                            <input type='text' class="form-control" id='info'
                                                   name='info' value='${(user.info)!}'/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- div class="col-xs-12" -->
                        </div>
                        <!-- div class="row" -->
                    </div>
                    <!--div class="form-body"-->
                    <div class="form-actions fluid">
                        <div class="col-xs-offset-5 col-xs-7">
                            <button type="button" id="savebtn" class="btn btn-shadow green J_BTN"><i
                                    class="fa fa-save"></i> 保 存
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <!--div class="row"-->

    </div>
    <!--div class="scroller" style="height:630px" -->
</div>
<!--div class="page-content extended"-->


<!-- 页面JS类库引入 st-->
<!-- 表单验证 -->
<script type="text/javascript" src="/admins/assets/plugins/jquery-validation/dist/jquery.validate.js"></script>
<script type="text/javascript" src="/admins/assets/plugins/jquery-validation/dist/additional-methods.min.js"></script>
<script type="text/javascript" src="/admins/assets/plugins/jquery-validation/dist/message_cn.js"></script>
<script type="text/javascript" src="/admins/assets/plugins/jquery-validation/dist/jquery.metadata.js"></script>
<!-- 页面所需组件的js文件 -->
<!-- 页面JS类库引入 ed-->
<!-- 私有JS st -->
<script src="/admins/js/form.js" type="text/javascript"></script>
<!-- 私有JS ed -->
<script>
    jQuery(document).ready(function () {
        var v_ = formValidation('adminUserForm');
        var saveUrl = "${save_admin_user_ajax}";

        var options = formSaveOptions(saveUrl, null, null, null, 'id');

        $('#savebtn').click(function () {
            if (!v_.form()) {
                return;
            }
            var form = $("#adminUserForm");
            form.ajaxSubmit(options);
        });
    });
</script>
</body>
</html>