//服务id
var serviceId = service_prefix.dict;

//接口地址
var url = '/wordname';
//列表数据
var columns = [
	{type:'checkbox',fixed: 'left'}
	,{type:'numbers',title: '序号',fixed: 'left'}
	, {field: 'wordname',  title: '主题词', sort: true}
	, {field: 'tongyici', title: '主题词同义词'}
	, {field: 'pinyin', title: '主题词拼音'}
	, {field: 'tongyinci', title: '主题同音词'}
	, {field: 'entongyici', title: '英文同义词'}
	, {field: 'suoxie', title: '英文缩写'}
	, {field: 'source', title: '词条来源'}
	, {field: 'status', title: '词条状态',width:100}
	, {field: 'createTime', title: '创建时间'}
	, {field: 'opertime', title: '修改时间'}
	, {field: 'operuser', title: '修改人'}
	,{field:'', title: '操作', toolbar: '#operateTemplate',fixed: 'right',width:140}
];

setListData(serviceId,url);
getSearch(serviceId,url);
btnClick('550px','500px');
setOpenLayer();

formSubmitAdd(serviceId,url);
