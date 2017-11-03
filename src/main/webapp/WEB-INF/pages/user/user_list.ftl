<!DOCTYPE html>
<html class="not-ie" lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <title>用户列表</title>
<#include "/WEB-INF/pages/base/css.ftl">
    <link rel="stylesheet" href="/admins/assets/plugins/data-tables/DT_bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="/admins/assets/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css"/>
<#include "/WEB-INF/pages/base/js.ftl">
<#import "/WEB-INF/pages/base/base.ftl"  as b>
</head>
<body>

<div class="page-content extended ">
    <div class="row">
        <div class="col-xs-12">
            <div style="padding-top: 10px">
                <form action="${user_list_url}" id="thisForm" name="thisForm" class="form-inline" method="post">
                    <input type="hidden" id="pageNo" name="pageNo" value="1"/>
                    <input type="text" class="form-control input-sm" id="keyword" name="keyword"
                           placeholder="搜索关键词，如公司名、法人姓名、手机号码等" style="width:80%" value="${keyword!}"></input>
                    <input type="submit" hidden tabindex="-1"/>
                    <button type="button" class="btn yellow btn-sm" onclick="query(1)">
                        <i class="fa fa-search"></i> 查 询
                    </button>
                    <#--<a  style="margin-left: 10px;" data-toggle="modal" data-target="#myModal">-->
                        <#--用户数据导出-->
                    <#--</a>-->

                    <div style="padding-top: 10px">
                        <span>用户类型：</span>
                    <#list user_type_list as item>
                        <input type="radio" name="type" value="${item.id}"
                               <#if type == item.id>checked="checked"</#if> onclick="query(1)"/> ${item.value}
                        (${type_count_map[item.value]})
                    </#list>
                    </div>
                    <div style="padding-top: 10px">
                        <span>账户状态：</span>
                    <#list user_status_list as item>
                        <input type="radio" name="status" value="${item.id}"
                               <#if status == item.id>checked="checked"</#if> onclick="query(1)"/> ${item.value}
                        (${status_count_map[item.value]})
                    </#list>
                    </div>

                    <div style="padding-top: 10px">
                        <span>认证状态：</span>
                        <#list user_enterprise_status_list as item>
                            <input type="radio" name="enterpriseStatus" value="${item.id}"
                                   <#if enterpriseStatus == item.id>checked="checked"</#if> onclick="query(1)"/> ${item.value}
                            (${enterprise_status_count_map[item.value]})
                        </#list>
                    </div>

                    <div style="padding-top: 10px">
                        <span>排序方式：</span>
                        <select id="sortby" name="sortby" onchange="query(1)">
                        <#list sort_by_list as item>
                            <option value="${item.id}" <#if sortby == item.id>selected</#if>>${item.value}</option>
                        </#list>
                        </select>
                    </div>

                </form>
            </div>

            <div style="padding-top: 10px">
                <table id="table_1" table-adv="true"
                       class="table table-striped table-bordered table-hover table-full-width text-center">
                    <colgroup>
                        <col width='70px'>
                        <col width='100px'>
                        <col width='120px'>
                        <col width='100px'>
                        <col width='100px'>
                        <col width='100px'>
                        <col width='100px'>
                    </colgroup>
                    <thead>
                    <tr>
                        <th class="text-center">序号</th>
                        <th class="text-center">手机号</th>
                        <th class="text-center">注册时间</th>
                        <th class="text-center">名称</th>
                        <th class="text-center">类型</th>
                        <th class="text-center">状态</th>
                        <th class="text-center">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list user_list as user>
                        <tr>
                            <td>
                                ${user.userId}
                            </td>

                            <td>
                                ${user.mobile}
                            </td>

                            <td>
                                ${user.addTime?string("yyyy-MM-dd HH:mm:ss")}
                            </td>

                            <td>
                                <#if user.enterprise?? && user.enterprise.name??>
                                    ${user.enterprise.name}
                                </#if>
                            </td>

                            <td>
                                <#if user.enterpriseVerifyStatusDesc?? && user.enterpriseVerifyStatusDesc?length gt 0>
                                        ${user.enterpriseVerifyStatusDesc}
                                </#if>
                            </td>

                            <td>
                                ${user.statusDes}
                            </td>

                            <td>
                                <a href="${user_info_detail_url}?id=${user.userId}">会员信息</a>
                            </td>


                        </tr>
                    </#list>
                    </tbody>
                </table>


            <@b.pageSimple pageDto 'userList'/>
            <#--<#if (pageNo > 0)>-->
                <#--<div class="row" align="right">-->
                    <#--<div class="col-xs-12" style="text-align: center">-->
                        <#--<div class="dataTables_paginate paging_bootstrap pull-left" align="center">-->
                            <#--<ul class="pagination" style="visibility: visible; align-content: center">-->
                                <#--<#if (pageNo > 1)>-->
                                    <#--<li>-->
                                        <#--<a href="#" onclick="query(${pageNo?int-1})" title="上一页">上一页</a>-->
                                    <#--</li>-->
                                <#--</#if>-->
                                <#--<li class="active">-->
                                    <#--<a href="#">${pageNo}</a>-->
                                <#--</li>-->
                                <#--<#if user_list?? && user_list?size  &gt; 0>-->
                                    <#--<li>-->
                                        <#--<a href="#" onclick="query(${pageNo?int+1})" title="下一页">下一页</a>-->
                                    <#--</li>-->
                                <#--</#if>-->
                                <#--<li class="active">-->
                                    <#--<a href="javascript:void(0)">当前页${user_list?size}条</a>-->
                                <#--</li>-->
                            <#--</ul>-->
                        <#--</div>-->
                    <#--</div>-->
                <#--</div>-->
            <#--</#if>-->



            </div>
        </div>
    </div>
</div>
</div>

<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" style="width: 550px;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">请选择导出条件</h4>

            </div>
            <div class="modal-body">
                <div class="col-md-12">
                    <div class="row">

                        <div style="padding-top: 10px">
                            <span>用户类型：</span>
                        <#list user_type_list as item>
                            <#if item.id gt -1>
                                <input type="checkbox" name="type" id="type" value="${item.id}"
                                       <#if item.id == 1 ||item.id ==2>checked="checked"</#if>/> ${item.value}
                            </#if>
                        </#list>
                        </div>


                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary export-btn" data-id="1">确认</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                </div>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal-dialog -->
    </div>
</div>

<script src="/admins/assets/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
<script src="/admins/js/form.js" type="text/javascript"></script>

<script>

    function query(pageNo) {
        $('#pageNo').val(pageNo);
        var form = $("#thisForm");
        form.attr('action', form.attr('action'));
        form.submit();
    }

    $(".export-btn").on('click', function (e) {
                e.preventDefault();
        var checkstr = "";
        $("input:checkbox[name='type']:checked").each(function (i) {
            if (checkstr != "") {
                checkstr += ",";
            }
            checkstr += $(this).val();
        });
        var url = "${BASEPATH}/user/export?type="+checkstr;

                var d = dialog({
                    title: '导出用户数据',
                    url: url,
                    iframe_width: 300,
                    iframe_height: 150,
                    zIndex: 9996,
                    quickClose: false,
                });
                top.openDialog = d;   //保存后如果需要关闭窗口
                d.show();

    });
</script>
</body>
</html>