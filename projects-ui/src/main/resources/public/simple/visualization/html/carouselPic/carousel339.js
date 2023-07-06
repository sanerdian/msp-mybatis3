//获取文字列表数据
function renderCarousel(serviceId, url, curr, limit, entity, listView){
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
            var innerHtml = "";
            for (var i = 0; i < arr.length; i++) {
                innerHtml += "\n<div class=\"swiper-slide\" data-swiper-slide-index=\"" + i + "\" style=\"width: 411px;\">";
                innerHtml += "\n\t<a href=\"#\"><img src=\"" + arr[i].imageUrl + "\"></a>";
                innerHtml += "\n\t<h3><a href=\"#\">图片" + (i+1) + "</a></h3>";
                innerHtml += "\n\t<div class=\"visual_bg\"></div>";
                innerHtml += "\n</div>";
            }
            $("#" + listView).html(innerHtml);
            var swiper = new Swiper('.visual_swiper_pic .swiper-container', {
                pagination: '.swiper-pagination',
                nextButton: '.swiper-button-next',
                prevButton: '.swiper-button-prev',
                slidesPerView: 1,
                paginationClickable: true,
                loop: true
            });
        }else{
            console.log(data.msg);
        }
    });
}
$(function(){
    renderCarousel("exercise", "/carouselimage", 1, 10, {}, "carouselView");
})
