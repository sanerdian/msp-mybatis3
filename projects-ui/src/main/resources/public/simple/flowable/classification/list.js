//服务id  service_prefix.dict
var serviceId = "";

//接口地址
var url = '/flowable/processclass';

//列表数据
var columns = [
    {type: 'checkbox', fixed: 'left'}
    , {type: 'numbers', title: '序号', fixed: 'left'}
    , { field: 'className', title: '分类名称' , align: 'center'}
    , {field: 'orderNumber', title: '排序号', align: 'center'}
    , {field: 'wordname', title: '图标', align: 'center' ,templet: function(d){
            return  `<img src="${d.iconPath}" alt="" srcset=""></img>`;
        }}
    , {field: 'shuxing', title: '颜色', align: 'center',templet: function(d){
            return  `<div class="box" style="background-color: ${d.iconColor};"></div>`;
        }}
    , {field: '', title: '管理', toolbar: '#operateTemplate', align: 'center', fixed: 'right', minWidth: 300}
];
var form = layui.form, layer = layui.layer, tree = layui.tree, util = layui.util;

var jsonData = {
    pager: {
        current: 1,
        size:10,
        sortProps: [
            {
                key: "crtime",
                value: false
            }
        ]
    },
    entity: {}
};

// 添加分类
function addClass() {
    setLayer('addDiv', "添加工作流引擎", '500px', '440px');
    laytpl($('#editTemplate').html()).render({}, function (html) {
        $('#addDiv').html(html);
        layui.form.render()
    });
    imgAndColor();
    formSubmitAdd(serviceId, url);
}

// 上传组件渲染
function imgAndColor() {
    // 图片上传
    upload.render({
        elem: '#uploadImg'
        ,url: serviceId + '/member/user/importHead'
        ,before: function(obj){
            obj.preview(function(index, file, result){
                $('#showImg').attr('src', result);
            });
        }
        ,done: function(res){
            //如果上传失败
            if(res.code > 0){
                return layer.msg('上传失败');
            }
        }
    });
}

//设置主题词 相关的属性词信息
function setContactAttribute(id){
    ajax(serviceId,'/dictshuxing/view/' + id,'post').then(function (data){
        if(data.success){
            $('#attributeContact [name="relationword"]').val(data.obj.relationword);
            $('#attributeContact [name="shuxingtongyici"]').val(data.obj.tongyinci);
            $('#attributeContact [name="shuxingpinyin"]').val(data.obj.pinyin);
        }
    })
}

//列表中删除、编辑、详情
table.on('tool(tableData)', function(obj){
    var data = obj.data;
    var layEvent = obj.event;

    var curName = $('.layui-side .layui-this').text();
    if (layEvent == 'edit') {//编辑
        //表单提交
        formSubmitEdit(serviceId, url);
        id = data.jmetawordrelationid;
        var typetxt = $(this).text();
        var curName = $.trim($('.layui-side .layui-this').text());
        var tit = typetxt + curName;
        setLayer('editDiv', tit, '500px', '440px');
        ajax(serviceId,'/zhutici/allList','post',JSON.stringify(jsonData)).then(function (resData) {
            laytpl($('#editTemplate').html()).render(resData.obj, function (html) {
                $('#editDiv').html(html);
                layui.form.render()
            });
            imgAndColor();
        })
    } else if (layEvent == 'moveUp') { //下移

    } else if (layEvent == 'moveDownl') { //上移

    }
});

setListData(curr, defaultPageSize, serviceId, url);

/**
 *
 * @param {*} curr
 * @param {*} defaultPageSize
 * @param {*} serviceId
 * @param {*} url
 * @param {*} entity
 */
function setListData (curr, defaultPageSize, serviceId, url, entity) {
    if (entity) {
    }else{
        var entity = {};
    }
    var jsonData = {
        "pager": {
            "current": curr,
            "size": defaultPageSize,
            "sortProps": [
                // {
                //     "key": "crtime",
                //     "value": false
                // }
            ]
        },
        "entity": entity
    };

    ajax(serviceId, url + '/listing', 'post', JSON.stringify(jsonData)).then(function (data) {
        if (data.success) {
            for (let i = 0; i < data.obj.records.length; i++) {
                var status = data.obj.records[i].status;
            }
            setTableData('#tableData', data.obj, columns)
            curr = data.obj.current
            defaultPageSize = defaultPageSize
            var pegeObj = {}
            pegeObj.serviceId = serviceId
            pegeObj.url = url
            pegeObj.entity = entity
            pegeObj.elem = "page"
            pegeObj.total = data.obj.total
            pegeObj.curr = data.obj.current
            pegeObj.size = defaultPageSize
            loadPage1(pegeObj, setListData)
        } else {
            setTableData('#tableData', {}, columns)
        }
    })
}

formSubmitAdd(serviceId, url);
