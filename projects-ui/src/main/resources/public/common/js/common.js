// String.prototype.format = function () {
//     var values = arguments;
//     return this.replace(/\{(\d+)\}/g, function (match, index) {
//         if (values.length > index) {
//             return values[index];
//         } else {
//             return "";
//         }
//     });
// };
String.prototype.format = function(args) {
    var result = this;
    if (arguments.length > 0) {
        if (arguments.length == 1 && typeof (args) == "object") {
            for (var key in args) {
                if(args[key]!=undefined){
                    var reg = new RegExp("({" + key + "})", "g");
                    result = result.replace(reg, args[key]);
                }
            }
        }
        else {
            for (var i = 0; i < arguments.length; i++) {
                if (arguments[i] != undefined) {
                    //var reg = new RegExp("({[" + i + "]})", "g");//这个在索引大于9时会有问题
                    var reg = new RegExp("({)" + i + "(})", "g");
                    result = result.replace(reg, arguments[i]);
                }
            }
        }
    }
    return result;
}


$(document).click(function(){
    $(".layui-table-tips").remove();
})

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
 * 获取选中表格数据的ids
 * @param {*} table
 */
function checkChecked(table,field){
    if(!field) field = "id";
    var data = getTableCheck(table);
    if(data.length <= 0){
        layer.alert("请选择数据");
        return false;
    }else {
        var ids = [];
        for(var i in data){
            ids.push(data[i][field]);
        }
        return ids;
    }
}

/**
 * 获取选中表格的数据
 * @param {*} table
 */
function getTableCheck(table){
    var checkStatus = layui.table.checkStatus(table)
        ,data = checkStatus.data;
    return data;
}
function getTableCheck23(table){
    var checkStatus = layui.table.checkStatus(table)
        ,data = checkStatus.data;
    return data;
}

function getTableAllData(table){
    return layui.table.cache[table];
}
function getTableAllDataId(table){
    var data = getTableAllData(table);
    var ids = [];
    for(var i in data){
        ids.push(data[i].id);
    }
    return ids;
}

/**
 * 获取选中表格的数据
 * @param {*} table
 */
function getTableCheckData(table){
    var data = getTableCheck(table);
    if(data.length<=0){
        layer.alert("请选择数据");
        return false;
    }
    return data;
}
function getTableCheckDataid(table){
    console.log("..................................table")
    console.log(table)
    var data = getTableCheck23(table);
    console.log("...........................data12")
    console.log(data);
    if(data.length<=0){
        layer.alert("请选择数据");
        return false;
    }
    return data;
}
/**
 * 获取编辑表格的数据
 * @param {*} table
 */
function getTableEdit(table){
    var data = getTableCheck(table);
    if(data.length <=0){
        layer.alert("请选择数据");
        return false;
    }else {
        return data[0];
    }
}

/**
 * 获取编辑表格的数据
 * @param {*} table
 */
function getTableEditId(table){
    var data = getTableCheck(table);
    if(data.length <=0){
        layer.alert("请选择数据");
        return false;
    }else {
        return data[0].id;
    }
}

/**
 * 表格数据
 * @param {*} obj
 * @param {*} data
 * @param {*} columns
 */
function setTableData(obj,data,columns) {
    table.render({
        elem: obj
        ,data:data.records
        ,limit:data.size
        ,cols: [columns]
        // ,page: true
    });
}

/**
 * 过时方法
 * ueditor.all.js中有调用
 * @deprecated
 * @param {} options
 */
function doajax(options) {
    options.url = getAjaxUrl(options.url);
    options.crossDomain = true;
    options.xhrFields = {
        withCredentials: true
    };
    $.ajax(options);
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
      /*  console.log(e);*/
    });
}

function ajax2 (serviceId,url,type,data) {
    var index = layer.load();
    return new Promise(function (resovle, reject) {
        $.ajax({
            type: type,
            url: getAjaxUrl(serviceId,url),
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
    })
}

/**
 * 没有加载中效果
 */
function ajax3 (serviceId,url,type,data) {
    return new Promise(function (resovle, reject) {
        $.ajax({
            type: type,
            url: getAjaxUrl(serviceId,url),
            data: data,
            dataType: 'json',
            contentType: 'application/json',
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
                resovle(res);
            },
            error: function(err) {
                // layer.alert(err.responseText);
                reject(err);
            }
        })
    })
}
//没有加载中效果
function ajax4 (url,type,data) {
    return new Promise(function (resovle, reject) {
        $.ajax({
            type: type,
            url: url,
            data: data,
            dataType: 'json',
            success: function(res) {
                if(!res.success && res.msg == '登陆超时,请重新登陆'){
                    layer.alert(res.msg,function(){
                        window.location.href = 'login.html';
                    })
                    return false;
                }
                resovle(res);
            },
            headers:{'mspToken':com.jnetdata.mspToken},
            error: function(err) {
                // layer.alert(err.responseText);
                reject(err);
            }
        })
    })
}

/**
 * 按钮事件类型
 */
$(document).on('click', '.layui-btn',function(){
    var type = $(this).data('type');
    active[type] ? active[type].call(this) : '';
});
var active = {
    del: function(){
        del();
    },
    delAll: function(){
        delAll();
    },
    add: function () {
        add();
    },
    edit: function() {
        edit();
    },
    close: function(){
        layer.closeAll();
    },
    resetPassword: function(){
        resetPassword();
    },
    userStop:function(){
        userStop();
    },
    userRestore:function(){
        userRestore();
    }
};

/**
 * 渲染分页
 * @param {*} total 数据总数，从服务端得到
 * @param {*} curr
 * @param {*} size
 * @param {*} theme
 * @param {*} fnName
 */
function page(total,curr,size,theme,fnName,elem) {
    if(!elem) elem = "page";
    layui.laypage.render({
        elem: elem
        ,limit:size
        ,curr:curr
        ,prev: '<i class="layui-icon">&#xe603;</i>'
        ,next: '<i class="layui-icon">&#xe602;</i>'
        ,theme:theme
        ,count: total //数据总数，从服务端得到
        ,limits : [15,50,100]
        ,layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip']
        ,jump: function(obj, first){
            if(!first){
                defaultPageSize = obj.limit;
                if(fnName){
                    eval(fnName+"("+obj.curr+",{})");
                }else{
                    doList(obj.curr,{});
                }
            }
        }
    });
}

/**
 * 渲染分页
 * @param {*} total  数据总数，从服务端得到
 * @param {*} curr
 * @param {*} size
 */
function page2(total,curr,size,entity) {
    entity = entity?entity:{};
    layui.laypage.render({
        elem: 'page2'
        ,limit:size
        ,curr:curr
        ,prev: '<i class="layui-icon">&#xe603;</i>'
        ,next: '<i class="layui-icon">&#xe602;</i>'
        ,count: total //数据总数，从服务端得到
        ,layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip']
        ,jump: function(obj, first){
            if(!first){
                defaultPageSize = obj.limit;
                doList2(obj.curr,entity);
            }
        }
    });
}

/**
 * 渲染分页
 * @param {*} id
 * @param {*} total  数据总数，从服务端得到
 * @param {*} curr
 * @param {*} size
 */
function page3(id,total,curr,size) {
    layui.laypage.render({
        elem: 'page'
        ,limit:size
        ,curr:curr
        ,prev: '<i class="layui-icon">&#xe603;</i>'
        ,next: '<i class="layui-icon">&#xe602;</i>'
        ,count: total //数据总数，从服务端得到
        ,limits : [15,50,100]
        ,layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip']
        ,jump: function(obj, first){
            if(!first){
                defaultPageSize = obj.limit;
                doList(obj.curr,id,{});
            }
        }
    });
}

/**
 * 数据模板
 * @param {*} tplId
 * @param {*} divId
 * @param {*} data
 */
function lodTpl(tplId, divId, data, fn) {
    layui.laytpl($("#" + tplId).html()).render(data?data:{}, function (html) {
        $("#" + divId).html(html);
        layui.form.render();
        if (fn) {
            try {
                fn();
            } catch (error) {
              /*  console.log(error);*/
            }
        }
    })
}

/**
 * 弹出框
 * @param {*} title
 * @param {*} width
 * @param {*} height
 * @param {*} divId
 * @param {*} html
 */
function layerOpen(title,width,height,divId,html){
    return layer.open({
        type: 1,
        area: [width+'px', height+'px'],
        title: title,
        maxmin: false,
        content: '<div id="'+ (divId?divId:'openDiv') +'">'+(html?html:"")+'</div>'
    })
}
/**
 * 弹出框
 * @param {*} title
 * @param {*} width
 * @param {*} height
 * @param {*} divId
 * @param {*} html
 */
function layerOpenHtml(title,width,height,url,fn,fnstr){
    return layerOpenHtml0(title,width,height,url,fn,fnstr,1)
}


function layerOpenHtml0(title,width,height,url,fn,fnstr,type){
    var id = "openDiv" + guid();
    var area;
    if(height > 0) area = [width+'px', height+'px'];
    else area = width+'px';
    var index = layer.open({
        type: type?type:0,
        area: area,
        title: title,
        maxmin: false,
        content: '<div id="'+ id +'"></div>'
    })
    $("#"+id).load(url,function(){
        layui.form.render();
        if(fn){
            fn();
        }
        if(fnstr){
            eval(fnstr )
        }
    });
    return index;
}
/**
 * 弹出框
 * @param {*} title
 * @param {*} width
 * @param {*} height
 * @param {*} divId
 * @param {*} html
 */
function layerOpenFullHtml(title,url,fn,fnstr){
    var id = "openDiv" + guid();
    var index = layer.open({
        type: 1,
        area: ["98%", "98%"],
        title: title,
        maxmin: false,
        content: '<div id="'+ id +'"></div>'
    })
    $("#"+id).load(url,function(){
        layui.form.render();
        if(fn){
            fn();
        }
        if(fnstr){
            eval(fnstr )
        }
    });
    return index;
}

/**
 * 全屏弹出框
 * @param {*} title
 * @param {*} divId
 */
function layerOpenFull(title,divId){
    return layer.open({
        type: 1,
        area: ["98%", "98%"],
        title: title,
        maxmin: false,
        content: '<div id="'+ (divId?divId:'openDiv') +'"></div>'
    })
}
function layerOpenTpl(title,width,height,tplId,data){
    return layerOpenTpl0(title,width,height,tplId,data,0);
}

function layerOpenTpl0(title,width,height,tplId,data,type){
    var id = new Date().getTime();
    var index = layer.open({
        type:type?type:0,
        area: [width+'px', height+'px'],
        title: title,
        maxmin: false,
        content: '<div id="'+ id +'"></div>'
    })
    lodTpl(tplId,id,data);
    return index;
}

/**
 * 跳转页面
 * @param {*} url
 * @param {*} name
 */
function loaddiv(url,name){
    var urlparams;
    if(url.indexOf("?")>0){
        urlparams = url.split("?")[1].split("&");
        urlparams.forEach(function(item){
            var param = item.split("=");
            paramObj[param[0]] = param[1];
        })
    }
    if($.fn.zTree.getZTreeObj("treeDemo")){
        $.fn.zTree.getZTreeObj("treeDemo").destroy();
    }
    $('#LAY_app_body div').html("");
    $("#tabName").text(name);
    if(url){
        var mainHeight = $('#LAY_app_body').height()-170;
        var mainHeightmini = $('#LAY_app_body').height()-220;
        $("#LAY_app_body div").load(url);
        addMenuLog(url);

        setTimeout(function(){
            // layui.table.reload("demo",{height:100})
            // var mainHeight = $('#LAY_app_body').height();
            $('.table-area').height(mainHeight);
            $('.table-area-mini').height(mainHeightmini);
        },500)

    }else{
        $("#LAY_app_body div").load("/building.html");
    }

    layui.form.render();
}


/**
 * 跳转页面
 * @param {*} url
 * @param {*} divId
 */
function loadHtml(url,divId){
    $('#'+divId).html("");
    $('#'+divId).load(url);
    layui.form.render();
}

var flag=false;
/**
 * 关闭全部标签页
 */
function closeAllTabs() {
    $("#LAY_app_tabsheader").children('.tab').each(function () {
        $(this).remove();
    })
    loaddiv("../../simple/home/index.html","首页");
}
function closeThisTabs() {

}

function closeOtherTabs() {

}

/**
 * 新增标签页
 * @param {*} arr
 * @param {*} url
 * @param {*} name
 * @param {*} arr2
 */
function addTab (arr,url,name,arr2) {
    var arr1 = arr.filter(function (item,index,list) {
        return list.indexOf (item)=== index
    })
    $("#LAY_app_tabsheader").append("<li  class='tab'><span class ='tabName'></span></li>")
    $(".tabName").each(function (index,item) {
        if(flag){
            $(this).text(name)
            flag=false;
        }else{
            $(this).text(arr1[index])
        }
        $(this).click(function () {
            go2Menu(url,name)
        })
        var parent=$(this).parent();
        var others = $(this).siblings();
        others.bind('click',function () {
            arr1.splice(index,2)
           flag=true;
            item.remove()
            parent.remove();

           if(arr1.length===0){
                loaddiv("../../simple/home/index.html","首页");
           }else{
               loaddiv(arr2[index-1],arr1[index-1])
           }

        })
    })
}

function deleteTab() {

}

//var arr =[];
//var arr2=[]
/**
 * 跳转页面,菜单选中
 * @param {*} url
 * @param {*} name
 */
function go2Menu(url,name){
    $(".layui-this").removeClass("layui-this");
    $(".layui-nav-itemed").removeClass("layui-nav-itemed");
    // $(".magnify-modal").hide();
    $("#menu dd[lay-href='"+url+"']").addClass("layui-this").parents("li").addClass("layui-nav-itemed");

    loaddiv(url,name);
}

/**
 * 将"2018-05-19T08:04:52.000+0000"这种格式的时间转化为正常格式
 * @param time
 */
function timeFormat(time) {
    var d = new Date(time);
    var year = d.getFullYear();       //年
    var month = d.getMonth() + 1;     //月
    var day = d.getDate();            //日
    var hh = d.getHours();            //时
    var mm = d.getMinutes();          //分
    var ss = d.getSeconds();           //秒
    var clock = year + "/";
    if (month < 10)
        clock += "0";
    clock += month + "/";
    if (day < 10)
        clock += "0";
    clock += day + " ";
    if (hh < 10)
        clock += "0";
    clock += hh + ":";
    if (mm < 10) clock += '0';
    clock += mm + ":";
    if (ss < 10) clock += '0';
    clock += ss;
    return (clock);
}



////////////////////////////////////////////////////////////////////
// gateway用
var $ = layui.jquery;
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

/**
 * 加载html文件
 * @param {*} obj
 * @param {*} url
 */
function setLoad(obj,url){
	$(obj).load(url);
}

/**
 * 加载当前栏目下的html文件
 * @param {*} obj
 * @param {*} htmlName
 */
function setLoadCurPath(obj,htmlName){
	var url = $('.layui-side .layui-this').attr('lay-href');
	var num = url.lastIndexOf('/');
	url =  url.substring(0,num+1);
	$(obj).load(url+htmlName);
}

/**
 * 加载指定目录下的html文件
 * @param {*} obj
 * @param {*} htmlName
 * @param {*} url
 */
function setLoadAssignPath(obj,htmlName,url){
	var url = url;
	$(obj).load(url+htmlName);
}

/**
 * 关闭弹出层
 */
function closeLayer(){
    layer.closeAll();
}

/**
 * 自带渲染
 */
function setRender(){
	layui.form.render();
}

/**
 * //获取表单数据
 * @param {*} obj
 * @param {*} data
 */
function setFormVal(obj,data){
	form.val(obj, data);
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
 * 弹出层
 * @param {*} obj
 * @param {*} tit
 * @param {*} width
 * @param {*} height
 */
function setLayer(obj,tit,width,height){
    setRender();
	layer.open({
		title: tit,
		type: 1,
		skin: 'layui-layer-green',
		// scrollbar: false,
		btnAlign: 'c',
		area: [width, height],
        // content: $(obj),
        content: '<div id="'+obj+'" class="open_layer_form"></div>',
        // content: '<div id="'+ (divId?divId:'openDiv') +'">'+(html?html:"")+'</div>',
		// btn: ['确定','取消'],
		// btn1: function(index, layero){
		// 	layer.close(layer.index);
		// 	$(obj).hide();
		// },
		// btn2: function(index, layero){
		// 	$(obj).hide();
		// },
		cancel: function(){
			$(obj).hide();
		}
	});
}

/**
 * 分页
 * @param {*} obj
 * @param {*} total
 * @param {*} limit
 * @param {*} current
 * @param {*} callBack
 */
function setPages (obj,total,limit,current,callBack) {
    layui.laypage.render({
        elem: obj,
        count: total,
        prev: '<i class="layui-icon">&#xe603;</i>',
        next: '<i class="layui-icon">&#xe602;</i>',
        limit:limit,
        curr:current,
        layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip'],
        jump: function (obj, first) {
            if (!first) {
                callBack(obj.curr, obj.limit);
            }
        }
    });
}

/**
 * 上传文件
 * @param {*} elem
 * @param {*} serviceId 微服务id
 * @param {*} url
 * @param {*} type
 * @param {*} format
 */
function setUpload (elem, serviceId, url,type,format) {
	upload.render({
		elem: elem
		,url: getAjaxUrl(serviceId, url)
		,accept: type
		,exts: format
		,size: '512000'
		,choose:function (obj) {
			obj.preview(function(index, file, result){
				$(elem+'Pic').attr('src', result);
				// $(elem+'Text').html(file.name);
			});
		}
		,before: function(obj){
			obj.preview(function(index, file, result){
				$(elem+'Pic').attr('src', result);
				// $(elem+'Text').html(file.name);
			});
		}
		,done: function (res) {
			console.log(res);
			//layer.closeAll('loading');
		}
	});
}

/**
 * 设置日期
 * @param {*} id
 * @param {*} type
 */
function setDate(id,type){
	laydate.render({
		elem: id
		,type: type
	});
}

/**
 * 左侧栏目
 */
function findMenu () {
    const comid = '1167323268476895233';
	const url = "/menu/getMenu";
    ajax(service_prefix.member,url,"get",{type:1,companyId:comid})
    .then(res=>{
		layui.use(['element','laytpl'], function(){
			layui.laytpl($("#menuTemplate").html()).render(res, function(html){
				$("#menu").html(html);
            });
            //导航的hover效果、二级菜单等功能，需要依赖element模块
			var element = layui.element;
			element.init();
		});
		getTitle()
    })
    .catch(res=>{
        console.log(res)
	})
}

/**
 * 版权所有
 */
function getCopyRight() {
	ajax(service_prefix.manage,"/systemmsg/getinfo","get").then(function (data) {
		$("#copyright").html(data.obj.copyRight);
	})
}

/**
 * 左侧主标题
 */
function getTitle(){
	ajax(service_prefix.manage,"/systemmsg/getinfo","get").then(function (data) {
		$("#title").text(data.obj.name);
	})
}


/**
 * 详情
 * @param {*} data
 */
function setDetailData(data) {
    localStorage.setItem('detailId',data.id);
    setLoadCurPath('#DetailDiv','detail.html');
    $('#detail').show();
    $('#list').hide();
}

/**
 * 返回上一页
 */
function returnBack(){
    $('#list').show();
    $('#detail').hide();
}

/**
 * 加载弹出框
 * <button class="layui-btn layuiadmin-btn-admin" data-type="exportExcel" data-layer-html="exportExcel.html" data-layer-path="simple/gateway/export/">Excel导出</button>
 * data-type 弹框类型
 * data-layer-html 当前栏目下的html文件
 * data-layer-path 指定目录下的html文件
 */
function setOpenLayer(){
    $('div[id^="openLayerDiv"]').remove();
    var layerHtmlPrev = '';
    var layerHtml = '';
    var type = '';
    var layerPath = '';
    $('.layuiadmin-btn-admin').each(function(index,element){
        layerHtmlPrev =  layerHtml;
        type = $(this).attr('data-type');
        layerHtml = $(this).attr('data-layer-html');
        layerPath = $(this).attr('data-layer-path');
        if(type && layerHtml ){
            if(layerHtml != layerHtmlPrev){
                var openLayerDiv = '<div id="openLayerDiv'+index+'"></div>';
                $(document.body).append(openLayerDiv);
                if(layerPath){
                    setLoadAssignPath('#openLayerDiv'+index,layerHtml,layerPath);
                }else{
                    setLoadCurPath('#openLayerDiv'+index,layerHtml);
                }
            }
        }
    });
}

// function doListTable(params){
//     doListTable(params.serviceId,params.url,params.type,param.param,params.pageNo,params.sortNo,params.tableId,params.columns,params.pageId,params.size);
// }
/**
 * 加载表格
 * @param serviceId 微服务id, 示例：service_prefix.gateway
 * @param url 请求地址，示例："/routerParam/list"
 * @param type 请求的Http方法，示例："post"
 * @param param 请求参数（条件查询的条件）
 * @param pageNo  要加载第几页
 * @param sort 排序（暂未考虑）
 * @param tableId 要加载表格的div的id，示例："tableItem1"
 * @param columns 表格列
 * @param pageId 要加载分页工具栏的div的id，示例："page_tableItem1"
 * @param size 每页需要显示的行数
 */
function doListTable(serviceId,url,type,param,pageNo,sort,tableId,columns,pageId,size) {
    if(!pageNo){pageNo=1;}//默认第一页
    if(!sort){sort=[];}//默认不指定排序
    let params={};
    let defaultSize=15;
    if(size){
        defaultSize=size;
    }
    params.pager = {current:pageNo,size:defaultSize};
    params.pager.sortProps = sort;
    params.entity = param;
    ajax(serviceId, url,type,JSON.stringify(params)).then(function (data){
        if (!data.success) {
            layer.alert(data.msg);
            return;
        }
        let obj = data.obj;
        table.render({
            elem: "#" + tableId
            , data: obj.records
            , limit: obj.size
            , cols: [columns]
        });
        layui.laypage.render({
            elem: pageId
            , limit: obj.size
            , curr: obj.current
            ,prev: '<i class="layui-icon">&#xe603;</i>'
            ,next: '<i class="layui-icon">&#xe602;</i>'
            , count: obj.total
            , layout: ['count', 'prev', 'page', 'next', 'refresh', 'skip']
            , jump: function (object, first) {
                if (!first) {
                    doListTable(serviceId,url,type,param,object.curr,sort,tableId,columns,pageId,size);
                }
            }
        });
    });
}

/**
 * 表单验证
 */
form.verify({
    integer: [/^\d*$/, "只能填写整数"],
    naturalNumber:function (value) {
        if(!/^\d*$/.test(value)||value<=0){
            return '只能填写大于零的整数';
        }
    }
});



/**
 * 拼url参数
 * @param {*} param
 * @param {*} key
 * @param {*} encode
 */
function urlEncode(param, key, encode) {
    if (param==null) return '';
    var paramStr = '';
    var t = typeof (param);
    if (t == 'string' || t == 'number' || t == 'boolean') {
        paramStr += '&' + key + '='  + ((encode==null||encode) ? encodeURIComponent(param) : param);
    } else {
        for (var i in param) {
            var k = key == null ? i : key + (param instanceof Array ? '[' + i + ']' : '.' + i)
            paramStr += urlEncode(param[i], k, encode)
        }
    }
    return paramStr;
}

/**
 * 标签切换
 */
function showTab(id){
    $("#"+id).trigger("click");
}

/**
 * div切换
 */
function showTabDiv(id){
    $("#"+id).show().siblings().hide();
}

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
 * 设置主体高度
 */
function setMainHeight(){
    var heightL = $('#menu').height();
    var heightR = $('#LAY_app_body').height();
    var mainHeader = $('#mainHeader').height();
    $('#menu').height(heightL+20);
    $('#LAY_app_body #mainHeader').css("background-color","yellow");

    if(heightL > heightR){
        $('.layui-fluid').height(heightL - 120);
    }
}
//点击按钮操作
// function clickBtn(layerType){
// 	$('.layui-btn').click(function(){
//         var type = $(this).attr('data-type');
// 		if(type === layerType){
//             eval(layerType+"();");
// 		}
// 		if(type){return false;}
// 	});
// 	//加载弹出框
// 	setOpenLayer();
// }

//ztree
function getCheckNodes(treeObj){
    var nodes = treeObj.getCheckedNodes(true);
    return nodes;
}
function getCheckNodesIds(treeObj) {
    var nodes = getCheckNodes(treeObj);
    var ids = [];
    var names = [];
    for(var o of nodes){
        ids.push(o.id);
        names.push(o.name)
    }
    if(ids.length <= 0){
        layer.alert("请选择数据");
        return false;
    }
    return {ids:ids,names:names}
}
function isPermitted(str){
    var strs = str.split(":");
    if(myPermission.indexOf(strs[0])>=0){
        return true;
    }
    if(strs.length>1 && myPermission.indexOf(strs[0]+":"+strs[1])>=0){
        return true;
    }
    if(myPermission.indexOf(str)>=0){
        return true;
    }
    return false;
}

function limitShow(type,id,parentId){
    if(isPermitted(type+":child:"+id)){
        $(".limit-child").show();
    }else{
        $(".limit-child").hide();
    }
    if(isPermitted(type+":child:"+parentId)){
        $(".limit-parent").show();
    }else{
        $(".limit-parent").hide();
    }
    if(isPermitted(type+":user:"+id)){
        $(".limit-user").show();
    }else{
        $(".limit-user").hide();
    }
    if(isPermitted(type+":limit:"+id)){
        $(".limit-limit").show();
    }else{
        $(".limit-limit").hide();
    }
    // if(isPermitted(type+":add:"+id)){
    //     $(".limit-add").show();
    // }else{
    //     $(".limit-add").hide();
    // }
    if(isPermitted(type+":edit:"+id)){
        $(".limit-edit").show();
    }else{
        $(".limit-edit").hide();
    }
    if(isPermitted(type+":delete:"+id)){
        $(".limit-del").show();
    }else{
        $(".limit-del").hide();
    }
    if(isPermitted(type+":import:"+id)){
        $(".limit-import").show();
    }else{
        $(".limit-import").hide();
    }
    if(isPermitted(type+":export:"+id)){
        $(".limit-export").show();
    }else{
        $(".limit-export").hide();
    }
    if(isPermitted(type+":examine:"+id)){
        $(".limit-examine").show();
    }else{
        $(".limit-examine").hide();
    }
    if(isPermitted(type+":cite:"+id)){
        $(".limit-cite").show();
    }else{
        $(".limit-cite").hide();
    }
    if(isPermitted(type+":copy:"+id)){
        $(".limit-copy").show();
    }else{
        $(".limit-copy").hide();
    }
    if(isPermitted(type+":move:"+id)){
        $(".limit-move").show();
    }else{
        $(".limit-move").hide();
    }
    if(isPermitted(type+":sort:"+id)){
        $(".limit-sort").show();
    }else{
        $(".limit-sort").hide();
    }
    if(isPermitted(type+":top:"+id)){
        $(".limit-top").show();
    }else{
        $(".limit-top").hide();
    }
}

function limitShowTableList(type){
    $(".limit-tablelist").each(function(){
        if(isPermitted(type + ":"+$(this).attr("limit-type")+":" + $(this).attr("limit-id"))) $(this).removeClass("limit-tablelist");
    })
}

function loadPageLeft(elem,page,fn) {
    if(!elem) elem = "page";
    var setting = {
        elem: elem
        ,limit:page.size
        ,curr:page.current
        ,count: page.total //数据总数，从服务端得到
        ,layout: ['count', 'prev', 'page', 'next']
        ,prev: '<'
        ,next: '>'
        ,groups: 1
        ,jump: function(obj, first){
            if(!first){
                fn(obj.curr,obj.limit);
            }
        }
    }
    layui.laypage.render(setting);
}

function loadPage(page,fn) {
    loadPage2(page.elem,page,fn)
}

function loadPage2(elem,page,fn) {
    if(!elem) elem = "page";
    layui.laypage.render({
        elem: elem
        ,limit:page.size
        ,curr:page.current
        ,count: page.total //数据总数，从服务端得到
        ,limits : [10,15,50,100]
        ,layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip']
        ,jump: function(obj, first){
            if(!first){
                fn(obj.curr,obj.limit);
            }
        }
    });
}

function loadPage3(elem,page,entity,fn) {
    if(!elem) elem = "page";
    layui.laypage.render({
        elem: elem
        ,limit:page.size
        ,curr:page.current
        ,count: page.total //数据总数，从服务端得到
        ,limits : [15,50,100]
        ,layout: ['count', 'prev', 'page', 'next']
        ,jump: function(obj, first){
            if(!first){
                fn(obj.curr,obj.limit,entity);
            }
        }
    });
}

function loadTable(elem,cols,data,limit) {
    layui.table.render({
        elem: elem
        ,data:data
        ,cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
        ,cols: [cols],
        limit:limit
    });
}

function getFormVerify(){
    layui.form.verify({
        englishletter: [
            /^[a-zA-Z]+$/
            ,'必须是英文字母'
        ]
        ,username: function(value, item){ //value：表单的值、item：表单的DOM对象
            if(!new RegExp("^[a-zA-Z0-9_\\s·]+$").test(value)){
                return '用户名只能是英文,数字,下划线';
            }
            if(/(^\_)|(\__)|(\_+$)/.test(value)){
                return '用户名首尾不能出现下划线\'_\'';
            }
            if(/^\d+\d+\d$/.test(value)){
                return '用户名不能全为数字';
            }
        }
        //我们既支持上述函数式的方式，也支持下述数组的形式
        //数组的两个值分别代表：[正则匹配、匹配不符时的提示文字]
        ,pass: [
            /^[\S]{6,12}$/
            ,'密码必须6到12位，且不能出现空格'
        ]

        ,bankcard: [
            /^(\d{16,19})$/
            ,'银行卡号格式不对'
        ]

        ,carnum: [
            /^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼]{1}[A-Z]{1}[A-Z0-9]{4,5}[A-Z0-9挂学警港澳]{1}$/
            ,'车牌号有误'
        ]

        ,chinese: [/^[\u0391-\uFFE5]+$/
            ,'只能是汉字'
        ]
    });
}
getFormVerify()

function dayToString(date){
    if(!date) date = new Date();
    var year = date.getFullYear();
    var month =(date.getMonth() + 1).toString();
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

/**
 * 获取当前用户
 */
function getUser(fn) {
    var url = "/user/getLoginUser"
    ajax(service_prefix.member,url,"get").then(function (res){
        if(res.success){
            if(fn){
                fn(res);
            }
        }
    })
}
function guid() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = Math.random() * 16 | 0,
            v = c == 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    });
}

function ajaxJsonPost(url, data, fn){
    ajax('',url,"post",JSON.stringify(data)).then(function(res){
        fn(res);
    })
}

function ajaxPost(url,data,fn){
    ajax2('',url,"post",data).then(function(res){
        fn(res);
    })
}

function ajaxGet(url,fn){
    ajax('',url,"get").then(function (res){
        if(fn) fn(res);
    })
}


function htmlUnescapeForObj(data){
    for(var k in data){
        data[k] = htmlUnescape(data[k]);
    }
}

function htmlUnescape(str){
    if((typeof str=='string')&&str.constructor==String) {
        return str ? str.replace(/&((g|l|quo)t|amp|#39|nbsp);/g, function (m) {
            return {
                '&lt;': '<',
                '&amp;': '&',
                '&quot;': '"',
                '&gt;': '>',
                '&#39;': "'",
                '&nbsp;': ' '
            }[m]
        }) : '';
    }
}

//6到16位，且不能出现空格
function checkoutPass1(pass){
    var test = /^[\S]{6,16}$/;
    if(!test.test(pass)) return '密码必须6到16位，且不能出现空格';
}

//8到16位，包括数字、小写字母、大写字母、特殊符号中至少3类
function checkoutPass2(pass){
    var test = /^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![a-z0-9]+$)(?![A-Z\\W_!@#$%^&*`~()-+=]+$)(?![a-z\\W_!@#$%^&*`~()-+=]+$)(?![0-9\\W_!@#$%^&*`~()-+=]+$)[a-zA-Z0-9\\W_!@#$%^&*`~()-+=]{8,16}$/;
    if(!test.test(pass)) return '密码必须8到16位，包括数字、小写字母、大写字母、特殊符号中至少3类，且不能出现空格';
}

//长度在8-16位之间
//必须包含字母、数字和半角符号中的2种
//密码不能出现连续相邻字母
//密码不能包含password
//密码不能包含ABAB、ABCABC型字母或数字
function checkoutPass3(pass){
    pass = pass.trim().toLowerCase();
    if(!pass) return "密码不能为空";
    var test1 = /\s+/;
    if(test1.test(pass)) return "不能包含空格";
    if(pass.length<8||pass.length>16)  return "长度在8-16位之间";
    var test2 = /((?=.*\d)(?=.*\D)|(?=.*[a-zA-Z])(?=.*[^a-zA-Z]))(?!^.*[\u4E00-\u9FA5].*$)/;
    if(!test2.test(pass))  return "必须包含字母、数字和半角符号中的2种";
    var nums = "01234567890";
    var letters = "abcdefghijklmnopqrstuvwxyz";
    for(var i=0;i<pass.length-2;i++){
        var pass3 = pass.substring(i,i+3);
        var repass3 = pass3.split("").reverse().join("");
        if(nums.indexOf(pass3)>=0 || letters.indexOf(pass3)>=0 || nums.indexOf(repass3) >=0 || letters.indexOf(repass3) >=0){
            return "密码不能出现连续相邻字母";
        }
    }
    if(pass.indexOf("password")>=0) return '密码不能包含password';
    var test3 = /(\S)((?!\1)\S)\1\2/;
    if(test3.test(pass)) return '密码不能包含ABAB、ABCABC型字母或数字';
    var test3 = /(\S)((?!\1)\S)((?!\1\2)\S)\1\2\3/;
    if(test3.test(pass)) return '密码不能包含ABAB、ABCABC型字母或数字';
}

function checkoutPassword(pass){
    return checkoutPass1(pass);
}

function renderRightTable(id,cols,data,height){
    var rw = $(".jnet-right").width();
    if(rw<=0) rw = $(".md2-right").width();
    $("#"+id,).parent().width(rw-30);
    if(!height) height = $(".jnet-tab-content .layui-show .layui-card-body").height()-75;
    $("#"+id,).parent().height(height);

    var tableHeight = data.length * 39 + 41 + 17;
    if(tableHeight > height) tableHeight = height;
    for(var i=cols.length-1;i>=0;i--){
        if(cols[i].fixed != 'right'){
            cols.splice(i+1,0
                , {field: 'crUser', title: '创建人',width:80}
                , {field: 'createTime', title: '创建时间',width:160}
                , {field: 'modifyUser', title: '操作人',width:80}
                , {field: 'modifyTime', title: '操作时间',width:160}
                )
            break;
        }
    }

    layui.table.render({
        elem: '#'+id
        , data: data
        , cellMinWidth: 80
        , cols: [cols]
        , height: tableHeight
        , limit: 1000
    });

    var p = $("#"+id,).parent().find(".layui-table-body");
    if($(p).width() >= $(p).find("table").width()){
        $("div[lay-id='"+id+"']").height(tableHeight - 20);
    }
}

//获取站点树
function getSiteTreeDatas(fn) {
    ajax(service_prefix.manage,"/site/getSiteTree","get",{}).then(data => {
        var datas = data.obj;
        if(fn) fn(datas);
    })
}

//获取站点树
function getSiteTree(id,zTreeOnclickfn,loadDatafn) {
    getSiteTreeDatas(function(datas){
        var settingss = {
            data: {
                simpleData: {
                    enable: true, //true 、 false 分别表示 使用 、 不使用 简单数据模式
                    idKey: "id", //节点数据中保存唯一标识的属性名称
                    pIdKey: "parentId", //节点数据中保存其父节点唯一标识的属性名称
                    rootPId: -1 //用于修正根节点父节点数据，即 pIdKey 指定的属性值
                },
                key: {
                    isParent: "isp"
                }
            },
            view: {
                showLine: false,
                showIcon: true
            },
            callback: {
                onClick: function(event, treeId, treeNode){
                    zTreeOnclickfn(treeNode);
                }
            }
        };
        var zTreeObj = $.fn.zTree.init($("#"+id), settingss, datas); //初始化树
        zTreeObj.expandAll(true);
        if(loadDatafn) loadDatafn(zTreeObj,datas);
    })
}

function getProgramaTreeData(fn){
    ajax2(service_prefix.manage, "/programa/getTreeData", "post", {webclass: 1}).then(function (res) {
        if(fn) fn(res.obj);
    })
}

function getZtreeCurrNode(zTreeObj){
    try{
        return zTreeObj.getSelectedNodes()[0];
    }catch{
        return null;
    }
}

/***** 站群 *************/

 /*
 * 机构管理左侧树
 * */
function getCompanyTreeData(fn){
    ajaxGet("/member/company/treeList",function(data){
        data.obj.push({id:"0",name:"根机构",parentId:"-1",iconSkin:"companyTreeIcon1"});
        fn(data.obj);
    })
}