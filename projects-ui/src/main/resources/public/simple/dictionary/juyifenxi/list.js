//服务id
var serviceId = "";

//接口地址
// var url = '/dassetmnge/jmetajuyifenxi';
var url = '/dictbbsall/list'
var url_zhutici = '/dict/wordname';
var url_finish = '/dict2/dictpostsentense';
var url_pauseSentenceOther = '/dict2/dictpausesentenceother';

//列表数据
var columns = [
	{type:'checkbox',fixed: 'left'}
	, {type:'numbers',title: '序号',fixed: 'left'}
	// , {field: 'id',title: 'ID',fixed: 'left', hide: true}
	, {field: 'bbsId',title: '帖子ID',fixed: 'left'}
	, {field: 'bbsBt',  title: '标题', sort: true}
	, {field: 'bbsTablename',  title: '车型', sort: true}
	, {field: '', title: '车型指标分类'}
	, {field: '', title: '主题词'}
	, {field: '', title: '形容词'}
	, {field: '', title: '主题词停用词'}
	, {field: '', title: '地域'}
	, {field: 'bbsFtsj', title: '发帖时间'}
	, {field: 'examTime', title: '审核时间'}
	,{field:'', title: '操作', toolbar: '#operateTemplate',fixed: 'right',width:140}
];

//详情页面的列表colums
var detailColumns = [
	{field: 'serialNumber',title: '编号',fixed: 'left'}
	, {field: 'content',  title: '内容', sort: true}
	, {field: 'operateContent',  title: '操作内容', sort: true}
	, {field: 'pausesSentenceTime', title: '断句时间'}
	, {field: 'operatingMode', title: '操作方式'}
	, {field: 'operatingUser', title: '操作用户'}
];

var form = layui.form, layer = layui.layer;
table.on('tool(tableData)', function(obj){

	var data = obj.data;
	var layEvent = obj.event;
	var tr = obj.tr;

	var curName = $('.layui-side .layui-this').text();
	console.log('curName2',curName)
	if (layEvent == 'del') { //删除
		layer.alert("确定删除:[" + data.wordname + "]吗？", function () {
			doDeleteById(serviceId,url,data.metaid);
		})
	} else if (layEvent == 'edit') { //编辑
		//表单提交
		formSubmitEdit(serviceId,url);
		id = data.id;
		var typetxt = $(this).text();
		var	curName = $.trim($('.layui-side .layui-this').text());
		var tit = typetxt+curName;
		setLayer('editDiv',tit,'550px','320px');
		ajax(serviceId,'/shuxingcijibie/sxjbList','get',{"type":"get"}).then(function (returnData){

			data.obj=returnData.obj;
			laytpl($('#editTemplate').html()).render(data, function(html){
				$('#editDiv').html(html);
				$("input[name='category']").val(data.category);
				setRender();
				setFormVal('editForm',data);
			});
		});
		formSubmitEditExam(serviceId,url,id);
	} else if (layEvent == 'detail') { //详情
		var url='/simple/dictionary/juyifenxi/detail.html';
		$("#LAY_app_body div").html("");
		$("#LAY_app_body div").load(url);
	}
});
//点击编辑按钮后弹出编辑框
$("#LAY_app_body").on("click",".edit-btn",function(){
	$(this).parents("div.posiContent-item ").children("div.posiContent").hide();
	$(".edit-form").show();
});
//点击完成按钮隐藏编辑框
$("#LAY_app_body").on("click",".finish-btn",function(){
	$(this).parents("div.posiContent-item").children("div.posiContent ").show();
	$(".edit-form").hide();
});

//添加主题词
var index1,index2;
$("#LAY_app_body").on("click",".ztcAdd",function(ev){
	index1=setLayer2('addDiv',"添加主题词",'550px','650px');
	laytpl($('#addTemplate1').html()).render({}, function(html){
		$('#addDiv').html(html);
	});
	selectZhuTiCi();
	stopBubble(ev)
})
function selectZhuTiCi() {
	var param = {
		"pager": {
			"current": 1,
			"size": 10000,
			"sortProps": [
				{
					"key": "crtime",
					"value": false
				}
			]
		},
		"entity":null
	};

	ajax(serviceId,"/dict/wordname/list","post",JSON.stringify(param)).then(res=>{// JSON.stringify(param) 搜索条件
		debugger;
		if(res.success){
			var length = res.obj.records.length;
			var html = "";
			for(var i = 0; i < length; i++){
				var obj = res.obj.records[i];
				html += "<tr>\n" +
					"           <td>\n" +
					"           		<label>\n" +
					"                   	<input type='checkbox' value='"+ obj.wordname +"' name='zhutici'\n" +
					"                       	style='visibility: visible; width: 15px; height: 15px; left: 15px; margin-top: -10px;background: #3DD4F3; border: #3DD4F3'\n" +
					"                           onchange='checkedBox(this);'/>\n" + obj.wordname +
					"                   </label>\n" +
					"           </td>\n";
				if(i++ < length - 1){
					obj = res.obj.records[i];
					html +="        <td>\n" +
						"           		<label>\n" +
						"                   	<input type='checkbox' value='"+ obj.wordname +"' name='zhutici'\n" +
						"                       	style='visibility: visible; width: 15px; height: 15px; left: 15px; margin-top: -10px;background: #3DD4F3; border: #3DD4F3'\n" +
						"                           onchange='checkedBox(this);'/>\n" + obj.wordname +
						"                   </label>\n" +
						"           </td>\n" +
						"   </tr>"
				}else {
					html +="   </tr>";
				}
			}
			$("#zhuticiHtml").html(html);
		}else{
			layer.alert(res.msg);
		}
	})
	return true;
}
function addZhutici() {
	index2=setLayer2('addZhutici',"手工录入主题词管理",'550px','650px');
	laytpl($('#addTemplate2').html()).render({}, function(html){
		$('#addZhutici').html(html);
	});
}


//添加属性词
var index1,index2;
$("#LAY_app_body").on("click",".subjectWordAttributiveWordAdd",function(ev){
	index1=setLayer2('addDiv',"添加属性词",'550px','650px');
	laytpl($('#addTemplate1').html()).render({}, function(html){
		$('#addDiv').html(html);
	});
	selectSubjectWordAttributiveWord();
	stopBubble(ev)
})
function selectSubjectWordAttributiveWord() {
	var param = {
		"pager": {
			"current": 1,
			"size": 10000,
			"sortProps": [
				{
					"key": "crtime",
					"value": false
				}
			]
		},
		"entity":null
	};

	ajax(serviceId,"/dict/shuxingci/list","post",JSON.stringify(param)).then(res=>{// JSON.stringify(param) 搜索条件
		debugger;
		if(res.success){
			var length = res.obj.records.length;
			var html = "";
			for(var i = 0; i < length; i++){
				var obj = res.obj.records[i];
				html += "<tr>\n" +
					"           <td>\n" +
					"           		<label cssClass=''>\n" +
					"                   	<input type='checkbox' id='weekCheck4' name='weekCheck'\n" +
					"                       	style='visibility: visible; width: 15px; height: 15px; left: 15px; margin-top: -10px;background: #3DD4F3; border: #3DD4F3'\n" +
					"                           onchange=''/>\n" + obj.wordname +
					"                   </label>\n" +
					"           </td>\n";
				if(i++ < length - 1){
					obj = res.obj.records[i];
					html +="        <td>\n" +
						"           		<label cssClass=''>\n" +
						"                   	<input type='checkbox' id='weekCheck4' name='weekCheck'\n" +
						"                       	style='visibility: visible; width: 15px; height: 15px; left: 15px; margin-top: -10px;background: #3DD4F3; border: #3DD4F3'\n" +
						"                           onchange=''/>\n" + obj.wordname +
						"                   </label>\n" +
						"           </td>\n" +
						"   </tr>"
				}else {
					html +="   </tr>";
				}
			}
			$("#zhuticiHtml").html(html);
		}else{
			layer.alert(res.msg);
		}
	})
	return true;
}
function addShuXingCi() {
	index2=setLayer2('addShuXingCi',"手工录入属性词管理",'550px','650px');
	laytpl($('#addTemplate2').html()).render({}, function(html){
		$('#addShuXingCi').html(html);
	});
}
/*
.............................................
*/


function closeSelfLayer1(){
	layer.close(index1);
}
function closeSelfLayer2(){
	layer.close(index2);
}
function setLayer2(obj,tit,width,height){
	setRender();
	var index=layer.open({
		title: tit,
		type: 1,
		skin: 'layui-layer-green',
		btnAlign: 'c',
		area: [width, height],
		content: '<div id="'+obj+'" class="open_layer_form"></div>',
		cancel: function(){
			//$(obj).hide();
		}
	});
	return index;
}

//获取主题词拼音
function getzhuticipinyin() {
	var zhutici = $('#wordname').val();
	if(!zhutici) return false;
	zhutici = zhutici.replace(/\s*/g,"");
	// 获取全写拼音（调用js中的方法）
	var fullChars = pinyin.getFullChars(zhutici);
	//给文本框赋值
	$('#pinyin').val(fullChars);
}

//句义分析列表
function setJuyifenxiListData(serviceId,url,entity){
	var jsonData = {
		"pager": {
			"current": 1,
			"size": 15,
			// "sortProps": [
			// 	{
			// 		"key": "crtime",
			// 		"value": false
			// 	}
			// ]
		},
		"entity":entity
	};
	ajax(serviceId,url+'/listing','post',JSON.stringify(jsonData)).then(function (data){
		if(data.success){
			var records=data.obj.records;
			if(records){
				var listDataJuyifenxi_list=$("div.listDataJuyifenxi_list");
				listDataJuyifenxi_list.html('');

				var listDataJuyifenxi_templ=$("div.listDataJuyifenxi_templ");
				var listDataJuyifenxi_line_str=listDataJuyifenxi_templ.html();
				listDataJuyifenxi_templ.hide();
				for(var i=0;i<records.length;i++){
					var line=$(listDataJuyifenxi_line_str);
					line.find("div.pausesSentenceAdvice").html(records[i].pausesSentenceAdvice);
					listDataJuyifenxi_list.append(line);
				}
				console.log(listDataJuyifenxi_list.html());
			}
		}else{
			console.log(data.msg);
		}
	})
}
//统计的form操作--根据form表单加载chart
form.on("submit(submitBtnAddJuyifenxi)",function (data) {
	debugger;
	// 向数据库写入数据
	ajax(serviceId,url_pauseSentenceOther,'post',JSON.stringify(data.field)).then(function (data) {
		if(data.success){
			setJuyifenxiListData(serviceId,url_pauseSentenceOther);
		}else{
			console.log(data.msg);
		}
	});
	return false;
});



//统计的form操作--根据form表单加载chart
form.on("submit(LAY-back-search)",function (data) {
	Object.assign(param4Chart,data.field);
	loadChart();
	return false;
});
function setListData (curr, defaultPageSize, serviceId, url, entity) {
    if (entity) {
        if (entity.time == "近一天") {
            entity.time = getDay(-1)
        } else if (entity.time == "近三天") {
            entity.time = getDay(-3)
        } else if (entity.time == "近一周") {
            entity.time = getDay(-7)
        }
    }
    var jsonData = {
        "pager": {
            "current": curr,
            "size": defaultPageSize,
            // "sortProps": [
            //     {
            //         "key": "crtime",
            //         "value": false
            //     }
            // ]
        },
        "entity": entity
    };
    ajax(serviceId, url, 'post', JSON.stringify(jsonData)).then(function (data) {

        if (data.success) {
            setTableData('#tableData', data.obj, columns)
            curr = data.obj.current
            defaultPageSize = defaultPageSize
            var pegeObj = {}
            pegeObj.serviceId = serviceId
            pegeObj.url = url
            pegeObj.entity = entity
            pegeObj.elem = "page"
            pegeObj.total = data.obj.total
            pegeObj.curr = data.obj.current
            pegeObj.size = defaultPageSize
            loadPage1(pegeObj, setListData)
        } else {
            // console.log(data.msg);
            setTableData('#tableData', {}, columns)
        }
    })
}

setListData(1,15,serviceId, url);
getSearch(serviceId,url,curr, defaultPageSize);
btnClick();
setOpenLayer();
getpinyin();


formSubmitAdd(serviceId,url);

setDate('#date','date');
setDate('#date2','date');
setDate('#date3','date');
setDate('#date4','date');