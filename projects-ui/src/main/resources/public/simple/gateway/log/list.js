
//实体的基础路径，对应于该实体的controller类对应的访问路径。
const uriRoot="/log";
//列信息-需根据实体的列修改
const columns = [
	{type:'checkbox'}
	,{field:'rowNum', title: '序号',align: 'right',width:80}
	,{field:'logTime', title: '时间',align: 'left',sort: true,width:200}
	,{field:'customIp', title: '客户端ip',align: 'left',sort: true,width:140}
	,{field:'uri', title: 'uri', align: 'left',sort: true,width:240}
	,{field:'statusCode', title: '状态码', align: 'left',sort: true,width:90}
	,{field:'errType', title: '异常类型', align: 'left',sort: true}
	,{field:'remark', title: '备注', align: 'left',sort: true}
	,{field:'', title: '操作',fixed: 'right',toolbar: '#operateTemplate',align: 'left',width:80}
];

let param4Data={};
let param4Chart={};
param4Chart.type="thisMinute";//初始默认加载本分钟的统计
$(function(){
	init();
	//搜索列表默认显示数据
	doList(1);
	loadChart();
});
function init() {
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
		}

	});

	//点击按钮操作
	$('.layui-btn').click(function(){
		let type = $(this).attr('data-type');
		if(type === 'del'){
			var data = checkChecked('tableData');
			if(data){
				doDeleteByIds(data);
			}
		}else if(type=='exportExcel'){
			exportExcel(service_prefix.gateway,uriRoot+"/downloadExcel","log.xls",param4Data,columns);
		}
		if(type){return false;}
	});

	//日志统计的form操作--时间范围及时间粒度只有在选择“自定义”时才显示
	$("#item_timeRange").hide();
	$("#item_timeUnit").hide();
	form.on('select(LAY-chart-select-type)',function (data) {
		if(data.value=='userDefine'){
			$("#item_timeRange").show();
			$("#item_timeUnit").show();
		}else {
			$("#item_timeRange").hide();
			$("#item_timeUnit").hide();
		}
	});
	//日志统计的form操作--根据form表单加载chart
	form.on("submit(LAY-chart-back-search)",function (data) {
		if("userDefine"==data.field.type){//自定义的输入校验
			if(!data.field.timeStart){
				layer.alert('请输入：自定义开始时间');
				return false;
			}else if(!data.field.timeEnd){
				layer.alert('请输入：自定义结束时间');
				return false;
			}
		}
		Object.assign(param4Chart,data.field);
		loadChart();
		return false;
	});
	form.verify({
		statusCode:function (value) {
			if(value&&(!/^\d*$/.test(value)||value<=0)){
				return '状态码只能是正数';
			}
		}
	});
}


//条件列表搜索
function doList(current) {
	doListTable(service_prefix.gateway, uriRoot+"/list","post",param4Data,current,null
		,"tableData",columns,"pageData");
}//根据ids批量删除
function doDeleteByIds(data) {
	layer.alert("是否确定删除,该操作不可撤回",function () {
		ajax(service_prefix.gateway, uriRoot,"delete",JSON.stringify(data)).then(function (data){
			if(data.success){
				doList(1);
				layer.closeAll();
			}else{
				layer.alert(data.msg);
			}
		})
	})
}

function loadChart() {	// 基于准备好的dom，初始化echarts实例
	let myChart = echarts.init(document.getElementById('logChart'));
	ajax(service_prefix.gateway, "/log/statistics","post",JSON.stringify(param4Chart)).then(function (data){
		if(data.success){
			// 使用刚指定的配置项和数据显示图表。
			chartOption.xAxis[0].data=data.obj.time;
			chartOption.series[0].data=data.obj.count_all;
			chartOption.series[1].data=data.obj.count_err;
			myChart.setOption(chartOption);
		}else{
			layer.alert(data.msg);
		}
	});
}
// 指定图表的配置项和数据
let chartOption = {
	title: {
		text: '日志统计',
		subtext: ''
	},
	tooltip: {
		trigger: 'axis'
	},
	legend: {
		data: ['总请求数', '异常请求数']
	},
	toolbox: {
		show: true,
		feature: {
			dataView: {show: true, readOnly: false},
			magicType: {show: true, type: ['line', 'bar']},
			restore: {show: true},
			saveAsImage: {show: true}
		}
	},
	calculable: true,
	xAxis: [
		{
			type: 'category',
			data: []
		}
	],
	yAxis: [
		{
			type: 'value'
		}
	],
	series: [
		{
			name: '总请求数',
			type: 'bar',
			data: [],
			markPoint: {
				data: [
					{type: 'max', name: '最大值'},
					{type: 'min', name: '最小值'}
				]
			},
			markLine: {
				data: [
					{type: 'average', name: '平均值'}
				]
			}
		},
		{
			name: '异常请求数',
			type: 'bar',
			data: [],
			markPoint: {
				data: [
					{type: 'max', name: '最大值'},
					{type: 'min', name: '最小值'}
				]
			},
			markLine: {
				data: [
					{type: 'average', name: '平均值'}
				]
			}
		}
	]
};
//渲染form中部分组件
setRender();
//加载当前栏目弹框
setOpenLayer();

setDate('#date','date');
setDate('#date2','date');
setDate('#date3','date');
setDate('#date4','date');