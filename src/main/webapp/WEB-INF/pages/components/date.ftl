<#--时间：支持多种格式-->
<#macro date date_id defValue='' formart='YYYY-MM-DD' required='false'>

<input type='text' class="form-control" id='${date_id}' name='${date_id}' value='${defValue!}'
       data-date-format="${formart}" validate="{required:${required}}"/>

<script>
    jQuery(document).ready(function () {
        var op = {
            pickDate: false,
            pickTime: false,
            pickYear: false,
            pickYM: false,
            useMinutes: false,
            useSeconds: false,
            useCurrent: true,
            sideBySide: false,
            language: 'zh-cn',
            icons: {
                time: "fa fa-clock-o",
                date: "fa fa-calendar",
                up: "fa fa-arrow-up",
                down: "fa fa-arrow-down"
            }
        }

        var ff = '${formart}'
        if (ff.indexOf('ss') != -1) {  //HH:mm:ss / YYYY-MM-DD HH:mm:ss
            op.pickTime = true;
            op.useSeconds = true;
            op.useMinutes = true;
        }
        else if (ff.indexOf('mm') != -1) {  // HH:mm / YYYY-MM-DD HH:mm
            op.pickTime = true;
            op.useMinutes = true;
        }
        else if (ff.indexOf('HH') != -1) {  // HH / YYYY-MM-DD HH
            op.pickTime = true;
        }
        if (ff.indexOf('DD') != -1) {  //YYYY-MM-DD
            op.pickDate = true;
        }
        else if (ff.indexOf('MM') != -1) {  //YYYY-MM
            op.pickYM = true;
        }
        else if (ff.indexOf('YYYY') != -1) {  //YYYY
            op.pickYear = true;
        }
        $('#${date_id}').datetimepicker(op);

    });
</script>
</#macro>