<!DOCTYPE html>

<html class="not-ie" lang="en">
<head>
    <meta charset="utf-8"/>
    <title>新增单笔产品贷款</title>
<#include "/WEB-INF/pages/base/css.ftl">
<#include "/WEB-INF/pages/base/js.ftl">
<#import "/WEB-INF/pages/base/base.ftl"  as b>
</head>
<body>
<div class="page-content extended">
    <div class="scroller" data-height="auto">
        <div class="row">
            <div class="col-xs-12">
                <form id="adminUserForm" name="adminUserForm" action="${save_product_loan_ajax}" class="form-horizontal"
                      method="post">
                    <input  type="hidden" name="userId" id="userId" value="${userId}"/>

                    <div class="error" id="errorDiv" style="display:none;">
                         已无可用产品添加
                    </div>

                    <div class="form-body" id="formBdDiv">
                        <div class="row">
                            <div class="col-xs-6">
                                <div class="form-group">
                                    <label class="control-label col-xs-4">产品名<span class="required">*</span></label>

                                    <div class="col-xs-8">
                                        <div class="input-group">
                                            <select id="productId" name="productId">
                                                <#list product_list as product>
                                                    <option value="${product.id}">${product.name}</option>
                                                </#list>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-xs-6">
                                <div class="form-group">
                                    <label class="control-label col-xs-4">贷款额度<span class="required">*</span></label>

                                    <div class="col-xs-8">
                                        <div class="input-group">
                                            <input id = "productLoan" name ="productLoan" value="0.00">
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!--div class="form-body"-->
                    <div class="form-actions fluid" id="btnSaveDiv">
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
        var saveUrl = "${save_product_loan_ajax}";
        var options = formSaveOptions(saveUrl, null, null, null, 'id');
        var flag = "${product_flag}";


        //验证产品是否添加完了20170807
        if(flag == "false" || flag == "" ){
            $('#errorDiv').css("display", "");
            $('#btnSaveDiv').css("display", "none");
            $('#formBdDiv').css("display", "none");
        }

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