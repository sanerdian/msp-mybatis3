//服务id service_prefix.dict
var serviceId = "";

//接口地址
var url = '/dictchangjingci';

//列表数据
var columns = [
	{type:'checkbox',fixed: 'left'}
	,{type:'numbers',title: '序号',fixed: 'left'}
    , { field: 'fenlei', title: '模块名称' }
	, {field: 'changjingci',  title: '场景词'}
	, {field: 'status', title: '词条状态', templet: "#statusTpl"}
	, {field: 'source', title: '来源'}
	, {field: 'crtime', title: '创建时间'}
	, {field: 'cruser', title: '创建人'}
	, {field: 'modifyTime', title: '修改时间'}
	, {field: 'modifyUser', title: '修改人'}
	,{field:'', title: '操作',toolbar: '#operateTemplate',align: 'left',fixed:'right',width: 200}
];
var form = layui.form, layer = layui.layer;
// $(document).ready(function() {
// select下拉框选中触发事件
var jsonData = {
    "pager": {
        "current": 1,
        "size":9999999,
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
		if(type == 'addDict'){
			setLayer('addDiv',tit,'550px','300px');
			//表单提交
			formSubmitEdit(serviceId,url);
			$('#addDiv').html($('#addTemplate').html());
			setRender();
			//添加模块名称
			addModuleName();
		}else if(type == 'delDict'){
			del(2);
		}else if(type == 'examDict'){//审核
			formSubmitEdit(serviceId,url);
			// id = data.id;
			setLayer('examDiv',tit,'550px','300px');
			laytpl($('#examTemplate').html()).render(editdata, function(html){
				$('#examDiv').html(html);
				setFormVal('editForm',editdata);
			});
			formSubmitEditExam(serviceId,url,editdata.id);
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
		layer.alert("确定删除:[" + data.changjingci + "]吗？", function () {
			doDeleteById(serviceId,url,data.jmetachangjingid);
		})
	} else if (layEvent == 'edit')  { //编辑
		//表单提交
		formSubmitEdit(serviceId, url);
		id = data.jmetachangjingid;
		// id = data.id;
		var typetxt = $(this).text();
		var curName = $.trim($('.layui-side .layui-this').text());
		var tit = typetxt + curName;
		setLayer('editDiv', tit, '550px', '300px');
		// formSubmitEditExam(serviceId,url,id);
		ajax(serviceId, '/zhutici/list','post',JSON.stringify(jsonData)).then(function (resData) {
			laytpl($('#editTemplate').html()).render(resData, function (html) {
				$('#editDiv').html(html);
				setFormVal('editForm', data);
				$("select[name='wordname']").val(data.wordname);
				// ajax(serviceId, '/shuxingci/sxcByZtc', 'get', {"wordName": data.wordname}).then(function (data2) {
				// 	var optionHtml = "";
				// 	var dobj = data2.obj;
				// 	for (var i = 0; i < dobj.length; i++) {
				// 		if(data.shuxing==dobj[i]){
				// 			optionHtml += "<option value='" + dobj[i] + "' selected>" + dobj[i] + "</option>";
				// 		}else{
				// 			optionHtml += "<option value='" + dobj[i] + "'>" + dobj[i] + "</option>";
				// 		}
				// 	}
				// 	$("#shuxingKey").html(optionHtml);
					
				// });
                setRender();
				treeData(true);
			});
		})
	}
	else if (layEvent == 'detail') { //详情
		setLayer('detailDiv',tit,'550px','600px');
		if(data.modifyTime == null) {
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