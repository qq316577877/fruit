$(function () {

    $('#orderList').on('click','.containerDetails',function () {
        var orderNo =$(this).parents('.containerBody').siblings('.containerHeader').find('.orderNo').html().split('：')[1];
        self.location = '/admin/newOrder/orderAudit?orderNo='+orderNo;
        window.localStorage.setItem('containerClick',2);
    })
    $('#orderList').on('click','.logisticsContainer',function () {
        var orderNo =$(this).parents('.containerBody').siblings('.containerHeader').find('.orderNo').html().split('：')[1];
        self.location = '/admin/newOrder/orderAudit?orderNo='+orderNo;
        window.localStorage.setItem('logisticsContainer',4);
    })
})