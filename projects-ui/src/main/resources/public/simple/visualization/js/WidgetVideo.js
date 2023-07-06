/*__start_WidgetVideo__*/
/**
 * 视频组件
 */
function WidgetVideo() {
    this.create.apply(this, arguments);
}

WidgetVideo.prototype = {
    widgetId: 'widget_video_id',
    parent: "#app",
    id:'',
    type: 11,
    index: -1,
    left: 50.0,
    top: 50.0,
    zIndex: -1,
    width: 300, // 默认原始尺寸
    height: 300,
    videoTitle: '',
    videoType: 1, // 0 本地， 1 在线
    videoCover: '',
    playStyle: 'normal', // normal, popup
    videoUri: '../upload/images/logo1.png',
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
        this.videoTitle = data.videoTitle;
        this.videoType = data.videoType;
        this.videoCover = data.videoCover;
        this.playStyle = data.playStyle;
        this.videoUri = data.videoUri;
        this.relatedDbTable = data.relatedDbTable;
        this.relatedDbField = data.relatedDbField;
    },
    /**
     * 根据数据渲染组件
     */
    render: function () {
        let _this = this;
        laytpl($("#module_video").html()).render(_this.data, function (uiContent) {
            $(_this.parent).append(uiContent);
        });
        _this.currentWidget().css({top: _this.top, left: _this.left, zIndex: _this.zIndex});
        if (_this.editable) {
            laytpl($("#module_video_menu").html()).render(_this.data, function (menubar) {
                _this.menubar[0] = menubar;
                $("#menu").append(_this.menubar[0])
                laytpl($("#module_video_set").html()).render(_this.data, function (settingsContent) {
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
            _this.id == '' ? _this.moduleAdd() : _this.moduleEdit()
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
        if (_this.type !== 11) {
            layer.msg("数据格式错误");
            return;
        }
        _this.indexList.push(_this.index);
        console.log(_this.indexList);
        if ($("#module_video").length > 0) {
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
        $("#template").append("<div id='template_video'></div>");
        $("#template_video").load(`../module/video.html?_=${new Date().getTime()}`, function () {
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
        if (_this.width > 0) {
            content.css({"width": _this.width});
        }
        if (_this.height > 0) {
            content.css({"height": _this.height});
        }
        if (_this.width === 0) {
            content.css({"width": ""});
        }
        if (_this.height === 0) {
            content.css({"height": ""});
        }
        let parentWidth = $(_this.parent).width();
        let parentHeight = $(_this.parent).height();
        if (content.width() > parentWidth) {
            content.css({"width": parentWidth});
            _this.currentWidget().css({"left": 0});
        }
        if (content.height() > parentHeight) {
            content.css({"height": parentHeight});
            _this.currentWidget().css({"top": 0});
        }
    },
    /**
     * 初始化用户界面
     */
    uiInit: function (_this) {
        let video = document.getElementById(_this.widgetId + "_video");
        if (_this.playStyle === "normal") {
            _this.select(".video_play_icon").click(function () {
                video.play();
                _this.select(".video_cover").hide();
                _this.select(".video_play_icon").hide();
            });
        } else if (_this.playStyle === "popup") {
            _this.select(".video_play_icon").click(function () {
                layer.open({
                    type: 1,
                    title: _this.videoTitle,
                    id: `${_this.widgetId}_video_container`,
                    area: [''],
                    offset: '200px',
                    content: _this.select(".content"),
                    resize: true,
                    shadeClose: false,
                    moveType: 0,
                    shade: 0.5,
                    anim: -1,
                    closeBtn: 1,
                    cancel: function () {
                        video.pause();
                        _this.select(".video_cover").show();
                        _this.select(".video_play_icon").show();
                        _this.currentWidget().css({zIndex: _this.zIndex});
                    }
                });
                let shade = $(".layui-layer-shade");
                let shadeContent = shade.prop("outerHTML");
                shade.remove();
                _this.currentWidget().append(shadeContent);
                _this.currentWidget().css({zIndex: 99999});
                video.play();
                _this.select(".video_cover").hide();
                _this.select(".video_play_icon").hide();
            });
        }
        video.addEventListener('ended', function () {
            _this.select(".video_cover").show();
            _this.select(".video_play_icon").show();
        }, false);

        // TODO 关联数据表

    },
    /**
     * 初始化设置菜单参数
     */
    settingsInit: function () {
        let _this = this;
        console.log('menu & settings init')
        let widget = $(`#${_this.widgetId}`);
        _this.select2(`input:radio[name='play_style'][value=${_this.playStyle}]`).attr("checked", "checked");
        _this.select2(`input:radio[name='video_type'][value=${_this.videoType}]`).attr("checked", "checked");
        form.on(`radio(${_this.widgetId}_play_style)`, function (data) {
            _this.playStyle = data.value;
        });
        form.on(`radio(${_this.widgetId}_video_type)`, function (data) {
            _this.videoType = data.value;
        });

        _this.select2(".layui-icon-close").click(function () {
            layer.closeAll();
            setupDraggableWidget(_this.currentWidget(), true);
        });

        // image_picker
        upload.render({
            elem: "#" + _this.widgetId + "_settings" + ' .cover_upload'
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
                    _this.select2('.cover_upload').css({background: `url(${res.src})`})
                    // TODO 添加返回的图片地址，添加到res.bgImage
                }
                let item = this.item;
                layer.closeAll('loading');
            }
            , error: function (index, upload) {
                layer.closeAll('loading');
            }
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
            _this.setLeftTop();
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
            if (left >= 0 && left <= 1920) {
                _this.left = left;
            } else {
                return false;
            }
            if (top >= 0) {
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
        _this.videoTitle = _this.select2("input.text_content[name='video_title']").val();
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
            height: this.height,
            videoTitle: this.videoTitle,
            videoType: this.videoType,
            videoCover: this.videoCover,
            playStyle: this.playStyle,
            videoUri: this.videoUri,
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
        /** __start_widget_video_js_code__ **/
        (function(widgetId) {
            let __this = function () {
            };
            $.extend(__this,${JSON.stringify(this.data)});
            __this.widgetId = widgetId;
            __this.select = ${this.select.toString()};
            __this.currentWidget = ${this.currentWidget.toString()};
            (${this.uiInit.toString()})(__this);
        })('${this.widgetId}');
        /** __end_widget_video_js_code__ **/
        `;
    },
    /**
     * 新建
     */
    moduleAdd:function(){
        let _this = this;
        var jsonData = {
            "entity":{
                "buttonType": 0,
                "menuId": "string",
                "playbackMode": 0,
                "positionX": this.left,
                "positionY": this.top,
                "sizeHeight": this.height,
                "sizeWidth": this.width,
                "titlePicture": "string",
                "videoTitle": this.videoTitle
            }
        };
        ajax('','/visual/componentvideo/add','post',JSON.stringify(jsonData)).then(function (data){
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
                "buttonType": 0,
                "menuId": "string",
                "playbackMode": 0,
                "positionX": this.left,
                "positionY": this.top,
                "sizeHeight": this.height,
                "sizeWidth": this.width,
                "titlePicture": "string",
                "videoTitle": this.videoTitle
            }
        };
        ajax('','/visual/componentvideo/'+_this.id,'PUT',JSON.stringify(jsonData)).then(function (data){
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
        ajax('','/visual/componentvideo/'+_this.id,'DELETE').then(function (data){
            if(data.success){					
                layer.msg('删除成功');
            }else{
                console.log(data.msg);
            }
        })
    },
}

/*__end_WidgetVideo__*/