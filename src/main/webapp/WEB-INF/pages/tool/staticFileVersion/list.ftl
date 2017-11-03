<!DOCTYPE html>
<html class="not-ie" lang="en">
<head>
    <meta charset="utf-8"/>
    <title>静态资源版本管理</title>
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
                <h4>静态资源版本管理</h4>
                <hr>
                <form action="${BASEPATH}/staticFileVersion/list" id="searchForm" name="searchForm" class="form-horizontal"
                      method="get">

                    <button type="button" class="btn green btn-sm detail-btn" id="addBtn" data-id="0"><i
                            class="fa fa-plus"></i> 新增静态资源版本
                    </button>

                    <button type="button" class="btn red btn-sm detail-btn sync-btn" data-name="portal"><i
                            class="fa fa-reply"></i> 刷新PORTAL版本缓存
                    </button>

                    <#--<button type="button" class="btn red btn-sm detail-btn sync-btn" data-name="h5"><i-->
                            <#--class="fa fa-reply"></i> 刷新移动端版本缓存-->
                    <#--</button>-->

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

                    </colgroup>
                    <thead>
                    <tr>
                        <th>添加时间</th>
                        <th>ID</th>
                        <th>项目</th>
                        <th>版本号</th>
                        <th>描述</th>
                        <th>最后修改者</th>
                        <th>编辑信息</th>
                    </tr>
                    </thead>
                    <tbody>

                    <#list staticFileVersionModels as item>
                    <tr>

                        <td>${(item.addTime?string("yyyy-MM-dd HH:mm:ss"))!}</td>
                        <td>${(item.id!'')}</td>
                        <td>${(item.project!'')}</td>
                        <td>${(item.version!'')}</td>
                        <td>${(item.description!'')}</td>
                        <td>${(item.lastEditor!'')}</td>
                        <td>
                            <a href="" data-id="${item.id}" class="btn btn-sm btn-info detail-btn edit-btn">编辑信息</a>
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
    $("#addBtn").on('click', function (e) {
        e.preventDefault();
        sendDialogReq();
    });

    $(".sync-btn").on('click', function (e) {
        e.preventDefault();

        bootbox.confirm('刷新缓存 ？',function(result) {
            if (result) {
                var type = $(e.target).attr('data-name');
                var url = "${BASEPATH}/staticFileVersion/sync_ajax?type=" + type;

                $.ajax({
                    contentType : "application/json",
                    url     : url,
                    type    : "GET",
                    async   : false,
                    success : function(result, st) {
                        window.location.href = window.location.href.replace(/#/g, '');
                    }
                });
            }
        });
    });

    $(".edit-btn").on('click', function (e) {
        e.preventDefault();
        var id = $(e.target).attr('data-id');

        sendDialogReq(id);
    });

    function sendDialogReq(id) {
        var url = "${BASEPATH}/staticFileVersion/detail";
        if (id) {
            url += "?id=" + id;
        }

        var d = dialog({
            title: '静态资源版本信息',
            url: url,
            iframe_width: 520,
            iframe_height: 520,
            zIndex: 9996,
            quickClose: true,
            onclose: function () {
                window.location.href = window.location.href.replace(/#/g, '');
            }
        });
        top.openDialog = d;   //保存后如果需要关闭窗口
        d.show();
    }


</script>

<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>