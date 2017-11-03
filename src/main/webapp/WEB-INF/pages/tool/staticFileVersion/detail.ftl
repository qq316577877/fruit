<!DOCTYPE html>

<html class="not-ie" lang="en">
<head>
    <meta charset="utf-8"/>
    <title>静态资源版本信息</title>
<#include "/WEB-INF/pages/base/css.ftl">
<#include "/WEB-INF/pages/base/js.ftl">
<#import "/WEB-INF/pages/base/base.ftl"  as b>
</head>
<body>
<div class="page-content extended">
    <div class="scroller" data-height="auto">
        <div class="row">
            <div class="col-xs-12">
                <form id="thisForm" name="thisForm" action="${BASEPATH}/staticFileVersion/save_ajax"
                      class="form-horizontal" method="post">
                    <input type="hidden" name="id" id="id" value="${(data.id!0)}"/>
                    <!-- 隐藏域 ed -->
                    <div class="form-body">
                        <div class="row">
                            <div class="col-xs-6">
                                <div class="form-group">
                                    <label class="control-label col-xs-5">项目<span class="required">*</span></label>

                                    <div class="col-xs-7">
                                        <div class="input-group">
                                            <#if data??>
                                            <input type='text' class="form-control input-xlarge" validate="{required:true}" id='project'
                                                   name='project' value='${(data.project!'')}' readonly="readonly" />
                                            </#if>
                                            <select id ="project" name="project" type='text' class="form-control input-xlarge" <#if data??>style="display: none"</#if>>
                                                <#list static_file_version_list as staticVersion>
                                                    <option value ="${staticVersion.value}">${staticVersion.value}</option>
                                                </#list>
                                            </select>

                                        </div>
                                    </div>
                                </div>
                                <!--div class="form-group"-->
                            </div>
                            <!-- div class="col-xs-12" -->
                            <!-- div class="col-xs-12" -->
                        </div>
                        <div class="row">
                            <div class="col-xs-6">
                                <div class="form-group">
                                    <label class="control-label col-xs-5">版本号</label>

                                    <div class="col-xs-7">
                                        <div class="input-group">
                                            <input type='text' class="form-control  input-xlarge" id='version'
                                                   name='version' value='${(data.version!'')}'/>
                                        </div>
                                    </div>
                                </div>
                                <!--div class="form-group"-->
                            </div>
                            <!-- div class="col-xs-12" -->
                            <!-- div class="col-xs-12" -->
                        </div>

                        <div class="row">
                            <div class="col-xs-6">
                                <div class="form-group">
                                    <label class="control-label col-xs-5">描述</label>

                                    <div class="col-xs-7">
                                        <div class="input-group">
                                            <textarea type='text' rows="3" class="form-control input-xlarge" id='description'
                                                      name='description'><#if data??>${data.description}</#if></textarea>
                                        </div>
                                    </div>
                                </div>
                                <!--div class="form-group"-->
                            </div>
                            <!-- div class="col-xs-12" -->
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
        var v_ = formValidation('thisForm');
        var saveUrl = "${BASEPATH}/staticFileVersion/save_ajax";

        var options = formSaveOptions(saveUrl, null, null, null, 'id');

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