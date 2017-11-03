/**
 * 订单列表 - 创建表结构
 * 用于查询到订单的表之后，循环创建表结构
 */

var thisIncreaseNum = 1;//自增长数字，用于页面create table 元素id

var LIST_1_HEAD_CLASS_NAME = "list_1_head";//表头className

var LIST_1_BODY_CLASS_NAME = "list_1_body";//表body className

var ELEMENT_DIV = "div";//div元素


/**
 * 通过主divId和查询到orderList来生成需要展示的订单列表
 * 循环一个一个创建div（在div中生成table）
 * @param zhuId
 * @param orderList
 */
function createOrderDivs(zhuId, orderList) {
    var orderListLength = orderList.length;
    for (var i = 0; i < orderListLength; i++) {
        var order = orderList[i];
        createDiv(zhuId, order);
    }
}


/**
 * 在主divId中
 * @param zhuId
 * @param order
 */
// function createDiv(zhuId, order) {
//     var bigDiv = document.createElement(ELEMENT_DIV);
//     bigDiv.className = 'list_1';
//     bigDiv.id = ELEMENT_DIV + thisIncreaseNum;
//     var head = document.createElement(ELEMENT_DIV);
//
//     head.className = LIST_1_HEAD_CLASS_NAME;
//     var tag =
//         '<span class="orderDate">' + order.date + '</span>' +
//         '<div class="order-hao ">' +
//         '<span>订单号：</span>' +
//         '<span class="orderNo">' + order.orderNo + '</span>' +
//         '</div>' +
//         '<div class="order-hao">' +
//         '<span>采购商：</span>' +
//         (order.purchaseName) +
//         '</div>';
//     head.innerHTML = tag;
//     document.getElementById(zhuId).appendChild(bigDiv);
//     bigDiv.appendChild(head);
//
//     var body = document.createElement(ELEMENT_DIV);
//     body.className = LIST_1_BODY_CLASS_NAME;
//     body.id = 'createTable' + thisIncreaseNum;
//
//     createOrderListTable(body, order);
//     bigDiv.appendChild(body);
//
//     thisIncreaseNum++;
// }


// var list =response.data.records;
// var parents =document.getElementById('orderList')
// createOrderListTable(parents,list)

/**
 * 一个订单生成一个table，放到订单div里
 * @param parentDiv
 * @param order
 */
function createOrderListTable(parentDiv, order) {
    var tableNode = document.createElement("table");
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
        tdNode0.innerHTML = "" + moment(containerDetail.addTime).format('YYYY-MM-DD');
        // tdNode0.setAttribute('transactionNo',containerDetail.transactionNo);

        //第二列
        var tdNode1 = trNode.insertCell(1);
        tdNode1.innerHTML = "" + containerDetail.orderNo;
        tdNode1.className='orderNo';
        //第三列
        var tdNode2 = trNode.insertCell(2);
        tdNode2.innerHTML = "" + containerDetail.varietyName


        //第四列
        var tdNode3 = trNode.insertCell(3);
        tdNode3.innerHTML = "" + containerDetail.goodsNum


        //第五列
        var tdNode4 = trNode.insertCell(4);
        tdNode4.innerHTML = "" + containerDetail.purchaseName

        //第六列
        var tdNode5 = trNode.insertCell(5);
        var needLoanDesc =containerDetail.needLoan==1?'是':'否';
         tdNode5.innerHTML =""+needLoanDesc;

        //第七列
        var tdNode6 = trNode.insertCell(6);
        tdNode6.innerHTML='<div class="doubleHeight">'+
            '<div>'+containerDetail.statusDesc+'</div>'+
            '<a class="orderDetails orderDetailsLoock" href="javascript:;">订单详情</a>'+
                '</div>'

        //第八列
        var tdNode7 = trNode.insertCell(7);
        if(containerDetail.status==1 || containerDetail.status==2){
            tdNode7.innerHTML=
                '<div class="doubleHeight">'+
                '   <a class="orderDetails" href="javascript:;">审核订单</a>'+
                '<a href="javascript:;" class="cancelOrder">取消订单</a>'+
                '</div>'
        }
        else if(containerDetail.status==3){
            tdNode7.innerHTML=
                '<div class="doubleHeight">'+
                '   <a class="Advance" href="javascript:;">预付款确认</a>'+
                '<a href="javascript:;" style="display: none">取消订单</a>'+
                '</div>'
        }else if(containerDetail.status==4){
            tdNode7.innerHTML=
                '<div class="doubleHeight">'+
                '   <a class="prepaid" href="javascript:;">尾款确认</a>'+
                '<a href="javascript:;" class="sendShipping">发货处理</a>'+
                '</div>'
        }else if(containerDetail.status==5){
            tdNode7.innerHTML=
                '<div class="doubleHeight">'+
                '<a href="javascript:;"  class="sendShipping">发货处理</a>'+
                '</div>'
        }else if(containerDetail.status==100){
            tdNode7.innerHTML=''
        }

    }

    parentDiv.appendChild(tableNode);
};
// var tdSixHeight = tdNode6.children[0].style.height;
//
// var tdSevenHeight = tdNode5.children[0].style.height;
// var tableHeight;
// if (parseInt(tdSixHeight) > parseInt(tdSevenHeight)) {
//     tableHeight = tdSixHeight
// } else {
//     tableHeight = tdSevenHeight
// }
// tableNode.style.minHeight = tableHeight;
// tdNode5.children[0].style.minHeight = tableHeight;

//添加到那个位置






/**
 * 根据rowspan获取table高度
 * @param rowspan
 * @returns {number}
 */
// function getFinalHeightByRowspan(rowspan){
//     var height = 40;
//     if(rowspan==1){
//         height = 40*2;
//     }else{
//         height = 40*rowspan;
//     }
//     return height;
// }
