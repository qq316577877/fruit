/**
 * Created by yl on 2017/8/16.
 */


function Loading() {
    var loadingHtml="<div id='loading'>"+
        "<div  class=\"spinner\">"+
        "  <div class=\"double-bounce1\"></div>"+
        "  <div class=\"double-bounce2\"></div>"+
        "</div></div>";

    // 初始化方法
    init();

    this.open=open;
    this.close=close;

// 初始化
    function init() {
        // 页面加载完后
        $(function () {
            // 添加html
            $("body").append(loadingHtml);
        });
    }

    /**
     * 打开等待框
     */
    function open() {
        // 更新提示信息
        $("#loading").css("display","block");
    }

// 关闭等待框
    function close(time) {
        var timer=1;
        if(time){
            timer=time;
        }else{
            $("#loading").css("display","none");
        }
        setTimeout(function () {
            $("#loading").css("display","none");
        },timer*1000);
    }
}

var Loading=new Loading();
