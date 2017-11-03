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
function createDiv(zhuId, order) {
    var bigDiv = document.createElement(ELEMENT_DIV);
    bigDiv.className = 'containerList';
    // bigDiv.id = ELEMENT_DIV + thisIncreaseNum;
    var head = document.createElement(ELEMENT_DIV);

    head.className = 'containerHeader';
    var tag =
        '<span class="orderNo">订单号：'+order.orderNo+'</span>'+
        '<span>果品：'+order.varietyName+'</span>'+
        '<span>采购商：'+order.purchaseName+'</span>';
    head.innerHTML = tag;
    document.getElementById(zhuId).appendChild(bigDiv);
    bigDiv.appendChild(head);

    var body = document.createElement(ELEMENT_DIV);
    body.className ='containerBody';
    // body.id = 'createTable' + thisIncreaseNum;

    createOrderListTable(body, order);
    bigDiv.appendChild(body);

    // thisIncreaseNum++;
}


/**
 * 一个订单生成一个table，放到订单div里
 * @param parentDiv
 * @param order
 */
function createOrderListTable(parentDiv, order) {
    var tableNode = document.createElement("table");
    var orderLength = order.length;
    var containerDetails = order.containerInfoList;
    var createLastTd =false;
    var rowNum = containerDetails.length;
    // var height = getFinalHeightByRowspan(rowNum);
    // tableNode.style.height=height+"px";
    // console.log(order.containerDetails);
    // var controllerCreated = false;
    /*根据货柜list来循环生成行*/
    for (var x = 0; x < rowNum; x++) {
        var containerDetail = containerDetails[x];
        var trNode = tableNode.insertRow();

        //第一列
        var tdNode0 = trNode.insertCell(0);
        tdNode0.innerHTML = "" + containerDetail.submitTimeStr;
        // tdNode0.setAttribute('transactionNo',containerDetail.transactionNo);

        //第二列
        var tdNode1 = trNode.insertCell(1);
        tdNode1.innerHTML = "" + containerDetail.containerNo;

        //第三列
        var tdNode2 = trNode.insertCell(2);
        tdNode2.innerHTML =toThousands ("" + containerDetail.productAmount,2)
        tdNode2.className='moneyForm';


        //第四列
        var tdNode3 = trNode.insertCell(3);
        tdNode3.innerHTML = toThousands ("" + containerDetail.loanAmount,2)
        tdNode3.className='moneyForm';


        //第五列
        var tdNode4 = trNode.insertCell(4);
        var loanStatusDesc = containerDetail.loanStatusDesc;
        if(loanStatusDesc){
            tdNode4.innerHTML = "" + loanStatusDesc;
        }else{
            tdNode4.innerHTML = "-" ;
        }


        //第六列
        var tdNode5 = trNode.insertCell(5);
        tdNode5.innerHTML = "" + containerDetail.statusDesc;

        //第七列
        if(!createLastTd){
            var tdNode6 = trNode.insertCell(6);
            tdNode6.innerHTML =
                '<div class="operation">'+
                '<a href="javascript:;" class="containerDetails">货柜详情</a>' +
                ' <a class="logisticsContainer" href="javascript:;">物流详情</a>'+
                '</div>';
            createLastTd=true;
        }
        // console.log(tableNode.offsetHeight);
    }


    parentDiv.appendChild(tableNode);//添加到那个位置

}

function toThousands(s,n) {
    n = n > 0 && n <= 20 ? n : 2;
    s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";
    var l = s.split(".")[0].split("").reverse(), r = s.split(".")[1];
    t = "";
    for (i = 0; i < l.length; i++) {
        t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
    }
    return t.split("").reverse().join("") + "." + r;
}