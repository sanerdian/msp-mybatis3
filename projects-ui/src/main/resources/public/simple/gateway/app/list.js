
//实体的基础路径，对应于该实体的controller类对应的访问路径。
const uriRoot="/app";
//列信息-需根据实体的列修改
const columns = [
	{type:'checkbox'}
	,{field:'rowNum', title: '序号',align: 'right',width:60}
	,{field:'name', title: '应用名称',align: 'left',sort: true}
	,{field:'type', title: '应用分类',align: 'left',width:90,templet:function (data) {
		let typeName="其他应用";
		if(data.type=='major'){
			typeName="专业应用";
		}
		return typeName;
	}}
	,{field:'status', title: '状态',align: 'left',sort: true,width:80}
	,{field:'url', title: '应用URL',align: 'left',sort: true}
	,{field:'description', title: '描述',align: 'left',sort: true}
	,{field:'sequcing', title: '排序',align: 'right',sort: true,width:80}
	,{field:'name', title: '身份同步',align: 'left',toolbar:'#operateUserTemplate',width:120}
	,{field:'name', title: '机构同步',align: 'left',toolbar:'#operateGroupTemplate',width:120}
	,{field:'', title: '操作',align: 'right',toolbar: '#operateTemplate',align: 'left',width:160}
];
// 弹出的新增/修改界面的窗体的高度
const height4OpenData='600px';

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
			setOpenData('编辑',data);
		}else if(layEvent === 'detail'){
			setDetailData(data);
		}else if(layEvent==='userSyncSetting'){//跳转至用户同步的设置页面
			userSyncSetting(data);
		}else if(layEvent==='userSyncList'){//跳转至用户同步任务队列
			userSyncList(data);
		}else if(layEvent==='groupSyncSetting'){//跳转至部门同步的设置页面
			groupSyncSetting(data);
		}else if(layEvent==='groupSyncList'){//跳转至部门同步任务队列
			groupSyncList(data);
		}else{
			alert('未实现：'+layEvent)
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
			exportExcel(service_prefix.gateway,uriRoot+"/downloadExcel","app.xls",param4Data,columns);
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
}//根据ids批量删除
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
		})
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
		if(data.logo){//已有logo则显示logo，否则显示logo上传提示信息
			layui.$('#divAppLogo').find('.needUpload').addClass('layui-hide');
			layui.$('#divAppLogo').find('.showView').removeClass('layui-hide').find('img').attr('src', service_prefix.gateway +data.logo);
		}else {
			layui.$('#divAppLogo').find('.needUpload').removeClass('layui-hide');
			layui.$('#divAppLogo').find('.showView').addClass('layui-hide');
		}
		upload.render({
			elem: '#divAppLogo'
			,url: service_prefix.gateway + "/file/upload"
			,done: function(res){
				debugger
				if(res.success){
					layui.$('#divAppLogo').find('.needUpload').addClass('layui-hide');
					layui.$('#divAppLogo').find('.showView').removeClass('layui-hide').find('img').attr('src', service_prefix.gateway +res.obj.uri);
					layui.$('#logo').attr('value',res.obj.uri);
					layer.msg('上传成功!点击“确认”按钮提交表单后logo设置才会生效！');
				}else {
					layer.msg("上传失败！"+res.msg);
				}

			}
		});
	});

	// element.init();
}
function userSyncSetting(data) {
	localStorage.setItem('currentId',data.id);
	localStorage.setItem('currentName',data.name);
	localStorage.setItem('currentDiv','#userSyncSetting');
	setLoadCurPath('#userSyncSettingDiv','userSyncSetting.html');
	$('#userSyncSetting').show();
	$('#list').hide();
}
function userSyncList(data) {
	localStorage.setItem('currentId',data.id);
	localStorage.setItem('currentName',data.name);
	localStorage.setItem('currentDiv','#userSyncList');
	setLoadCurPath('#userSyncListDiv','userSyncList.html');
	$('#userSyncList').show();
	$('#list').hide();
}

function groupSyncSetting(data) {
	localStorage.setItem('currentId',data.id);
	localStorage.setItem('currentName',data.name);
	localStorage.setItem('currentDiv','#groupSyncSetting');
	setLoadCurPath('#groupSyncSettingDiv','groupSyncSetting.html');
	$('#groupSyncSetting').show();
	$('#list').hide();
}
function groupSyncList(data) {
	localStorage.setItem('currentId',data.id);
	localStorage.setItem('currentName',data.name);
	localStorage.setItem('currentDiv','#groupSyncList');
	setLoadCurPath('#groupSyncListDiv','groupSyncList.html');
	$('#groupSyncList').show();
	$('#list').hide();
}
function goBack(){
	let id=localStorage.getItem('currentDiv');
	$('#list').show();
	$(id).hide();
}
//渲染form中部分组件
setRender();
//加载当前栏目弹框
setOpenLayer();
