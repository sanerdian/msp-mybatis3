//基本信息
let signatureId = localStorage.getItem('detailId');
let detailObject;
let param4Item1={};
param4Item1.signatureId=signatureId
param4Item1.queryType="include";//取满足其他条件且在联合表中存在的api
param4Dialog1={};
param4Dialog1.signatureId=signatureId
param4Dialog1.queryType="exclude";//取满足其他条件且在联合表中存在的api
let columnItem1 = [
    {type:'checkbox'}
    ,{field:'rowNum', title: '序号',align: 'right',width:80}
    ,{field:'name', title: 'API名称',align: 'left',width:260,sort: true}
    ,{field:'dict.runType', title: '运行状态',align: 'left',sort: true,width:120,templet:function (data) {
            return data.dict.runType;
        }}
    ,{field:'reqMethod', title: 'Http请求类型',align: 'left',sort: true,width:120}
    ,{field:'reqServiceName', title: '请求服务名',align: 'left',sort: true,width:120}
    ,{field:'dict.groupName', title: '分组名称',align: 'left',width:160,templet:function (data) {
            return data.dict.groupName;
        }}
    ,{field:'reqUri', title: '请求路径',align: 'left',sort: true,width:260}
    ,{field:'remark', title: '描述', align: 'left',sort: true}
    ,{field:'', title: '操作',fixed: 'right',toolbar: '#operateItemTemplate',align: 'left',width:80}
];
init();
function init() {
    loadDetail();
    //条件列表搜索
    doList4Item1(1);

    //搜索提交
    form.on("submit(LAY-item1-back-search)",function(data){
        Object.assign(param4Item1,data.field)
        doList4Item1(1);
        return false;
    });

    form.on("submit(LAY-api-back-search)",function (data) {
        Object.assign(param4Dialog1,data.field);
        doList4Dialog1(1);
        return false;
    });
    //监听操作列工具条
    table.on('tool(tableItem1)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        let data = obj.data; //获得当前行数据
        let layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        if(layEvent === 'del'){ //删除
            doDeletes4Item1([data.id]);
        }
    });

    //表单提交
    form.on("submit(LAY-item1-back-add)",function(data){
        let arr = new Array();
        let ids=checkChecked('tableDataApi');
        for(let id of ids){
            let obj={"signatureId":signatureId,"apiId":id};
            arr.push(obj)
        }
        doAddsItem1(arr);
    });

    //点击按钮操作
    $('.layui-btn').click(function(){
        const type = $(this).attr('data-type');
        if(type === 'add'){
            setOpenData4Item1('绑定',param4Item1);
        }else if(type === 'del'){
            let ids=checkChecked("tableItem1");
            doDeletes4Item1(ids);
        }else if(type=='exportExcel'){
            let exportType=$(this).attr('export-type');
            if(exportType=='signatureApi'){
                exportExcel(service_prefix.gateway,"/signatureApi/downloadExcel","signatureApi.xls",param4Item1,columnItem1);
            }
        }
        if(type){return false;}
    });//分组下拉选
    getSearchSort("/group/listAll",".groupOption");
    setRender();
    setOpenLayer();
}

function doEditDetail() {
    setOpenData('修改',detailObject);
}

function loadDetail(){
    ajax(service_prefix.gateway, "/signature/"+signatureId,"get").then(function (data){
        if(data.success){
            getData(data.obj,'#baseInfoTemplate','#baseInfoView');
            detailObject=data.obj;
        }else {
            layer.alert(data.msg);
        }
    })
}
function doList4Item1(current) {
    doListTable(service_prefix.gateway, '/router/list',"post",param4Item1,current,null
        ,"tableItem1",columnItem1,"page_tableItem1");
}
function doList4Dialog1(current) {
    let columnDialog1 = [
        {type:'checkbox'}
        ,{field:'rowNum', title: '序号',align: 'right',width:80}
        ,{field:'name', title: 'API名称',align: 'left',width:260,sort: true}
        ,{field:'dict.runType', title: '运行状态',align: 'left',sort: true,width:120,templet:function (data) {
                return data.dict.runType;
            }}
        ,{field:'reqMethod', title: 'Http请求类型',align: 'left',sort: true,width:120}
        ,{field:'reqServiceName', title: '请求服务名',align: 'left',sort: true,width:120}
        ,{field:'dict.groupName', title: '分组名称',align: 'left',width:160,templet:function (data) {
                return data.dict.groupName;
            }}
        ,{field:'reqUri', title: '请求路径',align: 'left',sort: true,width:260}
        ,{field:'remark', title: '描述', align: 'left',sort: true}
    ];
    doListTable(service_prefix.gateway, '/router/list',"post",param4Dialog1,current,null
        ,"tableDataApi",columnDialog1,"page_tableDataApi",8);
}
function doAddsItem1(arr) {
    let insertBatchUrl = "/signatureApi/adds";
    let type = "post";
    ajax(service_prefix.gateway, insertBatchUrl,type,JSON.stringify(arr)).then(function (data){
        if(data.success){
            doList4Item1(1);
            layer.closeAll();
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

//设置弹出层表单数据（新建、修改）
function setOpenData4Item1(editType,data) {
    let tit = editType + "Api";
    setLayer('table_Item1',tit,'1100px','600px');
    laytpl($('#templateItem1').html()).render(data, function(html){
        $('#table_Item1').html(html);
        doList4Dialog1(1);
        getSearchSort("/group/listAll",".groupOption");
    });

}

//根据ids批量删除
function doDeletes4Item1(data) {
    if(!data||data.length<1){
        layer.alert("请选择要删除的数据！");
        return;
    }
    let param ={
        "signatureId":signatureId,
        "apiIds":data
    }
    layer.alert("是否确定删除,该操作不可撤回",function () {
        ajax(service_prefix.gateway, "/signatureApi/dels","delete",JSON.stringify(param)).then(function (data){
            if(data.success){
                doList4Item1(1);
                layer.closeAll();
            }else{
                layer.alert(data.msg);
            }
        });
    })
}
//搜索select下拉
function getSearchSort(url,clasz){
    ajax(service_prefix.gateway, url,"post",{}).then(function (data){
        if(data){
            for(let i=0; i<data.obj.length; i++){
                $(clasz).append("<option value='"+data.obj[i].id+"'>"+data.obj[i].name+"</option>");
            }
            setRender();
        }else {
            layer.alert("下拉选内容获取失败");
        }
    });
}
//所属组织
getSearchSort("/group/listAll",".groupOption");