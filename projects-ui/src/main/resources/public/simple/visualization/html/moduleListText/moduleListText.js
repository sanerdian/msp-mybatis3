//组件ID
var widgetId = 'widgetTable';
var layerSetId = widgetId + 'Settings';

//取消边框
function removeAllWidgetBorder() {
    $('#'+ widgetId).find(".widget_border_content").addClass("widget_inactive").removeClass("widget_active");
}

//添加边框
function activeWidgetBorder(widget) {
    // console.log(widget)
    $('#'+ widgetId).find(".widget_border_content:visible:first").addClass("widget_active").removeClass("widget_inactive");
}

//点击显示菜单
$('#'+ widgetId).on('click', function (e) {
    // console.log('e',e);
    activeWidgetBorder(widgetId);
    $('#'+ widgetId + ' .widget_menu').show();
    clickSet(layerSetId);
    return false;
});

//点击设置按钮弹出框
function clickSet(id){
    $('#'+ widgetId + ' .widget_set').click(function () {
        layer.open({
            type: 1,
            title: false,
            id: id,
            skin: 'layui-layer-molv',
            scrollbar:false,
            area: ['530px','350px'],
            btnAlign: 'c',
            offset: 't',
            btn: ['确定', '取消'],
            content: '<div class="layer_box" id="' + widgetId + 'SetView"></div>',
            success: function(layero, index){
                var data = [];
                var view = '#' + widgetId + 'SetView';
                var tpl = '#' + widgetId + 'SetTpl';
                getData(data, tpl, view);
                form.render();
            },
            btn1:function(){
                $('#'+ widgetId + 'SetView').hide();
                layer.closeAll();
            },
            btn2:function(){
                $('#'+ widgetId + 'SetView').hide();
                layer.closeAll();
            },
        });
    })
}

//点击删除
function deleteModule(){
    $('#'+ widgetId + ' .widget_delete').click(function () {
        $(this).parent().parent().remove();
    })
}
deleteModule();