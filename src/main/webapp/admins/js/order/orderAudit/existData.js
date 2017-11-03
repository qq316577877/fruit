/**
 * 订单初始化
 * Created by 杨隆
 */

var addIdNum;   // 货柜的
var addIdNum2;  // 新增的货柜id
var dataType2;  // 订单类型
var status;     // 订单状态

$(function () {

    var orderNo;    // 订单号
    var auditMsg="";    // 审核提示信息
    var requestUrl="";
    var supplierList=[];    // 供应商列表
    // 页面初始化
    init();
    // 绑定审核按钮
    $('#pass').click(auditOrder);

    // 审核订单方法
    function auditOrder() {
        $.ajax({
            url: requestUrl,
            data: {orderId: orderNo},
            success: function (data) {
                bootbox.alert(auditMsg);
                location.href ='/admin/order/list';
            }
        })
    }

    // 页面初始化
    function init(){
        var restrictSum;
        orderNo = $('#order').html();
        $('#pro_list').remove();

        //清除原来的结构
        //如果数据存在  就先加载缓存的数据
        $.ajax({
            url: '/admin/order/tab_order_ajax',
            data: {
                orderId: orderNo
            },
            success: function (data) {
                if (data.data.status == 1) {
                    $('#pass').val('暂存订单');
                    $('#orderTwo').css('display','none')
                };
                if (data.data.status == 2||data.data.status == 3) {
                    auditMsg="审核成功";
                    requestUrl='/admin/order/confirm_audit_ajax';
                };
                if (data.data.status == 4) {
                    $('#pass').val('签订合同');
                    $('#next_1').attr('disabled',true)
                    $('input[type="radio"]').parents('.radio').addClass('disabled');
                    var selectList = $('select');
                    $.each(selectList, function (k, v) {
                        v.disabled = true
                    });
                    $('#note')[0].disabled = true;
                    var inputList = $('input[type="text"]');

                    $.each(inputList, function (k, v) {
                        v.disabled = true
                    });
                    auditMsg="上传成功";
                    requestUrl='/admin/order/confirm_contract_ajax';
                }

                if (data.data.status == 5) {
                    $('#pass').val('确认收款');
                    $('input[type="radio"]').parents('.radio').addClass('disabled');
                    var selectList = $('select');
                    $.each(selectList, function (k, v) {
                        v.disabled = true
                    });
                    $('#note')[0].disabled = true;
                    var inputList = $('input[type="text"]');

                    $.each(inputList, function (k, v) {
                        v.disabled = true
                    });

                    auditMsg="确认收款成功";
                    requestUrl='/admin/order/confirm_pay_ajax';
                };
                if(data.data.status==6){
                    $('#next_step').attr('disabled',true)
                };
                if (data.data.status ==10) {
                    $('#pass').val('订单结算');
                    var selectList = $('select');
                    $.each(selectList, function (k, v) {
                        v.disabled = true
                    });
                    $('#note')[0].disabled = true;
                    var inputList = $('input[type="text"]');

                    $.each(inputList, function (k, v) {
                        v.disabled = true
                    });

                    auditMsg="订单结算成功";
                    requestUrl='/admin/order/finish_pay_ajax';
                };
                if(data.data.status==100){
                    $('#pass').val('订单完成');
                    $('#pass').click(function () {
                        bootbox.alert('订单完成');
                        location.href ='/admin/order/list'
                    })
                };

                var suppier = data.data.supplierList;
                supplierList=suppier;
                var data = data.data;
                $('#order').attr('type',data.type);
                status = data.status;
                var dataType = data.type;
                dataType2 = data.type;

                // 判断选择的type
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
                    var supplierHtmlList="";
                    $.each(supplierList, function (k, val) {
                        if(val.id==id){
                            supplierHtmlList += '<option selected supplierid="' + val.id + '">' + val.supplierContact + '</option>';
                        }else{
                            supplierHtmlList += '<option supplierid="' + val.id + '">' + val.supplierContact + '</option>';

                        }
                    });
                    $('.supplier-2 select').html(supplierHtmlList);

                    $('.selsct').change(function () {
                        supplierChange();
                    });

                    // 刷新供应商
                    supplierChange();
                }

                //清空表单
                var tableList = $('.pro-list-1');
                var addLists = $('.ad');

                for (var i = 0; i < tableList.length; i++) {
                    tableList[i].outerHTML=''
                }
                for(var j =0;j<addLists.length;j++){
                    addLists[j].outerHTML =''
                }

                //添加数据
                var list = data.orderContainers;

                var thisdivId = 1;
                var productName, rank, size, type_pro;
                var i = 1;
                var productName, rank, size, type_pro;
                $.each(list, function (k, v) {

                    $('#orderList').append(clo_tab);
                    $('.pro-list-1')[k].id = 'list' + i;
                    $('.pro-list-1').eq(k).find('.add-h-1').attr('id', 'tr' + i);
                    var tabInfo = list[k];
                    //添加表头

                    var tag1 =
                        '<h3>货柜批次号：' + v.batchNumber + '</h3>' +
                        '<h3 class="container-name" productid="'+v.productId+'">货柜名称：' + v.productName + '</h3>' +
                        '<h3 class="container-norms">货柜规格：0-' + v.maxQuantity + '箱</h3>' +
                        '<div class="x">' +
                        '<i class="iconfont icon-chacha cancel cancel-1 fr"></i>' +
                        '</div>';
                    productName = v.productName;
                    restrictSum =v.maxQuantity;
                    $('#list' + i).children('.pro-list-head').html(tag1);
                    $('#list' + i).find('.hide_inform').children('input')[0].value = v.totalQuantity;
                    $('#list' + i).find('.hideSum').val(v.totalQuantity)
                    $('#list' + i).find('.sum').find('input').val(v.totalPrice)
                    $('#list' + i).find('.hide_inform').attr('totalAmount',v.totalAmount)



                    var parameter = v.orderContainerDetails;
                    //这里
                    two(productName,i,parameter);

                    $('.main').on('click', '#orderList #tr' + i, function () {
                        var a = ($(this).siblings('table').find('tr:nth-of-type(2)').clone())[0].innerHTML;
                        var first_n = $(this).siblings('table').find('.first-td')[0].value;
                        var tr = document.createElement('tr');
                        tr.innerHTML = a;
                        $(this).siblings('table').append(tr);
                        $(this).siblings('table').find('.first-td:last').val(first_n);
                    })
                    i++;
                    addIdNum = i
                })
                // createOrderDivs('orderList',list,thisdivId);
                console.log("aaa");
                matching();

                addIdNum = thisdivId;
                var thisTrNum=1;
                $.each(list,function (k, v) {
                    $('.main').on('click', '#orderList #tr'+thisTrNum, function () {

                        var a = ($(this).siblings('table').find('tr:nth-of-type(2)').clone())[0].innerHTML;
                        console.log(a);
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
                var addList = '<button class="ad" id="ad_1">新增一个货柜</button>';
                $('#orderList').append(addList);
                $('#ad_1').css('display', 'block');
                if(status ==4){
                    var inputs =$('input[type="7"]');
                }
            }
        })
    }

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

    // 取消关闭
    $('#next_1').css('display', 'block');
    $('.mask').on('click','.cancel ',function () {
        $('.mask').css('display','none')
    })

    //取消订单
    $('.audit_info').on('click','#canc',function () {
        $('.mask').css('display','block');
        $('#canOrder').css('display','block')
    });
    $('.mask').on('click','#can_2',function () {
        $('.mask').css('display','none');
        $('#canOrder').css('display','none')
    })
    $('.mask').on('click','#can_1',function () {
            $('.mask').css('display','none');
            $('#canOrder').css('display','none');
            $.ajax({
                url:'/admin/order/order_cancle_ajax',
                type:'post',
                data:{
                    orderId:orderNo
                },
                success: function (data) {

                    if(data.code==200){
                        bootbox.alert('取消成功');
                        location.href ='/admin/order/list';
                    }
                }
            })
    })

    //获取总数量
    $('.main').on('input propertychange', '.price input', function () {

        var price = $(this).val();
        var amount = $(this).parents('.price').siblings('.amount').children('input').val();
        if(amount==''&& price!==''){
            amount=0
        }else if(amount!==''&& price==''){
            price=0
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
        if(amount==''&& price!==''){
            amount=0
        }else if(amount!==''&& price==''){
            price=0
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

    // 保存
    $('#next_1').on('click',function () {
        var i =addIdNum2
        //计算总量
        var sums_1 = $('#div' + (i)).children('.pro-list-body').find('.amount').children('input');
        var sum = 0;

        var num;
        for (var w = 0; w < sums_1.length; w++) {
            num = Number(sums_1[w].value);
            sum += num;
            //console.log(sum);
        }

        if (sum > restrictSum) {
            bootbox.alert('超出货柜规格');
            return
        };

        $('#div' + i).find('.container-norms').attr('totalquantity',sum);
        var sumLi = $('#orderList').find('.container-norms');
        var totalAmount = 0;//总数量
        var totalAmountList=$('.hideSum');
        $.each(totalAmountList, function (k, v) {
            totalAmount+=parseInt(v.value)
        })

        //获取总价格
        var priceAll =0;
        var amountList =$('.sum');
        $.each(amountList, function (k, v) {
            priceAll+=parseInt(v.children[1].value)
        })
        var num;

        var dataType;
        if($('#agency').parents('span').hasClass('checked')){
            dataType=1
        }else{
            dataType=2
        }
        var data = giveTheFinalValues($('#orderList .pro-list-1'), dataType, totalAmount,priceAll,orderNo);
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
                }else{
                    bootbox.alert('非法操作');
                    $('#next_1').attr('disabled','disabled');
                    $('#next_1').css('background','#ccc');
                }
            }
        });
    });

    //定义匹配数量价格匹配正整数
    function matching() {

        $('.price input').on('input', function() {

            this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');
        });

        var priceInput = $('.price');
        var amountInput = $('.amount');
        for (var i = 0; i < priceInput.length; i++) {
            priceInput[i].children[0].onkeyup = function () {
                console.log("开始校验啦");
                this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');
                //if (this.value.length == 1) {
                //    this.value = this.value.replace(/[^1-9]/g, '')
                //} else {
                //    this.value = this.value.replace(/\D/g, '')
                //}
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
    }
    function two (productName,i,parameter) {
        var rank, size, type;
        $.ajax({
            url: '/order/find_all_goods',
            success: function (data) {

                var allGoods = data.data;

                for (var k in allGoods) {
                    if (allGoods[k].name == productName) {
                        $('#list' + i).find('.two-th').html(allGoods[k].productDetails[0].name);
                        $('#list' + i).find('.two-th').attr('engName',allGoods[k].productDetails[0].engName);
                        $('#list' + i).find('.three-th').html(allGoods[k].productDetails[2].name);
                        $('#list' + i).find('.three-th').attr('engName',allGoods[k].productDetails[2].engName);
                        $('#list' + i).find('.four-th').html(allGoods[k].productDetails[1].name);
                        $('#list' + i).find('.four-th').attr('engName',allGoods[k].productDetails[1].engName);


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
})