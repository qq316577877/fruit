

/**
 * 创建商品价格表格
 * @param parentDiv
 * @param order
 */
function createOrderListTable(parentDiv, order) {
    var tableNode = document.createElement("table");

    var header = document.createElement('thead');
    header.innerHTML = '<th>商品名称</th>' +
        '<th>低位报价</th>' +
        '<th>高位报价</th>' +
        '<th>成交价</th>'+
        '<th>排序</th>';
    tableNode.appendChild(header)

    var orderLength = order.length;
    // var containerDetails = order.containerDetails;

    var rowNum = orderLength;
    // var height = getFinalHeightByRowspan(rowNum);
    // tableNode.style.height=height+"px";
    // console.log(order.containerDetails);
    // var controllerCreated = false;
    /*根据货柜list来循环生成行*/
    for (var x = 0; x < rowNum; x++) {
        var containerDetail = order[x];
        var trNode = tableNode.insertRow();

        //第一列
        var tdNode0 = trNode.insertCell(0);
        tdNode0.innerHTML = "" + containerDetail.name;
        tdNode0.setAttribute('commodityId',containerDetail.id);
        // tdNode0.setAttribute('transactionNo',containerDetail.transactionNo);

        //第二列
        var tdNode1 = trNode.insertCell(1);
        tdNode1.setAttribute('commodityId',containerDetail.id);
        tdNode1.setAttribute('class',"priceLow");
        tdNode1.className = 'priceLow';
        tdNode1.innerHTML = "<input type='text' value="+containerDetail.priceLow+">"

        //第三列
        var tdNode2 = trNode.insertCell(2);
        tdNode2.className = 'priceHigh';
        tdNode2.innerHTML = "<input type='text' value="+containerDetail.priceHigh+">"

        //第四列
        var tdNode3 = trNode.insertCell(3);
        tdNode3.className = 'priceActual';
        tdNode3.innerHTML = "<input type='text' value="+containerDetail.priceActual+">"

        //第五列
        var tdNode4 = trNode.insertCell(4);
        // tdNode4.className = 'priceActual';
        tdNode4.innerHTML = "<input type='text' value="+containerDetail.sortid+">"


    }

    parentDiv.appendChild(tableNode);
};

/*
* 提交修改的商品价格数据
* */
function getTable(quotationTime) {
    var tab_trs=$('#body table tr');
    var goodsCommodityInfos =new Array();
    var obj ={};
    // var quotationTime =
    for(var i =1;i<tab_trs.length;i++){
        var tab_tr = tab_trs[i];
        var commodityId =tab_tr.children[0].getAttribute('commodityId');
        // console.log(tab_tr.children[1]);
        var priceLow =tab_tr.children[1].children[0].value;
        var priceHigh =tab_tr.children[2].children[0].value;
        var priceActual =tab_tr.children[3].children[0].value;
        var sortid =tab_tr.children[4].children[0].value;


        var details={
            sortid:sortid,
            commodityId:commodityId,
            priceLow:priceLow,
            priceHigh:priceHigh,
            priceActual:priceActual,
        }
        goodsCommodityInfos.push(details)
    }
    obj={
        quotationTime:quotationTime ,
        goodsCommodityPriceDaysDTOs:goodsCommodityInfos

    };
    return obj
}





