function emptyEditor() {
    jsonSort(widgetArray, "renderId", true);
    widgetArray.forEach(item => {
        if (item && !item.containers) {
            item.destroy();
        }
    });
    widgetArray.forEach(item => {
        if (item && item.containers) {
            item.destroy();
        }
    });
    widgetArray = [];
}

function getPageJs() {

    let a = {};
    let pageJs = `
        /** __start_page_style_js_code__ **/
        let currentPageData = {bgColor: "${currentPageData.bgColor}"};
        (${loadPageStyle.toString()})();
        /** __end_page_style_js_code__ **/
        `;
    widgetArray.forEach((widget) => {
        let widgetJs = widget.jsCode();
        if (widgetJs) {
            pageJs += widgetJs;
        }
    });
    return pageJs;
}

function getPageHtml() {
    $("#app .table_container > table").children().remove();
    $("#app .table_container > div").children().remove();
    $("#app .table_container > div.layui-table-view").remove();
    return $("#app").html();
}

function savePageHtml(cb, pageHtmlData) {
    let reloadPage = $("#editor_content").is(":visible");
    if (!pageHtmlData) {
        pageHtmlData = {};
    }
    if (!pageHtmlData.html) {
        pageHtmlData.html = getPageHtml();
    }
    if (!pageHtmlData.js) {
        pageHtmlData.js = getPageJs();
    }
    if (!pageHtmlData.css) {
        pageHtmlData.css = "/** add style here to override default! **/";
    }
    if (!pageHtmlData.name) {
        pageHtmlData.name = currentPage;
    }
    if (reloadPage) {
        reloadFromCacheData(false, function () {
            saveHtml();
        });
    } else {
        saveHtml();
    }

    function saveHtml() {
        $.ajax({
            type: 'get',
            url: '/ProductDevelopment/simple/visualization/template/template.html',
            cache: false,
            success: function (html) {
                let pageHtml = {};
                pageHtml.name = pageHtmlData.name;
                pageHtml.html = html.replace("<app_holder></app_holder>", pageHtmlData.html)
                    .replace("<app_js_holder></app_js_holder>", pageHtmlData.js)
                    .replace(".css_holder{}", pageHtmlData.css)
                    .replace("<sidebar_holder></sidebar_holder>", sidebarHtml)
                    .replace("<navbar_holder></navbar_holder>", navbarHtml);
                if (reloadPage) {
                    reloadFromCacheData(true);
                }
                $.ajax({
                    type: 'post',
                    url: 'http://127.0.0.1:3000/html',
                    data: JSON.stringify(pageHtml),
                    contentType: 'application/json',
                    crossDomain: true,
                    xhrFields: {
                        withCredentials: true
                    },
                    success: function (res) {
                        if (typeof cb === "function") {
                            cb();
                        }
                    },
                    error: function (e) {
                        layer.msg("网络请求错误", {icon: 2, time: 2000});
                    }
                })
            }
        })
    }
}


/**
 * 页面设置
 * @param elem 页面所在容器的选择器
 * @param pageData 加载数据
 * @param ok 确定回调
 */
 let layer_index;
function loadPageSet(elem, pageData, ok) {
    
    if (!currentPage || currentPage.length === 0) {
        // layer.msg("请选择有效的页面", {icon: 0, time: 1000}); 
     layer_index = layer.open({
            type: 1,
            title: '标题设置',
            // id: `${_this.widgetId}_set`,
            area: ['542px'],
            offset: '200px',
            content:$("#editor_page_table_content"),
            // content:moduleGain(),
            resize: true,
            shadeClose: false,
            moveType: 0,
            shade: 0.5,
            anim: -1,
            closeBtn: 1
        });
        return;
    }
    let data = {
        name: '',
        title: '',
        parentPage: '',
        bgColor: 'rgba(50, 183, 188, 1)',
        bgImage: ''
    };
    if (pageData.name) {
        data = Object.assign({}, pageData)
    }
    laytpl($("#tpl_editor_page_set").html()).render(data, function (uiContent) {
        let i_tab_page_index;
        $(elem).html(uiContent);
        let treeId_pageAdd = loadPageTree(elem, "tree_" + elem, function (node) {
            console.log(node);
            data.parentPage = node.id;
        });
        i_tab_page_index = layer.open({
            type: 1,
            title: ['页面设置'],
            id: 'layer_tab_page_add',
            area: ['577px', '495px'],
            content: $(elem),
            resize: true,
            shadeClose: false,
            moveType: 0,
            shade: 0.5,
            anim: -1
        });
        reloadPageTreeDataWithRoot(treeId_pageAdd);
        $(elem + " .new_page_ok").click(function (e) {
            e.preventDefault();
            data.title = $("#page_name").val();
            ok(data);
            layer.close(i_tab_page_index);
        });
        $(elem + " .new_page_cancel").click(function (e) {
            e.preventDefault();
            layer.close(i_tab_page_index);
        });
        colorpicker.render({
            elem: elem + ' .color_picker'
            , color: data.bgColor
            , format: 'rgb'
            , alpha: true
            , size: 'xs'
            , change: function (color) {
                console.log(color);
                // $(".layui-colorpicker-main-input > .layui-btn-container > .layui-btn-primary").trigger("click");
            }
            , done: function (color) {
                console.log(color)
                console.log(color.length)
                if (color.length > 0) {
                    data.bgColor = color;
                    data.bgImage = '';
                    $('.page_bg_selector_preview img').attr("src", ''); // 清除已选图片
                }
                $(".page_bg .page_bg_selector").hide();
            }
        });
        // $("#editor_page_add i.layui-colorpicker-trigger-i").removeClass("layui-icon-down"); // 移除调色板上的箭头样式
        $(".page_bg .page_bg_picker").click(function () {
            $(".page_bg .page_bg_selector").show();
        });
        $(".page_bg .page_bg_selector i.layui-icon-close").click(function () {
            $(".page_bg .page_bg_selector").hide();
        });
        upload.render({
            elem: elem + ' .page_bg_selector_text'
            , url: '/api/upload/'
            , exts: 'jpg|png|svg'
            , size: 1024
            , accept: 'file'
            , acceptMime: '.jpg|.png|.svg'
            , before: function (obj) {
                layer.load();
            }
            , done: function (res, index, upload) {
                if (res.code === 0) {
                    $('.page_bg_selector_preview img').attr("src", res.src);
                    $('.page_bg_picker img').attr("src", res.src);
                    // TODO 添加返回的图片地址，添加到res.bgImage
                    data.bgColor = '';
                    $(".layui-colorpicker-main-input > .layui-btn-container > .layui-btn-primary").trigger("click"); // 清除已选颜色
                }
                let item = this.item;
                layer.closeAll('loading');
            }
            , error: function (index, upload) {
                layer.closeAll('loading');
            }
        });
    });
}

function setupDraggableWidget(widget, enable) {
    if (enable) {
        widget.draggable('enable');
    } else {
        widget.draggable('disable');
    }
}

function removeAllWidgetBorder() {
    $("div[class^='draggable']").find(".border_content").addClass("widget_inactive").removeClass("widget_active");
}

function activeWidgetBorder(widget) {
    // console.log(widget)
    removeAllWidgetBorder();
    widget.find(".border_content:visible:first").addClass("widget_active").removeClass("widget_inactive");
}

function setupActiveWidget(widget, parent, disableZone) {
    if (!parent) {
        parent = "#app";
    }
    widget.draggable({
        start: function () {
            $(".w_menu").hide();
            activeWidgetBorder(widget);
        },
        cancel: disableZone !== undefined ? disableZone : undefined,
        distance: 5,
        containment: parent,
        scroll: true
    });
    // widget.disableSelection();
    widget.click(function (e) {
        let clickable = $(e.target).css("cursor") === "pointer" || $(e.target).css("cursor") === "text";
        let isContent = e.target.className.indexOf('content') !== -1;
        if (isContent || !clickable) {
            e.stopPropagation();
        }
        $(".w_menu").hide();
        activeWidgetBorder(widget);
    });
}

function reloadFromCacheData(editable, callback) {
    let jsonArray = pageToJson();
    emptyEditor();
    loadFromData(jsonArray, editable, callback);
}

function loadFromData(data, editable, callback) {
    jsonSort(data, "renderId");
    let containersPromise = [];
    data.forEach(function (item) {
        let promise = new Promise(function (resolve) {
            let widget;
            switch (item.type) {
                case 3:
                    widget = new WidgetTag("#app", item, editable);
                    break;
                case 12:
                    widget = new WidgetPartition("#app", item, editable);
                    break;
            }
            if (widget && widget.widgetId) {
                widgetArray.push(widget);
            }
            resolve();
        });
        containersPromise.push(promise);
    });
    Promise.all(containersPromise).then(function () {
        $.each(data, function (i, item) {
            let widget;
            switch (item.type) {
                case 0:
                    widget = new WidgetLogin('#app', item, editable);
                    break;
                case 1:
                    widget = new WidgetText('#app', item, editable);
                    break;
                case 2:
                    widget = new WidgetImage('#app', item, editable);
                    break;
                // case 3:
                //     widget = new WidgetTag(item, true);
                //     break;
                case 4:
                    if (commonData.sidebar) {
                        commonData.sidebar.widgetId = item.widgetId;
                        commonData.sidebar.type = item.type;
                        commonData.sidebar.index = item.index;
                    } else {
                        commonData.sidebar = item;
                    }
                    widget = new WidgetSidebar('#sidebar', commonData.sidebar, editable);
                    break;
                case 5:
                    if (commonData.navbar) {
                        commonData.navbar.widgetId = item.widgetId;
                        commonData.navbar.type = item.type;
                        commonData.navbar.index = item.index;
                    } else {
                        commonData.navbar = item;
                    }
                    widget = new WidgetNavbar('#navbar', commonData.navbar, editable);
                    break;
                case 6:
                    widget = new WidgetTable('#app', item, editable)
                    break;
                case 7:
                    widget = new WidgetButton('#app', item, editable)
                    break;
                case 8:
                    widget = new WidgetSearch('#app', item, editable)
                    break;
                case 9:
                    widget = new WidgetRect('#app', item, editable)
                    break;
                case 10:
                    widget = new WidgetGraphic('#app', item, editable)
                    break;
                case 11:
                    widget = new WidgetVideo('#app', item, editable)
                    break;
            }
            if (widget && widget.widgetId) {
                widgetArray.push(widget);
            }
        });
        if (callback && typeof callback === "function") {
            callback();
        }
    });
}

function loadPageStyle() {
    let background = '';
    if (currentPageData.bgColor) {
        background = currentPageData.bgColor;
    }
    $("#editor_content").css({background: background});
}

function loadCurrentPage(editable, callback) {
    if (!currentPage || currentPage.length === 0) {
        layer.msg("请选择有效的页面", {icon: 0, time: 1000});
        return;
    }
    if (!editable) {
        editable = false;
    }
    let index = layer.load(1);
    $.getJSON(`../json/${currentPage}.json?_=${new Date().getTime()}`, function (res) {
        currentPageData = res;
        currentPageTitle = res.title;
        loadPageStyle();
        loadFromData(currentPageData.data, editable, function () {
            layer.close(index);
            if (typeof callback === "function") {
                callback();
            }
        });
    });
}

function pageToJson() {
    let jsonArray = [];
    let newWidgetArray = []; // 解除无效引用
    widgetArray.forEach((widget) => {
        let widgetJson = widget.toJson();
        if (widgetJson) {
            jsonArray.push(widgetJson);
            newWidgetArray.push(widget);
        }
    });
    widgetArray = newWidgetArray;
    return jsonArray;
}

function initEditorNav() {
    initEditorNavClick();
    let treeId = loadPageTree(`#editor_nav`, `tree_editor_nav`, function (node) {
        console.log(node)
        if (node && node.field && node.field.length > 0) {
            console.log(node.field);
            currentPage = node.field;
            if ($("#editor_content").is(":visible")) {
                $("#tools").show();
                emptyEditor();
                loadCurrentPage(true, function () {
                    $("html").css({zoom: 1});
                    layer.msg("左键拖拽，右键设置", {icon: 1, time: 1000})
                });
            } else if ($("#editor_code").is(":visible")) {
                $("#tools").hide();
                loadCurrentPageCode();
            }
        } else {
            layer.msg("请选择有效的页面", {icon: 0, time: 1000});
        }
    });
    reloadPageTreeData(treeId);

    $(".list_button").click(function () {
        layer.open({
            type: 1,
            title: ['管理页面'],
            id: 'layer_page_list',
            area: ['775px', '616px'],
            content: $("#editor_page_table_container"),
            resize: true,
            shadeClose: false,
            moveType: 0,
            shade: 0.5,
            anim: -1
        });
        table.reload('editor_page_table');
    });

    $("#editor_nav .page_set").click(function () {
        loadPageSet("#editor_page_set", currentPageData, function (res) {
            // TODO 确定回调
            console.log(res);
            currentPageData = res;
            currentPageTitle = res.title;
            loadPageStyle();
        });
    });

    $('#save').click(function () {
        if (!currentPage || currentPage.length === 0) {
            layer.msg("请选择要保存的页面", {icon: 0, time: 1000});
            return;
        }
        if ($("#editor_code").is(":visible")) {
            savePageFromCode();
            return;
        }
        if (!$("#editor_content").is(":visible")) {
            layer.msg("编辑器页面错误", {icon: 0, time: 1000});
            return;
        }
        layer.confirm("编辑器修改的代码将失效，确认保存？", {
            icon: 3,
            title: "系统提示",
            btn: ['确认', '取消']
        }, function (index) {
            layer.close(index);
            savePageFromUi();
        }, function (index) {
            layer.close(index);
        });

        function savePageFromCode() {
            let index = layer.load(1);
            savePageHtml(function () {
                layer.close(index);
                layer.msg("保存成功", {icon: 1, time: 1000})
            }, {
                name: currentPage,
                html: getCode(editor_html),
                js: getCode(editor_js),
                css: getCode(editor_css)
            });
        }

        function savePageFromUi() {
            let index = layer.load(1);
            console.log(widgetArray.length);
            let jsonArray = pageToJson();
            console.log(jsonArray);
            currentPageData.name = currentPage;
            currentPageData.data = jsonArray;
            $.ajax({
                type: 'post',
                url: 'http://127.0.0.1:3000/json',
                data: JSON.stringify(currentPageData),
                contentType: 'application/json',
                crossDomain: true,
                xhrFields: {
                    withCredentials: true
                },
                success: function (res) {
                    console.log('success')
                    saveCommonData(function () {
                            savePageHtml(function () {
                                layer.close(index);
                                layer.msg("保存成功", {icon: 1, time: 1000})
                            });
                        },
                        function () {
                            layer.close(index);
                            layer.msg("保存失败", {icon: 2, time: 1000})
                        });
                },
                error: function (e) {
                    layer.close(index);
                    console.log('error:' + e)
                    layer.msg("保存失败", {icon: 2, time: 1000})
                }
            })
        }
    });
    let zoom = document.body.clientWidth / 1920;
    $('#preview').click(function () {
        // $("#tools").hide();
        if (currentPage.length === 0) {
            layer.msg("请选择有效的页面", {icon: 0, time: 1000});
            return;
        }
        // loadPageStyle();
        // reloadFromCacheData(false);
        // $("html").css({zoom: zoom});
        let pageHtmlData = {};
        pageHtmlData.name = "preview";
        if ($("#editor_code").is(":visible")) {
            pageHtmlData.html = getCode(editor_html);
            pageHtmlData.css = getCode(editor_css);
            pageHtmlData.js = getCode(editor_js);
            emptyEditor();
            loadCurrentPage(true, function () {
                _savePageHtml();
            });
            return;
        }
        _savePageHtml();

        function _savePageHtml() {
            sidebarHtml = "";
            navbarHtml = "";
            for (let widget of widgetArray) {
                if (widget.type === 4) {
                    sidebarHtml = widget.html;
                }
                if (widget.type === 5) {
                    navbarHtml = widget.html;
                }
            }
            savePageHtml(function () {
                    window.open(`../dist/preview.html?_=${new Date().getTime()}`);
                },
                pageHtmlData);
        }
    });

    $('#cancel').click(function () {
        $("html").css({zoom: 1});
        if (!currentPage) {
            return;
        }
        if ($("#editor_content").is(":visible")) {
            $("#tools").show();
            emptyEditor();
            loadCurrentPage(true);
        } else if ($("#editor_code").is(":visible")) {
            loadCurrentPageCode();
        }
    });
}

function initToolBar() {
    let tool = $("#tools");
    let tool_bar1 = $("#tools .tool_bar1");
    let tool_bar2 = $("#tools .tool_bar2");
    let tool_content = $("#tools .tool_content");
    let tool_group_content = $("#tools .tool_main > div");
    let tool_group_zone = $("#tools div.group_zone");
    let tool_group_icon = $("#tools .group_icon");
    let tool_width = tool.width();
    let tool_height = tool.height();
    let tool_bar2_width = tool_bar2.width();
    let tool_bar2_height = tool_bar2.height();

    // tool_bar ui_init
    tool.draggable({
        containment: "#editor_content",
        scroll: false
    });

    function pre_show_content() {
        if (tool_group_zone.hasClass("bar_shrink")) {
            let left = tool.position().left;
            tool.css("left", left - tool_width + tool_bar2_width);
            tool.css("width", tool_width);
            tool.css("height", tool_height);
        }
        tool_bar1.show();
        tool_content.show();
        tool_group_content.hide();
        tool_group_zone.removeClass("bar_shrink");
        tool_group_icon.removeClass("group_icon_active");
    }

    function pre_hide_content() {
        let left = tool.position().left;
        tool.css("left", left + tool_width - tool_bar2_width);
        tool.css("width", tool_bar2_width);
        tool.css("height", tool_bar2_height);
        tool_bar1.hide();
        tool_content.hide();
        tool_group_zone.addClass("bar_shrink");
        tool_group_icon.removeClass("group_icon_active");
    }

    pre_hide_content();
    $("#tools .tool_close").click(function () {
        pre_hide_content();
    });
    $("#tools .basic_widget_icon").click(function () {
        pre_show_content();
        $("#tools #basic_widget").show();
        $(this).addClass("group_icon_active");
        //基础组件分类列表
        componentSort('35','#basicTemplate', '#basicView');
    });
    $("#tools .app_widget_icon").click(function () {
        pre_show_content();
        $("#tools #app_widget").show();
        $(this).addClass("group_icon_active");
        //应用组件分类列表
        componentSort('138','#appTemplate', '#appView');
    });
    $("#tools .report_widget_icon").click(function () {
        pre_show_content();
        $("#tools #report_widget").show();
        $(this).addClass("group_icon_active");
    });
    $("#tools .page_widget_icon").click(function () {
        pre_show_content();
        $("#tools #page_widget").show();
        $(this).addClass("group_icon_active");
    });
    $("#tools .custom_widget_icon").click(function () {
        pre_show_content();
        $("#tools #custom_widget").show();
        $(this).addClass("group_icon_active");
    });

    // function getParent() {
    //     let parent = '#app';
    //     let id = $("#app .widget_active.container_content:visible").attr('id');
    //     if (id) {
    //         parent = '#' + id;
    //     }
    //     return parent;
    // }

    // // creat widgets
    // $("#tool_widget_login").click(function () {
    //     let widget = new WidgetLogin(getParent());
    //     if (widget.widgetId) {
    //         widgetArray.push(widget);
    //     }
    // });
    
    // /*组件的点击事件 开始*/
    // $("#tool_widget_text_test").click(function () {
    //     console.log('click')
    //     let widget = new WidgetTextTest(getParent());
    //     if (widget.widgetId) {
    //         widgetArray.push(widget);
    //     }
    // });
    // /*组件的点击事件 结束*/

    // $("#tool_widget_text").click(function () {
    //     let widget = new WidgetText(getParent());
    //     if (widget.widgetId) {
    //         widgetArray.push(widget);
    //     }
    // });
    // $("#tool_widget_image").click(function () {
    //     let widget = new WidgetImage(getParent());
    //     if (widget.widgetId) {
    //         widgetArray.push(widget);
    //     }
    // });
    // $("#tool_widget_tag").click(function () {
    //     let widget = new WidgetTag(getParent());
    //     if (widget.widgetId) {
    //         widgetArray.push(widget);
    //     }
    // });
    // $("#tool_widget_partition").click(function () {
    //     let widget = new WidgetPartition(getParent());
    //     if (widget.widgetId) {
    //         widgetArray.push(widget);
    //     }
    // });
    // $("#tool_widget_sidebar").click(function () {
    //     let widget = new WidgetSidebar('#sidebar', commonData.sidebar, true);
    //     if (widget.widgetId) {
    //         widgetArray.push(widget);
    //     }
    // });
    // $("#tool_widget_navbar").click(function () {
    //     let widget = new WidgetNavbar('#navbar', commonData.navbar, true);
    //     if (widget.widgetId) {
    //         widgetArray.push(widget);
    //     }
    // });
    // $("#tool_widget_table").click(function () {
    //     let widget = new WidgetTable(getParent());
    //     if (widget.widgetId) {
    //         widgetArray.push(widget);
    //     }
    // });
    // $("#tool_widget_button").click(function () {
    //     let widget = new WidgetButton(getParent());
    //     if (widget.widgetId) {
    //         widgetArray.push(widget);
    //     }
    // });
    // $("#tool_widget_search").click(function () {
    //     let widget = new WidgetSearch(getParent());
    //     if (widget.widgetId) {
    //         widgetArray.push(widget);
    //     }
    // });
    // $("#tool_widget_rect").click(function () {
    //     let widget = new WidgetRect(getParent());
    //     if (widget.widgetId) {
    //         widgetArray.push(widget);
    //     }
    // });
    // $("#tool_widget_graphic").click(function () {
    //     let widget = new WidgetGraphic(getParent());
    //     if (widget.widgetId) {
    //         widgetArray.push(widget);
    //     }
    // });
    // $("#tool_widget_video").click(function () {
    //     let widget = new WidgetVideo(getParent());
    //     if (widget.widgetId) {
    //         widgetArray.push(widget);
    //     }
    // });
}

function  moduleGain(){
    ajax(service_prefix.member,'/menu/tree?type=2','get').then(function (data){
        console.log(data);
		if(data.success){			
        table.render({
        elem: '#editor_page_table',
        height: 400,
        width: 720,
        data:data.obj,
        // limit: data.size,
        // url: 'http://36.138.169.165:8081/member/menu/tree?type=2',
        skin: 'nob',
        even: true,
        size: 'lg', 
        limit:30, 
        cols: [[
            {field: 'name', title: '页面', width: 223, unresize: true},
            {
                field: 'visible', title: '菜单显示', width: 102, unresize: true, templet: function (d) {
                    if (d.visible)
                        return '<i class="layui-icon layui-icon-ok i_tab i_tab_visible" lay-event="visible"></i>';
                    else
                        return '<i class="layui-icon layui-icon-close i_tab i_tab_hidden" lay-event="visible"></i>';
                }
            },
            {field: 'order', title: '排序', width: 179, unresize: true, toolbar: '#i_tab_order_bar'},
            {title: '操作', width: 198, unresize: true, toolbar: '#i_tab_right_bar'},
        ]],
        done(res) {
                   $("#editor_page_table_container .total_number").text(`总共 ${res.data.length} 个`)
       }
            // ,page: true
        });
		}else{
			console.log(data.msg);
		}
	})
}

function initNavPageTable() {
    function setTableData(obj, data, columns) {
        table.render({
            elem: '#editor_page_table',
        height: 400,
        width: 720,
        data:data.records,
        // limit: data.size,
        // url: 'http://36.138.169.165:8081/member/menu/tree?type=2',
        skin: 'nob',
        even: true,
        size: 'lg',  
        limit:30,
        cols: [[
            {field: 'name', title: '页面', width: 223, unresize: true},
            {
                field: 'visible', title: '菜单显示', width: 102, unresize: true, templet: function (d) {
                    if (d.visible)
                        return '<i class="layui-icon layui-icon-ok i_tab i_tab_visible" lay-event="visible"></i>';
                    else
                        return '<i class="layui-icon layui-icon-close i_tab i_tab_hidden" lay-event="visible"></i>';
                }
            },
            {field: 'order', title: '排序', width: 179, unresize: true, toolbar: '#i_tab_order_bar'},
            {title: '操作', width: 198, unresize: true, toolbar: '#i_tab_right_bar'},
        ]],
            // ,page: true
        });
    }

    // ajax(service_prefix.member,'/menu/tree?type=2','get').then(function (data){
    //     console.log(data);
	// 	if(data.success){			
	// 		// setTableData('#editor_page_table',data.obj);
            
    //     table.render({
    //         elem: '#editor_page_table',
    //     height: 400,
    //     width: 720,
    //     data:data.obj,
    //     // limit: data.size,
    //     // url: 'http://36.138.169.165:8081/member/menu/tree?type=2',
    //     skin: 'nob',
    //     even: true,
    //     size: 'lg', 
    //     limit:30, 
    //     cols: [[
    //         {field: 'name', title: '页面', width: 223, unresize: true},
    //         {
    //             field: 'visible', title: '菜单显示', width: 102, unresize: true, templet: function (d) {
    //                 if (d.visible)
    //                     return '<i class="layui-icon layui-icon-ok i_tab i_tab_visible" lay-event="visible"></i>';
    //                 else
    //                     return '<i class="layui-icon layui-icon-close i_tab i_tab_hidden" lay-event="visible"></i>';
    //             }
    //         },
    //         {field: 'order', title: '排序', width: 179, unresize: true, toolbar: '#i_tab_order_bar'},
    //         {title: '操作', width: 198, unresize: true, toolbar: '#i_tab_right_bar'},
    //     ]],
    //     done(res) {
    //                $("#editor_page_table_container .total_number").text(`总共 ${res.data.length} 个`)
    //    }
    //         // ,page: true
    //     });
	// 	}else{
	// 		console.log(data.msg);
	// 	}
	// })
    // table.render({
    //     // id: 'editor_page_table',
    //     elem: '#editor_page_table',
    //     height: 400,
    //     width: 720,
    //     // data:data.records,
    //     limit: data.size,
    //     // url: 'http://36.138.169.165:8081/member/menu/tree?type=2',
    //     skin: 'nob',
    //     even: true,
    //     size: 'lg',  
    //     cols: [[
    //         {field: 'name', title: '页面', width: 223, unresize: true},
    //         {
    //             field: 'visible', title: '菜单显示', width: 102, unresize: true, templet: function (d) {
    //                 if (d.visible)
    //                     return '<i class="layui-icon layui-icon-ok i_tab i_tab_visible" lay-event="visible"></i>';
    //                 else
    //                     return '<i class="layui-icon layui-icon-close i_tab i_tab_hidden" lay-event="visible"></i>';
    //             }
    //         },
    //         {field: 'order', title: '排序', width: 179, unresize: true, toolbar: '#i_tab_order_bar'},
    //         {title: '操作', width: 198, unresize: true, toolbar: '#i_tab_right_bar'},
    //     ]],
    //     // parseData(res) {
    //     //     return {
    //     //         'status': 0,
    //     //         'data': res
    //     //     }
    //     // },
    //     // request: {
    //     //     pageName: 'curr',
    //     //     limitName: 'nums'
    //     // },
    //     // response: {
    //     //     statusName: 'status'
    //     // },
    //     done(res) {
    //         $("#editor_page_table_container .total_number").text(`总共 ${res.data.length} 个`)
    //     }
    // })
    moduleGain()
    table.on('tool(filter_editor_page_table)', (obj) => {
        switch (obj.event) {
            case 'visible':
                let visible_sel = obj.tr.find("td:nth-child(2) i");
                if (obj.data.visible) {
                    visible_sel.addClass("layui-icon-close");
                    visible_sel.removeClass("layui-icon-ok");
                } else {
                    visible_sel.addClass("layui-icon-ok");
                    visible_sel.removeClass("layui-icon-close");
                }
                obj.update({visible: !obj.data.visible})
                obj.data.visible = !obj.data.visible;
                console.log(table.cache['editor_page_table'])
                break;
            case 'update' :
                layer.alert('修改数据：' + `<input  id="zss" value="${obj.data.name}"></input>`, (index) => {
                    layer.close(index)
                    table.reload('editor_page_table')
                    const jsonData={
                        "active":"1",
                        "companyId"	:"1167323268476895233",
                        "name":$('#zss')[0].value,
                        "parentId":	"0",
                        "seq":	"0",
                        "type": 2,
                      }
                    moduleUpdate(obj.data.id,jsonData)
                    moduleGain()
                })
                break;
            case 'copy':
                break;
            case 'add':
                break;
            case 'delete' :
                layer.alert('删除数据：' + obj.data.id, (index) => {
                    layer.close(index)
                    table.reload('editor_page_table')
                    moduleDelete(obj.data.id)
                    moduleGain()
                })
                break;
            case 'up':
                moduleUp(obj.data.id)
                break;
            case 'down':
                moduleDown(obj.data.id)
                break;
            case 'left':
                moduleLeft(obj.data.id)
                break;
            case 'right':
                moduleRight(obj.data.id)
                break;
        }
    })

    $("#editor_table_ok").click(function () {
        console.log(111);
        console.log(table.cache['editor_page_table']);
        layer.closeAll();
        moduleTree()
        moduleGain()
    });
    $("#editor_table_cancel").click(function () {
        layer.closeAll();
    });
    $("#editor_table_ok2").click(function () {
        // console.log();
        // console.log(33,this);
        // console.log(111);
        const jsonData={
            "active":"1",
            "companyId"	:"1167323268476895233",
            "name": $('#gain_title')[0].value,
            "parentId":	"0",
            "seq":	"0",
            "type": 2,
          }
        // console.log(table.cache['editor_page_table']);
        ajax('','/member/menu','POST',JSON.stringify(jsonData)).then(function(res){
            console.log(res);
        })
        layer.close(layer_index);
        moduleGain()
    });
    $("#editor_table_cancel2").click(function () {
        layer.close(layer_index);
    });
    $("#editor_page_table_container .i_tab_add_page").click(function () {
        loadPageSet("#editor_page_add", {}, function (res) {
            // TODO 确定回调
            console.log(res);
        });
    });
}

function initEditorNavClick() {
    // 点击编辑器导航栏，取消组件选中
    $("#editor_tab").click(function () {
        $(".w_menu").hide();
        removeAllWidgetBorder();
    });
}

function initCommonData(callback, param) {
    // $.getJSON(`../json/common.json?_=${new Date().getTime()}`, function (res) {
    //     commonData.sidebar = res.sidebar;
    //     commonData.navbar = res.navbar;
    //     $.getJSON(`../json/page.json?_=${new Date().getTime()}`, function (res) {
    //         commonData.pages = res;
    //         if (typeof callback === "function") {
    //             callback(param);
    //         }
    //     });
    // });
    moduleTree()
}

function moduleTree(){
    ajax('','/member/menu/tree?type=2','get').then(function (data){
        console.log(data);
        if(data.success){	
            // console.log(JSON.parse(data));		
            // commonData.pages = data;
            // console.log(JSON.parse(data).obj);
            // _this.id = JSON.parse(data).obj.id;	
            commonData.pages = data.obj		
        }else{
            console.log(data.msg);
        }
    })
}

function saveCommonData(success, error) {
    widgetArray.forEach((widget) => {
        if (widget.type === 4) {
            let widgetJson = widget.toJson();
            if (widgetJson) {
                commonData.sidebar = widgetJson;
            }
            sidebarHtml = widget.html;
        }
        if (widget.type === 5) {
            let widgetJson = widget.toJson();
            if (widgetJson) {
                commonData.navbar = widgetJson;
            }
            navbarHtml = widget.html;
        }
    });
    commonData.name = "common";
    $.ajax({
        type: 'post',
        url: 'http://127.0.0.1:3000/json',
        data: JSON.stringify(commonData),
        contentType: 'application/json',
        crossDomain: true,
        xhrFields: {
            withCredentials: true
        },
        success: function (res) {
            success();
        },
        error: function (e) {
            error();
        }
    })
}
/* 
删除菜单名称 
*/
function moduleDelete(id){
    ajax('','/member/menu/'+id,'DELETE').then(function (data){
        if(data.success){					
            layer.msg('删除成功');
        }else{
            console.log(data.msg);
        }
    })
}
/* 
修改菜单名称 
*/
function moduleUpdate(id,jsonData){
    ajax('','/member/menu/'+id,'PUT',JSON.stringify(jsonData)).then(function (data){
        if(data.success){					
            layer.msg('修改成功');
        }else{
            console.log(data.msg);
        }
    })
}
/* 
上移 
*/
function moduleUp(id){
    ajax('','/visual/page/pageUp?pageId='+id,'POST').then(function (data){
        if(data.success){					
            layer.msg('上移成功');
        }else{
            console.log(data.msg);
        }
    })
}
/* 
下移 
*/
function moduleDown(id){
    ajax('','/visual/page/pageDown?pageId='+id,'POST').then(function (data){
        if(data.success){					
            layer.msg('下移成功');
        }else{
            console.log(data.msg);
        }
    })
}
/* 
左移 
*/
function moduleLeft(id){
    ajax('','/visual/page/pageLeft?pageId='+id,'POST').then(function (data){
        if(data.success){					
            layer.msg('左移成功');
        }else{
            console.log(data.msg);
        }
    })
}
/* 
右移 
*/
function moduleRight(id){
    ajax('','/visual/page/pageRight?pageId='+id,'POST').then(function (data){
        if(data.success){					
            layer.msg('右移成功');
        }else{
            console.log(data.msg);
        }
    })
}
