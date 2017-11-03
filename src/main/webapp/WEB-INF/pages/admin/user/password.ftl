<!DOCTYPE html>

<html class="not-ie" lang="en">
<head>
    <meta charset="utf-8"/>
    <title>修改密码</title>
    <link rel="stylesheet" href="/admins/assets/plugins/data-tables/DT_bootstrap.css"/>
    <link rel="stylesheet" href="/admins/assets/css/base.css"/>
    <link href="/admins/assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<#include "/WEB-INF/pages/base/css.ftl">
<#include "/WEB-INF/pages/base/js.ftl">
<#import "/WEB-INF/pages/base/base.ftl"  as base>
</head>
<body>

        <div class="main clearfix">
            <div id="content">
                <div>
                    <h4>修改密码</h4>
                    <hr>

                    <!-- 注意:table的table-adv对应隐藏列组件的ID-->
                    <form id="thisForm" name="thisForm" action="${BASEPATH}/admin/user/changepwd_ajax"
                          class="form-horizontal" method="post">

                        <div class="form-body">
                            <div class="row">
                                <div class="col-xs-6">
                                    <div class="form-group">
                                        <label class="control-label col-xs-4">
                                            原密码：<span class="required">*</span>
                                        </label>

                                        <div class="col-xs-8">
                                            <div class="input-group">
                                                <input type='password' class="form-control" validate="{required:true}"
                                                       id='password' name='password' value=''/>
                                            </div>
                                        </div>
                                    </div>
                                    <!--div class="form-group"-->
                                </div>
                                <!-- div class="col-xs-12" -->
                            </div>

                            <div class="row">
                                <div class="col-xs-6">
                                    <div class="form-group">
                                        <label class="control-label col-xs-4">
                                            新密码：<span class="required">*</span>
                                        </label>

                                        <div class="col-xs-8">
                                            <div class="input-group">
                                                <input type='password' class="form-control" validate="{required:true}"
                                                       id='newPassword' name='newPassword' value=''/>
                                            </div>
                                        </div>
                                    </div>
                                    <!--div class="form-group"-->
                                </div>
                                <!-- div class="col-xs-12" -->
                            </div>

                            <div class="row">
                                <div class="col-xs-6">
                                    <div class="form-group">
                                        <label class="control-label col-xs-4">
                                            确认密码：<span class="required">*</span>
                                        </label>

                                        <div class="col-xs-8">
                                            <div class="input-group">
                                                <input type='password' class="form-control" validate="{required:true}"
                                                       id='confirmPassword' name='confirmPassword' value=''/>
                                            </div>
                                        </div>
                                    </div>
                                    <!--div class="form-group"-->
                                </div>
                                <!-- div class="col-xs-12" -->
                            </div>
                            <!-- div class="row" -->
                            <!-- div class="row" -->
                        </div>
                        <!--div class="form-body"-->
                        <div class="form-actions fluid">
                            <div class="col-xs-offset-5 col-xs-7">
                                <button type="button" id="savebtn" class="btn btn-shadow green"><i
                                        class="fa fa-save"></i> 修 改
                                </button>
                            </div>
                        </div>
                    </form>

                </div>
            </div>
        </div>
        <!--div class="page-content extended "-->


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
        var saveUrl = "${BASEPATH}/admin/user/changepwd_ajax";
        var redirectUrl = "${BASEPATH}/logout";

        var options = formSaveOptions(saveUrl, redirectUrl, null, null, 'roleid');

        options.success = function (data) {
            if (data.result) {
                bootbox.alert("修改成功!", function () {

                            if (redirectUrl) {
                                window.parent.location.href = redirectUrl;
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