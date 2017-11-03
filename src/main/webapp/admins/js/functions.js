var Functions = function () {

    return {

        removeLastChar: function (str) {
            if (str.length == 0) {
                return str;
            }
            return str.substring(0, str.length - 1);
        }/*,

         *//** 事件链中断 **//*
         stopBubble:function (e) {
         //如果提供了事件对象，则这是一个非IE浏览器
         if (e && e.stopPropagation) {
         　　	e.stopPropagation();  //因此它支持W3C的stopPropagation()方法
         }
         else
         {
         　　	window.event.cancelBubble = true;  //否则，我们需要使用IE的方式来取消事件冒泡
         }
         return false;
         }*/

    };

}();