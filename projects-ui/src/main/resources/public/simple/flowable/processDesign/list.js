var url = "/procModelList";
var treeUrl = "/processclass/listing";
var menuObj = {};
var menuObjp = {};
var defaultPageSize = 15;
var zTreeObj;
let entity={}
let pager={
    "current": 1,
    "size": 15,
    "sortProps": [
        // {
        //     "key": "crtime",
        //     "value": false
        // }
    ]
}
var settingss = {
    data: {
        simpleData: {
            enable: true,  //true 、 false 分别表示 使用 、 不使用 简单数据模式
            idKey: "id",  //节点数据中保存唯一标识的属性名称
            pIdKey: "parentId",    //节点数据中保存其父节点唯一标识的属性名称
            rootPId: -1,  //用于修正根节点父节点数据，即 pIdKey 指定的属性值
        }
    },
    view: {
        showLine: false, 
        showIcon: true
    },
    callback: {
        onClick: zTreeOnClick
    }
};

//条件列表查询
function doList(current, id,parms) {
    if(!parms){
        parms = {};
    }
    var data = {};
    data.pager = {current: current, size: defaultPageSize};
    parms.companyId = id;
    parms.siteId = id;
    data.entity = parms;
    ajax(service_prefix.flowable,url, "post", JSON.stringify(data)).then(function (res) {
        if (res.success) {
            setTableData(res.obj);
        }
    })
}

//获取菜单树结构数据
function getClassMenu() {
    ajax(service_prefix.flowable,treeUrl, "post", JSON.stringify({entity,pager})).then(res => {
        var datas = res.obj.records;
        showTreeMenu(datas);
    }).catch(function(res) {
    })

}

// 新建流程
function newProcess() {
    localStorage.setItem("processData","{id:''}")
    console.log("跳转设置流程页面")
    go2Menu('simple/flowable/processDesign/setProcessView.html', '流程编辑器')
}

// 设置流程
function setProcess(data){
    localStorage.setItem("processData",JSON.stringify(data))
    go2Menu('simple/flowable/processDesign/setProcessView.html', '流程编辑器')
}


// 发布内容
function deploy(id){
    var param = {modelId:id}
    ajax(service_prefix.flowable,"/deploy","post",JSON.stringify(param)).then(function(res){
        if(res.success){
            alert("发布成功");
        }else{
            alert("发布失败");
        }
        doList(1);
    })
}
// 删除流程
function delProcess(id) {
    ajax(service_prefix.flowable,"/deploy","post",JSON.stringify(param)).then(function(res){
        if(res.success){
            alert("发布成功");
        }else{
            alert("发布失败");
        }
        doList(1);
    })
}

layui.form.on("submit(addMenu)", function (data) {
    if (data.field.id) {
        doUpdate(data.field);
    } else {
        doAdd(data.field);
    }
    return false;
})

layui.form.on("submit(searchMnu)", function (data) {
    doList(1,"",data.field);
    return false;
})

layui.table.on('tool(deploy_proc)', function (obj) {
    var data = obj.data;
    if(obj.event == "detailSet"){
        addProcess()
    }else if(obj.event == "setTool"){
        setProcess(data)
    }else if(obj.event == "del"){
        // alert("删除")
        delProcess(data.id)
    }
})

//渲染数据到table
function setTableData(data) {
    //第一个实例
    layui.table.render({
        elem: '#processTable'
        , data: data
        , limit: defaultPageSize
        , cols: [[ //表头
            {type: 'numbers', title: '序号',fixed: 'left',minWidth: 60, align: 'center'}
            , {field: 'name', title: '流程名称',templet: '#titleTpl',minWidth: 160,fixed: 'left', align: 'center'}
            ,{field: 'modstr', title: '类型',minWidth: 160, align: 'center'}
            ,{field: 'realmName', title: '表单',minWidth: 100, align: 'center'}
            ,{field: 'modSuffix', title: '已建个数',minWidth: 100, align: 'center'}
            , {field: 'parentTitle', title: '管理',  toolbar: '#operateTemplate', align: 'center', minWidth: 300}
        ]]
    });
}





//渲染树结构
function showTreeMenu(data) {
    data.forEach(e=>{
        e.name = e.className
    })
    data.forEach(e => {
        e.icon="common/img/u2615.png"
    });
    zTreeObj = $.fn.zTree.init($("#processTree"), settingss, data); //初始化树
    zTreeObj.expandAll(true);    //true 节点全部展开、false节点收缩
}

// tree点击事件
function zTreeOnClick(event, treeId, treeNode) {
    if (treeNode.parentId <= 0) {
        menuObjp = treeNode;
        doList(1, treeNode.id);
        return false;
    }
    getMenuObj(treeNode.id).then(function(res) {
        menuObj = res.obj;
    });
};




layui.colorpicker.render({
    elem: '#test-form'
    ,color: '#1c97f5'
    ,done: function(color){
      $('#test-form-input').val(color);
    }
});

//监听提交
layui.form.on('submit(formDemo)', function (data) {
    layer.msg(JSON.stringify(data.field));
    return false;
});

layui.form.render()



getClassMenu();
doList(1, "");