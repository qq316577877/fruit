var Tree = function () {
    var DataSourceTree_ = function (options) {
        this._data = options.data;
        this._delay = options.delay;
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
            if ("nodeList" in options)
                $data = options.nodeList;
            else $data = {}
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