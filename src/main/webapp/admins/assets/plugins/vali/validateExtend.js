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

jQuery.validator.addMethod("phone", function(value, element) {
    var mobile = $("#mobile").val();// 手机号码
    var telephone = $("#telephone").val();// 固定电话
    var mobileRule = /^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0-9]|170)\d{8}$/;
    var telephoneRule = /^\d{3,4}-?\d{7,9}$/;

    // 都没填
    if (isEmpty(mobile) && isEmpty(telephone)) {
        //自定义错误提示
        $("#receivingMobile_tip").addClass("errorHint").text("请填写固定电话或手机号码");
        return false;
    }
    var mobilePass = false;
    var telephonePass = false;
    // 手机填了、固定电话没填
    if (!isEmpty(mobile) && isEmpty(telephone)) {
        if (!mobileRule.test(mobile)) {
            //自定义错误提示
            $("#receivingMobilePhone_tip").removeClass("successHint").addClass("errorHint").text("手机号码格式不对");
            return false;
        } else {
            mobilePass = true;
        }
    }

    // 手机没填、固定电话填了
    if (isEmpty(mobile) && !isEmpty(telephone)) {
        if (!telephoneRule.test(telephone)) {
            //自定义错误提示
            $("#receivingTelephone_tip").removeClass("successHint").addClass("errorHint").text("固定电话格式不对");
            return false;
        } else {
            telephonePass = true;
        }
    }

    if (mobilePass || telephonePass) {
        //自定义成功提示
        $("#receivingTelephone_tip").removeClass("errorHint").addClass("successHint").text('');
        return true;
    } else {
        return false;
    }
}, "ignore");

//密码
jQuery.validator.addMethod("isPassWord", function(value, element) {
    var regEng = /((?=.*\d)(?=.*\D)|(?=.*[a-zA-Z])(?=.*[^a-zA-Z]))^.{6,20}$/;
    return this.optional(element) || (regEng.test(value));
}, "请输入原密码");

//邮政编码
jQuery.validator.addMethod("isZipCode", function(value, element) {
    var regEng =  /^[0-9]{6,10}$/g;
    return this.optional(element) || (regEng.test(value));
}, "只能使用数字6-10位");


//详细地址
jQuery.validator.addMethod("isAddress", function(value, element) {
    var regEng =  /^[\u4e00-\u9fa5_a-zA-Z0-9]+$/g;
    return this.optional(element) || (regEng.test(value));
}, "只能输入汉字、字母、数字");

//供应商名称
jQuery.validator.addMethod("isSupplierName", function(value, element) {
    var regEng =  /^[\u4e00-\u9fa5_a-zA-Z0-9]+$/g;
    return this.optional(element) || (regEng.test(value));
}, "只能输入汉字、字母、数字");


//联系人姓名
jQuery.validator.addMethod("isReceiver", function(value, element) {
    var regEng =  /^[\u4e00-\u9fa5_a-zA-Z0-9]+$/g;
    return this.optional(element) || (regEng.test(value));
}, "只能输入汉字、字母、数字");


//座机号区号
jQuery.validator.addMethod("isArea", function(value, element) {
    var regEng =  /^(0[0-9]{3,4}$)/g;
    return this.optional(element) || (regEng.test(value));
}, "区号只能输入3-4位数字");

//座机号区号
jQuery.validator.addMethod("isPhoneNum", function(value, element) {
    var regEng =  /^[0-9]{8}$/;
    return this.optional(element) || (regEng.test(value));
}, "只能输入数字8位,选座机请填区号");

//匹配字数限制(供应商地址)
jQuery.validator.addMethod("limitSumAdd", function(value, element) {
    var regEng =  /^[\u4e00-\u9fa5_a-zA-Z0-9]{4,6}/g;
    return this.optional(element) || (regEng.test(value));
}, "详细地址为4-120个字符");

//匹配字数限制(供应商名称)
jQuery.validator.addMethod("limitSumName", function(value, element) {
    var regEng = /\S{2,120}/g;
    return this.optional(element) || (regEng.test(value));
}, "长度为2-120个字符");

//匹配字数限制(供应商-联系人姓名)
jQuery.validator.addMethod("limitSumNa", function(value, element) {
    var regEng =  /^[\u4e00-\u9fa5_a-zA-Z0-9]{2,25}$/g;
    return this.optional(element) || (regEng.test(value));
}, "长度为2-25个字符");

//匹配企业名称
jQuery.validator.addMethod("isEnterpriseName", function(value, element) {
    var regEng =  /^[\u4e00-\u9fa5_a-zA-Z0-9]+$/g;
    return this.optional(element) || (regEng.test(value));
}, "只能输入汉字、字母、数字");

//匹配营业执照号
jQuery.validator.addMethod("isCredential", function(value, element) {
    var regEng =  /^[a-zA-Z0-9]+$/g;
    return this.optional(element) || (regEng.test(value));
}, "只能输入字母、数字");

//匹配身份证
jQuery.validator.addMethod("isIdentity", function(value, element) {
    var regEng =  /^(\d{6})(\d{4})(\d{2})(\d{2})(\d{3})([0-9]|X)$/;
    return this.optional(element) || (regEng.test(value));
}, "请输入正确格式的身份证号码");

//匹配字数限制(企业名称)
jQuery.validator.addMethod("limitEnterpriseName", function(value, element) {
    var regEng = /^[\u4e00-\u9fa5_a-zA-Z0-9]{5,60}$/g;
    return this.optional(element) || (regEng.test(value));
}, "长度为5-60个字符");


//匹配字数限制(营业照)
jQuery.validator.addMethod("limitCredential", function(value, element) {
    var regEng1 =  /^[a-zA-Z0-9]{15}$/g;
    var regEng2 =  /^[a-zA-Z0-9]{18}$/g;
    return this.optional(element) || (regEng1.test(value)) ||(regEng2.test(value)) ;
}, "证照号为15位或18位");

//匹配银行名称B
jQuery.validator.addMethod("isAccountName", function(value, element) {
    var regEng = /^[\u4e00-\u9fa5]{2,25}$/g;
    return this.optional(element) || (regEng.test(value));
}, "长度为2-25个中文字符");

//匹配银行账号
jQuery.validator.addMethod("isBankCard", function(value, element) {
    var regEng =  /^[0-9]{16,21}$/;
    return this.optional(element) || (regEng.test(value));
}, "只能输入数字16-21位");
