<!DOCTYPE html>
<html class="not-ie" lang="en">
<head>
    <meta charset="utf-8"/>
    <title>管理员管理</title>
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
                <h4>管理员管理</h4>
                <hr>
                <form action="##" id="userListForm" name="userListForm" class="form-inline" method="post">
                    <div class="form-group">
                    <#--<button type="button" class="btn yellow btn-sm" onclick="query()">-->
                    <#--<i class="fa fa-search"></i> 查 询-->
                    <#--</button>-->
                        <button type="button" class="btn green btn-sm" id="addBtn" onclick="showDetail()">
                            <i class="fa fa-plus"></i> 新 增
                        </button>


                    </div>
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
                        <th>创建时间</th>
                        <th>用户名</th>
                        <th>部门</th>
                        <th>角色</th>
                        <th>邮箱</th>
                        <th>状态</th>
                        <th>更新时间</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>

                    <#list records as item>
                    <tr>
                        <td>${(item.sysId!'')}</td>
                        <td>${(item.addTime?string("yyyy-MM-dd HH:mm:ss"))!}</td>
                        <td>${(item.userName!'')}</td>
                        <td>${(item.departName!'')}</td>
                        <#--<td><#if item.type == 1>系统管理员<#elseif item.type ==2>普通管理员</#if></td>-->
                        <td>
                            <#list role_list as role>
                                <#if item.type== role.id> ${role.name}</#if>
                            </#list>
                        </td>
                        <td>${(item.mail!'')}</td>
                        <td>
                            <#if item.status == 1>正常<#elseif item.status ==2>禁用</#if>
                        </td>
                        <td>${(item.updateTime?string("yyyy-MM-dd HH:mm:ss"))!}</td>
                        <td>
                            <#if item.sysId!=1>
                            <button onclick="deleteUser(${item.sysId})" type="button" class="btn red btn-sm">删除</button>
                            </#if>
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
<#--<script src="/admins/js/form.js" type="text/javascript"></script>-->
<!-- 页面JS类库引入 ed-->
<script src="/admins/js/form.js" type="text/javascript"></script>

    <script>
        function showDetail(id) {
            var url = "${admin_user_detail}";
            var v_title = '新增';
            if (id) {
                url += "?id=" + id;
                v_title = '修改';
            }

            var d = dialog({
                title: v_title,
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
            deleteFunc("${BASEPATH}/admin/user/delete");
        }

        function deleteUser(id) {
            var url = "${BASEPATH}/admin/user/delete";
            bootbox.confirm("是否删除数据?", function (result) {
                if (result) {

                    $.ajax(url, {
                        data: {
                            'id': id
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
            });
        }

    </script>

<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>