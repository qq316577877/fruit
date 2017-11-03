<!DOCTYPE html>
<html class="not-ie" lang="en">

<head>
    <meta charset="utf-8"/>
    <title>角色</title>
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
            <form action="${BASEPATH}/role/list" id="thisForm" name="thisForm" class="form-horizontal" method="post">
                <input type="hidden" name="pageNo" id="pageNo" value="${pageDto.currentPageNo!}"/>

                <div class="form-group">
                    <label class="control-label col-xs-2">名称</label>

                    <div class="col-xs-4">
                        <div class="input-group">
                            <input type='text' class="form-control" id='name' name='name' value='${(params["name"])!}'/>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <button type="button" class="btn yellow btn-sm" onclick="query()"><i class="fa fa-search"></i> 查
                            询
                        </button>
                        <button type="button" class="btn green btn-sm" id="addBtn" onclick="showDetail()"><i
                                class="fa fa-plus"></i> 新 增
                        </button>
                        <button type="button" class="btn red btn-sm" onclick="deleteData()"><i
                                class="fa fa-trash-o"></i> 删 除
                        </button>
                    </div>
                </div>
                <!--div class="form-group"-->
            </form>
            <!-- 注意:table的table-adv对应隐藏列组件的ID-->
            <table id="table_1" table-adv="true" class="table table-striped table-bordered table-hover table-full-width"
                   style="width: 70%!important">
                <thead>
                <tr>
                    <!-- 注意:checkbox的data-set对应jquery选择器,格式不能错#TABLEID .checkboxes-->
                    <th class="table-checkbox"><input type="checkbox" class="group-checkable"
                                                      data-set="#table_1 .checkboxes"/></th>
                    <th>名称</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <#list pageDto.result as e>
                <tr>
                    <td width="20px"><input type="checkbox" class="checkboxes" value="${e.id}"/></td>
                    <td width="50%"><a href="#" onclick="showDetail(${e.id!})">${e.name!}</a></td>
                    <td width="50%">
                        <a class="btn btn-sm btn-info" id="detailBtn" href="#" onclick="showDetail(${e.id!})">查看</a>
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


<!-- 表格 -->
<script type="text/javascript" src="/admins/assets/plugins/data-tables/jquery.dataTables.min.js"></script>
<!-- 表格添加水平滚动，内容不会换行
<script type="text/javascript" src="/admins/assets/plugins/data-tables/DT_bootstrap.js"></script>
-->

<script src="/admins/js/form.js" type="text/javascript"></script>


<script>
    jQuery(document).ready(function () {
    });

    function query() {
        var form = $("#thisForm");
        form.attr('action', "${BASEPATH}/role/list")
        form.submit();
    }

    function showDetail(id) {
        var url = "${BASEPATH}/role/detail";
        if (id) {
            url += "?id=" + id;
        }
        window.location.href = url;
    }

    function deleteData() {
        deleteFunc("${BASEPATH}/role/delete");
    }
</script>
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>