/**
 * Created by qinmenghuan on 2017/9/25.
 * 物流信息
 */

var containerList = [];
$(function () {
    // 订单号
    var orderNo = window.location.href.split('=')[1];
    var containerListIndex = 0;

    var orderId = $('#order').val();
    var logisticsId = $('#logisticsId').val();
    // 初始化
    init();

    // 初始化
    function init() {
        var data = {orderNo: orderNo};
        $.ajax({
            url: '/admin/neworder/center/container/logistics_init_ajax',
            data: JSON.stringify(data),
            type: 'post',
            success: function (data) {
                //console.log("response:",data);
                if (data.code == 200) {
                    var selectstr = "";
                    containerList = data.data;
                    // 物流详情数据
                    if (data.data && data.data.length > 0) {
                        $.each(data.data, function (index, val) {
                            selectstr += '<option value="' + val.containerId + '">' + val.containerNo + '</option>';
                        });
                        $("#containerNoSelect").html(selectstr);
                        // 查询物流列表信息
                        getLogisticsAndShow(containerList[containerListIndex].containerId);

                        // 货柜对象
                        var container = containerList[containerListIndex];
                        if (container.status == 0 || container.status == 7) {
                            $("#addDails").hide();
                        }

                        // 物流类型
                        $("#logisticsType").html('<option value="' + containerList[containerListIndex].type + '">' + containerList[containerListIndex].typeDesc + '</option>');

                    }
                }
            }
        });

        // 货柜批次号：
        $('#containerNoSelect').on('change', function () {

            //console.log("orderDetailInfo123:",orderDetailInfo);

            // 切换下拉框状态

            // 判断订单状态
            var orderStatus = orderDetailInfo.status;
            if (orderStatus == "2" || orderStatus == "3" || orderStatus == "4" || orderStatus == "5") {
                $("#addDails").show();

                var containerVal = $('#containerNoSelect').val();
                // 货柜批次列表
                if (containerList.length > 0) {
                    $.each(containerList, function (index, val) {
                        var container = containerList[index];
                        if (containerVal == container.containerId) {
                            // 根据货柜状态判断是否有添加物流信息
                            //console.log("container:",container);
                            if (container.status == 0 || container.status == 7) {

                                $("#addDails").hide();
                            }

                            // 物流类型
                            containerListIndex = index;
                            $("#logisticsType").html('<option value="' + containerList[containerListIndex].type + '">' + containerList[containerListIndex].typeDesc + '</option>');

                        }
                    })
                }
            } else {
                $("#addDails").hide();
            }

            // 查询物流列表信息
            getLogisticsAndShow($('#containerNoSelect').val());
        });
    }

    // 查询物流信息列表
    //function logistics_detail_list(){
    //    var data = {containerId: containerList[containerListIndex].containerId};
    //    $.ajax({
    //        url:'/admin/neworder/center/container/logistics_detail_list_ajax',
    //        data: JSON.stringify(data),
    //        type: 'post',
    //        success: function (data) {
    //            console.log("response:",data);
    //            if(data.code==200){
    //
    //            }
    //        }
    //    });
    //}


    //$.ajax({
    //    url:'/admins/js/neworder/newOrderAudit/logistics_id.json',
    //    success:function (response) {
    //        response=JSON.parse(response);
    //        if(response.code==200){
    //            var list =response.data.containerId;
    //            $.each(list,function (k, v) {
    //                var tag ='<option>'+v+'</option>';
    //                $('#select').append(tag)
    //            })
    //        }
    //    }
    //});

    //清空列表
    $('.logistics_l .logistics_body').html('');
    //默认选中第一个
    $('#select option').eq(0).attr('selected', true);


    //查询第一个数据
    var NoOne = $('#select option').eq(0).html();
    $('#containNo').val(NoOne);
    //getLogisticsAndShow(NoOne);
    //改变货柜
    //$('#select').on('change', function () {
    //    var selectContain = $('#select option:selected').html();
    //    $('#containNo').val(selectContain);
    //    $('.logistics_l .logistics_body').html('');
    //    getLogisticsAndShow(selectContain);
    //})

    //鼠标点击获取大图
    $('.logistics_body').on('click', '.img', function () {
        var src = $(this).find('img').attr('src');
        $('.mask').css('display', 'block');
        $('#imgmask').css('display', 'block');
        $('#maskImg').find('img').attr('src', src);

    });

    //点击关闭--关闭大图
    $('.mask').on('click', '#maskImg', function () {
        $('.mask').css('display', 'none');
    });

    //添加物流信息----打开弹出框
    $('#addDails').click(function () {
        $("#imgmask").hide();


        // 如果货柜批次号
        if (containerList.length == 0) {
            return;
        }

        // 初始化form表单
        // $("#preReceiveDateTime").val("");
        // $('#preReceiveDateTime').val(moment().format('YYYY-MM-DD'))
        $("#lockId").val("");
        $("#logisticsContainerNo").val("");
        $("#transportNumber").val("");
        $("#driverName").val("");
        $("#driverMobile").val("");
        $("#signer").val("");
        $("#signerMobile").val("");
        $("#detailInfo").val("");

        var containerType = containerList[containerListIndex].type;
        // 发货
        if (containerType == 1) {
            // 司机姓名和手机号
            $('.driverName').show();
            $('.driverMobile').show();
            // 预计到货时间
            $('.preReceiveTime').hide();
            // 锁
            $('.lockId').hide();
            // 货柜号
            $('.logisticsContainerNo').show();
            // 车牌号
            $('.transportNumber').show();
            // 签收人及电话
            $('.signer').hide();
            $('.signerMobile').hide();

        }// 通关
        else if (containerType == 2) {
            // 司机姓名和手机号
            $('.driverName').show();
            $('.driverMobile').show();
            // 预计到货时间
            $('.preReceiveTime').show();
            // 锁
            $('.lockId').show();
            // 货柜号
            $('.logisticsContainerNo').hide();
            // 车牌号
            $('.transportNumber').show();
            // 签收人及电话
            $('.signer').hide();
            $('.signerMobile').hide();
        }// 签收
        else if (containerType == 3) {
            // 司机姓名和手机号
            $('.driverName').hide();
            $('.driverMobile').hide();

            // 预计到货时间
            $('.preReceiveTime').hide();
            // 锁
            $('.lockId').hide();
            // 货柜号
            $('.logisticsContainerNo').hide();
            // 车牌号
            $('.transportNumber').hide();
            // 签收人及电话
            $('.signer').show();
            $('.signerMobile').show();
        }

        $('.driver').remove();

        $('#textarea').val('');
        $('#showImg').attr('src', '/admins/assets/img/img_1_r2_c2.gif');
        $('#showImg2').attr('src', '/admins/assets/img/img_1_r2_c2.gif');
        $('.mask').css('display', 'block');
        $('#addLogisticsInfo').css('display', 'block');
        $('#mask').css('display', 'none');
        $('#imgPicker>div:nth-of-type(2)').css({
            'width': '60px',
            'height': '30px',
        })
        // $('#imgPicker2>div:nth-of-type(2)').css({
        //     'width': '170px',
        //     'height': '120px',
        // })
        // $('input[name="tong"]')[1].checked=true;
        if ($('input[name="receiving"]').length != 0) {
            $('input[name="receiving"]')[1].checked = true;
        }
        if ($('input[name="tong"]').length != 0) {
            $('input[name="tong"]')[1].checked = true;
        }
    });

    // 保存物流信息
    $('#logisticsAdd').click(function () {

        var preReceiveDateTime = $('#preReceiveDateTime').val();
        // preReceiveDateTime += " 00:00:00";
        // console.log("time:",preReceiveDateTime);
        // return;
        var todayDate =moment().format('YYYY-MM-DD');
        if($('#logisticsType option').eq(0).attr('value')=='2' ){
            if(preReceiveDateTime==''){
                bootbox.alert('请选择预计到货时间');
                return
            }
            else if (preReceiveDateTime<todayDate){
                bootbox.alert('预计到货时间选择有误');
                return
        }
        }
        var containerId = $('#containerNoSelect').val();
        var typevalue = $('#logisticsType').val();
        var containerNo = $('#logisticsContainerNo').val();
        var transportNumber = $('#transportNumber').val();
        var driverName = $('#driverName').val();
        var driverMobile = $('#driverMobile').val();
        var logisticsTextarea = $('#logisticsTextarea').val();
        var lockId = $('#lockId').val();
        var signer = $('#signer').val();
        var signerMobile = $('#signerMobile').val();

        var contractUrl = myContractUrl();
        var voucherUrl = myVoucherUrl();
        //console.log(contractUrl);
        var path = [];
        // if ($('#showImg').attr('src') == '/admins/assets/img/img_1_r2_c2.gif' && $('#showImg2').attr('src') == '/admins/assets/img/img_1_r2_c2.gif') {
        //     path = [];
        // } else if ($('#showImg').attr('src') != '/admins/assets/img/img_1_r2_c2.gif' && $('#showImg2').attr('src') == '/admins/assets/img/img_1_r2_c2.gif') {
        //     path = [contractUrl, null]
        // } else if ($('#showImg').attr('src') == '/admins/assets/img/img_1_r2_c2.gif' && $('#showImg2').attr('src') != '/admins/assets/img/img_1_r2_c2.gif') {
        //     path = [null, voucherUrl]
        // } else {
        //     path.push(contractUrl);
        //     path.push(voucherUrl);
        // }
        var pathLists = $('#showImage img');
        $.each(pathLists, function (k, v) {
            path.push(v.getAttribute('imgpath'))
        });


        var jsonData = {
            orderNo: orderNo,
            containerId: containerId,
            containerBoxNo: containerNo,
            transportNumber: transportNumber,
            type: typevalue,
            driverName: driverName,
            driverMobile: driverMobile,
            detailInfo: logisticsTextarea,
            lockId: lockId,
            signer: signer,
            signerMobile: signerMobile,
            preReceiveTime: preReceiveDateTime,
            filePaths: path
        };

        // console.log("jsonData:",jsonData);
        // return;

        $.ajax({
            url: '/admin/neworder/center/container/logistics/add',
            data: JSON.stringify(jsonData),
            type: 'post',
            success: function (data) {
                // console.log("response:",data);
                if (data.code == 200) {
                    //$('#textarea')[0].value='';
                    bootbox.alert('添加成功');
                    $('.mask').css('display', 'none');
                    $('#addLogisticsInfo').css('display', 'none');
                    var orderNo = $('#order').html();
                    // 查询物流列表信息1
                    getLogisticsAndShow(containerId);
                    self.location = '/admin/newOrder/orderAudit?orderNo=' + orderNo;
                    window.localStorage.setItem('addLogisticSuccess', containerId);
                } else {
                    bootbox.alert(data.msg);
                }
            },
        })
    });

    //添加物流 ---弹窗关闭
    $('.mask').on('click', '#logisticsCancel', function () {
        $('.mask').css('display', 'none');
        $('#addLogisticsInfo').css('display', 'none');
        $('#showImage').html('');
        var orderNo = $('#order').html();
        self.location = '/admin/newOrder/orderAudit?orderNo=' + orderNo;
        window.localStorage.setItem('closeLogisticPop', 1);
    });

    $('.xuan .icon-ditu').on('click', function () {
        var containerid = $('#select').val();
        // console.log("click");
        logistics(containerid);
    });

    /**
     * 获取总的物流信息
     * @param id
     */
    function getLogisticsAndShow(containerId) {
        $.ajax({
            url: '/admin/neworder/center/container/logistics_detail_list_ajax',
            data: JSON.stringify({
                containerId: containerId
            }),
            type: 'post',
            success: function (data) {
                //data=JSON.parse(data);
                if (data.code == 200) {

                    var list = data.data;

                    //清空列表
                    $('#logisticsDetails').html('');

                    $.each(list, function (index, val) {

                        var infoStr = ' <div class="logistics_1 clearfix">' +
                            '<span> ' + val.addTimeStr + '  </span>';
                        // 签收
                        if (val.type == 3) {
                            infoStr += '<span>已' + val.typeDesc + ",联系人(" + val.signer + val.signerMobile + ")" + '</span>';
                        } else {
                            infoStr += '<span>已' + val.typeDesc + ",联系人(" + val.driverName + val.driverMobile + ")" + '</span>';
                        }
                        infoStr += '<div class="showImageLists">';
                        // if(val.filePaths.length>0){
                        //     if(val.filePaths[0].path=='' && val.filePaths[1].path!=''){
                        //         infoStr +='<div class="img"><img src="' + val.filePaths[1].url + '"></div>' ;
                        //     }else if(val.filePaths[0].path!='' && val.filePaths[1].path==''){
                        //         infoStr +='<div class="img"><img src="' + val.filePaths[0].url + '"></div>' ;
                        //     }else if(val.filePaths[0].path=='' && val.filePaths[1].path==''){
                        //         infoStr +='' ;
                        //     }else{
                        //         $.each(val.filePaths, function (index, file) {
                        //             infoStr +='<div class="img"><img src="' + file.url + '"></div>' ;
                        //         });
                        //     }
                        // }

                        if (val.filePaths.length > 0) {
                            $.each(val.filePaths, function (k, v) {
                                if (v.path != '') {
                                    infoStr += '<div class="img"><img src="' + v.url + '"></div>';
                                } else {
                                    infoStr += '';
                                }
                            })
                        }
                        infoStr += '</div>' +
                            '</div>';
                        $('#logisticsDetails').append(infoStr);


                    })

                } else {
                    //todo 弹出错误提示
                    bootbox.alert(data.msg);

                }
            }
        })
    }
})

