/**
 * Created by yl on 2017/9/29.
 */
$(function () {
    // 保险信息
    $("#insuranceTab").click(function () {
        tabClick(3);
        var data = {
            orderNo: $('#order').html()
        };
        data = JSON.stringify(data);
        $.ajax({
            url: '/admin/neworder/center/container/get_insurance_ajax',
            type: 'post',
            data: data,
            success: function (response) {
                var list = response.data;
                if (list.length == 0) {
                    bootbox.alert('暂无数据')
                } else {
                    var tag = '';
                    var tagHeader = '  <tr>' +
                        '        <th>货柜批次号</th>' +
                        '        <th>货物价值</th>' +
                        '        <th>运输方式</th>' +
                        '        <th>货柜号</th>' +
                        '        <th>越南车牌号</th>' +
                        '        <th>国内车牌号</th>' +
                        '        <th>投保单号</th>' +
                        '        <th>是否变更投保</th>' +
                        '        </tr>';
                    $('#orderInsurance').html(tagHeader);
                    $.each(list, function (k, v) {
                        // var details =v.detailInfo=='null'?'':v.detailInfo;
                        if(v.changeFlag==0){
                            tag +=
                                ' <tr>' +
                                '            <td containerId="' + v.containerId + '">' + judgeIsNull(v.containerNo) + '</td>' +
                                '            <td>' + judgeIsNull(v.containerTotalAmount) + '</td>' +
                                '            <td>' + judgeIsNull(v.deliveryTypeDesc) + '</td>' +
                                '            <td>' + judgeIsNull(v.containerBoxNo) + '</td>' +
                                '            <td>' + judgeIsNull(v.foreignTravelNo) + '</td>' +
                                '            <td>' + judgeIsNull(v.mainTravelNo) + '</td>' +
                                '            <td>' +
                                '                <input type="text" value="' + judgeIsNull(v.applicationNo) + '">' +
                                '            </td>' +
                                '            <td>' +
                                '                <select name="" id="">' +
                                '                    <option selected value="">否</option>' +
                                '                    <option value="">是</option>' +
                                '                </select>' +
                                '            </td>' +
                                '        </tr>';
                        }else{
                            tag +=
                                ' <tr>' +
                                '            <td containerId="' + v.containerId + '">' + judgeIsNull(v.containerNo) + '</td>' +
                                '            <td>' + judgeIsNull(v.containerTotalAmount) + '</td>' +
                                '            <td>' + judgeIsNull(v.deliveryTypeDesc) + '</td>' +
                                '            <td>' + judgeIsNull(v.containerBoxNo) + '</td>' +
                                '            <td>' + judgeIsNull(v.foreignTravelNo) + '</td>' +
                                '            <td>' + judgeIsNull(v.mainTravelNo) + '</td>' +
                                '            <td>' +
                                '                <input type="text" value="' + judgeIsNull(v.applicationNo) + '">' +
                                '            </td>' +
                                '            <td>' +
                                '                <select name="" id="">' +
                                '                    <option value="">否</option>' +
                                '                    <option selected value="">是</option>' +
                                '                </select>' +
                                '            </td>' +
                                '        </tr>';
                        }

                    });
                    $('#orderInsurance').append(tag);
                }
            }
        })
    });


    //保存
    $('#subInsurance').click(function () {
        var data = {
            orderNo: $('#order').html(),
            insuranceList: getTableData()
        };
        data=JSON.stringify(data);

        $.ajax({
            url:'/admin/neworder/center/container/update_insurance_ajax',
            type:'post',
            data:data,
            success:function (response) {
                if(response.code==200){
                    bootbox.alert('添加成功');
                    location =location
                }
            }
        })
    })

    function judgeIsNull(judge) {
        var a;
        if (judge == null || judge == '') {
            a = ''
        } else {
            a = judge
        }
        return a
    }

    //取出table里面的数据
    function getTableData() {
        var tab_trs = $('#orderInsurance').find('tr');
        var insuranceList = [];
        for (var i = 1; i < tab_trs.length; i++) {
            var tab_tr = tab_trs[i];

            var containerId = tab_tr.children[0].getAttribute('containerId');
            var applicationNo = tab_tr.children[6].children[0].value;
            var changeFlag ;
            if(tab_tr.children[7].children[0].children[0].selected==true){
                changeFlag=0
            }else{
                changeFlag=1
            }


            var obj = {
                containerId: containerId,
                applicationNo: applicationNo,
                changeFlag:changeFlag
            };

            insuranceList.push(obj)
        }

        return insuranceList
    }
})