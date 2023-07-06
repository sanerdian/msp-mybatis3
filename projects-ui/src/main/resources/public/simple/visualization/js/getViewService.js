function getViewService() {
    return service_prefix.demo;
}
//阻止冒泡
function stopBubble(e) {
    if (e && e.stopPropagation)
        e.stopPropagation();
    else
        window.event.cancelBubble = true;
}
//获取表单，对象返回
$.fn.serializeObject = function () {
    var ct = this.serializeArray();
    var obj = {};
    $.each(ct, function () {
        if (obj[this.name] !== undefined) {
            if (!obj[this.name].push) {
                obj[this.name] = [obj[this.name]];
            }
            obj[this.name].push(this.value || "");
        } else {
            obj[this.name] = this.value || "";
        }
    });
    return obj;
};

//展开收起框
function toggleFilter(_this) {
    $(_this).siblings('.filter').slideToggle(300);
    $(_this).toggleClass('slide');
    if ($(_this).hasClass('slide')) {
        $(_this).siblings('.editBody').css('top', '50px');
        $('.viewChild textarea').height(bodyH - 130);
    } else {
        $(_this).siblings('.editBody').css('top', '237px');
        $('.viewChild textarea').height(bodyH - 318);

    }
}

//layui子层级展开收起
function toggleChild(_this) {
    $(_this).parent().toggleClass('layui-nav-itemed');
}

//初始化所有组件
function initAllGroup(mId, dId, fn) {
    var modelId = 'groupListModel';
    var domId = 'groupListDom';
    if (mId) {
        modelId = mId;
    }
    if (dId) {
        domId = dId;
    }
    // ajax(service_prefix.visual,"/module/tree","post","{}").then(function (data) {
    //     if (data && data.success) {
    //         lodTpl(modelId, domId, data.obj);

    //编辑组件获取信息
    var activeid = getQueryString('id');
    if (activeid) {
        getGroupInfoById(activeid);

    }
    //     } else {
    //         var err = '请求失败';
    //         if (data && data.msg) {
    //             err = '请求失败：' + data.msg;
    //         }
    //         showWinTips(err);
    //     }
    //     if (fn) {
    //         fn(data);
    //     }
    // });
}

//根据id获取组件
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

//弹出提示框
//根据是否传load参数，判断是否需要页面刷新
function showWinTips(msg, fn, load) {
    layer.open({
        content: msg,
        yes: function (index, layero) {
            if (fn) {
                fn();
            }
            layer.close(index);
            if (load) {
                return false;
            } else {
                window.location.reload();
            }
        },
        cancel: function (index, layero) {
            layer.close(index);
            window.location.reload();
            return false;
        }
    });
}



//查询hash参数
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.hash.substr(1).match(reg);
    var strValue = "";
    if (r != null) {
        strValue = unescape(r[2]);
    }
    return strValue;
}

// $.fn.autoHeight = function(){    
//     function autoHeight(elem){
//         elem.style.height = 'auto';
//         elem.scrollTop = 0; //防抖动
//         elem.style.height = elem.scrollHeight + 'px';
//     }
//     this.each(function(){
//         autoHeight(this);
//         $(this).on('keyup', function(){
//             autoHeight(this);
//         });
//     });     
// }     


//将生成的代码片段变成完整的html
function toWholeHtml(data) {
    var dropCode = data.htmlCodePure + data.freeHtmlCode;
    console.log('dropCode',dropCode)
    // dropCode = dropCode.replaceAll('<script src="/simple/visualization/js/moduleSet.js"></script>',''); //用于发布生成文件（去掉组件设置）
    if (!data.title) {
        data.title = 'Document';
    }
    var html = '<!Doctype html>'
        + '<html>'
        + '<head>'
        + '<meta charset="utf-8">'
        + '<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">'
        + '<title>' + data.title + '</title>'
        + '<link rel="stylesheet" href="/thirdparty/layui/css/layui.css">'
        // + '<link rel="stylesheet" href="/thirdparty/swiper/swiper.min.css">' //轮播css
        + '<script src="/thirdparty/jquery/jquery-1.12.4.min.js"></script>'
        // + '<script type="text/javascript" src="/thirdparty/swiper/swiper.min.js"></script>' //轮播js
        + '</head>'
        + '<body>'
        + dropCode //组件相关的html/css/js
        + '</body>'
        + '<link rel="stylesheet" href="/simple/visualization/css/publish.css">' //用于发布生成文件的css（去掉布局边框）
        + '<link rel="stylesheet" href="' + data.outFileName + '.css">' //用于发布生成的设置组件样式的css
        + '<link rel="stylesheet" href="' + data.outFileName + 'Custom.css">' //用于生成组件自定义样式的css
        + '<script src="/thirdparty/layui/layui.all.js"></script>'
        + '<script type="text/javascript" src="/common/js/config.js"></script>'
        // + '<script type="text/javascript" src="/common/js/common.js"></script>'
        + '<script type="text/javascript" src="/simple/visualization/js/commonVisual.js"></script>'
        + '<script type="text/javascript" src="' + data.outFileName + '.js"></script>' //产品模板的动态js（获取模板的输出文件名）
        + '<script type="text/javascript" src="' + data.outFileName + 'Custom.js"></script>' //用于生成组件自定义的js
        + '<script type="text/javascript" src="/simple/visualization/js/publish.js"></script>' //用于发布生成文件（面包屑、左侧栏目）
        + '</html>';
    return html;
}

//js下划线之后的小写字母转大写
function lowerToUpper(str){
    var newStr = '';
    var arr = str.split('_') //先用字符串分割成数组
    arr.forEach((item, index) => {
        if (index > 0) {
            return newStr += item.replace(item[0], item[0].toUpperCase())
        } else {
            return newStr += item
        }
    })
    return newStr;
}