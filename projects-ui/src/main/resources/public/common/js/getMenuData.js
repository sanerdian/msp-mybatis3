function renderTree(limitNames,limits,zTreeNodes,settingsFn,treeTag,title,data,expandAll,limitType,isCheck){
    var length = limits.length;
    if(limits.length > 1) length += 1;
    var width = Math.floor(84 / length * 100) / 100;
    var setting = {
        view: {
            showLine: false,
            showIcon: true,
            addDiyDom: addDiyDom
        },
        data: {
            simpleData: {
                enable: true,
                pIdKey: "parentId"
            },
            key: {
                url: 'url1'
            }
        }
    };

    if(settingsFn) settingsFn(setting);

    /**
     * 自定义DOM节点
     */
    function addDiyDom(treeId, treeNode) {
        var spaceWidth = 15;
        var liObj = $("#" + treeNode.tId);
        var aObj = $("#" + treeNode.tId + "_a");
        var switchObj = $("#" + treeNode.tId + "_switch");
        var icoObj = $("#" + treeNode.tId + "_ico");
        var spanObj = $("#" + treeNode.tId + "_span");
        aObj.attr('title', '');
        aObj.append('<div class="divTd swich fnt" style="width:15%"></div>');
        var div = $(liObj).find('div').eq(0);
        //从默认的位置移除
        switchObj.remove();
        spanObj.remove();
        icoObj.remove();
        //在指定的div中添加
        div.append(switchObj);
        div.append(spanObj);
        //隐藏了层次的span
        var spaceStr = "<span style='height:1px;display: inline-block;width:" + (spaceWidth * treeNode.level) + "px'></span>";
        switchObj.before(spaceStr);
        //图标垂直居中
        // icoObj.css("margin-top","9px");
        switchObj.after(icoObj);
        var editStr = '';
        if(limits.length>1) {
            editStr = '<div class="divTd"  style="width:' + width + '%"></div>';
            if (!isCheck || isCheck(treeNode)) {
                editStr = '<div class="divTd"  style="width:' + width + '%"><input type="checkbox" name="checkBox1" lay-skin="primary" lay-filter="checkAll1"></div>';
            }
        }
        //宽度需要和表头保持一致
        for (var o of limits) {
            editStr += '<div class="divTd"  style="width:' + width + '%">' + opt(treeNode, o) + '</div>';
        }
        aObj.append(editStr);
    }

    //初始化列表
    function queryHandler() {
        //初始化树
        var zTreeObj = $.fn.zTree.init($("#"+treeTag), setting, zTreeNodes);
        if(expandAll) {
            zTreeObj.expandAll(true);
        }else{
            var pnodes = zTreeObj.getNodesByFilter(function (node) {
                return node.isSite == 0
            });
            pnodes.forEach(function (res) {
                zTreeObj.expandNode(res, true);
            })
        }

        //添加表头
        var li_head = ' <li class="head"><div class="divTd" style="width:15%">'+title+'</div>';
        if(limits.length > 1) li_head += '<div class="divTd" style="width:' + width + '%">全选</div>';
        for (var o of limitNames) {
            li_head += '<div class="divTd" style="width:' + width + '%">' + o + '</div>';
        }
        li_head += '</li>';
        var li_headCheck = ' <li class="headCheck"><div class="divTd divTdFirst" style="width:15%">全选</div>';
        if (limits.length>1){
            li_headCheck += '<div class="divTd" style="width:' + width + '%"><input type="checkbox" name="checkBox1" lay-skin="primary" lay-filter="checkAll2"></div>';
        }
        for (var o of limits) {
            li_headCheck += '<div class="divTd" style="width:' + width + '%"><input type="checkbox" name="checkBox" value="' + o + '" lay-skin="primary" lay-filter="checkAll"></div>';
        }
        li_headCheck += '</li>';
        var rows = $("#"+treeTag).find('li');
        if (rows.length > 0) {
            rows.eq(0).before(li_head);
            rows.eq(0).before(li_headCheck);
        } else {
            $("#"+treeTag).append(li_head);
            $("#"+treeTag).append('<li ><div style="text-align: center;line-height: 15px;">无符合条件数据</div></li>')
        }
        data.forEach(function (item) {
            $("#"+treeTag+" input[value='" + item + "']").prop("checked", true);
        });
        layui.form.render();
    }

    function opt(treeNode, str) {
        var htmlStr = '';
        if (!isCheck || isCheck(treeNode)) {
            if(treeNode.checks && treeNode.checks.indexOf(str)>=0){
                htmlStr += '<input type="checkbox" alt="' + str + '" name="limitBox" node_id="'+treeNode.id+'" value="'+limitType+':' + str + ":" + treeNode.id + '" lay-skin="primary" checked lay-filter="checkItem">';
            }else{
                htmlStr += '<input type="checkbox" alt="' + str + '" name="limitBox" node_id="'+treeNode.id+'" value="'+limitType+':' + str + ":" + treeNode.id + '" lay-skin="primary" lay-filter="checkItem">';
            }
        }
        return htmlStr;
    }

    queryHandler();
}


/**
 * 获取前台菜单类权限树 结构数据
 */
function getFrontMenuDate(data){
    var limitNames = ['新建', '修改', '删除', '列表查看', '排序', '移动'];
    var limits = ['add', 'edit', 'delete', 'view', 'sort', 'move'];
    ajax(service_prefix.member, "/menu/tree", "get", {type: 2}).then(function(res){
        if (res.success) {
            renderTree(limitNames,limits,res.obj,false,"frontMenuDataTree","前台菜单",data,true,"menu",function(treeNode){return treeNode.url});
        }
    })
}

/**
 * 获取后台菜单类权限树 结构数据
 */
function getMenuDate(data) {
    var limitNames = ['新建', '修改', '删除', '列表查看', '排序', '移动'];
    var limits = ['add', 'edit', 'delete', 'view', 'sort', 'move'];

    ajax(service_prefix.member, "/menu/tree", "get", {type: 1}).then(function(res){
        if (res.success) {
            renderTree(limitNames,limits,res.obj,false,"dataTree","后台菜单",data,true,"menu",function(treeNode){return treeNode.url});
        }
    })
}

/**
 * 获取栏目类权限 树结构数据
 */
function getProgramaDate(data) {
    var limitNames = ['新建', '修改', '删除', '元数据信息列表' ,'审核', '元数据查看', '授权子栏目','子栏目继承'];
    var limits = ["add", "edit", "delete", "list", "limit", "metadataView", "child", 'inherit'];

    var flag = getColumnTreeAsync();
    var setting = false;
    var datas;
    var expandAll = true;

    if (flag) {
        setting = function(settings){
            settings.async = {
                enable: true,
                autoParam: ["id"],
                type: "get",
                url: getAjaxUrl('', "/manage/programa/asyncTree"),
                xhrFields: {withCredentials: true}
            }
            settings.callback = {
                onAsyncSuccess: function () {
                    data.forEach(function (item) {
                        $("#programaLimit input[value='" + item + "']").prop("checked", true);
                    });
                    layui.form.render();
                }
            }
        }
        datas = limitSiteDatas;
        expandAll = !flag;
    } else {
        getProgramaTreeData(function(res){
            datas = res.obj;
        })
    }
    renderTree(limitNames, limits, datas, setting, "programaDataTree", "栏目", data,expandAll,"column",function(treeNode){return treeNode.isSite != 0 && treeNode.isSite != 1});
}

/**
 * 获取元数据信息类权限 树结构数据
 */
function getMetadataDate(data,id){
    var limitNames = ['新建','修改','删除','导入','导出','审核','引用','复制','移动','排序','置顶','子栏目继承'];
    var limits = ['add','edit','delete','import','export','examine','cite','copy','move','sort','top', 'inherit'];

    var flag = getColumnTreeAsync();
    var setting = false;
    var datas;
    var expandAll = true;

    if (flag) {
        var ajaxDataFilter = function(treeId, parentNode, responseData){
            if (responseData) {
                if(updatePermissions['metadata:inherit:'+parentNode.id] || (data.indexOf('metadata:inherit:' + parentNode.id)>=0 && updatePermissions['metadata:inherit:' + parentNode.id]!=false)){
                    var checks = [];
                    for(var i of limits){
                        if(updatePermissions['metadata:'+i+':'+parentNode.id] || (data.indexOf('metadata:'+i+':' + parentNode.id)>=0 && updatePermissions['metadata:'+i+':' + parentNode.id]!=false)){
                            checks.push(i);
                        }
                    }
                    for(var o of responseData){
                        o.checks = checks;
                    }
                }
            }
            return responseData;
        }
        var getParams = function(treeId,treeNode){
            var isInherit = $("#metadataDataTree input[value='metadata:inherit:" + treeNode.id + "']").attr("checked");
            if(isInherit){
                var tags = $("#metadataDataTree input[node_id='" + treeNode.id + "']:checked");
                var checks = [];
                for(var o of tags){a
                    checks.push($(o).attr("alt"));
                }
                return {"userId": id,checks:checks};
            }
            return {"userId": id};
        }
        setting = function(settings){
            settings.async = {
                enable: true,
                autoParam: ["id"],
                type: "post",
                contentType: "application/json",
                url: getAjaxUrl('', "/manage/programa/asyncTreeForSetLimit"),
                xhrFields: {withCredentials: true},
                otherParam: getParams,
                // dataFilter: ajaxDataFilter
            }
            settings.callback = {
                onAsyncSuccess: function () {
                    // data.forEach(function (item) {
                    //     $("#metadataDataTree input[value='" + item + "']").prop("checked", true);
                    // });
                    layui.form.render();
                }
            }
        }
        datas = limitSiteDatas;
        expandAll = !flag;
    } else {
        getProgramaTreeData(function(res){
            datas = res.obj;
        })
    }
    renderTree(limitNames, limits, datas, setting, "metadataDataTree", "元数据信息", data,expandAll,"metadata",function(treeNode){return treeNode.isSite != 0 && treeNode.isSite != 1});
}

/**
 * 获取组织设置 树结构数据
 */
function getGroupDate(data){
    var limitNames = ['设置子组织','查看组织列表','子组织继承上一级组织权限','设置组长','设置用户'];
    var limits = ['child','peer','inherit','setLeader','user'];

    // var flag = ifGroupTreeAsync();
    var flag = true;
    var setting = false;
    var datas;
    var expandAll = true;

    if (flag) {
        ajax(service_prefix.member,"/group/asyncTreeCompanys","post").then(function(res){
            if(res.success){
                setting = function(settings){
                    settings.async = {
                        enable: true,
                        autoParam: ["id"],
                        type: "get",
                        url: getAjaxUrl('', "/member/group/asyncTree"),
                        xhrFields: {withCredentials: true}
                    }
                    settings.callback = {
                        onAsyncSuccess: function () {
                            data.forEach(function (item) {
                                $("#groupDataTree input[value='" + item + "']").prop("checked", true);
                            });
                            layui.form.render();
                        }
                    }
                }
                datas = res.obj;
                renderTree(limitNames, limits, datas, setting, "groupDataTree", "组织", data, false,"group");
            }
        })
    } else {
        ajax(service_prefix.member,"/group/tree","post").then(res=>{
            if(res.success){
                //初始化数据
                datas = res.obj;
                renderTree(limitNames, limits, datas, setting, "groupDataTree", "组织", data,expandAll,"group");
            }
        })
    }
}

function getCompanyDate(data){
    var limitNames = ['新建','修改','删除','列表','排序','授权'];
    var limits = ['add','edit','delete','view','sort','limit'];
    getCompanyTreeData(function(res){
        renderTree(limitNames,limits,res,false,"companyRightInfoTable","机构",data,true,"company");
    })
}


function getSiteDate(data){
    var limitNames = ['新建','修改','删除','导入','导出','停用','恢复','复制','发布','发布首页','移动','查看','回收站','权限设置','栏目回收站','栏目还原','栏目回收站清空','分发','排序','工作流管理'];
    var limits = ["add","update","delete","import","export","stop","restore","copy","publish","pubindex","move","view","recycle","limit","columnrecycle","columnrevert","deletecolumnrecycle","fenfa","sort","flow"];
    renderTree(limitNames,limits,limitSiteDatas,false,"siteRightInfoTable","站点",data,true,"site",function(treeNode){return treeNode.isSite==1});
}

function getClassDate(data){
    var limitNames = ['查看'];
    var limits = ["view"];
    var setting = function(settings){
        settings.data.key = {name : "className"};
    }
    ajax(service_prefix.metadata, "/class/tree", "post", JSON.stringify({"parentId": 0})).then(function(res) {
        if (res.obj) {
            renderTree(limitNames,limits,res.obj,setting,"classRightInfoTable","分类法",data,true,"class");
        }
    })
}

function getMetadataModuleDate(data){
    var limitNames = ['查看','新增','修改','删除'];
    var limits = ["view",'add','edit','del'];
    ajaxJsonPost("/metadata/moduleinfo/list",{"pager": {"current": 1, "size": -1, "sortProps": [{"key": "createTime", "value": false}]}},function(res1){
        var cols = [
            {field: 'modulename', title: '名称', align: "left"}
            ]
        for (let i = 0; i < limitNames.length; i++) {
            cols.push({field:'',title:limitNames[i],align: 'center',templet:'<div><input type="checkbox" name="limitBox" value="metadataModule:'+limits[i]+':{{d.id}}" lay-skin="primary" lay-filter="checkItem"></div>'})
        }
        layui.table.render({
            elem: '#metadataModuleInfoTable'
            , cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
            , cols: [cols]
            , text: {
                none: ''
            }
            ,height: 520
            ,data: res1.obj.records
            ,limit: res1.obj.records.length
        });
        data.forEach(function (item) {
            $("#metadataModuleInfoItem input[value='" + item + "']").prop("checked", true);
        });
    })
    layui.form.render();
}

function getManageDate(data){
    var limits = [{modulename:'重启服务',limit:"manage:server:restart"}];
    var cols = [
        {field: 'modulename', title: '名称', align: "center"},
        {field: '', title: '授权',align: 'center', templet:'<div><input type="checkbox" name="limitBox" value="{{d.limit}}" lay-skin="primary" lay-filter="checkItem"></div>'}
    ]
    layui.table.render({
        elem: '#manageInfoTable'
        , cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
        , cols: [cols]
        , text: {
            none: ''
        }
        ,height: 520
        ,data: limits
        ,limit: limits.length
    });
    data.forEach(function (item) {
        $("#manageInfoItem input[value='" + item + "']").prop("checked", true);
    });
    layui.form.render();
}