<#--分页-->
<#macro pageSimple pageVoData pageId='defId'>


    <#if (pageVoData.currentPageNo >0)>

    <div class="row">
        <div class="col-xs-12">
            <div class="dataTables_paginate paging_bootstrap pull-left">
                <ul class="pagination" style="visibility: visible;">
                    <#if (pageVoData.currentPageNo>1)>
                        <li><a href="#" title="上一页" onclick="${pageId}_topage('${pageVoData.currentPageNo?int -1 }')">上一页</a>
                        </li>
                    </#if>

                    <li class="active"><a href="#">${pageVoData.currentPageNo}</a></li>


                    <#if pageVoData.result?? && pageVoData.result?size  &gt; 0>
                        <li><a href="#" title="下一页" onclick="${pageId}_topage('${pageVoData.currentPageNo?int+1 }')">下一页</a>
                        </li>
                    </#if>

                </ul>
            </div>
        </div>
    </div>

    <script>

        function ${pageId}_topage(pageNo) {
            var form = $("#thisForm");
            $('#pageNo').val(pageNo);
            form.attr('action', form.attr('action'));
            form.submit();
        }
    </script>
    </#if>
</#macro>