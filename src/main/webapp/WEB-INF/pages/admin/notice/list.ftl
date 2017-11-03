<!DOCTYPE html>
<html class="not-ie" lang="en">
<head>
    <meta charset="utf-8"/>
    <title>通知消息</title>
    <link rel="stylesheet" href="/admins/assets/plugins/data-tables/DT_bootstrap.css"/>
    <link rel="stylesheet" href="/admins/assets/css/base.css"/>
    <link href="/admins/assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<#include "/WEB-INF/pages/base/css.ftl">
<#include "/WEB-INF/pages/base/js.ftl">
<#import "/WEB-INF/pages/base/base.ftl"  as base>
</head>
<body>
<section class="section" style="">
    <div class="main clearfix">
        <div id="content">
            <div>
                <h4>通知消息</h4>
                <hr>
                <#--<form action="${BASEPATH}/notice/center/list" id="searchForm" name="searchForm" class="form-horizontal"-->
                      <#--method="get">-->
                    <#--<input type="text" id="pageNo" name="pageNo" hidden value="1"></input>-->
                    <#--<select id="select-type" name="status" class="form-control input-medium select2me"-->
                            <#--validate="{required:false}">-->
                        <#--<#list statusItems as item>-->
                            <#--<option value="${item.id}"-->
                                <#--<#if item.selected == 1>selected="selected"</#if>>${item.value}</option>-->
                        <#--</#list>-->
                    <#--</select>-->
                    <#--<input type='text' class=" input-medium"-->
                           <#--style="padding: 7px 9px 7px 10px;border: 1px solid #ccc;" id='buyer' name='buyer' placeholder="请输入买家公司"-->
                           <#--value='${buyer}'/>-->
                    <#--<button type="button" class="btn yellow btn-sm search-btn">-->
                        <#--<i class="fa fa-search"></i> 查询-->
                    <#--</button>-->
                <#--</form>-->

                <!-- 注意:table的table-adv对应隐藏列组件的ID-->
                <div align="right">
                    <a href="" onclick="checkAll()">设置全部已读</a>
                </div>
                <table id="table_1" table-adv="true"
                       class="table table-striped table-bordered table-hover table-full-width">
                    <colgroup>
                        <col width='100px'>
                        <col width='100px'>
                        <col width='100px'>
                        <col width='100px'>
                        <col width='100px'>
                        <col width='100px'>
                        <col width='100px'>
                        <col width='100px'>
                    </colgroup>
                    <thead>
                    <tr>
                        <th>时间</th>
                        <th>类型</th>
                        <th>内容</th>
                        <th>链接</th>
                        <th>操作</th>
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

                <div class="row" style="margin-left: 5px">
                    <div class="col-xs-12">
                        <div class="dataTables_paginate paging_bootstrap pull-left" align="center">
                            <ul class="pagination" style="visibility: visible; align-content: center">
                            <#--<#if (pageNo > 1)>-->
                                <#--<li>-->
                                    <#--<a href="" class="paging" pg-no="${pageNo?int-1}" title="上一页">上一页</a>-->
                                <#--</li>-->
                            <#--</#if>-->
                                <#--<li class="active"><a href="#">${pageNo}</a></li>-->
                            <#--<#if pageResult.totalPage gt pageNo>-->
                                <#--<li>-->
                                    <#--<a href="" class="paging" pg-no="${pageNo?int+1}" title="下一页">下一页</a>-->
                                <#--</li>-->
                            <#--</#if>-->
                                <li class="active">
                                    <a href="javascript:void(0)">当前页${admin_notices?size}条<#--，共计${pageResult.totalPage}页，${pageResult.totalCount}条--></a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!--div class="page-content extended "-->

</section>
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
        $(".search-btn").on('click', function (e) {
            e.preventDefault();
            var form = $("#searchForm");
            form.attr('action', "${BASEPATH}/notice/center/list")
            form.submit();
        });
        $(".paging").on('click', function (e) {
            e.preventDefault();
            var pageNo = $(e.target).attr('pg-no');
            $('#pageNo').val(pageNo);
            var form = $("#searchForm");
            form.attr('action', "${BASEPATH}/notice/center/list")
            form.submit();
        });

        function showDetail(id) {

        }

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

<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>