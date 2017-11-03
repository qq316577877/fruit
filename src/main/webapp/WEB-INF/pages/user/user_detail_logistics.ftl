<!DOCTYPE html>

<html class="not-ie" lang="en">
<head>
    <meta charset="utf-8"/>
    <title>用户详情-物流清关公司配置信息</title>
<#include "/WEB-INF/pages/base/css.ftl">
    <link rel="stylesheet" href="/admins/assets/plugins/data-tables/DT_bootstrap.css"/>

<#include "/WEB-INF/pages/base/js.ftl">
<#import "/WEB-INF/pages/base/base.ftl"  as b>

</head>
<body>
<div class="page-content extended ">

    <div class="tabbable-custom">



        <div class="base-info-box">
            <span style="margin-top: 10px;font-weight:bolder;font-size:16px">国内物流公司配置</span>

            <#if userProfileModel?? && userProfileModel.nationalLogisticsModel??>
                <div id="nationalLogistics-info" name="nationalLogistics-info" class="base-info">
                    <ul style="list-style: none">
                        <li style="margin-top: 10px">
                            <span>企业名称：</span><span id="view-nationalLogistics-name">${userProfileModel.nationalLogisticsModel.name}</span>
                        </li>
                        <li style="margin-top: 10px">
                            <span>英文名称：</span><span id="view-nationalLogistics-enName">${userProfileModel.nationalLogisticsModel.enName}</span>
                        </li>
                        <li style="margin-top: 10px">
                            <span>证件号：</span><span id="view-nationalLogistics-credential">${userProfileModel.nationalLogisticsModel.credential}</span>
                        </li>
                        <li style="margin-top: 10px">
                            <span>联系方式：</span><span id="view-nationalLogistics-contact">${userProfileModel.nationalLogisticsModel.contact}</span>
                        </li>
                    </ul>
                </div>
            </#if>

            <div id="nationalLogistics-contorller" name="nationalLogistics-contorller" style="padding-left: 100px">
                <a class="btn btn-sm btn-primary" href="javascript:;" onclick="showNationalLogisticsInfoEditor()">配置信息</a>
            </div>

            <div id="nationalLogistics-info-editor" name="nationalLogistics-info-editor" class="" style="display: none; padding-top: 10px">
                <form id="nationalLogistics-info-form" name="nationalLogistics-info-form" class="base-info-form form-horizontal">

                    <table cellpadding="5px" width="400px">
                        <tr style="display: none">
                            <td align="right">USERID：</td>
                            <td class="input-group"><input name="id" type="text" value="${userId}"/></td>
                        </tr>

                        <tr class="form-group">
                            <td align="right" class="control-label">选择企业：<span class="required">*</span></td>
                            <td class="input-group">
                                <@b.select select_id='edit_nationalLogistics' dictcode='nationalLogistics' required='true' url='/configure/enterpriseinfo/enterprise_selector_list' defValue='${(userProfileModel.nationalLogisticsModel.id)!}' />
                            </td>
                        </tr>
                    </table>
                </form>
                <div style="padding-left: 100px">
                    <a class="btn btn-sm btn-primary" href="javascript:;" onclick="saveNationalLogisticsInfo()">保存</a>
                    <a class="btn btn-sm btn-primary" href="javascript:;" onclick="hideNationalLogisticsInfoEditor()">取消</a>
                </div>
            </div>
        </div>



        <hr>





        <div class="base-info-box">
            <span style="margin-top: 10px;font-weight:bolder;font-size:16px">国际物流公司配置</span>

        <#if userProfileModel?? && userProfileModel.internationalLogisticsModel??>
            <div id="internationalLogistics-info" name="internationalLogistics-info" class="base-info">
                <ul style="list-style: none">
                    <li style="margin-top: 10px">
                        <span>企业名称：</span><span id="view-internationalLogistics-name">${userProfileModel.internationalLogisticsModel.name}</span>
                    </li>
                    <li style="margin-top: 10px">
                        <span>英文名称：</span><span id="view-internationalLogistics-enName">${userProfileModel.internationalLogisticsModel.enName}</span>
                    </li>
                    <li style="margin-top: 10px">
                        <span>证件号：</span><span id="view-internationalLogistics-credential">${userProfileModel.internationalLogisticsModel.credential}</span>
                    </li>
                    <li style="margin-top: 10px">
                        <span>联系方式：</span><span id="view-internationalLogistics-contact">${userProfileModel.internationalLogisticsModel.contact}</span>
                    </li>
                </ul>
            </div>
        </#if>

            <div id="internationalLogistics-contorller" name="internationalLogistics-contorller" style="padding-left: 100px">
                <a class="btn btn-sm btn-primary" href="javascript:;" onclick="showInternationalLogisticsInfoEditor()">配置信息</a>
            </div>

            <div id="internationalLogistics-info-editor" name="internationalLogistics-info-editor" class="" style="display: none; padding-top: 10px">
                <form id="internationalLogistics-info-form" name="internationalLogistics-info-form" class="base-info-form form-horizontal">

                    <table cellpadding="5px" width="400px">
                        <tr style="display: none">
                            <td align="right">USERID：</td>
                            <td class="input-group"><input name="id" type="text" value="${userId}"/></td>
                        </tr>

                        <tr class="form-group">
                            <td align="right" class="control-label">选择企业：<span class="required">*</span></td>
                            <td class="input-group">
                                <@b.select select_id='edit_internationalLogistics' dictcode='internationalLogistics'  required='true' url='/configure/enterpriseinfo/enterprise_selector_list'  defValue='${(userProfileModel.internationalLogisticsModel.id)!}' />
                            </td>
                        </tr>
                    </table>
                </form>
                <div style="padding-left: 100px">
                    <a class="btn btn-sm btn-primary" href="javascript:;" onclick="saveInternationalLogisticsInfo()">保存</a>
                    <a class="btn btn-sm btn-primary" href="javascript:;" onclick="hideInternationalLogisticsInfoEditor()">取消</a>
                </div>
            </div>
        </div>



        <hr>


        <div class="base-info-box">
            <span style="margin-top: 10px;font-weight:bolder;font-size:16px">国内清关公司配置</span>

        <#if userProfileModel?? && userProfileModel.nationalClearanceModel??>
            <div id="nationalClearance-info" name="nationalClearance-info" class="base-info">
                <ul style="list-style: none">
                    <li style="margin-top: 10px">
                        <span>企业名称：</span><span id="view-nationalClearance-name">${userProfileModel.nationalClearanceModel.name}</span>
                    </li>
                    <li style="margin-top: 10px">
                        <span>英文名称：</span><span id="view-nationalClearance-enName">${userProfileModel.nationalClearanceModel.enName}</span>
                    </li>
                    <li style="margin-top: 10px">
                        <span>证件号：</span><span id="view-nationalClearance-credential">${userProfileModel.nationalClearanceModel.credential}</span>
                    </li>
                    <li style="margin-top: 10px">
                        <span>联系方式：</span><span id="view-nationalClearance-contact">${userProfileModel.nationalClearanceModel.contact}</span>
                    </li>
                </ul>
            </div>
        </#if>

            <div id="nationalClearance-contorller" name="nationalClearance-contorller" style="padding-left: 100px">
                <a class="btn btn-sm btn-primary" href="javascript:;" onclick="showNationalClearanceInfoEditor()">配置信息</a>
            </div>

            <div id="nationalClearance-info-editor" name="nationalClearance-info-editor" class="" style="display: none; padding-top: 10px">
                <form id="nationalClearance-info-form" name="nationalClearance-info-form" class="base-info-form form-horizontal">

                    <table cellpadding="5px" width="400px">
                        <tr style="display: none">
                            <td align="right">USERID：</td>
                            <td class="input-group"><input name="id" type="text" value="${userId}"/></td>
                        </tr>

                        <tr class="form-group">
                            <td align="right" class="control-label">选择企业：<span class="required">*</span></td>
                            <td class="input-group">
                                <@b.select select_id='edit_nationalClearance' dictcode='nationalClearance' required='true' url='/configure/enterpriseinfo/enterprise_selector_list'  defValue='${(userProfileModel.nationalClearanceModel.id)!}' />
                            </td>
                        </tr>
                    </table>
                </form>
                <div style="padding-left: 100px">
                    <a class="btn btn-sm btn-primary" href="javascript:;" onclick="saveNationalClearanceInfo()">保存</a>
                    <a class="btn btn-sm btn-primary" href="javascript:;" onclick="hideNationalClearanceInfoEditor()">取消</a>
                </div>
            </div>
        </div>



        <hr>


        <div class="base-info-box">
            <span style="margin-top: 10px;font-weight:bolder;font-size:16px">国际清关公司配置</span>

        <#if userProfileModel?? && userProfileModel.internationalClearanceModel??>
            <div id="internationalClearance-info" name="internationalClearance-info" class="base-info">
                <ul style="list-style: none">
                    <li style="margin-top: 10px">
                        <span>企业名称：</span><span id="view-internationalClearance-name">${userProfileModel.internationalClearanceModel.name}</span>
                    </li>
                    <li style="margin-top: 10px">
                        <span>英文名称：</span><span id="view-internationalClearance-enName">${userProfileModel.internationalClearanceModel.enName}</span>
                    </li>
                    <li style="margin-top: 10px">
                        <span>证件号：</span><span id="view-internationalClearance-credential">${userProfileModel.internationalClearanceModel.credential}</span>
                    </li>
                    <li style="margin-top: 10px">
                        <span>联系方式：</span><span id="view-internationalClearance-contact">${userProfileModel.internationalClearanceModel.contact}</span>
                    </li>
                </ul>
            </div>
        </#if>

            <div id="internationalClearance-contorller" name="internationalClearance-contorller" style="padding-left: 100px">
                <a class="btn btn-sm btn-primary" href="javascript:;" onclick="showInternationalClearanceInfoEditor()">配置信息</a>
            </div>

            <div id="internationalClearance-info-editor" name="internationalClearance-info-editor" class="" style="display: none; padding-top: 10px">
                <form id="internationalClearance-info-form" name="internationalClearance-info-form" class="base-info-form form-horizontal">

                    <table cellpadding="5px" width="400px">
                        <tr style="display: none">
                            <td align="right">USERID：</td>
                            <td class="input-group"><input name="id" type="text" value="${userId}"/></td>
                        </tr>

                        <tr class="form-group">
                            <td align="right" class="control-label">选择企业：<span class="required">*</span></td>
                            <td class="input-group">
                                <@b.select select_id='edit_internationalClearance' dictcode='internationalClearance' required='true' url='/configure/enterpriseinfo/enterprise_selector_list'  defValue='${(userProfileModel.internationalClearanceModel.id)!}' />
                            </td>
                        </tr>
                    </table>
                </form>
                <div style="padding-left: 100px">
                    <a class="btn btn-sm btn-primary" href="javascript:;" onclick="saveInternationalClearanceInfo()">保存</a>
                    <a class="btn btn-sm btn-primary" href="javascript:;" onclick="hideInternationalClearanceInfoEditor()">取消</a>
                </div>
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
    var v_nationalLogistics_info;
    var v_internationalLogistics_info;
    var v_nationalClearance_info;
    var v_internationalClearance_info;
    jQuery(document).ready(function () {
        v_nationalLogistics_info = formValidation('nationalLogistics-info-form');
        v_internationalLogistics_info = formValidation('internationalLogistics-info-form');
        v_nationalClearance_info = formValidation('nationalClearance-info-form');
        v_internationalClearance_info = formValidation('internationalClearance-info-form');
    });


    function refreshThisPage(){
        $("#tab_3_iframe").context.location.reload();
    }
</script>

<script>
    function showNationalLogisticsInfoEditor() {
        $('#nationalLogistics-info').hide();
        $('#nationalLogistics-contorller').hide();
        $("#nationalLogistics-info-editor").show();
        $("html,body").animate({scrollTop: $("nationalLogistics-info-editor").offset()}, 300);
    }
    function hideNationalLogisticsInfoEditor() {
        $('#nationalLogistics-info').show();
        $('#nationalLogistics-contorller').show();
        $("#nationalLogistics-info-editor").hide();
        $("html,body").animate({scrollTop: $("nationalLogistics-info").offset()}, 300);
    }
    function saveNationalLogisticsInfo() {
        if (!v_nationalLogistics_info.form()) {
            return;
        }else{
            $.ajax("${save_nationalLogistics_info_ajax}", {
                data: $("#nationalLogistics-info-form").serialize(),
                type: "POST"
            }).always(function () {
            }).done(function (data) {
                if (data.result) {
                    bootbox.alert("操作成功!", function () {
                        hideNationalLogisticsInfoEditor();
                        randerNationalLogisticsInfo(data.t);
                        refreshThisPage();//刷新此tab（iframe）
                    });
                } else {
                    bootbox.alert(data.msg);
                }
            }).fail(function () {
            });

        }
    }

    function randerNationalLogisticsInfo(datainfo) {
        var model = null;
        if(datainfo && datainfo.nationalLogisticsModel){
            model = datainfo.nationalLogisticsModel;
        }
        if(model){
            $('#view-nationalLogistics-name').html(model.name);
            $('#view-nationalLogistics-enName').html(model.enName);
            $('#view-nationalLogistics-credential').html(model.credential);
            $('#view-nationalLogistics-contact').html(model.contact);
        }
    }

</script>


<script>
    function showInternationalLogisticsInfoEditor() {
        $('#internationalLogistics-info').hide();
        $('#internationalLogistics-contorller').hide();
        $("#internationalLogistics-info-editor").show();
        $("html,body").animate({scrollTop: $("internationalLogistics-info-editor").offset()}, 300);
    }
    function hideInternationalLogisticsInfoEditor() {
        $('#internationalLogistics-info').show();
        $('#internationalLogistics-contorller').show();
        $("#internationalLogistics-info-editor").hide();
        $("html,body").animate({scrollTop: $("internationalLogistics-info").offset()}, 300);
    }
    function saveInternationalLogisticsInfo() {
        if (!v_internationalLogistics_info.form()) {
            return;
        }else{
            $.ajax("${save_internationalLogistics_info_ajax}", {
                data: $("#internationalLogistics-info-form").serialize(),
                type: "POST"
            }).always(function () {
            }).done(function (data) {
                if (data.result) {
                    bootbox.alert("操作成功!", function () {
//                        hideInternationalLogisticsInfoEditor();
//                        randerInternationalLogisticsInfo(data.t);
                        refreshThisPage();//刷新此tab（iframe）
                    });
                } else {
                    bootbox.alert(data.msg);
                }
            }).fail(function () {
            });

        }
    }

    function randerInternationalLogisticsInfo(datainfo) {
        var model = null;
        if(datainfo && datainfo.internationalLogisticsModel){
            model = datainfo.internationalLogisticsModel;
        }
        if(model){
            console.log(model);
            $('#view-internationalLogistics-name').html(model.name);
            $('#view-internationalLogistics-enName').html(model.enName);
            $('#view-internationalLogistics-credential').html(model.credential);
            $('#view-internationalLogistics-contact').html(model.contact);
        }

    }

</script>



<script>
    function showNationalClearanceInfoEditor() {
        $('#nationalClearance-info').hide();
        $('#nationalClearance-contorller').hide();
        $("#nationalClearance-info-editor").show();
        $("html,body").animate({scrollTop: $("nationalClearance-info-editor").offset()}, 300);
    }
    function hideNationalClearanceInfoEditor() {
        $('#nationalClearance-info').show();
        $('#nationalClearance-contorller').show();
        $("#nationalClearance-info-editor").hide();
        $("html,body").animate({scrollTop: $("nationalClearance-info").offset()}, 300);
    }
    function saveNationalClearanceInfo() {
        if (!v_nationalClearance_info.form()) {
            return;
        }else{
            $.ajax("${save_nationalClearance_info_ajax}", {
                data: $("#nationalClearance-info-form").serialize(),
                type: "POST"
            }).always(function () {
            }).done(function (data) {
                if (data.result) {
                    bootbox.alert("操作成功!", function () {
//                        hideNationalClearanceInfoEditor();
//                        randerNationalClearanceInfo(data.t);
                        refreshThisPage();//刷新此tab（iframe）
                    });
                } else {
                    bootbox.alert(data.msg);
                }
            }).fail(function () {
            });

        }
    }

    function randerNationalClearanceInfo(datainfo) {
        var model = null;
        if(datainfo && datainfo.nationalClearanceModel){
            model = datainfo.nationalClearanceModel;
        }
        if(model){
            $('#view-nationalClearance-name').html(model.name);
            $('#view-nationalClearance-enName').html(model.enName);
            $('#view-nationalClearance-credential').html(model.credential);
            $('#view-nationalClearance-contact').html(model.contact);
        }
    }

</script>




<script>
    function showInternationalClearanceInfoEditor() {
        $('#internationalClearance-info').hide();
        $('#internationalClearance-contorller').hide();
        $("#internationalClearance-info-editor").show();
        $("html,body").animate({scrollTop: $("internationalClearance-info-editor").offset()}, 300);
    }
    function hideInternationalClearanceInfoEditor() {
        $('#internationalClearance-info').show();
        $('#internationalClearance-contorller').show();
        $("#internationalClearance-info-editor").hide();
        $("html,body").animate({scrollTop: $("internationalClearance-info").offset()}, 300);
    }
    function saveInternationalClearanceInfo() {
        if (!v_internationalClearance_info.form()) {
            return;
        }else{
            $.ajax("${save_internationalClearance_info_ajax}", {
                data: $("#internationalClearance-info-form").serialize(),
                type: "POST"
            }).always(function () {
            }).done(function (data) {
                if (data.result) {
                    bootbox.alert("操作成功!", function () {
//                        hideInternationalClearanceInfoEditor();
//                        randerInternationalClearanceInfo(data.t);
                        refreshThisPage();//刷新此tab（iframe）
                    });
                } else {
                    bootbox.alert(data.msg);
                }
            }).fail(function () {
            });

        }
    }

    function randerInternationalClearanceInfo(datainfo) {
        var model = null;
        if(datainfo && datainfo.internationalClearanceModel){
            model = datainfo.internationalClearanceModel;
        }
        if(model){
            $('#view-internationalClearance-name').html(model.name);
            $('#view-internationalClearance-enName').html(model.enName);
            $('#view-internationalClearance-credential').html(model.credential);
            $('#view-internationalClearance-contact').html(model.contact);
        }
    }

</script>




</body>
</html>