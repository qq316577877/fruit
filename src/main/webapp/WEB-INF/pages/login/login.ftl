<!DOCTYPE HTML>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>登录</title>
    <link href="/admins/assets/css/login.css" rel="stylesheet" type="text/css"/>
<#--<#include "/WEB-INF/pages/base/js.ftl">-->
<#--<#import "/WEB-INF/pages/base/base.ftl"  as base>-->
    <script src="/admins/assets/plugins/jquery-1.10.2.js" type="text/javascript"></script>
    <script src="/admins/assets/plugins/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="/admins/assets/plugins/jquery-ui-1.10.4/jquery-ui-1.10.4.min.js"></script>
</head>

<#--<body style="background: url('http://picture.0ku.com/1-desktop2.jpg-20160129101708333-00007851cbd6.jpg') no-repeat center top; background-size:cover;">-->
<body style="background: url('/admins/assets/img/main-back.jpg') no-repeat center top; background-size:cover;">
<div class="login-warp" style="background: url(''); margin-top: 100px">
    <div class="warp">
        <div class="login-box">
            <div class="login-modebox" style="margin-right: 320px;">
                <h6 class="login-tit">管理后台登录</h6>

                <form action="" id="loginForm" name="loginForm" method="post" class="form-horizontal">
                    <div class="login-frame">
                        <ul>
                            <li>
                                <div class="login-input login-name J_login_item">
                                    <input name="username" type="text" class="J_username" placeholder="请输入用户名/手机号">
                                </div>
                                <!--激活输入框把clol-gray样式去掉--></li>
                            <li>
                                <div class="login-input login-pass J_login_item">
                                    <input name="password" type="password" class="J_password" placeholder="请输入密码">
                                </div>
                                <!--激活输入框把clol-gray样式去掉--></li>
                        </ul>
                    </div>
                    <div class="login-auto col-sm-offset-2 col-sm-10">
                        <label class="auto-label">
                            <input name="auto" type="checkbox" value="1" class="auto-check J_auto_login">下次自动登录
                        </label>
                    <#--<a  href="//www.0ku-alpha.com/member/password/find" title="" class="retrieve">找回密码</a>-->
                    </div>
                    <div class="login-error J_error" style="display: none">${error_msg}</div>
                    <div class="login-btn-box">
                    <#--<a href="##" class="login-btn J_login_submit" title="登录" > </a>-->
                        <input type="button" class="login-btn J_login_submit" style="display: inherit; width: 320px"
                               value="登录"></input>

                    </div>
                    <div class="login-tips"></div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>

<script src="/admins/js/form.js" type="text/javascript"></script>
<script>
    $(".J_login_submit").on("click", function (e) {
        e.preventDefault();
        submit();
    });
    $(".J_username").on('keyup', function (e) {
        $(".J_login_submit").removeClass("disabled");
        $(".J_error").hide();
    });
    $(".J_password").on('keyup', function (e) {
        if(e.keyCode == 13) {
            submit();
        } else {
            $(".J_login_submit").removeClass("disabled");
            $(".J_error").hide();
        }
    });

    function submit() {
        checkinput();
        var submitEL = $(".J_login_submit");
        if (submitEL.hasClass("disabled"))
        {
            return;
        }
        var errorEL = $(".J_error");
        submitEL.attr('disabled', "true");
        $.ajax("${login_ajax_url}", {
            data: $("#loginForm").serialize(),
            type: "POST"
        }).always(function () {
        }).done(function (data) {
            submitEL.removeAttr("disabled");
            if (data.result) {
                window.location.href = '${redir_url}';
            } else {
                errorEL.html(data.msg);
                errorEL.show();
            }
        }).fail(function () {
            submitEL.removeAttr("disabled");
        });
    }

    function checkinput() {
        var submitEL = $(".J_login_submit");
        var errorEL = $(".J_error");
        submitEL.removeClass("disabled");
        errorEL.hide();
        var input = $(".J_username").val();
        if (input == "") {
            submitEL.addClass("disabled");
            errorEL.html("用户名不能为空");
            errorEL.show();
            return;
        }
        input = $(".J_password").val();
        if (input == "") {
            submitEL.addClass("disabled");
            errorEL.html("密码不能为空");
            errorEL.show();
            return;
        }
    }
</script>

</html>
