/*__start_WidgetNavbar__*/
/**
 * 导航栏组件
 */
function WidgetNavbar() {
    this.create.apply(this, arguments);
}

WidgetNavbar.prototype = {
    widgetId: 'widget_navbar_id',
    parent: "#navbar",
    navId:'',
    type: 5,
    index: -1,
    withShrink: true,
    withRefresh: true,
    withLocation: true,
    withUserInfo: true,
    withPasswordUpdate: true,
    withFullscreen: true,
    withLogout: true,
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
    currentWidget: function () {
        return $("#" + this.widgetId);
    },
    currentMenu: function () {
        return $(`#${this.widgetId}_menu`);
    },
    currentSettings: function () {
        return $(`#container_${this.widgetId}_settings`);
    },
    assignData: function (name) {
        let _this = this;
        eval(`if (_this.data.${name} !== 'undefined') {
            _this.${name} = _this.data.${name}
        } else {
            _this.data.${name} = _this.${name}
        }`);
    },
    /**
     * 初始化组件数据
     * @param data 组件数据
     */
    dataInit: function (data) {
        this.data = data;
        if (data.widgetId) {
            // 加载已有组件
            this.widgetId = data.widgetId;
            this.index = data.index;
        } else {
            // 根据通用数据创建
            this.index = ++this.indexMax[0];
            this.data.index = this.index;
            this.widgetId = this.widgetId + "_" + this.index;
            this.data.widgetId = this.widgetId;
        }
        this.assignData('withShrink');
        this.assignData('withRefresh');
        this.assignData('withLocation');
        this.assignData('withUserInfo');
        this.assignData('withPasswordUpdate');
        this.assignData('withFullscreen');
        this.assignData('withLogout');
    },
    /**
     * 根据数据渲染组件
     */
    render: function () {
        let _this = this;
        laytpl($("#module_navbar").html()).render(_this.data, function (uiContent) {
            $(_this.parent).append(uiContent);
            _this.html = uiContent;
        });
        if (_this.editable) {
            laytpl($("#module_navbar_menu").html()).render(_this.data, function (menubar) {
                _this.menubar[0] = menubar;
                $("#menu").append(_this.menubar[0])
                laytpl($("#module_navbar_set").html()).render(_this.data, function (settingsContent) {
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
        if (!data || !data.widgetId) {
            _this.index = ++_this.indexMax[0];
            _this.widgetId = _this.widgetId + "_" + _this.index;
            console.log("create new widget index=" + _this.index);
            _this.editable = true;
            _this.data = this.toJson();
        } else {
            _this.dataInit(data);
            console.log(78456,_this);
            console.log(82934,_this.navId);
            _this.navId == '' ? _this.moduleAdd() : _this.moduleEdit()
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
        if (_this.type !== 5) {
            layer.msg("数据格式错误");
            return;
        }
        _this.indexList.push(_this.index);
        console.log(_this.indexList);
        if ($("#module_navbar").length > 0) {
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
        $("#template").append("<div id='template_navbar'></div>");
        $("#template_navbar").load(`../module/navbar.html?_=${new Date().getTime()}`, function () {
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
        let app = $("#app");
        app.css({minHeight: 1080 - 50 - 2});
        _this.select("i.layui-icon-shrink-right").click(function () {
            if ($(this).hasClass("layui-icon-shrink-right")) {
                $(this).removeClass("layui-icon-shrink-right").addClass("layui-icon-spread-left");
                $("#sidebar").trigger("sidebar", ['shrink', true]);
            } else {
                $(this).removeClass("layui-icon-spread-left").addClass("layui-icon-shrink-right");
                $("#sidebar").trigger("sidebar", ['shrink', false]);
            }
        });
        _this.select(".nav_current_location").text(currentPageTitle);
        _this.select("a i.layui-icon-screen-full").click(function () {
            fullscreen();
        });
    },
    /**
     * 初始化设置菜单参数
     */
    settingsInit: function () {
        let _this = this;
        console.log('menu & settings init');
        let widget = $(`#${_this.widgetId}`);
        _this.select2(`input:radio[name='shrink'][value=${_this.withShrink ? '1' : '0'}]`).attr("checked", "checked");
        _this.select2(`input:radio[name='refresh'][value=${_this.withRefresh ? '1' : '0'}]`).attr("checked", "checked");
        _this.select2(`input:radio[name='location'][value=${_this.withLocation ? '1' : '0'}]`).attr("checked", "checked");
        _this.select2(`input:radio[name='userinfo'][value=${_this.withUserInfo ? '1' : '0'}]`).attr("checked", "checked");
        _this.select2(`input:radio[name='password_update'][value=${_this.withPasswordUpdate ? '1' : '0'}]`).attr("checked", "checked");
        _this.select2(`input:radio[name='fullscreen'][value=${_this.withFullscreen ? '1' : '0'}]`).attr("checked", "checked");
        _this.select2(`input:radio[name='logout'][value=${_this.withLogout ? '1' : '0'}]`).attr("checked", "checked");
        form.render();

        // setting ok
        function set_submit(e) {
            console.log('submit')
            if (_this.modifyUiStyle()) {
                let widgetJson = _this.toJson();
                console.log(widgetJson);
                layer.closeAll();
                _this.destroy();
                _this.create('#navbar', widgetJson, true);
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
                title: '导航栏设置',
                id: `${_this.widgetId}_set`,
                area: ['317px'],
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
        _this.withShrink = _this.select2("input:radio:checked[name='shrink']").val() === '1';
        _this.withRefresh = _this.select2("input:radio:checked[name='refresh']").val() === '1';
        _this.withLocation = _this.select2("input:radio:checked[name='location']").val() === '1';
        _this.withUserInfo = _this.select2("input:radio:checked[name='userinfo']").val() === '1';
        _this.withPasswordUpdate = _this.select2("input:radio:checked[name='password_update']").val() === '1';
        _this.withFullscreen = _this.select2("input:radio:checked[name='fullscreen']").val() === '1';
        _this.withLogout = _this.select2("input:radio:checked[name='logout']").val() === '1';
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
            withShrink: this.withShrink,
            withRefresh: this.withRefresh,
            withLocation: this.withLocation,
            withUserInfo: this.withUserInfo,
            withPasswordUpdate: this.withPasswordUpdate,
            withFullscreen: this.withFullscreen,
            withLogout: this.withLogout
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
        /** __start_widget_navbar_js_code__ **/
        (function(widgetId) {
            let __this = function () {
            };
            $.extend(__this,${JSON.stringify(this.data)});
            __this.widgetId = widgetId;
            __this.select = ${this.select.toString()};
            __this.currentWidget = ${this.currentWidget.toString()};
            let uiInit = ${this.uiInit.toString()};
            let initCommonData = ${initCommonData.toString()};
            let currentPageTitle = '${currentPageTitle}';
            initCommonData(uiInit, __this);
        })('${this.widgetId}');
        /** __end_widget_navbar_js_code__ **/
        `;
    },
    /**
     * 新建
     */
    moduleAdd:function(){
        let _this = this;
        var jsonData = {
            "entity":{
                "changePassword": this.withPasswordUpdate,
                "currentLocation": this.withLocation,
                "fullScreenview": this.withFullscreen,
                "logOut": this.withFullscreen,
                "pageRefresh": this.withRefresh,
                "sideExpansion": this.withShrink,
                "userInformation": this.withRefresh
            }
        };
        ajax('','/visual/componentnavigation/add','post',JSON.stringify(jsonData)).then(function (data){
            if(data.success){			
                // _this.id = JSON.parse(data).obj.id;			
                layer.msg('添加成功');
                _this.navId = data.obj.id
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
                "changePassword": this.withPasswordUpdate,
                "currentLocation": this.withLocation,
                "fullScreenview": this.withFullscreen,
                "logOut": this.withFullscreen,
                "pageRefresh": this.withRefresh,
                "sideExpansion": this.withShrink,
                "userInformation": this.withRefresh
            }
        };
        ajax('','/visual/componentnavigation/'+_this.navId,'PUT',JSON.stringify(jsonData)).then(function (data){
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
        ajax('','/visual/componentnavigation/'+_this.navId,'DELETE').then(function (data){
            if(data.success){					
                layer.msg('删除成功');
            }else{
                console.log(data.msg);
            }
        })
    },
}

/*__end_WidgetNavbar__*/