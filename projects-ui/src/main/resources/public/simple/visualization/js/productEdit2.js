$(function(){
    //拖拽事件
    $('.domBodyBox').on('click', '.canEdit', function(e){
        stopBubble(e);
        if (!$(this).hasClass('canEdied')) {
            $('.canEdied').removeClass('canEdied');
        }
        $(this).toggleClass('canEdied');
        toggleEidtDesign();
    });
    $('.topTitle .layui-nav-item').click(function(){
        $(this).addClass('active');
        $(this).siblings().removeClass('active');
        if ($(this).hasClass('showCodeView')) {                         //展示代码视图
            synchViewToCode();
            $('.panelView .codeView').siblings().removeClass('active');
            $('.panelView .codeView').addClass('active');
        } else if ($(this).hasClass('showEditView')) {                  //展示可编辑视图
            codeToSynchView();
            $('.panelView .editPanel').siblings().removeClass('active');
            $('.panelView .editPanel').addClass('active');
            //重新渲染的时候也需要绑定事件
            bindDragEvent();
        } else {
            $('.panelView .helpView').siblings().removeClass('active');
            $('.panelView .helpView').addClass('active');
        }
	});
	//预览
	$('.topTitle .showPageView').click(function(){
        $('.topTitLeft .layui-nav').removeClass('layui-this');
        if ($('.panelView .pageView').siblings('.active').hasClass('showCodeView')) {
            codeToSynchView();
        } else if ($('.panelView .pageView').siblings('.active').hasClass('showEditView')) {
            synchViewToCode();
        }
		$('.panelView .pageView').siblings().removeClass('active');
        $('.panelView .pageView').addClass('active');
        var htmlCode = $.trim($('.domBodyBox').html());

        
        var imgReg = /<img.*?(?:>|\/>)/gi;
        // var srcReg = /src=[\'\"]?([^\'\"]*)[\'\"]?/i;
        var reg = new RegExp( imgReg , "g" )
        var htmlCode = htmlCode.replace( reg , '<img src="common/img/u6147.png" style="width:100%;" />');
        // console.log('newstr------->', htmlCode);
        var codeHtml = '<div>' + htmlCode + '</div>';
        $('.panelView .pageView').html(codeHtml);
	});
                    //删除当前选中元素
    $('.editPanel').on('click', '.deleteDom', function(){
        deleteEditedDom();
    });
                    //弹出编辑框
    $('.editPanel').on('click', '.designEditDom', function(){
        var codes = $('.editPanel .canEdied').attr('code');
		if (!codes && $('.editPanel .canEdied').hasClass('layoutBox')) {
			codes = 0;
		}
        showDesignWinByCode(codes);
    });
                    //弹出数据编辑框
    $('.editPanel .designEditData').click(function(){
        showDesiginDataWin();
    });
    
    $('.topTitle>span:nth-child(3)').click(function(){
        initAllGroup();
    });

    $('.leftCodeView>div').click(function(){
        $(this).siblings().removeClass('active');
        $(this).addClass('active');
        var ind = $(this).index() + 1;
        $(this).parent().next().children('div:nth-child(' + ind + ')').addClass('layui-show');
        $(this).parent().next().children('div:nth-child(' + ind + ')').siblings().removeClass('layui-show');
    });

    dragSize();
    //先初始化公共的css
    // commonCssBox();
    //初始化编辑页
    // initExistModel();
	
	//拖拽左边的拖拽图标事件
	$('.leftSide .dragleIcon').on('dargend', function(){
		getCodeById();
    });
    
    //设置导航弹框选中事件
    $('#designEditWinForm').on('click', '.styleChange > div', function(){
        $(this).siblings().removeClass('selected');
        $(this).addClass('selected');
    });
    //设置导航弹框选中事件
    $('#designEditWinForm').on('click', '.desgin_nav_styleChange > div', function(){
        $(this).siblings().removeClass('selected');
        $(this).addClass('selected');
    });
    //设置导航弹框选中事件
    $('#designEditWinForm').on('click', '.colorChange > span', function(){
        $(this).siblings().removeClass('selected');
        $(this).addClass('selected');
    });

    //设置数据来源框中的页签切换
    $('#designEditDataWin .label-check>span').click(function(){
        if ($(this).hasClass('label-check-active')) {
            return;
        }
        $(this).addClass('label-check-active');
        $(this).siblings().removeClass('label-check-active');
        var ind = $(this).index() + 1;
        $(this).parent().next().children('div:nth-child(' + ind + ')').siblings().addClass('layui-hide');
        $(this).parent().next().children('div:nth-child(' + ind + ')').removeClass('layui-hide');
    });
    // showDesignWinByCode();
    //页面加载,如果有内容也需要绑定一次
    bindDragEvent();

    //保存按钮点击事件
    $('.rightHead .saveModel').click(function(){
        if (verificationBasic()) {
            // if ($(this).attr('activeid')) {
            //     saveModuleData($(this).attr('activeid'));
            // } else {
            //     //如果是新建就直接打开一级分类选择框
            //     // openChooseModesWin();
            // }
            saveModuleData($(this).attr('activeid'));
        }
    });
    //删除按钮
    $('.rightHead .deleteModel').click(function(){
        deleteActiveTemplate($(this).siblings('.saveModel').attr('activeid'));
    });
    //编辑按钮
    // $('.rightHead .editModel').click(function(){
    //     setPageOnEdit();
    // });

    // setPageOnAdd();         //初始化为新增状态

    getPerfix().then(function(res){
        var activeid = getQueryString('id');
        if (activeid) {
            getModelInfoById(activeid);
        }
        // 获取所有的组件
	    initAllGroup();
    })
    
});
var myTree = new MyZTree();         //设置一个全局变量

//设置当前页面为正在编辑状态
// function setPageOnEdit(){
//     //编辑按钮显示和新增按钮隐藏
//     $('.rightHead>span.editModel').hide();
//     // $('.leftHead>span.addModel').hide();
//     //删除和保存显示
//     $('.rightHead>span.saveModel').show();
//     $('.rightHead>span.deleteModel').show();
// }
//设置当前页面为可编辑状态和可新增状态
// function setPageCanEdit(){
//     //编辑按钮和新增按钮显示
//     $('.rightHead>span.editModel').show();
//     // $('.leftHead>span.addModel').show();
//     //删除和保存隐藏
//     $('.rightHead>span.saveModel').hide();
//     $('.rightHead>span.deleteModel').hide();
// }
//设置当前页面为只可新增状态
// function setPageCanAdd(){
//     //新增按钮显示
//     // $('.leftHead>span.addModel').show();
//     //删除和保存和编辑隐藏
//     $('.leftHead>span.saveModel').hide();
//     $('.leftHead>span.deleteModel').hide();
//     $('.leftHead>span.editModel').hide();
// }
//设置当前页面为新增状态
// function setPageOnAdd(){
//      //删除按钮和新增按钮隐藏
//      $('.rightHead>span.deleteModel').hide();
//     //  $('.leftHead>span.addModel').hide();
//      $('.rightHead>span.editModel').hide();
//     //保存显示
//      $('.rightHead>span.saveModel').show();
// }

//删除当前模板
function deleteActiveTemplate(id){
    if (id === null || id === undefined) {
        return;
    }
    var name = $('#groupFrom input[name=title]').val();
    layer.confirm('确认删除' + name + '模板?', {icon: 3, title:'提示'}, function(index){
        layer.close(index);
        ajax(service_prefix.visual,"/template/" + id + "/batch","delete").then(function (data) {
            var msg = data.msg;
            if (data.success) {
                msg = '删除成功';
            }
			showWinTips(msg, function(){
                if (data.success) {
                    window.location.href = '/';
                }
            });
			
        });
    });
}

//查询当前组件模板
function getTemlateById(id, fn){
    ajax(service_prefix.visual,"/template/" + id,"get").then(function (data) {
        if (!data.success) {
            showWinTips('数据获取失败');
            return;
        }
        if (fn) {
            fn(data.obj);
        }
	});
}

//
function getObjFromHtml(){
    var js = $('.domBodyBox').children('script').html();
    var css = $('.domBodyBox').children('style').html();
    var html = $('.domBodyBox').children('.me_body').html();
    return {jsCode: js, cssCode: css, htmlCode: html};
}
//页面同步到代码
function synchViewToCode(){
    var obj = getObjFromHtml();
    $('#htmlCode').val(obj.htmlCode);
    $('#cssCode').val(obj.cssCode);
    $('#jsCode').val(obj.jsCode);
    setCodeViewLines(['#htmlCode', '#cssCode', '#jsCode']);

    // $('textarea[autoHeight]').autoHeight();  
}
//设置代码行
function setCodeViewLines(ids){
    for (var i = 0; i < ids.length; i++) {
        var areaRows = $(ids[i]).val().split("\n").length;
        if (areaRows < 10) {
            areaRows = 50;
        }
        var lins = '';
        for (var j = 1; j < areaRows + 10; j++) {
            lins += '<span>' + j + '</span>';
        }
        $(ids[i]).css('height', 21*areaRows + 'px');
        $(ids[i]).prev('.codeLine').html(lins);
        $(ids[i]).prev('.codeLine').css('height', 21*areaRows + 'px');
    }
}

//代码同步到网页
function codeToSynchView(){
    var htmlCode = getModuleHtml($('#cssCode').val(), $('#htmlCode').val(), $('#jsCode').val());
    $('.domBodyBox').html(htmlCode);
}

//获取到网页模板数据回显相关信息
function getModelInfoById(id){
    getTemlateById(id, function(data){
        // setPageCanEdit();
        //保存当前正在修改的内容的id
        $('.rightHead .saveModel').attr('activeid', id);
        var htmlCode = getModuleHtml(data.cssCode, data.htmlCode, data.jsCode);
        $('.domBodyBox').html(htmlCode);
        $('.domBodyBox .layoutBox').removeClass('noBorder');
        synchViewToCode(data);
        //将信息展现在页面上
        for (var k in data) {
            if ($('#groupFrom [name=' + k + ']').length > 0) {
                $('#groupFrom [name=' + k + ']').val(data[k]);
            }
        }
        layui.form.render();
        bindDragEvent();
    });
}

//验证基本内容
function verificationBasic(){
    var obj = $('#groupFrom').serializeObject();
    obj.htmlCode = $('#htmlCode').val();
    obj.cssCode = $('#cssCode').val();
    obj.jsCode = $('#jsCode').val();
    if (!verificationInp(obj)) {
        return false;
    }
    return true;
}

//保存已经编辑好的模板
function saveModuleData(id) {
    if ($('.editPanel .domBodyBox>.me_body').length <= 0) {
        return;
    }
    $('.domBodyBox .layoutBox').addClass('noBorder');
    new html2canvas($('.editPanel .domBodyBox>.me_body')[0]).then(canvas => {
        var obj = $('#groupFrom').serializeObject();
        obj.htmlCode = $('#htmlCode').val();
        obj.cssCode = $('#cssCode').val();
        obj.jsCode = $('#jsCode').val();
        let oImg = new Image();
        oImg.src = canvas.toDataURL();  // 导出图片
        obj.img = oImg.src;
        console.log('obj',obj);
        $('.domBodyBox .layoutBox').removeClass('noBorder');
        if (verificationInp(obj)) {
            if (!id) {
                // obj.type = $('#chooseFirstModesWin input[name=type]:checked').val();
                obj.type = $('select[name="type"]').val();
                createTemplate(obj);        //新建
            } else {
                obj.id = id;
                saveActTemplate(obj);        //修改
            }
        }
    });
}

//打开选择分类弹框
// function openChooseModesWin(){
//     $('#chooseFirstModesWin').show();
// } 

//动态绑定可拖放事件
function bindDragEvent(){
    $('.canEdit').each(function(){
        this.ondragover = function(e){
            dragOverEvent(this, e);
        }
    });
}

//增加字段
function addFilds(){
    var filds = myTree.getFileChecked();
    getFildsByIds(filds);
    closeDesignFildWin();
}

//根据字段id进行查询
function getFildsByIds(ids){
    if (!ids) {
        lodTpl('desiginFildCheckModel', 'desiginFildCheckDom', []);
        return;
    }
    var idsArr = [];
    for (var i = 0; i < ids.length; i++) {
        if (ids[i]) {
            idsArr.push(ids[i]);
        }
    }
    if (idsArr.length <= 0) {
        lodTpl('desiginFildCheckModel', 'desiginFildCheckDom', []);
        return;
    }
    ajax(service_prefix.metadata, '/fieldinfo/listByIds',  'post', JSON.stringify(idsArr)).then(function(res){
        if (res && res.obj) {
            lodTpl('desiginFildCheckModel', 'desiginFildCheckDom', res.obj);
        } else if (res && res.msg) {
            showWinTips(res.msg);
        } else {
            showWinTips('请求失败');
        }
        
    });
}

//打开增加字段弹框
function openAddFildWin(){
    var actDomId = $('.canEdied').attr('actdomid');
    getFildTreeByAjax(actDomId, actDomId);
}
//查询树节点
function getFildTreeByAjax(openDom) {
    var param = {webclass:1};
    ajax(service_prefix.manage, '/programa/getTreeData', 'post', JSON.stringify(param)).then(function(res){
        // var myTree = new MyZTree();
        if (res && (res.success || res.obj)) {
            myTree.setTreeinit(res, openDom);
            $('#designFildData').show();
        } else {
            var msg = '请求失败';
            if (res && res.msg) {
                msg = res.msg;
            }
            showWinTips(msg);
        }
    });
}

//向上 向下移动
function upDownListTr(_this, code){
    if (code == '-1' && $(_this).parent().parent().prev().length <= 0 || code == '1' && $(_this).parent().parent().next().length <= 0) {
        return;
    }
    var _thisTds = $(_this).parent().parent().children('td');
    var chageTds = null;
    if (code == '1') {                                 //与下一行交换
        chageTds = $(_this).parent().parent().next().children('td');
    } else if (code == '-1') {                        //与上一行交换
        chageTds = $(_this).parent().parent().prev().children('td');
    }
    for (var i = 0; i < chageTds.length; i++) {
        if ($(_thisTds[i]).children('input').length <= 0) {
            continue;
        }
        var _thisInp = $(_thisTds[i]).children('input');
        var changeInp = $(chageTds[i]).children('input');
        var _thisV = _thisInp.val();
        var prevV = changeInp.val();
        _thisInp.val(prevV);
        changeInp.val(_thisV);
        var _thisT = _thisInp.prev().text();
        var prevT = changeInp.prev().text();
        _thisInp.prev().text(prevT);
        changeInp.prev().text(_thisT);
    }
   
}

//删除当前行数据
function deleteListTr(_this){
    layer.confirm('确认删除当前数据?', {icon: 3, title:'提示'}, function(index){
        layer.close(index);
        $(_this).parent().parent().remove();
        layer.open({
            content: '删除成功',
            yes: function(index, layero){
              layer.close(index);
              $('.panelView .editDesign').hide();
            }
          }); 
    });
}
//编辑标签页
function editListTr(_this){
    $(_this).parent().siblings().children('input').removeClass('layui-hide');
    $(_this).parent().siblings().children('input').prev().addClass('layui-hide');
    $(_this).siblings('.delThisTr').removeClass('layui-hide');
    $(_this).siblings('.saveThisTr').removeClass('layui-hide');
    $(_this).addClass('layui-hide');
}

//保存标签页
function saveListTr(_this){
    var val = $(_this).parent().siblings().children('input').val();
    if (!$.trim(val)) {
        layer.open({
            content: '内容不能未空',
            yes: function(index, layero){
              layer.close(index);
            }
        }); 
        return;
    }
    $(_this).parent().siblings().children('input').addClass('layui-hide');
    $(_this).parent().siblings().children('input').each(function(){
        if ($(this).prev()[0].tagName == 'IMG') {
            $(this).prev().attr('src', this.value);
        } else {
            $(this).prev().text(this.value);
        }
        $(this).prev().removeClass('layui-hide');
    });
    // $(_this).parent().siblings().children('.objtext').text(val);
    $(_this).siblings('.delThisTr').addClass('layui-hide');
    $(_this).siblings('.eidtThisTr').removeClass('layui-hide');
    $(_this).addClass('layui-hide');
    // $(_this).parent().siblings().children('.objtext').removeClass('layui-hide');
}

//打开设置数据块内容
function showDesiginDataWin(){
    var childs = $('.canEdied').children();
    console.log('childs >>>',childs)
    // var datas = [];
    if ($('.canEdied').find('[datalist]').length > 0) {         //如果有标明数据源就获取指定数据源
        childs = $('.canEdied').find('[datalist]').children();
    }
    //显示当前用户可配置的项
    showTempCanConfigFild(childs[0]);
    //查询当前节点已经配置好的字段进行回显
    var actFildsId = $('.canEdied').attr('fildid');
    if (actFildsId) {
        getFildsByIds(actFildsId.split(','));
    } else {
        getFildsByIds('');
    }
    $('#designEditDataWin').show();
}
//显示当前用户可配置的项
function showTempCanConfigFild(fistChild){
    var objs = findChildData($(fistChild));
    var textconfig = '';
    console.log('fistChild >>',fistChild)
    console.log('objs >>',objs)
    //遍历当前模板可配置的项
    var ind = 1;
    for (var k in objs) {
        console.log('k >>',k)
        // textconfig += '第' + ind + '个可配置类型：'
        textconfig += ind + '=>';
        if (k.indexOf('url') > -1) {
            textconfig += '链接';
        }
        if (k.indexOf('img') > -1) {
            textconfig += '图片';
        }
        if (k.indexOf('title') > -1) {
            textconfig += '文字';
        }
        textconfig += '(';
        if (objs[k]) {
            if (objs[k].length > 4 && objs[k].indexOf('{{') == -1) {
                textconfig += objs[k].substring(0, 4) + '...'; 
            } else {
                textconfig += objs[k];  
            }
        } else {
            textconfig += '未配置';  
        }
        textconfig += ')';
        textconfig += '; ';
        ind ++;
    }
    $('#designEditDataWin .designConfigFild').text(textconfig);
}

//遍历节点获取数据 (此方法和获取节点模板方法相对应)
function findChildData(jqDom, obj){
    if (!obj) {
        obj = {};
    }
    jqDom.find('a').each(function(i, elem){
        obj['url'+i] = $(this).attr('href');
    });
    jqDom.find('img').each(function(i, elem){
        obj['img'+i] = $(this).attr('src');
    });
    var txtl = 0;
    jqDom.find('*').each(function(){
        if ($(this).children().length <= 0 && $.trim($(this).text())) {
            obj['title' + txtl] = $(this).text();
            txtl++;
        }
    });
    return obj;
}

//向节点中追加数据
function setEditData(){
    var fildsIds = '';
    var proNames = '';
    $('#desiginFildCheckDom input[name=id]').each(function(){
        fildsIds += this.value + ',';
    });
    $('#desiginFildCheckDom input[name=proName]').each(function(){
        proNames += this.value + ',';
    });
    var actDomId = myTree.getActDomId();
    var actGroupId = myTree.getGroupCheckId();
    var tableId = myTree.getCheckId();
    $('.canEdied').attr('fildid', fildsIds);
    $('.canEdied').attr('actdomid', actDomId);
    $('.canEdied').attr('groupid', actGroupId);
    $('.canEdied').attr('tableid', tableId);
    setFildInDom(proNames);     //组件关联字段
}

//将字段名称与Dom名称匹配
function setFildInDom(prokeys){
    var parents = $('.canEdied');
    if ($('.canEdied').find('[datalist]').length > 0) {             //如果有标明数据源就获取指定数据源
        parents = $('.canEdied').find('[datalist]');
    }
    var childs = parents.children();
    var unComClasN = findUnComClasN(childs); 
    var oneDom = findChildDom2($(childs[0]), unComClasN, prokeys);
    var htmlStr = '';
    childs.each(function(){
        htmlStr += oneDom;
    });
    parents.html(htmlStr);
    for(var i = 0; i < unComClasN.length; i++){
        parents.children(':nth-child(1)').addClass(unComClasN[i]);
    }
    closeDesignWin();
    synchViewToCode();
}

//格式化第一个节点，让其和数据关联,放回第一个节点的模板
function findChildDom2(jqDom, remveClasN, keys){
    var str = '';
    var cDom = jqDom.clone();
    var keysArr = keys.split(',');
    var ind = 0;
        //第一次的时候替换
    cDom.find('a').each(function(i, elem){
        if (keysArr[ind]) {
            $(this).attr('href', '{{' + keysArr[ind]  + '}}');
            ind ++;
        }
    });
    cDom.find('img').each(function(i, elem){
        if (keysArr[ind]) {
            $(this).attr('src', '{{' + keysArr[ind]  + '}}');
            ind ++;
            console.log($(this).attr('src'));
        }
    });
    cDom.find('*').each(function(){
        if ($(this).children().length <= 0 && $.trim($(this).text())) {
            if (keysArr[ind]) {
                $(this).text('{{' + keysArr[ind]  + '}}');
                ind ++;
            }
        }
    });
    str += '<' + jqDom[0].tagName.toLowerCase();
    if (jqDom.attr('style')) {
        str += ' style="' + jqDom.attr('style') + '"';
    }
    if (jqDom[0].className) {
        var clasN = jqDom[0].className;
        if (remveClasN && remveClasN.length > 0) {
            for (var i = 0; i < remveClasN.length; i++) {
                clasN = clasN.replace(remveClasN[i], '');
            }
            clasN = $.trim(clasN);
        }
        if (clasN) {
            str += ' class="' + clasN + '"';
        }
    }
    str += '>';
    str += cDom.html();
    //单标签不用闭合
    if (jqDom[0].tagName != 'IMG' && jqDom[0].tagName != 'INPUT') {
        str += '</' + jqDom[0].tagName.toLowerCase() + '>';
    }
    return str;
}

//将文字节点设置为模板
// function setTextToTem2(dom, ind, keysArr){
//     if (ind === null || ind === undefined) {
//         ind = 0;
//     }
//     // console.log('************');
//     // console.log(keysArr);
//     // console.log(ind);
//     dom.each(function(){
//         if ($(this).children().length > 0 && keysArr && keysArr[ind]) {
//             setTextToTem2($(this).children(), ind, keysArr);
//         } else if ($(this).text() && keysArr && keysArr[ind]){
//             $(this).text('{{' + keysArr[ind] + '}}');
//             ind++;
//             console.log('************');
//             console.log(ind);
//             console.log('************');
//         }
//     });
   
// }

//将对象字符串转换成对象
function objStrToXhrStr(objStr){
    var str = objStr.replace('{', '');
    str = str.replace('}', '');
    str = str.replace(/"|'|\s/g, '');
    str = str.replace(/:/g, '=');
    str = str.replace(/,/g, '&');
    return str;
}
//获取接口返回的数据
function getDatasByInterface(){
    var datasObj = $('#interfaceForm').serializeObject();
    if (!datasObj.url) {
        layer.open({
            content: '接口不能为空！',
            yes: function(index, layero){
              layer.close(index);
            }
        });
        return;
    }
    if (!datasObj.method) {
        datasObj.method = 'POST';       //默认POST
    }
    var params = {};
    if (datasObj.param) {
        try {
            params = objStrToXhrStr(datasObj.param);
        } catch (error) {
            layer.open({
                content: '参数格式有误，请检查！',
                yes: function(index, layero){
                  layer.close(index);
                }
            });
            return;
        }
    }
    var xhr=new XMLHttpRequest();
        xhr.open(datasObj.method,datasObj.url,false);
        // 新建http头，发送信息至服务器时内容编码类型
        if (datasObj.contype) {
            xhr.setRequestHeader('Content-Type',datasObj.contype);
        } else {
            xhr.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
        }
        xhr.onreadystatechange=function(){
            if (xhr.readyState==4){
                if (xhr.status==200){
                    $('#designEditDataWin .dataResult').html(xhr.responseText);
                } else {
                    $('#designEditDataWin .dataResult').empty();
                    $('#designEditDataWin .dataResult').append('<span style="color: red">发生了一个错误：</span>')
                    $('#designEditDataWin .dataResult').append('<div>状态码：' + xhr.status +'</div>');
                    if (xhr.statusText) {
                        $('#designEditDataWin .dataResult').append('<div>状态描述：' + xhr.statusText +'</div>');
                    }
                    if (xhr.responseText) {
                        $('#designEditDataWin .dataResult').append('<div>返回内容：' + xhr.responseText +'</div>');
                    }
                }
            }
        }
        if (datasObj.param) {
            xhr.send(params);
        } else {
            xhr.send();
        }
}
//获取自己设置的数据
function getDatasByDefined(fn){
    //判断数据是否保存
    var saveBtns = $('#desiginDataTbodyDom .saveThisTr');
    for (var i = 0; i < saveBtns.length; i++) {
        if (!$(saveBtns[i]).hasClass('layui-hide')){
            layer.open({
                content: '您还有数据未保存，请先保存！',
                yes: function(index, layero){
                  layer.close(index);
                }
            }); 
            return;
        }
    }
    var datas = [];
    var tit = [];
    $('#desiginDataTbodyDom thead th').each(function(){
        if ($(this).attr('key')) {
            tit.push($(this).attr('key'));
        }
    });
    $('#desiginDataTbodyDom tbody tr').each(function(){
        var obj = {};
        $(this).children('td').children('input[name]').each(function(){
            obj[this.name] = this.value;
        });
        // var obj = {title: $(this).children('.dataName').children('input').val()};
        // if (tit.indexOf('img') > -1) {
        //     obj['img'] = $(this).children('.dataImg').children('input').val();
        // }
        // if (tit.indexOf('url') > -1) {
        //     obj['url'] = $(this).children('.dataUrl').children('input').val();
        // }
        datas.push(obj);
    });
    if (fn) {
        fn(datas);
    }
}

//根据数据初始化节点
// function initDomBydatas(data){
//     var parents = $('.canEdied');
//     if ($('.canEdied').find('[datalist]').length > 0) {             //如果有标明数据源就获取指定数据源
//         parents = $('.canEdied').find('[datalist]');
//     }
//     var childs = parents.children();
//     var htmlStr = '';
//     var unComClasN = findUnComClasN(childs);                        //查找第一个不是公共类名的class,生成模板的时候剔除掉
//     // formartFirstTemplate($(childs[0]));
//     var domStr = findChildDom($(childs[0]), unComClasN);            //查找Dom节点模板
//     /**********根据数据和模板组装dom节点Start*********/
//     for (var i = 0; i < data.length; i++) {
//         var obj = data[i];
//         var oneDom = domStr;
//         for (var k in obj) {
//             var reg = new RegExp('{{' + k + '}}', 'g');
//             oneDom = oneDom.replace(reg, obj[k]);
//         }
//         htmlStr += oneDom;
//     }
//     parents.html(htmlStr);
//     /**********根据数据和模板组装dom节点END*********/
//     for(var i = 0; i < unComClasN.length; i++){
//         parents.children(':nth-child(1)').addClass(unComClasN[i]);
//     }
//     closeDesignWin();
// }

//查找jqDom节点集合不是公共类名的class名,为了生成模板的时候剔除掉
function findUnComClasN(jqDom){
    var unComClasN = [];           
    if (jqDom.length < 2) {
        return unComClasN;
    }
    var classObj = [];
    jqDom.each(function(){
        classObj = classObj.concat(this.className.split(' '));
    });
    jqDom.each(function(){
        for (var i = 0; i < classObj.length; i++) {
            if (!classObj[i]) {
                continue;
            }
            if (!$(this).hasClass(classObj[i]) && unComClasN.indexOf(classObj[i]) == -1) {
                unComClasN.push(classObj[i]);
            }
        }
    });
    return unComClasN;
}

//格式化第一个节点，让其和数据关联,放回第一个节点的模板
// function findChildDom(jqDom, remveClasN){
//     var str = '';
//     var cDom = jqDom.clone();
//         //第一次的时候替换
//     cDom.find('a').each(function(i, elem){
//         $(this).attr('href', '{{url' + i + '}}');
//     });
//     cDom.find('img').each(function(i, elem){
//         $(this).attr('src', '{{img' + i + '}}');
//     });
//     setTextToTem(cDom);
//     str += '<' + jqDom[0].tagName.toLowerCase();
//     if (jqDom.attr('style')) {
//         str += ' style="' + jqDom.attr('style') + '"';
//     }
//     if (jqDom[0].className) {
//         var clasN = jqDom[0].className;
//         if (remveClasN && remveClasN.length > 0) {
//             for (var i = 0; i < remveClasN.length; i++) {
//                 clasN = clasN.replace(remveClasN[i], '');
//             }
//             clasN = $.trim(clasN);
//         }
//         if (clasN) {
//             str += ' class="' + clasN + '"';
//         }
//     }
//     str += '>';
//     str += cDom.html();
//     str += '</' + jqDom[0].tagName.toLowerCase() + '>';
//     return str;
// }

//将文字节点设置为模板
// function setTextToTem(dom, textLen){
//     if (textLen === null || textLen === undefined) {
//         textLen = 0;
//     }
//     dom.each(function(){
//         if ($(this).children().length > 0) {
//             setTextToTem($(this).children(), textLen);
//         } else if ($(this).text()){
//             $(this).text('{{title' + textLen + '}}');
//             textLen ++;
//         }
//     });
// }

//设置样式的左侧导航切换
function changeLeftForm(_this){
    $(_this).addClass('active');
    $(_this).siblings().removeClass('active');
    var ind = $(_this).index() + 1;
    $('.divRight>div:nth-child(' + ind + ')').addClass('layui-show');
    $('.divRight>div:nth-child(' + ind + ')').siblings().removeClass('layui-show');
}

//用html css js拼成一个节点html网页
function getModuleHtml(css, html, js){
    var htmlCode = '<style>' + css + '</style>';
    htmlCode += '<div class="me_body">' + html + '</div>';
    htmlCode += '<script>' + js + '</script>';
    return htmlCode;
}
//组装组件节点
function getGroupHtml(css, html, js){
    var htmlCode = '<style scoped>' + css + '</style>';
    htmlCode += html;
    htmlCode += '<script>' + js + '</script>';
    return htmlCode;
}

//根据id获取当前的拖拽的代码内容
function getCodeById(id){
    //拖拽
    console.log('组件id:'+id);
    getGroupById(id, function(data){
        dropEnd(id, getGroupHtml(data.cssCode, data.htmlCode, data.jsCode));
    });
}

//设置当前选中的样式
function setEditStyle(){
    if ($('.canEdied').hasClass('me_ui_nav_label') || $('.canEdied').hasClass('me_ui_nav_ul')) {      //标签样式
        var bgColor = $('#designEditWinForm .colorChange>.selected').attr('data');
        var styles = $('#designEditWinForm .styleChange>.selected').attr('data');
        if (!styles) {
            styles = $('#designEditWinForm .desgin_nav_styleChange>.selected').attr('data');
        }
        setLabelStyleClass(bgColor, styles);
    }
    //自定义样式设置
    setDefinedStyle();
    closeDesignWin();
    synchViewToCode();
}

//input的name名和css里面的name名的映射关系 code:1 -> 属性名获取input的name名, code:2 ->input的name名获取css的属性名, 默认1
function cssNinpNrelation(k, code){
    var relation = {
        'marginLeft': 'margin-left',
        'marginTop': 'margin-top',
        'borderRadius': 'border-radius',
        'paddingTop': 'padding-top',
        'paddingBottom': 'padding-bottom',
        'paddingLeft': 'padding-left',
        'paddingRight': 'padding-right',
        'backgroundColor': 'background-color',
        'backgroundImage': 'background-image'
    };
    if (!k) {
        return k;
    }
    if (!code) {
        code = 1;
    }
    //从css的属性名获取input的name名
    if (code == 1) {
        for (var r in relation) {
            if (relation[r] == k) {
                return r;
            }
        }
    }
    //从input的name名获取css的属性名
    if (code == 2) {
        if (relation[k]) {
            return relation[k];
        }
    }
    return k;
}

//根据选中的框初始化自定义的样式
function initDefinedStyleByEdit(){
    //初始化
    $('#changLabelForm input[value=selfDefind]').each(function(){
        $(this).parent().nextAll().addClass('layui-hide');
    });
    $('#changLabelForm input[type=radio]').each(function(){
        if (this.value == 'default') {
            this.checked = true;
        } else {
            this.checked = false;
        }
    });
    //设置选中框的默认样式
    if ($('.canEdied').length > 0){
        var data = $('.canEdied').attr('style');
        if (!data) {return;}
        var dataObj = {};
        var arr = data.split(';');
        for (var i = 0; i < arr.length; i++) {
            var s = $.trim(arr[i].split(':')[0]);
            if (!s) {
                continue;
            }
            var v = $.trim(arr[i].split(':')[1]);
            if ($('#changLabelForm input[name=' + cssNinpNrelation(s, 1) + ']').length > 0) {
                if (cssNinpNrelation(s, 1) == 'backgroundImage') {
                    $('#changLabelForm input[name=' + cssNinpNrelation(s, 1) + ']').val(v.replace('url(', '').replace(')', ''));
                } else {
                    $('#changLabelForm input[name=' + cssNinpNrelation(s, 1) + ']').val(v);
                }
                var way = $('#changLabelForm input[name=' + cssNinpNrelation(s, 1) + 'Way]');
                if (s.indexOf('-') != -1 && way.length <= 0) {
                    way = $('#changLabelForm input[name=' + s.split('-')[0] + 'Way]');
                } 
                way.each(function(){
                    if (v && this.value == 'selfDefind') {
                       this.checked = true;
                       $(this).parent().next().removeClass('layui-hide');
                    } else {
                        this.checked = false;
                    }
                    if (v && s == 'background-color') {
                        fillWayChangeEvent('backgroundColor');
                        $('select[name=fillWay]').val('backgroundColor');
                        renderForm('select');
                    }
                    if (v && s == 'background-image') {
                        fillWayChangeEvent('backgroundImage');
                        $('select[name=fillWay]').val('backgroundImage');
                        renderForm('select');
                    }
                });
            }
        }
    }
}

//重新渲染表单函数
function renderForm(label) {
    layui.use('form', function() {
        var form = layui.form; //高版本建议把括号去掉，有的低版本，需要加()
        form.render(label);
    });
}

//设置自定义样式
function setDefinedStyle(){
    if ($('.canEdied').length <= 0) {
        return;
    }
    var cssObj = $('#changLabelForm').serializeObject();
    var newObj = {};
    var myCss = $('.canEdied').attr('style');
    var editCss = [];
    if (myCss) {
        editCss = myCss.split(';');
    }
    //获取现有的样式
    for (var i = 0; i <editCss.length; i++) {
        var k = $.trim(editCss[i].split(':')[0]);
        if (!k) {
            continue;
        }
        var v = $.trim(editCss[i].split(':')[1]);
        var transK = cssNinpNrelation(k, 1);
        newObj[transK] = v;
    }
    //获取设置框中的样式
    for (var c in cssObj) {
        var k =  c;
        if (cssObj[k+'Way'] && cssObj[k+'Way'] == 'selfDefind' && $.trim(cssObj[k])) {
            newObj[k] = $.trim(cssObj[k]);
        }
        if (k.indexOf('Way') == -1 && cssObj[k] && $.trim(cssObj[k])) {
            newObj[k] = $.trim(cssObj[k]);
        }
    }
    //新的样式拼接
    var newStyle = '';
    var isRepeat = '';
    for (var n in newObj) {
        var tarsK = cssNinpNrelation(n, 2);
        if (n == 'backgroundImage') {
            if (isRepeat.indexOf(',' + tarsK + ',') == -1) {
                newStyle += tarsK + ':url(' + newObj[n] + ');';
                isRepeat += ',' + tarsK + ',';
            }
        } else {
            if (isRepeat.indexOf(',' + tarsK + ',') == -1) {
                newStyle += tarsK + ':' + newObj[n] + ';';
                if (tarsK == 'height') {
                    newStyle += 'min-height:auto;';          //重置最小高度
                }
                isRepeat += ',' + tarsK + ',';
            }
            
        }
    }
    $('.canEdied').attr('style', newStyle);
}

//设置标签切换的样式(colorClass: 皮肤颜色的class名， sClas: 页签样式的class名)
function setLabelStyleClass(colorClass, sClas){
    if (colorClass) {
        $('.canEdied').removeClass('bgDefault');
        for (var i = 0; i < 13; i++) {
            $('.canEdied').removeClass('bgColor'+i);
        }
        $('.canEdied').addClass(colorClass);
    }
    if (sClas) {
        $('.canEdied').removeClass('styleDefault');
        for (var i = 1; i < 15; i++) {
            $('.canEdied').removeClass('style'+i);
            $('.canEdied').removeClass('me_nav_style'+i);
        }
        $('.canEdied').addClass(sClas);
    }
}

//关闭设置面板
function closeDesignWin() {
    $('#designEditWin').hide();
    $('#designEditDataWin').hide();
    // $('#chooseFirstModesWin').hide();
}

function closeDesignFildWin(){
    $('#designFildData').hide();
}

/*
    //显示设置弹框
    code: 1  ->布局
    code: 2  ->按钮
    code: 3  ->文字
    code: 4  ->切换标签
*/
function showDesignWinByCode(code) {
    if (code == null || code == undefined) {
        code = '0';
    }
    var showEdit = ',1,';          //当前选中元素可编辑的样式 ,1, ==> 可编辑自定义样式（一般针对框） ,2, ==> 可编辑文本样式 ,3, ==> 可编辑切换页签样式
    if ($('.editPanel .canEdied').attr('showedit')) {
        showEdit = $('.editPanel .canEdied').attr('showedit');
    }
    lodTpl('desiginEditModel', 'designEditWinForm', showEdit);
    changeLeftForm($('.divLeft>ul>li:nth-child(1)')[0]);
    //绑定事件
    bindDesginFormEvent();
    //根据选中框初始化自定义样式内容
    initDefinedStyleByEdit();
    if (document.getElementById('desgin_nav_carousel')) {
        var myCarousel = new MyCarousel('#desgin_nav_carousel');
        myCarousel.init();
    }
    if (document.getElementById('bgColor')) {
        layui.use('colorpicker', function(){
            var colorpicker = layui.colorpicker;
            //渲染
            colorpicker.render({
              elem: '#bgColor'  //绑定元素
              ,color: $('#changLabelForm input[name=backgroundColor]').val()
              ,change: function(color){
                $('#changLabelForm input[name=backgroundColor]').val(color);
              }
            });
        });
    }
    $('#designEditWin').show();
    
}

function bindDesginFormEvent(){
    //绑定表单中的事件
    $('#changLabelForm input[type=radio]').bind('click', function(){
        if (this.checked && this.value == 'selfDefind') {
            $(this).parent().parent().find('input[value=selfDefind]').parent().next().removeClass('layui-hide');
        } else {
            $(this).parent().parent().find('input[value=selfDefind]').parent().nextAll().addClass('layui-hide');
        }
    });
    //绑定背景方式选择方式
    var form = layui.form;
    form.on('select(fillWay)', function(data){
        fillWayChangeEvent(data.value);
    });
}

//背景方式选择changge事件
function fillWayChangeEvent(v){
    $('#changLabelForm .bgColorBox').addClass('layui-hide');
    $('#changLabelForm input[name=backgroundImage]').parent().addClass('layui-hide');
    if (v == 'backgroundColor'){
        $('#changLabelForm .bgColorBox').removeClass('layui-hide');
    }
    if (v == 'backgroundImage'){
        $('#changLabelForm input[name=backgroundImage]').parent().removeClass('layui-hide');
    }
}
//删除选中元素
function deleteEditedDom(){
    layer.confirm('确认删除?', {icon: 3, title:'提示'}, function(index){
        $('.editPanel .canEdied').prev('style').remove();
        $('.editPanel .canEdied').next('script').remove();
        $('.editPanel .canEdied').remove();
        layer.close(index);
        layer.open({
            content: '删除成功',
            yes: function(index, layero){
              layer.close(index);
              $('.panelView .editDesign').hide();
            }
          }); 
    });
}

//根据是否有选中元素展示设计按钮
function toggleEidtDesign(){
    if ($('.editPanel .canEdied').length <= 0) {
        $('.editPanel .editDesign').hide();
    } else {
        //放左下角
        var left = $('.editPanel .canEdied').offset().left - $('.editPanel').offset().left;
        var top = $('.editPanel .canEdied').offset().top - $('.editPanel').offset().top + $('.editPanel .canEdied').outerHeight();
        $('.editPanel .editDesign').css({'left': left + 'px', 'top': top + 'px'}).show();
        if ($('.editPanel .canEdied').attr('editdata') && $('.editPanel .canEdied').attr('editdata') == '1') {
            $('.editDesign .designEditData').show();
        } else {
            $('.editDesign .designEditData').hide();
        }
    }
}

//新增网页模板
function createTemplate(param){
    ajax(service_prefix.visual,"/template","post", JSON.stringify(param)).then(function (data) {
        var msg = data.msg;
        if (data.success) {
            msg = '新建成功';
            // initAllGroup();
            // initDataForNew();
        }
        showWinTips(msg, function(){
            if (data.success) {
                closeDesignWin();
                // $('.domBodyBox .layoutBox').removeClass('noBorder');
              }
        });
	});
}
//修改内容
function saveActTemplate(param){
    ajax(service_prefix.visual,"/template/" + param.id + "/allColumn","put", JSON.stringify(param)).then(function (data) {
        var msg = data.msg;
        if (data.success) {
            msg = '修改成功';
        }
        showWinTips(msg, function(){
            if (data.success) {
                // $('.domBodyBox .layoutBox').removeClass('noBorder');
              }
        });
	});
}
//验证表单验证
function verificationInp(data){
    var requiredKey = [
        {key: 'title', tips: '请输入产品名称！'},
        {key: 'netType', tips: '请选择网页类型！'},
        {key: 'outFileName', tips: '请输入输出文件名！'},
        {key: 'exteName', tips: '请输入扩展名！'},
        {key: 'htmlCode', tips: '当前未获取到内容！'}
    ];
    for (var i = 0; i < requiredKey.length; i++) {
        if (!data[requiredKey[i].key]) {
            showWinTips(requiredKey[i].tips);
            return false;
        }
    }
    return true;
}
//公共css
function commonCssBox(){
    var cssCode = '.editBtn{display: inline-block;border: 1px solid #C9C9C9;background-color: #fff;color: #555;height: 38px;line-height: 38px;padding: 0 18px;cursor: pointer;}';
    cssCode += '.editBox {display: inline-block;vertical-align: middle;width: 100px;height: 100px;}';
    cssCode += '.firstEdit .canEdit {box-sizing: border-box;position: absolute;}.canEdit.firstEdit {position: relative;}';
    cssCode += '.editDesign {display:none;}'
    // cssCode += '.firstEdit .canEdit {box-sizing: border-box;}.canEdit.firstEdit {position: relative;}';
    $('#cssCode').val(cssCode);
}

function dragOverEvent(_this, e){
    stopBubble(e);
    $('.canEdied').removeClass('canEdied');
    $(_this).addClass('canEdied');
}

//拖拽事件结束后统一调用（新建元素）
function dropEnd(dataid, html){
    // if (code == '1') {                                  //追加布局
    //     if ($('.canEdied').hasClass('domBodyBox')) {
    //         $('.canEdied').append('<div class="canEdit editBox firstEdit" code="' + code + '" ondragover="dragOverEvent(this, event)"><span class="layoutSize"></span></div>');
    //     } else {
    //         $('.canEdied').append('<div class="canEdit editBox" code="' + code + '" ondragover="dragOverEvent(this, event)"><span class="layoutSize"></span></div>');
    //     }
    // } else if (code == '2') {                           //追加按钮
    //     $('.canEdied').append('<div class="canEdit editBtn" code="' + code + '">按钮</div>');
    // } else if (code == '3' && txt) {                    //追加文字
    //     $('.canEdied').append('<span class="canEdit txt" code="' + code + '">' + txt + '</span>');
    // } else if (html) {
    // $('.canEdied').attr();
    // $(html).attr('dataid', dataid);
    if ($('.canEdied').hasClass('domBodyBox')) {
        $('.canEdied').append('<div class="me_body">' + html + '</div>');
    } else {
        $('.canEdied').append(html);
    }
    $('.canEdied').removeClass('canEdied');
    bindDragEvent();
    synchViewToCode();
}

//拖拽
function dragSize(){
    var x0, y0, x1, y1;
    var draging = false;
    var _thisDiv = null;
    var darg = false;
    var divMove = null;
    $('.editPanel').on('mousedown', '.layoutSize', function(e){
        stopBubble(e);
        x0 = e.clientX;
        y0 = e.clientY;
        _thisDiv = $(this).parent();
        darg = true;
    });
    $('.editPanel').on('mousedown', '.canEdit.absPos', function(e){
        stopBubble(e);
        x0 = e.clientX;
        y0 = e.clientY;
        divMove = $(this);
        darg = true;
    });
    $('.editPanel').on('mousemove', function(e){
        if (darg && !draging) {
            draging = true;
            x1 = e.clientX;
            y1 = e.clientY;
            //布局框大小
            if (_thisDiv && _thisDiv.length > 0) {
                var wid = _thisDiv.outerWidth() + (x1 - x0);
                var hei = _thisDiv.outerHeight() + (y1 - y0);
                _thisDiv.css({'width': wid + 'px', 'height': hei + 'px','minHeight': 'auto'});
            }
            //元素移动
            if (divMove && divMove.length > 0) {
                var left = divMove.position().left + (x1 - x0);
                var top = divMove.position().top + (y1 - y0);
                divMove.css({'left': left + 'px', 'top': top + 'px'});
            }
            x0 = x1;
            y0 = y1;
            draging = false;
        }
    });
    document.addEventListener('mouseup', function(){
        darg = false;
        _thisDiv = null;
        divMove = null;
        x0 = 0;
        y0 = 0;
        x1 = 0;
        y1 = 0;
    });
}
//导航设置框里面的左右滚动方法
function MyCarousel(id){
    this.left = 0;
    this.speed = 1000;
    this.activeInd = 0;
    this.isMoving = false;
    this.len = $(id).find('.desgin_carousel_move').children().length;
    var _this = this;
    this.init = function(){
        $(id).children('.desgin_carousel_left').unbind('click').click(function(){
            _this.prevSlide();
        });
        $(id).children('.desgin_carousel_right').unbind('click').click(function(){
            _this.nextSlide();
        });
    };
    this.nextSlide = function(){
        if (this.canRemove(1)) {
            var oneWid = $(id).children('.desgin_carousel_item').outerWidth();
            this.left = this.left - oneWid;
            this.move();
        }
    };
    this.prevSlide = function(){
        if (this.canRemove(-1)) {
            var oneWid = $(id).children('.desgin_carousel_item').outerWidth();
            this.left = this.left + oneWid;
            this.move();
        }
    };
    this.canRemove = function(code){
        if (code == '-1' && this.left >= 0) {
            return false;
        } 
        if (code == 1 &&  this.left*-1 >= $(id).children('.desgin_carousel_item').outerWidth() * (this.len-1)){
            return false;
        }
        return true;
    };
    this.move = function(){
        if (!this.isMoving) {
            this.isMoving = true;
            $(id).find('.desgin_carousel_move').animate({left: this.left + 'px'}, this.speed, function(){
                _this.isMoving = false;
            });
        }
    };
}

//生成ztree树
function MyZTree(){
    var _this = this;
    this.treeId = 'fildTree';
    this.actTableId = '';
    this.actGroupId = '';
    this.actDomId = '';
    //返回选中的tableid,用于查询字段
    this.getCheckId = function(){
        return this.actTableId;
    }
    this.getActDomId = function(){
        return this.actDomId;
    }
    this.getGroupCheckId = function(){
        return this.actGroupId;
    }
    this.setActDomId = function(v){
        this.actDomId = v;
    };
    this.setCheckId = function(v){
        this.actTableId = v;
    }
    this.setGroupCheckId = function(v){
        this.actGroupId = v;
    }
    this.clearNodeInfo = function(domId){
        this.setCheckId('');
        this.setGroupCheckId('');
        this.setActDomId(domId);
    }
    this.setActNodeInfo = function(node){
        this.setCheckId(node.tableId);
        this.setGroupCheckId(node.id);
        this.setActDomId(node.tId);
    }
    //查询当前节点的数据
    this.getActiveFildsData = function(checkdIds){
        var tableid = this.getCheckId();
        if (!tableid) {
            // showWinTips('当前节点无字段信息');
            lodTpl('desiginFildModel', 'designFildDom', {showTit:[], data: null, checked: ''});
            return;
        }
        ajax(service_prefix.metadata,"/fieldinfo/all?tableId="+tableid,"get").then(function (data) {
            var showTit = [
                {title: '字段名', attrkey: 'anothername'},
                {title: '字段变量名', attrkey: 'proName'},
                {title: '字段类型', attrkey: 'fieldTypeStr'}
            ];
            var datas = {showTit: showTit, data: data.obj, checked: checkdIds};
            lodTpl('desiginFildModel', 'designFildDom', datas);
        });
    }
    this.zTreeOnClick1 = function(event, treeId, treeNode, checkedid){
        var treeObj=$.fn.zTree.getZTreeObj(treeId);
        treeObj.cancelSelectedNode();
        treeObj.selectNode(treeNode, true);
        treeObj.checkNode(treeNode, true);
        _this.setActNodeInfo(treeNode);
        if (!checkedid || typeof checkedid == 'number') {
            checkedid = '';
        }
        _this.getActiveFildsData(checkedid);
    }
    this.settingss = {
        data: {
          simpleData: {
            enable: true,           //true 、 false 分别表示 使用 、 不使用 简单数据模式
            idKey: "id",            //节点数据中保存唯一标识的属性名称
            pIdKey: "parentId",     //节点数据中保存其父节点唯一标识的属性名称
            rootPId: -1             //用于修正根节点父节点数据，即 pIdKey 指定的属性值
          },
          key: {
            name: "name"            //zTree 节点数据保存节点名称的属性名称  默认值："name"
          }
        },
        check: {                    //表示tree的节点在点击时的相关设置
            enable: true,           //是否显示radio/checkbox
            autoCheckTrigger: false,
            chkStyle: "radio",      //值为checkbox或者radio表示
            radioType:"all",
            chkboxType: {"Y": "", "N": ""}  //表示父子节点的联动效果，不联动
        },
        view: {showLine: false, showIcon: true},
        callback:{
          onClick:_this.zTreeOnClick1,
          onCheck:_this.zTreeOnClick1
        }
      };
    this.setTreeinit = function(data, defaultOpenDom) {
        delTreeChildren1(data.obj);
        zTreeObj = $.fn.zTree.init($('#' + this.treeId), this.settingss, data.obj); //初始化树
        if (!defaultOpenDom) {
            defaultOpenDom = 'fildTree_4';
        }
        //初始化设置
        _this.clearNodeInfo(defaultOpenDom);
        var node = zTreeObj.getNodeByTId(defaultOpenDom);
        zTreeObj.expandAll(true);                                   //true 节点全部展开、false节点收缩
        zTreeObj.selectNode(node);
        _this.settingss.callback.onClick(null, zTreeObj.setting.treeId, node, $('.canEdied').attr('fildid'));   //$('.canEdied').attr('fildid')进行回显
        
    }
    this.getFileChecked = function(){   //获取当前勾选的内容
        var filds = [];
        $('#designFildDom input[name=filds]:checked').each(function(){
            filds.push(this.value);
        });
        return filds;
    }
}
function searchId(param){
    var params = {
        "entity": {
            "pid":param
        },
        "pager": {
            "current": 1,
            "size": 1000,
            "sortProps":{
                key:"modifyTime",
                value: false
            }
        }
    }
    ajax(service_prefix.visual,"/module/list","post",JSON.stringify(params)).then(function (data) {
        console.log(data.obj.records);
        var data = data.obj.records
        var htmlStr=''
        for (var i = 0;i<data.length;i++){
            htmlStr += `<li id="${data[i].id}"><span ><img src="/simple/visualization/img/icoItemText.png" ></span>${data[i].title}</li>`
            // htmlStr += `<li ><span ><img src="/simple/visualization/img/icoItemText.png" ></span>${data[i].title}</li>`
        }
        
        $(".componentBox .tabCon ul").html(htmlStr);

        $("#238").click(function () {
            let widget = new WidgetImage(getParent());
            if (widget.widgetId) {
                widgetArray.push(widget);
            }
        });
        $("#155").click(function () {
            console.log('155绑定点击事件');
            let widget = new WidgetSkySearch(getParent());
            if (widget.widgetId) {
                widgetArray.push(widget);
            }
        });
        $("#260").click(function () {
            console.log('260绑定点击事件');
            let widget = new WidgetFoot(getParent());
            if (widget.widgetId) {
                widgetArray.push(widget);
            }
        });
        $("#255").click(function () {
            console.log('255绑定点击事件');  
            let widget = new WidgetFoot(getParent());
            if (widget.widgetId) {
                widgetArray.push(widget);
            } 

        });

        function getParent() {
            let parent = '#app';
            let id = $("#app .widget_active.container_content:visible").attr('id');
            if (id) {
              parent = '#' + id;
            }
            return parent;
        }

    });

   
    
}

