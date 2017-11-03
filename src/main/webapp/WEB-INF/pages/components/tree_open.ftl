<!DOCTYPE html>

<html class="not-ie" lang="en">
<head>
    <meta charset="utf-8"/>
    <title>树</title>
<#include "/WEB-INF/pages/base/css.ftl">
    <!-- 树 -->
    <link rel="stylesheet" type="text/css" href="/admins/assets/plugins/fuelux/css/tree-metronic.css"/>
<#include "/WEB-INF/pages/base/js.ftl">
<#import "/WEB-INF/pages/base/base.ftl"  as b>
</head>
<body class="page-header-fixed" onselectstart="return false;">
<div class="page-content extended ">
    <div id="scroller_id" class="scroller" style="height:400px;width:500px;" data-always-visible="1"
         data-rail-visible="0">
        <div id="tree_id" class="tree tree-solid-line">
            <div class="tree-folder" style="display:none;">
                <div class="tree-folder-header">
                    <span class="text-success"><i class="tree-dot"></i>&nbsp;</span><span class="text-primary"><i
                        class="fa fa-folder"></i></span>

                    <div class="tree-folder-name"></div>
                </div>
                <div class="tree-folder-content"></div>
                <div class="tree-loader" style="display:none"></div>
            </div>
            <div class="tree-item" style="display:none;">
                <span class="text-success"><i class="tree-dot"></i></span>

                <div class="tree-item-name"></div>
            </div>
        </div>
    </div>
    <!--div class="scroller" -->
</div>
<!--div class="page-content extended "-->

<script src="/admins/assets/plugins/fuelux/js/tree.js"></script>
<script src="/admins/js/Tree.js"></script>
<script>
    jQuery(document).ready(function () {
        var dialog = top.dialog.get(window);   //根据获取打开的对话框实例
        //根据window获取iframe的窗口名称，再top窗口中查询所有的dialog中的iframe，比对获取对应的dialog
        var params = dialog.data; // 获取对话框传递过来的数据
        UITree(params);
    });

    var returnSelectData = function () {
        var data_ = $('#tree_id').tree('selectedItems')[0];
        return data_;
    }

    var UITree = function (params) {
        $.ajax(params.url, {
            type: "POST",
            dataType: "json"
        }).always(function () {
        }).done(function (data) {
            if (params.defValue != '') {
                var defData = data.t.dataMapping[params.defValue];
                params.tree_name.val(defData);
                params.tree_id.val(params.defValue);
            }

            var data_tmp;
            if (params.isShowRoot == 'true') {
                data_tmp = [data.t];
            } else {
                data_tmp = data.t.nodeList;
            }
            var treeDataSource = Tree.DataSourceTree({data: data_tmp, delay: 200});
            $('#tree_id').tree({
                dataSource: treeDataSource,
                loadingHTML: "<img src='/assets/img/input-spinner.gif'/>",
                multiSelect: false,
                selectable: true,
                folderselect: params.folderSelect
            });
        }).fail(function () {
        });
    }
</script>
</body>
</html>