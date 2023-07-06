
//实体的基础路径，对应于该实体的controller类对应的访问路径。
const uriRoot="/useCase";
//列信息-需根据实体的列修改
const columns = [
	{type:'checkbox'}
	,{field:'rowNum', title: '序号',align: 'right',width:80}
	,{field:'id', title: '实例id',align: 'left',sort: true}
	,{field:'name', title: '实例名称',align: 'left',sort: true}
	,{field:'dict.status', title: '实例状态',align: 'left',sort: true,templet:function (data) {
			return data.dict.status;
		}}
	,{field:'commodity.name', title: '实例规格',align: 'left',sort: true,templet:function (data) {
		if(data.commodity){
			return data.commodity.name;
		}	
		}}
	,{field:'commodity.maxRequest', title: '最大每秒请求数',align: 'left',sort: true,templet:function (data) {
		if(data.commodity){
			return data.commodity.maxRequest;
		}
		}}
	,{field:'commodity.maxConnection', title: '最大连接数',align: 'left',sort: true,templet:function (data) {
		if(data.commodity){
			return data.commodity.maxConnection;
		}
		}}
	,{field:'securityPolicy', title: '安全策略',align: 'left',sort: true}
	,{field:'remark', title: '备注', align: 'left',sort: true}
	,{field:'', title: '操作',fixed: 'right',toolbar: '#operateTemplate',align: 'left',width:120}
];
// 弹出的新增/修改界面的窗体的高度
const height4OpenData='330px';

let param4Data={};
$(function(){
	init();
	//搜索列表默认显示数据
	doList(1);
});
function init() {
	//搜索提交
	form.on("submit(LAY-data-back-search)",function(data){
		Object.assign(param4Data,data.field)
		doList(1);
		return false;
	});

	//监听操作列工具条
	table.on('tool(tableData)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
		const data = obj.data; //获得当前行数据
		const layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
		// const tr = obj.tr; //获得当前行 tr 的DOM对象

		if(layEvent === 'del'){ //删除
			doDeleteByIds([data.id]);
		}else if(layEvent === 'edit'){ //编辑
			setOpenData('编辑',data,1);
		}

	});
	form.on("submit(LAY-data-back-add)",function (data) {
		doSave(data);
	});
	//点击按钮操作
	$('.layui-btn').click(function(){
		var type = $(this).attr('data-type');
		if(type === 'add'){
			setOpenData('新建',{});
		}else if(type === 'del'){
			var data = checkChecked('tableData');
			if(data){
				doDeleteByIds(data);
			}
		}else if(type=='exportExcel'){
			exportExcel(service_prefix.gateway,uriRoot+"/downloadExcel","useCase.xls",param4Data,columns);
		}
		if(type){return false;}
	});
}

function doSave(data) {
	let saveUrl = uriRoot;
	let type = "post";
	if(data.field.id){//有id，为修改
		saveUrl += "/" +data.field.id;
		type = "put";
	}
	ajax(service_prefix.gateway, saveUrl,type,JSON.stringify(data.field)).then(function (data){
		if(data.success){
			doList(1);
			layer.closeAll();
			layer.msg(data.msg,{
					time:3000
				},function(){
					layer.close();
				}
			);
		}else{
			layer.alert(data.msg);
		}
	})
}

//条件列表搜索
function doList(current) {
	doListTable(service_prefix.gateway, uriRoot+"/list","post",param4Data,current,null
		,"tableData",columns,"pageData");
}

//根据ids批量删除
function doDeleteByIds(data) {
	let deletesUrl=uriRoot;
	layer.alert("是否确定删除,该操作不可撤回",function () {
		ajax(service_prefix.gateway, deletesUrl,"delete",JSON.stringify(data)).then(function (data){
			if(data.success){
				doList(1);
				layer.closeAll();
			}else{
				layer.alert(data.msg);
			}
		});
	})
}
//设置弹出层表单数据（新建、修改）
function setOpenData(editType,data) {
	let	curName = $.trim($('.layui-side .layui-this').text());
	let tit = editType + curName;
	setLayer('table_data',tit,'700px',height4OpenData);
	laytpl($('#templateData').html()).render(data, function(html){
		$('#table_data').html(html);
		setFormVal('formEditData',data);
		getSearchSort("/useCaseCommodity/listAll",".commodityOption");
	});

	// element.init();
}

function getSearchSort(url,clasz){
	ajax(service_prefix.gateway, url,"post",{}).then(function (data){
		if(data){
			for(let i=0; i<data.obj.length; i++){
				$(clasz).append("<option value='"+data.obj[i].id+"'>"+data.obj[i].name+"</option>");
			}
			setRender();
		}else {
			layer.alert("获取下拉选失败！");
		}
	});
}
getSearchSort("/useCaseCommodity/listAll",".commodityOption");
//渲染form中部分组件
setRender();
//加载当前栏目弹框
setOpenLayer();
