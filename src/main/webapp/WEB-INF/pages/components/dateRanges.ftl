<#--时间域-->
<#macro dateRanges st_id ed_id st_def='' ed_def='' required='false'>
<div class="input-group" style="width:320px">
    <input type='text' class="form-control" style="width:150px;" id='${st_id}' name='${st_id}' value='${st_def!}'
           validate="{required:${required}}" data-date-format="YYYY-MM-DD"/>
    <span class="input-group-addon">到</span>
    <input type='text' class="form-control" style="width:150px;" id='${ed_id}' name='${ed_id}' value='${ed_def!}'
           validate="{required:${required}}" data-date-format="YYYY-MM-DD"/>
</div>
<script>
    jQuery(document).ready(function () {
        var op = {
            pickDate: true,
            pickTime: false,
            useSeconds: false,
            useCurrent: false,
            language: 'zh-cn',
            icons: {
                time: "fa fa-clock-o",
                date: "fa fa-calendar",
                up: "fa fa-arrow-up",
                down: "fa fa-arrow-down"
            }
        }

        $('#${st_id}').datetimepicker(op);
        $('#${ed_id}').datetimepicker(op);

        if ('${ed_def!}' != '') {
            $('#${st_id}').data("DateTimePicker").setMaxDate('${ed_def!}');
        }
        if ('${st_def!}' != '') {
            $('#${ed_id}').data("DateTimePicker").setMinDate('${st_def!}');
        }

        $("#${st_id}").on("dp.change", function (e) {
            $('#${ed_id}').data("DateTimePicker").setMinDate(e.date);
        });
        $("#${ed_id}").on("dp.change", function (e) {
            $('#${st_id}').data("DateTimePicker").setMaxDate(e.date);
        });
    });
</script>
</#macro>