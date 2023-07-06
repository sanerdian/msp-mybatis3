/**
 * 获取用户未读消息
 */
function getUserNoRead(){
    var jsondata={
        "pager":{
            "current":1,
            "size":10,
            "sortProps": [
                {
                    "key": "status",
                    "value": true
                },
                {
                    "key": "sendTime",
                    "value": false
                }
            ]
        },
        "entity":{
            "receptionId":-1
        }
    };

    ajax(service_prefix.message,"/msg/listcount","post",JSON.stringify(jsondata))
    .then((data) => {
       /* var res =data.obj.records;*/
        var res =data.obj
        var count = 0;
        for(var i = 0 ; i < res.length ; i++){
            if(res[i].status == 0){
                count++;
            }
        }
        if(count > 0){
            $('.layui-badge-dot').show();
        }else{
            $('.layui-badge-dot').hide();
        }
    })

}


function addMenuLog(data){
    if(!$("#currUser").text()) return;
    ajax(service_prefix.log,"/userMenu","post",JSON.stringify({content:data,crUser:$("#currUser").text()}))
    .then(res=>{

    })
}

/**
 * 全屏
 */
function entryFullScreen(event) {
    var docE = document.documentElement;
    if (docE.requestFullScreen) {
        docE.requestFullScreen();
     } else if (docE.mozRequestFullScreen) {
       docE.mozRequestFullScreen();
    } else if (docE.webkitRequestFullScreen) {
        docE.webkitRequestFullScreen();
    }
}

function toFrontUrl() {
    ajax(service_prefix.manage,"/systemmsg/getinfo","get")
    .then(res=>{
        window.open(res.obj.frontUrl) ;
    })
}

/**
 * 左侧菜单收缩展开
 */
function initThis() {
    var isShow = true;
    $('#flex').click(function() {
        if(isShow){
            $("#menu").addClass('side_menu_narrow');
            $('.layui-header,.layadmin-pagetabs,.layui-body,.layui-footer').css('left', 60+'px');
            // $('.layui-nav-tree .layui-nav-item:gt(7) .layui-nav-child').css('bottom',0);
            //修改标志位
            isShow =false;
        }else{
            $("#menu").removeClass('side_menu_narrow');
            $('.layui-nav-item i').css('margin-right', '10%');
            $('.layui-header,.layadmin-pagetabs,.layui-body,.layui-footer').css('left', 220+'px');
            isShow =true;
        }
    })
    layui.element.init();
    var flag = $('#menu').hasClass('side_menu_narrow');
    if(flag){
        $('.layui-header').css('left', 60+'px');
        $('.layadmin-pagetabs').css('left', 60+'px');
    }
}

//便签
function note(){
    var $win = $(window).width();
    layer.open({
        title: '便签'
        ,shade: 0
        ,offset: ['41px',$win-450]
        ,anim: -1
        ,id: 'LAY_adminNote'
        ,skin: 'layadmin-note layui-anim layui-anim-upbit'
        ,content: '<textarea placeholder="内容">'+localStorage.getItem('noteText')+'</textarea>'
        ,resize: false
        ,success: function(layero, index){
            var textarea = layero.find('textarea');
            textarea.focus().on('keyup', function(){
                var noteText = textarea.val();
                localStorage.setItem('noteText',noteText);
            });
        }
        , yes: function (params) {
            var nodeText = localStorage.getItem("noteText"); //获取键为allJson的字符串
            var params = $("#textCon").val();
            $.ajax({
                type: 'get',
                data: params,
                url: '/simple/manage/setting/js/notedata.json',
                dataType: 'json',
                success: function (data) {
                }
            })
            var myDate = new Date;
            var year = myDate.getFullYear(); //获取当前年
            var mon = myDate.getMonth() + 1; //获取当前月
            var date = myDate.getDate(); //获取当前日

            var jsonStr = {}
            jsonStr.con = params;
            localStorage.setItem('bqcon', jsonStr)
            $('#list').append('<ul class="box"><li><p class="head" style="display: flex;"><span >' + year + "-" + mon + "-" + date + '</span><span><i class="layui-icon layui-icon-close" style="font-size: 20px; color: #ffffff; font-weight: bold;" onclick="deleteNote()"></i></span></p><p class="main">' + nodeText + '</p></li></ul>')


        }
    });

}

/**
 * 退出
 */
function logout(){
    ajax(service_prefix.member,"/home/ajaxLeave","post",{}).then(function(res){
        localStorage.setItem('state',false)
        if(ifApiAsync()){
            var param = {objectType:"user",objectValue: {},operation:"logout"};
            ajax(service_prefix.gateway,"/task","post",JSON.stringify(param)).then(function (res1) {
                window.location.href='login.html'
            });
        }else{
            window.location.href='login.html'
        }
    });
}

var myPermission = [];

function getMyPermission(){
    ajax(service_prefix.member,"/permission/myPermission","get").then(function (res) {
        myPermission = res.obj;
    })
}

var paramObj={};
var user = {};

var comid = '1167323268476895233';

/////////////////////////////////////////////////////////////
//

$(function(){
	setRender();
	setLoad('#headerDiv','common/html/header.html');
	setLoad('#footerDiv','common/html/footer.html');
});

$(function(){
    getPerfix().then(function(res){
        getUser(function(res){
            if(res.success&&res.obj.user){
                lodTpl("userItemTemplate","userItemView",res);
            }else{
                window.location.href='login.html';
                return false;
            }
        })
        findMenu();
        getUserNoRead();
        getMyPermission();
        go2Menu("simple/home/index.html","首页");
        initThis();
        getCopyRight()

        // WordPaster.getInstance({
        //     PostUrl:com.jnetdata.url_prefix + '/resources/files/ue/word',
        //     // Cookie: '<%=clientCookie%>',
        //     ImageMatch: '\"([^\"]*)\"', //可以直接使用UEditor的上传地址，
        //     License2: "FFpYF3350t5XPQ01AGAJL1r3ndNuyKS2H5QaDAe6RVsL+Yg1Lb3jKQvbGzAHTgEFw8j4JnIi6tW9Snzsex7HHUfv4WlxD+Mmfrxsc6jx7MUAUxil05cHYituDFKoIq/Qsr3JBK8+cubqSbQ4IMDG8FTTPiVEM5wuQyr1/xpd+hXXxdPQAbgyZyk/zBRbvotTqgi0kQMplacG3W885xSqTha+cNdVlflh33JEl194AlslgPFe3oJWrplv4kPbpo07hsL4qg2tDKZqvhUb0TjO8w0GY0zHKgSTzpgbTz8CJq/E0gkTTuomWGBREX6OuNs3K7m9xCas+gZ7xf0L7L9gOMDV3kaQxkN8xLqMhEJm0cHsIF9au0OFbQXc41H4gXz1/QSCmN7TJkpXWw5vkBEads5Mbp9BhFJu4+Cz7d3nFbPZMFwFXW19vVpyPJJ+jNIELfR227QfHhWtNFY4rBXYMAZXrKixqqzZ0YY4MyW7rLJu3fXohz52gxP73DH7yKOF+5vybUzuNtUGB18kwe7YuJd2cIK98Dn/yx6uieZ3f+wu93fMJDqy4sSZDOLXEvsenJ3GjJIuecjmpfX16xl+pA=="
        // }).Load();//加载控件

        // var ue = UE.getEditor('myEditor');
        //
        // ue.ready(function() {
        //     //设置编辑器的内容
        //     ue.setContent('hello');
        //     //获取html内容，返回: <p>hello</p>
        //     var html = ue.getContent();
        //     //获取纯文本内容，返回: hello
        //     var txt = ue.getContentTxt();
        //
        //     //WordPaster快捷键 Ctrl + V
        //     ue.addshortcutkey({
        //         "wordpaster": "ctrl+86"
        //     });
        // });



        // var pasterMgr = new WordPasterManager();//这里必须声明成全局变量。
        // pasterMgr.Config["PostUrl"] = com.jnetdata.url_prefix + '/resources/files/ue/word'//可以直接使用UEditor的上传地址，
        // pasterMgr.Config["ImageMatch"] = '\"([^\"]*)\"'//可以直接使用UEditor的上传地址，
        // pasterMgr.Config["License2"] = "go3EOGm9R1HaNibP2FwW7lczQ0K9tnjnrR4DLLeZltDlcSs0xVupADKFEKDU1DKxPkE/XRcpHoIszwgzx1zDrWfVwi7BS64AXe/MMK4kzEfPlnncEi6voNb51NZnXrtdpHoHbqmXQNwpgaoK1q6iRME42GYEjvmAfYCkuinFT4TM0x55MiL5VK8DhCuztLOi+NevtIolMxFu7GbOuQfPo0yC9WdC6QpLxALNGJun2gjdw6oorMobzecMw5gexAAMdIAB+VnS1gCyyh3oB4crYgTFxkLUMkkjes/A4J96X7Nzh7RKZb8+EYYIXZIhWqi2w1pOBKJKiEznX7gz+ACvUTd5bKmDC6twwlWeD9g6PORgXGMlQ984d1fbAZloxKQoFunmMZJ6eQUAXCmjoUXhIUh3ngQpel1ooMEfb0qmsY5TGG3Zn+ghIlmCbPAQ5dkJNRmQmve6PV59WIQVNKhNa6Ku7b8xRliIXsh40Lb3sbulP25IWNaevh+nrx/6QXwTlUdRg00cp8S96UF1bS4oNoPM2hRqP0yYsmLS+hs0UeI3FLrYfnTYHASBOLfyjIeL5IVlIsT9Y3cETiqpq64o2w=="
        // pasterMgr.Load();//加载控件
    })
});
