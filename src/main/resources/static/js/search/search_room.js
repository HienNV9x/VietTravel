// Lấy từ khóa từ localStorage và hiển thị trong thẻ h2, span
const keyword = localStorage.getItem('lastSearch');
if (keyword) {
  const formattedKeyword = keyword.split(' ').map(word => word.charAt(0).toUpperCase() + word.slice(1)).join(' ');
  document.getElementById('searchKeyword').textContent = formattedKeyword;
  document.getElementById('searchKeyword_sub').textContent = formattedKeyword;
}

//Chức năng Tìm kiếm + Phân trang
var courseApi = 'http://localhost:8080/room';
let thisPage = 1;
let limit = 4;
let currentSearch = ''; 											// Thêm biến này để lưu từ khóa tìm kiếm

function getCoursesSearch(callback, search = ''){
	currentSearch = search; 										// Cập nhật từ khóa tìm kiếm hiện tại         
    fetch(courseApi + '?search=' + encodeURIComponent(search))           
        .then(function(response){                      
            return response.json();                     
        })
        .then(callback);                               
}

function renderCoursesSearch(data){                   
    var listSearch = document.querySelector('#slider-hotel');
    var coursesSearch = data.listResult; 
    document.getElementById('quantity_room').textContent = coursesSearch.length;	// Cập nhật số lượng phòng
    var htmls = coursesSearch.map(function(course, index){
		
		var roomPointDisplay = course.roomPoint && course.roomPoint >= 4.0 ? course.roomPoint.toFixed(2) : 'New'; // Kiểm tra và cập nhật giá trị hiển thị cho roomPoint
  		var voteDisplay = ""; 										 // Chuẩn bị chuỗi rỗng để chứa thông tin vote
  		var vote = 0;
        if (roomPointDisplay >= 4.00) {
            vote = Math.round((roomPointDisplay - 4.00) / 0.05) + 1; // Trừ đi 4.00 rồi chia cho 0.05 và cuối cùng cộng thêm 1 để bắt đầu từ 1 chứ không phải từ 0
        } else {
            vote = 0;
        }
        if(roomPointDisplay != 'New'){
	  	    roomPointDisplay = parseFloat(roomPointDisplay);
	  	    voteDisplay = `(${vote})`; 								// Thêm thông tin vote nếu roomPointDisplay khác 'New'
	    }
		
        return `
			<div class="hotel hotel-hover course-item-${course.id}" data-id="${course.id}" style="display: ${index >= limit * (thisPage - 1) && index < limit * thisPage ? 'block' : 'none'};">
                <div class="hotel-image">
                    <img src="${course.imageUrl}" alt="">
                    <div class="heart-hotel" onmouseover="showNotification(this)" onmouseout="hideNotification(this)">
                    	<button class="heart-btn" onclick="handleLikeButton(${course.id})">
                    		<i class="fa-solid fa-heart heart-icon" onclick="toggleColor(this)"></i>
                    	</button>  
                        <div class="notification-heart">Like</div>
                    </div>
                </div>
                <div class="hotel-content">
                  <div class="room-content room-city room-truncate">
                    <span class="room-info truncate-text-sub" id="title-${course.id}">${course.title}</span>
                    <span class="star">
                      <i class="star-icon fa-solid fa-star"></i><span id="roomPoint-${course.id}">${roomPointDisplay}${voteDisplay}</span>
                    </span>
                  </div>
                  <div class="room-content room-properties">
                    <p id="shortDescription-${course.id}" class="truncate-text">${course.shortDescription}</p>
                    <p id="roomService-${course.id}" class="truncate-text">${course.roomService}</p>
                  </div>
                  <div class="room-content room-price">
                    <span class="room-info" id="price-${course.id}">$${course.price}</span> night
                  </div>
                </div>
            </div>
			`;
    });
    listSearch.innerHTML = htmls.join('');
    listPage(coursesSearch.length);       
}

function listPage(length){											 //Tính toán hiển thị 4 page 1 lần
    let count = Math.ceil(length / limit);
    let listPageElement = document.querySelector('.listPage');
    listPageElement.innerHTML = '';

    // Xác định trang bắt đầu và trang kết thúc
    let startPage = Math.max(thisPage - 2, 1);
    let endPage = Math.min(startPage + 4, count);

    // Điều chỉnh trang bắt đầu nếu số trang hiển thị ít hơn 5
    if (endPage - startPage < 4) {
        startPage = Math.max(endPage - 4, 1);
    }

    if(thisPage != 1){
        let prev = document.createElement('li');
        prev.innerText = '<';
        prev.onclick = function() { changePage(thisPage - 1); };
        listPageElement.appendChild(prev);
    }

    for(let i = startPage; i <= endPage; i++){
        let newPage = document.createElement('li');
        newPage.innerText = i;
        if(i == thisPage){
            newPage.classList.add('active');
        }
        newPage.onclick = function() { changePage(i); };
        listPageElement.appendChild(newPage);
    }

    if(thisPage != count && count != 0){
        let next = document.createElement('li');
        next.innerText = '>';
        next.onclick = function() { changePage(thisPage + 1); };
        listPageElement.appendChild(next);
    }
}

function changePage(i){
    thisPage = i;
    getCoursesSearch(renderCoursesSearch, currentSearch); 			// Sử dụng từ khóa tìm kiếm hiện tại khi thay đổi trang
}
getCoursesSearch(renderCoursesSearch, keyword);

//Begin: Search + Enter at <input>
document.getElementById('search-input').addEventListener('keydown', function(event) {
	if (event.key === 'Enter') {
		handleSearch();
	}
});

function handleSearch() {
	const keywords = ["ha giang", "ha noi", "ha long", "ninh binh", "da nang", "hoi an", "da lat", "ho chi minh city", "phu quoc", "ca mau"];
	const searchInput = document.getElementById('search-input').value.toLowerCase();

	if (searchInput) {
		const formattedKeyword_sub = searchInput.split(' ').map(word => word.charAt(0).toUpperCase() + word.slice(1)).join(' ');
		if (keywords.includes(searchInput)) {
			document.getElementById('searchKeyword').textContent = formattedKeyword_sub;
			document.getElementById('searchKeyword_sub').textContent = formattedKeyword_sub;
			thisPage = 1; 											// Đặt lại giá trị của thisPage về 1
			getCoursesSearch(renderCoursesSearch, searchInput);
		} else {
			showWarningToast_3();
		}
	}

	document.getElementById('search-input').value = '';
}
//End: Search + Enter at <input>

// Search Button
let input = document.querySelector(".input-btn");
let btn = document.querySelector(".btn-header");
let parent = document.querySelector(".search-btn");

btn.addEventListener("click", (event) => {
    event.stopPropagation();            							// Ngăn chặn sự kiện click từ việc lan ra các phần tử cha
    parent.classList.toggle("active");
    input.focus();
});
document.addEventListener("click", (event) => {
    if (!parent.contains(event.target)) {       					// Kiểm tra xem sự kiện click có xảy ra trong phạm vi của parent hay không
        parent.classList.remove("active");      					// Nếu không, loại bỏ lớp "active" khỏi parent
    }
});

//Like phòng
function showNotification(element) {
    var notification = element.querySelector(".notification-heart");
    notification.style.display = "block";
}
function hideNotification(element) {
    var notification = element.querySelector(".notification-heart");
    notification.style.display = "none";
}
function toggleColor(element) {
    var currentColor = element.style.color;
    if (currentColor === 'rgb(255, 255, 255)' || currentColor === '' || currentColor === 'white') {
        element.style.color = 'red'; // Red
    } else {
        element.style.color = '#fff'; // White
    }
}
function toggleColor(element) {
    element.classList.toggle("heart-icon-active");
}

//Scroll Top Button Click 
(function () {
    "use strict";
    /** Easy selector helper function */
    const select = (el, all = false) => {
        el = el.trim()
        if (all) {
            return [...document.querySelectorAll(el)]
        } else {
            return document.querySelector(el)
        }
    }

    /** Easy event listener function */
    const on = (type, el, listener, all = false) => {
        let selectEl = select(el, all)
        if (selectEl) {
            if (all) {
                selectEl.forEach(e => e.addEventListener(type, listener))
            } else {
                selectEl.addEventListener(type, listener)
            }
        }
    }

    /** Easy on scroll event listener */
    const onscroll = (el, listener) => {
        el.addEventListener('scroll', listener)
    }

    /** Scrolls to an element with header offset */
    const scrollto = (el) => {
        let header = select('#header')
        let offset = header.offsetHeight

        if (!header.classList.contains('header-scrolled')) {
            offset -= 20
        }

        let elementPos = select(el).offsetTop
        window.scrollTo({
            top: elementPos - offset,
            behavior: 'smooth'
        })
    }

    /** Toggle .header-scrolled class to #header when page is scrolled */
    let selectHeader = select('#header')
    if (selectHeader) {
        const headerScrolled = () => {
            if (window.scrollY > 100) {
                selectHeader.classList.add('header-scrolled')
            } else {
                selectHeader.classList.remove('header-scrolled')
            }
        }
        window.addEventListener('load', headerScrolled)
        onscroll(document, headerScrolled)
    }

    /** Back to top button */
    let backtotop = select('.back-to-top')
    if (backtotop) {
        const toggleBacktotop = () => {
            if (window.scrollY > 100) {
                backtotop.classList.add('active')
            } else {
                backtotop.classList.remove('active')
            }
        }
        window.addEventListener('load', toggleBacktotop)
        onscroll(document, toggleBacktotop)
    }
})()

// Hiển thị số lượng sản phẩm ra giao diện
document.addEventListener('DOMContentLoaded', function() {
    updateNumberOfProductsDisplay();
});