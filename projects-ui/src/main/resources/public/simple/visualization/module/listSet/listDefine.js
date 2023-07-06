
function computerCodeClick(){
    layer.open({
        type: 1,
        title: false,
        id: 'widgetBox6Layer',
        skin: 'layui-layer-molv',
        scrollbar:false,
        area: ['80%','90%'],
        btnAlign: 'c',
        btn: ['确定', '取消'],
        content: '<div class="layer_box"><div id="widgetBox6LayerView"></div></div>',
        success: function(index, layero){
            var data = {};
            getData(data, '#widgetBox6 .visual_layer_template', '#widgetBox6LayerView'); //弹出框内容渲染
        },
        yes: function(index, layero){
        },
        btn2:function(index, layero){
            layer.close(index);
        },
        cancel: function(index, layero){ 
            layer.close(index);
            return false; 
        } 
    });
}
