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

// ------------------------------------------------------------
//Tab Header Underline
const tabs = document.querySelectorAll(".tab-item");
const tabActive = document.querySelector(".tab-item.active");
const line = document.querySelector(".tabs .line");

function updateLinePosition(element) {
    const aElement = element.querySelector("a");                                        // Lấy thẻ <a> bên trong tab
    const rect = aElement.getBoundingClientRect();                                      // Lấy tọa độ và kích thước của thẻ <a>
    const navRect = document.querySelector("#nav").getBoundingClientRect();             // Lấy tọa độ của container chứa tất cả tab

    // Lấy giá trị padding từ CSS của thẻ <a>
    const style = window.getComputedStyle(aElement);
    const paddingLeft = parseFloat(style.paddingLeft);                                  // Padding bên trái
    const paddingRight = parseFloat(style.paddingRight);                                // Padding bên phải

    line.style.left = rect.left - navRect.left + paddingLeft + "px";                    // Di chuyển dòng đến vị trí của thẻ <a> (bù trừ padding)
    line.style.width = rect.width - paddingLeft - paddingRight + "px";                  // Điều chỉnh chiều rộng của dòng để khớp với nội dung thẻ <a>
}

// Cập nhật vị trí cho tab active ban đầu
requestIdleCallback(() => updateLinePosition(tabActive));

// Lắng nghe sự kiện click và cập nhật line
tabs.forEach((tab) => {
    tab.onclick = function () {
        document.querySelector(".tab-item.active").classList.remove("active");
        this.classList.add("active");
        updateLinePosition(this);
    };
});
// ------------------------------------------------------------

// Tab Header Navigation
document.addEventListener("DOMContentLoaded", function () {
    const navLinks = document.querySelectorAll('#nav a[href^="#"]');                    // Lấy tất cả các liên kết trong menu điều hướng
    navLinks.forEach(link => {
        link.addEventListener("click", function (e) {
            e.preventDefault();
            const targetId = this.getAttribute("href").substring(1);
            if (targetId === "") {
                window.scrollTo({                                                       // Nếu liên kết là "#", cuộn lên đầu trang
                    top: 0,
                    behavior: "smooth"                                                  // Cuộn mượt mà
                });
            } else {
                const targetElement = document.getElementById(targetId);
                if (targetElement) {
                    const headerHeight = document.querySelector(".tabs").offsetHeight;   // Tính toán vị trí mục tiêu cộng thêm khoảng trống
                    const offsetPosition = targetElement.offsetTop - headerHeight;
                    window.scrollTo({                                                    // Cuộn đến vị trí đã tính toán
                        top: offsetPosition,
                        behavior: "smooth"
                    });
                }
            }
        });
    });
});
