var formValidation = function (form_id) {
    // for more info visit the official plugin documentation:
    // http://docs.jquery.com/Plugins/Validation
    var form1 = $('#' + form_id);

    $.metadata.setType("attr", "validate");
    var v_ = form1.validate({
        errorElement: 'span', //default input error message container
        errorClass: 'help-block', // default input error message class
        focusInvalid: false, // do not focus the last invalid input
        ignore: "",
        invalidHandler: function (e, obj) {
            App.revertJBTN();
            var f_ = obj.findFirstErrorElement()[0].element;
            if (f_) {
                f_.focus();
            }
        },
        errorPlacement: function (error, element) { // render error placement for each input type
            var p_ = $(element).parent('.input-group');
            if (p_ && p_.length > 0) {
                p_.after(error);
            } else {
                error.insertAfter(element);
            }
        },
        highlight: function (element) { // hightlight error inputs
            $(element).closest('.form-group').addClass('has-error'); // set error class to the control group
        },

        unhighlight: function (element) { // revert the change done by hightlight
            var p = $(element).closest('.form-group');
            var hasError = false;
            p.find(".help-block").each(function (e, obj) {
                if (obj.innerText != '' && obj.style.display!="none") {
                    hasError = true;
                }
            });
            if (!hasError) {
                //console.log("hasError1");
                p.removeClass('has-error');
            }
            //else if (!p.hasClass('has-error')) {
            //    console.log("hasError2");
            //    p.addClass('has-error');
            //}
            else{
                //console.log("hasError3");
                p.addClass('has-error');
            }


            // set error class to the control group
        },

        success: function (label) {
            label
                .closest('.form-group').removeClass('has-error'); // set success class to the control group
        }
    });

    return v_;
};


var formSaveOptions = function (saveUrl, params, redirectUrl, successMsg, field_id) {
    successMsg = successMsg ? successMsg : "保存成功!";
    field_id = field_id ? field_id : 'id';
    var options = {
        url: saveUrl,
        type: 'post',
        contentType: "application/x-www-form-urlencoded; charset=utf-8",
        data: params,
        success: function (data) {

            App.revertJBTN();

            if (data.result) {
                bootbox.alert(successMsg, function () {
                        if (data.t) {
                            $("#" + field_id).val(data.t);
                        }

                        if (redirectUrl) {
                            if (redirectUrl.indexOf('?') > 0) {
                                window.location.href = redirectUrl + "&" + field_id + "=" + $("#" + field_id).val();
                            } else {
                                window.location.href = redirectUrl + "?" + field_id + "=" + $("#" + field_id).val();
                            }
                        } else {
                            if (top.openDialog) {
                                top.openDialog.close();
                            } else {
                                window.close();
                            }

                        }
                    }
                );
            } else {
                bootbox.alert(data.msg);
            }

        }
    };

    return options;
};


var set4Detail = function () {
    //查看只读
    if (location.href.indexOf('detail.htm') != -1) {
        $("form input:not(:hidden)").prop("disabled", true);
        $("form select").prop("disabled", true);
        $("form button:not(.selectpicker)").css("display", "none");
        $("form a").attr({"disabled": "disabled"});
        $("form textArea").prop("readonly", true);
        $(':button[id^=files_choose_]').css("display", "none");
        $(':button[id^=files_upload_]').css("display", "none");
        return true;
    }

    return false;
}

//form表单查询条件重置
var reset = function () {
    $("form input:not(:hidden)").val("");
    $("form input[isreset]").val("");
    $("form select").val("");
    $("form textArea").val("");
}


var deleteFunc = function (url) {
    var ids = [];
    $('#table_1 .checkboxes:checked').each(function () {
        ids.push($(this).val());
    });

    if (ids.length == 0) {
        bootbox.alert("请选择一条数据！");
        return;
    }
    bootbox.confirm("是否删除数据?", function (result) {
        if (result) {

            $.ajax(url, {
                data: {
                    'ids': ids.join(",")
                },
                type: "POST",
                dataType: "json"
            }).always(function () {
            }).done(function (data) {
                if (data.result) {
                    window.location.href = window.location.href.replace(/#/g, '');
                } else {
                    bootbox.alert(data.msg);
                }
            }).fail(function () {
            });
        }
    });
}

var confirmFunc = function (url, title) {

    bootbox.confirm(title, function (result) {
        if (result) {

            $.ajax(url, {
                data: {

                },
                type: "GET",
                dataType: "json"
            }).always(function () {
            }).done(function (data) {
                if (data.result) {
                    window.location.href = window.location.href.replace(/#/g, '');
                } else {
                    bootbox.alert(data.msg);
                }
            }).fail(function () {
            });
        }
    });

}

var doCallAjax = function (url, jsonData) {
    $.ajax(url, {
        data: jsonData,
        type: "POST",
        dataType: "json"
    }).always(function () {
    }).done(function (data) {
        if (data.result) {
            bootbox.alert("处理成功", function () {
                    window.location.href = window.location.href.replace(/#/g, '');
                }
            );
        } else {
            bootbox.alert(data.msg);
        }
    }).fail(function () {
    });
}

