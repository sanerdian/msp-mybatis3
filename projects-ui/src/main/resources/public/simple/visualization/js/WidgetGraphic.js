/*__start_WidgetGraphic__*/
/**
 * 图文组件
 */
function WidgetGraphic() {
    this.dbTable = '';
    this.dbField = [{title: '初级保安员', field: 'image1', graphic: '../upload/images/graphic_default.png'},
        {title: '安检证', field: 'image2', graphic: '../upload/images/graphic_default.png'},
        {title: '消防中控证', field: 'image3', graphic: '../upload/images/graphic_default.png'}];
    this.create.apply(this, arguments);
}

WidgetGraphic.prototype = {
    widgetId: 'widget_graphic_id',
    parent: "#app",
    type: 10,
    index: -1,
    left: 50.0,
    top: 50.0,
    zIndex: -1,
    width: 600, // 默认原始尺寸
    content: '../upload/images/graphic_default.png',
    numPerLine: 3,
    graphicSpace: 10,
    numPerPage: 5,
    titlePosition: 'bottom',
    indexList: [],
    indexMax: [-1],
    menubar: [],
    settingsContent: [],
    limit: 999,
    editable: false,
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
        if (data.dbTable) {
            this.dbTable = data.dbTable;
        }
        if (data.dbField) {
            this.dbField = data.dbField;
        }
        this.zIndex = data.zIndex;
        this.index = data.index;
        this.content = data.content;
        this.numPerLine = data.numPerLine;
        this.graphicSpace = data.graphicSpace;
        this.titlePosition = data.titlePosition;
        this.numPerPage = data.numPerPage;
    },
    /**
     * 根据数据渲染组件
     */
    render: function () {
        let _this = this;
        laytpl($("#module_graphic").html()).render(_this.data, function (uiContent) {
            $(_this.parent).append(uiContent);
        });
        _this.currentWidget().css({top: _this.top, left: _this.left, zIndex: _this.zIndex});
        if (_this.editable) {
            laytpl($("#module_graphic_menu").html()).render(_this.data, function (menubar) {
                _this.menubar[0] = menubar;
                $("#menu").append(_this.menubar[0])
                laytpl($("#module_graphic_set").html()).render(_this.data, function (settingsContent) {
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
            if (editable) {
                _this.editable = true;
                console.log("modify widget index=" + _this.index);
            } else {
                _this.editable = false;
                console.log("loading widget index=" + _this.index);
            }
        }
        if (_this.type !== 10) {
            layer.msg("数据格式错误");
            return;
        }
        _this.indexList.push(_this.index);
        console.log(_this.indexList);
        if ($("#module_graphic").length > 0) {
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
        $("#template").append("<div id='template_graphic'></div>");
        $("#template_graphic").load(`../module/graphic.html?_=${new Date().getTime()}`, function () {
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
            setupActiveWidget(_this.currentWidget(), _this.parent);
        }
    },
    /**
     * 初始化用户界面样式
     */
    uiStyleInit: function () {
        let _this = this;
        let content = _this.currentWidget().children('.content');
        let graphicContent = _this.select(".graphic_content");
        let graphicTitle = _this.select(".graphic_title");
        let parentWidth = $(_this.parent).width();
        if (_this.width > parentWidth) {
            _this.width = parentWidth;
            _this.left = 0;
        }
        _this.currentWidget().css({"left": _this.left});
        _this.currentWidget().css({"top": _this.top});
        _this.select(".graphic_space").css({"width": _this.graphicSpace});
        let graphicWidth = _this.width ? (_this.width + _this.graphicSpace) / _this.numPerLine - _this.graphicSpace : 0;
        let graphicWidthMax = parentWidth ? (parentWidth + _this.graphicSpace) / _this.numPerLine - _this.graphicSpace : 1920;
        if (graphicWidth > 0) {
            graphicContent.css({"width": graphicWidth});
            _this.select(".graphic_space").css({"width": _this.graphicSpace});
        }
        if (graphicWidth === 0) {
            graphicContent.css({"width": "", "max-width": graphicWidthMax});
        }

        _this.select(".graphic_content").get(0).onload = function () {
            let graphicHeight = _this.select(".graphic_content").eq(0).height();
            graphicWidth = _this.select(".graphic_content").eq(0).width();
            if ((_this.titlePosition === 'left') || (_this.titlePosition === 'right')) {
                graphicTitle.css({"width": "", "line-height": graphicHeight + 'px'});
                _this.select(".graphic_element").css({"float": "left"});
            } else if((_this.titlePosition === 'top') || (_this.titlePosition === 'bottom')) {
                graphicTitle.css({"width": graphicWidth-10});
                _this.select(".graphic_element").css({"float": ""});
            }
            _this.select(".content").css({height: _this.currentWidget().height()})
        };
    },
    /**
     * 初始化用户界面
     */
    uiInit: function (_this) {
        // TODO 关联数据表

    },
    /**
     * 初始化设置菜单参数
     */
    settingsInit: function () {
        let _this = this;
        console.log('menu & settings init')
        let widget = $(`#${_this.widgetId}`);

        let style = 0;
        if(_this.titlePosition === "top") style = 1;
        if(_this.titlePosition === "right") style = 2;
        if(_this.titlePosition === "left") style = 3;
        _this.select2(".tag_style").eq(style).addClass("tag_style_active");
        _this.select2(".tag_style").click(function () {
            $(".tag_style").removeClass("tag_style_active");
            $(this).addClass("tag_style_active");
        });

        _this.select2(`input:radio[name='num_per_page'][value=${_this.numPerPage > 0 ? 1 : 0}]`).attr("checked", "checked");
        if (_this.numPerPage === 0) {
            _this.select2(".set_per_page").hide();
        } else {
            _this.select2(".set_per_page").show();
        }
        form.on(`radio(${_this.widgetId}_num_per_page)`, function (data) {
            if (data.value === "1") {
                _this.select2(".set_per_page").show();
                _this.select2("input.text_set_input_with_border[name='num_per_page']").val(5);
            }
            if (data.value === "0") {
                _this.select2(".set_per_page").hide();
            }
        });

        _this.select2(".layui-icon-close").click(function () {
            layer.closeAll();
            setupDraggableWidget(_this.currentWidget(), true);
        });

        function set_submit(e) {
            console.log('submit')
            if (_this.modifyUiStyle()) {
                let widgetJson = _this.toJson();
                console.log(widgetJson);
                layer.closeAll();
                _this.destroy();
                _this.create(_this.parent, widgetJson, true);
            } else {
                layer.msg("样式参数错误", {icon: 0, time: 1000});
                return false;
            }
            setupDraggableWidget(_this.currentWidget(), true);
            return false;
        }

        function set_cancel(e) {
            console.log('cancel');
            e.preventDefault();
            layer.closeAll();
            setupDraggableWidget(_this.currentWidget(), true);
            return false;
        }

        // setting ok
        _this.select2(".set_ok").click(function (e) {
            return set_submit(e);
        });
        _this.select2(".set_cancel").click(function (e) {
            return set_cancel(e);
        });

        // click set button
        _this.select1(".set").click(function () {
            layer.open({
                type: 1,
                title: false,
                id: `${_this.widgetId}_set`,
                area: ['542px'],
                offset: '70px',
                content: _this.select2(),
                resize: true,
                shadeClose: false,
                moveType: 0,
                shade: 0.5,
                anim: -1,
                closeBtn: 0
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
            return false;
        });
        form.render();
    },
    /**
     * 根据设置修改用户界面和组件数据
     * @returns {boolean} 成功状态
     */
    modifyUiStyle: function () {
        let _this = this;
        try {
            let left = parseInt(_this.select2("input.text_set_input[name='text_left']").val());
            let top = parseInt(_this.select2("input.text_set_input[name='text_top']").val());
            let width = parseInt(_this.select2("input.text_set_input[name='text_width']").val());
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
        } catch (e) {
            return false;
        }
        _this.titlePosition = "bottom";
        if (_this.select2("form .tag_style").eq(1).hasClass("tag_style_active")) {
            _this.titlePosition = "top";
        }
        if (_this.select2("form .tag_style").eq(2).hasClass("tag_style_active")) {
            _this.titlePosition = "right";
        }
        if (_this.select2("form .tag_style").eq(3).hasClass("tag_style_active")) {
            _this.titlePosition = "left";
        }

        let isNumPerPageShow = _this.select2("input:radio:checked[name='num_per_page']").val() === '1';
        _this.numPerPage = 0;
        if (isNumPerPageShow) {
            _this.numPerPage = parseInt(_this.select2("input.text_set_input_with_border[name='num_per_page']").val());
        }
        if (_this.numPerPage < 0) {
            return false;
        }
        _this.numPerLine = parseInt(_this.select2("input.text_set_input[name='text_num_per_line']").val());
        _this.graphicSpace = parseInt(_this.select2("input.text_set_input[name='text_graphic_space']").val());
        _this.currentWidget().css({"left": _this.left});
        _this.currentWidget().css({"top": _this.top});
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
            width: this.width,
            content: this.content,
            numPerLine: this.numPerLine,
            titlePosition: this.titlePosition,
            graphicSpace: this.graphicSpace,
            numPerPage: this.numPerPage,
            dbTable: this.dbTable,
            dbField: this.dbField
        }
    },
    destroy: function () {
        this.currentWidget().remove();
        this.currentMenu().remove();
        this.currentSettings().remove();
        this.indexList.splice(this.indexList.indexOf(this.index), 1)
        this.widgetId = undefined;
    },
    jsCode() {
        return `
        /** __start_widget_graphic_js_code__ **/
        (function(widgetId) {
            let __this = function () {
            };
            $.extend(__this,${JSON.stringify(this.data)});
            __this.widgetId = widgetId;
            __this.select = ${this.select.toString()};
            __this.currentWidget = ${this.currentWidget.toString()};
            (${this.uiInit.toString()})(__this);
        })('${this.widgetId}');
        /** __end_widget_graphic_js_code__ **/
        `;
    }
}

/*__end_WidgetGraphic__*/