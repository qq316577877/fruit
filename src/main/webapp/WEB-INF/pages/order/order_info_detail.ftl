<!DOCTYPE html>
<html class="not-ie" lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <title>用户列表</title>
<#include "/WEB-INF/pages/base/css.ftl">
    <link rel="stylesheet" href="/admins/assets/css/order/upload.css">
    <link rel="stylesheet" href="/admins/assets/plugins/data-tables/DT_bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="/admins/assets/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css"/>
    <link rel="stylesheet" href="/admins/assets/css/order/base.css">
<#--<link rel="stylesheet" href="/admins/assets/css/order/vipCenter.css">-->
    <link rel="stylesheet" href="/admins/assets/css/order/order_audit.css">
<#--<link rel="stylesheet" href="/admins/assets/css/order/newImportOrder.css">-->
    <link rel="stylesheet" href="/admins/assets/css/order/importStep2.css">
    <link rel="stylesheet" href="/admins/assets/css/order/orderReview.css">
    <link rel="stylesheet" href="/admins/assets/css/order/x/iconfont.css">
<#include "/WEB-INF/pages/base/js.ftl">
<#import "/WEB-INF/pages/base/base.ftl"  as b>
</head>
<body>
<div id="order" style="display: none">${orderId}</div>
<div class="audit_head">
    <a href="/admin/order/list">订单中心</a>
    >审核订单
</div>

<div class="audit_info">
<#--<div><span class="audit_h">订单号</span>：10000120170411001</div>-->
<#--<div>下单日期：2017-04-11</div>-->
<#--<div>采购商：浙江温州市大东水果批发商</div>-->
<#--<div>历史成交：10单</div>-->
<#--<div>最近交易时间：2017-3-25</div>-->
<#--<div>最近交易订单号：10000120170325001</div>-->
<#--<div>账号：18664938582</div>-->
<#--<div>联系人：刘达</div>-->
<#--<div>联系电话：13252114152</div>-->
<#--<div>订单状态：待审核</div>-->
    <div class="audit_btn">
        <input id="pass" type="button" value="审核通过">
        <input id="canc" type="button" class="cancelOrder" value="取消订单">
    </div>
</div>

<div class="orderStep">
    <a id="orderOne" class="clickStep" href="javascript:;">订单第一步</a>
    <a id="orderTwo" href="javascript:;">订单第二步</a>
</div>

<div id="oneStep">
    <div class="main">
        <div class="order-type">
            <h2>订单类型</h2>
            <label>
                <div class="type-1">
                    <input type="radio" id="agency" name="type"/>
                    <span>海外代采</span>
                    <span>（平台提供物流代采、物流通关、保险资金一条龙服务）</span>
                </div>
            </label>
            <label>
                <div class="type-1 type-2">
                    <input type="radio" id="direct" name="type"/>
                    <span>通关物流</span>
                    <span>（您直接联系供应商进行采购，平台可提供物流通关服务）</span>
                </div>
            </label>
        </div>
        <div class="supplier">
            <h2>供应商</h2>
            <div class="supplier-2">
                <span>供应商</span>
                <select name="" id="" class="selsct">
                    <option selected>请选择供应商</option>
                </select>
                <button class="add-supplier">新增供应商</button>
            </div>
            <ul id="suppierList">
                <li><span>供应商名称：</span>越南正心火龙果工厂</li>
                <li><span>所在地区：</span>越南</li>
                <li><span>详细地址：</span>越南同塔省沙沥市黎利路3坊3组284/3A号</li>
                <li><span>邮政编码：</span>000000</li>
                <li><span>联系人：</span>胡志敏</li>
                <li><span>手机号：</span>86-1391234567</li>
            </ul>
        </div>
        <div class="supplier" id="orderList">
            <h2>采购商品</h2>
            <div class="container-add container-add-3">
                <p>商品信息不可为空，请先添加货柜商品</p>
                <button class="container-btn" id="container-add-1">新增一个货柜</button>
            </div>

            <div class="pro-list clearfix" id="pro_list">
                <p>*商品成交价波动较大，以客服回访商议为准，如您下单时需要了解商品价格，
                    可致电4008-830-830或联系企业QQ400454580询价</p>
                <div class="pro-list-1" id="p-l-1">
                    <div class="pro-list-head">
                        <!--<h3>货柜批次号：</h3>-->
                        <!--<h3 class="container-name">货柜名称：</h3>-->
                        <!--<h3 class="container-norms">货柜规格：0-1400箱</h3>-->
                        <!--<div class="x">-->
                        <!--<i class="iconfont icon-chacha cancel cancel-1 fr"></i>-->
                        <!--</div>-->
                    </div>
                    <div class="pro-list-body clearfix" id="pro-list-body">
                        <div class="hide_inform">
                            <input type="text" class="hideSum"/>
                        </div>
                        <table>
                            <tr class="list-head">
                                <th class="first-th">商品名称</th>
                                <th class="two-th">等级</th>
                                <th class="three-th">大小</th>
                                <th class="four-th">品种</th>
                                <th style="color: #ff4848">成交价/箱</th>
                                <th style="margin-left: 5px">数量</th>
                                <th class="total">合计</th>
                            </tr>
                            <tr class="clone-1">
                                <td class="in-1">
                                    <input type="text" class="first-td"/>
                                </td>
                                <td class="in-2">
                                    <select>
                                        <option>一级</option>
                                    </select>
                                </td>
                                <td class="in-3">
                                    <select>
                                        <option>大</option>
                                    </select>
                                </td>
                                <td class="in-4">
                                    <select>
                                        <option>红心</option>
                                    </select>
                                </td>
                                <td class="price">
                                    <input type="number" placeholder="0.00"/>
                                    <span>元</span>
                                </td>
                                <td class="amount">
                                    <input type="number"/>
                                    <span>箱</span>
                                </td>
                                <td class="price-all">
                                    <input type="text" value="0.00"/>
                                    <span>元</span>
                                </td>
                            </tr>
                            <tr>
                                <td class="in-1">
                                    <input type="text" class="first-td"/>
                                </td>
                                <td class="in-2">
                                    <select>
                                        <option>一级</option>
                                    </select>
                                </td>
                                <td class="in-3">
                                    <select>
                                        <option>大</option>
                                    </select>
                                </td>
                                <td class="in-4">
                                    <select>
                                        <option>红心</option>
                                    </select>
                                </td>
                                <td class="price">
                                    <input type="number" placeholder="0.00"/>
                                    <span>元</span>
                                </td>
                                <td class="amount">
                                    <input type="number"/>
                                    <span>箱</span>
                                </td>
                                <td class="price-all">
                                    <input type="text" value="0.00"/>
                                    <span>元</span>
                                </td>
                            </tr>
                            <tr>
                                <td class="in-1">
                                    <input type="text" class="first-td"/>
                                </td>
                                <td class="in-2">
                                    <select>
                                        <option>一级</option>
                                    </select>
                                </td>
                                <td class="in-3">
                                    <select>
                                        <option>大</option>
                                    </select>
                                </td>
                                <td class="in-4">
                                    <select>
                                        <option>红心</option>
                                    </select>
                                </td>
                                <td class="price">
                                    <input type="number" placeholder="0.00"/>
                                    <span>元</span>
                                </td>
                                <td class="amount">
                                    <input type="number"/>
                                    <span>箱</span>
                                </td>
                                <td class="price-all">
                                    <input type="text" value="0.00"/>
                                    <span>元</span>
                                </td>
                            </tr>

                        </table>
                        <div class="add-h add-h-1">
                            <span>+</span>
                            <span>添加一行</span>
                        </div>
                        <div class="sum clearfix">
                            <span>总计：</span>
                            <input type="text" value="0.00" disabled/>
                            <span>元</span>
                        </div>
                    </div>
                </div>

                <button style="display: block" class="ad" id="ad_1">新增一个货柜</button>

            </div>
        </div>

        <button class="order-next" id="next_1">保存</button>
        <button class="order-next" id="next_2">下一步</button>
    </div>

</div>

<div id="twoStep">
    <div class="main">
        <div class="adress">
            <div class="adress-head">
                <h2>收货地址</h2>

            </div>
            <div class="adress-body">
                <div class="adress-1">
                    <h4>收货人</h4>
                    <select id="select">
                        <option value="">请选择</option>
                    </select>
                <#--<a id="add_1" href="#">增加新收件人</a>-->
                </div>
                <ul id="select_list">
                    <li>
                        <span>收件人：</span>
                        邱老板
                    </li>
                    <li>
                        <span>所在地区：</span>

                        <div class="country"></div>
                        <div class="province"></div>
                        <div class="xian"></div>
                    </li>
                    <li>
                        <span>详细地址：</span>
                        邱老板
                    </li>
                    <li>
                        <span>邮政编码：</span>
                        邱老板
                    </li>
                    <li>
                        <span>手机：</span>
                        邱老板
                    </li>
                    <li>
                        <span>固定电话：</span>
                        邱老板
                    </li>
                </ul>
            </div>
        </div>
        <div class="adress logistics">
            <div class="adress-head">
                <h2>物流服务</h2>
            </div>
            <div class="adress-body">
                <div class="logistics_1">
                    <span>运输方式</span>

                    <div class="logistics_2">
                        <input type="radio" name="logistics"/>
                        海运
                    </div>
                    <div class="logistics_2">
                        <input type="radio" name="logistics"/>
                        陆运
                    </div>
                </div>
                <div class="logistics_1">
                    <span>国际物流</span>
                    <span class="international-logistic">国际物流公司A</span>
                </div>
                <div class="logistics_1 logistics_3">
                    <span>国内物流</span>
                    <span class="international-logistic">宁波长运物流公司B</span>
                </div>
            </div>
        </div>
        <div class="adress trade">
            <div class="adress-head">
                <h2>贸易方式</h2>
            </div>
            <div class="adress-body">
                <div class="trade_1">
                    <input type="radio" name="trade"/>
                    <span>FOB</span>
                    <span>(离岸价，成本)</span>
                </div>
                <div class="trade_1">
                    <input type="radio" name="trade"/>
                    <span>CIF</span>
                    <span>(到岸价，成本+运费+保险费)</span>
                </div>
            </div>
        </div>
        <div class="adress customsClearance">
            <div class="adress-head">
                <h2>通关服务</h2>
            </div>
            <div class="adress-body">
                <div class="customsClearance_1">
                    <input type="checkbox" checked disabled name="customs"/>
                    <span>报关</span>
                </div>
                <div class="customsClearance_1">
                    <input type="checkbox" checked disabled name="customs"/>
                    <span>清关</span>
                </div>
                <div class="customsClearance_2">
                    <span>清关公司</span>
                    <span>广西进口清关有限责任公司B</span>
                </div>
            </div>
        </div>
        <div class="adress insurance">
            <div class="adress-head">
                <h2>保险服务</h2>
            </div>
            <div class="adress-body">
                <div class="insurance_1">
                    <input type="checkbox" checked disabled name="insurance"/>
                    <span>保险</span>
                    <span>（保险为保障您的货物安全，为第三方保险承保）</span>
                </div>

            </div>
        </div>
        <div class="adress money">
            <div class="adress-head">
                <h2>资金服务</h2>
            </div>
            <div class="adress-body">
                <div class="loan">
                    <div class="loan-limit">
                        可用贷款额度：0
                    </div>
                    <a href="javascript:;" class="loan-cal" id="loan-cal">
                        贷款计算器
                    </a>
                </div>
                <p class="loan-info">（利息按天计算，日利率低至万3.35）</p>

                <div class="need-loan">
                    <label for="">
                        <input type="radio" name="need-loan"/>
                        <span>需要</span>
                    </label>
                    <label for="">
                        <input type="radio" name="need-loan"/>
                        暂不需要
                    </label>
                </div>
                <table class="loan-table">
                    <tr class="t-head">
                        <th>货柜批次号</th>
                        <th>货柜名称</th>
                        <th>可贷款金额</th>
                        <th>申请金额</th>
                        <th>确认金额</th>
                        <th>服务费</th>
                    </tr>
                    <tr class="tab-tr">
                        <td>20170517001</td>
                        <td>越南火龙果</td>
                        <td>50,000.00元</td>
                        <td>
                            <input type="text" class="fl"/>
                            <span class="fl">元</span>
                        </td>
                        <td>
                            <input type="text" class="fl"/>
                            <span class="fl">元</span>
                        </td>
                    </tr>
                    <tr class="tab-tr">
                        <td>20170517001</td>
                        <td>越南香蕉</td>
                        <td>50,000.00元</td>
                        <td>
                            <input type="text" class="fl"/>
                            <span class="fl">元</span>
                        </td>
                        <td>
                            <input type="text" class="fl"/>
                            <span class="fl">元</span>
                        </td>
                    </tr>
                </table>

                <div class="attention">
                    *说明：申请金额必须为1000的整数倍,贷款期限以实际放款日与实际还款日为准 <br/>
                    放款时间：验货发车后24小时以内<br/>
                    还款时间：预计到货时间前一天还款<br/>
                </div>
            </div>
        </div>
        <div class="adress contract">
            <div class="adress-head">
                <h2>采购合同</h2>
            </div>
            <div class="adress-body clearfix">
                <div class="contract_1 clearfix">
                    <h5><span style="color: #ff4848">*</span>上传采购合同</h5>

                    <div class="big-photo">
                        <div id="preview">
                            <div id="imgPicker"><img id="showImg" src="/admins/assets/img/img_1_r2_c2.gif"/></div>
                        </div>

                    </div>
                </div>
                <div class="contract_1 clearfix">
                    <h5>付款凭证</h5>

                    <div class="big-photo">
                        <div id="preview_2">
                            <div id="imgPicker2"><img id="showImg2" src="/admins/assets/img/img_1_r2_c2.gif"/></div>
                        </div>

                    </div>
                </div>
                <p class="clearfix add_info"><span>温馨提示：</span>证照只支持3M以下的jpg、png、pdf、jpeg格式的图片</p>
            </div>
        </div>
        <div class="adress settlement">
            <div class="adress-head">
                <h2>结算方式</h2>
            </div>
            <div class="adress-body">
                <label for="">
                    <div class="trade_1">
                        <input type="radio" name="settlement"/>
                        <span>预付全款</span>
                        <span>(预付订单所有款项)</span>
                    </div>
                </label>
                <label for="">
                    <div class="trade_1">
                        <input type="radio" name="settlement"/>
                        <span>预付货款</span>
                        <span>(预付订单所有货款)</span>
                    </div>
                </label>
                <label for="">
                    <div class="trade_1">
                        <input type="radio" name="settlement"/>
                        <span>预付定金</span>
                        <span>(预付40%-60%定金，货到结算尾款)</span>
                    </div>
                </label>
            </div>
        </div>
        <div class="order-type clearfix" id="money_list">
            <span class="">费用清单</span>
            <table class=" clearfix">
                <tbody>
                <tr>
                    <th>货柜批次号</th>
                    <th>采购货款</th>
                    <th>进口代理费</th>
                    <th>保险费</th>
                </tr>
                <#--<tr>-->
                <#--<td>11170622789101</td>-->
                <#--<td> 500000元</td>-->
                <#--<td><input value="0.00" type="text"><span>元</span></td>-->
                <#--<td><input value="0.00" type="text"><span>元</span></td>-->
                <#--</tr>-->
                </tbody>
            </table>
            <p id="info">*说明：费用清单所列费用为我平台收取费用，进口代理费、保险费可能存在较小误差，多退少补</p>
        </div>
        <div class="order-type settlement clearfix">
            <span class="fl" style="margin-right: 120px">结算流程</span>
            <div class="settlement_1 ">
                <div>
                    预付款：
                    <span id="settlement_1"><input id="advance" type="text">元</span>
                <#--<select name="" id="settle1">-->
                <#--<option value="">已支付</option>-->
                <#--<option value="">未支付</option>-->
                <#--</select>-->
                </div>
                <div>
                    尾&nbsp;&nbsp;&nbsp;&nbsp;款：
                    <span id="settlement_2"><input id="restPay" type="text">元</span>
                <#--<select name="" id="settle2">-->
                <#--<option value="">已支付</option>-->
                <#--<option value="">未支付</option>-->
                <#--</select>-->
                </div>
            </div>
        </div>
        <div class="order-type settlement clearfix">
            <span class="fl" style="margin-right: 120px">备注</span>
            <textarea id="note">

            </textarea>
        </div>
        <div class="submit">
            <input type="button" value="保存" id="next_step"/>
        </div>
    </div>

</div>
<!--弹出框-->
<!--mask-->
<div class="mask">
    <div class="inform inform-2" style="display: none" id="inform2">
        <div class="inform-head">
            <span class="fl">新增货柜</span>
            <i class="iconfont icon-chacha cancel fr"></i>
        </div>
        <div class="inform-body">

            <div class="lab">
                <label>
                    <div class="input-l"><span class="red">*</span> 水果类型：</div>
                    <select class="type_product" id="type_pro">
                        <option>请选择</option>

                    </select>
                    <p class="red judge"></p>
                </label>
            </div>

            <div class="lab">
                <label>
                    <div class="input-l input-2"><span class="red">*</span> 最高容量：</div>
                    <input maxlength="10" id="num-fruit" class="txt num-fruit" type="text"/>
                    <p class="red judge"></p>
                </label>
            </div>

            <button class="save save-1" id="save_2">确&nbsp;定</button>
        </div>
    </div>
    <div class="inform " style="display: none" id="inform_2">
        <div class="inform-head">
            <span class="fl">贷款计算器</span>
            <i class="iconfont icon-chacha cancel fr" id="cancel_1"></i>
        </div>
        <div class="inform-body">
            <label for="">
                <span>借多少</span>
                <input type="text" id="money_loan"/>
                <span>元</span>
            </label>
            <span id="empty">请输入正整数</span>
            <label for="">
                <span>借多久</span>
                <input type="text" id="money-day"/>
                <span>天</span>
            </label>
            <span id="wrong-day">钱数不能为空</span>
            <label for="">
                <span>怎么还</span>
                <select name="" id="">
                    <option value="">
                        到期还本付息
                    </option>
                </select>
            </label>

            <p>月利率&nbsp;<span id="monthRet"></span> &nbsp;&nbsp; 服务费&nbsp;0.1%</p>

            <div class="btn-ca">
                <button id="btn_1">计算</button>
                <button id="btn_2">重置</button>
            </div>
            <h4>还款详情</h4>

            <div class="loan-details">
                <div>到期还本金</div>
                <div>借满天总利息</div>
                <div>借款服务费</div>
            </div>
        </div>

    </div>
    <div class="inform" id="canOrder">
        <p>是否取消订单</p>
        <div class="btn">
            <input id="can_1" type="button" value="确认">
            <input id="can_2" type="button" value="取消">
        </div>
    </div>
</div>


<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" style="width: 550px;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">请选择导出条件</h4>

            </div>
            <div class="modal-body">
                <div class="col-md-12">
                    <div class="row">

                        <div style="padding-top: 10px">
                            <span>用户类型：</span>
                        <#list user_type_list as item>
                            <#if item.id gt -1>
                                <input type="checkbox" name="type" id="type" value="${item.id}"
                                       <#if item.id == 1 ||item.id ==2>checked="checked"</#if>/> ${item.value}
                            </#if>
                        </#list>
                        </div>


                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary export-btn" data-id="1">确认</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                </div>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal-dialog -->
    </div>
</div>

<script src="/admins/assets/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
<script src="/admins/js/form.js" type="text/javascript"></script>

<script src="/admins/assets/plugins/webuploader-0.1.5/webuploader.js"></script>
<script src="/admins/assets/plugins/webuploader-0.1.5/uploadInit.js"></script>
<script src="/admins/assets/plugins/webuploader-0.1.5/uploadPath.js"></script>

<#--<script src="/admins/js/order/orderAudit/newOrderAudit.js"></script>-->
<script src="/admins/js/order/orderAudit/orderAudit_bak.js"></script>
<script src="/admins/js/order/orderAudit/orderFunc.js"></script>
<script src="/admins/js/order/orderAudit/orderType.js"></script>
<#--<script src="/admins/js/order/orderAudit/existData.js"></script>-->
<script src="/admins/js/order/orderAudit/existData_bak.js"></script>
<script src="/admins/js/order/orderAudit/orderSecondStep.js"></script>

<script>


    function query(pageNo) {
        $('#pageNo').val(pageNo);
        var form = $("#thisForm");
        form.attr('action', form.attr('action'));
        form.submit();
    }

    $(".export-btn").on('click', function (e) {
        e.preventDefault();
        var checkstr = "";
        $("input:checkbox[name='type']:checked").each(function (i) {
            if (checkstr != "") {
                checkstr += ",";
            }
            checkstr += $(this).val();
        });
        var url = "${BASEPATH}/user/export?type=" + checkstr;

        var d = dialog({
            title: '导出用户数据',
            url: url,
            iframe_width: 300,
            iframe_height: 150,
            zIndex: 9996,
            quickClose: false,
        });
        top.openDialog = d;   //保存后如果需要关闭窗口
        d.show();

    });
</script>
</body>
</html>