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
                <form id="thisForm" name="thisForm" action="${BASEPATH}/menu/save"
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
                            <!-- div class="col-xs-12" -->
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
                        <div class="row">
                            <div class="col-xs-6">
                                <div class="form-group">
                                    <label class="control-label col-xs-4">类型<span class="required">*</span></label>

                                    <div class="col-xs-8">
                                        <div class="input-group">
                                        <@b.select select_id='type' dictcode='AdminMenuType' required='true' defValue='${(data.type)!}' />
                                        </div>
                                    </div>
                                </div>
                                <!--div class="form-group"-->
                            </div>
                            <!-- div class="col-xs-12" -->
                            <div class="col-xs-6">
                                <div class="form-group">
                                    <label class="control-label col-xs-4">链接</label>

                                    <div class="col-xs-8">
                                        <div class="input-group">
                                            <input type='text' class="form-control" validate="{required:false}" id='url'
                                                   name='url' value='${(data.url)!}'/>
                                        </div>
                                    </div>
                                </div>
                                <!--div class="form-group"-->
                            </div>
                            <!-- div class="col-xs-12" -->
                            <div class="col-xs-6">
                                <div class="form-group">
                                    <label class="control-label col-xs-4">名称空间</label>

                                    <div class="col-xs-8">
                                        <div class="input-group">
                                            <input type='text' class="form-control" validate="{required:false}" id='namespace'
                                                   name='namespace' value='${(data.namespace)!}'/>
                                        </div>
                                    </div>
                                </div>
                                <!--div class="form-group"-->
                            </div>
                            <!-- div class="col-xs-12" -->
                        </div>
                        <!-- div class="row" -->
                        <div class="row">
                            <div class="col-xs-12">
                                <div class="form-group">
                                    <label class="control-label col-xs-2">上级菜单<span class="required">*</span></label>

                                    <div class="col-xs-10">
                                        <div class="input-group">
                                        <@b.tree tree_title='菜单' tree_id='PId' url='${BASEPATH}/tree/menu' defValue='${(data.PId)!}' folderSelect='true' required='true' isShowRoot='true' />
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
        var saveUrl = "${BASEPATH}/menu/save";

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