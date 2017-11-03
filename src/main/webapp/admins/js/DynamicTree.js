/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

var DynamicTree = function () {
    var DataSourceTree_ = function (options) {
        this._data = options.data;
        this._delay = options.delay;
        this._url = options.url;
    };

    DataSourceTree_.prototype.data = function (options, callback) {
        var self = this;
        var $data = null;

        if (!("name" in options) && !("type" in options)) {
            $data = this._data;//根节点
            callback({data: $data});
            return;
        }
        else if ("type" in options) {
            if ("nodeList" in options) {
                var parentId = options.id;
                    $.ajax({
                        url: this._url,
                        data: {"parentId": parentId},
                        type: 'POST',
                        dataType: 'json',
                        success: function (response) {
                            if (response)
                                callback({ data: response.t.nodeList })
                        },
                        error: function (response) {

                        }
                    })

            } else{
                $data = {}
            }
        }


        if ($data != null)
            setTimeout(function () {
                callback({data: $data});
            }, this._delay);
    };
    return {
        DataSourceTree: function (options) {
            return new DataSourceTree_(options)
        }
    };
}();