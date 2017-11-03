<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <title>请求错误</title>
<#include "/WEB-INF/pages/base/css.ftl">
</head>
<body>
<br/>

<div class="page-content extended ">
    <div class="row">
        <div class="col-xs-8">
            <div class="note note-danger">
                <h4 class="block">请求错误</h4>

                <p>
                <#if error_msg?? && error_msg?length  &gt; 0>${errror_msg},</#if>请联系管理员进行处理!
                </p>
            </div>
        </div>
    </div>
</div>
</body>
</html>
