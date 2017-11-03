var Index = function () {
    var click_event = $.fn.tap ? "tap" : "click"

    var sidebar_collapsed = function (isMenu_min) {
        isMenu_min = isMenu_min || false;
        var sidebar = $("#sidebar");
        var collapse = $("#sidebar-collapse i[class*='fa-']")
        var icon1 = collapse.attr("data-icon1");
        var icon2 = collapse.attr("data-icon2");

        var scroll = $("#menu_scroller");
        if (isMenu_min) {
            sidebar.addClass("menu-min");
            collapse.removeClass(icon1);
            collapse.addClass(icon2);
            scroll.attr("style", "");
            scroll.attr("class", "");
        } else {
            sidebar.removeClass("menu-min");
            collapse.removeClass(icon2);
            collapse.addClass(icon1);

            sidebar.find(".open ul.submenu").slideUp();
            sidebar.find("li.open").removeClass("open");

            sidebar.find("li.leaf-menu.active").each(function () {
                $(this).parents("ul.submenu").slideDown();
                $(this).parents("li").addClass("open");
            })
            scroll.css('height', ($(window).height() - 100) + "px");
            scroll.css('overflow-y', 'auto');
            scroll.css('overflow-x', 'hidden');
        }
    };

    var handle_side_menu = function () {
        $("#menu-toggler").on(click_event,
            function () {
                $("#sidebar").toggleClass("display");
                $(this).toggleClass("display");
                return false
            });

        var isMenu_min = $("#sidebar").hasClass("menu-min");
        $("#sidebar-collapse").on(click_event,
            function () {
                isMenu_min = $("#sidebar").hasClass("menu-min");
                sidebar_collapsed(!isMenu_min)
            });

        $(".nav-list").on(click_event,
            function (h) {
                var menu_a_find = $(h.target).closest("a");
                if (!menu_a_find || menu_a_find.length == 0) {
                    return
                }

                if (menu_a_find.parent().hasClass("leaf-menu")) {
                    $('#sidebar li.leaf-menu').removeClass('active');
                    menu_a_find.parent().addClass("active");
                    return
                }

                isMenu_min = $("#sidebar").hasClass("menu-min");

                var menu_a = menu_a_find.next().get(0);
                if (!$(menu_a).is(":visible")) {

                    //菜单a标签所在li的上层ul
                    var parent_ul = $(menu_a.parentNode).closest("ul");

                    //隐藏菜单 一级菜单
                    if (isMenu_min && parent_ul.hasClass("nav-list")) {
                        return
                    }
                    //同级的其他目录 收起列表
                    parent_ul.find("> .open > .submenu").each(function () {
                        if (this != menu_a && !$(this.parentNode).hasClass("active")) {
                            $(this).slideUp(200).parent().removeClass("open")
                        }
                    })
                } else {
                }

                //隐藏菜单 一级目录
                if (isMenu_min && $(menu_a.parentNode.parentNode).hasClass("nav-list")) {
                    return false
                }
                $(menu_a).slideToggle(200).parent().toggleClass("open");
                return false
            });
    };
    return {
        init: function () {
            handle_side_menu();
        }
    };

}();