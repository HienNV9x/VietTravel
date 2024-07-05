$(document).ready(function () {
    // Function to initialize slider and manage arrow buttons
    function initializeSlider(sliderId) {
      $(sliderId).slick({
        arrows: true,
        prevArrow: "<i class='bx bx-chevron-left btn-prev'></i>",
        nextArrow: "<i class='bx bx-chevron-right btn-next'></i>",
        slidesToShow: 4,
        slidesToScroll: 1,
        infinite: false,
        initialSlide: 0,
      });
    }  
    
    //Chờ sự kiện 'coursesRendered' trước khi khởi tạo slider
    document.addEventListener('coursesRendered', function() {
        initializeSlider('#slider-hotel');
    });
    
    document.addEventListener('cuisineRendered', function() {
        initializeSlider('#slider-cuisine');
    });
    
    document.addEventListener('entertainmentRendered', function() {
        initializeSlider('#slider-entertainment');
    });
});
