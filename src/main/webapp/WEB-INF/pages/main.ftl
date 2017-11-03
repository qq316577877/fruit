<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <title>首页</title>
<#include "/WEB-INF/pages/base/css.ftl">
<#include "/WEB-INF/pages/base/js.ftl">
</head>
<body>
<br/>

<div class="page-content extended ">
    <div class="row">
        <div class="col-xs-8">
            <div class="note note-info">
                <h4 class="block">欢迎使用</h4>
                <p>
                    欢迎使用九创金服管理后台！
                </p>
            </div>

            <div class="note note-warning">
                <h4 class="block">通知消息</h4>
                <#if admin_notices?size gt 0>
                    <p>
                        您有 ${admin_notices?size} 条消息未处理。
                    </p>
                    <div align="right">
                        <a href="" onclick="checkAll()">设置全部已读</a>
                    </div>
                    <table id="table_1" table-adv="false" bgcolor="#FCF3E1" border="1px solid" width="100%" style="border-color: #ddd;"
                           <#--class="table table-bordered table-hover table-full-width "-->>
                        <colgroup>
                            <col width='15%'>
                            <col width='15%'>
                            <col width='50%'>
                            <col width='10%'>
                            <col width='10%'>
                        </colgroup>
                        <thead>
                        <tr>
                            <th height="30px">时间</th>
                            <th height="30px">类型</th>
                            <th height="30px">内容</th>
                            <th height="30px">链接</th>
                            <th height="30px">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                            <#list admin_notices as item>
                                <tr>
                                    <td>${(item.addTime?string("yyyy-MM-dd HH:mm:ss"))}</td>
                                    <td>${(item.typeDesc!'')}</td>
                                    <td>${(item.sysCenterMsg!'')}</td>
                                    <td>
                                        <#if item.sysCenterUrl!>
                                            <a href="${(item.sysCenterMsg!'')}">查看详情</a>
                                        </#if>
                                    </td>
                                    <td>
                                        <a class="btn btn-sm btn-primary"
                                           href="#" onclick="check(${item.id!0})">已读</a>
                                    </td>
                                </tr>
                            </#list>
                        </tbody>
                    </table>
                <#else>
                    <p>
                        暂无。
                    </p>
                </#if>
            </div>
        </div>
    </div>
</div>

<!-- 页面JS类库引入 st-->
<!-- 表格 -->
<script type="text/javascript" src="/admins/assets/plugins/data-tables/jquery.dataTables.min.js"></script>
<!-- 表格添加水平滚动，内容不会换行  -->
<script type="text/javascript" src="/admins/assets/plugins/data-tables/DT_bootstrap.js"></script>

<!-- 日期-->
<script src="/admins/assets/plugins/bootstrap-datetimepicker-master/js/moment-with-locales.js" type="text/javascript"></script>
<script src="/admins/assets/plugins/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"
        type="text/javascript"></script>

<!-- 页面所需组件的js文件 -->
<script src="/admins/js/form.js" type="text/javascript"></script>
<!-- 页面JS类库引入 ed-->

<script>

    function check(id) {
        $.ajax('${BASEPATH}/notice/center/check_ajax?id=' + id, {
            data: {

            },
            type: "POST",
            dataType: "json"
        }).always(function () {
        }).done(function (data) {
            if (data.result) {
                window.location.href = window.location.href.replace(/#/g, '');
            } else {
                bootbox.alert(data.msg);
            }
        }).fail(function () {
        });
    }

    function checkAll() {
        $.ajax("${BASEPATH}/notice/center/check_all_ajax", {
            data: {

            },
            type: "POST",
            dataType: "json"
        }).always(function () {
        }).done(function (data) {
            if (data.result) {
                window.location.href = window.location.href.replace(/#/g, '');
            } else {
                bootbox.alert(data.msg);
            }
        }).fail(function () {
        });
    }

</script>

</body>
</html>
