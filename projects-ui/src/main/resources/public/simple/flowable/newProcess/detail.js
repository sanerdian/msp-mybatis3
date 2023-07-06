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


$(document).ready(function(){
    initSessionStorage();
});

layui.form.render();

// 标签切换事件监听
$(".tabType").on("click",function () {
    console.log($(this).data("url"));
    // 页面加载
    $("#view").load("simple/flowable/newProcess/ChildComponents/"+$(this).data("url"))
})
