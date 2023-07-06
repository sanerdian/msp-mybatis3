//服务id
// var serviceId = service_prefix.dict;
var serviceId = "";

//接口地址
var url = '/zsk/unit';
console.log('url',url);
//列表数据
var columns = [
    { type: 'checkbox', fixed: 'left' }
    , { type: 'numbers', title: '序号', fixed: 'left' }
    , { field: 'fenlei', title: '模块名称' }
    , { field: 'wordname', title: '机构' }
    , { field: 'tongyici', title: '机构同义词' }
    , { field: 'pinyin', title: '机构拼音' }
    , { field: 'tongyinci', title: '机构同音词' }
    , { field: 'entongyici', title: '英文同义词' }
    , { field: 'suoxie', title: '英文缩写' }
    , { field: 'sourceTerms', title: '词条来源' }
    , { field: 'status', title: '词条状态', width: 100, templet: "#statusTpl"}
    , { field: 'createTime', title: '创建时间' }
    , { field: 'crUser', title: '创建人' }
    , { field: 'modifyTime', title: '修改时间' }
    , { field: 'modifyUser', title: '修改人' }
    , { field: '', title: '操作', toolbar: '#operateTemplate', fixed: 'right', width: 180 }
];

setListData(curr, defaultPageSize, serviceId, url);
getSearch(serviceId, url,curr,defaultPageSize);
btnClick('550px', '500px');
setOpenLayer();