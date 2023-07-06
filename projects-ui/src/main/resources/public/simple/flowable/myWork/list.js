// 弹框页面加载
setLoadCurPath('#editOpenDiv', 'expert.html');

// 批量导出方法
function batchExpert(params) {
    setLayer('batchExpertView', "导出", '500px', '200px');
	laytpl($('#batchExpertTemple').html()).render({}, function (html) {
        $('#batchExpertView').html(html);
        layui.form.render()
    });
    formSubmitAdd(serviceId, url);
}
layui.use('element', function () {
    var $ = layui.jquery
        , element = layui.element; //Tab的切换功能，切换事件监听等，需要依赖element模
    $("li").on('click', function (e) {
        var others = $(this).siblings();
        if ($(this).hasClass('unselect')) {
            $(this).removeClass('unselect');
            $(this).addClass('select');
            others.addClass('unselect');
        }
    })
});
layui.use(['form', 'layedit', 'laydate'], function () {
    var form = layui.form
        , layer = layui.layer
        , layedit = layui.layedit
        , laydate = layui.laydate;

    //日期
    laydate.render({
        elem: '#date'
    });
    laydate.render({
        elem: '#date1'
    });
});

layui.table.on('tool(workTable)', function(obj){
    var row = obj.data;
    if(obj.event === 'toDetail'){
        showDetail(row);
    }
});

// tab切换监听
layui.element.on('tabDelete(docDemoTabBrief)', function(data){
    console.log(data.elem); //得到当前的Tab大容器
});

// $(document).ready(function(){
//     initSessionStorage();
// });

layui.form.render();