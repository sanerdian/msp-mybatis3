var serviceIp = com.jnetdata.url_prefix;
var productDocId = getQueryString('id');//组件文章ID
var moduleIndex = 0; //拖拽的组件索引值
$(function () {
    //拖拽事件
    $('.domBodyBox').on('click', '.canEdit', function (e) {
        stopBubble(e);
        // if (!$(this).hasClass('canEdied')) {
        //     $('.canEdied').removeClass('canEdied');
        // console.log('html',$(this).html());
        var moduleHtml = $(this).html();
        if (moduleHtml != '') {
            showModuleMenu(); //点击显示组件菜单   
        };
        // }
        // $(this).toggleClass('canEdied');
        toggleEidtDesign();
    });
    $('.topTitle .layui-nav-item').click(function () {
        $(this).addClass('active');
        $(this).siblings().removeClass('active');
        if ($(this).hasClass('showCodeView')) { //展示代码视图
            synchViewToCode();
            $('.panelView .codeView').siblings().removeClass('active');
            $('.panelView .codeView').addClass('active');
        } else if ($(this).hasClass('showEditView')) {
            //展示可编辑视图
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
    // $('.topTitle .showPageView').click(function(){
    //     $('.topTitLeft .layui-nav').removeClass('layui-this');
    //     if ($('.panelView .pageView').siblings('.active').hasClass('showCodeView')) {
    //         codeToSynchView();
    //     } else if ($('.panelView .pageView').siblings('.active').hasClass('showEditView')) {
    //         synchViewToCode();
    //     }
    // 	$('.panelView .pageView').siblings().removeClass('active');
    //     $('.panelView .pageView').addClass('active');
    //     var htmlCode = $.trim($('.domBodyBox').html());


    //     var imgReg = /<img.*?(?:>|\/>)/gi;
    //     // var srcReg = /src=[\'\"]?([^\'\"]*)[\'\"]?/i;
    //     var reg = new RegExp( imgReg , "g" )
    //     var htmlCode = htmlCode.replace( reg , '<img src="common/img/u6147.png" style="width:100%;" />');
    //     // console.log('newstr------->', htmlCode);
    //     var codeHtml = '<div>' + htmlCode + '</div>';
    //     $('.panelView .pageView').html(codeHtml);
    // });
    //删除当前选中元素
    // $('.editPanel').on('click', '.deleteDom', function(){
    //     deleteEditedDom();
    // });
    //弹出编辑框
    // $('.editPanel').on('click', '.designEditDom', function(){
    //     var codes = $('.editPanel .canEdied').attr('code');
    // 	if (!codes && $('.editPanel .canEdied').hasClass('layoutBox')) {
    // 		codes = 0;
    // 	}
    //     showDesignWinByCode(codes);
    // });
    //弹出数据编辑框
    // $('.editPanel .designEditData').click(function(){
    //     showDesiginDataWin();
    // });

    // $('.topTitle>span:nth-child(3)').click(function(){
    //     initAllGroup();
    // });

    $('.leftCodeView>div').click(function () {
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

    //设置导航弹框选中事件
    // $('#designEditWinForm').on('click', '.styleChange > div', function(){
    //     $(this).siblings().removeClass('selected');
    //     $(this).addClass('selected');
    // });
    // //设置导航弹框选中事件
    // $('#designEditWinForm').on('click', '.desgin_nav_styleChange > div', function(){
    //     $(this).siblings().removeClass('selected');
    //     $(this).addClass('selected');
    // });
    // //设置导航弹框选中事件
    // $('#designEditWinForm').on('click', '.colorChange > span', function(){
    //     $(this).siblings().removeClass('selected');
    //     $(this).addClass('selected');
    // });

    //设置数据来源框中的页签切换
    // $('#designEditDataWin .label-check>span').click(function(){
    //     if ($(this).hasClass('label-check-active')) {
    //         return;
    //     }
    //     $(this).addClass('label-check-active');
    //     $(this).siblings().removeClass('label-check-active');
    //     var ind = $(this).index() + 1;
    //     $(this).parent().next().children('div:nth-child(' + ind + ')').siblings().addClass('layui-hide');
    //     $(this).parent().next().children('div:nth-child(' + ind + ')').removeClass('layui-hide');
    // });
    // showDesignWinByCode();
    //页面加载,如果有内容也需要绑定一次
    bindDragEvent();

    //保存按钮点击事件
    $('.rightHead .saveModel').click(function () {
        // if (verificationBasic()) {
        //     // if ($(this).attr('activeid')) {
        //     //     saveModuleData($(this).attr('activeid'));
        //     // } else {
        //     //     //如果是新建就直接打开一级分类选择框
        //     //     // openChooseModesWin();
        //     // }
        //     saveModuleData($(this).attr('activeid'));
        // }

        if ($('.topTitLeft .layui-this a').text() == '可视化产品模板') {
            ajax(service_prefix.visual, "/template/" + productDocId, "get").then(function (data) {
                // console.log(data);
                if (data.success) {
                    saveModuleData(data.obj, productDocId);
                }
            });
        } else {
            layer.msg('请切换到可视化产品模板界面再点击保存');
        }
    });

    //删除按钮
    $('.rightHead .deleteModel').click(function () {
        deleteActiveTemplate($(this).siblings('.saveModel').attr('activeid'));
    });

    //编辑按钮
    // $('.rightHead .editModel').click(function(){
    //     setPageOnEdit();
    // });

    // setPageOnAdd();         //初始化为新增状态

    getPerfix().then(function (res) {
        var activeid = getQueryString('id');
        if (activeid) {
            getTemplateInfoById(activeid);
        }
        // 获取所有的组件
        // initAllGroup();
    });
    saveTemplate(); //保存并同步动态模板
    preview(); //预览按钮
});
// var myTree = new MyZTree();         //设置一个全局变量

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
function deleteActiveTemplate(id) {
    if (id === null || id === undefined) {
        return;
    }
    var name = $('#groupFrom input[name=title]').val();
    layer.confirm('确认删除' + name + '模板?', { icon: 3, title: '提示' }, function (index) {
        layer.close(index);
        ajax(service_prefix.visual, "/template/" + id + "/batch", "delete").then(function (data) {
            var msg = data.msg;
            if (data.success) {
                msg = '删除成功';
            }
            showWinTips(msg, function () {
                if (data.success) {
                    window.close();
                }
            });

        });
    });
}

//查询当前组件模板
function getTemlateById(id, fn) {
    ajax(service_prefix.visual, "/template/" + id, "get").then(function (data) {
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
function getObjFromHtml() {
    var html = $('.domBodyBox').html();
    return { htmlCode: html };
}

//页面同步到代码
function synchViewToCode() {
    var obj = getObjFromHtml();
    console.log('同步obj', obj);
    $('#htmlCode').val(obj.htmlCode);
    setCodeViewLines(['#htmlCode', '#cssCode', '#jsCode']);
}

//设置代码行
function setCodeViewLines(ids) {
    for (var i = 0; i < ids.length; i++) {
        var areaRows = $(ids[i]).val().split("\n").length;
        console.log('areaRows', areaRows);
        if (areaRows < 10) {
            areaRows = 50;
        }
        var lins = '';
        for (var j = 1; j < areaRows + 10; j++) {
            lins += '<span>' + j + '</span>';
        }
        $(ids[i]).css('height', 20.5 * areaRows + 'px');
        $(ids[i]).prev('.codeLine').html(lins);
        $(ids[i]).prev('.codeLine').css('height', 20.5 * areaRows + 'px');
    }
}

//代码同步到网页
function codeToSynchView() {
    var htmlCode = $('#htmlCode').val();
    $('.domBodyBox').html(htmlCode);
}

//获取到网页模板数据回显相关信息
function getTemplateInfoById(id) {
    getTemlateById(id, function (data) {
        console.log('获取模板数据',data);
        moduleIndex = data.moduleCount; //拖拽的组件索引值
        // console.log('回显moduleCount', moduleIndex);
        // setPageCanEdit();
        //保存当前正在修改的内容的id
        $('.rightHead .saveModel').attr('activeid', id);
        var htmlCode = data.htmlCode;
        $('.domBodyBox').html(htmlCode); //返显模板组件设置的html
        // $('.publish_html').html(data.htmlCodePure); //返显模板发布生成文件的html
        $('title').html(data.title);
        $('#cssCode').val(data.cssCode); //自定义样式
        $('#jsCode').val(data.jsCode); //自定义方法
        $('#freeHtmlCode').val(data.freeHtmlCode); //自定义模板html
        $('.domBodyBox .layoutBox').removeClass('noBorder');
        layui.form.render();
        bindDragEvent();
    });
}
//根据id查模板类型title
// function getTitleById(id){
//     if(id)
//     ajax(service_prefix.visual,"/templatetype/"+id,"get").then(function (data) {
// 		if (data && data.success) {
//             $('#setNetform [name=mouldType]').html( '&nbsp; '+ data.obj.title);
// 		} else {
// 			var err = '请求失败';
// 			if (data && data.msg) {
// 				err = '请求失败：' + data.msg;
// 			}
// 			showWinTips(err);
// 		}
// 	});
// }
//验证基本内容
// function verificationBasic(){
//     var obj = $('#groupFrom').serializeObject();
//     obj.title = localStorage.getItem('visual-mould-title'); 
//     obj.outFileName = localStorage.getItem('visual-mould-fileName'); 
//     obj.netType = localStorage.getItem('visual-mould-netType');
//     obj.businessType = localStorage.getItem('visual-mould-industry');  
//     obj.mjType = localStorage.getItem('visual-mould-medium'); 
//     obj.templateType = localStorage.getItem('visual-mould-type');
//     obj.exteName = localStorage.getItem('visual-mould-extension');
//     obj.proDes = localStorage.getItem('visual-mould-des'); 
//     obj.htmlCode = $('#htmlCode').val();
//     obj.cssCode = $('#cssCode').val();
//     obj.jsCode = $('#jsCode').val();
//     if (!verificationInp(obj)) {
//         return false;
//     }
//     return true;
// }

//用于模板发布生成文件的html（去掉组件设置）
function deleteSetHtml() {
    var publishHtml = $('.domBodyBox').html().replaceAll('<!--设置按钮开始-->', '').replaceAll('<!--设置按钮结束-->', '').replaceAll('<!--设置弹出框开始-->', '').replaceAll('<!--设置弹出框结束-->', '').replace(/(\n[\s\t]*\r*\n)/g, '\n').replace(/^[\n\r\n\t]*|[\n\r\n\t]*$/g, ''); //删除组件设置注释
    publishHtml = publishHtml.replaceAll('<script src="/simple/visualization/js/moduleSet.js"></script>', ''); //用于发布生成文件（去掉组件设置）
    $('.publish_html').html(publishHtml);
    for (var i = 0; i < $('.publish_html .widget_box').length; i++) {
        $('.publish_html .widget_menu, .publish_html .widget_set_tpl').remove(); //删除组件设置html
        $('.publish_html .widget_box').removeAttr('data-module-type').removeAttr('data-module-id'); //删除组件设置的类型和id属性
    }
}
//保存已经编辑好的模板
function saveModuleData(data, id) {
    deleteSetHtml();
    if ($('.editPanel .domBodyBox').length <= 0) {
        return;
    }
    $('.domBodyBox .layoutBox').addClass('noBorder');
    new html2canvas($('.editPanel .domBodyBox')[0]).then(canvas => {
        var obj = $('#setNetform').serializeObject();
        // console.log('obj',obj);
        // var htmlCode = $('#htmlCode').val();
        var htmlCode = $('.editPanel .domBodyBox').html();
        var htmlCodePure = $('.editPanel .publish_html').html();
        obj.htmlCode = htmlCode; //获取模板组件设置的html
        obj.htmlCodePure = htmlCodePure; //获取模板发布生成文件的html
        obj.cssCode = $('#cssCode').val(); //自定义样式
        obj.jsCode = $('#jsCode').val(); //自定义方法
        obj.freeHtmlCode = $('#freeHtmlCode').val(); //自定义模板html
        obj.title = data.title;
        obj.outFileName = data.outFileName;
        obj.netType = data.netType;
        obj.businessType = data.businessType;
        obj.mjType = data.mjType;
        obj.templateType = data.templateType;
        obj.exteName = data.exteName;
        obj.type = data.type;
        obj.proDes = data.proDes;
        obj.moduleList = data.moduleList;
        obj.moduleCount = moduleIndex; //拖拽的组件索引值
        let oImg = new Image();
        oImg.src = canvas.toDataURL(); // 导出图片
        obj.img = oImg.src;
        // console.log('obj',obj);
        $('.domBodyBox .layoutBox').removeClass('noBorder');
        if (verificationInp(obj)) {
            if (!id) {
                // obj.type = $('#chooseFirstModesWin input[name=type]:checked').val();
                obj.type = $('select[name="type"]').val();
                createTemplate(obj); //新建
            } else {
                obj.id = id;
                saveActTemplate(obj); //修改
            }
        }
    });
}

//打开选择分类弹框
// function openChooseModesWin(){
//     $('#chooseFirstModesWin').show();
// } 

//动态绑定可拖放事件
//遍历所有的.canEdit的标签，给其添加ondragover方法
function bindDragEvent() {
    $('.canEdit').each(function () {
        this.ondragover = function (e) {
            dragOverEvent(this, e);
        };
    });
}

//增加字段
// function addFilds(){
//     var filds = myTree.getFileChecked();
//     getFildsByIds(filds);
//     closeDesignFildWin();
// }

//根据字段id进行查询
// function getFildsByIds(ids){
//     if (!ids) {
//         lodTpl('desiginFildCheckModel', 'desiginFildCheckDom', []);
//         return;
//     }
//     var idsArr = [];
//     for (var i = 0; i < ids.length; i++) {
//         if (ids[i]) {
//             idsArr.push(ids[i]);
//         }
//     }
//     if (idsArr.length <= 0) {
//         lodTpl('desiginFildCheckModel', 'desiginFildCheckDom', []);
//         return;
//     }
//     ajax(service_prefix.metadata, '/fieldinfo/listByIds',  'post', JSON.stringify(idsArr)).then(function(res){
//         if (res && res.obj) {
//             lodTpl('desiginFildCheckModel', 'desiginFildCheckDom', res.obj);
//         } else if (res && res.msg) {
//             showWinTips(res.msg);
//         } else {
//             showWinTips('请求失败');
//         }

//     });
// }

//打开增加字段弹框
// function openAddFildWin(){
//     var actDomId = $('.canEdied').attr('actdomid');
//     getFildTreeByAjax(actDomId, actDomId);
// }
//查询树节点
// function getFildTreeByAjax(openDom) {
//     var param = {webclass:1};
//     ajax(service_prefix.manage, '/programa/getTreeData', 'post', JSON.stringify(param)).then(function(res){
//         // var myTree = new MyZTree();
//         if (res && (res.success || res.obj)) {
//             myTree.setTreeinit(res, openDom);
//             $('#designFildData').show();
//         } else {
//             var msg = '请求失败';
//             if (res && res.msg) {
//                 msg = res.msg;
//             }
//             showWinTips(msg);
//         }
//     });
// }

//向上 向下移动
// function upDownListTr(_this, code){
//     if (code == '-1' && $(_this).parent().parent().prev().length <= 0 || code == '1' && $(_this).parent().parent().next().length <= 0) {
//         return;
//     }
//     var _thisTds = $(_this).parent().parent().children('td');
//     var chageTds = null;
//     if (code == '1') {                                 //与下一行交换
//         chageTds = $(_this).parent().parent().next().children('td');
//     } else if (code == '-1') {                        //与上一行交换
//         chageTds = $(_this).parent().parent().prev().children('td');
//     }
//     for (var i = 0; i < chageTds.length; i++) {
//         if ($(_thisTds[i]).children('input').length <= 0) {
//             continue;
//         }
//         var _thisInp = $(_thisTds[i]).children('input');
//         var changeInp = $(chageTds[i]).children('input');
//         var _thisV = _thisInp.val();
//         var prevV = changeInp.val();
//         _thisInp.val(prevV);
//         changeInp.val(_thisV);
//         var _thisT = _thisInp.prev().text();
//         var prevT = changeInp.prev().text();
//         _thisInp.prev().text(prevT);
//         changeInp.prev().text(_thisT);
//     }

// }

//删除当前行数据
// function deleteListTr(_this){
//     layer.confirm('确认删除当前数据?', {icon: 3, title:'提示'}, function(index){
//         layer.close(index);
//         $(_this).parent().parent().remove();
//         layer.open({
//             content: '删除成功',
//             yes: function(index, layero){
//               layer.close(index);
//               $('.panelView .editDesign').hide();
//             }
//           }); 
//     });
// }
//编辑标签页
// function editListTr(_this){
//     $(_this).parent().siblings().children('input').removeClass('layui-hide');
//     $(_this).parent().siblings().children('input').prev().addClass('layui-hide');
//     $(_this).siblings('.delThisTr').removeClass('layui-hide');
//     $(_this).siblings('.saveThisTr').removeClass('layui-hide');
//     $(_this).addClass('layui-hide');
// }

//保存标签页
// function saveListTr(_this){
//     var val = $(_this).parent().siblings().children('input').val();
//     if (!$.trim(val)) {
//         layer.open({
//             content: '内容不能未空',
//             yes: function(index, layero){
//               layer.close(index);
//             }
//         }); 
//         return;
//     }
//     $(_this).parent().siblings().children('input').addClass('layui-hide');
//     $(_this).parent().siblings().children('input').each(function(){
//         if ($(this).prev()[0].tagName == 'IMG') {
//             $(this).prev().attr('src', this.value);
//         } else {
//             $(this).prev().text(this.value);
//         }
//         $(this).prev().removeClass('layui-hide');
//     });
//     // $(_this).parent().siblings().children('.objtext').text(val);
//     $(_this).siblings('.delThisTr').addClass('layui-hide');
//     $(_this).siblings('.eidtThisTr').removeClass('layui-hide');
//     $(_this).addClass('layui-hide');
//     // $(_this).parent().siblings().children('.objtext').removeClass('layui-hide');
// }

//打开设置数据块内容
// function showDesiginDataWin(){
//     var childs = $('.canEdied').children();
//     console.log('childs >>>',childs)
//     // var datas = [];
//     if ($('.canEdied').find('[datalist]').length > 0) {         //如果有标明数据源就获取指定数据源
//         childs = $('.canEdied').find('[datalist]').children();
//     }
//     //显示当前用户可配置的项
//     showTempCanConfigFild(childs[0]);
//     //查询当前节点已经配置好的字段进行回显
//     var actFildsId = $('.canEdied').attr('fildid');
//     if (actFildsId) {
//         getFildsByIds(actFildsId.split(','));
//     } else {
//         getFildsByIds('');
//     }
//     $('#designEditDataWin').show();
// }
//显示当前用户可配置的项
// function showTempCanConfigFild(fistChild){
//     var objs = findChildData($(fistChild));
//     var textconfig = '';
//     console.log('fistChild >>',fistChild)
//     console.log('objs >>',objs)
//     //遍历当前模板可配置的项
//     var ind = 1;
//     for (var k in objs) {
//         console.log('k >>',k)
//         // textconfig += '第' + ind + '个可配置类型：'
//         textconfig += ind + '=>';
//         if (k.indexOf('url') > -1) {
//             textconfig += '链接';
//         }
//         if (k.indexOf('img') > -1) {
//             textconfig += '图片';
//         }
//         if (k.indexOf('title') > -1) {
//             textconfig += '文字';
//         }
//         textconfig += '(';
//         if (objs[k]) {
//             if (objs[k].length > 4 && objs[k].indexOf('{{') == -1) {
//                 textconfig += objs[k].substring(0, 4) + '...'; 
//             } else {
//                 textconfig += objs[k];  
//             }
//         } else {
//             textconfig += '未配置';  
//         }
//         textconfig += ')';
//         textconfig += '; ';
//         ind ++;
//     }
//     $('#designEditDataWin .designConfigFild').text(textconfig);
// }

//遍历节点获取数据 (此方法和获取节点模板方法相对应)
function findChildData(jqDom, obj) {
    if (!obj) {
        obj = {};
    }
    jqDom.find('a').each(function (i, elem) {
        obj['url' + i] = $(this).attr('href');
    });
    jqDom.find('img').each(function (i, elem) {
        obj['img' + i] = $(this).attr('src');
    });
    var txtl = 0;
    jqDom.find('*').each(function () {
        if ($(this).children().length <= 0 && $.trim($(this).text())) {
            obj['title' + txtl] = $(this).text();
            txtl++;
        }
    });
    return obj;
}

//向节点中追加数据
// function setEditData(){
//     var fildsIds = '';
//     var proNames = '';
//     $('#desiginFildCheckDom input[name=id]').each(function(){
//         fildsIds += this.value + ',';
//     });
//     $('#desiginFildCheckDom input[name=proName]').each(function(){
//         proNames += this.value + ',';
//     });
//     var actDomId = myTree.getActDomId();
//     var actGroupId = myTree.getGroupCheckId();
//     var tableId = myTree.getCheckId();
//     $('.canEdied').attr('fildid', fildsIds);
//     $('.canEdied').attr('actdomid', actDomId);
//     $('.canEdied').attr('groupid', actGroupId);
//     $('.canEdied').attr('tableid', tableId);
//     setFildInDom(proNames);     //组件关联字段
// }

//将字段名称与Dom名称匹配
// function setFildInDom(prokeys){
//     var parents = $('.canEdied');
//     if ($('.canEdied').find('[datalist]').length > 0) {             //如果有标明数据源就获取指定数据源
//         parents = $('.canEdied').find('[datalist]');
//     }
//     var childs = parents.children();
//     var unComClasN = findUnComClasN(childs); 
//     var oneDom = findChildDom2($(childs[0]), unComClasN, prokeys);
//     var htmlStr = '';
//     childs.each(function(){
//         htmlStr += oneDom;
//     });
//     parents.html(htmlStr);
//     for(var i = 0; i < unComClasN.length; i++){
//         parents.children(':nth-child(1)').addClass(unComClasN[i]);
//     }
//     closeDesignWin();
//     synchViewToCode();
// }

//格式化第一个节点，让其和数据关联,放回第一个节点的模板
function findChildDom2(jqDom, remveClasN, keys) {
    var str = '';
    var cDom = jqDom.clone();
    var keysArr = keys.split(',');
    var ind = 0;
    //第一次的时候替换
    cDom.find('a').each(function (i, elem) {
        if (keysArr[ind]) {
            $(this).attr('href', '{{' + keysArr[ind] + '}}');
            ind++;
        }
    });
    cDom.find('img').each(function (i, elem) {
        if (keysArr[ind]) {
            $(this).attr('src', '{{' + keysArr[ind] + '}}');
            ind++;
            console.log($(this).attr('src'));
        }
    });
    cDom.find('*').each(function () {
        if ($(this).children().length <= 0 && $.trim($(this).text())) {
            if (keysArr[ind]) {
                $(this).text('{{' + keysArr[ind] + '}}');
                ind++;
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
function objStrToXhrStr(objStr) {
    var str = objStr.replace('{', '');
    str = str.replace('}', '');
    str = str.replace(/"|'|\s/g, '');
    str = str.replace(/:/g, '=');
    str = str.replace(/,/g, '&');
    return str;
}
//获取接口返回的数据
// function getDatasByInterface(){
//     var datasObj = $('#interfaceForm').serializeObject();
//     if (!datasObj.url) {
//         layer.open({
//             content: '接口不能为空！',
//             yes: function(index, layero){
//               layer.close(index);
//             }
//         });
//         return;
//     }
//     if (!datasObj.method) {
//         datasObj.method = 'POST';       //默认POST
//     }
//     var params = {};
//     if (datasObj.param) {
//         try {
//             params = objStrToXhrStr(datasObj.param);
//         } catch (error) {
//             layer.open({
//                 content: '参数格式有误，请检查！',
//                 yes: function(index, layero){
//                   layer.close(index);
//                 }
//             });
//             return;
//         }
//     }
//     var xhr=new XMLHttpRequest();
//         xhr.open(datasObj.method,datasObj.url,false);
//         // 新建http头，发送信息至服务器时内容编码类型
//         if (datasObj.contype) {
//             xhr.setRequestHeader('Content-Type',datasObj.contype);
//         } else {
//             xhr.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
//         }
//         xhr.onreadystatechange=function(){
//             if (xhr.readyState==4){
//                 if (xhr.status==200){
//                     $('#designEditDataWin .dataResult').html(xhr.responseText);
//                 } else {
//                     $('#designEditDataWin .dataResult').empty();
//                     $('#designEditDataWin .dataResult').append('<span style="color: red">发生了一个错误：</span>')
//                     $('#designEditDataWin .dataResult').append('<div>状态码：' + xhr.status +'</div>');
//                     if (xhr.statusText) {
//                         $('#designEditDataWin .dataResult').append('<div>状态描述：' + xhr.statusText +'</div>');
//                     }
//                     if (xhr.responseText) {
//                         $('#designEditDataWin .dataResult').append('<div>返回内容：' + xhr.responseText +'</div>');
//                     }
//                 }
//             }
//         }
//         if (datasObj.param) {
//             xhr.send(params);
//         } else {
//             xhr.send();
//         }
// }
//获取自己设置的数据
function getDatasByDefined(fn) {
    //判断数据是否保存
    var saveBtns = $('#desiginDataTbodyDom .saveThisTr');
    for (var i = 0; i < saveBtns.length; i++) {
        if (!$(saveBtns[i]).hasClass('layui-hide')) {
            layer.open({
                content: '您还有数据未保存，请先保存！',
                yes: function (index, layero) {
                    layer.close(index);
                }
            });
            return;
        }
    }
    var datas = [];
    var tit = [];
    $('#desiginDataTbodyDom thead th').each(function () {
        if ($(this).attr('key')) {
            tit.push($(this).attr('key'));
        }
    });
    $('#desiginDataTbodyDom tbody tr').each(function () {
        var obj = {};
        $(this).children('td').children('input[name]').each(function () {
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
function findUnComClasN(jqDom) {
    var unComClasN = [];
    if (jqDom.length < 2) {
        return unComClasN;
    }
    var classObj = [];
    jqDom.each(function () {
        classObj = classObj.concat(this.className.split(' '));
    });
    jqDom.each(function () {
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
function changeLeftForm(_this) {
    $(_this).addClass('active');
    $(_this).siblings().removeClass('active');
    var ind = $(_this).index() + 1;
    $('.divRight>div:nth-child(' + ind + ')').addClass('layui-show');
    $('.divRight>div:nth-child(' + ind + ')').siblings().removeClass('layui-show');
}

/*
* 拼合组件 html css js
* @param id 组件id
* @param css 组件引入的css文件
* @param html 组件html代码
* @param js 组件引入的js文件
*/
function getModuleHtml(id, htmlCode, cssCode, jsCode) {
    var moduleHtmlCode = '';
    //相关CSS文件
    if (cssCode) {
        var file = JSON.parse(cssCode);//字符串转换为对象
        for (var k in file) {
            moduleHtmlCode += '<link rel="stylesheet" href="' + serviceIp + file[k] + '"></link>';
        }
    }
    //html文件代码
    if (htmlCode) {
        //图片发布路径前缀
        var imgPubUrl = 'src="' + serviceIp + '/files/visual/img/' + id + '/';
        //替换CSS文件代码中的图片路径
        var codeHtmlReplace = htmlCode.replaceAll('src="img/', imgPubUrl);
        moduleHtmlCode += '<div class="module_box">' + codeHtmlReplace + '</div>';
    }
    //相关JS文件
    if (jsCode) {
        var file = JSON.parse(jsCode);
        for (var k in file) {
            moduleHtmlCode += '<script src="' + serviceIp + file[k] + '"></script>';
        }
    }
    return moduleHtmlCode;
}

//根据id获取当前的拖拽的代码内容
function getCodeById(id) {
    //拖拽
    getGroupById(id, function (data) {
        // console.log("通过组件id拿到的数据",data);
        moduleIndex = parseInt(moduleIndex) + 1;
        // console.log('拖拽moduleIndex', moduleIndex);
        var typeCode = 'id="widgetBox' + moduleIndex + '" class="widget_box" data-module-type="' + data.typeCode + '"'; //组件类型
        var pageCode = 'class="visual_page"' + 'id="widgetBox' + moduleIndex + 'Page"';
        var htmlCode = data.htmlCode;
        htmlCode = htmlCode.replace('class="widget_box"', typeCode);
        htmlCode = htmlCode.replace('class="visual_page"', pageCode);
        dropEnd(getModuleHtml(id, htmlCode, data.cssCode, data.jsCode));
        //引入组件设置js
        var myHtmlCode = $('.domBodyBox').html();
        if (myHtmlCode.indexOf('simple/visualization/js/moduleSet.js') == -1) {
            var moduleSetJsStr = '<script src="/simple/visualization/js/moduleSet.js"></script>';
            $('.domBodyBox').append(moduleSetJsStr);
        }
    });
}

//设置当前选中的样式
// function setEditStyle(){
//     if ($('.canEdied').hasClass('me_ui_nav_label') || $('.canEdied').hasClass('me_ui_nav_ul')) {      //标签样式
//         var bgColor = $('#designEditWinForm .colorChange>.selected').attr('data');
//         var styles = $('#designEditWinForm .styleChange>.selected').attr('data');
//         if (!styles) {
//             styles = $('#designEditWinForm .desgin_nav_styleChange>.selected').attr('data');
//         }
//         setLabelStyleClass(bgColor, styles);
//     }
//     //自定义样式设置
//     setDefinedStyle();
//     closeDesignWin();
//     synchViewToCode();
// }

//input的name名和css里面的name名的映射关系 code:1 -> 属性名获取input的name名, code:2 ->input的name名获取css的属性名, 默认1
function cssNinpNrelation(k, code) {
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
function initDefinedStyleByEdit() {
    //初始化
    $('#changLabelForm input[value=selfDefind]').each(function () {
        $(this).parent().nextAll().addClass('layui-hide');
    });
    $('#changLabelForm input[type=radio]').each(function () {
        if (this.value == 'default') {
            this.checked = true;
        } else {
            this.checked = false;
        }
    });
    //设置选中框的默认样式
    if ($('.canEdied').length > 0) {
        var data = $('.canEdied').attr('style');
        if (!data) { return; }
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
                way.each(function () {
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
    layui.use('form', function () {
        var form = layui.form; //高版本建议把括号去掉，有的低版本，需要加()
        form.render(label);
    });
}

//设置自定义样式
function setDefinedStyle() {
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
    for (var i = 0; i < editCss.length; i++) {
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
        var k = c;
        if (cssObj[k + 'Way'] && cssObj[k + 'Way'] == 'selfDefind' && $.trim(cssObj[k])) {
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
function setLabelStyleClass(colorClass, sClas) {
    if (colorClass) {
        $('.canEdied').removeClass('bgDefault');
        for (var i = 0; i < 13; i++) {
            $('.canEdied').removeClass('bgColor' + i);
        }
        $('.canEdied').addClass(colorClass);
    }
    if (sClas) {
        $('.canEdied').removeClass('styleDefault');
        for (var i = 1; i < 15; i++) {
            $('.canEdied').removeClass('style' + i);
            $('.canEdied').removeClass('me_nav_style' + i);
        }
        $('.canEdied').addClass(sClas);
    }
}

//关闭设置面板
// function closeDesignWin() {
//     $('#designEditWin').hide();
//     $('#designEditDataWin').hide();
//     // $('#chooseFirstModesWin').hide();
// }

// function closeDesignFildWin(){
//     $('#designFildData').hide();
// }

/*
    //显示设置弹框
    code: 1  ->布局
    code: 2  ->按钮
    code: 3  ->文字
    code: 4  ->切换标签
*/
// function showDesignWinByCode(code) {
//     if (code == null || code == undefined) {
//         code = '0';
//     }
//     var showEdit = ',1,';          //当前选中元素可编辑的样式 ,1, ==> 可编辑自定义样式（一般针对框） ,2, ==> 可编辑文本样式 ,3, ==> 可编辑切换页签样式
//     if ($('.editPanel .canEdied').attr('showedit')) {
//         showEdit = $('.editPanel .canEdied').attr('showedit');
//     }
//     lodTpl('desiginEditModel', 'designEditWinForm', showEdit);
//     changeLeftForm($('.divLeft>ul>li:nth-child(1)')[0]);
//     //绑定事件
//     bindDesginFormEvent();
//     //根据选中框初始化自定义样式内容
//     initDefinedStyleByEdit();
//     if (document.getElementById('desgin_nav_carousel')) {
//         var myCarousel = new MyCarousel('#desgin_nav_carousel');
//         myCarousel.init();
//     }
//     if (document.getElementById('bgColor')) {
//         layui.use('colorpicker', function(){
//             var colorpicker = layui.colorpicker;
//             //渲染
//             colorpicker.render({
//               elem: '#bgColor'  //绑定元素
//               ,color: $('#changLabelForm input[name=backgroundColor]').val()
//               ,change: function(color){
//                 $('#changLabelForm input[name=backgroundColor]').val(color);
//               }
//             });
//         });
//     }
//     $('#designEditWin').show();

// }

function bindDesginFormEvent() {
    //绑定表单中的事件
    $('#changLabelForm input[type=radio]').bind('click', function () {
        if (this.checked && this.value == 'selfDefind') {
            $(this).parent().parent().find('input[value=selfDefind]').parent().next().removeClass('layui-hide');
        } else {
            $(this).parent().parent().find('input[value=selfDefind]').parent().nextAll().addClass('layui-hide');
        }
    });
    //绑定背景方式选择方式
    var form = layui.form;
    form.on('select(fillWay)', function (data) {
        fillWayChangeEvent(data.value);
    });
}

//背景方式选择changge事件
function fillWayChangeEvent(v) {
    $('#changLabelForm .bgColorBox').addClass('layui-hide');
    $('#changLabelForm input[name=backgroundImage]').parent().addClass('layui-hide');
    if (v == 'backgroundColor') {
        $('#changLabelForm .bgColorBox').removeClass('layui-hide');
    }
    if (v == 'backgroundImage') {
        $('#changLabelForm input[name=backgroundImage]').parent().removeClass('layui-hide');
    }
}
//删除选中元素
// function deleteEditedDom(){
//     layer.confirm('确认删除?', {icon: 3, title:'提示'}, function(index){
//         $('.editPanel .canEdied').prev('style').remove();
//         $('.editPanel .canEdied').next('script').remove();
//         $('.editPanel .canEdied').remove();
//         layer.close(index);
//         layer.open({
//             content: '删除成功',
//             yes: function(index, layero){
//               layer.close(index);
//               $('.panelView .editDesign').hide();
//             }
//           }); 
//     });
// }

//根据是否有选中元素展示设计按钮
function toggleEidtDesign() {
    if ($('.editPanel .canEdied').length <= 0) {
        $('.editPanel .editDesign').hide();
    } else {
        //放左下角
        var left = $('.editPanel .canEdied').offset().left - $('.editPanel').offset().left;
        var top = $('.editPanel .canEdied').offset().top - $('.editPanel').offset().top + $('.editPanel .canEdied').outerHeight();
        $('.editPanel .editDesign').css({ 'left': left + 'px', 'top': top + 'px' }).show();
        if ($('.editPanel .canEdied').attr('editdata') && $('.editPanel .canEdied').attr('editdata') == '1') {
            $('.editDesign .designEditData').show();
        } else {
            $('.editDesign .designEditData').hide();
        }
    }
}

//新增网页模板
function createTemplate(param) {
    ajax(service_prefix.visual, "/template", "post", JSON.stringify(param)).then(function (data) {
        var msg = data.msg;
        if (data.success) {
            msg = '新建成功';
            // initAllGroup();
            // initDataForNew();
        }
        showWinTips(msg, function () {
            if (data.success) {
                // closeDesignWin();
                // $('.domBodyBox .layoutBox').removeClass('noBorder');
            }
        });
    });
}
//修改内容
function saveActTemplate(param) {
    ajax(service_prefix.visual, "/template/" + param.id, "put", JSON.stringify(param)).then(function (data) {
        // ajax(service_prefix.visual,"/template/" + param.id + "/allColumn","put", JSON.stringify(param)).then(function (data) {
        // console.log(data);
        var msg = data.msg;
        if (data.success) {
            msg = '保存成功';
        }
        showWinTips(msg, function () {
            if (data.success) {
                // $('.domBodyBox .layoutBox').removeClass('noBorder');
            }
        });
    });
}
//验证表单验证
function verificationInp(data) {
    var requiredKey = [
        { key: 'title', tips: '请输入产品名称！' },
        { key: 'netType', tips: '请选择网页类型！' },
        { key: 'outFileName', tips: '请输入输出文件名！' },
        { key: 'exteName', tips: '请输入扩展名！' },
        // {key: 'htmlCode', tips: '当前未获取到内容！'}
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
// function commonCssBox(){
//     var cssCode = '.editBtn{display: inline-block;border: 1px solid #C9C9C9;background-color: #fff;color: #555;height: 38px;line-height: 38px;padding: 0 18px;cursor: pointer;}';
//     cssCode += '.editBox {display: inline-block;vertical-align: middle;width: 100px;height: 100px;}';
//     cssCode += '.module_box .canEdit {box-sizing: border-box;position: absolute;}.canEdit.module_box {position: relative;}';
//     cssCode += '.editDesign {display:none;}'
//     // cssCode += '.module_box .canEdit {box-sizing: border-box;}.canEdit.module_box {position: relative;}';
//     $('#cssCode').val(cssCode);
// }

function dragOverEvent(_this, e) {
    stopBubble(e);
    $('.canEdied').removeClass('canEdied');
    $(_this).addClass('canEdied');
}

function html() {
}

//拖拽事件结束后统一调用（新建元素）
function dropEnd(html) {
    if (html) {
        $('.canEdied').append(html);
    }
    $('.canEdied').removeClass('canEdied');
    // 防止添加的组件是布局，动态绑定可拖放事件添加
    bindDragEvent();
    // synchViewToCode();
}

//拖拽
function dragSize() {
    var x0, y0, x1, y1;
    var draging = false;
    var _thisDiv = null;
    var darg = false;
    var divMove = null;
    $('.editPanel').on('mousedown', '.layoutSize', function (e) {
        stopBubble(e);
        x0 = e.clientX;
        y0 = e.clientY;
        _thisDiv = $(this).parent();
        darg = true;
    });
    $('.editPanel').on('mousedown', '.canEdit.absPos', function (e) {
        stopBubble(e);
        x0 = e.clientX;
        y0 = e.clientY;
        divMove = $(this);
        darg = true;
    });
    $('.editPanel').on('mousemove', function (e) {
        if (darg && !draging) {
            draging = true;
            x1 = e.clientX;
            y1 = e.clientY;
            //布局框大小
            if (_thisDiv && _thisDiv.length > 0) {
                var wid = _thisDiv.outerWidth() + (x1 - x0);
                var hei = _thisDiv.outerHeight() + (y1 - y0);
                _thisDiv.css({ 'width': wid + 'px', 'height': hei + 'px', 'minHeight': 'auto' });
            }
            //元素移动
            if (divMove && divMove.length > 0) {
                var left = divMove.position().left + (x1 - x0);
                var top = divMove.position().top + (y1 - y0);
                divMove.css({ 'left': left + 'px', 'top': top + 'px' });
            }
            x0 = x1;
            y0 = y1;
            draging = false;
        }
    });
    document.addEventListener('mouseup', function () {
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
function MyCarousel(id) {
    this.left = 0;
    this.speed = 1000;
    this.activeInd = 0;
    this.isMoving = false;
    this.len = $(id).find('.desgin_carousel_move').children().length;
    var _this = this;
    this.init = function () {
        $(id).children('.desgin_carousel_left').unbind('click').click(function () {
            _this.prevSlide();
        });
        $(id).children('.desgin_carousel_right').unbind('click').click(function () {
            _this.nextSlide();
        });
    };
    this.nextSlide = function () {
        if (this.canRemove(1)) {
            var oneWid = $(id).children('.desgin_carousel_item').outerWidth();
            this.left = this.left - oneWid;
            this.move();
        }
    };
    this.prevSlide = function () {
        if (this.canRemove(-1)) {
            var oneWid = $(id).children('.desgin_carousel_item').outerWidth();
            this.left = this.left + oneWid;
            this.move();
        }
    };
    this.canRemove = function (code) {
        if (code == '-1' && this.left >= 0) {
            return false;
        }
        if (code == 1 && this.left * -1 >= $(id).children('.desgin_carousel_item').outerWidth() * (this.len - 1)) {
            return false;
        }
        return true;
    };
    this.move = function () {
        if (!this.isMoving) {
            this.isMoving = true;
            $(id).find('.desgin_carousel_move').animate({ left: this.left + 'px' }, this.speed, function () {
                _this.isMoving = false;
            });
        }
    };
}

//生成ztree树
// function MyZTree(){
//     var _this = this;
//     this.treeId = 'fildTree';
//     this.actTableId = '';
//     this.actGroupId = '';
//     this.actDomId = '';
//     //返回选中的tableid,用于查询字段
//     this.getCheckId = function(){
//         return this.actTableId;
//     }
//     this.getActDomId = function(){
//         return this.actDomId;
//     }
//     this.getGroupCheckId = function(){
//         return this.actGroupId;
//     }
//     this.setActDomId = function(v){
//         this.actDomId = v;
//     };
//     this.setCheckId = function(v){
//         this.actTableId = v;
//     }
//     this.setGroupCheckId = function(v){
//         this.actGroupId = v;
//     }
//     this.clearNodeInfo = function(domId){
//         this.setCheckId('');
//         this.setGroupCheckId('');
//         this.setActDomId(domId);
//     }
//     this.setActNodeInfo = function(node){
//         this.setCheckId(node.tableId);
//         this.setGroupCheckId(node.id);
//         this.setActDomId(node.tId);
//     }
//     //查询当前节点的数据
//     this.getActiveFildsData = function(checkdIds){
//         var tableid = this.getCheckId();
//         if (!tableid) {
//             // showWinTips('当前节点无字段信息');
//             lodTpl('desiginFildModel', 'designFildDom', {showTit:[], data: null, checked: ''});
//             return;
//         }
//         ajax(service_prefix.metadata,"/fieldinfo/all?tableId="+tableid,"get").then(function (data) {
//             var showTit = [
//                 {title: '字段名', attrkey: 'anothername'},
//                 {title: '字段变量名', attrkey: 'proName'},
//                 {title: '字段类型', attrkey: 'fieldTypeStr'}
//             ];
//             var datas = {showTit: showTit, data: data.obj, checked: checkdIds};
//             lodTpl('desiginFildModel', 'designFildDom', datas);
//         });
//     }
//     this.zTreeOnClick1 = function(event, treeId, treeNode, checkedid){
//         var treeObj=$.fn.zTree.getZTreeObj(treeId);
//         treeObj.cancelSelectedNode();
//         treeObj.selectNode(treeNode, true);
//         treeObj.checkNode(treeNode, true);
//         _this.setActNodeInfo(treeNode);
//         if (!checkedid || typeof checkedid == 'number') {
//             checkedid = '';
//         }
//         _this.getActiveFildsData(checkedid);
//     }
//     this.settingss = {
//         data: {
//           simpleData: {
//             enable: true,           //true 、 false 分别表示 使用 、 不使用 简单数据模式
//             idKey: "id",            //节点数据中保存唯一标识的属性名称
//             pIdKey: "parentId",     //节点数据中保存其父节点唯一标识的属性名称
//             rootPId: -1             //用于修正根节点父节点数据，即 pIdKey 指定的属性值
//           },
//           key: {
//             name: "name"            //zTree 节点数据保存节点名称的属性名称  默认值："name"
//           }
//         },
//         check: {                    //表示tree的节点在点击时的相关设置
//             enable: true,           //是否显示radio/checkbox
//             autoCheckTrigger: false,
//             chkStyle: "radio",      //值为checkbox或者radio表示
//             radioType:"all",
//             chkboxType: {"Y": "", "N": ""}  //表示父子节点的联动效果，不联动
//         },
//         view: {showLine: false, showIcon: true},
//         callback:{
//           onClick:_this.zTreeOnClick1,
//           onCheck:_this.zTreeOnClick1
//         }
//       };
//     this.setTreeinit = function(data, defaultOpenDom) {
//         delTreeChildren1(data.obj);
//         zTreeObj = $.fn.zTree.init($('#' + this.treeId), this.settingss, data.obj); //初始化树
//         if (!defaultOpenDom) {
//             defaultOpenDom = 'fildTree_4';
//         }
//         //初始化设置
//         _this.clearNodeInfo(defaultOpenDom);
//         var node = zTreeObj.getNodeByTId(defaultOpenDom);
//         zTreeObj.expandAll(true);                                   //true 节点全部展开、false节点收缩
//         zTreeObj.selectNode(node);
//         _this.settingss.callback.onClick(null, zTreeObj.setting.treeId, node, $('.canEdied').attr('fildid'));   //$('.canEdied').attr('fildid')进行回显

//     }
//     this.getFileChecked = function(){   //获取当前勾选的内容
//         var filds = [];
//         $('#designFildDom input[name=filds]:checked').each(function(){
//             filds.push(this.value);
//         });
//         return filds;
//     }
// }
//组件一级分类
function groupType() {
    var jsonData = {
        "entity": {
            "parentId": 0,
        },
        "pager": {
            "current": 1,
            "size": 20
        }
    };
    ajax(service_prefix.visual, '/moduletype/listing', 'post', JSON.stringify(jsonData)).then(function (data) {
        if (data.success) {
            getData(data.obj.records, '#groupTypeTemplate', '#groupTypeView');
            //点击当前组件分类
            $('#groupTypeView li').click(function () {
                $('.module_search>h3').text($(this).text());
                $(this).addClass('cur').siblings().removeClass('cur');
                var typeId = $(this).attr('data-id');
                groupSubType(typeId);
                //  // groupTypeParam();		
                searchGroup();
            });

            $(".componentBox .tab ul li").click(function () {
                $('.componentBox').css('width', '840px');
                $('.tool_bar').css('width', '830px');
                $(this).siblings("li").removeClass("cur").end().addClass("cur");
                $('.tab_con, .tool_close').css('display', 'block');
                $('.tab').css('height', '610px');
                let vmTitle = $(this).attr('data');
                $('#moduleName').html(vmTitle);
                $('.module_search h3').html(vmTitle);
                if (($('.viewBox').width() - $('.componentBox').offset().left) < 850)//当工具栏展开时超出右侧屏幕时
                    $('.componentBox').css('left', ($('.viewBox').width() - 850) + 'px');
            });
            $('.tool_close').click(function () {
                $('.componentBox, .tool_bar').css('width', '80px');
                $('.tab_con, .tool_close').css('display', 'none');
                $('.tab').css('height', '356px');
            });
        } else {
            console.log(data.msg);
        }
    });
}

//组件二级分类 parentId为它的类型分类
function groupSubType(parentId) {
    var jsonData = {
        "entity": {
            "parentId": parentId,
        },
        "pager": {
            "current": 1,
            "size": 20
        }
    };
    ajax(service_prefix.visual, '/moduletype/listing', 'post', JSON.stringify(jsonData)).then(function (data) {
        if (data.success) {
            getData(data.obj.records, '#groupSubTypeTemplate', '#groupSubTypeView');
            //点击当前组件分类
            $('#groupSubTypeView li').click(function () {
                $(this).addClass('cur').siblings().removeClass('cur');
                groupTypeParam();
                $('#moduleName').html($(this).text());
            });
            groupTypeParam();
            var curName = $('#groupSubTypeView li.cur').text();
            $('#moduleName').html(curName);
        } else {
            console.log(data.msg);
        }
    });
}

//组件分类的列表数据
var serviceId = service_prefix.visual;
var url = '/module/list';
function groupList(serviceId, url, curr, size, param) {
    var jsonData = {
        "entity": param,
        "pager": {
            "current": curr,
            "size": size,
            "sortProps": {
                key: "modifyTime",
                value: false
            }
        }
    };
    ajax(serviceId, url, 'post', JSON.stringify(jsonData)).then(function (data) {
        if (data.success) {
            getData(data.obj.records, '#groupListTemplate', '#groupListView');
            page(data.obj.total, curr, size, groupList, '', param);
        } else {
            console.log(data.msg);
        }
    });
}

//默认分类
//搜索组件添加title参数
function groupTypeParam(title) {
    var groupId = $('#groupTypeView li.cur').attr('data-id');
    var groupSubId = $('#groupSubTypeView li.cur').attr('data-id');
    var param = {
        vmType: groupId,
        vcType: groupSubId,
        title: title
    };
    groupList(serviceId, url, 1, 10, param);
}

function initEditorNav() {
    $(".componentBox").draggable({
        containment: ".editPanel",
        scroll: false
    });
    $(".tab_con").on('mousedown', function () {//阻止子元素的拖拽受影响
        window.event.stopPropagation();
    });
    groupType();
    // $(".list_button").click(function () {
    //     layer.open({
    //         type: 1,
    //         title: ['管理页面'],
    //         id: 'layer_page_list',
    //         area: ['775px', '616px'],
    //         content: $("#editor_page_table_container"),
    //         resize: true,
    //         shadeClose: false,
    //         moveType: 0,
    //         shade: 0.5,
    //         anim: -1
    //     });
    //     table.reload('editor_page_table');
    // });
}
// function  moduleGain(){
//     ajax(service_prefix.member,'/menu/tree?type=2','get').then(function (data){
//         console.log(data);
// 		if(data.success){			
//         table.render({
//         elem: '#editor_page_table',
//         height: 400,
//         width: 720,
//         data:data.obj,
//         skin: 'nob',
//         even: true,
//         size: 'lg', 
//         limit:30, 
//         cols: [[
//             {field: 'name', title: '页面', width: 223, unresize: true},
//             {
//                 field: 'visible', title: '菜单显示', width: 102, unresize: true, templet: function (d) {
//                     if (d.visible)
//                         return '<i class="layui-icon layui-icon-ok i_tab i_tab_visible" lay-event="visible"></i>';
//                     else
//                         return '<i class="layui-icon layui-icon-close i_tab i_tab_hidden" lay-event="visible"></i>';
//                 }
//             },
//             {field: 'order', title: '排序', width: 179, unresize: true, toolbar: '#i_tab_order_bar'},
//             {title: '操作', width: 198, unresize: true, toolbar: '#i_tab_right_bar'},
//         ]],
//         done(res) {
//                    $("#editor_page_table_container .total_number").text(`总共 ${res.data.length} 个`)
//        }
//             // ,page: true
//         });
// 		}else{
// 			console.log(data.msg);
// 		}
// 	})
// }
// function initNavPageTable() {
//     function setTableData(obj, data, columns) {
//         table.render({
//             elem: '#editor_page_table',
//         height: 400,
//         width: 720,
//         data:data.records,
//         // limit: data.size,
//         // url: 'http://36.138.169.165:8081/member/menu/tree?type=2',
//         skin: 'nob',
//         even: true,
//         size: 'lg',  
//         limit:30,
//         cols: [[
//             {field: 'name', title: '页面', width: 223, unresize: true},
//             {
//                 field: 'visible', title: '菜单显示', width: 102, unresize: true, templet: function (d) {
//                     if (d.visible)
//                         return '<i class="layui-icon layui-icon-ok i_tab i_tab_visible" lay-event="visible"></i>';
//                     else
//                         return '<i class="layui-icon layui-icon-close i_tab i_tab_hidden" lay-event="visible"></i>';
//                 }
//             },
//             {field: 'order', title: '排序', width: 179, unresize: true, toolbar: '#i_tab_order_bar'},
//             {title: '操作', width: 198, unresize: true, toolbar: '#i_tab_right_bar'},
//         ]],
//             // ,page: true
//         });
//     }
//     moduleGain();
//     table.on('tool(filter_editor_page_table)', (obj) => {
//         switch (obj.event) {
//             case 'visible':
//                 let visible_sel = obj.tr.find("td:nth-child(2) i");
//                 if (obj.data.visible) {
//                     visible_sel.addClass("layui-icon-close");
//                     visible_sel.removeClass("layui-icon-ok");
//                 } else {
//                     visible_sel.addClass("layui-icon-ok");
//                     visible_sel.removeClass("layui-icon-close");
//                 }
//                 obj.update({visible: !obj.data.visible})
//                 obj.data.visible = !obj.data.visible;
//                 console.log(table.cache['editor_page_table'])
//                 break;
//             case 'update' :
//                 layer.alert('修改数据：' + `<input  id="zss" value="${obj.data.name}"></input>`, (index) => {
//                     layer.close(index)
//                     table.reload('editor_page_table')
//                     const jsonData={
//                         "active":"1",
//                         "companyId"	:"1167323268476895233",
//                         "name":$('#zss')[0].value,
//                         "parentId":	"0",
//                         "seq":	"0",
//                         "type": 2,
//                       }
//                     moduleUpdate(obj.data.id,jsonData)
//                     moduleGain()
//                 })
//                 break;
//             case 'copy':
//                 break;
//             case 'add':
//                 break;
//             case 'delete' :
//                 layer.alert('删除数据：' + obj.data.id, (index) => {
//                     layer.close(index)
//                     table.reload('editor_page_table')
//                     moduleDelete(obj.data.id)
//                     moduleGain()
//                 })
//                 break;
//             case 'up':
//                 moduleUp(obj.data.id)
//                 break;
//             case 'down':
//                 moduleDown(obj.data.id)
//                 break;
//             case 'left':
//                 moduleLeft(obj.data.id)
//                 break;
//             case 'right':
//                 moduleRight(obj.data.id)
//                 break;
//         }
//     })

//     $("#editor_table_ok").click(function () {
//         console.log(111);
//         console.log(table.cache['editor_page_table']);
//         layer.closeAll();
//         moduleTree()
//         moduleGain()
//     });
//     $("#editor_table_cancel").click(function () {
//         layer.closeAll();
//     });
//     $("#editor_table_ok2").click(function () {
//         const jsonData={
//             "active":"1",
//             "companyId"	:"1167323268476895233",
//             "name": $('#gain_title')[0].value,
//             "parentId":	"0",
//             "seq":	"0",
//             "type": 2,
//           }
//         ajax('','/member/menu','POST',JSON.stringify(jsonData)).then(function(res){
//             console.log(res);
//         })
//         layer.close(layer_index);
//         moduleGain()
//     });
//     $("#editor_table_cancel2").click(function () {
//         layer.close(layer_index);
//     });
//     $("#editor_page_table_container .i_tab_add_page").click(function () {
//         layer.open({
//             title: '添加页面',
//             content: $('#addNet').html(),
//             btn: '',
//             area: ['570px'],
//             });   
//             layui.use('form', function(){
//               var form = layui.form;      
//               form.render();
//               form.on('submit(formDemo)', function(data){
//                 layer.close(layer.index);
//                 return false;
//                });

//             });
//     });
// }
/* 
删除菜单名称 
*/
// function moduleDelete(id){
//     ajax('','/member/menu/'+id,'DELETE').then(function (data){
//         if(data.success){					
//             layer.msg('删除成功');
//         }else{
//             console.log(data.msg);
//         }
//     })
// }
/* 
修改菜单名称 
*/
// function moduleUpdate(id,jsonData){
//     ajax('','/member/menu/'+id,'PUT',JSON.stringify(jsonData)).then(function (data){
//         if(data.success){					
//             layer.msg('修改成功');
//         }else{
//             console.log(data.msg);
//         }
//     })
// }
/* 
上移 
*/
function moduleUp(id) {
    ajax('', '/visual/page/pageUp?pageId=' + id, 'POST').then(function (data) {
        if (data.success) {
            layer.msg('上移成功');
        } else {
            console.log(data.msg);
        }
    });
}
/* 
下移 
*/
function moduleDown(id) {
    ajax('', '/visual/page/pageDown?pageId=' + id, 'POST').then(function (data) {
        if (data.success) {
            layer.msg('下移成功');
        } else {
            console.log(data.msg);
        }
    });
}
/* 
左移 
*/
function moduleLeft(id) {
    ajax('', '/visual/page/pageLeft?pageId=' + id, 'POST').then(function (data) {
        if (data.success) {
            layer.msg('左移成功');
        } else {
            console.log(data.msg);
        }
    });
}
/* 
右移 
*/
function moduleRight(id) {
    ajax('', '/visual/page/pageRight?pageId=' + id, 'POST').then(function (data) {
        if (data.success) {
            layer.msg('右移成功');
        } else {
            console.log(data.msg);
        }
    });
}

//预览按钮
function preview() {
    $('.rightHead .preview').click(function () {
        ajax(service_prefix.visual, "/template/preview/" + productDocId, "post").then(function (data) {
            // console.log(data);
            if (data.success) {
                window.open(serviceIp + data.obj, '_blank');//跳转预览地址
            } else {
                console.log(data.msg);
            }
        });
    });
}

//保存并同步动态模板
function saveTemplate() {
    $('.rightHead .saveTemplate').click(function () {
        ajax(service_prefix.visual, "/template/" + productDocId, "get").then(function (data) {
            // console.log(data);
            if (data.success) {
                var param = {
                    "visualTemplateId": productDocId,
                    "temphtml": toWholeHtml(data.obj)
                };
                ajax(service_prefix.visual, "/template/syncAndPublish", "post", JSON.stringify(param)).then(function (data) {
                    // console.log(data);
                    if (data.success) {
                        layer.msg('保存并同步动态模板');
                    } else {
                        console.log(data.msg);
                    }
                });
            } else {
                console.log(data.msg);
            }
        });
    });
}

//获取模板类型数据
function getLabels(fn) {
    ajax(service_prefix.visual, "/templatetype/typeList", "post").then(function (data) {
        if (data && data.success) {
            fn(data);
        } else {
            var err = '请求失败';
            if (data && data.msg) {
                err = '请求失败：' + data.msg;
            }
            showWinTips(err);
        }
    });
}

//模板设置弹窗
function setNetfunction() {
    getLabels(function (data) {
        $('.visualMouldtype').empty();
        for (var k = 0; k < data.obj.length; k++) {
            $('.visualMouldtype').append(' <option value="' + data.obj[k].id + '">' + data.obj[k].title + '</option>');
        }
        form.render();
    });
    layer.open({
        title: '模板设置',
        content: $('#setNet').html(),
        btn: '',
        area: ['570px'],
    });
    layui.use('form', function () {
        var form = layui.form;
        // // form.render();
        //点击取消后关闭
        $('.reverse-btn').on('click', function () {
            layer.closeAll();
        });
        getTemlateById(productDocId, function (data) {
            form.val('setTemplateform', data);//数据返显
            //提交表单触发  
            form.on('submit(templateBtnAdd)', function (data) {
                // console.log('save',data.field)
                ajax(service_prefix.visual, "/template/" + productDocId, "put", JSON.stringify(data.field)).then(function (data) {
                    if (data.success) {
                        layer.msg('模板设置成功');
                    }
                });
                layer.close(layer.index);
                return false;
            });
        });
        form.render();
    });
}

//搜索组件功能
// 开始行
function searchGroup() {
    $('.widget_search').click(function () {
        var title = $('.module_search_title').val();
        groupTypeParam(title);
    });
    $('.module_search_title').bind('keydown', function (event) {
        if (event.keyCode == "13") {
            var title = $('.module_search_title').val();
            groupTypeParam(title);
        }
    });
}
// 结束行

