//服务id
var serviceId = "";

//接口地址
var url = '/dictshuxing'; 

//列表数据
var columns = [
	{type:'checkbox',fixed: 'left'}
	,{type:'numbers',title: '序号',fixed: 'left'}
    , { field: 'fenlei', title: '模块名称' }
	, {field: 'wordname',  title: '主题词'}
	, {field: 'shuxingci',  title: '属性词'}
	, {field: 'relationword', title: '属性词相关词'}
	, {field: 'tongyici', title: '属性词同义词'}
	, {field: 'pinyin', title: '属性拼音'}
	, {field: 'tongyinci', title: '属性同音词'}
/*	, {field: 'rank', title: '级别'}*/
	, {field: 'status', title: '词条状态', templet: "#statusTpl"}
	, {field: 'crtime', title: '创建时间'}
	, {field: 'cruser', title: '创建人'}
	/*modify_by
	modify_time*/
	, {field: 'opertime', title: '修改时间'}
	, {field: 'operuser', title: '修改人'}
	,{field:'', title: '操作',toolbar: '#operateTemplate',fixed:'right',width:180}
];

var form = layui.form, layer = layui.layer;
// $(document).ready(function() {
// select下拉框选中触发事件



setListData(curr, defaultPageSize, serviceId, url);
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

var selectThemeName = [];
//点击按钮
function btnClick(){
	$('.layui-btn').click(function(){
		var type = $(this).attr('data-type');
		var typetxt = $(this).text();
		var	curName = $.trim($('.layui-side .layui-this').text());
		var tit = typetxt+curName;
		if(type == 'addDict'){
			setLayer('addDiv',tit,'550px','460px');
			//表单提交
			// formSubmitEdit(serviceId,url);
			$('#addDiv').html($('#addTemplate').html());
			//获取主题词数据
			ajax(serviceId,'/zhutici/list','post',JSON.stringify(jsonData)).then(function (data){
				laytpl($('#addTemplate').html()).render(data.obj, function(html){
					$('#addDiv').html(html);
					setRender();
                    //添加模块名称
                    addModuleName();
					if (data.success) {
						// console.log('data', data);
						var wordName = data.obj.records;
						// console.log('data.obj.records', wordName);
						var list = [];
						for (var i = 0; i < wordName.length; i++) {
							var obj = {};
							obj.name = wordName[i].wordname;
							obj.value = wordName[i].wordname;
							obj.id = wordName[i].jmetasearwordid;
							list.push(obj);
						}
						// console.log('list',list)
						var themeName = xmSelect.render({
							el: '#themeWord',
							radio: true,
							paging: true,
							pageSize: 5,
							filterable: true,
							data: list,
                            name: 'wordname',//修改name
                            on: function (data) {
                                //arr:  当前多选已选中的数据
                                var arr = data.arr;
								// console.log('arr',arr);
								if(arr.length == 0){	
									$('[name="wordid"]').val('');							
								}else{
									var id = arr[0].id;
									$('[name="wordid"]').val(id);
								}
                            }
						})
						
					}
				});
			});
            //表单提交
            formSubmitAdd(serviceId, url);
		}else if(type == 'delDict'){
            del(1);
		}else if(type == 'examDict'){//审核
			setLayer('examDiv',tit,'550px','650px');
			formSubmitEdit(serviceId,url);
			ajax(serviceId,'/zhutici/list','post',JSON.stringify(jsonData)).then(function (data){
				editdata.obj=data.obj;
				laytpl($('#examTemplate').html()).render(editdata, function(html){
					$('#examDiv').html(html);
					$("#wordnameKey").val(editdata.wordname);
					setRender();
					setFormVal('editForm',editdata);
				});
				formSubmitEditExam(serviceId,url,editdata.id);
			});
		}
	});
}

//列表中删除、编辑、详情
table.on('tool(tableData)', function(obj){
	var data = obj.data;
	var layEvent = obj.event;
	var tr = obj.tr;

	var curName = $('.layui-side .layui-this').text();
	console.log('curName2',curName)
	if (layEvent == 'del') { //删除
		layer.alert("确定删除:[" + data.wordname + "]吗？", function () {
			doDeleteById(serviceId,url,data.jmetashuxingciid);
		})
	} else if (layEvent == 'edit') {//编辑
		//表单提交
		formSubmitEdit(serviceId, url);
		// id = data.id;
		id = data.jmetashuxingciid;
		var typetxt = $(this).text();
		var curName = $.trim($('.layui-side .layui-this').text());
		var tit = typetxt + curName;
		setLayer('editDiv', tit, '550px','650px');
		// formSubmitEditExam(serviceId,url,id);		
		//获取主题词数据
		ajax(serviceId,'/zhutici/list','post',JSON.stringify(jsonData)).then(function (resData) {
			laytpl($('#editTemplate').html()).render(resData.obj, function (html) {
				$('#editDiv').html(html);
				setFormVal('editForm', data);
				// console.log('edit',data);
				var themeWord = data.wordname;
				if (resData.success) {
					// console.log('data', data);
					var wordName = resData.obj.records;
					var list = []
					for (var i = 0; i < wordName.length; i++) {
						var obj = {}
						obj.name = wordName[i].wordname;
						obj.value = wordName[i].wordname;
						list.push(obj);
					}
					console.log('list',list);
					var themeName = xmSelect.render({
						el: '#themeWord',
						radio: true,
						paging: true,
						pageSize: 5,
						filterable: true,
						data: list,
						name: 'wordname'//修改name
					})
					themeName.setValue([
						{name: themeWord, value: themeWord}
					]);
				}
				setRender();
				// treeData(true);
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