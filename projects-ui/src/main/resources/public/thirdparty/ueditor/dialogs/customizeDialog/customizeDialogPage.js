/**
 * User: gyx
 * Date: 20-05-08
 * Time: 上午8:34
 * 插入线上资源对话框逻辑代码,包括tab: 图片库 视频库 音频库
 */

(function () {

    var serverVideo,
        serverAudio,
        serverImage,
        onlineFile,
        selectedItem = null,
        webclass;

    window.onload = function () {
        initTabs();
        initAlign();
        initButtons();

    };
 
    /* 初始化tab标签 */
    function initTabs () {
        var tabs = $G('tabhead').children;
        for (var i = 0; i < tabs.length; i++) {
            domUtils.on(tabs[i], "click", function (e) {
                var target = e.target || e.srcElement;
                // console.log(target.getAttribute('data-content-id'))
                setTabFocus(target.getAttribute('data-content-id'));
            });
        }

        var img = editor.selection.getRange().getClosedNode();
        if (img && img.tagName && img.tagName.toLowerCase() == 'img') {
            setTabFocus('remote');
        } else {
            setTabFocus('myImg');
        }
    }

    /* 初始化tabbody */
    function setTabFocus (id) {
        console.log(id)
        if (!id) return;
        var i, bodyId, tabs = $G('tabhead').children;
        for (i = 0; i < tabs.length; i++) {
            bodyId = tabs[i].getAttribute('data-content-id');
            // console.log($G(bodyId))
            if (bodyId == id) {
                domUtils.addClass(tabs[i], 'focus');
                domUtils.addClass($G(bodyId), 'focus');
            } else {
                domUtils.removeClasses(tabs[i], 'focus');
                domUtils.removeClasses($G(bodyId), 'focus');
            }
        }
        switch (id) {
            case 'myImg':
                // setAlign(editor.getOpt('imageManagerInsertAlign'));
                serverImage = serverImage || new ServerImage('serverimageList');
                webclass = 3
                getPerfix().then(function (res) {
                    getTreeData(webclass);
                })
                // serverImage.resetSerimg();
                break;
            case 'myAudio':
                // setAlign(editor.getOpt('imageManagerInsertAlign'));
                serverAudio = serverAudio || new ServerAudio('serveraudioList');
                webclass = 5
                getTreeData(webclass);
                // serverAudio.resetSeraudio();
                break;
            case 'myVideo':
                // setAlign(editor.getOpt('imageManagerInsertAlign'));
                serverVideo = serverVideo || new ServerVideo('servervideoList');
                webclass = 6
                getTreeData(webclass);
                // serverVideo.resetServideo();
                break;
            case 'myFile':
                onlineFile = onlineFile || new OnlineFile('fileList');
                webclass = 4
                getTreeData(webclass);
                break;
        }
    }

    /* 初始化onok事件 */
    function initButtons () {
        var me = this;
        dialog.onok = function () {
            var remote = false, list = [], list2 = [], list3 = [], id, tabs = $G('tabhead').children;
            for (var i = 0; i < tabs.length; i++) {
                if (domUtils.hasClass(tabs[i], 'focus')) {
                    id = tabs[i].getAttribute('data-content-id');
                    break;
                }
            }
            console.log(id)

            switch (id) {
                case 'myImg':
                    list = serverImage.getInsertList();
                    editor.execCommand('insertimage', list);
                    remote && editor.fireEvent("catchRemoteImage");
                    break;
                case 'myAudio':
                    list2 = serverAudio.getInsertList();
                    for (let i = 0; i < list2.length; i++) {
                        let obj = list2[i]
                        editor.execCommand('music', obj);
                    }
                    break;
                case 'myVideo':
                    list3 = serverVideo.getInsertList('servervideoList');
                    editor.execCommand('insertvideo', list3);
                    break;
                case 'myFile':
                    list4 = onlineFile.getInsertList();
                    editor.execCommand('insertfile', list4);
                    break;
            }
        };
    }

    /* 初始化对其方式的点击事件 */
    function initAlign () {
        /* 点击align图标 */
        domUtils.on($G("alignIcon"), 'click', function (e) {
            var target = e.target || e.srcElement;
            if (target.className && target.className.indexOf('-align') != -1) {
                setAlign(target.getAttribute('data-align'));
            }
        });
    }

    /* 设置对齐方式 */
    function setAlign (align) {
        align = align || 'none';
        var aligns = $G("alignIcon").children;
        for (i = 0; i < aligns.length; i++) {
            if (aligns[i].getAttribute('data-align') == align) {
                domUtils.addClass(aligns[i], 'focus');
                $G("align").value = aligns[i].getAttribute('data-align');
            } else {
                domUtils.removeClasses(aligns[i], 'focus');
            }
        }
    }
    /* 获取对齐方式 */
    function getAlign () {
        var align = $G("align").value || 'none';
        return align == 'none' ? '' : align;
    }

    function getTimes (time) {
        var timeStemp = new Date(time);
        return timeStemp.getFullYear() + "-" + (timeStemp.getMonth() + 1) + "-" + timeStemp.getDate() + "  " + timeStemp
            .getHours() + ":" + timeStemp.getMinutes()
    }

    // 机构树
    var siteId;
    var columnId;
    var companyId;
    var zTreeObj;

    function setIdParms (id1, id2, id3) {
        companyId = id1;
        siteId = id2;
        columnId = id3;
    }

    function setCompanyId (id) {
        setIdParms(id, "", "");
    }

    function setSiteId (id) {
        setIdParms("", id, "");
    }

    function setColumnId (id) {
        setIdParms("", "", id);
    }

    /*ztree*/
    function getTreeData (webclass) { //获取左侧树结构的数据
        ajax2(service_prefix.manage, "/programa/getTreeData", "post", { webclass: webclass }).then(function (data) {
            if (webclass == 3) {
                zTreeObj = $.fn.zTree.init($("#treeDemoimg"), settingss, data.obj); //初始化树
                var nodeimg = zTreeObj.getNodeByTId("treeDemoimg_4");
                zTreeObj.expandAll(true); //true 节点全部展开、false节点收缩
                zTreeObj.selectNode(nodeimg);
                settingss.callback.onClick(null, zTreeObj.setting.treeId, nodeimg);
            }
            if (webclass == 5) {
                zTreeObj = $.fn.zTree.init($("#treeDemoaudio"), settingss, data.obj); //初始化树
                var nodeaudio = zTreeObj.getNodeByTId("treeDemoaudio_4");
                zTreeObj.expandAll(true); //true 节点全部展开、false节点收缩
                zTreeObj.selectNode(nodeaudio);
                settingss.callback.onClick(null, zTreeObj.setting.treeId, nodeaudio);
            }
            if (webclass == 6) {
                zTreeObj = $.fn.zTree.init($("#treeDemovideo"), settingss, data.obj); //初始化树
                var nodevideo = zTreeObj.getNodeByTId("treeDemovideo_4");
                zTreeObj.expandAll(true); //true 节点全部展开、false节点收缩
                zTreeObj.selectNode(nodevideo);
                settingss.callback.onClick(null, zTreeObj.setting.treeId, nodevideo);
            }
            if (webclass == 4) {
                zTreeObj = $.fn.zTree.init($("#treeDemoFile"), settingss, data.obj); //初始化树
                var nodevideo = zTreeObj.getNodeByTId("treeDemoFile_4");
                zTreeObj.expandAll(true); //true 节点全部展开、false节点收缩
                zTreeObj.selectNode(nodevideo);
                settingss.callback.onClick(null, zTreeObj.setting.treeId, nodevideo);
            }
            
        })
    }
    var settingss = {
        data: {
            simpleData: {
                enable: true, //true 、 false 分别表示 使用 、 不使用 简单数据模式
                pIdKey: "parentId", //节点数据中保存其父节点唯一标识的属性名称
                rootPId: -1 //用于修正根节点父节点数据，即 pIdKey 指定的属性值
            }
        },
        view: {
            showLine: false,
            showIcon: true
        },
        callback: {
            onClick: zTreeOnClick1
        }
    };
    //点击树节点的回调函数
    function zTreeOnClick1 (even, treeId, treeNode) {
        columnName = treeNode.name;
        if (treeNode.level == 1) {
            setSiteId(treeNode.id);
        } else if (treeNode.level >= 2) {
            var parents = treeNode.getPath();
            parents.forEach(res => {
                if (res.level == 1) {
                    setIdParms("", res.id, treeNode.id);
                }
            })
        } else if (treeNode.level == 0) {
            setColumnId(treeNode.id);
        }
        // columnList(1, siteId, columnId);
        bool = true;
        var obj = {};
        if (treeNode.level === 0) {
            obj.companyId = treeNode.id;
        } else if (treeNode.level === 1) {
            obj.siteId = treeNode.id;
        } else {
            if (flag && treeNode.children.length != 0) {
                var arr = []
                for (var i in treeNode.children) {
                    arr.push(treeNode.children[i].id);
                }
                var id = treeNode.id + "," + arr.join(",");
                obj.columnId = id;
            } else {
                obj.columnId = treeNode.id;
            }
        }
        if (webclass == 3) {
            serverImage.initContainer();
            serverImage.initImageData(1, obj)
        }
        if (webclass == 5) {
            serverAudio.initContainer();
            serverAudio.initAudioData(1, obj)
        }
        if (webclass == 6) {
            serverVideo.initContainer();
            serverVideo.initVideoData(1, obj)
        }
        if (webclass == 4) {
            onlineFile.initContainer();
            onlineFile.initData(1, obj)
        }

    }


    /* 我的图片 */
    function ServerImage (target) {
        this.container = utils.isString(target) ? document.getElementById(target) : target;
        // console.log(target)
        this.initImg();
    }
    ServerImage.prototype = {
        initImg: function () {
            // this.resetSerimg();
            this.initEvents();
        },
        /* 初始化容器 */
        initContainer: function () {
            this.container.innerHTML = '';
            this.list = document.createElement('ul');
            this.clearFloat = document.createElement('li');

            domUtils.addClass(this.list, 'list');
            domUtils.addClass(this.clearFloat, 'clearFloat');

            this.list.appendChild(this.clearFloat);
            this.container.appendChild(this.list);
        },
        /* 初始化滚动事件,滚动到地步自动拉取数据 */
        initEvents: function () {
            var _this = this;

            /* 滚动拉取图片 */
            domUtils.on($G('serverimageList'), 'scroll', function (e) {
                var panel = this;
                if (panel.scrollHeight - (panel.offsetHeight + panel.scrollTop) < 10) {
                    if (!_this.listEnd) {
                        _this.listIndex = ++_this.listIndex
                        // getPerfix().then(function (res) {
                        //     _this.getServerImageData();
                        // })
                    }
                }
            });
            /* 选中图片 */
            domUtils.on(this.container, 'click', function (e) {
                var target = e.target || e.srcElement,
                    li = target.parentNode;

                if (li.tagName.toLowerCase() == 'li') {
                    if (domUtils.hasClass(li, 'imgli selected')) {
                        domUtils.removeClasses(li, 'selected');
                    } else {
                        domUtils.addClass(li, 'selected');
                    }
                }
            });
        },
        /* 初始化第一次的数据 */
        initImageData: function (curr, obj) {
            var _this = this;
            if (curr) {
                _this.listIndex = curr
            }
            if (obj) {
                _this.obj = obj
            } else {
                _this.obj = {}
            }
            _this.obj.sign = 0;
            /* 拉取数据需要使用的值 */
            _this.state = 0;
            _this.listSize = 32;
            // _this.listIndex = 1;
            _this.isNew = 1
            _this.listEnd = false;
            /* 第一次拉取数据 */
            _this.getServerImageData();
        },
        /* 重置界面 */
        resetSerimg: function () {
            this.initContainer();
            this.initImageData();
        },

        /* 向后台拉取图片列表数据 */
        getServerImageData: function () {
            var _this = this;
            // if (!_this.listEnd && !this.isLoadingData) {
            var url = '/picture';
            this.isLoadingData = true;

            var data = {
                "pager": {
                    "current": this.listIndex,
                    "size": this.listSize,
                    "sortProps": [{
                        "key": "datetime",
                        "value": false
                    }]
                },
                "picture": _this.obj
            }
            ajax(service_prefix.resources, url + "/list", 'post', JSON.stringify(data)).then(function (res) {

                layui.use('laypage', function () {
                    var laypage = layui.laypage;

                    // 执行一个laypage实例
                    laypage.render({
                        elem: 'test1',
                        count: res.obj.total,
                        curr: res.obj.current,
                        limit: res.obj.size,
                        //    'limit'
                        layout: ['count', 'prev', 'page', 'next', 'refresh', 'skip'],
                        jump: function (obj, first) {
                            if (!first) {
                                curr = obj.curr
                                this.listSize = obj.limit
                                var obj1 = {};
                                obj1.sign = 0

                                if (columnId) {
                                    obj1.columnId = columnId;
                                } else if (siteId) {
                                    obj1.companyId = companyId;
                                } else if (companyId) {
                                    obj1.siteId = siteId;
                                }
                                _this.initContainer();
                                _this.initImageData(curr, obj1)
                            }
                        }
                    });
                });
                records = res.obj.records;
                localStorage.setItem("imgInfos", JSON.stringify(records));
                var list = [];
                for (var i in records) {
                    var obj = {};
                    var imgurl = getAjaxUrl("", records[i].url)
                    // console.log(imgurl)
                    obj.url = imgurl
                    obj.name = records[i].name
                    list.push(obj);
                    records[i].datetime = getTimes(records[i].datetime)
                }
                if (res.obj.total == _this.listIndex) {
                    _this.listEnd = true
                }
                _this.pushData(list);
            })
            // }
        },
        /* 添加图片到列表界面上 */
        pushData: function (list) {
            var i, item, img, icon, text, _this = this;
            // urlPrefix = editor.getOpt('imageManagerUrlPrefix');
            for (i = 0; i < list.length; i++) {
                if (list[i] && list[i].url) {
                    item = document.createElement('li');
                    img = document.createElement('img');
                    icon = document.createElement('span');
                    text = document.createElement('p')

                    domUtils.on(img, 'load', (function (image) {
                        return function () {
                            _this.scale(image, image.parentNode.offsetWidth, image.parentNode.offsetHeight);
                        }
                    })(img));

                    img.width = 113;
                    // + (list[i].url.indexOf('?') == -1 ? '?noCache=' : '&noCache=') + (+new Date()).toString(36)
                    img.setAttribute('src', list[i].url);
                    img.setAttribute('_src', list[i].url);
                    domUtils.addClass(icon, 'icon');
                    text.innerHTML = list[i].name;
                    domUtils.addClass(text, 'text');

                    domUtils.addClass(item, 'imgli')
                    item.appendChild(img);
                    item.appendChild(icon);
                    item.appendChild(text);
                    _this.list.insertBefore(item, _this.clearFloat);
                }
            }

            if (list.length === 0) {
                item = document.createElement('li');
                text = document.createElement('p');
                domUtils.addClass(item, 'imgli');
                domUtils.addClass(text, 'text1');
                text.innerHTML = "暂无数据";
                item.appendChild(text);
                _this.list.insertBefore(item, _this.clearFloat);
            }

        },
        /* 改变图片大小 */
        scale: function (img, w, h, type) {
            var ow = img.width,
                oh = img.height;

            if (type == 'justify') {
                if (ow >= oh) {
                    img.width = w;
                    img.height = h * oh / ow;
                    img.style.marginLeft = '-' + parseInt((img.width - w) / 2) + 'px';
                } else {
                    img.width = w * ow / oh;
                    img.height = h;
                    img.style.marginTop = '-' + parseInt((img.height - h) / 2) + 'px';
                }
            } else {
                if (ow >= oh) {
                    img.width = w * ow / oh;
                    img.height = h;
                    img.style.marginLeft = '-' + parseInt((img.width - w) / 2) + 'px';
                } else {
                    img.width = w;
                    img.height = h * oh / ow;
                    img.style.marginTop = '-' + parseInt((img.height - h) / 2) + 'px';
                }
            }
        },
        getInsertList: function () {
            var i, lis = this.list.children, list = [], align = getAlign();
            for (i = 0; i < lis.length; i++) {
                if (domUtils.hasClass(lis[i], 'selected')) {
                    var img = lis[i].firstChild,
                        src = img.getAttribute('_src');
                    list.push({
                        src: src,
                        _src: src,
                        alt: src.substr(src.lastIndexOf('/') + 1),
                        floatStyle: align,
                        width: "400px",
                        height: "auto"
                    });
                }

            }
            return list;
        }
    };


    /* 我的音频 */
    function ServerAudio (target) {
        this.container = utils.isString(target) ? document.getElementById(target) : target;
        // console.log(target)
        this.initAudio();
    }
    ServerAudio.prototype = {
        initAudio: function () {
            // this.resetSeraudio();
            this.initEvents();
        },
        /* 初始化容器 */
        initContainer: function () {
            this.container.innerHTML = '';
            this.list = document.createElement('ul');
            this.clearFloat = document.createElement('li');

            domUtils.addClass(this.list, 'list');
            domUtils.addClass(this.clearFloat, 'clearFloat');

            this.list.appendChild(this.clearFloat);
            this.container.appendChild(this.list);
        },
        /* 初始化滚动事件,滚动到地步自动拉取数据 */
        initEvents: function () {
            var _this = this;
            /* 滚动拉取图片 */
            domUtils.on($G('serveraudioList'), 'scroll', function (e) {
                var panel = this;
                if (panel.scrollHeight - (panel.offsetHeight + panel.scrollTop) < 10) {
                    // getPerfix().then(function (res) {
                    //     _this.getServerAudioData();
                    // })
                }
            });
            /* 选中图片 */
            domUtils.on(this.container, 'click', function (e) {
                var target = e.target || e.srcElement,
                    li = target.parentNode;

                if (li.tagName.toLowerCase() == 'li') {
                    if (domUtils.hasClass(li, 'audioli selected')) {
                        domUtils.removeClasses(li, 'selected');
                    } else {
                        domUtils.addClass(li, 'selected');
                    }
                }
            });
        },
        /* 初始化第一次的数据 */
        initAudioData: function (curr, obj) {
            var _this = this
            if (curr) {
                _this.listIndex = curr
            }
            if (obj) {
                _this.obj = obj
            } else {
                _this.obj = {}
            }
            _this.obj.sign = 3;
            /* 拉取数据需要使用的值 */
            _this.state = 0;
            _this.listSize = 32;
            _this.isNew = 1
            _this.listEnd = false;

            /* 第一次拉取数据 */
            _this.getServerAudioData();

        },
        /* 重置界面 */
        resetSeraudio: function () {
            this.initContainer();
            this.initAudioData();
        },
        /* 向后台拉取图片列表数据 */
        getServerAudioData: function () {
            var _this = this;
            // if (!_this.listEnd && !this.isLoadingData) {
            var url = '/picture';

            var data = {
                "pager": {
                    "current": _this.listIndex,
                    "size": _this.listSize,
                    "sortProps": [{
                        "key": "datetime",
                        "value": false
                    }]
                },
                "picture": _this.obj
            }
            ajax(service_prefix.resources, url + "/list", 'post', JSON.stringify(data)).then(function (res) {
                layui.use('laypage', function () {
                    var laypage = layui.laypage;

                    // 执行一个laypage实例
                    laypage.render({
                        elem: 'test2',
                        count: res.obj.total,
                        curr: res.obj.current,
                        limit: res.obj.size,
                        //    'limit'
                        layout: ['count', 'prev', 'page', 'next', 'refresh', 'skip'],
                        jump: function (obj, first) {
                            if (!first) {
                                curr = obj.curr
                                this.listSize = obj.limit
                                var obj1 = {};
                                obj1.sign = 0

                                if (columnId) {
                                    obj1.columnId = columnId;
                                } else if (siteId) {
                                    obj1.companyId = companyId;
                                } else if (companyId) {
                                    obj1.siteId = siteId;
                                }
                                _this.initContainer();
                                _this.initAudioData(curr, obj1)
                            }
                        }
                    });
                });
                records = res.obj.records;
                localStorage.setItem("audioInfos", JSON.stringify(records));
                var list = [];
                for (var i in records) {
                    var obj = {};
                    var imgurl;
                    if (records[i].cover.length !== 0) {
                        imgurl = getAjaxUrl("", records[i].cover)
                    } else {
                        imgurl = './images/u8901.png'
                    }
                    obj.name = records[i].name
                    obj.imgurl = imgurl
                    obj.url = getAjaxUrl("", records[i].url)
                    list.push(obj);
                    records[i].datetime = getTimes(records[i].datetime)
                }

                //   layui.laytpl($("#imgtpl").html()).render(records, function (html) {
                //     $("#imgList").html(html);
                //     layui.form.render();
                //   })
                // console.log(list)
                _this.pushData(list);
            })
            // }
        },
        /* 添加音频到列表界面上 */
        pushData: function (list) {
            // console.log(list)
            var i, item, img, icon, text, _this = this;
            // urlPrefix = editor.getOpt('imageManagerUrlPrefix');
            for (i = 0; i < list.length; i++) {
                if (list[i] && list[i].url) {
                    item = document.createElement('li');
                    img = document.createElement('img');
                    icon = document.createElement('span');
                    text = document.createElement('p');

                    domUtils.on(img, 'load', (function (image) {
                        return function () {
                            _this.scale(image, image.parentNode.offsetWidth, image.parentNode.offsetHeight);
                        }
                    })(img));
                    img.width = 113;
                    // + (list[i].url.indexOf('?') == -1 ? '?noCache=' : '&noCache=') + (+new Date()).toString(36)
                    img.setAttribute('src', list[i].imgurl);
                    img.setAttribute('_src', list[i].url);
                    domUtils.addClass(icon, 'icon');
                    text.innerHTML = list[i].name;
                    domUtils.addClass(text, 'text');

                    domUtils.addClass(item, 'audioli')
                    item.appendChild(img);
                    item.appendChild(icon);
                    item.appendChild(text)
                    this.list.insertBefore(item, this.clearFloat);
                }
            }
            if (list.length === 0) {
                item = document.createElement('li');
                text = document.createElement('p');
                domUtils.addClass(item, 'audioli');
                domUtils.addClass(text, 'text1');
                text.innerHTML = "暂无数据";
                item.appendChild(text);
                this.list.insertBefore(item, this.clearFloat);
            }
        },
        /* 改变图片大小 */
        scale: function (img, w, h, type) {
            var ow = img.width,
                oh = img.height;

            if (type == 'justify') {
                if (ow >= oh) {
                    img.width = w;
                    img.height = h * oh / ow;
                    img.style.marginLeft = '-' + parseInt((img.width - w) / 2) + 'px';
                } else {
                    img.width = w * ow / oh;
                    img.height = h;
                    img.style.marginTop = '-' + parseInt((img.height - h) / 2) + 'px';
                }
            } else {
                if (ow >= oh) {
                    img.width = w * ow / oh;
                    img.height = h;
                    img.style.marginLeft = '-' + parseInt((img.width - w) / 2) + 'px';
                } else {
                    img.width = w;
                    img.height = h * oh / ow;
                    img.style.marginTop = '-' + parseInt((img.height - h) / 2) + 'px';
                }
            }
        },
        getInsertList: function () {
            var i, lis = this.list.children, list = [], align = getAlign();
            for (i = 0; i < lis.length; i++) {
                if (domUtils.hasClass(lis[i], 'selected')) {
                    var img = lis[i].firstChild,
                        src = img.getAttribute('_src');
                    list.push({
                        url: src,
                        width: 400,
                        height: 95
                    });
                }
            }
            return list;
        }
    };



    /* 我的视频 */
    function ServerVideo (target) {
        this.container = utils.isString(target) ? document.getElementById(target) : target;
        // console.log(target)
        this.initvideo();
    }
    ServerVideo.prototype = {
        initvideo: function () {
            // this.resetServideo();
            this.initEvents();
        },
        /* 初始化容器 */
        initContainer: function () {
            this.container.innerHTML = '';
            this.list = document.createElement('ul');
            this.clearFloat = document.createElement('li');

            domUtils.addClass(this.list, 'list');
            domUtils.addClass(this.clearFloat, 'clearFloat');

            this.list.appendChild(this.clearFloat);
            this.container.appendChild(this.list);
        },
        /* 初始化滚动事件,滚动到地步自动拉取数据 */
        initEvents: function () {
            var _this = this;

            /* 滚动拉取图片 */
            domUtils.on($G('servervideoList'), 'scroll', function (e) {
                var panel = this;
                if (panel.scrollHeight - (panel.offsetHeight + panel.scrollTop) < 10) {

                    // getPerfix().then(function (res) {
                    //     _this.getServerVideoData();
                    // })
                }
            });
            /* 选中图片 */
            domUtils.on(this.container, 'click', function (e) {
                var target = e.target || e.srcElement,
                    li = target.parentNode;

                if (li.tagName.toLowerCase() == 'li') {
                    if (domUtils.hasClass(li, 'videoli selected')) {
                        domUtils.removeClasses(li, 'selected');
                    } else {
                        domUtils.addClass(li, 'selected');
                    }
                }
            });
        },
        /* 初始化第一次的数据 */
        initVideoData: function (curr, obj) {
            var _this = this
            if (curr) {
                _this.listIndex = curr
            }
            if (obj) {
                _this.obj = obj
            } else {
                _this.obj = {}
            }
            _this.obj.sign = 1;
            /* 拉取数据需要使用的值 */
            _this.state = 0;
            _this.listSize = 32;
            _this.sign = 1
            _this.listEnd = false;

            /* 第一次拉取数据 */
            _this.getServerVideoData();
        },
        /* 重置界面 */
        resetServideo: function () {
            this.initContainer();
            this.initVideoData();
        },
        //选中
        doselect: function (i) {
            var me = this;
            if (typeof i == 'object') {
                selectedItem = i;
            } else if (typeof i == 'number') {
                selectedItem = me.data[i];
            }
        },
        // 移除多余内容
        _removeHtml: function (str) {
            var reg = /<\s*\/?\s*[^>]*\s*>/gi;
            return str.replace(reg, "");
        },
        _getUrl: function (isTryListen) {
            var me = this;
            var param = 'from=tiebasongwidget&url=&name=' + encodeURIComponent(me._removeHtml(selectedItem.title)) + '&artist='
                + encodeURIComponent(me._removeHtml(selectedItem.author)) + '&extra='
                + encodeURIComponent(me._removeHtml(selectedItem.album_title))
                + '&autoPlay=' + isTryListen + '' + '&loop=true';
            return me.playerUrl + "?" + param;
        },

        /* 向后台拉取图片列表数据 */
        getServerVideoData: function () {
            var _this = this;
            // if (!_this.listEnd && !this.isLoadingData) {
            var url = '/picture';
            this.isLoadingData = true;
            var data = {
                "pager": {
                    "current": this.listIndex,
                    "size": this.listSize,
                    "sortProps": [{
                        "key": "datetime",
                        "value": false
                    }]
                },
                "picture": _this.obj
            }
            ajax(service_prefix.resources, url + "/list", 'post', JSON.stringify(data)).then(function (res) {
                layui.use('laypage', function () {
                    var laypage = layui.laypage;

                    // 执行一个laypage实例
                    laypage.render({
                        elem: 'test3',
                        count: res.obj.total,
                        curr: res.obj.current,
                        limit: res.obj.size,
                        //    'limit'
                        layout: ['count', 'prev', 'page', 'next', 'refresh', 'skip'],
                        jump: function (obj, first) {
                            if (!first) {
                                curr = obj.curr
                                this.listSize = obj.limit
                                var obj1 = {};
                                obj1.sign = 0

                                if (columnId) {
                                    obj1.columnId = columnId;
                                } else if (siteId) {
                                    obj1.companyId = companyId;
                                } else if (companyId) {
                                    obj1.siteId = siteId;
                                }
                                _this.initContainer();
                                _this.initVideoData(curr, obj1)
                            }
                        }
                    });
                });
                records = res.obj.records;
                localStorage.setItem("videoInfos", JSON.stringify(records));
                var list = [];
                for (var i in records) {
                    var obj = {};
                    var imgurl = getAjaxUrl("", records[i].cover)
                    var url = getAjaxUrl("", records[i].url)
                    // console.log(imgurl)
                    obj.name = records[i].name
                    obj.cover = imgurl
                    obj.url = url
                    list.push(obj);
                    records[i].datetime = getTimes(records[i].datetime)
                }

                //   layui.laytpl($("#imgtpl").html()).render(records, function (html) {
                //     $("#imgList").html(html);
                //     layui.form.render();
                //   })
                // console.log(list)
                _this.pushData(list);
            })
            // }
        },
        /* 添加图片到列表界面上 */
        pushData: function (list) {
            var i, item, img, icon, text, preview, _this = this;
            // urlPrefix = editor.getOpt('imageManagerUrlPrefix');
            for (i = 0; i < list.length; i++) {
                if (list[i] && list[i].url) {
                    item = document.createElement('li');
                    img = document.createElement('img');
                    icon = document.createElement('span');
                    text = document.createElement('p')
                    preview = document.createElement('img')

                    domUtils.on(img, 'load', (function (image) {
                        return function () {
                            _this.scale(image, image.parentNode.offsetWidth, image.parentNode.offsetHeight);
                        }
                    })(img));
                    img.width = 113;
                    // + (list[i].url.indexOf('?') == -1 ? '?noCache=' : '&noCache=') + (+new Date()).toString(36)
                    img.setAttribute('src', list[i].cover);
                    img.setAttribute('_src', list[i].url);
                    domUtils.addClass(icon, 'icon');
                    text.innerHTML = list[i].name;
                    domUtils.addClass(text, 'text');

                    preview.setAttribute('src', './images/u7603.png');
                    domUtils.addClass(preview, 'preview');
                    preview.addEventListener('click', function () {
                        Window.open("url", "name", 其他参数);
                    }, false);
                    domUtils.addClass(item, 'videoli')
                    item.appendChild(img);
                    item.appendChild(icon);
                    item.appendChild(text);
                    item.appendChild(preview);
                    this.list.insertBefore(item, this.clearFloat);
                }
            }
            if (list.length === 0) {
                item = document.createElement('li');
                text = document.createElement('p');
                domUtils.addClass(item, 'videoli');
                domUtils.addClass(text, 'text1');
                text.innerHTML = "暂无数据";
                item.appendChild(text);
                this.list.insertBefore(item, this.clearFloat);
            }
        },
        /* 改变图片大小 */
        scale: function (img, w, h, type) {
            var ow = img.width,
                oh = img.height;

            if (type == 'justify') {
                if (ow >= oh) {
                    img.width = w;
                    img.height = h * oh / ow;
                    img.style.marginLeft = '-' + parseInt((img.width - w) / 2) + 'px';
                } else {
                    img.width = w * ow / oh;
                    img.height = h;
                    img.style.marginTop = '-' + parseInt((img.height - h) / 2) + 'px';
                }
            } else {
                if (ow >= oh) {
                    img.width = w * ow / oh;
                    img.height = h;
                    img.style.marginLeft = '-' + parseInt((img.width - w) / 2) + 'px';
                } else {
                    img.width = w;
                    img.height = h * oh / ow;
                    img.style.marginTop = '-' + parseInt((img.height - h) / 2) + 'px';
                }
            }
        },
        /**
     * 将元素id下的所有代表视频的图片插入编辑器中
     * @param id
     */
        getInsertList: function (id) {
            var i, lis = this.list.children,
                list = [];
            for (i = 0; i < lis.length; i++) {
                console.log(lis[i])
                if (domUtils.hasClass(lis[i], 'selected')) {
                    var img = lis[i].firstChild,
                        url;
                    url = img.getAttribute('_src')
                    list.push({
                        url: url,
                        width: 420,
                        height: 280,
                        align: "none"
                    });
                }
            }
            return list;
            // editor.execCommand('insertvideo', videoObjs);
        },
    };

 /* 在线附件 */
 function OnlineFile(target) {
    this.container = utils.isString(target) ? document.getElementById(target) : target;
    this.init();
}
OnlineFile.prototype = {
    init: function () {
        this.initContainer();
        this.initEvents();
        // this.initData();
    },
    /* 初始化容器 */
    initContainer: function () {
        this.container.innerHTML = '';
        this.list = document.createElement('ul');
        this.clearFloat = document.createElement('li');

        domUtils.addClass(this.list, 'list');
        domUtils.addClass(this.clearFloat, 'clearFloat');

        this.list.appendChild(this.clearFloat);
        this.container.appendChild(this.list);
    },
    /* 初始化滚动事件,滚动到地步自动拉取数据 */
    initEvents: function () {
        var _this = this;

        /* 滚动拉取图片 */
        // domUtils.on($G('fileList'), 'scroll', function(e){
        //     var panel = this;
        //     if (panel.scrollHeight - (panel.offsetHeight + panel.scrollTop) < 10) {
        //         _this.getFileData();
        //     }
        // });
        /* 选中图片 */
        domUtils.on(this.container, 'click', function (e) {
            var target = e.target || e.srcElement,
                li = target.parentNode;
            console.log(li)
            if (li.tagName.toLowerCase() == 'li') {
                if (domUtils.hasClass(li, 'fileli selected')) {
                    domUtils.removeClasses(li, 'selected');
                } else {
                    domUtils.addClass(li, 'selected');
                }
            }
        });
    },
    /* 初始化第一次的数据 */
    initData: function (curr, obj) {

        /* 拉取数据需要使用的值 */
        var _this = this
        if (curr) {
            _this.listIndex = curr
        }
        if (obj) {
            _this.obj = obj
        } else {
            _this.obj = {}
        }
        _this.obj.sign = 2;
        /* 拉取数据需要使用的值 */
        _this.state = 0;
        _this.listSize = 32;
        _this.isNew = 1
        _this.listEnd = false;
        
        /* 第一次拉取数据 */
        this.getFileData();
    },
    /* 向后台拉取图片列表数据 */
    getFileData: function () {
        var _this = this;
        // if (!_this.listEnd && !this.isLoadingData) {
        var url = '/picture';

        var data = {
            "pager": {
                "current": _this.listIndex,
                "size": _this.listSize,
                "sortProps": [{
                    "key": "datetime",
                    "value": false
                }]
            },
            "picture": _this.obj
        }
        ajax(service_prefix.resources, url + "/list", 'post', JSON.stringify(data)).then(function (res) {
            layui.use('laypage', function () {
                var laypage = layui.laypage;

                // 执行一个laypage实例
                laypage.render({
                    elem: 'test4',
                    count: res.obj.total,
                    curr: res.obj.current,
                    limit: res.obj.size,
                    //    'limit'
                    layout: ['count', 'prev', 'page', 'next', 'refresh', 'skip'],
                    jump: function (obj, first) {
                        if (!first) {
                            curr = obj.curr
                            this.listSize = obj.limit
                            var obj1 = {};
                            obj1.sign = 0

                            if (columnId) {
                                obj1.columnId = columnId;
                            } else if (siteId) {
                                obj1.companyId = companyId;
                            } else if (companyId) {
                                obj1.siteId = siteId;
                            }
                            _this.initContainer();
                            _this.initData(curr, obj1)
                        }
                    }
                });
            });
            records = res.obj.records;
            localStorage.setItem("audioInfos", JSON.stringify(records));
            var list = [];
            for (var i in records) {
                var obj = {};
                var imgurl;
                if (records[i].cover.length !== 0) {
                    imgurl = getAjaxUrl("", records[i].cover)
                } else {
                    imgurl = './images/u8901.png'
                }
                obj.name = records[i].name
                obj.imgurl = imgurl
                obj.url = getAjaxUrl("", records[i].url)
                list.push(obj);
                records[i].datetime = getTimes(records[i].datetime)
            }

            //   layui.laytpl($("#imgtpl").html()).render(records, function (html) {
            //     $("#imgList").html(html);
            //     layui.form.render();
            //   })
            // console.log(list)
            _this.pushData(list);
        })
        // }
    },
    /* 添加图片到列表界面上 */
    pushData: function (list) {
        var i, item, img, filetype, preview, icon, _this = this,
            urlPrefix = editor.getOpt('fileManagerUrlPrefix');
        for (i = 0; i < list.length; i++) {
            if(list[i] && list[i].url) {
                item = document.createElement('li');
                icon = document.createElement('span');
                filetype = list[i].url.substr(list[i].url.lastIndexOf('.') + 1);
                domUtils.addClass(item, 'fileli')

                if ( "png|jpg|jpeg|gif|bmp".indexOf(filetype) != -1 ) {
                    preview = document.createElement('img');
                    domUtils.on(preview, 'load', (function(image){
                        return function(){
                            _this.scale(image, image.parentNode.offsetWidth, image.parentNode.offsetHeight);
                        };
                    })(preview));
                    preview.width = 113;
                    preview.setAttribute('src', urlPrefix + list[i].url + (list[i].url.indexOf('?') == -1 ? '?noCache=':'&noCache=') + (+new Date()).toString(36) );
                } else {
                    var ic = document.createElement('i'),
                        textSpan = document.createElement('span');
                    textSpan.innerHTML = list[i].url.substr(list[i].url.lastIndexOf('/') + 1);
                    preview = document.createElement('div');
                    preview.appendChild(ic);
                    preview.appendChild(textSpan);
                    domUtils.addClass(preview, 'file-wrapper');
                    domUtils.addClass(textSpan, 'file-title');
                    domUtils.addClass(ic, 'file-type-' + filetype);
                    domUtils.addClass(ic, 'file-preview');
                }
                domUtils.addClass(icon, 'icon');
                item.setAttribute('data-url', list[i].url);
                if (list[i].original) {
                    item.setAttribute('data-title', list[i].original);
                }

                item.appendChild(preview);
                item.appendChild(icon);
                this.list.insertBefore(item, this.clearFloat);
            }
        }
    },
    /* 改变图片大小 */
    scale: function (img, w, h, type) {
        var ow = img.width,
            oh = img.height;

        if (type == 'justify') {
            if (ow >= oh) {
                img.width = w;
                img.height = h * oh / ow;
                img.style.marginLeft = '-' + parseInt((img.width - w) / 2) + 'px';
            } else {
                img.width = w * ow / oh;
                img.height = h;
                img.style.marginTop = '-' + parseInt((img.height - h) / 2) + 'px';
            }
        } else {
            if (ow >= oh) {
                img.width = w * ow / oh;
                img.height = h;
                img.style.marginLeft = '-' + parseInt((img.width - w) / 2) + 'px';
            } else {
                img.width = w;
                img.height = h * oh / ow;
                img.style.marginTop = '-' + parseInt((img.height - h) / 2) + 'px';
            }
        }
    },
    getInsertList: function () {
        var i, lis = this.list.children, list = [];
        for (i = 0; i < lis.length; i++) {
            if (domUtils.hasClass(lis[i], 'selected')) {
                var url = lis[i].getAttribute('data-url');
                var title = lis[i].getAttribute('data-title') || url.substr(url.lastIndexOf('/') + 1);
                list.push({
                    title: title,
                    url: url
                });
            }
        }
        return list;
    }
};

})();
