
    function getParent() {
        let parent = '#app';
        let id = $("#app .widget_active.container_content:visible").attr('id');
        if (id) {
            parent = '#' + id;
        }
        return parent;
    }
$(function(){
    // creat widgets
    $("#tool_widget_login").click(function () {
        let widget = new WidgetLogin(getParent());
        if (widget.widgetId) {
            widgetArray.push(widget);
        }
    });
    
    $("#tool_widget_text").click(function () {
        let widget = new WidgetText(getParent());
        if (widget.widgetId) {
            widgetArray.push(widget);
        }
    });
    $("#tool_widget_image").click(function () {
        let widget = new WidgetImage(getParent());
        if (widget.widgetId) {
            widgetArray.push(widget);
        }
    });
    $("#tool_widget_tag").click(function () {
        let widget = new WidgetTag(getParent());
        if (widget.widgetId) {
            widgetArray.push(widget);
        }
    });
    $("#tool_widget_partition").click(function () {
        let widget = new WidgetPartition(getParent());
        if (widget.widgetId) {
            widgetArray.push(widget);
        }
    });
    $("#tool_widget_sidebar").click(function () {
        let widget = new WidgetSidebar('#sidebar', commonData.sidebar, true);
        if (widget.widgetId) {
            widgetArray.push(widget);
        }
    });
    $("#tool_widget_navbar").click(function () {
        let widget = new WidgetNavbar('#navbar', commonData.navbar, true);
        if (widget.widgetId) {
            widgetArray.push(widget);
        }
    });
    $("#tool_widget_table").click(function () {
        let widget = new WidgetTable(getParent());
        if (widget.widgetId) {
            widgetArray.push(widget);
        }
    });
    $("#tool_widget_button").click(function () {
        let widget = new WidgetButton(getParent());
        if (widget.widgetId) {
            widgetArray.push(widget);
        }
    });
    $("#tool_widget_search").click(function () {
        let widget = new WidgetSearch(getParent());
        if (widget.widgetId) {
            widgetArray.push(widget);
        }
    });
    $("#tool_widget_rect").click(function () {
        let widget = new WidgetRect(getParent());
        if (widget.widgetId) {
            widgetArray.push(widget);
        }
    });
    $("#tool_widget_graphic").click(function () {
        let widget = new WidgetGraphic(getParent());
        if (widget.widgetId) {
            widgetArray.push(widget);
        }
    });
    $("#tool_widget_video").click(function () {
        let widget = new WidgetVideo(getParent());
        if (widget.widgetId) {
            widgetArray.push(widget);
        }
    }); 
})

//组件分类列表
function componentSort(id,tpl,view){    
    var params = {
        "entity": {
            "pid": id
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
        console.log('basic_widget',data.obj.records);
        // getData(data.obj.records, '#basicTemplate', '#basicView');
        getData(data.obj.records, tpl, view);
         /*组件的点击事件 开始*/
        $("#tool_widget_text").click(function () {
            let widget = new WidgetText(getParent());
            if (widget.widgetId) {
                widgetArray.push(widget);
            }
        });
        $("#tool_widget_image").click(function () {
            let widget = new WidgetImage(getParent());
            if (widget.widgetId) {
                widgetArray.push(widget);
            }
        });
        $("#tool_widget_topLayout").click(function () {
            let widget = new topLayoutWidget(getParent());
            if (widget.widgetId) {
                widgetArray.push(widget);
            }
        });
        $("#tool_widget_leftBar").click(function () {
            let widget = new leftBarWidget(getParent());
            if (widget.widgetId) {
                widgetArray.push(widget);
            }
        });
        $("#tool_widget_tableList").click(function () {
            let widget = new tableListWidget(getParent());
            if (widget.widgetId) {
                widgetArray.push(widget);
            }
        });
         /*组件的点击事件 结束*/
    })
}
