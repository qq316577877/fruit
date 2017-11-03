<!DOCTYPE html>

<html class="not-ie" lang="en">
<head>
    <meta charset="utf-8"/>
    <title>角色</title>
<#include "/WEB-INF/pages/base/css.ftl">
    <link rel="stylesheet" type="text/css" href="/admins/assets/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css"/>

<#include "/WEB-INF/pages/base/js.ftl">
<#import "/WEB-INF/pages/base/base.ftl"  as b>
</head>
<body>
<div class="page-content opener">
    <div class="scroller" style="height:350px;width:500px;" data-always-visible="1" data-rail-visible="0">
        <ul id="tree_id" class="ztree"></ul>
    </div>
    <!--div class="scroller" style="height:630px" -->

    <div class="form-actions fluid">
        <div class="col-xs-offset-5 col-xs-7">
            <button type="button" id="savebtn" class="btn btn-shadow green"><i class="fa fa-save"></i> 保 存</button>
        </div>
    </div>
</div>
<!--div class="page-content extended"-->


<!-- 页面JS类库引入 st-->
<script src="/admins/assets/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
<!-- 页面JS类库引入 ed-->
<!-- 私有JS st -->

<script src="/admins/js/form.js" type="text/javascript"></script>
<!-- 私有JS ed -->
<script>
    jQuery(document).ready(function () {
        var setting = {
            check: {enable: true},
            data: {
                key: {children: "nodeList"}
            }

        };

        $.ajax("${BASEPATH}/role/menuTree", {
            data: {roleId: '${roleId!}'},
            type: "POST",
            dataType: "json"
        }).always(function () {
        }).done(function (data) {
            $.fn.zTree.init($("#tree_id"), setting, data.t.nodeList);
        }).fail(function () {
        });

        var saveUrl = "${BASEPATH}/role/saveMenu";

        $('#savebtn').click(function () {
            var treeObj = $.fn.zTree.getZTreeObj("tree_id");
            var nodes = treeObj.getCheckedNodes(true);
            var node_ids = [];
            for (var i = 0; i < nodes.length; i++) {
                node_ids.push(nodes[i].id);
            }

            $.ajax(saveUrl, {
                data: {
                    menuIds: node_ids,
                    roleId: '${roleId!}'
                },
                type: "POST",
                dataType: "json"
            }).always(function () {
            }).done(function (data) {
                if (data.result) {
                    bootbox.alert("保存成功!");
                } else {
                    bootbox.alert(data.msg);
                }
            }).fail(function () {
            });

        });
    });
</script>
</body>
</html>