<#--下拉选择，默认绑定字典-->
<#-- 
	select_id : 组件ID
	dictcode : 字典编码
	defValue : 默认值
	required : 是否必须
	url : 数据加载的action,如果是自定义的树形，则不需要dictcode
	nameField : 显示名称对应的字段名
	valueField : 值对应的字段名
	className: 样式
-->

<#macro select  select_id dictcode='' defValue='' required='false'
url='/dic/list' className='input-small' disabled_code='false'>

<select id="${select_id}" name="${select_id}" class="form-control ${className} select2me"
        validate="{required:${required}}"
        <#if disabled_code?? && disabled_code=="true">
            disabled = "${disabled_code}"
        </#if>
         >
</select>

<script>
    jQuery(document).ready(function () {
        select_${select_id}.init();
    });

    var select_${select_id} = function () {
        return {
            init: function () {
                $.ajax("${BASEPATH}${url}", {
                    data: {
                        key: "${dictcode}"
                    },
                    type: "POST",
                    dataType: "json"
                }).always(function () {
                }).done(function (data) {
                    $("#${select_id}").empty();

                    for (var key in data.t) {
                        $("#${select_id}").append("<option value='" + key + "'>" + data.t[key] + "</option>")
                    }

                    $("#${select_id}").selectpicker('refresh');
                    <#if defValue??>
                        $("#${select_id}").selectpicker("val", '${defValue!}')
                    </#if>
                }).fail(function () {
                });
            }

        };

    }();
</script>
</#macro>