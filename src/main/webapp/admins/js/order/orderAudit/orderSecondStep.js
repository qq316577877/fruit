
var type;

/**
 * 进口下单第二步
 * Created by 杨隆
 */

// var type;   // 订单类型;
$(function () {
 var interests;  // 利息
 var service;    // 服务费
 var test = /[0-9]*[1-9][0-9]*$/;

 //获取利率
    //获取月利率
    $.ajax({
        url:'/admin/user/loan/get_loan_info_interest_rate_ajax',
        type:'post',
        success: function (data) {
            var monthInterestRate =data.data.monthInterestRate;
            monthInterestRate=monthInterestRate*100;
            $('#monthRet').html(monthInterestRate+'%');
        }
    });

 // 隐藏
 $('.loan-table').css('display', 'none');
 $('.attention').css('display', 'none');

 //贷款计算器
 $('#loan-cal').click(function () {
     $('.mask').css('display', 'block');
     $('#inform_2').css('display', 'block');
     $('#inform2').css('display', 'none');
     $('#canOrder').css('display', 'none');
     $('#inform_2 .inform-body h4').css('display', 'none');
     $('#inform_2 .loan-details').css('display', 'none');
 })

 // 借多少
 $('#money_loan').blur(function () {
     if (!test.test($(this).val())) {
         $('#empty').css('display', 'block')
     } else {
         $('#empty').css('display', 'none')
     }
 })

 // 借多少天
 $('#money-day').blur(function () {
     if (!test.test($(this).val())) {
         $('#wrong-day').css('display', 'block')
     } else {
         $('#wrong-day').css('display', 'none')
     }
 });

 //计算
 $('#btn_1').click(function () {

     if($('#money_loan').val()==""||$('#money-day').val()==""){
         bootbox.alert('请输入有效的金额和天数');
         return;
     }

     if ($('#empty').css('display') == 'block' || $('#money-day').css('display') == 'block') {
         bootbox.alert('请输入正确的格式');
     } else {
         $('#inform_2 .inform-body h4').css('display','block');
         $('#inform_2 .loan-details').css('display','block');
         var ret = (parseInt($('#money-day').val())) / 30;
         var money_1 = $('#money_loan').val();
         interests = ret * 0.01 * (parseInt($('#money_loan').val()));
         //console.log(interests.toFixed(2));
         service = (parseInt($('#money_loan').val())) * 0.001;
         var tag = ' <div>到期还本金：' + money_1 + '元</div>' +
             '<div>借满'+$('#money-day').val()+'天总利息：' + interests.toFixed(2) + '元</div>' +
             '<div>借款服务费：' + service + '元</div>';
         $('.loan-details').html(tag)
     }
 })

 //重置计算器
 $('#btn_2').click(function () {
     $('#money_loan').val('');
     $('#money-day').val('');
     var tag = '<div>到期还本金</div>' +
         '<div>借满天总利息</div>' +
         '<div>借款服务费</div>';
     $('.loan-details').html(tag)



 })

 // 关闭计算器
 $('#cancel_1').click(function () {
     $(this).parents('.inform-head').parents('#inform_2').css('display', 'none');
     $('.mask').css('display', 'none')
 });

 //选中陆运后隐藏贸易方式
 var logi = $('.logistics_2')
 $('.logistics_2').on('change', function () {
     if (logi[0].children[0].checked) {
         $('.trade').css('display', 'block')
     } else {
         $('.trade').css('display', 'none')
     }
 })

 //提交照片
 $('.main').on('click', '#last_step', function () {
     location.href = '/order/create';
 })
})



