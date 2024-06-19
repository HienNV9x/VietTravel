const buyBtns = document.querySelector('.js-buy-tour')
const buyBtnss = document.querySelector('.js-buy-tour-2')
const buyBtnsss = document.querySelector('.js-buy-tour-3')

const modal = document.querySelector('.js-modal')
const modals = document.querySelector('.js-modal-2')
const modalss = document.querySelector('.js-modal-3')

const modalClose = document.querySelector('.js-modal-close')
const modalClosee = document.querySelector('.js-modal-close-2')
const modalCloseee = document.querySelector('.js-modal-close-3')

const modalContainer = document.querySelector('.js-modal-container')
const modalContainerr = document.querySelector('.js-modal-container-2')
const modalContainerrr = document.querySelector('.js-modal-container-3')

//Hàm hiển thị modal mua vé(thêm class open vào modal)
function showBuyTours() {
    modal.classList.add('open')
}
function showBuyTourss() {
    modals.classList.add('open')
}
function showBuyToursss() {
    modalss.classList.add('open')
}

//Hàm ẩn modal mua vé (gỡ bỏ class open của modal)
function hideBuyTours() {
    modal.classList.remove('open')
}
function hideBuyTourss() {
    modals.classList.remove('open')
}
function hideBuyToursss() {
    modalss.classList.remove('open')
}

buyBtns.addEventListener('click', showBuyTours)
buyBtnss.addEventListener('click', showBuyTourss)
buyBtnsss.addEventListener('click', showBuyToursss)

//Nghe hành vi click vào button close
modalClose.addEventListener('click', hideBuyTours)
modalClosee.addEventListener('click', hideBuyTourss)
modalCloseee.addEventListener('click', hideBuyToursss)

modal.addEventListener('click', hideBuyTours)
modals.addEventListener('click', hideBuyTourss)
modalss.addEventListener('click', hideBuyToursss)

modalContainer.addEventListener('click', function (event) {
    event.stopPropagation()
})
modalContainerr.addEventListener('click', function (event) {
    event.stopPropagation()
})
modalContainerrr.addEventListener('click', function (event) {
    event.stopPropagation()
})

// ---------------------------------------------------------------
var header = document.getElementById('header');
var mobileMenu = document.getElementById('mobile-menu');
var headerHeight = header.clientHeight;

//Đóng mở mobile menu
mobileMenu.onclick = function(){
    var isClosed = header.clientHeight === headerHeight;
    //alert(header.clientHeight);
    if(isClosed){
        header.style.height = 'auto';
    }else{
        header.style.height = null;
    }
}
//Tự động đóng khi chọn menu
var menuItems = document.querySelectorAll('#nav li a[href*="#"]');
for(var i = 0; i<menuItems.length; i++){
    var menuItem = menuItems[i];

    menuItem.onclick = function(event){
        var isParentMenu = this.nextElementSibling && this.nextElementSibling.classList.contains('subnav');
        if(isParentMenu){
            event.preventDefault();
        }else{
            header.style.height = null;
        }
    }
}
// ------------------------------------------------------------
// Search Button
let input = document.querySelector(".input-btn");
let btn = document.querySelector(".btn-header");
let parent = document.querySelector(".search-btn");

btn.addEventListener("click", (event) => {
    event.stopPropagation();            		// Ngăn chặn sự kiện click từ việc lan ra các phần tử cha
    parent.classList.toggle("active");
    input.focus();
});
document.addEventListener("click", (event) => {
    if (!parent.contains(event.target)) {       // Kiểm tra xem sự kiện click có xảy ra trong phạm vi của parent hay không
        parent.classList.remove("active");      // Nếu không, loại bỏ lớp "active" khỏi parent
    }
});