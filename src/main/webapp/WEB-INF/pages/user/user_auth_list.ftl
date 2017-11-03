<!DOCTYPE html>
<html class="not-ie" lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <title>会员审核-列表</title>
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
                <form action="${user_auth_list_url}" id="thisForm" name="thisForm" class="form-inline" method="post">
                    <input type="hidden" id="pageNo" name="pageNo" value="1"/>
                    <input type="text" class="form-control input-sm" id="keyword" name="keyword"
                           placeholder="搜索关键词，如公司名、法人姓名、手机号码等" style="width:80%" value="${keyword!}"></input>
                    <input type="submit" hidden tabindex="-1"/>
                    <button type="button" class="btn yellow btn-sm" onclick="query(1)">
                        <i class="fa fa-search"></i> 查 询
                    </button>


                    <div style="padding-top: 10px">
                        <span>用户类型：</span>
                    <#list user_type_list as item>
                        <#if 0 != item.id>
                            <input type="radio" name="type" value="${item.id}"
                                    <#if type == item.id>checked="checked"</#if> onclick="query(1)"/> ${item.value}
                            (${type_count_map[item.value]})
                        </#if>
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
                        <col width='50px'>
                        <col width='100px'>
                        <col width='100px'>
                        <col width='100px'>
                        <col width='100px'>
                        <col width='80px'>
                        <col width='50px'>
                        <col width='50px'>
                    </colgroup>
                    <thead>
                    <tr>
                        <th class="text-center">序号</th>
                        <th class="text-center">手机号</th>
                        <th class="text-center">注册时间</th>
                        <th class="text-center">提交时间</th>
                        <th class="text-center">企业名称/姓名</th>
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
                                <#if user.enterprise?? && user.enterprise.addTime??>
                                    ${user.enterprise.addTime?string("yyyy-MM-dd HH:mm:ss")}
                                </#if>

                            </td>

                            <td>
                                <#if user.enterprise?? && user.enterprise.enterpriseName??>
                                    企业名称：${user.enterprise.enterpriseName}<br/>
                                </#if>
                                <#if user.enterprise?? && user.enterprise.name??>
                                    姓名：${user.enterprise.name}
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

                                <#if user.enterpriseVerifyStatus==3>
                                    <a href="${user_info_auth_verify_url}?id=${user.userId}">审核</a>
                                    <#else>
                                        <a href="${user_info_auth_detail_url}?id=${user.userId}">查看</a>
                                </#if>
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