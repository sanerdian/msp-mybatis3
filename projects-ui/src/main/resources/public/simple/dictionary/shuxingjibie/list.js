//服务id service_prefix.dict
var serviceId = "";

//接口地址
var url = '/dictdianpinglevel';

//列表数据
var columns = [

    {type: 'checkbox', fixed: 'left'}
    , {type: 'numbers', title: '序号', fixed: 'left'}
    , {field: 'shuxingjibie', title: '属性级别', fixed: 'left'}
    , {field: 'manyidu', title: '满意度', fixed: 'left'}
    , {field: 'createTime', title: '创建时间', fixed:'left'}
    , {field: 'crUser', title: '创建人', fixed: 'left'}
    , {field: 'modifyTime', title: '修改时间', fixed: 'left'}
    , {field: 'modifyUser', title: '修改人', fixed: 'left'}
    , {field: '', title: '操作', toolbar: '#operateTemplate', align: 'left', fixed: 'right', width: 200}
   /* , {field: 'reltongyinci', title: '相关同音词', fixed: 'left'}
    , {field: 'sxtongyici', title: '属性同义词', fixed: 'left'}
    , {field: 'sxpinyin', title: '属性拼音', fixed: 'left'}
    , {field: 'sxtongyinci', title: '属性同音词', fixed: 'left'}
    , {field: 'tingyongci', title: '停用词', fixed: 'left'}
    , {field: 'changjingci', title: '场景词', fixed: 'left'}
    , {field: 'rule', title: '规则', fixed: 'left'}
    , {field: 'ctfl', title: '指标四级分类', fixed: 'left'}
    , {field: 'ctfl_sanji', title: '指标三级分类', fixed: 'left'}
    , {field: 't2', title: '指标二级分类', fixed: 'left'}
    , {field: 't1', title: '指标一级分类', fixed: 'left'}

    , {field: 'createTime', title: '创建时间', fixed: 'left'}*/
    /*modify_by
    modify_time*/
   /* , {field: 'modifyTime', title: '修改时间', fixed: 'left'}
    , {field: 'modifyUser', title: '修改人', fixed: 'left'}
    , {field: 'operuser', title: '相关贴', fixed: 'left'}*/
];

//点击按钮
function btnClick(){
    $('.layui-btn').click(function(){
        var type = $(this).attr('data-type');
        var typetxt = $(this).text();
        var	curName = $.trim($('.layui-side .layui-this').text());
        var tit = typetxt+curName;
        if(type == 'add'){
            setLayer('addDiv',tit,'550px','300px');
            //表单提交
            formSubmitEdit(serviceId,url);
            $('#addDiv').html($('#addTemplate').html());
            setRender();
            // ajax(serviceId,'/xingrongci/xrcList','get',{"type":"get"}).then(function (data){
            ajax(serviceId,'/dict/shuxingcijibie/sxjbList','get').then(function (data){
                laytpl($('#addTemplate').html()).render(data, function(html){
                    $('#addDiv').html(html);
                    setRender();
                });
            });
        }else if(type == 'del'){
            del();
        }else if(type == 'exam'){//审核
            // formSubmitEdit(serviceId,url);
            // id = data.id;
            setLayer('examDiv',tit,'550px','350px');
            laytpl($('#examTemplate').html()).render(editdata, function(html){
                $('#examDiv').html(html);
                setFormVal('editForm',editdata);
            });
            formSubmitEditExam(serviceId,url,editdata.id);
        }else if(type == 'out'){//停用
            alert("1111")
        }
    });
}

table.on('tool(tableData)', function(obj){
    var data = obj.data;
    var layEvent = obj.event;
    var tr = obj.tr;

    var curName = $('.layui-side .layui-this').text();
    console.log('curName2',curName)
    if (layEvent == 'del') { //删除
        layer.alert("确定删除:[" + data.manyidu + "]吗？", function () {
            doDeleteById(serviceId,url,data.jmetashuxingciid);
        })
    } else if (layEvent == 'edit') { //编辑
        //表单提交
        formSubmitEdit(serviceId,url);
        id = data.id;
        var typetxt = $(this).text();
        var	curName = $.trim($('.layui-side .layui-this').text());
        var tit = typetxt+curName;
        setLayer('editDiv',tit,'550px','300px');
        laytpl($('#editTemplate').html()).render(data, function(html){
            $('#editDiv').html(html);
            setFormVal('editForm',data);
        });
        // formSubmitEditExam(serviceId,url,id);
    } else if (layEvent == 'detail') { //详情
        setLayer('detailDiv',tit,'550px','400px');
        if(data.modifyTime == null){
            data.modifyTime = ''
        }
        getData(data,'#detailTemplate','#detailDiv');

    }
});

setListData(1,15,serviceId, url);
getSearch(serviceId,url,1,15);
btnClick('550px','500px');
setOpenLayer();

$(document).on('click',"#huoquzhuticipinyin",function(){
    layer.msg("按钮点击");
});

function getzhuticipinyin() {
    // alert("353434")
    var zhutici = $('#wordname').val();
    // alert(zhutici)
    if(!zhutici) return false;
    zhutici = zhutici.replace(/\s*/g,"");
    // alert(zhutici)

    console.log("zhutici", zhutici);

// 获取全写拼音（调用js中的方法）
    var fullChars = pinyin.getFullChars(zhutici);
    // alert("s属性词 pingyin="+fullChars)
// 		 console.log("shuxingpinyin", fullChars);

//给文本框赋值
    $('#pinyin').val(fullChars);
}
formSubmitAdd(serviceId,url);