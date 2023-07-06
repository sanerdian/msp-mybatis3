
//获取文字列表数据
function renderList1(serviceId, url, curr, limit, entity, listView){
    var params = {
        "entity": entity,
        "pager": {
            current: curr,
            size: limit
        }
    };
    ajax(serviceId, url + "/listing", "post", JSON.stringify(params)).then(function(data){
        if(data.success){
            var arr = data.obj.records;
            var innerHtml = "<ul>";
            for (var i = 0; i < arr.length; i++) {
                innerHtml += "\n\t<li>\n\t\t<h4><a href=\"#\">" + arr[i].computerCode + "</a></h4>";
                innerHtml += "\n\t\t<span>" + arr[i].userPerson + "</span>";
                innerHtml += "\n\t</li>";
            }
            innerHtml += "\n</ul>";
            $("#" + listView).html(innerHtml);
        }else{
            console.log(data.msg);
        }
    });
}
$(function(){
    renderList1("exercise", "/computer", 1, 10, {}, "widgetTextListView");
})
//获取文字列表数据
function renderList2(serviceId, url, curr, limit, entity, listView){
    var params = {
        "entity": entity,
        "pager": {
            current: curr,
            size: limit
        }
    };
    ajax(serviceId, url + "/listing", "post", JSON.stringify(params)).then(function(data){
        if(data.success){
            var arr = data.obj.records;
            var innerHtml = "<ul>";
            for (var i = 0; i < arr.length; i++) {
                innerHtml += "\n\t<li>\n\t\t<h4><a href=\"#\">" + arr[i].computerCode + "</a></h4>";
                innerHtml += "\n\t\t<span>" + arr[i].startTime + "</span>";
                innerHtml += "\n\t</li>";
            }
            innerHtml += "\n</ul>";
            $("#" + listView).html(innerHtml);
        }else{
            console.log(data.msg);
        }
    });
}
$(function(){
    renderList2("exercise", "/computer", 1, 10, {}, "widgetTextListView2");
})