var addNum3;
var clo_tab = ($('#pro_list div:nth-of-type(1)').clone())[0].outerHTML;
$(function () {

    var restrictSum;
    var logicsId;//物流id1
    var orderNo = $('#order').html();

    // $('#pro_list').remove();
    var orderId = $('#order').html();
    $.ajax({
        url: '/admin/order/last_order_ajax',
        data: {
            orderId: orderId
        },
        success: function (data) {
                // 如果成功
                if (data.code == 200) {
                    var list = data.data;
                    var lasttime = list.lastTime == undefined ? '' : list.lastTime;
                    var lastOrderId = list.lastOrderId == undefined ? '' : list.lastOrderId;
                    var tag = ' <div><span class="audit_h">订单号：</span>' + list.orderId + '</div>' +
                        '<div><span class="audit_h">下单日期：</span>' + list.placeOrderTime + '</div>' +
                        '<div><span class="audit_h">采购商：</span>' + list.purchaserName + '</div>' +
                        '<div><span class="audit_h">账号：</span>' + list.userId + '</div>' +
                        '<div><span class="audit_h">联系人：</span>' + list.contactName + '</div>' +
                        '<div><span class="audit_h">联系电话：</span>' + list.contactMobile + '</div>' +
                        '<div><span class="audit_h">历史成交：</span>' + list.successCount + '单</div>' +
                        '<div><span class="audit_h">最近交易时间：</span>' + lasttime + ' </div>' +
                        '<div><span class="audit_h">最近交易订单号：</span> ' + lastOrderId + '</div>' +
                        '<div><span class="audit_h">订单状态：</span>' + list.orderStatusDesc + '</div>';
                    $('.audit_info .audit_btn').before(tag)
                }

        }
    });


    //清空资金服务
    var loanLists = $('.loan-table').find('.tab-tr');
    $.each(loanLists, function (k, v) {
        v.outerHTML = ''
    })
    $('#orderOne').addClass('clickStep');
    $('#orderOne').css('color', '#fff');
    $('#oneStep').css('display', 'block');
    $('#twoStep').css('display', 'none');

    $('#orderTwo').click(function () {

        $('#advance').removeAttr('disabled');
        $('#restPay').removeAttr('disabled');
        //清空所有input选项
        $('input[type="radio"]').parents('span').removeClass('checked');

        //清空资金服务
        var loanLists = $('.loan-table').find('.tab-tr');
        $.each(loanLists, function (k, v) {
            v.outerHTML = ''
        });
        //清空费用清单
        $('.feeList').remove();
        $('#orderOne').removeClass('clickStep');
        $('#orderOne').css('color', '#00aa5c');
        $(this).css('color', '#fff');
        $(this).addClass('clickStep');
        $('#twoStep').css('display', 'block');
        $('#oneStep').css('display', 'none');
        $.ajax({
            url: '/admin/order/logistics/service/query',
            data: {
                orderId: orderNo
            },
            success: function (data) {
                // if (data.data.status == 6) {
                //     $('#next_step').attr('disabled', true)
                // }
                logicsId = data.data.id;
                var type = $('#order').attr('type');

                if (type == 1) {
                    $('.contract_1').eq(1).css('display', 'none');
                    $('.logistics_2').eq(0).find('span').addClass('checked');
                    $('.logistics_1').eq(1).css('display', 'none');
                    $('.logistics_1').eq(2).css('display', 'none');
                    var backMemo =data.data.backMemo;
                    var clearanceCompany = data.data.clearanceCompany;
                    var outerExpress = data.data.outerExpress;
                    var innerExpress = data.data.innerExpress;
                    var contractUrl = data.data.contractUrl;
                    var logisticsType = data.data.logisticsType;
                    var advance = data.data.advance;
                    var restPay = data.data.restPay;
                    var tradeType = data.data.tradeType;
                    var payType = data.data.payType;
                    var feeList = data.data.feeList;
                    var needLoan = data.data.needLoan;
                    var selectAdd = data.data.deliveryAddress;
                    var data = data.data.deliveryAddressList;

                    $('#advance').val(advance);
                    $('#restPay').val(restPay);

                    var idDIvs = '<div id="idDIvs" clearanceCompany="' + clearanceCompany.id + '" outerExpress="' + outerExpress.id + '" innerExpress="' + innerExpress.id + '"></div>'
                    $('.main').append(idDIvs);
                    //物流方式
                    if (logisticsType == 1) {
                        $('input[name="logistics"]').eq(0).parents('span').addClass('checked')
                    } else {
                        $('input[name="logistics"]').eq(1).parents('span').addClass('checked');
                        $('input[name="logistics"]').eq(0).parents('span').removeClass('checked');
                        $('.trade').css('display', 'none')
                    }

                    $('#select').html('');

                    $.each(data, function (k, v) {

                        var tag = '<option receiverId="' + v.id + '">' + v.receiver + '</option>';
                        $('#select').append(tag);
                    })

                    $('#select').change(function () {
                        $.each(data, function (k, v) {
                            if (v.receiver == $('#select option:selected').html()) {
                                deliveryId = v.id;
                                var tag = '  <li>' +
                                    '<span>收件人：</span>' + data[k].receiver +
                                    '</li>' +
                                    '<li>' +
                                    '<span>所在地区：</span>' +
                                    '<div class="country">' + data[k].countryName + '</div>' +
                                    '<div class="province">' + data[k].provinceName + '</div>' +
                                    '<div class="xian">' + data[k].districtName + '</div>' +
                                    '</li>' +
                                    '<li>' +
                                    '<span>详细地址：</span>' + data[k].address +
                                    '</li>' +
                                    '<li>' +
                                    '<span>邮政编码：</span>' + data[k].zipCode +
                                    '</li>' +
                                    '<li>' +
                                    '<span>手机：</span>' + data[k].cellPhone +
                                    '</li>' +
                                    '<li>' +
                                    '<span>固定电话：</span>' + data[k].phoneNum +
                                    '</li>';
                                $('#select_list').html(tag);
                                $('#select_list').css('display', 'block')

                            }
                            else if ($('#select option:selected').html() == '请选择') {
                                $('#select_list').html('');
                                $('#select_list').css('display', 'none')
                            }
                        })
                    });
                    console.log(selectAdd.receiver);
                    // $.each(data, function (k, v) {
                    //     if (v.receiver == selectAdd.receiver) {
                    //         $('#select option').eq(k + 1)[0].selected = true;
                    //
                    //
                    //     }
                    // })
                    var tag = '  <li>' +
                        '<span>收件人：</span>' + selectAdd.receiver +
                        '</li>' +
                        '<li>' +
                        '<span>所在地区：</span>' +
                        '<div class="country">' + selectAdd.countryName + '</div>' +
                        '<div class="province">' + selectAdd.provinceName + '</div>' +
                        '<div class="xian">' + selectAdd.districtName + '</div>' +
                        '</li>' +
                        '<li>' +
                        '<span>详细地址：</span>' + selectAdd.address +
                        '</li>' +
                        '<li>' +
                        '<span>邮政编码：</span>' + selectAdd.zipCode +
                        '</li>' +
                        '<li>' +
                        '<span>手机：</span>' + selectAdd.cellPhone +
                        '</li>' +
                        '<li>' +
                        '<span>固定电话：</span>' + selectAdd.phoneNum +
                        '</li>';
                    $('#select_list').html(tag);
                    $('#select_list').css('display', 'block')

                    if (needLoan == null || needLoan == 0) {
                        $('.loan-table').css('display','none');
                        $('.attention').css('display','none');
                        $('.need-loan label:nth-of-type(2)').find('span').addClass('checked')
                    } else {
                        $('.need-loan label:nth-of-type(1)').find('span').addClass('checked');

                        $('.loan-table').css('display', 'block');
                        $('.attention').css('display', 'block');
                        $('.loan-table').find('.tab-tr').remove();
                        $.ajax({
                            url: '/admin/order/container_detail_ajax',
                            data: {
                                orderId: orderNo
                            },
                            success: function (data) {

                                var data = data.data;
                                $('.tab-tr').remove()
                                $.each(data, function (k, v) {
                                    var tag = '<tr class="tab-tr">' +
                                        '<td transactionNo="' + v.transactionNo + '">' + v.containerId + '</td>' +
                                        '<td  productId="' + v.productId + '">' + v.productName + '</td>' +
                                        '<td class="loanQuota"><span>' + v.loanQuota + '</span> &nbsp;元</td>' +
                                        '<td>' +
                                        '<input value="' + v.applyQuota + '" type="text" class="loanMoney fl">' +
                                        '<span class="fl">元</span>' +
                                        '</td>' +
                                        '<td> <input class="confirmMoney" type="text" value="' + v.confirmLoan + '">&nbsp;元</td>' +
                                        '<td class="sumMOney">' +
                                        '<input value="' + v.serviceFee + '" disabled type="text" class="fl serviceFee">' +
                                        '<span class="fl">元</span>' +
                                        '</td>' +
                                        '</tr>';
                                    $('.loan-table tbody').append(tag)
                                })
                                matchingLoan()
                            }
                        })


                    }
                    ;
                    var feeList_1 = $('.feeList');
                    $.each(feeList_1, function (k, v) {
                        v.outerHTML = ''
                    })


                    //清关公司
                    $('.customsClearance_2 span:nth-of-type(2)').html(clearanceCompany.name)

                    $.each(feeList, function (k, v) {

                        var tag1 = '<tr class="feeList">' +
                            '<td id="' + v.id + '">' + v.batchNumber + '</td>' +
                            '<td> ' + v.productAmount + '元</td>' +
                            '<td><input  value="' + v.agencyAmount + '" type="number"><span>元</span></td>' +
                            '<td><input value="' + v.premiumAmount + '" type="number"><span>元</span></td>' +
                            '</tr>';
                        $('#money_list table').append(tag1)
                    })
                    if (payType == 1) {
                        $('input[name="settlement"]').eq(0).parents('span').addClass('checked')
                    } else if (payType == 2) {
                        $('input[name="settlement"]').eq(1).parents('span').addClass('checked')
                    } else {
                        $('input[name="settlement"]').eq(2).parents('span').addClass('checked')
                    }

                    if (tradeType == 1) {
                        $('input[name="trade"]').eq(0).parents('span').addClass('checked')
                    } else {
                        $('input[name="trade"]').eq(1).parents('span').addClass('checked')
                    }

                    //加载合同
                    if(contractUrl){
                        $('#showImg').attr('src', contractUrl);
                    }

                    $('#imgPicker>div:nth-of-type(2)').css({
                        'width': '170px',
                        'height': '120px',
                    })
                    $('#imgPicker2>div:nth-of-type(2)').css({
                        'width': '170px',
                        'height': '120px',
                    })

                    //加载备注
                    $('#note').val(backMemo);

                } else {


                    $('.logistics_2').eq(0).find('span').addClass('checked');
                    var backMemo =data.data.backMemo;
                    var clearanceCompany = data.data.clearanceCompany;
                    var outerExpress = data.data.outerExpress;
                    var innerExpress = data.data.innerExpress
                    var contractUrl = data.data.contractUrl;
                    var voucherUrl = data.data.voucherUrl;
                    var advance = data.data.advance;
                    var restPay = data.data.restPay;
                    var tradeType = data.data.tradeType;
                    var payType = data.data.payType;
                    var feeList = data.data.feeList;
                    var needLoan = data.data.needLoan;
                    var selectAdd = data.data.deliveryAddress;
                    var data = data.data.deliveryAddressList;


                    //清关公司
                    $('.customsClearance_2 span:nth-of-type(2)').html(clearanceCompany.name);
                    $('.customsClearance_2 span:nth-of-type(2)').attr('id', clearanceCompany.id);

                    //物流公司
                    $('.international-logistic').eq(0).html(outerExpress.name)
                    $('.international-logistic').eq(0).attr('id', outerExpress.id)
                    $('.international-logistic').eq(1).html(innerExpress.name);
                    $('.international-logistic').eq(1).attr('id', innerExpress.id)

                    $('#select').html('');

                    $.each(data, function (k, v) {
                        var tag = '<option receiverId="' + v.id + '">' + v.receiver + '</option>';
                        $('#select').append(tag);
                    })

                    $('#select').change(function () {
                        $.each(data, function (k, v) {
                            if (v.receiver == $('#select option:selected').html()) {
                                deliveryId = v.id;
                                var tag = '  <li>' +
                                    '<span>收件人：</span>' + data[k].receiver +
                                    '</li>' +
                                    '<li>' +
                                    '<span>所在地区：</span>' +
                                    '<div class="country">' + data[k].countryName + '</div>' +
                                    '<div class="province">' + data[k].provinceName + '</div>' +
                                    '<div class="xian">' + data[k].districtName + '</div>' +
                                    '</li>' +
                                    '<li>' +
                                    '<span>详细地址：</span>' + data[k].address +
                                    '</li>' +
                                    '<li>' +
                                    '<span>邮政编码：</span>' + data[k].zipCode +
                                    '</li>' +
                                    '<li>' +
                                    '<span>手机：</span>' + data[k].cellPhone +
                                    '</li>' +
                                    '<li>' +
                                    '<span>固定电话：</span>' + data[k].phoneNum +
                                    '</li>';
                                $('#select_list').html(tag);
                                $('#select_list').css('display', 'block')

                            }
                        })
                    });

                    var tag = '  <li>' +
                        '<span>收件人：</span>' + selectAdd.receiver +
                        '</li>' +
                        '<li>' +
                        '<span>所在地区：</span>' +
                        '<div class="country">' + selectAdd.countryName + '</div>' +
                        '<div class="province">' + selectAdd.provinceName + '</div>' +
                        '<div class="xian">' + selectAdd.districtName + '</div>' +
                        '</li>' +
                        '<li>' +
                        '<span>详细地址：</span>' + selectAdd.address +
                        '</li>' +
                        '<li>' +
                        '<span>邮政编码：</span>' + selectAdd.zipCode +
                        '</li>' +
                        '<li>' +
                        '<span>手机：</span>' + selectAdd.cellPhone +
                        '</li>' +
                        '<li>' +
                        '<span>固定电话：</span>' + selectAdd.phoneNum +
                        '</li>';
                    $('#select_list').html(tag);
                    $('#select_list').css('display', 'block')

                    if (needLoan == null || needLoan == 0) {
                        $('.loan-table').css('display','none');
                        $('.attention').css('display','none');
                        $('.need-loan label:nth-of-type(2)').find('span').addClass('checked')
                    } else {
                        $('.need-loan label:nth-of-type(1)').find('span').addClass('checked');

                        $('.need-loan label:nth-of-type(1)').find('span').addClass('checked');

                        $('.loan-table').css('display', 'block');
                        $('.attention').css('display', 'block');
                        $('.loan-table').find('.tab-tr').remove();
                        $.ajax({
                            url: '/admin/order/container_detail_ajax',
                            data: {
                                orderId: orderNo
                            },
                            success: function (data) {

                                var data = data.data;
                                $('.tab-tr').remove();
                                $.each(data, function (k, v) {
                                    //
                                    var tag = '<tr class="tab-tr">' +
                                        '<td transactionNo="' + v.transactionNo + '">' + v.containerId + '</td>' +
                                        '<td  productId="' + v.productId + '">' + v.productName + '</td>' +
                                        '<td class="loanQuota"><span>' + v.loanQuota + '</span> &nbsp;元</td>' +
                                        '<td>' +
                                        '<input value="' + v.applyQuota + '" type="text" class="loanMoney fl">' +
                                        '<span class="fl">元</span>' +
                                        '</td>' +
                                        '<td> <input class="confirmMoney" type="text" value="' + v.confirmLoan + '">&nbsp;元</td>' +
                                        '<td class="sumMOney">' +
                                        '<input value="' + v.serviceFee + '" disabled type="text" class="fl serviceFee">' +
                                        '<span class="fl">元</span>' +
                                        '</td>' +
                                        '</tr>';
                                    $('.loan-table tbody').append(tag)
                                })
                                matchingLoan()
                            }
                        })


                    }
                    ;

                    //清关公司
                    $('.customsClearance_2 span:nth-of-type(2)').html(clearanceCompany.name);
                    $('.customsClearance_2 span:nth-of-type(2)').attr('id', clearanceCompany.id);

                    //物流公司
                    $('.international-logistic').eq(0).html(outerExpress.name)
                    $('.international-logistic').eq(0).attr('id', outerExpress.id)
                    $('.international-logistic').eq(1).html(innerExpress.name);
                    $('.international-logistic').eq(1).attr('id', innerExpress.id)


                    //添加合同
                    if (contractUrl == '' && voucherUrl !== '') {
                        $('#showImg2').attr('src', voucherUrl);
                    } else if (voucherUrl == '' && contractUrl !== '') {
                        $('#showImg').attr('src', contractUrl);
                    } else if (contractUrl !== '' && voucherUrl !== '') {
                        $('#showImg').attr('src', contractUrl);
                        $('#showImg2').attr('src', voucherUrl);
                    }

                    //加载合同
                    $('#imgPicker>div:nth-of-type(2)').css({
                        'width': '170px',
                        'height': '120px',
                    })
                    $('#imgPicker2>div:nth-of-type(2)').css({
                        'width': '170px',
                        'height': '120px',
                    })

                    $.each(feeList, function (k, v) {
                        var tag1 = '<tr class="feeList">' +
                            '<td id="' + v.id + '">' + v.batchNumber + '</td>' +
                            '<td> ' + v.productAmount + '元</td>' +
                            '<td><input value="' + v.agencyAmount + '" type="number"><span>元</span></td>' +
                            '<td><input value="' + v.premiumAmount + '" type="number"><span>元</span></td>' +
                            '</tr>';
                        $('#money_list table').append(tag1)
                    })
                    if (payType == 1) {
                        $('input[name="settlement"]').eq(0).parents('span').addClass('checked')
                    } else if (payType == 2) {
                        $('input[name="settlement"]').eq(1).parents('span').addClass('checked')
                    } else {
                        $('input[name="settlement"]').eq(2).parents('span').addClass('checked')
                    }

                    if (tradeType == 1) {
                        $('input[name="trade"]').eq(0).parents('span').addClass('checked')
                    } else {
                        $('input[name="trade"]').eq(1).parents('span').addClass('checked')
                    }

                    $('#settlement_1 #advance').val(advance);
                    $('#settlement_2 #restPay').val(restPay);


                    //加载备注
                    $('#note').val(backMemo);
                }
            }
        })
    });
    //     //选中陆运后隐藏贸易方式
    var logi = $('.logistics_2')
    $('input[name="logistics"]').on('change', function () {
        if ($('input[name="logistics"]').eq(0).parents('span').hasClass('checked')) {
            $('.trade').css('display', 'block')
        } else {
            $('.trade').css('display', 'none')
        }
    })

    //实时计算服务费
    $('.main').on('input propertychange', '.confirmMoney', function () {
        var loanQuotaMoney =parseInt($(this).parents('td').siblings('.loanQuota').find('span').html());
        var confirmLoan = $(this).val();
        var serviceFee = parseInt(confirmLoan) * 0.001;
        $(this).parents('td').siblings('.sumMOney').find('input').val(serviceFee)
    });
    //申请何确认金额不能超过可贷款金额

    $('.main').on('blur','.loanMoney', function () {
        //判断贷款额度不能超过80%
        var loanQuotaMoney =$(this).parents('td').siblings('.loanQuota').find('span').html();
        console.log(loanQuotaMoney);
        if(parseInt($(this).val())>parseInt(loanQuotaMoney)){
            bootbox.alert('该申请金额超过可贷款金额');
            $(this).val('0');
            $(this).parents('td').siblings('.sumMOney').find('input').val('')
            return
        }
    })
    $('#twoStep').on('blur','.confirmMoney', function () {
        var confirmLoan = $(this).val();
        var loanQuotaMoney =$(this).parents('td').siblings('.loanQuota').find('span').html();
        if(parseInt($(this).val())>parseInt(loanQuotaMoney)){
            bootbox.alert('该确认金额超过可贷款金额');
            $(this).val('0');
            $(this).parents('td').siblings('.sumMOney').find('input').val('')
            return
        }
    });
    var i = 1;
    $('#orderOne').click(function () {
        $('#orderTwo').removeClass('clickStep');
        $('#orderTwo').css('color', '#00aa5c');
        $(this).css('color', '#fff');
        $(this).addClass('clickStep');
        $('#oneStep').css('display', 'block');
        $('#twoStep').css('display', 'none');

        //清空表单
        var tableList = $('.pro-list-1');
        var addLists = $('.ad');

        for (var i = 0; i < tableList.length; i++) {
            tableList[i].outerHTML = ''
        }
        for(var j =0;j<addLists.length;j++){
            addLists[j].outerHTML =''
        }
        $.ajax({
            url: '/admin/order/tab_order_ajax',
            data: {
                orderId: orderNo
            },
            success: function (data) {

                if (data.data.status == 4) {
                    $('#next_1').attr('disabled', true)
                }
                var suppier = data.data.supplierList;
                var data = data.data;
                status = data.status;
                var dataType = data.type;
                dataType2 = data.type;

                //判断选择的type
                if (data.type == 1) {
                    $('#agency').parents('span').addClass('checked')
                    $('#direct').attr('disabled', 'disabled');
                    var tag = '<option supplierid=1 selected disabled>浙江创意生物科技股份有限公司</option>';
                    $('.supplier-2 select').html(tag);
                    $('.container-add-3').css('display', 'none');
                } else {
                    $('#direct').parents('span').addClass('checked');
                    $('#agency').attr('disabled', 'disabled');

                    $('.container-add-3').css('display', 'none');
                    var id = data.supplierId;
                    $.each(suppier, function (k, v) {

                        var tag = '<option supplierid="' + v.id + '">' + v.supplierContact + '</option>';
                        $('.supplier-2 select').append(tag);
                    })

                    $('.selsct').change(function () {
                        $.each(suppier, function (k, v) {
                            if (v.supplierContact == $('.selsct option:selected').html()) {
                                // deliveryId = v.id;
                                var tag = '  <li>' +
                                    '<span>收件人：</span>' + v.supplierContact +
                                    '</li>' +
                                    '<li>' +
                                    '<span>所在地区：</span>' +
                                    '<div class="country">' + v.countryName + '</div>' +
                                    '<div class="province">' + v.provinceName + '</div>' +
                                    '<div class="xian">' + v.districtName + '</div>' +
                                    '</li>' +
                                    '<li>' +
                                    '<span>详细地址：</span>' + v.address +
                                    '</li>' +
                                    '<li>' +
                                    '<span>邮政编码：</span>' + v.zipCode +
                                    '</li>' +
                                    '<li>' +
                                    '<span>手机：</span>' + v.cellPhone +
                                    '</li>' +
                                    '<li>' +
                                    '<span>固定电话：</span>' + v.phoneNum +
                                    '</li>';
                                $('.supplier').eq(0).find('ul').html(tag);
                                $('.supplier').eq(0).find('ul').css('display', 'block')

                            }
                        })
                    });

                    $.each(suppier, function (k, v) {
                        if (v.id == id) {

                            $('.selsct option').eq(k + 1).attr('selected', true)
                            $('.selsct option').eq(0).attr('selected', false)
                            var tag = '  <li>' +
                                '<span>收件人：</span>' + v.supplierContact +
                                '</li>' +
                                '<li>' +
                                '<span>所在地区：</span>' +
                                '<div class="country">' + v.countryName + '</div>' +
                                '<div class="province">' + v.provinceName + '</div>' +
                                '<div class="xian">' + v.districtName + '</div>' +
                                '</li>' +
                                '<li>' +
                                '<span>详细地址：</span>' + v.address +
                                '</li>' +
                                '<li>' +
                                '<span>邮政编码：</span>' + v.zipCode +
                                '</li>' +
                                '<li>' +
                                '<span>手机：</span>' + v.cellPhone +
                                '</li>' +
                                '<li>' +
                                '<span>固定电话：</span>' + v.phoneNum +
                                '</li>';
                            $('.supplier').eq(0).find('ul').html(tag);
                            $('.supplier').eq(0).find('ul').css('display', 'block')

                        }
                    })
                }

                //添加数据
                var list = data.orderContainers;
                var thisdivId = 1;
                createOrderDivs('orderList', list, thisdivId);
                var productName, rank, size, type_pro;
                addIdNum = thisdivId;
                var i =1;
                $.each(list, function (k, v) {
                    var parameter = v.orderContainerDetails;
                    //这里

                    productName=v.productName;
                    two(productName,i,parameter);
                    i++;
                    matching();

                    $('.main').on('click', '#orderList  #tr' + thisdivId, function () {
                        var a = ($(this).siblings('table').find('tr:nth-of-type(2)').clone())[0].innerHTML;
                        var first_n = $(this).siblings('table').find('.first-td')[0].value;

                        var tr = document.createElement('tr');
                        tr.innerHTML = a;
                        $(this).siblings('table').append(tr);
                        $(this).siblings('table').find('.first-td:last').val(first_n);
                        $(this).siblings('table').find('tr:last-of-type').find('.amount input').val('0')
                    });
                    // thisdivId++;
                    // addIdNum = thisdivId;
                })
                //添加货柜
                // var addList = '<button class="ad" id="ad_1">新增一个货柜</button>';
                // $('#orderList').append(addList);
                // $('#ad_1').css('display', 'block');
                if (status == 4) {
                    var inputs = $('input[type="7"]')
                }
            }
        })
        $('#next_1').css('display', 'block')

        $('.main').on('click', '.cancel', function () {
            $(this).parents('.x').parents('.pro-list-head').parents('.pro-list-1').remove()
        })

    })

    $('#ad_1').css('display','none');
    // $('.main').on('click', '#ad_1', function () {
    //     //获取创建了几个表单
    //     var tableListNum = $('.pro-list-1').length;
    //
    //     var i = parseInt(tableListNum);
    //
    //     var sums_1 = $('#div' + (i)).children('.pro-list-body').find('.amount').children('input');
    //     var sum = 0;
    //
    //     var num;
    //     for (var w = 0; w < sums_1.length; w++) {
    //         num = Number(sums_1[w].value);
    //         sum += num;
    //
    //     }
    //     ;
    //     var restrictSum = parseInt($('#div' + (i)).find('.container-norms').attr('maxquantity'));
    //
    //     if (sum > restrictSum) {
    //         bootbox.alert('超出货柜规格');
    //         return
    //     }
    //
    //     $('#div' + i).find('.container-norms').attr('totalquantity',sum);
    //
    //
    //     //多次点击后重复添加
    //     if (document.getElementById('type_pro').childElementCount !== 1) {
    //         $('#type_pro').html('');
    //         $('#type_pro option').text('请选择');
    //     }
    //
    //     $('.ad').css('display', 'block');
    //     $('.mask').css('display', 'block')
    //     $('.inform').css('display', 'none');
    //     $('.inform-1').css('display', 'none');
    //     $('#inform2').css('display', 'block');
    //     $('.order-next').css('display', 'block');
    //     $('#next_2').css('display', 'none');
    //
    //     $.ajax({
    //         url: '/admin/order/find_all_goods',
    //         success: function (data) {
    //             var list = data.data;
    //
    //             $.each(list, function (k, v) {
    //                 var tag = '<option>' + v.name + '</option>';
    //                 $('#type_pro').append(tag)
    //             });
    //             $('#num-fruit').attr('disabled','disabled');
    //             $('#type_pro').change(function () {
    //                 $.each(list, function (k, v) {
    //                     console.log(v);
    //                     if (v.name == $('#inform2 #type_pro option:selected').html()) {
    //                         $('#num-fruit')[0].value = list[k].capacitySize + list[k].unit;
    //                         //点击添加货柜
    //                         // list = find_all_goods.data;
    //                         var selected_pro = $('#inform2 #type_pro option:selected').html();
    //
    //
    //                         $('#save_2').unbind("click"); //移除click
    //                         $('#save_2').click(function () {
    //
    //                             //添加隐藏标签
    //                             var tagSumHidden = '<input class="hide_inform" style="display:none" value="">';
    //                             $('#div' + i).find('.pro-list-head').append(tagSumHidden);
    //
    //                             $('.mask').css('display', 'none');
    //                             $('#ad_1').before(clo_tab);
    //                             $('#orderList').find('.pro-list-1:last').attr('id', 'div' + (i + 1));
    //
    //                             var addHead =
    //                                 '<h3>货柜批次号：   </h3>' +
    //                                 '<h3 class="container-name" productid="' + v.id + '" >货柜名称：' + v.name + '</h3>' +
    //                                 '<h3 maxquantity="' + v.capacitySize + '" class="container-norms">货柜规格：0-' + v.capacitySize + '箱</h3>' +
    //                                 '<div class="x">' +
    //                                 '<i class="iconfont icon-chacha cancel cancel-1 fr"></i>' +
    //                                 '</div>';
    //                             $('#div' + (i + 1)).find('.pro-list-head').html(addHead);
    //                             var amountLists = $('#div' + (i + 1)).find('.amount');
    //                             $.each(amountLists, function (k, v) {
    //                                 v.children[0].value = 0;
    //                             })
    //                             var first_input = $('#div' + (i + 1)).find('.first-td');
    //                             $('#div' + (i + 1)).find('.add-h').attr('id', 'tr' + (i + 1));
    //                             restrictSum = v.capacitySize;
    //
    //                             for (var s = 0; s < first_input.length; s++) {
    //                                 first_input[s].setAttribute('productId', v.id);
    //                                 first_input[s].value = v.name;
    //                                 first_input[s].disabled = true;
    //                             }
    //                             ;
    //                             var allGoods = list;
    //                             var rank, size, type;
    //                             for (var k in allGoods) {
    //                                 if (allGoods[k].name == v.name) {
    //                                     var details = allGoods[k].productDetails;
    //                                     //等级
    //                                     for (var q = 0; q < details.length; q++) {
    //                                         if (details[q].name == $('#div' + (i + 1)).find('.two-th').html()) {
    //                                             var values = details[q].values;
    //                                             $.each(values, function (k, v) {
    //                                                 rank += '<option>' + v.value + '</option>';
    //                                             })
    //                                             $('#div' + (i + 1)).find('.in-2 select').html(rank);
    //                                             rank = '';
    //                                         }
    //                                     }
    //                                     //大小
    //                                     for (var q = 0; q < details.length; q++) {
    //                                         if (details[q].name == $('#div' + (i + 1)).find('.three-th').html()) {
    //                                             var values = details[q].values;
    //                                             $.each(values, function (k, v) {
    //                                                 size += '<option>' + v.value + '</option>';
    //                                             })
    //                                             $('#div' + (i + 1)).find('.in-3 select').html(size);
    //                                             size = '';
    //                                         }
    //                                     }
    //                                     //品种
    //                                     for (var q = 0; q < details.length; q++) {
    //                                         if (details[q].name == $('#div' + (i + 1)).find('.four-th').html()) {
    //                                             var values = details[q].values;
    //                                             $.each(values, function (k, v) {
    //                                                 type_pro += '<option>' + v.value + '</option>';
    //                                             })
    //                                             $('#div' + (i + 1)).find('.in-4 select').html(type_pro);
    //                                             type_pro = '';
    //                                         }
    //                                     }
    //                                 }
    //                             }
    //                             ;
    //                             $('.main').on('click', '#orderList  #tr'+(i+1), function () {
    //                                 var a = ($(this).siblings('table').find('tr:nth-of-type(2)').clone())[0].innerHTML;
    //                                 var first_n = $(this).siblings('table').find('.first-td')[0].value;
    //
    //
    //                                 //console.log(($(this).siblings('table').find('tr:nth-of-type(2)').clone())[0].innerHTML);
    //                                 var tr = document.createElement('tr');
    //                                 tr.innerHTML = a;
    //                                 $(this).siblings('table').append(tr);
    //                                 $(this).siblings('table').find('.first-td:last').val(first_n);
    //                                 $(this).siblings('table').find('tr:last-of-type').find('.amount input').val('0');
    //
    //                             });
    //                             matching();
    //
    //                             i++;
    //                             addIdNum2 = i;
    //                             addIdNum++
    //                         })
    //
    //
    //                     }
    //                 })
    //
    //             })
    //         }
    //     })
    //
    //
    // })


    //保存更新物流信息
    //提交审核
    $('#next_step').click(function () {
        var orderId;
        var type;
        if ($('#uniform-agency').find('span').hasClass('checked')) {
            type = 1
        } else {
            type = 2
        }


        var id;//物流方式
        if ($('input[name="logistics"]').eq(0).parents('span').hasClass('checked')) {
            id = 1
        } else {
            id = 2
        }

        var tradeType;//贸易方式
        if ($('input[name="trade"]').eq(0).parents('span').hasClass('checked')) {
            tradeType = 1
        } else {
            tradeType = 2
        }

        //报关 清关 保险 默认勾选
        var payType;//支付方式
        if ($('input[name="settlement"]').eq(0).parents('span').hasClass('checked')) {
            payType = 1
        } else if ($('input[name="settlement"]').eq(1).parents('span').hasClass('checked')) {
            payType = 2
        } else {
            payType = 3
        }
        //费用清单
        var feeList = [];
        var fees = $('.feeList');
        for (var i = 0; i < fees.length; i++) {
            var feeTab = fees[i];
            var batchNumber = feeTab.children[0].innerHTML;
            var feeId = feeTab.children[0].getAttribute('id');
            feeId = Number(feeId);
            var productAmount = feeTab.children[1].innerHTML;
            productAmount = parseInt(productAmount)
            var agencyAmount = feeTab.children[2].children[0].value;
            var premiumAmount = feeTab.children[3].children[0].value;

            var details = {
                batchNumber: batchNumber,
                id: feeId,
                productAmount: productAmount,
                agencyAmount: agencyAmount,
                premiumAmount: premiumAmount
            }
            feeList.push(details)
        }

        var deliveryId = $('#select option:selected').attr('receiverid');

        var needLoan
        var nLoan = $('.need-loan label');
        if ($('input[name="need-loan"]').eq(0).parents('span').hasClass('checked')) {
            needLoan = 1
        } else {
            needLoan = 0
        }
        ;
        //预付款 尾款
        var advance = $('#advance').val();
        var restPay = $('#restPay').val();

        var loadInfo = [];
        var tr = $('.loan-table').find('.tab-tr');
        for (var i = 0; i < tr.length; i++) {
            var tab_tr = tr[i];
            var containerId = tab_tr.children[0].innerHTML;
            var productName = tab_tr.children[1].innerHTML;
            var transactionno = tab_tr.children[0].getAttribute('transactionno');
            var productId = tab_tr.children[1].getAttribute('productId');
            var loanQuota = tab_tr.children[2].children[0].innerHTML;
            // loanQuota = parseInt(loanQuota);
            var applyQuota = tab_tr.children[3].children[0].value;
            var confirmLoan = tab_tr.children[4].children[0].value;
            // confirmLoan =parseInt(confirmLoan);
            var details = {
                containerId: containerId,
                productName: productName,
                productId: productId,
                loanQuota: loanQuota,
                applyQuota: applyQuota,
                transactionNo: transactionno,
                confirmLoan: confirmLoan
            }
            loadInfo.push(details)
        }
        var contractUrl = myContractUrl();
        var voucherUrl = myVoucherUrl();

        //备注
        var backMemo = $('#note').val();

        //
        var type = $('#order').attr('type');
        if (type == 1) {
            var innerExpressId = $('#idDIvs').attr('innerExpress');
            var outerExpressId = $('#idDIvs').attr('outerExpress');
            var clearanceCompanyId = $('#idDIvs').attr('clearanceCompany');
        } else {
            var clearanceCompanyId = parseInt($('.customsClearance_2 span:nth-of-type(2)').attr('id'));

            var innerExpressId = parseInt($('.international-logistic').eq(1).attr('id'));
            var outerExpressId = parseInt($('.international-logistic').eq(0).attr('id'));
        }


        var data = {
            orderNo: orderNo, //第一步返回id
            type: id,
            id: logicsId,
            // status:status,
            tradeType: tradeType,
            preClearance: 1,
            feeList: feeList,
            clearance: 1,
            advance: advance,
            restPay: restPay,
            clearanceCompanyId: clearanceCompanyId, //后台获取
            insurance: 1,
            innerExpressId: innerExpressId,//后台获取
            outerExpressId: outerExpressId,//后台获取
            contractUrl: contractUrl,
            voucherUrl: voucherUrl,
            payType: payType,
            loadInfo: loadInfo,
            needLoan: needLoan,
            deliveryId: deliveryId,
            backMemo: backMemo
        }
        data = JSON.stringify(data);
        $.ajax({
            url: '/admin/order/logistics/service/update',
            data: data,
            type: 'post',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                if (data.code == 200) {
                    bootbox.alert('保存成功')
                }else if(data.code==100010){
                    bootbox.alert(data.msg)
                }else if(data.code==405){
                    bootbox.alert('备注过长')
                }
                else {
                    bootbox.alert('非法操作')
                    $('#next_step').attr('disabled', 'disabled');
                    $('#next_step').css('background','rgb(204, 204, 204)');
                    $('#next_step').css('border','none');
                    $('#next_step').css('color','#fff');

                }
            }
        })

    })

    $('input[name="need-loan"]').on('change', function () {
        if ($('input[name="need-loan"]')[0].checked) {
            $('.loan-table').css('display', 'block');
            $('.loan-table').find('.tab-tr').remove();
            $.ajax({
                url: '/admin/order/container_detail_ajax',
                data: {
                    orderId: orderNo
                },
                success: function (data) {

                    var data = data.data;
                    $('.tab-tr').remove()
                    $.each(data, function (k, v) {
                        var tag = '<tr class="tab-tr">' +
                            '<td transactionNo="' + v.transactionNo + '">' + v.containerId + '</td>' +
                            '<td  productId="' + v.productId + '">' + v.productName + '</td>' +
                            '<td><span>' + v.loanQuota + '</span>&nbsp;元</td>' +
                            '<td>' +
                            '<input value="0" type="text" class="loanMoney fl">' +
                            '<span class="fl">元</span>' +
                            '</td>' +
                            '<td> <input class="confirmMoney" type="text" value="0">&nbsp;元</td>' +
                            '<td class="sumMOney">' +
                            '<input value="0" disabled type="text" class="fl serviceFee">' +
                            '<span class="fl">元</span>' +
                            '</td>' +
                            '</tr>';
                        $('.loan-table tbody').append(tag)
                    })
                    matchingLoan()
                }
            })
        } else {
            $('.loan-table').css('display', 'none');
            $('.attention').css('display', 'none')
        }
    })

    //审核


    function matching() {
        var priceInput = $('.price');
        var amountInput = $('.amount');
        //$('.price input').on('input', function() {
        //    //this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1').
        //    //replace(".", "$#$").replace(/\./g, "").replace("$#$", ".").replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3');
        //    this.value = this.value.replace(/[^\d.]/g, "").replace(/(\..*)\./g, '$1').
        //    replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3');
        //});

        $('.price input').blur(function () {
            this.value = this.value.replace(/[^\d.]/g, "").replace(/(\..*)\./g, '$1').
            replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3');
        });

        for (var i = 0; i < priceInput.length; i++) {

            //priceInput[i].children[0].onkeyup = function () {
            //    if (this.value < 0) {
            //        this.value = ''
            //    }
            //}
            amountInput[i].children[0].onkeyup = function () {
                if (this.value.length == 1) {
                    this.value = this.value.replace(/[^1-9]/g, '')
                } else {
                    this.value = this.value.replace(/\D/g, '')
                }
            }

            amountInput[i].onafterpaste = function () {
                if (this.value.length == 1) {
                    this.value = this.value.replace(/[^1-9]/g, '')
                } else {
                    this.value = this.value.replace(/\D/g, '')
                }
            }
        }
    }
    function matchingLoan() {
        var loanMoney = $('.loanMoney');
        var confirmMoney = $('.confirmMoney');
        for (var i = 0; i < confirmMoney.length; i++) {
            confirmMoney[i].onkeyup = function () {
                if (this.value.length == 1) {
                    this.value = this.value.replace(/[^1-9]/g, '')
                } else {
                    this.value = this.value.replace(/\D/g, '')
                }
            }
            loanMoney[i].onkeyup = function () {
                if (this.value.length == 1) {
                    this.value = this.value.replace(/[^1-9]/g, '')
                } else {
                    this.value = this.value.replace(/\D/g, '')
                }
            }

            confirmMoney[i].onafterpaste = function () {
                if (this.value.length == 1) {
                    this.value = this.value.replace(/[^1-9]/g, '')
                } else {
                    this.value = this.value.replace(/\D/g, '')
                }
            }
            loanMoney[i].onafterpaste = function () {
                if (this.value.length == 1) {
                    this.value = this.value.replace(/[^1-9]/g, '')
                } else {
                    this.value = this.value.replace(/\D/g, '')
                }
            }
        }
    }
    function two (productName,i,parameter) {
        var rank, size, type;
        $.ajax({
            url: '/admin/order/find_all_goods',
            success: function (data) {

                var allGoods = data.data;


                for (var k in allGoods) {

                    if (allGoods[k].name == productName) {
                        $('#div' + i).find('.two-th').html(allGoods[k].productDetails[0].name);
                        $('#div' + i).find('.two-th').attr('engName',allGoods[k].productDetails[0].engName);
                        $('#div' + i).find('.three-th').html(allGoods[k].productDetails[2].name);
                        $('#div' + i).find('.three-th').attr('engName',allGoods[k].productDetails[2].engName);
                        $('#div' + i).find('.four-th').html(allGoods[k].productDetails[1].name);
                        $('#div' + i).find('.four-th').attr('engName',allGoods[k].productDetails[1].engName);

                        //添加具体的选项
                        $('#div' + i)[0].getElementsByClassName('.first-td').value =productName;
                        var inputList = $('#div' + i).find('.first-td');

                        for (var j = 0; j < inputList.length; j++) {
                            inputList[j].value = productName;
                            inputList[j].setAttribute('productId', allGoods[k].id)
                        }
                        var details = allGoods[k].productDetails;
                        //等级
                        for (var q = 0; q < details.length; q++) {
                            if (details[q].name == $('#div' + i).find('.two-th').html()) {
                                var values = details[q].values;
                                $.each(values, function (k, v) {
                                    rank += '<option>' + v.value + '</option>';
                                });
                                console.log(rank);
                                $('#div' + i).find('.in-2 select').html(rank);
                                rank = '';
                            }
                        }
                        //大小
                        for (var q = 0; q < details.length; q++) {
                            if (details[q].name == $('#div' + i).find('.three-th').html()) {
                                var values = details[q].values;
                                $.each(values, function (k, v) {
                                    size += '<option>' + v.value + '</option>';
                                })
                                console.log(size);
                                $('#div' + i).find('.in-3 select').html(size);
                                size = '';
                            }
                        }
                        //品种
                        for (var q = 0; q < details.length; q++) {
                            if (details[q].name == $('#div' + i).find('.four-th').html()) {
                                var values = details[q].values;
                                $.each(values, function (k, v) {
                                    type_pro += '<option>' + v.value + '</option>';
                                });
                                console.log(type_pro);
                                $('#div' + i).find('.in-4 select').html(type_pro);
                                type_pro = '';
                            }
                        }

                        //根据回传数据，默认选择项目
                        var first_name = $('#div' + i).find('.first-td');
                        $.each(first_name, function (k, v) {
                            v.disabled = true
                        })
                        //默认属性选择


                        var totalPrices = $('#div' + i).find('.price');
                        var totalAmount = $('#div' + i).find('.amount');
                        var sumPrices = $('#div' + i).find('.price-all');
                        var in_3 = $('#div' + i).find('.in-3');
                        var in_2 = $('#div' + i).find('.in-2');
                        var in_4 = $('#div' + i).find('.in-4');
                        // i++;
                        for (var a = 0; a < parameter.length; a++) {
                            totalPrices[a].children[0].value = parameter[a].price;
                            totalAmount[a].children[0].value = parameter[a].quantity;
                            sumPrices[a].children[0].value = parameter[a].totalPrice;

                            var productDetail = parameter[a].productDetail;

                            var sizeSelect = in_3[a].children[0].children;
                            var rankSelect = in_2[a].children[0].children;
                            var typeSelect = in_4[a].children[0].children;

                            //大小
                            for (var b = 0; b < sizeSelect.length; b++) {
                                for (var k in productDetail) {
                                    if (productDetail[k] == sizeSelect[b].innerHTML) {
                                        sizeSelect[b].selected = true;
                                    }
                                }
                            }

                            //等级
                            for (var b = 0; b < rankSelect.length; b++) {
                                for (var k in productDetail) {
                                    if (productDetail[k] == rankSelect[b].innerHTML) {
                                        rankSelect[b].selected = true;
                                    }
                                }
                            }

                            //品种
                            for (var b = 0; b < typeSelect.length; b++) {
                                for (var k in productDetail) {
                                    if (productDetail[k] == typeSelect[b].innerHTML) {
                                        typeSelect[b].selected = true;
                                    }
                                }
                            }

                        }

                    }
                    i++
                }

            }
        })
    }
})