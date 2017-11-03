<!DOCTYPE html>
<html class="not-ie" lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <title>用户列表</title>
    <link rel="stylesheet" href="/admins/assets/css/neworder/orderInfo/orderInfoContainer.css">
    <link rel="stylesheet" href="/admins/assets/plugins/loading/loading.css">
</head>
<body>


<div class="orderInfo_children" style="display: none;" id="container_info">
    <ul id='containerIdList'>
    </ul>
    <div class="clear"></div>
    <div class="containerDetails">
        <div class="containerAddress">
            <div class="addreddAudit">
                <input type="button" id="addressBtnChange" value="选择收货地址">
                <input type="button" id="addressBtn" value="新增收货地址">
            </div>
            <div>
                <p id="addreddInfo">收货地址：彭凤霞，86-18664938582，云南河口北山国际商贸城B幢04号-小蔡物流 ，430000</p>
            <#--<p id="addreddInfo_2">收货地址：彭凤霞，86-18664938582，湖北省 武汉市 洪山区 关山街道 武汉市东湖新技术开发区光谷大道77号光谷金融港B13栋五层 ，430000-->
            <#--，430000</p>-->
            </div>
            <div>
                <span>运输方式：</span>
                <input type="radio" name="logisticsType">海运
                <input type="radio" name="logisticsType">陆运
            </div>
        </div>
        <div class="containerInfo">
            <div class="containerTableHeader" id="containerTableHeader">
                <span>货柜名称：越南火龙果</span>
                <span>货柜批次号：
                    <input type="text">
                </span>
                <span>状态：未提交</span>
            </div>
            <div class="containerTableBody">
            <#--<table>-->
            <#--<thead>-->
            <#--<th>商品名称</th>-->
            <#--<th>品种</th>-->
            <#--<th>等级</th>-->
            <#--<th>大小</th>-->
            <#--<th>成交价/箱</th>-->
            <#--<th>数量</th>-->
            <#--<th>合计</th>-->
            <#--</thead>-->
            <#--<tr>-->
            <#--<td>越南火龙果</td>-->
            <#--<td>-->
            <#--<select name="" id="">-->
            <#--<option value="">白心</option>-->
            <#--</select>-->
            <#--</td>-->
            <#--<td>-->
            <#--<select name="" id="">-->
            <#--<option value="">一级</option>-->
            <#--</select>-->
            <#--</td>-->
            <#--<td>-->
            <#--<select name="" id="">-->
            <#--<option value="">大</option>-->
            <#--</select>-->
            <#--</td>-->
            <#--<td>-->
            <#--<input type="text">元-->
            <#--</td>-->
            <#--<td>-->
            <#--<input type="text">箱-->
            <#--</td>-->
            <#--<td>-->
            <#--<input type="text">元-->
            <#--</td>-->
            <#--</tr>-->
            <#--<tr>-->
            <#--<td>越南火龙果</td>-->
            <#--<td>-->
            <#--<select name="" id="">-->
            <#--<option value="">白心</option>-->
            <#--</select>-->
            <#--</td>-->
            <#--<td>-->
            <#--<select name="" id="">-->
            <#--<option value="">一级</option>-->
            <#--</select>-->
            <#--</td>-->
            <#--<td>-->
            <#--<select name="" id="">-->
            <#--<option value="">大</option>-->
            <#--</select>-->
            <#--</td>-->
            <#--<td>-->
            <#--<input type="text">元-->
            <#--</td>-->
            <#--<td>-->
            <#--<input type="text">箱-->
            <#--</td>-->
            <#--<td>-->
            <#--<input type="text">元-->
            <#--</td>-->
            <#--</tr>-->
            <#--<tr>-->
            <#--<td>越南火龙果</td>-->
            <#--<td>-->
            <#--<select name="" id="">-->
            <#--<option value="">白心</option>-->
            <#--</select>-->
            <#--</td>-->
            <#--<td>-->
            <#--<select name="" id="">-->
            <#--<option value="">一级</option>-->
            <#--</select>-->
            <#--</td>-->
            <#--<td>-->
            <#--<select name="" id="">-->
            <#--<option value="">大</option>-->
            <#--</select>-->
            <#--</td>-->
            <#--<td>-->
            <#--<input type="text">元-->
            <#--</td>-->
            <#--<td>-->
            <#--<input type="text">箱-->
            <#--</td>-->
            <#--<td>-->
            <#--<input type="text">元-->
            <#--</td>-->
            <#--</tr>-->
            <#--</table>-->

                <div class="add-h add-h-1" id="containerAdd">
                    <span>+</span>
                    <span>添加一行</span>
                </div>
                <div class="sum clearfix">
                    <span>总计：</span>
                    <input type="text" value="0.00" id="finallPrice" disabled/>
                    <span>元</span>
                </div>
                <div class="clear"></div>
                <h6 class="moneyInfoTitle">资金服务</h6>
                <p class="moneyInfoP">
                    <span>本单申请总贷款金额：￥<i id="thisLoan"></i>元</span>
                    <span>本单剩余可贷款金额：￥<i id="surplusLoan"></i>元</span>
                </p>
                <div class="loanMoney">
                    本柜贷款金额：<input id="thisConLoan" value="0">元
                </div>

                <input type="button" class="loanBtn" value="智能分配贷款金额">
            </div>
        </div>


        <div class=subBtn>
            <input type="button" id="saveCon" value="保存">
            <input type="button" id="subCon" value="保存并提交">
        </div>
    </div>

    <div id="conMaks">
        <div class="inform" id="address_inform" style="display: none;">
            <div class="inform-head">
                <span class="fl">新增收货地址</span>
                <i class="iconfont icon-chacha cancel fr"></i>
            </div>
            <form id="addAdressCon" action="">
                <div class="inform-body">
                    <div class="lab">
                        <div class="input-l"><span class="red">*</span> 所在地区 ：</div>

                        <div class="selectList">
                            <select id="country" name="country">
                                <option value="0">选择国家</option>
                                <option value="1" data-tel="086">中国</option>
                                <option value="2" data-tel="084">越南</option>
                            </select>
                            <select class="province" id="province" name="province">
                                <option value="0">选择省</option>
                            </select>
                            <select class="city" id="city" name="city">
                                <option value="0">选择市</option>
                            </select>
                            <select class="district" value="0" id="district" name="district">
                                <option>选择区</option>
                            </select>
                        </div>
                        <p class="red judge"></p>
                    </div>

                    <div class="lab">
                        <div class="input-l"><span class="red">*</span> 详细地址：</div>
                        <input placeholder="请输入详细地址" id="address" name="address" class="txt" type="text">
                        <p class="red judge"></p>
                    </div>

                    <div class="lab">
                        <div class="input-l"><span class="red">*</span> 邮政编码：</div>
                        <input placeholder="如您不清楚邮政编码，请填写000000" maxlength="10" id="zipCode" name="zipCode" class="txt"
                               type="text">
                        <p class="red judge"></p>
                    </div>

                    <div class="lab">
                        <div class="input-l fuml"><span class="red">*</span>收件人姓名：</div>
                        <input placeholder="长度不超过25个字" id="receiver" class="ml-0 txt" name="receiver" type="text">
                        <p class="red judge"></p>
                    </div>

                    <div class="lab lab-tel">
                        <div class="input-l">手机号：</div>
                        <select class="country" id="cellPhone-c">
                            <option value="0">选择国家</option>
                            <option value="1" data-tel="086">中国086</option>
                            <option value="2" data-tel="084">越南084</option>
                        </select>
                        <input id="cellPhone" maxlength="20" placeholder="手机号/电话号码必填一项" type="text">
                        <p class="red judge"></p>
                    </div>

                    <div class="lab lab-phone">
                        <div class="input-l">电话号码：</div>
                        <select class="country" id="phoneNum-c">
                            <option value="0">选择国家</option>
                            <option value="1" data-tel="086">中国086</option>
                            <option value="2" data-tel="084">越南084</option>
                        </select>
                        <input id="area" maxlength="10" placeholder="区号" type="text">
                        <input id="phoneNum" placeholder="电话号码" maxlength="8" type="text">
                        <p class="red judge"></p>
                    </div>

                    <p class="red reminder">手机号、电话号码选填一项，其余为必填</p>
                    <button class="save" type=button href="javascript:;" id="saveAdressCon">保 &nbsp;&nbsp; 存</button>

                </div>
            </form>
        </div>

        <div id="addressLists" style="display: none;">
            <label>
                <input type="radio" name="addressInfo">
            </label>
        </div>
    </div>
</div>

</body>
<script src="/admins/assets/plugins/loading/loading.js"></script>
<script src="/admins/assets/plugins/vali/additional-methods.min.js"></script>
<script src="/admins/assets/plugins/vali/jquery.validate.js"></script>
<script src="/admins/assets/plugins/vali/validateform.js"></script>
<script src="/admins/assets/plugins/vali/validateExtend.js"></script>
<script src='/admins/js/neworder/newOrderAudit/containerCreateTable.js'></script>
<script src="/admins/js/neworder/newOrderAudit/containerAudit.js"></script>
</html>