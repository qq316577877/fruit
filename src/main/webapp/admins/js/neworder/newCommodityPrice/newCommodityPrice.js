/**
 * 商品管理中心
 * Created by yl on 2017/9/21.
 */
$(function () {
    var flag =true;
    // 价格校验
    var isPriceVerify=true;

    //更改成交价
    $('#productCommodityList').on('blur', '.priceActual input', function () {
        var lowPrice = $(this).parents('.priceActual').siblings('.priceLow').children('input').val();
        var heighPrice = $(this).parents('.priceActual').siblings('.priceHigh').children('input').val();
        //console.log("value:",$(this).val(),"-lowPrice：",lowPrice,"-heighPrice:",heighPrice);
        if ($(this).val() > parseInt(heighPrice) || $(this).val() < parseInt(lowPrice)) {
            $(this).parent().addClass("errorinput");
            bootbox.alert('超出价格区间');
            isPriceVerify=false;
            return;
        }else {
            $(this).parent().removeClass("errorinput");
            var errorlist=$(".errorinput");
            if(errorlist.length>0){
                isPriceVerify=false;
            }else{
                isPriceVerify=true;
            }
        }
        //var amount = $(this).parents('.priceActual').siblings('.amount').find('input').val();
        //$(this).parents('.priceActual').siblings('.priceAll').html($(this).val() * amount);
        //// 计算总金额
        //var amountLists = $('.priceAll');
        //var amountSum = 0;
        //$.each(amountLists, function (index, val) {
        //    amountSum+=parseFloat($('.priceAll').eq(index).html());
        //})
        //$("#totalPri").html(amountSum);
    });

    /*
    * 获取商品种类
    * */
    $.ajax({
        // url:'/admins/js/neworder/newCommodityPrice/getCategories.json',
        url:'/admin/neworder/goods/find_all_goods_categories',
        success:function (response) {
            // response=JSON.parse(response);
            var options;
            var goodsCategories =response.data.goodsCategories;
            $.each(goodsCategories,function (k, v) {
                options+='<option id="'+v.id+'">'+v.name+'</option>'
            });
            $('#selector').html(options);
            $('#selector option').eq(0).attr('selected','true')
        }
    });

    $.ajax({
        // url:'/admins/js/neworder/newCommodityPrice/goodsVarieties.json',
        url:'/admin/neworder/goods/find_goods_varieties_by_category',
        data:{
            categoryId:1
        },
        success:function (response) {
            // response=JSON.parse(response);
            var name =response.data.goodsVarieties[0].name;
            $('#productName').html(name)
        }
    })
    var date =moment().format('YYYY-MM-DD');
    $('#day_1').val(date);
    checkPrice(date);
    matching();

    /*
    * 查询
    * */
    $('#btn').click(function () {
        $('#body table').remove();
        if($('#day_1').val()==''){
            var date =moment().format('YYYY-MM-DD');
            checkPrice(date);
        }else{
            var date =$('#day_1').val();
            checkPrice(date)
        }
    })

    /*;
    * 编辑
    * */
    $('#audit').click(function () {
        if(flag){
            $('table input').removeAttr('disabled');
            $('#audit').val('保存');
            // alert('修改')
            flag=false
        }else{
            // alert(3333333)
            // 判断价格
            if (!isPriceVerify) {
                bootbox.alert('超出价格区间');
                return;
            }

            var quotationTime =$('#day_1').val();
            var data =JSON.stringify(getTable(quotationTime));

            $.ajax({
                url:'/admin/neworder/goods/update_commodities_price_everyday',
                data:data,
                type:'post',
                success:function (response) {
                    if(response.code==200){
                        bootbox.alert('编辑成功');
                        $('#audit').val('编辑');
                        $('table input').attr('disabled','true')
                        // $('#body table').remove();
                        // checkPrice()
                    }
                }
            })
            flag=true
        }
    });

    /*
    * 保存
    * */


    /*
    * 封装查询价格函数
    * */
    function checkPrice(date) {
        $.ajax({
            // url:'/admins/js/neworder/newCommodityPrice/commodity_find.json',
            url:'/admin/neworder/goods/find_goods_commodities_by_variety',
            data:{
                varietyId:1
            },
            success:function (response) {
                // response=JSON.parse(response); //删除
                if(response.code==200){
                  var list =response.data.goodsCommodityInfos;
                    if(list){
                        createOrderListTable($('#body')[0],list);
                        $('table input').attr('disabled','true')
                    }else{
                        bootbox.alert('暂无数据')
                    }
                }
            }
        })
    }

    /*
    * 定义匹配数量价格匹配正整数
    * */
    function matching() {
        var amountInput =$('table input');
        $('.commodityList').on('input','table input', function () {
            //this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1').
            //replace(".", "$#$").replace(/\./g, "").replace("$#$", ".").replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3');
            this.value = this.value.replace(/[^\d.]/g, "").replace(/(\..*)\./g, '$1').replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3');
        });
    }
});