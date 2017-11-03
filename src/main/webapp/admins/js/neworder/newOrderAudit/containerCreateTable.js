/**
 * Created by yl on 2017/9/28.
 */

// 创建完整的一个表格结构

function createDivContainer(zhuId, list) {
    var bigDiv = document.createElement('div');
    bigDiv.className = 'form_horizontal';
    bigDiv.id="containerBigDiv"
    var body = document.createElement('div');
    // body.className = 'pro-list-body';
    bigDiv.appendChild(body);
    createTab1Con(body, list);

    $(zhuId).before(bigDiv);

}

/**
 * 一个订单生成一个table，放到订单div里
 * @param parentDiv
 * @param order
 */
//封装创建第一个表单
function createTab1Con(parentDiv, list) {
    var tableNode = document.createElement("table");
    tableNode.id = 'tabledCreateCon';
    // var tag1 =
    //     ' <tr>' +
    //     '                <th>商品名称</th>' +
    //     '                <th class="two-th">品种</th>' +
    //     '                <th class="three-th">等级</th>' +
    //     '                <th class="four-th">大小</th>' +
    //     '                <th>数量</th>' +
    //     '                <th>低位报价/箱</th>' +
    //     '                <th>高位报价/箱</th>' +
    //     '                <th>参考单价</th>' +
    //     '                <th>合计</th>' +
    //     '            </tr>';
    var tag1 =
        ' <tr>' +
        '                <th >商品类别</th>' +
        '                <th>商品名称</th>' +
        '                <th>数量</th>' +
        '                <th>成交价</th>' +
        '                <th>合计</th>' +
        '            </tr>';
    tableNode.innerHTML = tag1;

    var containerDetails = list.containerGoodsList;
    // console.log(list);
    // var rowNum = containerDetails.length;
    for (var x = 0; x < containerDetails.length; x++) {
        var containerDetail = containerDetails[x];
        var trNode = tableNode.insertRow();

        //第一列
        var tdNode0 = trNode.insertCell(0);
        tdNode0.innerHTML = '' + containerDetail.varietyName;

        //第二列
        var tdNode1 = trNode.insertCell(1);
        tdNode1.innerHTML = ' <td class="in-2">' +
            '<select>' +
            '<option commodityId="' + containerDetail.commodityId + '">' + containerDetail.commodityName + '</option>' +
            '</select>' +
            '</td>';
        tdNode1.className = 'in-2';

        //第五列
        var tdNode2 = trNode.insertCell(2);
        tdNode2.innerHTML = '<input value="' + containerDetail.commodityNum + '"/>';
        tdNode2.className = 'amount'


        //第七列
        var tdNode3 = trNode.insertCell(3);
        tdNode3.innerHTML =
            '<input value="' + containerDetail.dealPrice + '"/>';
        tdNode3.className = 'priceActual';

        //第八列
        var tdNode4 = trNode.insertCell(4);
        tdNode4.innerHTML = '<input disabled value="'+containerDetail.totalAmount+'"/>';
        tdNode4.className = 'priceAll';
        tdNode4.id='priceAllCon'
    }
    parentDiv.appendChild(tableNode);//添加到那个位置

}


/*
 * 加载属性
 * */
function goodsProCon(goodsInfoPro) {
    $.ajax({
        url: '/admin/neworder/goods/find_goods_commodities_by_variety',
        data: {
            varietyId: 1
        },
        success: function (response) {
            var list = response.data.goodsCommodityInfos;
            var option;
            $.each(list, function (k, v) {
                option += '<option commodityId="' + v.id + '">' + v.name + '</option>'
            });
            $('.in-2 select').html(option);
            $.each(list, function (k, v) {
                for (var i = 0; i < goodsInfoPro.length; i++) {
                    // console.log(v.id);
                    if (v.id == goodsInfoPro[i].commodityId) {
                        // console.log($('.in-2').eq(i).find('select option'));
                        $('#tabledCreateCon .in-2').eq(i).find('select option').eq(k).attr('selected', true);

                    }
                }
            });
            var priceAll = 0;
            var priceLists = $('#tabledCreateCon .priceAll');
            $.each(priceLists, function (k, v) {
                priceAll += parseInt(v.children[0].value)
            });
            $('#finallPrice').val(priceAll);
        }
    })
}

/*
 * 更换商品属性
 * */
function changeGoodsPro(that) {
    $.ajax({
        url: '/admin/neworder/goods/find_goods_commodities_by_variety',
        data: {
            varietyId: 1
        },
        success: function (response) {
            var list = response.data.goodsCommodityInfos;
            $.each(list, function (k, v) {
                if (v.id == $(that).children('select').find('option:selected').attr('commodityid')) {
                    $(that).siblings('.priceLow').html(v.priceLow);
                    $(that).siblings('.priceHigh').html(v.priceHigh);
                    $(that).siblings('.priceActual').find('input').val(v.priceActual);
                    var pri = v.priceActual;
                    var num = $(that).siblings('.amount').find('input').val();
                    $(that).siblings('.priceAll').html(pri * num)
                }
            });

        }
    })
}


function changeGoodsProCon(that) {
    $.ajax({
        url: '/admin/neworder/goods/find_goods_commodities_by_variety',
        data: {
            varietyId: 1
        },
        success: function (response) {
            var list = response.data.goodsCommodityInfos;
            $.each(list, function (k, v) {
                if (v.id == $(that).children('select').find('option:selected').attr('commodityid')) {
                    $(that).siblings('.priceLow').html(v.priceLow);
                    $(that).siblings('.priceHigh').html(v.priceHigh);
                    $(that).siblings('.priceActual').find('input').val(v.priceActual);
                    var pri = v.priceActual;
                    var num = $(that).siblings('.amount').find('input').val();
                    var tag ='<input value="'+pri * num+'"/>';
                    // console.log(tag);

                    // $(that).siblings('.priceAll').html(pri * num);
                    $(that).siblings('#priceAllCon').html(tag);
                }
            });

        }
    })
}



//切换货柜
function checkCon(headerData,ulConNum) {
    Loading.open();
    $.ajax({
        url: '/admin/neworder/center/container/container_detail_orderno_ajax',
        data: headerData,
        type: 'post',
        success: function (response) {
            var data = response.data;
            //创建货柜列表并默认加载第一个
            Loading.close();
            $.each(data,function (k,v) {
                if(v.containerNo==ulConNum){
                    var firstContainer = v;
                    if(firstContainer.deliveryAdress==null){
                        // $('#addressBtn').val('添加收货地址');
                        var addressInfo = '收货地址：';
                        $('#addreddInfo').html(addressInfo);
                        // $('#addreddInfo').html(addressInfo);
                        $('#addreddInfo').removeAttr('addressId');
                    }else{
                        // $('#addressBtn').val('修改收货地址');
                        var addressInfo = '收货地址：' + firstContainer.deliveryAdress.receiver + '，' + firstContainer.deliveryAdress.cellPhone + '，' + firstContainer.deliveryAdress.address + ' ，' + firstContainer.deliveryAdress.zipCode + ''
                        $('#addreddInfo').html(addressInfo);
                        $('#addreddInfo').attr('addressId',firstContainer.deliveryAdress.id);
                    }

                    if(firstContainer.containerGoodsList==null || firstContainer.containerGoodsList.length==0){
                        // bootbox.alert('暂无货柜信息');
                       window.localStorage.setItem('trLength',1)
                        var list =$('#tabledCreateCon tr');
                        for(var i =1;i<list.length;i++){
                            list[i].outerHTML=''
                        }
                        $('#finallPrice').val('0');
                        $('#containerTableHeader').attr('containerId', firstContainer.id);
                        $('#containerNo').val(firstContainer.containerNo);
                        //加载头部信息
                        var tag ='   <span>货柜名称：'+firstContainer.containerName+'</span>'+
                            '<span>货柜批次号：  '+
                            '<input id="containerNo"  type="text" value="'+firstContainer.containerNo+'">'+
                            '</span>'+
                            '<span>状态：'+firstContainer.statusDesc+'</span>';
                        $('#containerTableHeader').html(tag);
                        $('#containerTableHeader').attr('containerNo',firstContainer.containerNo);
                        $('#containerTableHeader').attr('containerId', firstContainer.id);
                    }else{
                        $('.containerInfo').show();
                        $('#containerBigDiv').remove();
                        // var list =firstContainer.containerGoodsList
                        createDivContainer('#containerAdd',firstContainer);
                        var proLists =firstContainer.containerGoodsList;

                        goodsProCon(proLists);


                        //选择运输方式;

                        if (firstContainer.deliveryType == 1) {
                            $('input[name="logisticsType"]').eq(0).parents('span').addClass('checked');
                            $('input[name="logisticsType"]').eq(1).parents('span').removeClass('checked')
                        } else {
                            $('input[name="logisticsType"]').eq(1).parents('span').addClass('checked');
                            $('input[name="logisticsType"]').eq(0).parents('span').removeClass('checked')
                        }

                        //计算总数
                        var amounts =$('#tabledCreateCon').find('.amount');
                        var a =0;
                        $.each(amounts,function (k, v) {
                            a+=parseInt(v.children[0].value)
                        })
                        $('#finallPrice').attr('finallAmount', a)

                        //加载头部信息
                        var tag ='   <span>货柜名称：'+firstContainer.containerName+'</span>'+
                            '<span>货柜批次号：  '+
                            '<input id="containerNo"  type="text" value="'+firstContainer.containerNo+'">'+
                            '</span>'+
                            '<span>状态：'+firstContainer.statusDesc+'</span>';
                        $('#containerTableHeader').html(tag);
                        $('#containerTableHeader').attr('containerNo',firstContainer.containerNo);
                        $('#containerTableHeader').attr('containerId', firstContainer.id);
                    }

                    $('#thisConLoan').val(firstContainer.loanAmount);
                    $('#thisLoan').html(firstContainer.orderLoanAmount);
                    var surplusLoan = Number(firstContainer.orderLoanAmount)-Number(firstContainer.orderLoaned)
                    $('#surplusLoan').html(surplusLoan)
                    if(firstContainer.status>=1){
                        $('#saveCon').hide();
                        $('#subCon').hide();
                        $('.loanBtn').attr('disabled',true).css('background','#999')
                    }else{
                        $('#saveCon').show();
                        $('#subCon').show();
                        $('.loanBtn').removeAttr('disabled').css('background','#00aa5c')
                    }
                    // $('.loanMoney span').html('0')
                }
            })
        }
    })


}

//新增收货地址
$(function() {
    var tel = /0?(13|14|15|17|18)[0-9]{9,10}$/;
    var areaNum = /^[0-9]{3,4}$/;
    var tesePhone = /^[0-9]{7,10}$/;
    //validate控件

    $("#addAdressCon").validate({
        errorClass: 'error formError errorBottom', // default input error message class
        debug: false, //调试模式取消submit的默认提交功能
        focusInvalid: false, //当为false时，验证无效时，没有焦点响应
        onkeyup: false,
        submitHandler: function (form) {   //表单提交句柄,为一回调函数，带一个参数：form

            //判断选填的内容
            if ($('#cellPhone').val() == '' && $('#phoneNum').val() == '') {
                $(".reminder").html("手机号或座机号必填一个");
                return
            }
            ;

            if ($('#cellPhone').val() != '') {
                if ($('#cellPhone-c option:selected').html() == '选择国家') {
                    $('#cellPhone-c').siblings('.judge').html('请选择国家').css('display', 'block');
                    return
                } else if (!tel.test($('#cellPhone').val())) {
                    $('#cellPhone-c').siblings('.judge').html('请输入有效的手机号码').css('display', 'block');
                    return
                }
            }
            ;

            if ($('#phoneNum').val() != '') {
                if ($('#phoneNum-c option:selected').html() == '选择国家') {
                    $('#phoneNum-c').siblings('.judge').html('请选择国家').css('display', 'block');
                    return
                } else if (!areaNum.test($('#area').val())) {
                    $('#phoneNum-c').siblings('.judge').html('区号只能输入3-4位数字').css('display', 'block');
                    $('#area').focus()
                    return
                } else if (!tesePhone.test($('#phoneNum').val())) {
                    $('#phoneNum-c').siblings('.judge').html('只能输入数字7-10位').css('display', 'block');
                    return
                }
            }

            var receiver = $('#receiver').val();
            var countryId = $('#country').val();
            var provinceId = $('.selectList>.province').find("option:selected").attr('value')
            var cityId = $('.selectList>.city').find("option:selected").attr('value')
            var districtId = $('.selectList>.district').find("option:selected").attr('value')
            var address = ($('#address').val());
            var zipCode = $('#zipCode').val();
            //var cellPhone = $('#cellPhone-c option:selected').attr('countryid')+'-'+ $('#cellPhone').val();
            var cellPhone =  $('#cellPhone').val();
            if(cellPhone==''){
                cellPhone=''
            }else{
                cellPhone = '086-'+ $('#cellPhone').val();
            }
            //var supplierName = $('#supplierName').val();
            var Area = $('#area').val();
            var phoneNum = '086-'+ Area + '-' + $('#phoneNum').val();

            if (Area == '') {
                phoneNum = ''
            } else {
                phoneNum = '086-'+ Area + '-' + $('#phoneNum').val();
            }

            var orderNo =$('#order').html();
            //发送请求
            $.ajax({
                url: '/admin/user/delivery_address/add_user_receive_address_ajax',
                type:'post',
                data:   {
                    receiver: receiver,
                    countryId: countryId,
                    provinceId: provinceId,
                    cityId: cityId,
                    districtId: districtId,
                    address: address,
                    zipCode: zipCode,
                    cellPhone: cellPhone,
                    phoneNum: phoneNum,
                    orderNo:orderNo
                },
                success: function (response) {
                    if(response.code==200){
                        bootbox.alert('添加成功');
                        $('#conMaks').hide();
                        $('.inform').hide();
                        var orderNo =$('#order').html();
                        self.location = '/admin/newOrder/orderAudit?orderNo='+orderNo;
                        window.localStorage.setItem('addAddressSuccess',1);
                    }
                }
            });
        },
        rules: {
            country: {
                required: true,
                min: 1
            },
            province: {
                required: true,
                min: 1
            },
            city: {
                required: true,
                min: 1
            },
            district: {
                required: true,
                min: 1
            },
            address: {
                required: true,
                //isAddress: true,
                limitSumAdd:true,
                maxlength:120,
            },
            zipCode: {
                required: true,
                isZipCode: true,

            },
            receiver: {
                required: true,
                isReceiver: true,
                limitSumNa:true,
                maxlength:120,
            },
        },
        messages: {
            country: '请选择国家和省/市/区',
            province: '请选择国家和省/市/区',
            city: '请选择国家和省/市/区',
            district: '请选择国家和省/市/区',
            address: {
                required: '带*选项不能为空',
                maxlength:'详细地址为4-120个字符'
            },
            zipCode: {
                required: '带*选项不能为空',
            },
            receiver: {
                required: '带*选项不能为空',
                maxlength:'长度为2-25个字符'
            },

        }
    });


    // 初始化页面
    init();
    // 新增收获地址弹框

    // 页面初始化
    function init() {
        //获取国家
        $("#country").html('<option value="">请选择</option>');
        $.post('/admin/common/supported_countries_ajax',{},function(res){
            $.each(res.data, function(k, v) {
                $("#country").append('<option value=' + v.id + ' countryid="'+v.areaCode+'">' +v.name+ '</option>');
            });
        });
        //获取国家
        $("#cellPhone-c").html('<option value="">选择国家</option>');
        $("#phoneNum-c").html('<option value="">选择国家</option>');
        $.post('/admin/common/supported_countries_ajax',{},function(res){
            $.each(res.data, function(k, v) {
                $("#cellPhone-c").append('<option value=' + v.id + ' countryid="'+v.areaCode+'">' +v.name+ '</option>');
            });
            $.each(res.data, function(k, v) {
                $("#phoneNum-c").append('<option value=' + v.id + ' countryid="'+v.areaCode+'">' +v.name+ '</option>');
            });
        });
    }

    //初始化
    function createAddressForm() {

        // 清空收货地址
        $('#address').val("");
        $('#country').val("");
        //$('#country')
        //$('#country').html('<option>请选择</option>');
        $('.province').html('<option>请选择</option>');
        $('.city').html('<option>请选择</option>');
        $('.district').html('<option>请选择</option>');
        $('#country_phone').html('<option>请选择</option>');
        $('#zipCode').val('');
        $('#supplierName').val('');
        $('#receiver').val('');
        $('#cellPhone').val('');
        $('#phoneNum').val('');
        $('#area').val('');
    };
    // 关闭
    // $('.cancel').click(function() {
    //     $('.mask').addClass('animated bounceOut');
    // });
    // $('.out').click(function() {
    //     $('.mask').addClass('animated bounceOut');
    // })



    // 更新地址
    // function  refreshAddress() {
    //
    //     httpRequest({
    //         url: AjaxUrl.member_get_user_receive_address_ajax,
    //         success: function (response) {
    //             $('#select').html('');
    //             var data=response.data.receiveAddress;
    //             $.each(data, function (k, v) {
    //                 var tag = '<option>' + v.receiver + '</option>';
    //                 $('#select').append(tag);
    //                 $('#select option:last-of-type').attr('selected','true').attr('deliveryId',v.id)
    //                 if (v.id == $('#select option:selected').attr('deliveryid')) {
    //                     deliveryId = v.id;
    //                     var tag = '  <li>' +

    //                         '<span>收件人：</span>' + data[k].receiver +
    //                         '</li>' +
    //                         '<li>' +
    //                         '<span>所在地区：</span>' +
    //                         '<div class="country">' + data[k].countryName + '</div>' +
    //                         '<div class="province">' + data[k].provinceName + '</div>' +
    //                         '<div class="xian">' + data[k].districtName + '</div>' +
    //                         '</li>' +
    //                         '<li>' +
    //                         '<span>详细地址：</span>' + (data[k].address) +
    //                         '</li>' +
    //                         '<li>' +
    //                         '<span>邮政编码：</span>' + data[k].zipCode +
    //                         '</li>' +
    //                         '<li>' +
    //                         '<span>手机：</span>' + data[k].cellPhone +
    //                         '</li>' +
    //                         '<li>' +
    //                         '<span>固定电话：</span>' + data[k].phoneNum +
    //                         '</li>';
    //                     $('#select_list').html(tag);
    //                     $('#select_list').css('display', 'block');
    //                 }
    //             })
    //         }
    //     });
    // }

    //当国家值改变
    $("#country").change(function() {
        var id = $("#country").val();
        if(id==0){
            $('.province').html('<option value="0">选择省</option>');
            $('.city').html('<option value="0">选择市</option>');
            $('.district').html('<option value="0">选择区</option>');
        }
        countryAjax(id);
    });

    function countryAjax(countryId,provinceId,cityId,districtId) {

        $.ajax({
            url: '/admin/common/supported_cities_ajax',
            data:  {
                countryId:countryId
            },
            success: function (response) {
                $(".selectList").each(function() {
                    var areaJson = response.data;
                    var temp_html;
                    var oProvince = $(this).find(".province");
                    var oCity = $(this).find(".city");
                    var oDistrict = $(this).find(".district");
                    //初始化省
                    // initProvince(areaJson);
                    var province = function() {
                        $.each(areaJson, function(i, province) {
                            temp_html += "<option value='" + province.id + "'>" + province.name + "</option>";
                        });
                        oProvince.html(temp_html);
                        city();
                    };
                    //赋值市
                    var city = function() {
                        temp_html = "";
                        var n = oProvince.get(0).selectedIndex;
                        $.each(areaJson[n].cities, function(i, city) {
                            temp_html += "<option value='" + city.id + "'>" + city.name + "</option>";
                        });
                        oCity.html(temp_html);
                        district();
                    };
                    //赋值县
                    var district = function() {
                        temp_html = "";

                        var m = oProvince.get(0).selectedIndex;
                        var n = oCity.get(0).selectedIndex;
                        if(typeof(areaJson[m].cities[n].areas) == "undefined") {
                            oDistrict.css("display", "none");
                        } else {
                            oDistrict.css("display", "inline");
                            $.each(areaJson[m].cities[n].areas, function(i, district) {
                                temp_html += "<option value='" + district.id + "'>" + district.name + "</option>";
                            });
                            oDistrict.html(temp_html);
                        };
                    };
                    //选择省改变市
                    oProvince.change(function() {
                        city();
                    });
                    //选择市改变县
                    oCity.change(function() {
                        district();
                    });
                    province();
                    if(!provinceId)return false;
                    $('.province').val(provinceId);
                    $('.province').change();
                    $('.city').val(cityId);
                    $('.city').change();
                    $('.district').val(districtId);
                });
            }
        });
    };

    // 初始化省
    function initProvince (areaJson) {
        $.each(areaJson, function(i, province) {
            temp_html += "<option value='" + province.id + "'>" + province.name + "</option>";
        });
        $(".selectList .province").html(temp_html);
        initCity(areaJson);
    };

    // 初始化市
    function initCity(areaJson) {
        temp_html = "";
        var n = $(".selectList .province").get(0).selectedIndex;
        $.each(areaJson[n].cities, function(i, city) {
            temp_html += "<option value='" + city.id + "'>" + city.name + "</option>";
        });
        $(".selectList .province").html(temp_html);
        // district();
    };

});
