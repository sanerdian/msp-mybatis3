$(function(){
	//获取最初的数据
	// getGropData();
	var myGroup = new Group();
	myGroup.init();
	//初始化事件
	//一级导航切换
	$('.sghLeft .layui-nav-item').on('click', function(){
		if ($(this).hasClass('layui-this')) {
			return;
		}
		$(this).siblings().removeClass('layui-this');
		$(this).addClass('layui-this');
		myGroup.getDataByModuleType($(this).attr('data'));
	});
	
			//组件内部按钮切换
	$('.btnRowRight').on('click', 'button', function(){
		if (!$(this).hasClass('layui-btn-primary')) {
			return;
		}
		$('.btnRowRight button').addClass('layui-btn-primary');
		$(this).removeClass('layui-btn-primary');
		if ($(this).attr('data') && $(this).attr('datatype')) {
			myGroup.getDataByKey($(this).attr('data'), $(this).attr('datatype'));
		}
		// getGropData();
	});
	// initAllGroup('groupView', 'groupList');
});

function Group(){
	var _this = this;
	this.data = null;
	this.param = null;
	this.pageSize = 12;
	this.init = function(pid, netType){				//初始化时请求数据
		this.getDataByAjax({'pid': 0}, function(data){
			_this.initLabelDom(data);
		});
		//初始化加载所有数据
		this.getDataByKey(-1, 'group');
	};

	this.getDataByAjax = function(param, fn, current){
		if (!current || isNaN(current)) {
			current = 1;
		};
		var size = this.pageSize;
		if (param.pid == 0) {
			size = 1000;
		}
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
		ajax(service_prefix.visual,"/module/list","post",JSON.stringify(params)).then(function (data) {
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
	}

	//初始化分页
	this.initPage = function(count){
		setPages('subGroupPa', count, _this.pageSize, 1, function(cur, limit){
			_this.getDataByAjax(_this.param, function(data){
				lodTpl('groupList', 'groupView', data.records);
			}, cur);
		});
		// laypage.render({
		// 	elem: 'subGroupPa'
		// 	,count: count //数据总数，从服务端得到
		// 	,limit: _this.pageSize
		// 	,jump: function(obj, first){
		// 	  //首次不执行
		// 	  if(!first){
				
		// 		//do something
		// 	  }
		// 	}
		//   });
	};

	// function setPages (obj,total,limit,current,callBack) {
	// 	layui.laypage.render({
	// 		elem: obj,
	// 		count: total,
	// 		limit:limit,
	// 		curr:current,
	// 		layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip'],
	// 		jump: function (obj, first) { 
	// 			if (!first) {
	// 				callBack(obj.curr, obj.limit);
	// 			}
	// 		}
	// 	});
	// }
	
	// subGroupPa();

				//初始化显示所有数据
	this.initAdataDom = function(data){
		var datas = [];
		for (var i = 0; i < data.length; i++) {
			var childs = data[i].children;
			if (childs.length > 0) {
				datas = datas.concat(childs);
			}
		}
		lodTpl('groupList', 'groupView', datas);
	}
					/*k => 当前数据k值， type => 是组件类型还是网页类型*/
	this.getDataByKey = function(k, type){
		var params = {};
		if (type == 'group') {
			params = {pid: k};
		} else {
			params = {netType: k};
		}
		if ($('.sghLeft .layui-nav-item.layui-this').attr('data')) {
			params.moduleType = $('.sghLeft .layui-nav-item.layui-this').attr('data');
		}
		this.getDataByAjax(params, function(data){
			_this.initPage(data.total, 1);
			lodTpl('groupList', 'groupView', data.records);
		});
	};
	this.getDataByModuleType = function(moduleType){			//组件页签切换
		var params = {};
		if (moduleType) {
			params.moduleType = moduleType;
		}
		params.pid = -1;
		$('.subGroupFilter .btnRowRight .layui-btn').addClass('layui-btn-primary');
		$('.subGroupFilter .btnRowRight .initStatus').removeClass('layui-btn-primary');
		this.getDataByAjax(params, function(data){
			_this.initPage(data.total, 1);
			lodTpl('groupList', 'groupView', data.records);
		});
	};

	this.formatData = function(data, k){		//根据组件类型分类
		var obj = {};
		for (var i = 0; i < data.length; i++) {
			obj[data[i][k]] = {title: data[i].title, data: data[i].children};
		}
		return obj;
	};
	this.formatNetData = function(data, k){		//根据网页类型分类
		var obj = {};
		for (var i = 0; i < data.length; i++) {
			var childs = data[i].children;
			for (var j = 0; j < childs.length; j++) {
				if (childs[j].netType) {
					if (!obj[childs[j].netType]) {
						obj[childs[j].netType] = [];
					}
					obj[childs[j].netType].push(childs[j]);
				}
			}
		}
		return obj;
	};
	
	this.initLabelDom = function(data) {	//初始化筛选项
		var obj = data.records;
		$('.subGroupFilter .groupType').empty();
		for (var k = 0; k < obj.length; k++) {
			$('.subGroupFilter .groupType').append('<button type="button" class="layui-btn layui-btn-primary" data="' + obj[k].id + '" datatype="group">' + obj[k].title + '</button>');
		}
	}
}
