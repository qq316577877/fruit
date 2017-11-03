<!DOCTYPE html>

<html class="not-ie" lang="en">
<head>
    <meta charset="utf-8"/>
    <title>还款管理-还款详情</title>
<#include "/WEB-INF/pages/base/css.ftl">
    <link rel="stylesheet" href="/admins/assets/plugins/data-tables/DT_bootstrap.css"/>
    <link rel="stylesheet" href="/admins/assets/css/order/upload.css">
    <link rel="stylesheet" type="text/css" href="/admins/assets/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css"/>
    <link rel="stylesheet" href="/admins/assets/css/order/base.css">
    <link rel="stylesheet" type="text/css" href="/admins/assets/plugins/sweetAlert2/dist/sweetalert2.min.css"/>
<#--<link rel="stylesheet" href="/admins/assets/css/order/vipCenter.css">-->
    <link rel="stylesheet" href="/admins/assets/css/order/orderLogistic.css">
    <link rel="stylesheet" href="/admins/assets/css/order/x/iconfont.css">
    <link rel="stylesheet" href="/admins/assets/css/order/icon/iconfont.css">
    <link rel="stylesheet" type="text/css" href="/admins/assets/loan/loan.css"/>
<#include "/WEB-INF/pages/base/js.ftl">
<#import "/WEB-INF/pages/base/base.ftl"  as b>

    <style>

        .maskNew{
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
        /*.maskNew>div{*/
            /*width: 500px;*/
            /*height: 400px;*/
            /*position: absolute;*/
            /*left: 50%;*/
            /*margin-left: -250px;*/
            /*top: 5%;*/
        /*}*/
        .maskNew img{
            width: 60%;
            height: 100%;
            display: block;
        }


    </style>

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
            <span style="font-size: 16px;font-weight: 800">订单信息</span>
            <table cellpadding="5px">
                <tr class="form-group" validate="{required:true}">
                    <td align="right" class="control-label">手机号：</td>
                    <td align="left" >${userLoanManagement.mobile}</td>
                    <td align="right" class="control-label">姓名：</td>
                    <td align="left" >${userLoanManagement.username}</td>
                </tr>

                <tr class="form-group" validate="{required:true}">
                    <td align="right" class="control-label">订单号：</td>
                    <td align="left" >${userLoanManagement.orderId}</td>
                    <td align="right" class="control-label">订单状态：</td>
                    <td align="left" >${userLoanManagement.orderStatusDesc}</td>
                    <td style="padding-left: 60px">
                        <div class="img" style="margin-left: 20px">
                            <span id="contractUrlShow" contractUrl="${contractUrl}">采购合同：</span>
                            <button type=button id="seeCont">查看合同</button>
                        </div>
                    </td>
                </tr>
                <tr class="form-group" validate="{required:true}">
                    <td align="right" class="control-label">货柜批次号：</td>
                    <td align="left" >${userLoanManagement.containerId}</td>
                    <td align="right" class="control-label">货柜状态：</td>
                    <td align="left" >${userLoanManagement.containerStatusDesc}</td>
                </tr>

                <tr class="form-group" validate="{required:true}">
                    <td align="right" class="control-label">保险合同号：</td>
                    <td style="max-width: 50px;word-wrap: break-word;"
                        align="left">${userLoanManagement.contractNumber}</td>
                    <td align="right" class="control-label">物流信息：</td>
                    <td align="left">
                        <div id="getMap"></div>

                        <button type="button" style="margin-right: 20px" class="btn blue btn-sm" onclick="showLogistics('${userLoanManagement.containerId}')">
                            <i class="fa"></i> 查看物流
                        </button>

                        <a href="#" onclick="logistics()"><img src="/admins/assets/img/map.png"></a>

                    </td>

                </tr>

                <tr class="form-group" validate="{required:true}">
                    <td align="right" class="control-label">发货时间：</td>
                    <td align="left" >${userLoanManagement.deliveryTime}</td>
                    <td align="right" class="control-label" >预计到货时间：</td>
                    <td align="left" >${userLoanManagement.preReceiveTime}</td>
                </tr>
            </table>
        </form>
        <br><br>
        <form id="loan-info-form" name="user-info-form" class="base-info-form form-horizontal">
            <span style="font-size: 16px;font-weight: 800">借据信息</span>
            <table cellpadding="5px" >
                <tr class="form-group" validate="{required:true}">
                    <td align="right" class="control-label">借据号：</td>
                    <td align="left" >${userLoanManagement.dbtNo}</td>
                    <td align="right" class="control-label">借据到期日期：</td>
                    <td align="left" >${userLoanManagement.dbtExpDtString}</td>
                    <td align="right" class="control-label">强制还款日期：</td>
                    <td align="left" >${userLoanManagement.expiresTimeString}</td>
                </tr>
                <tr class="form-group" validate="{required:true}">
                    <td align="right" class="control-label">借据金额：</td>
                    <td align="left" >${userLoanManagement.offerLoan?string('0.00')}</td>
                    <td align="right" class="control-label">起息日：</td>
                    <td align="left" >${userLoanManagement.addTimeString}</td>
                </tr>
                <tr class="form-group" validate="{required:true}">
                    <td align="right" class="control-label">还款方式：</td>
                    <td align="left" >
                        <#list repayment_type_list as type>
                            <#if repaymentMethod = type.id><span > ${type.value}</span></#if>
                        </#list>
                    </td>
                    <td align="right" class="control-label">执行利率：</td>
                    <td align="left" >${performanceRate?if_exists?string.number}%</td>
                </tr>
                <tr class="form-group" validate="{required:true}">

                    <td align="right" class="control-label">贷款状态：</td>
                    <td align="left" >
                    <#list repayment_status_list as item>
                        <#if userLoanManagement.status = item.id><span > ${item.value}</span></#if>
                    </#list>
                    </td>
                    <td align="right" class="control-label"></td>
                    <td align="right" class="control-label"></td>
                </tr>

            </table>
        </form>
        <br><br>
        <#if userLoanManagement.status==1 || userLoanManagement.status==200>
            <form id="repayment-info-form" name="user-info-form" class="base-info-form form-horizontal">
                <span style="font-size: 16px;font-weight: 800">还款明细</span>
                <table cellpadding="5px" width="400px">
                    <tr class="form-group" validate="{required:true}">
                        <td align="right" class="control-label">还款本金：</td>
                        <td align="left" class="control-label">${userLoanManagement.offerLoan?string('0.00')}元</td>
                        <td align="right" class="control-label">还款利息：</td>
                        <td align="left" class="control-label">${userLoanManagement.repaymentInterest?string('0.00')}元</td>
                    </tr>
                    <tr class="form-group" validate="{required:true}">
                        <td align="right" class="control-label">还款日期：</td>
                        <td align="left" class="control-label">${userLoanManagement.repaymentTimeString}</td>
                        <td align="right" class="control-label"></td>
                        <td align="right" class="control-label"></td>
                    </tr>
                </table>
            </form>
        </#if>

        <form id="loan-result-form" name="loan-result-form" class="base-info-form form-horizontal">

            <table cellpadding="5px" width="400px">

                <tr style="display: none">
                    <td class="input-group"><input name="id" type="text" value="${userLoanManagement.id}" validate="{required:true}"/></td>
                    <td class="input-group"><input name="userId" type="text" value="${userLoanManagement.userId}" validate="{required:true}"/></td>
                    <td class="input-group"><input name="dbtNo" type="text" value="${userLoanManagement.dbtNo}" validate="{required:true}"/></td>
                    <td align="right"><input id ="status" name="status" type="text" validate="{required:true}"/></td>
                </tr>
            </table>

        </form>

        <div style="padding-left: 100px">
        <#if userLoanManagement.status==6 && flag>
            <button class="btn btn-sm btn-primary"  onclick="updateRepayment(1)">提前还款</button>
        </#if>
        </div>

    </div>

</div>

<#--显示大图-->
<div id="maskNew" class="maskNew">

    <div id="pdfNew" class="pdfNew">
        1It appears you don't have Adobe Reader or PDF support in this web browser.
        <a href="${credit.contractUrl}">Click here to download the PDF</a>

    </div>

    <img src="" alt="">
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

    function updateRepayment(code){

        $("#status").val(code);
        bootbox.confirm('确定执行立即还款操作？',function(result) {
            if (result) {
                $.ajax("${update_user_repayment_info_ajax}", {

                    data: $("#loan-result-form").serialize(),
                    type: "POST"
                }).always(function () {
                }).done(function (data) {
                    if (data.result) {
                        bootbox.alert("操作成功!", function () {

                        });
                        window.location.href = "${BASEPATH}/user/repayment/detail/base?id=${userLoanManagement.id}";
//                        refresh();

                    } else {
                        bootbox.alert(data.msg);
//                        bootbox.alert("操作失败!请核对还款条件条件", function () {
//
//                        });
                        window.location.href = "${BASEPATH}/user/repayment/detail/base?id=${userLoanManagement.id}";
//                        refresh();
                    }
                }).fail(function () {
                });
            }
        });
    }

    function closePop() {
        $('#maskNew').hide();
    }

    $(function () {
        var list =$('.control-label');
        $.each(list, function (k, v) {
            v.style.width="140px";
        });


        $('#seeCont').click(function () {
            $('#maskNew').show();

            var url =$('#contractUrlShow').attr('contractUrl');
            console.log(url);
            if( url.indexOf(".pdf") >=0){
                $('#pdfNew').show();
                $('#maskNew img').hide();

                var options = {
                    fallbackLink: "<p>该浏览器不支持pdf预览，请点击<a href='["+url+"]'>此处</a>下载预览</p>",

                };

                PDFObject.embed(decodeURIComponent(url, "#pdfNew",options));
                $('embed').after('<a href="${BASEPATH}/user/repayment/detail/base?id=${userLoanManagement.id}"  id="closeBut">关闭</a>')
            }else{
                $('#pdf1').hide();
                $('#mask img').show().attr('src',url)
                $('#mask img').after('<a href="${BASEPATH}/user/repayment/detail/base?id=${userLoanManagement.id}"  id="closeBut">关闭</a>')
//                console.log(options);
//                PDFObject.embed(url, "#pdfNew",options);
            }


        })
    });

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
                    var arrHtml=new Array();
                    var headLine = ' <div class="logistics_1 clearfix"></div>';
                    arrHtml.push(headLine);



                    if(list != null && list.length > 0) {
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
                        confirmButtonText:'确定'
                    });

                }
            }
        });
    }


    //查看采购合同
    $('.contractImg').click(function () {
        var src =$(this).attr('src');
        $('.maskNew').show();

        $('.maskNew img').attr('src',src);
    });

    $('.maskNew').on('click',function () {
        $('.maskNew').hide();
    });

</script>

</body>
</html>