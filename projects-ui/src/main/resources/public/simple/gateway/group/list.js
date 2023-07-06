
let uriRoot = "/group";
let param4Data={};
//渲染数据到table
let columns = [
	{type:'checkbox'}
	,{field:'rowNum', title: '序号',align: 'right',width:80}
	,{field:'name', title: '分组名称',align: 'left',sort: true}
	,{field:'caseId', title: '实例id',align: 'left',sort: true}
	,{field:'remark', title: '描述', align: 'left',sort: true}
	,{field:'dict.apiCount',title:'Api数量',align:'left',sort:true,templet:function (item) {
			return item.dict.apiCount;
		}}
	,{field:'', title: '操作',fixed: 'right',toolbar: '#operateTemplate',align: 'left',width:160}
];
var treearr = ''
// 左侧树
let groupId = '';
	gettree()
	function gettree(){
		ajax3(service_prefix.gateway, "/group/tree", "get", {}).then(function (res) {
			treearr = res.obj
			console.log(treearr)
			layui.use(['transfer','tree','layer', 'util'], function(){
            var tree = layui.tree;
            var $ = layui.$
            ,transfer = layui.transfer
            ,layer = layui.layer
            ,util = layui.util;
            //渲染
            tree.render({
                elem: '#treeDemo1'
                ,data: treearr
                ,showCheckbox: true  //是否显示复选框
				,id: 'demoId'
				// ,showLine: false
				,onlyIconControl: true
                ,isJump: false //是否允许点击节点时弹出新窗口跳转
                ,click: function(obj){
					if(obj.data.type == "group"){
						if(obj.data.children.length<1){
							if(obj.data.parentId == ''){
								var data = obj.data;  //获取当前点击的节点数据
								// localStorage.setItem("groupId",data.id)
								loadDetail1(data.id,'')
							}else{
								var parentname = obj.elem[0].parentNode.parentNode.innerText.split("\n")[0]
								console.log(parentname)
								loadDetail1(obj.data.id,parentname)
							}
						}
					}
					
                }
			});
        });
		})
	}
	// loadDetail1(groupId)
	function loadDetail1(groupId,parentname){
		ajax(service_prefix.gateway, "/group/"+groupId,"get").then(function (data){
			// getData(data.obj,'#baseInfoTemplate','#baseInfoView');
			if(data.success){
				data.obj.parentname = parentname
				getData(data.obj,'#baseInfoTemplate','#baseInfoView');
				detailObject=data.obj;
			}else {
				layer.alert(data.msg)
			}
		})
	}

	layui.use('upload', function () {
		var $ = layui.jquery
			, upload = layui.upload;
			var index = layer.load(); 
		upload2 = upload.render({
			elem: '#importexcel'
			, url: service_prefix.gateway+'/group/importExcel'
			, accept: 'file' //普通文件
			,exts: 'xlsx|xls|csv'
			,choose: function(data){
				layer.load()
			}
			, before: function (obj) {
				
			}
			, done: function (res) {
				layer.close(layer.load()); 
				gettree()
			}
		});
	});

$(function(){
	//搜索列表默认显示数据
	doList(1);
	//搜索提交
	form.on("submit(LAY-back-search)",function(data){
		Object.assign(param4Data,data.field)
		doList(1);
		return false;
	});

	//监听操作列工具条
	table.on('tool(tabData)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
		const data = obj.data; //获得当前行数据
		const layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
		// const tr = obj.tr; //获得当前行 tr 的DOM对象
		if(layEvent === 'del'){ //删除
			doDeleteByIds([data.id]);
		}else if(layEvent === 'edit'){ //编辑
			data.editType = 1;
			setOpenData('编辑',data,1);
		}else if(layEvent === 'detail'){
			setDetailData(data);
		}else if(layEvent === 'excel'){
			upExcel(data);
		}

	});
	
	form.on("submit(LAY-api-back-add)",function (data) {
		doSave(data);
	});
	//点击按钮操作
	$('.layui-btn').click(function(){
		var type = $(this).attr('data-type');
		if(type === 'add'){
			gettreess()
			setOpenData('新建',{});
		}else if(type === 'del'){
			// doDeleteByIds(ids);
		// }else if(type=='exportExcel'){
		// 	exportExcel(service_prefix.gateway, uriRoot+"/downloadExcel","group.xls",param4Data,columns);
		}
		if(type){return false;}
	});
});
function doSave(data) {
	let thisurl = uriRoot;
	let type = "post";
	data.field.parentId = ''
	debugger
	//按钮事件
	util.event('lay-demo', {
			getChecked: function(othis){
				var checkedData = tree.getChecked('demoId33'); //获取选中节点的数据
				function getlast (arrayselect) {
					var newArr = []
					var flat = function (item) {
						for (var i = 0; i < item.length; i++) {
							if (Array.isArray(item[i].children)) {
								if (item[i].children.length>0) {
									flat(item[i].children)
								} else {
									newArr.push(item[i])
								}
							} else {
								newArr.push(item[i])
							}
						}
					}
					flat(arrayselect)
					return newArr
				}
				data.field.parentId = getlast (checkedData)[0].id
				console.log(data)
				if(data.field.id){//有id，为修改
					thisurl += "/" +data.field.id;
					type = "put";
				}
				ajax(service_prefix.gateway, thisurl,type,JSON.stringify(data.field)).then(function (data){
					if(data.success){
						gettree()
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
		})
	
	
	
}
//设置弹出层表单数据（新建、修改）
function setOpenData(editType,data) {
	let	curName = $.trim($('.layui-side .layui-this').text());
	let tit = editType + curName;
	setLayer('table_data',tit,'700px','300px');
	laytpl($('#apiParamTemplate').html()).render(data, function(html){
		$('#table_data').html(html);
		setFormVal('editForm',data);
	});

	// element.init();
}

//条件列表搜索
function doList(current) {
	doListTable(service_prefix.gateway, uriRoot+"/list","post",param4Data,current,null
		,"tabData",columns,"pageData");
}

//根据ids批量删除
function doDeleteByIds(data) {
	console.log(service_prefix.gateway, uriRoot,"delete",JSON.stringify(data))
	if(!data||data.length<1){
		layer.alert("请选择要删除的数据！");
		return;
	}
	layer.alert("是否确定删除,该操作不可撤回",function () {
		ajax(service_prefix.gateway, uriRoot,"delete",JSON.stringify(data)).then(function (data){
			if(data.success){
				gettree();
				layer.closeAll();
			}else{
				layer.alert(data.msg);
			}
		})
	})
}

//加载当前栏目弹框
setOpenLayer();
