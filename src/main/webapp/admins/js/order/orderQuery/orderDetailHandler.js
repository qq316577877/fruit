/**
 * 订单列表操作
 */

$(function () {

    var that;//提供给$(this)使用

    //提交审核--审核订单跳转
    $('.main-r').on('click','.submitAudit',function () {
        var orderId =$(this).parents('.order-ctrl').parents('.list_1_body').siblings('.list_1_head').find('.orderNo').html();
        self.location='/admin/order/detail/first?orderId='+orderId;
    });

    //添加合同
    $('.main-r').on('click','.contract',function () {
        var orderId =$(this).parents('.order-ctrl').parents('.list_1_body').siblings('.list_1_head').find('.orderNo').html();
        self.location='/admin/order/detail/first?orderId='+orderId;
    });

    //取消订单
    $('#orderList').delegate('.cancelOrder','click',function (){
        that =$(this);
        var orderNo =$(this).parents('.order-ctrl').parents('.list_1_body').siblings('.list_1_head').find('.orderNo').html();
        $('.mask').css('display','block');
        $('#logistics').css('display','none');
        $('#inform-cancle').css('display','block');
    });

    //取消订单-取消
    $('.mask').on('click','#btn-cancle-cancle',function () {
        $('.mask').css('display','none');
        $('#inform-cancle').css('display','none');
    });

    //取消订单--确认
    $('.mask').on('click','#btn-cancle-confirm',function () {
        $('.mask').css('display','none');
        $('#inform-cancle').css('display','none');
        var orderNo =$(that).parents('.order-ctrl').parents('.list_1_body').siblings('.list_1_head').find('.orderNo').html();
        $.ajax({
            url:'/admin/order/order_cancle_ajax',
            type:'post',
            data:{
                orderId:orderNo
            },
            success: function (data) {
                if(data.code==200){
                    $(that).parents('.order-ctrl').parents('.list_1_body').parents('.list_1').remove();
                    history.go(0);
                }
            }
        })
    });


    //确认收款
    $('.main-r').on('click','.comfirmPay',function () {
        var orderId =$(this).parents('.order-ctrl').parents('.list_1_body').siblings('.list_1_head').find('.orderNo').html();
        self.location='/admin/order/detail/first?orderId='+orderId;
    });

    //订单详情
    $('.main-r').on('click','.orderDetails',function () {
        var orderId =$(this).parents('.order-details').parents('.list_1_body').siblings('.list_1_head').find('.orderNo').html();
        self.location='/admin/order/detail/first?orderId='+orderId;
    });
    //订单详情
    $('.main-r').on('click','.stagingOrder',function () {
        var orderId =$(this).parents('.order-details').parents('.list_1_body').siblings('.list_1_head').find('.orderNo').html();
        self.location='/admin/order/detail/first?orderId='+orderId;
    });

    //添加投保
    $('.main-r').on('click','.insured',function () {
        var orderId =$(this).parents('.order-ctrl').parents('.list_1_body').siblings('.list_1_head').find('.orderNo').html();
        that =$(this);
        $('.mask').css('display','block');
        $('#inform-insured').css('display','block');
        var list =$('#inform-insured .orderNo');
        $.each(list,function (k, v) {
            v.outerHTML='';
        })
        $.ajax({
            url:'/admin/order/simple_query_container_ajax',
            data:{
                orderId:orderId
            },
            success:function (data) {
                var list =data.data;
                $.each(list,function (k, v) {
                    var contractNumber =v.contractNumber;
                    if(contractNumber==null){
                        contractNumber=''
                    }
                    var tag =' <div class="orderNo">'+
                                '<span transactionNo="'+v.transactionNo+'">'+v.containerId+'</span>'+
                               ' <input class="contractNumber" value="'+contractNumber+'" type="text">'+
                                '</div>';
                    $('#inform-insured .can-btn').before(tag);
                })
            }
        })
    });


    //添加投保--取消
    $('.mask').on('click','#btn-insured-cancle',function () {
        $('.mask').css('display','none');
        $('#inform-insured').css('display','none');
    });


    //添加投保--确认
    $('.mask').on('click','#btn-insured-confirm',function () {
        //判断投保保单长度
        var flag=true;
        var contractNumbers =$('.contractNumber');
        $.each(contractNumbers,function (k, v) {
            if(v.value.length>64){
                bootbox.alert('您输入的保险合同号过长（0-64位），请确认后再输入。');
                flag=false;
                return
            }else if (v.value==''){
                bootbox.alert('保险合同号不能为空。');
                flag=false;
                return
            }
        })

       if(flag){
           var list =[];
           var orderLists =$('#inform-insured .orderNo');

           for(var i =0;i<orderLists.length;i++){
               var order =orderLists[i];

               var transactionno =order.children[0].getAttribute('transactionno');
               var contactNo =order.children[1].value;
               var details={
                   transactionno:transactionno,
                   contractNumber:contactNo
               };
               list.push(details);
           };

           list =JSON.stringify(list);
           $.ajax({
               url:'/admin/order/add_insurance_ajax',
               data:list,
               type:'post',
               success:function (data) {
                   if(data.code==200){
                       bootbox.alert('添加成功');
                       $('.mask').css('display','none');
                       // $(that).html('保险信息');
                       $(that).removeClass('insured');
                       $(that).addClass('insuredInfo');
                   }
               }
           })
       }
    });

    //添加物流信息
    $('.main-r').on('click','.delivery ',function () {
        var orderId =$(this).parents('.order-ctrl').parents('.list_1_body').siblings('.list_1_head').find('.orderNo').html();
        self.location='/admin/order/logistics/show?orderId='+orderId;
    });




    //暂存订单添加备注
    $('.main-r').on('click','.audit ',function () {
        var orderId =$(this).parents('.order-ctrl').parents('.list_1_body').siblings('.list_1_head').find('.orderNo').html();
        $('#inform-remark').css('display','block');
        $('.mask').css('display','block');
        $.ajax({
            url:'/admin/order/query_order_ajax',
            data:{
                orderId:orderId
            },
            success:function (data) {
                var info =data.data.backMemo;
                $('#inform-remark textarea').val(info)
                $('#inform-remark h2').html(data.data.no);
            }
        })
    });

    //添加备注信息-取消
    $('.mask').on('click','#btn-remark-cancle',function () {
        $('.mask').css('display','none');
        $('#inform-remark').css('display','none');
    });

    //添加备注信息-确认
    $('.mask').on('click','#btn-remark-confirm',function () {
        var orderId =$('#inform-remark h2').html();
        var backMemo =$('#inform-remark textarea').val();

        // 校验备注
        if(backMemo.length==0){
            bootbox.alert('请输入备注信息');
            return;
        }

        $.ajax({
            url:'/admin/order/memo_add_ajax',
            data:{
                orderId:orderId,
                backMemo:backMemo
            },
            type:'post',
            success:function (data) {
                if(data.code==200){
                    bootbox.alert('添加成功');
                    $('.mask').css('display','none');
                    $('#inform-remark').css('display','none');
                }else {
                    bootbox.alert('字数过多');
                }
            }
        })
    });

    //查看保险信息
    $('.main-r').on('click','.insuredInfo ',function () {
        var orderId =$(this).parents('.order-ctrl').parents('.list_1_body').siblings('.list_1_head').find('.orderNo').html();
        $('.mask').css('display','block');
        $('#inform-insured').css('display','block');
        var list =$('#inform-insured .orderNo');
        $.each(list,function (k, v) {
            v.outerHTML='';
        })
        $.ajax({
            url:'/admin/order/simple_query_container_ajax',
            data:{
                orderId:orderId
            },
            success:function (data) {
                var list =data.data;
                $.each(list,function (k, v) {
                    var contractNumber =v.contractNumber;
                    if(contractNumber ==null){
                        console.log(111);
                          var tag =' <div class="orderNo">'+
                              '<span transactionNo="'+v.transactionNo+'">'+v.containerId+'</span>'+
                              ' <input class="contractNumber"  value="" type="text">'+
                              '</div>';
                        $('#inform-insured .can-btn').before(tag)
                    }else{
                        console.log(22);
                        var tag =' <div class="orderNo">'+
                            '<span transactionNo="'+v.transactionNo+'">'+v.containerId+'</span>'+
                            ' <input  class="contractNumber"  value="'+contractNumber+'" type="text">'+
                            '</div>';
                        $('#inform-insured .can-btn').before(tag);
                    }

                })
            }
        })
    });

    //订单结算
    $('.main-r').on('click','.settlementOrder',function () {
        var orderId =$(this).parents('.order-ctrl').parents('.list_1_body').siblings('.list_1_head').find('.orderNo').html();
        self.location='/admin/order/detail/first?orderId='+orderId;
    });


})



