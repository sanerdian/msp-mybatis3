function getViewService(){
	return service_prefix.demo;
}
//阻止冒泡
function stopBubble(e) { 
    if ( e && e.stopPropagation ) 
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
function toggleFilter(_this){
    $(_this).siblings('.filter').slideToggle(300);
    $(_this).toggleClass('slide');
    if($(_this).hasClass('slide')){
        $(_this).siblings('.editBody').css('top','50px');
        $('.viewChild textarea').height(bodyH - 130);
    }else{
        $(_this).siblings('.editBody').css('top','237px');
        $('.viewChild textarea').height(bodyH - 318);
        
    }
}

//layui子层级展开收起
function toggleChild(_this) {
	$(_this).parent().toggleClass('layui-nav-itemed');
}

//初始化所有组件
function initAllGroup(mId, dId, fn){
    var modelId = 'groupListModel';
    var domId = 'groupListDom';
    if (mId) {
        modelId = mId;
    }
    if (dId) {
        domId = dId;
    }
    ajax(service_prefix.visual,"/module/tree","post","{}").then(function (data) {
        if (data && data.success) {
            lodTpl(modelId, domId, data.obj);
            
            //编辑组件获取信息
            var activeid = getQueryString('id');
            if (activeid) {
                getGroupInfoById(activeid);

            }
        } else {
            var err = '请求失败';
            if (data && data.msg) {
                err = '请求失败：' + data.msg;
            }
            showWinTips(err);
        }
        if (fn) {
            fn(data);
        }
	});
}

//根据id获取组件
function getGroupById(id, fn){
    ajax(service_prefix.visual,"/module/" + id,"get").then(function (data) {
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
function showWinTips(msg, fn){
    layer.open({
        content: msg,
        yes: function(index, layero){
          layer.close(index);
          if (fn) {
            fn();
          }
        }
    });
}


//查询hash参数
function getQueryString(name){
    var reg = new RegExp("(^|&)"+name+"=([^&]*)(&|$)"); 
    var r =  window.location.hash.substr(1).match(reg);
    var strValue = "";
    if (r!=null){
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


var bodyH = $('body').height();
$('.viewChild textarea').height(bodyH - 318);
// $('.helpView').height(bodyH-295);
// $('.panelView .editPanel').height(bodyH-390);
// $('.panelView .codeView').height(bodyH-327);