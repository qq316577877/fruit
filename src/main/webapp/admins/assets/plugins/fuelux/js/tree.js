/*
 * Fuel UX Spinner
 * https://github.com/ExactTarget/fuelux
 * Improved by keenthemes for metronic theme
 * Copyright (c) 2012 ExactTarget
 * Licensed under the MIT license.
 */

!function ($) {
    var Tree = function (element, options) {
        this.$element = $(element);
        this.options = $.extend({}, $.fn.tree.defaults, options);

        if (!this.options.onDblclick) {
            this.$element.on('click', '.tree-item', $.proxy(function (ev) {
                this.selectItem(ev.currentTarget);
            }, this));
        }

        if (this.options.onDblclick) {
            this.$element.on('dblclick', '.tree-item', $.proxy(function (ev) {
                this.onDblclick_(ev.currentTarget);
            }, this));
        }

        this.$element.on('click', '.tree-folder-header', $.proxy(function (ev) {
            this.selectFolder(ev.currentTarget);
        }, this));

        if (this.options.onDblclick && this.options.folderselect) {
            this.$element.on('dblclick', '.tree-folder-header', $.proxy(function (ev) {
                this.onDblclick_(ev.currentTarget);
            }, this));
        }

        //this.$element.on('click', '.tree-folder-header', $.proxy( function(ev) { this.selectFolder(ev.currentTarget); }, this));

        /*
         if(this.options.folderselect && this.options.multiSelect){
         this.$element.on('dblclick', '.tree-folder-header', $.proxy( function(ev) { this.selectFolder(ev.currentTarget); }, this));
         }else{
         this.$element.on('click', '.tree-folder-header', $.proxy( function(ev) { this.selectFolder(ev.currentTarget); }, this));
         }*/

        this.render();


        if (this.options.openFirstFolder) {
            var firstFolder = $(this.$element).find('.tree-folder-header:eq(1)');
            if (firstFolder) this.selectFolder(firstFolder);
        }

    };

    Tree.prototype = {
        constructor: Tree,
        timer: null,
        render: function () {
            this.populate(this.$element);
        },

        populate: function ($el) {
            var self = this;
            var $parent = $el.parent();
            var loader = $parent.find('.tree-loader:eq(0)');

            var lvl = "lvl" in $el.data() ? ($el.data().lvl + 1) : 1;

            loader.show();
            this.options.dataSource.data($el.data(), function (items) {
                loader.hide();

                $.each(items.data, function (index, value) {
                    value.lvl = lvl;
                    var $entity;
                    value.type = self.options.customType(value.type);
                    if (value.type === "folder") {
                        $entity = self.$element.find('.tree-folder:eq(0)').clone().show();
                        $entity.find('.tree-folder-name').html(self.options.customHTML(value));
                        $entity.find('.tree-loader').html(self.options.loadingHTML);
                        $entity.find('.tree-folder-header').data(value);
                    } else if (value.type === "item") {
                        $entity = self.$element.find('.tree-item:eq(0)').clone().show();
                        $entity.find('.tree-item-name').html(self.options.customHTML(value));
                        $entity.data(value);
                    }

                    // Decorate $entity with data making the element
                    // easily accessable with libraries like jQuery.
                    //
                    // Values are contained within the object returned
                    // for folders and items as dataAttributes:
                    //
                    // {
                    //     name: "An Item",
                    //     type: 'item',
                    //     dataAttributes = {
                    //         'classes': 'required-item red-text',
                    //         'data-parent': parentId,
                    //         'guid': guid
                    //     }
                    // };

                    var dataAttributes = value.dataAttributes || [];
                    $.each(dataAttributes, function (key, value) {
                        switch (key) {
                            case 'class':
                            case 'classes':
                            case 'className':
                                $entity.addClass(value);
                                break;

                            // id, style, data-*
                            default:
                                $entity.attr(key, value);
                                break;
                        }
                    });

                    if ($el.hasClass('tree-folder-header')) {
                        $parent.find('.tree-folder-content:eq(0)').append($entity);
                    } else {
                        $el.append($entity);
                    }
                });

                // return newly populated folder
                self.$element.trigger('loaded', $parent);
            });
        },
        onDblclick_: function (el) {

            this.selectItem_(el);
            clearTimeout(this.timer);
            this.options.onDblclick($(el).data());
        },
        selectItem: function (el) {
            var this_ = this;
            if (this.options.onDblclick) {
                clearTimeout(this.timer);
                this.timer = setTimeout(function () {
                    this_.selectItem_(el);
                }, 200);
            } else {
                this.selectItem_(el);
            }
        },
        selectItem_: function (el) {
            var $el = $(el);
            var $all = this.$element.find('.tree-selected');
            var data = [];

            if (this.options.multiSelect) {
                $.each($all, function (index, value) {
                    var $val = $(value);
                    if ($val[0] !== $el[0]) {
                        data.push($(value).data());
                    }
                });
            } else if ($all[0] !== $el[0]) {
                if ($all && $all.data()) {
                    $all.removeClass('tree-selected')
                        .find('i.fa-check').removeClass('fa fa-check').addClass('tree-dot');
                }
                data.push($el.data());
            }

            if (this.options.selectable) {
                var eventType = 'selected';
                if ($el.hasClass('tree-selected')) {
                    eventType = 'unselected';
                    $el.removeClass('tree-selected');

                    $el.find('i.fa-check').removeClass('fa fa-check').addClass('tree-dot');

                } else {
                    $el.addClass('tree-selected');
                    $el.find('i.tree-dot').removeClass('tree-dot').addClass('fa fa-check');
                    if (this.options.multiSelect) {
                        data.push($el.data());
                    }
                }
            }

            if (data.length) {
                this.$element.trigger('selected', {info: data});
            }

            // Return new list of selected items, the item
            // clicked, and the type of event:
            $el.trigger('updated', {
                info: data,
                item: $el,
                eventType: eventType
            });
        },
        selectFolder: function (el) {
            var this_ = this;
            if (this.options.onDblclick) {
                clearTimeout(this.timer);
                this.timer = setTimeout(function () {
                    this_.selectFolder_(el);
                }, 200);
            } else {
                this.selectFolder_(el);
            }
        },
        selectFolder_: function (el) {
            if (this.options.folderselect && !this.options.onDblclick) {
                this.selectItem(el);
            }

            var $el = $(el);
            var $parent = $el.parent();
            var $treeFolderContent = $parent.find('.tree-folder-content');
            var $treeFolderContentFirstChild = $treeFolderContent.eq(0);

            var eventType, classToTarget, classToAdd;
            if ($el.find('.fa.fa-folder').length) {
                eventType = 'opened';
                classToTarget = '.fa.fa-folder';
                classToAdd = 'fa fa-folder-open';

                $treeFolderContentFirstChild.show();
                if (!$treeFolderContent.children().length) {
                    this.populate($el);
                }
            } else {
                eventType = 'closed';
                classToTarget = '.fa.fa-folder-open';
                classToAdd = 'fa fa-folder';

                $treeFolderContentFirstChild.hide();
                if (!this.options.cacheItems) {
                    $treeFolderContentFirstChild.empty();
                }
            }

            $parent.find(classToTarget).eq(0)
                .removeClass('fa fa-folder fa-folder-open')
                .addClass(classToAdd);

            this.$element.trigger(eventType, $el.data());
        },
        selectedItems: function () {
            var $sel = this.$element.find('.tree-selected');
            var data = [];

            $.each($sel, function (index, value) {
                data.push($(value).data());
            });
            return data;
        },

        reloadFolder: function () {
            var $el = this.$element.find('.tree-selected'); // tree-item tree-folder-header
            var $treeFolderContent = $el.parent().find('.tree-folder-content');
            var $treeFolderContentFirstChild = $treeFolderContent.eq(0);
            $treeFolderContentFirstChild.empty();
            if ($el.hasClass('tree-folder-header')) {
                this.populate($el);
            }
        },
        reloadParentFolder: function () {
            var $selected = this.$element.find('.tree-selected'); // tree-item tree-folder-header
            var $el = $selected.parent().parent().parent().find('.tree-folder-header:eq(0)')
            if ($el) {
                var $treeFolderContent = $el.parent().find('.tree-folder-content');
                var $treeFolderContentFirstChild = $treeFolderContent.eq(0);
                $treeFolderContentFirstChild.empty();
                if ($el.hasClass('tree-folder-header')) {
                    this.populate($el);
                }
            }

        },

        // collapses open folders
        collapse: function () {
            var cacheItems = this.options.cacheItems;

            // find open folders
            this.$element.find('.fa.fa-folder-open').each(function () {
                // update icon class
                var $this = $(this)
                    .removeClass('fa fa-folder fa-folder-open')
                    .addClass('fa fa-folder');

                // "close" or empty folder contents
                var $parent = $this.parent().parent();
                var $folder = $parent.children('.tree-folder-content');

                $folder.hide();
                if (!cacheItems) {
                    $folder.empty();
                }
            });
        }
    };


    // TREE PLUGIN DEFINITION

    $.fn.tree = function (option, value) {
        var methodReturn;

        var $set = this.each(function () {
            var $this = $(this);
            var data = $this.data('tree');
            var options = typeof option === 'object' && option;

            if (!data) $this.data('tree', (data = new Tree(this, options)));
            if (typeof option === 'string') methodReturn = data[option](value);
        });

        return (methodReturn === undefined) ? $set : methodReturn;
    };

    $.fn.tree.defaults = {
        selectable: true,
        multiSelect: false,
        loadingHTML: '<div>Loading...</div>',
        cacheItems: true,
        openFirstFolder: false, //是否展开第一层
        folderselect: false, //目录是否可选
        customHTML: function (item) {    //自定义节点展现
            return item.name;
        },
        customType: function (type) { //自定义类型转换
            return type;
        }

    };

    $.fn.tree.Constructor = Tree;
}(window.jQuery);