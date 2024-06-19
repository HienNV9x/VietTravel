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
    
    // Initialize sliders
    window.onload = function() {
    	initializeSlider('#slider-hotel');
    	initializeSlider('#slider-cuisine');
    	initializeSlider('#slider-entertainment');
    };
});
