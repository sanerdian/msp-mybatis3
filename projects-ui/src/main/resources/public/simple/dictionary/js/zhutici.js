

/**
 * 设置列表数据
 * @param {*} serviceId 微服务id(对应地址前缀)
 * @param {*} url 请求地址
 */
function setListData (curr, defaultPageSize, serviceId, url, entity) {
    if (entity) {
    }else{
        var entity = {};
    }
    var jsonData = {
        "pager": {
            "current": curr,
            "size": defaultPageSize,
            "sortProps": [
                {
                    "key": "crtime",
                    "value": false
                }
            ]
        },
        "entity": entity
    };

    ajax(serviceId, url + '/listing', 'post', JSON.stringify(jsonData)).then(function (data) {
        if (data.success) {
            setTableData('#tableData', data.obj, columns)
            curr = data.obj.current
            defaultPageSize = defaultPageSize
            var pegeObj = {}
            pegeObj.serviceId = serviceId
            pegeObj.url = url
            pegeObj.entity = entity
            pegeObj.elem = "page"
            pegeObj.total = data.obj.total
            pegeObj.curr = data.obj.current
            pegeObj.size = defaultPageSize
            loadPage1(pegeObj, setListData)
        } else {
            // console.log(data.msg);
            setTableData('#tableData', {}, columns)
        }
    })
}

//新增数据
function doAddData(curr,defaultPageSize,serviceId,url,data){	
	ajax(serviceId,url,'post',JSON.stringify(data)).then(function (data) {
		if(data.success){
			setListData(curr,defaultPageSize,serviceId,url);
		}else{
			console.log(data.msg);
		}		
	});
}

//删除数据
function doDeleteById(serviceId,url,id) {
	ajax(serviceId,url+'/'+id,'delete').then(function (data) {
		if(data.success){
			setListData(curr, defaultPageSize,serviceId,url);
			closeLayer();
		}else{
			console.log(data.msg);
		}
	});
}
/**
 * 删除 批量删除
 * source  0 主题词  1属性词 2场景词 3停用词  4相关词  5形容词  6反义词 7 满意度强度 8任务管理
 * **/
 function del () {
    var data = getTableCheck('tableData');//选取表格中的数据
    console.log('selectTableId',data)
    if (data.length <= 0) {
        layer.alert("请选择数据");
        return false;
    } else {
        var ids = "";
        var infodata = {};
        for (var i in data) {
            if (ids == "") {
                ids = data[i].id;
            } else {
                ids = ids + "," + data[i].id;
            }
        }
        // console.log('ids',ids)
        doDeleteByIds(ids);
    }
}
//根据ids批量删除
function doDeleteByIds (data) {
    layer.alert("是否确定删除,该操作不可撤回", function () {
        ajax("", url + "/real/" + data + "/batch", "delete").then(res => {
            res.msg = "删除失败";
            if (res.success) {
                setListData(curr, defaultPageSize, serviceId, url);;
                layer.closeAll();
            } else {
                layer.alert(res.msg);
            }
        }).catch(res => {

        })
    });
}

//修改数据
function doEditData(serviceId,url,id,data){	
	ajax(serviceId,url+'/'+id,'put',JSON.stringify(data.field)).then(function (data) {
		if(data.success){
			// console.log('修改数据',data);
			setListData(curr, defaultPageSize,serviceId,url);
		}else{
			console.log('data.msg',data.msg);
		}		
	});
}


/**
 * 设置列表数据 条件搜索
 * @param {*} serviceId 微服务id(对应地址前缀)
 * @param {*} url 请求地址
 */
 function setListDataSearch (curr, defaultPageSize, serviceId, url, entity) {
    if (entity) {
    }else{
        var entity = {};
    }
    var jsonData = {
        "pager": {
            "current": curr,
            "size": defaultPageSize,
            "sortProps": [
                {
                    "key": "crtime",
                    "value": false
                }
            ]
        },
        "entity": entity
    };

    ajax(serviceId, url + '/search', 'post', JSON.stringify(jsonData)).then(function (data) {
        if (data.success) {
            setTableData('#tableData', data.obj, columns)
            curr = data.obj.current
            defaultPageSize = defaultPageSize
            var pegeObj = {}
            pegeObj.serviceId = serviceId
            pegeObj.url = url
            pegeObj.entity = entity
            pegeObj.elem = "page"
            pegeObj.total = data.obj.total
            pegeObj.curr = data.obj.current
            pegeObj.size = defaultPageSize
            loadPage1(pegeObj, setListData)
        } else {
            // console.log(data.msg);
            setTableData('#tableData', {}, columns)
        }
    })
}

// //条件搜索
// function getSearch(serviceId,url,curr,defaultPageSize){
// 	form.on("submit(LAY-back-search)",function(data){
// 		setListDataSearch(curr,defaultPageSize,serviceId,url,data.field);
// 		return false;
// 	})
// }








//点击按钮
function btnClick () {
    $('.layui-btn').click(function () {
        var type = $(this).attr('data-type');
        var typetxt = $(this).text();
        var curName = $.trim($('.layui-side .layui-this').text());
        var tit = typetxt + curName;
        if (type == 'addDict') {
            setLayer('addDiv', tit, '650px', '650px');
            //表单提交
            formSubmitAdd(serviceId, url);
            $('#addDiv').html($('#addTemplate').html());
            setRender();
            //添加模块名称
            addModuleName();
        } else if (type == 'delDict') {
            del();
        } else if (type == 'examDict') {//审核
            // formSubmitEdit(serviceId,url);
            // id = data.id;
            setLayer('examDiv', tit, '650px', '700px');
            laytpl($('#examTemplate').html()).render(editdata, function (html) {
                $('#examDiv').html(html);
                setFormVal('editForm', editdata);
            });
            formSubmitEditExam(serviceId, url, editdata.id);
        }
    });
}

table.on('tool(tableData)', function (obj) {
    var data = obj.data;
    var layEvent = obj.event;
    var tr = obj.tr;
    // console.log('data',data);
    var curName = $('.layui-side .layui-this').text();
    if (layEvent == 'del') { //删除
        // console.log(data)
        layer.alert("确定删除:[" + data.wordname + "]吗？", function () {
            doDeleteById(serviceId, url, data.id);
        })
    } else if (layEvent == 'edit') { //编辑
        //表单提交
        formSubmitEdit(serviceId, url);
        id = data.id;
        // id = data.id
        var typetxt = $(this).text();
        var curName = $.trim($('.layui-side .layui-this').text());
        var tit = typetxt + curName;
        setLayer('editDiv', tit, '650px', '660px');
        //后台往前台传,data
        laytpl($('#editTemplate').html()).render(data, function (html) {
            $('#editDiv').html(html);
            setFormVal('editForm', data);
        });
        //前台保存data根据Id 往后台传
        //使用的方式ajax
        // formSubmitEditExam(serviceId, url, id);
    } else if (layEvent == 'detail') { //详情
        setLayer('detailDiv', tit, '723px', '600px');
        if (data.modifyTime == null) {
            data.modifyTime = ''
        }
        getData(data, '#detailTemplate', '#detailDiv');
    }
}); 


//获取主题词拼音
function getzhuticipinyin () {
    var zhutici = $('#wordname').val();
    if (!zhutici) return false;
    zhutici = zhutici.replace(/\s*/g, "");
    // console.log("zhutici", zhutici);

    // 获取全写拼音（调用js中的方法）
    var fullChars = pinyin.getFullChars(zhutici);
    // alert("s属性词 pingyin="+fullChars)
    // 		 console.log("shuxingpinyin", fullChars);

    //给文本框赋值
    $('#pinyin').val(fullChars);
}
formSubmitAdd(serviceId, url);

//获取模块信息
function getMoudleInfo(){
    var jsonData = {
        "entity": {},
        "pager": {
          "current": 1,
          "size": 10
        }
    }
    ajax(serviceId,'/metadata/moduleinfo/list','post',JSON.stringify(jsonData)).then(function (data){
        if(data.success){
            getData(data.obj.records, '#moduleNameTpl', '#moduleNameView');
            setRender();
        }
    })
}

//选择模块确认
function setupModuleConfirm(){
    var setupModuleName = $('#moduleNameView').val();
    localStorage.setItem('dictionarySetupModuleName',setupModuleName);
    var jsonData = {
        "fenlei": setupModuleName
    }
    ajax(serviceId,'/zhutici/batchModify','post',JSON.stringify(jsonData)).then(function (data){
        if(data.success){
            layer.msg('设置模块成功！');
        }
    })
}

//设置模块弹出框
function setupModule(){
    layer.open({
        type: 1,
        scrollbar:false,
        area: '520px',
        btnAlign: 'c',
        title: ['设置模块'],
        shadeClose: true,
        btn: ['确认', '取消'],
        content: $('#setupModule'),
        success: function(layero, index){
            var mask = $(".layui-layer-shade");
            mask.appendTo(layero.parent());
            //获取模块信息
            getMoudleInfo();
        },
        btn1:function(){
            //选择模块确认
            setupModuleConfirm();
            $('#setupModule').hide();
            layer.closeAll();
        },
        btn2:function(){
            $('#setupModule').hide();
            layer.closeAll();
        },
        cancel:function(){
            $('#setupModule').hide();
            layer.closeAll();
        }
    });
}