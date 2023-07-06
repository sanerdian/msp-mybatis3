$(function(){
  function carouselPic(){
    var swiper = new Swiper('.visual_swiper_pic .swiper-container', {
      pagination: '.swiper-pagination',
      nextButton: '.swiper-button-next',
      prevButton: '.swiper-button-prev',
      speed: 500,
      autoplay : 5000,
      slidesPerView: 1,
      paginationClickable: true,
      loop: true
    });
  }
  carouselPic();
})