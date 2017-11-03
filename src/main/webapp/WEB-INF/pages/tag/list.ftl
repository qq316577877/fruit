<!DOCTYPE html>
<html class="not-ie" lang="en">
<head>
    <meta charset="utf-8"/>
    <title>标签管理</title>
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
                <h4>标签管理</h4>
                <hr>
                <form action="${BASEPATH}/item/explosive/event/list" id="searchForm" name="searchForm" class="form-horizontal"
                      method="get">

                    <button type="button" class="btn green btn-sm detail-btn" id="addBtn" data-id="0"><i
                            class="fa fa-plus"></i> 新增标签
                    </button>

                    <br/>
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

                    </colgroup>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>父标签</th>
                        <th>标签名称</th>
                        <th>描述</th>
                        <th>备注</th>
                        <th>创建时间</th>
                        <th>更新时间</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>

                    <#list tagModels as item>
                    <tr>
                        <td>${(item.id!'')}</td>
                        <td>${(item.parent!'')}</td>
                        <td>${(item.name!'')}</td>
                        <td>${(item.description!'')}</td>
                        <td>${(item.memo!'')}</td>
                        <td>${(item.addTime?string("yyyy-MM-dd HH:mm:ss"))!}</td>
                        <td>${(item.updateTime?string("yyyy-MM-dd HH:mm:ss"))!}</td>
                        <td>
                            <a href="" data-id="${item.id}" class="btn btn-sm btn-info detail-btn edit-btn">编辑</a>
                            <a href="" data-id="${item.id}" data-name="${(item.name!'')}" class="btn red btn-sm btn-info detail-btn delete-btn">删除</a>
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
    /*$(".search-btn").on('click', function (e) {
        e.preventDefault();
        var form = $("#searchForm");
        form.attr('action', "${BASEPATH}/item/explosive/event/list")
        form.submit();
    });*/
    $("#addBtn").on('click', function (e) {
        e.preventDefault();
        sendDialogReq();
    });

    $(".edit-btn").on('click', function (e) {
        e.preventDefault();
        var id = $(e.target).attr('data-id');

        sendDialogReq(id);
    });

    function sendDialogReq(id) {
        var url = "${BASEPATH}/tag/detail";
        if (id) {
            url += "?id=" + id;
        }

        var d = dialog({
            title: '标签信息',
            url: url,
            iframe_width: 520,
            iframe_height: 500,
            zIndex: 9996,
            onclose: function () {
                window.location.href = window.location.href.replace(/#/g, '');
            }
        });
        top.openDialog = d;   //保存后如果需要关闭窗口
        d.show();
    }

    $(".delete-btn").on('click', function (e) {
        e.preventDefault();
        var title = $(e.target).attr('data-name');
        bootbox.confirm('是否删除标签 ' + title + ' ？',function(result) {
            if (result) {
                var id = $(e.target).attr('data-id');
                var url = "${BASEPATH}/tag/delete_ajax?id=" + id;

                $.ajax({
                    contentType : "application/json",
                    url     : url,
                    type    : "POST",
                    async   : false,
                    success : function(res, st) {
                        if (res.result)
                            window.location.href = window.location.href.replace(/#/g, '');
                    }
                });
            }
        });
    });


</script>

<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>