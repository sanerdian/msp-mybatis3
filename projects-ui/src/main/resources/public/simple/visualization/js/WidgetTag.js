/*__start_WidgetTag__*/
/**
 * 标签内容组件（容器组件内添加普通组件，需要先选中容器组件后再添加）
 */
function WidgetTag() {
    this.containers = [{id: 0, title: '默认标签'}]; // 容器组件的编号
    this.create.apply(this, arguments);
}

WidgetTag.prototype = {
    widgetId: 'widget_tag_id',
    parent: '#app', // 容器组件不可修改
    type: 3,
    id:'',
    index: -1,
    left: 500.0,
    top: 50.0,
    zIndex: -1,
    renderId: -1,
    width: 500, // 默认原始尺寸
    height: 500,
    style: 0, // 0标题在上, 1标题在下, 2标题在左
    background: 'rgba(255, 255, 255, 0.1)',
    border: true,
    scroll: false,
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
        laytpl($("#module_tag").html()).render(_this.data, function (uiContent) {
            $(_this.parent).append(uiContent);
        });
        _this.currentWidget().css({top: _this.top, left: _this.left, zIndex: _this.zIndex});
        if (_this.editable) {
            laytpl($("#module_tag_menu").html()).render(_this.data, function (menubar) {
                _this.menubar[0] = menubar;
                $("#menu").append(_this.menubar[0])
                laytpl($("#module_tag_set").html()).render(_this.data, function (settingsContent) {
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
        console.log(5,this);
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
            console.log(903,_this);
            console.log(123,_this.id);
            _this.id == '' ? _this.moduleAdd() : _this.moduleEdit()
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
        if (_this.type !== 3) {
            layer.msg("数据格式错误");
            return;
        }
        _this.indexList.push(_this.index);
        console.log(_this.indexList);
        if ($("#module_tag").length > 0) {
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
        $("#template").append("<div id='template_tag'></div>");
        $("#template_tag").load(`../module/tag.html?_=${new Date().getTime()}`, function () {
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
        let container_holder = _this.currentWidget().find('.container_tag_holder');
        if (_this.width > 0) {
            border_content.css({"width": _this.width});
            container_holder.css({"width": parseInt(_this.width) + 2});
        }
        if (_this.height > 0) {
            border_content.css({"height": _this.height});
            container_holder.css({"height": parseInt(_this.height) + 2});
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
        _this.select(".tag_btn:first").addClass("tag_btn_active");
        _this.containers.forEach(item => {
            $(`#button_tag_${_this.widgetId}_${item.id}`).click(function () {
                _this.select(".container_content").hide();
                $(`#container_tag_${_this.widgetId}_${item.id}`).show();
                _this.select(".tag_btn").removeClass("tag_btn_active");
                $(this).addClass("tag_btn_active");
            });
        });
    },
    /**
     * 初始化设置菜单参数
     */
    settingsInit: function () {
        let _this = this;
        console.log('menu & settings init');
        let widget = $(`#${_this.widgetId}`);

        _this.select2(".tag_style").eq(_this.style).addClass("tag_style_active");
        _this.select2(".tag_style").click(function () {
            $(".tag_style").removeClass("tag_style_active");
            $(this).addClass("tag_style_active");
        });

        if (_this.border) {
            _this.select2(".border_img").addClass("border_img_active");
        }
        _this.select2(".border_img").click(function () {
            $(this).toggleClass("border_img_active");
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

        let tableId = _this.widgetId + "_settings_table";
        let [...tableData] = _this.containers;

        function initSettingsTable() {
            table.render({
                id: tableId,
                elem: '#' + _this.widgetId + "_settings_table",
                width: 574,
                data: tableData,
                skin: 'nob',
                even: true,
                size: 'lg',
                cols: [[
                    {field: 'title', title: '标签页名称', width: 227, unresize: true},
                    {field: 'order', title: '排序', width: 177, unresize: true, toolbar: '#i_tab_settings_order_bar'},
                    {title: '操作', width: 161, unresize: true, toolbar: '#i_tab_settings_right_bar'},
                ]],
                parseData(res) {
                    return {
                        'status': 0,
                        'data': res
                    }
                },
                request: {
                    pageName: 'curr',
                    limitName: 'nums'
                },
                response: {
                    statusName: 'status'
                }
            });

            table.on(`tool(filter_${_this.widgetId}_settings_table)`, (obj) => {
                let line_num = parseInt($(obj.tr).attr("data-index"));
                switch (obj.event) {
                    case 'update' :
                        _this.moduleModify()
                        let elem = `#${_this.widgetId}_tag_update_page_content`;
                        let index = layer.open({
                            type: 1,
                            title: "修改标签页",
                            id: `${_this.widgetId}_update_page`,
                            area: ['556px', '193px'],
                            offset: '200px',
                            content: $(elem),
                            resize: true,
                            shadeClose: false,
                            moveType: 0,
                            shade: 0.5,
                            anim: -1
                        });
                        $(elem + " input").val(obj.data.title);
                        $(elem + ' .new_tab_set_ok').click(function (e) {
                            console.log(54,e);
                            e.preventDefault();
                            let title = $(elem + " input").val();
                            if (!title || title.length === 0) {
                                layer.msg("请输入名称", {icon: 0, time: 1000});
                                return false;
                            }
                            tableData[line_num].title = title;
                            table.reload(tableId, {data: tableData});
                            layer.close(index);
                            return false;
                        });
                        $(elem + ' .new_tab_set_cancel').click(function (e) {
                            e.preventDefault();
                            layer.close(index);
                            return false;
                        });
                        break;
                    case 'delete' :
                        _this.deleteTag()
                        if ($("#container_tag_" + _this.widgetId + "_" + obj.data.id).find("div[id^='widget']").length > 0) {
                            layer.msg("请先移除标签内的其他组件", {icon: 0, time: 1000});
                            return;
                        }
                        tableData.forEach((item, index) => {
                            if (item.id === obj.data.id) {
                                tableData.splice(index, 1);
                                return false;
                            }
                        });
                        table.reload(tableId, {data: tableData});
                        break;
                    case 'up':
                        _this.moduleUp()
                        if (line_num > 0) {
                            [tableData[line_num], tableData[line_num - 1]] = [tableData[line_num - 1], tableData[line_num]];
                        }
                        table.reload(tableId, {data: tableData});
                        break;
                    case 'down':
                        _this.moduleDown()
                        if (line_num < tableData.length - 1) {
                            [tableData[line_num], tableData[line_num + 1]] = [tableData[line_num + 1], tableData[line_num]];
                        }
                        console.log(tableData);
                        table.reload(tableId, {data: tableData});
                        break;
                }
            })
        }

        function reloadTable() {
            tableData.splice(0, tableData.length, ..._this.containers);
            table.reload(tableId, {data: tableData});
        }

        initSettingsTable();

        // 设置添加标签页
        (function initAddTabPage() {
            let layer_index;
            let elem = `#${_this.widgetId}_tag_add_page_content`;
            _this.select2(".i_tab_add_page").click(function () {
                layer_index = layer.open({
                    type: 1,
                    title: "添加标签页",
                    id: `${_this.widgetId}_add_page`,
                    area: ['556px', '193px'],
                    offset: '200px',
                    content: $(elem),
                    resize: true,
                    shadeClose: false,
                    moveType: 0,
                    shade: 0.5,
                    anim: -1
                });
                $(elem + " input").val('');
            });
            $(elem + ' .new_tab_set_ok').click(function (e) {
                console.log(902903);
                e.preventDefault();
                let title = $(elem + " input").val();
                if (!title || title.length === 0) {
                    layer.msg("请输入名称", {icon: 0, time: 1000});
                    return false;
                }
                let content = {id: ++_this.containerIndexMax, title: title};
                tableData.push(content);
                table.reload(tableId, {data: tableData});
                layer.close(layer_index);
                return false;
            });
            $(elem + ' .new_tab_set_cancel').click(function (e) {
                e.preventDefault();
                layer.close(layer_index);
                return false;
            });
        })();

        _this.select2(".layui-icon-close").click(function () {
            layer.closeAll();
            setupDraggableWidget(_this.currentWidget(), true);
        });

        // setting ok
        _this.select2(".set_ok").click(function (e) {
            console.log('submit');
            _this.containers.splice(0, _this.containers.length, ...tableData);
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
            reloadTable();
            layer.open({
                type: 1,
                title: "标签页设置",
                id: `${_this.widgetId}_set`,
                area: ['629px'],
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
        })

        // click delete button
        _this.select1(".delete").click(function () {
            _this.destroy();
            console.log(33311,_this.moduleDelete);
            _this.moduleDelete()
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
            if (height >= 0 && height <= 1080) {
                _this.height = height;
            } else {
                return false;
            }
        } catch (e) {
            return false;
        }
        _this.style = 0;
        if (_this.select2("form .tag_style").eq(1).hasClass("tag_style_active")) {
            _this.style = 1;
        }
        if (_this.pickerColor && _this.pickerColor.length > 0) {
            _this.background = _this.pickerColor;
        }
        _this.border = _this.select2(".border_img").hasClass("border_img_active");
        _this.scroll = _this.select2("input:radio:checked[name='scroll']").val() === '1';
        return true;
    },
    toJson: function () {
        if (!this.widgetId) {
            return undefined;
        }
        try {
            // this.left = this.currentWidget().position().left;
            // this.top = this.currentWidget().position().top;
            let position = getDraggablePosition(this.currentWidget());
            if (position) {
                this.left = position[0];
                this.top = position[1];
            }
        } catch (e) {
            console.log("use default position.")
        }
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
            style: this.style,
            background: this.background,
            border: this.border,
            scroll: this.scroll,
            containers: this.containers,
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
        /** __start_widget_tag_js_code__ **/
        (function(widgetId) {
            let __this = function () {
            };
            $.extend(__this,${JSON.stringify(this.data)});
            __this.widgetId = widgetId;
            __this.select = ${this.select.toString()};
            __this.currentWidget = ${this.currentWidget.toString()};
            (${this.uiInit.toString()})(__this);
        })('${this.widgetId}');
        /** __end_widget_tag_js_code__ **/
        `;
    },
    /**
     * 新建
     */
    moduleAdd:function(){
        let _this = this;
        var jsonData = {
            "entity":{
                "backgroundColor":_this.background,
                "menuId": "string",
                "moduleStyle": _this.style,
                "outLine": _this.border,
                "positionX": _this.left,
                "positionY": _this.top,
                "rollSetup": _this.scroll,
                "sizeHeight": _this.height,
                "sizeWidth": _this.width
            }
        };
        ajax('','/visual/visualcomponenttab/add','post',JSON.stringify(jsonData)).then(function (data){
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
                "backgroundColor":_this.background,
                "menuId": "string",
                "moduleStyle": _this.style,
                "outLine": _this.border,
                "positionX": _this.left,
                "positionY": _this.top,
                "rollSetup": _this.scroll,
                "sizeHeight": _this.height,
                "sizeWidth": _this.width
            }
        };
        ajax('','/visual/visualcomponenttab/'+_this.id,'PUT',JSON.stringify(jsonData)).then(function (data){
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
        ajax('','/visual/visualcomponenttab/'+_this.id,'DELETE').then(function (data){
            if(data.success){					
                layer.msg('删除成功');
            }else{
                console.log(data.msg);
            }
        })
    },
    /* 
    下移 
    */
    moduleDown:function (){
        let _this = this;
        ajax('','/visual/componenttabitem/pageDown?id='+_this.id,'POST').then(function (data){
            if(data.success){					
                layer.msg('下移成功');
            }else{
                console.log(data.msg);
            }
        })
    },
    /* 
    上移 
    */
   moduleUp:function (){
        let _this = this;
        ajax('','/visual/componenttabitem/pageUp?id='+_this.id,'POST').then(function (data){
            if(data.success){					
                layer.msg('上移成功');
            }else{
                console.log(data.msg);
            }
        })
    },
    /* 
    修改标签页 
    */
   moduleModify:function (){
        let _this = this;
        var jsonData = {
            "entity":{
                "itemName": "string",
                "itemOrder": 0,
                "tabId": 0
            }
        }
        ajax('','/visual/componenttabitem/'+_this.id,'POST',JSON.stringify(jsonData)).then(function (data){
            if(data.success){					
                layer.msg('修改成功');
            }else{
                console.log(data.msg);
            }
        })
    },
    /* 
    删除标签 
    */
    deleteTag:function (){
        let _this = this;
        ajax('','/visual/componenttabitem/'+_this.id,'DELETE').then(function (data){
            if(data.success){					
                layer.msg('删除成功');
            }else{
                console.log(data.msg);
            }
        })
    },

}

/*__end_WidgetTag__*/