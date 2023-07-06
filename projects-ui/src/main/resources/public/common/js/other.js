/**
 * 没有调用?
 * 数据库字段转属性
 * @param {*} field 
 */
function fieldToProperty(field) {
    if (!field) {
        return "";
    }
    field = field.toLowerCase();
    var result = "";
    for (var i = 0; i < field.length; i++) {
        var c = field.charAt(i);
        if (c == '_') {
            var j = i + 1;
            if (j < field.length) {
                result += field.charAt(j).toUpperCase();
                i++;
            }
        } else {
            result += c;
        }
    }
    return result;
}

/**
 * 工作流引擎-我的待办
 */
function getWorkFlow(){
    return new Promise((resovle, reject) => {
        ajax(service_prefix.flowable, "/process","post",{}).then(res => {
            resovle(res.obj);
        })
    })
}

function setChildren(data){
    for(var i in data){
        data[i].icon = "common/img/wf_node1.png";
        if(data[i].appDefinition.models){
            data[i].children = data[i].appDefinition.models;
            for(var j in data[i].children){
                data[i].children[j].icon = "common/img/wf_node2.png";
            }
        }
    }
}
function getTypeTree(){
    return new Promise((resovle, reject) => {
        // $.ajax({
        //     type: "get",
        //     url: "/fmodeler/app/rest/models?filter=apps&modelType=3&sort=modifiedDesc",
        //     dataType: 'json',
        //     success: function(res) {
        //         setChildren(res.data);
        //         resovle(res.data);
        //     },
        //     error: function(err) {
        //         console.log(err);
        //     }
        // })
        ajax(service_prefix.flowable,"/process","post",{}).then(function(res){
            resovle([{name:"全部流程",children:res.obj}]);
        })
        // $.ajax({
        //     type: "get",
        //     url: "/fmodeler/app/rest/models?filter=apps&modelType=3&sort=modifiedDesc",
        //     dataType: 'json',
        //     success: function(res) {
        //         setChildren(res.data);
        //         resovle(res.data);
        //     },
        //     error: function(err) {
        //         console.log(err);
        //     }
        // })
    })

    // ajax(service_prefix.flowable,"/getType","get").then(res=>{
    //     console.log("type:",res);
    // })
}

function showFlowImg(procInstId, metadataId){
    console.info("procInstId: " + procInstId + ", metadataId: " + metadataId);
    layer.open({
        type: 1 //此处以iframe举例
        ,title: '流程进度图'
        ,area: ['1000px', '600px']
        ,maxmin: false
        ,content: "<div class='layui-card-body' style='text-align: center'><img style='max-height: 710px;max-width: 1300px' src='"+getAjaxUrl(service_prefix.flowable,"/getImg?id=" + procInstId)+"&metadataId="+ metadataId +"'/></div>"
    });
}
/**
 * 元数据栏目管理
 * 查询接口地址
 * @param {*} tableId
 */
function getMetadataUri(tableId) {
    return new Promise((resovle, reject) => {
        if(tableId && tableId != 0){
            ajax(service_prefix.metadata, "/" + tableId, "get", {}).then(res => {
                if(res.success && res.obj.ownerid){
                    ajax(service_prefix.metadata, "/moduleinfo/"+res.obj.ownerid).then(res1 => {
                        if(!res1.success) {
                            resovle("");
                            return false;
                        }
                        var tablename = res.obj.tablename;
                        var strs = tablename.split("_");
                        if(res.obj.tabletype != 'es') strs.shift();
                        if(res1.obj){
                            var modelName = res1.obj.englishname;
                            resovle("/"+modelName+"/"+strs.join("").toLowerCase());
                        }else{
                            resovle("");
                        }
                    })
                }else{
                    resovle("");
                }
            })
        }else{
            resovle("");
        }
    })
}

/**
 * @param {*} arr
 */
function unique (arr) {
    return Array.from(new Set(arr))
}

/**
 * 元数据栏目管理
 * 查分类法名称
 * @param {*} records
 * @param {*} classFields
 */
function setClassName(records,classFields) {
    return new Promise((resovle, reject) => {
        var classIds = [];
        records.forEach(r => {
            classFields.forEach(cf => {
                classIds.push(r[cf.proName]);
            })
        })
        classIds = unique(classIds);
        classIds = unique(classIds.join().split(","));
        ajax(service_prefix.metadata, "/class/classByIds","post",JSON.stringify(classIds)).then(names => {
            records.forEach(r => {
                classFields.forEach(cf => {
                    if(r[cf.proName]){
                        var ids = r[cf.proName].split(",");
                        var name = [];
                        ids.forEach(cid => {
                            name.push(names.obj[cid]);
                        })
                        if(name.join()){
                            r[cf.proName] = name.join();
                        }
                    }
                })
            })
            resovle(1);
        })
    }).catch((e) => {
        console.log(e);
    });
}

/**
 * 元数据栏目管理
 * 查分类法字段
 * @param {*} fields
 */
function getClassFields(fields){
    var classFields = [];
    for(var i in fields){
        fields[i].forEach(f => {
            if(f.fieldtype == 13){
                classFields.push(f);
            }
        })
    }
    return classFields;
}