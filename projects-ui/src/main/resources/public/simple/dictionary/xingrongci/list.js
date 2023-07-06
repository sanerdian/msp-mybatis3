//服务id
var serviceId = "";

//接口地址
var url = '/dictajectrelation';

//列表数据
var columns = [
	{type:'checkbox',fixed: 'left'}
	,{type:'numbers',title: '序号',fixed: 'left'}
	, {field: 'wordname',  title: '形容词'}
	, {field: 'category',  title: '属性级别'}
	, {field: 'source',  title: '词条来源'}
	, {field: 'status',  title: '词条状态'}
	, {field: 'crtime', title: '创建时间'}
	, {field: 'cruser', title: '创建人'}
	/*modify_by
	modify_time*/
	, {field: 'opertime', title: '修改时间'}
	, {field: 'operuser', title: '修改人'}
	,{field:'', title: '操作',toolbar: '#operateTemplate',align: 'left'}
];
var form = layui.form, layer = layui.layer;
// $(document).ready(function() {
// select下拉框选中触发事件


// });
var jsonData = {
	"pager": {
		"current": 1,
		"size":9999,
		"sortProps": [
			{
				"key": "crtime",
				"value": false
			}
		]
	},
	"entity": {}
};

//点击按钮
function btnClick(){
	$('.layui-btn').click(function(){
		var type = $(this).attr('data-type');
		var typetxt = $(this).text();
		var	curName = $.trim($('.layui-side .layui-this').text());
		var tit = typetxt+curName;
		if(type == 'addx'){
			setLayer('addDiv',tit,'550px','320px');
			//表单提交
			formSubmitEdit(serviceId,url);
			$('#addDiv').html($('#addTemplate').html());
			setRender();
			// ajax(serviceId,'/shuxingcijibie/sxjbList','get',{"type":"get"}).then(function (data){
			// 	laytpl($('#addTemplate').html()).render(data.obj, function(html){
			// 		$('#addDiv').html(html);
			// 		setRender();
			// 	});
			// });
			//属性级别
			ajax(serviceId,'/dict/shuxingcijibie/sxjbList','get').then(function (data){
				laytpl($('#addTemplate').html()).render(data.obj, function(html){
					$('#addDiv').html(html);
					setRender();
				});
			});
		}else if(type == 'del'){
			del();
		}else if(type == 'exam'){//审核
	//		formSubmitEdit(serviceId,url);
			// id = data.id;
			console.log(editdata)
			setLayer('examDiv',tit,'550px','320px');
			setRender();
			// ajax(serviceId,'/shuxingcijibie/sxjbList','get',{"type":"get"}).then(function (data){
			// 	editdata.obj=data.obj;
			// 	laytpl($('#examTemplate').html()).render(editdata, function(html){
			// 		$('#examDiv').html(html);
			// 		$("input[name='category']").val(data.category);
			// 		setRender();
			// 		setFormVal('editForm',editdata);
			// 	});
			// });
			formSubmitEditExam(serviceId,url,editdata.id);
		}else if(type == 'out'){//停用
			alert("1111")
		}
	});
}

table.on('tool(tableData)', function(obj){
	var data = obj.data;
	var layEvent = obj.event;
	var tr = obj.tr;

	var curName = $('.layui-side .layui-this').text();
	console.log('curName2',curName)
	if (layEvent == 'del') { //删除
		layer.alert("确定删除:[" + data.wordname + "]吗？", function () {
			doDeleteById(serviceId,url,data.jmetaadjectelationid);
		})
	} else if (layEvent == 'edit') { //编辑
		//表单提交
		formSubmitEdit(serviceId,url);
		id = data.jmetaadjectelationid;
		var typetxt = $(this).text();
		var	curName = $.trim($('.layui-side .layui-this').text());
		var tit = typetxt+curName;
		setLayer('editDiv',tit,'550px','320px');
		// ajax(serviceId,'/shuxingcijibie/sxjbList','get',{"type":"get"}).then(function (returnData){
		ajax(serviceId,'/dictajectrelation/list','post',JSON.stringify(jsonData)).then(function (returnData){
			data.obj=returnData.obj;
			laytpl($('#editTemplate').html()).render(data, function(html){
				$('#editDiv').html(html);
				// $("input[name='category']").val(data.category);
				setRender();
				setFormVal('editForm',data);
			});
		});
		// formSubmitEditExam(serviceId,url,id);
	} else if (layEvent == 'detail') { //详情
		setLayer('detailDiv',tit,'550px','500px');
		if(data.modifyTime == null){
			data.modifyTime = ''
		}
		getData(data,'#detailTemplate','#detailDiv');
	}
});



setListData(curr, defaultPageSize, serviceId, url);
getSearch(serviceId,url,curr, defaultPageSize);
btnClick();

setOpenLayer();
// getpinyin();

formSubmitAdd(serviceId,url);