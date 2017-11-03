<#--树形选择，默认绑定字典 -->
<#-- 
	tree_id : 组件ID
	tree_title : 弹框标题
	defValue : 默认值
	folderSelect : 目录是否可选
	required : 是否必须
	isShowRoot : 是否显示根节点
	url : 数据加载的action
-->
<#macro tree tree_title tree_id url defValue=''
folderSelect='false' required='false' isShowRoot='false'
width='550' height='400'>

    <#assign tree_btn=tree_id+"_btn">
    <#assign tree_id=tree_id+"_tree">
    <#assign tree_name=tree_id+"_name">

<input type="text" class="form-control input-xlarge form-control-inline" id="${tree_name}" name="${tree_name}" value=""
       readonly>
<input type="hidden" class="form-control" id="${tree_id}" name="${tree_id}" value="" validate="{required:${required}}">
<a href="#" role="button" class="btn default" id="${tree_btn}"><i class="fa fa-gears"></i></a>

<script>
    jQuery(document).ready(function () {
        var params = {};
        params.url = '${url}';
        params.defValue = '${defValue}';
        params.isShowRoot = '${isShowRoot}';
        params.folderSelect = '${folderSelect}' == 'false' ? false : true;
        params.tree_name = $('#${tree_name}');
        params.tree_id = $('#${tree_id}');

        var d = dialog({
            title: '${tree_title}',
            url: '${BASEPATH}/tree/template',
            iframe_width: ${width},
            iframe_height: ${height},
            data: params, // 给 iframe 的数据
            zIndex: 9996,
            button: [
                {
                    value: '确 定',
                    className: 'btn blue',
                    callback: function () {
                        var data_ = this.iframeNode.contentWindow.returnSelectData();
                        if (data_) {
                            $('#${tree_name}').val(data_.lineal);
                            $('#${tree_id}').val(data_.value);
                        }
                        this.close();
                        return false; //无返回值则销毁窗口
                    }
                },
                {
                    value: '清 空',
                    className: 'btn yellow',
                    callback: function () {
                        $('#${tree_name}').val('');
                        $('#${tree_id}').val('');
                        this.close();
                        return false; //无返回值则销毁窗口
                    }
                }],
            cancelValue: '取 消',
            cancel: function () {
                this.close();
                return false; //无返回值则销毁窗口
            }
        });

        $('#${tree_btn}').on('click', function () {
            d.showModal();
        });


    });

</script>
</#macro>