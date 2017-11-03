<!DOCTYPE html>

<html class="not-ie" lang="en">
<head>
    <meta charset="utf-8"/>
    <title>用户详情-产品额度管理</title>
<#include "/WEB-INF/pages/base/css.ftl">
    <link rel="stylesheet" href="/admins/assets/plugins/data-tables/DT_bootstrap.css"/>
<#include "/WEB-INF/pages/base/js.ftl">
<#import "/WEB-INF/pages/base/base.ftl"  as b>
</head>
<body>
<div  class="page-content extended ">

    <div class="status-info-box">
        <button type="button" class="btn green btn-sm" id="addBtn" onclick="showDetail(${userId})">
            <i class="fa fa-plus"></i> 新 增
        </button>

    <div style="padding-top: 10px">
        <form id="product-loan-info-form" name="user-loan-info-form" >
        <table id="productList" table-adv="true"
               class="table table-striped table-bordered table-hover table-full-width text-center">
            <colgroup>
                <col width='100px'>
                <col width='100px'>
                <col width='100px'>
                <col width='100px'>
            </colgroup>
            <thead>
            <tr>
                <th class="text-center">序号</th>
                <th class="text-center">产品名称</th>
                <th class="text-center">单笔贷款额度（元）</th>
                <th class="text-center">操作</th>
            </tr>
            </thead>

            <tbody>
            <#list user_product_list as product>
            <tr>

                <td>
                ${product.productId}
                </td>

                <td>
                ${product.productName}
                </td>

                <td>
                    <input  id = "${product.id}" value="${product.productLoan}">
                </td>

                <td>
                    <button id ="${product.id}btn" type="button" class="btn red btn-sm" onclick="updateLoan(${product.id})">修改</button>
                    <#--<button id ="${product.id}btn" type="button" class="btn red btn-sm" >修改</button>-->
                </td>

            </tr>
            </#list>
            </tbody>
        </table>
        </form>


    <#--<@b.pageSimple pageDto 'userList'/>-->


</div>


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

        function updateLoan(productId) {
                    $.ajax("${update_product_loan_ajax}", {
                    data: {
                        "productId":productId,
                        "productLoan":$("#"+productId).val()
                    },
                    type: "POST"
                }).always(function () {
                }).done(function (data) {
                    if (data.result) {
                        bootbox.alert("操作成功!", function () {
                        window.location.href = "${BASEPATH}/user/detail/product?id=${userId}";
                        });
                    } else {
                        bootbox.alert(data.msg);
                    }
                }).fail(function () {
                });
        }

    function showDetail(userId) {
        var url = "${product_loan_add}";
        var v_title = '新增';
        if (userId) {
            url += "?userId=" + userId;
            v_title = '增加贷款商品';
        }

        var d = dialog({
            title: v_title,
            url: url,
            iframe_width: 700,
            iframe_height: 200,
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
</body>
</html>