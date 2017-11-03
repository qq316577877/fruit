<!DOCTYPE html>
<html class="not-ie" lang="en">
<head>
    <meta charset="utf-8"/>
    <title>九创金服管理后台</title>
<#include "/WEB-INF/pages/base/css.ftl">
    <link rel="stylesheet" href="/admins/assets/css/pages/index.css"/>

<#include "/WEB-INF/pages/base/js.ftl">
<#import "/WEB-INF/pages/base/base.ftl"  as b>
    <style type="text/css">
        #notif {
            position: fixed;
            bottom: 30px;
            right:40px;
            margin-left: 5px;
            z-index:99;
        }
        .notification {
            color: #000;
            background-color: #E8F6FC;
            text-align: left;
            margin-top: 8px;
            padding: 15px 15px 5px 15px;
            border-left: 5px solid #57b5e3;
            float: right;
            clear: right;
            width: 335px;
        }
        .hidetag {
            width: 60px;
            padding-top: 10px;
            margin-bottom: -40px;
            margin-left: 240px;
            position: relative;
            text-align: right;
            bottom: 21px;
            color: #ccc;
            cursor: pointer;
        }
    </style>
</head>
<body>
<form action="#" id="thisForm" name="thisForm" method="post">
    <input type="hidden" id="loginNo" name="loginNo">
</form>
<div class="header navbar navbar-inverse navbar-fixed-top">
    <div id="new-notice-voice" class="new-notice-voice" style="display:none"></div>
    <div class="header-inner">
        <!-- LOGO -->
        <a class="navbar-brand" href="${BASEPATH}/index" style="margin-left: 20px">
            九创金服管理后台
        </a>
        <!--右侧导航按钮-->
        <ul class="nav navbar-nav pull-right">
            <!-- 登陆用户信息 -->
            <li class="dropdown user">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown"
                   data-close-others="true" style="background-color: transparent;">
                    <#--<span class="nav-user-photo"></span>-->
                    <#if _header_admin_notice_count! && _header_admin_notice_count gt 0>
                        <i class="fa fa-bell-slash-o head-notice-count" id="head-notice-count" style="color: red"></i>
                    <#else>
                        <i class="fa fa-bell-slash-o head-notice-count" id="head-notice-count" style="color: red; display: none"></i>
                    </#if>
                    <span class="username" style="color: #ffffff">欢迎您，系统管理员，ID：${(USERINFO.sysId)!}，${(USERINFO.userName)!}</span>
                    <i class="fa fa-angle-down"></i>
                </a>
                <ul class="dropdown-menu">
                    <li>
                        <a href="javascript:void(0)" onclick="showNC();return false;">
                            <i class="fa fa-bell-slash-o"></i>通知中心
                            <#if _header_admin_notice_count! && _header_admin_notice_count gt 0>
                                <i class="menu-notice-count" id="menu-notice-count" style="color: red">${_header_admin_notice_count}</i>
                            <#else>
                                <i class="menu-notice-count" id="menu-notice-count" style="color: red; display: none">${_header_admin_notice_count}</i>
                            </#if>

                        </a>
                        <a href="javascript:void(0)" onclick="logout();return false;"><i class="fa fa-key"></i>用户注销</a>
                    </li>
                </ul>
            </li>
            <!-- 登陆用户信息 -->
        </ul>
        <!--右侧导航按钮-->
    </div>
    <!-- header-inner -->
    <!-- END TOP NAVIGATION BAR -->
</div>

<div class="main-container" id="main_container">
    <div class="main-container-inner">
        <a class="menu-toggler" id="menu-toggler" href="#">
            <span class="menu-text"></span>
        </a>

        <div class="sidebar" id="sidebar">
        <@b.menu AdminMenus/>     <!-- 左侧菜单 -->
        </div>
        <div class="main-content" id="main_content">
            <div class="breadcrumbs" id="breadcrumbs">
                <ul id="page_breadcrumb" class="page-breadcrumb breadcrumb">
                    <li>
                        <i class="fa fa-globe home-icon"></i>
                        <a href="${BASEPATH}/index" class="btn">首页</a>
                    </li>
                </ul>
            </div>
            <iframe SRC="${BASEPATH}/main" id="content" frameborder="0" scrolling="auto" marginheight="0"
                    marginwidth="0" name="content" width="100%">

            </iframe>

        </div>
        <!-- /.main-container-inner -->
    </div>
    </div>
    <div id="notif"></div>
    <!-- /.main-container -->

    <!-- BEGIN PAGE LEVEL SCRIPTS -->
    <script src="/admins/js/index.js" type="text/javascript"></script>
    <!-- END PAGE LEVEL SCRIPTS -->
    <script>
        var noticeCount = ${_header_admin_notice_count!0};
        var intervals;
        var twinkleFlag = true;

        jQuery(document).ready(function () {
            $("#main_content").css("height", $(window).height() - 50);
            $("#content").attr("height", $(window).height() - 100);
            App.init(); //公共组件的初始化
            Index.init();


            //TODO 消息中心
//            // 第一次询问
//            setTimeout(function () {
//                inquireNotice();
//            }, 3000);
//            /*60s轮询*/
//            setInterval(function () {
//                inquireNotice();
//            }, 1000 * 60);
        });

        function reLogin(loginNo, roleid, dept) {
            $("#roleid").val(roleid);
            $("#loginNo").val(loginNo);
            $("#dept").val(dept);
            var form = $("#thisForm");
            form.attr('action', "/reLogin.htm")
            form.submit();
        }

        function loginConfig() {
            var url = "/loginConfigDetail.htm";
            var d = dialog({
                title: '用户登录配置',
                url: url,
                iframe_width: 600,
                iframe_height: 200,
                zIndex: 9996,
                quickClose: true
            });
            d.show();
        }

        function logout() {
            top.location.href = "${logoutUrl!}";
        }

        function showNC() {
            <#--window.location.href = "${BASEPATH}/notice/center/list";-->
            window.location.href = window.location.href.replace(/#/g, '');
        }

        function inquireNotice() {
            $.ajax({
                url: '${BASEPATH}/notice/center/inquire',
                type: 'POST',
                success: function (json) {
                    if (json.result) {
                        if (json.datas.notice_count > noticeCount) {
                            play();
                            $("#head-notice-count").show();
                            $("#menu-notice-count").show();
                            showMsg(json.datas.notice_list);
                        }
                        if (json.datas.notice_count == 0) {
                            $("#head-notice-count").hide();
                            $("#menu-notice-count").hide();
                        }
                        noticeCount = json.datas.notice_count;
                        $("#menu-notice-count").html(json.datas.notice_count);
                    }
                }
            });
        }

        function play(){
            if($.browser.msie && $.browser.version=='8.0'){
                $('#new-notice-voice').html('<embed src="/admins/assets/audio/new_msg.mp3"/>');
            }else{
                //IE9+,Firefox,Chrome均支持<audio/>
                $('#new-notice-voice').html('<audio autoplay="autoplay"><source src="/admins/assets/audio/new_msg.mp3" type="audio/mpeg"/></audio>');
            }
        }

        function showMsg(array) {
            for (var i = 0; i < array.length; i++) {
                var msg = array[i].sysCenterMsg;
                var id = array[i].id;
                notification(id, msg, 8000);
            }
        }

        function notification(id, text, time) {
            var html_element,
                    time = time,
                    $cont = $('#notif');

            if(time){
                time = time;
            }else{
                time = 5000;
            }
            /*if (icon) {
                icon_span = "<span class='" + icon + "'></span> ";
            }*/
            var div = '<div class="notification">' +
                    '   <div data-id="' + id + '" class="hidetag">已阅</div>' +
                    '   <div style="padding: 15px 10px;">' + text + '</div>' +
                    '</div>';
            html_element = $(div).fadeIn('fast', function(){
                if (intervals == undefined){
                    intervals = setInterval("scroll('九创金服管理后台')", 500);
                }
            });
            $cont.append(html_element);
            var $div = $('div.notification:visible', $cont);
            /*if ($div.length > 4) {
                $div.eq(0).fadeOut('normal', function() {
                    $(this).hide();
                });
            }*/
            if ($div.length > 4) {
                var $divs = $('div.notification:visible:lt(' + ($div.length - 4) + ')', $cont);
                $divs.each(function(i) {
                    $(this).fadeOut('normal', function() {
                        $(this).hide();
                    });
                });
            }
            $('div.hidetag', html_element).on('click', function() {
                check($(this), html_element);
            });
            /*setTimeout(function() {
                notification_close($cont.children('.notification').first());
            }, time);*/
        }

        function notification_close(elem) {
            if (elem.siblings().length == 0) {
                clearInterval(intervals);
                document.title = "九创金服管理后台";
                intervals = undefined;
            }
            if (typeof elem !== "undefined") {
                elem.fadeOut('fast', function() {
                    $(this).remove();
                });
            } else {
                $('.alert').fadeOut('fast', function() {
                    $(this).remove();
                });
            }

            var $cont = $('#notif');
            var $div = $('div.notification:hidden', $cont);
            if ($div.length > 0) {
                $div.last().fadeIn('normal');
            }
        }

        function check(elem, notification) {
            var id = elem.attr("data-id");

            notification_close(notification);
            var count = parseInt($("#menu-notice-count").html());
            if (count > 1){
                $("#menu-notice-count").html(--count);
                noticeCount = count;
            } else if (count == 1) {
                $("#head-notice-count").hide();
                $("#menu-notice-count").hide();
                noticeCount = 0;
            }

            $.ajax('${BASEPATH}/notice/center/check_ajax?id=' + id, {
                data: {

                },
                type: "POST",
                dataType: "json"
            }).always(function () {
            }).done(function () {
            }).fail(function () {
            });
        }

        function scroll(title) {
            var lbl1 = "【新消息】";
            var lbl2 = "【      】";
            if (twinkleFlag)
            {
                document.title = lbl1 + title;
                twinkleFlag = false;
            }
            else
            {
                document.title = lbl2 + title;
                twinkleFlag = true;
            }
        }
    </script>
    <!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>
