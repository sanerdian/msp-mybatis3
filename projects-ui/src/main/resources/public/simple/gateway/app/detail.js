//基本信息
let appId = localStorage.getItem('detailId');
let detailObject;
let param4Item1={};
param4Item1.appId=appId
let param4Item2={};
param4Item2.appId=appId;
param4Dialog1={};
param4Dialog1.appId=appId
param4Dialog1.queryType="exclude";//取满足其他条件且在联合表中存在的api
let param4Item3={};
param4Item3.targetAppId=appId;
//渲染数据到table
let columnItem1 = [
    {type:'checkbox'}
    ,{field:'rowNum', title: '序号',align: 'right',width:80}
    ,{field:'grantTimeType', title: '授权时间类型',align: 'left',sort: true,width:120,templet:function (data) {
            return data.dict.grantTimeType;
        }}
    ,{field:'grantTimeBegin', title: '授权开始时间',align: 'left',sort: true,width:130}
    ,{field:'grantTimeEnd', title: '授权截至时间',align: 'left',sort: true,width:130}
    ,{field:'name', title: 'API名称',align: 'left',width:260,sort: true}
    ,{field:'dict.runType', title: '运行状态',align: 'left',sort: true,width:120,templet:function (data) {
            return data.dict.runType;
        }}
    ,{field:'reqMethod', title: 'Http请求类型',align: 'left',sort: true,width:120}
    ,{field:'reqServiceName', title: '请求服务名',align: 'left',sort: true,width:120}
    ,{field:'groupName', title: '分组名称',align: 'left',width:160}
    ,{field:'reqUri', title: '请求路径',align: 'left',sort: true,width:260}
    ,{field:'remark', title: '描述', align: 'left',sort: true}
    ,{field:'', title: '操作',fixed: 'right',toolbar: '#operateItemTemplate',align: 'left',width:80}
];

init();
function init() {
    loadDetail();
    //条件列表搜索
    doList4Item1(1);
    doList4Item2(1);
    // doList4Item3(1);
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
        if(layEvent === 'edit'){ //编辑
            doRefreshAppKey(data);
        }
    });

    table.on('tool(tableItem3)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        let data = obj.data; //获得当前行数据
        let layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        if(layEvent === 'edit'){ //编辑
            setOpenData4Item3('编辑',data);
        }else if(layEvent=='del'){
            doDeletes4Item3([data.id]);
        }
    });
    form.on("submit(LAY-item2-back-add)",function(data){
        doSaveItem2(data);
    });

    //授权api-点击单选按钮切换时间item是否显示
    form.on("radio(LAY-app-step1-time-type)",function (data) {
        if(data.value=='short'){
            $('.grantTime').show();
        }else {
            $('.grantTime').hide();
        }
    });
    form.on("radio(LAY-add-sync-radio-type)",function (data) {
        if(data.value=='condition'){
            $("#syncCondition").show();
        }else {
            $("#syncCondition").hide();
        }
    });
    form.on("select(LAY-add-sync-select-sourceAppId)",function (obj) {
        listApiOptions4App(obj.value,"#addSync_optionSourceApi");
    });
    form.on("submit(LAY-item3-back-add)",function(data){
        doSaveItem3(data);
    });
    let grant;
    //授权api-点击下一步
    form.on("submit(LAY-item1-step1-next)",function (data) {
        grant=data.field;
        if(grant.grantTimeType=='short'&&(!grant.grantTimeEnd||!grant.grantTimeBegin)){
            layer.alert('短期授权必须设置授权起止时间！');
        }else {
            $('#appDetailForm').hide();
            $('#listNext').show();
        }
        return false;
    });
    //授权api-提交
    form.on("submit(LAY-item1-back-add)",function(data){
        let arr = new Array();
        let ids=checkChecked('tableDataApi');
        for(let id of ids){
            let obj={
                "appId":appId,
                "apiId":id,
                "grantTimeType":grant.grantTimeType,
                "grantTimeBegin":grant.grantTimeBegin,
                "grantTimeEnd":grant.grantTimeEnd
            };
            arr.push(obj)
        }
        doAddsItem1(arr);
    });
    //点击按钮操作
    $('.layui-btn').click(function(){
        debugger
        const type = $(this).attr('data-type');
        const dataLayer = $(this).attr('data-layer-html');
        if(type === 'add'){
            if(dataLayer==='addItem1.html'){
                setOpenData4Item1('授权',param4Item1);
            }else if(dataLayer==='addItem3.html'){
                setOpenData4Item3('新建',param4Item3);
            }
        }else if(type === 'del'){
            let ids;
            if(dataLayer==='addItem1.html'){
                ids=checkChecked("tableItem1");
                doDeletes4Item1(ids);
            }else if(dataLayer==='addItem2.html'){
                ids=checkChecked("tableItem2");
                doDeletes4Item2(ids);
            }else if(dataLayer==='addItem3.html'){
                ids=checkChecked("tableItem3");
                doDeletes4Item3(ids);
            }
        }else if(type=='exportExcel'){
            let exportType=$(this).attr('export-type');
            if(exportType==='appApi'){
                exportExcel(service_prefix.gateway,"/appApi/downloadExcel","appApi.xls",param4Item1,columnItem1);
            }
        }
        if(type){return false;}
    });
    //分组下拉选
    getSearchSort("/group/listAll",".groupOption");
    setRender();
    setOpenLayer();
}
//授权api-点击上一步
function appDetailGoBack(){
    $('#listNext').hide();
    $('#appDetailForm').show();
}

function doEditDetail() {
    setOpenData('修改',detailObject);
}

function loadDetail(){
    ajax(service_prefix.gateway, "/app/"+appId,"get").then(function (data){
        if(data.success){
            getData(data.obj,'#baseInfoTemplate','#baseInfoView');
            detailObject=data.obj;
        }else {
            layer.alert(data.msg);
        }
    })
}
function doList4Item1(current) {
    doListTable(service_prefix.gateway, '/appApi/listGrantApi',"post",param4Item1,current,null
        ,"tableItem1",columnItem1,"page_tableItem1");
}

function doList4Item2(current) {
    //渲染数据到table
    let columnItem2 = [
        {field:'appId', title: '应用id',align: 'right',sort: true}
        ,{field:'appSecret', title: '应用key',align: 'left',sort: true}
        ,{field:'', title: '操作',fixed: 'right',toolbar: '#operateItem2Template',align: 'left',width:80}
    ];

    doListTable(service_prefix.gateway, '/appKey/list',"post",param4Item2,current,null
        ,"tableItem2",columnItem2,"page_tableItem2");
}
function doList4Item3(current) {
    //渲染数据到table
    let columnItem3 = [
        {type:'checkbox'}
        ,{field:'rowNum', title: '序号',align: 'right',width:80}
        ,{field:'name', title: '同步名称',align: 'left',sort: true}
        ,{field:'', title: '源应用',align: 'left',sort: true,templet:function (data) {
                return data.dict.sourceAppName;
            }}
        ,{field:'', title: '源API',align: 'left',sort: true,templet:function (data) {
                return data.dict.sourceApiName;
            }}
        ,{field:'', title: '同步类型',align: 'left',sort: true,templet:function (data) {
                return data.dict.type;
            }}
        ,{field:'', title: '目标API',align: 'left',sort: true,templet:function (data) {
                return data.dict.targetApiName;
            }}
        ,{field:'', title: '操作',fixed: 'right',toolbar: '#operateItem3Template',align: 'left',width:120}
    ];

    doListTable(service_prefix.gateway, '/sync/list',"post",param4Item3,current,null
        ,"tableItem3",columnItem3,"page_tableItem3");
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
    let insertBatchUrl = "/appApi/adds";
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
function doSaveItem3(data) {
    let type = "post";
    let url="/sync";
    if(data.field.id){//有id，为修改
        url += "/" +data.field.id;
        type = "put";
    }
    ajax(service_prefix.gateway, url,type,JSON.stringify(data.field)).then(function (data){
        if(data.success){
            doList4Item3(1);
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
//根据ids批量删除
function doDeletes4Item1(data) {
    if(!data||data.length<1){
        layer.alert("请选择要删除的数据！");
        return;
    }
    let param ={
        "appId":appId,
        "apiIds":data
    }
    layer.alert("是否确定删除,该操作不可撤回",function () {
        ajax(service_prefix.gateway, "/appApi/dels","delete",JSON.stringify(param)).then(function (data){
            if(data.success){
                doList4Item1(1);
                layer.closeAll();
            }else{
                layer.alert(data.msg);
            }
        });
    })
}
function doDeletes4Item3(data) {
    if(!data||data.length<1){
        layer.alert("请选择要删除的数据！");
        return;
    }
    layer.alert("是否确定删除,该操作不可撤回",function () {
        ajax(service_prefix.gateway, "/sync","delete",JSON.stringify(data)).then(function (data){
            if(data.success){
                doList4Item3(1);
                layer.closeAll();
            }else{
                layer.alert(data.msg);
            }
        })
    });
}
function doRefreshAppKey(data) {
    layer.alert("是否确定刷新,该操作不可撤回",function () {
        ajax(service_prefix.gateway, "/appKey/addOrRefresh","post",JSON.stringify(data.appId)).then(function (data){
            if(data.success){
                doList4Item2(1);
                layer.closeAll();
            }else{
                layer.alert(data.msg);
            }
        })
    });
}

//搜索select下拉
function getSearchSort(url,clasz,rowSelected,rowDisabled){
    ajax(service_prefix.gateway, url,"post",{}).then(function (data){
        if(data){
            for(let i=0; i<data.obj.length; i++){
                if(rowSelected&&data.obj[i].id==rowSelected.id){
                    $(clasz).append("<option value='"+data.obj[i].id+"' selected>"+data.obj[i].name+"</option>");
                }else if(rowDisabled&&data.obj[i].id==rowDisabled.id){
                    $(clasz).append("<option value='"+data.obj[i].id+"' disabled>"+data.obj[i].name+"</option>");
                }else {
                    $(clasz).append("<option value='"+data.obj[i].id+"'>"+data.obj[i].name+"</option>");
                }
            }
            setRender();
        }else if(rowSelected){
            $(clasz).append("<option value='"+rowSelected.id+"' selected>"+rowSelected.name+"</option>");
        }
    })
}

function setOpenData4Item1(editType,data) {
    let tit = editType + "Api";
    setLayer('table_Item1',tit,'1100px','600px');
    laytpl($('#templateItem1').html()).render(data, function(html){
        $('#table_Item1').html(html);
        setDate('#grantTimeBegin','date');//日期
        setDate('#grantTimeEnd','date');//日期
        doList4Dialog1(1);
        getSearchSort("/group/listAll",".groupOption");
    });
}
function setOpenData4Item3(editType,data) {
    debugger
    let tit = editType + "数据同步";
    setLayer('table_Item3',tit,'700px','600px');
    laytpl($('#templateItem3').html()).render(data, function(html){
        let sourceAppSelected=null;
        if(editType=='编辑'){
            sourceAppSelected={id:data.sourceAppId,name:data.dict.sourceAppName};
            getSearchSort(null,"#addSync_optionSourceApi",{id:data.sourceApiId,name:data.dict.sourceApiName});
        }

        $('#table_Item3').html(html);
        getSearchSort("/app/listAll","#addSync_optionSourceApp",sourceAppSelected,detailObject);
        getSearchSort(null,"#addSync_optionTargeApp",detailObject);
        listApiOptions4App(appId,"#addSync_optionTargeApi");

        setFormVal("formSyncAdd",data);
        setRender();
        if(data.type=='condition'){
            $("#syncCondition").show();
        }else {
            $("#syncCondition").hide();
        }
    });
}
/**
 * 为下拉选获取指定应用下的api（获取的api作为下拉选的选项）
 */
function listApiOptions4App(appId,divId){
    let body={
        pager:{
            current:1,
            size:1000
        },
        entity:{
            appId:appId
        }
    };
    ajax(service_prefix.gateway, "/appApi/listGrantApi","post",JSON.stringify(body)).then(function (data){
        if(data){
            $(divId).empty();
            for(let i=0; i<data.obj.records.length; i++){
                $(divId).append("<option value='"+data.obj.records[i].id+"'>"+data.obj.records[i].name+"</option>");
            }
            setRender();
        }
    });
}
