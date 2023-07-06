//服务id
var serviceId = service_prefix.dict;

//接口地址
var url = '/taskmanager'; 

//列表数据
var columns = [
	{type:'checkbox',fixed: 'left'}
	,{type:'numbers',title: '序号',fixed: 'left'}
	, {field: 'wordname',  title: '任务名称'}
	, {field: 'shuxingci',  title: '任务类型'}
	, {field: 'sx_relationword', title: '任务代码位置'}
	, {field: 'tongyici', title: '任务结束表'}
	, {field: 'pinyin', title: '任务开始时间'}
	, {field: 'tongyinci', title: '任务结束时间'}
/*	, {field: 'rank', title: '级别'}*/
	, {field: 'status', title: '任务执行人'}
    , {field: '', title: '任务执行类型'}
	, {field: 'createTime', title: '创建时间'}
	, {field: 'crUser', title: '创建人'}
	, {field: 'modifyTime', title: '修改时间'}
	, {field: 'modifyUser', title: '修改人'}
	,{field:'', title: '操作',toolbar: '#operateTemplate',fixed:'right',width:120}
];

var form = layui.form, layer = layui.layer;


setListData(serviceId,url);
getSearch(serviceId,url,curr, defaultPageSize);
// btnClick();
btnClick('550px','500px');
setOpenLayer();
getpinyin();

$(document).on('click',"#huoqupinyin",function(){
	layer.msg("按钮点击");
});

function getpinyin(){
	console.log("shuxingci", shuxingci);
	var shuxingci = $('#shuxingci').val();
	if(!shuxingci) return false;
	shuxingci = shuxingci.replace(/\s*/g,"");

	console.log("shuxingci", shuxingci);

// 获取全写拼音（调用js中的方法）
	var fullChars = pinyin.getFullChars(shuxingci);
	// alert("s属性词 pingyin="+fullChars)
// 		 console.log("shuxingpinyin", fullChars);

//给文本框赋值
	$('#shuxingpinyin').val(fullChars);
}
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
		if(type == 'add'){
			setLayer('addDiv',tit,'550px','650px');
			//表单提交
			formSubmitEdit(serviceId,url);
			$('#addDiv').html($('#addTemplate').html());
			ajax(serviceId,'/zhutici/list','POST',JSON.stringify(jsonData)).then(function (data){
				laytpl($('#addTemplate').html()).render(data.obj, function(html){
					$('#addDiv').html(html);
					setRender();
				});
			});
		}else if(type == 'del'){
			del();
		}else if(type == 'exam'){//审核
			setLayer('examDiv',tit,'550px','650px');
			formSubmitEdit(serviceId,url);
			ajax(serviceId,'/zhutici/list','POST',JSON.stringify(jsonData)).then(function (data){
				editdata.obj=data.obj;
				laytpl($('#examTemplate').html()).render(editdata, function(html){
					$('#examDiv').html(html);
					$("#wordnameKey").val(editdata.wordname);
					setRender();
					setFormVal('editForm',editdata);
				});
				formSubmitEditExam(serviceId,url,editdata.id);
			});
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
			doDeleteById(serviceId,url,data.id);
		})
	} else if (layEvent == 'edit') {//编辑
		//表单提交
		formSubmitEdit(serviceId, url);
		id = data.id;
		var typetxt = $(this).text();
		var curName = $.trim($('.layui-side .layui-this').text());
		var tit = typetxt + curName;
		setLayer('editDiv', tit, '550px','650px');
		// formSubmitEditExam(serviceId,url,id);
		ajax(serviceId,'/zhutici/list','POST',JSON.stringify(jsonData)).then(function (resData) {
			laytpl($('#editTemplate').html()).render(resData.obj, function (html) {
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
	} else if (layEvent == 'detail') { //详情
		setLayer('detailDiv',tit,'550px','700px');
		if(data.modifyTime==null){
			data.modifyTime='';
		};
		getData(data,'#detailTemplate','#detailDiv');
	}
});

formSubmitAdd(serviceId,url);