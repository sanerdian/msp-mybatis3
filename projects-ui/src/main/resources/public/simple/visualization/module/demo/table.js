var widgetId = 'widgetTable'; //组件div的id
var layerSetId = widgetId + 'Settings'; //组件设置弹出框div的id

/*
 * 取消边框
 */
function removeAllWidgetBorder() {
    $('#'+ widgetId).find(".widget_border_content").addClass("widget_inactive").removeClass("widget_active");
}

/*
 * 添加边框
 */
function activeWidgetBorder() {
    $('#'+ widgetId).find(".widget_border_content:visible:first").addClass("widget_active").removeClass("widget_inactive");
}

/*
 * 点击显示组件菜单
 */
$('#'+ widgetId).click( function (e) {
    // console.log('e',e);
    activeWidgetBorder();
    $('#'+ widgetId + ' .widget_menu').show();
    var id = $(this).find('.widget_id').val();
    // console.log('id',id)
    clickSet(layerSetId);
    return false;
})

/*
 * 点击删除按钮，删除组件
 */
function deleteModule(){
    $('#'+ widgetId + ' .widget_delete').click(function () {
        $(this).parent().parent().remove();
    })
}
deleteModule();

/*
 * 隐藏组件菜单
 */
function widgetMenuHide(){
    $('#'+ widgetId + ' .widget_menu').hide();
}

/*
 * 点击设置按钮弹出框
 * @param id 组件设置弹出框div的id
 */
function clickSet(id){
    $('#'+ widgetId + ' .widget_set').click(function () {
        layer.open({
            type: 1,
            title: false,
            id: id,
            skin: 'layui-layer-molv',
            scrollbar:false,
            area: ['530px','350px'],
            btnAlign: 'c',
            offset: 't',
            btn: ['确定', '取消'],
            content: '<div class="layer_box" id="' + widgetId + 'SetView"></div>',
            success: function(layero, index){
                var data = [];
                var view = '#' + widgetId + 'SetView';
                var tpl = '#' + widgetId + 'SetTpl';
                getData(data, tpl, view); //弹出框内容渲染         
                metadataTable(); //获取元数据列表              
                widgetMenuHide(); //隐藏组件菜单
                colorPicker('#' + widgetId + 'ColorInput', '#' + widgetId + 'ColorPicker'); //颜色选择器
            },
			yes: function(index, layero){
                getModuleSetForm(); //设置文字样式和关联字段信息提交
                widgetMenuHide(); //隐藏组件菜单
                removeAllWidgetBorder(); //取消边框
                layer.close(index);
                layero.find('form').find('button[lay-submit]').click();
            },
            btn2:function(index, layero){
                widgetMenuHide(); //隐藏组件菜单
                removeAllWidgetBorder(); //取消边框
                layer.close(index);
            },
            cancel: function(index, layero){ 
                widgetMenuHide(); //隐藏组件菜单
                removeAllWidgetBorder(); //取消边框
                layer.close(index);
                return false; 
            }
        });
    })
}

/*
 * 获取元数据列表
 */
function metadataTable(){        
    ajax(service_prefix.metadata,"/all","post",{}).then(function (data) {
        if (data.success) {
            var html = '';
            for (var i in data.obj) {
                html += '<option value="' + data.obj[i].id + '">' + data.obj[i].anothername + ' ['+ data.obj[i].tablename + ']' + '</option>';
            }
            $('#' + widgetId + 'Metadata').html(html);
            form.render();
            metadataTableFields(data.obj[0].id); //获取元数据表中的字段
            //改变元数据表下拉时，显示相应元数据表的字段
            form.on('select(widgetTableMetadata)', function(data){
                metadataTableFields(data.value);
            });
        } else {
            console.log(data.msg);
        }
    });
}     

/*
 * 获取元数据表中的字段
 * @param id 组件id
 */
function metadataTableFields(id){
    ajax(service_prefix.metadata, "/fieldinfo/listall?tableid=" + id, "get").then(function (data) {
        if (data.success) {
            var html = '';
            for (var i in data.obj) {
                var n = parseInt(i) + 1 ;
                html += '<li><span class="visual_num">' + n + '</span><input type="checkbox" name="fieldList" lay-skin="primary" lay-filter="checkItemFields" data-fieldname="' + data.obj[i].fieldname + '" data-anothername="' + data.obj[i].anothername + '" value="' + data.obj[i].id + '" title="' + data.obj[i].anothername + ' ('+ data.obj[i].fieldname  +')</span>' + '"></li>';
            }
            $('#' + widgetId + 'Fields').html(html);
            form.render();
            var moduleId = $('#'+ widgetId).find('.widget_id').val(); //生成的组件id
            console.log('moduleId',moduleId)
            if(moduleId){
                getModuleSet(moduleId); //查看组件设置
            }
        }else{
            console.log(data.msg);            
        }
    })
}

/*
 * 设置文字样式和关联字段信息提交
 */
function getModuleSetForm(){    
    var params = {};//存放设置参数                
    var moduleId = $('#'+ widgetId).find('.widget_id').val(); //生成的组件id
    //获取表单设置的样式数据
    form.on('submit(setStyleSubmit)', function(data){
        var setStyleData = data.field;
        params = Object.assign(params, setStyleData); //拼合设置的样式对象和元数据字段对象
        console.log('setStyleData',params);
        return false;
    });
    //获取选中的字段值
    var checkboxFieldsArr = [];
    $('input[name="fieldList"]:checked').each(function() {
        var fieldId = $(this).val();
        // var fieldname = $(this).attr('data-fieldname');
        // var fieldname = $(this).attr('data-fieldname');
        // var anothername = $(this).attr('data-anothername');
        var checkboxFieldObj = {
            "fieldId":fieldId,
            // "fieldname":fieldname,
            // "anothername":anothername,
            // "moduleType":'table',
            // "moduleId":moduleId
        };
        checkboxFieldsArr.push(checkboxFieldObj);
    });
    // console.log(checkboxFieldsArr);
    //获取表单设置的元数据数据
    form.on('submit(setMetadataSubmit)', function(data){
        var setMetadataData = data.field;
        params = Object.assign(params, setMetadataData); //拼合设置的样式对象和元数据字段对象
        params.fieldList = checkboxFieldsArr;
        console.log('setMetadataData',params);
        if(moduleId){
            editModuleSet(params, moduleId); //编辑组件设置
        }else{
            addModuleSet(params); //新建组件设置
        }
        return false;
    });
}

/*
 * 新建组件设置
 * @param params 新建组件参数
 */
function addModuleSet(params){
    console.log('params',params)
    ajax(service_prefix.visual,"/moduletable", "post", JSON.stringify(params)).then(function (data) {
        if (data.success) {
            $('#'+ widgetId).find('.widget_id').val(data.obj.id); //存放组件id
            layer.msg('设置成功');
            //存放组件信息，用于模板保存组件
            var subGroupObj = {
                "moduleId":data.obj.id,
                "moduleType":'table'
            };
            selectModuleListArr.push(subGroupObj);
            console.log('selectModuleListArr',selectModuleListArr);
        } else {
            console.log(data.msg);
        }
    });
}

/*
 * 编辑组件设置
 * @param params 编辑组件参数
 * @param id 组件id
 */
function editModuleSet(params, id){
    ajax(service_prefix.visual,"/moduletable/" + id, "put", JSON.stringify(params)).then(function (data) {
        if (data.success) {
            layer.msg('设置成功');
        } else {
            console.log(data.msg);
        }
    });
}

/*
* 查看组件设置
* @param id 组件id
*/
function getModuleSet(id){
    ajax(service_prefix.visual,"/moduletable/" + id, "get").then(function (data) {
        if (data.success) {
            $('#'+ widgetId).find('.widget_id').val(data.obj.id); //存放组件id
            // form.val("setStyleFrom", data.obj); //设置样式表单赋值
            form.val("setMetadataFrom", data.obj); //设置元数据表单赋值
            // console.log('fieldList',data.obj.fieldList);
            //返显选中的字段
            var checkboxFields = $('input[type="checkbox"]');
            var myCheckedFielsArr = data.obj.fieldList;
            for(var i in myCheckedFielsArr){
                for (var k in checkboxFields) { //遍历checkbox所有项
                    if (checkboxFields[k].value == myCheckedFielsArr[i].fieldId) {                  
                        checkboxFields[k].checked = true; //设置选中项
                    }                
                }
            }
            form.render(); //设置完选中后记得渲染
        } else {
            console.log(data.msg);
        }
    });
}