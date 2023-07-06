//表单组件设置元数据
var widgetFormMetadataId = ''; //表单组件的元数据表id

/*
* 点击显示组件菜单
*/
function showModuleMenu(){
    $('.widget_box').click(function(e){
        var widgetId = $(this).attr('id'); //组件元素id
        var createModuleId = $(this).attr('data-module-id'); //组件id
        var widgetCustomId = $(this).find('div').attr('id'); //定制组件元素id
        var moduleType = $(this).attr('data-module-type'); //组件类型
        widgetFormMetadataId = $('.visual_form_metadata_button').parent().parent().attr('data-metadata-id'); //表单组件的元数据表id
        activeWidgetBorder(widgetId);
        widgetMenuShow(widgetId);
        console.log(widgetId,createModuleId, moduleType, widgetFormMetadataId);
        if(moduleType == 'table'){
            clickSet('80%',widgetId, createModuleId, moduleType, widgetCustomId);
        }else if(moduleType == 'formelement' || moduleType == 'chart'){
            clickSet('70%',widgetId, createModuleId, moduleType, widgetCustomId);
        }else{
            clickSet('50%',widgetId, createModuleId, moduleType, widgetCustomId);
        }
        deleteModule(widgetId, createModuleId, moduleType); //点击删除按钮，删除组件
        return false;
    });
}
showModuleMenu();

/*
* 取消边框
* @param widgetId 组件元素
*/
function removeAllWidgetBorder(widgetId) {
    $('#'+ widgetId).find(".widget_border_content").addClass("widget_inactive").removeClass("widget_active");
}

/*
* 添加边框
* @param widgetId 组件元素
*/
function activeWidgetBorder(widgetId) {
    $('#'+ widgetId).find(".widget_border_content:visible:first").addClass("widget_active").removeClass("widget_inactive");
}

/*
* 隐藏组件菜单
* @param widgetId 组件元素
*/
function widgetMenuHide(widgetId){
    $('#'+ widgetId + ' .widget_menu').hide();
}

/*
* 显示组件菜单
* @param widgetId 组件元素
*/
function widgetMenuShow(widgetId){
    $('#'+ widgetId + ' .widget_menu').show();
}

/*
* 获取元数据列表
* @param widgetId 组件元素
* @param createModuleId 组件id
* @param moduleType 组件类型
* @param tableId 选中的元数据表id
* @param checkedFields 选中的字段
*/
function metadataTable(widgetId, moduleType, createModuleId, tableId, checkedFields){
    ajax(service_prefix.metadata,"/all", "post", {}).then(function (data) {
        if (data.success) {
            var html = '';
            for (var i in data.obj) {
                html += '<option value="' + data.obj[i].id + '">' + data.obj[i].anothername + ' ['+ data.obj[i].tablename + ']' + '</option>';
            }
            $('#' + widgetId + 'Metadata').html(html);
            form.render();
            if(tableId){
                metadataTableFields(widgetId, createModuleId, moduleType, tableId, checkedFields); //设置选中的字段返显
                $('#'+ widgetId + 'Metadata').find('option[value=' + tableId +']').attr("selected",true); //设置选中的元数据表返显
            }else{
                metadataTableFields(widgetId, createModuleId, moduleType, data.obj[0].id); //默认获取第一个元数据表中的字段
            }
            //改变元数据表下拉时，显示相应元数据表的字段
            form.on('select(' + widgetId + 'Metadata' +')', function(data){
                metadataTableFields(widgetId, createModuleId, moduleType, data.value);
            });
        } else {
            console.log(data.msg);
        }
    });
}     

/*
* 获取元数据表中的字段 设置字段的事件、样式名、排序
* @param widgetId 组件元素
* @param createModuleId 组件id
* @param moduleType 组件类型
* @param tableId 选中的元数据表id
* @param myCheckedFiels 选中的字段
*/
function metadataTableFields(widgetId, createModuleId, moduleType, tableId, myCheckedFiels){
    console.log('myCheckedFiels',myCheckedFiels)
    if(createModuleId){
        var params = {
            "dbTableId": tableId,
            "moduleId": createModuleId,
            "moduleType": moduleType
        }
    }else{
        var params = {
            "dbTableId": tableId
        }
    }
    ajax(service_prefix.visual, "/relationmodulefield/list", "post", JSON.stringify(params)).then(function (data) {
        if (data.success) {
            var html = '';
            for (var i in data.obj) {
                var fileType = data.obj[i].fieldtype; //字段类型
                var enmvalue = data.obj[i].enmvalue; //字段枚举值

                //根据组件的字段类型显示对应的设置字段
                var typeHtml = '';
                var typeValHtml = '';
                var typeCustomHtml = '<div class="visual_select">' +
                    '<select name="eventType">' +
                        '<option value="">请选择事件</option>' +
                        '<option value="Click">单击事件</option>' +
                    '</select>' +
                '</div>' +
                '<div class="visual_select">' +
                    '<select name="styleFlag">' +
                        '<option value="">是否有样式名</option>' +
                        '<option value="1">是</option>' +
                        '<option value="0">否</option>' +
                    '</select>' +
                '</div>';
                if(moduleType == 'textlist' || moduleType == 'carousel'){ //文字列表、图片轮播组件
                    typeHtml =  typeCustomHtml;
                }else if(moduleType == 'table'){ //表格组件                    
                    typeHtml =  typeCustomHtml + 
                    '<div class="visual_input">' + //表格组件 宽度
                        '<input type="text" name="showWidth" autocomplete="off" placeholder="请输入宽度" class="layui-input">' +
                    '</div>'+
                    '<div class="visual_select">' + //表格组件 传参字段
                        '<select name="isPassGinseng">' +
                            '<option value="">是否默认传参</option>' +
                            '<option value="1">是</option>' +
                            '<option value="0">否</option>' +
                        '</select>' +
                    '</div>';
                    if(fileType == 6 || fileType == 7 || fileType == 8){ //表格组件 单选框、多选框、下拉选择框字段值
                        if(enmvalue){
                            var valArr = enmvalue.split(',');
                            for(var j in valArr){
                                typeValHtml += '<option value="' + valArr[j] + '">' + valArr[j] + '</option>';
                            }
                            typeHtml += '<div class="visual_select">' + //表格组件 传参值
                                '<select name="fieldValue">' +
                                    '<option value="0">请选择参数值</option>' +
                                    typeValHtml +
                                '</select>' +
                            '</div>';
                        }
                    }else{
                        typeHtml += '<div class="visual_input">' + //表格组件 传参值
                            '<input type="text" name="fieldValue" autocomplete="off" placeholder="请输入传参值" class="layui-input">' +
                        '</div>';
                    }
                }else if(moduleType == 'formelement'){//表单组件
                    // typeHtml = typeCustomHtml + '<div class="visual_select">' + //表单组件 必填项
                    //     '<select name="isChoose">' +
                    //         '<option value="">是否必填项</option>' +
                    //         '<option value="1">是</option>' +
                    //         '<option value="0">否</option>' +
                    //     '</select>' +
                    // '</div>';
                    typeHtml = '<div class="visual_select">' + //表单组件 必填项
                        '<select name="isChoose">' +
                            '<option value="">是否必填项</option>' +
                            '<option value="1">是</option>' +
                            '<option value="0">否</option>' +
                        '</select>' +
                    '</div>';
                    if(fileType == 4){ //日期类型
                        typeHtml += '<div class="visual_select">' +
                            '<select name="dateType">' +
                                '<option value="date">请选择日期类型</option>' +
                                '<option value="date">年月日</option>' +
                                '<option value="month">年月</option>' +
                                '<option value="year">年</option>' +
                                '<option value="datetime">年月日时分秒</option>' +
                                '<option value="time">时分秒</option>' +
                            '</select>' +
                        '</div>';
                    }else if(fileType == 14 || fileType == 15 || fileType == 16 || fileType == 17){ //表单组件 上传视频、图片、音频、文件的扩展名
                        typeHtml += '<div class="visual_input">' +
                            '<input type="text" name="fileExtension" autocomplete="off" placeholder="请输入上传扩展名" class="layui-input">' +
                        '</div>';
                    }
                }else if(moduleType == 'chart'){//图表组件
                    typeHtml += '<div class="visual_select">' +
                        '<select name="seriesType">' +
                            '<option value="1">请选择类型</option>' +
                            '<option value="bar">bar</option>' +
                            '<option value="line">line</option>' +
                        '</select>' +
                    '</div>' +
                    '<div class="visual_input">' +
                        '<input type="text" name="fieldColor" value="" placeholder="请选择颜色" class="layui-input" id="{{ d.widgetId }}ColorInput">' +
                        '<div id="{{ d.widgetId }}ColorPicker"></div>' + 
                    '</div>';

                }

                if(moduleType == 'form'){ //表单元数据表组件
                    html += '<li>' +
                        '<div class="visual_field_name">' +
                            data.obj[i].anothername + ' ('+ data.obj[i].fieldname  +')' +
                        '</div>' +
                    '</li>';
                }else{
                    html += '<li>' +
                        '<input type="checkbox" name="fieldList" lay-skin="primary" lay-filter="checkItemFields" data-fieldname="' + data.obj[i].fieldname + '" data-anothername="' + data.obj[i].anothername + '" data-fieldtype="' + data.obj[i].fieldtype + '" data-enmvalue="' + data.obj[i].enmvalue + '" value="' + data.obj[i].id + '" title="' + data.obj[i].anothername + ' ('+ data.obj[i].fieldname  +')' + '">' +
                        '<span class="visual_down" title="下移"></span>' +
                        '<span class="visual_up" title="上移"></span>' +
                        typeHtml + //根据组件类型显示对应的组件字段
                    '</li>';
                }
            }
            $('#' + widgetId + 'Fields').html(html);

            //返显选中的字段、事件、生成样式名称
            var checkboxFields = $('#' + widgetId + 'Settings input[type="checkbox"]');
            for(var i in myCheckedFiels){
                for (var k in checkboxFields) { //遍历checkbox所有复选框项
                    if (checkboxFields[k].value == myCheckedFiels[i].fieldId) {
                        checkboxFields[k].checked = true; //设置复选框选中项
                    }
                }
                $('#' + widgetId + 'Settings [name="eventType"]').eq(i).val(myCheckedFiels[i].eventType); //事件
                $('#' + widgetId + 'Settings [name="styleFlag"]').eq(i).val(myCheckedFiels[i].styleFlag); //是否有样式名
                $('#' + widgetId + 'Settings [name="isPassGinseng"]').eq(i).val(myCheckedFiels[i].isPassGinseng); //表格组件是否默认传参
                $('#' + widgetId + 'Settings [name="fieldValue"]').eq(i).val(myCheckedFiels[i].fieldValue); //表格组件传参值
                $('#' + widgetId + 'Settings [name="showWidth"]').eq(i).val(myCheckedFiels[i].showWidth); //表格组件列宽
                $('#' + widgetId + 'Settings [name="isChoose"]').eq(i).val(myCheckedFiels[i].isChoose); //表单组件是否必填
                $('#' + widgetId + 'Settings [name="fileExtension"]').eq(i).val(myCheckedFiels[i].fileExtension); //表单组件上传文件扩展名
                $('#' + widgetId + 'Settings [name="dateType"]').eq(i).val(myCheckedFiels[i].dateType); //表单组件日期类型
                $('#' + widgetId + 'Settings [name="seriesType"]').eq(i).val(myCheckedFiels[i].seriesType); //图表组件系列类型
                $('#' + widgetId + 'Settings [name="fieldColor"]').eq(i).val(myCheckedFiels[i].fieldColor); //图表组件选择颜色
                
            }
            form.render(); //设置完选中后记得渲染
            fieldsUp(); //字段上移
            fieldsDown(); //字段下移
        }else{
            console.log(data.msg);
        }
    })
}

/*
* 新建组件设置
* @param params 新建组件参数
* @param widgetId 组件元素
* @param moduleType 组件类型
*/
function addModuleSet(params, widgetId, moduleType){
    // console.log('params',params);
    var moduleUrl = 'module' + moduleType; //获取组件接口地址
    ajax(service_prefix.visual, moduleUrl, "post", JSON.stringify(params)).then(function (data) {
        if (data.success) {
            $('#'+ widgetId).attr('data-module-id', data.obj.id); //存放组件id
            setModuleStyle(widgetId); //设置组件样式
            // selectModuleList(data.obj.id, moduleType); //存放组件信息，用于模板保存组件
            layer.msg('设置成功');
        } else {
            layer.msg(data.msg);
        }
    });
}

/*
* 编辑组件设置
* @param params 编辑组件参数
* @param createModuleId 组件id
* @param moduleType 组件类型
*/
function editModuleSet(params, widgetId, createModuleId, moduleType){
    var moduleUrl = 'module' + moduleType; //获取组件接口地址
    ajax(service_prefix.visual, moduleUrl+ "/" + createModuleId, "put", JSON.stringify(params)).then(function (data) {
        if (data.success) {
            // selectModuleList(createModuleId, moduleType); //存放组件信息，用于模板保存组件，接口返回组件ID和类型后，需要去掉
            setModuleStyle(widgetId); //设置组件样式
            layer.msg('修改设置成功');
        } else {
            console.log(data.msg);
        }
    });
}

/*
* 点击删除按钮，更新组件数
* @param widgetId 组件元素
*/
function deleteModuleCount(widgetId) {
    layer.msg('删除成功');
    moduleIndex = moduleIndex -1; //更新拖拽的组件索引值
    // console.log('deletemoduleIndex',moduleIndex);
    // $('#' + widgetId).parent().parent().html('');
    // 删除组件
    $('#' + widgetId).parent().remove();
    
}

/*
* 点击删除按钮，删除组件
* @param widgetId 组件元素
* @param createModuleId 组件id
* @param moduleType 组件类型
*/
function deleteModule(widgetId, createModuleId, moduleType){
    $('#'+ widgetId + ' .widget_delete').click(function () {
        var moduleUrl = 'module' + moduleType; //获取组件接口地址
        layer.open({
            type: 1,
            title: false,
            id: widgetId + 'Settings',
            skin: 'layui-layer-molv',
            scrollbar:false,
            area: ['300px','150px'],
            btnAlign: 'c',
            // offset: 't',
            btn: ['确定', '取消'],
            content: '<div class="layer_box"><div class="delete_tip">删除后不能恢复，请确认是否要删除？</div></div>',
            yes: function(index, layero){
                if(createModuleId){
                    ajax(service_prefix.visual, moduleUrl+ "/" + createModuleId, "delete").then(function (data) {
                        if (data.success) {
                            deleteModuleCount(widgetId);
                        } else {
                            console.log(data.msg);
                        }
                    });
                }else{
                    deleteModuleCount(widgetId);
                }
                layer.close(index);
            },
            btn2:function(index, layero){
                layer.close(index);
            },
            cancel: function(index, layero){ 
                layer.close(index);
                return false; 
            }
        });
    })
}

/*
* 获取元数据栏目站点
* @param widgetId 组件元素
* @param checkedId 选中的站点id
*/
var zTreeObj;
function getSiteTree(widgetId, checkedId){
    var setting = {
        check: {
            enable: true,
            chkStyle: "radio",
            radioType: "level"
        },
        data: {
            simpleData: {
                enable: true
            }
        }
    };
	ajax(service_prefix.manage,"/site/getSiteTree","get",{}).then(function(data){
        var nodes = data.obj;
		// console.log('nodes',nodes);
        var newNodesArr = [];
        for(var i = 0; i < nodes.length; i++){
            var nodesChildren = nodes[i].sites;
            var newNodesChildrenArr = [];
            var newNodesChildrenObj = {};
            //站点数据列表
            for(k in nodesChildren){
                if(nodesChildren[k].id == checkedId){ //选中站点id返显
                    newNodesChildrenObj = {
                        "id": nodesChildren[k].id,
                        "isSite": nodesChildren[k].isSite,
                        "name": nodesChildren[k].name,
                        "sites": [],
                        "checked": true
                    }
                }else{
                    newNodesChildrenObj = {
                        "id": nodesChildren[k].id,
                        "isSite": nodesChildren[k].isSite,
                        "name": nodesChildren[k].name,
                        "sites": []
                    }
                }
                newNodesChildrenArr.push(newNodesChildrenObj);
            }
            //机构下的数据列表
            var newNodesObj = {
                "id": nodes[i].id,
                "isSite": 0,
                "name": nodes[i].name,
                "sites": newNodesChildrenArr
           };
           newNodesArr.push(newNodesObj);
        }
        setTreeIcon(newNodesArr);
        setTimeout(function(){
            zTreeObj = $.fn.zTree.init($('#' + widgetId + 'Tree'), setting, newNodesArr); //初始化树
            zTreeObj.expandAll(false); //true 节点全部展开、false节点收缩
        },2000);
	});
}

/*
* 获取中台菜单
* @param widgetId 组件元素
* @param checkedId 选中的站点id
*/
var zTreeMenuObj;
function getMenuTree(widgetId, checkedId){
    var setting = {
        check: {
            enable: true,
            chkStyle: "radio",
            radioType: "level"
        },
        data: {
            simpleData: {
                enable: true
            }
        }
    };
	ajax(service_prefix.member,"/menu/tree?type=2","get",{}).then(function(data){
        var nodes = data.obj;
        var newNodesArr = [];
        var newNodesAllArr = [];
        var newNodesChildrenArr = [];
        var newNodesChildrenObj = {};
        for(var o of nodes){
            if(o.parentId == 0){
                newNodesArr.push(o); //机构列表
            }else{
                newNodesChildrenArr.push(o);
            }
        }
        // console.log('newNodesArr',newNodesArr)
        // console.log('newNodesChildrenArr',newNodesChildrenArr)
        for(var i = 0; i < newNodesArr.length; i++){
            var newNodesChildrenListArr = [];
            for(var j = 0; j < newNodesChildrenArr.length; j++){
                if(newNodesArr[i].id == newNodesChildrenArr[j].companyId){
                    if(newNodesChildrenArr[j].id == checkedId){ //选中站点id返显
                        newNodesChildrenObj = {
                            "parentId": newNodesChildrenArr[j].parentId,
                            "id": newNodesChildrenArr[j].id,
                            "isSite": newNodesChildrenArr[j].isSite,
                            "name": newNodesChildrenArr[j].name,
                            "children": [],
                            "checked": true
                        }
                    }else{
                        newNodesChildrenObj = {
                            "parentId": newNodesChildrenArr[j].parentId,
                            "id": newNodesChildrenArr[j].id,
                            "isSite": newNodesChildrenArr[j].isSite,
                            "name": newNodesChildrenArr[j].name,
                            "children": []
                        }
                    }
                    newNodesChildrenListArr.push(newNodesChildrenObj);
                }
            }            
            var newNodesObj = {
                "parentId": newNodesArr[i].parentId,
                "id": newNodesArr[i].id,
                "isSite": newNodesArr[i].isSite,
                "name": newNodesArr[i].name,
                "children": newNodesChildrenListArr
           };
           newNodesAllArr.push(newNodesObj);
        }
        // console.log('newNodesAllArr',newNodesAllArr);
        delTreeChildrenMenu(newNodesAllArr);
        setTimeout(function(){
            zTreeMenuObj = $.fn.zTree.init($('#' + widgetId + 'TreeMenu'), setting, newNodesAllArr); //初始化树
            zTreeMenuObj.expandAll(false); //true 节点全部展开、false节点收缩
        },2000);
        
    })
}

/**
 * 删除ztree结构树栏目
 * @param {*} url
 * @param {*} name
 */
function delTreeChildrenMenu(data) {
    if(data){
        data.forEach(function(res) {
            res.icon = res.parentId == 0?"/common/img/u5548.png":res.parentId == res.companyId?"/common/img/u2615.png":"/common/img/u2628.png";
            res.url1 = res.url;
            res.url = "";
            if(res.children.length == 0){
                delete res["children"];
            }else{
                delTreeChildrenMenu(res.children);
            }
        })
    }
}
/*
* 获取元数据栏目
* @param widgetId 组件元素
* @param checkedId 选中的站点id
*/
function getColumnTree(widgetId, checkedId){
    // console.log('checkedId',checkedId)
    var setting = {
        check: {
            enable: true,
            chkStyle: "radio",
            radioType: "level"
        },
        data: {
            simpleData: {
				pIdKey: "parentId",
                enable: true
            }
        }
    };
	ajax(service_prefix.manage,"/programa/getTreeData","post",{}).then(function(data){
        var nodes = data.obj;
        var newNodesArr = [];
        for(var i = 0; i < nodes.length; i++){
            var newNodesObj = nodes;
            if(checkedId){
                if(newNodesObj[i].id == checkedId){ //选中栏目id返显
                    newNodesObj[i].checked = true;                
                }
            }
           newNodesArr.push(newNodesObj[i]);
        }        
        setColumnTreeIcon(newNodesArr);
        setTimeout(function(){
            zTreeObj = $.fn.zTree.init($('#' + widgetId + 'Tree'), setting, newNodesArr); //初始化树
            zTreeObj.expandAll(false); //true 节点全部展开、false节点收缩
        },1000);	  
	})
}

/**
* 设置元数据栏目图标
* @param data 元数据栏目数据
 */
function setColumnTreeIcon(data) {
    if(data){
        data.forEach(function(res) {
            switch (res.isSite) {
                case 0 :  res.icon = "/common/img/u5548.png"; break;
                case 1 :  res.icon = "/common/img/u5554.png"; break;
                case 2 :  res.icon = "/common/img/u2615.png"; break;
            }
            if(res.children1.length == 0){
                delete res["children"];
            }else{
                delTreeChildren(res.children1);
            }
        })
    }
}

/*
* 设置站点图标
* @param data 站点数据
*/
function setTreeIcon(data) {
    if(data){
        data.forEach(function(res) {
            switch (res.isSite) {
                case 0 :  res.icon = "/common/img/u5548.png"; break;
                case 1 :  res.icon = "/common/img/u5554.png"; break;
                case 2 :  res.icon = "/common/img/u2615.png"; break;
            }
            if(res.sites.length != 0){
                setTreeIcon(res.sites);
                res.children = res.sites;
            }
        })
    }
}

/**
 * 删除ztree结构树栏目
 * @param {*} url
 * @param {*} name
 */
function delTreeChildren(data) {
    if (data) {
        data.forEach(function (res) {
            if (res.children.length == 0) {
                delete res["children"];
            } else {
                delTreeChildren(res.children);
            }
        });
    }
}

/*
* 点击设置按钮弹出框
* @param widgetId 组件元素
* @param createModuleId 组件id
* @param moduleType 组件类型
* @param widgetCustomId 定制组件元素id
*/
function clickSet(width, widgetId, createModuleId, moduleType, widgetCustomId){
    $('#'+ widgetId + ' .widget_set').click(function () {
        layer.open({
            type: 1,
            title: false,
            id: widgetId + 'Settings',
            skin: 'layui-layer-molv',
            area: [width,'530px'],
            btnAlign: 'c',
            // offset: 't',
            btn: ['确定', '取消'],
            content: '<div class="layer_box" id="' + widgetId + 'SetView"></div>',
            success: function(layero, index){
                var widgetData = {
                    "widgetId": widgetId
                };
                var view = '#' + widgetId + 'SetView';
                var tpl = '#' + widgetId + ' .' + 'widget_set_tpl';
                getData(widgetData, tpl, view); //弹出框内容渲染
                getModuleSetForm(widgetId, createModuleId, moduleType, widgetCustomId); //设置文字样式和关联字段信息提交 
                // widgetMenuHide(widgetId); //隐藏组件菜单
            },
            yes: function(index, layero){
                getModuleSetForm(widgetId, createModuleId, moduleType, widgetCustomId); //设置文字样式和关联字段信息提交
                widgetMenuHide(widgetId); //隐藏组件菜单
                removeAllWidgetBorder(widgetId); //取消边框
                layer.close(index);
                layero.find('form').find('button[lay-submit]').click();
            },
            btn2:function(index, layero){
                widgetMenuHide(widgetId); //隐藏组件菜单
                removeAllWidgetBorder(widgetId); //取消边框
                layer.close(index);
            },
            cancel: function(index, layero){ 
                widgetMenuHide(widgetId); //隐藏组件菜单
                removeAllWidgetBorder(widgetId); //取消边框
                layer.close(index);
                return false; 
            }
        });
    })
}

/*
* 获取元数据栏目id
* @param params 表单字段
*/
function getColumnId(params){
    var checkedNodes = zTreeObj.getCheckedNodes(true);	
    // console.log('nodesChecked2',checkedNodes); 
    if(checkedNodes.length != 0){
        params.channelId = checkedNodes[0].id; //栏目id
        params.innerUrl = checkedNodes[0].listUrl; //栏目链接
    }
}

/*
* 自定义按钮组件 新建按钮
* @param widgetId 组件元素
*/
function customButtonAdd(widgetId){
    $('#' + widgetId + 'AddBtn').click(function(){
        $('#' + widgetId + 'Settings .visual_button_event').prepend($('#' + widgetId + 'Settings .visual_button_event_add .layui-form-item').clone(true).show());
        form.render();
    });
}

/*
* 自定义按钮组件 删除按钮
* @param widgetId 组件元素
*/
function customButtonDel(widgetId){
    $('.' + widgetId + 'DelBtn').click(function(){ //删除按钮
        $(this).parent().parent().parent().remove();
        form.render();
    });
}

/*
* 登录组件 站内/站外链接单选按钮切换
* @param widgetId 组件元素
* @param channelId 站内链接选中的元数据栏目id
*/
function loginSkip(widgetId, channelId){
    form.on('radio(' + widgetId + 'loginSkip)', function(data){
        // console.log(data.elem); //得到radio原始DOM对象
        // console.log(data.value); //被点击的radio的value值
        if(data.value == 1){ 
            if(channelId){
                getColumnTree(widgetId, channelId); //获取元数据栏目返显
            }else{
                getColumnTree(widgetId); //获取元数据栏目
            }
            $('#' + widgetId + 'Tree').show();
            $('#' + widgetId + 'Settings input[name="innerUrl"]').show();
            $('#' + widgetId + 'Settings input[name="outerUrl"]').hide();
        }else{
            $('#' + widgetId + 'Tree').hide();
            $('#' + widgetId + 'Settings input[name="innerUrl"]').hide();
            $('#' + widgetId + 'Settings input[name="outerUrl"]').show();
        }
    });
}

/*
* 登录组件 登录位置居中、居左、居右单选按钮切换
* @param widgetId 组件元素
* @param place 组件位置
*/
function loginPosition(widgetId, place){
    if(place == 'center'){
        $('#' + widgetId + 'Settings input[name="loginMargin"]').parent().parent().parent().hide();
        $('#' + widgetId + 'Settings input[name="loginHeight"]').parent().parent().parent().hide();
    }
    form.on('radio(' + widgetId + 'loginPosition)', function(data){
        if(data.value == 'center'){ 
            $('#' + widgetId + 'Settings input[name="loginMargin"]').parent().parent().parent().hide();
            $('#' + widgetId + 'Settings input[name="loginHeight"]').parent().parent().parent().hide();
        }else{
            $('#' + widgetId + 'Settings input[name="loginMargin"]').parent().parent().parent().show();
            $('#' + widgetId + 'Settings input[name="loginHeight"]').parent().parent().parent().show();
        }
    });
}

/*
* 菜单组件 菜单和栏目切换
* @param widgetId 组件元素
* @param siteId 选中的元数据栏目的站点id
* @param menuId 选中的中台菜单栏目id
*/
function menuChannelShow(widgetId, siteId, menuId){
    form.on('radio(' + widgetId + 'chooseMenu)', function(data){
        if(data.value == 'menu'){ 
            if(menuId){
                getMenuTree(widgetId, menuId); //获取中台菜单返显
            }else{
                getMenuTree(widgetId); //获取中台菜单
            }
            $('#' + widgetId + 'TreeMenu').show();
            $('#' + widgetId + 'Tree').hide();
        }else{
            if(siteId){
                getSiteTree(widgetId, siteId); //获取元数据栏目站点返显
            }else{
                getSiteTree(widgetId); //获取元数据栏目站点
            }
            $('#' + widgetId + 'TreeMenu').hide();
            $('#' + widgetId + 'Tree').show();
        }
    });
}

/*
* 设置文字样式和关联字段信息提交
* @param widgetId 组件元素
* @param createModuleId 组件id
* @param moduleType 组件类型
* @param widgetCustomId 定制组件元素id
*/
function getModuleSetForm(widgetId, createModuleId, moduleType, widgetCustomId){
    colorPicker('#' + widgetId + 'ColorInput', '#' + widgetId + 'ColorPicker', '#333'); //文字颜色选择器
    colorPicker('#' + widgetId + 'BackgroundColorInput', '#' + widgetId + 'BackgroundColorPicker', '#333'); //背景颜色选择器
    var params = {}; //存放设置参数
    var searchHtml = ''; //搜索组件条件结构
    // var formHtml = ''; //表单组件结构

    //根据组件类型，执行相应组件方法，来显示组件设置内容
    if(moduleType == 'table' || moduleType == 'search' || moduleType == 'detail' || moduleType == 'textlist' || moduleType == 'carousel' || moduleType == 'formelement' || moduleType == 'form' || moduleType == 'chart'){
        if(moduleType == 'textlist'){
            metadataTable(widgetId, moduleType); //获取元数据列表
            getColumnTree(widgetId); //获取元数据栏目
        }else if(moduleType == 'formelement'){
            if(widgetFormMetadataId){
                metadataTable(widgetId, moduleType, createModuleId, widgetFormMetadataId);
            }
        }else{            
            metadataTable(widgetId, moduleType); //获取元数据列表
        }
        //获取各个组件选中的字段值，用于模板保存
        var checkboxFieldsArr = []; //存放选中的字段
        
        $('#' + widgetId + 'Settings input[name="fieldList"]:checked').each(function(i) {
            var fieldId = $(this).val(); //字段id
            var fieldname = $(this).attr('data-fieldname'); //字段名
            var anothername = $(this).attr('data-anothername'); //字段名
            var fieldtype = $(this).attr('data-fieldtype'); //字段类型：普通文本1、密码文本2、多行文本3、日期4、自定义5、单选框6、多选框7、下拉选择框8、分类法13、视频14、图片15、音频16、文件17、大文本编辑器18、访问量19
            var enmvalue = $(this).attr('data-enmvalue'); //枚举值
            var eventType = $(this).parent('li').find('[name="eventType"]').val(); //字段事件
            var styleFlag = $(this).parent('li').find('[name="styleFlag"]').val(); //字段样式名
            var isPassGinseng = $(this).parent('li').find('[name="isPassGinseng"]').val(); //表格组件是否传参字段
            var fieldValue = $(this).parent('li').find('[name="fieldValue"]').val(); //表格组件字段传参值
            var showWidth = $(this).parent('li').find('[name="showWidth"]').val(); //表格组件字段列宽
            var isChoose = $(this).parent('li').find('[name="isChoose"]').val(); //表单组件必填项
            var fileExtension = $(this).parent('li').find('[name="fileExtension"]').val(); //表单组件上传文件扩展名
            var dateType = $(this).parent('li').find('[name="dateType"]').val(); //表单组件选择日期类型
            var seriesType = $(this).parent('li').find('[name="seriesType"]').val(); //图表组件选择系列类型
            var fieldColor = $(this).parent('li').find('[name="fieldColor"]').val(); //图表组件选择颜色
            var checkboxFieldObj = {
                "fieldId":fieldId,
                "fieldname":fieldname,
                "anothername":anothername,
                "fieldType":fieldtype, //字段类型
                "eventType":eventType,
                "styleFlag":styleFlag,
                "isPassGinseng":isPassGinseng, //表格组件是否传参字段
                "fieldValue":fieldValue, //表格组件字段传参值
                "showWidth":showWidth, //表格组件列宽度
                "isChoose":isChoose, //表单组件必填项
                "fileExtension":fileExtension, //表单组件上传文件扩展名
                "dateType":dateType, //表单组件选择日期类型
                "seriesType":seriesType, //图表组件选择系列类型
                "fieldColor":fieldColor //图表组件选择颜色
            };
            checkboxFieldsArr.push(checkboxFieldObj);
    
            //搜索组件 根据勾选的字段显示条件结构
            if(moduleType == 'search'){
                // searchHtml = searchSetHtml(widgetId, fieldtype, anothername, fieldname, enmvalue);
                var searchDateNum = 0; //搜索组件日期类型个数
                if(fieldtype == 1){ //普通文本
                    searchHtml += '<div class="layui-form-item">' +
                        '<label class="layui-form-label">' + anothername + '</label>' + 
                        '<div class="layui-inline">' + 
                            '<input type="text" name="' + fieldname+ '" placeholder="请输入" autocomplete="off" class="layui-input">' +
                        '</div>' +
                    '</div>';
                }else if(fieldtype == 2){ //密码文本
                    searchHtml += '<div class="layui-form-item">' +
                        '<label class="layui-form-label">' + anothername + '</label>' + 
                        '<div class="layui-inline">' + 
                            '<input type="password" name="' + fieldname+ '" placeholder="请输入" autocomplete="off" class="layui-input">' +
                        '</div>' +
                    '</div>';
                }else if(fieldtype == 4){ //日期
                    searchDateNum = searchDateNum + 1;
                    searchHtml += '<div class="layui-form-item">' +
                        '<label class="layui-form-label">' + anothername + '</label>' + 
                        '<div class="layui-inline">' + 
                            '<input type="text" name="' + fieldname + '" class="layui-input" placeholder="请选择" id="'+ widgetId +'Date'+ searchDateNum +'">' +
                        '</div>' +
                    '</div>';
                }else if(fieldtype == 6 || fieldtype == 7 || fieldtype == 8){ //单选框、多选框、下拉选择框
                    var valArr = enmvalue.split(',');
                    var html = '';
                    for(var i in valArr){                
                        html += '<option value="'+ valArr[i] +'">'+ valArr[i] +'</option>'
                    }
                    searchHtml += '<div class="layui-form-item">' +
                        '<label class="layui-form-label">' + anothername + '</label>' + 
                        '<div class="layui-inline">' + 
                            '<select name="">' +
                                '<option value="">请选择</option>' +
                                html + 
                            '</select>' +
                        '</div>' +
                    '</div>';
                }
            }
            
            //表单组件 根据选中的字段类型，生成对应的html结构
            if(moduleType == 'formelement'){
                // formHtml = formSetHtml(fieldtype, anothername, fieldname, isChoose);                
                if(fieldtype == 1){ //普通文本
                    if(isChoose == 1){
                        formHtml = '<label class="layui-form-label">' + anothername + '</label>' + 
                        '<div class="layui-input-block">' + 
                            '<input type="text" name="' + fieldname+ '" placeholder="请输入" autocomplete="off" class="layui-input" lay-verify="required">' +
                        '</div>';
                    }else{
                        formHtml = '<label class="layui-form-label">' + anothername + '</label>' + 
                        '<div class="layui-input-block">' + 
                            '<input type="text" name="' + fieldname+ '" placeholder="请输入" autocomplete="off" class="layui-input">' +
                        '</div>';
                    }
                    // var lowerStr = fieldname.toLowerCase(); //转换小写字母
                    // if(isChoose == 1 && eventType == 'Click'){ //必填项、事件
                    //     formHtml = '<label class="layui-form-label">' + anothername + '</label>' + 
                    //     '<div class="layui-input-block">' + 
                    //         '<input type="text" name="' + fieldname + '" placeholder="请输入" autocomplete="off" class="layui-input" lay-verify="required" onclick="' + lowerToUpper(lowerStr) +'Click()">' +
                    //     '</div>';
                    // }else if(isChoose == 1){
                    //     formHtml = '<label class="layui-form-label">' + anothername + '</label>' + 
                    //     '<div class="layui-input-block">' + 
                    //         '<input type="text" name="' + fieldname + '" placeholder="请输入" autocomplete="off" class="layui-input" lay-verify="required">' +
                    //     '</div>';
                    // }else if(eventType == 'Click'){
                    //     formHtml = '<label class="layui-form-label">' + anothername + '</label>' + 
                    //     '<div class="layui-input-block">' + 
                    //         '<input type="text" name="' + fieldname + '" placeholder="请输入" autocomplete="off" class="layui-input" onclick="' + lowerToUpper(lowerStr) +'Click()">' +
                    //     '</div>';
                    // }else{
                    //     formHtml = '<label class="layui-form-label">' + anothername + '</label>' + 
                    //     '<div class="layui-input-block">' + 
                    //         '<input type="text" name="' + fieldname + '" placeholder="请输入" autocomplete="off" class="layui-input">' +
                    //     '</div>';
                    // }
                }else if(fieldtype == 2){ //密码文本
                    if(isChoose == 1){
                        formHtml = '<label class="layui-form-label">' + anothername + '</label>' + 
                        '<div class="layui-input-block">' + 
                            '<input type="password" name="' + fieldname + '" placeholder="请输入" autocomplete="off" class="layui-input" lay-verify="required">' +
                        '</div>';
                    }else{
                        formHtml = '<label class="layui-form-label">' + anothername + '</label>' + 
                        '<div class="layui-input-block">' + 
                            '<input type="password" name="' + fieldname + '" placeholder="请输入" autocomplete="off" class="layui-input">' +
                        '</div>';
                    }
                }else if(fieldtype == 3){ //多行文本
                    if(isChoose == 1){
                        formHtml = '<label class="layui-form-label">' + anothername + '</label>' + 
                        '<div class="layui-input-block">' + 
                            '<textarea name="' + fieldname + '" placeholder="请输入" class="layui-textarea"  lay-verify="required"></textarea>' +
                        '</div>';
                    }else{
                        formHtml = '<label class="layui-form-label">' + anothername + '</label>' + 
                        '<div class="layui-input-block">' + 
                            '<textarea name="' + fieldname + '" placeholder="请输入" class="layui-textarea"></textarea>' +
                        '</div>';
                    }
                }else if(fieldtype == 4){ //日期
                    if(isChoose == 1){
                        formHtml = '<label class="layui-form-label">' + anothername + '</label>' + 
                        '<div class="layui-input-block">' + 
                            '<input type="text" name="' + fieldname + '" class="layui-input" placeholder="请选择" lay-verify="required" id="' + widgetId + 'Date">' +
                        '</div>';
                    }else{
                        formHtml = '<label class="layui-form-label">' + anothername + '</label>' + 
                        '<div class="layui-input-block">' + 
                            '<input type="text" name="' + fieldname + '" class="layui-input" placeholder="请选择" id="' + widgetId + 'Date">' +
                        '</div>';
                    }
                }else if(fieldtype == 6){ //单选框
                    var valArr = enmvalue.split(',');
                    var html = '';
                    for(var i in valArr){
                        if(i == 0){                         
                            html += '<input type="radio" name="' + fieldname + '" title="'+ valArr[i] +'" checked>'
                        }else{
                            html += '<input type="radio" name="' + fieldname + '" title="'+ valArr[i] +'">'
                        }
                    }
                    formHtml = '<label class="layui-form-label">' + anothername + '</label>' + 
                        '<div class="layui-input-block">' + 
                            html + 
                        '</div>';
                }else if(fieldtype == 7){ //多选框
                    var valArr = enmvalue.split(',');
                    var html = '';
                    for(var i in valArr){
                        if(i == 0){                            
                            html += '<input type="checkbox" name="' + fieldname + '" title="'+ valArr[i] +'" checked>'
                        }else{
                            html += '<input type="checkbox" name="' + fieldname + '" title="'+ valArr[i] +'">'
                        }
                    }
                    formHtml = '<label class="layui-form-label">' + anothername + '</label>' + 
                        '<div class="layui-input-block">' + 
                            html + 
                        '</div>';
                }else if(fieldtype == 8){ //下拉选择框
                    var valArr = enmvalue.split(',');
                    var html = '';
                    for(var i in valArr){                
                        html += '<option value="'+ valArr[i] +'">'+ valArr[i] +'</option>'
                    }
                    formHtml = '<label class="layui-form-label">' + anothername + '</label>' + 
                    '<div class="layui-input-block">' + 
                        '<select name="">' +
                            '<option value="">请选择</option>' +
                            html + 
                        '</select>' +
                    '</div>';
                }else if(fieldtype == 17){ //文件
                    if(isChoose == 1){
                        formHtml = '<label class="layui-form-label">' + anothername + '</label>' + 
                        '<div class="layui-input-block">' + 
                            '<div class="layui-input-inline">' +
                                '<input type="text" name="" value="" class="layui-input" id="'+ widgetId + 'FilePath"  lay-verify="required">' +
                            '</div>' +
                            '<button type="button" class="layui-btn" id="'+ widgetId +'Upload">上传</button>' + 
                            '<span id="'+ widgetId + 'UploadName"></span>' +
                        '</div>';
                    }else{
                        formHtml = '<label class="layui-form-label">' + anothername + '</label>' + 
                        '<div class="layui-input-block">' + 
                            '<div class="layui-input-inline">' +
                                '<input type="text" name="" value="" class="layui-input" id="'+ widgetId + 'FilePath">' +
                            '</div>' +
                            '<button type="button" class="layui-btn" id="'+ widgetId +'Upload">上传</button>' + 
                            '<span id="'+ widgetId + 'UploadName"></span>' +
                        '</div>';
                    }
                }
            }
        });
    }else if(moduleType == 'menu'){
        getMenuTree(widgetId); //获取中台菜单一级栏目
        menuChannelShow(widgetId);
    }else if(moduleType == 'login'){       
        fileUpload('#' + widgetId, 'member', '/user/importHead'); //上传图片
        loginSkip(widgetId); //站内/站外链接单选按钮切换
        loginPosition(widgetId); //登录位置单选按钮切换
    }else if(moduleType == 'custombutton'){
        customButtonAdd(widgetId); //自定义按钮组件 新建按钮
        customButtonDel(widgetId); //自定义按钮组件 删除按钮
        var customButtonArr = []; //存放新建按钮
        $('#' + widgetId + 'Settings .visual_button_event .layui-form-item').each(function(i) {
            var buttonName = $(this).find('input[name="buttonName"]').val();
            var anotherName = $(this).find('input[name="anotherName"]').val();
            var eventType = $(this).find('select[name="eventType"]').val();
            if(buttonName){
                var customButtonObj = {
                    "buttonName":buttonName,
                    "anotherName":anotherName,
                    "eventType":eventType
                };
                customButtonArr.push(customButtonObj);
            }
        });
    }
    
    //获取表单设置的样式数据
    form.on('submit(' + widgetId + 'FormSubmit)', function(data){
        var setStyleData = data.field;
        params = Object.assign(params, setStyleData); //拼合设置的样式对象和元数据字段对象
        // console.log('setStyleData',params);
        $(widgetId).addClass(widgetId + '_style'); //添加样式class
        if(moduleType == 'textlist'){
            getColumnId(params); //获取元数据栏目id
        }
        return false;
    });

    //获取表单设置的元数据数据
    form.on('submit(' + widgetId + 'FormTotalSubmit)', function(data){
        // console.log('formField', data.field);
        var setMetadataData = data.field;
        params = Object.assign(params, setMetadataData); //拼合设置的样式对象和元数据字段对象
        params.visualTemplateId = productDocId; //组件设置关联到模板id   
        params.styleName = widgetId + '_style'; //设置文字样式属性styleName对应的值
        // console.log('params',params);
        //根据组件类型，传相应组件参数
        if(moduleType == 'table' || moduleType == 'search' || moduleType == 'detail' || moduleType == 'textlist' || moduleType == 'carousel' || moduleType == 'formelement' || moduleType == 'form' ||moduleType == 'chart'){
            params.fieldList = checkboxFieldsArr;
            params.dataAreaId = widgetId; //组件html结构渲染标签
            if(moduleType == 'table'){ //表格组件根据id生成表格和分页的html结构id
                $('#' + widgetId + ' .visual_table table').attr('id', widgetId + 'View'); 
                $('#' + widgetId + ' .visual_page').attr('id', widgetId + 'Page'); 
            }else if(moduleType == 'chart'){ //图表组件的html结构id
                $('#' + widgetId + ' .visual_chart').attr('id', widgetId + 'ChartView'); 
                $('#' + widgetId + ' .visual_chart_table').attr('id', widgetId + 'TableView'); 
            }
            if(moduleType == 'search'){
                //搜索组件 显示勾选的字段条件替换    
                searchHtml += '<div class="layui-form-item">' +
                    '<button class="layui-btn" lay-submit lay-filter="LAY-back-search">' +
                        '<i class="layui-icon layui-icon-search"></i>' +
                    '</button>' +
                '</div>';
                $('#' + widgetId + ' .visual_search .layui-form').html(searchHtml);
            }
            if(moduleType == 'form'){
                $('#' + widgetId).attr('data-metadata-id',data.field.dbTableId); //存放表单组件的元数据表id
                // console.log('widgetMetadataId',data.field.dbTableId);
            }
            if(moduleType == 'formelement'){
                //表单组件 显示勾选的字段结构替换  
                $('#' + widgetId + ' .layui-form-item').html(formHtml);
            }
        }else if(moduleType == 'menu'){
            /*头部菜单栏目 开始*/
            console.log('chooseMenu',data.field.chooseMenu)
            if(data.field.chooseMenu == 'menu'){
                var checkedMenuNodes = zTreeMenuObj.getCheckedNodes(true); //选中的中台菜单
                if(checkedMenuNodes.length != 0){
                    params.menuId = checkedMenuNodes[0].id; //中台菜单的栏目id
                }
            }else{
                var checkedNodes = zTreeObj.getCheckedNodes(true); //选中的元数据栏目
                // console.log('nodesChecked',checkedNodes);
                if(checkedNodes.length != 0){
                    params.siteId = checkedNodes[0].id; //元数据栏目的站点id
                }
            }
            params.dataAreaId = widgetCustomId + 'View'; //头部菜单栏目 html结构id
            params.dataTplId = widgetCustomId + 'Tpl'; //头部菜单栏目 模板渲染id
            /*头部菜单栏目 结束*/
        }else if(moduleType == 'login'){
            var loginTypeArr = [];
            $('#' + widgetId + 'Settings input[name="loginType"]:checked').each(function(){
                loginTypeArr.push($(this).val());
            });
            params.loginType = loginTypeArr.join(','); //登录类型
            params.dataAreaId = widgetId; //组件html结构渲染标签
            getColumnId(params); //获取元数据栏目id
            $('#' + widgetId + ' .visual_login').css('width',params.loginWidth); //登录框宽度
        }else if(moduleType == 'custombutton'){
            params.dataAreaId = widgetId; //组件html结构渲染标签
            params.buttonList = customButtonArr; //按钮列表
        }
        if(createModuleId){
            editModuleSet(params, widgetId, createModuleId, moduleType); //编辑组件设置
        }else{
            addModuleSet(params, widgetId, moduleType); //新建组件设置
        }
        return false;
    });
    
    form.render();
    if(createModuleId){
        getModuleSetEdit(widgetId, createModuleId, moduleType); //查看组件设置返显
    }
}

/*
* 编辑组件设置
* @param widgetId 组件元素
* @param createModuleId 组件id
* @param moduleType 组件类型
*/
function getModuleSetEdit(widgetId, createModuleId, moduleType){
    var moduleUrl = 'module' + moduleType; //获取组件接口地址
    ajax(service_prefix.visual, moduleUrl+ "/" + createModuleId, "get").then(function (data) {
        if (data.success) {
            form.val(widgetId + 'StyleFrom', data.obj); //设置样式表单赋值
            form.val(widgetId + 'MetadataFrom', data.obj); //设置元数据表单赋值
            getColorPicker('#' + widgetId, data.obj.fontColor); //设置文字颜色
            getColorPicker('#' + widgetId + 'Background', data.obj.backgroundColor); //设置背景颜色
            //根据组件类型，执行相应组件方法
            if(moduleType == 'table' || moduleType == 'search' || moduleType == 'detail' || moduleType == 'textlist' || moduleType == 'carousel' || moduleType == 'formelement' || moduleType == 'form' || moduleType == 'chart'){
                metadataTable(widgetId, moduleType, createModuleId, data.obj.dbTableId, data.obj.fieldList); //设置选中的元数据表和字段返显
                if(moduleType == 'textlist'){
                    getColumnTree(widgetId, data.obj.channelId); //选中的栏目返显
                }
            }else if(moduleType == 'menu'){
                if(data.obj.chooseMenu == 'menu'){
                    getMenuTree(widgetId, data.obj.menuId); //选中的中台菜单一级栏目返显
                    $('#' + widgetId + 'TreeMenu').show();
                    $('#' + widgetId + 'Tree').hide();
                }else{
                    getSiteTree(widgetId, data.obj.siteId); //选中的元数据栏目站点返显
                    $('#' + widgetId + 'TreeMenu').hide();
                    $('#' + widgetId + 'Tree').show();
                }
                menuChannelShow(widgetId, data.obj.siteId, data.obj.menuId);
            }else if(moduleType == 'login'){
                getColumnTree(widgetId, data.obj.channelId); //选中的栏目返显
                loginSkip(widgetId, data.obj.channelId); //站内/站外链接单选按钮切换
                loginPosition(widgetId, data.obj.loginPosition); //登录位置单选按钮切换
            }else if(moduleType == 'custombutton'){
                var myButtons = data.obj.buttonList; //按钮列表
                if(myButtons.length > 0){
                    var html = '';
                    $('#' + widgetId + 'Settings .visual_button_event').html(html);
                    for(var i in myButtons){
                        if( Number(i) + 1 == myButtons.length){
                            // form.val(widgetId + 'MetadataFrom', myButtons[0]); //设置元数据表单赋值
                            html = '<div class="layui-form-item">' +
                                '<label class="layui-form-label">按钮名称</label>' +
                                '<div class="layui-input-block">' +
                                    '<div class="visual_check_button">' +
                                        '<input type="text" name="buttonName" placeholder="请输入中文名称" class="layui-input" value="' + myButtons[i].buttonName + '" lay-verify="required">' +
                                        '<input type="text" name="anotherName" placeholder="请输入英文名称" class="layui-input" value="' + myButtons[i].anotherName + '" lay-verify="required">' +
                                        '<div class="visual_select">' +
                                            '<select name="eventType" lay-verify="required">' +
                                                '<option value="">请选择事件</option>' +
                                                '<option value="Click">单击事件</option>' +
                                            '</select>' +
                                        '</div>' +
                                        '<div class="layui-btn layui-btn-primary layui-btn-sm" id="' + widgetId + 'AddBtn">' +
                                            '<i class="layui-icon">&#xe654;</i>' +
                                        '</div>' +
                                    '</div>' +
                                '</div>' +
                            '</div>';
                        }else{
                            html = '<div class="layui-form-item">' +
                                '<label class="layui-form-label">按钮名称</label>' +
                                '<div class="layui-input-block">' +
                                    '<div class="visual_check_button">' +
                                        '<input type="text" name="buttonName" placeholder="请输入中文名称" class="layui-input" value="' + myButtons[i].buttonName + '">' +
                                        '<input type="text" name="anotherName" placeholder="请输入英文名称" class="layui-input" value="' + myButtons[i].anotherName + '">' +
                                        '<div class="visual_select">' +
                                            '<select name="eventType">' +
                                                '<option value="">请选择事件</option>' +
                                                '<option value="Click">单击事件</option>' +
                                            '</select>' +
                                        '</div>' +
                                        '<div class="layui-btn layui-btn-primary layui-btn-sm ' + widgetId +'DelBtn">' +
                                            '<i class="layui-icon">&#xe640;</i>' +
                                        '</div>' +
                                    '</div>' +
                                '</div>' +
                            '</div>';
                        }                    
                        $('#' + widgetId + 'Settings .visual_button_event').append(html);
                        $('#' + widgetId + 'Settings .visual_button_event select[name="eventType"]').eq(i).val(myButtons[i].eventType); //设置事件选中项
                    } 
                    customButtonAdd(widgetId); //自定义按钮组件 新建按钮
                    customButtonDel(widgetId); //自定义按钮组件 删除按钮
                    layui.form.render();
                }
            }
        } else {
            console.log(data.msg);
        }
    });
}

/*
* 字段排序 上移
*/
function fieldsUp(){
    $('.visual_up').click(function(){
        var parentDom = $(this).parent();
        var prevDom = parentDom.prev();
        if(prevDom.length){
            parentDom.insertBefore(prevDom);
        }else{
            layer.msg('到顶了');
        }
        parentDom.addClass('cur').siblings('li').removeClass('cur');
    });
}

/*
* 字段排序 下移
*/
function fieldsDown(){
    $('.visual_down').click(function(){
        var parentDom = $(this).parent();
        var nextDom = parentDom.next();
        if(nextDom.length){
            parentDom.insertAfter(nextDom);
        }else{
            layer.msg('到底了');
        }
        parentDom.addClass('cur').siblings('li').removeClass('cur');
    });
}

/*
* 设置组件样式
* @param widgetId 组件元素
*/
function setModuleStyle(widgetId){
    if(! $('#'+ widgetId).hasClass(widgetId + '_style')){
        $('#'+ widgetId).addClass(widgetId + '_style'); //设置文字样式新增class
    }
}