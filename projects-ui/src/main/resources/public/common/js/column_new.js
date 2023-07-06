var columnPageSize = 17;
var columnSortabel;
var lanmutype;
var advancedAttributeData
var global = {
    metadataParams: {
        "entity": {
            docstatus: 0
        },
        "pager": {
            current: 1,
            size: 14
        }
    }
    , delMetadataParams: {
        "entity": {
            docstatus: 1
        },
        "pager": {
            current: 1,
            size: 14
        }
    }
    , columnParams: {
        "entity": {
            status: 0
        },
        "pager": {
            current: 1,
            size: 15,
            sortProps: [{key: "parentId", value: true}, {key: "chnlOrder", value: true}]
        }
    }
}

function resetColumnParams() {
    global.columnParams = {
        "entity": {
            status: 0
        },
        "pager": {
            current: 1,
            size: 15,
            sortProps: [{key: "parentId", value: true}, {key: "chnlOrder", value: true}]
        }
    }
}

function resetMetadataParams() {
    global.metadataParams = {
        "entity": {
            docstatus: 0
        },
        "pager": {
            current: 1,
            size: 14
        }
    }
}

function resetDelMetadataParams() {
    global.delMetadataParams = {
        "entity": {
            docstatus: 1
        },
        "pager": {
            current: 1,
            size: 14
        }
    }
}

/************************************** column-ztree-start ****************************************/
var settingss = {
    data: {
        simpleData: {
            enable: true, //true 、 false 分别表示 使用 、 不使用 简单数据模式
            idKey: "id", //节点数据中保存唯一标识的属性名称
            pIdKey: "parentId", //节点数据中保存其父节点唯一标识的属性名称
            rootPId: -1 //用于修正根节点父节点数据，即 pIdKey 指定的属性值
        }
    },
    async: {
        enable: getColumnTreeAsync(),
        autoParam: ["id"],
        type: "get",
        url: getAjaxUrl('',"/manage/programa/asyncTree"),
        headers:{'mspToken':com.jnetdata.mspToken},
        dataFilter: ajaxTreeDataFilter,
        xhrFields: {withCredentials: true}
    },
    view: {
        showLine: false,
        showIcon: true
    },
    callback: {
        onClick: zTreeOnClick
    }
};

function ajaxTreeDataFilter(treeId, parentNode, responseData) {
    if (responseData) {
        global.treeData = global.treeData.concat(responseData);
        for (var i = 0; i < responseData.length; i++) {
            responseData[i].icon = "common/img/u2615.png";
        }
    }
    return responseData;
}

function getSiteTreeData(id) {
    getColumnTreeData(1).then(function (res) {
        initTree(res, id)
    })
}

function getTreeData() {
    var currNode = global.zTreeObj.getSelectedNodes()[0];
    // global.zTreeObj.expandNode(currNode,true,false);
    currNode.isParent = true;
    global.zTreeObj.reAsyncChildNodes(currNode, "refresh", false);
}

function initTree(data, id) {
    global.treeData = data;
    global.zTreeObj = $.fn.zTree.init($("#treeDemo"), settingss, data);
    //初始化树
    var flag = getColumnTreeAsync();
    if (!flag) {
        global.zTreeObj.expandAll(true); //true 节点全部展开、false节点收缩
    }else{
        var pnodes = global.zTreeObj.getNodesByFilter(function (node) {
            return node.isSite == 0
        });
        pnodes.forEach(function (res) {
            global.zTreeObj.expandNode(res, true);
        })
    }
    var node;
    if (id) {
        node = global.zTreeObj.getNodeByParam("id", id);
    } else {
        node = global.zTreeObj.getNodeByTId("treeDemo_3");
    }
    // global.zTreeObj.selectNode(node);
    setTimeout(function () {
        var container = $('#treeDemo'),
            scrollTo = $('#' + node.tId);

        container.scrollTop(
            scrollTo.offset().top - container.offset().top + container.scrollTop() - container.height() / 2
        );

        container.animate({
            scrollTop: scrollTo.offset().top - container.offset().top + container.scrollTop() - container.height() / 2
        })
    }, 500)
    // node.scrollIntoView();
    // settingss.callback.onClick(null, global.zTreeObj.setting.treeId, node);
}

function getTableInfoByTableId(id,fn) {
    if(!id) return false;
    ajax(service_prefix.metadata, "/" + id, "get", {}).then(function (res) {
        if(fn){
            fn(res.obj);
        }else{
            return res.obj;
        }
    })
}

function getTableInfos(id,fn) {
    if(!id) return false;
    ajaxGet("/metadata/tableInfo/" + id, function (res) {
        if(res.success){
            if(fn){
                fn(res.obj);
            }else{
                return res.obj;
            }
        }else{
            return false;
        }
    })
}

function zTreeOnClick(event, treeId, treeNode) {
    // createPath = "/pub" + treeNode.chnlDataPath;
    global.metadataUri = "";
    resetColumnParams();
    resetMetadataParams();
    global.fields = [];
    global.classFields = [];
    if (treeNode.isSite == 1) {
        if (global.showTabFn) {
            eval(global.showTabFn);
        } else {
            showTab("columnTab");
        }
        hideSearchBar(true);
    } else if (treeNode.isSite == 2) {
        hideSearchBar(!treeNode.tableId || treeNode.quotaid);
        if (global.showTabFn) {
            eval(global.showTabFn);
        } else {
            showTab("metadataListTab");
            // columnInfo(treeNode.id);
        }
    }
    // changeLimitShow(treeNode.id,treeNode.getParentNode()?treeNode.getParentNode().id:-1);
}

function hideSearchBar(id) {
    if (id) {
        $(".search-bar1").hide()
    } else {
        $(".search-bar1").show()
    }
}

/************************************** column-ztree-end ****************************************/
/*************************************** metadata-start *******************************************/
function showMetadataTab() {
    resetMetadataParams();
    var currNode = global.zTreeObj.getSelectedNodes()[0];
    if (!currNode.tableId) {
        showNoMetadata();
        return;
    }
    importMetadataBtn.reload({
        url: getAjaxUrl(service_prefix.metadata, "/import?id=" + currNode.tableId + "&columnid=" + currNode.id)
    })
    getTableInfos(currNode.tableId, function (ti) {
        if (ti) {
            tableName = ti.tablename;
            // $("#fieldTableInfo").html("<span>元数据:" + res.anothername + "</span> <span>表名:" + res.tablename + "</span>  <span>创建人:" + ti.cruser + "</span> <span>创建时间:" + res.crtime + "</span>")
            global.fields = ti.fieldinfos;
            for (var i in global.fields) {
                global.fields[i].forEach(function (f) {
                    if (!f.anothername) f.anothername = f.fieldname;
                })
            }
            global.classFields = getClassFields(global.fields);
            global.metadataUri = ti.url;
        } else {
            global.fields = [];
            global.classFields = [];
        }
        showMetadataTable();
        showSearchBar();
        showFieldList();
        global.showTabFn = "showMetadataTab()";
    })
}

layui.form.on("submit(searchMetadata)", function (data) {
    resetMetadataParams();
    global.metadataParams.entity = data.field;
    getMetadataList();
    return false;
})

function getMetadataList() {
    var currNode = global.zTreeObj.getSelectedNodes()[0];
    var params = global.metadataParams;
    params.pager.sortProps = [];
    if (currNode.metadataType == "table") {
        params.pager.sortProps.push({key: "seqencing", value: false});
        if (sortField) {
            params.pager.sortProps.push({key: sortField, value: false});
        } else {
            params.pager.sortProps.push({key: "createTime", value: false});
        }
    }
    if (global.metadataUri) {
        if ($("#range").val() != 1 && currNode.metadataType == "table") {
            params.entity.columnid = currNode.quotaid ? currNode.quotaid : currNode.id;
            params.entity.status = currNode.quotaid ? 21 : "";
            params.entity.docstatus = currNode.quotaid ? -1 : 0;
        }
        ajax("", global.metadataUri + "/listing", "post", JSON.stringify(params)).then(function (datas) {
            if (datas.success) {
                showMetadataTableDate(datas.obj.records);
                showMetadataPage(datas.obj);
            } else {
                showErrorMetadata();
            }
        }).catch(function (res) {
            showErrorMetadata();
        })
    } else {
        showErrorMetadata();
    }
}

/**
 * 元数据视图栏目管理
 * 查询接口地址
 * @param {*} tableId
 */
function getViewMetadataUri(tableId) {
    return new Promise((resovle, reject) => {
        if (tableId && tableId != 0) {
            var params = {
                pager: {
                    current: 1,
                    size: 1
                },
                entity: {
                    viewid: tableid
                }
            }
            ajax(service_prefix.metadata, "/view/" + tableId).then(function (view) {
                ajax(service_prefix.metadata, "/moduleview/listModuleView" + tableId, "post", JSON.stringify(params)).then(res => {
                    if (res.obj.records[0].moduleinfoid) {
                        ajax(service_prefix.metadata, "/moduleinfo/" + res.obj.records[0].moduleinfoid).then(res1 => {
                            var tablename = view.obj.tablename;
                            var strs = tablename.split("_");
                            strs.shift();
                            if (res1.obj) {
                                var modelName = res1.obj.englishname;
                                resovle("/" + modelName + "/" + strs.join("").toLowerCase());
                            } else {
                                resovle("");
                            }
                        })
                    }
                })
            })
        } else {
            resovle("");
        }
    })
}

function showMetadataTableDate(data) {
    data = data ? data : [];
    setClassName(data, global.classFields).then(function (rrrr) {
        layui.table.reload("metadataTable", {
            data: data,
            text: {
                none: '无数据'
            },
            limit: global.metadataParams.pager.size
        })
    })
}

function showErrorMetadata() {
    layui.table.reload("metadataTable", {
        text: {
            none: '接口未生成'
        },
        data: []
    })
    showMetadataPage();
}

function showNoMetadata() {
    showSearchBar();
    layui.table.render({
        elem: '#metadataTable'
        , cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
        , cols: [[{field: "", title: "提示"}]]
        , text: {
            none: '未选择元数据'
        },
        data: []
    });
    showMetadataPage();
    showFieldList();
}

function showNoDelMetadata() {
    layui.table.render({
        elem: '#metadataTable2'
        , cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
        , cols: [[{field: "", title: "提示"}]]
        , text: {
            none: '未选择元数据'
        },
        data: []
    });
    showDelMetadataPage();
    // showFieldList();
}

function showMetadataPage(pager) {
    layui.laypage.render({
        elem: 'metadataPager'
        , limit: pager ? pager.size : 0
        , curr: pager ? pager.current : 0
        , count: pager ? pager.total : 0 //数据总数，从服务端得到
        , limits: [14, 28, 42, 1000]
        , layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip']
        , jump: function (obj, first) {
            if (!first) {
                global.metadataParams.pager.current = obj.curr;
                global.metadataParams.pager.size = obj.limit;
                getMetadataList();
            }
        }
    });
}

function showDelMetadataPage(pager) {
    layui.laypage.render({
        elem: 'metadataPager2'
        , limit: pager ? pager.size : 0
        , curr: pager ? pager.current : 0
        , count: pager ? pager.total : 0 //数据总数，从服务端得到
        , limits: [14, 28, 42, 1000]
        , layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip']
        , jump: function (obj, first) {
            if (!first) {
                global.delMetadataParams.pager.current = obj.curr;
                global.delMetadataParams.pager.size = obj.limit;
                getDelMetadataList();
            }
        }
    });
}

function showMetadataTable() {
    var cols = getTableCols();
    layui.table.render({
        elem: '#metadataTable'
        , cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
        , limit: global.metadataParams.pager.size
        , cols: [cols]
    });
}

function getTableCols() {
    var currNode = global.zTreeObj.getSelectedNodes()[0];
    sortField = "";
    var cols = [];
    // var cols = [{type:'numbers',title:'序号'}];
    for (var i in global.fields) {
        var index = 0;
        global.fields[i].forEach(function (f) {
            if (f.showList == 1) {
                var col = {field: f.proName, title: f.anothername, align: "left", minWidth: f.width};
                if (f.fieldtype == 15) {
                    col = {field: '', title: f.anothername, align: "center", minWidth: f.width, style: 'background:#d4d4d4;'};
                    col.templet = '<div><img src="{{d.' + f.proName + '?getAjaxUrl(\'\',d.' + f.proName + '):\'\'}}"></div>'
                }
                if (index == 0) {
                    var col = {field: f.proName, title: f.anothername, align: "left", minWidth: f.width/*,templet:"#ysjyy"*/
                      /*  function (d) {
                            if (d.status == 0) {
                             return   ' <span>{{d.' + f.proName + '}}</span>'  ;
                            }
                        }*/};
                    /*col.templet = d.status == 1?'引用':'镜像';*/
                    col.event = 'showMetadataInfo';
                    col.style = col.style + 'color:blue;';


                }
                cols.push(col);
                index++;
            }
            if (f.issort == 1) {
                sortField = f.proName;
            }
        });
    }
    if (currNode.metadataType == "table" || currNode.metadataType == "es") {
        cols.unshift({type: 'checkbox', field: "id"},{field: 'id', title: '编号', width: 80, align: "center",templet:'#photographF'});
        cols.push({
            field: "seqencing",
            title: "排序<i class='layui-icon' onclick='showPXHelp(this)' style='vertical-align: middle;margin-left: 5px;'>&#xe60b;</i>",
            align: "left",
            minWidth: 60,
            edit: 'text',
            style: "color:blue;text-decoration:underline;cusor:pointer;"
        });
        cols.push({
            field: "seqencing",
            title: "查看",
            align: "left",
            minWidth: 60,
            event: 'preview',
            style: "color:blue;text-decoration:underline;cusor:pointer;"
        });
        cols.push({field: '', title: "操作", align: "left", event: 'toTop', minWidth: 80, style: 'color:blue', templet: toTop, fixed: "right"});
        // cols.push({field: '', title: "发布预览", align: "left", minWidth: 210, templet: toYulanTpl});
        cols.push({field: '', title: "审核状态", align: "left", width: 90, templet: "#flowShenHeStateTpl", fixed: "right"});
        if (currNode.workFlow) {
            cols.push({field: 'doctitle', title: "审核人", align: "left", width: 90, fixed: "right"});
            cols.push({field: '', title: "审核", align: "center", width: 300, templet: "#shenHeTpl", fixed: "right"});
        }
    }
    return cols;
}

function showPXHelp(obj) {
    layer.tips('点击下方单元格输入排序数值,数值越大排位越靠前', obj, {time: 5000});
}

function showSearchBar() {
    lodTpl("searchTpl", "searchForm", global.fields);
    for (var i in global.fields) {
        global.fields[i].forEach(function (res) {
            if (res.fieldtype == 13) {
                ajax(service_prefix.metadata,"/class/all", "post", JSON.stringify({parentId: res.classparentid})).then(function (res2) {
                    var html = "";
                    html += '<select class="layui-input" name="' + res.proName + '">';
                    html += '<option value="">--请选择--</option>';
                    res2.obj.forEach(function (res1) {
                        html += '<option value="' + res1.id + '">' + res1.className + '</option>';
                    })
                    html += '</select>';
                    $("#class_" + res.proName + "_search").html(html);
                    layui.form.render();
                })
            } else if (res.fieldtype == 4) {
                layui.laydate.render({elem: '#date_search_' + res.proName, type: 'datetime'});
                if (res.matchType == 7) {
                    layui.laydate.render({elem: '#date_search_' + res.proName + "BT2", type: 'datetime'});
                }
            } else if (res.fieldtype == 20) {
                var par = {elem: '#date_search_' + res.proName}
                if (res.fieldname == "ZBSJ") par.value = new Date();
                layui.laydate.render(par);
                if (res.matchType == 7) {
                    layui.laydate.render({elem: '#date_search_' + res.proName + "BT2"});
                }
            }
        })
    }
    $("#searchMetadata").trigger("click");
    // getMetadataList();
    // layui.laydate.render({elem: '#dangtianshuju'});
}

function showFieldList() {
    var datas = [];
    for (var i in global.fields) {
        datas.push.apply(datas, global.fields[i]);
    }
    layui.table.render({
        elem: '#fieldTabel'
        , data: datas
        , cols: [[
            {type: 'numbers', title: '序号'}
            , {field: 'fieldname', title: '英文名称'}
            , {field: 'fieldTypeStr', title: '字段类型'} //minWidth：局部定义当前单元格的最小宽度，layui 2.2.1 新增
            , {field: 'dbTypeStr', title: '库中类型',}
            , {field: 'groupname', title: '所属分组'}
        ]]
        , limit: datas.length
    });
}

active.addMetadata = function () {
    showMetadataForm();
}

active.publishData = function () {
    var ids = checkChecked("metadataTable");
    if (!ids) return false;
    publishData(ids, 0);
}
active.rePubData = function () {
    var datas = getTableCheck("metadataTable");
    if (!datas) return false;
    var errnum = 0;
    republishData(datas, 0, errnum);
}

function publishData(ids) {
    ajax3("", global.metadataUri + "/pub" , "post", JSON.stringify(ids)).then(function (res) {
        reflashData();
    })
}

function republishData(datas, i) {
    var data = datas[i];
    data.status = 4;
    var docchannelid = data.docchannelid;
    ajax3("", global.metadataUri + "/" + data.id, "put", JSON.stringify({docchannelid: -1, status: 4})).then(function (res) {
        if (data.docchannelid) {
            ajax("", global.metadataUri + "/" + docchannelid, "delete").then(function (res) {
                i++;
                if (i == datas.length) reflashData();
                else republishData(datas, i);
            })
        }
    })
}

function showMetadataForm(datas) {
    layerOpenFull((datas&&datas.id?"修改":"新建") + "元数据信息");
    var params = {}
    params.fields = global.fields;
    params.id = datas ? datas.id : '';
    lodTpl("addMetadataBodyTpl", "openDiv", params, function () {
        for (var i in global.fields) {
            global.fields[i].forEach(function (res) {
                if (res.fieldtype == 4) {
                    layui.laydate.render({elem: '#date_edit_' + res.proName, type: 'datetime'});
                } else if (res.fieldtype == 20) {
                    layui.laydate.render({elem: '#date_edit_' + res.proName});
                } else if (res.fieldtype == 15) {
                    if (datas && datas[res.proName]){
                        for(var o of datas[res.proName].split(",")){
                            $("#img_" + res.proName).append('<img className="layui-upload-img" style="margin-right:10px;border:1px solid;max-width: 200px;max-height: 200px;" src="'+getAjaxUrl("", o)+'">');
                        }
                    }
                    layui.upload.render({
                        elem: '#upbtn_' + res.proName
                        , multiple: true
                        , acceptMime: "image/*"
                        , url: getAjaxUrl(service_prefix.member, "/user/importHead")
                        , before: function (obj) {
                            layer.load();
                            //预读本地文件示例，不支持ie8
                            obj.preview(function (index, file, result) {
                                $('#img_' + res.proName).append('<img className="layui-upload-img" style="margin-right:10px;border:1px solid;max-width: 200px;max-height: 200px;" src="'+getAjaxUrl("",result)+'">');
                            });
                        }, done: function (res1) {
                            layer.closeAll('loading');
                            if (res1.success) {
                                layer.msg("上传成功");
                                $("#up_" + res.proName).val(($("#up_" + res.proName).val()?$("#up_" + res.proName).val()+',':'') + res1.obj.url);
                            } else {
                                layer.alert(res1.msg);
                            }
                        }
                    });
                } else if (res.fieldtype == 17) {
                    layui.upload.render({
                        elem: '#upbtn_' + res.proName
                        , url: getAjaxUrl(service_prefix.member, "/user/importHead")
                        , accept: "file"
                        , multiple: true
                        ,before: function(obj){
                            //预读本地文件示例，不支持ie8
                            obj.preview(function(index, file, result){
                                $('#img_' + res.proName).val(($('#img_' + res.proName).val()?$('#img_' + res.proName).val()+",":'') + file.name);
                            });
                        }
                        , done: function (res1) {
                            if (res1.success) {
                                $("#up_" + res.proName).val(($("#up_" + res.proName).val()?$("#up_" + res.proName).val()+",":'') + res1.obj.url);
                            } else {
                                layer.alert(res1.msg);
                            }
                        }, allDone: function(obj){ //多文件上传完毕后的状态回调
                            layer.closeAll('loading');
                            layer.msg("上传成功");
                        }
                    });
                    if(datas && datas[res.proName])$("#img_" + res.proName).val(datas[res.proName]);
                } else if (res.fieldtype == 16 || res.fieldtype == 14) {
                    if (res.fieldtype == 14 && datas && datas[res.proName]) {
                        var url = datas[res.proName];
                        if (url.indexOf("http") != 0) url = getAjaxUrl("", datas[res.proName]);
                        $("#img_" + res.proName).attr("src", url).show();

                    }
                    layui.upload.render({
                        elem: '#upbtn_' + res.proName
                        , url: getAjaxUrl(service_prefix.member, "/user/importHead")
                        , accept: "file"
                        , before: function (obj) {
                            layer.load();
                            //预读本地文件示例，不支持ie8
                            obj.preview(function (index, file, result) {
                                $('#img_' + res.proName).val(file.name); //图片链接（base64）git
                            });
                        }, done: function (res1) {
                            layer.closeAll('loading');
                            if (res1.success) {
                                layer.msg("上传成功");
                                if (res.fieldtype == 14) $("#img_" + res.proName).attr("src", getAjaxUrl("", res1.obj.url)).show();
                                $("#up_" + res.proName).val(res1.obj.url);
                            } else {
                                layer.alert(res1.msg);
                            }
                        }
                    });
                } else if (res.fieldtype == 18) {
                    UE.delEditor('ue_' + res.proName);
                    var ue = UE.getEditor('ue_' + res.proName);
                    if (datas && datas[res.proName]) {
                        ue.addListener("ready", function () {

                            //赋值
                            ue.setContent(datas[res.proName]);

                            pasterMgr.SetEditor(this);

                            //WordPaster快捷键 Ctrl + V
                            ue.addshortcutkey({
                                "wordpaster": "ctrl+86"
                            });
                        });
                    }
                    // var str1 = '<div class="layui-form-item"> <label class="layui-form-label">关键词:</label> <div class="layui-input-block">  <textarea name="" id="keyword" rows="10" placeholder="请输入关键词或提取关键词"></textarea> </div> </div>'
                    // $('#ue_' + res.proName).parents('.layui-form-item').after(str1);
                    // ue.addListener("ready", function () {
                    //     $('.edui-editor-bottomContainer').html('');
                    //     var str = '<div class="uebot"><div class="btn1" onclick="mgcfun()">敏感词提醒</div><div class="btn1" onclick="gjcfun()">提取关键词</div></div>';
                    //     //赋值
                    //     $('.edui-editor-bottomContainer').html(str);
                    //     ue.setContent(datas?datas[res.proName]:"");
                    // });
                } else if (res.fieldtype == 13) {
                    // ajax(service_prefix.metadata, "/class/all", "post", JSON.stringify({parentId: res.classid})).then(function (res2) {
                    //     var html = "";
                    //     if (res.classtype == 1) {
                    //         res2.obj.forEach(function (res1) {
                    //             html += '<input type="checkbox" name="check_' + res.proName + '_' + res1.id + '" lay-skin="primary" title="' + res1.className + '"><br>';
                    //         })
                    //         $("#class_" + res.proName + "_div").html(html);
                    //         valFormCheck(datas, res.proName);
                    //     } else {
                    //         res2.obj.forEach(function (res1) {
                    //             html += '<input type="radio" name="' + res.proName + '" value="' + res1.id + '" title="' + res1.className + '"><br>';
                    //         })
                    //         $("#class_" + res.proName + "_div").replaceWith(html);
                    //         if (datas && datas[res.proName]) {
                    //             $("input[name='" + res.proName + "'][value='" + datas[res.proName] + "']").prop("checked", true);
                    //         }
                    //     }
                    //     layui.form.render();
                    // })
                } else if (res.fieldtype == 7) {
                    valFormCheck(datas, res.proName);
                }
            })
        }
        $(".edui-for-autotypeset .edui-icon").text("一键排版");
        if (datas) {
            layui.form.val("addMetadataForm", datas);
            layui.form.render();
        }
    });
}

layui.form.on("submit(addMetadataBtn)", function (data) {
    addMetadata(data, 0);
    return false;
})

layui.form.on("submit(addgaoji)", function (data) {
    addMetadatagaoji(data, 0);
    return false;
})
function addMetadatagaoji(data, status) {
    var currNode = global.zTreeObj.getSelectedNodes()[0];
    var fields = data.field;
    console.log(".....................fields")
    console.log(fields)
    console.log(status)
    var checks = {};
    /*for (var i in fields) {
        if (i.indexOf("check_") == 0) {
            console.log(".................check_")
            var vals = i.split("_");
            if (!checks[vals[1]]) {
                console.log("..................vals[1]")
                checks[vals[1]] = [];
            }
            checks[vals[1]].push(vals[2]);
        }
    }
    for (var i in checks) {
        console.log("......................checks")
        fields[i] = checks[i].join(",");
    }*/
    var url = "";
    var type = "";
    url = global.metadataUri;
    type = "post";
    if(!fields.columnid) fields.columnid = currNode.id;
    fields.docstatus = 0;
    if (status) fields.status = status;
    advancedAttributeData = fields;
    ajax("", url, type, JSON.stringify(fields)).then(function (res) {
        if (!res.success) {
            layer.alert(res.msg);
        } else {
            if (!fields.id && currNode.workFlow) {
                delete fields["id"];
                //startFlow($.extend({id:res.obj.id},fields), true);
            }
            var dataid = fields.id ? fields.id : res.obj.id;
           /* if (status && status == 21) {
                if (!fields.docchannelid || data.docchannelid == -1) {
                    fields.docstatus = -1;
                    ajax("", global.metadataUri, "post", JSON.stringify(fields)).then(function (res) {
                        if (res.success) {
                            ajax("", global.metadataUri + "/" + dataid, "put", JSON.stringify({docchannelid: res.obj.id})).then(function (res) {
                            })
                        } else {
                            layer.alert(res.msg);
                        }
                    })
                } else {
                    fields.docstatus = -1;
                    ajax("", global.metadataUri + "/" + fields.docchannelid, "put", JSON.stringify(fields)).then(function (res) {
                        if (!res.success) {
                            layer.alert(res.msg);
                        }
                        layer.alert("成功");
                    })
                }
            } else {
                layer.alert("失败")
            }*/
            console.log("添加成功")
            $(".heigh_project").click()
            advancedAttributeData.id=res.obj.id;

        }
    })
}
layui.form.on("submit(addMetadataPublishBtn)", function (data) {
    addMetadata(data, 21);
    return false;
})

function dayToString(date) {
    if (!date) date = new Date();
    var year = date.getFullYear();
    var month = (date.getMonth() + 1).toString();
    var day = (date.getDate()).toString();
    if (month.length == 1) {
        month = "0" + month;
    }
    if (day.length == 1) {
        day = "0" + day;
    }
    var dateTime = year + "-" + month + "-" + day;
    return dateTime;
}

//js日期比较( 要求日期格式：yyyy-mm-dd)

function DateBiJiao(firstDate, lastDate) {
    var arr = firstDate.split(" ")[0].split("-");
    var firsttime = new Date(arr[0], arr[1], arr[2]);
    var firsttimes = firsttime.getTime();

    var arrs = lastDate.split(" ")[0].split("-");
    var lasttime = new Date(arrs[0], arrs[1], arrs[2]);
    var lasttimes = lasttime.getTime();

    if (firsttimes == lasttimes)
        return 0;
    else if (firsttimes > lasttimes)
        return 1;
    else
        return -1;
}

function addMetadata(data, status) {
    var currNode = global.zTreeObj.getSelectedNodes()[0];
    var fields = data.field;
    var checks = {};
    for (var i in fields) {
        if (i.indexOf("check_") == 0) {
            var vals = i.split("_");
            if (!checks[vals[1]]) {
                checks[vals[1]] = [];
            }
            checks[vals[1]].push(vals[2]);
        }
    }
    for (var i in checks) {
        fields[i] = checks[i].join(",");
    }
    var url = "";
    var type = "";
    if (fields.id) {
        url = global.metadataUri + "/" + fields.id;
        type = "put";
    } else {
        url = global.metadataUri;
        type = "post";
    }
    if(!fields.columnid) fields.columnid = currNode.id;
    fields.docstatus = 0;
    if (status) fields.status = status;
    ajax("", url, type, JSON.stringify(fields)).then(function (res) {
        if (!res.success) {
            layer.alert(res.msg);
        } else {
            if (!fields.id && currNode.workFlow) {
                delete fields["id"];
                //startFlow($.extend({id:res.obj.id},fields), true);
            }
            var dataid = fields.id ? fields.id : res.obj.id;
            if (status && status == 21) {
                if (!fields.docchannelid || data.docchannelid == -1) {
                    fields.docstatus = -1;
                    ajax("", global.metadataUri, "post", JSON.stringify(fields)).then(function (res) {
                        if (res.success) {
                            ajax("", global.metadataUri + "/" + dataid, "put", JSON.stringify({docchannelid: res.obj.id})).then(function (res) {

                            })
                        } else {
                            layer.alert(res.msg);
                        }
                        layer.closeAll();
                    })
                } else {
                    fields.docstatus = -1;
                    ajax("", global.metadataUri + "/" + fields.docchannelid, "put", JSON.stringify(fields)).then(function (res) {
                        if (!res.success) {
                            layer.alert(res.msg);
                        }
                        layer.closeAll();
                    })
                }
            } else {
                layer.closeAll();
            }

            reflashData();
        }
    })
}

layui.table.on('tool(metadataTable)', function (obj) {
    var data = obj.data;
    var currNode = global.zTreeObj.getSelectedNodes()[0];
    if (obj.event == "showMetadataInfo") {
        var param = {data: data, fields: global.fields};
        layerOpenFull("查看元数据信息");
        global.editMetadataId = data.id;
        // alert(editMetadataId);
        lodTpl("metadataDetailTpl", "openDiv", param);
        $(".classTd").each(function () {
            var ids = $(this).text();
            ajax(service_prefix.metadata, "/class/classNames", "get", {ids: ids}).then(function (res) {
                $(this).text(res.msg);
            })
        })
    } else if (obj.event == "toTop") {
        ajax3(service_prefix.metadata, "/toTop", "get", {tableId: currNode.tableId, dataId: data.id}).then(function (res) {
            reflashData(1);
        })
    } else if (obj.event == "preview") {
        if(data.createTime){
            var paths = data.createTime.split(" ")[0].split("-");
            var link = getColumnCurrNode().listUrl;
            var url = link.substring(0,link.lastIndexOf("/")) + "/" + paths[0]+paths[1]+"/"+paths[2] + "/" + data.id + ".html";
            window.open(url);
        }
    } else if (obj.event == "toStart") {
        startFlow(data);
    } else if (obj.event == "statusPass") {
        finishWork(data.flowId, data.id);
        // ajax3("", metadataUri + "/" + data.id, "put", JSON.stringify({status: 10})).then(function (res) {
        //     reflashData();
        // })
    } else if (obj.event == "statusNoPass") {
        layerOpen("审核不通过", 400, 250);
        lodTpl("shReasionTpl", "openDiv", {id: data.id});
    } else if (obj.event == "startSH") {
        startSH(data.id);
    } else if(obj.event === 'toFlowDetail'){
        //获取元数据对应的流程
        ajax(service_prefix.flowable, '/metadata/getProc',"post",JSON.stringify({metadataTable:tableName, metadataId:data.id})).then(res => {
            data.procInstId = res.obj.processInstanceId;
            data.taskId = data.flowId;
            showDetail(data);
        });
    } else if(obj.event === 'flowImg'){
        showFlowImg('',data.id);
    }
})

function startFlow(data, isNew) {
    //获取栏目信息
    ajax3(service_prefix.manage, "/programa/" + data.columnid, "get", {}).then(function (res) {
        var procKey = res.obj.workFlow;
        if (!procKey && !isNew) {
            layer.alert("所在栏目没有配置工作流");
            return;
        }
        //获取栏目关联的流程信息
        ajax3(service_prefix.flowable, "/latestProc?procKey=" + procKey, "get", {}).then(function (res2) {
            var procId = res2.obj.id;
            var params = {
                processDefinitionId: procId,
                metadataTable: tableName,
                metadataId: data.id
            };
            //启动流程
            ajax(service_prefix.flowable, "/startAndSubmit", "post", JSON.stringify(params)).then(function (res3) {
                if (res3.success) {
                    if (!isNew) layer.alert("流程启动成功");
                    reflashData(1);
                } else {
                    layer.alert(res3.msg);
                }
            })
        })
    })
}

function showMetadataFieldTab() {
    global.showTabFn = "showMetadataTab()";
}

function showDelMetadataTab() {
    global.showTabFn = "showDelMetadataTab()";
    getDelMetadataList();
}

function getDelMetadataList() {
    resetDelMetadataParams();
    var currNode = global.zTreeObj.getSelectedNodes()[0];
    if (currNode.metadataType != "table") {
        showNoDelMetadata();
        return;
    }
    if (!currNode.tableId) {
        showNoDelMetadata();
        return;
    }
    ajax(service_prefix.metadata, "/fieldinfo/all", "get", {tableId: currNode.tableId}).then(function (res) {
        if (res.success) {
            global.fields = res.obj;
            for (var i in global.fields) {
                global.fields[i].forEach(function (f) {
                    if (!f.anothername) f.anothername = f.fieldname;
                })
            }
            global.classFields = getClassFields(global.fields);
        } else {
            global.fields = [];
            global.classFields = [];
        }
        var cols = getTableCols();
        cols.push({field: '', title: "操作", align: "left", minWidth: 210, templet: toYulanTpl2});
        var params = global.delMetadataParams;
        getMetadataUri(currNode.tableId).then(function (metadataUri) {
            if (global.metadataUri) {
                ajax("", global.metadataUri + "/listing", "post", JSON.stringify(params)).then(function (datas) {
                    var records = datas.obj.records;
                    setClassName(records, classFields).then(function (rrrr) {
                        layui.table.render({
                            elem: '#metadataTable2'
                            , cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
                            , limit: global.delMetadataParams.pager.size
                            , cols: [cols]
                            , data: records
                        });
                        layui.laypage.render({
                            elem: 'metadataPager2'
                            , limit: datas.obj.size
                            , curr: datas.obj.current
                            , count: datas.obj.total //数据总数，从服务端得到
                            , layout: ['count', 'prev', 'page', 'next', 'refresh', 'skip']
                            , jump: function (obj, first) {
                                if (!first) {
                                    global.delMetadataParams.pager.current = obj.curr;
                                    getDelMetadataList();
                                }
                            }
                        });
                    })
                })
            }
        })
    })

}

/*************************************** metadata-end *******************************************/

function showColumnTab() {
    global.showTabFn = "showColumnTab()";
    columnList();
}

function showColumnInfoTab() {
    global.showTabFn = "showColumnInfoTab()";
    showColumnInfo();
}

function columnList() {
    var currNode = global.zTreeObj.getSelectedNodes()[0];
    if (currNode.isSite == 0) return;
    reloadImportColumnUrl(currNode);
    var searchObj = global.columnParams;
    if (currNode.isSite == 1) {
        searchObj.entity.siteId = currNode.id;
        searchObj.entity.parentId = 0;
    } else
        searchObj.entity.parentId = currNode.id;
    ajax(service_prefix.manage, "/programa/list", "post", JSON.stringify(searchObj)).then(function (data) {
        layui.laytpl($("#columnListTmpl").html()).render(data.obj.records, function (html) {
            $("#first").html(html);
            layui.form.render();
        })
        if (columnSortabel) {
            columnSortabel.destroy();
        }
        columnSortabel = Sortable.create(document.getElementById('first'), {
            animation: 150, //动画参数
            // 列表内元素顺序更新的时候触发
            onUpdate: function (/**Event*/evt) {
                var id = evt.item.getAttribute("id");
                var order;
                if (evt.item.previousElementSibling) {
                    var others = $(evt.item).prev();
                    order = others.attr("order");
                } else {
                    order = "-1";
                }
                var url = "/programa/sort/" + id + "/" + (Number(order) + 1);
                ajax(service_prefix.manage, url, "get").then(function (res) {
                    if (res.success) {
                        columnList();
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
                sortProps: [{key: "parentId", value: true}, {key: "chnlOrder", value: true}]
            }
        }
        if (siteObj.id) {
            Json.entity.siteId = siteObj.id;
        }
        if (columnObj.id) {
            Json.entity.parentId = columnObj.id;
        }
        ajax(service_prefix.manage, "/programa/list", "post", JSON.stringify(Json)).then(function (res) {
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
        limit: data.obj.size, //每页条数
        curr: data.obj.current, //当前页码
        jump: function (obj, first) {
            if (!first) {
                global.columnParams.pager.current = obj.curr;
                columnList();
            }
        }
    });
}

function getSite() {
    var nodes = global.zTreeObj.getSelectedNodes()[0].getPath();
    for (var i in nodes) {
        if (nodes[i].isSite == 1) {
            return nodes[i];
        }
    }
}

function showColumnDiv(data, type) {
    if (!data) data = {};
    var columnObj = global.zTreeObj.getSelectedNodes()[0];
    var siteObj = getSite();
    if (data.id) {
        if (data.stop === 1) {
            layerOpen("修改栏目", 600, 800);
        } else {
            layerOpen("修改栏目", 600, 900);
        }
        lodTpl("columnTpl", "openDiv", data);
    } else {
        layerOpen("添加栏目", 600, 900);
        lodTpl("columnTpl", "openDiv", {siteName: siteObj.name});
    }

    metadataType = data.metadataType ? data.metadataType : "table";
    $("#metadataOrView #mv_" + metadataType + " select").attr("name", "tableId");

    if (global.zTreeObj.getSelectedNodes().length > 0) {
        var nodes;
        if (data.id) {
            nodes = global.zTreeObj.getSelectedNodes()[0].children;
        } else {
            data.siteId = siteObj.id;
            data.parentId = columnObj.isSite < 2 ? 0 : columnObj.id;
            nodes = global.zTreeObj.getSelectedNodes()[0].children;
        }
        console.log("data",data);
        var seqs = "<option value='0'>————最前面————</option>";
        if (nodes && (!data.id || nodes.length > 2)) {
           /* nodes.forEach(s=>{
                if(s.id!=data.id){
                    seqs += "<option value='" + s.chnlOrder + "'>" + s.name + "</option>"
                }
                }
            )*/
            for (var i in nodes) {
                if (i < nodes.length-1&&nodes[i].id!=data.id) seqs += "<option value='" + nodes[Number(i) + 1].chnlOrder + "'>" + nodes[i].name + "</option>"
            }
            var lastOrder = nodes.length > 0 ? nodes[nodes.length - 1].chnlOrder + 1 : 1;
            seqs += "<option value='" + lastOrder + "'>————最后面————</option>";
            if (!data.chnlOrder&&data.chnlOrder!=0) {
                data.chnlOrder = lastOrder
            };
        }
        $("#chnlOrder").html(seqs);
    }
    layui.form.val("columnForm", data);
    addParentSelectOptions(data ? data.parentId : 0);
    getAddSites(data ? data.siteId : "");
    getAddMetadatas(metadataType == "table" && data ? data.tableId : "", "table");
    getAddMetadatas(metadataType == "view" && data ? data.tableId : "", "view");
    getAddMetadatas(metadataType == "tview" && data ? data.tableId : "", "tview");
    getAddMetadatas(metadataType == "es" && data ? data.tableId : "", "es");
    setWorkFlow(data ? data.workFlow : "");

}




layui.form.on('select(metadataType)', function (data) {
    $("#metadataOrView #mv_" + data.value).show().siblings().hide();
    $("#metadataOrView select[name='tableId']").removeAttr("name");
    $("#metadataOrView #mv_" + data.value + " select").attr("name", "tableId");
});

function getAddMetadatas(tableId, type) {
    var url = type == "view" ? "/view/all" : type == "tview" ? "/tview/all" :  type == "es" ? "/es/all" :"/all";
    ajax(service_prefix.metadata, url, "post", {}).then(function (res) {
        var records = res.obj;
        var metadatasStr;

        for (var i in records) {
            var name = type == "view" ? records[i].tablename : records[i].anothername;
            metadatasStr += '<option value="' + records[i].id + '">' + name + '</option>'
        }
        $("#mv_" + type + " select").append(metadatasStr);

        if (tableId) {
            layui.form.val("columnForm", {tableId: tableId});
        }
        layui.form.render();
    })
}

//new
function showColumnDiv2(data, type, pName, siteName) {
    if (data.id) {
        if (data.stop === 1) {
            layerOpen("修改栏目", 820, 655);
        } else {
            layerOpen("修改栏目", 820, 795);
        }
        lodTpl("columnTpl", "openDiv", {
            parentName: pName ? pName : "根栏目",
            siteName: siteName,
            detailtplName: data.detailtplName,
            edittplName: data.edittplName,
            listtplName: data.listtplName,
            stop: data.stop
        });
    } else {
        layerOpen("添加栏目", 400, 500);
        lodTpl("columnTpl", "openDiv", {parentName: pName ? pName : "根栏目", siteName: siteName});
    }
    if (global.zTreeObj.getSelectedNodes().length > 0) {
        var nodes;
        if (data.id) {
            nodes = global.zTreeObj.getSelectedNodes()[0].getParentNode().children;
        } else {
            nodes = global.zTreeObj.getSelectedNodes()[0].children;
        }
        var seqs = "<option value='0'>————最前面————</option>";
        if (nodes && nodes.length > 0) {
            nodes.forEach(function (res) {
                seqs += "<option value='" + (res.chnlOrder + 1) + "'>" + res.name + "</option>"
            })
            seqs += "<option value='" + (nodes[nodes.length - 1].chnlOrder + 1) + "'>————最后面————</option>";
        }
        $("#chnlOrder").html(seqs);
    }
    layui.form.val("columnForm", data);
    addParentSelectOptions(data.parentId);
    getAddSites(data ? data.siteId : "");
    getAddMetadatas(data ? data.tableId : "");
    setWorkFlow(data ? data.workFlow : "");
}

function addParentSelectOptions(id) {
    var html = "<option value='0'>===跟栏目===</option>";
    global.treeData.forEach(function (res) {
        if (res.chnlOrder !== undefined) {
            html += "<option value='" + res.id + "'>" + res.name + "</option>";
        }
    })
    $("#parentSelect").append(html);
    layui.form.val("columnForm", {parentId: id});
    layui.form.render();
}

function setWorkFlow(id){
    getWorkFlow().then(function(res) {
        var htmlStr;
        for (var i in res) {
            htmlStr += '<option value="' + res[i].key + '">' + res[i].name + '</option>'
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
                    columnList();
                }
            },
            error: function (data) {
                layer.alert(data.msg)
            }

        })
    }
}

active.showAddColumn = function () {
    showColumnDiv();
}

function delColumn1(delId, status) {
    if (delId){
        layer.alert("是否删除"+(name?name:'')+"数据？", function () {
            $.ajax({
                type: "get",
                url: getAjaxUrl(service_prefix.manage, "/programa/updateStatus?ids=" + delId + "&status=" + status),
                dataType: "json",
                success: function (data) {
                    if (data.success) {
                        layer.closeAll();
                        columnList();
                        var currNode = global.zTreeObj.getSelectedNodes()[0];
                        getTreeData(currNode.id)
                    }
                },
                error: function (data) {
                    layer.alert(data.msg)
                }
            })
        })
    }
  /*  $.ajax({
        type: "get",
        url: getAjaxUrl(service_prefix.manage, "/programa/updateStatus?ids=" + delId + "&status=" + status),
        dataType: "json",
        success: function (data) {
            if (data.success) {
                layer.closeAll();
                columnList();
                var currNode = global.zTreeObj.getSelectedNodes()[0];
                getTreeData(currNode.id)
            }
        },
        error: function (data) {
            layer.alert(data.msg)
        }
    })*/
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
                columnList();
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
    if(currNode.isSite == 1){
        window.location.href = service_prefix.manage+"/programa/export?parentId=0&status=0&siteId="+currNode.id;
    }else if(currNode.isSite == 2){
        window.location.href = service_prefix.manage+"/programa/export?status=0&parentId="+currNode.id;
    }
}

function reloadImportColumnUrl(node){
    importColumnBtn.reload({url:getAjaxUrl(service_prefix.manage, '/programa/import?') + (node.isSite==1?"siteId="+node.id+"&columnId=0":"siteId="+ node.siteId +"&columnId="+node.id)})
}

var importColumnBtn;

//导入栏目
layui.use('upload', function () {
    var $ = layui.jquery
        , upload = layui.upload;

    //指定允许上传的文件类型
    importColumnBtn = upload.render({
        elem: '#import'
        , url: getAjaxUrl(service_prefix.manage, '/programa/import')
        , accept: 'file' //普通文件
        , done: function (res) {
            if(res.success){
                layer.alert("导入成功");
            }else{
                layer.alert(res.msg);
            }
        }
    });
});

active.editMetadata = function () {
    var data = getTableEdit("metadataTable");
    global.editMetadataId = data.id;
    showMetadataForm(data);
}

//将元数据信息放入回收站
active.delMetadata = function () {
    var node = getColumnCurrNode();
    var columnid = node.id;
    var ids = checkChecked("metadataTable");
    if (ids) {
        layer.alert("是否删除数据？", function () {
            ajaxJsonPost(global.metadataUri+"/delBatch",ids,function(res){
                if (!res.success) {
                    layer.alert(res.msg);
                }else{
                    getMetadataList();
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
            ajax("", global.metadataUri + "/updateDocStatus?docstatus=" + docstatus, "post", JSON.stringify(ids)).then(function (res) {
                if (!res.success) {
                    layer.alert(res.msg);
                } else {
                    getDelMetadataList(1);
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
            ajax('', global.metadataUri + "/" + ids.join() + "/batch", "delete", {}).then(function (res) {
                if (!res.success) {
                    layer.alert(res.msg);
                } else {
                    getDelMetadataList(1);
                    layer.closeAll();
                }
            })
        });
    }
}

/*清空回收站*/
active.clearRecycle = function () {
    layer.alert("确定清空回收站吗？", function () {
        var currNode = global.zTreeObj.getSelectedNodes()[0];
        ajax3(service_prefix.metadata, "/clearRecycle", "get", {tableId: currNode.tableId}).then(function (res) {
            getDelMetadataList(1);
            layer.closeAll();
        })
    })
}

/*导出元数据*/
active.exportMetadata = function () {
    layerOpen("导出元数据", 500, 250);
    var currNode = global.zTreeObj.getSelectedNodes()[0];
    lodTpl("exportMetadataTpl", "openDiv", {tableId: currNode.tableId,columnid:currNode.id});
    $("#otherParm").html($("#searchForm #toCopy").clone());
    $("#exportMetadataForm").attr("action", getAjaxUrl("", global.metadataUri + "/export"));
}

active.passAll = function () {
    ajax("/metadata/statusPass?status=10&tableName=" + tableName, "post", JSON.stringify(checkChecked("metadataTable"))).then(function (res) {
        reflashData();
    })
}

active.unpassAll = function () {
    ajax("/metadata/statusPass?status=20&tableName=" + tableName, "post", JSON.stringify(checkChecked("metadataTable"))).then(function (res) {
        reflashData();
    })
}

/*active.ysjfz = function () {
    console.log("...............wau")
    var datas = getTableCheckDataid("metadataTable");
    if (datas) {
        showColumnTree(datas, copyMetadata, "元数据复制", "checkbox");
    }
}*/

active.ysjyyyy = function () {

    /*var treeObj = $.fn.zTree.getZTreeObj("tree");
    var nodes= treeObj.getSelectedNodes();*/
    var currnode=global.zTreeObj.getSelectedNodes()[0];
    var datas=currnode.tableId;
    if (datas) {
        showColumnTreee(datas, yiyongsum, "元数据引用", "checkbox");
    }
}


active.ysjyy = function () {
    var datas = getTableCheckData("metadataTable");
    if (datas) {
        showColumnTree(datas, yinYongMetadata, "元数据引用", "checkbox");
    }
}

active.ysjyd = function () {
    var datas = getTableCheckData("metadataTable");
    if (datas) {
        showColumnTree(datas, yiDongMetadata, "元数据移动", "radio");
    }
}

function showYinyong() {
    showColumnTree([], setYidongColumn, "栏目引用", "radio");
}

function reflashData(pageNo) {
    getMetadataList(pageNo ? pageNo : $("#metadataPager .layui-laypage-skip .layui-input").val());
}

function reflashDataPublish(no, pageNo) {
    // if(no == 1) layer.msg("日期大于当天和审核未通过的早报没有发布.");
    getMetadataList(pageNo ? pageNo : $("#metadataPager .layui-laypage-skip .layui-input").val());
}

layui.form.on("submit(addColumn)", function (data) {
    var id = data.field.id;
    if(data.field.chnlOrder=='NaN'){
        layer.alert("前一栏目错误!");
        return false;
    }
    data.field.status = 0;
    if(lanmutype){
        getlanmutype(data)
    }
    if (id) {
        type = "put";
        url = "/programa/" + id;
    } else {
        var type = "post";
        var url = "/programa";
    }
    ajax(service_prefix.manage, url, type, JSON.stringify(data.field)).then(function (res) {
        if (res.success) {
            layer.closeAll();
            if(data.field.ismenu){
                if(id){
                    setmenu(id,data.field);
                }else {
                    menui = res.obj.id;
                    setmenu(menui,data.field);
                }
            }
            columnList();
            getTreeData(id ? id : data.field.parentId);
        } else {
            layer.alert(res.msg)
        }
    })
    return false;
})

function editColumn(id, type) {
    ajax(service_prefix.manage, "/programa/" + id, "get", {}).then(function (data) {
        if (data.success) {
            showColumnDiv(data.obj, type);
        } else {
            layer.alert(data.msg);
        }
    })
}


function showColumnInfo() {
    var currNode = global.zTreeObj.getSelectedNodes()[0];
    ajax(service_prefix.manage, "/programa/" + currNode.id, "get", {}).then(function (data) {
        if (data.success) {
            var columnInfo = data.obj;
            lodTpl("columnDetailTpl", "columnDetail", data.obj);
            if (global.metadataUri) {
                $("#json1").text(global.metadataUri + "/{id}");
                $("#json2").text(global.metadataUri + "/list");
                $("#json3").text(global.metadataUri + "/updateDocStatus");
                $("#json4").text(global.metadataUri + "/{ids}/batch");
            }
            // getDetgitailRelation(columnInfo);
            if (columnInfo.imgColumn && columnInfo.imgColumn != 0) {
                ajax(service_prefix.manage, "/programa/" + columnInfo.imgColumn, "get", {}).then(function (res) {
                    $("#imgColumnName").text(res.obj.name);
                })
            }

            if (columnInfo.fileColumn && columnInfo.fileColumn != 0) {
                ajax(service_prefix.manage, "/programa/" + columnInfo.fileColumn, "get", {}).then(function (res) {
                    $("#fileColumnName").text(res.obj.name);
                })
            }

            if (columnInfo.audioColumn && columnInfo.audioColumn != 0) {
                ajax(service_prefix.manage, "/programa/" + columnInfo.audioColumn, "get", {}).then(function (res) {
                    $("#audioColumnName").text(res.obj.name);
                })
            }

            if (columnInfo.videoColumn && columnInfo.videoColumn != 0) {
                ajax(service_prefix.manage, "/programa/" + columnInfo.videoColumn, "get", {}).then(function (res) {
                    $("#videoColumnName").text(res.obj.name);
                })
            }
        } else {
            layer.alert(data.msg);
        }
    })
}

function getDetailRelation(columnInfo) {
    if (columnInfo.tableId) {
        getTableInfoById(columnInfo.tableId).then(function (res) {
            $("#columnTableName").text(res.anothername);
        })
    }
    if (columnInfo.siteId) {
        getSiteInfoById(columnInfo.siteId).then(function (res) {
            $("#columnSiteName").text(res.name);
        })
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
    ajax(service_prefix.manage, "/site/all", "post", {}).then(function (res) {
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
    ajax(service_prefix.manage, "/template/" + id, "get", {}).then(function (res) {
        if (res.success) {
            if (res.obj.siteid) {
                ajax(service_prefix.manage, "/site/" + res.obj.siteid, "get", {}).then(function (siteResult) {
                    if (siteResult.success) {
                        layerOpenFull("修改模板");
                        lodTpl("templetTpl", "openDiv", {siteName: siteResult.obj.name});
                        $("#text1").initTextarea();
                        layui.form.val("templetForm", res.obj);
                    } else {
                        layerOpenFull("修改模板");
                        lodTpl("templetTpl", "openDiv", {siteName: ""});
                        $("#text1").initTextarea();
                        layui.form.val("templetForm", res.obj);
                    }
                })
            } else {
                layerOpenFull("修改模板");
                lodTpl("templetTpl", "openDiv", {siteName: ""});
                $("#text1").initTextarea();
                layui.form.val("templetForm", data);
            }
        }
    })
}


function getColumnTreeData(webclass,async) {
    return new Promise(function (resovle, reject) {
        var flag;
        if(async>=0){
            flag = async == 1;
        }else{
            flag = getColumnTreeAsync();
        }
        if (flag) {
            ajax2(service_prefix.manage, "/site/getSiteTree", "get", {webclass: webclass}).then(function (data) {
                resovle(data.obj);
            })
        } else {
            ajax2(service_prefix.manage, "/programa/getTreeData", "post", {webclass: webclass}).then(function (data) {
                resovle(data.obj);
            })
        }
    })
}

var openZTreeObj;
var columnTreeIndex;

function showColumnTree(datas, fn, title, checkType, checks, noChecks, checked) {
    columnTreeIndex = layer.open({
        title: title
        , area: ['355px', '500px']
        , content: '<ul id="ydTree" class="ztree openTree"></ul>'
        , btn: ['确定', '取消']
        , yes: function (index, layero) {
            var nodes = getCheckNodesIds(openZTreeObj);
            var ids = nodes.ids;
            if (!ids || ids.length <= 0) {
                layer.alert("请选择栏目");
                return null;
            }
            if(fn){
                fn(ids, datas, nodes.names);
                layer.close(columnTreeIndex);
            }
        }
        , btn2: function (index, layero) {
            layer.close(index);
        }
    })
    getColumnTreeData(1).then(function (res) {
        function ajaxTreeDataFilter1(treeId, parentNode, responseData) {
            if (responseData) {
                for(var o of responseData){
                    var flag = false;
                    for(var k in checks){
                        if (o[k] != checks[k]){
                            flag = true;
                        }
                    }
                    for(var k in noChecks){
                        if (o[k] == noChecks[k]){
                            flag = true;
                        }
                    }
                    o.nocheck = flag;

                    /*if(checked.indexOf(o.id) >= 0) o.checked = true;*/
                }
            }
            return responseData;
        }
        var flag = getColumnTreeAsync();
        var settingss = {
            data: {
                simpleData: {
                    enable: true, //true 、 false 分别表示 使用 、 不使用 简单数据模式
                    idKey: "id", //节点数据中保存唯一标识的属性名称
                    pIdKey: "parentId", //节点数据中保存其父节点唯一标识的属性名称
                    rootPId: -1 //用于修正根节点父节点数据，即 pIdKey 指定的属性值
                }
            },
            async: {
                enable: flag,
                autoParam: ["id"],
                type: "get",
                url: getAjaxUrl('',"/manage/programa/asyncTree"),
                headers:{'mspToken':com.jnetdata.mspToken},
                dataFilter: ajaxTreeDataFilter1,
                xhrFields: {withCredentials: true}
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
                chkboxType: {"Y": "", "N": ""},  //表示父子节点的联动效果，不联动
                nocheckInherit: false
            }

        }
        openZTreeObj = $.fn.zTree.init($("#ydTree"), settingss, res);

        if (!flag) {
            openZTreeObj.expandAll(true); //true 节点全部展开、false节点收缩
        }else{
            var pnodes = openZTreeObj.getNodesByFilter(function (node) {
                return node.isSite == 0
            });
            pnodes.forEach(function (res) {
                openZTreeObj.expandNode(res, true);
            })
        }
    })
}

function showColumnTreee(datas, fn, title, checkType, checks, noChecks, checked) {
    columnTreeIndex = layer.open({
        title: title
        , area: ['355px', '500px']
        , content: '<ul id="ydTree" class="ztree openTree"></ul>'
        , btn: ['确定', '取消']
        , yes: function (index, layero) {
            var nodes = getCheckNodesIds(openZTreeObj);
            var ids = nodes.ids;
            var compid=nodes.parentid;
            if (!ids || ids.length <= 0) {
                layer.alert("请选择栏目");
                return null;
            }
            if(compid==0){
                compid=nodes.ids;
            }
            if(fn){
                fn(datas, ids);
                layer.close(columnTreeIndex);
            }
        }
        , btn2: function (index, layero) {
            layer.close(index);
        }
    })
    getColumnTreeData(1).then(function (res) {
        function ajaxTreeDataFilter1(treeId, parentNode, responseData) {
            if (responseData) {
                for(var o of responseData){
                    var flag = false;
                    for(var k in checks){
                        if (o[k] != checks[k]){
                            flag = true;
                        }
                    }
                    for(var k in noChecks){
                        if (o[k] == noChecks[k]){
                            flag = true;
                        }
                    }
                    o.nocheck = flag;

                    /*if(checked.indexOf(o.id) >= 0) o.checked = true;*/
                }
            }
            return responseData;
        }
        var flag = getColumnTreeAsync();
        var settingss = {
            data: {
                simpleData: {
                    enable: true, //true 、 false 分别表示 使用 、 不使用 简单数据模式
                    idKey: "id", //节点数据中保存唯一标识的属性名称
                    pIdKey: "parentId", //节点数据中保存其父节点唯一标识的属性名称
                    rootPId: -1 //用于修正根节点父节点数据，即 pIdKey 指定的属性值
                }
            },
            async: {
                enable: flag,
                autoParam: ["id"],
                type: "get",
                url: getAjaxUrl('',"/manage/programa/asyncTree"),
                headers:{'mspToken':com.jnetdata.mspToken},
                dataFilter: ajaxTreeDataFilter1,
                xhrFields: {withCredentials: true}
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
                chkboxType: {"Y": "", "N": ""},  //表示父子节点的联动效果，不联动
                nocheckInherit: false
            }
        }
        openZTreeObj = $.fn.zTree.init($("#ydTree"), settingss, res);
        if (!flag) {
            openZTreeObj.expandAll(true); //true 节点全部展开、false节点收缩
        }else{
            var pnodes = openZTreeObj.getNodesByFilter(function (node) {
                return node.isSite == 0
            });
            pnodes.forEach(function (res) {
                openZTreeObj.expandNode(res, true);
            })
        }
    })
}

function yulanthis() {
    layerOpenTpl("预览", 743, 733, "yulantpl", {});
}

function copyMetadata(columnIds, datas) {
    for (var i in columnIds) {
        for (var j in datas) {
            datas[j].columnid = columnIds[i];
            ajax("", global.metadataUri, "post", JSON.stringify(datas[j])).then(function (res) {

            })
        }
    }
    layer.alert("复制成功");
}

function yinYongMetadata(columnIds, datas) {
    for (var i in columnIds) {
        for (var j in datas) {
            datas[j].columnid = columnIds[i];
            datas[j].status = 40;
            ajax("", global.metadataUri, "post", JSON.stringify(datas[j])).then(function (res) {

            })
        }
    }
    layer.alert("引用成功");
}

function yiyongsum(id,ids) {
    ajax(service_prefix.manage,"/manage/programa/citeadd?id="+id+"&pid"+ids,"get",{}.then(function (res) {
    }))

    layer.alert("引用成功");


}

function yiDongMetadata(columnIds, datas) {
    for (var j in datas) {
        datas[j].columnid = columnIds[0];
        ajax("", global.metadataUri + "/" + datas[j].id, "put", JSON.stringify(datas[j])).then(function (res) {
            getMetadataList();
        })
    }
    layer.alert("移动成功");
}

function setYidongColumn(columnIds, datas) {
    // for(var j in datas){
    //     datas[j].columnId = columnIds[0];
    //     ajax("",metadataUri+"/"+datas[j].id,"put",JSON.stringify(datas[j])).then(function(res){
    //         getMetadataList();
    //     })
    // }
    $("#quotaid").val(columnIds[0]);
    layer.close(columnTreeIndex);

}

active.generatorHtml = function () {
    var columnObj = global.zTreeObj.getSelectedNodes()[0];
    ajax2(service_prefix.manage, "/programa/createHtml", "post", {columnId: columnObj.id}).then(function (res) {
        if (res.success) {
            layer.alert("发布成功！");
            showTab("infoTab");
        } else {
            layer.alert(res.msg);
        }
    })
}
function getFieldHtml(field) {
    var html = "";
    switch (field.fieldtype) {
        case 1:
            html = '<input type="text" name="{0}" placeholder="请输入" autocomplete="off" class="layui-input" lay-verify="{1}">';
            break;
        case 2:
            html = '<input type="password" name="{0}" placeholder="请输入" autocomplete="off" class="layui-input" lay-verify="{1}">';
            break;
        case 3:
            html = '<textarea name="{0}" placeholder="请输入" class="layui-textarea" lay-verify="{1}"></textarea>';
            break;
        case 4:
            html = '<input type="text" name="{0}" id="date_edit_{0}" placeholder="yyyy-MM-dd HH:mm:ss" autocomplete="off" class="layui-input" lay-verify="{1}">'
            break;
        case 20:
            html = '<input type="text" name="{0}" id="date_edit_{0}" placeholder="yyyy-MM-dd" autocomplete="off" class="layui-input" lay-verify="{1}">'
            break;
        case 6:
            var enmvalue = field.enmvalue.split(",");
            for (var i in enmvalue) {
                html += '<input type="radio" name="{0}" value="{1}" title="{1}" {2}>'.format(field.proName, enmvalue[i], i == 0 ? "checked" : "");
            }
            break;
        case 7:
            var enmvalue = field.enmvalue.split(",");
            for (var i in enmvalue) {
                html += '<input type="checkbox" name="check_{0}_{1}" lay-skin="primary" title="{1}">'.format(field.proName, enmvalue[i]);
            }
            break;
        case 8:
            html = '<select name="{0}" lay-filter="{0}" lay-verify="{1}">';
            var enmvalue = field.enmvalue.split(",");
            for (var i in enmvalue) {
                html += '<option value="{0}">{0}</option>'.format(enmvalue[i]);
            }
            html += '</select>';
            break;
        case 15:
            html = '<div class="layui-upload-list" id="img_{0}">\n' +
                '   <p id="demoText"></p>\n' +
                '</div>\n' +
                '<button type="button" class="layui-btn" id="upbtn_{0}">上传图片</button>\n' +
                '<input type="hidden" id="up_{0}" name="{0}">'
            break;
        case 14:
            html = '<div class="layui-upload-list">\n' +
                '   <input disable id="img_{0}" class="layui-input">\n' +
                '   <p id="demoText"></p>\n' +
                '</div>\n' +
                '<button type="button" class="layui-btn" id="upbtn_{0}">上传视频</button>\n' +
                '<input type="text" class="layui-input" id="up_{0}" name="{0}" placeholder="外链地址,可填写视频文件地址">'
            break;
        case 16:
            html = '<div class="layui-upload-list">\n' +
                '   <input disable id="img_{0}" class="layui-input">\n' +
                '   <p id="demoText"></p>\n' +
                '</div>\n' +
                '<button type="button" class="layui-btn" id="upbtn_{0}">上传音频</button>\n' +
                '<input type="hidden" id="up_{0}" name="{0}">'
            break;
        case 17:
            html = '<div class="layui-upload-list">\n' +
                '   <input disable id="img_{0}" class="layui-input">\n' +
                '   <p id="demoText"></p>\n' +
                '</div>\n' +
                '<button type="button" class="layui-btn" id="upbtn_{0}">上传文件</button>\n' +
                '<input type="hidden" id="up_{0}" name="{0}">'
            break;
        case 18:
            html = '<textarea style="width: 1026px;min-height:300px" name="{0}" id="ue_{0}" placeholder="请输入" lay-verify="{1}"></textarea>';
            break;
        case 13:
            html = '<input type="text" onclick="showMetadataFormClassTree(\'{0}\',\''+(field.classid?field.classid:field.classparentid)+'\')" id="{0}_class_name" name="{0}_class_name" placeholder="请输入" autocomplete="off" class="layui-input" lay-verify="{1}">'
            html += '<input type="hidden" id="{0}_class_hidden"  name="{0}" lay-verify="{1}">'
            break;
        default:
            html = '<input type="text" name="{0}" placeholder="请输入" autocomplete="off" class="layui-input" lay-verify="{1}">';
    }
    return html.format(field.proName, getVerifyStr(field.verifyType, field.regularStr));
}

function getVerifyStr(verifyType, regx, verifyinfo) {
    var str;
    switch (verifyType) {
        case 1:
            str = 'required';
            break;
        case 2:
            str = 'required|number';
            break;
        case 3:
            str = 'required|englishletter';
            break;
        case 4:
            str = 'required|username';
            break;
        case 5:
            str = 'required|pass';
            break;
        case 6:
            str = 'required|phone';
            break;
        case 7:
            str = 'required|email';
            break;
        case 8:
            str = 'required|identity';
            break;
        case 9:
            str = 'required|bankcard';
            break;
        case 10:
            str = 'required|carnum';
            break;
        case 11:
            str = 'required|chinese';
            break;
        case 90:
            str = getThisVerify(regx, verifyinfo);
            break;
        default:
            str = '';
    }
    return str;
}

function getThisVerify(rex, verifyinfo) {
    if (!verifyinfo) verifyinfo = '输入内容不匹配';
    var nowstr = guid();
    var thisverify = {};
    thisverify[nowstr] = [new RegExp(rex), verifyinfo];
    layui.form.verify(thisverify);
    return 'required|' + nowstr;
}

function getChnlDataPath(obj) {
    var str = $(obj).val();
    var nodes = global.zTreeObj.getSelectedNodes()[0].getPath();
    var path = "";
    for (var o of nodes) {
        if (o.isSite == 0) continue;
        var p = o.isSite == 1 ? o.code : o.skuNumber;
        path += p + "\\";
    }
    $("#chnlDataPath").val(path + str);
}

function getColumnCurrNode(){
    return global.zTreeObj.getSelectedNodes()[0];
}


/******************************************分发***************************************************/
function showFenfaTab(){
    global.showTabFn = "showFenfaTab()";
    getFenfaList();
}

function getFenfaList(current,size) {
    if(!size) size = 14;
    if(!current) current = 1;
    var currNode = global.zTreeObj.getSelectedNodes()[0];
    ajax(service_prefix.manage, "/fenfa/listing", "post", JSON.stringify({"entity": {columnid: currNode.id}, "pager": {current: current,size: size}})).then(function (data) {
        for(var d of data.obj.records){
            d.syncwhilestr = getFenfaSyncwhile(d.syncwhile);
        }

        layui.table.render({
            elem: '#fenfaTable'
            , cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
            , cols: [[
                {type: 'checkbox', field: "id"}
                , {type: 'numbers', title: '序号'}
                , {field: 'columnname', title: '目标栏目'}
                , {field: 'starttime', title: '分发开始时间'}
                , {field: 'endtime', title: '分发结束时间'}
                , {field: 'filetime', title: '文档创建开始时间'}
                , {field: 'syncwhilestr', title: '同步时机'}
                , {field: 'synctype', title: '分发模式', templet: '<div><span>{{d.synctype==0?"复制":d.synctype==1?"引用":d.synctype==2?"镜像":""}}</span></div>'}
            ]]
            , data: data.obj.records
            , limit: 10
        })
        loadPage2("fenfapage",data.obj,getFenfaList);
    })
}

function getFenfaSyncwhile(str){
    if(!str) return '';
    var results = [];
    if(str.indexOf(0) >= 0) results.push("新建时");
    if(str.indexOf(1) >= 0) results.push("修改时");
    if(str.indexOf(2) >= 0) results.push("发布后同步且不发布");
    if(str.indexOf(3) >= 0) results.push("发布后同步且发布");
    return results.join("；");

}

$("#newFenfa").click(function(){
    layerOpenHtml("新建栏目分发",512,472,"simple/manage/column/fenfaForm.html")
})

$("#editFenfa").click(function(){
    var id = getTableEditId("fenfaTable");
    if(id)
        layerOpenHtml("修改栏目分发",512,472,"simple/manage/column/fenfaForm.html",null, "showFenfaData('"+id+"')");
})

$("#delFenfa").click(function(){
    var ids = checkChecked("fenfaTable");
    ajax("","/manage/fenfa/real/"+ids.join()+"/batch","delete").then(function(res){
        getFenfaList();
    })
})
function addclasspushf() {
    if (advancedAttributeData !=undefined&&advancedAttributeData.id != undefined&&advancedAttributeData!="") {
        var pageNo = 1;
        var currnode = zTreeObj.getSelectedNodes()[0];
        /*var datasss = getTableCheckDataid("metadataTable")[0];*/
        var currNode111 = global.zTreeObj.getSelectedNodes()[0]
        var id = currNode111.tableId;
        var names = currNode111.name;
        var jsondata = {
            "pager": {
                "current": pageNo,
                "size": pageSize,
            },
            "entity": {
                "classid": currnode.id,
                "classname": currnode.className,
                "pushname": names,
                "pushid": id,
                "xwid": advancedAttributeData.id,
                "tableid": currNode111.tableId
            }
        }
        ajax("", "/addpushcotegory/push/add", "post", JSON.stringify(jsondata)).then(function (data) {
            if (data.success) {
                if (data.success) {
                    advancedAttributeData="";
                    console.log(".........................adv");
                    console.log(advancedAttributeData)
                    layer.closeAll();
                } else {
                    layer.alert(data.msg);
                }
            }
        })
    } else {
        layer.alert("请先保存数据");
    }
}
    //}
    /*   let param=data.field;*/


function addpushClassName() {
    /*var datas = getTableCheckDataiSystem("metadataTable");*/
    /*if (datas) {
        showColumnTreeSystem(datas, copyMetadata, "元数据复制", "checkbox");
    }*/
    if(advancedAttributeData !=undefined && advancedAttributeData.id!=undefined) {
        showColumnTreeSystem(copyMetadata, "元数据复制", "checkbox");
    }else {
            layer.alert("请先保存数据");
            return false;
    }
}



function showColumnTreeSystem(fn, title, checkType, checks, noChecks, checked) {
    columnTreeIndex = layer.open({
        title: title
        , area: ['355px', '500px']
        , content: '<ul id="ydTree" class="ztree openTree"></ul>'
        , btn: ['确定', '取消']
        , yes: function (index, layero) {
            addpush2()
            /*layer.closeAll();*/
        }
        , btn2: function (index, layero) {
            layer.close(index);
        }
    })
    getColumnTreeDataSystem(1).then(function (res) {
        function ajaxTreeDataFilter1(treeId, parentNode, responseData) {
            if (responseData) {
                for(var o of responseData){
                    var flag = false;
                    for(var k in checks){
                        if (o[k] != checks[k]){
                            flag = true;
                        }
                    }
                    for(var k in noChecks){
                        if (o[k] == noChecks[k]){
                            flag = true;
                        }
                    }
                    o.nocheck = flag;
                }
            }
            return responseData;
        }
        var flag = getColumnTreeAsync();
        var settingss = {
            data: {
                simpleData: {
                    enable: true, //true 、 false 分别表示 使用 、 不使用 简单数据模式
                    idKey: "id", //节点数据中保存唯一标识的属性名称
                    pIdKey: "parentId", //节点数据中保存其父节点唯一标识的属性名称
                    rootPId: -1 //用于修正根节点父节点数据，即 pIdKey 指定的属性值
                }
            },
            async: {
                enable: flag,
                autoParam: ["id"],
                type: "get",
                url: getAjaxUrl('',"/manage/programa/asyncTree"),
                headers:{'mspToken':com.jnetdata.mspToken},
                dataFilter: ajaxTreeDataFilter1,
                xhrFields: {withCredentials: true}
            },
            view: {
                showLine: false,
                showIcon: true
            },
            check: {                    //表示tree的节点在点击时的相关设置
                enable: false,           //是否显示radio/checkbox
                autoCheckTrigger: false,
                chkStyle: checkType,      //值为checkbox或者radio表示
                // radioType:"all",
                chkboxType: {"Y": "", "N": ""},  //表示父子节点的联动效果，不联动
                nocheckInherit: false
            },
            /*  callback: {
                  onClick:ztreetime1l
              }*/
        }
        openZTreeObjjj = $.fn.zTree.init($("#ydTree"), settingss, res);
        if (!flag) {
            openZTreeObj.expandAll(true); //true 节点全部展开、false节点收缩
        }else{
            var pnodes = openZTreeObj.getNodesByFilter(function (node) {
                return node.isSite == 0
            });
            pnodes.forEach(function (res) {
                openZTreeObj.expandNode(res, true);
            })
        }
    })

    function addpush2() {
        var currNode111 = global.zTreeObj.getSelectedNodes()[0]
        var currnode=global.zTreeObj.getSelectedNodes()[0];
        var datas=currnode.tableId;
        var crunode=openZTreeObjjj.getSelectedNodes()[0];
        var pageNo=1;
        var cruser =localStorage.getItem("username")
        var jsondata = {
            "pager": {
                "current": pageNo,
                "size": pageSize,
            },
            "entity": {
                "xwid": advancedAttributeData.id,
                "name": crunode.name,
                "nameid": crunode.id,
                "pushtableid": crunode.tableId,
                "tableid": datas,
                "cruser": cruser,
                "lanmid": currNode111.id,
                "lanmname": currNode111.name
            }
        }
        ajax("","/trtable/push/add","post",JSON.stringify(jsondata)).then(data=>{
            if(data.success){
                addMetadatapush1(0)
                layer.closeAll();
            }else {
                layer.alert("请选择相同表的栏目")
            }
        })

    }
    function addMetadatapush1(status) {
        /*var currNode = global.zTreeObj.getSelectedNodes()[0];*/
        var currNode=openZTreeObjjj.getSelectedNodes()[0];
        var fields = advancedAttributeData
        fields.id="";
        var checks = {};
        /*for (var i in fields) {
            if (i.indexOf("check_") == 0) {
                console.log(".................check_")
                var vals = i.split("_");
                if (!checks[vals[1]]) {
                    console.log("..................vals[1]")
                    checks[vals[1]] = [];
                }
                checks[vals[1]].push(vals[2]);
            }
        }
        for (var i in checks) {
            console.log("......................checks")
            fields[i] = checks[i].join(",");
        }*/
        var url = "";
        var type = "";
        url = global.metadataUri;
        type = "post";
        fields.columnid = currNode.id;
        fields.docstatus = 0;
        if (status) fields.status = status;
        ajax("", url, type, JSON.stringify(fields)).then(function (res) {
            if (!res.success) {
                layer.alert(res.msg);
            } else {
                if (!fields.id && currNode.workFlow) {
                    delete fields["id"];
                    //startFlow($.extend({id:res.obj.id},fields), true);
                }
                var dataid = fields.id ? fields.id : res.obj.id;
                /* if (status && status == 21) {
                     if (!fields.docchannelid || data.docchannelid == -1) {
                         fields.docstatus = -1;
                         ajax("", global.metadataUri, "post", JSON.stringify(fields)).then(function (res) {
                             if (res.success) {
                                 ajax("", global.metadataUri + "/" + dataid, "put", JSON.stringify({docchannelid: res.obj.id})).then(function (res) {
                                 })
                             } else {
                                 layer.alert(res.msg);
                             }
                         })
                     } else {
                         fields.docstatus = -1;
                         ajax("", global.metadataUri + "/" + fields.docchannelid, "put", JSON.stringify(fields)).then(function (res) {
                             if (!res.success) {
                                 layer.alert(res.msg);
                             }
                             layer.alert("成功");
                         })
                     }
                 } else {
                     layer.alert("失败")
                 }*/
                layer.alert("添加推送成功");

            }
        })
    }
}
function getColumnTreeDataSystem(webclass,async) {
    return new Promise(function (resovle, reject) {
        var flag;
        if(async>=0){
            flag = async == 1;
        }else{
            flag = getColumnTreeAsync();
        }
        if (flag) {
            ajax2(service_prefix.manage, "/site/getSiteTree", "get", {webclass: webclass}).then(function (data) {
                resovle(data.obj);
            })
        } else {
            ajax2(service_prefix.manage, "/programa/getTreeData", "post", {webclass: webclass}).then(function (data) {
                resovle(data.obj);
            })
        }
    })
}
/*
function getTableCheckDataiSystem(table){
    var data = getTableCheck23(table);
    console.log(data);
    if(data.length<=0){
        layer.alert("请选择数据");
        return false;
    }
    return data;
}*/
/*添加元数据调用*/
function addMetadata1(){
    layer.open({
        type: 1,
        title:"添加引用",
        area:['1100px', '700px']
        // ,offset: type //具体配置参考：http://doc/modules/layer.html#offset
        ,id: 'layerDemo' //防止重复弹出
        /*,content: '<div style="padding: 20px 100px;">'+ 1111 +'</div>'*/
        ,content:'<div id="ttttt"></div>'
        ,btn: ['确定','取消']
        ,btnAlign: 'c' //按钮居中
        ,shade: 0 //不显示遮罩
        ,yes: function(){
            addMetadataPort();
            layer.closeAll();
        },
        done:function(){
            layui.form.render();
        },
        success: function(layero){
            $("#ttttt").load("simple\\manage\\column\\tjyy.html");
            layer.setTop(layero); //重点2
            columnTree();
        },
        zIndex: layer.zIndex

    })
}
function addMetadataPort() {
    var currnode=global.zTreeObj.getSelectedNodes()[0];
    var datas=currnode.tableId;
    var crunode=zTreeObjj1.getSelectedNodes()[0];
    var pageNo=1;
    var cruser =localStorage.getItem("username")
    var jsondata = {
        "pager": {
            "current": pageNo,
            "size": pageSize,
        },
        "entity": {
            "xwid": advancedAttributeData.id,
            "xwname": advancedAttributeData.cstext,
            "name": crunode.name,
            "nameid": crunode.id,
            "pushid": currnode.id,
            "pushname": currnode.name,
            "tableid": datas,
            "cruser": cruser
        }
    }
    ajax("","/addreference/pushg/add","post",JSON.stringify(jsondata)).then(data=>{
        if(data.success){
            layer.alert("添加元数据成功")

            layer.closeAll();
            /* $("#classid").html(html2);
             layui.form.render("select")
             var val1 = document.getElementById("classid").value;*/
        }else {
            layer.alert(res.msg)
        }
    })
}
function columnTree(selectedId){
    ajax(service_prefix.member,"/group/tree","post",{}).then(res=>{
        treeNode = res.obj;
        showTreerPort1(res.obj,selectedId);
    }).catch(function(res){

    })
}
var zTreeObjj1;
function showTreerPort1(data,selectedId) {
    zTreeObjj1 = $.fn.zTree.init($(".treeDemoq23"), settingsslll, data); //初始化树
    zTreeObjj1.expandAll(true);    //true 节点全部展开、false节点收缩
    if(selectedId){
        var node = zTreeObjj1.getNodesByParam("id", selectedId, null)[0];
        // $("#"+node.tId + "_a").trigger("click")
    }else{
        // $("#treeDemo_2_a").trigger("click");
    }
}

$(document).on("click",".list .kuang-r",function () {
    $(this).addClass("list-bg").siblings().removeClass("list-bg");
    $(".lists>div").eq($(this).index()).addClass("show").siblings().removeClass("show");
    columnTree();
})
var settingsslll = {
    edit: {
        enable: true,
        showRemoveBtn: false,
        showRenameBtn: false,
        drag: {
            isCopy: false,//所有操作都是move
            isMove: true,
            prev: true,
            next: true,
            inner: false
        }
    },
    data: {
        simpleData: {
            enable: true,  //true 、 false 分别表示 使用 、 不使用 简单数据模式
            idKey: "id",  //节点数据中保存唯一标识的属性名称
            pIdKey: "parentId",    //节点数据中保存其父节点唯一标识的属性名称
            rootPId: -1  //用于修正根节点父节点数据，即 pIdKey 指定的属性值
        }
    },
    /*  check: {
          enable: true,            //true / false 分别表示 显示 / 不显示 复选框或单选框
          autoCheckTrigger: true,   //true / false 分别表示 触发 / 不触发 事件回调函数
          chkStyle: "checkbox",     //勾选框类型(checkbox 或 radio）
          chkboxType: {"Y":"","N":""}
      },*/
    view: { showLine: false,showIcon:true },
    /* callback: {
        // onClick: zTreeOnClick,
         beforeDrop: beforeDrop,
         onDrop: onDrop
     }*/
    callback: {
        onClick:ztreetime14
    }
};
var nameid;
function ztreetime14(event,treeid,treeNode) {
    if(treeNode.treeType>0){
        nameid=treeNode.id;
        doListt1(1);

    }
}
function doListt1(current) {
    var jsondata={
        "pager":{
            "current":1,
            "size":pageSize,
        },
        "entity":{
            "nameid": nameid
        }
    }
    ajax("","/addreference/pushg/listing","post",JSON.stringify(jsondata)).then(function (data){
        if(data.success){
            layui.use(['table','laypage'],function () {
                var table=layui.table;
                /*,laypage=layui.laypage;*/
                table.render({
                    elem:'#test23'
                    //elem:'#subscribeTabel'
                    ,data:data.obj.records
                    ,cols: [[
                        {type: 'checkbox', fixed: 'left'},
                        {type:'numbers',title: '序号',fixed: 'left'}
                        ,{field:'xwid', width: 276, title: 'id',fixed: 'left'}
                        ,{field:'crtime', width: 170, title: '创建时间'}
                        ,{field:'cruser', width: 141, title: '创建人'}
                    ]],
                    id: "id"
                    ,limit: 10
                })
                laypage.render({
                    elem:'demo9'
                    ,count:data.obj.total
                    ,curr:data.obj.current
                    ,limit:pageSize
                    ,layout:['count','prev','page','next','refresh','skip']
                    ,jump:function (obj,first) {
                        if (!first){
                            getUserLogin(obj.curr)
                        }
                    }
                })
            })

        }
    })
}

/*新增中添加引用*/
function addReference2() {
    var datasss = getTableCheckDataid("metadataTable")[0];
    var currNode111 = global.zTreeObj.getSelectedNodes()[0]
   /* var currnode=global.zTreeObj.getSelectedNodes()[0];*/
    var datas=currnode.tableId;
    var crunode=openZTreeObjjj.getSelectedNodes()[0];
    var pageNo=1;
    var cruser =localStorage.getItem("username")
    var jsondata = {
        "pager": {
            "current": pageNo,
            "size": pageSize,
        },
        "entity": {
            "xwid": advancedAttributeData.id,
            "name": crunode.name,
            "nameid": crunode.id,
            "pushtableid": crunode.tableId,
            "tableid": datas,
            "cruser": cruser,
            "lanmid": currNode111.id,
            "lanmname": currNode111.name
        }
    }
    ajax("","/trtable/push/add","post",JSON.stringify(jsondata)).then(data=>{
        if(data.success){
            /*identifyym();*/
            layer.closeAll();
            /* $("#classid").html(html2);
             layui.form.render("select")
             var val1 = document.getElementById("classid").value;*/
        }else {
            layer.alert("请选择相同表的栏目")
        }
    })
}

function showMetadataFormClassTree(field,classid){
    var classIndex = layer.open({
        type: 1,
        title: "选择子分类法",
        area: ['340px', '600px'],
        maxmin: true,
        content: '<div id="addChildTreeForm" class="h100"></div>',
    });
    layui.laytpl($("#childtreeTemplate").html()).render({}, function (html) {
        $("#addChildTreeForm").html(html);
        getChildTree();
    });
//ztree
    var settings = {
        data: {
            simpleData: {
                enable: true,  //true 、 false 分别表示 使用 、 不使用 简单数据模式
                idKey: "id",  //节点数据中保存唯一标识的属性名称
                pIdKey: "parentId",    //节点数据中保存其父节点唯一标识的属性名称
                rootPId: 0  //用于修正根节点父节点数据，即 pIdKey 指定的属性值
            },
            key: {
                name: "className"
            }
        },
        view: {showLine: false, showIcon: true}
    };

    function getChildTree() {
        var jsondata = {
            "parentId": classid
        }
        ajax(service_prefix.metadata, "/class/tree", "post", JSON.stringify(jsondata)).then(function(data) {
            var res = data.obj;
            if (res) {
                showTree(res);
            }
        })
    }
    //渲染树结构
    function showTree(data) {
        zTreeObj = $.fn.zTree.init($("#metadataFormClassTreeUl"), settings, data); //初始化树
    }

    layui.form.on('submit(selectChildClass)', function (data) {
        var treeObj = $.fn.zTree.getZTreeObj("metadataFormClassTreeUl");
        var nodes = treeObj.getSelectedNodes();
        var data = {};
        data[field+"_class_name"] = nodes[0].className;
        data[field] = nodes[0].id;
        layui.form.val("addMetadataForm", data);
        layui.form.render();
        layer.close(classIndex);
        return false;
    });

    function classMetadataFormClassTree(){
        layer.close(classIndex);
    }
}
