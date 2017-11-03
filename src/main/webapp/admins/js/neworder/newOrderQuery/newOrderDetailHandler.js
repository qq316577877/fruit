/**
 * 订单列表操作
 */

//预付款
$(function () {
    $('.main-r').on('click','.Advance',function () {
        var orderNo =$(this).parents('td').siblings('.orderNo').html();
        $('#advinceNum').val('');
        $('#advinceText').val('');
        $('#listMask').show();
        $('#advince').show();
        $('#advince').attr('orderNo',orderNo);
    });

    $('#listMask').on('input', '#advinceNum',function () {
         this.value = this.value.replace(/[^\d.]/g, "").replace(/(\..*)\./g, '$1').replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3');
    });
    $('#listMask').on('input', '#prepaidNum',function () {
        this.value = this.value.replace(/[^\d.]/g, "").replace(/(\..*)\./g, '$1').replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3');
    });

    $('#listMask').on('click','#collectBtn',function () {
        if($('#advinceNum').val()==''){
            bootbox.alert('请输入预付款金额');
            return
        }
        var data ={
            orderNo:$('#advince').attr('orderNo'),
            prepaidAmount:$('#advinceNum').val(),
            detailInfo:$('#advinceText').val()
        };
        data=JSON.stringify(data);
        $.ajax({
            url:'/admin/neworder/center/prepaid_receive_ajax',
            type:'post',
            data:data,
            success:function (response) {
                if(response.code==200){
                    bootbox.alert('确认成功');
                    location=location
                }
            }
        })
    });

    //取消
    $('#listMask').on('click','#collectCancel',function () {
        $('#listMask').hide();
        $('#advince').hide();
    });

    //尾款
    $('.main-r').on('click','.prepaid',function () {
        var orderNo =$(this).parents('td').siblings('.orderNo').html();
        $('#prepaidNum').val();
        $('#prepaidText').val();
        $('#listMask').show();
        $('#prepaid').show();
        $('#prepaid').attr('orderNo',orderNo);
    });
    $('#listMask').on('click','#prepaidBtn',function () {
        if($('#prepaidNum').val()==''){
            bootbox.alert('请输入预付款金额');
            return
        }
        var data ={
            orderNo:$('#prepaid').attr('orderNo'),
            tailAmount:$('#prepaidNum').val(),
            detailInfo:$('#prepaidText').val()
        };
        data=JSON.stringify(data);
        $.ajax({
            url:'/admin/neworder/center/tail_receive_ajax',
            type:'post',
            data:data,
            success:function (response) {
                if(response.code==200){
                    bootbox.alert('确认成功');
                    location=location
                }
            }
        })
    });
    //取消
    $('#listMask').on('click','#prepaidCancel',function () {
        $('#listMask').hide();
        $('#prepaid').hide();
    });

    //取消订单
    $('.main-r').on('click','.cancelOrder',function () {
        var orderNo =$(this).parents('td').siblings('.orderNo').html();
        $('#listMask').show();
        $('#cancelPop').show();
        $('#cancelPop').attr('orderNo',orderNo);
    });
    $('#listMask').on('click','#insureBtn',function () {
        var orderNo =  $('#cancelPop').attr('orderNo');
        var data ={
            orderNo:orderNo
        };
        data=JSON.stringify(data);
        $.ajax({
            url:'/admin/neworder/center/order_cancle_ajax',
            type:'post',
            data:data,
            success:function (response) {
                if(response.code==200){
                    bootbox.alert('取消成功');
                    location=location;
                    $('#listMask').hide();
                    $('#cancelPop').hide();
                }
            }
        })
    });
    $('#listMask').on('click','#canBtnOrder',function () {
      $('#listMask').hide();
      $('#cancelPop').hide();
    })


    //发货处理
    $('.main-r').on('click','.sendShipping',function () {
        var orderNo =$(this).parents('td').siblings('.orderNo').html();
        self.location = '/admin/newOrder/orderAudit?orderNo='+orderNo;
        window.localStorage.setItem('sendShipping',2);
    });

    //订单详情

    $('#orderList').on('click','.orderDetailsLoock',function () {
        var orderNo =$(this).parents('td').siblings('.orderNo').html();
        // console.log(orderNo);
        self.location = '/admin/newOrder/orderAudit?orderNo='+orderNo;
        window.localStorage.setItem('orderDetails',1);
    })
})



