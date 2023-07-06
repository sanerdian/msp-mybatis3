$(function(){
	//初始化事件
	//一级导航切换
	$('.prohLeft .layui-nav-item').on('click', function(){
		if ($(this).hasClass('layui-this')) {
			return;
		}
		$(this).siblings().removeClass('layui-this');
		$(this).addClass('layui-this');
		var ind = $(this).index() + 1;
		$('.productBody > div').removeClass('layui-show');
		$('.productBody > div:nth-child(' + ind + ')').addClass('layui-show').removeClass('layui-hide');
		$('.productBody > div:nth-child(' + ind + ')').siblings().addClass('layui-hide');
	});
	var myTemplate = new Template();
	myTemplate.init();
});

//模板
function Template(){
	var _this = this;
	this.pageSize = 12;
	this.init = function(){
		//初始化加载所有模板内容
						//全部模板
		this.getDataByFilter({}, {pageBox: 'productViewPage', modeBox: 'productList', domBox: 'productView'});
						//概览模板
		// setListDataView('/template', 'generalView', {type: 1});
		this.getDataByFilter({type: 1}, {pageBox: 'generalViewPage', modeBox: 'productList', domBox: 'generalView'});
						//细览模板
		this.getDataByFilter({type: 2}, {pageBox: 'productPerViewPage', modeBox: 'productList', domBox: 'productPerView'});
						//首页模板
		// setListDataView('/template', 'nestView', {type: 3});
		this.getDataByFilter({type: 0}, {pageBox: 'nesViewPage', modeBox: 'productList', domBox: 'nestView'});

						//全部模板筛选按钮
		$('.allModel.productFilter .btnRowRight .layui-btn').click(function(){
			var params = _this.divChangeAndParam(this);
			if (params) {
				_this.getDataByFilter(params, {pageBox: 'productViewPage', modeBox: 'productList', domBox: 'productView'});
			}
		});
						//概览模板筛选按钮
		$('.overviewvModel.productFilter .btnRowRight .layui-btn').click(function(){
			var params = _this.divChangeAndParam(this);
			if (params) {
				_this.getDataByFilter(params, {pageBox: 'generalViewPage', modeBox: 'productList', domBox: 'generalView'});
			}
		});
						//细览模板筛选按钮
		$('.prevModel.productFilter .btnRowRight .layui-btn').click(function(){
			var params = _this.divChangeAndParam(this, 2);
			if (params) {
				_this.getDataByFilter(params, {pageBox: 'productPerViewPage', modeBox: 'productList', domBox: 'productPerView'});
			}
		});
						//首页模板筛选按钮
		$('.nestedModel.productFilter .btnRowRight .layui-btn').click(function(){
			var params = _this.divChangeAndParam(this);
			if (params) {
				_this.getDataByFilter(params, {pageBox: 'nesViewPage', modeBox: 'productList', domBox: 'nestView'});
			}
		});
	};

	//按钮点击时样式修改，返回获取筛选条件
	this.divChangeAndParam = function(_button, type){
		if (!$(_button).hasClass('layui-btn-primary')) {
			return null;
		}
		$(_button).siblings().addClass('layui-btn-primary').removeClass('active');
		$(_button).removeClass('layui-btn-primary').addClass('active');
		var k = $(_button).parent().attr('key');
		var params = {};
		if (type != null && type != undefined) {
			params.type = type;
		}
		if ($(_button).attr('data')) {
			params[k] = $(_button).attr('data');
		}
		var siblingsDom = $(_button).parent().parent().siblings().children('.btnRowRight');
		if (siblingsDom.children('.active').attr('data')) {
			params[siblingsDom.attr('key')] = siblingsDom.children('.active').attr('data');
		}
		return params;
	}

	//获取模板数据
	this.getTemplateData = function (current, param, fn) {
		if (!current || isNaN(current)) {
			current = 1;
		};
		var size = this.pageSize;
		var params = {
			"entity": param,
			"pager": {
				"current": current,
				"size": size,
                "sortProps":{
                    key:"modifyTime",
                    value: false
                }
			}
		}
		this.param = param;
		ajax(service_prefix.visual,"/template/list","post",JSON.stringify(params)).then(function (data) {
			if (data && data.success) {
				if (fn) {
					fn(data.obj);
				}
			} else {
				var err = '请求失败';
				if (data && data.msg) {
					err = '请求失败：' + data.msg;
				}
				showWinTips(err);
			}
		});
	};
	//筛选
	this.getDataByFilter = function(param, ids){
		this.getTemplateData(1, param, function(datainit){		//初始化加载
			// console.log(datainit.records);
			lodTpl(ids.modeBox, ids.domBox, datainit.records);
			setPages(ids.pageBox, datainit.total, _this.pageSize, 1, function(cur, lim){		//初始化分页
				_this.getTemplateData(cur, param, function(datas){
					lodTpl(ids.modeBox, ids.domBox, datas.records);
				});
			});
		});
	}
}
//格式化数据，让他正常的加载到页面上
function formatRecordsData(datas){
	datas.forEach(obj => {
		obj.img = '<img src="' + obj.img + '"/>';
		obj.operate = '<a style="color: red;">应用</a>';
	});
	return datas;
}

//设置列表数据
function setListDataView(url,id,entity){
	var jsonData = {
		"pager": {
		  "current": 1,
		  "size": 10
		},
		"entity":entity
	};
	var tit = [
		{field: 'id', title: 'ID'},
		{field: 'title', title: '模板名称'},
		{field: 'crUser', title: '创建人'},
		{field: 'createBy', title: '创建时间'},
		{field: 'img', title: '缩略图'},
		{field: 'operate', title: '操作'}
	];
	ajax(service_prefix.visual,url+'/list','post',JSON.stringify(jsonData)).then(function (data){
		if(data.success){		
			var datas = formatRecordsData(data.obj.records);
			setTableData('#'+id,data.obj,tit);
		}else{
			console.log(data.msg);
		}
	})
}
var zTreeObj;
//将模板应用在某个站点
function applyTemplate(tempId,type){
    layer.open({
		type: 1,
		area: ['400px', '500px'],
		title:'应用模板到选择栏目',
		maxmin: true,
		content: '<div id="applyView"></div>',
	  });
  
	  layui.laytpl($("#applyTemplate").html()).render({}, function(html){
		  $("#applyView").html(html);
	  })
  
	  var settingss = {
		  data: {
			  simpleData: {
				  enable: true, //true 、 false 分别表示 使用 、 不使用 简单数据模式
				  idKey: "id", //节点数据中保存唯一标识的属性名称
				  pIdKey: "parentId", //节点数据中保存其父节点唯一标识的属性名称
				  rootPId: -1 //用于修正根节点父节点数据，即 pIdKey 指定的属性值
			  },
			  key: {
				  name: "name" //zTree 节点数据保存节点名称的属性名称  默认值："name"
			  }
		  },
		  view: {
			  showLine: false,
			  showIcon: false
		  },
		  check: {                    //表示tree的节点在点击时的相关设置
			  enable: true,           //是否显示radio/checkbox
			  autoCheckTrigger: false,
			  chkStyle: "checkbox",      //值为checkbox或者radio表示
			  // radioType:"all",
			  chkboxType: { "Y" : "", "N" : "" },  //表示父子节点的联动效果，不联动
			  nocheckInherit: false
  
		  }
	  };
	  ajax(service_prefix.manage,"/programa/getTreeData","post",{}).then(function(data){
		  var nodes = data.obj;
		//   console.log('nodes',nodes);+
		  zTreeObj = $.fn.zTree.init($("#treeDemo"), settingss, nodes); //初始化树
  
		  zTreeObj.expandAll(true); //true 节点全部展开、false节点收缩
		  
	  })
	  layui.form.on("submit(apply_submit)",function(data){


		  var nodes = zTreeObj.getCheckedNodes(true);
		  console.log(nodes);
		  var ids = [];	
		  var siteIds = [];
		  for(var i in nodes){
			  if(nodes[i].level>1){
				ids.push(nodes[i].id);
			  }else if(nodes[i].level == 1){
				siteIds.push(nodes[i].id);
			  }
		  }		
		ajax(service_prefix.visual,"/template/" + tempId,"get").then(function (data) {
			if (!data || !data.success) {
				showWinTips('数据获取失败');
				return;
			}
			console.log(data);
			//key之间的关系
			var keyRela = {
				'tempdesc': 'proDes',
				'temphtml': 'htmlCode',
				'tempname': 'title',
				'themecsscontent': 'cssCode',
				'temptype': 'type',
				'outputfilename': 'outFileName',	//输出文件名
				'tempext': 'exteName' 		//扩展名
			};
			var param = {};
			var obj = data.obj;
			for (var k in keyRela) {
				if (obj[keyRela[k]]) {
					if (keyRela[k] == 'htmlCode') {
						param[k] = toWholeHtml(obj);
					} else {
						param[k] = obj[keyRela[k]];
					}
				}
			}
			var parentNode = nodes[0].getParentNode();
			console.log('parentNode',parentNode);
			param.siteid = parentNode.id;
			ajax(service_prefix.manage,"/template","post", JSON.stringify(param)).then(function (datas) {
				// /manage/template
				console.log(datas);
				var id = datas.obj.id;
				var p = {};
				p.ids = ids;
				p.siteIds = siteIds;
				p.tplType = type;
				ajax(service_prefix.manage,"/programa/updateColumn?id="+id,"post",JSON.stringify(p)).then(function(data){	
					layer.msg('应用模板成功', function(){
						closeLayer();
					}); 
				})
			});
		});
	  });
}

//将生成的代码片段变成完整的html
function toWholeHtml(data){
	if (!data.title) {
		data.title = 'Document';
	}
	var html = '<!Doctype html>'
	+ '<html>'
	+ '<head>'
	+ '<meta charset="utf-8">'
	+ '<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">'
	+ '<title>' + data.title + '</title>'
	+ '<link rel="stylesheet" href="thirdparty/layui/css/layui.css">'
	+ '<link rel="stylesheet" href="/pub/css/jw_ui.css">'
	+ '<script src="thirdparty/jquery/jquery-1.12.4.min.js"></script>'
	+ '<style>'
	+ data.cssCode
	+ '</style>'
	+ '</head>'
	+ '<body>'
	+ data.htmlCode
	+ '</body>'
	+ '<script src="thirdparty/layui/layui.all.js"></script>'
	+ '<script type="text/javascript" src="common/js/config.js"></script>'
	+ '<script type="text/javascript" src="common/js/common.js"></script>'
	+ '<script type="text/javascript" src="/pub/js/jw_behavior.js"></script>'
	+ '<script type="text/javascript">'
	+ data.jsCode
	+ '</script>'
	+ '</html>';
	return html;
}