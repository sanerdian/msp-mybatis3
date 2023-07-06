/*__start_WidgetTable__*/
/**
 * 表格组件
 */
function WidgetTable() {
    this.tableTitle = [{type: 'checkbox', style: 2}, {type: 'numbers', title: '序号', width: 0},
        {type: 'normal',title: '用户ID',field: "title"},{type: 'normal',title: '姓名',field: "title"},
        {type: 'normal',title: '手机号',field: "title"},{type: 'normal',title: '用户类型',field: "title"},
        {type: 'normal',title: '注册时间',field: "title"}];
    this.margin = [5, 5, 0, 5];
    this.create.apply(this, arguments);
}

WidgetTable.prototype = {
    widgetId: 'widget_table_id',
    parent: "#app",
    type: 6,
    index: -1,
    left: 50.0,
    top: 50.0,
    zIndex: -1,
    width: 900,
    height: 430,
    style: 0,
    fontFamily: '微软雅黑',
    textAlign: 'left',
    dataSelectStyle: 2,
    numPerPage: 5,
    relatedDbTable: '',
    relatedDbField: '',
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
    currentTable: function () {
        return $("#" + "table_" + this.widgetId);
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
        this.style = data.style;
        this.fontFamily = data.fontFamily;
        this.margin = data.margin;
        this.textAlign = data.textAlign;
        this.dataSelectStyle = data.dataSelectStyle;
        this.numPerPage = data.numPerPage;
        this.relatedDbTable = data.relatedDbTable;
        this.relatedDbField = data.relatedDbField;
        if (data.tableTitle) {
            this.tableTitle = data.tableTitle;
        }
    },
    /**
     * 根据数据渲染组件
     */
    render: function () {
        let _this = this;
        laytpl($("#module_table").html()).render(_this.data, function (uiContent) {
            $(_this.parent).append(uiContent);
        });
        _this.currentWidget().css({top: _this.top, left: _this.left, zIndex: _this.zIndex});
        if (_this.editable) {
            laytpl($("#module_table_menu").html()).render(_this.data, function (menubar) {
                _this.menubar[0] = menubar;
                $("#menu").append(_this.menubar[0])
                laytpl($("#module_table_set").html()).render(_this.data, function (settingsContent) {
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
        if (_this.type !== 6) {
            layer.msg("数据格式错误");
            return;
        }
        _this.indexList.push(_this.index);
        console.log(_this.indexList);
        if ($("#module_table").length > 0) {
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
        $("#template").append("<div id='template_table'></div>");
        $("#template_table").load(`../module/table.html?_=${new Date().getTime()}`, function () {
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
            setupActiveWidget(_this.currentWidget(), _this.parent, '.layui-table-header');
        }
    },
    /**
     * 初始化用户界面样式
     */
    uiStyleInit: function () {
        let _this = this;
        let content = _this.currentWidget().children('.content');
        if (_this.width) {
            _this.currentWidget().css({"width": _this.width});
        }
        if (_this.height) {
            _this.currentWidget().css({"height": _this.height});
        }
        if (_this.fontFamily) {
            content.css({"font-family": _this.fontFamily});
        }
        if (_this.margin && _this.margin.length === 4) {
            let margin = _this.margin[0] + 'px ' + _this.margin[1] + 'px ' + _this.margin[2] + 'px ' + _this.margin[3] + 'px';
            _this.select(".table_container").css({padding: margin});
        }
        let delCount = _this.tableTitle[0].style ? 1 : 0;
        if (_this.dataSelectStyle === 0) {
            _this.tableTitle.splice(0, delCount);
        }
        if (_this.dataSelectStyle === 1) {
            _this.tableTitle.splice(0, delCount, {type: 'radio', width: 0, style: 1});
        }
        if (_this.dataSelectStyle === 2) {
            _this.tableTitle.splice(0, delCount, {type: 'checkbox', width: 0, style: 2});
        }
        if (_this.dataSelectStyle > 0 && _this.tableTitle[1].type !== 'numbers') {
            _this.tableTitle.splice(1, 0, {type: 'numbers', title: '序号', width: 0});
        }
        if (_this.dataSelectStyle === 0 && _this.tableTitle[0].type !== 'numbers') {
            _this.tableTitle.splice(0, 0, {type: 'numbers', title: '序号', width: 0});
        }
        // _this.tableTitle.push({field: 'title', title: '名称', width: 0, unresize: false});

        if (_this.textAlign === "left" || _this.textAlign === "center" || _this.textAlign === "right") {
            _this.tableTitle.forEach(item => {
                item.align = _this.textAlign;
            });
        }
    },
    /**
     * 初始化用户界面
     */
    uiInit: function (_this) {
        if (_this.style === 0) {
            table.render({
                elem: '#table_' + _this.widgetId,
                height: _this.height - _this.margin[0] - _this.margin[2] - 2 - (!!_this.numPerPage)*54,
                width: _this.width - _this.margin[1] - _this.margin[3] - 2,
                url: '../json/page.json', // TODO
                even: false,
                page: false,
                size: 'lg',
                cols: [_this.tableTitle],
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
                },
                done(res) {
                    let tableLength = res.data.length;
                    renderPage(tableLength);
                    // _this.select(".total_number").text(`共 ${res.data.length} 条`);
                    _this.select("th .layui-table-cell span").css({"user-select": "none"})
                    _this.select("td div.layui-table-cell").css({"user-select": "none"})
                    _this.select("th .layui-table-cell").prepend('<div class="fake-table-cell" style="width: calc(100% - 20px);height:100%;position: absolute;left:10px">')
                    _this.select(".fake-table-cell").on({
                        mousedown: function (e) {
                            $(document).on('mousemove.drag', function (e1) {
                                // console.log(e.pageX - e1.pageX)
                                if ((Math.abs(e.pageY - e1.pageY) > 5) || (Math.abs(e.pageX - e1.pageX) > 5)) {
                                    layer.msg("拖拽表头分割线调整宽度", {icon: 0, time: 1500});
                                    $(document).off('mousemove.drag')
                                }
                            });
                        },
                        mouseup: function () {
                            $(document).off('mousemove.drag')
                        }
                    })
                }
            });

            // TODO
            $(_this.parent).on('table_event_' + _this.parent, function (e, type, msg) {
                let selectedData = table.checkStatus('table_' + _this.widgetId).data;
                alert(type + ":  " + _this.widgetId)
                switch (type) {
                    case "insert":
                        break;
                    case "delete":
                        break;
                    case "update":
                        break;
                    case "search":
                        break;
                    case "redirect":
                        break;
                }
            });

            function renderPage(tableLength) {
                if (_this.numPerPage > 0) {
                    laypage.render({
                        elem: 'table_page_' + _this.widgetId,
                        count: tableLength,
                        limit: _this.numPerPage,
                        layout: ['count', 'prev', 'page', 'next'],
                        theme: '#009688',
                        curr: location.hash.replace('#!page=', ''),
                        hash: 'page',
                        jump(obj) {
                            console.log(obj)
                        }
                    });
                    _this.select(".table_page_br").show();
                } else {
                    _this.select(".table_page_br").hide();
                }
            }

            // TODO 关联数据表
        } else if (_this.style === 2) {
            let tableHeight = _this.height - _this.margin[0] - _this.margin[2] - 2
            let tableWidth = _this.width - _this.margin[1] - _this.margin[3] - 2
            table.render({
                elem: '#table_' + _this.widgetId,
                height: tableHeight + 50, // 增加空表头的尺寸，最后再设置为0
                width: tableWidth,
                url: '../json/page.json', // TODO
                even: false,
                page: false,
                size: 'lg',
                cols: [[{title: '', field: "content1"}, {title: '', field: "content2"},
                    {title: '', field: "content3"},{title: '', field: "content4"}]],
                parseData(res) {
                    return {
                        'status': 0,
                        'data': [{"content1": "姓名","content2": "宋江","content3": "身份证号","content4": "110100201111224786"},
                            {"content1": "年龄","content2": "31","content3": "性别","content4": "男"},
                            {"content1": "出生日期","content2": "1990-03-07","content3": "最高学历","content4": "本科"},
                            {"content1": "民族","content2": "汉族","content3": "身高","content4": "188cm"},
                            {"content1": "体重","content2": "75kg","content3": "婚姻状态","content4": "未婚"},
                            {"content1": "政治面貌","content2": "无","content3": "联系电话","content4": "18618451745"},
                            {"content1": "紧急联系人","content2": "吴用","content3": "紧急联系电话","content4": "18618451746"},
                            {"content1": "疾病史","content2": "无","content3": "特长","content4": "跑步、擒拿"}]
                    }
                },
                request: {
                    pageName: 'curr',
                    limitName: 'nums'
                },
                response: {
                    statusName: 'status'
                },
                done(res) {
                    // _this.select("th").hide();
                    _this.select("tr").css({"background-color":"white", "border": "none"});
                    _this.select(".layui-border-box").css("border", "none");
                    _this.select("th").css({"border": "none", "padding": "2px 1px"});
                    _this.select("th .layui-table-cell").css("height", "0px");
                    _this.select(".layui-table-view").css("height", `${tableHeight}px`);
                    _this.select("td:even").css("background-color", "#f2f2f2");
                    _this.select("td").css("border-left", "1px #e6e6e6 solid");
                }
            });
        }
        _this.select(".layui-table-header").addClass("disableDragZone");
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
        _this.select2(`input:radio[name='select_style'][value=${_this.dataSelectStyle}]`).attr("checked", "checked");
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
                layer.msg("参数错误或无法切换样式", {icon: 0, time: 1000});
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
                title: false,
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
            let left = _this.select2("input.text_set_input[name='text_left']").val();
            let top = _this.select2("input.text_set_input[name='text_top']").val();
            let width = _this.select2("input.text_set_input[name='text_width']").val();
            let height = _this.select2("input.text_set_input[name='text_height']").val();
            let fontFamily = _this.select2(".font_family_sel option:selected").val();
            let margin0 = _this.select2("input.text_set_input[name='text_margin0']").val();
            let margin1 = _this.select2("input.text_set_input[name='text_margin1']").val();
            let margin2 = _this.select2("input.text_set_input[name='text_margin2']").val();
            let margin3 = _this.select2("input.text_set_input[name='text_margin3']").val();
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
            if (margin0 >= 0 && margin0 <= 200) {
                _this.margin[0] = margin0;
            } else {
                return false;
            }
            if (margin0 >= 0 && margin0 <= 200) {
                _this.margin[1] = margin1;
            } else {
                return false;
            }
            if (margin0 >= 0 && margin0 <= 200) {
                _this.margin[2] = margin2;
            } else {
                return false;
            }
            if (margin0 >= 0 && margin0 <= 200) {
                _this.margin[3] = margin3;
            } else {
                return false;
            }
            _this.fontFamily = fontFamily;
        } catch (e) {
            return false;
        }
        if (_this.select2("form .tag_style").eq(0).hasClass("tag_style_active")) {
            if (_this.style === 2) {
                return false
            }
            _this.style = 0;
        }
        if (_this.select2("form .tag_style").eq(1).hasClass("tag_style_active")) {
            // _this.style = 1;
        }
        if (_this.select2("form .tag_style").eq(2).hasClass("tag_style_active")) {
            _this.style = 2;
        }
        if (_this.select2("form .tag_style").eq(3).hasClass("tag_style_active")) {
            // _this.style = 3;
        }
        if (_this.style === undefined) {
            _this.style = 0;
        }

        _this.dataSelectStyle = parseInt(_this.select2("input:radio:checked[name='select_style']").val());
        let isNumPerPageShow = _this.select2("input:radio:checked[name='num_per_page']").val() === '1';
        _this.numPerPage = 0;
        if (isNumPerPageShow) {
            _this.numPerPage = parseInt(_this.select2("input.text_set_input_with_border[name='num_per_page']").val());
        }
        if (_this.numPerPage < 0) {
            return false;
        }

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
        try {
            let position = getDraggablePosition(this.currentWidget());
            if (position) {
                this.left = position[0];
                this.top = position[1];
            }
        } catch (e) {
            console.log("use default position.");
        }
        let theTable = this.select(".layui-table")[0];
        let tableThs = $(theTable).find('th');
        let _this = this;
        if (_this.style === 0) {
            $.each(tableThs, function (index, element) {
                _this.tableTitle[index].width = $(element)[0].clientWidth;
            });
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
            style: this.style,
            fontFamily: this.fontFamily,
            margin: this.margin,
            textAlign: this.textAlign,
            dataSelectStyle: this.dataSelectStyle,
            numPerPage: this.numPerPage,
            tableTitle: this.tableTitle,
            relatedDbTable: this.relatedDbTable,
            relatedDbField: this.relatedDbField
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
        /** __start_widget_table_js_code__ **/
        (function(widgetId) {
            let __this = function () {
            };
            $.extend(__this,${JSON.stringify(this.data)});
            __this.widgetId = widgetId;
            __this.select = ${this.select.toString()};
            __this.currentWidget = ${this.currentWidget.toString()};
            (${this.uiInit.toString()})(__this);
        })('${this.widgetId}');
        /** __end_widget_table_js_code__ **/
        `;
    }

}

/*__end_WidgetTable__*/