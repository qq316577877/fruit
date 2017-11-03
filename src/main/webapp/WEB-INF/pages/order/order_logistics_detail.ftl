<!DOCTYPE html>
<html class="not-ie" lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <title>用户列表</title>
<#include "/WEB-INF/pages/base/css.ftl">
    <link rel="stylesheet" href="/admins/assets/css/order/upload.css">
    <link rel="stylesheet" href="/admins/assets/plugins/data-tables/DT_bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="/admins/assets/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css"/>
    <link rel="stylesheet" href="/admins/assets/css/order/base.css">
<#--<link rel="stylesheet" href="/admins/assets/css/order/vipCenter.css">-->
    <link rel="stylesheet" href="/admins/assets/css/order/orderLogistic.css">
    <link rel="stylesheet" href="/admins/assets/css/order/x/iconfont.css">
    <link rel="stylesheet" href="/admins/assets/css/order/icon/iconfont.css">
<#include "/WEB-INF/pages/base/js.ftl">
<#import "/WEB-INF/pages/base/base.ftl"  as b>
</head>
<body>
<div style="display: none">
    <input id="order" value="${data.orderNo}">
</div>


<div class="audit_head"><a href="/admin/order/list">订单中心</a>>物流信息></div>

<div class="logistics_head">

    <div><span class="formlabel">订单号：</span>${data.orderNo}</div>
    <div><span class="formlabel">下单日期：</span>${data.placeOrderTime}</div>
    <div><span class="formlabel">货柜批次号：</span>
        <div id="containerId" style="display: inline-block">
        <#list data.orderContainers as item>
        ${item}、
        </#list>
        </div>
    </div>
    <div><span class="formlabel">国际物流公司：</span>${data.outerExpress.name}</div>
    <div><span class="formlabel">国内物流公司：</span>${data.innerExpress.name}</div>
    <input id="logisticsId" style="display: none" value="${data.logiticsId}">


</div>

<div class="logistics_body">
    <div class="logistics_l logistics_info fl">
        <h3>物流详情</h3>
        <div class="xuan">
            货柜批次号：
            <select class="" name="" id="select">
            <#list data.orderContainers as item>
                <option>${item}</option>
            </#list>
            </select>
            <i class="iconfont map icon-ditu"></i>
            <input type="button" value="添加物流信息" id="addDails">
        </div>
        <div class="logistics_body" id="logisticsDetails">
        </div>
    </div>
</div>
<!--弹出框-->
<!--mask-->
<div class="mask">
    <div id="mask" >
        <div id="maskImg">
            <img src="" alt="">
        </div>
    </div>
    <div id="addLogisticsInfo" style="display: none">
        <div class="logistics_l logistics_r fr">
            <h3>添加物流信息</h3>
            <div class="containers orderContainer">
                <span>货柜批次号：</span>
                <#--<input id="containNo" type="text">-->
                <select class="" name="" id="containNo">
                <#list data.orderContainers as item>
                    <option>${item}</option>
                </#list>
                </select>
            <#--<textarea name="" id="" cols="100" rows="60"></textarea>-->
            </div>
            <div class="containers textarea">
                <span>填写物流信息：</span>
                <textarea name="" id="textarea" cols="40" rows="5"></textarea>
            </div>
            <div class="containers photoTake clearfix">
                <span class="fl">单据影像：</span>

                <div class="big-photo">
                    <div id="preview">
                        <div id="imgPicker"><img id="showImg" src="/admins/assets/img/img_1_r2_c2.gif"/></div>
                    </div>

                </div>

                <div class="big-photo">
                    <div id="preview_2">
                        <div id="imgPicker2"><img id="showImg2" src="/admins/assets/img/img_1_r2_c2.gif"/></div>
                    </div>
                </div>

            </div>

            <div class="containers preReceive">
                <span>预计到货时间：</span>
                <input id="preReceiveTime" type="text">
            </div>
            <input type="button" class="button" id="btn" value="添加">
            <input type="button" class="" id="btnCanLogi" value="取消">
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
<script src="/admins/assets/plugins/moment/moment.min.js"></script>
<script src="/admins/assets/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
<script src="/admins/js/form.js" type="text/javascript"></script>
<script src="/admins/js/order/orderLogistics/logisticsHandler.js"></script>
<script src="/admins/assets/plugins/webuploader-0.1.5/webuploader.js"></script>
<script src="/admins/assets/plugins/webuploader-0.1.5/uploadInit.js"></script>
<script src="/admins/assets/plugins/webuploader-0.1.5/uploadPath.js"></script>
<script src="/admins/assets/plugins/date/laydate.dev.js"></script>
<script src="/admins/js/layer/layer.js" type="text/javascript"></script>

<script type="text/javascript">
    laydate({
        elem: '#preReceiveTime'
    });

    function logistics (containerId){
        var clearAddress=encodeURI(encodeURI($("#clearAddress").val()));
        var receiverAddress=encodeURI(encodeURI($("#receiverAddress").val()));
        //页面层
        layer.open({
            type: 2,
            title: '',
            shadeClose: true,
            area: ['650px', '80%'],
        <#--content: ['${BASEPATH}/user/loanManagement/detail/map?deliveryTime=${userLoanManagement.deliveryTime}&preReceiveTime=${userLoanManagement.preReceiveTime}&clearFlag=${userLoanManagement.clearFlag}&clearAddress=${userLoanManagement.clearAddress}&receiverAddress=${userLoanManagement.receiverAddress}', 'no'] //iframe的url-->
            <#--content: ['${BASEPATH}/user/loanManagement/detail/map?deliveryTime=${userLoanManagement.deliveryTime}&preReceiveTime=${userLoanManagement.preReceiveTime}&clearFlag=${userLoanManagement.clearFlag}&clearAddress='+clearAddress+'&receiverAddress='+receiverAddress, 'no'] //iframe的url-->
            content: ['${BASEPATH}/order/logistics/map?containerId='+containerId] //iframe的url
        });
    }

</script>

</body>
</html>