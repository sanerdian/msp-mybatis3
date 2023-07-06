var columnPageSize = 17;

var columnSortabel;

function columnList(pageNo,siteId,columnId) {
    console.log(siteId,columnId);
    var searchObj = {
        "entity": {
            status: 0,
            parentId: 0
        },
        "pager": {
            current: 1,
            size: columnPageSize
        }
    }
    searchObj.pager.current = pageNo;
    searchObj.pager.sortProps = [{key:"parentId",value:true},{key:"chnlOrder",value:true}];
    if (siteId) {
        searchObj.entity.siteId = siteId;
    }
    if (columnId) {
        searchObj.entity.parentId = columnId;
    }
    ajax(service_prefix.manage, "/programa/list", "post", JSON.stringify(searchObj)).then(function(data) {

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
    })
}

function searchColumn() {
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
        if (siteObj.id) {
            Json.entity.siteId = siteObj.id;
        }
        if (columnObj.id) {
            Json.entity.parentId = columnObj.id;
        }
        ajax(service_prefix.manage, "/programa/list", "post", JSON.stringify(Json)).then(function(res) {
            if (res.success) {
                searchPage(res)
            }
        })
    } else {
        columnList(1);
    }

}

function columnPage(data) {
    layui.laypage.render({
        elem: 'demo8',
        count: data.obj.total, //数据总条数
        limit: columnPageSize, //每页条数
        curr: data.obj.current, //当前页码
        jump: function (obj, first) {
            if (!first) {
                columnList(obj.curr);
            }
        }
    });
}

function showColumnDiv(data,type) {
    if(data.id){
        if(data.stop===1){
            layerOpen("修改栏目",820,655);
        }else{
            layerOpen("修改栏目",820,795);
        }
        lodTpl("columnTpl", "openDiv", {parentName:data.parentName?data.parentName:"根栏目",siteName:data.siteName,detailtplName:data.detailtplName,edittplName:data.edittplName,listtplName:data.listtplName,stop:data.stop});
    }else{
        layerOpen("添加栏目",600,995);
        lodTpl("columnTpl", "openDiv", {parentName:columnObj.name?columnObj.name:"根栏目",siteName:siteObj.name});
    }
    if(zTreeObj.getSelectedNodes().length>0){
        var nodes;
        if(data.id){
            nodes = zTreeObj.getSelectedNodes()[0].getParentNode().children;
        }else{
            nodes = zTreeObj.getSelectedNodes()[0].children;
        }
        var seqs = "<option value='-1'>————最前面————</option>";
        if(nodes && nodes.length>0){
            nodes.forEach(function(res) {
                seqs += "<option value='"+(res.chnlOrder+1)+"'>"+res.name+"</option>"
            })
            seqs += "<option value='"+(nodes[nodes.length-1].chnlOrder+1)+"'>————最后面————</option>";
        }
        $("#chnlOrder").html(seqs);
    }
    layui.form.val("columnForm", data);
    addParentSelectOptions(data.parentId);
    getAddSites(data ? data.siteId : "");
    getAddMetadatas(data ? data.tableId : "");
    setWorkFlow(data ? data.workFlow : "");
}

//new
function showColumnDiv2(data,type,pName,siteName) {
    if(data.id){
        if(data.stop===1){
            layerOpen("修改栏目",820,655);
        }else{
            layerOpen("修改栏目",820,795);
        }
        lodTpl("columnTpl", "openDiv", {parentName:pName?pName:"根栏目",siteName:siteName,detailtplName:data.detailtplName,edittplName:data.edittplName,listtplName:data.listtplName,stop:data.stop});
    }else{
        layerOpen("添加栏目",400,500);
        lodTpl("columnTpl", "openDiv", {parentName:pName?pName:"根栏目",siteName:siteName});
    }
    if(zTreeObj.getSelectedNodes().length>0){
        var nodes;
        if(data.id){
            nodes = zTreeObj.getSelectedNodes()[0].getParentNode().children;
        }else{
            nodes = zTreeObj.getSelectedNodes()[0].children;
        }
        var seqs = "<option value='0'>————最前面————</option>";
        if(nodes && nodes.length>0){
            nodes.forEach(function(res) {
                seqs += "<option value='"+(res.chnlOrder+1)+"'>"+res.name+"</option>"
            })
            seqs += "<option value='"+(nodes[nodes.length-1].chnlOrder+1)+"'>————最后面————</option>";
        }
        $("#chnlOrder").html(seqs);
    }
    layui.form.val("columnForm", data);
    addParentSelectOptions(data.parentId);
    getAddSites(data ? data.siteId : "");
    getAddMetadatas(data ? data.tableId : "");
    setWorkFlow(data ? data.workFlow : "");
}

function addParentSelectOptions(id){
    var html = "<option value='0'>===跟栏目===</option>";
    columnData.forEach(function(res){
        html += "<option value='"+res.id+"'>"+res.name+"</option>";
    })
    $("#parentSelect").append(html);
    layui.form.val("columnForm",{parentId:id});
    layui.form.render();
}

function setWorkFlow(id){
    getWorkFlow().then(function(res) {
        var htmlStr;
        for (var i in res) {
            htmlStr += '<option value="' + res[i].id + '">' + res[i].name + '</option>'
        }
        $("#workFlow").append(htmlStr);
        if (id) {
            layui.form.val("columnForm", {workFlow: id});
        }
        layui.form.render();
    })
}

function delColumn() {
    var data = document.getElementsByName("ids")
    var ids = "";
    var count = 0;
    for (var i in data) {
        if (data[i].checked) {
            if (count !== 0) {
                ids += ",";
            }
            ids += data[i].value;
            count++;
        }
    }
    if (ids === "") {
        layer.alert("请选择要删除的数据")
        return false;
    }
    if (confirm("是否删除")) {
        $.ajax({
            type: "delete",
            url: getAjaxUrl(service_prefix.manage, "/programa/" + ids),
            dataType: "json",
            success: function (data) {
                if (data.success) {
                    columnList(1,siteObj.id,columnObj.id);
                }
            },
            error: function (data) {
                layer.alert(data.msg)
            }

        })
    }
}
active.showAddColumn = function(){
    showColumnDiv({parentId:columnObj.id?columnObj.id:0,siteId:siteObj.id,stop:columnObj.stop});
}

function delColumn1(delId, status) {
    $.ajax({
        type: "get",
        url: getAjaxUrl(service_prefix.manage, "/programa/updateStatus?ids=" + delId + "&status=" + status),
        dataType: "json",
        success: function (data) {
            if (data.success) {
                layer.closeAll();
                columnList(1,siteObj.id,columnObj.id);
                getTreeData(status, delId)
            }
        },
        error: function (data) {
            layer.alert(data.msg)
        }
    })
}

function delColumn2() {
    var data = document.getElementsByName("ids")
    var ids = "";
    var count = 0;
    for (var i in data) {
        if (data[i].checked) {
            if (count !== 0) {
                ids += ",";
            }
            ids += data[i].value;
            count++;
        }
    }
    if (ids === "") {
        layer.alert("请选择要回收的数据")
        return false;
    }
    delColumn1(ids, 1)
}

function delByIds() {
    var data = document.getElementsByName("ids");
    var ids = "";
    var count = 0;
    for (var i in data) {
        if (data[i].checked) {
            if (count != 0) {
                ids += ",";
            }
            ids += data[i].value;
            count++;
        }
    }
    if (ids === "") {
        layer.alert("请选择要删除的数据")
        return false;
    }
    if (confirm("是否确定删除")) {
        var jsondata = {
            "ids": ids
        }
        $.ajax({
            type: "get",
            url: getAjaxUrl(service_prefix.manage, "/programa/delByIds"),
            contentType: "application/json",
            data: jsondata,
            dataType: "json",
            success: function (data) {
                columnList(1,siteObj.id,columnObj.id);
            },
            error: function (data) {
                layer.alert(data.msg)
            }
        })
    }
}

//导出
function exportExc() {

    var currNode = global.zTreeObj.getSelectedNodes()[0];
    if(currNode.isSite==1){
        window.location.href = service_prefix.manage+"/programa/export?parentId=0&status=0&siteId="+currNode.id;
    }else if(currNode.isSite == 2){
        window.location.href = service_prefix.manage+"/programa/export?status=0&parentId="+currNode.id;
    }
   // window.location.href = service_prefix.manage+"/programa/export?current=1&size=10";
}

active.addMetadata = function () {
    showMetadataForm();
}

//导入栏目
layui.use('upload', function () {
    var $ = layui.jquery
        , upload = layui.upload;

    //指定允许上传的文件类型
    upload.render({
        elem: '#import'
        , url: getAjaxUrl(service_prefix.manage, '/programa/import')
        , accept: 'file' //普通文件
        , done: function (res) {
        }
    });
});

active.editMetadata = function () {
    var data = getTableEdit("metadataTable");
    if(data){
        editMetadataId = data.id;
        showMetadataForm(data);
    }
}

//将元数据信息放入回收站
active.delMetadata = function () {
    var ids = checkChecked("metadataTable");
    var docstatus = 1;
    if (ids) {
        layer.alert("是否删除数据？", function () {
            ajax("", metadataUri + "/updateDocStatus?docstatus=" + docstatus, "post", JSON.stringify(ids)).then(function(res) {
                if (!res.success) {
                    layer.alert(res.msg);
                } else {
                    getMetadataList(1);
                    getMetadataList2(1);
                    layer.closeAll();
                }
            })
        })
    }
}

//恢复元数据信息
active.resetMetadata = function () {
    var ids = checkChecked("metadataTable2");
    var docstatus = 0;
    if (ids) {
        layer.alert("是否还原该数据？", function () {
            ajax("", metadataUri+"/updateDocStatus?docstatus="+docstatus, "post", JSON.stringify(ids)).then(function(res) {
                if (!res.success) {
                    layer.alert(res.msg);
                } else {
                    getMetadataList(1);
                    getMetadataList2(1);
                    layer.closeAll();
                }
            })
        });
    }
}
//删除元数据信息
active.delMetadataT = function () {
    var ids = checkChecked("metadataTable2");
    if (ids) {
        layer.alert("是否永久性删除数据？", function () {
            ajax(service_prefix.metadata, metadataUri+"/"+ids.join()+"/batch","delete",{}).then(function(res) {
                if (!res.success) {
                    layer.alert(res.msg);
                } else {
                    getMetadataList2(1);
                    layer.closeAll();
                }
            })
        });
    }
}

/*清空回收站*/
active.clearRecycle = function () {
    layer.alert("确定清空回收站吗？", function () {
        ajax3(service_prefix.metadata, "/clearRecycle", "get", {tableId: columnObj.tableId}).then(function(res) {
            getMetadataList2(1);
            layer.closeAll();
        })
    })
}

/*导出元数据*/

active.exportMetadata = function () {
   window.location.href=service_prefix.metadata+"/export?columnId="+columnObj.id+"&id="+columnObj.tableId;
   // window.location.href=service_prefix.metadata+'/export?id='+columnObj.id+"&columnId="+columnObj.tableId
}

active.passAll = function () {
    ajax("/metadata/statusPass?status=10&tableName="+tableName,"post",JSON.stringify(checkChecked("metadataTable"))).then(function(res){
        reflashData();
    })
}

active.unpassAll = function () {
    ajax("/metadata/statusPass?status=20&tableName="+tableName,"post",JSON.stringify(checkChecked("metadataTable"))).then(function(res){
        reflashData();
    })
}

active.ysjfz = function(){
    var datas = getTableCheckData("metadataTable");
    if(datas){
        showColumnTree(datas,"copyMetadata","元数据复制","checkbox");
    }
}

active.ysjyy = function(){
    var datas = getTableCheckData("metadataTable");
    if(datas){
        showColumnTree(datas,"yinYongMetadata","元数据引用","checkbox");
    }
}

active.ysjyd = function(){
    var datas = getTableCheckData("metadataTable");
    if(datas){
        showColumnTree(datas,"yiDongMetadata","元数据移动","radio");
    }
}

function reflashData(pageNo){
    getMetadataList(pageNo?pageNo:$("#metadataPager .layui-laypage-skip .layui-input").val());
}

layui.form.on("submit(addColumn)", function (data) {
    var id = data.field.id;
    data.field.status=0;
    if (id) {
        type = "put";
        url = "/programa/" + id;
    } else {
        var type = "post";
        var url = "/programa";
    }
    ajax(service_prefix.manage, url, type, JSON.stringify(data.field)).then(function(res) {
        if (res.success) {
            layer.closeAll();
            columnList(1,siteObj.id,columnObj.id);
            getTreeData();
        } else {
            layer.alert(res.msg)
        }
    })
    return false;
})

function editColumn(id,type) {
    ajax(service_prefix.manage, "/programa/" + id, "get", {}).then(function(data) {
        if (data.success) {
            showColumnDiv(data.obj,type);
        } else {
            layer.alert(data.msg);
        }
    })
}

function getColumnById(id) {
    ajax(service_prefix.manage, "/programa/" + id, "get", {}).then(function(data) {
        if (data.success) {
            var columnInfo = data.obj;
            layui.laytpl($("#columnDetailTpl").html()).render(data.obj, function (html) {
                $("#columnDetail").html(html);
                if(metadataUri){
                    $("#json1").text(metadataUri+"/{id}");
                    $("#json2").text(metadataUri+"/list");
                    $("#json3").text(metadataUri+"/updateDocStatus");
                    $("#json4").text(metadataUri+"/{ids}/batch");
                }
                getDetailRelation(columnInfo);
            })
            document.getElementById("main").className = "layui-tab-item";
            document.getElementById("columnDetail").className = "layui-tab-item layui-show";
            $("#infos").addClass("layui-this");
            $("#first1").removeClass("layui-this");

            if(columnInfo.imgColumn && columnInfo.imgColumn!=0){
                ajax(service_prefix.manage, "/programa/"+columnInfo.imgColumn,"get",{}).then(function(res){
                    $("#imgColumnName").text(res.obj.name);
                })
            }

            if(columnInfo.fileColumn && columnInfo.fileColumn!=0){
                ajax(service_prefix.manage, "/programa/"+columnInfo.fileColumn,"get",{}).then(function(res){
                    $("#fileColumnName").text(res.obj.name);
                })
            }

            if(columnInfo.audioColumn && columnInfo.audioColumn!=0){
                ajax(service_prefix.manage, "/programa/"+columnInfo.audioColumn,"get",{}).then(function(res){
                    $("#audioColumnName").text(res.obj.name);
                })
            }

            if(columnInfo.videoColumn && columnInfo.videoColumn!=0){
                ajax(service_prefix.manage, "/programa/"+columnInfo.videoColumn,"get",{}).then(function(res){
                    $("#videoColumnName").text(res.obj.name);
                })
            }
        } else {
            layer.alert(data.msg);
        }
    })
}
function getColumnByIds() {
    if (columnObj.id) {
        getColumnById(columnObj.id);
    } else {
        layer.alert("请选择栏目!");
        return false;
    }
}
function editByIds() {
    var data = document.getElementsByName("ids")
    var ids = "";
    var count = 0;
    for (var i in data) {
        if (data[i].checked) {
            if (count !== 0) {
                ids += ",";
            }
            ids += data[i].value;
            count++;
        }
    }
    if (ids === "") {
        layer.alert("请选择要修改的数据")
        return false;
    }
    if (ids && ids.split(",").length > 1) {
        layer.alert('不允许同时修改多个栏目');
        return;
    }
    editColumn(ids);
}
function getAddSites(siteId) {
    ajax(service_prefix.manage, "/site/all", "post", {}).then(function(res) {
        var records = res.obj;
        var sitesStr;
        for (var i in records) {
            sitesStr += '<option value="' + records[i].id + '">' + records[i].name + '</option>'
        }
        $("#siteId").append(sitesStr);
        if (siteId) {
            layui.form.val("columnForm", {siteId: siteId});
        }
        layui.form.render();
    })
}
function editTemplate(id) {
    ajax(service_prefix.manage, "/template/"+id, "get", {}).then(function(res) {
        if (res.success) {
            if(res.obj.siteid){
                ajax(service_prefix.manage, "/site/"+res.obj.siteid,"get",{}).then(function(siteResult) {
                    if(siteResult.success){
                        layerOpenFull("修改模板");
                        lodTpl("templetTpl", "openDiv", {siteName:siteResult.obj.name});
                        $("#text1").initTextarea();
                        layui.form.val("templetForm",res.obj);
                    }else{
                        layerOpenFull("修改模板");
                        lodTpl("templetTpl", "openDiv", {siteName:""});
                        $("#text1").initTextarea();
                        layui.form.val("templetForm",res.obj);
                    }
                })
            }else{
                layerOpenFull("修改模板");
                lodTpl("templetTpl", "openDiv", {siteName:""});
                $("#text1").initTextarea();
                layui.form.val("templetForm",data);
            }
        }
    })
}

function getColumnTreeData(webclass) {
    return new Promise(function (resovle, reject) {
        ajax2(service_prefix.manage,"/programa/getTreeData", "post", {webclass: webclass}).then(function(data) {
            resovle(data.obj);
        })
    })
}
var openZTreeObj;
function showColumnTree(datas,fnName,title,checkType){
    layer.open({
        title: title
        ,area: ['355px', '500px']
        ,content: '<ul id="ydTree" class="ztree openTree"></ul>'
        ,btn: ['确定', '取消']
        ,yes: function(index, layero){
            var ids = getCheckNodesIds(openZTreeObj);
            if(!ids || ids.length<=0){
                layer.alert("请选择栏目");
                return null;
            }
            eval(fnName+"("+JSON.stringify(ids)+","+JSON.stringify(datas)+")");
        }
        ,btn2: function(index, layero){
            layer.close(index);
        }
    })

    getColumnTreeData(1).then(function(res){
        var columnData = res;
        var settingss = {
            data: {
                simpleData: {
                    enable: true, //true 、 false 分别表示 使用 、 不使用 简单数据模式
                    idKey: "id", //节点数据中保存唯一标识的属性名称
                    pIdKey: "parentId", //节点数据中保存其父节点唯一标识的属性名称
                    rootPId: -1 //用于修正根节点父节点数据，即 pIdKey 指定的属性值
                }
            },
            view: {
                showLine: false,
                showIcon: false
            },
            check: {                    //表示tree的节点在点击时的相关设置
                enable: true,           //是否显示radio/checkbox
                autoCheckTrigger: false,
                chkStyle: checkType,      //值为checkbox或者radio表示
                // radioType:"all",
                chkboxType: { "Y" : "", "N" : "" },  //表示父子节点的联动效果，不联动
                nocheckInherit: false
            }
        };
        openZTreeObj = $.fn.zTree.init($("#ydTree"), settingss, columnData);
        openZTreeObj.expandAll(true);
    })

}

function copyMetadata(columnIds,datas){
    for(var i in columnIds){
        for(var j in datas){
            datas[j].columnId = columnIds[i];
            ajax("",metadataUri,"post",JSON.stringify(datas[j])).then(function(res){

            })
        }
    }
    layer.alert("复制成功");
}

function yinYongMetadata(columnIds,datas){
    for(var i in columnIds){
        for(var j in datas){
            datas[j].columnId = columnIds[i];
            datas[j].status = 40;
            ajax("",metadataUri,"post",JSON.stringify(datas[j])).then(function(res){

            })
        }
    }
    layer.alert("引用成功");
}

function yiDongMetadata(columnIds,datas){
    for(var j in datas){
        datas[j].columnId = columnIds[0];
        ajax("",metadataUri+"/"+datas[j].id,"put",JSON.stringify(datas[j])).then(function(res){
            getMetadataList();
        })
    }
    layer.alert("移动成功");
}