var siteId;
var columnId;
var companyId;
var pName;
var siteName;
var zTreeObj;

function searchColumn1() {
    var keyword = $("#val").val();
    if (keyword) {
        var Json = {
            entity: {
                status: 0,
                name: keyword
            },
            pager: {
                currNum: 0,
                current: 1, size: 10,
                sortProps:[{key:"parentId",value:true},{key:"chnlOrder",value:true}]
            }
        }
        if (siteId) {
            Json.entity.siteId = siteId;
        }
        if (companyId) {
            Json.entity.parentId = companyId;
        }
        ajax(service_prefix.manage, "/programa/list", "post", JSON.stringify(Json)).then(function(data) {
            if (data.success) {
                layui.laytpl($("#columnListTmpl").html()).render(data.obj.records, function (html) {
                    $("#first").html(html);
                    layui.form.render();
                })
                if(columnSortabel){
                    columnSortabel.destroy();
                }
                columnSortabel = Sortable.create(document.getElementById('first'), {
                    animation: 150, //动画参数
                    // 列表内元素顺序更新的时候触发
                    onUpdate: function (/**Event*/evt) {
                        var id=evt.item.getAttribute("id");
                        var order;
                        if(evt.item.previousElementSibling){
                            var others=$(evt.item).prev();
                            order=others.attr("order");
                        }else{
                            order="-1";
                        }
                        var url = "/programa/sort/" + id + "/" + (Number(order)+1);
                        ajax(service_prefix.manage,  url,"get").then(function (res) {
                            if(res.success){
                                columnList(pageNo,siteId,columnId);
                            }
                        })
                    },
                });
                columnPage(data);
            }
        })
    } else {
        columnList(1,siteId,companyId);
    }

}

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
        obj.status = 10
        ajax(service_prefix.resources, "/picture/changeStatus?ids=" + id + "&status=" + obj.status, 'post').then(
            function (res) {
                if (res.success) {
                    $("#form9").hide();
                    $("#list1").show();
                    getTreeData()
                    localStorage.setItem("picId", "")
                }
            })
    } else {
        var obj = {};
        var status = 20
        ajax(service_prefix.resources, "/picture/changeStatus?ids=" + id + "&status=" + status, "post").then(function (
            res) {
            if (res.success) {
                $("#form9").hide();
                $("#list1").show();
                localStorage.setItem("picId", id)
                getTreeData()
            }
        })
    }
}

function setIdParms(id1,id2,id3){
    companyId = id1;
    siteId = id2;
    columnId = id3;
}

function setCompanyId(id){
    setIdParms(id,"","");
}

function setSiteId(id){
    setIdParms("",id,"");
}

function setColumnId(id){
    setIdParms("","",id);
}

/*ztree*/
function getTreeData() { //获取左侧树结构的数据
    ajax2(service_prefix.manage, "/programa/getTreeData", "post", {webclass: 3}).then(function (data) {
        zTreeObj = $.fn.zTree.init($("#treeDemo"), settingss, data.obj); //初始化树
        var node = zTreeObj.getNodeByTId("treeDemo_4");
        zTreeObj.expandAll(true); //true 节点全部展开、false节点收缩
        zTreeObj.selectNode(node);
        settingss.callback.onClick(null, zTreeObj.setting.treeId, node);
    })
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
    columnName = treeNode.name;
    if (treeNode.level == 1) {
        setSiteId(treeNode.id);
    } else if (treeNode.level >= 2) {
        var parents = treeNode.getPath();
        parents.forEach(res => {
            if (res.level == 1) {
                setIdParms("",res.id,treeNode.id);
            }
        })
        uploadFileBtn.reload({
            url: getAjaxUrl(service_prefix.resources, '/picture/upload?id=' + columnId), //上传接口
        })
    } else if (treeNode.level == 0) {
        setColumnId(treeNode.id);
    }
    columnList(1, siteId, columnId);
    bool = true;
    if (treeNode.level < 2) {
        $("#uploads").hide();
    } else {
        $("#uploads").show()
    }
    var obj = {};
    if (treeNode.level === 0) {
        obj.companyId = treeNode.id;
    } else if (treeNode.level === 1) {
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
    getImgList(1, obj);     
}

function addImgColumn(){
    var thisNode = zTreeObj.getSelectedNodes()[0];
    var pName = thisNode.name;
    var nodes = thisNode.getPath();
    var siteName;
    nodes.forEach(function(res){
        if(res.level == 1){
            siteName = res.name;
        }
    })
    showColumnDiv2({parentId:columnId?columnId:0,siteId:siteId},1,pName,siteName);
}

function showColumnInfo() {
    var thisNode = zTreeObj.getSelectedNodes()[0];
    var nodes = thisNode.getPath();
    var siteName;
    nodes.forEach(function(res){
        if(res.level == 2){
            siteName = res.name;
        }
    })
    thisNode.siteName = siteName;
    layui.laytpl($("#columnDetailTpl").html()).render(thisNode, function (html) {
        $("#columnDetail").html(html);
    })
    $("#main").removeClass("layui-show");
    $("#columnDetail").addClass("layui-show");
    $("#infos").addClass("layui-this");
    $("#first1").removeClass("layui-this");
}