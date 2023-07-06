var taskId;//任务id（详情页）
var procInstId;//流程id（详情页）
var tabNumber = 1;//标签页
var tableData = {};//元数据具体信息
var tableProperty = [];//元数据属性信息
var metadataUri;//元数据访问url（模块名 + 对象名）
var metadataId;//元数据信息id

doList(1);
initSessionStorage();

//保存当前用户的id、登陆名、真实姓名
function initSessionStorage() {
    ajax(service_prefix.flowable,"/loginUser","get", null).then(res=>{
        sessionStorage.setItem("userId", res.obj.id);
        sessionStorage.setItem("userName", res.obj.name);
        sessionStorage.setItem("userTrueName", res.obj.trueName);
    });
}

//刷新当前标签页的列表
function doList(current){
    if(tabNumber == 2){
        historyWork(current);
    }else if(tabNumber == 3){
        startWork(current);
    }else{
        taskList(current);
    }
}

//数据显示
function showTable(datas,type){
    tabNumber = type;
    var cols = [
        {type: 'numbers', title: '序号', width:80}
    ];
    if(type == 1){
        cols = [
            {type: 'numbers', title: '序号', width:80},
            {field:'taskName',  title: '任务名称'},
            {field:'procName',  title: '所属流程'},
            {field:'startUserName',  title: '流程发起人', width:180},
            {field:'assigneeName',  title: '任务处理人', width:180},
            {field:'taskStartTime',  title: '任务开始时间', templet:"#taskStartTimeTpl"}
        ];
    }else if(type == 2){
        cols = [
            {type: 'numbers', title: '序号', width:80},
            {field:'taskName',  title: '任务名称'},
            {field:'procName',  title: '所属流程'},
            {field:'startUserName',  title: '流程发起人', width:100},
            {field:'assigneeName',  title: '任务处理人', width:100},
            {field:'taskStartTime',  title: '任务开始时间', width:160, templet:"#taskStartTimeTpl"},
            {field:'taskEndTime',  title: '任务结束时间', width:160, templet:"#taskEndTimeTpl"},
            {field:'procStartTime',  title: '流程开始时间', width:160, templet:"#procStartTimeTpl"},
            {field:'procEndTime',  title: '流程结束时间', width:160, templet:"#procEndTimeTpl"},
            {field:'procState',  title: '流程状态', width:100, templet:"#procStateTpl"}
        ];
    }else if(type == 3){
        cols = [
            {type: 'numbers', title: '序号', width:80},
            {field:'procName',  title: '流程名称'},
            {field:'startUserName',  title: '流程发起人', width:180},
            {field:'procStartTime',  title: '流程开始时间', width:160, templet:"#procStartTimeTpl"},
            {field:'procEndTime',  title: '流程结束时间', width:160, templet:"#procEndTimeTpl"},
            {field:'taskName',  title: '当前任务'},
            {field:'assigneeName',  title: '任务处理人', width:180},
            {field:'taskStartTime',  title: '任务开始时间', width:160, templet:"#taskStartTimeTpl"},
            {field:'procState',  title: '流程状态', width:100, templet:"#procStateTpl"}
        ];
    }
    cols.push({field:'operations',  title: '操作', width:200, toolbar:"#operation"});
    layui.table.render({
        elem: '#workTable'
        ,data:datas
        ,limit:15
        ,cols: [cols]
    });
}

//我的待办
function taskList(current){
    ajax(service_prefix.flowable,"/workbench/taskList","post",JSON.stringify({entity:{},pager:{current:current,size:15}})).then(res => {
        showTable(res.obj.records,1);
        page(res.obj.total,current,15,"","");
    });
}
//我的已办
function historyList(current){
    ajax(service_prefix.flowable,"/workbench/historyList","post",JSON.stringify({entity:{},pager:{current:current,size:15}})).then(res => {
        showTable(res.obj.records,2);
        page(res.obj.total,current,15,"","");
    });
}
//我的发起
function procList(current) {
    ajax(service_prefix.flowable,"/workbench/procList","post",JSON.stringify({entity:{},pager:{current:current,size:15}})).then(res => {
        showTable(res.obj.records,3);
        page(res.obj.total,current,15,"","");
    });
}

//显示任务操作对话框
function showDialog(type, titleName){
    $('#label_audit_opinion').html("审批意见：");
    layer.open({
        title: titleName,
        type: 1,
        area: ['700px', '300px'],
        btn: ['确认', '取消'],
        content: $("#task_audit"),
        success: function(layero) {
            //防止遮层覆盖弹出框
            $(".layui-layer-shade").appendTo(layero.parent());
        },
        yes: function(index, layero){
            if(type == 1){
                completeTask();
            }else if(type == 4){
                rollbackTask();
            }
        }
    });
}

//完成任务
function completeTask(){
    var params = {
        id: taskId,
        auditOpinion: $('#audit_opinion').val()
    };
    ajax(service_prefix.flowable,"/completeTask","post", JSON.stringify(params)).then(res=>{
        closeAndRefresh();
    });
}

//驳回任务
function rollbackTask(){
    var param = {
        taskId:taskId,
        auditOpinion: $('#audit_opinion').val()
    };
    ajax(service_prefix.flowable,"/rollbackTask","post",JSON.stringify(param)).then(function(res){
        closeAndRefresh();
    });
}

//关闭弹出框，并刷新列表
function closeAndRefresh(){
    layer.closeAll();
    $('.jilu_list').hide();
    doList(1);
}

//显示详情页对话框
function showDetail(row){
    taskId = row.taskId;
    procInstId = row.procInstId;
    layer.open({
        type:1,
        title:"审核详情",
        area:['1200px', '600px'],
        content: $('.audit_detail'),
        success: function(layero){
            //防止遮层覆盖弹出框
            $(".layui-layer-shade").appendTo(layero.parent());
            //获取数据
            nodeList(row.status);
            getMetaPub();
            operateList();
        },
        cancel: function(){
            //关闭弹出框后，防止数据在列表页显示
            $('.jilu_list').hide();
            //隐藏按钮
            $('#button_agree').hide();
            $('#button_rollback').hide();
        }
    });
}

//环节列表
function nodeList(status){
    ajax(service_prefix.flowable, '/nodeList',"post",JSON.stringify({procInstId:procInstId})).then(res => {
        getData(res.obj,'#nodeListTemplate','#nodeListView');
        if(status == "4"){
            $('.endHover').css("background","url(common/img/flowable/ico_t5.png) no-repeat");
            $('.endHover').css("background-size","100% 100%");
            $('.endHover').css("color","#fff");
        }else{
            $('.endHover').css("background","url(common/img/flowable/ico_t2.png) no-repeat");
            $('.endHover').css("background-size","100% 100%");
            $('.endHover').css("color","#000");
        }
    });
}

//获取元数据公共信息
function getMetaPub(){
    ajax(service_prefix.flowable, '/getMetaPub',"post",JSON.stringify({procInstId:procInstId})).then(res => {
        getData(res.obj, '#myAuditDetailTemplate','#myAuditDetailView');//显示数据
        //根据当前登录用户是否当前处理人，显示可以操作的按钮
        var metadata = res.obj[0];
        metadataId = metadata.metaId;
        var userId = sessionStorage.getItem("userId");//当前用户id
        if(userId == metadata.handlerId){
            //当前用户是审核人，显示同意、驳回按钮
            $('#button_agree').show();
            $('#button_rollback').show();
        }
        getTableProperty(metadata.tableName);
    });
}

//获取操作记录
function operateList(){
    ajax(service_prefix.flowable, '/operateList',"post",JSON.stringify({entity:{procInstId:procInstId},pager:{current:1,size:15}})).then(res => {
        getData(res.obj.records,'#myAuditOperateTemplate','#myAuditOperateView');
        $('.jilu_tit .operateNum').html(res.obj.total);
    });
}

//获取元数据具体信息
function getTableData(table_info_id){
    getMetadataUri(table_info_id).then(function (metadataUriTemp) {
        metadataUri = metadataUriTemp;
        console.log('metadataUri:', metadataUri);
        ajax("", metadataUri + "/" + metadataId, "get", "").then(res => {
            tableData = res.obj;
        });
    })
}

//获取元数据属性
function getTableProperty(table_name){
    //根据表名获取表信息id
    console.log("table_name:", table_name);
    ajax(service_prefix.metadata,"/list","post",JSON.stringify({entity:{tablename: table_name},pager:{current:1,size:15}})).then(res1 => {
        var tableInfoId = res1.obj.records[0].id;
        getTableData(tableInfoId);
        //根据表信息id获取字段信息列表
        console.log("tableInfoId:", tableInfoId);
        ajax(service_prefix.metadata, "/fieldinfo/all", "get", {tableId: tableInfoId}).then(function (res2) {
            tableProperty = res2.obj;
            for (var i in tableProperty) {
                tableProperty[i].forEach(function (f) {
                    if (!f.anothername) f.anothername = f.fieldname;
                })
            }
            console.log("tableProperty:", tableProperty);
        });
    });
}

//查看元数据信息
function showMetaDetail(){
    var param = {data: tableData, fields: tableProperty};
    layerOpenFull("查看元数据信息");
    lodTpl("metadataDetailTpl", "openDiv", param);
}