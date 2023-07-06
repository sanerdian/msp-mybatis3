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
        ,page: false
    });
}

//设置翻页
function loadPage1 (page, fn) {
    if (!page.elem) page.elem = "page";
    layui.laypage.render({
        elem: page.elem
        , limit: page.size
        , curr: page.curr
        , count: page.total //数据总数，从服务端得到
        , limits: [15, 50, 100]
        , layout: ['count', 'prev', 'page', 'next']
        , jump: function (obj, first) {
            if (!first) {
                fn(obj.curr, obj.limit,page.serviceId,page.url,page.entity);
            }
        }
    });
}


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

    ajax(serviceId, url + '/list', 'post', JSON.stringify(jsonData)).then(function (data) {
        if (data.success) {
            for (let i = 0; i < data.obj.records.length; i++) {
                var status = data.obj.records[i].status;
            }
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
            setTableData('#tableData', {}, columns)
        }
    })
}


setLoadCurPath('#deatilOpenDiv', 'detail.html');


//获取时间
function getDay (day) {
    var today = new Date();
    var targetday_milliseconds = today.getTime() + 1000 * 60 * 60 * 24 * day;
    today.setTime(targetday_milliseconds); //注意，这行是关键代码
    var tYear = today.getFullYear();
    var tMonth = today.getMonth();
    var tDate = today.getDate();
    var thours = today.getHours();
    var tmin = today.getMinutes();
    var tsecond = today.getSeconds();
    tMonth = doHandleMonth(tMonth + 1);
    tDate = doHandleMonth(tDate);
    return tYear + "-" + tMonth + "-" + tDate + " " + "00:00:00";
}
// 时间月份格式替换
function doHandleMonth (month) {
    var m = month;
    if (month.toString().length == 1) {
        m = "0" + month;
    }
    return m;

}

setLoadCurPath('#editOpenDiv', 'edit.html');
var editdata = {};
table.on('checkbox(tableData)', function (obj) {
    editdata = obj.data;
    console.log(obj.data)
})



/**
 * 删除 批量删除
 * source  0 主题词  1属性词 2场景词 3停用词  4相关词  5形容词  6反义词 7 满意度强度 8任务管理
 * **/
function del (source) {
    var data = getTableCheck('tableData');//选取表格中的数据
    console.log('selectTableId',data)
    if (data.length <= 0) {
        layer.alert("请选择数据");
        return false;
    } else {
        var ids = "";
        var infodata = {}
        for (var i in data) {
            if (source == 0) {
                if (ids == "") {
                    ids = data[i].jmetasearwordid
                } else {
                    ids = ids + "," + data[i].jmetasearwordid
                }
            }
            else if (source == 1) {
                if (ids == "") {
                    ids = data[i].jmetashuxingciid
                } else {
                    ids = ids + "," + data[i].jmetashuxingciid
                }
            } else if (source == 2) {
                if (ids == "") {
                    ids = data[i].jmetachangjingid
                } else {
                    ids = ids + "," + data[i].jmetachangjingid
                }
            } else if (source == 3) {
                if (ids == "") {
                    ids = data[i].jmetastopwordid
                } else {
                    ids = ids + "," + data[i].jmetastopwordid
                }
            } else if (source == 4) {
                if (ids == "") {
                    ids = data[i].jmetawordrelationid
                } else {
                    ids = ids + "," + data[i].jmetawordrelationid
                }
            } else if (source == 5) {
                if (ids == "") {
                    ids = data[i].jmetaadjectelationid
                } else {
                    ids = ids + "," + data[i].jmetaadjectelationid
                }
            } else if (source == 6) {
                if (ids == "") {
                    ids = data[i].jmetafanyiciid
                } else {
                    ids = ids + "," + data[i].jmetafanyiciid
                }
            } else if (source == 7) {
                if (ids == "") {
                    ids = data[i].metaid
                } else {
                    ids = ids + "," + data[i].metaid
                }
            } else if (source == 8) {
            }
        }
        infodata.field1 = ids
        // console.log(infodata)
        doDeleteByIds(infodata);
    }
}

//根据ids批量删除
function doDeleteByIds (data) {
    layer.alert("是否确定删除,该操作不可撤回", function () {
        ajax("", url + "/batchDel", "post", JSON.stringify(data)).then(res => {
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


//反义词列表从形容词中取数据

function setXingrongciListData (serviceId, url, entity) {
    var jsonData = {
        "pager": {
            "current": 1,
            "size": defaultPageSize,
            "sortProps": [
                {
                    "key": "xingrongciKey",
                    "value": false
                }
            ]
        },
        "entity": entity
    };
    ajax(serviceId, url + '/list', 'post', JSON.stringify(jsonData)).then(function (data) {
        if (data.success) {
            setTableData('#tableData', data.obj, columns);
        } else {
            console.log(data.msg);
        }
    })
}

//新setListData
function setListData1 (serviceId, url, entity) {
    var entity = {};
    if (entity) {
        if (entity.time == "近一天") {
            entity.time = getDay(-1)
        } else if (entity.time == "近三天") {
            entity.time = getDay(-3)
        } else if (entity.time == "近一周") {
            entity.time = getDay(-7)
        }
    }
    var jsonData = {
        "pager": {
            "current": 1,
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
    ajax(serviceId, url + '/list', 'post', JSON.stringify(jsonData)).then(function (data) {
        console.log(data)
        if (data.success) {
            for (let i = 0; i < data.obj.records.length; i++) {
                var status = data.obj.records[i].status;
                if ("21" == status) { data.obj.records[i].status = "未生效"; }
                else if ("22" == status) { data.obj.records[i].status = "已生效"; }
                else if ("23" == status) { data.obj.records[i].status = "停用"; }
                else if ("24" == status) { data.obj.records[i].status = "已编辑"; }
                else if ("25" == status) { data.obj.records[i].status = "待审核"; }
                var source = data.obj.records[i].source;
                if ("0" == source) { data.obj.records[i].source = "手工录入"; }
                else if ("1" == source) { data.obj.records[i].source = "检索日志"; }
                else if ("2" == source) { data.obj.records[i].source = "外部数据"; }
                else if ("3" == source) { data.obj.records[i].source = "nlp分析"; }
                var unixTimestamp = new Date(data.obj.records[i].crtime);
            }
            setTableData('#tableData', data.obj, columns)
        } else {
            console.log(data.msg);
        }
    })
}

//新增数据提交 
function formSubmitAdd(serviceId,url){
	form.on("submit(submitBtnAdd)",function(data){
		doAddData(curr,defaultPageSize,serviceId,url,data.field);	
		closeLayer();
		return false;
	})
}

//修改数据
function doEditData(serviceId,url,id,data){	
	// 词条状态变为数字
	ajax(serviceId,url+'/modify/'+id,'post',JSON.stringify(data.field)).then(function (data) {
		if(data.success){
			console.log('修改数据',data);
			setListData(curr, defaultPageSize,serviceId,url);
		}else{
			console.log('data.msg',data.msg);
		}		
	});
}

//新建数据时，把设置模块的名称保存
function addModuleName(){
    var moduleName = localStorage.getItem('dictionarySetupModuleName');
    $('[name="fenlei"]').val(moduleName);
}