/**
     Core script to handle the entire theme and core functions
 **/
var App = function () {

    // IE mode
    var isIE8 = false;
    var isIE9 = false;
    var isIE10 = false;

    var handleInit = function () {
        isIE8 = !!navigator.userAgent.match(/MSIE 8.0/);
        isIE9 = !!navigator.userAgent.match(/MSIE 9.0/);
        isIE10 = !!navigator.userAgent.match(/MSIE 10.0/);

        if (isIE10) {
            jQuery('html').addClass('ie10'); // detect IE10 version
        }

        if (isIE10 || isIE9 || isIE8) {
            jQuery('html').addClass('ie'); // detect IE10 version
        }
    }

    // Handles portlet tools & actions
    var handlePortletTools = function () {
        jQuery('body').on('click', '.portlet > .portlet-title > .tools > a.remove', function (e) {
            e.preventDefault();
            jQuery(this).closest(".portlet").remove();
        });

        jQuery('body').on('click', '.portlet > .portlet-title > .tools > a.reload', function (e) {
            e.preventDefault();
            var el = jQuery(this).closest(".portlet").children(".portlet-body");
            App.blockUI(el);
            window.setTimeout(function () {
                App.unblockUI(el);
            }, 1000);
        });

        jQuery('body').on('click', '.portlet > .portlet-title > .tools > .collapse, .portlet .portlet-title > .tools > .expand', function (e) {
            e.preventDefault();
            var el = jQuery(this).closest(".portlet").children(".portlet-body");
            if (jQuery(this).hasClass("collapse")) {
                jQuery(this).removeClass("collapse").addClass("expand");
                el.slideUp(200);
            } else {
                jQuery(this).removeClass("expand").addClass("collapse");
                el.slideDown(200);
            }
        });
    }

    // Handles custom checkboxes & radios using jQuery Uniform plugin
    var handleUniform = function () {
        if (!jQuery().uniform) {
            return;
        }
        var test = $("input[type=checkbox]:not(.toggle), input[type=radio]:not(.toggle, .star)");
        if (test.size() > 0) {
            test.each(function () {
                if ($(this).parents(".checker").size() == 0) {
                    $(this).show();
                    $(this).uniform();
                }
            });
        }
    }

    // Handles Bootstrap Dropdowns
    var handleDropdowns = function () {
        /*
         Hold dropdown on click
         */
        $('body').on('click', '.dropdown-menu.hold-on-click', function (e) {
            e.stopPropagation();
        });
    }

    // Handle Hower Dropdowns
    var handleDropdownHover = function () {
        $('[data-hover="dropdown"]').dropdownHover();
    }

    // Handles scrollable contents using jQuery SlimScroll plugin.
    var handleScrollers = function () {
        $('.scroller').each(function () {
            var height;
            if ($(this).attr("data-height")) {
                height = $(this).attr("data-height");
            } else {
                height = $(this).css('height');
            }

            height = height == 'auto' ? ($(window).height()) : height;

            $(this).css('height', height);
            $(this).css('overflow-y', 'auto');
            $(this).css('overflow-x', 'hidden');
            /*
             去除滚动条特效
             $(this).slimScroll({
             size: '7px',
             color: ($(this).attr("data-handle-color")  ? $(this).attr("data-handle-color") : '#a1b2bd'),
             railColor: ($(this).attr("data-rail-color")  ? $(this).attr("data-rail-color") : '#333'),
             position: 'right',
             height: height,
             alwaysVisible: ($(this).attr("data-always-visible") == "1" ? true : false),
             railVisible: ($(this).attr("data-rail-visible") == "1" ? true : false),
             disableFadeOut: true
             });*/

        });
    };


    //表格高级配置
    var handleAdvTable = function () {

        $('table[table-adv]').each(function () {
            var target = $(this);
            if(target && target.dataTable){
                var oTable = target.dataTable(
                    {
                        "bSort": false,
                        "bPaginate": false,
                        "bFilter": false,
                        "bInfo": false,
                        "bAutoWidth": false, //自适应宽度
                        "oLanguage": {
                            "sEmptyTable": "没有查到匹配的数据！",
                            "sLoadingRecords": "加载中..."
                        }
                        //"aoColumnDefs": [{"sClass": "myword-allwrap", "aTargets": ['_all']}]
                        //"aoColumnDefs": [{"sType": "numeric", "aTargets": ['_all']}]
                        //"aoColumns": [{"sType": "numeric"}],
                        //"aaSorting": [[0, "desc"]]
                    });
                var col_toggler_id = $(this).attr("table-adv");
                $('#' + col_toggler_id + ' input[type="checkbox"]').change(function () {
                    var iCol = parseInt($(this).attr("data-column"));
                    var bVis = oTable.fnSettings().aoColumns[iCol].bVisible;
                    oTable.fnSetColumnVis(iCol, (bVis ? false : true));
                });

                jQuery('#' + $(this).attr("id") + ' .group-checkable').change(function () {
                    var set = jQuery(this).attr("data-set");
                    var checked = jQuery(this).is(":checked");
                    jQuery(set).each(function () {
                        if (checked) {
                            $(this).attr("checked", true);
                        } else {
                            $(this).attr("checked", false);
                        }
                        $(this).parents('tr').toggleClass("active");
                    });
                    jQuery.uniform.update(set);
                });

                jQuery('#' + $(this).attr("id") + ' tbody tr .checkboxes').change(function () {
                    $(this).parents('tr').toggleClass("active");
                });

                var tab_id = $(this).attr("id");
                jQuery('#' + $(this).attr("id") + ' tbody tr .checkRadioes').change(function () {
                    jQuery('#' + tab_id + ' tbody tr.active').toggleClass("active");
                    $(this).parents('tr').toggleClass("active");
                });
            }


        });
    };
    //文本域长度
    var handleTextarea = function () {
        $('textarea[maxlength]').each(function () {
            $(this).maxlength({
                limitReachedClass: "label label-danger",
                alwaysShow: true
            });

        });
    };

    // Fix input placeholder issue for IE8 and IE9
    var handleFixInputPlaceholderForIE = function () {
        //fix html5 placeholder attribute for ie7 & ie8
        if (isIE8 || isIE9) { // ie8 & ie9
            // this is html5 placeholder fix for inputs, inputs with placeholder-no-fix class will be skipped(e.g: we need this for password fields)
            jQuery('input[placeholder]:not(.placeholder-no-fix), textarea[placeholder]:not(.placeholder-no-fix)').each(function () {

                var input = jQuery(this);

                if (input.val() == '' && input.attr("placeholder") != '') {
                    input.addClass("placeholder").val(input.attr('placeholder'));
                }

                input.focus(function () {
                    if (input.val() == input.attr('placeholder')) {
                        input.val('');
                    }
                });

                input.blur(function () {
                    if (input.val() == '' || input.val() == input.attr('placeholder')) {
                        input.val(input.attr('placeholder'));
                    }
                });
            });
        }
    }

    // Handle Select2 Dropdowns
    var handleSelect2 = function () {
        if (jQuery().selectpicker) {
            $('.select2me').selectpicker();
        }
    }

    var submitEffect = function () {
        $('.J_BTN').click(function () {
            $(this).attr('disabled', 'disabled')
        })
    }


    return {

        init: function () {

            //core handlers
            handleInit(); //设置 浏览器版本

            if (!isIE8) {
                handleUniform(); // 自定义 radio & checkboxes 样式，有额外的渲染过程，不要求样式时可以去除
                handleTextarea(); //文本域 字数显示
            }
            handleFixInputPlaceholderForIE(); // fixes/enables html5 placeholder attribute for IE9, IE8

            handleAdvTable(); //表格
            handleSelect2(); //下拉框
            handlePortletTools(); //portlet工具条
            handleDropdowns(); // handle dropdowns

            //最后加载滚动条
            handleScrollers(); // handles slim scrolling contents
            submitEffect();
        },

        //main function to initiate core javascript after ajax complete
        initAjax: function () {
            handleSelect2(); // handle custom Select2 dropdowns
            handleDropdowns(); // handle dropdowns
            handleDropdownHover() // handles dropdown hover      
            if (!isIE8) {
                handleUniform(); // 自定义 radio & checkboxes 样式，有额外的渲染过程，不要求样式时可以去除
            }
        },

        initScrollers: function () {
            handleScrollers(); // handles slim scrolling contents
        },

        // wrapper function to  block element(indicate loading)
        blockUI: function (el, centerY) {
            var el = jQuery(el);
            if (el.height() <= 400) {
                centerY = true;
            }
            el.block({
                message: '<img src="./assets/img/ajax-loading.gif" align="">',
                centerY: centerY != undefined ? centerY : true,
                css: {
                    top: '10%',
                    border: 'none',
                    padding: '2px',
                    backgroundColor: 'none'
                },
                overlayCSS: {
                    backgroundColor: '#000',
                    opacity: 0.05,
                    cursor: 'wait'
                }
            });
        },

        // wrapper function to  un-block element(finish loading)
        unblockUI: function (el, clean) {
            jQuery(el).unblock({
                onUnblock: function () {
                    jQuery(el).css('position', '');
                    jQuery(el).css('zoom', '');
                }
            });
        },

        // initializes uniform elements
        initUniform: function (els) {
            if (els) {
                jQuery(els).each(function () {
                    if ($(this).parents(".checker").size() == 0) {
                        $(this).show();
                        $(this).uniform();
                    }
                });
            } else {
                handleUniform();
            }
        },

        // check IE8 mode
        isIE8: function () {
            return isIE8;
        },

        // check IE9 mode
        isIE9: function () {
            return isIE9;
        },
        revertJBTN: function () {
            $('.J_BTN').attr('disabled', false)
        }


    };

}();

function findElementById(id) {
    var top = window.top;
    if (top) {
        var component = top.document.getElementById(id);
        if (component) {
            return component;
        }
    }
    return window.document.getElementById(id);
}