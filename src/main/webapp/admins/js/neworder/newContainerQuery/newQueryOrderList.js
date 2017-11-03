/**
 * 货柜列表
 * Created by 杨隆
 */

// $(function () {
//     var INIT_PAGE_NO = 1;//初始化pageNo
//     var INIT_PAGE_SIZE = 5;//初始化pageSize
//     var INIT_ORDER_STATUS = 0;//初始化订单状态
//     var orderStatus=INIT_ORDER_STATUS;// 订单状态 默认0
//     var selectDateData={}; //日期查询对象
//
//
//     //页面初始化样式
//     $('#login_description').html('我的订单');//login头部定义
//     $('#orderList').html('');
//     $('.query-head li:nth-of-type(1)').addClass('tabCurrent').end().removeClass('current');
//     $('.query-head li:nth-of-type(1)').removeClass('current');
//
//     //初始化列表数据
//     getOrderList(null,null,null,null,orderStatus);
//
//     //显示全部订单
//     $('.main-r').on('click','#allOr', function () {
//         location.href='/admin/order/list';
//     });
//
//     //给点击事件添加禁用
//     if($('.pageDiv span:nth-of-type(1)').hasClass('disabled')){
//         $('#btn-next').addClass('pageDisable');
//         $('#btn-next').attr('disabled','disabled');
//     }
//
//     //下一页点击事件
//     $('#btn-next').click(function () {
//         $('.pageDiv .nextPage').click()
//     });
//
//
//
//     //上一页点击事件
//     $('#btn-last').click(function () {
//
//         $('.pageDiv .prevPage').click()
//     });
//
//     //查询--输入订单号查询
//     $('#orderSea').click(function () {
//         Loading.open();
//         queryOrderListByOrderNo();
//     });
//
//     //按照日期查询--查询今天
//     $('#today').click(function () {
//         // 清空订单号
//         $('#orderSearch').val('');
//         Loading.open();
//         $(this).addClass('selected').siblings().removeClass('selected');
//         var beginTime=moment().format('YYYY-MM-DD');
//         var endTime=moment().format('YYYY-MM-DD');
//         queryOrderListByTimes(beginTime,endTime);
//     });
//
//     //按照日期查询--查询本周
//     $('#toweek').click(function () {
//         // 清空订单号
//         $('#orderSearch').val('');
//         Loading.open();
//         $(this).addClass('selected').siblings().removeClass('selected');
//         var begin = moment().startOf('week').isoWeekday(1);
//         var beginTime=begin.add('d',7).format('YYYY-MM-DD');
//         var endTime=begin.add('d',6).format('YYYY-MM-DD');
//         queryOrderListByTimes(beginTime,endTime);
//     });
//
//     //本月
//     $('#tomonth').click(function () {
//         // 清空订单号
//         $('#orderSearch').val('');
//         Loading.open();
//         $(this).addClass('selected').siblings().removeClass('selected');
//         var date = new Date(), y = date.getFullYear(), m = date.getMonth();
//         var firstDay = new Date(y, m, 1);
//         var lastDay = new Date(y, m + 1, 0);
//         var beginTime = moment(firstDay).format('YYYY-MM-DD');
//         var endTime = moment(lastDay).format('YYYY-MM-DD');
//         queryOrderListByTimes(beginTime,endTime);
//     });
//
//     //三个月前
//     $('#threeMonth').click(function () {
//         // 清空订单号
//         $('#orderSearch').val('');
//         Loading.open();
//         $(this).addClass('selected').siblings().removeClass('selected');
//         var endTime=moment().format('YYYY-MM-DD');
//         var beginTime=moment().add('d',-90).format('YYYY-MM-DD');
//         queryOrderListByTimes(beginTime,endTime);
//     });
//
//     //日期自定义查询
//     $('#day_3').click(function () {
//         // 清空订单号
//         $('#orderSearch').val('');
//         var beginTime = $('#day_1').val();
//         var endTime = $('#day_2').val();
//         if(endTime!=""&&beginTime!="") {
//             if (endTime >= beginTime) {
//                 queryOrderListByTimes(beginTime, endTime);
//             } else {
//                 bootbox.alert('日期输入有误');
//             }
//         }else{
//             queryOrderListByTimes(beginTime, endTime);
//         }
//     });
//
//     // tab状态切换
//     $(".query-head").on("click","li",function () {
//
//         // 清空订单号和时间
//         $('#orderSearch').val('');
//         // 清除class
//         $(".filtrate-day ul li").removeClass();
//         $("#day_1").val('');
//         $("#day_2").val('');
//
//         var thisTab = $(this);
//         queryOrderListByChangeTab(thisTab);
//     });
//
//     /**
//      * 页面初始化列表
//      * @param pageIndex
//      * @param beginTime
//      * @param filter2
//      * @param pageSize
//      */
//     function getOrderList(pageNo,pageSize,beginTime,endTime,status) {
//         if(!pageNo) {
//             pageNo = INIT_PAGE_NO;
//         }
//
//         if(!pageSize) {
//             pageSize = INIT_PAGE_SIZE;
//         }
//
//         if(!status){
//             status = INIT_ORDER_STATUS;
//         }
//
//         var selectData = {
//             beginTime: beginTime,
//             endTime: endTime,
//             pageNo: pageNo,
//             pageSize: pageSize,
//             status: status,
//         };
//         var selectDataJsonStr =JSON.stringify(selectData);
//
//         queryOrderListAjax(selectDataJsonStr,true);
//     };
//
//
//     /**
//      * 获取当前日期
//      * @returns {string}
//      */
//     function getNowFormatDate() {
//         var date = new Date();
//         var seperator1 = "-";
//         var year = date.getFullYear();
//         var month = date.getMonth() + 1;
//         var strDate = date.getDate();
//         if (month >= 1 && month <= 9) {
//             month = "0" + month;
//         }
//         if (strDate >= 0 && strDate <= 9) {
//             strDate = "0" + strDate;
//         }
//         var currentdate = year + seperator1 + month + seperator1 + strDate;
//         return currentdate;
//     }
//
//
//     /**
//      *
//      * @param selectData 查询
//      * @param flushPageDiv 是否刷新page控件
//      */
//     function queryOrderListAjax(selectData,flushPageDiv){
//         // selectDateData=selectData;
//         $.post('/admin/order/find_order_byPage_ajax',selectData, function(data) {
//             var listObj = null;
//             var totalRecords = 0;
//             var totalPages = 0;
//             var pageNo = INIT_PAGE_NO;
//             var pageSize = INIT_PAGE_SIZE;
//             if(data.code == 200) {
//                 var listData = data.data;
//                 if(listData){
//                     listObj = listData.records;
//                     totalRecords = listData.totalCount;
//                     totalPages = listData.totalPage;
//                     pageNo = listData.pageNo;
//                     pageSize = listData.pageSize;
//                 }
//
//                 $("#orderList").empty();
//                 if(listObj && listObj.length>0 && totalRecords>0){
//                     Loading.close(1);
//                     createOrderDivs('orderList', listObj);
//                     $('.noData').css('display','none');
//                     $('#orderList').css('display','block');
//                     $('.pageDiv').css('display','block');
//                 }else{
//                     Loading.close(1);
//                     $("#orderList").empty();
//                     $('.pageDiv').css('display','none');
//                     $('.noData').css('display','block');
//
//                 }
//
//                 if(flushPageDiv){
//                     $(".pageDiv").createPage({
//                         pageCount: totalPages ,
//                         current: pageNo,
//                         turndown: 'false',
//                         backFn: function (currentPageNo) {
//                              getOrderList(currentPageNo,pageSize,selectDateData.beginTime, selectDateData.endTime,orderStatus);
//                         }
//                     });
//                 }
//             }
//         })
//     }
//
//     /**
//      * 通过订单号查询订单列表
//      */
//     function queryOrderListByOrderNo(){
//         var orderId = $('#orderSearch').val();
//         if(orderId==''){
//             getOrderList(null,null,null,null,orderStatus);
//         }else{
//             // 清空查询时间
//             $(".filtrate-day ul li").removeClass();
//             $("#day_1").val('');
//             $("#day_2").val('');
//
//             $('.noData').css('display', 'none');
//             var selectData = {
//                 orderId: orderId,
//                 pageNo: INIT_PAGE_NO,
//                 pageSize: INIT_PAGE_SIZE,
//                 status: orderStatus
//             };
//             var selectDataJsonStr = JSON.stringify(selectData);
//             queryOrderListAjax(selectDataJsonStr,true);
//         }
//     }
//
//     /**
//      * 通过 时间 查询订单列表
//      */
//     function queryOrderListByTimes(beginTime,endTime){
//         $('.noData').css('display', 'none');
//         $('#orderList').html('');
//         Loading.open();
//         var selectData = {
//             beginTime: beginTime,
//             endTime: endTime,
//             pageNo: INIT_PAGE_NO,
//             pageSize: INIT_PAGE_SIZE,
//             status: orderStatus
//         };
//         selectDateData=selectData;
//         var selectDataJsonStr =JSON.stringify(selectData);
//         queryOrderListAjax(selectDataJsonStr,true);
//     }
//
//     /**
//      * tab 切换 查询订单列表
//      */
//     function queryOrderListByChangeTab(thisTab){
//         // 清除class
//         $(".query-head li").removeClass();
//         // 添加class
//         thisTab.addClass("tabCurrent");
//
//         var tabIndex=thisTab.index();
//         switch (tabIndex){
//             // 全部
//             case 0:
//                 orderStatus=0;
//                 break;
//             // 待审核
//             case 1:
//                 orderStatus=2;
//                 break;
//             // 待提交
//             case 2:
//                 orderStatus=3;
//                 break;
//             // 待付款：
//             case 3:
//                 orderStatus=5;
//                 break;
//             // 待发货???
//             case 4:
//                 orderStatus=6;
//                 break;
//             // 待收货：
//             case 5:
//                 orderStatus=9;
//                 break;
//             // 完成：
//             case 6:
//                 orderStatus=100;
//                 break;
//         }
//     Loading.open(1);
//         // 刷新列表
//         getOrderList(null,null,null,null,orderStatus);
//     }
// })

$(function () {
   $('#orderList').on('click','.logisticsDetails',function () {
       self.location='/admin/newOrder/containerLogistics'
   });


    /*
    * 获取分页数据
    * */
    // $.ajax({
    //     url:'/admins/js/neworder/newContainerQuery/container_find_page.json',
    //     type:'get',
    //     // datatype:'json',
    //     success:function (response) {
    //       var order =JSON.parse(response).data.records;
    //         createOrderDivs('orderList',order)
    //     }
    // })

        var INIT_PAGE_NO = 1;//初始化pageNo
    var INIT_PAGE_SIZE = 10;//初始化pageSize
    var INIT_ORDER_STATUS = 0;//初始化订单状态
    var orderStatus=INIT_ORDER_STATUS;// 订单状态 默认0
    var selectDateData={}; //日期查询对象


    //初始化列表数据
    getOrderList(null,null,null,null,orderStatus);

    //显示全部订单
    $('.main-r').on('click','#allOr', function () {
        location.href='/admin/order/list';
    });

    //给点击事件添加禁用
    if($('.pageDiv span:nth-of-type(1)').hasClass('disabled')){
        $('#btn-next').addClass('pageDisable');
        $('#btn-next').attr('disabled','disabled');
    }

    //下一页点击事件
    $('#btn-next').click(function () {
        $('.pageDiv .nextPage').click()
    });



    //上一页点击事件
    $('#btn-last').click(function () {

        $('.pageDiv .prevPage').click()
    });

    //查询--输入订单号查询
    $('#orderSea').click(function () {
        Loading.open();
        queryOrderListByOrderNo();
    });

    //按照日期查询--查询今天
    $('#today').click(function () {
        // 清空订单号
        $('#orderSearch').val('');
        Loading.open();
        $(this).addClass('selected').siblings().removeClass('selected');
        var beginTime=moment().format('YYYY-MM-DD');
        var endTime=moment().format('YYYY-MM-DD');
        queryOrderListByTimes(beginTime,endTime);
    });

    //按照日期查询--查询本周
    $('#toweek').click(function () {
        // 清空订单号
        $('#orderSearch').val('');
        Loading.open();
        $(this).addClass('selected').siblings().removeClass('selected');
        var begin = moment().startOf('week').isoWeekday(1);
        var beginTime=begin.add('d',7).format('YYYY-MM-DD');
        var endTime=begin.add('d',6).format('YYYY-MM-DD');
        queryOrderListByTimes(beginTime,endTime);
    });

    //本月
    $('#tomonth').click(function () {
        // 清空订单号
        $('#orderSearch').val('');
        Loading.open();
        $(this).addClass('selected').siblings().removeClass('selected');
        var date = new Date(), y = date.getFullYear(), m = date.getMonth();
        var firstDay = new Date(y, m, 1);
        var lastDay = new Date(y, m + 1, 0);
        var beginTime = moment(firstDay).format('YYYY-MM-DD');
        var endTime = moment(lastDay).format('YYYY-MM-DD');
        queryOrderListByTimes(beginTime,endTime);
    });

    //三个月前
    $('#threeMonth').click(function () {
        // 清空订单号
        $('#orderSearch').val('');
        Loading.open();
        $(this).addClass('selected').siblings().removeClass('selected');
        var endTime=moment().format('YYYY-MM-DD');
        var beginTime=moment().add('d',-90).format('YYYY-MM-DD');
        queryOrderListByTimes(beginTime,endTime);
    });

    //日期自定义查询
    $('#day_3').click(function () {
        // 清空订单号
        $('#orderSearch').val('');
        var beginTime = $('#day_1').val();
        var endTime = $('#day_2').val();
        if(endTime!=""&&beginTime!="") {
            if (endTime >= beginTime) {
                queryOrderListByTimes(beginTime, endTime);
            } else {
                bootbox.alert('日期输入有误');
            }
        }else{
            queryOrderListByTimes(beginTime, endTime);
        }
    });

    // tab状态切换
    $(".query-head").on("click","li",function () {

        // 清空订单号和时间
        $('#orderSearch').val('');
        // 清除class
        $(".filtrate-day ul li").removeClass();
        $("#day_1").val('');
        $("#day_2").val('');

        var thisTab = $(this);
        queryOrderListByChangeTab(thisTab);
    });

    /**
     * 页面初始化列表
     * @param pageIndex
     * @param beginTime
     * @param filter2
     * @param pageSize
     */
    function getOrderList(pageNo,pageSize,beginTime,endTime,status) {
        if(!pageNo) {
            pageNo = INIT_PAGE_NO;
        }

        if(!pageSize) {
            pageSize = INIT_PAGE_SIZE;
        }

        if(!status){
            status = INIT_ORDER_STATUS;
        }

        var selectData = {
            beginTime: beginTime,
            endTime: endTime,
            pageNo: pageNo,
            pageSize: pageSize,
            status: status,
        };
        var selectDataJsonStr =JSON.stringify(selectData);

        queryOrderListAjax(selectDataJsonStr,true);
    };


    /**
     * 获取当前日期
     * @returns {string}
     */
    function getNowFormatDate() {
        var date = new Date();
        var seperator1 = "-";
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        var currentdate = year + seperator1 + month + seperator1 + strDate;
        return currentdate;
    }


    /**
     *
     * @param selectData 查询
     * @param flushPageDiv 是否刷新page控件
     */
    function queryOrderListAjax(selectData,flushPageDiv){
        // selectDateData=selectData;
        // $.post('/admins/js/neworder/newContainerQuery/container_find_page.json',selectData, function(data) {
        $.post('/admin/neworder/center/container/find_container_byPage_ajax',selectData, function(data) {
            var listObj = null;
            var totalRecords = 0;
            var totalPages = 0;
            var pageNo = INIT_PAGE_NO;
            var pageSize = INIT_PAGE_SIZE;
            // data=JSON.parse(data);//删除
            if(data.code == 200) {
                var listData = data.data;
                if(listData){
                    listObj = listData.records;
                    totalRecords = listData.totalCount;
                    totalPages = listData.totalPage;
                    pageNo = listData.pageNo;
                    pageSize = listData.pageSize;
                }

                $("#orderList").empty();
                if(listObj && listObj.length>0 && totalRecords>0){
                    Loading.close(1);
                    createOrderDivs('orderList', listObj);
                    $('.noData').css('display','none');
                    $('#orderList').css('display','block');
                    $('.pageDiv').css('display','block');
                }else{
                    Loading.close(1);
                    $("#orderList").empty();
                    $('.pageDiv').css('display','none');
                    $('.noData').css('display','block');

                }

                if(flushPageDiv){
                    $(".pageDiv").createPage({
                        pageCount: totalPages ,
                        current: pageNo,
                        turndown: 'false',
                        backFn: function (currentPageNo) {
                             getOrderList(currentPageNo,pageSize,selectDateData.beginTime, selectDateData.endTime,orderStatus);
                        }
                    });
                }
            }
        })
    }

    /**
     * 通过订单号查询订单列表
     */
    function queryOrderListByOrderNo(){
        var orderId = $('#orderSearch').val();
        if(orderId==''){
            getOrderList(null,null,null,null,orderStatus);
        }else{
            // 清空查询时间
            $(".filtrate-day ul li").removeClass();
            $("#day_1").val('');
            $("#day_2").val('');

            $('.noData').css('display', 'none');
            var selectData = {
                containerNo: orderId,
                pageNo: INIT_PAGE_NO,
                pageSize: INIT_PAGE_SIZE,
                status: orderStatus
            };
            var selectDataJsonStr = JSON.stringify(selectData);
            queryOrderListAjax(selectDataJsonStr,true);
        }
    }

    /**
     * 通过 时间 查询订单列表
     */
    function queryOrderListByTimes(beginTime,endTime){
        $('.noData').css('display', 'none');
        $('#orderList').html('');
        Loading.open();
        var selectData = {
            beginTime: beginTime,
            endTime: endTime,
            pageNo: INIT_PAGE_NO,
            pageSize: INIT_PAGE_SIZE,
            status: orderStatus
        };
        selectDateData=selectData;
        var selectDataJsonStr =JSON.stringify(selectData);
        queryOrderListAjax(selectDataJsonStr,true);
    }

    /**
     * tab 切换 查询订单列表
     */
    function queryOrderListByChangeTab(thisTab){
        // 清除class
        $(".query-head li").removeClass();
        // 添加class
        thisTab.addClass("tabCurrent");

        var tabIndex=thisTab.index();
        switch (tabIndex){
            // 全部
            case 0:
                orderStatus=0;
                break;
            // 已提交,待发货
            case 1:
                orderStatus=1;
                break;
            // 已经发货
            case 2:
                orderStatus=2;
                break;
            // 已放款：
            // case 3:
            //     orderStatus=3;
            //     break;
            // 已清关???
            case 3:
                orderStatus=4;
                break;
            // 待收货：
            case 4:
                orderStatus=5;
                break;
            // 平台处理：
            case 5:
                orderStatus=6;
                break;
            // 已收货：
            case 6:
                orderStatus=7;
                break;
        }
    Loading.open(1);
        // 刷新列表
        getOrderList(null,null,null,null,orderStatus);
    }

    /*
    * 格式化金额
    * */
    function toThousands(num) {
        return  num.replace(/[^\d]/g,"")
                .replace (/(\d{1,3})(?=(\d{3})+(\.\d*)?$)/g, '$1，');
    }
});