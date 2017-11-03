/*
 无刷新异步上传插件
 2013-10-16 Devotion Created
 */
(function ($) {
    var defaultSettings = {
        url: "",                                 //上传地址
        url_delete: "",
        url_list: "",
        url_img: "",
        url_download: "",
        isDeleteOnComplete: false,
        readOnly: null, //只读
        buttonFeature: true,                    //true:点击按钮时仅选择文件； false:选择完文件后立即上传
        fileSuffixs: [],             //允许上传的文件后缀名列表
        errorText: "不能上传后缀为 {0} 的文件！", //错误提示文本，其中{0}将会被上传文件的后缀名替换
        onCheckUpload: function (text) { //上传时检查文件后缀名不包含在fileSuffixs属性中时触发的回调函数，(text为错误提示文本)
            bootbox.alert(text);
        },
        onComplete: function (msg) { //上传完成后的回调函数[不管成功或失败，它都将被触发](msg为服务端的返回字符串)
        },
        onAllComplete: function (res) {
        },//全部文件上传完成触发的事件
        fileObjName: 'file', //文件name,对应后台获取参数key
        onChosen: function (file, obj, fileSize, errorText) { //选择文件后的回调函数，(file为选中文件的本地路径;obj为当前的上传控件实例;fileSize为当前文件的大小,errorText为获取文件大小时的错误提示文本)
            //alert(file);
            return true;//在此回调中返回false将取消当前选择的文件
        },
        maximumFilesUpload: 5,//最大文件选择数(当此属性大于1时，buttonFeature属性只能为true)
        submitFilesNum: 3,//最大提交上传数（当触发submitUpload方法时，文件上传的个数）
        onSubmitHandle: function (uploadFileNumber) { //提交上传时的回调函数，uploadFileNumber为当前上传的文件数量
            //在此回调中返回false上传提交将被阻止
            return true;
        },
        onSameFilesHandle: function (file) { //当重复选择相同的文件时触发
            //在此回调中返回false当前选择的文件将从上传队列中取消
            return true;
        },
        isGetFileSize: false,//是否获取文件大小，默认为false

        isSaveErrorFile: true,//是否保存上传失败的文件，默认true

        perviewElementId: "",//用于预览的元素id（请传入一个div元素的id）

        perviewImgStyle: null//用于设置图片预览时的样式（可不设置，在不设置的情况下多文件上传时只能显示一张图片），如{ width: '100px', height: '100px', border: '1px solid #ebebeb' }
    };

    $.fn.uploadFile = function (settings) {

        settings = $.extend({}, defaultSettings, settings || {});

        if (settings.perviewElementId) {
            //设置图片预览元素的必须样式
            if (!settings.perviewImgStyle) {
                var perviewImg = document.getElementById(settings.perviewElementId);
                perviewImg.style.overflow = "hidden";
            }
        }

        return this.each(function () {
            var self = $(this);

            var upload = new UploadAssist(settings);

            upload.createIframe(this);
            upload.initUploadInput();


            if (settings.url_list && settings.url_list != '') {
                upload.loadAttList();
            }

            //绑定当前按钮点击事件
            self.bind("click", function (e) {
                upload.chooseFile();
            });

            //将上传辅助类的实例，存放到当前对象中，方便外部获取
            self.data("uploadFileData", upload);


        });
    };
})(jQuery);

//上传辅助类
function UploadAssist(settings) {
    //保存设置
    this.settings = settings;
    //已选择文件的路径集合
    this.choseFilePath = [];
    //上传错误文件集合
    this.uploadError = [];
    //创建的iframe唯一名称
    this.iframeName = "upload" + this.getTimestamp();
    //提交状态
    this.submitStatus = true;
    //已经上传的文件数
    this.uploadFilesNum = 0;
    //上传完成计数
    this.uploadNum = 0;
    //针对IE上传获取文件大小时的错误提示文本
    this.errorText = "请设置浏览器一些参数后再上传文件，方法如下（设置一次即可）：\n请依次点击浏览器菜单中的\n'工具->Internet选项->安全->可信站点->自定义级别'\n在弹出的自定义级别窗口中找到 'ActiveX控件和插件' 项，将下面的子项全部选为 '启用' 后，点击确定。\n此时不要关闭当前窗口，再点击 '站点' 按钮，在弹出的窗口中将下面复选框的 '√' 去掉，然后点击 '添加' 按钮并关闭当前窗口。\n最后一路 '确定' 完成并刷新当前页面。";
    return this;
}

UploadAssist.prototype = {
    //辅助类构造器
    constructor: UploadAssist,

    //创建iframe
    createIframe: function (/*插件中指定的dom对象*/elem) {
        var html = "<html>"
            + "<head>"
            + "<title>upload</title>"
            + "<script>"
            + "function getDCMT(iframeName){return window.frames[iframeName].document;}"
            + "</" + "script>"
            + "</head>"
            + "<body>"
            + "</body>"
            + "</html>";

        this.iframe = $("<iframe name='" + this.iframeName + "'></iframe>")[0];
        this.iframe.style.width = "0px";
        this.iframe.style.height = "0px";
        this.iframe.style.border = "0px solid #fff";
        this.iframe.style.margin = "0px";
        elem.parentNode.insertBefore(this.iframe, elem);
        var iframeDocument = this.getIframeContentDocument();
        iframeDocument.write(html);
        iframeDocument.close();
    },

    //获取时间戳
    getTimestamp: function () {
        return (new Date()).valueOf();
    },

    //TODO: 加载附件列表
    loadAttList: function () {
        var that = this;
        var dcmt = this.getIframeContentDocument();


        $.ajax(that.settings.url_list, {
            data: {},
            type: "POST",
            dataType: "json"
        }).always(function () {
        }).done(function (data) {
            if (data.result) {
                for (var i = 0; i < data.t.length; i++) {
                    var d_ = data.t[i];
                    var ul = perviewImage.getPerviewRegion(that.settings.perviewElementId);
                    var main = perviewImage.createPreviewElement(d_.fileId, d_.fileName, that.settings.perviewImgStyle);

                    var li = document.createElement("li");
                    if ($.browser.msie) {
                        li.style.styleFloat = "left";
                    }
                    else {
                        li.style.cssFloat = "left";
                    }

                    li.style.margin = "5px";
                    li.appendChild(main);
                    ul.appendChild(li);
                    var div = $(main).children("div").get(0);

                    var fileSuf = d_.fileName.substring(d_.fileName.lastIndexOf(".") + 1);
                    perviewImage.beginDisplayList(d_, div, fileSuf, that.settings);
                }
                $("#" + that.settings.uploaded_file_flag).val(data.t.length > 0 ? "true" : "");
            } else {
                bootbox.alert(data.msg);
            }
        }).fail(function () {
        });
    },

    //创建上传控件到创建的iframe中
    createInputFile: function () {
        var that = this;
        var dcmt = this.getIframeContentDocument();
        var input = dcmt.createElement("input");
        var randomNum = this.getTimestamp();
        input.type = "file";

        $(input).attr("name", that.settings.fileObjName);//"input" + randomNum);
        $(input).attr("id", "input" + randomNum);

        input.onchange = function () {

            //保存已经选择的文件路径
            that.choseFilePath.push({"name": this.id, "value": this.value});

            var fileSuf = this.value.substring(this.value.lastIndexOf(".") + 1);

            //检查是否为允许上传的文件,fileSuffixs 为空则不验证
            if (that.settings.fileSuffixs.length > 0 && !that.checkFileIsUpload(fileSuf, that.settings.fileSuffixs)) {
                that.removeFile(this.id);
                that.settings.onCheckUpload(that.settings.errorText.replace("{0}", fileSuf));
                return;
            }

            var fileSize;
            var errorTxt = null;
            //是否获取上传文件大小
            if (that.settings.isGetFileSize) {
                fileSize = perviewImage.getFileSize(this, dcmt);
                if (fileSize == "error") {
                    fileSize = 0;
                    errorTxt = that.errorText;
                }
            }

            //选中后的回调
            var chosenStatus = that.settings.onChosen(this.value, this, fileSize, errorTxt);
            if (typeof chosenStatus === "boolean" && !chosenStatus) {
                that.removeFile(this.id);
                return;
            }

            if (that.checkFileIsExist(this.value)) {
                var status = that.settings.onSameFilesHandle(this.value);
                if (typeof status === "boolean" && !status) {
                    that.removeFile(this.id);
                    return;
                }
            }

            //是否开启了图片预览
            if (that.settings.perviewElementId) {
                if (!that.settings.perviewImgStyle) {
                    perviewImage.beginPerview(this, that.settings.perviewElementId, dcmt, fileSuf);
                } else {
                    var ul = perviewImage.getPerviewRegion(that.settings.perviewElementId);
                    var main = perviewImage.createPreviewElement(this.id, this.value, that.settings.perviewImgStyle);
                    var li = document.createElement("li");
                    if ($.browser.msie) {
                        li.style.styleFloat = "left";
                    }
                    else {
                        li.style.cssFloat = "left";
                    }

                    li.style.margin = "5px";
                    li.appendChild(main);
                    ul.appendChild(li);
                    var div = $(main).children("div").get(0);

                    perviewImage.beginPerview(this, div, dcmt, fileSuf);

                    //TODO: 绑定取消上传事件
                    /*$(main).find("a[name]").click(function () {
                     var name_ = $(this).attr("name");
                     //判断是否取消按钮
                     if( name_.indexOf('_delete') !=-1){
                     that.removeFile($(this).attr("name").replace('_delete',''));
                     $(this).parents("li").fadeOut(200, function () {
                     $(this).remove();
                     });
                     }
                     });*/
                    var file_id = this.id;
                    $('#' + this.id + '_delete').click(function () {
                        //前台删除
                        that.removeFile(file_id);
                        $(this).parents("li").fadeOut(200, function () {
                            $(this).remove();
                        });
                    });
                }
            }

            if (!that.settings.buttonFeature) {
                that.submitUpload();
            }
        };

        var formName = "form" + randomNum;
        var form = $('<form method="post" target="iframe' + randomNum + '" enctype="multipart/form-data" action="' + that.settings.url + '" name="' + formName + '"></form>');
        form.append(input);

        $(dcmt.body).append($("<div></div>").append(form)
            .append($("<iframe name='iframe" + randomNum + "'></iframe>").on("load", function () {
                var dcmt1 = that.getInsideIframeContentDocument(this.name);
                if (dcmt1.body.innerHTML) {
                    //开始上传下一个文件
                    that.insideOperation();
                    that.uploadNum++;

                    //注意：上传失败的响应文本默认为"error"
                    var responseText = $(dcmt1.body).text();
                    var res_json = eval("(" + responseText + ")");

                    if (!res_json.result && that.settings.isSaveErrorFile) {
                        //保存上传失败的文件
                        that.uploadError.push(this.name.replace("iframe", "input"));
                    }

                    var obj = that.getObjectByName(this.name.replace("iframe", "input"));
                    if (obj) {
                        var flag_icon = $('#' + obj.name + '_flag');

                        //是否开启了预览
                        if (that.settings.perviewElementId) {
                            var loadingimg = $("#" + that.settings.perviewElementId).find("img[name='" + obj.name + "']");
                            loadingimg.hide();
                            if (res_json.result) {
                                /*TODO: 不删除预览,绑定ID
                                 loadingimg.parents("li").fadeOut("slow", function () {
                                 //$(this).remove();
                                 });
                                 loadingimg.css("visibility", "visible").parents("li").css({
                                 "border": "2px solid #66CDAA"//,"background-color": "#9BCD9B"
                                 });*/

                                //TODO: 修改绑定，删除附件

                                $("#" + that.settings.uploaded_file_flag).val("true");

                                var op = {};
                                op.fileID = res_json.t;
                                op.setting = that.settings;
                                perviewImage.bindDeleteEvent($('#' + obj.name + '_delete')[0], op);
                                /*
                                 $('#'+obj.name+'_delete').click(function () {
                                 var deleteBtn = this;

                                 //后台删除
                                 $.ajax(that.settings.url_delete, {
                                 data: {
                                 id: res_json.t
                                 },
                                 type: "POST",
                                 dataType: "json"
                                 }).always(function () {
                                 }).done(function (data) {
                                 if(data.result){
                                 //前台删除
                                 that.removeFile(obj.name);
                                 $(deleteBtn).parents("li").fadeOut(200, function () {
                                 $(deleteBtn).remove();
                                 });
                                 }else{
                                 bootbox.alert(data.msg);
                                 }
                                 }).fail(function () {
                                 });
                                 });*/

                                flag_icon.attr('class', 'fa fa-check normal');
                            } else {

                                flag_icon.attr('class', 'fa fa-warning warning');

                                //上传失败的文件，加亮显示
                                /*
                                 loadingimg.css("visibility", "visible").parents("li").css({
                                 "border": "2px solid #ff9999"//,"background-color": "#ffdddd"
                                 });*/
                            }
                        }
                    }

                    if (that.settings.onComplete) {
                        that.settings.onComplete(dcmt1.body.innerHTML);
                    }

                    if (that.uploadNum == that.uploadFilesNum) {
                        that.submitStatus = true;
                        that.clearUploadQueue();  //TODO： 初始化iframe中的form(预留一个空的form)，不清空预览框中的图片
                        that.uploadFilesNum = 0;
                        that.uploadNum = 0;
                        that.settings.onAllComplete(res_json);

                        if (that.settings.isDeleteOnComplete) {
                            $("#" + that.settings.perviewElementId).find("ul").empty();
                        }

                    }

                    dcmt1.body.innerHTML = "";
                }
            })));
        return input;
    },

    //获取创建的iframe中的document对象
    getIframeContentDocument: function () {
        return this.iframe.contentDocument || this.iframe.contentWindow.document;
    },

    //获取创建的iframe所在的window对象
    getIframeWindow: function () {
        return this.iframe.contentWindow || this.iframe.contentDocument.parentWindow;
    },

    //获取创建的iframe内部iframe的document对象
    getInsideIframeContentDocument: function (iframeName) {
        return this.getIframeWindow().getDCMT(iframeName);
    },
    //预加载上传控件，即时创建会使浏览器有延时
    initUploadInput: function () {
        this.nextUploadInput = this.createInputFile();
    },
    //获取上传input控件
    getUploadInput: function () {
        var that = this;
        var inputs = this.getIframeContentDocument().getElementsByTagName("input");
        var len = inputs.length;
        var _input;
        var _isLastOne = false;
        if (len > 0) {
            for (var i = 0; i < len; i++) {
                if (!inputs[i].value) {
                    _input = inputs[i];
                    _isLastOne = i == len - 1;
                    break;
                }
            }

            //最后一个进行预加载
            if (_isLastOne) {
                var time = 100;
                window.setTimeout(function () {
                    that.nextUploadInput = that.createInputFile();
                }, time);
            }

            if (_input) {
                return _input;
            }

            /*
             if (!inputs[len - 1].value) {
             return inputs[len - 1];
             } else {
             return this.createInputFile();
             }*/
        }
        return this.createInputFile();
    },

    //forEach迭代函数
    forEach: function (/*数组*/arr, /*代理函数*/fn) {
        var len = arr.length;
        for (var i = 0; i < len; i++) {
            var tmp = arr[i];
            if (fn.call(tmp, i, tmp) == false) {
                break;
            }
        }
    },

    //提交上传
    submitUpload: function () {
        var status = this.settings.onSubmitHandle(this.choseFilePath.length);

        if (typeof status === "boolean") {
            if (!status) {
                return;
            }
        }

        var sbmtNum = this.settings.submitFilesNum;
        var len = this.choseFilePath.length;
        var dcmt = this.getIframeContentDocument();
        var that = this;

        if (!len) return;
        if (!this.submitStatus) return;
        this.filesNum = len;

        this.clearedNotChooseFile();

        //设置有效上传数量，有可能选择的文件小于设置的提交数量
        var advisableSubmitNum = sbmtNum < len ? sbmtNum : len;

        this.uploadFilesNum = advisableSubmitNum;

        this.submitStatus = false;
        for (var i = 0; i < advisableSubmitNum; i++) {
            (function (n) {
                var time = (n + 1) * 500;
                window.setTimeout(function () {
                    var obj = that.choseFilePath[n];
                    var formName = obj.name.replace("input", "form");
                    that.forEach(dcmt.forms, function () {
                        //TODO: 判断form是否为空,如果空则不提交
                        var input_ = dcmt.getElementById(obj.name);
                        if (this.name == formName && input_ && input_.value) {
                            this.submit();
                            return false;
                        }
                    });
                    if (that.settings.perviewElementId) {
                        //用于设置上传loading图片显示 
                        var imgloading = $("#" + that.settings.perviewElementId).find("img[name='" + obj.name + "']");
                        imgloading.show();
                        //imgclose.css("visibility", "hidden");
                    }
                }, time);
            })(i);
        }
    },
    //内部提交操作，外部不能调用
    insideOperation: function () {
        var len = this.choseFilePath.length;
        var dcmt = this.getIframeContentDocument();
        var that = this;

        if (!len) return;
        var obj = this.choseFilePath[this.uploadFilesNum];

        if (obj && obj.name) {
            this.uploadFilesNum++;
            (function (o) {
                window.setTimeout(function () {
                    var formName = o.name.replace("input", "form");

                    that.forEach(dcmt.forms, function (i) {
                        if (this.name == formName) {
                            this.submit();
                            return false;
                        }
                    });

                    if (that.settings.perviewElementId) {
                        //用于设置上传loading图片显示 
                        var imgloading = $("#" + that.settings.perviewElementId).find("img[name='" + obj.name + "']");
                        imgloading.show();
                    }
                }, 300);
            })(obj);
        }
    },
    //检查文件是否可以上传
    checkFileIsUpload: function (fileSuf, suffixs) {

        var status = false;
        this.forEach(suffixs, function (i, n) {
            if (fileSuf.toLowerCase() === n.toLowerCase()) {
                status = true;
                return false;
            }
        });
        return status;
    },

    //检查上传的文件是否已经存在上传队列中
    checkFileIsExist: function (/*当前上传的文件*/file) {

        var status = false;
        this.forEach(this.choseFilePath, function (i, n) {
            if (n.value == file) {
                status = true;
                return false;
            }
        });
        return status;
    },

    //清除未选择文件的上传控件
    clearedNotChooseFile: function () {
        var files = this.getIframeContentDocument().getElementsByTagName("input");

        this.forEach(files, function (i, n) {
            if (!n.value) {
                var div = n.parentNode.parentNode;
                div.parentNode.removeChild(div);
                return false;
            }
        });
    },

    //将指定上传的文件从上传队列中删除
    removeFile: function (name) {
        var that = this;
        var files = this.getIframeContentDocument().getElementsByTagName("input");
        this.forEach(this.choseFilePath, function (i, n) {
            if (n.name == name) {
                that.forEach(files, function (j, m) {
                    if (m.id == n.name) {
                        var div = m.parentNode.parentNode;
                        div.parentNode.removeChild(div);
                        return false;
                    }
                });
                that.choseFilePath.splice(i, 1);
                return false;
            }
        });
    },
    //获取选择的上传文件对象
    getObjectByName: function (name) {
        var obj, that = this;
        this.forEach(this.choseFilePath, function (i) {
            if (this.name === name) {
                obj = that.choseFilePath[i];
                return false;
            }
        });
        return obj;
    },
    //清空上传队列
    clearUploadQueue: function () {
        var len = this.uploadError.length;
        var that = this;
        if (!len) {
            this.choseFilePath.length = 0;
            /*清空file中的数据
             var files = this.getIframeContentDocument().getElementsByTagName("input");
             for(var i=0;i<files.length;i++){
             if(files[i] && files[i].value){
             files[i].outerHTML=files[i].outerHTML;
             }
             }*/
            this.getIframeContentDocument().body.innerHTML = "";
        } else {
            var errorFiles = this.uploadError.join();
            var newArr = this.choseFilePath.slice(0);
            this.forEach(newArr, function () {
                if (errorFiles.indexOf(this.name) == -1) {
                    that.removeFile(this.name);
                }
            });
        }
        //最后一个进行预加载
        var time = 100;
        window.setTimeout(function () {
            that.nextUploadInput = that.createInputFile();
        }, time);

        this.uploadError.length = 0;
    },

    //选择上传文件
    chooseFile: function () {
        var uploadfile;
        //TODO: 不清空预览区域；或者添加已完成的区域
        if (!this.choseFilePath.length && this.settings.perviewElementId) {
            //$("#" + this.settings.perviewElementId).find("ul").empty();
        }

        var file_number = $('#' + this.settings.perviewElementId + ' li:visible').length;

        if (file_number >= this.settings.maximumFilesUpload) {


            bootbox.alert("最多上传" + this.settings.maximumFilesUpload + "个文件！")
            return;

            /*
             if (this.settings.maximumFilesUpload <= 1) {
             this.choseFilePath.length = 0;
             var files = this.getIframeContentDocument().getElementsByTagName("input");
             if (!files.length) {
             uploadfile = this.getUploadInput();
             $(uploadfile).click();
             return;
             } else {
             uploadfile = files[0];
             $(uploadfile).click();
             return;
             }
             } else {
             return;
             }*/
        }
        uploadfile = this.getUploadInput();
        $(uploadfile).click();
    }
};

//图片预览操作
var perviewImage = {
    timers: [],
    closeImg: {},
    loading: "/assets/plugins/upload-custom/img/loading.gif",
    fileImg: "/assets/plugins/upload-custom/img/file.png",

    //获取预览元素
    getElementObject: function (elem) {
        if (elem.nodeType && elem.nodeType === 1) {
            return elem;
        } else {
            return document.getElementById(elem);
        }
    },
    //开始图片预览
    beginPerview: function (/*文件上传控件实例*/file, /*需要显示的元素id或元素实例*/perviewElemId, /*上传页面所在的document对象*/ dcmt, /*文件后缀名*/ fileSuf) {
        var imgSufs = ",jpg,jpeg,bmp,png,gif,";
        var isImage = imgSufs.indexOf("," + fileSuf.toLowerCase() + ",") > -1;//检查是否为图片

        if (isImage) {
            this.imageOperation(file, perviewElemId, dcmt);
        } else {
            this.fileOperation(perviewElemId, fileSuf, file);
        }
    },

    //TODO: 开始加载附件列表
    beginDisplayList: function (/*附件对象*/att, /*需要显示的元素id或元素实例*/perviewElemId, /*文件后缀名*/ fileSuf, setting) {
        var imgSufs = ",jpg,jpeg,bmp,png,gif,";
        var isImage = imgSufs.indexOf("," + fileSuf.toLowerCase() + ",") > -1;//检查是否为图片

        this.fixAttItem(perviewElemId, isImage, att, setting);
    },

    //填充附件
    fixAttItem: function (/*需要显示的元素id或元素实例*/perviewElemId, isImg, att, setting) {
        var preview_div = this.getElementObject(perviewElemId);
        preview_div.className = "ih-item square effect6 top_to_bottom";

        var main_div = document.createElement("div");
        main_div.style.width = preview_div.style.width;
        main_div.style.height = preview_div.style.height;

        var img_div = document.createElement("div");
        img_div.className = 'img';
        img_div.style.width = preview_div.style.width;
        img_div.style.height = preview_div.style.height;

        var MAXWIDTH = preview_div.clientWidth;
        var MAXHEIGHT = preview_div.clientHeight;

        var img = document.createElement("img");

        img_div.appendChild(img);
        main_div.appendChild(img_div);
        var op = {};
        op.fileName = att.fileName.substring(att.fileName.lastIndexOf("\\") + 1, att.fileName.length);
        op.fileID = att.fileId;
        op.setting = setting;
        main_div.appendChild(this.fixGallery(op));
        preview_div.appendChild(main_div);

        img.style.visibility = "hidden";

        img.onload = function () {
            var rect = perviewImage.clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
            img.style.width = rect.width + 'px';
            img.style.height = rect.height + 'px';
            img.style.marginLeft = rect.left + 'px';
            img.style.marginTop = rect.top + 'px';
            img.style.visibility = "visible";
        };

        img.src = isImg ? setting.url_img + "?id=" + att.fileId : this.fileImg;
        if (!isImg) {
            var txtTop = 0 - (MAXHEIGHT * 2 / 3);
            var txt_length = MAXWIDTH * 2 / 3;
            var left = MAXWIDTH / 6;
            $('<div style="width:' + txt_length + 'px;word-wrap: break-word; word-break: normal;text-align:center; position:relative; z-index:100; color:#404040;font: 13px/27px Arial,sans-serif;"></div>')
                .text(op.fileName).css("top", txtTop + "px").css("left", left + "px").appendTo(img_div);
        }

    },

    //TODO:
    fixGallery: function (op) {

        var tools_top = document.createElement("div");
        tools_top.className = 'info';

        var h3 = document.createElement("h3");

        var a_download = document.createElement("a");
        a_download.href = 'javascript:void(0)';
        a_download.id = op.fileID + "_download";
        a_download.name = op.fileID + "_download";

        var fa_download = document.createElement("i");
        fa_download.className = 'fa fa-download';
        a_download.appendChild(fa_download);

        var a_search = document.createElement("a");
        a_search.href = 'javascript:void(0)';

        var fa_search = document.createElement("i");
        fa_search.className = 'fa fa-search';
        a_search.appendChild(fa_search);

        var a_delete = document.createElement("a");
        a_delete.href = 'javascript:void(0)';
        a_delete.id = op.fileID + "_delete";
        a_delete.name = op.fileID + "_delete";

        var fa_delete = document.createElement("i");
        fa_delete.className = 'fa fa-trash-o warning';

        a_delete.appendChild(fa_delete);


        //tools_top.appendChild(h3);
        //tools_top.appendChild(a_download);
        //tools_top.appendChild(a_search);
        if (!(op.setting && op.setting.readOnly && $('#' + op.setting.readOnly).css("display") == "none")) {
            tools_top.appendChild(a_delete);
        }

        if (op.setting) { //加载的已上传附件列表
            a_download.href = op.setting.url_download + "?id=" + op.fileID;
            tools_top.appendChild(a_download);

            var flag_icon = $('#' + op.fileID + '_flag');
            flag_icon.attr('class', 'fa fa-check normal');

            //TODO: 修改绑定，删除附件
            this.bindDeleteEvent(a_delete, op);

        }
        return tools_top;
    },

    bindDeleteEvent: function (a_delete, op) {
        $(a_delete).unbind('click');
        $(a_delete).click(function () {
            var deleteBtn = this;
            bootbox.confirm("是否删除附件？", function (result) {
                if (result) {
                    //后台删除
                    $.ajax(op.setting.url_delete, {
                        data: {
                            id: op.fileID
                        },
                        type: "POST",
                        dataType: "json"
                    }).always(function () {
                    }).done(function (data) {

                        if (data.result) {
                            //前台删除
                            $(deleteBtn).parents("li").fadeOut(200, function () {
                                $(deleteBtn).remove();
                                var file_number = $('#' + op.setting.perviewElementId + ' li:visible').length;
                                $("#" + op.setting.uploaded_file_flag).val(file_number > 0 ? "true" : "");
                            });

                        } else {
                            bootbox.alert(data.msg);
                        }
                    }).fail(function () {
                    });
                }
            });
        });


    },

    //一般文件显示操作
    fileOperation: function (/*需要显示的元素id或元素实例*/perviewElemId, /*文件后缀名*/ fileSuf, file) {
        var preview_div = this.getElementObject(perviewElemId);
        preview_div.className = "ih-item square effect6 top_to_bottom";

        var main_div = document.createElement("div");
        main_div.style.width = preview_div.style.width;
        main_div.style.height = preview_div.style.height;

        var img_div = document.createElement("div");
        img_div.className = 'img';
        img_div.style.width = preview_div.style.width;
        img_div.style.height = preview_div.style.height;

        var MAXWIDTH = preview_div.clientWidth;
        var MAXHEIGHT = preview_div.clientHeight;

        var img = document.createElement("img");

        img_div.appendChild(img);
        main_div.appendChild(img_div);
        var op = {};
        op.fileName = file.value.substring(file.value.lastIndexOf("\\") + 1, file.value.length);
        op.fileID = file.id;
        main_div.appendChild(this.fixGallery(op));
        preview_div.appendChild(main_div);

        img.style.visibility = "hidden";
        img.onload = function () {
            var rect = perviewImage.clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
            img.style.width = rect.width + 'px';
            img.style.height = rect.height + 'px';
            img.style.marginLeft = rect.left + 'px';
            img.style.marginTop = rect.top + 'px';
            img.style.visibility = "visible";
        };
        img.src = this.fileImg; //图片加载过快不会调用onload

        var txtTop = 0 - (MAXHEIGHT * 2 / 3);

        var txt_length = MAXWIDTH * 2 / 3;

        var left = MAXWIDTH / 6;

        $('<div style="width:' + txt_length + 'px;word-wrap: break-word; word-break: normal;text-align:center; position:relative; z-index:100; color:#404040;font: 13px/27px Arial,sans-serif;"></div>')
            .text(op.fileName).css("top", txtTop + "px").css("left", left + "px").appendTo(img_div);

    },

    //图片预览操作
    imageOperation: function (/*文件上传控件实例*/file, /*需要显示的元素id或元素实例*/perviewElemId, /*上传页面所在的document对象*/ dcmt) {
        for (var t = 0; t < this.timers.length; t++) {
            window.clearInterval(this.timers[t]);
        }
        this.timers.length = 0;

        var preview_div = this.getElementObject(perviewElemId);
        preview_div.className = "ih-item square effect6 top_to_bottom";

        var MAXWIDTH = preview_div.clientWidth;
        var MAXHEIGHT = preview_div.clientHeight;

        var main_div = document.createElement("div");
        main_div.style.width = preview_div.style.width;
        main_div.style.height = preview_div.style.height;

        var img_div = document.createElement("div");
        img_div.className = 'img';
        img_div.style.width = preview_div.style.width;
        img_div.style.height = preview_div.style.height;

        var op = {};
        op.fileID = file.id;
        main_div.appendChild(img_div);
        main_div.appendChild(this.fixGallery(op));
        preview_div.appendChild(main_div);

        if (file.files && file.files[0]) { //此处为Firefox，Chrome以及IE10的操作


            img_div.innerHTML = "";
            var img = document.createElement("img");

            img_div.appendChild(img);
            //preview_div.appendChild(img);
            img.style.visibility = "hidden";
            img.onload = function () {
                var rect = perviewImage.clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
                img.style.width = rect.width + 'px';
                img.style.height = rect.height + 'px';
                img.style.marginLeft = rect.left + 'px';
                img.style.marginTop = rect.top + 'px';
                img.style.visibility = "visible";
            }

            var reader = new FileReader();
            reader.onload = function (evt) {
                img.src = evt.target.result;
            }
            reader.readAsDataURL(file.files[0]);
        }
        else {//此处为IE6，7，8，9的操作
            file.select();
            var src = dcmt.selection.createRange().text;

            var div_sFilter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='scale',src='" + src + "')";
            var img_sFilter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='image',src='" + src + "')";


            img_div.innerHTML = "";
            var img = document.createElement("div");
            img_div.appendChild(img);

            img.style.filter = img_sFilter;
            img.style.visibility = "hidden";
            img.style.width = "100%";
            img.style.height = "100%";

            function setImageDisplay() {
                var rect = perviewImage.clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
                img_div.innerHTML = "";
                var div = document.createElement("div");
                div.style.width = rect.width + 'px';
                div.style.height = rect.height + 'px';
                div.style.marginLeft = rect.left + 'px';
                div.style.marginTop = rect.top + 'px';
                div.style.filter = div_sFilter;

                img_div.appendChild(div);
            }

            //图片加载计数
            var tally = 0;

            var timer = window.setInterval(function () {
                if (img.offsetHeight != MAXHEIGHT) {
                    window.clearInterval(timer);
                    setImageDisplay();
                } else {
                    tally++;
                }
                //如果超过两秒钟图片还不能加载，就停止当前的轮询
                if (tally > 20) {
                    window.clearInterval(timer);
                    setImageDisplay();
                }
            }, 100);

            this.timers.push(timer);
        }
    },
    //按比例缩放图片
    clacImgZoomParam: function (maxWidth, maxHeight, width, height) {
        var param = {width: width, height: height};
        if (width > maxWidth || height > maxHeight) {
            var rateWidth = width / maxWidth;
            var rateHeight = height / maxHeight;

            if (rateWidth > rateHeight) {
                param.width = maxWidth;
                param.height = Math.round(height / rateWidth);
            } else {
                param.width = Math.round(width / rateHeight);
                param.height = maxHeight;
            }
        }

        param.left = Math.round((maxWidth - param.width) / 2);
        param.top = Math.round((maxHeight - param.height) / 2);
        return param;
    },
    //创建图片预览元素
    createPreviewElement: function (/*关闭图片名称*/name, /*上传时的文件名*/file, /*预览时的样式*/style) {
        var img = document.createElement("div");
        img.title = file;
        img.style.overflow = "hidden";
        for (var s in style) {
            img.style[s] = style[s];
        }


        var text = document.createElement("div");
        var style_ = 'width:' + style.width + ';';
        style_ += 'overflow:hidden;textOverflow:ellipsis;whiteSpace:nowrap;word-wrap:break-word;word-break:normal;text-align:center';

        /*	
         text.style.width = style.width;
         text.style.overflow = "hidden";
         text.style.textOverflow = "ellipsis";
         text.style.whiteSpace = "nowrap";
         text.style['word-wrap']='break-word';
         text.style['word-break']='normal';
         text.style['text-align']='center';
         */
        text.setAttribute("style", style_);

        text.id = name + "_text";
        text.innerHTML = file.substring(file.lastIndexOf("\\") + 1, file.length);

        var flag = document.createElement("i");
        flag.id = name + "_flag";
        text.appendChild(flag);

        /*
         var top = 0 - window.parseInt(style.width) - 15;
         var right = 0 - window.parseInt(style.width) + 14;
         var close = document.createElement("img");
         close.setAttribute("name", name);
         close.src = this.closeImg.before;
         close.style.position = "relative";
         close.style.top = top + "px";
         close.style.right = right + "px";
         close.style.cursor = "pointer";
         */

        var loadtop = (0 - window.parseInt(style.height)) / 2 - 26;//;
        var loadright = (0 - window.parseInt(style.width)) / 2 + 18;//+ 22;
        var imgloading = document.createElement("img");
        imgloading.setAttribute("name", name);
        imgloading.src = this.loading;
        imgloading.style.position = "relative";
        imgloading.style.top = loadtop + "px";
        imgloading.style.right = loadright + "px";
        imgloading.style.display = "none";

        var main = document.createElement("div");
        main.appendChild(img);
        main.appendChild(text);
        //main.appendChild(close);
        main.appendChild(imgloading);
        return main;
    },

    //获取预览区域
    getPerviewRegion: function (elem) {
        var perview = $(this.getElementObject(elem));
        if (!perview.find("ul").length) {
            var ul = document.createElement("ul");
            ul.style.listStyleType = "none";
            ul.style.margin = "0px";
            ul.style.padding = "0px";

            var div = document.createElement("div");
            div.style.clear = "both";
            perview.append(ul).append(div);
            return ul;
        } else {
            return perview.children("ul").get(0);
        }
    },
    //获取上传文件大小
    getFileSize: function (/*上传控件dom对象*/file, /*上传控件所在的document对象*/dcmt) {
        var fileSize;
        if (file.files && file.files[0]) {
            fileSize = file.files[0].size;
        } else {
            file.select();
            var src = dcmt.selection.createRange().text;
            try {
                var fso = new ActiveXObject("Scripting.FileSystemObject");
                var fileObj = fso.getFile(src);
                fileSize = fileObj.size;
            } catch (e) {
                return "error";
            }
        }
        fileSize = ((fileSize / 1024) + "").split(".")[0];
        return fileSize;
    }
}