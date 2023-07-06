/*
* 获取左侧子栏目
*/
function getMenuSubColumn(){    
    ajax(service_prefix.manage,"/programa/" + columnId, "get").then(function(data){ 
        if(data.success){
            var parentId = data.obj.parentId;
            if(parentId == 0){ //parentId为0有子栏目，否则无子栏目
                var id = columnId;
            }else{
                var id = parentId;
            }                
            ajax(service_prefix.manage,"/programa/getColumn?id=" + id, "post").then(function(data){
                if(data.success){
                    getData(data.obj, '#widgetLeftMenuTpl', '#widgetLeftMenuView');
                }else{
                    console.log(data.msg);
                }
            });
        }else{
            console.log(data.msg);
        }
    });
    // ajax(service_prefix.manage,"/programa/getTreeData1/" + columnId, "get").then(function(data){
    //     if(data.success){
    //         getData(data.obj, '#widgetLeftMenuTpl', '#widgetLeftMenuView');
    //     }else{
    //         console.log(data.msg);
    //     }
    // });
}

/*
* 获取栏目当前位置
*/
function getCurrentColumn(){
    ajax(service_prefix.manage,"/programa/getNaviPath/" + columnId, "get").then(function(data){
        if(data.success){
			$('#widgetCrumbs').html('当前位置：<a href="#">首页</a> &gt; <span>' + data.obj + '</span>')
        }else{
            console.log(data.msg);
        }
    });
}

/*
* 发布表单页面,删除表单元设置元数据按钮
*/
function formMetadataBtnDel(){
    $('.visual_form_metadata_button').parent().parent().parent().parent().remove();
}

$(function(){
    getMenuSubColumn();
	getCurrentColumn();
    formMetadataBtnDel();
})