//实体的基础路径，对应于该实体的controller类对应的访问路径。
const uriRoot="/apply/app";
//列信息-需根据实体的列修改
const columns = [
	{type:'checkbox'}
	,{field:'rowNum', title: '序号',align: 'right',width:60}
	,{field:'name', title: '应用名称',align: 'left',sort: true}
	,{field:'url', title: '应用地址',align: 'left',sort: true}
	,{field:'description', title: '应用介绍',align: 'left',sort: true}
	,{field:'reason', title: '申请理由',align: 'left',sort: true}
	,{field:'statusName', title: '审核状态',align: 'right',sort: true}
	,{field:'appId', title: 'appId',align: 'right',sort: true}
	,{field:'appSecret', title: 'app秘钥',align: 'left',width:120,templet:function (data) {
		if (data.dict.appSecret) {
			return data.dict.appSecret;
		} else {
			return '';
		}
		
	}}
	,{field:'userName', title: '申请人',align: 'left',width:120}
	,{field:'applyTime', title: '申请时间',align: 'left',width:120}
	,{field:'', title: '操作',align: 'right',toolbar: '#operateTemplate',align: 'left',width:180}
];
const columns1 = [
	{type:'checkbox'}
	,{field:'rowNum', title: '序号',align: 'right',width:60}
	,{field:'userName', title: '订阅用户',align: 'left',sort: true,}
	,{field:'apiName', title: '订阅接口',align: 'left',sort: true,toolbar:"#apiname"}
	,{field:'appname', title: '关联应用',align: 'left',sort: true,toolbar:"#appname"}
	,{field:'appId', title: 'appId',align: 'left',sort: true}
	,{field:'', title: 'app秘钥',align: 'right',sort: true,templet:function (data) {
		if (data.dict.appSecret) {
			return data.dict.appSecret;
		} else {
			return '';
		}
	}}
	,{field:'applyTime', title: '申请时间',align: 'right',sort: true}
	,{field:'statusName', title: '审核状态',align: 'left',width:120}
	,{field:'', title: '操作',align: 'right',toolbar: '#operateTemplate',align: 'left'}
];
// 弹出的新增/修改界面的窗体的高度
const height4OpenData='600px';


function groupOption(){
	ajax(service_prefix.gateway,'/apply/app/listAll',"post",{}).then(function (data){
		var str = ''
		if(data.success){
			// $.each(data.obj, function (index, item) { 
			// 	console.log(item)
			// 	str+='<option value="'+item.id+'">'+item.name+'</option>'
			// 	$("#groupOption").append(str)
			// });
				laytpl($('#tableDataTemplate').html()).render(data.obj, function (html) {
					$('#groupOption').html(html);
				});
		}
	})
}


let param4Data={};
let sortProps={"key": "createTime","value": false}
let param4Data1={};
$(function(){
	init();
	groupOption()
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
	form.on("submit(LAY-data1-back-search)",function(data){
		Object.assign(param4Data1,data.field)
		doListok(1);
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
		}else if(layEvent==='apppass'){//审核通过
			apppass(data);
		}else if(layEvent==='appnopass'){//审核不通过
			layui.use('layer', function(){
				var layer = layui.layer;
				layer.open({
					formType: 0,
					value: '',
					type: 1,
					title: '请输入审核不通过原因',
					content: '<textarea class="msg" name="msg" id="msg" value="" placeholder="请输入驳回理由" cols="30" rows="10" style="margin: 20px;border: 1px solid #f2f2f2;"></textarea>'
					,btn: ['确定', '取消']
					,yes: function(index, layero){
						var msg = $('.msg').val();
						ajax(service_prefix.gateway, '/apply/app/reject',"get",{id: data.id,msg:msg}).then(function (data){
							if(data.success){
								doList(1);
								layer.closeAll();
							}else{
								layer.alert(data.msg);
							}
						})
					}
					,btn2: function(index, layero){
					}
				});
			});
		}else{
			alert('未实现：'+layEvent)
		}

	});
	table.on('tool(tableData1)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
		const data = obj.data; //获得当前行数据
		const layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
		// const tr = obj.tr; //获得当前行 tr 的DOM对象

		if(layEvent==='apppass'){//审核通过
			pass(data);
		}else if(layEvent==='appnopass'){//审核不通过
			layui.use('layer', function(){
				var layer = layui.layer;
				layer.open({
					formType: 0,
					value: '',
					type: 1,
					title: '请输入审核不通过原因',
					content: '<textarea class="msg" name="msg" id="msg" value="" placeholder="请输入驳回理由" cols="30" rows="10" style="margin: 20px;border: 1px solid #f2f2f2;"></textarea>'
					,btn: ['确定', '取消']
					,yes: function(index, layero){
						var msg = $('.msg').val();
						ajax(service_prefix.gateway, '/apply/api/reject',"get",{id: data.id,msg:msg}).then(function (data){
							if(data.success){
								doListok(1);
								layer.closeAll();
							}else{
								layer.alert(data.msg);
							}
						})
					}
					,btn2: function(index, layero){
					}
				});
			});
		}else{
			alert('未实现：'+layEvent)
		}

	});
	// form.on("submit(LAY-data-back-add)",function (data) {
	// 	doSave(data);
	// });
	//点击按钮操作
	$('.layui-btn').click(function(){
		var type = $(this).attr('data-type');
		if(type === 'delapp'){
			var data = checkChecked('tableData');
			console.log(data)
			if(data){
				doDeleteByappIds(data);
			}
		}
		if(type){return false;}
	});
	$('.layui-btn').click(function(){
		var type = $(this).attr('data-type');
		if(type === 'del'){
			var data = checkChecked('tableData1');
			if(data){
				doDeleteByapiIds(data);
			}
		}
		if(type){return false;}
	});
}

// function doSave(data) {
// 	let saveUrl = uriRoot;
// 	let type = "post";
// 	if(data.field.id){//有id，为修改
// 		saveUrl += "/" +data.field.id;
// 		type = "put";
// 	}
// 	ajax(service_prefix.gateway, saveUrl,type,JSON.stringify(data.field)).then(function (data){
// 		if(data.success){
// 			doList(1);
// 			layer.closeAll();
// 			layer.msg(data.msg,{
// 					time:3000
// 				},function(){
// 					layer.close();
// 				}
// 			);
// 		}else{
// 			layer.alert(data.msg);
// 		}
// 	})
// }

//条件列表搜索
function doList(current) {
	doListTable(service_prefix.gateway, uriRoot+"/list","post",param4Data,current,sortProps
		,"tableData",columns,"pageData");
}
doListok(1)
//条件列表搜索
function doListok(current) {
	param4Data1.status = ''
	doListTable(service_prefix.gateway, "/apply/api/list","post",param4Data1,current,sortProps
		,"tableData1",columns1,"pageData1");
}

//根据ids批量删除
function doDeleteByappIds(data) {
	let deletesUrl=uriRoot;
	layer.alert("是否确定删除,该操作不可撤回",function () {
		ajax(service_prefix.gateway, "/apply/app","delete",JSON.stringify(data)).then(function (data){
			if(data.success){
				doList(1);
				layer.closeAll();
			}else{
				layer.alert(data.msg);
			}
		})
	})
}
function doDeleteByapiIds(data) {
	let deletesUrl=uriRoot;
	layer.alert("是否确定删除,该操作不可撤回",function () {
		ajax(service_prefix.gateway, "/apply/api","delete",JSON.stringify(data)).then(function (data){
			if(data.success){
				doListok(1);
				layer.closeAll();
			}else{
				layer.alert(data.msg);
			}
		})
	})
}

function apppass(data) {
	ajax(service_prefix.gateway, '/apply/app/approved',"get",{id: data.id}).then(function (data){
		if(data.success){
			doList(1);
			layer.closeAll();
		}else{
			layer.alert(data.msg);
		}
	})
}

function pass(data) {
	var id = []
	id.push(data.id)
	console.log(data)
	ajax(service_prefix.gateway, '/apply/api/approved',"post",JSON.stringify(id)).then(function (data){
		if(data.success){
			doListok(1);
			layer.closeAll();
		}else{
			layer.alert(data.msg);
		}
	})
}

//渲染form中部分组件
setRender();
//加载当前栏目弹框
setOpenLayer();
