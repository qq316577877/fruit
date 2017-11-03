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
<div class="page-content opener">
    <div class="row">
        <div class="col-xs-12">
            <form action="${BASEPATH}/role/dper" id="thisForm" name="thisForm" class="form-horizontal" method="post">
                <input type="hidden" name="pageNo" id="pageNo" value="${pageDto.currentPageNo!}"/>
                <input type="hidden" name="roleId" id="roleId" value="${(params["roleId"])!}"/>

                <div class="form-group">
                    <label class="control-label col-xs-2">用户名</label>

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
            <table id="table_1" table-adv="true" class="table table-striped table-bordered table-hover"
                   style="width: 50%!important">
                <thead>
                <tr>
                    <!-- 注意:checkbox的data-set对应jquery选择器,格式不能错#TABLEID .checkboxes-->
                    <th class="table-checkbox"><input type="checkbox" class="group-checkable"
                                                      data-set="#table_1 .checkboxes"/></th>
                    <th>用户名</th>
                </tr>
                </thead>
                <tbody>
                <#list pageDto.result as e>
                <tr>
                    <td width="20px"><input type="checkbox" class="checkboxes" value="${e.id}"/></td>
                    <td width="90%">${e.userName!}</td>
                </tr>
                </#list>
                </tbody>
            </table>
        <@b.pageSimple pageDto 'mainList'/>
        </div>
        <!--div class="col-xs-12"-->
    </div>

</div>
<!--div class="page-content extended"-->


<!-- 页面JS类库引入 st-->
<!-- 表格 -->
<script type="text/javascript" src="/admins/assets/plugins/data-tables/jquery.dataTables.min.js"></script>
<!-- 表格添加水平滚动，内容不会换行
<script type="text/javascript" src="/admins/assets/plugins/data-tables/DT_bootstrap.js"></script>
-->
<!-- 页面所需组件的js文件 -->
<script src="/admins/js/form.js" type="text/javascript"></script>
<!-- 页面JS类库引入 ed-->


<script>
    jQuery(document).ready(function () {
    });

    function query() {
        var form = $("#thisForm");
        form.attr('action', "${BASEPATH}/role/dper")
        form.submit();
    }

    function showDetail(id) {


        /*  var d = dialog({
              title: '菜单',
              url:url,
              iframe_width: 700,
              iframe_height: 380,
              zIndex:9996,
              quickClose: true,
              onclose: function () {
                  window.location.href = window.location.href.replace(/#/g,'');
              }
          });*/

        var d = dialog({
//            content: "<input type='text' class='form-control' id='M_dperId' class='J_user_name' name='M_dperId' value=''/>" +
            content:
            "<select class='form-control' id='M_dperId' class='J_user_name' name='M_dperId' >" +
            "<#list user_info_list as user ><option>${user.userName}</option></#list>" +
            "</select>",
            title: '添加人员',
            width: 300,
            height: 50,
            zIndex: 9996,
            quickClose: true,
            onshow: function () {
                findElementById('M_dperId').focus();
            },
            button: [
                {
                    value: '确 定',
                    className: 'btn blue',
                    callback: function () {
                        var dperId = findElementById('M_dperId').value;
                        if (dperId == '') {
                            return;
                        }
                        saveDper(dperId);
                        this.close();
                        return true; //无返回值则销毁窗口
                    }
                }],
            cancelValue: '取 消',
            cancel: function () {
                this.close();
                return true; //无返回值则销毁窗口
            }
        });

        d.show();
    }

    var saveDper = function (name) {
        $.ajax("${BASEPATH}/role/saveDper", {
            data: {
                name: name,
                roleId: '${(params["roleId"])!}'
            },
            type: "POST",
            dataType: "json"
        }).always(function () {
        }).done(function (data) {
            if (data.result) {
                bootbox.alert("保存成功!", function () {
                            window.location.href = window.location.href.replace(/#/g, '');
                        }
                );
            } else {
                bootbox.alert(data.msg);
            }
        }).fail(function () {
        });
    }

    function deleteData() {
        deleteFunc("${BASEPATH}/role/deleteDper");
    }
</script>
</body>
</html>