/**
 * 订单审核
 * Created by  on 2017/09/25.
 **/


// 订单详细信息
var orderDetailInfo = {};

$(function () {
    var orderNo = window.location.href.split('=')[1];
    var goodsNum = 0;
    $('#order').html(orderNo);


    //判断是否要去货柜
    var containerClick = window.localStorage.getItem('containerClick');
    if (containerClick) {
        $('#containerTab').click();
        window.localStorage.removeItem('containerClick')
    }
    //判断是否要去物流
    var logisticsContainer = window.localStorage.getItem('logisticsContainer');
    if (logisticsContainer) {
        tabClick(2);
        // tabClick(2);
        window.localStorage.removeItem('logisticsContainer')
    }

    //判断是否在订单列表点击发货处理
    var sendShipping = window.localStorage.getItem('sendShipping');
    if (sendShipping) {
        tabClick(2);
        // tabClick(2);
        window.localStorage.removeItem('sendShipping')
    }

    var detailsItem = window.localStorage.getItem('orderDetails');
    if (detailsItem) {
        window.localStorage.removeItem('orderDetails');
        $('.audit_head').html('订单详情')
    }

    var closeLogisticPop = window.localStorage.getItem('closeLogisticPop');
    if (closeLogisticPop) {
        window.localStorage.removeItem('closeLogisticPop');
        tabClick(2)
    }

    var addAddressSuccess = window.localStorage.getItem('addAddressSuccess');
    if (addAddressSuccess) {
        window.localStorage.removeItem('addAddressSuccess');
        $('#containerTab').click();
    }
    var addLogisticSuccess = window.localStorage.getItem('addLogisticSuccess');
    if (addLogisticSuccess) {
        tabClick(2);
        // console.log(addLogisticSuccess);
        // window.onload = function () {
        //     var containerNoLists = $('#containerNoSelect option');
        //     $.each(containerNoLists, function (k, v) {
        //         console.log(v.value);
        //         if (v.value == addLogisticSuccess) {
        //             console.log(k);
        //             $('#containerNoSelect option').eq(k).attr('selected',true)
        //         }
        //     })
        // }
        window.localStorage.removeItem('addLogisticSuccess');

    }


    var headerData = {
        orderNo: orderNo
    };

    // 商品列表
    var goodsCommodityLi
    st = [];
    var newTableRow = "";
    var isPriceVerify = true;
    // 初始化页面
    init();

    // tab点击事件 订单信息

    $("#orderTab").click(function () {

        var orderNo = $('#order').html();

        self.location = '/admin/newOrder/orderAudit?orderNo=' + orderNo;

        tabClick(0);

        $('#tableOrder').remove();
        var headerData = {
            orderNo: $('#order').html()
        };
        headerData = JSON.stringify(headerData)
        // 订单详细信息
        $.ajax({
            url: '/admin/neworder/center/order_detail_ajax',
            type: 'post',
            data: headerData,
            success: function (response) {
                createDiv('#preservationCon', response.data);
                var info = response.data;
                // console.log("orderDetailInfo222:", orderDetailInfo);
                orderDetailInfo = response.data;

                // 是否贷款
                if (info.needLoan == 1) {
                    $('#checkboxLoan').parents('span').addClass('checked');
                    $('.fundInput').val(info.loanLimit);
                    $('.thisOrderLoanNeed i').html(info.loanAmount)
                } else {
                    $('#checkboxLoan').parents('span').removeClass('checked')
                }
                // 意向发货时间段
                $('#startTime').val(moment(info.intentStartDate).format('YYYY-MM-DD'));
                $('#endTime').val(moment(info.intentEndDate).format('YYYY-MM-DD'));
                // 安柜
                $('#sumCon').html(info.containerNum + '柜');
                $('#numCon').val(info.containerNum);
                // $('#totalPri').html(orderDetailInfo.totalAmount)
                // 果品选择
                $('#fruitSelect').html('<option>' + info.varietyName + '</option>');
                var proLists = response.data.goodsInfoList;

                // 商品种类
                goodsPro(proLists);

                // 如果是按柜
                if (orderDetailInfo.modeType == 1) {
                    //console.log("安规");
                    getAllGoods();
                };


                if (orderDetailInfo.status == 1 || orderDetailInfo.status == 2) {
                    $("#cancelOrder").show();
                    $('.thisOrderLoanNeed').hide()
                } else {
                    $("#cancelOrder").hide();
                    $('.thisOrderLoanNeed').show()
                }

                if(info.needLoan!=1){
                    $('.thisOrderLoanNeed').css('display','none')
                }
            }
        });
    });
    // 物流信息
    $("#logisticsTab").click(function () {



        // 判断订单状态
        var orderStatus = orderDetailInfo.status;
        if (orderStatus == "2" || orderStatus == "3" || orderStatus == "4" || orderStatus == "5") {
            //$("#addDails").show();
            // 货柜批次列表
            if (containerList.length > 0) {
                var container = containerList[0];
                // 根据货柜状态判断是否有添加物流信息
                if (container.status == 0 || container.status == 7) {
                    $("#addDails").hide();
                }
            }
        } else {
            $("#addDails").hide();
        }

        tabClick(2);
    });

    // 关闭合同
    $("#closeContract").click(function () {
        $("#mask").hide();
    });
    // 加货柜
    $("#containerNumOporateAdd").click(function () {
        // console.log("closeContract");
        $("#mask").hide();
    });

    // 审核通过
    $("#auditOrder").click(function () {
        if (!$('input[name="Fruit"]').parents('span').hasClass('checked')) {
            bootbox.alert('请查看并审核《采购服务合同》 ');
            return
        }

        // 判断价格
        if (!isPriceVerify) {
            bootbox.alert('超出价格区间');
            return;
        }

        var needLoan;
        if ($('#checkboxLoan').parents('span').hasClass('checked')) {
            needLoan = true
        } else {
            needLoan = false
        }

        var loanAmount = ($('.fundInput').val());
        if (loanAmount == '') {
            loanAmount = 0
        }
        var intentStartDate = $('#startTime').val();
        var intentEndDate = $('#endTime').val();
        if(intentStartDate>intentEndDate){
            bootbox.alert('意向发货时间选择有误');
            return
        }
        var containerNum = $('#numCon').val();
        var goodsNum = Number($('#totalAm').html());
        var totalAmount = Number($('#totalPri').html());
        var goodsInfoList = next('#tabledCreateId', orderNo);

        var data = {
            needLoan: needLoan,
            loanLimit: parseInt(loanAmount),
            intentStartDate: intentStartDate,
            intentEndDate: intentEndDate,
            containerNum: containerNum,
            goodsNum: goodsNum,
            totalAmount: totalAmount,
            orderNo: orderNo,
            goodsList: goodsInfoList
        }
        data = JSON.stringify(data);

        Loading.open();
        $.ajax({
            url: '/admin/neworder/center/auditOrder',
            data: data,
            type: 'post',
            success: function (data) {
                if (data.code == 200) {
                    bootbox.alert("订单审核通过");
                    var orderNo = $('#order').html();
                    self.location = '/admin/newOrder/orderAudit?orderNo=' + orderNo;
                } else {
                    bootbox.alert(data.msg)
                }
            },
            complete:function () {
                Loading.close();
            }
        });
    });

    // 取消订单
    $("#cancelOrder").click(function () {
        var orderData = {
            orderNo: orderNo
        };
        var data = JSON.stringify(orderData);
        $.ajax({
            url: '/admin/neworder/center/order_cancle_ajax',
            data: data,
            type: 'post',
            success: function (data) {
                if (data.code == 200) {
                    bootbox.alert("订单已取消成功");
                    location.href = '/admin/newOrder/orderList'
                }
            }
        });
    });

    // 初始化页面方法
    function init() {
        var headerData = {
            orderNo: orderNo
        };
        headerData = JSON.stringify(headerData);
        // 获取订单基本信息
        $.ajax({
            url: '/admin/neworder/center/head_order_ajax',
            data: headerData,
            type: 'post',
            success: function (data) {
                if (data.code == 200) {
                    // 如果成功
                    var list = (data).data;
                    var lasttime = list.lastTime == undefined ? '' : list.lastTime;
                    var lastOrderId = list.lastOrderId == undefined ? '' : list.lastOrderId;
                    var tag = '<div class="infoCloms">' +
                        '        <div><span class="audit_h">订单号：</span>' + list.orderId + '</div>' +
                        '        <div><span class="audit_h">历史成交：</span>' + list.successCount + '单</div>' +
                        '        <div><span class="audit_h">账号：</span>' + list.userId + '</div>' +
                        '        <div><span class="audit_h">订单状态：</span>' + list.orderStatusDesc + '</div>' +
                        '    </div>' +
                        '    <div class="infoCloms">' +
                        '        <div><span class="audit_h">下单日期：</span>' + list.placeOrderTime + '</div>' +
                        '        <div><span class="audit_h">最近交易时间：</span>' + lasttime + '</div>' +
                        '        <div><span class="audit_h">联系人：</span>' + list.contactName + '</div>' +
                        '    </div>' +
                        '    <div class="infoCloms">' +
                        '        <div>采购商：' + list.purchaserName + '</div>' +
                        '        <div><span class="audit_h">最近交易订单号：</span> ' + lastOrderId + '</div>' +
                        '        <div>联系电话：' + list.contactMobile + '</div>' +
                        '    </div>';
                    $('.audit_info').html(tag);
                } else {
                    bootbox.alert(data.msg);
                }
            }
        });

        // 订单详细信息
        $.ajax({
            url: '/admin/neworder/center/order_detail_ajax',
            type: 'post',
            data: headerData,
            success: function (response) {
                createDiv('#preservationCon', response.data);
                var info = response.data;
                orderDetailInfo = response.data;
                goodsNum = info.goodsNum;

                // 是否贷款
                if (info.needLoan == 1) {
                    $('#checkboxLoan').parents('span').addClass('checked');
                    $('.fundInput').val(info.loanLimit);
                    $('.thisOrderLoanNeed').show();
                    $('.thisOrderLoanNeed i').html(info.loanAmount)
                } else {
                    $('#checkboxLoan').parents('span').removeClass('checked');
                }

                // 判断保存
                if (orderDetailInfo.status == 1 || orderDetailInfo.status == 2) {
                    $("#preservationCon").show();
                } else {
                    $("#preservationCon").hide();
                }

                // 判断审核按钮
                if (orderDetailInfo.status == 1 || orderDetailInfo.status == 2) {
                    $("#auditOrder").show();
                } else {
                    $("#auditOrder").hide();
                }

                // 判断合同已签订 如果签订则不能取消订单
                if (orderDetailInfo.status == 1 || orderDetailInfo.status == 2) {
                    $("#cancelOrder").show();
                    $('.thisOrderLoanNeed').hide()
                } else {
                    $("#cancelOrder").hide();
                    $('.thisOrderLoanNeed').show()
                }

                if(info.needLoan!=1){
                    $('.thisOrderLoanNeed').css('display','none')
                }

                // 意向发货时间段
                $('#startTime').val(moment(info.intentStartDate).format('YYYY-MM-DD'));
                $('#endTime').val(moment(info.intentEndDate).format('YYYY-MM-DD'));
                // 安柜
                $('#sumCon').html(info.containerNum + '柜');
                $('#numCon').val(info.containerNum);
                // 果品选择
                $('#fruitSelect').html('<option>' + info.varietyName + '</option>');
                var proLists = response.data.goodsInfoList;

                // 商品种类
                goodsPro(proLists);

                // 如果是按柜
                if (orderDetailInfo.modeType == 1) {
                    //console.log("安规");
                    getAllGoods();
                }
                ;

                if (orderDetailInfo.status >= 2) {
                    $('input[name="Fruit"]').eq(0).parents('span').addClass('checked');
                }
                if (orderDetailInfo.status >= 3) {
                    $('#seeContract').attr('disabled', true).css('background', 'gray')
                }
            }
        });
    }

    //添加一行
    $('#new_order_info_detail').on('click', '#add-h', function () {

        // 按商品下单
        if (orderDetailInfo.modeType == 2) {
            var htmlL = $(this).parents('div').siblings('table').find('tr:last-of-type').clone();
            $('#tabledCreateId').append(htmlL);
        }
        // 按柜下单
        else if (orderDetailInfo.modeType == 1) {
            // 添加一行
            $('#tabledCreateId tbody').append(newTableRow);
            // 绑定事件
            bindTableSelect();
        }
    });

    // 绑定table的change事件
    bindTableSelect();

    //更改数量
    $('#new_order_info_detail').on('input', '.amount input', function () {
        this.value = this.value.replace(/[^\d.]/g, "").replace(/(\..*)\./g, '$1').replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3');
        var price = $(this).parents('.amount').siblings('.priceActual').find('input').val();
        $(this).parents('.amount').siblings('.priceAll').html($(this).val() * price);
        var amountLists = $('.amount');
        var sum = 0;
        $.each(amountLists, function (k, v) {
            // console.log(v.children[0].value);
            sum += parseInt(v.children[0].value)
        })
        goodsNum = sum;
        $('#totalAm').html(sum);
        // 计算总金额
        var amountLists = $('.priceAll');
        var amountSum = 0;
        $.each(amountLists, function (index, val) {
            amountSum += parseFloat($('.priceAll').eq(index).html());
        })
        $("#totalPri").html(amountSum);
    })

    //更改价格
    $('#new_order_info_detail').on('input', '.priceActual input', function () {
        this.value = this.value.replace(/[^\d.]/g, "").replace(/(\..*)\./g, '$1').replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3');
    });
    $('#new_order_info_detail').on('blur', '.priceActual input', function () {
        this.value = this.value.replace(/[^\d.]/g, "").replace(/(\..*)\./g, '$1').replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3');
        var lowPrice = $(this).parents('.priceActual').siblings('.priceLow').html();
        var heighPrice = $(this).parents('.priceActual').siblings('.priceHigh').html();
        if ($(this).val() > parseInt(heighPrice) || $(this).val() < parseInt(lowPrice)) {
            $(this).parent().addClass("errorinput");
            bootbox.alert('超出价格区间');
            isPriceVerify = false;
            return
        } else {
            $(this).parent().removeClass("errorinput");
            var errorlist = $(".errorinput");
            if (errorlist.length > 0) {
                isPriceVerify = false;
            } else {
                isPriceVerify = true;
            }
        }
        var amount = $(this).parents('.priceActual').siblings('.amount').find('input').val();
        $(this).parents('.priceActual').siblings('.priceAll').html($(this).val() * amount);
        // 计算总金额
        var amountLists = $('.priceAll');
        var amountSum = 0;
        $.each(amountLists, function (index, val) {
            amountSum += parseFloat($('.priceAll').eq(index).html());
        })
        $("#totalPri").html(amountSum);
    });


    //计算总价格
    var pricesAll = $('.priceAll');


    //生成合同
    $('#seeContract').click(function () {
        var headerData = {
            orderNo: orderNo
        };
        headerDataStr = JSON.stringify(headerData);
        $.ajax({
            url: '/admin/neworder/center/contract/view',
            data: headerDataStr,
            type: 'post',
            success: function (response) {
                if (response.code = 200) {
                    var contractUrl = response.data.contractPath;
                    var options = {
                        fallbackLink: "<p>该浏览器不支持pdf预览，请点击<a href='+contractUrl+'>此处</a>下载预览</p>"
                    };
                    PDFObject.embed(contractUrl, "#pdf1", options);
                    $("#mask").show();
                }
            }
        })

        var options = {
            fallbackLink: "<p>该浏览器不支持pdf预览，请点击<a href='[url]'>此处</a>下载预览</p>"
        };
        PDFObject.embed(decodeURIComponent("../lib/javastudy.pdf"), "#pdf1", options);
    });

    //查看合同
    $('#lookContract').click(function () {
        var headerData = {
            orderNo: orderNo
        };
        headerDataStr = JSON.stringify(headerData);
        $.ajax({
            url: '/admin/neworder/center/contract/view',
            data: headerDataStr,
            type: 'post',
            success: function (response) {
                if (response.code = 200) {
                    var contractUrl = response.data.contractPath;
                    var options = {
                        fallbackLink: "<p>该浏览器不支持pdf预览，请点击<a href='+contractUrl+'>此处</a>下载预览</p>"
                    };
                    PDFObject.embed(contractUrl, "#pdf2", options);
                    $("#maskSee").show();
                }
            }
        })

        var options = {
            fallbackLink: "<p>该浏览器不支持pdf预览，请点击<a href='[url]'>此处</a>下载预览</p>"
        };
        PDFObject.embed(decodeURIComponent("../lib/javastudy.pdf"), "#pdf2", options);
    });

    $('#maskSee').on('click', '#closeContract_2', function () {
        $('#maskSee').hide()
    })

    //保存
    $('#preservation').click(function () {
        // if (!$('input[name="Fruit"]').parents('span').hasClass('checked')) {
        //     bootbox.alert('请查看并审核《采购服务合同》 ');
        //     return
        // }

        var needLoan;
        if ($('#checkboxLoan').parents('span').hasClass('checked')) {
            needLoan = true
        } else {
            needLoan = false
        }

        // 判断价格
        if (!isPriceVerify) {
            bootbox.alert('超出价格区间');
            return;
        }
        var loanAmount = ( $('.fundInput').val());
        // console.log(loanAmount==NaN);
        if (loanAmount == 'null' || loanAmount == '' || loanAmount == NaN) {
            loanAmount = 0
        }

        var intentStartDate = $('#startTime').val();
        var intentEndDate = $('#endTime').val();
        if(intentStartDate>intentEndDate){
            bootbox.alert('意向发货时间选择有误');
            return
        }
        var containerNum = $('#numCon').val();
        var goodsNum = Number($('#totalAm').html());
        var totalAmount = Number($('#totalPri').html());
        var goodsInfoList = next('#tabledCreateId', orderNo);
        var data = {
            needLoan: needLoan,
            loanLimit: parseInt(loanAmount),
            intentStartDate: intentStartDate,
            intentEndDate: intentEndDate,
            containerNum: containerNum,
            goodsNum: goodsNum,
            totalAmount: totalAmount,
            orderNo: orderNo,
            goodsList: goodsInfoList
        }
        data = JSON.stringify(data);

        Loading.open();
        $.ajax({
            url: '/admin/neworder/center/modifyOrder',
            type: 'post',
            data: data,
            success: function (response) {
                if (response.code == 200) {
                    bootbox.alert('保存成功')
                } else {
                    bootbox.alert(response.msg)
                }
            },
            complete:function () {
                Loading.close();
            }
        })
    })

    // 绑定下拉框
    function bindTableSelect() {
        // 更改商品属性
        $('#new_order_info_detail').on('change', '.in-2', function () {
            changeGoodsPro(this);
        });
    }

    //  获取全部商品信息
    function getAllGoods() {
        $.ajax({
            url: '/admin/neworder/goods/find_goods_commodities_by_variety',
            data: {varietyId: 1},
            success: function (response) {
                goodsCommodityList = response.data.goodsCommodityInfos;
                // console.log("goodsCommodityList--",goodsCommodityList);

                newTableRow = "<tr>";
                newTableRow += "<td>" + orderDetailInfo.varietyName + "</td><td  class='in-2'><select >";
                $.each(goodsCommodityList, function (index, val) {
                    if (index == 0) {
                        newTableRow += '<option commodityId="' + val.id + '" selected="selected">' + val.name + '</option>';
                    } else {
                        newTableRow += '<option commodityId="' + val.id + '">' + val.name + '</option>';
                    }
                });
                newTableRow += '</select></td><td class="amount"><input value="0"></td>';
                newTableRow += '<td class="priceLow">' + goodsCommodityList[0].priceLow + '</td>';
                newTableRow += '<td class="priceHigh">' + goodsCommodityList[0].priceHigh + '</td>';
                newTableRow += '<td class="priceActual"><input type="number" value="' + goodsCommodityList[0].priceActual + '"></td>';
                newTableRow += '<td class="priceAll">' + 0 + '</td></tr>';

            }
        })
    }


    //--------------------------------------------------------------------------
    //收款信息
    // 收款信息
    $("#MoneyTab").click(function () {
        tabClick(4);
        var data = {
            orderNo: $('#order').html()
        };
        data = JSON.stringify(data);
        $.ajax({
            url: '/admin/neworder/center/get_receive_info_ajax',
            type: 'post',
            data: data,
            success: function (response) {
                var list = response.data;

                if (list == null || list == '') {
                    bootbox.alert('暂无信息')
                } else {
                    var tag = '';
                    var tagHeader = '    <tr>' +
                        '            <th>收款时间</th>' +
                        '            <th>收款类别</th>' +
                        '            <th>金额（元）</th>' +
                        '            <th>备注信息</th>' +
                        '        </tr>';
                    $('#collectionTable').html(tagHeader);
                    $.each(list, function (k, v) {
                        var details = v.detailInfo == 'null' ? '' : v.detailInfo;
                        tag += '  <tr>' +
                            '            <td>' + v.addTimeStr + '</td>' +
                            '            <td>' + v.typeDesc + '</td>' +
                            '            <td>' + v.amount + '</td>' +
                            '            <td>' + details + '</td>' +
                            '        </tr>';
                    });
                    $('#collectionTable').append(tag);
                }

            }
        })
    });


})
// 订单点击事件
function tabClick(index) {
    // 样式清空
    $(".orderStep a").removeClass();
    // 样式选中
    $(".orderStep a").eq(index).attr("class", "clickStep");
    // 全部隐藏
    $(".orderInfo>div").hide();
    // 显示选中
    $(".orderInfo>div").eq(index).show();
}
