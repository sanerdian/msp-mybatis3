
//实体的基础路径，对应于该实体的controller类对应的访问路径。
const uriRoot="/msp/user";
//列信息-需根据实体的列修改
const columns = [
	{type:'checkbox',fixed:"left"}
	,{field:'rowNum',title:'序号',align:'center',width:80,fixed:"left"}
	,{field:'name',title:'用户名',align:'left',sort:true,fixed:"left"}
	,{field:'trueName',title:'真实姓名',align:'left',sort:true}
	,{field:'mobile',title:'手机号码',align:'center',sort:true}
	,{field:'tellPhone',title:'座机',align:'center',templet: function(d){
        return (d.tellPhone == "NULL"?"":d.tellPhone)
      }}
	,{field:'email',title:'电子邮箱',align:'left',sort:true}
	//,{field:'groupName', title: '所属组织',align:'left'}
	,{field:'crUser',title: '创建人',align: 'center'}
	,{field:'createTime', title: '创建时间',align: 'center'}
	,{field:'locktimes', title: '连续输错密码次数',align: 'center'}
	//,{field:'modifyUser', title: '操作人',align: 'left'}
	//,{field:'modifyTime', title: '操作时间',align: 'center'}
	,{field:'', title: '操作',toolbar: '#permissionTemplate',align: 'center',fixed:"right"}
	//,{field:'', title: '操作',align: 'right',toolbar: '#operateTemplate',align: 'left',width:120,fixed:"right"}
];
// 弹出的新增/修改界面的窗体的高度
const height4OpenData='600px';

let param4Data={sign:0};
let sortList = {key: "createTime", value: false};
$(function(){
	//渲染表格数据
	doList(1);

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
		}else if(layEvent==='permission'){//应用授权
			setLimit(data,0);
		}else if(layEvent==="unlock"){//解锁
			unlock(data);
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
		/*if(type === 'add'){
			setOpenData('新建',{});
		}else if(type === 'del'){
			let data = checkChecked('tableData');
			if(data){
				doDeleteByIds(data);
			}
		}else */if(type=='exportExcel'){
			exportExcel(service_prefix.gateway,uriRoot+"/downloadExcel","app.xls",param4Data,columns);
		}/*else if(type=='resetPass'){
            let data = checkChecked('tableData');
            if(data){
                setOpenData4ResetPass();
            }
        }*/
		if(type){return false;}
	});
	/*form.on("submit(LAY-data-reset-pass)",function (data) {
        doResetPass(data);
    })*/
	gettree();
});
function gettree(){
	var treearr = ''
	ajax3(service_prefix.gateway, "/msp/groupinfo/tree", "get", {}).then(function (res) {
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
							let data = obj.data;  //获取当前点击的节点数据
							setLimit(data,2);
						}
					}
				});
			});
		} else {
			layer.alert(res.msg);
		}


	})
}
/*function doResetPass(data) {
    if(data.field.passWord!=data.field.passWord2){
        layer.msg("密码与确认密码不一致！",{
                time:3000
            },function(){
                layer.close();
            }
        );
        return false;
    }
    let body={
        ids:checkChecked('tableData'),
        passWord:data.field.passWord
    }
    let resetPassUrl=uriRoot+"/resetPassword"
    ajax(service_prefix.gateway, resetPassUrl,"post",JSON.stringify(body)).then(function (data){
        if(data.success){
            // doList(1);
            layer.closeAll();
            layer.msg("密码重置成功",{
                    time:3000
                },function(){
                    layer.close();
                }
            );
        }else{
            layer.alert(data.msg);
        }
    })
}*/
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
	doListTable(service_prefix.gateway, uriRoot+"/list","post",param4Data,current,sortList,"tableData",columns,"pageData");
}
//设置弹出层表单数据（新建、修改）
function setOpenData(editType,data) {
	let	curName = $.trim($('.layui-side .layui-this').text());
	let tit = editType + curName;
	setLayer('table_data',tit,'700px',height4OpenData);
	laytpl($('#templateData').html()).render(data, function(html){
		$('#table_data').html(html);
		if(editType=='修改'){
			data.editType=editType;
		}
		setFormVal('formEditData',data);

	});

	// element.init();
}
function setOpenData4ResetPass(){
    let tit = '重置密码';
    setLayer('table_data',tit,'700px','300px');
    laytpl($('#templateResetPass').html()).render({}, function(html){
        $('#table_data').html(html);
        setFormVal('formResetPass',{});

    });
}
function setLimit(data,type) {

	let param={
		id:data.id,
		type:type,
	};
	let title;
	if(type==0){
		title='用户';
		param.label="用户:"+data.trueName+"【登录名:"+data.name+",id:"+data.id+"】";
	}else if(type==2){
		title='部门';
		param.label="部门:"+data.title+"【id:"+data.id+"】";
	}
	layer.open({
		type: 1,
		area: ["800px", "680px"],
		title: '应用权限:'+title,
		maxmin: false,
		content: '<div id="openDiv"></div>'
	});
	lodTpl("setLimitTpl","openDiv",param);
	getRightInfo(data.id,type);
}
function unlock(data){
	var index = layer.confirm('确定要解锁该用户吗?', {icon: 3, title:'解锁'}, function(index){
		//do something
		$.ajax({
			url:'/apiGateway/msp/user/unlock',
			type: 'get',
			data:{
				"id":data.id
			},
			dataType: 'json',
			contentType: 'application/json',
			success: function (data) {
				layer.msg('解锁'+data.msg);
				layer.close(index);
				doList(1);
			}
		})
		//layer.close(index);
	});
}
//渲染form中部分组件
setRender();
//加载当前栏目弹框
setOpenLayer();
function getRightInfo (id,type) {
	// var uri = type==0?"userPermission":type==1?"rolePermission":"groupPermission";
	ajax(service_prefix.gateway,"/msp/permission/allStr","post",JSON.stringify({ownerId:id,ownerType:type})).then(function (data) {
		getApiAppDate(data.obj);
	})
}

layui.form.on('submit(addRightInfo)', function(data){
	let arr=[];
	$("#temp input:checkbox:checked").each(function(){
		arr.push({
			permission:$(this).val(),
			ownerId:data.field.id,
			ownerType:data.field.type
		});
	});
	if(arr.length>0){
		ajax(service_prefix.gateway,"/msp/permission/setPermission","post",JSON.stringify(arr)).then(function (res) {
			layer.closeAll();
		});
	}else {
		ajax(service_prefix.gateway,"/msp/permission/deleteByOwner/"+data.field.id,"delete",{}).then(function (res) {
			layer.closeAll();
		});
	}

	return false;
});
function getApiAppDate(data){
	ajax(service_prefix.gateway,"/app/listAll","post").then(function (res) {
		layui.table.render({
			elem: '#apiAppRightInfo'
			,data:res.obj
			,cellMinWidth: 80
			,limit:res.obj.length
			,cols: [[
				{field:'name', width:520, title: '应用',align:'center'}
				,{field:'read',title: '可读',align:"center",templet:'<div><input type="checkbox" name="limitBox" value="apiApp:read:{{d.id}}" lay-skin="primary"></div>'}
				// ,{field:'write',title: '可写',align:"center",templet:'<div><input type="checkbox" name="limitBox" value="apiApp:write:{{d.id}}" lay-skin="primary"></div>'}
			]]
			,done:function(){
				data.forEach(function(item){
					$(".apiAppRightInfo input[value='"+item+"'").prop("checked",true);
				});
				layui.form.render();
			}
		});
	})
		.catch(err => {
			console.log(err)
		})
}
