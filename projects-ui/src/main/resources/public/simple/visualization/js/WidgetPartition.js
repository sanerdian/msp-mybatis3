/*__start_WidgetPartition__*/
/**
 * 内容区组件（容器组件内添加普通组件，需要先选中容器组件后再添加）
 */
function WidgetPartition() {
    this.containers = [{id: 0, title: '默认标签'}]; // 容器组件的编号
    this.create.apply(this, arguments);
}

WidgetPartition.prototype = {
    widgetId: 'widget_partition_id',
    parent: '#app', // 容器组件不可修改
    type: 12,
    index: -1,
    left: 500.0,
    top: 50.0,
    zIndex: -1,
    renderId: -1,
    width: 500, // 默认原始尺寸
    height: 500,
    style: 0, // 0标题在上, 1标题在下, 2标题在左
    background: 'rgba(255, 255, 255, 0.5)',
    border: true,
    scroll: false,
    align: 'left', // left, center
    treeData: "",
    indexList: [],
    indexMax: [-1],
    menubar: [],
    settingsContent: [],
    limit: 999,
    editable: false,
    containerIndexMax: [-1],
    pickerColor: '',
    select: function (child) {
        return $("#" + this.widgetId + " " + child);
    },
    select1: function (child) {
        if (child) {
            return $("#" + this.widgetId + "_menu " + child);
        } else {
            return $("#" + this.widgetId + "_menu");
        }
    },
    select2: function (child) {
        if (child) {
            return $("#" + this.widgetId + "_settings " + child);
        } else {
            return $("#" + this.widgetId + "_settings");
        }
    },
    currentWidget: function () {
        return $("#" + this.widgetId);
    },
    currentMenu: function () {
        return $(`#${this.widgetId}_menu`);
    },
    currentSettings: function () {
        return $(`#container_${this.widgetId}_settings`);
    },
    /**
     * 初始化组件数据
     * @param data 组件数据
     */
    dataInit: function (data) {
        this.data = data;
        this.widgetId = data.widgetId;
        if (data.parent) {
            this.parent = data.parent;
        }
        this.type = data.type;
        if (data.left) {
            this.left = data.left;
        }
        if (data.top) {
            this.top = data.top;
        }
        if (data.width) {
            this.width = data.width;
        }
        if (data.height) {
            this.height = data.height;
        }
        this.zIndex = data.zIndex;
        this.index = data.index;
        this.renderId = data.renderId;
        this.style = data.style;
        this.background = data.background;
        this.border = data.border;
        this.scroll = data.scroll;
        this.align = data.align;
        this.containers = data.containers;
        this.containers.forEach(item => {
            if (item.id > this.containerIndexMax) {
                this.containerIndexMax = item.id;
            }
        });
    },
    /**
     * 根据数据渲染组件
     */
    render: function () {
        let _this = this;
        laytpl($("#module_partition").html()).render(_this.data, function (uiContent) {
            $(_this.parent).append(uiContent);
        });
        _this.currentWidget().css({top: _this.top, left: _this.left, zIndex: _this.zIndex});
        if (_this.editable) {
            laytpl($("#module_partition_menu").html()).render(_this.data, function (menubar) {
                _this.menubar[0] = menubar;
                $("#menu").append(_this.menubar[0])
                laytpl($("#module_partition_set").html()).render(_this.data, function (settingsContent) {
                    _this.settingsContent[0] = settingsContent;
                    $("#settings").append(`<div id='container_${_this.widgetId}_settings'></div>`);
                    _this.currentSettings().append(_this.settingsContent[0])
                    form.render();
                    _this.init();
                });
            });
        } else {
            form.render();
            _this.init();
        }
    },
    /**
     * 创建组件
     * @param parent 父组件elem
     * @param data 加载组件参数，为空时新建
     * @param editable 是否可编辑，为空时不可编辑
     */
    create: function (parent, data, editable) {
        let _this = this;
        if (_this.indexList.length >= _this.limit) {
            layer.msg("当前组件超过最大数量限制！")
            _this.widgetId = undefined;
            return;
        }
        if (parent && parent.length > 0) {
            _this.parent = parent;
        }
        if (!data) {
            _this.index = ++_this.indexMax[0];
            _this.zIndex = ++zIndexMax;
            _this.renderId = ++renderIdMax;
            _this.widgetId = _this.widgetId + "_" + _this.index;
            console.log("create new widget index=" + _this.index);
            _this.editable = true;
            _this.data = this.toJson();
        } else {
            _this.dataInit(data);
            if (_this.index > _this.indexMax[0]) {
                _this.indexMax[0] = _this.index;
            }
            if (_this.zIndex > zIndexMax) {
                zIndexMax = _this.zIndex;
            }
            if (_this.renderId > renderIdMax) {
                renderIdMax = _this.renderId;
            }
            if (editable) {
                _this.editable = true;
                console.log(`modify widget, type=${_this.type}, index=${_this.index}`);
            } else {
                _this.editable = false;
                console.log(`loading widget, type=${_this.type}, index=${_this.index}`);
            }
        }
        if (_this.type !== 12) {
            layer.msg("数据格式错误");
            return;
        }
        _this.indexList.push(_this.index);
        console.log(_this.indexList);
        if ($("#module_partition").length > 0) {
            _this.render();
            return;
        }
        _this.loadTmpl(function () {
            _this.render();
        });
    },
    /**
     * 加载组件模板
     */
    loadTmpl: function (callback) {
        $("#template").append("<div id='template_partition'></div>");
        $("#template_partition").load(`../module/partition.html?_=${new Date().getTime()}`, function () {
            callback();
        });
    },
    /**
     * 初始化组件
     */
    init: function () {
        const _this = this;
        _this.uiStyleInit();
        _this.uiInit(_this);
        if (_this.editable) {
            _this.settingsInit();
            // 通过disableDragZone设置容器内不可拖拽的区域
            setupActiveWidget(_this.currentWidget(), _this.parent, '.disableDragZone');
        }
    },
    /**
     * 初始化用户界面样式
     */
    uiStyleInit: function () {
        let _this = this;
        let border_content = _this.currentWidget().find('.border_content.container_content');
        if (_this.width > 0) {
            _this.currentWidget().css({"width": _this.width});
            border_content.css({"width": _this.width});
        }
        if (_this.height > 0) {
            _this.currentWidget().css({"height": _this.height});
            border_content.css({"height": _this.height});
        }
        border_content.css({background: _this.background});
        if (_this.border) {
            border_content.addClass("tag_border");
        }
    },
    /**
     * 初始化用户界面
     */
    uiInit: function (_this) {
        let top = parseInt(_this.currentWidget().css("top"));
        let height = parseInt(_this.currentWidget().css("height"));
        if (top + height > 1028) {
            $("#editor_content").css({height: top + height + 52});
            $("#app").css({height: top + height});
        }
    },
    /**
     * 初始化设置菜单参数
     */
    settingsInit: function () {
        let _this = this;
        console.log('menu & settings init');
        let widget = $(`#${_this.widgetId}`);

        if (_this.border) {
            _this.select2(".border_img").addClass("border_img_active");
        }
        _this.select2(".border_img").click(function () {
            $(this).toggleClass("border_img_active");
        });

        _this.select2(".text_icon_style i").click(function () {
            if ($(this).hasClass("text_style")) {
                _this.select2(".text_icon_style i.text_style").removeClass("active");
                $(this).addClass("active");
                return;
            }
            if ($(this).hasClass("active")) {
                $(this).removeClass("active");
            } else {
                $(this).addClass("active");
            }
        });

        colorpicker.render({
            elem: "#" + this.widgetId + "_settings" + " .color_picker"
            , color: _this.background
            , format: 'rgb'
            , alpha: true
            , size: 'xs'
            , change: function (color) {
                console.log(color);
            }
            , done: function (color) {
                console.log(color)
                _this.pickerColor = color;
            }
        });

        let treeId = loadPageTree(`#${_this.widgetId}_settings`, `tree_${_this.widgetId}`,
            function (node) {
                _this.treeData = node;
            });
        reloadPageTreeData(treeId);

        form.on(`radio(${_this.widgetId}_page_style)`, function (data) {
            if (data.value === "0") {
                _this.select2(".blank_page_set").show();
                _this.select2(".copy_page_set").hide();
            }
            if (data.value === "1") {
                _this.select2(".blank_page_set").hide();
                _this.select2(".copy_page_set").show();
            }
        });
        form.render();

        _this.select2(".layui-icon-close").click(function () {
            layer.closeAll();
            setupDraggableWidget(_this.currentWidget(), true);
        });

        function copyWidgetsFromPage(widgetJson) {
            if (!_this.treeData || _this.treeData === "") {
                return;
            }
            let widgetId = _this.widgetId;
            let pageJsonArray = pageToJson();
            let index = layer.load(1);
            $.ajax({
                type: 'get',
                timeout: 3000,
                url: `../json/${_this.treeData.field}.json`,
                cache: false,
                success: function (res) {
                    let widgetData = res.data;
                    console.log(widgetData);
                    let indexBase = new Date().getTime() / 100;
                    for (let i = 0; i < widgetData.length; i++) {
                        widgetData[i].widgetId = "copy_" + widgetData[i].widgetId;
                        widgetData[i].index += indexBase;
                        if (widgetData[i].parent === undefined || widgetData[i].parent === "#app") {
                            widgetData[i].parent = "#" + widgetId;
                            if (widgetData[i].renderId !== undefined) {
                                widgetData[i].renderId = parseInt(widgetData[i].renderId) + renderIdMax + 1;
                            }
                        }
                        if (widgetData[i].parent === "#sidebar" || widgetData[i].parent === "#navbar") {
                            widgetData.splice(i--, 1);
                        }
                    }
                    for (let i = 0; i < pageJsonArray.length; i++) {
                        widgetData.push(pageJsonArray[i]);
                    }
                    emptyEditor();
                    loadFromData(widgetData, true);
                    layer.close(index);
                },
                error: function () {
                    layer.close(index);
                    layer.msg("需要拷贝的页面加载失败", {icon: 2, time: 2000});
                }
            })
        }

        // setting ok
        _this.select2(".set_ok").click(function (e) {
            console.log('submit');
            if (_this.modifyUiStyle()) {
                let widgetJson = _this.toJson();
                console.log(widgetJson);
                layer.closeAll();
                copyWidgetsFromPage(widgetJson);
            } else {
                layer.msg("样式参数错误", {icon: 0, time: 1000});
                return false;
            }
            setupDraggableWidget(_this.currentWidget(), true);
            reloadFromCacheData(true);
            return false;
        });

        _this.select2(".set_cancel").click(function (e) {
            console.log('cancel');
            e.preventDefault()
            layer.closeAll();
            setupDraggableWidget(_this.currentWidget(), true);
        });

        // click set button
        _this.select1(".set").click(function () {
            layer.open({
                type: 1,
                title: "内容区设置",
                id: `${_this.widgetId}_set`,
                area: ['488px'],
                offset: '100px',
                content: _this.select2(),
                resize: true,
                shadeClose: false,
                moveType: 0,
                shade: 0.5,
                anim: -1
            });
            _this.currentMenu().hide();
            setupDraggableWidget(_this.currentWidget(), false);
            _this.setLeftTop();
        })

        // click delete button
        _this.select1(".delete").click(function () {
            _this.destroy();
        })

        // right click
        widget.on('contextmenu', function (e) {
            activeWidgetBorder(widget);
            $(".w_menu").hide();
            _this.currentMenu().css("left", e.clientX);
            _this.currentMenu().css("top", e.clientY);
            _this.currentMenu().show();
            return false
        });
    },
    /**
     * 根据设置修改用户界面和组件数据
     * @returns {boolean} 成功状态
     */
    modifyUiStyle: function () {
        let _this = this;
        $('.layui-colorpicker-main').remove();
        _this.pickerColor = _this.select2('.color_picker span.layui-colorpicker-trigger-span').css("background-color");
        try {
            let left = _this.select2("input.text_set_input[name='text_left']").val();
            let top = _this.select2("input.text_set_input[name='text_top']").val();
            let width = _this.select2("input.text_set_input[name='text_width']").val();
            let height = _this.select2("input.text_set_input[name='text_height']").val();
            if (left >= 0 && left <= 1920) {
                _this.left = left;
            } else {
                return false;
            }
            if (top >= 0 && top <= 1080) {
                _this.top = top;
            } else {
                return false;
            }
            if (width >= 0 && width <= 1920) {
                _this.width = width;
            } else {
                return false;
            }
            if (height >= 0) {
                _this.height = height;
            } else {
                return false;
            }
        } catch (e) {
            return false;
        }
        if (_this.pickerColor && _this.pickerColor.length > 0) {
            _this.background = _this.pickerColor;
        }
        _this.border = _this.select2(".border_img").hasClass("border_img_active");
        _this.scroll = _this.select2("input:radio:checked[name='scroll']").val() === '1';
        if (_this.select2('.text_icon_style i.layui-icon-align-left').hasClass('active')) {
            _this.align = 'left';
        }
        if (_this.select2('.text_icon_style i.layui-icon-align-center').hasClass('active')) {
            _this.align = 'center';
        }
        return true;
    },
    setLeftTop: function () {
        try {
            let position = getDraggablePosition(this.currentWidget());
            if (position) {
                this.left = position[0];
                this.top = position[1];
            }
        } catch (e) {
            console.log("use default position.")
        }
        this.select2("input.text_set_input[name='text_left']").val(this.left)
        this.select2("input.text_set_input[name='text_top']").val(this.top)
    },
    toJson: function () {
        if (!this.widgetId) {
            return undefined;
        }
        this.setLeftTop();
        return {
            widgetId: this.widgetId,
            parent: this.parent,
            type: this.type,
            index: this.index,
            left: this.left,
            top: this.top,
            zIndex: this.zIndex,
            renderId: this.renderId,
            width: this.width,
            height: this.height,
            background: this.background,
            border: this.border,
            scroll: this.scroll,
            align: this.align,
            containers: this.containers
        }
    },
    destroy: function () {
        if (this.currentWidget().find(".content div[id^='widget']").length > 0) {
            layer.msg("请先移除容器内的其他组件", {icon: 0, time: 1000});
            return;
        }
        this.currentWidget().remove();
        this.currentMenu().remove();
        this.currentSettings().remove();
        this.indexList.splice(this.indexList.indexOf(this.index), 1)
        this.widgetId = undefined;
    },
    jsCode() {
        return `
        /** __start_widget_partition_js_code__ **/
        (function(widgetId) {
            let __this = function () {
            };
            $.extend(__this,${JSON.stringify(this.data)});
            __this.widgetId = widgetId;
            __this.select = ${this.select.toString()};
            __this.currentWidget = ${this.currentWidget.toString()};
            (${this.uiInit.toString()})(__this);
        })('${this.widgetId}');
        /** __end_widget_partition_js_code__ **/
        `;
    }

}

/*__end_WidgetPartition__*/