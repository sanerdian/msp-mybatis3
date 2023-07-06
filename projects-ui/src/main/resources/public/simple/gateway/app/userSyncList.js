let appId = localStorage.getItem('currentId');
let param4UserSync={"appId":appId,"objectType":"user"};
init();
function init() {
    doList4UserSync(1);
    form.on("submit(LAY-userSync-back-search)",function (data) {
        Object.assign(param4UserSync,data.field)
        doList4UserSync(1);
        return false;
    });
    //点击按钮操作
    $('.layui-btn').click(function(){
        var type = $(this).attr('data-type');
        if(type=='reset'){
            let ids=checkChecked("tableUserSync");
            doReset4UserSync(ids);
        }else if(type=='deletes'){
            let ids=checkChecked("tableUserSync");
            doDeleteByIds4UserSync(ids);
        }
        if(type){return false;}
    });
    setRender();
    setOpenLayer();
}

//条件列表搜索
function doList4UserSync(current) {
    let columns=[
        {type:'checkbox'}
        ,{field:'rowNum', title: '序号',align: 'right',width:80}
        ,{field:'operation', title: '操作',align: 'left'}
        ,{field:'objectValue', title: '数据',align: 'left'}
        ,{field:'status', title: '同步状态',align: 'left',templet:function (data) {
                let name='';
                if(data.status==0){
                   name='待执行';
                }else if(data.status==1){
                    name='同步成功';
                }else if(data.status==2){
                    name='同步失败';
                }else if(data.status==3){
                    name='抛出异常';
                }
                return name;
            }}
        ,{field:'modifyTime', title: '同步执行时间', align: 'left'}
        ,{field:'returnCode', title: '响应码',align: 'left'}
        ,{field:'returnMsg', title: '响应信息', align: 'left'}
    ];
    doListTable(service_prefix.gateway, "/taskResult/list","post",param4UserSync,current,null
        ,"tableUserSync",columns,"pageUserSync");
}//根据ids批量删除
function doDeleteByIds4UserSync(ids) {
    layer.alert("是否确定删除,该操作不可撤回",function () {
        ajax(service_prefix.gateway, "/taskResult","delete",JSON.stringify(ids)).then(function (data){
            if(data.success){
                doList4UserSync(1);
                layer.closeAll();
            }else{
                layer.alert(data.msg);
            }
        })
    })
}
function doReset4UserSync(ids) {
    ajax(service_prefix.gateway, "/taskResult/reset","put",JSON.stringify(ids)).then(function (data){
        if(data.success){
            doList4UserSync(1);
            layer.closeAll();
        }else{
            layer.alert(data.msg);
        }
    })
}
