
$(function(){
    //app扫码登录点击效果
    $(".visual_login .visual_qr_tab1").click(function(){
        $(".visual_login .visual_login_user").hide();
        $(".visual_login .visual_login_qr").show();
    });
    $(".visual_login .visual_qr_tab2").click(function(){
        $(".visual_login .visual_login_user").show();
        $(".visual_login .visual_login_qr").hide();
    });
    
    //账号登录
    var form = layui.form;
    function userNameLogin(){
        form.on("submit(userNameLogin)", function(data){
            var param = data.field;
            localStorage.setItem("username",param.name);
            if(param.remember == "on"){
                localStorage.setItem("password",param.passWord);
            }else{
                localStorage.setItem("password","");
            }
            ajax(service_prefix.member,"/home/wxbackAjaxLogin","post",JSON.stringify(param)).then(function(data){ 
                if(data.success){
                    if(ifApiAsync()){
                        var param1 = {objectType:"user",objectValue: param,operation:"login"};
                        ajax(service_prefix.gateway,"/task","post",JSON.stringify(param1)).then(function (data1) {
                            window.location.href="index.html";
                        });
                    }else{
                        // window.location.href="index.html";
                        layer.msg('登录成功！');
                    }
                    localStorage.setItem("state",true);
                    state=true;
                }else{
                    layer.alert(data.msg);
                }
            })
            return false;
        });
    }
    userNameLogin();

    //账号验证码
    function changeImg() {
        $(".visual_login .visual_vercode_img").click(function(){
            return new Promise(function (resovle, reject) {
                $(this).attr("src", getAjaxUrl(service_prefix.member,"/home/imgCode") + "?t=" + new Date().getTime());
                resovle(1);
            })
        });
    }
    changeImg();

    //获取手机短信验证码
    function phoneCode(){
        $(".visual_login .visual_vercode_btn").click(function(){
            var mobile = $('.visual_login input[name="mobile"]').val();
            if(mobile){
                var param = {
                    "mobile":mobile
                };
            }else{
                layer.msg("请输入手机号!");
            }
            ajax(service_prefix.message,"/phoneMessage/sendPhoneVerifyCode", "post",JSON.stringify(param)).then(function(data){ 
                if(data.success){
                    layer.msg("发送手机短信验证码成功！");
                }else{
                    console.log(data.msg);
                }
            });
        });
    }
    phoneCode();

    //手机验证码登录
    function phoneLogin(){
        form.on("submit(phoneLogin)", function(data){
            var param = data.field;
            if(data.field.rememberMe == "on"){
                param.rememberMe = true;
            }
            ajax(service_prefix.member,"/home/loginByPhone", "post",JSON.stringify(param)).then(function(data){ 
                if(data.success){
                    layer.msg("成功！");
                }else{
                    console.log(data.msg);
                }
            });
        });
    }
    phoneLogin();
})