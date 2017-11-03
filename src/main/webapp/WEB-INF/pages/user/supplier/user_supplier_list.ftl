<!DOCTYPE html>
<html class="not-ie" lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <title>用户供应商列表</title>
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
                <form action="${user_supplier_list_url}" id="thisForm" name="thisForm" class="form-inline" method="post">
                    <input type="hidden" id="pageNo" name="pageNo" value="1"/>
                    <input type="text" class="form-control input-sm" id="keyword" name="keyword"
                           placeholder="搜索关键词，如供应商名称、联系人、交易用户账号等" style="width:80%" value="${keyword!}"></input>
                    <input type="submit" hidden tabindex="-1"/>
                    <button type="button" class="btn yellow btn-sm" onclick="query(1)">
                        <i class="fa fa-search"></i> 查 询
                    </button>
                </form>
            </div>

            <div style="padding-top: 10px">
                <table id="table_1" table-adv="true"
                       class="table table-striped table-bordered table-hover table-full-width text-center">
                    <colgroup>
                        <col width='70px'>
                        <col width='150px'>
                        <col width='150px'>
                        <col width='100px'>
                        <col width='100px'>
                        <col width='80px'>
                    </colgroup>
                    <thead>
                    <tr>
                        <th class="text-center">序号</th>
                        <th class="text-center">供应商名称</th>
                        <th class="text-center">所在地区</th>
                        <th class="text-center">联系人</th>
                        <th class="text-center">交易用户</th>
                        <th class="text-center">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                        <#list user_supplier_list as supplier>
                            <tr>
                                <td>
                                    ${supplier.id}
                                </td>

                                <td>
                                    ${supplier.supplierName}
                                </td>

                                <td>
                                    ${supplier.countryName} - ${supplier.provinceName} - ${supplier.cityName} - ${supplier.districtName}
                                </td>

                                <td>
                                    ${supplier.supplierContact}
                                </td>

                                <td>
                                    <#if supplier.userInfo?? && supplier.userInfo.mobile?length gt 0>
                                            ${supplier.userInfo.mobile}
                                    </#if>
                                </td>


                                <td>
                                    <a href="${user_supplier_info_detail_url}?id=${supplier.id}">查看</a>
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