//基本信息
let ipRuleId = localStorage.getItem('detailId');
let detailObject;
let param4Item1={};
param4Item1.ipRuleId=ipRuleId
param4Item1.queryType="include";//取满足其他条件且在联合表中存在的api
let param4Item2={};
param4Item2.ipRuleId=ipRuleId;
param4Dialog1={};
param4Dialog1.ipRuleId=ipRuleId
param4Dialog1.queryType="exclude";//取满足其他条件且在联合表中存在的api
let columnItem1 = [
    {type:'checkbox'}
    ,{field:'rowNum', title: '序号',align: 'right',width:80}
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
let columnItem2 = [
    {type:'checkbox'}
    ,{field:'rowNum', title: '序号',align: 'right',width:80}
    ,{field:'dict.ipRange', title: '范围',align: 'left',sort: true,templet:function (data) {
            return data.dict.ipRange;
        }}
    ,{field:'ipAddress', title: 'ip地址',align: 'left',sort: true}
    ,{field:'remark', title: '备注',align: 'left',sort: true}
    ,{field:'', title: '操作',fixed: 'right',toolbar: '#operateItem2Template',align: 'left',width:120}
];

init();
function init() {
    loadDetail();
    //条件列表搜索
    doList4Item1(1);
    doList4Item2(1);
    //搜索提交
    form.on("submit(LAY-item1-back-search)",function(data){
        Object.assign(param4Item1,data.field)
        doList4Item1(1);
        return false;
    });
    form.on("submit(LAY-item2-back-search)",function(data){
        Object.assign(param4Item2,data.field)
        doList4Item2(1);
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
    table.on('tool(tableItem2)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        let data = obj.data; //获得当前行数据
        let layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        if(layEvent === 'del'){ //删除
            doDeletes4Item2([data.id]);
        }else if(layEvent === 'edit'){ //编辑
            data.editType = 1;
            setOpenData4Item2('编辑',data,"GroupModelItem");
        }
    });
    //表单提交
    form.on("submit(LAY-item1-back-add)",function(data){
        let arr = new Array();
        let ids=checkChecked('tableDataApi');
        for(let id of ids){
            let obj={"ipRuleId":ipRuleId,"apiId":id};
            arr.push(obj)
        }
        doAddsItem1(arr);
    });
    form.on("submit(LAY-item2-back-add)",function(data){
        doSaveItem2(data);
    });
    //点击按钮操作
    $('.layui-btn').click(function(){
        const type = $(this).attr('data-type');
        const dataLayer = $(this).attr('data-layer-html');
        if(type === 'add'){
            if(dataLayer==='addItem1.html'){
                setOpenData4Item1('绑定',param4Item1);
            }else if(dataLayer==='addItem2.html'){
                setOpenData4Item2('新建',param4Item2);
            }
        }else if(type === 'del'){
            let ids;
            if(dataLayer==='addItem1.html'){
                ids=checkChecked("tableItem1");
                doDeletes4Item1(ids);
            }else if(dataLayer==='addItem2.html'){
                ids=checkChecked("tableItem2");
                doDeletes4Item2(ids);
            }
        }else if(type=='exportExcel'){
            let exportType=$(this).attr('export-type');
            if(exportType==='ipRuleApi'){
                exportExcel(service_prefix.gateway,"/ipRuleApi/downloadExcel","ipRuleApi.xls",param4Item1,columnItem1);
            }else if(exportType==='ipRuleItem'){
                exportExcel(service_prefix.gateway,"/ipRuleItem/downloadExcel","ipRuleItem.xls",param4Item2,columnItem2);
            }
        }
        if(type){return false;}
    });
    //分组下拉选
    getSearchSort("/group/listAll",".groupOption");
    setRender();
    setOpenLayer();
}//渲染数据到table

function doEditDetail() {
    setOpenData('修改',detailObject);
}

function loadDetail(){
    ajax(service_prefix.gateway, "/ipRule/"+ipRuleId,"get").then(function (data){
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

function doList4Item2(current) {
    doListTable(service_prefix.gateway, '/ipRuleItem/list',"post",param4Item2,current,null
        ,"tableItem2",columnItem2,"page_tableItem2");
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
    let insertBatchUrl = "/ipRuleApi/adds";
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
    })
}
function doSaveItem2(data) {
    let thisurl = "/ipRuleItem";
    let type = "post";
    if(data.field.id){
        thisurl += "/" +data.field.id;
        type = "put";
    }
    ajax(service_prefix.gateway, thisurl,type,JSON.stringify(data.field)).then(function (data){
        if(data.success){
            doList4Item2(1);
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
    })
}

//根据ids批量删除
function doDeletes4Item1(data) {
    if(!data||data.length<1){
        layer.alert("请选择要删除的数据！");
        return;
    }
    let param ={
        "ipRuleId":ipRuleId,
        "apiIds":data
    }
    layer.alert("是否确定删除,该操作不可撤回",function () {
        ajax(service_prefix.gateway, "/ipRuleApi/dels","delete",JSON.stringify(param)).then(function (data){
            if(data.success){
                doList4Item1(1);
                layer.closeAll();
            }else{
                layer.alert(data.msg);
            }
        })
    })
}
function doDeletes4Item2(data) {
    if(!data||data.length<1){
        layer.alert("请选择要删除的数据！");
        return;
    }
    layer.alert("是否确定删除,该操作不可撤回",function () {
        ajax(service_prefix.gateway, "/ipRuleItem","delete",JSON.stringify(data)).then(function (data){
            if(data.success){
                doList4Item2(1);
                layer.closeAll();
            }else{
                layer.alert(data.msg);
            }
        });
    });
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
            layer.alert("获取下拉选失败！");
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
function setOpenData4Item2(editType,data) {
    var tit = editType + "限流特殊应用";
    setLayer('table_Item2',tit,'700px','250px');
    laytpl($('#templateItem2').html()).render(data, function(html){
        $('#table_Item2').html(html);
        setFormVal('editFormItem2',data);
    });
}