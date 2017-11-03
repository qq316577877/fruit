<#--上传-->
<#--
upload_id: 组件ID
dataId: 业务关联ID
typePath: 上传服务器路径
isImgOnly: 是否仅能上传图片
imgWidth: 预览图片宽度
imgHeight: 预览图片高度
maxNum: 上传文件的上限
-->
<#macro upload upload_id="" isImgOnly='false' url=''  onAllComplete='' isDeleteOnComplete='false'
imgWidth='140' imgHeight='140' maxNum='10' required='false' >

<input id="files_choose_${upload_id}" type="button" class="btn btn-shadow green" value="选择文件"/>
<input id="files_upload_${upload_id}" type="button" class="btn default" value="上传"/>
<input type="hidden" id="uploaded_file_${upload_id}" value="" validate="{required:${required}}">
<div id="fileList_${upload_id}"></div>


<script>
    jQuery(document).ready(function () {
        var fileSuffixs_ = [];
        <#if isImgOnly=='true' >
            fileSuffixs_ = ["jpg", "png", "gif", "jpeg", "bmp"];
        </#if>
        var btn = $("#files_choose_${upload_id}").uploadFile({
            url: "${url!}",
        <#--url: "${BASEPATH}/attachment/upload.json?dataId=${dataId!}&typePath=${typePath}",-->
        <#--url_delete: "${BASEPATH}/attachment/delete.json",-->
        <#--url_list: "${BASEPATH}/attachment/list.json?dataId=${dataId!}",-->
        <#--url_img: "${BASEPATH}/attachment/download.json",-->
        <#--url_download: "${BASEPATH}/attachment/download.json",-->
            isDeleteOnComplete:${isDeleteOnComplete!false},
            fileSuffixs: fileSuffixs_,
            readOnly: "files_upload_${upload_id}",
            maximumFilesUpload: ${maxNum},//最大文件上传数
            onComplete: function (msg) {
                //$("#testdiv").append(msg + "<br/>");
            },
            onCheckUpload: function (text) { //上传时检查文件后缀名不包含在fileSuffixs属性中时触发的回调函数，(text为错误提示文本)
                bootbox.alert(text);
            },
            onAllComplete: function (res) {

                if (res.result) {
                    bootbox.alert("全部上传完成!");
                } else {
                    bootbox.alert(res.msg);
                }

                if ('${onAllComplete}' != '') {
                    ${onAllComplete}(res);
                }

            },
            isGetFileSize: false,//是否获取上传文件大小，设置此项为true时，将在onChosen回调中返回文件fileSize和获取大小时的错误提示文本errorText
            uploaded_file_flag: "uploaded_file_${upload_id}", //必填验证
            perviewElementId: "fileList_${upload_id}", //设置预览图片的元素id
            perviewImgStyle: {width: '${imgWidth}px', height: '${imgHeight}px', border: '1px solid #ebebeb'}//设置预览图片的样式
        });

        var upload = btn.data("uploadFileData");

        $("#files_upload_${upload_id}").click(function () {
            upload.submitUpload();
        });

    });
</script>
</#macro>