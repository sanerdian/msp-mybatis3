var updatePermissions = {};
var limitSiteDatas;
/**
 * 权限管理
 * @param {*} id
 * @param {*} name
 * @param {*} type
 */
// function setLimit(id,name,type) {
//     updatePermissions = {};
//     layer.open({
//         type: 1,
//         area: ["1300px", "680px"],
//         title: name+'&nbsp&nbsp&nbsp&nbsp权限管理',
//         maxmin: false,
//         content: '<div id="openDiv"></div>'
//     })
//     lodTpl("setLimitTpl","openDiv",{id:id,name:name,type:type});
//     getRightInfo(id,type);
// }

/**
 * 获取权限
 * @param {获取获取} id 授权对象id
 * @param {*} type 类型（0: 用户、1: 角色、2: 组）
 */
function getRightInfo (id,type) {
    // var uri = type==0?"userPermission":type==1?"rolePermission":"groupPermission";
    ajax(service_prefix.member,"/permission/allStr","post",JSON.stringify({ownerId:id,ownerType:type})).then(function (data) {
        getSiteTreeDatas(function(datas){
            limitSiteDatas = datas;
            getSiteDate(data.obj);
            getProgramaDate(data.obj);
            getMetadataDate(data.obj,id);
        })
        getCompanyDate(data.obj);
        getFrontMenuDate(data.obj);
        getMenuDate(data.obj);
        getGroupDate(data.obj);
        getClassDate(data.obj);
        getMetadataModuleDate(data.obj);
        getManageDate(data.obj);
        // notSystemLimit(data.obj);
        // getApiAppDate(data.obj);
    })
}

function getApiAppDate(data){
    ajax2(service_prefix.gateway,"/app/listAll","post").then(function (res) {
        layui.table.render({
            elem: '#apiAppRightInfo'
            ,data:res.obj
            ,cellMinWidth: 80
            ,limit:res.obj.length
            ,cols: [[{field:'name', width:250, title: '应用'}
                ,{field:'read',title: '可读',align:"center",templet:'<div><input type="checkbox" name="limitBox" value="apiApp:read:{{d.id}}" lay-skin="primary"></div>'}
                ,{field:'write',title: '可写',align:"center",templet:'<div><input type="checkbox" name="limitBox" value="apiApp:write:{{d.id}}" lay-skin="primary"></div>'}
            ]]
            ,done:function(){
                data.forEach(function(item){
                    $(".apiAppRightInfo input[value='"+item+"'").prop("checked",true);
                });
                layui.form.render();
            }
        });
    })
    .catch(err => {
        console.log(err)
    })
}

function getLimitTableCols(fields) {
    var cols = [{checkbox: true}];
    var index = 0;
    for (var i in fields) {
        if(index > 10) break;
        for (var f of fields[i]){
            if(index > 10) break;
            if (f.showList == 1) {
                var col = {field: f.proName, title: f.anothername, align: "left", minWidth: f.width};
                if (f.fieldtype == 15) {
                    col = {field: '', title: f.anothername, align: "center", minWidth: f.width, style: 'background:#d4d4d4;'};
                    col.templet = '<div><img src="{{d.' + f.proName + '?getAjaxUrl(\'\',d.' + f.proName + '):\'\'}}"></div>'
                }
                if (index == 0) {
                    var col = {field: f.proName, title: f.anothername, align: "left", minWidth: f.width};
                }
                cols.push(col);
                index++;
            }
        }
    }
    return cols;
}

function xfefwef(limits,tableId,tableName){
    var limitId = 'metadataLimit_'+tableId;
    var html = '<div class="layui-tab-item">\n' +
        '                    <table id="'+limitId+'" lay-filter="'+limitId+'"></table>\n' +
        '                    <div id="'+limitId+'_page"></div>\n' +
        '                    <div class="layui-btn-container">\n' +
        '                        <div class="button-bar">\n' +
        '                            <button class="layui-btn" lay-submit lay-filter="addRightInfo">确认</button>\n' +
        '                            <button class="layui-btn" type="button" data-type="close">取消</button>\n' +
        '                        </div>\n' +
        '                    </div>\n' +
        '                </div>';
    $("#getAllLimit").append('<li>' + tableName + '</li>');
    $("#limitContent").append(html)
    ajaxGet("metadata/tableInfo/"+tableId,function(res){
        getDatas(1,10)
        function getDatas(pageNo,size){
            ajaxJsonPost(res.obj.url+"/listing",{"pager": {"current": pageNo, "size": size, "sortProps": [{"key": "createTime", "value": false}]}},function(res1){
                var fields = res.obj.fieldinfos
                var cols = getLimitTableCols(fields)
                var datas = res1.obj.records;
                for(var o of datas){
                    if(updatePermissions[tableId + ':limit:' + o.id] || (limits.indexOf(tableId + ':limit:' + o.id)>=0 && updatePermissions[tableId + ':limit:' + o.id]!=false)){
                        o.LAY_CHECKED = true;
                    }
                }
                layui.table.render({
                    elem: '#'+limitId
                    , data: datas
                    , cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
                    , cols: [cols]
                    , limit: 10
                });
                layui.table.on('checkbox('+limitId+')', function(obj){
                    if(obj.type == 'one') {
                        updatePermissions[tableId+':limit:'+obj.data.id] = obj.checked;
                    }else{
                        var ids = getTableAllDataId(limitId);
                        for(var id of ids){
                            updatePermissions[tableId+':limit:'+id] = obj.checked;
                        }
                    }
                });
                loadPage2(limitId+"_page",res1.obj,getDatas)
            })
        }
    })
}

//全选
layui.form.on('checkbox(checkAll)', function (data) {
    var child = $(data.elem).parents('.layui-tab-item').find('input[alt="'+data.value+'"]');
    child.each(function (index, item) {
        item.checked = data.elem.checked;
        updatePermissions[$(item).val()] = data.elem.checked;
    });
    form.render('checkbox');
});

//全选功能
layui.form.on('checkbox(checkAll1)', function (data) {
    var child = $(data.elem).parents('li:first').find('input');
    // child.checked = data.elem.checked;
    child.each(function (index, item) {
        item.checked = data.elem.checked;
        updatePermissions[$(item).val()] = data.elem.checked;
    });
    form.render('checkbox');
});

//全选功能
layui.form.on('checkbox(checkAll2)', function (data) {

    var child = $(data.elem).parents('.ztree:first').find('input');
    // child.checked = data.elem.checked;
    child.each(function (index, item) {
        item.checked = data.elem.checked;
        updatePermissions[$(item).val()] = data.elem.checked;
    });
    form.render('checkbox');
});

//全选和部分选中时候,表头全选按钮的样式变化
form.on('checkbox(checkItem)', function (data) {
    updatePermissions[$(data.elem).val()] = data.elem.checked;
    var par = $(data.elem).parents('.layui-tab-item');
    var sib = par.find('input[alt="'+data.elem.alt+'"]:checked').length;
    var total = par.find('input[alt="'+data.elem.alt+'"]').length;
    if (sib == total) {
        par.find('.headCheck input[value="'+data.elem.alt+'"]').prop("checked", true);
        form.render();
    } else {
        par.find('.headCheck input[value="'+data.elem.alt+'"]').prop("checked",false);
        form.render();
    }
});

function notSystemLimit(datas){
    ajaxGet("member/limit/type/getAll",function(res){
        res.obj.forEach(function (data) {
            if(data.type == 'metadata') xfefwef(datas,data.tableId,data.name)
        })
    })
}
