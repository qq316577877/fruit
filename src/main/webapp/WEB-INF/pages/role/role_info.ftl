<!DOCTYPE html>

<html class="not-ie" lang="en">
<head>
    <meta charset="utf-8"/>
    <title>角色</title>
<#include "/WEB-INF/pages/base/css.ftl">
<#include "/WEB-INF/pages/base/js.ftl">
<#import "/WEB-INF/pages/base/base.ftl"  as b>
</head>
<body>
<div class="page-content opener">
    <div class="row">
        <div class="col-xs-12">
            <form id="thisForm" name="thisForm" action="${BASEPATH}/role/save"
                  class="form-horizontal" method="post">
                <!-- 隐藏域 st -->
                <input type="hidden" name="id" id="id" value="${(data.id)!}"/>
                <!-- 隐藏域 ed -->
                <div class="form-body">
                    <div class="row">
                        <div class="col-xs-6">
                            <div class="form-group">
                                <label class="control-label col-xs-4">名称<span class="required">*</span></label>

                                <div class="col-xs-8">
                                    <div class="input-group">
                                        <input type='text' class="form-control" validate="{required:true}" id='name'
                                               name='name' value='${(data.name)!}'/>
                                    </div>
                                </div>
                            </div>
                            <!--div class="form-group"-->
                        </div>
                        <div class="col-xs-6">
                            <div class="form-group">
                                <label class="control-label col-xs-4">父角色<span class="required">*</span></label>

                                <div class="col-xs-8">
                                    <div class="input-group">
                                    <#--<@b.select select_id='type' dictcode='AdminType' required='true' defValue='${(data.type)!}' />-->
                                        <select id="parentId" name="parentId">
                                        <#list role_list as role>
                                            <option value="${role.id}" <#if role.id == data.parentId>selected</#if> >${role.name}</option>
                                        </#list>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        <!-- div class="col-xs-12" -->
                    </div>
                    <!-- div class="row" -->

                    <div class="row">
                        <div class="col-xs-6">
                            <div class="form-group">
                                <label class="control-label col-xs-4">状态<span class="required">*</span></label>

                                <div class="col-xs-8">
                                    <div class="input-group">
                                    <@b.select select_id='status' dictcode='AdminStatus' required='true' defValue='${(data.status)!}' />
                                    </div>
                                </div>
                            </div>
                            <!--div class="form-group"-->
                        </div>
                        <!-- div class="col-xs-12" -->
                    </div>
                    <!-- div class="row" -->
                </div>
                <!--div class="form-body"-->
                <div class="form-actions fluid">
                    <div class="col-xs-offset-5 col-xs-7">
                        <button type="button" id="savebtn" class="btn btn-shadow green"><i class="fa fa-save"></i> 保 存
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <!--div class="row"-->

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
        var v_ = formValidation('thisForm');
        var saveUrl = "${BASEPATH}/role/save";
        var redirectUrl = "${BASEPATH}/role/info?id=${(data.id)!}";

        var options = formSaveOptions(saveUrl, null, redirectUrl, null, 'roleid');

        options.success = function (data) {
            if (data.result) {
                bootbox.alert("保存成功!", function () {
                            if (data.t) {
                                if ($("#id").val() == '') {
                                    parent.location.href = "${BASEPATH}/role/detail?id=" + data.t;
                                    return;
                                }
                                $("#id").val(data.t);
                            }

                            if (redirectUrl) {
                                window.location.href = redirectUrl;
                            }
                        }
                );
            } else {
                bootbox.alert(data.msg);
            }
        }

        $('#savebtn').click(function () {
            if (!v_.form()) {
                return;
            }
            var form = $("#thisForm");
            form.ajaxSubmit(options);
        });


    });
</script>
</body>
</html>