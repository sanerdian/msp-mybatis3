var url = "/procModelList";
var menuObj = {};
var menuObjp = {};
var defaultPageSize = 15;
var zTreeObj;
let entity={}
let pager={
    "current": 1,
    "size": 15,
    "sortProps": [
        {
            "key": "crtime",
            "value": false
        }
    ]
}
var zpid;
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

// 新建流程
function addProcess() {
    go2Menu('simple/flowable/processDesign/toDesigner.html', '流程编辑器')
}


//根据id查询数据
function getMenuObj(id) {
    return new Promise((resovle, reject) => {
        ajax(service_prefix.member,url + "/" + id, "get", {}).then(res => {
            resovle(res);
        }).catch(res => {
            (rerejects);
        })
    })
}

//获取菜单树结构数据
function tempFindMenu() {
    ajax(service_prefix.flowable,url, "post", JSON.stringify({entity,pager})).then(res => {
        var datas = res.obj;
        showTreeMenu(datas);
    }).catch(function(res) {
    })
}

//渲染树结构
function showTreeMenu(data) {
    data.forEach(e => {
        e.icon="common/img/u2615.png"
    });
    zTreeObj = $.fn.zTree.init($("#treeDemo"), settingss, data); //初始化树
    zTreeObj.expandAll(true);    //true 节点全部展开、false节点收缩
}


doList(1, "");

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
    if(obj.event == "deploy"){
        deploy(data.id);
    }
})

//渲染数据到table
function setTableData(data) {
    layui.use('table', function () {
        var table = layui.table;
        //第一个实例
        table.render({
            elem: '#demo'
            , data: data
            , limit: defaultPageSize
            , cols: [[ //表头
                {type: 'numbers', title: '序号',fixed: 'left',minWidth: 60, align: 'center'}
                , {field: 'name', title: '流程名称',templet: '#titleTpl',minWidth: 160,fixed: 'left', align: 'center'}
                ,{field: 'modstr', title: '类型',minWidth: 160, align: 'center'}
                ,{field: 'realmName', title: '表单',minWidth: 100, align: 'center'}
                ,{field: 'modSuffix', title: '以建个数',minWidth: 100, align: 'center'}
                , {field: 'parentTitle', title: '管理',  toolbar: '#operateTemplate', align: 'center', minWidth: 300}
            ]]
        });
    });
}







// tree点击事件
function zTreeOnClick(event, treeId, treeNode) {
    zpid = treeNode.id;
    if (treeNode.parentId <= 0) {
        menuObjp = treeNode;
        doList(1, treeNode.id);
        return false;
    }
    getMenuObj(treeNode.id).then(function(res) {
        menuObj = res.obj;
    });
};


tempFindMenu();

layui.form.render()
layui.colorpicker.render({
    elem: '#test-form'
    ,color: '#1c97f5'
    ,done: function(color){
      $('#test-form-input').val(color);
    }
});
layui.use('form', function () {
    var form = layui.form;
    //监听提交
    form.on('submit(formDemo)', function (data) {
        layer.msg(JSON.stringify(data.field));
        return false;
    });
});

// 行政办公点击事件
$(".processBox").on('click',function () {
    console.log($(this).find('span').html());
    go2Menu('simple/flowable/newProcess/detail.html','新建流程')
})
