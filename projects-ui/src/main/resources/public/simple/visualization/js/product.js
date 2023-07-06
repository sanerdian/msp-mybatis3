$(function () {
  //初始化事件
  //一级导航切换
  var myTemplate = new Template();
  myTemplate.init();

  //终端切换
  $('.prohLeft .layui-nav-item').on('click', function () {
    $(this).siblings().addClass('layui-this').removeClass('layui-this');
    $(this).removeClass('layui-this').addClass('layui-this');
    var params = {
      netType: $('.productHead .layui-nav .layui-this a').text(),
      businessType: $('.businessType .active').attr('data'),
      mjType: $('.mediaType .active').attr('data'),
      templateType: $('.templateType .active').attr('data')
    };
    if (params) {
      myTemplate.getDataByFilter(params, {
        pageBox: 'productViewPage',
        modeBox: 'productList',
        domBox: 'productView'
      });
    }
  });

  //新建模板类型
  $('#templateTypeAddBtn').click(function () {
    templateTypeLayer();
  });

  //新建模板弹框
  $('.buildMould').on('click', buildMouldfunctionsecond);
});
//添加模板类型弹出框
function templateTypeLayer() {
  layer.open({
    type: 1,
    title: '模版类型',
    btnAlign: 'c',
    btn: ['确定', '取消'],
    area: ['570px', '180px'],
    content: '<div id="templateTypeLayerView"></div>',
    success: function (layero, index) {
      var data = [];
      getData(data, '#templateTypeLayerTemplate', '#templateTypeLayerView');
      form.on('submit(templateTypeFormSubmit)', function (data) {
        addTemplateType(data.field.title, 0);
        layer.close(layer.index);
        return false;
      });
    },
    yes: function (index, layero) {
      form.on('submit(templateTypeFormSubmit)', function (data) {
        addTemplateType(data.field.title, 0);
        layer.close(layer.index);
        return false;
      });
      layer.close(index);
      layero.find('form').find('button[lay-submit]').click();
    },
    btn2: function (index, layero) {
      layer.close(index);
    },
    cancel: function (index, layero) {
      layer.close(index);
      return false;
    }
  });
}
//新建模板弹出框
function buildMouldfunctionsecond() {
  getLabels(function (data) {
    $('.visualMouldtype').empty();
    for (var k = 0; k < data.obj.length; k++) {
      $('.visualMouldtype').append(
        ' <option value="' +
        data.obj[k].id +
        '">' +
        data.obj[k].title +
        '</option>'
      );
    }
    form.render();
  });
  layer.open({
    title: '新建模板',
    content: $('#buildMould').html(),
    btn: '',
    area: ['570px']
  });
  let index = layer.index;
  layui.use('form', function () {
    var form = layui.form;
    form.render();
    //点击取消后关闭
    $('.reverse-btn').on('click', function () {
      layer.closeAll();
    });
    form.on('submit(templateBtnAdd)', function (data) {
      // console.log(data.field)
      ajax(
        service_prefix.visual,
        '/template',
        'post',
        JSON.stringify(data.field)
      ).then(function (data) {
        if (data.success) {
          go2Menu('/simple/visualization/product/list.html', '产品管理');
          var url =
            'simple/visualization/product/productEdit.html#id=' + data.obj.id;
          window.open(url, '_blank');
        }
      });
      layer.close(index);
      return false;
    });
  });
}

//新建模板类型
function addTemplateType(title, parentId) {
  var params = {
    id: parentId,
    title: title
  };
  ajax(
    service_prefix.visual,
    '/templatetype',
    'post',
    JSON.stringify(params)
  ).then(function (data) {
    if (data && data.success) {
      showWinTips('添加成功');
      go2Menu('/simple/visualization/product/list.html', '产品管理');
    } else {
      var err = '请求失败';
      if (data && data.msg) {
        err = '请求失败：' + data.msg;
      }
      showWinTips(err);
    }
  });
}

// 新增搜索模板功能
var serviceId = service_prefix.visual;
var url = '/template/list';
function productList(serviceId, url, curr, size, param) {
  var params = {
    entity: param,
    pager: {
      current: curr,
      size: size,
      sortProps: {
        key: 'modifyTime',
        value: false
      }
    }
  };
  ajax(serviceId, url, 'post', JSON.stringify(params)).then(function (data) {
    if (data.success) {
      getData(data.obj.records, '#productList', '#productView');
      page(data.obj.total, curr, size, productList, 'productViewPage', param);
    } else {
      console.log(data.msg);
    }
  });
}

function searchProduct() {
  form.on('submit(searchProduct)', function (data) {
    var param = {
      title: data.field.title
    };
    productList(serviceId, url, 1, 10, param);
    $('.productFilter .btnRowRight .layui-btn').addClass('layui-btn-primary');
    return false;
  });
}


searchProduct();



//模板
function Template() {
  var _this = this;
  this.pageSize = 10;
  this.init = function () {
    getLabels(function (data) {
      $('.templateType').empty();
      $('.templateType').append(
        '<button type="button" class="layui-btn active" data="' +
        data.obj[0].id +
        '" datatype="templateType">' +
        data.obj[0].title +
        '</button>'
      );
      for (var k = 1; k < data.obj.length; k++) {
        $('.templateType').append(
          '<button type="button" class="layui-btn layui-btn-primary" data="' +
          data.obj[k].id +
          '" datatype="templateType">' +
          data.obj[k].title +
          '</button>'
        );
      }

      var params = _this.divChangeAndParam(this);
      if (params) {
        _this.getDataByFilter(params, {
          pageBox: 'productViewPage',
          modeBox: 'productList',
          domBox: 'productView'
        });
      }
      $('.allModel.productFilter .btnRowRight .layui-btn').click(function () {
        var params = _this.divChangeAndParam(this);
        if (params) {
          _this.getDataByFilter(params, {
            pageBox: 'productViewPage',
            modeBox: 'productList',
            domBox: 'productView'
          });
        }
      });
    });
    //初始化加载模板所有内容
    // this.getDataByFilter(this.divChangeAndParam($('.allModel.productFilter .businessBox .btnRowRight .active')), {pageBox: 'productViewPage', modeBox: 'productList', domBox: 'productView'});
  };

  //按钮点击时样式修改，返回获取筛选条件
  this.divChangeAndParam = function (_button, type) {
    $(_button).siblings().addClass('layui-btn-primary').removeClass('active');
    $(_button).removeClass('layui-btn-primary').addClass('active');

    var params = {
      netType: $('.productHead .layui-nav .layui-this a').text(),
      businessType: $('.businessType .active').attr('data'),
      mjType: $('.mediaType .active').attr('data'),
      templateType: $('.templateType .active').attr('data')
    };
    return params;
  };
  //获取模板数据
  this.getTemplateData = function (current,size, param, fn) {
    if (!current || isNaN(current)) {
      current = 1;
    }
    
    var params = {
      entity: param,
      pager: {
        current: current,
        size: size,
        sortProps: {
          key: 'modifyTime',
          value: false
        }
      }
    };
    this.param = param;
    // console.log('param',this.param)
    ajax(
      service_prefix.visual,
      '/template/list',
      'post',
      JSON.stringify(params)
    ).then(function (data) {
      if (data && data.success) {
        if (fn) {
          fn(data.obj);
        }
      } else {
        var err = '请求失败';
        if (data && data.msg) {
          err = '请求失败：' + data.msg;
        }
        showWinTips(err);
      }
    });
  };

  //筛选
  this.getDataByFilter = function (param, ids) {
    var size = this.pageSize;
    this.getTemplateData(1,size, param, function (datainit) {
      //初始化加载
      // console.log(datainit.records);
      lodTpl(ids.modeBox, ids.domBox, datainit.records);
      setPages(
        ids.pageBox,
        datainit.total,
        _this.pageSize,
        1,
        function (cur,lim) {
          //初始化分页
          _this.getTemplateData(cur,lim, param, function (datas) {
            lodTpl(ids.modeBox, ids.domBox, datas.records);
          });
        }
      );
      // console.log(ids.pageBox,datainit.total,_this.pageSize,1,);
    });
  };
}

//获取模板类型数据
function getLabels(fn) {
  ajax(service_prefix.visual, '/templatetype/typeList', 'post').then(function (
    data
  ) {
    if (data && data.success) {
      fn(data);
    } else {
      var err = '请求失败';
      if (data && data.msg) {
        err = '请求失败：' + data.msg;
      }
      showWinTips(err);
    }
  });
}

//格式化数据，让他正常的加载到页面上
function formatRecordsData(datas) {
  datas.forEach((obj) => {
    obj.img = '<img src="' + obj.img + '"/>';
    obj.operate = '<a style="color: red;">应用</a>';
  });
  return datas;
}

//设置列表数据
function setListDataView(url, id, entity) {
  var jsonData = {
    pager: {
      current: 1,
      size: 10
    },
    entity: entity
  };
  var tit = [
    { field: 'id', title: 'ID' },
    { field: 'title', title: '模板名称' },
    { field: 'crUser', title: '创建人' },
    { field: 'createBy', title: '创建时间' },
    { field: 'img', title: '缩略图' },
    { field: 'operate', title: '操作' }
  ];
  ajax(
    service_prefix.visual,
    url + '/list',
    'post',
    JSON.stringify(jsonData)
  ).then(function (data) {
    if (data.success) {
      var datas = formatRecordsData(data.obj.records);
      setTableData('#' + id, data.obj, tit);
    } else {
      console.log(data.msg);
    }
  });
}


// 删除模板功能
// 开始行
function delTemplate(id, name) {
  layer.confirm(
    '确认删除' + name + '模板?',
    { icon: 3, title: '提示' },
    function (index) {
      layer.close(index);
      ajax(service_prefix.visual, '/template/' + id + '/batch', 'delete').then(
        function (data) {
          var msg = data.msg;
          if (data.success) {
            msg = '删除成功';
            showWinTips(msg,null,1);

          }
          go2Menu('/simple/visualization/product/list.html', '产品管理');
        }
      );
    }

  );
}
// 结束行




/**
 * 删除ztree结构树栏目
 * @param {*} url
 * @param {*} name
 */
function delTreeChildren1(data) {
  if (data) {
    data.forEach(function (res) {
      switch (res.isSite) {
        case 0:
          res.icon = 'common/img/u5548.png';
          break;
        case 1:
          res.icon = 'common/img/u5554.png';
          break;
        case 2:
          res.icon = 'common/img/u2615.png';
          break;
      }
      if (res.children1.length == 0) {
        delete res['children'];
      } else {
        delTreeChildren(res.children1);
      }
    });
  }
}

var zTreeObj;
//将模板应用在某个站点
function applyTemplate(tempId, tplType) {
  layer.open({
    type: 1,
    area: ['400px', '500px'],
    title: '应用模板到选择栏目',
    maxmin: true,
    content: '<div id="applyView"></div>'
  });

  layui.laytpl($('#applyTemplate').html()).render({}, function (html) {
    $('#applyView').html(html);
  });

  var settingss = {
    data: {
      simpleData: {
        enable: true, //true 、 false 分别表示 使用 、 不使用 简单数据模式
        idKey: 'id', //节点数据中保存唯一标识的属性名称
        pIdKey: 'parentId', //节点数据中保存其父节点唯一标识的属性名称
        rootPId: -1 //用于修正根节点父节点数据，即 pIdKey 指定的属性值
      },
      key: {
        name: 'name' //zTree 节点数据保存节点名称的属性名称  默认值："name"
      }
    },
    view: {
      showLine: false,
      showIcon: false
    },
    check: {
      //表示tree的节点在点击时的相关设置
      enable: true, //是否显示radio/checkbox
      autoCheckTrigger: false,
      chkStyle: 'checkbox', //值为checkbox或者radio表示
      // radioType:"all",
      chkboxType: { Y: '', N: '' }, //表示父子节点的联动效果，不联动
      nocheckInherit: false
    }
  };
  ajax(service_prefix.manage, '/programa/getTreeData', 'post', {}).then(
    function (data) {
      var nodes = data.obj;
      // console.log('nodes',nodes);
      delTreeChildren1(nodes);
      zTreeObj = $.fn.zTree.init($('#treeDemo'), settingss, nodes); //初始化树
      zTreeObj.expandAll(true); //true 节点全部展开、false节点收缩
    }
  );
  layui.form.on('submit(apply_submit)', function (data) {
    // debugger;
    var nodes = zTreeObj.getCheckedNodes(true);
    var siteId = nodes[0].siteId; //站点ID
    var columnId = nodes[0].id; //栏目ID
    // console.log('nodes',nodes);
    var ids = [];
    var siteIds = [];
    for (var i in nodes) {
      if (nodes[i].level > 1) {
        ids.push(nodes[i].id);
      } else if (nodes[i].level == 1) {
        siteIds.push(nodes[i].id);
      }
    }
    ajax(service_prefix.visual, '/template/' + tempId, 'get').then(function (
      data
    ) {
      if (!data || !data.success) {
        showWinTips('数据获取失败');
        return;
      }
      console.log(data);
      //key之间的关系
      var keyRela = {
        tempdesc: 'proDes',
        temphtml: 'htmlCode',
        tempname: 'title',
        themecsscontent: 'cssCode',
        temptype: 'type',
        outputfilename: 'outFileName', //输出文件名
        tempext: 'exteName' //扩展名
      };
      var param = {};
      var obj = data.obj;
      for (var k in keyRela) {
        if (obj[keyRela[k]]) {
          if (keyRela[k] == 'htmlCode') {
            param[k] = toWholeHtml(obj);
          } else {
            param[k] = obj[keyRela[k]];
          }
        }
      }
      // var parentNode = nodes[0].getParentNode();
      // console.log('parentNode',parentNode);
      // param.siteid = columnId;
      param.siteid = siteId;
      // param.columnid = columnId;
      param.temptype = tplType; //概览1，细览2，嵌套3
      param.tpltype = 2; //静态模板1，动态模板2
      param.visualTemplateId = tempId; //模板ID
      // console.log('columnid',columnId);
      ajax(
        service_prefix.manage,
        '/template',
        'post',
        JSON.stringify(param)
      ).then(function (datas) {
        // /manage/template
        console.log(datas);
        var id = datas.obj.id;
        var p = {};
        p.ids = ids;
        p.siteIds = siteIds;
        // p.tplType = type;
        p.tplType = tplType; //首页模板1，详情模板2
        ajax(
          service_prefix.manage,
          '/programa/updateColumn?id=' + id,
          'post',
          JSON.stringify(p)
        ).then(function (data) {
          layer.msg('应用模板成功', function () {
            closeLayer();
          });
        });
      });
    });
  });
}
