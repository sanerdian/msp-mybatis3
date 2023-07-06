//服务id
// var serviceId = service_prefix.dict;
var serviceId = "";

//接口地址
var url = '/zhutici';
//列表数据
var columns = [
    { type: 'checkbox', fixed: 'left' }
    , { type: 'numbers', title: '序号', fixed: 'left' }
    , { field: 'fenlei', title: '模块名称' }
    , { field: 'wordname', title: '主题词' }
    , { field: 'tongyici', title: '主题词同义词' }
    , { field: 'pinyin', title: '主题词拼音' }
    , { field: 'tongyinci', title: '主题同音词' }
    , { field: 'entongyici', title: '英文同义词' }
    , { field: 'suoxie', title: '英文缩写' }
    , { field: 'source', title: '词条来源' }
    , { field: 'status', title: '词条状态', width: 100, templet: "#statusTpl"}
    , { field: 'crtime', title: '创建时间' }
    , { field: 'cruser', title: '创建人' }
    , { field: 'opertime', title: '修改时间' }
    , { field: 'operuser', title: '修改人' }
    , { field: '', title: '操作', toolbar: '#operateTemplate', fixed: 'right', width: 180 }
];

//点击按钮
function btnClick () {
    $('.layui-btn').click(function () {
        var type = $(this).attr('data-type');
        var typetxt = $(this).text();
        var curName = $.trim($('.layui-side .layui-this').text());
        var tit = typetxt + curName;
        if (type == 'addDict') {
            setLayer('addDiv', tit, '650px', '650px');
            //表单提交
            formSubmitAdd(serviceId, url);
            $('#addDiv').html($('#addTemplate').html());
            setRender();
            //添加模块名称
            addModuleName();
        } else if (type == 'delDict') {
            del(0);
        } else if (type == 'examDict') {//审核
            // formSubmitEdit(serviceId,url);
            // id = data.id;
            setLayer('examDiv', tit, '650px', '700px');
            laytpl($('#examTemplate').html()).render(editdata, function (html) {
                $('#examDiv').html(html);
                setFormVal('editForm', editdata);
            });
            formSubmitEditExam(serviceId, url, editdata.jmetasearwordid);
        }
    });
}

table.on('tool(tableData)', function (obj) {
    var data = obj.data;
    var layEvent = obj.event;
    var tr = obj.tr;
    console.log('data',data);
    var curName = $('.layui-side .layui-this').text();
    if (layEvent == 'del') { //删除
        console.log(data)
        layer.alert("确定删除:[" + data.wordname + "]吗？", function () {
            doDeleteById(serviceId, url, data.jmetasearwordid);
        })
    } else if (layEvent == 'edit') { //编辑
        //表单提交
        formSubmitEdit(serviceId, url);
        id = data.jmetasearwordid;
        // id = data.id
        var typetxt = $(this).text();
        var curName = $.trim($('.layui-side .layui-this').text());
        var tit = typetxt + curName;
        setLayer('editDiv', tit, '650px', '660px');
        //后台往前台传,data
        laytpl($('#editTemplate').html()).render(data, function (html) {
            $('#editDiv').html(html);
            setFormVal('editForm', data);
        });
        //前台保存data根据Id 往后台传
        //使用的方式ajax
        // formSubmitEditExam(serviceId, url, id);
    } else if (layEvent == 'detail') { //详情
        setLayer('detailDiv', tit, '723px', '600px');
        if (data.modifyTime == null) {
            data.modifyTime = ''
        }
        getData(data, '#detailTemplate', '#detailDiv');
    }
}); 

setListData(curr, defaultPageSize, serviceId, url);
getSearch(serviceId, url,curr,defaultPageSize);
btnClick('550px', '500px');
setOpenLayer();

$(document).on('click', "#huoquzhuticipinyin", function () {
    layer.msg("按钮点击");
});

//获取主题词拼音
function getzhuticipinyin () {
    // alert("353434")
    var zhutici = $('#wordname').val();
    // alert(zhutici)
    if (!zhutici) return false;
    zhutici = zhutici.replace(/\s*/g, "");
    // alert(zhutici)

    console.log("zhutici", zhutici);

    // 获取全写拼音（调用js中的方法）
    var fullChars = pinyin.getFullChars(zhutici);
    // alert("s属性词 pingyin="+fullChars)
    // 		 console.log("shuxingpinyin", fullChars);

    //给文本框赋值
    $('#pinyin').val(fullChars);
}
formSubmitAdd(serviceId, url);

//获取模块信息
function getMoudleInfo(){
    var jsonData = {
        "entity": {},
        "pager": {
          "current": 1,
          "size": 10
        }
    }
    ajax(serviceId,'/metadata/moduleinfo/list','post',JSON.stringify(jsonData)).then(function (data){
        if(data.success){
            getData(data.obj.records, '#moduleNameTpl', '#moduleNameView');
            setRender();
        }
    })
}

//选择模块确认
function setupModuleConfirm(){
    var setupModuleName = $('#moduleNameView').val();
    localStorage.setItem('dictionarySetupModuleName',setupModuleName);
    var jsonData = {
        "fenlei": setupModuleName
    }
    ajax(serviceId,'/zhutici/batchModify','post',JSON.stringify(jsonData)).then(function (data){
        if(data.success){
            layer.msg('设置模块成功！');
        }
    })
}

//设置模块弹出框
function setupModule(){
    layer.open({
        type: 1,
        scrollbar:false,
        area: '520px',
        btnAlign: 'c',
        title: ['设置模块'],
        shadeClose: true,
        btn: ['确认', '取消'],
        content: $('#setupModule'),
        success: function(layero, index){
            var mask = $(".layui-layer-shade");
            mask.appendTo(layero.parent());
            //获取模块信息
            getMoudleInfo();
        },
        btn1:function(){
            //选择模块确认
            setupModuleConfirm();
            $('#setupModule').hide();
            layer.closeAll();
        },
        btn2:function(){
            $('#setupModule').hide();
            layer.closeAll();
        },
        cancel:function(){
            $('#setupModule').hide();
            layer.closeAll();
        }
    });
}