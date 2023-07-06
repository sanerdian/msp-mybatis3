//服务id
var serviceId = service_prefix.dict;

//接口地址
var url = '/fuci';

//列表数据
var columns = [
	{type:'checkbox',fixed: 'left'}
	,{type:'numbers',title: '序号',fixed: 'left'}
	, {field: 'fuci',  title: '副词'}
	, {field: 'source',  title: '词条来源'}
	, {field: 'docstatus',  title: '词条状态'}
	, {field: 'createTime', title: '创建时间'}
	/*modify_by
	modify_time*/
	, {field: 'modifyTime', title: '修改时间'}
	, {field: 'modifyUser', title: '修改人'}
	,{field:'', title: '操作',toolbar: '#operateTemplate',align: 'left'}
];
var form = layui.form, layer = layui.layer;
// $(document).ready(function() {
// select下拉框选中触发事件
form.on("select(timeSlot)", function (data) {
	console.log(data.vlaue); // 获取选中的值
	var times;
	if(data.value=="近一天"){
		times=getDay(-1)
	}
	else if(data.value=="近三天"){
		times=getDay(-3)
	}
	else if(data.value=="近一周"){
		times=timesgetDay(-7)
	}
	console.log(times)
	setListData(serviceId,url,{time:times});

	form.render('select')
});

form.on("select(status)", function (data) {
	console.log(data.vlaue); // 获取选中的值
	if(data.value=="已生效"){
		setListData(serviceId,url,{status:"22"});
	}
	else if(data.value=="未生效"){
		setListData(serviceId,url,{status:"21"});
	}
	else if(data.value=="停用"){
		setListData(serviceId,url,{status:"23"});
	}
	else if(data.value=="已编辑"){
		setListData(serviceId,url,{status:"24"});
	}
	else if(data.value=="待审核"){
		setListData(serviceId,url,{status:"25"});
	}else {
		setListData(serviceId,url,{status:""});
	}
	form.render('select');
});

form.on("select(ctly)", function (data) {
	console.log(data.vlaue); // 获取选中的值
	if(data.value=="手工录入"){
		setListData(serviceId,url,{source:"手工录入"});
	}
	else if(data.value=="检索日志"){
		setListData(serviceId,url,{source:"检索日志"});
	}
	else if(data.value=="外部数据"){
		setListData(serviceId,url,{source:"外部数据"});
	}
	else if(data.value=="ckm分析"){
		setListData(serviceId,url,{source:"ckm分析"});
	}else {
		setListData(serviceId,url,{source:""});
	}
	form.render('select');
});
console.log(1);
setListData(serviceId,url);
getSearch(serviceId,url);
btnClick();
setOpenLayer();