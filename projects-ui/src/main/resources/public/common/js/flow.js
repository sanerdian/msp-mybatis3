//保存当前用户的id、登陆名、真实姓名
function initSessionStorage() {
    ajax(service_prefix.flowable,"/loginUser","get", null).then(res=>{
        if(res.success){
            sessionStorage.setItem("userId", res.obj.id);
            sessionStorage.setItem("userName", res.obj.name);
            sessionStorage.setItem("userTrueName", res.obj.trueName);
        }else{
            console.log(res.msg);
        }
    });
}

var taskId;//任务id（详情页）
var tabNumber = 5;//标签页
doList(1);
//刷新当前标签页的列表
function doList(current){
    if(tabNumber == 2){
        historyWork(current);
    }else if(tabNumber == 3){
        startWork(current);
    }else if(tabNumber == 4){
        focusWork(current);
    }else if(tabNumber == 5){
        newsList(current);
    }else{
        taskList(current);
    }
}

//数据显示
function showTable(datas,type){
    tabNumber = type;
    var cols = [
        {field:'id', title: 'ID', width:200},
        {field:'newsTitle',  title: '新闻标题'},
        {field:'newsClassify',  title: '新闻分类', width:100},
        {field:'procStartName',  title: '流程发起人', width:100},
        {field:'doctitle',  title: '当前处理人', width:100},
        {field:'opertime',  title: '审核日期', width:200},
        {field:'status',  title: '审核状态', width:100, templet:"#statusTpl"},
        {field:'linkurl',  title: '发布状态', width:100, templet:"#linkurlTpl"},
        {field:'operuser',  title: '审核结果', width:100, templet:"#operuserTpl"}
    ];
    if(type == 4){//我的关注
        cols = [
            {field:'id', title: '流水号'},
            {field:'taskName',  title: '工作名称'},
            {field:'processDefinitionName',  title: '所属流程'},
            {field:'startUserName',  title: '流程发起人'},
            {field:'assigneeName',  title: '当前处理人'},
            {field:'state',  title: '状态', templet:"#stateTpl"}
        ];
    }
    cols.push({field:'operations',  title: '操作', width:300, toolbar:"#operation"});
    layui.table.render({
        elem: '#workTable'
        ,data:datas
        ,limit:15
        ,cols: [cols]
    });
}

//我的待办
function taskList(current){
    ajax(service_prefix.flowable,"/news/notDisposeList","post",JSON.stringify({entity:{},pager:{current:current,size:15}})).then(res => {
        showTable(res.obj.records,1);
        page(res.obj.total,current,15,"","");
    });
}
//我的已办
function historyWork(current){
    ajax(service_prefix.flowable,"/news/disposedList","post",JSON.stringify({entity:{},pager:{current:current,size:15}})).then(res => {
        showTable(res.obj.records,1);
        page(res.obj.total,current,15,"","");
    });
}
//我的发起
function startWork(current) {
    var userId = sessionStorage.getItem("userId");
    ajax(service_prefix.flowable,"/news/list","post",JSON.stringify({entity:{procStartId:userId},pager:{current:current,size:15}})).then(res => {
        showTable(res.obj.records,3);
        page(res.obj.total,current,15,"","");
    });
}
//我的关注
function focusWork(current){
    ajax(service_prefix.flowable,"/focusTaskList","post",JSON.stringify({entity:{},pager:{current:current,size:15}})).then(res => {
        showTable(res.obj.records,4);
        page(res.obj.total,current,15,"","");
    });
}
//示例列表
function newsList(current){
    ajax(service_prefix.flowable,"/news/list","post",JSON.stringify({entity:{},pager:{current:current,size:15}})).then(res => {
        showTable(res.obj.records,5);
        page(res.obj.total,current,15,"","");
    });
}

//选择流程下拉框
function initSelect(){
    ajax(service_prefix.flowable,"/process","post",{}).then(function(res){
        $.each(res.obj, function (index, item) {
            $('#selectProc').append(new Option(item.name, item.id));// 下拉菜单里添加元素
        });
        form.render("select");
    });
}
//选择审核人下拉框
function initSelect2(){
    ajax(service_prefix.member,"/user/list2","post",JSON.stringify({entity:{},pager:{current:1,size:100}})).then(function(res){
        $.each(res.obj.records, function (index, item) {
            $('#selectAuditor').append(new Option(item.trueName, item.id));// 下拉菜单里添加元素
        });
        form.render("select");
    });
}

//显示任务操作对话框
function showDialog(type, titleName){
    $('#label_audit_opinion').html("理由：");
    if(type == 0){
        $('#div_select_user').hide();
    }else if(type == 1){
        $('#label_audit_opinion').html("审批意见：");
        $('#div_select_user').show();
    }else if(type == 2){
        $('#div_select_user').show();
    }else if(type == 3){
        $('#div_select_user').hide();
    }else if(type == 4){
        $('#div_select_user').hide();
    }
    layer.open({
        title: titleName,
        type: 1,
        area: ['700px', '300px'],
        btn: ['确认', '取消'],
        content: $("#task_audit"),
        success: function(layero) {
            initUserSelect(layero, '#select_user');
        },
        yes: function(index, layero){
            if(type == 0){
                recallTask();
            }else if(type == 1){
                completeTask();
            }else if(type == 2){
                transferTask();
            }else if(type == 3){
                cancelTask();
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
        auditor_id: $("#select_user").val(),
        auditOpinion: $('#audit_opinion').val()
    };
    ajax(service_prefix.flowable,"/completeTask","post", JSON.stringify(params)).then(res=>{
        if(res.success){
            closeAndRefresh();
            reflashData();
        }else{
            layer.alert(res.msg);
        }
    });
}

//转办任务
function transferTask(){
    var params = {
        taskId: taskId,
        userId: $("#select_user").val(),
        auditOpinion: $('#audit_opinion').val()
    };
    ajax(service_prefix.flowable,"/transferTask","post",JSON.stringify(params)).then(function(res){
        if(res.success){
            closeAndRefresh();
        }else{
            layer.alert(res.msg);
        }
    });
}

//撤销任务
function cancelTask(){
    var param = {
        taskId:taskId,
        auditOpinion: $('#audit_opinion').val()
    };
    ajax(service_prefix.flowable,"/terminate","post",JSON.stringify(param)).then(function(res){
        if(res.success){
            closeAndRefresh();
        }else{
            layer.alert(res.msg);
        }
    });
}

//驳回任务
function rollbackTask(){
    var param = {
        taskId:taskId,
        auditOpinion: $('#audit_opinion').val()
    };
    ajax(service_prefix.flowable,"/rollbackTask","post",JSON.stringify(param)).then(function(res){
        if(res.success){
            closeAndRefresh();
            reflashData();
        }else{
            layer.alert(res.msg);
        }
    });
}

//撤回任务
function recallTask() {
    var param = {
        taskId:taskId,
        auditOpinion: $('#audit_opinion').val()
    };
    ajax(service_prefix.flowable,"/recallTask","post",JSON.stringify(param)).then(function(res){
        if(res.success){
            closeAndRefresh();
            reflashData();
        }else{
            layer.alert(res.msg);
        }
    });
}

//初始化选择用户下拉框
function initUserSelect(layero, viewId){
    //防止遮层覆盖弹出框
    $(".layui-layer-shade").appendTo(layero.parent());
    ajax(service_prefix.member,"/user/list2","post",JSON.stringify({entity:{},pager:{current:1,size:100}})).then(function(res){
        if(res.success){
            getData(res.obj.records, '#userTemplate', viewId);
            form.render("select");
        }else{
            console.error(res.msg);
        }
    });
}

// function initProcessSelect(viewId){
//     ajax(service_prefix.flowable,"/process","post",{}).then(function(res){
//         // if(res.success){
//         //     getData(res.obj.records, '#userTemplate', viewId);
//         //     form.render("select");
//         // }else{
//         //     console.error(res.msg);
//         // }
//     });
//
//     ajax(service_prefix.flowable,"/process","post",{}).then(function(res){
//         $.each(res.obj, function (index, item) {
//             $('#selectProc').append(new Option(item.name, item.id));// 下拉菜单里添加元素
//         });
//         form.render("select");
//     });
// }

//关闭弹出框，并刷新列表
function closeAndRefresh(){
    layer.closeAll();
    $('.jilu_list').hide();
    doList(1);
}

//显示详情页对话框
function showDetail(row){
    taskId = row.flowId;
    layer.open({
        type:1,
        title:"审核详情",
        area:['1200px', '600px'],
        content: $('.audit_detail'),
        success: function(layero){
            //防止遮层覆盖弹出框
            $(".layui-layer-shade").appendTo(layero.parent());
            //获取数据
            nodeList(row.id, row.status);
            metadataInfo(row.id);
            operateList(row.id);
        },
        cancel: function(){
            //关闭弹出框后，防止数据在列表页显示
            $('.jilu_list').hide();
            //隐藏按钮
            $('#button_revoke').hide();
            $('#button_agree').hide();
            $('#button_submit').hide();
            $('#button_recall').hide();
            $('#button_rollback').hide();
            $('#button_transfer').hide();
        }
    });
}
//环节列表
function nodeList(metadataId, status){
    ajax(service_prefix.flowable, '/nodeList',"post",JSON.stringify({metadataId:metadataId})).then(res => {
        if(res.success){
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
        }else{
            console.alert(res.msg);
        }
    });
}
//获取元数据信息
function metadataInfo(metadataId){
    ajax(service_prefix.flowable, '/news/list',"post",JSON.stringify({entity:{id:metadataId},pager:{current:1,size:15}})).then(res => {
        if(res.success){
            //显示数据
            getData(res.obj.records,'#myAuditDetailTemplate','#myAuditDetailView');
            //根据当前登录用户是否流程发起人或当前处理人，显示可以操作的按钮
            var news = res.obj.records[0];
            $('.audit_detail .news_title').html(news.newsTitle);
            var userId = sessionStorage.getItem("userId");//当前用户id
            if(userId == news.procStartId){
                if(userId == news.flowUser){
                    //当前用户是发起人，也是审核人，显示提交按钮
                    $('#button_submit').show();
                }else if(news.opertime == ""){
                    //当前用户是发起人，并且审核时间为空，显示撤回按钮
                    $('#button_recall').show();
                }
            }else if(userId == news.flowUser){
                //当前用户是审核人，显示同意、驳回、转办按钮
                $('#button_agree').show();
                $('#button_rollback').show();
                $('#button_transfer').show();
            }
        }else{
            console.alert(res.msg);
        }
    });
}

function display(recall, agree, transfer, revoke, rollback){

}

//获取操作记录
function operateList(metadataId){
    ajax(service_prefix.flowable, '/operateList',"post",JSON.stringify({entity:{metadataId:metadataId},pager:{current:1,size:15}})).then(res => {
        if(res.success){
            getData(res.obj.records,'#myAuditOperateTemplate','#myAuditOperateView');
            $('.jilu_tit .operateNum').html(res.obj.total);
        }else{
            console.log(res.msg);
        }
    });
}
//显示启动流程弹出框
function showProcess(row){
    layer.open({
        type: 1,
        area: ['400px', '240px'],
        content: '<div style="margin:0 auto;"><table style="margin:0 auto;">' +
            '<tr style="line-height: 50px;height: 50px;"><td align="right">选择流程：</td><td><select id="selectProc"></select></td></tr>' +
            '<tr style="line-height: 50px;height: 50px;"><td align="right">选择审核人：</td><td><select id="selectAuditor"></select></td></tr>' +
            '</table></div>',
        success:function(){
            initSelect();
            initSelect2();
        },
        btn: ['确定', '取消'],
        yes: function(index, layero){
            startProcess(row);
        }
    });
}

function startProcess(row) {
    var params = {
        processDefinitionId: $("#selectProc").val(),
        auditor_id: $("#selectAuditor").val(),
        metadataTable: row.tableName,
        metadataId: row.id,
        metadataTitle: row.newsTitle,
        metadataUrl: row.newsPuburl
    };
    ajax(service_prefix.flowable,"/startAndSubmit","post", JSON.stringify(params)).then(res=>{
        //如果流程启动成功，更新元数据信息
        if(res.success){
            ajax(service_prefix.flowable,"/news/submitProc","put", JSON.stringify({id:row.id})).then(res=>{
                if(res.success){
                    doList(1);
                    layer.closeAll();
                }else{
                    layer.alert(res.msg);
                }
            });
        }else{
            layer.alert(res.msg);
        }
    });
}