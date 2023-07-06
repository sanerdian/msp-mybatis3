
//获取轮播图数据
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
				innerHtml += "\n<div class=\"swiper-slide\" data-swiper-slide-index=\"" + i + "\">";
				innerHtml += "\n\t<a href=\"#\"><img src=\"" + arr[i].imageUrl + "\"></a>";
				innerHtml += "\n\t<h3><a href=\"#\">" + arr[i].imageDesc + "</a></h3>";
				innerHtml += "\n\t<div class=\"visual_bg\"></div>";
				innerHtml += "\n</div>";
			}
			$("#" + listView + " .swiper-wrapper").html(innerHtml);
			var swiper = new Swiper('#'+ listView + ' .swiper-container', {
				pagination: '#'+ listView + ' .swiper-pagination',
				nextButton: '#'+ listView + ' .swiper-button-next',
				prevButton: '#'+ listView + ' .swiper-button-prev',
				speed: 500,
				autoplay : 5000,
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
	renderCarousel("exercise", "/carouselimage", 1, 10, {}, "widgetBox2");
})
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
				innerHtml += "\n\t\t<span>" + arr[i].startTime + "</span>";
				innerHtml += "\n\t</li>";
			}
			innerHtml += "\n</ul>";
			$("#" + listView + " .visual_text_list_view").html(innerHtml);
		}else{
			console.log(data.msg);
		}
	});
}
//获取文字列表标题
function renderTitle1(listView){
	var titleHtml = "\n\t<div class=\"more\"><a href=\"/pub/html/visualStyle/enterprise/list.html\">更多 &gt;</a></div>";
	titleHtml += "<h3><a href=\"/pub/html/visualStyle/enterprise/list.html\">机床企业</a></h3>";
	$("#" + listView + " .visual_title").html(titleHtml);
}
$(function(){
	renderTitle1("widgetBox3");
	renderList1("exercise", "/computer", 1, 5, {}, "widgetBox3");
})
//获取轮播图数据
function renderCarousel2(serviceId, url, curr, limit, entity, listView){
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
				innerHtml += "\n<div class=\"swiper-slide\" data-swiper-slide-index=\"" + i + "\">";
				innerHtml += "\n\t<a href=\"#\"><img src=\"" + arr[i].imageUrl + "\"></a>";
				innerHtml += "\n\t<h3><a href=\"#\">" + arr[i].imageDesc + "</a></h3>";
				innerHtml += "\n\t<div class=\"visual_bg\"></div>";
				innerHtml += "\n</div>";
			}
			$("#" + listView + " .swiper-wrapper").html(innerHtml);
			var swiper = new Swiper('#'+ listView + ' .swiper-container', {
				pagination: '#'+ listView + ' .swiper-pagination',
				nextButton: '#'+ listView + ' .swiper-button-next',
				prevButton: '#'+ listView + ' .swiper-button-prev',
				speed: 500,
				autoplay : 5000,
				slidesPerView: 1,
				spaceBetween: 20,
				slidesPerView: 7,
				loop: true,
			});
		}else{
			console.log(data.msg);
		}
	});
}
$(function(){
	renderCarousel2("exercise", "/carouselimage", 1, 10, {}, "widgetBox8");
})
function getMenuColumn(){
	var params = {
		"entity": {
			status: 0,
			stop: 0,
			parentId: 0,
			siteId:"1549310008678813698"
		
		},
		"pager": {
			current: 1,
			size: 100,
			sortProps: [
				{key: "parentId", value: true},
				{key: "chnlOrder", value: true}
			]
		}
	};
	ajax(service_prefix.manage,"/programa/list", "post", JSON.stringify(params)).then(function(data){
		getData(data.obj.records, '#widgetHeaderMenuTpl', '#widgetHeaderMenuView');
	});
}
$(function(){
	$("#widgetHeaderMenuView").html("");
	getMenuColumn();
})