<!DOCTYPE html>
<html class="not-ie" lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <title>用户列表</title>

    <link rel="stylesheet" href="/admins/assets/css/neworder/newOrderLogistic.css">
    <link rel="stylesheet" href="/admins/assets/css/neworder/orderInfo/orderInfoLogistics.css">
</head>
<body>
    <div class="orderInfo_children" style="display: none;">
        <div class="logistics_body">
            <div class="logistics_l logistics_info fl">
                <h3>物流详情</h3>
                <div class="xuan">
                    货柜批次号：
                    <select class="" name="" id="containerNoSelect">
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
            <div id="imgmask" >
                <div id="maskImg">
                    <img src="" alt="">
                </div>
            </div>
            <div id="addLogisticsInfo" style="display: none">
                <div class="logistics_l logistics_r fr">
                    <h3>添加物流信息</h3>
                    <div class="containers">
                        <span><i>*</i>物流类型：</span>
                        <select class="" name="" disabled="disabled" id="logisticsType">
                        </select>
                    </div>
                    <div class="containers preReceiveTime">
                        <span><i>*</i>预计到货时间：</span>
                        <input id="preReceiveDateTime" type="text" >
                    </div>
                    <div class="containers lockId">
                        <span>物流锁编号：</span>
                        <input id="lockId" type="text" maxlength="64">
                    </div>
                    <div class="containers logisticsContainerNo">
                        <span><i>*</i>货柜号：</span>
                        <input id="logisticsContainerNo" type="text" maxlength="60">
                    </div>
                    <div class="containers transportNumber">
                        <span><i>*</i>车牌号：</span>
                        <input id="transportNumber" type="text" maxlength="32">
                    </div>
                    <div class="containers driverName">
                        <span>司机姓名：</span>
                        <input id="driverName" type="text" maxlength="64">
                    </div>
                    <div class="containers driverMobile">
                        <span>联系电话：</span>
                        <input id="driverMobile" type="text" maxlength="32">
                    </div>

                    <div class="containers signer">
                        <span><i>*</i>签收人：</span>
                        <input id="signer" type="text" maxlength="64">
                    </div>
                    <div class="containers signerMobile">
                        <span>联系电话：</span>
                        <input name="" id="signerMobile"   maxlength="32"></input>
                    </div>
                    <div class="containers detailInfo">
                        <span>备注信息：</span>
                        <textarea name="" id="detailInfo" cols="40" rows="5" maxlength="250"></textarea>
                    </div>

                    <div class="containers photoTake clearfix">
                        <span class="fl">单据影像：</span>
                        <button id="imgPicker">上传图片</button>
                        <#--<div class="big-photo">-->
                            <#--<div id="preview">-->
                                <#--<div id="imgPicker"><img id="showImg" src="/admins/assets/img/img_1_r2_c2.gif"/></div>-->
                            <#--</div>-->
                        <#--</div>-->

                        <#--<div class="big-photo">-->
                            <#--<div id="preview_2">-->
                                <#--<div id="imgPicker2"><img id="showImg2" src="/admins/assets/img/img_1_r2_c2.gif"/></div>-->
                            <#--</div>-->
                        <#--</div>-->
                        <div id="showImage">

                        </div>
                    </div>

                    <input type="button" class="button" id="logisticsAdd" value="确定">
                    <input type="button" class="" id="logisticsCancel" value="取消">
                </div>
            </div>
        </div>
    </div>
    <script src="/admins/assets/plugins/moment/moment.min.js"></script>
    <script src="/admins/assets/plugins/date/laydate.dev.js"></script>
    <script src="/admins/js/neworder/newOrderAudit/logisticsDetails.js"></script>
    <script src="/admins/assets/plugins/webuploader-0.1.5/webuploader.js"></script>
    <script src="/admins/assets/plugins/webuploader-0.1.5/uploadInit.js"></script>
    <script src="/admins/assets/plugins/webuploader-0.1.5/logisticsuploadPath.js"></script>
</body>
<script>
    laydate({
        elem: '#preReceiveDateTime',
        min: laydate.now(+1)
    });
</script>
</html>