// $(function () {
//     $('#settle1 option').eq(1).attr('selected', true);
//     $('#settle2 option').eq(1).attr('selected', true);
//     var orderNo = $('#order').html();
//     $.ajax({
//         url: '/admin/order/tab_order_ajax',
//         data: {
//             orderId: orderNo
//         },
//         success: function (data) {
//             if (data.data.status == 2) {
//                 $('#pass').click(function () {
//                     $.ajax({
//                         url: '/admin/order/confirm_audit_ajax',
//                         data: {
//                             orderId: orderNo
//                         },
//                         success: function (data) {
//                             bootbox.alert('审核成功');
//                             location.href ='/admin/order/list';
//                         }
//                     })
//                 });
//             }
//             if (data.data.status == 4) {
//                 $('#pass').val('签订合同');
//                 $('input[type="radio"]').parents('.radio').addClass('disabled');
//                 var selectList = $('select');
//                 $.each(selectList, function (k, v) {
//                     v.disabled = true
//                 });
//                 $('#note')[0].disabled = true;
//                 var inputList = $('input[type="text"]');
//
//                 $.each(inputList, function (k, v) {
//                     v.disabled = true
//                 });
//
//                 $('#pass').on('click', function () {
//
//                     $.ajax({
//                         url: '/admin/order/confirm_contract_ajax',
//                         data: {
//                             orderId: orderNo
//                         },
//                         success: function (data) {
//                             bootbox.alert('上传成功');
//                             location.href ='/admin/order/list'
//                         }
//                     })
//                 })
//             }
//             ;
//             if (data.data.status == 5) {
//                 $('#pass').val('确认收款');
//                 $('input[type="radio"]').parents('.radio').addClass('disabled');
//                 var selectList = $('select');
//                 $.each(selectList, function (k, v) {
//                     v.disabled = true
//                 });
//                 $('#note')[0].disabled = true;
//                 var inputList = $('input[type="text"]');
//
//                 $.each(inputList, function (k, v) {
//                     v.disabled = true
//                 });
//
//                 // $('#settle1')[0].disabled = false;
//                 // $('#settle2')[0].disabled = false;
//                 $('#pass').on('click',function () {
//                     $.ajax({
//                         url: '/admin/order/confirm_pay_ajax',
//                         data: {
//                             orderId: orderNo
//                         },
//                         success: function (data) {
//                             if (data.code == 200) {
//                                 bootbox.alert('确认收款成功');
//                             }
//                         }
//                     })
//                 })
//
//             };
//             if (data.data.status ==9) {
//                 $('#pass').val('订单结算');
//                 var selectList = $('select');
//                 $.each(selectList, function (k, v) {
//                     v.disabled = true
//                 });
//                 $('#note')[0].disabled = true;
//                 var inputList = $('input[type="text"]');
//
//                 $.each(inputList, function (k, v) {
//                     v.disabled = true
//                 });
//
//                 // $('#settle1')[0].disabled = false;
//                 // $('#settle2')[0].disabled = false;
//                 $('#pass').on('click',function () {
//
//                     $.ajax({
//                         url: '/admin/order/finish_pay_ajax',
//                         data: {
//                             orderId: orderNo
//                         },
//                         type:'post',
//                         success: function (data) {
//                             if (data.code == 200) {
//                                 bootbox.alert('订单结算成功');
//                             }
//                         }
//                     })
//                 })
//
//             };
//         }
//     })
//
//
// })