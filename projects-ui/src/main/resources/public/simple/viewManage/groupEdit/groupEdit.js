$(function(){
    $('.groupDomBox').on('click', '.canEdit', function(e){
        stopBubble(e);
        if (!$(this).hasClass('canEdied')) {
            $('.canEdied').removeClass('canEdied');
        }
        $(this).toggleClass('canEdied');
        if (!$(this).hasClass('canEdied')) {
            synchViewToCode();
        }
        toggleEidtDesign();
    });
                    //删除当前选中元素
    $('.editPanel').on('click', '.deleteDom', function(){
        deleteEditedDom();
    });
                    //弹出编辑框
    $('.editPanel').on('click', '.designEditDom', function(){
        showDesignWinByCode($('.editPanel .canEdied').attr('code'));
    });
    // $('.topTitle>span:nth-child(3)').click(function(){
    //     initAllGroup();
    // });
    $('.groupDomBox').on('click', 'a', function(e){
        e.preventDefault();
    });
    $('.topTitRight .layui-nav-item').click(function(){
        if ($(this).hasClass('showHelpView')) {
            $('.codePannel>.helpView').addClass('layui-show');
            $('.codePannel>.helpView').siblings().removeClass('layui-show');
        } else {
            $('.codePannel>.codeView').addClass('layui-show');
            $('.codePannel>.codeView').siblings().removeClass('layui-show');
        }
    });
    $('.codeView .layui-nav-item').click(function(){
        $(this).siblings().removeClass('layui-this');
        $(this).addClass('layui-this');
        var ind = $(this).index() + 1;
        $('.viewChild>div:nth-child(' + ind + ')').siblings().removeClass('active');
        $('.viewChild>div:nth-child(' + ind + ')').addClass('active');
    });

    //保存按钮点击事件
    $('.rightHead .saveModel').click(function(){
        console.log($(this).attr('activeid'));
        if ($(this).attr('activeid')) {
            console.log('activeid',$(this).attr('activeid'));
            saveActiveModule($(this).attr('activeid'));
        } else {
            //如果是新建就直接打开一级分类选择框
            // openChooseClassWin();
            saveGroupData();
        }
    });
    //删除按钮点击事件
    $('.rightHead .deleteModel').click(function(){
        if (!$(this).siblings('.saveModel').attr('activeid')) {
            showWinTips('请选中一个组件！');
            return;
        }
        deleteActiveModel($(this).siblings('.saveModel').attr('activeid'));
    });
    //编辑按钮
    // $('.rightHead .editModel').click(function(){
    //     setPageOnEdit();
    // });
    //新增
    // $('.rightHead .addModel').click(function(){
    //     initDataForNew();
    //     setPageOnAdd();
    // });
    //阻止默认事件
    document.oncontextmenu = function(e){
        e.preventDefault();
    };
   

    // setPageOnAdd();       //初始化按钮状态

    dragSize();             //拖拽事件

    //先初始化公共的css
    // commonCssBox();
    
    getPerfix().then(function(res){

        
        //获取组件一级分类
        initGroupFirstList();
        
        var activeid = getQueryString('id');
        // if (activeid) {
        //     getGroupInfoById(activeid);

        // }
        // 获取所有的组件
        initAllGroup('', '', function(){
            setActiveLabel('', activeid);
            bindRightClickEvent();
        });
    })
});

//给一级分类绑定右击点击事件
function bindRightClickEvent(){
    //绑定右击事件
    $('#groupListDom .layui-nav-item').each(function(){
        this.oncontextmenu = function(){
            showRightMenuBox($(this).attr('dataid'), $(this).children('a').text(), this);
        }
    });
}

//展示一级菜单的操作
function showRightMenuBox(id, tit, _this){
    var rightMenu = $('<div id="rightMenu"></div>');
    rightMenu.append('<span onclick="showAddFirstClassWin(\'' + id + '\', \'' + tit + '\')">编辑</span>');
    rightMenu.append('<span onclick="deleteActiveModel(\'' + id + '\', \'' + tit + '\')">删除</span>');
    // rightMenu.append('<span onclick="showAddFirstClassWin()">新增</span>');
    var left = $(_this).offset().left + 10;
    var top = $(_this).offset().top - $(window).scrollTop() + 30;
    rightMenu.css('left', left + 'px').css('top', top + 'px');
    $('body').append(rightMenu);
    $('body').append('<div id="rightMenuMask" onclick="hideRightMenuBox();"></div>')
}

function hideRightMenuBox(){
    $('#rightMenu').remove();
    $('#rightMenuMask').remove();
}
//删除当前组件
function deleteActiveModel(id, tit){
    hideRightMenuBox();
    var name = $('#groupFrom input[name=title]').val();
    if (tit) {
        name = tit;
    }
    layer.confirm('确认删除' + name + '组件?', {icon: 3, title:'提示'}, function(index){
        layer.close(index);
        ajax(service_prefix.visual,"/module/" + id + "/batch","delete").then(function (data) {
            var msg = data.msg;
            if (data.success) {
                msg = '删除成功';
                initAllGroup('', '', bindRightClickEvent);
                initDataForNew();
                //获取组件一级分类
                initGroupFirstList();
                // window.close();
            }
            showWinTips(msg);
        });
    });
}

//保存正在编辑的组件
function saveActiveModule(id){
    console.log(id);
    if ($('.groupDomBox>div.firstEdit').length <= 0) {
        return;
    }
    formatDiv();
    new html2canvas($('.groupDomBox>div')[0]).then(canvas => {
        let oImg = new Image();
        oImg.src = canvas.toDataURL();  // 导出图片
        var obj = $('#groupFrom').serializeObject();
        obj.htmlCode = $('#htmlCode').val();
        obj.cssCode = $('#cssCode').val();
        obj.jsCode = $('#jsCode').val();
        obj.img = oImg.src;
        obj.id = id;
        if (verificationInp(obj)) {
            saveEditData(obj);
        }
    });
}

//保存正在编辑的数据
function saveEditData(param){
    console.log('param',param);
    ajax(service_prefix.visual,"/module/" + param.id,"put", JSON.stringify(param)).then(function (data) {
        var msg = data.msg;
        if (data.success) {
            msg = '修改成功';
            initAllGroup('', '', bindRightClickEvent);
            //获取组件一级分类
            initGroupFirstList();
        }
        showWinTips(msg);
	});
}


//初始化所有的内容选项
function initDataForNew(){
    //清空当前正在修改的内容的id
    $('.rightHead .saveModel').attr('activeid', '');
    $('.groupDomBox').html('');
    synchViewToCode();
    $('#groupFrom [name]').each(function(){
        this.value = '';
    });
    layui.form.render();
    // setPageOnAdd();
    // setPageCanAdd();
    // setPageOnAdd();
    window.location.hash = '';
    $('#groupListDom dd.layui-this').removeClass('layui-this');
}

function getHtmlAndDropDown(id){
    //拖拽
    getGroupById(id, function(data){
        if ($('.groupDomBox').children().length == 0) {
            dropEnd('', '', getModuleHtml(data.cssCode, data.htmlCode, data.jsCode));
        } else {
            dropEnd('', '', getModuleHtml(data.cssCode, data.htmlCode, data.jsCode, true));
        }
    });
}

//设置当前页面为正在编辑状态
// function setPageOnEdit(){
//     //编辑按钮显示和新增按钮隐藏
//     $('.rightHead .editModel').hide();
//     $('.rightHead .addModel').hide();
//     //删除和保存显示
//     $('.rightHead .saveModel').show();
//     $('.rightHead .deleteModel').show();
// }
//设置当前页面为可编辑状态和可新增状态
// function setPageCanEdit(){
//     //编辑按钮和新增按钮显示
//     $('.rightHead .editModel').show();
//     $('.rightHead .addModel').show();
//     //删除和保存隐藏
//     $('.rightHead .saveModel').hide();
//     $('.rightHead .deleteModel').hide();
// }
//设置当前页面为只可新增状态
// function setPageCanAdd(){
//     //新增按钮显示
//     $('.rightHead .addModel').show();
//     //删除和保存和编辑隐藏
//     $('.rightHead .saveModel').hide();
//     $('.rightHead .deleteModel').hide();
//     $('.rightHead .editModel').hide();
// }
//设置当前页面为新增状态
// function setPageOnAdd(){
//      //删除按钮和新增按钮隐藏
//      $('.rightHead .deleteModel').hide();
//      $('.rightHead .addModel').hide();
//      $('.rightHead .editModel').hide();
//     //保存显示
//      $('.rightHead .saveModel').show();
// }

//选中并展示在页面上
function getGroupInfoById(id, _this){
    window.location.hash = 'id='+id;
    // setPageCanEdit();
    getGroupById(id, function(data){
        //保存当前正在修改的内容的id
        $('.rightHead .saveModel').attr('activeid', id);
        var htmlCode = getModuleHtml(data.cssCode, data.htmlCode, data.jsCode);
        $('.groupDomBox').html(htmlCode);
        synchViewToCode();
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
    if (_this) {
        setActiveLabel(_this);
    }
}

//设置当前选项的样式
function setActiveLabel(_this, id){
    $('#groupListDom dd.layui-this').removeClass('layui-this');
    if (_this) {
        $(_this).parent().parent().addClass('layui-this');
    } else if (id) {
        $('#groupListDom span[dataid=' + id + ']').parent().parent().addClass('layui-this');
    }
}

//用html css js拼成一个节点
function getModuleHtml(css, html, js, noAddFirst){
    var htmlCode = '<style scoped>' + css + '</style>';
    if (noAddFirst) {
        htmlCode += html;
    } else {
        htmlCode += '<div class="firstEdit">' + html + '</div>';
    }
    htmlCode += '<script>' + js + '</script>';
    return htmlCode;
}

//从整体的html取截取html css js
function getObjFromHtml(){
    var js = $('.groupDomBox').children('script').html();
    var css = $('.groupDomBox').children('style').html();
    var html = $('.groupDomBox').children('div.firstEdit').html();
    return {jsCode: js, cssCode: css, htmlCode: html};
}

//从视图同步代码
function synchViewToCode(){
    var obj = getObjFromHtml();
    $('#htmlCode').val(obj.htmlCode);
    $('#cssCode').val(obj.cssCode);
    $('#jsCode').val(obj.jsCode);
}

//从代码同步视图
function synchCodeToView(){
    var htmlCode = getModuleHtml($('#cssCode').val(),  $('#htmlCode').val(), $('#jsCode').val());
    $('.editPanel .groupDomBox').html(htmlCode);
}

//设置当前选中的样式
function setEditStyle(){
    var objD = $('#setDesiForm').serializeObject();
}

//打开选择分类弹框
// function openChooseClassWin(){
//     initAllGroup('firstClassListModel', 'firstClassList');
//     $('#chooseFirstClassWin').show();
// }
//打开新增分类弹框,如果传入id和tit，说明是编辑
function showAddFirstClassWin(id, tit){
    $('#addFirstClassWin').show();
    if (id) {
        $('#addFirstClassWin').attr('dataid', id);
        $('#addFirstClassWin input').val(tit);
    } else {
        $('#addFirstClassWin').attr('dataid', '');
        $('#addFirstClassWin input').val('');
    }
    hideRightMenuBox();
}
//增加一级分类
function addFirstClass(){
    var val = $.trim($('#addFirstClassWin input[name=title]').val());
    var reg = new RegExp(/^[\u4e00-\u9fa5a-zA-Z0-9]+$/);
    var err = '';
    if (!reg.test(val)){
        err = '分类名称只能包含中文、英文、数字！';
    }
    if (!$.trim(val)) {
        err = '分类名称不能为空！';
    }
    if (err) {
        showWinTips(err);
        return;
    }
    if ($('#addFirstClassWin').attr('dataid')) {
        //编辑
        saveEditData({'title': $.trim(val),'pid': 0, id: $('#addFirstClassWin').attr('dataid')});
    }
    //  else {
    //     addVermoduleByAjax({'title': $.trim(val),'pid': 0});
    // }
}

//新增模型或分类
function addVermoduleByAjax(param){
    ajax(service_prefix.visual,"/module","post", JSON.stringify(param)).then(function (data) {
        var msg = data.msg;
        if (data.success) {
            msg = '新建成功';
            initAllGroup('', '', bindRightClickEvent);
            initDataForNew();
            //获取组件一级分类
            initGroupFirstList();
        }
        showWinTips(msg, function(){
            if (data.success) {
                closeDesignWin();
            }
        });
	});
}

//关闭设置面板
function closeDesignWin() {
    $('#designEditWin').hide();
    $('#addFirstClassWin').hide();
    // $('#chooseFirstClassWin').hide();
}
/*
    //显示设置弹框
    code: 1  ->布局
    code: 2  ->按钮
    code: 3  ->文字
*/
function showDesignWinByCode(code) {
    if (code == null || code == undefined) {
        return;
    }
    lodTpl('desiginEditModel', 'designEditWinForm', code);
    $('#designEditWin').show();
}

//删除选中元素
function deleteEditedDom(){
    layer.confirm('确认删除?', {icon: 3, title:'提示'}, function(index){
        $('.editPanel .canEdied').remove();
        layer.close(index);
        showWinTips('删除成功', function(){
            $('.panelView .editDesign').hide();
            synchViewToCode();
        });
    });
}

//根据是否有选中元素展示设计按钮
function toggleEidtDesign(){
    if ($('.editPanel .canEdied').length <= 0) {
        $('.editPanel .editDesign').hide();
    } else {
        //放左下角
            // var left = $('.editPanel .canEdied').offset().left - $('.editPanel').offset().left + $('.editPanel .canEdied').outerWidth() - 50;
        var left = $('.editPanel .canEdied').offset().left - $('.editPanel').offset().left;
        var top = $('.editPanel .canEdied').offset().top - $('.editPanel').offset().top + $('.editPanel .canEdied').outerHeight() - 26;
        $('.editPanel .editDesign').css({'left': left + 'px', 'top': top + 'px'}).show();
    }
}

// 保存已经编辑好的组件
function saveGroupData() {
    if ($('.groupDomBox>div.firstEdit').length <= 0) {
        layer.msg('请先点击右下角的运行按钮，查看效果');
        return;
    }
    formatDiv();
    new html2canvas($('.groupDomBox>div')[0]).then(canvas => {
        let oImg = new Image();
        oImg.src = canvas.toDataURL();  // 导出图片
        var obj = $('#groupFrom').serializeObject();
        obj.htmlCode = $('#htmlCode').val();
        obj.cssCode = $('#cssCode').val();
        obj.jsCode = $('#jsCode').val();
        obj.img = oImg.src;
        obj.pid = $('#groupFirstList').val();
        console.log(obj);
        console.log(obj.pid);
        if (verificationInp(obj)) {
            addVermoduleByAjax(obj);
        }
    });
}

//生成图片前的处理
function formatDiv(){
    if ($('.groupDomBox>div.firstEdit').children('div').hasClass('absPos')) {
        var wid = $('.groupDomBox>div.firstEdit').children('div').outerWidth();
        var hei = $('.groupDomBox>div.firstEdit').children('div').outerHeight();
        $('.groupDomBox>div.firstEdit').css('width', wid + 'px').css('height', hei + 'px');
    }
}

//验证表单验证
function verificationInp(data){
    var requiredKey = [
        {key: 'title', tips: '请输入产品名称！'},
        {key: 'moduleType', tips: '请选择组件类型！'},
        {key: 'netType', tips: '请选择网页类型！'},
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
    cssCode += '.firstEdit .absPos{position:absolute;}.firstEdit .canEdit {box-sizing: border-box;}';
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
function dropEnd(code, txt, html){
    if (code == '1') {                                  //追加布局
        if ($('.canEdied').hasClass('groupDomBox')) {
            $('.canEdied').append('<div class="canEdit absPos editBox firstEdit" code="' + code + '" ondragover="dragOverEvent(this, event)"><span class="layoutSize"></span></div>');
        } else {
            $('.canEdied').append('<div class="canEdit absPos editBox" code="' + code + '" ondragover="dragOverEvent(this, event)"><span class="layoutSize"></span></div>');
        }
    } else if (code == '2') {                           //追加按钮
        $('.canEdied').append('<div class="canEdit absPos editBtn" code="' + code + '">按钮</div>');
    } else if (code == '3' && txt) {                    //追加文字
        $('.canEdied').append('<span class="canEdit absPos txt" code="' + code + '">' + txt + '</span>');
    } else if (html) {
        var dom = $(html);
        dom.addClass('canEdit absPos');
        //表单
        if (dom.hasClass('layui-form')) {
            dom.children('.layui-form-item').addClass('canEdit');
            dom.find('.layui-form-label').addClass('canEdit').addClass('txt');
        } else if (dom.hasClass('editBox')) {
            if ($('.canEdied').hasClass('groupDomBox')) {
                dom.addClass('firstEdit');
            }
        }
        $('.canEdied').append(dom);
        if (dom.hasClass('layui-form')) {
            layui.form.render();
        }
    }
    bindDragEvent();
    $('.canEdied').removeClass('canEdied');
    synchViewToCode();
}

function bindDragEvent(){
    $('.canEdit').each(function(){
        this.ondragover = function(e){
            dragOverEvent(this, e);
        }
    });
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
                // console.log(top);
                divMove.css({'left': left + 'px', 'top': top + 'px'});
            }
            x0 = x1;
            y0 = y1;
            draging = false;
        }
    });
    document.addEventListener('mouseup', function(){
        if (darg) {
            synchViewToCode();
        }
        darg = false;
        _thisDiv = null;
        divMove = null;
        x0 = 0;
        y0 = 0;
        x1 = 0;
        y1 = 0;
        
    });
}


//获取组件一级分类
function initGroupFirstList(){    
    initAllGroup('groupFirstListModel', 'groupFirstList');
}

//搜索组件
function componentSearch(){
    $('.search_btn .layui-btn').click(function(){
        var componentName = $('#componentName]').val();
        console.log(1)


    });
}