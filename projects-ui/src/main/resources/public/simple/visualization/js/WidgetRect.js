/*__start_WidgetRect__*/
/**
 * 矩形组件
 */
function WidgetRect() {
    this.create.apply(this, arguments);
}

WidgetRect.prototype = {
    widgetId: 'widget_rect_id',
    parent: "#app",
    type: 9,
    id:'',
    index: -1,
    left: 50.0,
    top: 50.0,
    zIndex: -1,
    width: 700,
    height: 43,
    borderStyle: 1, // 上右下左，位操作
    content: '矩形标题',
    borderWidth: 5,
    radius: 0,
    paddingColor: '#f9f9f9',
    borderColor: '#009688',
    fontFamily: '微软雅黑', // reserved
    isUnderline: false, // reserved
    isItalic: false, // reserved
    isBold: true, // reserved
    textAlign: 'left', // reserved
    color: '#333333', // reserved
    jumpType: -1, // 0 内部链接, 1 外部链接
    jumpAddress: "",
    jumpName: "",
    treeData: "",
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
    select3: function (child) {
        if (child) {
            return $("#" + this.widgetId + "_link " + child);
        } else {
            return $("#" + this.widgetId + "_link");
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
        this.borderStyle = data.borderStyle;
        this.content = data.content;
        this.radius = data.radius;
        this.paddingColor = data.paddingColor;
        this.borderWidth = data.borderWidth;
        this.borderColor = data.borderColor;
        this.fontFamily = data.fontFamily;
        this.isUnderline = data.isUnderline;
        this.isItalic = data.isItalic;
        this.isBold = data.isBold;
        this.textAlign = data.textAlign;
        this.color = data.color;
        this.jumpType = data.jumpType;
        this.jumpAddress = data.jumpAddress;
        this.jumpName = data.jumpName;
    },
    /**
     * 根据数据渲染组件
     */
    render: function () {
        let _this = this;
        laytpl($("#module_rect").html()).render(_this.data, function (uiContent) {
            $(_this.parent).append(uiContent);
        });
        _this.currentWidget().css({top: _this.top, left: _this.left, zIndex: _this.zIndex});
        if (_this.editable) {
            laytpl($("#module_rect_menu").html()).render(_this.data, function (menubar) {
                _this.menubar[0] = menubar;
                $("#menu").append(_this.menubar[0])
                laytpl($("#module_rect_set").html()).render(_this.data, function (settingsContent) {
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
            console.log(903,_this);
            console.log(123,_this.id);
            _this.id==''?  _this.moduleAdd() :_this.moduleEdit()
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
        if (_this.type !== 9) {
            layer.msg("数据格式错误");
            return;
        }
        _this.indexList.push(_this.index);
        console.log(_this.indexList);
        if ($("#module_rect").length > 0) {
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
        $("#template").append("<div id='template_rect'></div>");
        $("#template_rect").load(`../module/rect.html?_=${new Date().getTime()}`, function () {
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
            setupActiveWidget(_this.currentWidget(), _this.parent, '');
        }
    },
    /**
     * 初始化用户界面样式
     */
    uiStyleInit: function () {
        let _this = this;
        let content = _this.currentWidget().children('.content');
        let rect_content = _this.select('.rect_content');
        if (_this.width) {
            content.css({"width": _this.width});
            rect_content.css({"width": _this.width - 30});
        }
        if (_this.height) {
            content.css({"height": _this.height + 'px'});
            rect_content.css({"line-height": _this.height + 'px'});
        }
        if (_this.fontFamily) {
            rect_content.css({"font-family": _this.fontFamily});
        }
        rect_content.css({"font-size": "14px"});
        rect_content.css({"color": _this.color});
        rect_content.css({"text-decoration": _this.isUnderline ? "underline" : "none"});
        rect_content.css({"font-style": _this.isItalic ? "italic" : "normal"});
        rect_content.css({"font-weight": _this.isBold ? "bold" : "normal"});
        if (_this.textAlign === "left" || _this.textAlign === "center" || _this.textAlign === "right") {
            rect_content.css({"text-align": _this.textAlign});
        }
        if (_this.color) {
            rect_content.css({"color": _this.color});
        }
        if (_this.paddingColor) {
            content.css({"background-color": _this.paddingColor});
            rect_content.css({"background-color": _this.paddingColor});
        }
        rect_content.css({"border": "none"});
        let borderCss = "5px solid";
        if (_this.borderWidth && _this.borderWidth > 0) {
            borderCss = _this.borderWidth + "px solid";
        }
        // top border
        if ((_this.borderStyle & 0b1000) !== 0) {
            rect_content.css({"border-top": borderCss});
        }
        // right border
        if ((_this.borderStyle & 0b0100) !== 0) {
            rect_content.css({"border-right": borderCss});
        }
        // bottom border
        if ((_this.borderStyle & 0b0010) !== 0) {
            rect_content.css({"border-bottom": borderCss});
        }
        // left border
        if ((_this.borderStyle & 0b0001) !== 0) {
            rect_content.css({"border-left": borderCss});
        }
        if (_this.borderColor) {
            rect_content.css({"border-color": _this.borderColor});
        }
        if (_this.radius) {
            content.css({borderRadius: _this.radius + "px"});
            rect_content.css({borderRadius: _this.radius + "px"});
        }
        _this.currentWidget().css({"cursor": "pointer"});
    },
    /**
     * 初始化用户界面
     */
    uiInit: function (_this) {
        let isJump = _this.jumpAddress && _this.jumpAddress.length > 0;
        if (isJump && !_this.editable) {
            _this.currentWidget().click(function () {
                location.href = _this.jumpAddress;
            });
        }
    },
    /**
     * 初始化设置菜单参数
     */
    settingsInit: function () {
        let _this = this;
        console.log('menu & settings init');
        let widget = $(`#${_this.widgetId}`);

        _this.select2(".layui-icon-close").click(function () {
            layer.closeAll();
            setupDraggableWidget(_this.currentWidget(), true);
        });

        if ((_this.borderStyle & 0b1000) === 0) {
            _this.select2(".rect_active_top").addClass("hide_element");
        }
        if ((_this.borderStyle & 0b0100) === 0) {
            _this.select2(".rect_active_right").addClass("hide_element");
        }
        if ((_this.borderStyle & 0b0010) === 0) {
            _this.select2(".rect_active_bottom").addClass("hide_element");
        }
        if ((_this.borderStyle & 0b0001) === 0) {
            _this.select2(".rect_active_left").addClass("hide_element");
        }

        _this.select2(".rect_left").click(function () {
            _this.select2(".rect_active_left").toggleClass("hide_element");
        });
        _this.select2(".rect_right").click(function () {
            _this.select2(".rect_active_right").toggleClass("hide_element");
        });
        _this.select2(".rect_top").click(function () {
            _this.select2(".rect_active_top").toggleClass("hide_element");
        });
        _this.select2(".rect_bottom").click(function () {
            _this.select2(".rect_active_bottom").toggleClass("hide_element");
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
            elem: "#" + this.widgetId + "_settings" + " .color_picker_padding"
            , color: _this.paddingColor
            , format: 'rgb'
            , alpha: false
            , size: 'xs'
            , change: function (color) {
                console.log(color);
            }
            , done: function (color) {
                console.log(color)
                _this.paddingColor = color;
            }
        });
        colorpicker.render({
            elem: "#" + this.widgetId + "_settings" + " .color_picker_border"
            , color: _this.borderColor
            , format: 'rgb'
            , alpha: false
            , size: 'xs'
            , change: function (color) {
                console.log(color);
            }
            , done: function (color) {
                console.log(color)
                _this.borderColor = color;
            }
        });

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
        _this.select3(".set_ok").click(function (e) {
            return set_submit(e);
        });
        _this.select3(".set_cancel").click(function (e) {
            return set_cancel(e);
        });

        // click set button
        _this.select1(".set").click(function () {
            layer.open({
                type: 1,
                title: '矩形设置',
                id: `${_this.widgetId}_set`,
                area: ['542px'],
                offset: '200px',
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
        })

        let treeId = loadPageTree(`#${_this.widgetId}_link`, `tree_${_this.widgetId}`,
            function (node) {
                _this.treeData = node;
            });
        // click link button
        _this.select1(".link").click(function () {
            layer.open({
                type: 1,
                title: '设置链接地址',
                id: `${_this.widgetId}_set_link`,
                area: ['542px'],
                offset: '200px',
                content: _this.select3(),
                resize: true,
                shadeClose: false,
                moveType: 0,
                shade: 0.5,
                anim: -1,
                closeBtn: 1
            });
            _this.currentMenu().hide();
            reloadPageTreeData(treeId);
            initLinkAddressModule(_this.widgetId + '_link')
            setupDraggableWidget(_this.currentWidget(), false);
        })

        // click delete button
        _this.select1(".delete").click(function () {
            _this.destroy();
            _this.moduleDelete()
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
        $('.layui-colorpicker-main').remove();
        _this.paddingColor = _this.select2('.color_picker_padding span.layui-colorpicker-trigger-span').css("background-color");
        _this.borderColor = _this.select2('.color_picker_border span.layui-colorpicker-trigger-span').css("background-color");
        try {
            let left = _this.select2("input.text_set_input[name='text_left']").val();
            let top = _this.select2("input.text_set_input[name='text_top']").val();
            let width = _this.select2("input.text_set_input[name='text_width']").val();
            let height = _this.select2("input.text_set_input[name='text_height']").val();
            let content = _this.select2("input.text_content[name='text_content']").val();
            let radius = _this.select2("input.text_set_input[name='text_radius']").val();
            let borderWidth = _this.select2("input.text_set_input[name='text_border_width']").val();
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
            if (width > 0 && width <= 1920) {
                _this.width = width;
            } else {
                return false;
            }
            if (height > 0 && height <= 1080) {
                _this.height = height;
            } else {
                return false;
            }
            if (content.length > 0) {
                _this.content = content;
                console.log(_this.content);
            } else {
                return false;
            }
            if (radius >= 0) {
                _this.radius = radius;
            } else {
                return false;
            }
            if (borderWidth >= 0 && borderWidth <= 1920) {
                _this.borderWidth = borderWidth;
            } else {
                return false;
            }
        } catch (e) {
            return false;
        }
        _this.borderStyle = + _this.select2(".rect_active_left").is(":visible") +
            ( + _this.select2(".rect_active_bottom").is(":visible")) * 2 +
            ( + _this.select2(".rect_active_right").is(":visible")) * 4 +
            ( + _this.select2(".rect_active_top").is(":visible")) * 8 ;
        let res = true;
        // 获取链接数据
        if (_this.select3().is(":visible")) {
            let data = {treeData: _this.treeData};
            res = getFromLinkAddressModule(_this.widgetId + "_link", data);
            _this.jumpName = data.jumpName;
            _this.jumpType = data.jumpType;
            _this.jumpAddress = data.jumpAddress;
        }
        return res;
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
            borderStyle: this.borderStyle,
            content: this.content,
            radius: this.radius,
            paddingColor: this.paddingColor,
            borderWidth: this.borderWidth,
            borderColor: this.borderColor,
            fontFamily: this.fontFamily,
            isUnderline: this.isUnderline,
            isItalic: this.isItalic,
            isBold: this.isBold,
            textAlign: this.textAlign,
            color: this.color,
            jumpType: this.jumpType,
            jumpAddress: this.jumpAddress,
            jumpName: this.jumpName
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
        /** __start_widget_rect_js_code__ **/
        (function(widgetId) {
            let __this = function () {
            };
            $.extend(__this,${JSON.stringify(this.data)});
            __this.widgetId = widgetId;
            __this.select = ${this.select.toString()};
            __this.currentWidget = ${this.currentWidget.toString()};
            (${this.uiInit.toString()})(__this);
        })('${this.widgetId}');
        /** __end_widget_rect_js_code__ **/
        `;
    },
    /**
     * 新建
     */
    moduleAdd:function(){
        let _this = this;
        var jsonData = {
            "entity":{
                "lineWidth": this.borderWidth,
                "menuId": "",
                "pictureRound": this.radius,
                "positionX": this.left,
                "positionY": this.top,
                "rectangleColor": this.borderColor,
                "rectangleEdge": this.borderStyle,
                "rectangleFill": this.paddingColor,
                "sizeHeight": this.height,
                "sizeWidth": this.width
            }
        };
        ajax('','/visual/componentrectangle/add','post',JSON.stringify(jsonData)).then(function (data){
            if(data.success){			
                // _this.id = JSON.parse(data).obj.id;			
                layer.msg('添加成功');
                _this.id = data.obj.id
                console.log(9,_this.id);
            }else{
                console.log(data.msg);
            }
        })
    },
    /**
     * 修改
     */
    moduleEdit:function(){
        let _this = this;
        var jsonData = {
            "entity":{
                "lineWidth": this.borderWidth,
                "menuId": "",
                "pictureRound": this.radius,
                "positionX": this.left,
                "positionY": this.top,
                "rectangleColor": this.borderColor,
                "rectangleEdge": this.borderStyle,
                "rectangleFill": this.paddingColor,
                "sizeHeight": this.height,
                "sizeWidth": this.width
            }
        };
        ajax('','/visual/componentrectangle/'+_this.id,'PUT',JSON.stringify(jsonData)).then(function (data){
            if(data.success){					
                layer.msg('修改成功');
            }else{
                console.log(data.msg);
            }
        })
    },
    /**
     * 删除
     */
    moduleDelete:function (){
        let _this = this;
        ajax('','/visual/componentrectangle/'+_this.id,'DELETE').then(function (data){
            if(data.success){					
                layer.msg('删除成功');
            }else{
                console.log(data.msg);
            }
        })
    },
}

/*__end_WidgetRect__*/