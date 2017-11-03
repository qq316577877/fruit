<!DOCTYPE html>

<html class="not-ie" lang="en">
<head>
    <meta charset="utf-8"/>
    <title>清关物流公司</title>
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
                                    <label class="control-label col-xs-4">企业名称<span class="required">*</span></label>

                                    <div class="col-xs-8">
                                        <div class="input-group">
                                            <input type='text' class="form-control" maxlength="64" validate="{required:true}" id='name'
                                                   name='name' value='${(data.name)!}'/>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="col-xs-6">
                                <div class="form-group">
                                    <label class="control-label col-xs-4">企业英文名称</label>

                                    <div class="col-xs-8">
                                        <div class="input-group">
                                            <input type='text' class="form-control" maxlength="64" validate="{required:false,englishOnly:true}" id='enName'
                                                   name='enName' value='${(data.enName)!}'/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-xs-6">
                                <div class="form-group">
                                    <label class="control-label col-xs-4">证件号</label>

                                    <div class="col-xs-8">
                                        <div class="input-group">
                                            <input type='text' class="form-control" maxlength="32" validate="{required:false}" id='credential'
                                                   name='credential' value='${(data.credential)!}'/>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="col-xs-6">
                                <div class="form-group">
                                    <label class="control-label col-xs-4">联系方式</label>

                                    <div class="col-xs-8">
                                        <div class="input-group">
                                            <input type='text' class="form-control" maxlength="32" validate="{required:false}" id='contact'
                                                   name='contact' value='${(data.contact)!}'/>
                                        </div>
                                    </div>
                                </div>
                            </div>


                        </div>

                        <div class="row">

                            <div class="col-xs-6">
                                <div class="form-group">
                                    <label class="control-label col-xs-4">公司类型<span class="required">*</span></label>

                                    <div class="col-xs-8">
                                        <div class="input-group">
                                        <@b.select select_id='type' dictcode='EnterpriseType' required='true' defValue='${(data.type)!}' />
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="col-xs-6">
                                <div class="form-group">
                                    <label class="control-label col-xs-4">地理类型<span class="required">*</span></label>

                                    <div class="col-xs-8">
                                        <div class="input-group">
                                        <@b.select select_id='locationType' dictcode='LocationType' required='true' defValue='${(data.locationType)!}' />
                                        </div>
                                    </div>
                                </div>
                            </div>


                        </div>

                        <#--<div class="row">-->

                            <#--<div class="col-xs-6">-->
                                <#--<div class="form-group">-->
                                    <#--<label class="control-label col-xs-4">状态<span class="required">*</span></label>-->

                                    <#--<div class="col-xs-8">-->
                                        <#--<div class="input-group">-->
                                        <#--<@b.select select_id='status' dictcode='DBStatus' required='true' defValue='${(data.status)!}' />-->
                                        <#--</div>-->
                                    <#--</div>-->
                                <#--</div>-->
                            <#--</div>-->

                        <#--</div>-->


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

<!-- 页面所需组件的js文件 -->
<!-- 页面JS类库引入 ed-->
<!-- 私有JS st -->
<script src="/admins/js/form.js" type="text/javascript"></script>
<!-- 私有JS ed -->
<script>
    jQuery(document).ready(function () {
        var v_ = formValidation('thisForm');
        var saveUrl = "${enterprise_add_url}";

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