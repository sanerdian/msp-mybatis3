var siteId;
var columnId;
var companyId;
var pName;
var siteName;

//发布
function publishImg() {
    var data = document.getElementsByName("ids")
    var ids = [];
    var count = 0;
    for (var i in data) {
        if (data[i].checked) {
            ids.push(data[i].value);
        }

    }
    if (ids == []) {
        layer.alert("请选择需要发布的图片");
        return false;
    }
    var id = localStorage.getItem("picId");
    if (id && id === ids) {
        layer.alert("该图片已发布过，请重新选择其他图片发布");
        return false
    }
    changeState("发布", ids.join());
}

function changeState(status, id) {
    if (status === "审核") {
        var obj = {};
        obj.status = 10;
        ajax(service_prefix.resources, "/picture/changeStatus?ids=" + id + "&status=" + obj.status, 'post').then(
            function (res) {
                if (res.success) {
                    $("#form9").hide();
                    $("#list1").show();
                    getTreeData();
                    localStorage.setItem("picId", "");
                }
            });
    } else {
        var obj = {};
        var status = 20;
        ajax(service_prefix.resources, "/picture/changeStatus?ids=" + id + "&status=" + status, "post").then(function (
            res) {
            if (res.success) {
                $("#form9").hide();
                $("#list1").show();
                localStorage.setItem("picId", id);
                getTreeData();
            }
        });
    }
}

function setIdParms(id1, id2, id3) {
    companyId = id1;
    siteId = id2;
    columnId = id3;
}

function setCompanyId(id) {
    setIdParms(id, "", "");
}

function setSiteId(id) {
    setIdParms("", id, "");
}

function setColumnId(id) {
    setIdParms("", "", id);
}

/*ztree*/
function getTreeData() { //获取左侧树结构的数据
    getColumnTreeData(6,0).then(function (resdata) {
        global.zTreeObj = $.fn.zTree.init($("#treeDemo"), settingss, resdata); //初始化树
        var node = global.zTreeObj.getNodeByTId("treeDemo_4");
        global.zTreeObj.expandAll(true); //true 节点全部展开、false节点收缩
        global.zTreeObj.selectNode(node);
        settingss.callback.onClick(null, global.zTreeObj.setting.treeId, node);
    });
}

var settingss = {
    data: {
        simpleData: {
            enable: true, //true 、 false 分别表示 使用 、 不使用 简单数据模式
            pIdKey: "parentId", //节点数据中保存其父节点唯一标识的属性名称
            rootPId: -1 //用于修正根节点父节点数据，即 pIdKey 指定的属性值
        }
    },
    view: {
        showLine: false,
        showIcon: true
    },
    callback: {
        onClick: zTreeOnClick1
    }
};

//点击树节点的回调函数
function zTreeOnClick1(event, treeId, treeNode) {
    if (treeNode.isSite >= 2) {
        uploadFileBtn.reload({
            url: getAjaxUrl(service_prefix.resources, '/picture/upload?id=' + columnId), //上传接口
        })
        getImgList(1, obj);
    }
    columnList();
    bool = true;
    if (treeNode.isSite < 2) {
        $("#uploads").hide();
    } else {
        $("#uploads").show();
    }
    var obj = {};
    if (treeNode.isSite === 0) {
        obj.companyId = treeNode.id;
    } else if (treeNode.isSite === 1) {
        obj.siteId = treeNode.id;
    } else {
        if (flag && treeNode.children.length != 0) {
            var arr = []
            for (var i in treeNode.children) {
                arr.push(treeNode.children[i].id);
            }
            var id = treeNode.id + "," + arr.join(",");
            obj.columnId = id;
        } else {
            obj.columnId = treeNode.id;
        }
    }
}
function addImgColumn() {
    var thisNode = global.zTreeObj.getSelectedNodes()[0];
    var pName = thisNode.isSite==2?thisNode.name:"跟路径";
    var nodes = thisNode.getPath();
    var siteName;
    var siteId;
    for(var res of nodes){
        if (res.isSite == 1) {
            siteId = res.id;
            siteName = res.name;
            break;
        }
    }
    showColumnDiv2({parentId: thisNode.isSite==2?thisNode.id:0, siteId: siteId}, thisNode.level, pName, siteName);
}

function showColumnInfo() {
    var thisNode = global.zTreeObj.getSelectedNodes()[0];
    var nodes = thisNode.getPath();
    var siteName;
    nodes.forEach(function (res) {
        if (res.level == 2) {
            siteName = res.name;
        }
    })
    thisNode.siteName = siteName;
    layui.laytpl($("#columnDetailTpl").html()).render(thisNode, function (html) {
        $("#columnDetail").html(html);
    })
    $("#main").removeClass("layui-show");
    $("#columnDetail").addClass("layui-show")
    $("#infos").addClass("layui-this");
    $("#first1").removeClass("layui-this");
}