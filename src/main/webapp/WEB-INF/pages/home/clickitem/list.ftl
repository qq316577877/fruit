<!DOCTYPE html>
<html class="not-ie" lang="en">
<head>
    <meta charset="utf-8"/>
    <title>新闻资讯管理</title>
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
                <h4>新闻资讯管理</h4>
                <hr>
                <form action="${BASEPATH}/home/clickitem/list" id="searchForm" name="searchForm" class="form-horizontal"
                      method="get">
                    <select id="type" name="type" class="form-control input-medium select2me"
                            validate="{required:false}">
                        <#list positionItems as item>
                            <option value="${item.id}"
                                    <#if item.selected == 1>selected="selected"</#if>>${item.value}</option>
                        </#list>
                    </select>
                    <button type="button" class="btn yellow btn-sm search-btn">
                        <i class="fa fa-search"></i> 查询
                    </button>
                    <button type="button" class="btn green btn-sm detail-btn" id="addBtn" data-id="0"><i
                            class="fa fa-plus"></i> 新增
                    </button>

                    <br/>
                    <br/>
                    <button type="button" class="btn btn-sm red cache-btn" id="addBtn" data-type="event" data-name="首页">
                        清理首页缓存
                    </button>

                </form>

                <!-- 注意:table的table-adv对应隐藏列组件的ID-->
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
                        <col width='100px'>
                    </colgroup>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>添加时间</th>
                        <th>名称</th>
                        <th>类型</th>
                        <th>排序</th>
                        <th>图片</th>
                        <th>链接</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>

                    <#list records as item>
                        <tr>
                            <td>${(item.id!'')}</td>
                            <td>${(item.addTime?string("yyyy-MM-dd HH:mm:ss"))!}</td>
                            <td>${(item.title!'')}</td>
                            <td>${(item.typeDesc!'')}</td>
                            <td>${(item.priority!'')}</td>
                            <td><a href="${item.imgUrl}" target="_blank">查看</a></td>
                            <td><a href="${item.link}" target="_blank">查看</a></td>
                            <td>
                                <a href="" data-id="${item.id}" class="btn btn-sm btn-info detail-btn">编辑信息</a>
                            </td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
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
            form.attr('action', "${BASEPATH}/home/clickitem/list");
            form.submit();
        });

        $('.cache-btn').on('click', function (e) {
            e.preventDefault();

            var url = '${BASEPATH}/cache/sync_home_data_ajax';
            var type = $(e.target).attr('data-type');
            var name = $(e.target).attr('data-name');

            bootbox.confirm('请确认是否清除' + name + '缓存？',
                    function (result) {
                if (result) {
                    $.ajax(url, {
                        data: {
                            type: type
                        },
                        type: "POST",
                        dataType: "json"
                    }).always(function () {
                    }).done(function (data) {
                        if (data.result) {
                            bootbox.alert("处理成功",
                                    function () {
                                    }
                            );
                        } else {
                            bootbox.alert(data.msg);
                        }
                    }).fail(function () {
                    });
                }
            });

        })

        $(".paging").on('click', function (e) {
            e.preventDefault();
            var pageNo = $(e.target).attr('pg-no');
            $('#pageNo').val(pageNo);
            var form = $("#searchForm");
            form.attr('action', "${BASEPATH}/home/clickitem/list")
            form.submit();
        });

        $(".detail-btn").on('click', function (e) {
            e.preventDefault();
            var id = $(e.target).attr('data-id');

            var url = "${BASEPATH}/home/clickitem/detail";
            if (id) {
                url += "?id=" + id;
            }

            var d = dialog({
                title: '新闻资讯',
                url: url,
                iframe_width: 700,
                iframe_height: 480,
                zIndex: 9996,
                quickClose: true,
                onclose: function () {
                    window.location.href = window.location.href.replace(/#/g, '');
                }
            });
            top.openDialog = d;   //保存后如果需要关闭窗口
            d.show();
        });


    </script>

<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>