//组件一级分类
function groupType() {
  var jsonData = {
    entity: {
      parentId: 0
    },
    pager: {
      current: 1,
      size: 20
    }
  };
  ajax(
    service_prefix.visual,
    '/moduletype/listing',
    'post',
    JSON.stringify(jsonData)
  ).then(function (data) {
    if (data.success) {
      getData(data.obj.records, '#groupTypeTemplate', '#groupTypeView');
      $('#groupTypeView .layui-btn:first').removeClass('layui-btn-primary');
      //点击当前组件分类
      $('#groupTypeView .layui-btn').click(function () {
        $(this)
          .removeClass('layui-btn-primary')
          .siblings()
          .addClass('layui-btn-primary');
        var typeId = $(this).attr('data-id');
        groupSubType(typeId);
      });
      //默认显示组件一级分类的二级分类
      var parentId = $('#groupTypeView .layui-btn:first').attr('data-id');
      console.log('parentId', parentId);
      groupSubType(parentId);
    } else {
      console.log(data.msg);
    }
  });
}

//组件二级分类
function groupSubType(parentId) {
  var jsonData = {
    entity: {
      parentId: parentId
    },
    pager: {
      current: 1,
      size: 20
    }
  };
  ajax(
    service_prefix.visual,
    '/moduletype/listing',
    'post',
    JSON.stringify(jsonData)
  ).then(function (data) {
    if (data.success) {
      getData(data.obj.records, '#groupSubTypeTemplate', '#groupSubTypeView');
      $('#groupSubTypeView .layui-btn:first').removeClass('layui-btn-primary');
      //点击当前组件分类
      $('#groupSubTypeView .layui-btn').click(function () {
        $(this)
          .removeClass('layui-btn-primary')
          .siblings()
          .addClass('layui-btn-primary');
        groupTypeParam();
      });
      groupTypeParam();
    } else {
      console.log(data.msg);
    }
  });
}

//组件分类的列表数据
var serviceId = service_prefix.visual;
var url = '/module/list';
function groupList(serviceId, url, curr, size, param) {
  var jsonData = {
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
  ajax(serviceId, url, 'post', JSON.stringify(jsonData)).then(function (data) {
    if (data.success) {
      getData(data.obj.records, '#groupListTemplate', '#groupListView');
      page(data.obj.total, curr, size, groupList, '', param);
    } else {
      console.log(data.msg);
    }
  });
}

//默认分类
function groupTypeParam() {
  var netType = $('#terminal ul li.layui-this').attr('data-type');
  var frame = $('#frameType .layui-btn')
    .not('.layui-btn-primary')
    .attr('data-type');
  var groupId = $('#groupTypeView .layui-btn')
    .not('.layui-btn-primary')
    .attr('data-id');
  var groupSubId = $('#groupSubTypeView .layui-btn')
    .not('.layui-btn-primary')
    .attr('data-id');
  console.log(netType, frame, groupId, groupSubId);
  var param = {
    netType: netType,
    frame: frame,
    vmType: groupId,
    vcType: groupSubId
  };
  groupList(serviceId, url, 1, 10, param);
}

//删除当前组件
function delmoudle(id, name) {
  layer.confirm(
    '确认删除' + name + '组件?',
    { icon: 3, title: '提示' },
    function (index) {
      layer.close(index);
      ajax(service_prefix.visual, '/module/' + id + '/batch', 'delete').then(
        function (data) {
          var msg = data.msg;
          if (data.success) {
            msg = '删除成功';
          }
          showWinTips(msg, null, 1);
          go2Menu('/simple/visualization/subGroup/list.html', '组件管理');
        }
      );
    }

  );
}

//图片转换base64
function changeFile(_this, id) {
  console.log('this', _this);
  var file = _this.files[0]; //检验选择文件格式
  var fileType = file.name.split('.').reverse()[0].toLowerCase();
  var imageList = ['png', 'gif', 'jpg', 'jpeg']; //图片文件格式列表
  if (!imageList.includes(fileType)) {
    alert('文件格式不正确');
    return false;
  }
  // 创建文件读取实例
  var fileReader = new FileReader();
  fileReader.readAsDataURL(file);
  fileReader.onload = (e) => {
    var imageBase64 = e.target.result; //获取base64字符串
    console.log('imageBase64', imageBase64);
    localStorage.setItem('visualSubGroupImageBase64', imageBase64);
    $('#' + id + '.uploadImg').attr('src', imageBase64);
    // layer.msg('图标上传成功');
  };
}

//添加组件类型和新建类型分类,parentId为0是组件类型，类型分类的parentId为父组件类型的id
function addModuleType(name, parentId) {
  console.log('1', localStorage.getItem('visualSubGroupImageBase64'));
  var imageBase64 = localStorage.getItem('visualSubGroupImageBase64');
  var params = {
    parentId: parentId,
    title: name,
    icon: imageBase64
  };
  ajax(
    service_prefix.visual,
    '/moduletype',
    'post',
    JSON.stringify(params)
  ).then(function (data) {
    if (data && data.success) {
      showWinTips('添加成功');
      go2Menu('/simple/visualization/subGroup/list.html', '组件管理');
    } else {
      var err = '请求失败';
      if (data && data.msg) {
        err = '请求失败：' + data.msg;
      }
      showWinTips(err);
    }
  });
}

//新建组件一级分类
function groupTypeLayer() {
  layer.open({
    type: 1,
    title: '组件类型',
    area: ['570px', '280px'],
    btnAlign: 'c',
    btn: ['确定', '取消'],
    content: '<div id="groupTypeLayerView"></div>',
    success: function (layero, index) {
      var data = [];
      getData(data, '#groupTypeLayerTemplate', '#groupTypeLayerView');
      form.on('submit(groupTypeFormSubmit)', function (data) {
        addModuleType(data.field.newMoudelname, 0);
        layer.close(layer.index);
        return false;
      });
    },
    yes: function (index, layero) {
      form.on('submit(groupTypeFormSubmit)', function (data) {
        addModuleType(data.field.newMoudelname, 0);
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

//新建组件二级分类
function groupSubTypeLayer() {
  layer.open({
    type: 1,
    title: '类型分类',
    btnAlign: 'c',
    btn: ['确定', '取消'],
    area: ['570px', '280px'],
    content: '<div id="groupSubTypeLayerView"></div>',
    success: function (layero, index) {
      var data = [];
      getData(data, '#groupSubTypeLayerTemplate', '#groupSubTypeLayerView');
      var parentId = $('#groupTypeView .layui-btn')
        .not('.layui-btn-primary')
        .attr('data-id');
      console.log('parentId', parentId);
      form.on('submit(groupSubTypeFormSubmit)', function (data) {
        addModuleType(data.field.newMoudelname, parentId);
        return false;
      });
    },
    yes: function (index, layero) {
      var parentId = $('#groupTypeView .layui-btn')
        .not('.layui-btn-primary')
        .attr('data-id');
      console.log('parentId', parentId);
      form.on('submit(groupSubTypeFormSubmit)', function (data) {
        addModuleType(data.field.newMoudelname, parentId);
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

//为组件设置表单获取组件类型和类型分类数据
function getLabels(fn) {
  var params = {
    entity: {
      pid: 0
    },
    pager: {
      current: 1,
      size: 1000,
      sortProps: {
        key: 'modifyTime',
        value: false
      }
    }
  };
  ajax(
    service_prefix.visual,
    '/moduletype/listing',
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
}

//初始换组件设置表单下拉框数据
function initSelect(data, vcId, vmId) {
  console.log('initSelect', data, vcId, vmId);
  let obj = data;
  let p = [];
  $('.visualMoudeltype').empty();
  $('.visualClasstype').empty();
  for (var k = 0; k < obj.length; k++) {
    if (obj[k].parentId == 0) {
      p.push(obj[k].id);
      $('.visualMoudeltype').append(
        ' <option value="' + obj[k].id + '">' + obj[k].title + '</option>'
      );
    }
  }
  let vmId1 = vmId ? vmId : p[0];
  for (var k = 0; k < obj.length; k++) {
    
    if (obj[k].parentId == vmId1) {
      $('.visualClasstype').append(
        ' <option value="' + obj[k].id + '">' + obj[k].title + '</option>'
      );
    }
  }
  // $('.visualMoudeltype').empty();
  // $('.visualClasstype').empty();
  // for (var k = 0; k < obj.length; k++) {
  //   if (obj[k].parentId == 0)
  //     $('.visualMoudeltype').append(
  //       ' <option value="' + obj[k].id + '">' + obj[k].title + '</option>'
  //     );
  //   if (obj[k].parentId == vmId)
  //     $('.visualClasstype').append(
  //       ' <option value="' + obj[k].id + '">' + obj[k].title + '</option>'
  //     );
  // }


  // $('#setGroupform [name=visualMoudeltype]').val(localStorage.getItem('visual-subgroup-moudleType'));
  // $('#setGroupform [name=visualClasstype]').val(localStorage.getItem('visual-subgroup-classType'));
}

//组件设置表单点击组件类型时修改类型分类
function changeSelect(data, parent) {
  let obj = data;
  $('.visualClasstype').empty();
  for (var k = 0; k < obj.length; k++) {
    if (obj[k].parentId == parent)
      $('.visualClasstype').append(
        ' <option value="' + obj[k].id + '">' + obj[k].title + '</option>'
      );
  }
}

//新建组件弹框
function buildModulefunction() {
  var labels = null;
  getLabels(function (data) {
    labels = data.records;
    console.log('labels', labels);
    initSelect(labels);
    form.render();
  });
  layer.open({
    title: '新建组件',
    content: $('#buildModule').html(),
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
    //下拉事件触发
    form.on('select(visualMoudeltype)', function (data) {
      changeSelect(labels, data.value);
      form.render();
    });
    //提交表单触发
    form.on('submit(groupBtnAdd)', function (data) {
      console.log(data.field);
      ajax(
        service_prefix.visual,
        '/module',
        'post',
        JSON.stringify(data.field)
      ).then(function (data2) {
        if (data2.success) {
          console.log('data2', data2);
          go2Menu('/simple/visualization/subGroup/list.html', '组件管理');
          var url =
            'simple/visualization/subGroup/groupEdit.html#id=' + data2.obj.id;
          window.open(url, '_blank');
        }
      });
      layer.close(index);
      return false;
    });
  });
}

//搜索组件
function searchGroup() {
  form.on('submit(searchGroup)', function (data) {
    var param = {
      title: data.field.title
    };
    groupList(serviceId, url, 1, 10, param);
    $('.subGroupFilter .btnRowRight .layui-btn').addClass('layui-btn-primary'); //去掉组件分类的当前状态
    return false;
  });
}

$(function () {
  groupType();

  //终端切换
  $('#terminal .layui-nav-item').click(function () {
    $(this).addClass('layui-this').siblings().removeClass('layui-this');
    groupTypeParam();
  });

  //开发框架切换
  $('#frameType .layui-btn').click(function () {
    $(this)
      .removeClass('layui-btn-primary')
      .siblings()
      .addClass('layui-btn-primary');
    groupTypeParam();
  });

  //新建组件一级分类按钮
  $('#groupTypeAddBtn').click(function () {
    groupTypeLayer();
  });

  //新建组件二级分类按钮
  $('#groupSubTypeAddBtn').click(function () {
    groupSubTypeLayer();
  });

  //新建组件弹框
  $('.buildModule').click(function () {
    buildModulefunction();
  });

  //搜索组件
  searchGroup();
});
