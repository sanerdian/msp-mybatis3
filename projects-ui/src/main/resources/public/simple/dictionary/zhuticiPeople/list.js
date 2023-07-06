//服务id
// var serviceId = service_prefix.dict;
var serviceId = "";

//接口地址
var url = '/zsk/people';
console.log('url',url);
//列表数据
var columns = [
    { type: 'checkbox', fixed: 'left' }
    , { type: 'numbers', title: '序号', fixed: 'left' }
    , { field: 'fenlei', title: '模块名称', width: 100 }
    , { field: 'wordname', title: '人名' , width: 100}
    // , { field: 'tongyici', title: '人名同义词', width: 100 }
    , { field: 'pinyin', title: '人名拼音', width: 140 }
    // , { field: 'tongyinci', title: '人名同音词' , width: 100}
    // , { field: 'entongyici', title: '英文同义词', width: 100 }
    // , { field: 'suoxie', title: '英文缩写' , width: 100}
    , { field: 'sourceTerms', title: '词条来源', width: 100 }
    , { field: 'status', title: '词条状态', width: 100, templet: "#statusTpl"}
    , { field: 'currentposition', title: '现任职务', width: 100}
    , { field: 'currentemployer', title: '现任单位', width: 100}
    , { field: 'currentstart', title: '现任开始', width: 100}
    , { field: 'currentend', title: '现任结束', width: 100 }
    , { field: 'historicalresume', title: '历史履历', width: 100}
    , { field: 'createTime', title: '创建时间', width: 170 }
    , { field: 'crUser', title: '创建人', width: 170}
    , { field: 'modifyTime', title: '修改时间', width: 170}
    , { field: 'modifyUser', title: '修改人', width: 170}
    , { field: '', title: '操作', toolbar: '#operateTemplate', fixed: 'right', width: 180 }
];

setListData(curr, defaultPageSize, serviceId, url);
getSearch(serviceId, url,curr,defaultPageSize);
btnClick('550px', '500px');
setOpenLayer();