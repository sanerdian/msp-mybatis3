
//实体的基础路径，对应于该实体的controller类对应的访问路径。
const uriRoot="/alarmLog";
//列信息-需根据实体的列修改
const columns = [
	{type:'checkbox'}
	,{field:'rowNum', title: '序号',align: 'right',width:80}
	,{field:'time', title: '警报时间',align: 'left',sort: true,width:200}
	,{field:'uri', title: 'uri',align: 'left',sort: true}
	,{field:'dict.type', title: '警报类型',align: 'left',sort: true,width:120,templet:function (item) {
			return item.dict.type;
		}}
	,{field:'value', title: '警报值', align: 'right',sort: true,width:120}
	,{field:'remark', title: '备注', align: 'left',sort: true}
	,{field:'', title: '操作',fixed: 'right',toolbar: '#operateTemplate',align: 'left',width:80}
];

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
		}
	});

	//点击按钮操作
	$('.layui-btn').click(function(){
		let type = $(this).attr('data-type');
		if(type === 'del'){
			var data = checkChecked('tableData');
			if(data){
				doDeleteByIds(data);
			}
		}else if(type=='exportExcel'){
			exportExcel(service_prefix.gateway,uriRoot+"/downloadExcel","alarmLog.xls",param4Data,columns);
        }
		if(type){return false;}
	});
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
			}else {
				layer.alert(data.msg);
			}
		})
	})
}

//渲染form中部分组件
setRender();
//加载当前栏目弹框
setOpenLayer();
