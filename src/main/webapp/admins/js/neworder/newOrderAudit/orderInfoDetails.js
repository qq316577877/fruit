/**
 * 订单审核订单 创建列表信息
 * Created by yl on 2017/9/26.
 */

// 创建完整的一个表格结构
function createDiv(zhuId, list) {
    var bigDiv = document.createElement('div');
    bigDiv.className = 'form_horizontal';
    bigDiv.id = 'tableOrder';
    var head = document.createElement('div');
    head.className = 'tableHeader';

    // 添加table 上表头信息
    var tableHeaderInfo = ' <h3 id="goodspro" >商品信息 <span class="productTotalNum">数量总计：</span><span id="totalAm">' +
        list.goodsNum + '</span>箱 <span class="productTotalAmount">参考合计金额</span>￥<span id="totalPri">'
        + list.tailAmount + '</span>元' +
        '</h3><hr>';
    head.innerHTML = tableHeaderInfo;
    bigDiv.appendChild(head);

    // 添加一行
    var tablebody = document.createElement('div');
    var tagAddRow = '<div id="add-h" class="add-h add-h-1">' +
        '<span>+</span>' +
        '<span>添加一行</span>' +
        '</div>';
    // 生成列表的主体
    createTabelBody(tablebody, list);
    var addRow = document.createElement('div');
    addRow.innerHTML = tagAddRow;
    tablebody.appendChild(addRow);
    bigDiv.appendChild(tablebody);
    $(zhuId).before(bigDiv);
}


/**
 * 一个订单生成一个table，放到订单div里
 * @param parentDiv
 * @param order
 */
// 封装创建第一个table
function createTabelBody(parentDiv, list) {
    var tableNode = document.createElement("table");
    tableNode.id = 'tabledCreateId';
    var tableHeader =
        ' <tr>' +
        '                <th>果品</th>' +
        '                <th >商品名称</th>' +
        '                <th>数量</th>' +
        '                <th>低位报价/箱</th>' +
        '                <th>高位报价/箱</th>' +
        '                <th>参考单价</th>' +
        '                <th>合计</th>' +
        '            </tr>';
    tableNode.innerHTML = tableHeader;

    var containerDetails = list.goodsInfoList;
    // console.log("containerDetails",containerDetails);
    var rowNum = containerDetails.length;
    for (var x = 0; x < rowNum; x++) {
        var containerDetail = containerDetails[x];
        var trNode = tableNode.insertRow();

        //第一列
        var tdNode0 = trNode.insertCell(0);
        tdNode0.innerHTML = '' + list.varietyName;

        //第二列
        var tdNode1 = trNode.insertCell(1);
        tdNode1.innerHTML = ' <td class="in-2">' +
            '<select>' +
            '<option commodityId="' + containerDetail.commodityId + '">' + containerDetail.commodityName + '</option>' +
            '</select>' +
            '</td>';
        tdNode1.className = 'in-2';

        //第三列
        var tdNode4 = trNode.insertCell(2);
        tdNode4.innerHTML = '<input value="' + containerDetail.commodityNum + '"/>';
        tdNode4.className = 'amount'

        //第四列
        var tdNode5 = trNode.insertCell(3);
        tdNode5.innerHTML = '' + containerDetail.priceLow;
        tdNode5.className = 'priceLow';

        //第五列
        var tdNode6 = trNode.insertCell(4);
        tdNode6.innerHTML = '' + containerDetail.priceHigh;
        tdNode6.className = 'priceHigh';

        //第六列
        var tdNode7 = trNode.insertCell(5);
        tdNode7.innerHTML =
            '<input value="' + containerDetail.priceActual + '"/>';
        tdNode7.className = 'priceActual';

        //第七列
        var tdNode8 = trNode.insertCell(6);
        tdNode8.innerHTML = '' + containerDetail.totalPrice;
        tdNode8.className = 'priceAll'
    }
    parentDiv.appendChild(tableNode);//添加到那个位置
}

/*
 * 加载属性
 * */
function goodsPro(goodsInfoPro) {
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
            // console.log($('.in-2'));
            $('.in-2 select').html(option);

            $.each(list, function (k, v) {
                for (var i = 0; i < goodsInfoPro.length; i++) {
                    // console.log(v.id);
                    if (v.id == goodsInfoPro[i].commodityId) {
                        // console.log($('.in-2').eq(i).find('select option'));
                        $('.in-2').eq(i).find('select option').eq(k).attr('selected', true);
                        $('.priceLow').eq(i).html(v.priceLow);
                        $('.priceHigh').eq(i).html(v.priceHigh);
                        $('#tableOrder .priceActual').eq(i).find('input').val(goodsInfoPro[i].dealPrice);
                        // console.log($('#tableOrder .priceActual').eq(i).find('input'));
                        // $('.priceActual').eq(i).find('input').val(v.priceActual);
                        var pri = v.priceActual;
                        var num = $('.amount').eq(i).find('input').val();
                        $('.priceAll').eq(i).html(goodsInfoPro[i].totalAmount)
                    }
                }
            });
    //计算总价格
            var priceAll = 0;
            var priceLists = $('.priceAll');
            $.each(priceLists, function (k, v) {
                priceAll += parseInt(v.innerHTML)
            });
            $('#totalPri').html(priceAll);
        }
    })
}
/*
 * 更换商品属性
 * */
function changeGoodsPro(that) {
    // console.log("changeGoodsPro");
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
                    $(that).siblings('.priceAll').html(pri * num);
                }
            });

        }
    })
}

/*
 * 保存订单信息
 * */
function next(table, orderNo) {
    var goodsInfoList = [];
    var table = $(table);
    var tab_trs = $(table).find('tr');
    for (var i = 1; i < tab_trs.length; i++) {
        var tab_tr = tab_trs[i];
        var commodityId = tab_tr.children[1].children[0].selectedOptions[0].getAttribute('commodityid');
        // var commodityName =tab_tr.children[1].children[0].selectedOptions[0].innerHTML;
        var commodityNum = tab_tr.children[2].children[0].value;
        if(commodityNum==''){
            commodityNum=0
        }
        var dealPrice = tab_tr.children[5].children[0].value;
        var totalAmount = tab_tr.children[6].innerHTML;
        var obj = {
            orderNo: orderNo,
            commodityId: commodityId,
            // commodityName:commodityName,
            commodityNum: commodityNum,
            dealPrice: dealPrice,
            totalAmount: totalAmount
        }
        goodsInfoList.push(obj)
    }
    return goodsInfoList
}