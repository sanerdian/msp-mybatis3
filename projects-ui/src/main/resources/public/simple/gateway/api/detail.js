//基本信息
let dialogSize=8;//弹窗中表格的行数
let apiId = localStorage.getItem('detailId');
let detailObject;
var dingyuefw = ''
var servMethod = 'get'
var dingyueurl = ''
/*api参数*/
let param4Item1={};
param4Item1.apiId=apiId;
/*限流策略*/
let param4Item2={};
param4Item2.apiId=apiId;
param4Item2.queryType="include";
let param4Dialog2={};
param4Dialog2.apiId=apiId;
param4Dialog2.queryType="exclude";
/*ip策略*/
let param4Item3={};
param4Item3.apiId=apiId;
param4Item3.queryType="include";
let param4Dialog3={};
param4Dialog3.apiId=apiId;
param4Dialog3.queryType="exclude";
/*应用*/
let param4Item4={};
param4Item4.apiId=apiId;
param4Item4.queryType="include";
let param4Dialog4={};
param4Dialog4.apiId=apiId;
param4Dialog4.queryType="exclude";
/*签名密钥*/
let param4Item5={};
param4Item5.apiId=apiId;
param4Item5.queryType="include";
let param4Dialog5={};
param4Dialog5.apiId=apiId;
param4Dialog5.queryType="exclude";
/*数据预览*/
let param4Item6={};
param4Item6.apiId=apiId;
param4Item6.queryType="include";
let param4Dialog6={};
param4Dialog6.apiId=apiId;
param4Dialog6.queryType="exclude";
/*订阅情况*/
let param4Item7={};
param4Item7.apiId=apiId;
param4Item7.status="ok";
let param4Dialog7={};
param4Dialog7.apiId=apiId;
param4Dialog7.queryType="exclude";
/*数据预览字段*/
let param4Item8={};
param4Item8.apiId=apiId;
param4Item8.status="ok";
let param4Dialog8={};
param4Dialog8.apiId=apiId;
param4Dialog8.queryType="exclude";
let column4Item1 = [
    {type:'checkbox'}
    ,{field:'rowNum', title: '序号',align: 'right',width:80}
    ,{field:'name', title: '参数中文含义',align: 'left',sort: true}
    ,{field:'dict.paramType', title: '参数类别',align: 'left',sort: true,templet:function (data) {
            return data.dict.paramType;
        }}
    // ,{field:'dict.isRequired', title: '是否必须', align: 'left',sort: true,templet:function (data) {
    //         return data.dict.isRequired;
    //     }}
    ,{field:'custParamName', title: '入参名称', align: 'left',sort: true}
    ,{field:'dict.custParamPosition', title: '入参位置', align: 'left',sort: true,templet:function (data) {
            return data.dict.custParamPosition;
        }}
    ,{field:'custParamDatatype', title: '入参数据类型', align: 'left',sort: true}
    ,{field:'servParamName', title: '出参名称', align: 'left',sort: true}
    ,{field:'servParamValueDefault', title: '出参默认值', align: 'left',sort: true}
    ,{field:'dict.servParamPosition', title: '出参位置', align: 'left',sort: true,templet:function (data) {
            return data.dict.servParamPosition;
        }}
    ,{field:'servParamDatatype', title: '出参数据类型', align: 'left',sort: true}
    ,{field:'', title: '操作',fixed: 'right',toolbar: '#operateItemTemplate1',align: 'left',width:120}
];
function baseInfo(){
    init();
}
baseInfo();

function init() {
    loadDetail();
    doList4Item1(1);
    doList4Item2(1);
    doList4Item3(1);
    doList4Item4(1);
    doList4Item5(1);
    doList4Item8(1)
    doList4Item7(1);
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
    form.on("submit(LAY-item3-back-search)",function(data){
        Object.assign(param4Item3,data.field)
        doList4Item3(1);
        return false;
    });
    form.on("submit(LAY-item4-back-search)",function(data){
        Object.assign(param4Item4,data.field)
        doList4Item4(1);
        return false;
    });
    form.on("submit(LAY-item5-back-search)",function(data){
        Object.assign(param4Item5,data.field)
        doList4Item5(1);
        return false;
    });
    form.on("submit(LAY-item6-back-search)",function(data){
        Object.assign(param4Item6,data.field)
        doList4Item6(1);
        return false;
    });
    form.on("submit(LAY-item7-back-search)",function(data){
        Object.assign(param4Item7,data.field)
        doList4Item7(1);
        return false;
    });
    form.on("submit(LAY-dialog2-back-search)",function(data){
        Object.assign(param4Dialog2,data.field)
        doList4Dialog2(1);
        return false;
    });
    form.on("submit(LAY-dialog3-back-search)",function(data){
        Object.assign(param4Dialog3,data.field)
        doList4Dialog3(1);
        return false;
    });
    form.on("submit(LAY-dialog4-back-search)",function(data){
        Object.assign(param4Dialog4,data.field)
        doList4Dialog4(1);
        return false;
    });
    form.on("submit(LAY-dialog5-back-search)",function(data){
        Object.assign(param4Dialog5,data.field)
        doList4Dialog5(1);
        return false;
    });
    // form.on("submit(LAY-dialog6-back-search)",function(data){
    //     Object.assign(param4Dialog6,data.field)
    //     doList4Dialog6(1);
    //     return false;
    // });
    form.on("submit(LAY-dialog7-back-search)",function(data){
        Object.assign(param4Dialog7,data.field)
        doList4Dialog7(1);
        return false;
    });
    //api参数，参数类型下拉选，变更后输入项隐藏/显示
    form.on("select(LAY-item1-select-apiParamType)",function (data) {
        if(data.value=='normal'){
            $('.apiParamIn').show();
        }else {
            $('.apiParamIn').hide();
        }
    });
    //表单提交
    form.on("submit(LAY-item1-back-add)",function(data){
        if(data.field.paramType=="normal"&&!data.field.custParamName){
            layer.alert("普通参数的入参名称不可缺少！")
        }else {
            doSaveItem1(data);
        }
    });
    form.on("submit(LAY-item2-back-add)",function(data){
        let ids=checkChecked('tableDialog2');
        doSaveItem2(ids);
    });
    form.on("submit(LAY-item3-back-add)",function(data){
        let ids=checkChecked('tableDialog3');
        doSaveItem3(ids);
    });

    //授权api-点击单选按钮切换时间item是否显示
    form.on("radio(LAY-item4-step1-time-type)",function (data) {
        if(data.value=='short'){
            $('.grantTime').show();
        }else {
            $('.grantTime').hide();
        }
    });
    let grant;
    form.on("submit(LAY-dialog4-step1-search)",function (data) {
        grant=data.field;
        if(grant.grantTimeType=='short'&&(!grant.grantTimeEnd||!grant.grantTimeBegin)){
            layer.alert('短期授权必须设置授权起止时间！');
        }else{
            $("#item4Step1").hide();
            $("#item4Step2").show();
        }
    });
    form.on("submit(LAY-item4-back-add)",function(data){
        let ids=checkChecked('tableDialog4');
        let arr = new Array();
        for(let id of ids){
            let obj={
                "appId":id,
                "apiId":apiId,
                "grantTimeType":grant.grantTimeType,
                "grantTimeBegin":grant.grantTimeBegin,
                "grantTimeEnd":grant.grantTimeEnd
            };
            arr.push(obj)
        }
        doSaveItem4(arr);
    });
    form.on("submit(LAY-item5-back-add)",function(data){
        let ids=checkChecked('tableDialog5');
        doSaveItem5(ids);
    });
    form.on("submit(LAY-item8-back-add)",function(data){
        data.field.apiId = apiId
        console.log(data)
        doSaveItem8(data);
    });
    form.on("submit(LAY-item7-back-add)",function(data){
        let ids=checkChecked('tableDialog7');
        doSaveItem7(ids);
    });
    //监听操作列工具条
    table.on('tool(tableItem1)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        let data = obj.data; //获得当前行数据
        let layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        if(layEvent === 'del'){ //删除
            doDeletes4Item1([data.id]);
        }else if(layEvent === 'edit'){ //编辑
            data.editType = 1;
            setOpenDate4Item1('编辑',data,1);
        }
    });
    table.on('tool(tableItem2)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        let data = obj.data; //获得当前行数据
        let layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        if(layEvent === 'del'){ //删除
            doDeletes4Item2([data.id]);
        }
    });
    table.on('tool(tableItem3)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        let data = obj.data; //获得当前行数据
        let layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        if(layEvent === 'del'){ //删除
            doDeletes4Item3([data.id]);
        }
    });
    table.on('tool(tableItem4)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        let data = obj.data; //获得当前行数据
        let layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        if(layEvent === 'del'){ //删除
            doDeletes4Item4([data.id]);
        }
    });

    table.on('tool(tableItem5)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        let data = obj.data; //获得当前行数据
        let layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        if(layEvent === 'del'){ //删除
            doDeletes4Item5([data.id]);
        }
    });
    table.on('tool(tableItem7)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        let data = obj.data; //获得当前行数据
        let layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        if(layEvent === 'del'){ //删除
            doDeletes4Item7([data.id]);
        }
    });
    table.on('tool(tableItem8)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        let data = obj.data; //获得当前行数据
        let layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        if(layEvent === 'del'){ //删除
            doDeletes4Item8([data.id]);
        }else if(layEvent === 'edit'){ //编辑
            data.editType = 1;
            setOpenData4Item8('编辑',data,1);
        }
    });
    //点击按钮操作
    $('.layui-btn').click(function(){
        const type = $(this).attr('data-type');
        const dataLayer = $(this).attr('data-layer-html');
        if(type === 'add'){
            if(dataLayer==='addItem1.html'){
                setOpenDate4Item1('新建',param4Item1);
            }else if(dataLayer==='addItem2.html'){
                setOpenData4Item2("绑定",param4Item2);
            }else if(dataLayer==='addItem3.html'){
                setOpenData4Item3("绑定",param4Item3);
            }else if(dataLayer==='addItem4.html'){
                setOpenData4Item4("授权",param4Item4);
            }else if(dataLayer==='addItem5.html'){
                setOpenData4Item5("绑定",param4Item5);
            }else if(dataLayer==='addItem6.html'){
                setOpenData4Item8("新建",param4Item8);
            }else if(dataLayer==='addItem7.html'){
                setOpenData4Item7("绑定",param4Item7);
            }

        }else if(type === 'del'){
            if(dataLayer==='addItem1.html'){
                let ids=checkChecked("tableItem1");
                doDeletes4Item1(ids);
            }else if(dataLayer==='addItem2.html'){
                let ids=checkChecked("tableItem2");
                doDeletes4Item2(ids);
            }else if(dataLayer==='addItem3.html'){
                let ids=checkChecked("tableItem3");
                doDeletes4Item3(ids);
            }else if(dataLayer==='addItem4.html'){
                let ids=checkChecked("tableItem4");
                doDeletes4Item4(ids);
            }else if(dataLayer==='addItem5.html'){
                let ids=checkChecked("tableItem5");
                doDeletes4Item5(ids);
            }else if(dataLayer==='addItem6.html'){
                let ids=checkChecked("tableItem8");
                doDeletes4Item8(ids);
            }else if(dataLayer==='addItem7.html'){
                let ids=checkChecked("tableItem7");
                doDeletes4Item7(ids);
            }
        }else if(type=='exportExcel'){
            let exportType=$(this).attr('export-type');
            if(exportType=='apiParam'){
                exportExcel(service_prefix.gateway, "/routerParam/downloadExcel","routerParam.xls",param4Item1,column4Item1);
            }
            if(exportType=='apiParam6'){
                exportExcel(service_prefix.gateway, "/routerParam/downloadExcel","routerParam.xls",param4Item6,column4Item6);
            }
        }
        if(type){return false;}
    });
    //加载弹出框
    setOpenLayer();
    setRender();
}

function item4StepBack() {
    $("#item4Step1").show();
    $("#item4Step2").hide();
}

function loadDetail() {
    ajax(service_prefix.gateway, "/router/"+apiId,"get").then(function (data){
        if(data.success){
            detailObject=data.obj;
            getData(data.obj,'#baseInfoTemplate','#baseInfoView');
            servMethod = data.obj.servMethod
            dingyuefw = data.obj.servServiceId
            dingyueurl = data.obj.servUri
            doList4Item6(1);
        }else {
            layer.alert(data.msg);
        }
    });
}

//条件列表搜索
function doList4Item1(current) {

    doListTable( service_prefix.gateway, "/routerParam/list","post",param4Item1,current,null
        ,"tableItem1",column4Item1,"page_tableItem1");
}
function doList4Dialog2(current) {
    let column4Dialog2 = [
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
    doListTable(service_prefix.gateway, "/rateLimit/list","post",param4Dialog2,current,null
        ,"tableDialog2",column4Dialog2,"page_tableDialog2",dialogSize);
}
function doList4Item2(current) {
    let column4Item2 = [
        {type:'checkbox'}
        ,{field:'rowNum', title: '序号',align: 'right',width:80}
        ,{field:'name', title: '限流策略',align: 'left',sort: true}
        ,{field:'dict.timeUnit', title: '时间单位',align: 'left',sort: true,templet:function (data) {
                return data.dict.timeUnit;
            }}
        ,{field:'limitApi', title: '限流值-Api', align: 'right',sort: true}
        ,{field:'limitUser', title: '限流值-用户', align: 'right',sort: true}
        ,{field:'limitApp', title: '限流值-应用', align: 'right',sort: true}
        ,{field:'remark', title: '备注', align: 'left',sort: true}
        ,{field:'', title: '操作',fixed: 'right',toolbar: '#operateItemTemplate2',align: 'left',width:80}
    ];
    doListTable(service_prefix.gateway, "/rateLimit/list","post",param4Item2,current,null
        ,"tableItem2",column4Item2,"page_tableItem2");
}
function doList4Item3(current) {
     let column4Item3 = [
        {type:'checkbox'}
         ,{field:'rowNum', title: '序号',align: 'right',width:80}
        ,{field:'name', title: 'ip规则名',align: 'left',sort: true}
        ,{field:'', title: '规则类型',align: 'left',sort: true,templet:function (data) {
                return data.dict.type;
            }}
        ,{field:'remark', title: '备注', align: 'left',sort: true}
        ,{field:'', title: '操作',fixed: 'right',toolbar: '#operateItemTemplate2',align: 'left',width:110}
    ];
    doListTable(service_prefix.gateway, "/ipRule/list","post",param4Item3,current,null
        ,"tableItem3",column4Item3,"page_tableItem3");
}
function doList4Dialog3(current) {
    let column4Dialog3 = [
        {type:'checkbox'}
        ,{field:'rowNum', title: '序号',align: 'right',width:80}
        ,{field:'name', title: 'ip规则名',align: 'left',sort: true}
        ,{field:'dict.type', title: '规则类型',align: 'left',sort: true,templet:function (data) {
                return data.dict.type;
            }}
        ,{field:'remark', title: '备注', align: 'left',sort: true}
    ];
    doListTable(service_prefix.gateway, "/ipRule/list","post",param4Dialog3,current,null
        ,"tableDialog3",column4Dialog3,"page_tableDialog3",dialogSize);
}
function doList4Item4(current) {
    let column4Item4 = [
        {type:'checkbox'}
        ,{field:'rowNum', title: '序号',align: 'right',width:80}
        ,{field:'name', title: '应用名称',align: 'left',sort: true}
        ,{field:'dict.grantTimeType', title: '授权类别',width:110, align: 'left',sort: true,templet:function (item) {
                return item.dict.grantTimeType;
            }}
        ,{field:'grantTimeEnd', title: '授权开始日期', align: 'left',sort: true,width:130}
        ,{field:'grantTimeEnd', title: '授权截至日期', align: 'left',sort: true,width:130}
        ,{field:'description', title: '描述',align: 'left',sort: true}
        ,{field:'remark', title: '备注', align: 'left',sort: true}

        ,{field:'', title: '操作',fixed: 'right',toolbar: '#operateItemTemplate2',align: 'left',width:110}
    ];
    doListTable(service_prefix.gateway, "/appApi/listByApp","post",param4Item4,current,null
        ,"tableItem4",column4Item4,"page_tableItem4");
}
function doList4Dialog4(current) {
    let column4Dialog4 = [
        {type:'checkbox'}
        ,{field:'rowNum', title: '序号',align: 'right',width:80}
        ,{field:'name', title: '应用名称',align: 'left',sort: true}
        ,{field:'description', title: '描述',align: 'left',sort: true}
        ,{field:'remark', title: '备注', align: 'left',sort: true}
    ];
    doListTable(service_prefix.gateway, "/app/list","post",param4Dialog4,current,null
        ,"tableDialog4",column4Dialog4,"page_tableDialog4",dialogSize);
}
function doList4Item5(current) {
    let column4Item5 = [
        {type:'checkbox'}
        ,{field:'rowNum', title: '序号',align: 'right',width:80}
        ,{field:'name', title: '名称',align: 'left',sort: true}
        ,{field:'signatureKey', title: '签名', align: 'left',sort: true}
        ,{field:'signatureSecret', title: '密钥', align: 'left',sort: true}
        ,{field:'remark', title: '备注', align: 'left',sort: true}
        ,{field:'', title: '操作',fixed: 'right',toolbar: '#operateItemTemplate2',align: 'left',width:110}
    ];
    doListTable(service_prefix.gateway, "/signature/list","post",param4Item5,current,null
        ,"tableItem5",column4Item5,"page_tableItem5");
}
function doList4Dialog5(current) {
    let column4Dialog5 = [
        {type:'checkbox'}
        ,{field:'rowNum', title: '序号',align: 'right',width:80}
        ,{field:'name', title: '名称',align: 'left',sort: true}
        ,{field:'signatureKey', title: '签名', align: 'left',sort: true}
        ,{field:'signatureSecret', title: '密钥', align: 'left',sort: true}
        ,{field:'remark', title: '备注', align: 'left',sort: true}
    ];
    doListTable(service_prefix.gateway, "/signature/list","post",param4Dialog5,current,null
        ,"tableDialog5",column4Dialog5,"page_tableDialog5",dialogSize);
}
function doList4Item6(current) {
    let column4Item6 = [
        {type:'checkbox'}
        ,{field:'rowNum', title: '序号',align: 'center',width:80}
    ];
    ajax(service_prefix.gateway, 'routerResponseField/layui?apiId='+apiId,'get',{}).then(function (data){
        if(data.success){
            console.log(data)
            $.each(data.obj, function (index, item) { 
                column4Item6.push(item)
            });
            // column4Item6.push({field:'', title: '操作',toolbar: '#operateItemTemplate1',align: 'left',width:150})
            // fixed: 'right',
            doListTable(dingyuefw, dingyueurl,servMethod,param4Item6,current,null
                ,"tableItem6",column4Item6,"page_tableItem6");
        }else{
            layer.alert(data.msg);
        }
    });
}
function doList4Item7(current) {
    let column4Item7 = [
        {type:'checkbox'}
        ,{field:'rowNum', title: '序号',align: 'right',width:80}
        ,{field:'name', title: '订阅用户',align: 'left',sort: true}
        ,{field:'createTime', title: '订阅时间',align: 'left',sort: true}
    ];
    doListTable(service_prefix.gateway, "/apply/api/list","post",param4Item7,current,null
        ,"tableItem7",column4Item7,"page_tableItem7");
}
function doList4Dialog7(current) {
    let column4Dialog7 = [
        {type:'checkbox'}
        ,{field:'rowNum', title: '序号',align: 'right',width:80}
        ,{field:'name', title: '订阅用户',align: 'left',sort: true}
        ,{field:'createTime', title: '订阅时间',align: 'left',sort: true}
    ];
    doListTable(service_prefix.gateway, "/apply/api/list","post",param4Dialog7,current,null
        ,"tableDialog7",column4Dialog7,"page_tableDialog7",dialogSize);
}
function doList4Item8(current) {
    let column4Item8 = [
        {type:'checkbox'}
        ,{field:'rowNum', title: '序号',align: 'right',width:80}
        ,{field:'apiId', title: 'API的主键',align: 'left',sort: true}
        ,{field:'field', title: '字段名',align: 'left',sort: true}
        ,{field:'title', title: '字段的中文名',align: 'left',sort: true}
        ,{field:'align', title: 'align',align: 'left',sort: true}
        ,{field:'width', title: '字段宽度',align: 'left',sort: true}
        ,{field:'seqencing', title: '排序号',align: 'left',sort: true}
        ,{field:'', title: '操作',fixed: 'right',toolbar: '#operateItemTemplate1',align: 'left',width:150}
    ];
    doListTable(service_prefix.gateway, "/routerResponseField/list","post",param4Item8,current,null
        ,"tableItem8",column4Item8,"page_tableItem8");
}
function doList4Dialog8(current) {
    let column4Dialog8 = [
        {type:'checkbox'}
        ,{field:'rowNum', title: '序号',align: 'right',width:80}
        ,{field:'apiId', title: 'API的主键',align: 'left',sort: true}
        ,{field:'field', title: '字段名',align: 'left',sort: true}
        ,{field:'title', title: '字段的中文名',align: 'left',sort: true}
        ,{field:'align', title: 'align',align: 'left',sort: true}
        ,{field:'width', title: '字段宽度',align: 'left',sort: true}
        ,{field:'seqencing', title: '排序号',align: 'left',sort: true}
    ];
    doListTable(service_prefix.gateway, "/routerResponseField/list","post",param4Dialog8,current,null
        ,"tableDialog8",column4Dialog8,"page_tableDialog8",dialogSize);
}

function editDetail(){
    setOpenData('修改',detailObject);
}


//设置弹出层表单数据（新建、修改）
function setOpenDate4Item1(editType,data) {
    var tit = editType + 'Api参数';
    setLayer('table_item1',tit,'700px','600px');
    laytpl($('#templateItem1').html()).render({}, function(html){
        $('#table_item1').html(html);
        setFormVal('editFormItem1',data);
        if(!data.paramType||data.paramType=='normal'){//新增时没有值
            $('.apiParamIn').show();
        }else {
            $('.apiParamIn').hide();
        }
    });

    // element.init();
}
function setOpenData4Item2 (editType,data) {
    var tit = editType + '限流策略';
    setLayer('table_item',tit,'1100px','600px');
    laytpl($('#templateItem2').html()).render({}, function(html){
        $('#table_item').html(html);
        doList4Dialog2(1);
        setRender();
    });
}
function setOpenData4Item3 (editType,data) {
    var tit = editType + 'ip策略';
    setLayer('table_item',tit,'1100px','600px');
    laytpl($('#templateItem3').html()).render(data, function(html){
        $('#table_item').html(html);
        doList4Dialog3(1);
        setRender();
    });
}
function setOpenData4Item4 (editType,data) {
    var tit = editType + '应用';
    setLayer('table_item',tit,'1100px','600px');
    laytpl($('#templateItem4').html()).render(data, function(html){
        $('#table_item').html(html);
        setDate('#grantTimeBegin','date');//日期
        setDate('#grantTimeEnd','date');//日期
        doList4Dialog4(1);
        setRender();
    });
}
function setOpenData4Item5 (editType,data) {
    var tit = editType + '签名密钥';
    setLayer('table_item',tit,'1100px','600px');
    laytpl($('#templateItem5').html()).render(data, function(html){
        $('#table_item').html(html);
        doList4Dialog5(1);
        setRender();
    });
}
// function setOpenData4Item6 (editType,data) {
//     var tit = editType + '数据预览';
//     setLayer('table_item',tit,'1100px','600px');
//     laytpl($('#templateItem6').html()).render(data, function(html){
//         $('#table_item').html(html);
//         doList4Dialog5(1);
//         setRender();
//     });
// }
function setOpenData4Item7 (editType,data) {
    var tit = editType + '订阅情况';
    setLayer('table_item',tit,'1100px','600px');
    laytpl($('#templateItem7').html()).render(data, function(html){
        $('#table_item').html(html);
        doList4Dialog5(1);
        setRender();
    });
}
function setOpenData4Item8 (editType,data) {
    var tit = editType + '数据预览字段';
    setLayer('table_item8',tit,'1100px','600px');
    laytpl($('#templateItem8').html()).render({}, function(html){
        $('#table_item8').html(html);
        setFormVal('editFormItem8',data);
        console.log(data)
        // if(!data.paramType||data.paramType=='normal'){//新增时没有值
        //     $('.apiParamIn').show();
        // }else {
        //     $('.apiParamIn').hide();
        // }
    });
}
function doSaveItem1(data) {
    let saveUrl = "/routerParam";
    let type = "post";
    if(data.field.id){
        saveUrl += "/" +data.field.id;
        type = "put";
    }
    ajax(service_prefix.gateway, saveUrl,type,JSON.stringify(data.field)).then(function (data){
        if(data.success){
            doList4Item1(1,param4Item1);
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
function doSaveItem2(ids) {
    if(!ids||ids.length==0){
        layer.alert("请选择数据");
        return false;
    }
    let item = new Array();
    for(let id of ids){
        let obj={"rateLimitId":id,"apiId":apiId};
        item.push(obj)
    }
    ajax(service_prefix.gateway, "/rateLimitApi/adds","post",JSON.stringify(item)).then(function (data){
        if(data.success){
            doList4Item2(1,param4Item2);
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
function doSaveItem3(ids) {
    if(!ids||ids.length==0){
        layer.alert("请选择数据");
        return false;
    }
    let item = new Array();
    for(let id of ids){
        let obj={"ipRuleId":id,"apiId":apiId};
        item.push(obj)
    }
    ajax(service_prefix.gateway, "/ipRuleApi/adds","post",JSON.stringify(item)).then(function (data){
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
function doSaveItem4(items) {
    ajax(service_prefix.gateway, "/appApi/adds","post",JSON.stringify(items)).then(function (data){
        if(data.success){
            doList4Item4(1);
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
function doSaveItem5(ids) {
    if(!ids||ids.length==0){
        layer.alert("请选择数据");
        return false;
    }
    let item = new Array();
    for(let id of ids){
        let obj={"signatureId":id,"apiId":apiId};
        item.push(obj)
    }
    ajax(service_prefix.gateway, "/signatureApi/adds","post",JSON.stringify(item)).then(function (data){
        if(data.success){
            doList4Item5(1);
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
function doSaveItem6(items) {
    ajax(service_prefix.gateway, "/apply/api/list","post",JSON.stringify(items)).then(function (data){
        if(data.success){
            doList4Item6(1);
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
function doSaveItem7(items) {
    ajax(service_prefix.gateway, "/apply/api/list","post",JSON.stringify(items)).then(function (data){
        if(data.success){
            doList4Item7(1);
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
function doSaveItem8(data) {
    let saveUrl = "/routerResponseField";
    let type = "post";
    if(data.field.id){
        saveUrl += "/" +data.field.id;
        type = "put";
    }
    ajax(service_prefix.gateway, saveUrl,type,JSON.stringify(data.field)).then(function (data){
        if(data.success){
            doList4Item8(1,param4Item8);
            doList4Item6(1);
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
    if(!data||data.length==0){
        layer.alert("请选择数据");
        return false;
    }
    layer.alert("是否确定删除,该操作不可撤回",function () {
        ajax(service_prefix.gateway, "//routerParam","delete",JSON.stringify(data)).then(function (data){
            if(data.success){
                doList4Item1(1);
                layer.closeAll();
            }else{
                layer.alert(data.msg);
            }
        });
    });
}
function doDeletes4Item2(data) {
    if(!data||data.length==0){
        layer.alert("请选择数据");
        return false;
    }
    let param ={
        "rateLimitIds":data,
        "apiId":apiId
    }
    layer.alert("是否确定删除,该操作不可撤回",function () {
        ajax(service_prefix.gateway, "//rateLimitApi/dels","delete",JSON.stringify(param)).then(function (data){
            if(data.success){
                doList4Item2(1);
                layer.closeAll();
            }else{
                layer.alert(data.msg);
            }
        });
    });
}
function doDeletes4Item3(data) {
    if(!data||data.length==0){
        layer.alert("请选择数据");
        return false;
    }
    let param ={
        "ipRuleIds":data,
        "apiId":apiId
    }
    layer.alert("是否确定删除,该操作不可撤回",function () {
        ajax(service_prefix.gateway, "//ipRuleApi/dels","delete",JSON.stringify(param)).then(function (data){
            if(data.success){
                doList4Item3(1);
                layer.closeAll();
            }else{
                layer.alert(data.msg);
            }
        });
    });
}
function doDeletes4Item4(data) {
    if(!data||data.length==0){
        layer.alert("请选择数据");
        return false;
    }
    let param ={
        "appIds":data,
        "apiId":apiId
    }
    layer.alert("是否确定删除,该操作不可撤回",function () {
        ajax(service_prefix.gateway, "//appApi/dels","delete",JSON.stringify(param)).then(function (data){
            if(data.success){
                doList4Item4(1);
                layer.closeAll();
            }else{
                layer.alert(data.msg);
            }
        });
    });
}
function doDeletes4Item5(data) {
    if(!data||data.length==0){
        layer.alert("请选择数据");
        return false;
    }
    let param ={
        "signatureIds":data,
        "apiId":apiId
    }
    layer.alert("是否确定删除,该操作不可撤回",function () {
        ajax(service_prefix.gateway, "//signatureApi/dels","delete",JSON.stringify(param)).then(function (data){
            if(data.success){
                doList4Item5(1);
                layer.closeAll();
            }else{
                layer.alert(data.msg);
            }
        });
    });
}
function doDeletes4Item8(data) {
    if(!data||data.length==0){
        layer.alert("请选择数据");
        return false;
    }
    layer.alert("是否确定删除,该操作不可撤回",function () {
        ajax(service_prefix.gateway, "/routerResponseField","delete",JSON.stringify(data)).then(function (data){
            if(data.success){
                doList4Item8(1);
                layer.closeAll();
            }else{
                layer.alert(data.msg);
            }
        });
    });
}
// function doDeletes4Item7(data) {
//     if(!data||data.length==0){
//         layer.alert("请选择数据");
//         return false;
//     }
//     let param ={
//         "signatureIds":data,
//         "apiId":apiId
//     }
//     layer.alert("是否确定删除,该操作不可撤回",function () {
//         ajax(service_prefix.gateway, "","delete",JSON.stringify(param)).then(function (data){
//             if(data.success){
//                 doList4Item7(1);
//                 layer.closeAll();
//             }else{
//                 layer.alert(data.msg);
//             }
//         });
//     });
// }


