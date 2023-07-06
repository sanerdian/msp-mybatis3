/*__start_WidgetSearch__*/
/**
 * 搜索组件
 */
 function WidgetSkySearch() {
    console.log('WidgetSkySearch 新形势调用');
    this.create.apply(this, arguments);
}

WidgetSkySearch.prototype = {
    widgetId: 'widget_search_id',
    parent: "#app",
    type: 8,
    index: -1,
    left: 50.0,
    top: 50.0,
    zIndex: -1,
    width: 0,
    height: 0,
    fieldLogic: 'and',
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
        if (data.height) {
            this.height = data.height;
        }
        if (data.fieldLogic) {
            this.fieldLogic = data.fieldLogic;
        }
        if (data.dbTable) {
            this.dbTable = data.dbTable;
        }
        if (data.dbField) {
            this.dbField = data.dbField;
        }
        this.zIndex = data.zIndex;
        this.index = data.index;
    },
    /**
     * 根据数据渲染组件
     */
    render: function () {
        let _this = this;
        laytpl($("#module_skySearch").html()).render(_this.data, function (uiContent) {
            $(_this.parent).append(uiContent);
        });
        _this.currentWidget().css({top: _this.top, left: _this.left, zIndex: _this.zIndex});
        if (_this.editable) {
            laytpl($("#module_skySearch_menu").html()).render(_this.data, function (menubar) {
                _this.menubar[0] = menubar;
                $("#menu").append(_this.menubar[0])
                laytpl($("#module_skySearch_set").html()).render(_this.data, function (settingsContent) {
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
        if (_this.type !== 8) {
            layer.msg("数据格式错误");
            return;
        }
        _this.indexList.push(_this.index);
        console.log(_this.indexList);
        if ($("#module_skySearch").length > 0) {
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
        $("#template").append("<div id='template_search'></div>");
        $("#template_search").load(`../skySearch/skySearch.html?_=${new Date().getTime()}`, function () {
            callback();
        });
    },
    /**
     * 初始化组件
     */
    init: function () {
        const _this = this;
        _this.uiInit(_this);
        if (_this.editable) {
            _this.settingsInit();
            setupActiveWidget(_this.currentWidget(), _this.parent);
        }
    },
    /**
     * 初始化用户界面参数
     */
    uiInit: function (_this) {
        let content = _this.currentWidget().children('.content');

        // TODO
        let parent = $(_this.parent);
    },
    /**
     * 初始化设置菜单参数
     */
    settingsInit: function () {
        let _this = this;
        console.log('menu & settings init');
        let widget = $(`#${_this.widgetId}`);

        function changeStyle() {
            // TODO 根据数据重新渲染
            console.log(_this.fieldLogic)
            _this.select2(`input:radio[name='field_logic'][value=${_this.fieldLogic}]`).attr("checked", "checked");
            form.render();
        }

        // setting ok
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
                title: '搜索设置',
                id: `${_this.widgetId}_set`,
                area: ['542px'],
                offset: '200px',
                content: _this.select2(),
                resize: true,
                shadeClose: false,
                moveType: 0,
                shade: 0.5,
                anim: -1,
                closeBtn: 1
            });
            _this.currentMenu().hide();
            setupDraggableWidget(_this.currentWidget(), false);
            changeStyle();
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
    },
    /**
     * 根据设置修改用户界面和组件数据
     * @returns {boolean} 成功状态
     */
    modifyUiStyle: function () {
        let _this = this;
        // TODO
        _this.fieldLogic = _this.select2("input:radio:checked[name='field_logic']").val();
        return true;
    },
    toJson: function () {
        if (!this.widgetId) {
            return undefined;
        }
        try {
            let position = getDraggablePosition(this.currentWidget());
            if (position) {
                this.left = position[0];
                this.top = position[1];
            }
        } catch (e) {
            console.log("use default position.");
        }
        return {
            widgetId: this.widgetId,
            parent: this.parent,
            type: this.type,
            index: this.index,
            left: this.left,
            top: this.top,
            zIndex: this.zIndex,
            width: this.width,
            height: this.height,
            fieldLogic: this.fieldLogic,
            dbTable: this.dbTable,
            dbField: this.dbField
        }
    },
    destroy: function () {
        this.currentWidget().remove();
        this.currentMenu().remove();
        this.currentSettings().remove();
        this.indexList.splice(this.indexList.indexOf(this.index), 1);
        this.widgetId = undefined;
    },
    jsCode() {
        return `
        /** __start_widget_search_js_code__ **/
        (function(widgetId) {
            let __this = function () {
            };
            $.extend(__this,${JSON.stringify(this.data)});
            __this.widgetId = widgetId;
            __this.select = ${this.select.toString()};
            __this.currentWidget = ${this.currentWidget.toString()};
            (${this.uiInit.toString()})(__this);
        })('${this.widgetId}');
        /** __end_widget_search_js_code__ **/
        `;
    }

}

/*__end_WidgetSearch__*/