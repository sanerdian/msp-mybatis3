var serviceIp = com.jnetdata.url_prefix;
var groupDocId = getQueryString('id');//组件文章ID
console.log('groupDocId', groupDocId);
var importCssObj = {};//存放上专的css文件
var importJsObj = {};//存放上传的js文件
var codeContent = '';//css文件代码转换图片后的内容
var codeJsontent = '';//js文件代码详情内容
var groupObj = {};
//新建组件 自动获取右侧的高度
function getGroupHeight() {
    var bodyH = $('#groupEdit').height();
    console.log('bodyH', bodyH);
    $('#groupEdit #htmlCode, #groupEdit .codeView .upload_list').height(bodyH - 140);
    $('#groupEdit #cssCode, #groupEdit #jsCode').height(bodyH - 190);
}
getGroupHeight();

//组件设置弹窗
function setBaseGroup(baseData) {
    layer.open({
        title: '组件设置',
        content: $('#setGroup').html(),
        btn: '',
        area: ['570px'],
    });
    layui.use('form', function () {
        var form = layui.form;
        //点击取消后关闭
        $('.reverse-btn').on('click', function () {
            layer.closeAll();
        });
        //下拉事件触发
        form.on('select(visualMoudeltype)', function (data) {
            changeSelect(labels, data.value);
            form.render();
        });

        getGroupById(groupDocId, function (data) {
            form.val('setGroupform', data);//数据返显  
            //提交表单触发  
            form.on('submit(groupBtnAdd)', function (data) {
                // console.log('save',data.field)
                ajax(service_prefix.visual, "/module/" + groupDocId, "put", JSON.stringify(data.field)).then(function (data) {
                    if (data.success) {
                        layer.msg('组件设置成功');
                    }
                });
                layer.close(layer.index);
                return false;
            });
        });

        form.render();
    });

}

//为组件设置表单获取组件类型和类型分类数据
function getLabels(fn) {
    var params = {
        "entity": {
            pid: 0
        },
        "pager": {
            "current": 1,
            "size": 1000,
            "sortProps": {
                key: "modifyTime",
                value: false
            }
        }
    };
    ajax(service_prefix.visual, "/moduletype/listing", "post", JSON.stringify(params)).then(function (data) {
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

//初始换组件设置表单下拉框数据
function initSelect(data, vcId, vmId) {
    console.log('initSelect', data, vcId, vmId);
    let obj = data;
    $('.visualMoudeltype').empty();
    $('.visualClasstype').empty();
    for (var k = 0; k < obj.length; k++) {
        if (obj[k].parentId == 0)
            $('.visualMoudeltype').append(' <option value="' + obj[k].id + '">' + obj[k].title + '</option>');
        if (obj[k].parentId == vmId)
            $('.visualClasstype').append(' <option value="' + obj[k].id + '">' + obj[k].title + '</option>');
    }
    // $('#setGroupform [name=visualMoudeltype]').val(localStorage.getItem('visual-subgroup-moudleType'));
    // $('#setGroupform [name=visualClasstype]').val(localStorage.getItem('visual-subgroup-classType'));
}

//组件设置表单点击组件类型时修改类型分类
function changeSelect(data, parent) {
    let obj = data;
    $('.visualClasstype').empty();
    for (var k = 0; k < obj.length; k++) {
        if (obj[k].parentId == parent)
            $('.visualClasstype').append(' <option value="' + obj[k].id + '">' + obj[k].title + '</option>');
    }
}

//上传CSS文件
function uploadCssfile(id) {
    var title;
    if (id) {
        title = '编辑CSS';
    } else {
        title = '新建CSS';
    }
    layer.open({
        title: title,
        content: '<div id="cssFileView"></div>',
        btn: '',
        area: ['400px', '200px'],
        success: function (layero, index) {
            var data = [];
            getData(data, '#cssFileTemplate', '#cssFileView');
            if (id) {
                layer.msg('编辑上传');//编辑上传CSS
            } else {
                uploadCss('/tplresource/importVisualModuleCss?moduleId=' + groupDocId);//新建上传图片
            }
            layui.form.render();
        }
    });
}

//上传CSS功能
function uploadCss(url) {
    upload.render({
        elem: '#cssFileuploadBtn' //绑定元素
        , url: getAjaxUrl(service_prefix.manage, url)//上传接口
        , accept: 'file'
        , exts: 'css'
        , before: function (obj) { //obj参数包含的信息，跟 choose回调完全一致，可参见上文。
            obj.preview(function (index, file, result) {
                // console.log(index); //得到文件索引
                // console.log(file); //得到文件对象
                // console.log(result); //得到文件base64编码，比如图片
            });
        }
        , done: function (res) {
            //上传完毕回调
            console.log('resCSS', res);
            layer.msg('上传CSS成功');
            uploadCssList();
        }
        , error: function () {
            //请求异常回调
        }
    });
}

//上传CSS列表
function uploadCssList() {
    importCssObj = {};//重置
    var jsonData = {
        "entity": {
            "templateId": groupDocId,
            "type": 'css'
        },
        "pager": {
            "current": 1,
            "size": 10
        }
    };
    ajax(service_prefix.manage, "/tplresource/cssList", "post", JSON.stringify(jsonData)).then(function (data) {
        if (data.success) {
            getData(data.obj.records, '#uploadCssListTemplate', '#uploadCssListView');
            //为了存放组件上传的css文件
            for (var i = 0; i < data.obj.records.length; i++) {
                var fileId = data.obj.records[i].id;
                var filePubUrl = data.obj.records[i].pubUrl;
                importCssObj[fileId] = filePubUrl;
            }
        } else {
            console.log(data.msg);
        }
    });
}

//上传CSS删除
function uploadCssDel(id) {
    layer.open({
        title: '删除',
        content: '确定要删除吗？',
        btn: '',
        area: ['200px', '170px'],
        btn: ['确定', '取消'],
        btn1: function () {
            ajax(service_prefix.manage, "/tplresource/delete/" + id, "get").then(function (data) {
                if (data.success) {
                    layer.msg('删除成功');
                    uploadCssList();
                } else {
                    console.log(data.msg);
                }
            });
            layer.closeAll();
        },
        btn2: function () {
            layer.closeAll();
        }
    });
}

//上传JS文件
function uploadJsfile(id) {
    var title;
    if (id) {
        title = '编辑JS';
    } else {
        title = '新建JS';
    }
    layer.open({
        title: title,
        content: '<div id="jsFileView"></div>',
        btn: '',
        area: ['400px', '200px'],
        success: function (layero, index) {
            var data = [];
            getData(data, '#jsFileTemplate', '#jsFileView');
            uploadJs();
            layui.form.render();
        }
    });
}

//上传JS功能
function uploadJs(url) {
    upload.render({
        elem: '#jsFileuploadBtn' //绑定元素
        , url: getAjaxUrl(service_prefix.manage, '/tplresource/importVisualModuleJs?moduleId=' + groupDocId)//上传接口
        , accept: 'file'
        , exts: 'js'
        , before: function (obj) { //obj参数包含的信息，跟 choose回调完全一致，可参见上文。
            obj.preview(function (index, file, result) {
                // console.log(index); //得到文件索引
                // console.log(file); //得到文件对象
                // console.log(result); //得到文件base64编码，比如图片
            });
        }
        , done: function (res) {
            //上传完毕回调
            layer.msg('上传JS成功');
            uploadJsList();
        }
        , error: function () {
            //请求异常回调
        }
    });
}

//上传JS列表
function uploadJsList() {
    importJsObj = {};//重置
    var jsonData = {
        "entity": {
            "templateId": groupDocId,
            "type": 'js'
        },
        "pager": {
            "current": 1,
            "size": 10
        }
    };
    ajax(service_prefix.manage, "/tplresource/jsList", "post", JSON.stringify(jsonData)).then(function (data) {
        if (data.success) {
            getData(data.obj.records, '#uploadJsListTemplate', '#uploadJsListView');
            //为了存放组件上传的js文件
            for (var i = 0; i < data.obj.records.length; i++) {
                var fileId = data.obj.records[i].id;
                var filePubUrl = data.obj.records[i].pubUrl;
                importJsObj[fileId] = filePubUrl;
            }
        } else {
            console.log(data.msg);
        }
    });
}

//上传JS删除
function uploadJsDel(id) {
    layer.open({
        title: '删除',
        content: '确定要删除吗？',
        btn: '',
        area: ['200px', '170px'],
        btn: ['确定', '取消'],
        btn1: function () {
            ajax(service_prefix.manage, "/tplresource/delete/" + id, "get").then(function (data) {
                if (data.success) {
                    layer.msg('删除成功');
                    uploadJsList();
                } else {
                    console.log(data.msg);
                }
            });
            layer.closeAll();
        },
        btn2: function () {
            layer.closeAll();
        }
    });
}

//上传图片弹出框
function uploadImgfile(id) {
    var title;
    if (id) {
        title = '编辑IMG';
    } else {
        title = '新建IMG';
    }
    layer.open({
        title: title,
        content: '<div id="imgFileView"></div>',
        btn: '',
        area: ['400px', '200px'],
        // btn: ['确定', '取消'],
        success: function (layero, index) {
            var data = [];
            getData(data, '#imgFileTemplate', '#imgFileView');
            if (id) {
                uploadImg('/tplresource/editImg/' + id);//编辑上传图片
            } else {
                uploadImg('/tplresource/importVisualModuleImg?moduleId=' + groupDocId);//新建上传图片
            }
            layui.form.render();
        }
    });
}

//上传图片功能
function uploadImg(url) {
    upload.render({
        elem: '#imgFileuploadBtn' //绑定元素
        , url: getAjaxUrl(service_prefix.manage, url)//上传接口
        , before: function (obj) { //obj参数包含的信息，跟 choose回调完全一致，可参见上文。
            console.log('bojf', obj);
            obj.preview(function (index, file, result) {
                // console.log(index); //得到文件索引
                // console.log(file); //得到文件对象
                // console.log(result); //得到文件base64编码，比如图片
            });
        }
        , done: function (res) {
            //上传完毕回调
            console.log('res', res);
            layer.msg('上传IMG成功');
            uploadImgList();
            // $("#img_show").attr('src','http://36.138.169.165:8081/'+res.obj.url);
            // $("#img_show").attr('src',com.jnetdata.url_prefix + res.obj.url);
            // $("#img_url").text(res.obj.url);
        }
        , error: function () {
            //请求异常回调
        }
    });
}

//上传图片-图片列表
function uploadImgList() {
    ajax(service_prefix.manage, "/tplresource/visualModuleImgList?moduleId=" + groupDocId, "post").then(function (data) {
        if (data.success) {
            getData(data.obj, '#uploadImgListTemplate', '#uploadImgListView');
        } else {
            console.log(data.msg);
        }
    });
}

//上传图片-删除
function uploadImgDel(id) {
    layer.open({
        title: '删除',
        content: '确定要删除吗？',
        btn: '',
        area: ['200px', '170px'],
        btn: ['确定', '取消'],
        btn1: function () {
            ajax(service_prefix.manage, "/tplresource/delete/" + id, "get").then(function (data) {
                if (data.success) {
                    layer.msg('删除成功');
                    uploadImgList();
                } else {
                    console.log(data.msg);
                }
            });
            layer.closeAll();
        },
        btn2: function () {
            layer.closeAll();
        }
    });
}

//查看css/js代码详情
function codeDetail(id, _this) {
    var curName = $(_this).parents('.upload_list').siblings('.code_box');
    $(_this).parents('.upload_list').hide();
    curName.show();
    ajax(service_prefix.manage, "/tplresource/" + id, "get").then(function (data) {
        if (data.success) {
            curName.find('.cur_name').html(data.obj.title + '.' + data.obj.type);
            curName.find('textarea').val(data.obj.content);
            curName.find('.code_save').attr('data-id', id);
        } else {
            console.log(data.msg);
        }
    });
}

///编辑css/js保存代码
function codeSave(_this) {
    var id = $(_this).attr('data-id');
    var code = $(_this).parent().siblings('textarea').val();
    codeDetailEdit(id, code, 'save');
}

//编辑css/js代码保存
function codeDetailEdit(id, code, type) {
    var jsonData = {
        "content": code
    };
    ajax(service_prefix.manage, "/tplresource/editVisualModule/" + id, "post", JSON.stringify(jsonData)).then(function (data) {
        if (data.success) {
            if (type == 'save') {
                layer.msg('代码保存成功');
            } else {
                console.log('代码图片路径替换');
            }
        } else {
            console.log(data.msg);
        }
    });
}

//点击显示js代码详情
function jsCodeDetail(_this) {
    $(_this).parents('.upload_list').hide();
    $(_this).parents('.upload_list').siblings('.code_box').show();
}

//返回css、js列表
function backList(_this) {
    $(_this).parents('.code_box').siblings('.upload_list').show();
    $(_this).parents('.code_box').hide();
}

uploadCssList();
uploadJsList();
uploadImgList();

//查找JS代码文件代码详情
function codeJsContent(id) {
    ajax(service_prefix.manage, "/tplresource/" + id, "get").then(function (data) {
        if (data.success) {
            codeJsontent = data.obj.content;//Js文件代码
            return codeJsontent;
        } else {
            console.log(data.msg);
        }
    });
}

//用html css js拼成一个节点
function getModuleHtml(html) {
    var htmlCode = '';
    // console.log('importCssObj', importCssObj);
    // console.log('importJsObj', importJsObj);
    //相关CSS文件
    if (importCssObj) {
        for (var k in importCssObj) {
            htmlCode += '<link rel="stylesheet" href="' + serviceIp + importCssObj[k] + '"></link>';
            (function () {
                var id = k;//文件ID
                //获取CSS文件中的代码
                ajax(service_prefix.manage, "/tplresource/" + id, "get").then(function (data) {
                    if (data.success) {
                        codeContent = data.obj.content;//文件代码 
                        //图片发布路径前缀
                        var imgPubUrl = 'url(' + serviceIp + '/files/visual/img/' + groupDocId + '/';
                        //替换CSS文件代码中的图片路径
                        var codeCssReplace = codeContent.replaceAll('url(img/', imgPubUrl);
                        // console.log('codeCssReplace',codeCssReplace);
                        codeDetailEdit(id, codeCssReplace);
                    } else {
                        console.log(data.msg);
                    }
                });
            })(k);//解决for循环引i指向问题
        }
    }
    //html文件代码
    if (html) {
        //图片发布路径前缀
        var imgPubUrl = 'src="' + serviceIp + '/files/visual/img/' + groupDocId + '/';
        //替换CSS文件代码中的图片路径
        var codeHtmlReplace = html.replaceAll('src="img/', imgPubUrl);
        htmlCode += '<div class="module_box">' + codeHtmlReplace + '</div>';
        // console.log('codeHtmlReplace',codeHtmlReplace);
    }
    //相关JS文件
    if (importJsObj) {
        for (var k in importJsObj) {
            htmlCode += '<script src="' + serviceIp + importJsObj[k] + '"></script>';
        }
    }
    $('.editPanel .groupDomBox').html(htmlCode);
}

$(function () {
    $('.codeView .layui-nav-item').click(function () {
        $(this).siblings().removeClass('layui-this');
        $(this).addClass('layui-this');
        var ind = $(this).index() + 1;
        $('.viewChild>div:nth-child(' + ind + ')').siblings().removeClass('active');
        $('.viewChild>div:nth-child(' + ind + ')').addClass('active');
    });

    getPerfix().then(function (res) {
        var activeid = getQueryString('id');
        if (activeid) {
            getGroupInfoById(activeid);
            $('#runCode').click(function () {
                synchCodeToView();
            });

        }
    });
});

//保存修改的组件
function saveActiveModule(data, id) {
    // var runCodeHtml = $('.groupDomBox').html();
    // console.log('runCodeHtml',runCodeHtml);
    if ($('.groupDomBox>div.module_box').length <= 0) {
        layer.msg('请先点击运行代码按钮，查看效果');
        return false;
    }
    formatDiv();
    new html2canvas($('.groupDomBox>div')[0]).then(canvas => {
        let oImg = new Image();
        oImg.src = canvas.toDataURL();  // 导出图片
        var obj = $('#groupFrom').serializeObject();
        obj.title = data.title;
        obj.outFileName = data.outFileName;
        obj.frame = data.frame;
        obj.vmType = data.vmType;
        obj.vcType = data.vcType;
        obj.netType = data.netType;
        obj.exteName = data.exteName;
        obj.proDes = data.proDes;
        obj.htmlCode = $('#htmlCode').val();
        // obj.htmlCoden = runCodeHtml;//组件拼合发布代码
        obj.cssCode = importCssObj;
        obj.jsCode = importJsObj;
        obj.img = oImg.src;
        obj.id = id;
        if (verificationInp(obj)) {
            saveEditData(obj);
        }
    });
}

//保存的数据
function saveEditData(param) {
    // console.log('param',param);
    ajax(service_prefix.visual, "/module/" + param.id, "put", JSON.stringify(param)).then(function (data) {
        var msg = data.msg;
        if (data.success) {
            msg = '保存成功';
        }
        showWinTips(msg);
    });
}

//点击保存
function saveModel() {
    $('.saveModel').click(function () {
        ajax(service_prefix.visual, "/module/" + groupDocId, "get").then(function (data) {
            if (data.success) {
                saveActiveModule(data.obj, groupDocId);
            }
        });
    });

}

//点击组件设置
function setGroup(data) {
    $('.rightHead .page_set').click(function () {
        console.log(groupObj);
        getLabels(function (data) {
            var labels = data.records;
            initSelect(labels, groupObj.vcType, groupObj.vmType);
        });
        setBaseGroup(data);
       
        
    });
}

//查询当前组件模板
function getGroupById(id, fn) {
    ajax(service_prefix.visual, "/module/" + id, "get").then(function (data) {
        if (!data.success) {
            showWinTips('数据获取失败');
            return;
        }
        if (fn) {
            fn(data.obj);
        }
    });
}


//选中并展示在页面上
function getGroupInfoById(id) {
    window.location.hash = 'id=' + id;
    // setPageCanEdit();
    getGroupById(id, function (data) {
        //保存当前正在修改的内容的id
        $('.rightHead .saveModel').attr('activeid', id);
        $('#htmlCode').val(data.htmlCode);
        synchCodeToView();
        $('title').html(data.title);
        console.log('获取到的组件基础信息', data);
        groupObj = data;
        setGroup(data);
        saveModel();
        //将信息展现在页面上
        for (var k in data) {
            if ($('#groupFrom [name=' + k + ']').length > 0) {
                // console.log('k',k,data[k]);
                $('#groupFrom [name=' + k + ']').val(data[k]);
            }
        }
        // console.log('pid',pid);
        // $("#groupFirstList").find("option[value="+pid+"]").prop("selected",true);
        layui.form.render();
    });
}

//从整体的html取截取html css js
// function getObjFromHtml(){
//     var js = $('.groupDomBox').children('script').html();
//     var css = $('.groupDomBox').children('style').html();
//     var html = $('.groupDomBox').children('div.module_box').html();
//     return {jsCode: js, cssCode: css, htmlCode: html};
// }

//从视图同步代码
// function synchViewToCode(){
//     var obj = getObjFromHtml();
//     $('#htmlCode').val(obj.htmlCode);
//     $('#cssCode').val(obj.cssCode);
//     $('#jsCode').val(obj.jsCode);
// }

//从代码同步视图
function synchCodeToView() {
    // var htmlCode = getModuleHtml($('#htmlCode').val());
    // $('.editPanel .groupDomBox').html(htmlCode);
    var htmlCode = $('#htmlCode').val();
    if (htmlCode != '') {
        getModuleHtml(htmlCode);
    }
    // else{
    //     layer.msg('html代码不能为空');
    // }
}

//生成图片前的处理
function formatDiv() {
    var wid = $('.groupDomBox>div.module_box').outerWidth();
    var hei = $('.groupDomBox>div.module_box').outerHeight();
    $('.groupDomBox>div.module_box').css('width', wid + 'px').css('height', hei + 'px');
}

//验证表单验证
function verificationInp(data) {
    var requiredKey = [
        { key: 'title', tips: '请输入组件名称！' },
        { key: 'outFileName', tips: '请输入输出文件名！' },
        { key: 'htmlCode', tips: '当前未获取到内容！' }
    ];
    for (var i = 0; i < requiredKey.length; i++) {
        if (!data[requiredKey[i].key]) {
            showWinTips(requiredKey[i].tips);
            return false;
        }
    }
    return true;
}

// function dragOverEvent(_this, e){
//     stopBubble(e);
//     $('.canEdied').removeClass('canEdied');
//     $(_this).addClass('canEdied');
// }
