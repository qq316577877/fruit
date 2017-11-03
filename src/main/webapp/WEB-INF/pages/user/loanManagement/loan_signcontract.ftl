<!doctype html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>签订借据合同</title>
    <#include "/WEB-INF/pages/base/css.ftl">
    <link rel="stylesheet" href="/admins/assets/plugins/data-tables/DT_bootstrap.css"/>
    <link rel="stylesheet" href="/admins/assets/loan/loan_signcontract.css"/>
    <#include "/WEB-INF/pages/base/js.ftl">
    <#import "/WEB-INF/pages/base/base.ftl"  as b>

</head>

<body>


<!--main-->
<div class="line"></div>
<div id="pdfDiv" class="main w clearfix">
    <h2 id="header">立即放款</h2>
    <div class="main-head">
    </div>

    <div id="pdf1" style="width:700px; height:600px; display: block">
        该浏览器不支持pdf预览，
        <a id="downloanContract" href="~/pdf/CGVET22-08-2011V2P.pdf">请点击下载预览PDF</a>
    </div>
    <input type="button" id="sub" value="确认签订电子合同">
</div>

<div id="maskSign">
    <input type="hidden" id="orderIdHidden" name="orderIdHidden" value=""/>
    <input type="hidden" id="transactionNoHidden" name="transactionNoHidden" value=""/>
    <input type="hidden" id="contractIdHidden" name="contractIdHidden" value=""/>
    <input type="hidden" id="contractUrlHidden" name="contractUrlHidden" value=""/>
    <input type="hidden" id="contractStatusHidden" name="contractStatusHidden" value=""/>
    <div id="pop">
        <div id="info">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您正在对安心签发出签名请求，委托安心签调用数字证书签署借据合同，数字证书一经调用立即生效。</div>

        <div class="userInfo">
            <div class="userName">验证码：</div>
            <input type="text" maxlength="6" id="capth">
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


</body>

<script src="/admins/assets/plugins/pdfobject.min.js"></script>
<script>
    var timer,
            num=60;

    window.onload=function() {
        $('#maskSign').hide();
        $('#orderIdHidden').val("${orderNo}");
        $('#transactionNoHidden').val("${transactionNo}");
        $('#contractIdHidden').val("${contractId}");
        $('#contractUrlHidden').val("${contractUrl}");
        $('#contractStatusHidden').val("${contractStatus}");


        var  errorCode = "${errorCode}";

        if(errorCode < 0){
            bootbox.alert("${errorMsg}", function () {
                window.location.href = "${BASEPATH}/user/loanManagement/detail/base?id=${dataId}";
                return;
            });
        }


        $('#downloanContract').attr('href',"${contractUrl}");
        var options = {
            fallbackLink: "<p>该浏览器不支持pdf预览，请点击<a href='["+"${contractUrl}"+"]'>此处</a>下载预览</p>"
        };
        PDFObject.embed("${contractUrl}", "#pdf1", options);

        $('#pdfDiv').on('click', '#sub', function () {



            if( 1 == $('#contractStatusHidden').val()){
                //签署完成
                $('#maskSign').hide();
                signDebtContract();
            }else {
                $('#maskSign').show();

                var num = 60;
                var timer;

                if (timer) {
                    num = 60;
                    clearInterval(timer);
                    $('#sendBtn').val('免费获取验证码');
                    $('#sendBtn')[0].disabled = false;
                    $('#sendBtn').css('background', '#00aa5c').end().css('color', '#fff');
                }
                $('#errormsg').css('visibility', 'hidden');
                $('#maskSign').show();
                $("#capth").val("");
            }

        });


        $('#sendBtn').click(function () {
            $('#sendBtn')[0].disabled = true;
            $('#sendBtn').css('background', '#ccc');
            $('#captchaCode').val('');
            timer = setInterval(function () {
                num--;
                $('#sendBtn').val(num + "秒后重发");
                if (num == 0) {
                    clearInterval(timer); //一定清理
                    $('#sendBtn').val('获取验证码');
                    $('#sendBtn')[0].disabled = false;//不要再禁用了
                    num = 60;
//
                }
            }, 1000)
            //发送短信给银行客户经理

            $.ajax("${captcha_send_ajax}", {
                data: {orderId:"${orderNo}",
                    transactionNo:"${transactionNo}"},
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
            var code = $("#capth").val().trim();
            // 校验
            if (code == "" || code.length != 6) {
                $("#errormsg").html("请输入有效的验证码");
                $('#errormsg').css('visibility', 'visible');
                return;
            }
            signDebtContract();

        });

        $('#maskSign').on('click','#cancelBtn', function () {
            $('#maskSign').hide();
        });

    }

    function signDebtContract() {
        $.ajax("${sign_debt_contract_ajax}", {
            data: {
                orderId:"${orderNo}",
                contractId:"${contractId}",
                transactionNo:"${transactionNo}",
                captchaCode: $('#capth').val()
            },
            type: "POST"
        }).always(function () {
        }).done(function (response) {

            if (response.code == 200) {
                $('#maskSign').hide();
                bootbox.alert("授权成功,等待后台处理借款凭证，处理成功后立即放款(预计3分钟内)", function () {

                });
                window.location.href = "${BASEPATH}/user/loanManagement/detail/base?id=${dataId}";
            } else {
                bootbox.alert("授权失败:"+response.msg, function () {

                });
                $("#errormsg").html(response.msg);
                $('#errormsg').css('visibility', 'visible');
            }
        }).fail(function () {
            $("#errormsg").html(response.msg);
            $('#errormsg').css('visibility', 'visible');
        });

    }
</script>

</html>