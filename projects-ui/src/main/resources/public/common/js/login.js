var id="1171988080056377346";
$(function () {
    var password=localStorage.getItem("password");
    var username=localStorage.getItem("username");
    if(username){
        $("#username").val(username);
    }
    if(password){
        $("#pwd").val(password);
        layui.form.val("formDemo", { //formTest 即 class="layui-form" 所在元素属性 lay-filter="" 对应的值
            "remember": "on" // "name": "value"
        });
    }
    layui.form.render();
})

var id = "1171988080056377346";

layui.use('form', function(){
    var form = layui.form;
    //监听提交
    form.on('submit(login)', function(data){
        var param = data.field;
        localStorage.setItem('username',param.name);
        if(param.remember == "on"){
            localStorage.setItem('password',param.passWord);
        }else{
            localStorage.setItem('password',"");
        }
        if(com.encrypted.passwordMd5){
            param.passWord=md5Salt(data.field.passWord);
        }
        ajax(service_prefix.member,"/home/wxbackAjaxLogin","post",JSON.stringify(param)).then(res=>{
            if(res.success){
                localStorage.setItem("userId",res.obj.userid);
                if(ifApiAsync()){
                    var param1 = {objectType:"user",objectValue: param,operation:"login"};
                    ajax(service_prefix.gateway,"/task","post",JSON.stringify(param1)).then(function (res1) {
                        window.location.href="index.html";
                    });
                }else{
                    window.location.href="index.html";
                }
                localStorage.setItem("state",true);
                com.jnetdata.mspToken = res.obj.mspToken;
                state=true;
            }else{
                changeImg();
                layer.alert(res.msg);
            }
        })
        return false;
    });
    layui.form.render();
});

function changeImg() {
    return new Promise(function (resovle, reject) {
        $("#verifyCodeImg").attr("src", getAjaxUrl(service_prefix.member,"/home/imgCode") + "?t=" + new Date().getTime());
        resovle(1);
    })

}

function initlogin(){
    changeImg();
    getUser(function(res){
        if(res.success&&res.obj.user){
            window.location.href = "index.html";
        }
    });
    getTitle();
    getCopyRight();
}

function initQrlogin(){
    ajax(service_prefix.member,"/home/getLoginQR","get").then(function(res){
        $("#qrcodeimg").attr("src",res.obj.img);
        qrid = res.obj.qrid;
        $("#qrid").val(qrid);
        openSocket();
    })
    getUser();
    getTitle();
    getCopyRight();
}

