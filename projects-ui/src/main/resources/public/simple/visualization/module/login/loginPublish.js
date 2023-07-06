//登录类型
//只显示手机号
$('#widgetBox1 .visual_login_title_user, #widgetBox1 .visual_login_userName').remove();
$('#widgetBox1 .visual_login_title_phone').addClass('layui-this');
$('#widgetBox1 .visual_login_phone').addClass('layui-show');

//只显示用户名
$('#widgetBox1 .visual_login_title_phone, #widgetBox1 .visual_login_phone').remove();
$('#widgetBox1 .visual_login_title_user').addClass('layui-this');
$('#widgetBox1 .visual_login_userName').addClass('layui-show');