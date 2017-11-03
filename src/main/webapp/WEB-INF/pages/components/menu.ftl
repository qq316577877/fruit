<#macro menu  list >
<div class="scroller" id="menu_scroller" style="height:400px;" data-always-visible="1" data-rail-visible="0">
    <ul class="nav nav-list">
        <li class="active leaf-menu">
            <a href="javascript:void(0)" onclick="menuClick('main','首页','0');return false;">
                <i class="fa fa-home"></i>
                <span class="menu-text"> 首页 </span>
            </a>
        </li>

        <#if list??>
            <#list list as f>
                <#if !f.nodeList?exists || f.nodeList?size == 0>
                    <!-- 一级菜单 -->
                    <li class="leaf-menu">
                        <a href="javascript:void(0)"
                           onclick="menuClick('${f.data.url!}','${f.lineal!}','${f.data.type!}');return false;">
                            <i class="fa fa-star-o"></i>
                            <span class="menu-text">${f.data.name!}</span>
                        </a>
                    </li>

                <#else>
                    <!-- 一级目录 -->
                    <li class="fold-menu">
                        <a href="#" class="dropdown-toggle">
                            <i class="fa fa-star-o"></i>
                            <span class="menu-text">${f.data.name!} </span>
                            <b class="arrow fa fa-angle-down"></b>
                        </a>
                        <ul class="submenu">
                            <#list f.nodeList as ff>
                                <#if !ff.nodeList?exists || ff.nodeList?size == 0>
                                    <!-- 二级菜单 -->
                                    <li class="leaf-menu">
                                        <a href="javascript:void(0)"
                                           onclick="menuClick('${ff.data.url!}','${ff.lineal!}','${ff.data.type!}');return false;">
                                            <i class="fa fa-angle-double-right"></i>
                                        ${ff.data.name!}
                                        </a>
                                    </li>
                                <#else>
                                    <!-- 二级目录 -->
                                    <li class="fold-menu">
                                        <a href="#" class="dropdown-toggle">
                                            <i class="fa fa-angle-double-right"></i>
                                        ${ff.data.name!}
                                            <b class="arrow fa fa-angle-down"></b>
                                        </a>
                                        <ul class="submenu">
                                            <#list ff.nodeList as fff>
                                                <!-- 三级菜单 -->
                                                <li class="leaf-menu">
                                                    <a href="javascript:void(0)"
                                                       onclick="menuClick('${fff.data.url!}','${fff.lineal!}','${fff.data.type!}');return false;">
                                                        <i class="fa fa-asterisk"></i>
                                                    ${fff.data.name!}
                                                    </a>
                                                </li>
                                            </#list>
                                        </ul>
                                    </li>
                                </#if><#-- <#if !ff.nodeList?exists || ff.nodeList?size == 0>-->
                            </#list> <#-- <#list f.nodeList as ff>  -->
                        </ul>
                    </li>
                </#if>
            </#list>
        </#if>
    </ul>
    <!-- /.nav-list -->
</div>
<div class="sidebar-collapse" id="sidebar-collapse">
    <i class="fa fa-angle-double-left" data-icon1="fa fa-angle-double-left" data-icon2="fa fa-angle-double-right"></i>
</div>


<script>
    jQuery(document).ready(function () {
        $("#menu_scroller").css("height", $(window).height() - 100);
    });

    function menuClick(src, linealStr, type) {

        var url;
        if (type == '0') {
            url = src ? '${BASEPATH}/' + src : src;
        }
        else {
            url = src;
        }

        $('#content').attr('src', url);

        var str = '';
        var lineal = linealStr.split(',');
        for (var i = 0; i < lineal.length; i++) {
            if (lineal[i] == '') {
                break;
            }
            var firstIcon = i == 0 ? '<i class=\'fa fa-globe home-icon\'></i>' : '';
            str += '<li> ' + firstIcon + ' <a href=\'#\' class="btn">' + lineal[i] + '</a>';
            str += i == lineal.length - 1 ? '</li>' : '</li>';
        }
        $('#page_breadcrumb').html(str);
    }
</script>
</#macro>