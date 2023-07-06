//基本信息
let dialogSize=8;//弹窗中表格每页显示的行数
let detailObject;
let groupId = localStorage.getItem('detailId');
let param4Item1={};
param4Item1.groupId=groupId;
let param4Item2={};
param4Item2.groupId=groupId;
let param4RateLimit={};
let param4IpRule={};
let param4App={};
let param4Signature={};

let column4Item1 = [
    {type:'checkbox'}
    ,{field:'rowNum', title: '序号',align: 'right',width:80}
    ,{field:'name', title: '参数中文名',align: 'left',sort: true}
    ,{field:'dict.runType', title: '运行状态',align: 'left',sort: true,templet:function (data) {
            return data.dict.runType;
        }}
    ,{field:'argName', title: '参数名称', align: 'left',sort: true}
    ,{field:'argValue', title: '参数值', align: 'left',sort: true}
    ,{field:'remark', title: '备注', align: 'left',sort: true}
    ,{field:'', title: '操作',fixed: 'right',toolbar: '#operateTemplate',align: 'left',width:120}
];

let column4Item2 = [
    {type:'checkbox'}
    ,{field:'rowNum', title: '序号',align: 'right',width:80}
    ,{field:'modelId', title: '模型id',align: 'left',sort: true}
    ,{field:'columnName', title: '模型项名称',align: 'left',sort: true}
    ,{field:'datatype', title: '数据类型',align: 'left',sort: true}
    ,{field:'dict.isRequired', title: '是否必填', align: 'left',sort: true,templet:function (data) {
            return data.dict.isRequired;
        }}
    ,{field:'max', title: '最大值', align: 'left',sort: true}
    ,{field:'min', title: '最小值', align: 'left',sort: true}
    ,{field:'pattern', title: '正则校验表达式', align: 'left',sort: true}
    ,{field:'', title: '操作',fixed: 'right',toolbar: '#operateTemplate',align: 'left',width:120}
];
init();
function init() {
    // loadDetail();
    //条件列表搜索
    doList4Item1(1);
    doList4Item2(1);
    //搜索提交
    form.on("submit(LAY-item1-back-search)",function(data){
        for(let item in data.field){
            param4Item1[item]=data.field[item];
        }
        doList4Item1(1);
        return false;
    });
    form.on("submit(LAY-item2-back-search)",function(data){
        for(let item in data.field){
            param4Item2[item]=data.field[item];
        }
        doList4Item2(1);
        return false;
    });

    form.on("submit(LAY-rateLimit-back-search)",function(data){
        for(let item in data.field){
            param4RateLimit[item]=data.field[item];
        }
        doList4RateLimit(1);
        return false;
    });
    form.on("submit(LAY-ipRule-back-search)",function(data){
        for(let item in data.field){
            param4IpRule[item]=data.field[item];
        }
        doList4IpRule(1);
        return false;
    });
    form.on("submit(LAY-app-back-search)",function(data){
        for(let item in data.field){
            param4App[item]=data.field[item];
        }
        doList4App(1);
        return false;
    });
    form.on("submit(LAY-signature-back-search)",function(data){
        for(let item in data.field){
            param4Signature[item]=data.field[item];
        }
        doList4Signature(1);
        return false;
    });
    //监听操作列工具条
    table.on('tool(tableItem1)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        let data = obj.data; //获得当前行数据
        let layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        if(layEvent === 'del'){ //删除
            doDeletes4Item1([data.id]);
        }else if(layEvent === 'edit'){ //编辑
            data.editType = 1;
            setOpenData4Item1('编辑',data,"GroupArg");
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
        doSaveItem1(data);
    });
    form.on("submit(LAY-item2-back-add)",function(data){
        doSaveItem2(data);
    });
    form.on("submit(LAY-rateLimit-back-add)",function(data){
        let ids=checkChecked("tableRateLimit");
        if(!ids||ids.length==0){
            layer.alert("请选择限流策略");
            return false;
        }
        let param={
            "target":"rateLimit",
            "targetIds":ids,
            "groupId":groupId
        };
        doSave4Group(param);
    });
    form.on("submit(LAY-ipRule-back-add)",function(data){
        let ids=checkChecked("tableIpRule");
        if(!ids||ids.length==0){
            layer.alert("请选择ip策略");
            return false;
        }
        let param={
            "target":"ipRule",
            "targetIds":ids,
            "groupId":groupId
        };
        doSave4Group(param);
    });
    let grant;
    form.on("submit(LAY-app-stepNext-add)",function (data) {
        grant = data.field;
        if(grant.grantTimeType=='short'&&(!grant.grantTimeEnd||!grant.grantTimeBegin)){
            layer.alert('短期授权必须设置授权起止时间！');
        }else{
            $("#appStep1").hide();
            $("#appStep2").show();
        }
    });
    form.on("submit(LAY-app-back-add)",function(data){
        let ids=checkChecked('tableApp');
        if(!ids||ids.length==0){
            layer.alert("请选择应用");
            return false;
        }
        let param={
            "target":"app",
            "targetIds":ids,
            "groupId":groupId,
            "grantTimeType":grant.grantTimeType,
            "grantTimeBegin":grant.grantTimeBegin,
            "grantTimeEnd":grant.grantTimeEnd
        };
        doSave4Group(param);
    });
    form.on("radio(LAY-app-step1-time-type)",function (data) {
        if(data.value=='short'){
            $('.grantTime').show();
        }else {
            $('.grantTime').hide();
        }
    });
    form.on("submit(LAY-signature-back-add)",function(data){
        let ids=checkChecked("tableSignature");
        if(!ids||ids.length==0){
            layer.alert("请选择签名密钥");
            return false;
        }
        let param={
            "target":"signature",
            "targetIds":ids,
            "groupId":groupId
        };
        doSave4Group(param);
    });
    form.on("submit(LAY-ApiRunType-back-add)",function(data){
        doSaveRunType(data.field);
    });
    form.on("submit(LAY-swaggerImport-back-add)",function (data) {
        doSwaggerImport(data);
    });
    //点击按钮操作
    $('.layui-btn').click(function(){
        const type = $(this).attr('data-type');
        const dataLayer = $(this).attr('data-layer-html');
        if(type === 'add'){
            if(dataLayer==='addItem1.html'){
                setOpenData4Item1('新建',param4Item1);
            }else if(dataLayer==='addItem2.html'){
                setOpenData4Item2('新建',param4Item2);
            }else if(dataLayer==="bindingRateLimit.html"){
                setOpenData4RateLimit(param4RateLimit);
            }else if(dataLayer==="bindingIpRule.html"){
                setOpenData4IpRule(param4IpRule);
            }else if(dataLayer==="bindingApp.html"){
                setOpenData4App(param4App);
            }else if(dataLayer==="bindingSignature.html"){
                setOpenData4Signature(param4Signature);
            }else if(dataLayer==="swaggerImport.html"){
                setOpenData4SwaggerImport(detailObject);
            }
        }else if(type === 'del'){
            let ids;
            if(dataLayer==='addItem1.html'){
                ids=checkChecked("tableItem1");
                doDeletes4Item1(ids);
            }else if(dataLayer==='addItem2.html'){
                ids=checkChecked("tableItem2");
                doDeletes4Item2(ids);
            }else if(dataLayer==="bindingRateLimit.html"){
                doDeletes4Group("rateLimit");
            }else if(dataLayer==="bindingIpRule.html"){
                doDeletes4Group("ipRule");
            }else if(dataLayer==="bindingApp.html"){
                doDeletes4Group("app");
            }else if(dataLayer==="bindingSignature.html"){
                doDeletes4Group("signature");
            }else if(dataLayer==="bindingApiRunType.html"){
                doClearGroup();
            }
        }else if(type=='exportExcel'){
            let exportType=$(this).attr('export-type');
            if(exportType=='groupArg'){
                exportExcel(service_prefix.gateway,"/groupArg/downloadExcel","groupArg.xls",param4Item1,column4Item1);
            }else if(exportType=='groupModelItem'){
                exportExcel(service_prefix.gateway,"/groupModelItem/downloadExcel","groupModelItem.xls",param4Item2,column4Item2);
            }
        }else if(type=='runType'){
            setOpenData4ApiRunType();
        }
        if(type){return false;}
    });

    setOpenLayer();
    setRender();
}
function doEditGroup() {
    setOpenData('修改',detailObject);
}

function loadDetail(){
    ajax(service_prefix.gateway, "/group/"+groupId,"get").then(function (data){
        if(data.success){
            getData(data.obj,'#baseInfoTemplate','#baseInfoView');
            detailObject=data.obj;
        }else {
            layer.alert(data.msg)
        }
    })
}
function doList4Item1(current) {
    doListTable(service_prefix.gateway, "/groupArg/list","post",param4Item1,current,null
        ,"tableItem1",column4Item1,"page_tableItem1")
}
function doList4Item2(current) {
    doListTable(service_prefix.gateway, "/groupModelItem/list","post",param4Item2,current,null
        ,"tableItem2",column4Item2,"page_tableItem2")
}

function doList4RateLimit(current) {
    let column4RateLimit = [
        {type:'checkbox'}
        ,{field:'rowNum', title: '序号',align: 'right',width:80}
        ,{field:'name', title: '限流策略',align: 'left',sort: true}
        ,{field:'', title: '时间单位',align: 'left',sort: true,templet:function (data) {
                return data.dict.timeUnit;
            }}
        ,{field:'limitApi', title: '限流值-Api', align: 'right',sort: true}
        ,{field:'limitUser', title: '限流值-用户', align: 'right',sort: true}
        ,{field:'limitApp', title: '限流值-应用', align: 'right',sort: true}
        ,{field:'remark', title: '备注', align: 'left',sort: true}
    ];
    doListTable(service_prefix.gateway, "/rateLimit/list","post",param4RateLimit,current,null
        ,"tableRateLimit",column4RateLimit,"page_tableRateLimit",dialogSize)
}

function doList4IpRule(current) {
    let column = [
        {type:'checkbox'}
        ,{field:'rowNum', title: '序号',align: 'right',width:80}
        ,{field:'name', title: 'ip规则名',align: 'left',sort: true}
        ,{field:'', title: '规则类型',align: 'left',sort: true,templet:function (data) {
                return data.dict.type;
            }}
        ,{field:'remark', title: '备注', align: 'left',sort: true}
    ];
    doListTable(service_prefix.gateway, "/ipRule/list","post",param4IpRule,current,null
        ,"tableIpRule",column,"page_tableIpRule",dialogSize)
}

function doList4App(current) {
    let column = [
        {type:'checkbox'}
        ,{field:'rowNum', title: '序号',align: 'right',width:80}
        ,{field:'name', title: '应用名称',align: 'left',sort: true}
        ,{field:'description', title: '描述',align: 'left',sort: true}
        ,{field:'remark', title: '备注', align: 'left',sort: true}
    ];
    doListTable(service_prefix.gateway, "/app/list","post",param4App,current,null
        ,"tableApp",column,"page_tableApp",dialogSize)
}

function doList4Signature(current) {
    let column = [
        {type:'checkbox'}
        ,{field:'rowNum', title: '序号',align: 'right',width:80}
        ,{field:'name', title: '名称',align: 'left',sort: true}
        ,{field:'signatureKey', title: '签名', align: 'left',sort: true}
        ,{field:'signatureSecret', title: '密钥', align: 'left',sort: true}
        ,{field:'remark', title: '备注', align: 'left',sort: true}
    ];
    doListTable(service_prefix.gateway, "/signature/list","post",param4Signature,current,null
        ,"tableSignature",column,"page_tableSignature",dialogSize)
}
function doSaveItem1(data) {
    let thisurl = "/groupArg";
    let type = "post";
    if(data.field.id){
        thisurl += "/" +data.field.id;
        type = "put";
    }
    ajax(service_prefix.gateway, thisurl,type,JSON.stringify(data.field)).then(function (data){
        if(data.success){
            doList4Item1(1);
            layer.closeAll();
            layer.msg(data.msg,{
                    time:3000
                },function(){
                    layer.close();
                }
            );
        }else {
            layer.alert(data.msg);
        }
    })
}
function doSaveItem2(data) {
    let thisurl = "/groupModelItem";
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
function doSave4Group(param) {
    ajax(service_prefix.gateway, "/group/saveByGroup","post",JSON.stringify(param)).then(function (data){
        if(data.success){
            layer.closeAll();
        }
        layer.alert(data.msg);
    })
}
function doSaveRunType(param) {
    let data={
        "runType":param.runType,
        "groupId":groupId
    };
    ajax(service_prefix.gateway, "/group/updateApiRunType","post",JSON.stringify(data)).then(function (data){
        if(data.success){
            layer.closeAll();
        }
        layer.alert(data.msg);
    })
}
//根据ids批量删除
function doDeletes4Item1(data) {
    if(!data||data.length<1){
        layer.alert("请选择要删除的数据！");
        return;
    }
    layer.alert("是否确定删除,该操作不可撤回",function () {
        ajax(service_prefix.gateway, "/groupArg","delete",JSON.stringify(data)).then(function (data){
            if(data.success){
                doList4Item1(1);
                layer.closeAll();
            }else{
                layer.alert(data.msg);
            }
        });
    })
}
function doDeletes4Item2(data) {
    if(!data||data.length<1){
        layer.alert("请选择要删除的数据！");
        return;
    }
    layer.alert("是否确定删除,该操作不可撤回",function () {
        ajax(service_prefix.gateway, "/groupModelItem","delete",JSON.stringify(data)).then(function (data){
            if(data.success){
                doList4Item2(1);
                layer.closeAll();
            }else{
                layer.alert(data.msg);
            }
        });
    })
}
function doClearGroup() {

    layer.alert("是否确定清空分组？",function () {
        ajax(service_prefix.gateway, "/group/clearGroup/"+groupId,"delete").then(function (data){
            if(data.success){
                layer.alert('删除成功！')
            }else{
                layer.alert(data.msg);
            }
        });
    });
}
function doDeletes4Group(target) {
    let msg;
    if('rateLimit'==target){
        msg="绑定的限流策略";
    }else if('ipRule'==target){
        msg="绑定的Ip规则";
    }else if('app'==target){
        msg="授权的应用";
    }else if('signature'==target){
        msg="绑定的签名密钥";
    }else {
        layer.alert("未定义的删除目标："+target);
        return ;
    }
    layer.alert("是否确定删除所有"+msg+"?",function () {
        let data={
            "target":target,
            "groupId":groupId
        };
        ajax(service_prefix.gateway, "/group/deleteByGroup","delete",JSON.stringify(data)).then(function (data){
            if(data.success){
                layer.alert('删除成功！')
            }else{
                layer.alert(data.msg);
            }
        });
    });
}

function appStepBack() {
    $("#appStep1").show();
    $("#appStep2").hide();
}

//设置弹出层表单数据（新建、修改）
function setOpenData4Item1(editType,data) {
    let tit = editType + "分组参数";
    setLayer('table_Item1',tit,'700px','400px');
    laytpl($('#templateItem1').html()).render(data, function(html){
        $('#table_Item1').html(html);
        setFormVal('editFormItem1',data);
    });
}
function setOpenData4Item2(editType,data) {
    var tit = editType + "模型项";
    setLayer('table_Item2',tit,'700px','450px');
    laytpl($('#templateItem2').html()).render(data, function(html){
        $('#table_Item2').html(html);
        setFormVal('editFormItem2',data);
    });
}
function setOpenData4RateLimit(data){
    setLayer('table_group',"绑定限流策略",'1100px','600px');
    laytpl($('#templateRateLimit').html()).render(data, function(html){
        $('#table_group').html(html);
        doList4RateLimit(1);
        setRender();
    });
}
function setOpenData4IpRule(data){
    setLayer('table_group',"绑定ip策略",'1100px','600px');
    laytpl($('#templateIpRule').html()).render(data, function(html){
        $('#table_group').html(html);
        doList4IpRule(1);
        setRender();
    });
}
function setOpenData4App(data){
    setLayer('table_group',"授权应用",'1100px','600px');
    laytpl($('#templateApp').html()).render(data, function(html){
        $('#table_group').html(html);
        doList4App(1);
        setDate('#grantTimeBegin','date');//日期
        setDate('#grantTimeEnd','date');//日期
        setRender();
    });
}
function setOpenData4Signature(data){
    setLayer('table_group',"绑定签名密钥",'1100px','600px');
    laytpl($('#templateSignature').html()).render(data, function(html){
        $('#table_group').html(html);
        doList4Signature(1);
    });
}
function setOpenData4ApiRunType(){
    setLayer('table_group',"设置Api的运行状态",'700px','300px');
    laytpl($('#templateApiRunType').html()).render({}, function(html){
        $('#table_group').html(html);
        setRender();
    });
}
function setOpenData4SwaggerImport(data) {
    var tit = "swagger批量导入Api";
    setLayer('table_data',tit,'700px','400px');
    laytpl($('#templateSwaggerImport').html()).render(data, function(html){
        $('#table_data').html(html);
        $("#group4swagger").append("<option value='"+data.id+"'>"+data.name+"</option>");
        setFormVal('formSwaggerImport',data);
        setRender();
    });
    // element.init();
}
function doSwaggerImport(data) {
    const type = "post";
    const url="/router/swaggerImport";
    ajax(service_prefix.gateway, url,type,JSON.stringify(data.field)).then(function (data){
        if(data.success){
            layer.closeAll();
        }
        layer.alert(data.msg);
    });
}
