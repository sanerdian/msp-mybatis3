/**
 * 设置分页
 */
 function loadPage (page, fn) {
    if (!page.elem) page.elem = "page";
    layui.laypage.render({
        elem: page.elem
        , limit: page.size
        , curr: page.curr
        , count: page.total
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
 * 设置列表数据
 * @param {*} serviceId 微服务id(对应地址前缀)
 * @param {*} url 请求地址
 */
function setListData(curr,defaultPageSize,serviceId,url,entity){
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
		"entity":entity
	};
	ajax(serviceId,url+'/list','post',JSON.stringify(jsonData)).then(function (data){
		if(data.success){			
			setTableData('#tableData',data.obj,columns);
			var pegeObj = {};
            pegeObj.serviceId = serviceId;
            pegeObj.url = url;
            pegeObj.entity = entity;
            pegeObj.elem = "page";
            pegeObj.total = data.obj.total;
            pegeObj.curr = data.obj.current;
            pegeObj.size = defaultPageSize;
			loadPage(pegeObj, setListData);
		}else{
			console.log(data.msg);
		}
	})
}

//条件搜索
function getSearch(serviceId,url){
	form.on("submit(LAY-back-search)",function(data){
		setListData(serviceId,url,data.field);
		return false;
	})
}

//新增数据
function doAddData(serviceId,url,data){	
	ajax(serviceId,url,'post',JSON.stringify(data)).then(function (data) {
		if(data.success){
			setListData(serviceId,url);
		}else{
			console.log(data.msg);
		}		
	});
}

//新增数据提交
function formSubmitAdd(serviceId,url){
	// console.log(serviceId,url);
	form.on("submit(submitBtnAdd)",function(data){
		doAddData(serviceId,url,data.field);	
		setListData(serviceId,url);
		closeLayer();
		return false;
	})
}

//删除数据
function doDeleteById(serviceId,url,id) {
	ajax(serviceId,url+'/'+id,'delete').then(function (data) {
		if(data.success){
			setListData(serviceId,url);
			closeLayer();
		}else{
			console.log(data.msg);
		}
	});

}

//修改数据
function doEditData(serviceId,url,id,data){	
	ajax(serviceId,url+'/'+id,'put',JSON.stringify(data.field)).then(function (data) {
		if(data.success){
			setListData(serviceId,url);
		}else{
			console.log(data.msg);
		}		
	});
}

//修改数据提交
function formSubmitEdit(serviceId,url){
	form.on("submit(submitBtnEdit)",function(data){
		doEditData(serviceId,url,id,data);	
		setListData(serviceId,url);
		closeLayer();
		return false;
	})
}

setLoadCurPath('#editOpenDiv','edit.html');	

//点击按钮
function btnClick(){	
	$('.layui-btn').click(function(){
        var type = $(this).attr('data-type'); 
		var typetxt = $(this).text();
        var	curName = $.trim($('.layui-side .layui-this').text());
        var tit = typetxt+curName;
		if(type == 'add'){			
			setLayer('addDiv',tit,'550px','300px');
			//表单提交
			formSubmitAdd(serviceId,url);			
            var data = {};	
			laytpl($('#addTemplate').html()).render(data, function(html){
				$('#addDiv').html(html);
				setRender();
			});
		}else if(type == 'del'){
			del();
		}
	});
}

//获取删除、修改操作的id
table.on('tool(tableData)', function(obj){
	var data = obj.data;
	var layEvent = obj.event;
	var tr = obj.tr;	
	if (layEvent == 'del') { //删除
		layer.alert("确定删除:[" + data.naturalId + "]", function () {
			doDeleteById(serviceId,url,data.id);
		})
	} else if (layEvent == 'edit') { //编辑
		//表单提交
		formSubmitEdit(serviceId,url);
		id = data.id;
		var typetxt = $(this).text();
        var	curName = $.trim($('.layui-side .layui-this').text());
		var tit = typetxt+curName;
		setLayer('editDiv',tit,'550px','300px');
		laytpl($('#editTemplate').html()).render(data, function(html){
			$('#editDiv').html(html);
			setFormVal('editForm',data);
		});
	}
});

setRender();