// var orderNo = window.location.search;
// orderNo = orderNo.split('=')[1]
// $(function () {
//     var num_check = /[1-9]\d*/;
//     var supplierId;  //定义
//
//
//
// })
//
// //封装函数
// /**
//  * 通过tableId获取一个货柜的值
//  * @param table
//  * @return orderContainers
//  */
// function giveAHuoguiValues(table){
//     //获取tr
//     var tab_trs =$(table)[0].getElementsByTagName('tr');
//     var orderContainers =new Array();
//     var productId =table.parentNode.previousElementSibling.children[1].getAttribute('productId')
//     var tableTrLength = tab_trs.length;
//     for(var i=1;i<tableTrLength;i++){
//
//         var tab_tr = tab_trs[i];
//
//         var productName = tab_tr.children[0].children[0].value;//商品名称
//         var level = tab_tr.children[1].children[0].selectedOptions[0].innerHTML;//等级
//         var type = tab_tr.children[2].children[0].selectedOptions[0].innerHTML;//品种
//         var size = tab_tr.children[3].children[0].selectedOptions[0].innerHTML;//大小
//         var productId =tab_tr.children[0].children[0].getAttribute('productId');
//         console.log(productId);
//         var price = Number(tab_tr.children[4].children[0].value);//成交价
//         if(price ==''){
//             price=0
//         }
//         var quantity = tab_tr.children[5].children[0].value;//数量
//         var totalPrice = tab_tr.children[6].children[0].value;//合计A
//         var productDetail = {
//             level:level,
//             type:type,
//             size:size
//         }

//         var orderContainer = {
//             productId:productId,
//             productName:productName,
//             quantity:quantity,
//             price:price,
//             productDetail:productDetail
//         };
//         orderContainers.push(orderContainer);
//     };
//     return orderContainers;
// }
//
//
// /**
//  * 获取所有货柜的信息
//  * @param zhuDiv
//  * @return orderContainers
//  */
// var oneTotal,totalAmount;
// function giveAllHuoguiValues(zhuDiv){
//     var orderContainers = new Array();
//     for(var e =0;e<zhuDiv.length;e++){
//         //获取table
//         var table =zhuDiv[e].getElementsByTagName('table')[0];
//         var orderContainerDetails = giveAHuoguiValues(table);
//
//         // var productId =zhuDiv[e].children[0].children[1].getAttribute('productId');
//         var productId =$(zhuDiv[e]).find('.first-td').eq(0).attr('productId');
//         var totalAm =zhuDiv[e].getElementsByClassName('hide_inform')[0].children[0].value;
//         var totalPrice =zhuDiv[e].getElementsByClassName('sum')[0].getElementsByTagName('input')[0].value;
//         var orderContainer = {
//             orderContainerDetails:orderContainerDetails,
//             productName: orderContainerDetails[0].productName,
//             productId: productId,
//             totalQuantity: totalAm,
//             totalPrice: totalPrice
//         }
//         orderContainers.push(orderContainer);
//     }
//
//     return orderContainers;
// }
//
//
// /**
//  * 获取接口文件所需要的json信息
//  */
// function giveTheFinalValues(zhuDiv,supplierId,totalAmount){
//     var orderContainers = giveAllHuoguiValues(zhuDiv);
//     var jsonData = {
//         orderId:orderNo,
//         supplierId:supplierId,
//         type:1,
//         totalAmount:totalAmount,
//         productAmount:totalAmount,
//         orderContainers:orderContainers
//     };
//     return jsonData
// }


$(function () {
    var num_check = /[1-9]\d*/;
    var supplierId;  //定义

    $('input[type="radio"]').change(function () {
        //选择海外代采
        if ($('#agency')[0].checked) {
            $('.pro-list').css('display', 'none');
            $('#next_2').css('display','none');
            $('.container-add').css('display', 'block');
            $('#container-add-2').css('display', 'none');
            var tag = '<option supplierId="1" selected disabled>浙江创意生物科技股份有限公司</option>';
            $('.supplier-2 select').html(tag);
            $('.supplier ul').css('display', 'none');
            $('.add-supplier').css('display', 'none');
            // $('#agency').css('display', 'block');


            //清除多余的结构
            //var pro_list=document.getElementById('pro_list');
            //var pro_list_nums =pro_list.getElementsByClassName('pro-list-1');
            //console.log(pro_list_nums);
            //for(var q =0;q<pro_list_nums.length;q++){
            //    pro_list_nums[q].parentNode.removeChild(pro_list_nums[q]);
            //};
            //$('#pro_list #ad_1').before(table_2)
        }
        if ($('#direct')[0].checked) {
            //清空海外直采的选项
            $('.selsct option').html('请选择供应商');
            $('#next_1').css('display','none');
            $('.supplier').css('display', 'block');
            $('.pro-list').css('display', 'none')
            $('.pro-list-b').css('display', 'none');
            // $('.container-add').css('display', 'none');
            $('#container-add-2').css('display', 'block');
            $('.add-supplier').css('display', 'inline-block');

            //查询供应商信息
            //发送ajajx
            $.ajax({
                url: '/member/supplier/get_user_supplier_information_ajax',
                type: 'post',
                success: function (data) {
                    if (data.code == 200) {
                        var supplierList = data.data.supplierList;
                        // 校验供应商
                        if(supplierList ==null){
                            return;
                        }
                        $.each(supplierList, function (k, v) {
                            var tag = '<option supplierId="'+v.id+'">' + v.supplierName + '</option>';
                            $('.supplier-2 select').append(tag);
                        });
                        //通过选中的供应商判断json数据中的哪一项数组
                        $('.selsct').change(function () {

                            $.each(supplierList, function (k, v) {
                                if (v.supplierName == $('.selsct option:selected').html()) {
                                    //console.log(supplierList[k]);
                                    //拼接字符
                                    var tag = ' <li><span>供应商名称：</span>' + supplierList[k].supplierName + '</li>' +
                                        '<li><span>所在地区：</span>' + supplierList[k].countryName + '</li>' +
                                        '<li><span>详细地址：</span>' + supplierList[k].address + '</li>' +
                                        '<li><span>邮政编码：</span>' + supplierList[k].zipCode + '</li>' +
                                        '<li><span>联系人：</span>' + supplierList[k].supplierContact + '</li>' +
                                        '<li><span>手机号：</span>' + supplierList[k].cellPhone + '</li>';
                                    $('.supplier ul').html(tag);
                                    $('.supplier ul').css('display', 'block');

                                }
                            })
                        })
                    }
                }
            });
            //清除多余的结构
            //var pro_list=document.getElementById('pro_list');
            //var pro_list_nums =pro_list.getElementsByClassName('pro-list-1');
            //for(var k =0;k<pro_list_nums.length;k++){
            //    if(pro_list_nums[k].id !=='div1'&&pro_list_nums[k].id !=='p-l-1'){
            //        pro_list_nums[k].outerHTML =''
            //    }
            //}
        }

    })

})
//
// //封装函数
// /**
//  * 通过tableId获取一个货柜的值
//  * @param table
//  * @return orderContainers
//  */
// function giveAHuoguiValues(table){
//     //获取tr
//     var tab_trs =$(table)[0].getElementsByTagName('tr');
//     var orderContainers =new Array();
//     var productId =table.parentNode.previousElementSibling.children[1].getAttribute('productId');
//
//     var tableTrLength = tab_trs.length;
//     for(var i=1;i<tableTrLength;i++){
//
//         var tab_tr = tab_trs[i];
//
//         var productName = tab_tr.children[0].children[0].value;//商品名称
//         var level = tab_tr.children[1].children[0].selectedOptions[0].innerHTML;//等级
//         var type = tab_tr.children[3].children[0].selectedOptions[0].innerHTML;//品种
//         var size = tab_tr.children[2].children[0].selectedOptions[0].innerHTML;//大小
//         var price = Number(tab_tr.children[4].children[0].value);//成交价
//         if(price ==''){
//             price=0
//         }
//         var quantity = tab_tr.children[5].children[0].value;//数量
//         var totalPrice = tab_tr.children[6].children[0].value;//合计A
//         var productDetail = {
//             level:level,
//             type:type,
//             size:size
//         };
//         var orderContainer = {
//             productId:productId,
//             productName:productName,
//             quantity:quantity,
//             price:price,
//             productDetail:productDetail
//         };
//         orderContainers.push(orderContainer);
//     };
//     return orderContainers;
// }
//
//
// /**
//  * 获取所有货柜的信息
//  * @param zhuDiv
//  * @return orderContainers
//  */
// var oneTotal,totalAmount;
// function giveAllHuoguiValues(zhuDiv){
//     var orderContainers = new Array();
//     for(var e =0;e<zhuDiv.length;e++){
//         //获取table
//         var table =zhuDiv[e].getElementsByTagName('table')[0];
//         var orderContainerDetails = giveAHuoguiValues(table);
//         //console.log(this);
//         var productId =zhuDiv[e].children[0].children[1].getAttribute('productId');
//         var totalAm =zhuDiv[e].children[0].children[2].getAttribute('totalquantity');
//         var totalPrice =zhuDiv[e].getElementsByClassName('sum')[0].getElementsByTagName('input')[0].value;
//         var orderContainer = {
//             orderContainerDetails:orderContainerDetails,
//             productName: orderContainerDetails[0].productName,
//             productId: productId,
//             totalQuantity: totalAm,
//             totalPrice: totalPrice
//         }
//         orderContainers.push(orderContainer);
//     }
//
//     return orderContainers;
// }
//
//
// /**
//  * 获取接口文件所需要的json信息
//  */
// function giveTheFinalValues(zhuDiv,type,totalAmount,productAmount,orderId){
//     var orderContainers = giveAllHuoguiValues(zhuDiv);
//     var supplierId =$('.selsct option:selected').attr('supplierid');
//
//     var jsonData = {
//         supplierId:supplierId,
//         type:type,
//         totalAmount:totalAmount,
//         productAmount:productAmount,
//         orderContainers:orderContainers,
//         orderId:orderId
//     };
//     return jsonData
// }

/**
 * 通过tableId获取一个货柜的值
 * @param table
 * @return orderContainers
 */
function giveAHuoguiValues(table) {
    //获取tr
    var tab_trs = $(table)[0].getElementsByTagName('tr');
    var firstSelectedName = $(table).find('.two-th').attr('engname');
    var twoSelectedName = $(table).find('.three-th').attr('engname');
    var fourSelectedName = $(table).find('.four-th').attr('engname');
    var orderContainers = new Array();
    var productId = table.parentNode.previousElementSibling.children[1].getAttribute('productId')
    var tableTrLength = tab_trs.length;
    for (var i = 1; i < tableTrLength; i++) {

        var tab_tr = tab_trs[i];

        var productName = tab_tr.children[0].children[0].value;//商品名称
        var level = tab_tr.children[1].children[0].selectedOptions[0].innerHTML;//等级
        var type = tab_tr.children[3].children[0].selectedOptions[0].innerHTML;//品种
        var size = tab_tr.children[2].children[0].selectedOptions[0].innerHTML;//大小
        var price = Number(tab_tr.children[4].children[0].value);//成交价
        if (price == '') {
            price = 0
        }
        var quantity = tab_tr.children[5].children[0].value;//数量
        var totalPrice = tab_tr.children[6].children[0].value;//合计A
        var productDetail = {};
        productDetail[firstSelectedName] = level;
        productDetail[fourSelectedName] = type;
        productDetail[twoSelectedName] = size;
        var orderContainer = {
            productId: productId,
            productName: productName,
            quantity: quantity,
            price: price,
            productDetail: productDetail
        };
        orderContainers.push(orderContainer);
    }
    ;
    return orderContainers;
}


/**
 * 获取所有货柜的信息
 * @param zhuDiv
 * @return orderContainers
 */
var oneTotal, totalAmount;
function giveAllHuoguiValues(zhuDiv) {
    var orderContainers = new Array();
    for (var e = 0; e < zhuDiv.length; e++) {
        //获取table
        var table = zhuDiv[e].getElementsByTagName('table')[0];
        var orderContainerDetails = giveAHuoguiValues(table);
        var productId = zhuDiv[e].children[0].children[1].getAttribute('productId');
        var totalAm = zhuDiv[e].children[1].children[0].getAttribute('totalamount');
        var totalPrice = zhuDiv[e].getElementsByClassName('sum')[0].getElementsByTagName('input')[0].value;
        var orderContainer = {
            orderContainerDetails: orderContainerDetails,
            productName: orderContainerDetails[0].productName,
            productId: productId,
            totalQuantity: totalAm,
            totalPrice: totalPrice
        }
        orderContainers.push(orderContainer);
    }

    return orderContainers;
}


/**
 * 获取接口文件所需要的json信息
 */
function giveTheFinalValues(zhuDiv, type, totalAmount, productAmount, orderId) {
    var orderContainers = giveAllHuoguiValues(zhuDiv);
    var supplierId = $('.selsct option:selected').attr('supplierid');
    var jsonData = {
        supplierId: supplierId,
        type: type,
        totalAmount: totalAmount,
        productAmount: productAmount,
        orderContainers: orderContainers,
        orderId: orderId
    };
    return jsonData
}

