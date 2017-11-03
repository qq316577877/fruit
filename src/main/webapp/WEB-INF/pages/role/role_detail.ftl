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
<div class="page-content extended">
    <div class="tabbable-custom">
        <ul class="nav nav-tabs" style="width:80%">
            <li class="active"><a href="#tab_1" data-toggle="tab">角色信息</a></li>
            <li class="" data-type='display'><a href="#tab_2" data-toggle="tab">关联菜单</a></li>
            <li class="" data-type='display'><a href="#tab_3" data-toggle="tab">管理人员</a></li>

            <div class="pull-right" style="height:25px">
                <a href="#" class="btn btn-info btn-sm" id="backbtn"><i class="fa fa-reply"></i> 返 回</a>
            </div>
        </ul>
        <div class="tab-content">
            <div class="tab-pane active" id="tab_1">
                <iframe id="tab_1_iframe" name="tab_1_iframe" src="" height="450px" frameborder="0" scrolling="auto"
                        marginheight="0" marginwidth="0" width="100%"></iframe>
            </div>
            <div class="tab-pane" id="tab_2">
                <iframe id="tab_2_iframe" name="tab_2_iframe" src="" height="450px" frameborder="0" scrolling="auto"
                        marginheight="0" marginwidth="0" width="100%"></iframe>
            </div>
            <div class="tab-pane" id="tab_3">
                <iframe id="tab_3_iframe" name="tab_3_iframe" src="" height="450px" frameborder="0" scrolling="auto"
                        marginheight="0" marginwidth="0" width="100%"></iframe>
            </div>
        </div>
        <!--div class="tab-content"-->
    </div>
</div>
<!--div class="page-content extended"-->


<!-- 页面JS类库引入 st-->
<!-- 页面JS类库引入 ed-->
<!-- 私有JS st -->
<script src="/admins/js/form.js" type="text/javascript"></script>
<!-- 私有JS ed -->
<script>
    jQuery(document).ready(function () {
    <#if !id??>
        disableTab();
    </#if>

        $('#backbtn').click(function () {
            window.location.href = "${BASEPATH}/role/list";
        });

        refresh();
    });

    function refresh() {
        $('#tab_1_iframe').attr('src', "${BASEPATH}/role/info?id=${id!}");
    <#if id??>
        $('#tab_2_iframe').attr('src', "${BASEPATH}/role/menu?roleId=${id!}");
        $('#tab_3_iframe').attr('src', "${BASEPATH}/role/dper?roleId=${id!}");
    </#if>
    }

    var disableTab = function () {
        $("li[data-type]").attr({"class": "disabled"});
        $("li[data-type] a").removeAttr('data-toggle');

    }
    var showTab = function () {
        $("li[data-type]").attr({"class": ""});
        $("li[data-type] a").attr({"data-toggle": "tab"});
    }

</script>
</body>
</html>