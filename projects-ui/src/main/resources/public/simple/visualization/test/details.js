var defaultPageSize = 15;
var curr = 1;
/**
 * 设置列表数据
 * @param {*} serviceId 微服务id(对应地址前缀)
 * @param {*} url 请求地址
 */
function setListData(serviceId,url,curr,defaultPageSize,entity){
    console.log(11,entity);
	var jsonData = {
		"pager": {
		  "current": curr,
		  "size": defaultPageSize,
        "sortProps":{
            key:'createTime',
            value:false
        }
		},
		"entity":entity,
	};
	ajax(serviceId,url+'/list','post',JSON.stringify(jsonData)).then(function (data){
		if(data.success){	
            console.log(data.obj.records[0].crUser);		
			alertMes(data.obj.records[0].crUser);
		}else{
			console.log(data.msg);
		}
	})
}
setRender();