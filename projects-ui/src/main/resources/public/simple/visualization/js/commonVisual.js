var laytpl = layui.laytpl;
var layer = layui.layer;
var form = layui.form;
var laydate = layui.laydate;
var element = layui.element;
var laypage = layui.laypage;
var table = layui.table;
var upload = layui.upload;
var transfer = layui.transfer;
var util = layui.util;
var tree = layui.tree;
var slider = layui.slider;
var rate = layui.rate;
var carousel = layui.carousel;
var flow = layui.flow;
var colorpicker = layui.colorpicker;
var $ = layui.jquery;

/**
 * 获取服务地址
 */
function getPerfix(){
    return new Promise(function (resovle, reject) {
        ajax('', 'member/menu/getPerfix','post',{}).then(function (data){
            if(data.success){
                for(var key in data.obj){
                    service_prefix[key] = data.obj[key];
                }
                resovle(11111);
            }else{
                layer.alert(data.msg);
            }
        });
    })
}

/**
 * 获取最终提交地址
 * @param {*} serviceId 微服务id(对应地址前缀)
 * @param {*} url 请求地址
 */
function getAjaxUrl(serviceId,url) {
    var up = com.jnetdata.url_prefix;
    if((!up || up.charAt(up.length-1) != '/') && serviceId) up += '/';
    up += serviceId;
    if((!up || up.charAt(up.length-1) != '/') && url.charAt(0) != '/') up += '/';
    up += url;
    if(up.indexOf('http') != 0 && up.charAt(0) != '/') up = '/' + up;
    return up;
}

/**
 * ajax请求
 * 实际请求地址 = 全局地址前缀(url_prefix) + 微服务前缀(serviceId) + url
 * @param {*} serviceId 微服务id, 示例 service_prefix.member
 * @param {*} url url地址
 * @param {*} type 请求类型 'get', 'post', 'put', 'delete'等
 * @param {*} data 请求数据
 */
function ajax(serviceId,url,type,data) {
    var index = layer.load();
    return new Promise(function (resovle, reject) {
        $.ajax({
            type: type,
            // "async": para.async,
            url: getAjaxUrl(serviceId,url),
            contentType: 'application/json',
            data: data,
            dataType: 'json',
            crossDomain: true,
            xhrFields: {
                withCredentials: true
            },
            headers:{'mspToken':com.jnetdata.mspToken},
            success: function(res) {
                if(!res.success && res.msg == '登陆超时,请重新登陆'){
                    layer.alert(res.msg,function(){
                        window.location.href = 'login.html';
                    })
                    return false;
                }
                layer.close(index);
                resovle(res);
            },
            error: function(err) {
                layer.close(index);
                // layer.alert(err.responseText);
                reject(err);
            }
        })
    }).catch(function(e) {
        layer.close(index);
        console.log(e);
    });
}

/**
 * 数据模板
 * @param {*} data
 * @param {*} domTpl
 * @param {*} dom
 */
function getData(data, domTpl, dom) {
    layui.use('laytpl', function() {
        var laytpl = layui.laytpl;
        laytpl($(domTpl).html()).render(data, function (html) {
            $(dom).html(html);
        });
    })
}

/**
 * 分页
 * @param {*} obj
 * @param {*} total
 * @param {*} limit
 * @param {*} current
 * @param {*} callBack
 */
function page(total, current, limit, callBack, elem, entity) {
    if(!elem) elem = "page";
    layui.laypage.render({
        elem: elem,
        count: total,
        prev: '<i class="layui-icon">&#xe603;</i>',
        next: '<i class="layui-icon">&#xe602;</i>',
        limit:limit,
        curr:current,
        layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip'],
        jump: function (obj, first) {
            if (!first) {
                callBack(serviceId, url, obj.curr, obj.limit, entity);
            }
        }
    });
}

/**
 * 表格数据
 * @param {*} obj
 * @param {*} data
 * @param {*} columns
 */
function setTableData(obj, data, columns) {
    table.render({
        elem: obj
        , data: data.records
        , limit: data.size
        , cols: [columns]
        // ,page: true
    });
}

/**
 * 设置日期
 * @param {*} id
 * @param {*} type
 */
function setDate(id, type){
	laydate.render({
		elem: id
		,type: type
	});
}

/**
 * 自带渲染
 */
function setRender() {
    layui.form.render();
}

/**
 * 分页(文字列表)
 * @param {*} obj
 * @param {*} total
 * @param {*} limit
 * @param {*} current
 * @param {*} callBack
 */
function pageForTextlist(total, current, limit, callBack, elem, entity, widgetId, serviceId,url){
    if(!elem) elem = "page";
    layui.laypage.render({
        elem: elem,
        count: total,
        prev: '<i class="layui-icon">&#xe603;</i>',
        next: '<i class="layui-icon">&#xe602;</i>',
        limit:limit,
        curr:current,
        layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip'],
        jump: function (obj, first) {
            if (!first) {
                callBack(serviceId, url, obj.curr, obj.limit, entity, widgetId);
            }
        }
    });
}


/**
 * 颜色选择器
 * @param {*} elemInput 输入框元素
 * @param {*} elem 选择器元素
 */
function colorPicker(elemInput, elem, colorInput){
    colorpicker.render({
        elem: elem
        ,color: colorInput
        ,done: function(color){
            $(elemInput).val(color);
        }
    });
}

/*
 * 上传文件
 * @param {*} elem 上传按钮元素
 * @param {*} serviceId 接口模块id
 * @param {*} url 上传接口地址
 * @param {*} type 上传文件类型
 * @param {*} format 上传文件格式
*/
function fileUpload(elem, serviceId, url, type, format){
	upload.render({
		elem: elem + 'Upload'
		,url: getAjaxUrl(serviceId, url)
        ,method: 'post'
		,accept: type
		,exts: format
		,size: '512000'
		,choose:function (obj) {
			obj.preview(function(index, file, result){
				$(elem + 'UploadName').html(file.name);
                layer.load();
			});
		}
		,done: function (res) {
            layer.msg('上传成功！');
            $(elem + 'FilePath').val(res.obj.url);
			layer.closeAll('loading');
		}
	});
}



/*
* 获取文字颜色
* @param widgetId 组件元素
* @param color 颜色
*/
function getColorPicker(widgetId, color){
    if(color){
        colorPicker(widgetId + 'ColorInput', widgetId + 'ColorPicker', color); //设置选中的颜色返显
    }else{
        colorPicker(widgetId + 'ColorInput', widgetId + 'ColorPicker', '#333'); //设置默认颜色
    }
}

// /*
// * 搜索组件设置生成字段的html
// * @param fieldType 字段类型
// * @param anothername 字段中文名
// * @param fieldname 字段英文名
// // * @param eventType 选择事件
// // * @param styleFlag 是否有样式名
// * @param isChoose 是否必填项
// */

// function searchSetHtml(widgetId, fieldtype, anothername, fieldname, enmvalue){
//     var searchHtml = '';
//     var searchDateNum = 0; //搜索组件日期类型个数
//     if(fieldtype == 1){ //普通文本
//         searchHtml += '<div class="layui-form-item">' +
//             '<label class="layui-form-label">' + anothername + '</label>' +
//             '<div class="layui-inline">' +
//                 '<input type="text" name="' + fieldname+ '" placeholder="请输入" autocomplete="off" class="layui-input">' +
//             '</div>' +
//         '</div>';
//     }else if(fieldtype == 2){ //密码文本
//         searchHtml += '<div class="layui-form-item">' +
//             '<label class="layui-form-label">' + anothername + '</label>' +
//             '<div class="layui-inline">' +
//                 '<input type="password" name="' + fieldname+ '" placeholder="请输入" autocomplete="off" class="layui-input">' +
//             '</div>' +
//         '</div>';
//     }else if(fieldtype == 4){ //日期
//         searchDateNum = searchDateNum + 1;
//         searchHtml += '<div class="layui-form-item">' +
//             '<label class="layui-form-label">' + anothername + '</label>' +
//             '<div class="layui-inline">' +
//                 '<input type="text" name="' + fieldname + '" class="layui-input" placeholder="请选择" id="'+ widgetId +'Date'+ searchDateNum +'">' +
//             '</div>' +
//         '</div>';
//     }else if(fieldtype == 6 || fieldtype == 7 || fieldtype == 8){ //单选框、多选框、下拉选择框
//         var valArr = enmvalue.split(',');
//         var html = '';
//         for(var i in valArr){
//             html += '<option value="'+ valArr[i] +'">'+ valArr[i] +'</option>'
//         }
//         searchHtml += '<div class="layui-form-item">' +
//             '<label class="layui-form-label">' + anothername + '</label>' +
//             '<div class="layui-inline">' +
//                 '<select name="">' +
//                     '<option value="">请选择</option>' +
//                     html +
//                 '</select>' +
//             '</div>' +
//         '</div>';
//     }
//     searchHtml += '<div class="layui-form-item">' +
//         '<button class="layui-btn" lay-submit lay-filter="LAY-back-search">' +
//             '<i class="layui-icon layui-icon-search"></i>' +
//         '</button>' +
//     '</div>';
//     return searchHtml;
// }

// /*
// * 表单元素组件设置生成字段的html
// * @param fieldType 字段类型
// * @param anothername 字段中文名
// * @param fieldname 字段英文名
// // * @param eventType 选择事件
// // * @param styleFlag 是否有样式名
// * @param isChoose 是否必填项
// */
// function formSetHtml(fieldtype, anothername, fieldname, isChoose){
//     if(fieldtype == 1){ //普通文本
//         if(isChoose == 1){
//             formHtml = '<label class="layui-form-label">' + anothername + '</label>' +
//             '<div class="layui-input-block">' +
//                 '<input type="text" name="' + fieldname+ '" placeholder="请输入" autocomplete="off" class="layui-input" lay-verify="required">' +
//             '</div>';
//         }else{
//             formHtml = '<label class="layui-form-label">' + anothername + '</label>' +
//             '<div class="layui-input-block">' +
//                 '<input type="text" name="' + fieldname+ '" placeholder="请输入" autocomplete="off" class="layui-input">' +
//             '</div>';
//         }
//     }else if(fieldtype == 2){ //密码文本
//         if(isChoose == 1){
//             formHtml = '<label class="layui-form-label">' + anothername + '</label>' +
//             '<div class="layui-input-block">' +
//                 '<input type="password" name="' + fieldname+ '" placeholder="请输入" autocomplete="off" class="layui-input" lay-verify="required">' +
//             '</div>';
//         }else{
//             formHtml = '<label class="layui-form-label">' + anothername + '</label>' +
//             '<div class="layui-input-block">' +
//                 '<input type="password" name="' + fieldname+ '" placeholder="请输入" autocomplete="off" class="layui-input">' +
//             '</div>';
//         }
//     }else if(fieldtype == 3){ //多行文本
//         if(isChoose == 1){
//             formHtml = '<label class="layui-form-label">' + anothername + '</label>' +
//             '<div class="layui-input-block">' +
//                 '<textarea name="' + fieldname+ '" placeholder="请输入" class="layui-textarea"  lay-verify="required"></textarea>' +
//             '</div>';
//         }else{
//             formHtml = '<label class="layui-form-label">' + anothername + '</label>' +
//             '<div class="layui-input-block">' +
//                 '<textarea name="' + fieldname+ '" placeholder="请输入" class="layui-textarea"></textarea>' +
//             '</div>';
//         }
//     }else if(fieldtype == 4){ //日期
//         if(isChoose == 1){
//             formHtml = '<label class="layui-form-label">' + anothername + '</label>' +
//             '<div class="layui-input-block">' +
//                 '<input type="text" name="' + fieldname + '" class="layui-input" placeholder="请选择" lay-verify="required" id="' + widgetId + 'Date">' +
//             '</div>';
//         }else{
//             formHtml = '<label class="layui-form-label">' + anothername + '</label>' +
//             '<div class="layui-input-block">' +
//                 '<input type="text" name="' + fieldname + '" class="layui-input" placeholder="请选择" id="' + widgetId + 'Date">' +
//             '</div>';
//         }
//     }else if(fieldtype == 6){ //单选框
//         var valArr = enmvalue.split(',');
//         var html = '';
//         for(var i in valArr){
//             if(i == 0){
//                 html += '<input type="radio" name="' + fieldname + '" title="'+ valArr[i] +'" checked>'
//             }else{
//                 html += '<input type="radio" name="' + fieldname + '" title="'+ valArr[i] +'">'
//             }
//         }
//         formHtml = '<label class="layui-form-label">' + anothername + '</label>' +
//             '<div class="layui-input-block">' +
//                 html +
//             '</div>';
//     }else if(fieldtype == 7){ //多选框
//         var valArr = enmvalue.split(',');
//         var html = '';
//         for(var i in valArr){
//             if(i == 0){
//                 html += '<input type="checkbox" name="' + fieldname + '" title="'+ valArr[i] +'" checked>'
//             }else{
//                 html += '<input type="checkbox" name="' + fieldname + '" title="'+ valArr[i] +'">'
//             }
//         }
//         formHtml = '<label class="layui-form-label">' + anothername + '</label>' +
//             '<div class="layui-input-block">' +
//                 html +
//             '</div>';
//     }else if(fieldtype == 8){ //下拉选择框
//         var valArr = enmvalue.split(',');
//         var html = '';
//         for(var i in valArr){
//             html += '<option value="'+ valArr[i] +'">'+ valArr[i] +'</option>'
//         }
//         formHtml = '<label class="layui-form-label">' + anothername + '</label>' +
//         '<div class="layui-input-block">' +
//             '<select name="">' +
//                 '<option value="">请选择</option>' +
//                 html +
//             '</select>' +
//         '</div>';
//     }else if(fieldtype == 17){ //文件
//         if(isChoose == 1){
//             formHtml = '<label class="layui-form-label">' + anothername + '</label>' +
//             '<div class="layui-input-block">' +
//                 '<div class="layui-input-inline">' +
//                     '<input type="text" name="" value="" class="layui-input" id="'+ widgetId + 'FilePath"  lay-verify="required">' +
//                 '</div>' +
//                 '<button type="button" class="layui-btn" id="'+ widgetId +'Upload">上传</button>' +
//                 '<span id="'+ widgetId + 'UploadName"></span>' +
//             '</div>';
//         }else{
//             formHtml = '<label class="layui-form-label">' + anothername + '</label>' +
//             '<div class="layui-input-block">' +
//                 '<div class="layui-input-inline">' +
//                     '<input type="text" name="" value="" class="layui-input" id="'+ widgetId + 'FilePath">' +
//                 '</div>' +
//                 '<button type="button" class="layui-btn" id="'+ widgetId +'Upload">上传</button>' +
//                 '<span id="'+ widgetId + 'UploadName"></span>' +
//             '</div>';
//         }
//     }
//     return formHtml;
// }