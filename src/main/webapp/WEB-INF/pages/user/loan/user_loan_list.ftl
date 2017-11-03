<!DOCTYPE html>
<html class="not-ie" lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <title>授信申请列表</title>
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
                <form action="${user_loan_list_url}" id="thisForm" name="thisForm" class="form-inline" method="post">


                    <div>
                        <span>授信申请状态：</span>
                        <#list loan_status_list as item>
                            <#if item.value=="全部">
                                <input type="radio" name="status" value="${item.id}"
                                       <#if status == item.id>checked="checked"</#if> onclick="query(1)"/> ${item.value}
                            </#if>
                            <#if item.value=="申请中">
                                <input type="radio" name="status" value="${item.id}"
                                       <#if status == item.id>checked="checked"</#if> onclick="query(1)"/> ${item.value}
                            </#if>
                            <#if item.value=="已授信">
                                <input type="radio" name="status" value="${item.id}"
                                       <#if status == item.id>checked="checked"</#if> onclick="query(1)"/> ${item.value}
                            </#if>
                            <#if item.value=="已授信(未激活)">
                                <input type="radio" name="status" value="${item.id}"
                                       <#if status == item.id>checked="checked"</#if> onclick="query(1)"/> ${item.value}
                            </#if>
                            <#if item.value=="被驳回">
                                <input type="radio" name="status" value="${item.id}"
                                       <#if status == item.id>checked="checked"</#if> onclick="query(1)"/> ${item.value}
                            </#if>
                        </#list>

                        <input type="hidden" id="pageNo" name="pageNo" value="1"/>
                        <input style="margin-left:20px;" type="text" id="keyword" name="keyword"
                               placeholder="请输入用户手机号码"  value="${keyword!}"></input>
                        <input type="submit" hidden tabindex="-1"/>
                        <button type="button" class="btn yellow btn-sm" onclick="query(1)">
                            <i class="fa fa-search"></i> 查 询
                        </button>
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
                        <col width='120px'>
                        <col width='100px'>
                        <col width='100px'>
                        <col width='100px'>
                        <col width='100px'>
                        <col width='100px'>
                    </colgroup>
                    <thead>
                    <tr>
                        <th class="text-center">序号</th>
                        <th class="text-center">银行预留手机号</th>
                        <th class="text-center">姓名</th>
                        <th class="text-center">身份证号</th>
                        <th class="text-center">申请时间</th>
                        <th class="text-center">授信状态</th>
                        <th class="text-center">操作</th>
                    </tr>
                    </thead>

                    <tbody>
                    <#list user_loan_list as loan>
                        <tr>
                            <td>
                                ${loan.id}
                            </td>

                            <td>
                                ${loan.mobile}
                            </td>

                            <td>
                                ${loan.username}
                            </td>

                            <td>
                                ${loan.identity}
                                <label ></label>
                            </td>

                            <td>
                                ${loan.addTimeString}
                            </td>

                            <td>
                                <#list loan_status_list as item>
                                    <#if loan.status = item.id><span > ${item.value}</span></#if>
                                </#list>
                            </td>

                            <td>
                                <a href="${user_loan_info_detail_url}?id=${loan.id}">
                                    <#if loan.status== 2>批复
                                    <#else>查看
                                    </#if>
                                </a>
                            </td>


                        </tr>
                    </#list>
                    </tbody>
                </table>


            <@b.pageSimple pageDto 'userList'/>


            </div>
        </div>
    </div>
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

</script>
</body>
</html>