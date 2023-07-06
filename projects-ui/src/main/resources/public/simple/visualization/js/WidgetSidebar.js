/*__start_WidgetSidebar__*/
/**
 * 侧边栏组件
 */
function WidgetSidebar() {
    this.create.apply(this, arguments);
}

WidgetSidebar.prototype = {
    widgetId: 'widget_sidebar_id',
    parent: "#sidebar",
    type: 4,
    id:'',
    index: -1,
    title: '默认标题',
    fontFamily: '微软雅黑',
    fontSize: 16,
    lineHeight: 40,
    isUnderline: false,
    isItalic: false,
    isBold: false,
    textAlign: 'left',
    color: '#bcbdbf',
    indexList: [],
    indexMax: [-1],
    menubar: [],
    settingsContent: [],
    limit: 1,
    editable: false,
    html: '',
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
    select4: function (child) {
        if (child) {
            return $("#" + this.widgetId + "_manager " + child);
        } else {
            return $("#" + this.widgetId + "_manager");
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
        if (data.widgetId && data.type && data.index) {
            // 加载已有组件
            this.widgetId = data.widgetId;
            this.type = data.type;
            this.index = data.index;
        } else {
            // 根据通用数据创建
            this.index = ++this.indexMax[0];
            this.data.index = this.index;
            this.widgetId = this.widgetId + "_" + this.index;
            this.data.widgetId = this.widgetId;
        }
        if (data.title) {
            this.title = data.title;
        }
        if (data.fontFamily) {
            this.fontFamily = data.fontFamily;
        }
        if (data.fontSize) {
            this.fontSize = data.fontSize;
        }
        if (data.lineHeight) {
            this.lineHeight = data.lineHeight;
        }
        if (data.isUnderline) {
            this.isUnderline = data.isUnderline;
        }
        if (data.isItalic) {
            this.isItalic = data.isItalic;
        }
        if (data.isBold) {
            this.isBold = data.isBold;
        }
        if (data.textAlign) {
            this.textAlign = data.textAlign;
        }
        if (data.color) {
            this.color = data.color;
        }
    },
    /**
     * 根据数据渲染组件
     */
    render: function () {
        let _this = this;
        laytpl($("#module_sidebar").html()).render(_this.data, function (uiContent) {
            $(_this.parent).append(uiContent);
            _this.html = uiContent;
        });
        if (_this.editable) {
            laytpl($("#module_sidebar_menu").html()).render(_this.data, function (menubar) {
                _this.menubar[0] = menubar;
                $("#menu").append(_this.menubar[0])
                laytpl($("#module_sidebar_set").html()).render(_this.data, function (settingsContent) {
                    _this.settingsContent[0] = settingsContent;
                    $("#settings").append(`<div id='container_${_this.widgetId}_settings'></div>`);
                    _this.currentSettings().append(_this.settingsContent[0])
                    form.render();
                    element.render();
                    _this.init();
                });
            });
        } else {
            form.render();
            element.render();
            _this.init();
        }
    },
    /**
     * 创建组件
     * @param parent 父组件elem, 忽略
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
        if (!data) {
            _this.index = ++_this.indexMax[0];
            _this.widgetId = _this.widgetId + "_" + _this.index;
            console.log("create new widget index=" + _this.index);
            _this.editable = true;
            _this.data = this.toJson();
        } else {
            _this.dataInit(data);
            _this.id == '' ?_this.moduleAdd() : _this.moduleEdit()
            if (_this.index > _this.indexMax[0]) {
                _this.indexMax[0] = _this.index;
            }
            if (editable) {
                _this.editable = true;
                console.log("modify widget index=" + _this.index);
            } else {
                _this.editable = false;
                console.log("loading widget index=" + _this.index);
            }
        }
        if (_this.type !== 4) {
            layer.msg("数据格式错误");
            return;
        }
        _this.indexList.push(_this.index);
        console.log(_this.indexList);
        if ($("#module_sidebar").length > 0) {
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
        $("#template").append("<div id='template_sidebar'></div>");
        $("#template_sidebar").load(`../module/sidebar.html?_=${new Date().getTime()}`, function () {
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
    getChildMenu: function(data) {
        let _this = this;
        let $dd = $("<dd></dd>");
        let href = 'javascript:;'
        let hasHref = data.href && data.href.length > 0;
        let hasChildren = data.children && data.children.length > 0;
        if (hasHref && !hasChildren && !_this.editable) {
            href = data.href;
        }
        let icon = "layui_icon_padding";
        if (data.icon && data.icon.length > 0) {
            icon = data.icon;
        }
        let $a = $(`<a href=${href} class="nav_text"><i class="layui-icon ${icon}"></i><span>${data.name}</span></a>`);
        $dd.append($a);
        if (hasChildren) {
            let $dl = $("<dl class='layui-nav-child'></dl>");
            data.children.forEach(item => {
                $dl.append(_this.getChildMenu(item));
            });
            $dd.append($dl);
        }
        return $dd;
    },
    initMenu: function(_this) {
        let root = _this.select("ul.layui-nav");
        commonData.pages.forEach((item) => {
            console.log(item)
            if (!item || !item.name) {
                return;
            }
            let $li = $("<li class='layui-nav-item'></li>");
            let href = 'javascript:;'
            let hasHref = item.href && item.href.length > 0;
            let hasChildren = item.children && item.children.length > 0;
            if (hasHref && !hasChildren && !_this.editable) {
                href = item.href;
            }
            let icon = "layui-icon-list";
            if (item.icon && item.icon.length > 0) {
                icon = item.icon;
            }
            let $a = $(`<a href=${href} class="nav_text"><i class="layui-icon ${icon}"></i><span>${item.name}</span></a>`);
            $li.append($a);
            if (hasChildren) {
                let $dl = $("<dl class='layui-nav-child'></dl>");
                item.children.forEach(item => {
                    console.log(987,item);
                    $dl.append(_this.getChildMenu(item));
                });
                $li.append($dl);
            }
            root.append($li);
        });
        element.render();
    },
    /**
     * 初始化用户界面参数
     */
    uiInit: function (_this) {
        _this.initMenu(_this);
        let title = _this.select(".content .sidebar_title a");
        let content = _this.select(".content .nav_text");
        if (_this.color) {
            title.css("cssText", `color:${_this.color}!important;`);
            content.css("cssText", `color:${_this.color}!important;`);
        }
        if (_this.fontFamily) {
            content.css({"font-family": _this.fontFamily});
        }
        if (_this.fontSize) {
            content.css({"font-size": _this.fontSize + "px"});
            content.find('i').css({"font-size": _this.fontSize + "px"});
            title.css({"font-size": parseInt(_this.fontSize) + 2 + "px"});
        }
        content.css({"text-decoration": _this.isUnderline ? "underline" : "none"});
        content.css({"font-style": _this.isItalic ? "italic" : "normal"});
        content.css({"font-weight": _this.isBold ? "bold" : "normal"});
        if (_this.textAlign === "left" || _this.textAlign === "center" || _this.textAlign === "right") {
            content.css({"text-align": _this.textAlign});
        }
        if (_this.textAlign === "right") {
            content.addClass("sidebar_text_padding_right");
        }
        if (_this.lineHeight) {
            content.css({lineHeight: _this.lineHeight + "px", height: _this.lineHeight + "px"});
        }

        function barShrink() {
            _this.select(".layui-nav-itemed").removeClass("layui-nav-itemed");
            _this.select(".layui-icon-down").hide();
            _this.select(".layui-nav-item a span").hide();
            _this.currentWidget().css({width: 55});
        }

        function barSpread() {
            _this.select(".layui-icon-down").show();
            _this.select(".layui-nav-item a span").show();
            _this.currentWidget().css({width: 220});
        }

        _this.select(".layui-nav-item").click(function () {
            barSpread();
            $("#navbar i.layui-icon-spread-left").removeClass("layui-icon-spread-left").addClass("layui-icon-shrink-right");
        });

        $("#sidebar").on("sidebar", function (e, type, msg) {
            if (type === 'shrink' && msg === true) {
                barShrink();
            }
            if (type === 'shrink' && msg === false) {
                barSpread();
            }

        });
        _this.select(".layui-nav-bar").remove();
        element.render();
    },
    /**
     * 初始化设置菜单参数
     */
    settingsInit: function () {
        let _this = this;
        console.log('menu & settings init');
        let widget = $(`#${_this.widgetId}`);

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
            , color: _this.color
            , format: 'rgb'
            , alpha: false
            , size: 'xs'
            , change: function (color) {
                console.log(color);
            }
            , done: function (color) {
                console.log(color)
                _this.color = color;
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
                _this.create('#sidebar', widgetJson, true);
            } else {
                layer.msg("样式参数错误", {icon: 0, time: 1000});
                return false;
            }
            return false;
        }

        function set_cancel(e) {
            console.log('cancel');
            e.preventDefault();
            layer.closeAll();
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
                title: '标题设置',
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
        })

        // click manager button
        _this.select1(".manager").click(function () {
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
            _this.currentMenu().hide();
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
        _this.color = _this.select2('.color_picker span.layui-colorpicker-trigger-span').css("background-color");
        try {
            let content = _this.select2("input.text_content[name='text_content']").val();
            let fontFamily = _this.select2(".font_family_sel option:selected").val();
            let fontSize = _this.select2("input.text_set_input[name='text_font_size']").val();
            let lineHeight = _this.select2("input.text_set_input[name='text_line_height']").val();
            if (content.length > 0) {
                _this.title = content;
                console.log(_this.title);
            } else {
                return false;
            }
            _this.fontFamily = fontFamily;
            if (fontSize > 0 && fontSize <= 100) {
                _this.fontSize = fontSize;
            } else {
                return false;
            }
            if (lineHeight > 0 && lineHeight <= 100) {
                _this.lineHeight = lineHeight;
            }
        } catch (e) {
            return false;
        }
        if (!_this.color) {
            return false;
        }
        _this.isUnderline = _this.select2('.text_icon_style i.layui-icon-fonts-u').hasClass('active');
        _this.isItalic = _this.select2('.text_icon_style i.layui-icon-fonts-i').hasClass('active');
        _this.isBold = _this.select2('.text_icon_style i.layui-icon-fonts-strong').hasClass('active');
        if (_this.select2('.text_icon_style i.layui-icon-align-left').hasClass('active')) {
            _this.textAlign = 'left';
        }
        if (_this.select2('.text_icon_style i.layui-icon-align-center').hasClass('active')) {
            _this.textAlign = 'center';
        }
        if (_this.select2('.text_icon_style i.layui-icon-align-right').hasClass('active')) {
            _this.textAlign = 'right';
        }
        return true;
    },
    toJson: function () {
        if (!this.widgetId) {
            return undefined;
        }
        return {
            widgetId: this.widgetId,
            parent: this.parent,
            type: this.type,
            index: this.index,
            title: this.title,
            fontFamily: this.fontFamily,
            fontSize: this.fontSize,
            lineHeight: this.lineHeight,
            isUnderline: this.isUnderline,
            isItalic: this.isItalic,
            isBold: this.isBold,
            textAlign: this.textAlign,
            color: this.color
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
        /** __start_widget_sidebar_js_code__ **/
        (function(widgetId) {
            let __this = function () {
            };
            $.extend(__this,${JSON.stringify(this.data)});
            __this.widgetId = widgetId;
            __this.select = ${this.select.toString()};
            __this.currentWidget = ${this.currentWidget.toString()};
            __this.initMenu = ${this.initMenu.toString()};
            __this.getChildMenu = ${this.getChildMenu.toString()};
            let uiInit = ${this.uiInit.toString()};
            let initCommonData = ${initCommonData.toString()};
            let currentPageTitle = '${currentPageTitle}';
            initCommonData(uiInit, __this);
        })('${this.widgetId}');
        /** __end_widget_sidebar_js_code__ **/
        `;
    },
    /**
     * 新建
     */
    moduleAdd:function(){
        let _this = this;
        var jsonData = {
            "entity":{
                "fontSize": 0,
                "labelFont": "string",
                "labelName": "string",
                "labelTypesetting": "string",
                "lineSpace": 0
            }
        };
        ajax('','/visual/componentlabel/add','post',JSON.stringify(jsonData)).then(function (data){
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
                "fontSize": 0,
                "labelFont": "string",
                "labelName": "string",
                "labelTypesetting": "string",
                "lineSpace": 0
            }
        };
        ajax('','/visual/componentlabel/'+_this.id,'PUT',JSON.stringify(jsonData)).then(function (data){
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
        ajax('','/visual/componentlabel/'+_this.id,'DELETE').then(function (data){
            if(data.success){					
                layer.msg('删除成功');
            }else{
                console.log(data.msg);
            }
        })
    },  
}

/*__end_WidgetSidebar__*/