<!DOCTYPE html>
<html class="not-ie" lang="en">
<head>
    <meta charset="utf-8"/>
    <title>缓存管理工具</title>
<#include "/WEB-INF/pages/base/css.ftl">
<#include "/WEB-INF/pages/base/js.ftl">
    <link rel="stylesheet" type="text/css" href="/admins/assets/plugins/upload-custom/ihover.css"/>
    <link rel="stylesheet" href="/admins/assets/css/base.css"/>
    <link href="/admins/assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<#import "/WEB-INF/pages/base/base.ftl"  as base>
</head>

<body>
<div class="page-content extended " style="padding-top: 10px; padding-left: 30px">
    <div class="row">
        <div class="col-xs-8">
            <div class="note note-info">
                <h4 class="block">同步用户信息</h4>

                <form name="syncUserModel" action="${BASEPATH}/cache/sync_user_model_ajax">
                    UserId：<input type="text" id="user_id" name="user_id"/>
                    <input type="button" value="同步" class="submit-btn btn red btn-sm">
                </form>
            </div>

            <div class="note note-warning">
                <h4 class="block">同步商品信息</h4>

                <form name="syncOrderContent" action="${BASEPATH}/cache/sync_product_info_ajax">
                    <!-- ProductId：<input type="text" id="product_id" name="product_id"/>-->
                    <input type="button" value="同步" class="submit-btn btn red btn-sm">
                </form>
            </div>

            <div class="note note-warning">
                <h4 class="block">同步首页数据</h4>

                <form name="syncHomeData" action="${BASEPATH}/cache/sync_home_data_ajax">
                  <!--   Type：<input type="text" id="type" name="type"/> -->
                    <input type="button" value="同步" class="submit-btn btn red btn-sm">
                </form>
            </div>

            <!-- <div class="note note-warning">
                <h4 class="block">同步全局上下文</h4>

                <form name="syncGlobalContext" action="${BASEPATH}/cache/sync_global_context_ajax">
                    UserId：<input type="text" id="user_id" name="user_id"/>
                    UserToken：<input type="text" id="user_token" name="user_token"/>
                    <input type="button" value="同步" class="submit-btn btn red btn-sm">
                </form>
            </div>

            <div class="note note-warning">
                <h4 class="block">同步消息中心上下文</h4>

                <form name="syncGlobalContext" action="${BASEPATH}/cache/sync_mc_context_ajax">
                    BizId：<input type="text" id="biz_id" name="biz_id"/>
                    <input type="button" value="同步" class="submit-btn btn red btn-sm">
                </form>
            </div>

            <div class="note note-warning">
                <h4 class="block">生成卖家入驻协议</h4>

                <form name="genAgreement" action="${BASEPATH}/cache/gen_agreement_ajax">
                    SellerId：<input type="text" id="user_id" name="user_id"/>
                    <input type="button" value="生成" class="submit-btn btn red btn-sm">
                </form>
            </div>

            <div class="note note-warning">
                <h4 class="block">刷新分类缓存</h4>

                <form name="genAgreement" action="${BASEPATH}/cache/spy_sync_ajax?type=category">
                    <input type="button" value="刷新" class="submit-btn btn red btn-sm">
                </form>
            </div>

            <div class="note note-warning">
                <h4 class="block">刷新品牌缓存</h4>

                <form name="genAgreement" action="${BASEPATH}/cache/spy_sync_ajax?type=brand">
                    <input type="button" value="刷新" class="submit-btn btn red btn-sm">
                </form>
            </div> -->
        </div>
    </div>
</div>
<script>
    $('.submit-btn').on("click", function (e) {
        var form = $(this).parent('form');
        var form_url = form.attr('action');
        var data = form.serialize();
        /*alert(form_url + " " + data);*/
        $.ajax(form_url, {
            data: data,
            type: "POST"
        }).always(function () {
        }).done(function (data) {
            if (data.result) {
                bootbox.alert("操作成功!");
            } else {
                bootbox.alert(data.msg);
            }
        }).fail(function () {
        });
    });
</script>
<script>
</script>
</body>
</html>
