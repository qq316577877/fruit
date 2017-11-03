<script src="/admins/assets/plugins/jquery-1.10.2.js" type="text/javascript"></script>
<script src="/admins/assets/plugins/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>
<script type="text/javascript" src="/admins/assets/plugins/jquery-ui-1.10.4/jquery-ui-1.10.4.min.js"></script>

<script src="/admins/assets/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<script src="/admins/assets/plugins/bootstrap-hover-dropdown/twitter-bootstrap-hover-dropdown.min.js"
        type="text/javascript"></script>

<!--去除滚动条特效
<script src="/admins/assets/plugins/jquery-slimscroll/jquery.slimscroll.js" type="text/javascript"></script>
-->

<script src="/admins/assets/plugins/jquery.blockui.min.js" type="text/javascript"></script>
<script src="/admins/assets/plugins/jquery.cookie.min.js" type="text/javascript"></script>
<script src="/admins/assets/plugins/uniform/jquery.uniform.min.js" type="text/javascript"></script>
<script src="/admins/assets/plugins/jquery.form.min.js"></script>
<!-- 核心js ed-->

<!-- 页面组件js st-->

<!-- 下拉框 -->
<script type="text/javascript" src="/admins/assets/plugins/silviomoreto-bootstrap-select/js/bootstrap-select.js"></script>
<script type="text/javascript"
        src="/admins/assets/plugins/silviomoreto-bootstrap-select/js/i18n/defaults-zh_CN.min.js"></script>

<!-- 提示框-->
<script src="/admins/assets/plugins/bootbox/bootbox.min.js" type="text/javascript"></script>
<!-- 文本域的字数提示 -->
<script src="/admins/assets/plugins/bootstrap-maxlength/bootstrap-maxlength.min.js" type="text/javascript"></script>

<!-- 弹框-->
<script src="/admins/assets/plugins/artDialog-master/dialog-plus.js" type="text/javascript"></script>

<!-- tag标签
<script src="/admins/assets/plugins/bootstrap-tagsinput-master/bootstrap-tagsinput.js" type="text/javascript" ></script>
<script src="/admins/assets/plugins/bootstrap-tagsinput-master/bootstrap-tagsinput-angular.js" type="text/javascript" ></script>
 -->

<!-- 表格
<script type="text/javascript" src="/admins/assets/plugins/data-tables/jquery.dataTables.min.js"></script>
-->
<!--表格添加水平滚动，内容不会换行
<script type="text/javascript" src="/admins/assets/plugins/data-tables/DT_bootstrap.js"></script>
-->

<!-- 树
<script src="/admins/assets/plugins/fuelux/js/tree.js"></script>
-->

<!-- 日期 -->
<script src="/admins/assets/plugins/bootstrap-datetimepicker-master/js/moment-with-locales.js" type="text/javascript"></script>
<script src="/admins/assets/plugins/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"
        type="text/javascript"></script>


<!-- 表单验证-->
<script type="text/javascript" src="/admins/assets/plugins/jquery-validation/dist/jquery.validate.js"></script>
<script type="text/javascript" src="/admins/assets/plugins/jquery-validation/dist/additional-methods.min.js"></script>
<script type="text/javascript" src="/admins/assets/plugins/jquery-validation/dist/message_cn.js"></script>
<script type="text/javascript" src="/admins/assets/plugins/jquery-validation/dist/jquery.metadata.js"></script>
<script type="text/javascript" src="/admins/assets/util/validateExpand.js"></script>

<!-- 上传控件
<script type="text/javascript" charset="utf-8" src="/admins/assets/plugins/upload-custom/upload.js"></script>
 -->

<!--  页面组件js ed-->

<script src="/admins/assets/plugins/jquery-toast-plugin-master/jquery.toast.min.js" type="text/javascript"></script>


<!-- 自定义js st -->
<script src="/admins/js/app.js" type="text/javascript"></script>
<script src="/admins/js/functions.js" type="text/javascript"></script>
<script src="/admins/js/validate.js" type="text/javascript"></script>

<!-- 自定义js ed -->

<script type="text/javascript">

    //组件公共设置
    jQuery(document).ready(function () {
        App.init(); //公共组件的初始化
        //弹框和提示框设置
        if (top.dialog) {
            dialog = top.dialog;
        }
        if (top.bootbox) {
            bootbox = top.bootbox;
        }
        //提示框
        bootbox.setDefaults({
            locale: "zh_CN"
        });
    });

</script>
	