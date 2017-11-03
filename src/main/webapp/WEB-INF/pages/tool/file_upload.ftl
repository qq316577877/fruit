<!DOCTYPE html>
<html class="not-ie" lang="en">
<head>
    <meta charset="utf-8"/>
    <title>文件上传小工具</title>
<#include "/WEB-INF/pages/base/css.ftl">
<#include "/WEB-INF/pages/base/js.ftl">
    <link rel="stylesheet" type="text/css" href="/admins/assets/plugins/upload-custom/ihover.css"/>
    <link rel="stylesheet" href="/admins/assets/css/base.css"/>
    <link href="/admins/assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<#import "/WEB-INF/pages/base/base.ftl"  as base>
</head>

<body>
<div class="page-content extended " style="padding-top: 50px; padding-left: 50px">
    <div class="col-xs-12">
        <form id="uploadForm" name="uploadForm" action="" method="post" enctype="multipart/form-data">
            <div style="padding-bottom: 15px">
                图片URL是否加密：
                <select id="open" name="open" style="width: 150px;height: 30px">
                    <option value="0" <#if open == 0>selected</#if>>加密</option>
                    <option value="1" <#if open == 1>selected</#if>>不加密</option>
                </select>
            </div>
            <input type="file" name="pic" id="pic" style="height:30px;padding-bottom: 15px"/>
            <input type="submit" onclick="uploaded()" style="width:100px;height:30px;">
        </form>
    </div>
    <div class="col-xs-12" id="image_path" style="padding-top: 15px">
        <p style="color: red">${error_message!}</p>
        上传成功后将此文本复制提交到数据库：
        <input type="text" id="image_path" value="${file_path}" style="width: 600px;height: 30px"/><br/>
    <#if file_url?? && file_url?length gt 0>
        <div style="padding-top: 15px">
            <a href="${file_url}" target="_blank">点击这里查看、下载上传的文件</a>
        </div>
    </#if>
    </div>
    <div class="col-xs-12" id="img_show" style="padding-top: 15px">
    <#if image_url?? && image_url?length gt 0>
        <img src="${image_url}" height="400" width="600">
    </#if>
    </div>
</div>
<script>
    function uploaded() {
        var open = $('#open').val();
        var form = $("#uploadForm");
        var url = "${image_upload_url}?open=" + open;
        form.attr('action', url);
        form.submit();
    }
</script>
<script>
</script>
</body>
</html>
