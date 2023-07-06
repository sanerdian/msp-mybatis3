//关闭iframe弹出框，刷新父页面
function refreshClose(){
    setTimeout(function(){
        parent.location.reload(true);
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    },1000);
}

function formSubmit(){
    //服务id
    var serviceId = "exercise";

    //接口地址
    var url = '/computer';
    var id = '28';
    if(id){
        //编辑提交
        form.on('submit(visual_submit)', function(data){
            ajax(serviceId, url + '/' + id, "put", JSON.stringify(data.field)).then(function(data){
                if(data.success){
                    layer.msg('编辑成功');
                    refreshClose();//关闭iframe弹出框，刷新父页面
                }else{
                    console.log(data.msg);
                }
            });
            return false;
        });

    }else{
        //新建提交
        form.on('submit(visual_submit)', function(data){
            ajax(serviceId, url, "post", JSON.stringify(data.field)).then(function(data){
                if(data.success){
                    layer.msg('新建成功');
                    refreshClose();//关闭iframe弹出框，刷新父页面
                }else{
                    console.log(data.msg);
                }
            });
            return false;
        });
    }
}
$(function(){
    formSubmit();
})