let exportConfig={};
function exportExcel(serviceId, url,fileName,entity,columns) {
    setOpenData4ExportExcel(columns);
    exportConfig={
        serviceId:serviceId,
        url:url,
        fileName:fileName,
        entity:entity,
        columns:columns
    };
}
function setOpenData4ExportExcel(columns) {
    let tit = "excel导出选项";
    setLayer('table_data',tit,'700px','300px');
    laytpl($('#templateExportExcel').html()).render({}, function(html){
        $('#table_data').html(html);
        let checks =[];
        for(let i=0;i<columns.length;i++){
            let column = columns[i];
            if(column&&column.field&&column.title){
                checks.push(column);
            }
        }
        getData(checks,'#templateColumns','#checks');
        setRender();
    });
}
form.on("submit(LAY-exportExcel-back-add)",function (data) {
    let columnList=[];
    let headerList=[];
    for(let key in data.field){
        if(key.indexOf("column.")==0){
            let field=key.substr("column.".length);
            for(let i=0;i<exportConfig.columns.length;i++){
                if(field==exportConfig.columns[i].field){
                    columnList.push(exportConfig.columns[i].field);
                    headerList.push(exportConfig.columns[i].title);
                }
            }
        }else {
            exportConfig[key]=data.field[key];
        }
    }
    if(columnList.length>0){
        exportConfig.columnList=columnList;
        exportConfig.headerList=headerList;
        doDownload();
    }else {
        layer.alert('请选择要导出的列！');
    }

});


/**
 * // TODO 后台使用的是无参get方法，所以暂时屏蔽最后一个参数
 * @param serviceId 微服务id, 示例：service_prefix.gateway
 * @param url
 * @param fileName
 * @param param
 */
function doDownload () {
    let url=getAjaxUrl(exportConfig.serviceId,exportConfig.url);
    let params={
        fileName:exportConfig.fileName,
        entity: exportConfig.entity,
        columnList:exportConfig.columnList,
        headerList:exportConfig.headerList,
        range:exportConfig.range
    };
    let xhr = new XMLHttpRequest();
    xhr.open('post', url, true);        // 也可以使用POST方式，根据接口
    xhr.responseType = "blob";    // 返回类型blob
    xhr.withCredentials=true;
    xhr.onload = function (data) {
        if (this.status === 200) {
            var blob = this.response;
            var reader = new FileReader();
            reader.readAsDataURL(blob);    // 转换为base64，可以直接放入a表情href
            reader.onload = function (e) {
                var a = document.createElement('a');
                a.download = exportConfig.fileName;//下载文件名
                a.href = e.target.result;
                $("body").append(a);    // 修复firefox中无法触发click
                a.click();
                $(a).remove();
            }
            layer.closeAll();
        }else{
            layer.alert('excel导出遇到问题！错误码：'+this.status);
        }
    };
    // 发送ajax请求
    // xhr.send();
    // 发送带参数的方法。参考自：https://blog.csdn.net/harryhare/article/details/80778066
    xhr.setRequestHeader("Content-Type", "application/json");
    let data = JSON.stringify(params);
    xhr.send(data);
}
