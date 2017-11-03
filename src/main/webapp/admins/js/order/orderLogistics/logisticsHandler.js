/**
 * 添加物流信息
 */
$(function () {
    var orderId = $('#order').val();
    var logisticsId = $('#logisticsId').val();

    //清空列表
    $('.logistics_l .logistics_body').html('');
    //默认选中第一个
    $('#select option').eq(0).attr('selected', true);


    //查询第一个数据
    var NoOne = $('#select option').eq(0).html();
    $('#containNo').val(NoOne);
    getLogisticsAndShow(NoOne);
    //改变货柜
    $('#select').on('change', function () {
        var selectContain = $('#select option:selected').html();
        $('#containNo').val(selectContain);
        $('.logistics_l .logistics_body').html('');
        getLogisticsAndShow(selectContain);
    })

    //鼠标点击获取大图
    $('.logistics_body').on('click', '.img', function () {
        var src = $(this).find('img').attr('src');
        $('.mask').css('display', 'block');
        $('#mask').css('display', 'block');
        $('#maskImg').find('img').attr('src', src);
    });

    //点击关闭--关闭大图
    $('.mask').on('click', '#maskImg', function () {
        $('.mask').css('display', 'none');
    });



    //添加物流信息----打开弹出框
    $('#addDails').click(function () {
        $('.driver').remove();

        $('#textarea').val('');
        $('#showImg').attr('src', '/admins/assets/img/img_1_r2_c2.gif');
        $('#showImg2').attr('src', '/admins/assets/img/img_1_r2_c2.gif');
        $('.mask').css('display', 'block');
        $('#addLogisticsInfo').css('display', 'block');
        $('#mask').css('display', 'none');
        $('#imgPicker>div:nth-of-type(2)').css({
            'width': '170px',
            'height': '120px',
        })
        $('#imgPicker2>div:nth-of-type(2)').css({
            'width': '170px',
            'height': '120px',
        })
        // $('input[name="tong"]')[1].checked=true;
        if( $('input[name="receiving"]').length!=0){
            $('input[name="receiving"]')[1].checked=true;
        }
        if($('input[name="tong"]').length!=0){
            $('input[name="tong"]')[1].checked=true;
        }
    });



    //确认收货--按钮点击事件
    $('.mask').on('click', '#btnSign', function () {
        path = null;
        var containerNo = $('#containNo').val();
        var logisticsId = $('#logisticsId').val();
        var detailInfo = $('#textarea').val();
        var receivingTime;
        if ($('input[name="receiving"]')[0].checked) {
            receivingTime = 1;
        } else {
            receivingTime = 0;
        }
        var contractUrl = myContractUrl();
        var voucherUrl = myVoucherUrl();
        var path = [];
        // path.push(contractUrl);
        // path.push(voucherUrl);
        if ($('#showImg').attr('src') == '/admins/assets/img/img_1_r2_c2.gif' && $('#showImg2').attr('src') == '/admins/assets/img/img_1_r2_c2.gif') {
            path = [null, null]
        } else if ($('#showImg').attr('src') != '/admins/assets/img/img_1_r2_c2.gif' && $('#showImg2').attr('src') == '/admins/assets/img/img_1_r2_c2.gif') {
            path = [contractUrl, null]
        } else if ($('#showImg').attr('src') == '/admins/assets/img/img_1_r2_c2.gif' && $('#showImg2').attr('src') != '/admins/assets/img/img_1_r2_c2.gif') {
            path = [null, voucherUrl]
        } else {
            path.push(contractUrl);
            path.push(voucherUrl);
        }
        var data = {
            detailInfo: detailInfo,
            logisticsId: logisticsId,
            orderNo: orderId,
            containerNo: containerNo,
            receiveFlag: receivingTime,
            filePaths: path
        };
        data = JSON.stringify(data);

        $.ajax({
            url: '/admin/order/logistics/add',
            data: data,
            type: 'post',
            success: function (data) {
                if (data.code == 200) {
                    if (receivingTime == 0) {
                        bootbox.alert('添加成功');
                    } else {
                        bootbox.alert('确认收货');
                    }
                    $('.mask').css('display', 'none');
                    $('#addLogisticsInfo').css('display', 'none');
                    getLogisticsAndShow(containerNo);
                } else {
                    bootbox.alert(data.msg);
                    location.href = '/admin/order/list'
                }
            }
        })

    });

    //确认通关
    $('.mask').on('click', '#btnAdd', function () {
        var containerNo = $('#containNo').val();
        var orderNo = orderId;
        var logisticsId = $('#logisticsId').val();
        var detailInfo = $('#textarea').val();
        var preReceiveTime = '';
        var driverMobile;
        if ($('input[name="tong"]')[0].checked) {
            if ($('#driverNo').val() == '') {
                $('#driverNo').focus();
                bootbox.alert('请输入手机号');
                return;
            }
            preReceiveTime = $('#preReceiveTime').val();
            driverMobile = $('#driverNo').val();
        } else {
            $('#preReceiveTime').attr('disabled', true)
        }
        ;
        var contractUrl = myContractUrl();
        var voucherUrl = myVoucherUrl();
        console.log(contractUrl);
        var path = [];
        if ($('#showImg').attr('src') == '/admins/assets/img/img_1_r2_c2.gif' && $('#showImg2').attr('src') == '/admins/assets/img/img_1_r2_c2.gif') {
            path = [null, null]
        } else if ($('#showImg').attr('src') != '/admins/assets/img/img_1_r2_c2.gif' && $('#showImg2').attr('src') == '/admins/assets/img/img_1_r2_c2.gif') {
            path = [contractUrl, null]
        } else if ($('#showImg').attr('src') == '/admins/assets/img/img_1_r2_c2.gif' && $('#showImg2').attr('src') != '/admins/assets/img/img_1_r2_c2.gif') {
            path = [null, voucherUrl]
        } else {
            path.push(contractUrl);
            path.push(voucherUrl);
        }
        var data = {
            preReceiveTime: preReceiveTime,
            detailInfo: detailInfo,
            logisticsId: logisticsId,
            orderNo: orderId,
            containerNo: containerNo,
            filePaths: path,
            driverMobile: driverMobile
        };

        data = JSON.stringify(data);
        $.ajax({
            url: '/admin/order/logistics/add',
            data: data,
            type: 'post',
            success: function (data) {
                if (data.code == 200) {
                    $('#textarea')[0].value='';
                    bootbox.alert('添加成功');
                    $('.mask').css('display','none');
                    $('#addLogisticsInfo').css('display','none');
                    getLogisticsAndShow(containerNo);
                }else{
                    bootbox.alert(data.msg);
                }
            },
        })
    });

    //添加物流 ---弹窗关闭
    $('.mask').on('click', '#btnCanLogi', function () {
        $('.mask').css('display', 'none');
        $('#addLogisticsInfo').css('display', 'none');
    });

    $('.xuan .icon-ditu').on('click', function () {
        var containerid = $('#select').val();
        console.log("click");
        logistics(containerid);
    });

    /**
     * 获取总的物流信息
     * @param id
     */
    function getLogisticsAndShow(id) {
        $.ajax({
            url: '/admin/order/logistics_detail_ajax',
            type: 'post',
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
                    if (data.data.containerStatus == 12) {
                        $('#addDails').attr('disabled', 'disabled');
                        $('#addDails').css('background', '#ccc');
                        $('#addDails').css('color', '#666');
                    }else{
                        $('#addDails').removeAttr('disabled');
                        $('#addDails').css('background', '#00aa5c');
                        $('#addDails').css('color', '#fff');
                    }
                    var list = data.data.logisticsDetails;
                    //清空列表
                    $('.logistics_l .logistics_body').html('');

                    $.each(list, function (k, v) {
                        var time = v.addTime;
                        var time1 = moment(time).format('YYYY-MM-DD, HH:mm:ss');
                        var filePathsOne = v.filePaths[0].url;
                        var filePathsTwo = v.filePaths[1].url;
                        if (v.filePaths[0].path == '' && v.filePaths[1].path == '') {
                            var tag = ' <div class="logistics_1 clearfix">' +
                                '<span> ' + time1 + '  </span>' +
                                '<div>' + v.detailInfo + '</div>' +
                                '<div>' +
                                '</div>' +
                                '</div>';
                            $('.logistics_l .logistics_body').append(tag);
                        } else if (v.filePaths[0].path == '' && v.filePaths[1].path != '') {
                            var tag = ' <div class="logistics_1 clearfix">' +
                                '<span> ' + time1 + '  </span>' +
                                '<div>' + v.detailInfo + '</div>' +
                                '<div>' +
                                '<div class="img"><img src="' + v.filePaths[1].url + '"></div>' +
                                '</div>' +
                                '</div>';
                            $('.logistics_l .logistics_body').append(tag);
                        } else if (v.filePaths[0].path != '' && v.filePaths[1].path == '') {
                            var tag = ' <div class="logistics_1 clearfix">' +
                                '<span> ' + time1 + '  </span>' +
                                '<div>' + v.detailInfo + '</div>' +
                                '<div>' +
                                '<div class="img"><img src="' + v.filePaths[0].url + '"></div>' +
                                '</div>' +
                                '</div>';
                            $('.logistics_l .logistics_body').append(tag);
                        } else {
                            var tag = ' <div class="logistics_1 clearfix">' +
                                '<span> ' + time1 + '  </span>' +
                                '<div>' + v.detailInfo + '</div>' +
                                '<div>' +
                                '<div class="img"><img src="' + v.filePaths[0].url + '"></div>' +
                                '<div class="img"><img src="' + v.filePaths[1].url + '"></div>' +
                                '</div>' +
                                '</div>';
                            $('.logistics_l .logistics_body').append(tag);
                        }

                    })
                    $('.logistics_1').eq(0).css('color', '#00aa5c');
                    if (data.data.preReceiveTime !== null) {
                        $('.button').attr('id', 'btnSign');
                        $('.shou').html('');
                        var tag = '<div class="containers shou">' +
                            '<span>是否已签收：</span>' +
                            '<input type="radio" name="receiving">是' +
                            '<input type="radio" checked name="receiving">否' +
                            '</div>';
                        $('.logistics_l .preReceive').before(tag);
                        $('#preReceiveTime').css('display', 'none');
                        $('.preReceive').css('display', 'none');
                    }
                    else if (data.data.containerStatus == 5) {
                        $('.button').attr('id', 'btnAdd');
                        $('#addDails')[0].disabled = false;
                        $('#addDails').css('background', '#00aa5c');
                        $('#addDails').css('color', '#fff');
                        //是否通关
                        $('.shou').html('');
                        var tag = '<div class="containers shou">' +
                            '<span>是否已通关：</span>' +
                            '<input type="radio" name="tong">是' +
                            '<input type="radio" checked name="tong">否' +
                            '</div>'
                        $('.logistics_l .preReceive').before(tag);
                        $('.preReceive').css('display', 'block');
                        $('#preReceiveTime').css('display', 'inline-block');
                        $('input[name="tong"]').unbind('change');
                        $('input[name="tong"]').on('change', function () {
                            if ($('input[name="tong"]')[1].checked) {
                                $('#preReceiveTime').attr('disabled', true);
                            } else {
                                $('#preReceiveTime').attr('disabled', false);
                                var tag = '<div class="containers driver" style="">' +
                                    '<span>老司机手机号：</span>' +
                                    '<input id="driverNo" type="text" style="display: inline-block;margin-left: 15px">' +
                                    '</div>';
                                $('.preReceive').before(tag);
                            }
                        })

                    } else if (data.data.containerStatus == 6) {
                        $('.button').attr('id', 'btnAdd');
                        $('#addDails')[0].disabled = false;
                        $('#addDails').css('background', '#00aa5c');
                        $('#addDails').css('color', '#fff');
                        //是否通关
                        $('.shou').html('');
                        var tag = '<div class="containers shou">' +
                            '<span>是否已通关：</span>' +
                            '<input type="radio" name="tong">是' +
                            '<input type="radio" checked name="tong">否' +
                            '</div>'
                        $('.logistics_l .preReceive').before(tag);
                        $('.preReceive').css('display', 'block');
                        $('#preReceiveTime').css('display', 'inline-block');
                        $('input[name="tong"]').unbind('change');
                        $('input[name="tong"]').on('change', function () {
                            if ($('input[name="tong"]')[1].checked) {
                                $('#preReceiveTime').attr('disabled', true);
                            } else {
                                $('#preReceiveTime').attr('disabled', false);
                                var tag = '<div class="containers driver" style="">' +
                                    '<span>老司机手机号：</span>' +
                                    '<input id="driverNo" type="text" style="display: inline-block;margin-left: 15px">' +
                                    '</div>';
                                $('.preReceive').before(tag);
                            }
                        })

                    }
                    else if (data.data.containerStatus == 11) {
                        $('.button').attr('id', 'btnSign');
                        $('#addDails').removeAttr('disabled');
                        $('#addDails').css('background', '#00aa5c');
                        $('#addDails')[0].disabled = false;
                        $('#addDails').css('background', '#00aa5c');
                        $('#addDails').css('color', '#fff');
                        $('.shou').html('');
                        var tag = '<div class="containers shou">' +
                            '<span>是否已签收：</span>' +
                            '<input type="radio" name="receiving">是' +
                            '<input type="radio" checked name="receiving">否' +
                            '</div>';
                        $('.logistics_l .preReceive').before(tag);
                        $('#preReceiveTime').css('display', 'none');
                        $('.preReceive').css('display', 'none');

                    }
                } else {
                    //todo 弹出错误提示
                }
            }
        })
    }


    //阻止冒泡
    function stopPropagation(event) {
        if (event.stopPropagation) {
            event.stopPropagation();
        } else {
            event.cancelBubble = true;
        }
    }


})

