<!DOCTYPE html>
<html class="not-ie" lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <title>授信额度管理</title>
<#include "/WEB-INF/pages/base/css.ftl">
    <link rel="stylesheet" href="/admins/assets/plugins/data-tables/DT_bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="/admins/assets/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css"/>
    <link rel="stylesheet" type="text/css" href="/admins/assets/loan/loan.css"/>
<#include "/WEB-INF/pages/base/js.ftl">
<#import "/WEB-INF/pages/base/base.ftl"  as b>
</head>

<body>

<div class="page-content extended ">
    <div class="row">
        <div class="col-xs-12" id="creditInfo">
            <span>平台总授信额度：</span><span>${credit_collect_list.creditAmount?string('0.00')}</span><br>
            <span>平台已用贷款额度：</span><span>${credit_collect_list.expenditureTotal?string('0.00')}</span><br>
            <span>平台可用贷款额度：</span><span>${credit_collect_list.balanceTotal?string('0.00')}</span><br>
            <div style="padding-top: 10px">
                <form action="${user_credit_list_url}" id="thisForm" name="thisForm" class="form-inline" method="post">
                    <input type="hidden" id="pageNo" name="pageNo" value="1"/>
                    <input type="text" class="form-control input-sm" id="keyword" name="keyword" style="width: 200px"
                           placeholder="请输入用户手机号码" style="width:80%" value="${keyword!}"></input>
                    <input type="submit" hidden tabindex="-1"/>
                    <button type="button" class="btn yellow btn-sm" onclick="query(1)">
                        <i class="fa fa-search"></i> 查 询
                    </button>

                </form>
            </div>

            <div style="padding-top: 10px">
                <table id="table_1" table-adv="true"
                       class="table table-striped table-bordered table-hover table-full-width text-center">
                    <colgroup>
                        <col width='70px'>
                        <col width='120px'>
                        <col width='100px'>
                        <col width='100px'>
                        <col width='100px'>
                        <col width='100px'>
                        <col width='100px'>
                        <col width='100px'>
                    </colgroup>
                    <thead>
                    <tr>
                        <th class="text-center">序号</th>
                        <th class="text-center">银行预留手机号</th>
                        <th class="text-center">姓名</th>
                        <th class="text-center">授信额度（元）</th>
                        <th class="text-center">已用额度（元）</th>
                        <th class="text-center">可用额度（元）</th>
                        <th class="text-center">授信状态</th>
                        <th class="text-center">电子合同</th>
                        <th class="text-center">操作</th>
                    </tr>
                    </thead>

                    <tbody>
                    <#list user_credit_list as credit>
                    <tr>
                        <td>
                        ${credit.id}
                        </td>

                        <td>
                        ${credit.mobile}
                        </td>

                        <td>
                        ${credit.username}
                        </td>

                        <td>

                        ${credit.creditLine?string('0.00')}
                        </td>

                        <td>
                        ${credit.expenditure?string('0.00')}
                        </td>

                        <td>
                        ${credit.balance?string('0.00')}
                        </td>

                        <td>
                            <#list loan_status_list as item>
                                <#if credit.status = item.id><span > ${item.value}</span></#if>
                            </#list>
                        </td>

                        <td>
                            <#if credit.contractUrl !='' >
                                <#if credit.status = 6 >
                                    <a  href="javascript:;" onclick="contracSign('${credit.userId}','${credit.contractId}','${credit.contractUrl}')">签订合同</a>
                                <#else >
                                    <a  href="javascript:;" onclick="contracShow('${credit.contractUrl}')">查看合同</a>
                                </#if>
                            <#else>
                                <a href="javascript:;"></a>
                            </#if>
                            <#--<#if credit.contractUrl =='' >-->
                              <#---->
                            <#--</#if>-->
                        </td>

                        <td>
                            <a href="${user_credit_detail_info_url}?id=${credit.id}">查看</a>
                        </td>

                    </tr>
                    </#list>
                    </tbody>
                </table>


            <@b.pageSimple pageDto 'userList'/>


            </div>
        </div>
    </div>
</div>
<div id="mask">
    <div id="pdf1" style="width:825px; height:600px; display: none">
        1It appears you don't have Adobe Reader or PDF support in this web browser.
        <a href="${credit.contractUrl}">Click here to download the PDF</a>
    </div>
</div>

<div id="maskSign">
    <div id="pop">
        <input type="hidden" id="userIdHidden" name="userIdHidden" value=""/>
        <input type="hidden" id="contractIdHidden" name="contractIdHidden" value=""/>
        <input type="hidden" id="contractUrlHidden" name="contractUrlHidden" value=""/>
        <div id="info">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您正在对安心签发出签名请求，委托安心签调用数字证书签署借款合同，数字证书一经调用立即生效。</div>

        <div class="userInfo">
            <div class="userName">验证码：</div>
            <input type="text" id="capth">
            <input type="button" value="获取验证码" id="sendBtn" >
            <p id="errormsg" style="text-align: center;color:red;visibility:hidden;">
                请输入有效的验证码
            </p>
        </div>

        <div style="margin-left: 120px; margin-top: 16px;">
            <input type="button" class="btn1" id="grantBtn" value="授权">
            <input type="button" class="btn1" id="cancelBtn" value="取消">
        </div>
    </div>
</div>

</div>

<script src="/admins/assets/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="/admins/assets/plugins/data-tables/jquery.dataTables.min.js"></script>
<script src="/admins/js/form.js" type="text/javascript"></script>
<script src="/admins/assets/plugins/pdfobject.min.js"></script>
<script>
    var timer,
            num=60;
    window.onload=function(){
        $('#maskSign').hide();



        $('#sendBtn').click(function () {
            $('#sendBtn')[0].disabled = true;
            $('#sendBtn').css('background', '#ccc');
            $('#capth').val('');
            timer = setInterval(function () {
                //clearInterval(timer)
                num--;
                $('#sendBtn').val(num + "秒后重发");
                if (num === 0) {
                    clearInterval(timer); //一定清理
                    $('#sendBtn').val('获取验证码');
                    $('#sendBtn')[0].disabled = false;//不要再禁用了
                    num = 60;
//
                }
            }, 1000)
            //发送短信给银行客户经理

            $.ajax("${contract_SmsSend_Ajax_url}", {
                data: {userId:$('#userIdHidden').val()},
                type: "POST"
            }).always(function () {
            }).done(function (data) {
                $("#errormsg").html(data.msg);
                $('#errormsg').css('visibility', 'visible');
            }).fail(function () {
            });

        });

        // 授权事件
        $('#maskSign').on('click','#grantBtn', function () {
            console.log("点击");
            var code=$("#capth").val().trim();
            // 校验
            if(code==""||code.length!=6){
                $("#errormsg").html("请输入有效的验证码");
                $('#errormsg').css('visibility', 'visible');
                return;
            }


            $.ajax("${contract_SignBorrow_Ajax_url}", {
                data: {userId:$('#userIdHidden').val(),
                    contractId:$('#contractIdHidden').val(),
                    captchaCode:$('#capth').val()},
                type: "POST"
            }).always(function () {
            }).done(function (response) {
                if(response.code == 200){
                        $('#maskSign').hide();
                            bootbox.alert("合同授权成功！",function () {
                            location.href = "${user_credit_list_url}";
                        });
                }else{
                        $("#errormsg").html(response.msg);
                        $('#errormsg').css('visibility', 'visible');
                    }
            }).fail(function () {
                $("#errormsg").html(response.msg);
                $('#errormsg').css('visibility', 'visible');
            });


        })
        $('#maskSign').on('click','#cancelBtn', function () {
            $('#maskSign').hide();
        })

    }

    function query(pageNo) {
        $('#pageNo').val(pageNo);
        var form = $("#thisForm");
        form.attr('action', form.attr('action'));
        form.submit();
    }

    function contracShow(url) {
        $('#btnCan').remove();
        $('#bntSign').remove();
        $('#pdf1').show();
        $('#mask').show();
        $('#pdf1 embed').css({
            'width': '650px',
            'height': '550px'
        });

        var tag = '<input type="button" value="关闭" id="btnCan">';
        $('#mask').append(tag);

        console.log($('#btnCan'));
        var options = {
            fallbackLink: "<p>该浏览器不支持pdf预览，请点击<a href='[" + url + "]'>此处</a>下载预览</p>"
        };
        PDFObject.embed(url, "#pdf1", options);

        $('#mask').on('click', '#btnCan', function () {
            $('#mask').hide();
        });
    }



    function contracSign(userid,contractid,contracturl) {
        $('#btnCan').remove();
        $('#pdf1').show();
        $('#mask').show();
        $('#pdf1 embed').css({
            'width': '650px',
            'height': '550px'
        });

        $('#userIdHidden').val(userid);
        $('#contractIdHidden').val(contractid)
        $('#contractUrlHidden').val(contracturl)

        var tag = '<input type="button" value="确认签订电子合同" id="btnCan">';
        $('#mask').append(tag);

        console.log($('#btnCan'));

        var options = {
            fallbackLink: "<p>该浏览器不支持pdf预览，请点击<a href='[" + contracturl + "]'>此处</a>下载预览</p>"
        };
        PDFObject.embed(contracturl, "#pdf1", options);

        $('#mask').on('click', '#btnCan', function () {

            $('#maskSign').show();
            var num =60;
            var timer;

            if(timer){
                num=60;
                clearInterval(timer);
                $('#sendBtn').val('免费获取验证码');
                $('#sendBtn')[0].disabled = false;
                $('#sendBtn').css('background', '#00aa5c').end().css('color', '#fff');
            }
            $('#errormsg').css('visibility', 'hidden');
            $('#maskSign').show();
            $("#capth").val("");
        });

    }




</script>
</body>
</html>