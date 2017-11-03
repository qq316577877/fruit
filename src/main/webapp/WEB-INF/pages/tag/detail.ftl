<!DOCTYPE html>

<html class="not-ie" lang="en">
<head>
    <meta charset="utf-8"/>
    <title>标签信息</title>
    <link rel="stylesheet" href="/admins/assets/plugins/data-tables/DT_bootstrap.css"/>
    <link href="/admins/assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<#include "/WEB-INF/pages/base/css.ftl">
<#include "/WEB-INF/pages/base/js.ftl">
<#import "/WEB-INF/pages/base/base.ftl"  as b>
</head>
<body>
<div class="page-content extended">
    <div class="" data-height="auto">
        <div class="row">
            <div class="col-xs-12">
                <form id="thisForm" name="thisForm" action="${BASEPATH}/tag/save_ajax"
                      class="form-horizontal" method="post">
                    <input type="hidden" name="id" id="id" value="${(data.id!0)}"/>
                    <input type="hidden" name="status" id="status" value="1"/>
                    <!-- 隐藏域 ed -->
                    <div class="form-body">
                        <div class="row">
                            <div class="col-xs-6">
                                <div class="form-group">
                                    <label class="control-label col-xs-5">父标签ID</label>

                                    <div class="col-xs-7">
                                        <div class="input-group">
                                            <input type='text' class="form-control input-xlarge" id='parentId'
                                                   name='parentId' value='${(data.parentId!'')}'/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-xs-6">
                                <div class="form-group">
                                    <label class="control-label col-xs-5">标签名称<span class="required">*</span></label>

                                    <div class="col-xs-7">
                                        <div class="input-group">
                                            <input type='text' class="form-control input-xlarge" validate="{required:true}" id='name'
                                                   name='name' value='${(data.name!'')}'/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-xs-6">
                                <div class="form-group">
                                    <label class="control-label col-xs-5">描述</label>

                                    <div class="col-xs-7">
                                        <div class="input-group">
                                            <input type='text' class="form-control input-xlarge" id='description'
                                                   name='description' value='${(data.description!'')}'/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-xs-6">
                                <div class="form-group">
                                    <label class="control-label col-xs-5">备注</label>

                                    <div class="col-xs-7">
                                        <div class="input-group">
                                            <input type='text' class="form-control input-xlarge" id='memo'
                                                   name='memo' value='${(data.memo!'')}'/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

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

<script type="text/javascript" src="/admins/assets/plugins/data-tables/jquery.dataTables.min.js"></script>
<!-- 表格添加水平滚动，内容不会换行  -->
<script type="text/javascript" src="/admins/assets/plugins/data-tables/DT_bootstrap.js"></script>

<!-- 日期-->
<script src="/admins/assets/plugins/bootstrap-datetimepicker-master/js/moment-with-locales.js" type="text/javascript"></script>
<script src="/admins/assets/plugins/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"
        type="text/javascript"></script>

<!-- 页面所需组件的js文件 -->
<script src="/admins/js/form.js" type="text/javascript"></script>
<!-- 私有JS ed -->
<script>
    jQuery(document).ready(function () {

        var v_ = formValidation('thisForm');
        var saveUrl = "${BASEPATH}/tag/save_ajax";

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