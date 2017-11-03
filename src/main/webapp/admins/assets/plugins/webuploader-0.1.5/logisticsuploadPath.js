var contractUrl ;
var voucherUrl;
var myContractUrl;
var myVoucherUrl;
var urlListsNum;
$(function () {
    //上传图片
// 初始化Web Uploader

    var uploader = WebUploader.create({

        // 选完文件后，是否自动上传。
        auto: true,

        // swf文件路径
        swf: './Uploader.swf',

        // 文件接收服务端。
        server: '/admin/file/upload_private_ajax',

        // 选择文件的按钮。可选。
        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
        pick: '#imgPicker ',

        duplicate :true,

        fileNumLimit:'10',
        // 只允许选择图片文件。
        accept: {
            title: 'Images',
            extensions: 'gif,jpg,jpeg,bmp,png',
            mimeTypes: 'image/png,image/jpg,image/jpeg,imge/bmp,image/gif'
        }
    });

// 文件上传成功，给item添加成功class, 用样式标记上传成功。
    var tag ='';
    uploader.on( 'uploadSuccess', function(file, res) {
        // $("#showImg").attr("src",res.datas.url);
        tag +=' <img imgPath="'+res.datas.path+'" src="'+res.datas.url+'" alt="">';
        $('#showImage').html(tag);
        contractUrl =res.datas.path;
    });

    urlListsNum =function () {
        return tag
    }

// 文件上传失败，显示上传出错。
    uploader.on( 'uploadError', function( file ) {
        var $li = $( '#'+file.id ),
            $error = $li.find('div.error');

        // 避免重复创建
        if ( !$error.length ) {
            $error = $('<div class="error"></div>').appendTo( $li );
        }

        $error.text('上传失败');
    });

// 完成上传完了，成功或者失败，先删除进度条。

    uploader.on( 'uploadComplete', function( file ) {
        $( '#'+file.id ).find('.progress').remove();
        // tag ='';
    });


//第二个图
//上传图片
// 初始化Web Uploader
    var uploader2 = WebUploader.create({

        // 选完文件后，是否自动上传。
        auto: true,
        // swf文件路径
        swf: './Uploader.swf',
        // 文件接收服务端。
        server: '/admin/file/upload_private_ajax',
        // 选择文件的按钮。可选。
        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
        pick: '#imgPicker2 ',

        // 只允许选择图片文件。
        accept: {
            title: 'Images',
            extensions: 'gif,jpg,jpeg,bmp,png',
            mimeTypes: 'image/png,image/jpg,image/jpeg,imge/bmp,image/gif'
        }
    });
// 文件上传成功，给item添加成功class, 用样式标记上传成功。
    uploader2.on( 'uploadSuccess', function(file, res) {
        $("#showImg2").attr("src",res.datas.url);
        voucherUrl =res.datas.path;
    });
// 文件上传失败，显示上传出错。
    uploader2.on( 'uploadError', function( file ) {
        var $li = $( '#'+file.id ),
            $error = $li.find('div.error');

        // 避免重复创建
        if ( !$error.length ) {
            $error = $('<div class="error"></div>').appendTo( $li );
        }
        $error.text('上传失败');
    });

// 完成上传完了，成功或者失败，先删除进度条。
    uploader2.on( 'uploadComplete', function( file ) {
        $( '#'+file.id ).find('.progress').remove();
    });

    myContractUrl = function () {
        return contractUrl
    };
    myVoucherUrl = function () {
        return voucherUrl
    }
})

