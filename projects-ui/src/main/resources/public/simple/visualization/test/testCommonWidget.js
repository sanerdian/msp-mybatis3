
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