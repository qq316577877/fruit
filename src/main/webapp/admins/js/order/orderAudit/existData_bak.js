var addIdNum;
var addIdNum2;
var dataType2;
var status;
$(function () {

    var clo_tab;
    var restrictSum;
    var orderNo = $('#order').html();
    clo_tab = ($('#pro_list div:nth-of-type(1)').clone())[0].outerHTML;
    $('#pro_list').remove();

    //禁用选择类型的按钮
    $('#agency').attr('disabled', 'true');
    $('#direct').attr('disabled', 'true');
    //清除原来的结构

    //返回首页
    $('#index').click(function () {

        location.href = "/admin"
    })
    //如果数据存在  就先加载缓存的数据
    $.ajax({
        url: '/admin/order/tab_order_ajax',
        data: {
            orderId: orderNo
        },
        success: function (data) {
            if (data.data.status == 1) {
                $('#pass').val('暂存订单');
                $('#orderTwo').css('display', 'none');
                $('#pass').click(function () {
                    bootbox.alert('订单为暂存状态')
                });
                $('#pass').css('display', 'none');
                $('#next_1').css('display', 'none');
            }
            ;
            if (data.data.status == 2) {
                $('#pass').click(function () {
                    $.ajax({
                        url: '/admin/order/confirm_audit_ajax',
                        data: {
                            orderId: orderNo
                        },
                        success: function (data) {
                            bootbox.alert('审核成功');
                            location.href = '/admin/order/list';
                        }
                    })
                });
            }
            if (data.data.status == 3) {
                $('#pass').click(function () {
                    $.ajax({
                        url: '/admin/order/confirm_audit_ajax',
                        data: {
                            orderId: orderNo
                        },
                        success: function (data) {
                            bootbox.alert('审核成功');
                            location.href = '/admin/order/list';
                        }
                    })
                });
            }
            if (data.data.status == 4) {
                $('#pass').val('签订合同');
                $('#next_1').attr('disabled', true)
                $('input[type="radio"]').parents('.radio').addClass('disabled');
                var selectList = $('select');
                $.each(selectList, function (k, v) {
                    v.disabled = true
                });


                $('#pass').on('click', function () {

                    $.ajax({
                        url: '/admin/order/confirm_contract_ajax',
                        data: {
                            orderId: orderNo
                        },
                        success: function (data) {
                            bootbox.alert('上传成功');
                            location.href = '/admin/order/list'
                        }
                    })
                })
            }
            if (data.data.status == 5) {
                $('#pass').val('确认收款');
                $('#next_1').css('display','none');
                $('input[type="radio"]').parents('.radio').addClass('disabled');
                var selectList = $('select');
                $('#next_step').css('display','none');
                $('#canc').css('display','none');


                // $('#note')[0].disabled = true;
                // var inputList = $('input[type="text"]');
                //
                // $.each(inputList, function (k, v) {
                //     v.disabled = true
                // });

                // $('#settle1')[0].disabled = false;
                // $('#settle2')[0].disabled = false;
                $('#pass').on('click', function () {
                    $.ajax({
                        url: '/admin/order/confirm_pay_ajax',
                        data: {
                            orderId: orderNo
                        },
                        success: function (data) {
                            if (data.code == 200) {
                                bootbox.alert('确认收款成功');
                                location.href = '/admin/order/list'
                            }
                        }
                    })
                })

            }
            if (data.data.status == 6) {
                // $('#next_step').attr('disabled', true);
                $('#pass').css('display', 'none');
                $('#canc').css('display','none');
                $('#next_step').css('display','none');
            }
            if(data.data.status==9){
                $('#pass').css('display','none');
                $('#canc').css('display','none');
            }
            if (data.data.status == 10) {
                $('#pass').val('订单结算');
                $('#canc').css('display','none');
                $('#next_step').css('display','none');
                var selectList = $('select');
                $.each(selectList, function (k, v) {
                    v.disabled = true
                });
                // $('#note')[0].disabled = true;
                var inputList = $('input[type="text"]');


                // $('#settle1')[0].disabled = false;
                // $('#settle2')[0].disabled = false;
                $('#pass').on('click', function () {


                    $.ajax({
                        url: '/admin/order/finish_pay_ajax',
                        data: {
                            orderId: orderNo
                        },
                        type: 'post',
                        success: function (data) {
                            if (data.code == 200) {
                                bootbox.alert('订单结算成功');
                                location.href = '/admin/order/list'
                            }else{
                                bootbox.alert(data.msg);
                            }
                        },

                    })
                })

            }

            if (data.data.status == 11) {
                $('#pass').css('display','none');
                $('#canc').css('display','none');
                $('#next_1').css('display','none');
            }
            if (data.data.status == 100) {
                $('.audit_btn').css('display', 'none');
                $('#canc').css('display','none');
                $('#next_step').css('display','none');
            }
            ;
            var suppier = data.data.supplierList;
            var data = data.data;
            $('#order').attr('type', data.type);
            status = data.status;
            var dataType = data.type;
            dataType2 = data.type;

            //判断选择的type
            if (data.type == 1) {
                $('#agency').parents('span').addClass('checked');
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

                        $('.selsct option').eq(k + 1).attr('selected', true);
                        $('.selsct option').eq(0).attr('selected', false);
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

            //清空表单
            var tableList = $('.pro-list-1');
            var addLists = $('.ad');

            for (var i = 0; i < tableList.length; i++) {
                tableList[i].outerHTML = ''
            }
            for (var j = 0; j < addLists.length; j++) {
                addLists[j].outerHTML = ''
            }

            //添加数据
            var list = data.orderContainers;

            var thisdivId = 1;
            var productName, rank, size, type_pro;
            createOrderDivs('orderList', list, thisdivId);
            var i = 1;
            $('#ad_1').css('display', 'none');
            $.each(list, function (k, v) {
                var parameter = v.orderContainerDetails
                productName = v.productName;
                two(productName, i, parameter);
                i++
            })


            matching();

            addIdNum = thisdivId;
            var thisTrNum = 1;
            $.each(list, function (k, v) {
                $('.main').on('click', '#orderList #tr' + thisTrNum, function () {
                    var a = ($(this).siblings('table').find('tr:nth-of-type(2)').clone())[0].innerHTML;
                    var first_n = $(this).siblings('table').find('.first-td')[0].value;
                    var tr = document.createElement('tr');
                    tr.innerHTML = a;
                    $(this).siblings('table').append(tr);
                    $(this).siblings('table').find('.first-td:last').val(first_n);
                    $(this).siblings('table').find('tr:last-of-type').find('.amount input').val('0');
                });
                thisTrNum++
            });

            //添加货柜
            // var addList = '<button class="ad" id="ad_1">新增一个货柜</button>';
            // $('#orderList').append(addList);
            // $('#ad_1').css('display', 'block');
            if (status == 4) {
                var inputs = $('input[type="7"]');
            }

        }
    });


    $('#next_1').css('display', 'block');
    //添加

    $('.mask').on('click', '.cancel ', function () {
        $('.mask').css('display', 'none');
        $('#inform2').css('display', 'none');
    });

    //取消订单
    $('.audit_info').on('click', '#canc', function () {
        $('.mask').css('display', 'block');
        $('#canOrder').css('display', 'block');
    });
    $('.mask').on('click', '#can_2', function () {
        $('.mask').css('display', 'none');
        $('#canOrder').css('display', 'none');
    })
    $('.mask').on('click', '#can_1', function () {
        $('.mask').css('display', 'none');
        $('#canOrder').css('display', 'none');
        $.ajax({
            url: '/admin/order/order_cancle_ajax',
            type: 'post',
            data: {
                orderId: orderNo
            },
            success: function (data) {

                if (data.code == 200) {
                    bootbox.alert('取消成功');
                    // location.href ='/admin/order/list';
                }
            }
        })
    });

    //获取总数量
    $('.main').on('input propertychange', '.price input', function () {

        var price = $(this).val();
        var amount = $(this).parents('.price').siblings('.amount').children('input').val();
        if (amount == '' && price !== '') {
            amount = 0
        } else if (amount !== '' && price == '') {
            price = 0
        }
        var amountPrice = parseFloat(amount) * parseFloat(price);
        var price = $(this).parents('.price').siblings('.price-all').children('input').val(amountPrice);

        var amount_tos = $(this).parents('.price').parents('tr').parents('table').find('.price-all');

        var amountTotal = 0;
        for (var i = 0; i < amount_tos.length; i++) {
            amountTotal += parseFloat(amount_tos[i].children[0].value)
        }
        $(this).parents('.price').parents('tr').parents('table').siblings('.sum').children('input').val(amountTotal);
    });

    //获取总数量
    $('.main').on('input propertychange', '.amount input', function () {
        var amount = $(this).val();
        var price = $(this).parents('.amount').siblings('.price').children('input').val();
        if (amount == '' && price !== '') {
            amount = 0
        } else if (amount !== '' && price == '') {
            price = 0
        }
        var amountPrice = parseFloat(amount) * parseFloat(price);
        var price = $(this).parents('.amount').siblings('.price-all').children('input').val(amountPrice);
        var amount_tos = $(this).parents('.amount').parents('tr').parents('table').find('.price-all');

        var amountTotal = 0;
        for (var i = 0; i < amount_tos.length; i++) {
            amountTotal += parseFloat(amount_tos[i].children[0].value)
        }
        //$(this).parents('.amount').parents('tr').parents('table').siblings('.hide_inform').children('input').eq(0).val(amountTotal);
        $(this).parents('.amount').parents('tr').parents('table').siblings('.sum').children('input').val(amountTotal);
    });

    $('.main').on('click', '.cancel', function () {
        $(this).parents('.x').parents('.pro-list-head').parents('.pro-list-1').remove()
    })
    $('.main').on('click', '.cancel', function () {
        $(this).parents('.inform-head').parents('#inform2').css('display', 'none');
        $('.mask').css('display', 'none');
    })
    $('#next_1').on('click', function () {
        //验证至少有一个货柜
        var containerLists = $('.pro-list-1');
        if (containerLists.length == 0) {
            bootbox.alert('至少存在一个货柜');
            return
        }


        var i = addIdNum2;
        //计算总量

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
        ;

        // var sumList = '<h3 class="container-norms">货柜规格：' + sum + '箱</h3>';
        // $('#list' + (i - 1)).find('.container-norms').remove();
        // $('#list' + (i - 1)).find('.x').before(sumList);
        $('#div' + i).find('.container-norms').attr('totalquantity', sum);

        var sumLi = $('#orderList').find('.container-norms');

        var totalAmount = 0;//总数量
        var totalAmountList = $('.container-norms');

        $.each(totalAmountList, function (k, v) {
            totalAmount += parseInt(v.getAttribute('totalquantity'));

        });

        //获取总价格
        var priceAll = 0;
        var amountList = $('.sum');
        $.each(amountList, function (k, v) {
            priceAll += parseInt(v.children[1].value)
        });
        var num;

        var dataType;
        if ($('#agency').parents('span').hasClass('checked')) {
            dataType = 1
        } else {
            dataType = 2
        }
        var data = giveTheFinalValues($('#orderList .pro-list-1'), dataType, totalAmount, priceAll, orderNo);
        data = JSON.stringify(data);

        $.ajax({
            url: '/admin/order/create_order_ajax',
            data: data,
            type: 'post',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                if (data.code == 200) {
                    bootbox.alert('保存成功')
                } else {
                    bootbox.alert('非法操作');
                    $('#next_1').attr('disabled', 'disabled');
                    $('#next_1').css('background', '#ccc');
                }
            }
        });
        //location.href = 'orderReviewTwo.html?' + orderNo;
    });
    //定义匹配数量价格匹配正整数
    function matching() {
        var priceInput = $('.price');
        var amountInput = $('.amount');
        $('.price input').on('input', function () {
            //this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1').
            //replace(".", "$#$").replace(/\./g, "").replace("$#$", ".").replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3');
            this.value = this.value.replace(/[^\d.]/g, "").replace(/(\..*)\./g, '$1').replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3');
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

    function two(productName, i, parameter) {
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
                        $('#div' + i)[0].getElementsByClassName('.first-td').value = productName;
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