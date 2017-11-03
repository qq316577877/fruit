<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <title>系统错误</title>
<#include "/WEB-INF/pages/base/css.ftl">
</head>
<body>
<br/>

<div class="page-content extended ">
    <div class="row">
        <div class="col-xs-8">
            <div class="note note-danger">
                <h4 class="block">系统错误</h4>

                <p>
                <#if error_msg?? && error_msg?length  &gt; 0>${errror_msg},</#if>请联系开发进部行处理!
                </p>
            </div>
        </div>
    </div>
</div>
</body>
</html>
