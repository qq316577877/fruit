//检测手机号是否正确
jQuery.validator.addMethod("isMobile", function(value, element) {
    var length = value.length;
    //var regPhone = /^1[3|4|5|8][0-9]\d{4,8}$/;//最新手机js正则
    var regPhone = /^0?(13[0-9]|15[012356789]|17[013678]|18[0-9]|14[57])[0-9]{8}$/;//最新手机js正则，2017.7.7
    return this.optional(element) || ( length == 11 && regPhone.test( value ) );
}, "请填写正确的手机号码！");




//检测邮箱是否正确--允许为空
jQuery.validator.addMethod("isEmailCanEmpty", function(value, element) {
    var regEmail = /(^$)|^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
    return this.optional(element)|| (regEmail.test( value ) );
}, "请填写正确的邮箱！");


//检测邮箱是否正确-不允许为空
jQuery.validator.addMethod("isEmail", function(value, element) {
    var regEmail = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
    return this.optional(element)|| (regEmail.test( value ) );
}, "请填写正确的邮箱！");


// 只能输入英文-不允许为空
jQuery.validator.addMethod("englishOnly", function(value, element) {
    var regEng = /^([a-zA-Z]+)$/;
    return this.optional(element) || (regEng.test(value));
}, "请输入英文字母！");

// 只能输入英文-允许为空
jQuery.validator.addMethod("englishOnlyCanEmpty", function(value, element) {
    var regEng = /(^$)|^([a-zA-Z]+)$/;
    return this.optional(element) || (regEng.test(value));
}, "请输入英文字母！");

// 只能输入正整数
jQuery.validator.addMethod("positiveInteger", function(value, element) {
    var regEng = /^[1-9]\d*$/;
    return this.optional(element) || (regEng.test(value));
}, "请输入正整数！");

// 只能输入非负浮点数
jQuery.validator.addMethod("nonnegativeFloat", function(value, element) {
    var regEng = /^\d+(\.\d+)?$/;
    return this.optional(element) || (regEng.test(value));
}, "请输入非负浮点数！");
