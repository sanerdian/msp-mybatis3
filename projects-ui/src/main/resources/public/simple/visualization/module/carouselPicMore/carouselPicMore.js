$(function(){
  function carouselPicMore(){
    var swiper = new Swiper('.visual_swiper_pic_more .swiper-container', {
      pagination: '.swiper-pagination',
      nextButton: '.swiper-button-next',
      prevButton: '.swiper-button-prev',
      speed: 500,
      autoplay : 5000,
      spaceBetween: 20,
      slidesPerView: 7,
      loop: true,
    });
  }
  carouselPicMore();
})