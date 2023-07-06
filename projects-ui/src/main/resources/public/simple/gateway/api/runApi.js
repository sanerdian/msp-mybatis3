let apiId = localStorage.getItem('detailId');
let vo;
ajax(service_prefix.gateway, "/router/findById/"+apiId,"get").then(function (data){
    if(data.success){
        let api=data.obj.api;
        vo=data.obj;
        api.reqFullUrl=service_prefix.gateway+"/api/"+api.reqServiceName+"/"+api.groupId+"/"+api.reqUri;
        getData(api,'#formTemplateApi','#formApi');
        if(data.obj.apiParams&&data.obj.apiParams.length>0){//只有api包含参数时，这部分才加载
            let params=[];
            for(let index=0;index<data.obj.apiParams.length;index++){
                if(data.obj.apiParams[index].paramType=='normal'){
                    params.push(data.obj.apiParams[index]);
                }
            }

            getData(params,'#formTemplateParams','#formParams');
        }
        // getData(data.obj.signature,'#formTemplateSignature','#formSignature');
        // getData(data.obj.signature,'#formTemplateAppKey','#formAppKey');
        // getData(data.obj.appKey,'#formTemplateAppKey','#formAppKey');
        if(data.obj.signature){
            $("#checkType").append("<option value='signature'>签名密钥</option>");
            getData(data.obj.signature,'#formTemplateSignature','#formSignature');
            // getData(data.obj.signature,'#formTemplateSignature','#formSignature');
            // getData(data.obj.signature,'#formTemplateAppKey','#formAppKey');
        }
        if(data.obj.appKey){
            $("#checkType").append("<option value='appKey'>应用密钥</option>");
            getData(data.obj.appKey,'#formTemplateAppKey','#formAppKey');
        }
        setRender();
    }else {
        layer.alert(data.msg);
    }
});
form.on("select(LAY-select-checkType-runApi)",function (data) {
    if(data.value=='signature'){
        $("#formSignature").show();
        $("#formAppKey").hide();
    }else if(data.value=='appKey'){
        $("#formSignature").hide();
        $("#formAppKey").show();
    }
});
function getParamDatatype(field){
    let result="";
    let apiParams=vo.apiParams;
    for(let i=0;i<apiParams.length;i++){
        let apiParam=apiParams[i];
        if(apiParam.custParamName==field){
            result = apiParam.custParamDatatype;
        }
    }
    return result;
}
form.on("submit(LAY-runApi-back-search)",function (data) {
    let param={};
    let apiParam={};
    for(let key in data.field){
        if(key.indexOf('apiParam')==0){
            if(data.field[key].length>0){
                let field=key.substr('apiParam.'.length);
                let value=data.field[key];
                let datatype=getParamDatatype(field);
                //用数据类型对数据进行校验！
                if('integer'==datatype){
                    let re = /^[0-9]+.?[0-9]*$/;
                    if (!re.test(value)) {
                        layer.alert(field+"只能输入数字");
                        return false;
                    }
                }else if('array'==datatype){
                    try{
                        let obj = JSON.parse(value)
                        if (!(obj instanceof Array)){
                            layer.alert(field+"只能是数组");
                            return false;
                        }
                    }catch (e) {
                        layer.alert(field+"只能是数组");
                        return false;
                    }
                }else if(!datatype||'object'==datatype){
                    try{
                        let obj = JSON.parse(value)
                        if (!(obj instanceof Object)){
                            layer.alert(field+"只能是对象");
                            return false;
                        }
                    }catch (e) {
                        layer.alert(field+"只能是对象");
                        return false;
                    }
                }
                apiParam[field]=value;
            }
        }else {
            param[key]=data.field[key];
        }
    }
    param.apiParam=apiParam;
    param.apiId=apiId;
    param[param.checkType]=vo[param.checkType];
    ajax(service_prefix.gateway, '/router/runApi','post',JSON.stringify(param)).then(function (data){
        let result=data.obj;
        if(data.success){
            let html="";
            html+="<blockquote class=\"layui-elem-quote\">请求信息</blockquote>";
            html +="<div>url:"+ result.request.url+"</div>";
            html+="<div>method:"+result.request.method+"</div>";
            if(result.request.headers){
                html+="<div>request headers:</div>";
                html+="<div><pre>"+ JSON.stringify(result.request.headers,null,4)+"</pre></div>";
            }
            if(result.request.body){
                html+="<div>request body:</div>";
                html+="<div><pre>"+ JSON.stringify(JSON.parse(result.request.body),null,4)+"</pre></div>";
            }

            html+="<blockquote class=\"layui-elem-quote\">响应信息</blockquote>";
            html +="<div>status code:"+ result.statusCode+"</div>";
            if(result.response){
                if(result.response.headers){
                    html+="<div>response headers:</div>";
                    html+="<div><pre>"+ JSON.stringify(result.response.headers,null,4)+"</pre></div>";
                    if(result.response.body){
                        html+="<div>response body:</div>";
                        try{//尝试将body按json格式显示，如果失败，则直接输出
                            let json = JSON.stringify(JSON.parse(result.response.body), null, 4);
                            html+="<div style='margin-left: 20px;'><pre>"+json+"</pre></div>";
                        }catch (e) {
                            html+="<div style='margin-left: 20px;'>"+result.response.body+"</div>";
                        }

                    }
                }else {
                    html+="<div style='margin-left: 20px;'>"+JSON.stringify(JSON.parse(result.response),null,4)+"</div>";
                }

            }else {
                html +="<div>msg:"+ result.msg+"</div>";
            }
            $("#runApiResult").html(html);
        }else{
            layer.alert(data.msg);
        }
    });
});

//加载弹出框
setOpenLayer();
