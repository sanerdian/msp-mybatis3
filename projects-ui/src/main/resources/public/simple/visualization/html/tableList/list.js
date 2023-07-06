var defaultPageSize = 5;
var curr = 1;
/**
 * 设置列表数据
 * @param {*} serviceId 微服务id(对应地址前缀)
 * @param {*} url 请求地址
 */
function setListData(serviceId,url,curr,defaultPageSize,entity){
	var columns = [
		{type: 'checkbox', fixed: 'left'}
		, {type: 'numbers', title: '序号', fixed: 'left'}
		// , {field: 'name', title: '用户名', width: 140, fixed: 'left', templet: function(d){
		//     return '<div data-id="'+ d.id+'">'+d.name+'</div>'
		// }}
		, {field: 'computerCode', title: '电脑编号', width: 200}
		, {field: 'userPerson', title: '员工名称'}
		, {field: 'startTime', title: '开始使用时间'}
		, {fixed: 'right', title:'操作', toolbar: '#tableOperateBar'}
	];

	var jsonData = {
		"pager": {
		  "current": curr,
		  "size": defaultPageSize
		},
		"entity":entity
	};
	ajax(serviceId,url+'/listing','post',JSON.stringify(jsonData)).then(function (data){
		if(data.success){	
			console.log('obj',data.obj)		
			setTableData('#tableData',data.obj,columns);
			page(data.obj.total, curr, defaultPageSize, setListData, 'tablePage', entity);
		}else{
			console.log(data.msg);
		}
	})
}

//条件搜索
function getSearch(serviceId,url,curr,defaultPageSize){
	form.on("submit(LAY-back-search)",function(data){
		setListData(serviceId,url,curr,defaultPageSize,data.field)
		return false;
	})
}

//新增数据
function doAddData(curr,defaultPageSize,serviceId,url,data){	
	ajax(serviceId,url+"/add",'post',JSON.stringify(data)).then(function (data) {
		if(data.success){
			setListData(curr,defaultPageSize,serviceId,url);
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
	ajax(serviceId,url+'/delbyid/'+id,'post').then(function (data) {
		if(data.success){
			setListData(curr, defaultPageSize,serviceId,url);
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
		// setListData(serviceId,url);
		closeLayer();
		return false;
	})
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