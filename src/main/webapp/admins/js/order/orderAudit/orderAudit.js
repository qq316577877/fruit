/**
 * 下单
 * Created by 杨隆
 */

// 列表的html的dom
var clo_tab = ($('#pro_list div:nth-of-type(1)').clone())[0].outerHTML;

$(function () {

    var restrictSum;    // 货柜限制数量
    var logicsId;   //物流id1
    var orderNo = $('#order').html();  // 订单id
    var supplierList=[];    // 供应商列表
    var productList=[];     // 商品列表
    var i = 1;      // 默认序号

    // 初始化列表
    init();
    // 下单第二布
    $('#orderTwo').click(orderSecondStep);
    // 下单第一步
    $('#orderOne').click(orderFirstStep);
    // 供应商列表切换
    $('.selsct').change(supplierChange);
    // 新增货柜绑定事件
    $('.main').on('click', '#ad_1',addContainer);
    // 产品切换事件
    $('#type_pro').change(productChange);
    // 产品保存
    $('#save_2').unbind("click"); //移除click
    $('#save_2').click(productSave);
    // 订单提交审核
    $('#next_step').click(saveOrder);

    // 初始化方法
    function init() {

        // 获取订单详情
        $.ajax({
            url: '/admin/order/last_order_ajax',
            data: {
                orderId: orderNo
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
        $('.loan-table .tab-tr').remove();

        // 添加一行
        $('.main').on('click', '#orderList  .add-h', function () {
            var a = ($(this).parents('div').siblings('table').find('tr:nth-of-type(2)').clone())[0].innerHTML;
            var first_n = $(this).parents('div').siblings('table').find('.first-td')[0].value;
            //console.log(($(this).siblings('table').find('tr:nth-of-type(2)').clone())[0].innerHTML);
            var tr = document.createElement('tr');
            tr.innerHTML = a;
            $(this).parents('div').siblings('table').append(tr);
            $(this).parents('div').siblings('table').find('.first-td:last').val(first_n);
            $(this).parents('div').siblings('table').find('tr:last-of-type').find('.amount input').val('0');
        })
    }

    $('#orderTwo').click(function () {

        $('.main').on('click', '.cancel', function () {
            $(this).parents('.x').parents('.pro-list-head').parents('.pro-list-1').remove()
        })
    });


    //$('#orderTwo').click(
    // 下单第二步事件
    function orderSecondStep() {
        //清空资金服务

        $('.loan-table .tab-tr').remove();
        // 移除第一步的样式
        $('#orderOne').removeClass('clickStep');
        // 添加第二布的样式
        $(this).addClass('clickStep');

        // 显示隐藏
        $('#twoStep').css('display', 'block');
        $('#oneStep').css('display', 'none');

        $.ajax({
            url: '/admin/order/logistics/service/query',
            data: {
                orderId: orderNo
            },
            success: function (data) {
                if(data.data.status==6){
                    $('#next_step').attr('disabled',true)
                }
                logicsId = data.data.id;
                var type = $('#order').attr('type');

                if (type == 1) {
                    $('.contract_1').eq(1).css('display', 'none');
                    $('.logistics_2').eq(0).find('span').addClass('checked');
                    $('.logistics_1').eq(1).css('display', 'none');
                    $('.logistics_1').eq(2).css('display', 'none');
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
                            else if($('#select option:selected').html()=='请选择'){
                                $('#select_list').html('');
                                $('#select_list').css('display', 'none')
                            }
                        })
                    });

                    $.each(data, function (k, v) {
                        if (v.receiver == selectAdd.receiver) {
                            $('#select option').eq(k + 1)[0].selected = true;
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

                    if (needLoan == null || needLoan == 0) {
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
                                console.log(11111);
                                var data = data.data;
                                $('.tab-tr').remove()
                                $.each(data, function (k, v) {
                                    var tag = '<tr class="tab-tr">' +
                                        '<td transactionNo="' + v.transactionNo + '">' + v.containerId + '</td>' +
                                        '<td  productId="' + v.productId + '">' + v.productName + '</td>' +
                                        '<td>' + v.loanQuota + ' &nbsp;元</td>' +
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
                            '<td><input value="' + v.agencyAmount + '" type="text"><span>元</span></td>' +
                            '<td><input value="' + v.premiumAmount + '" type="text"><span>元</span></td>' +
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
                    $('#showImg').attr('src', contractUrl);
                    $('#imgPicker>div:nth-of-type(2)').css({
                        'width': '170px',
                        'height': '120px',
                    })
                    $('#imgPicker2>div:nth-of-type(2)').css({
                        'width': '170px',
                        'height': '120px',
                    })

                } else {


                    $('.logistics_2').eq(0).find('span').addClass('checked');
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

                    $.each(data, function (k, v) {
                        if (v.receiver == selectAdd.receiver) {
                            $('#select option').eq(k + 1)[0].selected = true;
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

                    if (needLoan == null || needLoan == 0) {
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
                                $('.tab-tr').remove()
                                $.each(data, function (k, v) {
                                    //
                                    var tag = '<tr class="tab-tr">' +
                                        '<td transactionNo="' + v.transactionNo + '">' + v.containerId + '</td>' +
                                        '<td  productId="' + v.productId + '">' + v.productName + '</td>' +
                                        '<td>' + v.loanQuota + ' &nbsp;元</td>' +
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
                            '<td><input value="' + v.agencyAmount + '" type="text"><span>元</span></td>' +
                            '<td><input value="' + v.premiumAmount + '" type="text"><span>元</span></td>' +
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

                    $('#settlement_1').html(advance + '元')
                    $('#settlement_2').html(restPay + '元')
                }
            }
        })

    };


    //实时计算服务费
    $('.main').on('input propertychange', '.confirmMoney', function () {
        var confirmLoan = $(this).val();
        var serviceFee = parseInt(confirmLoan) * 0.001;
        $(this).parents('td').siblings('.sumMOney').find('input').val(serviceFee);
    });

    // 供应商更新
    function supplierChange() {
        $.each(supplierList, function (k, val) {
            if (v.supplierContact == $('.selsct option:selected').html()) {
                var tag = '  <li>' +
                    '<span>收件人：</span>' + val.supplierContact +
                    '</li>' +
                    '<li>' +
                    '<span>所在地区：</span>' +
                    '<div class="country">' + val.countryName + '</div>' +
                    '<div class="province">' + val.provinceName + '</div>' +
                    '<div class="xian">' + val.districtName + '</div>' +
                    '</li>' +
                    '<li>' +
                    '<span>详细地址：</span>' + val.address +
                    '</li>' +
                    '<li>' +
                    '<span>邮政编码：</span>' + val.zipCode +
                    '</li>' +
                    '<li>' +
                    '<span>手机：</span>' + val.cellPhone +
                    '</li>' +
                    '<li>' +
                    '<span>固定电话：</span>' + val.phoneNum +
                    '</li>';
                $('.supplier').eq(0).find('ul').html(tag);
                $('.supplier').eq(0).find('ul').css('display', 'block')
            }
        })
    }

    // 下单第一步
    function orderFirstStep() {
        // 按钮样式切换
        $('#orderTwo').removeClass('clickStep');

        $(this).addClass('clickStep');
        // 显示隐藏
        $('#oneStep').css('display', 'block');
        $('#twoStep').css('display', 'none');
        $('#next_1').css('display', 'block');
        //清空表单

        $('.pro-list-1').remove();

        // 获取订单详情
        $.ajax({
            url: '/admin/order/tab_order_ajax',
            data: {orderId: orderNo},
            success: function (data) {
                if(data.code!=200){
                    return;
                }

                // 如果订单是确认提交
                if (data.data.status == 4) {
                    $('#next_1').attr('disabled', true)
                }

                var data = data.data;
                status = data.status;
                var suppier=data.supplierList;
                supplierList=suppier;
                // 隐藏新增货柜提示
                $('.container-add-3').css('display', 'none');

                //判断选择的type 如果是代采
                if (data.type == 1) {
                    $('#agency').parents('span').addClass('checked')
                    $('#direct').attr('disabled', 'disabled');
                    var tag = "<option supplierid='1' selected disabled>浙江创意生物科技股份有限公司</option>";
                    $('.supplier-2 select').html(tag);
                } // 通关物流
                else {
                    $('#direct').parents('span').addClass('checked');
                    $('#agency').attr('disabled', 'disabled');
                    var id = data.supplierId;

                    // 绑定供应商下拉框
                    var supplierHtmlList="";
                    $.each(supplierList, function (k, val) {
                        if(val.id==id){
                            supplierHtmlList += '<option selected supplierid="' + val.id + '">' + val.supplierContact + '</option>';
                        }else{
                            supplierHtmlList += '<option supplierid="' + val.id + '">' + val.supplierContact + '</option>';
                        }
                    });
                    $('.supplier-2 select').html(supplierHtmlList);

                    // 更新选择的供应商信息
                    supplierChange();
                }

                //添加货柜数据
                var list = data.orderContainers;
                var thisdivId = 1;
                createOrderDivs('orderList', list, thisdivId);

                // 货柜添加校验
                addIdNum = thisdivId;
                // 添加校验
                matching();
                // $.each(list, function (k, v) {
                //     $('.main').on('click', '#orderList  #tr' + thisdivId, function () {
                //         var a = ($(this).siblings('table').find('tr:nth-of-type(2)').clone())[0].innerHTML;
                //         var first_n = $(this).siblings('table').find('.first-td')[0].value;
                //         var tr = document.createElement('tr');
                //         tr.innerHTML = a;
                //         $(this).siblings('table').append(tr);
                //         $(this).siblings('table').find('.first-td:last').val(first_n);
                //         $(this).siblings('table').find('tr:last-of-type').find('.amount input').val('0')
                //     });
                //     thisdivId++;
                // })
                //添加货柜新增按钮
                var addList = '<button class="ad" id="ad_1">新增一个货柜</button>';
                $('#orderList').append(addList);
                $('#ad_1').css('display', 'block');
            }
        })
    }

    // 新增一个货柜
    function addContainer() {
        //获取创建了几个表单
        var tableListNum = $('.pro-list-1').length;
        var i = parseInt(tableListNum);

        var sums_1 = $('#div' + (i)).children('.pro-list-body').find('.amount').children('input');
        var sum = 0;
        var num;
        for (var w = 0; w < sums_1.length; w++) {
            num = Number(sums_1[w].value);
            sum += num;
        }

        var restrictSum = parseInt($('#div' + (i)).find('.container-norms').attr('maxquantity'));
        if (sum > restrictSum) {
            bootbox.alert('超出货柜规格');
            return
        }

        $('#div' + i).find('.container-norms').attr('totalquantity',sum);

        //多次点击后重复添加
        if (document.getElementById('type_pro').childElementCount !== 1) {
            $('#type_pro').html('');
            $('#type_pro option').text('请选择');
        }

        $('.ad').css('display', 'block');
        $('.mask').css('display', 'block')
        $('.inform').css('display', 'none');
        $('.inform-1').css('display', 'none');
        $('#inform2').css('display', 'block');
        $('.order-next').css('display', 'block');
        $('#next_2').css('display', 'none');

        $.ajax({
            url: '/admin/order/find_all_goods',
            success: function (data) {
                if(data.code!=200){
                    return;
                }
                var list = data.data;
                productList=list;
                var productSelectHtml="";
                $.each(list, function (k, val) {
                    productSelectHtml += '<option>' + val.name + '</option>';
                });
                $('#type_pro').html(productSelectHtml);
            }
        })
    }

    // 产品下拉框切换
    function productChange() {
        var list=productList;
        $.each(list, function (k, v) {
            if (v.name == $('#inform2 #type_pro option:selected').html()) {
                $('#num-fruit')[0].value = list[k].capacitySize + list[k].unit;
            }
        })
    }

    // 保存选择的产品
    function productSave() {
        //添加隐藏标签
        var tagSumHidden = '<input class="hide_inform" style="display:none" value="">';
        $('#div' + i).find('.pro-list-head').append(tagSumHidden);

        $('.mask').css('display', 'none');
        $('#ad_1').before(clo_tab);
        $('#orderList').find('.pro-list-1:last').attr('id', 'div' + (i + 1));

        var addHead =
            '<h3>货柜批次号：   </h3>' +
            '<h3 class="container-name" productid="' + v.id + '" >货柜名称：' + v.name + '</h3>' +
            '<h3 maxquantity="' + v.capacitySize + '" class="container-norms">货柜规格：0-' + v.capacitySize + '箱</h3>' +
            '<div class="x">' +
            '<i class="iconfont icon-chacha cancel cancel-1 fr"></i>' +
            '</div>';
        $('#div' + (i + 1)).find('.pro-list-head').html(addHead);
        var amountLists = $('#div' + (i + 1)).find('.amount');
        $.each(amountLists, function (k, v) {
            v.children[0].value = 0;
        })
        var first_input = $('#div' + (i + 1)).find('.first-td');
        $('#div' + (i + 1)).find('.add-h').attr('id', 'tr' + (i + 1));
        restrictSum = v.capacitySize;

        for (var s = 0; s < first_input.length; s++) {
            first_input[s].setAttribute('productId', v.id);
            first_input[s].value = v.name;
            first_input[s].disabled = true;
        }
        ;
        var allGoods = list;
        var rank, size, type;
        for (var k in allGoods) {
            if (allGoods[k].name == v.name) {
                var details = allGoods[k].productDetails;
                //等级
                for (var q = 0; q < details.length; q++) {
                    if (details[q].name == $('#div' + (i + 1)).find('.two-th').html()) {
                        var values = details[q].values;
                        $.each(values, function (k, v) {
                            rank += '<option>' + v.value + '</option>';
                        })
                        $('#div' + (i + 1)).find('.in-2 select').html(rank);
                        rank = '';
                    }
                }
                //大小
                for (var q = 0; q < details.length; q++) {
                    if (details[q].name == $('#div' + (i + 1)).find('.three-th').html()) {
                        var values = details[q].values;
                        $.each(values, function (k, v) {
                            size += '<option>' + v.value + '</option>';
                        })
                        $('#div' + (i + 1)).find('.in-3 select').html(size);
                        size = '';
                    }
                }
                //品种
                for (var q = 0; q < details.length; q++) {
                    if (details[q].name == $('#div' + (i + 1)).find('.four-th').html()) {
                        var values = details[q].values;
                        $.each(values, function (k, v) {
                            type_pro += '<option>' + v.value + '</option>';
                        })
                        $('#div' + (i + 1)).find('.in-4 select').html(type_pro);
                        type_pro = '';
                    }
                }
            }
        }
        ;
        $('.main').on('click', '#orderList  #tr'+(i+1), function () {
            var a = ($(this).parents('div').siblings('table').find('tr:nth-of-type(2)').clone())[0].innerHTML;
            var first_n = $(this).parents('div').siblings('table').find('.first-td')[0].value;
            var tr = document.createElement('tr');
            tr.innerHTML = a;
            $(this).parents('div').siblings('table').append(tr);
            $(this).parents('div').siblings('table').find('.first-td:last').val(first_n);
            $(this).parents('div').siblings('table').find('tr:last-of-type').find('.amount input').val('0');

        });
        matching();

        i++;
        addIdNum2 = i;
        addIdNum++
    }

    // 提交订单审核
    function saveOrder() {
        var orderId;
        var type; // 1代采  2.物流通关
        if ($('#uniform-agency').find('span').hasClass('checked')) {
            type = 1;
        } else {
            type = 2;
        }

        var id;//物流方式
        if ($('input[name="logistics"]').eq(0).parents('span').hasClass('checked')) {
            id = 1
        } else {
            id = 2
        }

        var tradeType //贸易方式
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
            var loanQuota = tab_tr.children[2].innerHTML;
            loanQuota = parseInt(loanQuota);
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
                }else{
                    bootbox.alert('非法操作')
                    $('#next_step').attr('disabled','disabled');
                    $('#next_step').css('background','#ccc');
                }
            }
        })
    }


    $('input[name="need-loan"]').on('change',function () {
        if($('input[name="need-loan"]')[0].checked){
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
                            '<td>' + v.loanQuota + ' &nbsp;元</td>' +
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
        }else{
            $('.loan-table').css('display', 'none');
            $('.attention').css('display', 'none')
        }
    })

    //审核
    function two(productName, i, parameter) {
        var rank, size, type;
        $.ajax({
            url: '/admin/order/find_all_goods',
            success: function (data) {

                var allGoods = data.data;


                for (var k in allGoods) {

                    if (allGoods[k].name == productName) {


                        //添加具体的选项
                        //$('#list' + i)[0].getElementsByClassName('.first-td').value =productName;
                        var inputList = $('#list' + i).find('.first-td');

                        for (var j = 0; j < inputList.length; j++) {
                            inputList[j].value = productName;
                            inputList[j].setAttribute('productId', allGoods[k].id)
                        }
                        var details = allGoods[k].productDetails;
                        //等级
                        for (var q = 0; q < details.length; q++) {
                            if (details[q].name == $('#list' + i).find('.two-th').html()) {
                                var values = details[q].values;
                                $.each(values, function (k, v) {
                                    rank += '<option>' + v.value + '</option>';
                                })
                                $('#list' + i).find('.in-2 select').html(rank);
                                rank = '';
                            }
                        }
                        //大小
                        for (var q = 0; q < details.length; q++) {
                            if (details[q].name == $('#list' + i).find('.three-th').html()) {
                                var values = details[q].values;
                                $.each(values, function (k, v) {
                                    size += '<option>' + v.value + '</option>';
                                })
                                $('#list' + i).find('.in-3 select').html(size);
                                size = '';
                            }
                        }
                        //品种
                        for (var q = 0; q < details.length; q++) {
                            if (details[q].name == $('#list' + i).find('.four-th').html()) {
                                var values = details[q].values;
                                $.each(values, function (k, v) {
                                    type_pro += '<option>' + v.value + '</option>';
                                })
                                $('#list' + i).find('.in-4 select').html(type_pro);
                                type_pro = '';
                            }
                        }

                        //根据回传数据，默认选择项目
                        var first_name = $('#list' + i).find('.first-td');
                        $.each(first_name, function (k, v) {
                            v.disabled = true
                        })
                        //默认属性选择

                        // $('#list' + i).find('.sum input').val(v.totalPrice);


                        //console.log(parameter);
                        var totalPrices = $('#list' + i).find('.price');
                        var totalAmount = $('#list' + i).find('.amount');
                        var sumPrices = $('#list' + i).find('.price-all');
                        var in_3 = $('#list' + i).find('.in-3');
                        var in_2 = $('#list' + i).find('.in-2');
                        var in_4 = $('#list' + i).find('.in-4');
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

                }

            }
        })
    }

    function matching() {
        var priceInput = $('.price');
        var amountInput = $('.amount');
        for (var i = 0; i < priceInput.length; i++) {
            priceInput[i].children[0].onkeyup = function () {
                if (this.value.length == 1) {
                    this.value = this.value.replace(/[^1-9]/g, '')
                } else {
                    this.value = this.value.replace(/\D/g, '')
                }
            }
            amountInput[i].children[0].onkeyup = function () {
                if (this.value.length == 1) {
                    this.value = this.value.replace(/[^1-9]/g, '')
                } else {
                    this.value = this.value.replace(/\D/g, '')
                }
            }

            priceInput[i].onafterpaste = function () {
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
    };

    function matchingLoan () {
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
})