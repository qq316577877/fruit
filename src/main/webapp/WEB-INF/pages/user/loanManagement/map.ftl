<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <style type="text/css">
        body, html {width: 100%;height: 100%; margin:0;font-family:"微软雅黑";}
        #allmap{height:500px;width:100%;}
        #r-result,#r-result table{width:100%;}
    </style>
    <script type="text/javascript" src="https://api.map.baidu.com/api?v=2.0&ak=yBcf7XHDdFkKKlikH09FQTfchtrIn6O3&s=1"></script>
    <script src="https://libs.baidu.com/jquery/1.9.0/jquery.js"></script>

<#include "/WEB-INF/pages/base/css.ftl">
    <link rel="stylesheet" href="/admins/assets/plugins/data-tables/DT_bootstrap.css"/>
<#include "/WEB-INF/pages/base/js.ftl">
<#import "/WEB-INF/pages/base/base.ftl"  as b>

</head>
<body>
<div id="allmap"></div>
<div style="display: none;">
	<input id="clearFlag" value="${clearFlag}">
	<input id="clearAddress" value="${clearAddress}">
	<input id="receiverAddress" value="${receiverAddress}">
	<input id="deliveryTime" value="${deliveryTime}">
	<input id="preReceiveTime" value="${preReceiveTime}">
    <input id="containerNo" value="${containerNo}">

</div>
<div id="driving_way">

</div>
<div id="r-result"></div>
</body>
</html>
<script type="text/javascript">
    //传参有:1.清关关口地址  2.收货地址   3.是否已清关   4.发货时间    5.预计收货时间    6.货柜批次号
	var clearFlag = $("#clearFlag").val();//是否已清关
    var startDate = new Date($("#deliveryTime").val());//发货时间
    var endDate = new Date($("#preReceiveTime").val());//预计收货时间
    var containerNo = $("#containerNo").val();//货柜批次号
    var theTime = new Date();
    var ratio = (theTime - startDate)/(endDate - startDate);

    // 百度地图API功能
    var map = new BMap.Map("allmap");
    var start = $("#clearAddress").val();//清关关口地址
    var end = $("#receiverAddress").val();//收货地址
    map.centerAndZoom(new BMap.Point(116.404, 39.915), 11);
    var myIcon = new BMap.Icon("/admins/assets/img/car.png", new BMap.Size(32, 70), {    //小车图片
        //offset: new BMap.Size(0, -5),    //相当于CSS精灵
        imageOffset: new BMap.Size(0, 0)    //图片的偏移量。为了是图片底部中心对准坐标点。
    });
    //三种驾车策略：最少时间，最短距离，避开高速
    //var routePolicy = [BMAP_DRIVING_POLICY_LEAST_TIME,BMAP_DRIVING_POLICY_LEAST_DISTANCE,BMAP_DRIVING_POLICY_AVOID_HIGHWAYS];

    $(document).ready(function(){
        map.clearOverlays();
        var i=$("#driving_way select").val();
        search(start,end,BMAP_DRIVING_POLICY_LEAST_TIME);
        function search(start,end,route){
            var driving = new BMap.DrivingRoute(map, {renderOptions:{map: map, autoViewport: true},policy: route});
            driving.search(start,end);
            //  console.log(typeof driving);
            if(clearFlag){
           // if(true){
            	var drivingRouteResult = driving.getResults();

            	driving.setSearchCompleteCallback(function(){
                	var pts = getAllLocations();
                	var paths = pts.length;    //获得有几个点
                	var count = Math.round(paths);
                	var count1= Math.round(paths / 40);//一共最多跑40个点
                	var carMk = new BMap.Marker(pts[0],{icon:myIcon});
                	map.addOverlay(carMk);
                	i=0;
                	function resetMkPoint(i,paths){
                        if(pts[i]){
                            carMk.setPosition(pts[i]);
                        }
                    	if(i < count-1){
                        	setTimeout(function(){
                            		i+= count1;

                            resetMkPoint(i,paths);
                        	},100);
                    	}else{//展示最后一个点
                            carMk.setPosition(pts[paths-1]);
                        }
                	}
                	setTimeout(function(){
                    	resetMkPoint(1,paths);
                	},1000)
            	});
            }
        }});

    var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});// 左上角，添加比例尺
    var top_left_navigation = new BMap.NavigationControl();  //左上角，添加默认缩放平移控件
    var top_right_navigation = new BMap.NavigationControl({anchor: BMAP_ANCHOR_TOP_RIGHT, type: BMAP_NAVIGATION_CONTROL_SMALL}); //右上角，仅包含平移和缩放按钮
    /*缩放控件type有四种类型:
    BMAP_NAVIGATION_CONTROL_SMALL：仅包含平移和缩放按钮；BMAP_NAVIGATION_CONTROL_PAN:仅包含平移按钮；BMAP_NAVIGATION_CONTROL_ZOOM：仅包含缩放按钮*/
    map.addControl(top_left_control);
    map.addControl(top_left_navigation);
//    map.addControl(top_right_navigation);
    map.enableScrollWheelZoom(true);


    /**
     * 获取货柜的所有物流信息点
     */
    function getAllLocations(){
        var pts = new Array();
        $.ajax("${BASEPATH}/drivers/get_driver_location_by_container_ajax", {
            data: {containerNo:containerNo},
            type: "POST",
            async:false,
        }).always(function () {
        }).done(function (data) {
            if (data.code==200) {
                var locations = data.data;
                if(locations && locations.length>0){
                    for(var i=0;i<locations.length;i++){
                        pts[i] = new BMap.Point(locations[i].longitudeCoordinate,locations[i].latitudeCoordinates);
                    }
                }else{
                    pts[0] = start;
                    bootbox.alert("当前货柜司机未上传定位信息！");
                }
            } else {
                pts[0] = start;
                bootbox.alert(data.msg);
            }
        }).fail(function () {
            bootbox.alert("系统错误！");
        });

//        console.log("pts=="+pts);
        return pts;
    }

</script>                                                                                                                                                      '
