<!DOCTYPE html>
<html class="not-ie" lang="en">

<head>
    <meta charset="utf-8"/>
    <title>清关物流公司配置</title>
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
            <form action="${enterprise_list_url}" id="thisForm" name="thisForm" class="form-inline" method="post">
                <input type="hidden" name="pageNo" id="pageNo" value="${pageDto.currentPageNo!}"/>

                <div class="form-group">
                    <input type='text' class="form-control input-sm" style="width:300px;" id='keyword' name='keyword' placeholder="公司名称、证件号"
                           value='${(params["keyword"])!}'/>
                </div>
                <div class="form-group">
                    <button type="button" class="btn yellow btn-sm" onclick="query()"><i class="fa fa-search"></i> 查 询
                    </button>
                    <button type="button" class="btn green btn-sm" id="addBtn" onclick="showAdd()"><i
                            class="fa fa-plus"></i> 新 增
                    </button>

                <#-- 删除功能注释：因为删除这种关联性用户相关操作，暂时不做 -->
                <#--<button type="button" class="btn red btn-sm" onclick="deleteData()"><i class="fa fa-trash-o"></i> 删-->
                        <#--除-->
                    <#--</button>-->
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
                    <th>企业名称</th>
                    <th>证件号</th>
                    <th>公司类型</th>
                    <th>创建时间</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <#list pageDto.result as enterprise>
                <tr>
                    <td width="20px"><input type="checkbox" class="checkboxes" value="${enterprise.id}"/></td>
                    <td width="30%">${enterprise.name!}</td>
                    <td width="20%">${enterprise.credential!}</td>
                    <td width="15%">${enterprise.locationTypeDesc!} - ${enterprise.typeDesc!}</td>
                    <td width="15%">${enterprise.addTime?string("yyyy-MM-dd HH:mm:ss")}</td>
                    <td width="150px">

                        <a class="btn btn-sm btn-info" id="editBtn" href="#" onclick="showUpdate(${enterprise.id!})">修改</a>
                        <a class="btn btn-sm btn-info" id="editBtn" href="#" onclick="showDetail(${enterprise.id!})">查看详情</a>
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
        form.attr('action', form.attr('action'));
        form.submit();
    }

    /**
     * 查看详情
     * @param id
     */
    function showDetail(id) {
        var url = "${enterprise_info_detail_url}";
        if (id) {
            url += "?id=" + id;
        }

        var d = dialog({
            title: '清关物流公司详情',
            url: url,
            iframe_width: 750,
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



    /**
     * 新增
     */
    function showAdd() {
        var url = "${enterprise_info_add_url}";

        var d = dialog({
            title: '新增清关物流公司',
            url: url,
            iframe_width: 750,
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

    /**
     * 修改
     * @param id
     */
    function showUpdate(id) {
        var url = "${enterprise_info_update_url}";
        if (id) {
            url += "?id=" + id;
        }

        var d = dialog({
            title: '修改清关物流公司',
            url: url,
            iframe_width: 750,
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
        deleteFunc("${enterprise_info_delete_url}");
    }
</script>
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>