<!DOCTYPE html>
<html class="not-ie" lang="en">

<head>
    <meta charset="utf-8"/>
    <title>菜单</title>
<#include "/WEB-INF/pages/base/css.ftl">
    <link rel="stylesheet" href="/admins/assets/plugins/data-tables/DT_bootstrap.css"/>
<#include "/WEB-INF/pages/base/js.ftl">
<#import "/WEB-INF/pages/base/base.ftl"  as b>
</head>
<body>
<br/>

<div class="page-content extended ">
    <div class="row">
        <div class="col-xs-12">
            <form action="${BASEPATH}/menu/list" id="thisForm" name="thisForm" class="form-inline" method="post">
                <input type="hidden" name="pageNo" id="pageNo" value="${pageDto.currentPageNo!}"/>

                <div class="form-group">
                    <input type='text' class="form-control input-sm" id='name' name='name' placeholder="名称"
                           value='${(params["name"])!}'/>
                </div>
                <div class="form-group">
                    <button type="button" class="btn yellow btn-sm" onclick="query()"><i class="fa fa-search"></i> 查 询
                    </button>
                    <button type="button" class="btn green btn-sm" id="addBtn" onclick="showDetail()"><i
                            class="fa fa-plus"></i> 新 增
                    </button>
                    <button type="button" class="btn red btn-sm" onclick="deleteData()"><i class="fa fa-trash-o"></i> 删
                        除
                    </button>
                </div>
            </form>
            <!-- 注意:table的table-adv对应隐藏列组件的ID-->
            <table id="table_1" table-adv="true"
                   class="table table-striped table-bordered table-hover table-full-width">
                <thead>
                <tr>
                    <!-- 注意:checkbox的data-set对应jquery选择器,格式不能错#TABLEID .checkboxes-->
                    <th class="table-checkbox"><input type="checkbox" class="group-checkable"
                                                      data-set="#table_1 .checkboxes"/></th>
                    <th>名称</th>
                    <th>状态</th>
                    <th>名称空间</th>
                    <th>链接</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <#list pageDto.result as e>
                <tr>
                    <td width="20px"><input type="checkbox" class="checkboxes" value="${e.id}"/></td>
                    <td width="30%"><a href="#" onclick="showDetail(${e.id!})">${e.name!}</a></td>
                    <td width="20%"><#if e.status == 0>启用<#elseif e.status == 1>禁用</#if></td>
                    <td width="15%">${e.namespace!}</td>
                    <td width="50%">${e.url!}</td>
                    <td width="60px">
                        <a class="btn btn-sm btn-info" id="detailBtn" href="#" onclick="showDetail(${e.id!})">修改</a>
                    </td>
                </tr>
                </#list>
                </tbody>
            </table>
        <@b.pageSimple pageDto 'mainList'/>
        </div>
        <!--div class="col-xs-12"-->
    </div>
</div>
<!--div class="page-content extended "-->


<!-- 页面JS类库引入 st-->
<!-- 表格 -->
<script type="text/javascript" src="/admins/assets/plugins/data-tables/jquery.dataTables.min.js"></script>
<!-- 表格添加水平滚动，内容不会换行
<script type="text/javascript" src="/admins/assets/plugins/data-tables/DT_bootstrap.js"></script>
-->
<!-- 日期-->
<script src="/admins/assets/plugins/bootstrap-datetimepicker-master/js/moment-with-locales.js" type="text/javascript"></script>
<script src="/admins/assets/plugins/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"
        type="text/javascript"></script>

<!-- 页面所需组件的js文件 -->
<script src="/admins/js/form.js" type="text/javascript"></script>
<!-- 页面JS类库引入 ed-->


<script>
    jQuery(document).ready(function () {
    });

    function query() {
        var form = $("#thisForm");
        form.attr('action', "${BASEPATH}/menu/list")
        form.submit();
    }

    function showDetail(id) {
        var url = "${BASEPATH}/menu/detail";
        if (id) {
            url += "?id=" + id;
        }

        var d = dialog({
            title: '菜单',
            url: url,
            iframe_width: 700,
            iframe_height: 380,
            zIndex: 9996,
            quickClose: true,
            onclose: function () {
                window.location.href = window.location.href.replace(/#/g, '');
            }
        });
        top.openDialog = d;   //保存后如果需要关闭窗口
        d.show();
    }

    function deleteData() {
        deleteFunc("${BASEPATH}/menu/delete");
    }
</script>
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>