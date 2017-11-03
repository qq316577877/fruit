<!DOCTYPE html>
<html class="not-ie" lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <title>用户列表</title>
    <link rel="stylesheet" href="/admins/assets/css/neworder/orderInfo/orderInfoDetails.css">
    <script src="//g.alicdn.com/dingding/dinglogin/0.0.5/ddLogin.js"></script>
</head>
<body>

<div id="new_order_info_detail" class="orderInfo_detail">
    <div class="form_horizontal">
        <h3>资金服务 <span class="fundExplain">说明：“本单可贷款金额”最高可设置为下方商品信息中参考合计金额的60%，且必须为1000 的整数倍</span></h3>
        <hr/>
        <label><input type="checkbox" name="vehicle" value="Car" id="checkboxLoan" value=""/>本单使用资金服务</label>
        <div >
            <label class="availableLoanAmount">本单可贷款金额:</label><input class="fundInput" id="thisLoanInput" type="number"/>元
            <span class="thisOrderLoanNeed">本单申请贷款金额：￥<i></i>元</span>
        </div>
    </div>
    <div class="form_horizontal">
        <h3>意向发货时间段</h3>
        <hr/>
        <input id="startTime" type="text"/><span class="dateRegion">~</span><input id="endTime" type="text"/>
    </div>
    <div class="form_horizontal">
        <h3>货柜信息 <span class="totalNum">合计数量：</span><span id="sumCon">5柜</span></h3>
        <hr/>
        <label>果品：</label><select class="fruitSelect" id="fruitSelect">
        <option>越南火龙果</option>
    </select>
        <label>货柜数：</label>
        <#--<span class="containerNumOporate" id="containerNumOporateAdd">-</span>-->
        <input id="numCon" min="1" type="number"/>
        <#--<span id="containerNumOporateReduce" class="containerNumOporate">+</span>-->
    </div>
    <div class="oprateBtn" id="preservationCon">
        <button id="preservation">保存</button>
    </div>

    <div class="form_horizontal" id="contract">
        <h3>采购服务合同
            <button class="createContract" id="seeContract">生成合同</button>
        </h3>
        <hr/>
        <label><input name="Fruit" type="checkbox" value=""/>已查看并审核 <a href="javascript:;" id="lookContract">《境外采购服务合同》</a> </label>
    </div>
    <div class="oprateBtn">

        <button id="auditOrder">审核通过</button>
        <button id="cancelOrder">取消订单</button>
    </div>
    <div id="mask">
        <div id="pdf1" style="width:700px; height:550px;">
            如果浏览器没安装Adobe Reader 或支持pdf的插件
            <a href="~/pdf/CGVET22-08-2011V2P.pdf">Click here to download the PDF</a>
        </div>
        <button id="closeContract">关闭</button>
    </div>
    <div id="maskSee">
        <div id="pdf2" style="width:700px; height:550px;">
            如果浏览器没安装Adobe Reader 或支持pdf的插件
            <a href="~/pdf/CGVET22-08-2011V2P.pdf">Click here to download the PDF</a>
        </div>
        <button id="closeContract_2">关闭</button>
    </div>
</div>

</body>

<script src="/admins/assets/plugins/date/laydate.dev.js"></script>
<script src="/admins/assets/plugins/pdfobject.min.js"></script>
<script src="/admins/js/neworder/newOrderAudit/orderInfoDetails.js"></script>

<script>

    laydate({

        elem: '#startTime'

    });

    laydate({

        elem: '#endTime'
    });
</script>
</html>
