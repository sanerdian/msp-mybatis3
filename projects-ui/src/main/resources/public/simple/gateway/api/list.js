
let param4Data={};
//实体的基础路径，对应于该实体的controller类对应的访问路径。
const uriRoot="/router";
// const uriRoot="/apply/api";

// 左侧树
gettree()
	function gettree(){
		var treearr = ''
		ajax3(service_prefix.gateway, "/group/tree", "get", {}).then(function (res) {
			if (res.success) {
				treearr = res.obj
				// localStorage.setItem("detailId",treearr[0].id)
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
						,showCheckbox: false  //是否显示复选框
						,id: 'demoId1'
						// ,showLine: false
						,isJump: false //是否允许点击节点时弹出新窗口跳转
						,click: function(obj){
							if(obj.data.type == "group"){
								var data = obj.data;  //获取当前点击的节点数据
								// layer.msg('状态：'+ obj.state + '<br>节点数据：' + JSON.stringify(data));
								param4Data.groupId = obj.data.id
								doList(1)
							}
						}
					});
				});
			} else {
				layer.alert(res.msg);
			}
			
        
		})
	}
//渲染数据到table
let columns = [
	{type:'checkbox'}
	,{field:'rowNum', title: '序号',align: 'right',width:80}
	,{field:'name', title: 'API名称',align: 'left',width:260,sort: true,toolbar: '#opendetail'}/**toolbar: '#opendetail' */
	,{field:'dict.runType', title: '运行状态',align: 'left',sort: true,width:120,templet:function (data) {
			return data.dict.runType;
		}}
	,{field:'reqMethod', title: 'Http请求类型',align: 'left',sort: true,width:120}
	,{field:'reqServiceName', title: '请求服务名',align: 'left',sort: true,width:120}
	,{field:'dict.groupName', title: '分组名称',align: 'left',width:160,templet:function (data) {
			return data.dict.groupName;
		}}
	,{field:'reqUri', title: '请求路径',align: 'left',sort: true,width:160}
	,{field:'', title: '响应提供', align: 'left',width:260,templet:function (data) {
			if(data.mockIsUse){
				// return "mock:"+data.mockResponse;
				return "mock";
			}else {
				return data.servServiceId+data.servUri;
			}
		}}
	,{field:'', title: '操作',fixed: 'right',toolbar: '#operateTemplate',align: 'left',width:90}
];


$(function(){
	init();
	//搜索列表默认显示数据
	doList(1);
});
function init() {
	//搜索
	form.on("submit(LAY-data-back-search)",function(data){
		Object.assign(param4Data,data.field);
		doList(1);
		return false;
	});
	//保存
	form.on("submit(LAY-data-back-add)",function (data) {
		doSave(data);
	});
	// api移动确定
	form.on("submit(LAY-api-back-add)",function (data) {
		console.log("remove",data)
		removeapi(data);
	});
	form.on("submit(LAY-swaggerImport-back-add)",function (data) {
		var datalist = getTableCheck('tableData3');
		var dataarr = []
		$.each(datalist, function (index, item) { 
			 var date = {}
			 date.method = item.method
			 date.uri = item.uri
			 dataarr.push(date)
		});
		data.field.items = dataarr
		doSwaggerImport(data);
	});
	form.verify({
		urlSwagger:function(value){
			if(value.indexOf("/v2/api-docs")<0&&!value.indexOf("/swagger-ui.html")<0){
				return 'swagger网址异常！';
			}
		}
	});
	form.on("select(LAY-data-add-select-mockIsUse)",function (data){
		if(data.value=='1'){
			$('.mockValue').show();
		}else {
			$('.mockValue').hide();
		}
	});
	//监听操作列工具条
	table.on('tool(tableData)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
		let data = obj.data; //获得当前行数据
		let layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）

        if(layEvent === 'del'){ //删除
			doDeleteByIds([data.id]);
		}else if(layEvent === 'edit'){ //编辑
			setOpenData('编辑',data);
		}else if(layEvent === 'detail'){
			// console.log(data)
			setDetailData(data);
		}else if(layEvent==='run'){
			setOpenData4RunApi(data);
		}
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
		}else if(type === 'swaggerImport'){
			setOpenData4SwaggerImport({});
		}else if(type=='exportExcel'){
			exportExcel(service_prefix.gateway, uriRoot+"/downloadExcel","api.xls",param4Data,columns);
		}else if(type=='putapi'){
			var data = checkChecked('tableData');
			console.log(data)
			var dataarrlist = []
			$.each(data, function (index, item) { 
				var dataarr = {}
				dataarr.id = item
				dataarr.groupId = ''
				dataarrlist.push(dataarr)
			});
			console.log(dataarrlist)
			// sessionStorage.setItem("dataarrlist",dataarrlist)
			sessionStorage.setItem("dataarrlist",data)
			setOpenDataputapi(dataarrlist)
		}

		if(type){return false;}
	});
	//加载弹出框
	setOpenLayer();
}

function doSwaggerImport(data) {
    const type = "post";
	const url=uriRoot+"/swaggerImport";
	// const url=uriRoot+"/swaggerImport4Msp";
    ajax(service_prefix.gateway, url,type,JSON.stringify(data.field)).then(function (data){
        if(data.success){
			doList(1);
			layer.closeAll();
        }
		layer.alert(data.msg);
    });
}
function doSave(data) {
    let type = "post";
    let url=uriRoot;
    if(data.field.id){//有id，为修改
        url += "/" +data.field.id;
        type = "put";
    }
	if(data.field.reqUri&&data.field.reqUri.charAt(0)!='/'){
		data.field.reqUri='/'+data.field.reqUri;
	}
	if(data.field.servUri&&data.field.servUri.charAt(0)!='/'){
		data.field.servUri='/'+data.field.servUri;
	}
    ajax(service_prefix.gateway, url,"put",JSON.stringify(data.field)).then(function (data){
        if(data.success){
			layer.closeAll();
			doList(1);
			ajax(service_prefix.gateway, "/router/"+apiId,"get").then(function (data){
				if(data.success){
					getData(data.obj,'#baseInfoTemplate','#baseInfoView');
				}else {
					layer.alert(data.msg);
				}
			});
            layer.msg(data.msg,{
                    time:3000
                },function(){
                    layer.close();
                }
            );
        }else{
			layer.alert(data.msg);
		}
    });
}


//条件列表搜索
function doList(current) {
	var sortProps = [{"key": "createTime","value": false}]
	doListTable(service_prefix.gateway, uriRoot+"/list","post",param4Data,current,sortProps
		,"tableData",columns,"page_tableData");
}

//设置弹出层表单数据（新建、修改）
function setOpenData(editType,data) {
    let	curName = $.trim($('.layui-side .layui-this').text());
    let tit = editType + curName;
    setLayer('table_data',tit,'700px','600px');
    laytpl($('#tableDataTemplate').html()).render(data, function(html){
        $('#table_data').html(html);
        setFormVal('editForm',data);
		if(data.mockIsUse){
			$('.mockValue').show();
		}else {
			$('.mockValue').hide();
		}
		//加载弹出框
		setOpenLayer();

		//所属组织
		getSearchSort("/group/listAll","#addApi_groupOption",data.groupId);
    });

    // element.init();
}
function setOpenDataputapi(data) {
    let tit = "修改api分组";
    setLayer('table_data',tit,'700px','300px');
	laytpl($('#tableDataTemplateput').html()).render(data, function(html){
        $('#table_data').html(html);
        getSearchSort("/group/listAll","#addApi_groupOptions",data.groupId);
        setRender();
    });

    // element.init();
}
function setOpenData4RunApi(data) {
    ajax(service_prefix.gateway, "/router/check/"+data.id,"get").then(function (result){
        if(result.success){
            localStorage.setItem('detailId',data.id);
            setLoadCurPath('#runApi','runApi.html');
            $('#runApi').show();
            setRender();
            $('#list').hide();
        }else {
            layer.alert(result.msg);
        }
    });
}
function backApi() {
	$('#runApi').hide();
	$('#list').show();

}
//设置弹出层表单数据（新建、修改）
function setOpenData4SwaggerImport(data) {
    var tit = "swagger批量导入Api";
    setLayer('table_data',tit,'700px','400px');
    laytpl($('#templateSwaggerImport').html()).render(data, function(html){
        $('#table_data').html(html);
		// setFormVal('editForm',data);
		getjson()
        getSearchSort("/group/listAll","#group4swagger",data.groupId);
        setRender();
    });

    // element.init();
}
//根据ids批量删除
function doDeleteByIds(data) {
	if(!data||data.length==0){
		layer.alert("请选择数据");
		return false;
	}
	layer.alert("是否确定删除,该操作不可撤回",function () {
		ajax(service_prefix.gateway, uriRoot,"delete",JSON.stringify(data)).then(function (data){
            if(data.success){
                doList(1);
                layer.closeAll();
            }else{
				layer.alert(data.msg);
			}
        });
	})
}

//搜索select下拉
let groupOptions=[];
function getSearchSort(url,clasz,selectId){
    if(groupOptions.length==0){
        ajax(service_prefix.gateway,url,"post",{}).then(function (data){
            if(data){
                for(let i=0; i<data.obj.length; i++){
                    $(clasz).append("<option value='"+data.obj[i].id+"'>"+data.obj[i].name+"</option>");
                }
                groupOptions=data.obj;
                setRender();
            }
        });
    }else {
        for(let i=0; i<groupOptions.length; i++){
            if(groupOptions[i].id==selectId){
                $(clasz).append("<option value='"+groupOptions[i].id+"' selected>"+groupOptions[i].name+"</option>");
            }else {
                $(clasz).append("<option value='"+groupOptions[i].id+"'>"+groupOptions[i].name+"</option>");
            }
        }
        setRender();
    }
}
//所属组织
getSearchSort("/group/listAll",".groupOption");

// 批量更新api分组
function removeapi(data) {
	var array = sessionStorage.getItem("dataarrlist")
	var dataarrlist = []
	$.each(array.split(","), function (index, item) { 
		var dataarr = {}
		dataarr.id = item
		dataarr.groupId = data.field.groupId
		dataarr.runType = data.field.runType
		dataarrlist.push(dataarr)
	});
    ajax(service_prefix.gateway, "/router/batch","put",JSON.stringify(dataarrlist)).then(function (data){
        if(data.success){
			layer.closeAll();
			doList(1);
            layer.msg(data.msg,{
                    time:3000
                },function(){
                    layer.close();
                }
            );
        }else{
			layer.alert(data.msg);
		}
    });
}

// swagger导入所有uri是否全选
form.on("radio(LAY-item4-step1-isall-type)",function (data) {
	if(data.value=='all'){
		$('.istable').hide();
	}else {
		$('.istable').show();
	}
});
// swagger导入所有uri列表
function getjson(){
	layui.use('table', function () {
		var table = layui.table;
		table.render({
			elem: '#tableData3'
			, cols: [[
				{ type: 'checkbox' }
				, { field: 'method', width: 100, title: 'Method', sort: true }
				, { field: 'uri', title: '地址' }
				, { field: 'title', title: '说明' }
			]]
			,data:[
				{"method":"post","uri":" ","title":"add"},
				{"method":"post","uri":"/batch","title":"批量添加"},
				{"method":"put","uri":"/batchUpdate","title":"批量选择性更新操作"},
				{"method":"post","uri":"/delList","title":"查询已删除列表"},
				{"method":"post","uri":"/export2","title":"导出"},
				{"method":"get","uri":"/export3","title":"导出"},
				{"method":"post","uri":"/export3","title":"导出"},
				{"method":"post","uri":"/import","title":"导入"},
				{"method":"post","uri":"/listing","title":"查询列表"},
				{"method":"post","uri":"/listing/join","title":"查询列表(带关联表数据)"},
				{"method":"post","uri":"/notpass","title":"批量审核不通过"},
				{"method":"post","uri":"/notpass/{id}","title":"审核不通过"},
				{"method":"post","uri":"/pass","title":"批量审核通过"},
				{"method":"get","uri":"/pass/{id}","title":"审核通过"},
				{"method":"post","uri":"/pub","title":"批量发布"},
				{"method":"get","uri":"/pub/{id}","title":"发布"},
				{"method":"post","uri":"/pubList","title":"查询已发布列表"},
				{"method":"delete","uri":"/real/{ids}/batch","title":"deleteBatchIdsreal"},
				{"method":"delete","uri":"/real/{id}","title":"deleteByIdreal"},
				{"method":"post","uri":"/repub","title":"批量撤销发布"},
				{"method":"get","uri":"/repub/{id}","title":"撤销发布"},
				{"method":"post","uri":"/updateDocStatus","title":"选择性批量更新操作（放入回收站）"},
				{"method":"delete","uri":"/{ids}/batch","title":"批量假删"},
				{"method":"delete","uri":"/{ids}/batchdel/{columnid}","title":"批量假删"},
				{"method":"get","uri":"/{id}","title":"查看"},
				{"method":"put","uri":"/{id}","title":"选择性更新操作"},
				{"method":"delete","uri":"/{id}","title":"假删"},
				{"method":"put","uri":"/{id}/allColumn","title":"执行修改操作"},
				{"method":"get","uri":"/{id}/join","title":"查看(带关联表数据)"}
				]
				,page: true
		})
	})
}
$("input[name='selectedurl']").change(function (){
	var myvalue = $(this).val();
	console.log(myvalue)
	if (myvalue == "all") {
		$(".istable").hide()
	} else {
		$(".istable").show()
	}
})