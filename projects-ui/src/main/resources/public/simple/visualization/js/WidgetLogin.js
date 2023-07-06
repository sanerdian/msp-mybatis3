/*__start_WidgetLogin__*/
/**
 * 登录框组件
 */
 function WidgetLogin() {
    this.create.apply(this, arguments);
    console.log(this)
    console.log(arguments)
}

WidgetLogin.prototype = {
    widgetId: 'widget_login_id',
    parent: "#app",
    type: 0,
    index: -1,
    left: 50.0,
    top: 50.0,
    zIndex: -1,
    allowUserLogin: true,//账号密码
    allowPhoneLogin: true,//手机号登录
    allowQrcodeLogin: true,//扫码登录
    allowWechatLogin: true,//微信登录
    allowQQLogin: true,//QQ登录
    verifyType: 0, // 0 图片验证码 , 1 图片滑动 , 2 文字点击
    jumpType: 0, // 0 内部链接, 1 外部链接
    jumpAddress: "",
    jumpName: "",
    indexList: [],
    indexMax: [-1],
    menubar: [],
    settingsContent: [],
    limit: 2,
    editable: false,
    treeData: "",
    id: '',
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
     * 根据数据渲染组件
     */
    render: function () {
        let _this = this;
        laytpl($("#login").html()).render(_this.data, function (uiContent) {
            $(_this.parent).append(uiContent);
        });
        _this.currentWidget().css({top: _this.top, left: _this.left, zIndex: _this.zIndex});
        if (_this.editable) {
            laytpl($("#login-menu").html()).render(_this.data, function (menubar) {
                _this.menubar[0] = menubar;
                $("#menu").append(_this.menubar[0])
                laytpl($("#login-set").html()).render(_this.data, function (settingsContent) {
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
        this.left = data.left;
        this.top = data.top;
        this.zIndex = data.zIndex;
        this.index = data.index;
        this.allowUserLogin = data.allowUserLogin;
        this.allowPhoneLogin = data.allowPhoneLogin;
        this.allowQrcodeLogin = data.allowQrcodeLogin;
        this.allowWechatLogin = data.allowWechatLogin;
        this.allowQQLogin = data.allowQQLogin;
        this.verifyType = data.verifyType;
        this.jumpType = data.jumpType;
        this.jumpAddress = data.jumpAddress;
        this.jumpName = data.jumpName;
    },
    /**
     * 创建组件
     * @param parent 父组件elem
     * @param data 加载组件参数，为空时新建
     * @param editable 是否可编辑，为空时不可编辑
     */
    create: function (parent, data, editable) {
        console.log(parent, data, editable)
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
            _this.id == '' ? _this.moduleAdd() : _this.moduleEdit();//ID为空是新建，否则是修改
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
        if (_this.type !== 0) {
            layer.msg("数据格式错误");
            return;
        }
        _this.indexList.push(_this.index);
        console.log(_this.indexList);
        if ($("#login").length > 0) {
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
        $("#template").append("<div id='template_login'></div>");
        $("#template_login").load(`../module/login.html?_=${new Date().getTime()}`, function () {
            callback();
        });
    },
    /**
     * 初始化组件
     */
    init: function () {
        const _this = this;
        _this.uiInit(_this);
        _this.formLoginInit();
        if (_this.editable) {
            _this.settingsInit();
            setupActiveWidget(_this.currentWidget(), _this.parent);
        }
    },
    /**
     * 初始化用户界面参数
     */
    uiInit: function (_this) {
        let changeImg = function () {
            _this.select(".vercode_img").attr("src", getAjaxUrl("", "/member/home/imgCode") + "?t=" + new Date().getTime())
        };
        let changeQrcode = function () {
            // TODO changeQrcode
        };
        if (_this.select(".vercode_img").is(":visible")) {
            changeImg();
        }
        _this.select(".vercode_img").click(function () {
            changeImg()
        });
        _this.select(".qr_tab1").click(function () {
            _this.select(".login_user").hide();
            _this.select(".login_qr").show();
            changeQrcode();
        });

        _this.select(".qr_tab2").click(function () {
            _this.select(".login_user").show();
            _this.select(".login_qr").hide();
            changeImg();
        });
        _this.select(".login_qr .qr_code").click(function () {
            changeQrcode();
        });

        let vercodeBtn = _this.select(".vercode_btn");
        let vercodeBtnClicked = false;
        vercodeBtn.click(function (e) {
            if (vercodeBtnClicked) {
                return false;
            }
            vercodeBtnClicked = true;
            e.preventDefault();
            vercodeBtn.addClass("layui-btn-disabled")
            vercodeBtn.removeClass("layui-bg-green")
            let time = 60;
            let interval;

            function fnInterval() {
                vercodeBtn.text(`${time--}秒后再试`);
                if (time < 0) {
                    clearInterval(interval);
                    vercodeBtn.removeClass("layui-btn-disabled")
                    vercodeBtn.addClass("layui-bg-green")
                    vercodeBtn.text("获取验证码");
                    vercodeBtnClicked = false;
                }
                return fnInterval
            }

            interval = setInterval(fnInterval(), 1000);
            // TODO send phone msg
        });

        _this.select(".wechat_login").click(function () {
            // TODO wechat_login
        });
        _this.select(".qq_login").click(function () {
            // TODO qq_login
        });

        _this.select(".forget_password").click(function () {
            // TODO forget_password
        });
        form.render();
        _this.select(".layui-input").addClass("disableDragZone");
    },
    /**
     * 初始化用户表单参数
     */
    formLoginInit: function () {
        form.verify({
            username: [
                /^[a-zA-Z][a-zA-Z0-9_]*$/
                , '用户名格式不正确'
            ],
            password: [
                /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[^]{8,16}$/
                , '密码格式不正确'
            ],
            phone: [
                /^1[3456789]\d{9}$/
                , '手机号码格式不正确'
            ],
        })

        form.on("submit(username_login)", function (data) {
            let param = data.field;
            console.log(param.username)
            // TODO username_login
        })

        form.on("submit(phone_login)", function (data) {
            let param = data.field;
            console.log(param.phone)
            console.log(param.vercode2)
            // TODO phone_login
        })
    },
    /**
     * 初始化设置菜单参数
     */
    settingsInit: function () {
        let _this = this;
        console.log('menu & settings init')
        let widget = $(`#${_this.widgetId}`);

        form.on(`radio(${_this.widgetId}_link)`, function (data) {
            console.log('link')
            if (data.value === "inside") {
                _this.select2(".link_page").show();
                _this.select2(".link_address").hide();
            }
            if (data.value === "outside") {
                _this.select2(".link_page").hide();
                _this.select2(".link_address").show();
            }
        });

        // setting ok
        _this.select2(".set_ok").click(function (e) {
            console.log('submit')
            if (_this.modifyUiStyle()) {
                let widgetJson = _this.toJson();
                console.log(widgetJson);
                layer.closeAll()
                reloadFromCacheData(true);
                setupDraggableWidget(_this.currentWidget(), true);
            }
            return false;
        });

        _this.select2(".set_cancel").click(function (e) {
            console.log('cancel');
            e.preventDefault();
            layer.closeAll();
            setupDraggableWidget(_this.currentWidget(), true);
            return false;
        });

        // click set button
        let treeId = loadPageTree(`#${_this.widgetId}_settings`, `tree_${_this.widgetId}`,
            function (node) {
                _this.treeData = node;
            });
        _this.select1(".set").click(function () {
            layer.open({
                type: 1,
                title: ['登录框设置'],
                id: `${_this.widgetId}_set`,
                area: ['556px', '375px'],
                content: _this.select2(),
                resize: true,
                shadeClose: false,
                moveType: 0,
                shade: 0.5,
                anim: -1,
                cancel: function () {
                    setupDraggableWidget(_this.currentWidget(), true);
                }
            });
            _this.currentMenu().hide();
            reloadPageTreeData(treeId);
            setupDraggableWidget(_this.currentWidget(), false);
        })

        // click delete button
        _this.select1(".delete").click(function () {
            _this.destroy();
            _this.moduleDelete();
        })

        // right click
        widget.on('contextmenu', function (e) {
            activeWidgetBorder(widget);
            $(".w_menu").hide();
            _this.currentMenu().css("left", e.clientX)
            _this.currentMenu().css("top", e.clientY)
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
        let checkedItem = [];
        _this.select2("input[type=checkbox]:checked").each(function () {
            checkedItem.push($(this).val());
        });
        console.log('checkedItem',checkedItem)
        if (checkedItem.indexOf("username") < 0 && checkedItem.indexOf("phone") < 0) {
            layer.msg("账号或手机登录至少选择一个")
            return false;
        }
        _this.allowUserLogin = checkedItem.indexOf("username") >= 0;
        _this.allowPhoneLogin = checkedItem.indexOf("phone") >= 0;
        _this.allowQrcodeLogin = checkedItem.indexOf("qrcode") >= 0;
        _this.allowWechatLogin = checkedItem.indexOf("wechat") >= 0;
        _this.allowQQLogin = checkedItem.indexOf("qq") >= 0;

        // vercode select
        let vercodeOption = _this.select2(".vercode_sel option:selected").val();
        _this.verifyType = parseInt(vercodeOption);
        if (vercodeOption === 1) {
            // TODO slip_graph_item verify
        }
        if (vercodeOption === 2) {
            // TODO click_text_item verify
        }
        _this.jumpType = 0;
        console.log(_this.select2(".link_page"))
        console.log(_this.select2(".link_page").is(":hidden"))
        if (_this.select2(".link_page").is(":hidden")) {
            _this.jumpType = 1;
        }
        try {
            if (_this.jumpType === 0) {
                if (_this.treeData && _this.treeData.title.length > 0) {
                    _this.jumpAddress = _this.treeData.href;
                    _this.jumpName = _this.treeData.title;
                } else if (!_this.jumpAddress.length) {
                    layer.msg("请选择链接页面");
                    return false;
                }
            }
        } catch (e) {
            console.log(e)
        }
        if (_this.jumpType === 1) {
            let address = _this.select2(".address_text").val();
            if (address.length > 0) {
                _this.jumpAddress = address;
                _this.jumpName = "";
            } else {
                layer.msg("请输入链接地址");
                return false;
            }
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
            allowUserLogin: this.allowUserLogin,
            allowPhoneLogin: this.allowPhoneLogin,
            allowQrcodeLogin: this.allowQrcodeLogin,
            allowWechatLogin: this.allowWechatLogin,
            allowQQLogin: this.allowQQLogin,
            verifyType: this.verifyType,
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
        /** __start_widget_login_js_code__ **/
        (function(widgetId) {
            let __this = function () {
            };
            $.extend(__this,${JSON.stringify(this.data)});
            __this.widgetId = widgetId;
            __this.select = ${this.select.toString()};
            __this.currentWidget = ${this.currentWidget.toString()};
            (${this.uiInit.toString()})(__this);
        })('${this.widgetId}');
        /** __end_widget_login_js_code__ **/
        `;
    },
    /**
     * 新建
     */
    moduleAdd:function(){
        console.log('add');
        let _this = this;
        let loginType = '';
        if(_this.allowUserLogin){
            loginType = '1534446498203455489';
        }else if(_this.allowPhoneLogin){            
            loginType = '1534446558928588802';
        }else if(_this.allowQrcodeLogin){            
            loginType = '1534446630907039746';
        }
        let loginThird = '';
        if(_this.allowWechatLogin){
            loginThird = '1534446749610037250';
        }else if(_this.allowQQLogin){            
            loginThird = '1534446809278205953';
        }
        var jsonData = {
            "entity":{
                "linkMenuId": "",
                "linkUrl": _this.jumpAddress,
                // "loginThird": [_this.allowWechatLogin, _this.allowQQLogin],
                "loginThird": loginThird,
                "loginTo": _this.jumpType,
                // "loginType": [_this.allowUserLogin, _this.allowPhoneLogin, _this.allowQrcodeLogin],
                "loginType": loginType,
                "verifyType": _this.verifyType
            }
        };    
        ajax('','/visual/componentlogin','post',JSON.stringify(jsonData)).then(function (data){
            if(data.success){			
                _this.id = JSON.parse(data).obj.id;			
                layer.msg('添加成功');
            }else{
                console.log(data.msg);
            }
        }) 
    },
    /**
     * 修改
     */
     moduleEdit:function(){
        console.log('update');
        let _this = this;
        let loginType = '';
        if(_this.allowUserLogin){
            loginType = '1534446498203455489';
        }else if(_this.allowPhoneLogin){            
            loginType = '1534446558928588802';
        }else if(_this.allowQrcodeLogin){            
            loginType = '1534446630907039746';
        }
        let loginThird = '';
        if(_this.allowWechatLogin){
            loginThird = '1534446749610037250';
        }else if(_this.allowQQLogin){            
            loginThird = '1534446809278205953';
        }
        var jsonData = {
            "entity":{
                "linkMenuId": "",
                "linkUrl": _this.jumpAddress,
                // "loginThird": [_this.allowWechatLogin, _this.allowQQLogin],
                "loginThird": loginThird,
                "loginTo": _this.jumpType,
                // "loginType": [_this.allowUserLogin, _this.allowPhoneLogin, _this.allowQrcodeLogin],
                "loginType": loginType,
                "verifyType": _this.verifyType
            }
        };
        ajax('','/visual/componentlogin','put' + _this.id,JSON.stringify(jsonData)).then(function (data){
            if(data.success){	
                _this.id = JSON.parse(data).obj.id;			
                layer.msg('修改成功');
            }else{
                console.log(data.msg);
            }
        })
    },
    /**
     * 删除
     */
     moduleDelete:function(){
        ajax('','/visual/componentlogin','delete' + _this.id).then(function (data){
            if(data.success){			
                layer.msg('删除成功');
            }else{
                console.log(data.msg);
            }
        })
    }
}
/*__end_WidgetLogin__*/