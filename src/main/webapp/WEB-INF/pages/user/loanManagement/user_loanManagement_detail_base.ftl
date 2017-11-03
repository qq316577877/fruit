<!DOCTYPE html>

<html class="not-ie" lang="en">
<head>
    <meta charset="utf-8"/>
    <title>贷款管理-贷款详情</title>
<#include "/WEB-INF/pages/base/css.ftl">
    <link rel="stylesheet" href="/admins/assets/plugins/data-tables/DT_bootstrap.css"/>
    <link rel="stylesheet" href="/admins/assets/css/order/upload.css">
    <link rel="stylesheet" type="text/css" href="/admins/assets/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css"/>
    <link rel="stylesheet" href="/admins/assets/css/order/base.css">
    <link rel="stylesheet" type="text/css" href="/admins/assets/plugins/sweetAlert2/dist/sweetalert2.min.css"/>
<#--<link rel="stylesheet" href="/admins/assets/css/order/vipCenter.css">-->
    <link rel="stylesheet" type="text/css" href="/admins/assets/loan/loan.css"/>
    <link rel="stylesheet" href="/admins/assets/css/order/orderLogistic.css">
    <link rel="stylesheet" href="/admins/assets/css/order/x/iconfont.css">
    <link rel="stylesheet" href="/admins/assets/css/order/icon/iconfont.css">
<#include "/WEB-INF/pages/base/js.ftl">
<#import "/WEB-INF/pages/base/base.ftl"  as b>
</head>
<div style="display:none">
    <input id="clearFlag" value="${userLoanManagement.clearFlag}">
    <input id="clearAddress" value="${userLoanManagement.clearAddress}">
    <input id="receiverAddress" value="${userLoanManagement.receiverAddress}">
    <input id="deliveryTime" value="${userLoanManagement.deliveryTime}">
    <input id="preReceiveTime" value="${userLoanManagement.preReceiveTime}">
    <input id="containerId" value="${userLoanManagement.containerId}">
</div>
<div class="page-content extended ">
    <div class="tabbable-custom">
        <form id="order-info-form" name="user-info-form" class="base-info-form form-horizontal">
            <table cellpadding="5px">
                <tr class="form-group" validate="{required:true}">
                    <td align="right" class="control-label">订单号：</td>
                    <td align="left">${userLoanManagement.orderId}</td>
                    <td align="right" class="control-label">订单状态：</td>
                    <td align="left">${userLoanManagement.orderStatusDesc}</td>
                    <td style="padding-left: 60px">
                        <div class="img" style="margin-left: 20px">
                            <span id="contractUrlShow" contractUrl="${contractUrl}">采购合同：</span>
                            <button type=button id="seeCont">查看合同</button>
                        </div>
                    </td>
                </tr>
                <tr class="form-group" validate="{required:true}">
                    <td align="right" class="control-label">货柜批次号：</td>
                    <td align="left">${userLoanManagement.containerId}</td>
                    <td align="right" class="control-label">货柜状态：</td>
                    <td align="left">${userLoanManagement.containerStatusDesc}</td>
                </tr>
                <tr class="form-group" validate="{required:true}">
                    <td align="right" class="control-label">保险合同号：</td>
                    <td style="max-width: 50px;word-wrap: break-word;"
                        align="left">${userLoanManagement.contractNumber}</td>
                    <td align="right" class="control-label">物流信息：</td>
                    <td align="left">
                        <div id="getMap"></div>

                        <button type="button" style="margin-right: 20px" class="btn blue btn-sm"
                                onclick="showLogistics('${userLoanManagement.containerId}')">
                            <i class="fa"></i> 查看物流
                        </button>

                        <a href="#" onclick="logistics()"><img src="/admins/assets/img/map.png"></a>
                    </td>

                </tr>
                <tr class="form-group" validate="{required:true}">
                    <td align="right" class="control-label">发货时间：</td>
                    <td align="left">${userLoanManagement.deliveryTime}</td>
                    <td align="right" class="control-label">预计到货时间：</td>
                    <td align="left">${userLoanManagement.preReceiveTime}</td>
                </tr>
            </table>
        </form>
        <br><br>
        <form id="loan-info-form" name="user-info-form" class="base-info-form form-horizontal">
            <table cellpadding="5px">
                <tr class="form-group" validate="{required:true}">
                    <td align="right" class="control-label">贷款申请单号：</td>
                    <td align="left">${userLoanManagement.containerId}</td>
                    <td align="right" class="control-label">申请日期：</td>
                    <td align="left">${userLoanManagement.addTimeString}</td>
                </tr>
                <tr class="form-group" validate="{required:true}">
                    <td align="right" class="control-label">申请金额：</td>
                    <td align="left">${userLoanManagement.appliyLoan?string('0.00')}</td>
                    <td align="right" class="control-label">贷款状态：</td>
                    <td align="left">
                    <#list loanmanagement_status_list as item>
                        <#if userLoanManagement.status = item.id&&item.id!=999><span> ${item.value}</span></#if>
                        <#if userLoanManagement.status = item.id&&item.id==999><span
                                style="color: red"> ${item.value}</span></#if>
                    </#list>
                    </td>
                </tr>
                <tr class="form-group" validate="{required:true}">
                    <td align="right" class="control-label">确认金额：</td>
                    <td align="left">${userLoanManagement.confirmLoan?string('0.00')}</td>
                    <td align="right" class="control-label">服务费：</td>
                    <td align="left">${userLoanManagement.serviceFee?string('0.00')}</td>
                </tr>

            </table>
        </form>

        <form id="loan-result-form" name="loan-result-form" class="base-info-form form-horizontal">

            <table cellpadding="5px">

                <tr style="display: none">
                    <td class="input-group"><input name="id" type="text" value="${userLoanManagement.id}"
                                                   validate="{required:true}"/></td>
                    <td class="input-group"><input name="userId" type="text" value="${userLoanManagement.userId}"
                                                   validate="{required:true}"/></td>
                    <td class="input-group"><input name="confirmLoan" type="text"
                                                   value="${userLoanManagement.confirmLoan}"
                                                   validate="{required:true}"/></td>
                    <td class="input-group"><input name="dbtExpDtString" type="text"
                                                   value="${userLoanManagement.dbtExpDtString}"
                                                   validate="{required:true}"/></td>
                    <td align="right"><input id="status" name="status" type="text" validate="{required:true}"/></td>
                </tr>
            </table>

        <#--<span>备注：</span>-->
        <#--<textarea style="margin-left: 12px" id ="description" name="description"></textarea>-->
        </form>

    <#if userLoanManagement.status==3 && flag>
        <div style="padding-left: 100px">
            <button class="btn btn-sm btn-primary" onclick="loanTodo()">立即放款</button>
            <button class="btn btn-sm btn-primary" onclick="updateApply(4)">拒绝</button>
        </div>
    </#if>

    </div>

    <div id="mask">
        <div id="pdf1" style="width:60%; height:60%; display: none">
            1It appears you don't have Adobe Reader or PDF support in this web browser.
            <a href="${credit.contractUrl}">Click here to download the PDF</a>
        </div>

        <img src="" alt="">


    </div>

</div>


<!-- 页面JS类库引入 st-->
<!-- 表格 -->
<script type="text/javascript" src="/admins/assets/plugins/data-tables/jquery.dataTables.min.js"></script>
<script src="/admins/assets/plugins/moment/moment.min.js"></script>
<script src="/admins/assets/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
<script src="/admins/assets/plugins/webuploader-0.1.5/webuploader.js"></script>
<script src="/admins/assets/plugins/webuploader-0.1.5/uploadInit.js"></script>
<script src="/admins/assets/plugins/webuploader-0.1.5/uploadPath.js"></script>
<script src="/admins/assets/plugins/date/laydate.dev.js"></script>
<script src="/admins/assets/plugins/pdfobject.min.js"></script>
<script src="/admins/assets/plugins/sweetAlert2/dist/sweetalert2.min.js"></script>
<!-- 表格添加水平滚动，内容不会换行
<script type="text/javascript" src="/admins/assets/plugins/data-tables/DT_bootstrap.js"></script>
-->
<!-- 页面所需组件的js文件 -->
<script src="/admins/js/form.js" type="text/javascript"></script>
<script src="/admins/js/layer/layer.js" type="text/javascript"></script>
<!-- 页面JS类库引入 ed-->

<script>

    function updateApply(code) {

        $("#status").val(code);
        var handleMsg = "立即放款"
        if (code == 4) {
            handleMsg = "拒绝"
        }
        ;
        bootbox.confirm('确定执行' + handleMsg + '操作？', function (result) {
            if (result) {
                $.ajax("${update_user_loanManagement_info_ajax}", {

                    data: $("#loan-result-form").serialize(),
                    type: "POST"
                }).always(function () {
                }).done(function (data) {
                    if (data.result) {
                        bootbox.alert("操作成功!", function () {

                        });
                        window.location.href = "${BASEPATH}/user/loanManagement/detail/base?id=${userLoanManagement.id}";
//                        refresh();

                    } else {
                        bootbox.alert(data.msg);
//                        bootbox.alert("操作失败!请核对贷款信息是否符合条件", function () {
//
//                        });
                        window.location.href = "${BASEPATH}/user/loanManagement/detail/base?id=${userLoanManagement.id}";
//                        refresh();
                    }
                }).fail(function () {
                });
            }
        });
    };


    function logistics() {
        var clearAddress = encodeURI(encodeURI($("#clearAddress").val()));
        var receiverAddress = encodeURI(encodeURI($("#receiverAddress").val()));
        var containerId = encodeURI(encodeURI($("#containerId").val()));

        //页面层
        layer.open({
            type: 2,
            title: '',
            shadeClose: true,
            area: ['650px', '80%'],
        <#--content: ['${BASEPATH}/user/loanManagement/detail/map?deliveryTime=${userLoanManagement.deliveryTime}&preReceiveTime=${userLoanManagement.preReceiveTime}&clearFlag=${userLoanManagement.clearFlag}&clearAddress=${userLoanManagement.clearAddress}&receiverAddress=${userLoanManagement.receiverAddress}', 'no'] //iframe的url-->
            content: ['${BASEPATH}/user/loanManagement/detail/map?containerId=${userLoanManagement.containerId}&deliveryTime=${userLoanManagement.deliveryTime}&preReceiveTime=${userLoanManagement.preReceiveTime}&clearFlag=${userLoanManagement.clearFlag}&clearAddress=' + clearAddress + '&receiverAddress=' + receiverAddress, 'no'] //iframe的url

        });
    }

    function loanTodo() {

        $.ajax("${check_user_loanManagement_info_ajax}", {

            data: $("#loan-result-form").serialize(),
            type: "POST"
        }).always(function () {
        }).done(function (data) {
            if (data.result) {

                var form = $("#loan-result-form");
                form.attr('action', "${BASEPATH}/user/loanManagement/detail/toSignContract");
                form.submit();

            } else {
                bootbox.alert(data.msg);
                window.location.href = "${BASEPATH}/user/loanManagement/detail/base?id=${userLoanManagement.id}";

            }
        }).fail(function () {
        });
    }

    /**
     * 获取总的物流信息
     * @param id
     */
    function showLogistics(id) {

        $.ajax({
            url: '/admin/order/logistics_detail_ajax',
            type: 'get',
            data: {
                id: id
            },
            success: function (data) {
                if (data.code == 200) {
                    // 显示隐藏地图图标
                    if (data.data.containerStatus > 5) {
                        $('.xuan .icon-ditu').css('display', 'inline-block');
                    } else {
                        $('.xuan .icon-ditu').css('display', 'none');
                    }

                    var list = data.data.logisticsDetails;

                    //清空列表
                    $('#logisticsDetails').html('');
                    var arrHtml = new Array();
                    var headLine = ' <div class="logistics_1 clearfix"></div>';
                    arrHtml.push(headLine);

                    if (list != null && list.length > 0) {
                        $.each(list, function (k, v) {
                            var time = v.addTime;
                            var time1 = moment(time).format('YYYY-MM-DD, HH:mm:ss');

                            var tag = ' <div class="logistics_1 clearfix" style="text-align:left">' +
                                    '<span> ' + time1 + '  </span>' +
                                    '<div>' + v.detailInfo + '</div>' +
                                    '<div>';

                            if (v.filePaths != null && v.filePaths != '' && v.filePaths.length > 0) {
                                $.each(v.filePaths, function (m, n) {
                                    if (n != null && n.path != '') {
                                        tag = tag +
                                                '<div class="img"><a target="_blank" href="' + n.url + '"><img src="' + n.url + '"></a></div>';
                                    }
                                })
                            }

                            tag = tag + '</div>' +
                                    '</div>';
                            arrHtml.push(tag);

                        });
                    }
                    swal({
                        title: '物流信息',
                        html: arrHtml.join(""),
//                        type: 'info',
                        width: 500,
                        confirmButtonText: '确定'
                    });

                }
            }
        });
    }


    //查看采购合同
    $('.contractImg').click(function () {
        var src = $(this).attr('src');
        $('.mask').show();

        $('.mask img').attr('src', src);
    })

    $('.mask').on('click', function () {
        $('.mask').hide();
    })

    $('#seeCont').click(function () {
//        alert(333333)
        $('#mask').show();

        var url = $('#contractUrlShow').attr('contractUrl');
        if (url.indexOf(".pdf") >= 0) {
            $('#pdf1').show();
            $('#mask img').hide()
            var options = {
                fallbackLink: "<p>该浏览器不支持pdf预览，请点击<a href='[" + url + "]'>此处</a>下载预览</p>"
            };
            PDFObject.embed(decodeURIComponent(url, "#pdf1", options));
            $('embed').after('<a href="${BASEPATH}/user/loanManagement/detail/base?id=${userLoanManagement.id}"  id="closeBut">关闭</a>')
        } else {
            $('#pdf1').hide();
            $('#mask img').show().attr('src', url)
            $('#mask img').after('<a href="${BASEPATH}/user/loanManagement/detail/base?id=${userLoanManagement.id}"  id="closeBut">关闭</a>')
        }

    })

    function closeThisP() {
        $('#mask').hide()
    }
</script>
<style>
    .closeBut {
        position: absolute;
        top: 90%;
        left: 45%;
        background: #00aa5c;
        color: #fff;
        /* padding: 2px 5px; */
        height: 20px;
        width: 80px;
    }

    .contractImg {
        width: 200px;
        position: absolute;
    }

    .mask {
        width: 100%;
        height: 100%;
        position: fixed;
        top: 0;
        left: 0;
        transition: all .5s;
        background: rgba(0, 0, 0, .4);
        z-index: 50;
        display: none;
    }

    .mask > div {
        width: 500px;
        height: 400px;
        position: absolute;
        left: 50%;
        margin-left: -250px;
        top: 5%;
    }

    .mask img {
        width: 100%;
        height: 100%;
        display: block;
    }

    #mask img {
        width: 60%;
        margin-left: 20%;
        margin-top: 3%;
    }
</style>
</body>
</html>