let appId = localStorage.getItem('currentId');
init();
function init() {
    document.getElementById("idUserSyncSetting").innerHTML="应用["+localStorage.getItem("currentName")+"]用户同步设置";
    doLoadUserSetting();
    form.on("submit(LAY-set-user-submit)",function (data) {
        doSaveUserSetting(data);
    });
    form.on("select(LAY-set-user-select-syncType)",function (data) {
        if(data.value=='http'){
            $(".syncType-http").show();
        }else {
            $(".syncType-http").hide();
        }
    });
    setRender();
    setOpenLayer();
}
function doLoadUserSetting(){
    let url="/taskSettingUser/listAll";
    let type="post";
    ajax(service_prefix.gateway, url+"?appId="+appId,type).then(function (data){
        if(data.success){
            if(data.obj.length>0){
                setFormVal("setUserForm",data.obj[0]);
                if(data.obj[0].syncType=='http'){
                    $(".syncType-http").show();
                }else {
                    $(".syncType-http").hide();
                }
            }
        }else{
            layer.alert(data.msg);
        }
    });
}
function doSaveUserSetting(data) {
    let type = "post";
    let url="//taskSettingUser";
    if(data.field.id){//有id，为修改
        // url += "/" +data.field.id;
        url +="/allColumn";//更新全部列。用于解决checkbox反选无法保存进数据库的问题
        type = "put";
    }
    data.field.appId=appId;
    let formValue=data.field;
    ajax(service_prefix.gateway, url,type,JSON.stringify(data.field)).then(function (data){
        if(data.success){
            if(type=="post"){
                formValue.id=data.obj.id;
                setFormVal("setUserForm",formValue);
            }
            layer.msg(data.msg,{
                    time:3000
                },function(){
                    layer.close();
                }
            );
        }else{
            layer.alert(data.msg);
        }
    });
}
function checkUserLink(){
    alert('验证用户同步链接');
}
function notfinish(data){
    debugger
    alert('功能未实现');
}
