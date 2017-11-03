$(function () {
    /**
     * Created by yl on 2017/9/27.
     */
// 物流信息
    var newTableRow="";
    $("#containerTab").click(function () {
        tabClick(1);
        $('.containerInfo .form_horizontal').remove();
        // var orderNo = window.location.href.split('=')[1];
        // console.log(orderNo);
        // var orderId = $('#order').html(orderNo);
        var headerData = {
            orderNo: $('#order').html()
        };
        headerData = JSON.stringify(headerData);
        $('#mask').hide();
        Loading.open();
        $.ajax({
            url: '/admin/neworder/center/container/container_detail_orderno_ajax',
            data: headerData,
            type: 'post',
            success: function (response) {
                if (response.code == 405) {
                    bootbox.alert(response.msg);
                    $('.containerDetails').hide();
                    return
                }
                Loading.close();
                var data = response.data;
                $('.containerInfo').show();
                $('.containerDetails').show();
                //创建货柜列表并默认加载第一个
                var containerLis = '';
                $.each(data, function (k, v) {
                    containerLis += '<li>' + v.containerNo + '</li>'
                });

                $('#containerIdList').html(containerLis);
                $('#containerIdList li').eq(0).addClass('selectLi')
                var firstContainer = data[0];

                //更改收货地址

                if(firstContainer.deliveryAdress){
                    var addressInfo = '收货地址：' + firstContainer.deliveryAdress.receiver + '，' + firstContainer.deliveryAdress.cellPhone + '，' + firstContainer.deliveryAdress.address + ' ，' + firstContainer.deliveryAdress.zipCode + ''
                    $('#addreddInfo').html(addressInfo);
                    $('#addreddInfo').attr('addressId', firstContainer.deliveryAdress.id);
                }else{
                    var addressInfo = '收货地址：';
                    $('#addreddInfo').html(addressInfo);
                    $('#addreddInfo').removeAttr('addressId');
                }

                if(firstContainer.status>=1){
                    $('#saveCon').hide();
                    $('#subCon').hide();
                    $('.loanBtn').attr('disabled',true).css('background','#999')
                }else{
                    $('#saveCon').show();
                    $('#subCon').show();
                    $('.loanBtn').removeAttr('disabled');
                    $('.loanBtn').css('background','#00aa5c')
                }

                // var list =firstContainer.containerGoodsList
                createDivContainer('#containerAdd', firstContainer);
                var proLists = firstContainer.containerGoodsList;

                goodsProCon(proLists);

                if(proLists.length==0){
                    getAllGoods()
                }

                //计算总数
                var amounts = $('#tabledCreateCon').find('.amount');
                var a = 0;
                $.each(amounts, function (k, v) {
                    a += parseInt(v.children[0].value)
                })
                $('#finallPrice').attr('finallAmount', a)

                //选择运输方式;
                if (firstContainer.deliveryType == 1) {
                    $('input[name="logisticsType"]').eq(0).parents('span').addClass('checked');
                    $('input[name="logisticsType"]').eq(1).parents('span').removeClass('checked')
                } else {
                    $('input[name="logisticsType"]').eq(1).parents('span').addClass('checked');
                    $('input[name="logisticsType"]').eq(0).parents('span').removeClass('checked')
                }

                $('#thisConLoan').val(firstContainer.loanAmount);
                $('#thisLoan').html(firstContainer.orderLoanAmount);
                var surplusLoan = Number(firstContainer.orderLoanAmount)-Number(firstContainer.orderLoaned)
                $('#surplusLoan').html(surplusLoan)

                //加载头部信息
                var tag = '   <span>货柜名称：' + firstContainer.containerName + '</span>' +
                    '<span>货柜批次号：  ' +
                    '<input type="text" id="containerNo" value="' + firstContainer.containerNo + '">' +
                    '</span>' +
                    '<span>状态：' + firstContainer.statusDesc + '</span>';
                $('#containerTableHeader').html(tag);
                $('#containerTableHeader').attr('containerNo', firstContainer.containerNo);
                $('#containerTableHeader').attr('containerId', firstContainer.id);
            },
            complete:function () {
                Loading.close();
            }
        })
    });

//添加一行
    $('#container_info').on('click', '#containerAdd', function () {
        // alert($('#tabledCreateCon tr').length)
        if($('#tabledCreateCon tr').length==1 ){
            getAllGoods()


        }
        else if($('#tabledCreateCon tr').length==0){
            getAllGoods();
            $('#tabledCreateCon tbody').append(newTableRow)
        }
        else{
            var htmlL = $(this).siblings('.form_horizontal').find('table tr:last-of-type').clone();
            $('#tabledCreateCon').append(htmlL);
            $(this).siblings('.form_horizontal').find('table tr:last-of-type').find('.amount input').val('0')
            $(this).siblings('.form_horizontal').find('table tr:last-of-type').find('.priceAll input').val('0')
        }

        var finallPrice = 0;
        var priLists = $('#tabledCreateCon').find('.priceAll');
        $.each(priLists, function (k, v) {
            finallPrice += parseInt(v.children[0].value)
        });
        $('#finallPrice').val(finallPrice)
    });

//更改商品属性
    $('#container_info').on('change', '.in-2', function () {
        // console.log($(this).children('select').find('option:selected').attr());
        changeGoodsProCon(this)
    });

    $('#container_info .priceAll').on('propertychange', function () {
        // alert(3333)
    });

//更改数量
    $('#container_info').on('input', '.amount input', function () {
        this.value = this.value.replace(/[^\d.]/g, "").replace(/(\..*)\./g, '$1').replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3');
        var price = $(this).parents('.amount').siblings('.priceActual').find('input').val();
        $(this).parents('.amount').siblings('.priceAll').find('input').val($(this).val() * price);

        var finallPrice = 0;
        var priLists = $(this).parents('#tabledCreateCon').find('.priceAll');
        $.each(priLists, function (k, v) {
            finallPrice += parseInt(v.children[0].value)
        });
        $('#finallPrice').val(finallPrice);


        var finallAmount = 0;
        var AmountLists = $(this).parents('#tabledCreateCon').find('.amount');
        $.each(AmountLists, function (k, v) {
            finallAmount += parseInt(v.children[0].value)
        });
        $('#finallPrice').attr('finallAmount', finallAmount)
    })
//更改价格
    $('#container_info').on('input', '.priceActual input', function () {
        this.value = this.value.replace(/[^\d.]/g, "").replace(/(\..*)\./g, '$1').replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3');
        var amount = $(this).parents('.priceActual').siblings('.amount').find('input').val();
        $(this).parents('.priceActual').siblings('.priceAll').find('input').val($(this).val() * amount);

        var finallPrice = 0;
        var priLists = $(this).parents('#tabledCreateCon').find('.priceAll');
        $.each(priLists, function (k, v) {
            finallPrice += parseInt(v.children[0].value)
        });
        $('#finallPrice').val(finallPrice)
    });

//切换货柜
    $('#container_info ').on('click', '#containerIdList li', function () {
        var containerNo = $(this).html();
        var orderNo = $('#order').html();

        $(this).addClass('selectLi').siblings('li').removeClass('selectLi');
        var headerData = {
            orderNo: orderNo
        };
        headerData = JSON.stringify(headerData);
        checkCon(headerData, containerNo)
    })

//添加收货地址
    $('#addressBtn').click(function () {
        $('#conMaks').css('display', 'block');
        $('#address_inform').show();

    })

    $('#conMaks').on('click', '#address_inform .cancel', function () {
        $('#conMaks').hide();
        $('#address_inform').hide();
    })

    $('#saveAdressCon').click(function () {

        $("#addAdressCon").submit()
    })

    $('#addressBtnChange').click(function () {
        //收货地址
        $.ajax({
            url: '/admin/user/delivery_address/get_user_receive_address_ajax',
            type: 'post',
            data: {
                orderNo: $('#order').html()
            },
            success: function (response) {
                var list = response.data.receiveAddress;
                var tag = '';
                if(list){
                    $.each(list, function (k, v) {
                        tag += '<label><input type="radio" name="addressInfo"><span addressId="' + v.id + '">' + v.receiver + ',' + v.cellPhone + ',' + v.address + ',' + v.zipCode + '</span></label>'
                    })
                    $('#addressLists').html(tag);
                    $('#conMaks').show();
                    $('#addressLists').show();
                    $('#address_inform').hide();
                }else{
                    bootbox.alert('暂无收货地址，请先添加');
                    $('#conMaks').hide();
                    $('#address_inform').hide();
                }
                var btnTag = '<div class="operteBtn">' +
                    '<input value="确定" id="trueAddress" type="button">' +
                    '<input type="button" id="cancelAddress" value="取消">' +
                    '</div>';

                $('#addressLists').append(btnTag);
            }
        });

    });


//关闭
    $('#conMaks').on('click', '#cancelAddress', function () {
        $('#conMaks').hide();
        $('#addressLists').hide()
    });

    $('#conMaks').on('click', '#trueAddress', function () {
        var listCheck = $('input[name="addressInfo"]');
        $.each(listCheck, function (k, v) {
            if (v.checked == true) {
                var a = $('#addressLists label').eq(k).find('span').html();
                var b = $('#addressLists label').eq(k).find('span').attr('addressId')
                $('#conMaks').hide();
                $('#addressLists').hide();

                $('#addreddInfo').html('收货地址：' + a);
                $('#addreddInfo').attr('addressId', b);

            }
        })
    })

//只能分配金额
    $('.loanBtn').click(function () {
        Loading.open();
        var data = {
            orderNo: $('#order').html(),
            productAmount: $('#finallPrice').val(),
            containerId: $('#containerTableHeader').attr('containerId')
        };
        data = JSON.stringify(data);
        $.ajax({
            url: '/admin/neworder/center/container/loan_amount_ajax',
            type: 'post',
            data: data,
            success: function (response) {
                Loading.close();
                if(response.data.code==200){
                    var money = response.data.data;
                    $('.loanMoney input').val(money)
                }else if(response.data.code!=200){
                    bootbox.alert(response.data.msg)
                }
            }
        })
    })

//保存货柜
    $('#saveCon').click(function () {
        var clearance;
        if ($('input[name="logisticsType"]').eq(0).parents('span').hasClass('checked')) {
            clearance = 1
        } else {
            clearance = 2
        }
        ;

        var containerNo = $('#containerNo').val();

        var totalPrice = $('#finallPrice').val();
        var totalQuantity = $('#finallPrice').attr('finallAmount');
        var deliveryId = $('#addreddInfo').attr('addressId');
        if(deliveryId ==undefined){
            bootbox.alert('请选择收货地址');
            return
        }

        var containerLoan =$('#thisConLoan').val();
        if(containerLoan==''){
            containerLoan=0
        }else{
            containerLoan=parseInt(containerLoan)
        }

        var containerId = $('#containerTableHeader').attr('containerid');

        var containerGoodsList = [];

        var tab_trs = $('#tabledCreateCon tr');
        for (var i = 1; i < tab_trs.length; i++) {
            var tab_tr = tab_trs[i];
            var commodityId = tab_tr.children[1].children[0].selectedOptions[0].getAttribute('commodityid');
            // var commodityName =tab_tr.children[1].children[0].selectedOptions[0].innerHTML;
            var commodityNum = tab_tr.children[2].children[0].value;
            var dealPrice = tab_tr.children[3].children[0].value;
            var totalAmount = tab_tr.children[4].children[0].value;

            var obj = {
                orderNo: $('#order').html(),
                commodityId: commodityId,
                // commodityName:commodityName,
                commodityNum: commodityNum,
                dealPrice: dealPrice,
                totalAmout: totalAmount
            }
            containerGoodsList.push(obj)
        }

        var data = {
            deliveryType: clearance,
            containerId: containerId,
            containerNo: containerNo,
            productAmount: parseInt(totalPrice),
            goodsNum: parseInt($('#finallPrice').attr('finallAmount')),
            goodsList: containerGoodsList,
            containerLoan:containerLoan,
            deliveryId: parseInt(deliveryId)
        };
        data = JSON.stringify(data);
        Loading.open();
        $.ajax({
            url: '/admin/neworder/center/container/modify',
            type: 'post',
            data: data,
            success: function (response) {
                if(response.code==200){
                    bootbox.alert('保存成功');
                    // var orderNo =$('#order').html();
                    // self.location = '/admin/newOrder/orderAudit?orderNo='+orderNo;
                    // $('#containerTab').click()
                }else{
                    bootbox.alert(response.msg)
                }
            },
            complete:function () {
                Loading.close();
            }
        })
    })

    //保存货柜
    $('#subCon').click(function () {
        var clearance;
        if ($('input[name="logisticsType"]').eq(0).parents('span').hasClass('checked')) {
            clearance = 1
        } else {
            clearance = 2
        }
        ;

        var containerNo = $('#containerNo').val();

        var totalPrice = $('#finallPrice').val();
        var totalQuantity = $('#finallPrice').attr('finallAmount');
        var deliveryId = $('#addreddInfo').attr('addressId');

        var containerLoan =$('#thisConLoan').val();
        if(containerLoan==''){
            containerLoan=0
        }else{
            containerLoan=parseInt(containerLoan)
        }


        if(deliveryId ==undefined){
            bootbox.alert('请选择收货地址');
            return
        }

        var containerId = $('#containerTableHeader').attr('containerid');

        var containerGoodsList = [];

        var tab_trs = $('#tabledCreateCon tr');
        for (var i = 1; i < tab_trs.length; i++) {
            var tab_tr = tab_trs[i];
            var commodityId = tab_tr.children[1].children[0].selectedOptions[0].getAttribute('commodityid');
            // var commodityName =tab_tr.children[1].children[0].selectedOptions[0].innerHTML;
            var commodityNum = tab_tr.children[2].children[0].value;
            var dealPrice = tab_tr.children[3].children[0].value;
            var totalAmount = tab_tr.children[4].children[0].value;

            var obj = {
                orderNo: $('#order').html(),
                commodityId: commodityId,
                // commodityName:commodityName,
                commodityNum: commodityNum,
                dealPrice: dealPrice,
                totalAmout: totalAmount
            }
            containerGoodsList.push(obj)
        }

        var data = {
            deliveryType: clearance,
            containerId: containerId,
            containerNo: containerNo,
            productAmount: parseInt(totalPrice),
            goodsNum: parseInt($('#finallPrice').attr('finallAmount')),
            goodsList: containerGoodsList,
            containerLoan:containerLoan,
            deliveryId: parseInt(deliveryId)
        };
        data = JSON.stringify(data);

        Loading.open();
        $.ajax({
            url: '/admin/neworder/center/container/containerSubmit',
            type: 'post',
            data: data,
            success: function (response) {
                if(response.code==200){
                    bootbox.alert('保存成功');
                    var orderNo =$('#order').html();
                    self.location = '/admin/newOrder/orderAudit?orderNo='+orderNo;
                    // tabClick(1)

                }else{
                    bootbox.alert(response.msg);
                    var orderNo =$('#order').html();
                    self.location = '/admin/newOrder/orderAudit?orderNo='+orderNo;
                }
            },
            complete:function () {
                Loading.close();
            }
        })
    })

    //  获取全部商品信息
    function getAllGoods(){
        $.ajax({
            url: '/admin/neworder/goods/find_goods_commodities_by_variety',
            data: {varietyId: 1},
            success: function (response) {
                goodsCommodityList = response.data.goodsCommodityInfos;
                // console.log("goodsCommodityList--",goodsCommodityList);

                newTableRow="<tr>";
                newTableRow+="<td>火龙果</td><td  class='in-2'><select >";
                $.each(goodsCommodityList, function (index, val) {
                    if(index==0){
                        newTableRow += '<option commodityId="' + val.id + '" selected="selected">' + val.name + '</option>';
                    }else{
                        newTableRow += '<option commodityId="' + val.id + '">' + val.name + '</option>';
                    }
                });
                newTableRow+='</select></td><td class="amount"><input value="0"></td>';
                newTableRow+='<td class="priceActual"><input type="number" value="'+goodsCommodityList[0].priceActual+'"></td>';
                newTableRow+='<td class="priceAll"><input type="text" value="0"></td></tr>';

                $('#tabledCreateCon tbody').append(newTableRow)
            }
        })

    }
})