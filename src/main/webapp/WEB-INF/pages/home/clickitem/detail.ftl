<!DOCTYPE html>

<html class="not-ie" lang="en">
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title>新闻资讯</title>
<#include "/WEB-INF/pages/base/css.ftl">
<#include "/WEB-INF/pages/base/js.ftl">
<#import "/WEB-INF/pages/base/base.ftl"  as b>
<#--<link href="/admins/assets/plugins/webuploader-0.1.5/webuploader.css" rel="stylesheet" type="text/css"/>-->
</head>
<style>
    input.webuploader-element-invisible {
        position: absolute;
        bottom: -27px;
    }

</style>
<body>
<div class="page-content extended">
    <div class="scroller" data-height="auto">
        <div class="row">
            <div class="col-xs-12">
                <form id="thisForm" name="thisForm"  accept-charset="UTF-8"  action="${BASEPATH}/home/clickitem/save_ajax"
                      class="form-horizontal" method="post">
                    <input type="hidden" name="id" id="id" value="${(data.id)!0}"/>
                    <!-- 隐藏域 ed -->
                    <div class="form-body">
                        <div class="row">
                            <div class="col-xs-6">
                                <div class="form-group">
                                    <label class="control-label col-xs-5">标题<span class="required">*</span></label>

                                    <div class="col-xs-7">
                                        <div class="input-group">
                                            <input type='text' class="form-control input-xlarge"
                                                   validate="{required:true}" id='title'
                                                   name='title' maxlength="32" value='<#if data??>${data.title}</#if>'/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>


                        <div class="row">
                            <div class="col-xs-6">
                                <div class="form-group">
                                    <label class="control-label col-xs-5">子标题<span class="required">*</span></label>

                                    <div class="col-xs-7">
                                        <div class="input-group">
                                            <textarea id="subtitle" name="subtitle" rows="3" cols="42"
                                                      placeholder="此处填写公司新闻或行业资讯子标题"
                                                      maxlength="120" validate="{required:true}"
                                            ><#if data??>${data.subtitle}</#if></textarea>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-xs-6">
                                <div class="form-group">
                                    <label class="control-label col-xs-5">链接<span class="required">*</span></label>

                                    <div class="col-xs-7">
                                        <div class="input-group">
                                            <input type='text' class="form-control  input-xlarge"
                                                   validate="{required:true}" id='link'
                                                   name='link' value='<#if data??>${data.link}</#if>'/>
                                        </div>
                                    </div>
                                </div>
                                <!--div class="form-group"-->
                            </div>
                            <!-- div class="col-xs-12" -->
                            <!-- div class="col-xs-12" -->
                        </div>


                        <div class="row">
                            <div class="col-xs-6">
                                <div class="form-group">
                                    <label class="control-label col-xs-5">时间<span class="required">*</span></label>

                                    <div class="col-xs-7">
                                        <div class="input-group">
                                            <input type='text' class="form-control input-xlarge"
                                                   validate="{required:true}" id='time'
                                                   name='time'
                                                   value='<#if data??>${(data.time?string("yyyy-MM-dd"))!}</#if>'/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>


                        <!-- div class="row" -->
                        <div class="row" style="height: 190px">
                            <div class="col-xs-6">
                                <div class="form-group">
                                    <label class="control-label col-xs-5">图片<span class="required">*</span></label>

                                    <div class="col-xs-7">
                                        <div class="input-group">
                                            <div class="big-photo">
                                                <div id="preview">
                                                    <div id="imgPicker">
                                                        <img validate="{required:true}" height="150" width="300"
                                                             id="showImg" style="border: 1px solid #666" name='showImg'
                                                             src='<#if data??>${data.imgUrl}</#if>'/>

                                                        <input type="hidden" name="imgUrl" id="imgUrl"
                                                               value='<#if data??>${data.imgUrlPath}</#if>'/>
                                                    </div>
                                                </div>
                                            </div>

                                        </div>
                                    </div>
                                </div>
                                <!--div class="form-group"-->
                            </div>
                            <!-- div class="col-xs-12" -->
                        </div>

                        <div class="row">
                            <div class="col-xs-6">
                                <div class="form-group">
                                    <label class="control-label col-xs-5">类型<span class="required">*</span></label>
                                    <div class="col-xs-7">
                                        <select id="type" name="type" class="form-control input-medium select2me">
                                        <#list positionItems as item>
                                            <option value="${item.id}"
                                                    <#if item.selected == 1>selected="selected"</#if>>${item.value}</option>
                                        </#list>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>


                        <div class="row">
                            <div class="col-xs-6">
                                <div class="form-group">
                                    <label class="control-label col-xs-5">排序<span class="required">*</span></label>

                                    <div class="col-xs-7">
                                        <div class="input-group">
                                            <input type='number' class="form-control input-xlarge"
                                                   validate="{required:true}" id='priority'
                                                   name='priority' value='<#if data??>${data.priority!0}</#if>'/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- div class="col-xs-12" -->
                        </div>
                        <!-- div class="row" -->
                        <!-- div class="row" -->
                    </div>
                    <!--div class="form-body"-->
                    <div class="form-actions fluid">
                        <div class="col-xs-offset-5 col-xs-7">
                            <button type="button" id="savebtn" class="btn btn-shadow green J_BTN"><i
                                    class="fa fa-save"></i> 保 存
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <!--div class="row"-->

    </div>
    <!--div class="scroller" style="height:630px" -->
</div>
<!--div class="page-content extended"-->


<!-- 页面JS类库引入 st-->
<!-- 表单验证 -->
<script type="text/javascript" src="/admins/assets/plugins/jquery-validation/dist/jquery.validate.js"></script>
<script type="text/javascript" src="/admins/assets/plugins/jquery-validation/dist/additional-methods.min.js"></script>
<script type="text/javascript" src="/admins/assets/plugins/jquery-validation/dist/message_cn.js"></script>
<script type="text/javascript" src="/admins/assets/plugins/jquery-validation/dist/jquery.metadata.js"></script>
<script src="/admins/assets/plugins/webuploader-0.1.5/webuploader.js"></script>
<script src="/admins/assets/plugins/webuploader-0.1.5/uploadInit.js"></script>
<!-- 页面所需组件的js文件 -->
<!-- 页面JS类库引入 ed-->
<!-- 私有JS st -->
<script src="/admins/assets/plugins/moment/moment.min.js"></script>
<script src="/admins/assets/plugins/date/laydate.dev.js"></script>
<script src="/admins/js/form.js" type="text/javascript"></script>
<!-- 私有JS ed -->
<script>

    jQuery(document).ready(function () {
        var v_ = formValidation('thisForm');
        var saveUrl = "${BASEPATH}/home/clickitem/save_ajax";
        var options = formSaveOptions(saveUrl, null, null, null, 'id');

        window.onload=function () {
            $('#imgPicker>div:nth-of-type(2)').css('overflow','visible');
        }




        $('#savebtn').click(function () {
            if (!v_.form()) {
                return;
            }
            var form = $("#thisForm");
            form.ajaxSubmit(options);
        });

        initUpload();
        initDate();
    });


    function initUpload() {
        // 初始化Web Uploader

        var uploader = WebUploader.create({

            // 选完文件后，是否自动上传。
            auto: true,

            // swf文件路径
            swf: './Uploader.swf',

            // 文件接收服务端。
            server: '/admin/file/upload_private_ajax',

            // 选择文件的按钮。可选。
            // 内部根据当前运行是创建，可能是input元素，也可能是flash.
            pick: '#imgPicker ',

            // 只允许选择图片文件。
            accept: {
                title: 'Images',
                extensions: 'gif,jpg,jpeg,bmp,png',
                mimeTypes: 'image/png,image/jpg,image/jpeg,imge/bmp,image/gif'
            }
        });

        // 文件上传成功，给item添加成功class, 用样式标记上传成功。
        uploader.on('uploadSuccess', function (file, res) {
            $("#showImg").attr("src", res.datas.url);
            $("#imgUrl").val(res.datas.path);

            //todo 去掉未上传的css
//            $("#imgPicker").parent().next().css("display","none");
        });

        // 文件上传失败，显示上传出错。
        uploader.on('uploadError', function (file) {
            var $li = $('#' + file.id),
                    $error = $li.find('div.error');

            // 避免重复创建
            if (!$error.length) {
                $error = $('<div class="error"></div>').appendTo($li);
            }

            $error.text('上传失败');
        });

        // 完成上传完了，成功或者失败，先删除进度条。
        uploader.on('uploadComplete', function (file) {
            $('#' + file.id).find('.progress').remove();
        });

    }


    function initDate() {
        laydate({
            elem: '#time'
        });

        var time = $("#time").val();
        if (!time || time == "") {
            var sFM = 'YYYY-MM-DD';
            var nowTime = moment().format(sFM);
            $("#time").val(nowTime);
        }

    }
</script>
</body>
</html>