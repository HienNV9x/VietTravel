//Begin: JS Object Room
var roomByCategoryApi = 'http://localhost:8080/room/byCategory';  	//Get theo category				

function start(){					
	var categorySelect = document.getElementById('category-select');
    var categoryCode = categorySelect.getAttribute('value');
	getCourses(renderCourses, categoryCode);
	
	//Thêm event listener để cập nhật khi categorySelect thay đổi
    categorySelect.addEventListener('change', function() {
    	categoryCode = categorySelect.getAttribute('value');
    	getCourses(renderCourses, categoryCode);
  	});
}
document.addEventListener('DOMContentLoaded', start);

// Functions Get category
function getCourses(callback, categoryCode = '') {
  fetch(roomByCategoryApi + '?categoryCode=' + categoryCode)
    .then(function(response) {
      return response.json();
    })
    .then(callback);
}

//Render dữ liệu ra view
function renderCourses(data){											//courses là 1 mảng các đối tượng json đc lấy ra từ .then trc đó
  var listCoursesBlock = document.querySelector('#slider-hotel');		//lấy phần tử ul trong html
  var courses = data.listResult; 										// Truy cập vào mảng listResult
  var htmls = courses.map(function(course){								//sd map() để thay đổi giá trị các phần tử trong mảng

  var roomPointDisplay = course.roomPoint && course.roomPoint >= 4.0 ? course.roomPoint.toFixed(2) : 'New'; // Kiểm tra và cập nhật giá trị hiển thị cho roomPoint
  var voteDisplay = ""; 												// Chuẩn bị chuỗi rỗng để chứa thông tin vote
  var vote = 0;
        if (roomPointDisplay >= 4.00) {
            vote = Math.round((roomPointDisplay - 4.00) / 0.05) + 1; 	// Trừ đi 4.00 rồi chia cho 0.05 và cuối cùng cộng thêm 1 để bắt đầu từ 1 chứ không phải từ 0
        } else {
            vote = 0;
        }
        if(roomPointDisplay != 'New'){
	  	    roomPointDisplay = parseFloat(roomPointDisplay);
	  	    voteDisplay = `(${vote})`; 									// Thêm thông tin vote nếu roomPointDisplay khác 'New'
	    }

	  return `<div class="hotel hotel-hover course-item-${course.id}" data-id="${course.id}">
                <div class="hotel-image">
                    <img src="${course.imageUrl}" alt="">
                    <div class="heart-hotel" onmouseover="showNotification(this)" onmouseout="hideNotification(this)" onclick="event.stopPropagation();">
                    	<button class="heart-btn" onclick="handleLikeButton(${course.id})">
                    		<i class="fa-solid fa-heart heart-icon" data-id="${course.id}" onclick="toggleColor(this)"></i>
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
            </div>`
  });
  listCoursesBlock.innerHTML = htmls.join('');								//đưa mảng sau khi thay đổi sang dạng chuỗi vào thẻ ul trong html 
  
  // Gọi sự kiện sau khi render dữ liệu
  document.dispatchEvent(new Event('coursesRendered'));
  
  // Gắn sự kiện click cho mỗi thẻ hotel
  var courseItems = document.querySelectorAll('.hotel-hover');
  courseItems.forEach(function(item) {
     item.addEventListener('click', function() {
         var courseId = item.getAttribute('data-id');
         window.open('/detail?id=' + courseId, '_blank');
     });
  });
  
  // Kiểm tra trạng thái like của user cho mỗi phòng
	courses.forEach(function(course) {
      fetch('/api/userId')
        .then(response => {
            if (!response.ok) {
                throw new Error('User not authenticated');
            }
            return response.json();
        })
        .then(data => {
            const userId = String(data);
            fetch(`/room/isLiked/${course.id}/${userId}`)
                .then(response => response.json())
                .then(isLiked => {
                    var heartIcon = document.querySelector(`.heart-icon[data-id="${course.id}"]`);
                    if (heartIcon) {
                        heartIcon.style.color = isLiked ? 'red' : 'white';
                    }
                    likeStatus[course.id] = isLiked;
                    localStorage.setItem('likeStatus', JSON.stringify(likeStatus));
                })
                .catch(error => console.error('Error checking like status:', error));
        })
        .catch(error => console.error('Error fetching user ID:', error));
	});
}

//Kiểm tra trạng thái Like từ LocalStorage
document.addEventListener('DOMContentLoaded', function() {
    var storedLikeStatus = localStorage.getItem('likeStatus');
    if (storedLikeStatus) {
        likeStatus = JSON.parse(storedLikeStatus);
        for (var id in likeStatus) {
            if (likeStatus.hasOwnProperty(id)) {
                var heartIcon = document.querySelector(`.heart-icon[data-id="${id}"]`);
                if (heartIcon) {
                    heartIcon.style.color = likeStatus[id] ? 'red' : 'white';
                }
            }
        }
    }
});

//Attribute roomPoint
var roomPointAPI = 'http://localhost:8080/roomPoint';						//Nếu đưa lên trên đầu thì sẽ chồng lấn API
function updateRoomPoint(id, increment, userId) {
    return fetch(roomPointAPI + '/' + id, {
        method: 'PUT',
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ increment: increment, userId: userId })
    }).then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    });
}

function handleLikeButtonSub(id, userId) {
    var courseItem = document.querySelector('.course-item-' + id);    
    var roomPointElement = courseItem.querySelector(`#roomPoint-${id}`);
    var heartIcon = courseItem.querySelector('.heart-icon');
    var roomPointText = roomPointElement.innerText.split('(')[0];
    var roomPoint = parseFloat(roomPointText);

    if (likeStatus[id]) {
        roomPoint -= 0.05;
        likeStatus[id] = false;
        updateRoomPoint(id, -0.05, userId).then(updatedPoint => {
            heartIcon.style.color = 'white';
            updateRoomPointDisplay(roomPointElement, updatedPoint);
        }).catch(error => {
            console.error('Error updating room point:', error);
            showErrorToast();
        });
    } else {
        updateRoomPoint(id, +0.05, userId).then(updatedPoint => {
            roomPoint = updatedPoint;
            likeStatus[id] = true;
            heartIcon.style.color = 'red';
            updateRoomPointDisplay(roomPointElement, updatedPoint);
        }).catch(error => {
            console.error('Error updating room point:', error);
            showErrorToast();
        });
    }
    localStorage.setItem('likeStatus', JSON.stringify(likeStatus));
}

function updateRoomPointDisplay(roomPointElement, roomPoint) {
    var vote = 0;
    if (roomPoint >= 4.00) {
        vote = Math.round((roomPoint - 4.00) / 0.05) + 1;
    }
    var voteDisplay = vote > 0 ? `(${vote})` : '';
    if (roomPoint < 4.00) {
        roomPointElement.innerHTML = `New`;
    } else if (roomPoint === 4.00) {
        roomPointElement.innerHTML = `4${voteDisplay}`;
    } else {
        roomPointElement.innerHTML = `${parseFloat(roomPoint.toFixed(2))}${voteDisplay}`;
    }
}


// Biến để theo dõi trạng thái "like" cho mỗi course
var likeStatus = {};
//Hàm xử lý chức năng khi Click Button Like
function handleLikeButton(id) {
	fetch('/api/userId')
        .then(response => {
            if (!response.ok) {
                throw new Error('User not authenticated');
            }
            return response.json();
        })
        .then(data => {
			const userId = String(data); 									// Chắc chắn chuyển thành String
			//console.log("userId: " + userId);
            handleLikeButtonSub(id, userId); 								// Thực hiện hàm handleLikeButtonSub nếu user đã đăng nhập
        })
        .catch(error => {
            console.error('Error fetching user ID:', error);
            showErrorToast();
        });
}

//Khởi tạo trạng thái Like từ localStorage khi trang được tải
document.addEventListener('DOMContentLoaded', function() {
    // Khôi phục trạng thái Like từ localStorage
    var storedLikeStatus = localStorage.getItem('likeStatus');
    if (storedLikeStatus) {
        likeStatus = JSON.parse(storedLikeStatus);
    }
});
//End: JS Object Room

//==================================================================================================================

//Begin: JS Object Move
var moveApi = 'http://localhost:8080/move';							//địa chỉ lấy data
	
function startMove(){
  getMoves(renderMoves);											//thực hiện hàm getMoves. Hàm getMoves sẽ trả về callback hàm renderMoves
}
document.addEventListener('DOMContentLoaded', startMove);
//startMove();

//Functions Get All
function getMoves(callback){					
  fetch(moveApi)													//lấy data từ db ra dưới dạng json
    .then(function(response){					  					//fetch mặc định sử dụng phương thức GET
      return response.json();										//dữ liệu đc chuyển thành 1 mảng các đối tượng json
    })
    .then(callback);												//thực hiện renderCourses
}

//Render dữ liệu ra view
function renderMoves(data){		
  var moves = data.listResult; 										// Truy cập vào listResult trước khi thực hiện forEach					
  var touristCarHTML = document.getElementById('touristCar');
  var carHTML = document.getElementById('car');
  var motorbikeHTML = document.getElementById('motorbike');    

  var touristCarFormatted = '';
  var carFormatted = '';
  var motorbikeFormatted = '';

	moves.forEach(move => {
		touristCarFormatted += `<span id="touristCar-${move.id}">${move.touristCar.replace(/\.\s/g, '.<br>')}</span>`;
		carFormatted += `<span id="car-${move.id}">${move.car.replace(/\.\s/g, '.<br>')}</span>`;
		motorbikeFormatted += `<span id="motorbike-${move.id}">${move.motorbike.replace(/\.\s/g, '.<br>')}</span>`;
	});

  touristCarHTML.innerHTML = touristCarFormatted;
  carHTML.innerHTML = carFormatted;
  motorbikeHTML.innerHTML = motorbikeFormatted;
  
    // Add event listeners to buttons
    document.getElementById('btn-touristCar').addEventListener('click', function() {
      fetch('/api/userId')											//Lấy userId qua API để kiểm tra Login
        .then(response => {
            if (!response.ok) {
                throw new Error('User not authenticated');
            }
            return response.json();
        })
        .then(() => {
            reserveMove('touristCar', 'fa-bus');
        })
        .catch(error => {
            console.error('Error fetching user ID:', error);
            showErrorToast();
        });
    });

    document.getElementById('btn-car').addEventListener('click', function() {
      fetch('/api/userId')											//Lấy userId qua API để kiểm tra Login
        .then(response => {
            if (!response.ok) {
                throw new Error('User not authenticated');
            }
            return response.json();
        })
        .then(() => {
            reserveMove('car', 'fa-car-side');
        })
        .catch(error => {
            console.error('Error fetching user ID:', error);
            showErrorToast();
        });
    });

    document.getElementById('btn-motorbike').addEventListener('click', function() {
      fetch('/api/userId')											//Lấy userId qua API để kiểm tra Login
        .then(response => {
            if (!response.ok) {
                throw new Error('User not authenticated');
            }
            return response.json();
        })
        .then(() => {
            reserveMove('motorbike', 'fa-motorcycle');
        })
        .catch(error => {
            console.error('Error fetching user ID:', error);
            showErrorToast();
        });
    });
}

// Save to local storage function
function saveToLocalStorage(key, value) {
  localStorage.setItem(key, value);									//Thêm phần tử vào LocalStorage
  calculateNumberOfProducts();										//Cập nhật số lượng phần tử tại Icon Cart
  updateNumberOfProductsDisplay();
}

// Reserve move and update icon
function reserveMove(type, icon) {
    if (localStorage.getItem(type)) {
        showWarningToast_2();
        return;
    }

    var valueToSave = document.getElementById(type).innerHTML.replace(/<br>/g, ' ').replace(/ 5% discount when booking in advance\./, '');
    saveToLocalStorage(type, valueToSave);
    var iconElement = document.querySelector(`.tab-icon.${type}`);
    if (iconElement) {
        iconElement.classList.remove('fa-bus', 'fa-car-side', 'fa-motorcycle');
        iconElement.classList.add(icon);
    }
	showSuccessToast_2();
}

//End: JS Object Move

//===================================================================================================================

//Begin: JS Object Cuisine
var cuisineByCategoryApi = 'http://localhost:8080/cuisine/byCategory';
function startCuisine(){
  var categorySelect = document.getElementById('category-select');
  var categoryCode = categorySelect.getAttribute('value');
  getCuisines(renderCuisines, categoryCode);	
  
  categorySelect.addEventListener('change', function() {
    categoryCode = categorySelect.getAttribute('value');
    getCuisines(renderCuisines, categoryCode);
  });					
}
document.addEventListener('DOMContentLoaded', startCuisine);

// Functions Get category
function getCuisines(callback, categoryCode = '') {
  fetch(cuisineByCategoryApi + '?categoryCode=' + categoryCode)
    .then(function(response) {
      return response.json();
    })
    .then(callback);
}

//Initialize Slick for each slider-roomService
function initializeSliderCuisine(sliderIdCuisine){
  $(sliderIdCuisine).slick({
    arrows: true,
    prevArrow: "<i class='ti-arrow-circle-left btn-prev-sub btn-main'></i>",
    nextArrow: "<i class='ti-arrow-circle-right btn-next-sub btn-main'></i>",
    dots: true,
    infinite: false,
    pauseOnHover: false
  });
  
  //Kiểm tra slide hiện tại và cập nhật trạng thái hiển thị của các mũi tên
  function updateArrows(sliderId) {
    var currentSlide = $(sliderId).slick('slickCurrentSlide');
    var totalSlides = $(sliderId).slick('getSlick').slideCount;
    $(sliderId).siblings('.btn-prev-sub').toggle(currentSlide !== 0);
    $(sliderId).siblings('.btn-next-sub').toggle(currentSlide !== totalSlides - 1);
  }

  // Sự kiện khi slider khởi tạo và sau khi thay đổi slide
  $(sliderIdCuisine).on('init afterChange', function (event, slick, currentSlide) {
    var sliderId = '#' + $(this).attr('id');
    updateArrows(sliderId);
  });

  // Sự kiện trước khi thay đổi slide
  $(sliderIdCuisine).on('beforeChange', function (event, slick, currentSlide, nextSlide) {
    var sliderId = '#' + $(this).attr('id');
    updateArrows(sliderId);
  });

  // Sự kiện khi di chuột vào slider
  $(sliderIdCuisine).hover(
    function () { // Khi chuột vào
      var sliderId = '#' + $(this).attr('id');
      updateArrows(sliderId);
    },
    function () { // Khi chuột ra
      $(this).siblings('.btn-prev-sub, .btn-next-sub').hide();
    }
  );

  // Ban đầu ẩn các nút
  $(sliderIdCuisine).siblings('.btn-prev-sub, .btn-next-sub').hide();
}

//Render dữ liệu ra view
function renderCuisines(data){							
  var listCuisinesBlock = document.querySelector('#slider-cuisine');		
  var cuisines = data.listResult; 						// Truy cập vào mảng listResult
  var html_cuisine = cuisines.map(function(cuisine){	//sd map() để thay đổi giá trị các phần tử trong mảng

        var imageUrls = cuisine.imageUrls.split(', ');
        var imagesHtml = imageUrls.map(function(url, index) {
            return `<div class="service hotel-image">
                        <img src="${url}" alt="Image ${index + 1}">
                    </div>`;
        }).join('');

	return `<div class="hotel cuisine-item-${cuisine.id}">    
		       <div id="slider-roomService-${cuisine.id}" class="slider-cuisine">
				 ${imagesHtml}
		       </div>
		  
		       <div class="hotel-content">
		         <div class="room-content room-city">
		           <span class="room-info truncate-text" id="titleCuisine-${cuisine.id}">${cuisine.titleCuisine}</span>
		         </div>
		         <div class="room-content room-properties">
		           <p id="contentCuisine-${cuisine.id}" class="truncate-text">${cuisine.contentCuisine}</p>
		           <p id="locationCuisine-${cuisine.id}" class="truncate-text">${cuisine.locationCuisine}</p>
		         </div>
		         <div class="btn-reser">
		           <button class="btn-reserve" onclick='addToCartCuisine(${JSON.stringify(cuisine)})'>Reserve</button>
		         </div>
		       </div>
		    </div>`
  });
  listCuisinesBlock.innerHTML = html_cuisine.join('');
  
  // Gọi sự kiện sau khi render dữ liệu
  document.dispatchEvent(new Event('cuisineRendered'));	
  
  // Initialize Slick for each slider-roomService
  cuisines.forEach(function(cuisine) {
    initializeSliderCuisine(`#slider-roomService-${cuisine.id}`);
  });
}

//Thêm sản phẩm vào localStorage để thêm vào giỏ hàng
function addToCartCuisine(cuisine) {
	fetch('/api/userId')								//Lấy userId qua API để kiểm tra Login
		.then(response => {
			if (!response.ok) {
				throw new Error('User not authenticated');
			}
			return response.json();
		})
		.then(() => {									//Nếu Login
			let food = JSON.parse(localStorage.getItem('food')) || [];
			let found = food.find((c) => c.id === cuisine.id);
			if (found) {
				showWarningToast_2();
			} else {
				food.push(cuisine);
				showSuccessToast_2();
			}
			localStorage.setItem('food', JSON.stringify(food));					//Thêm ohaanf tử vào LocalStorage
			calculateNumberOfProducts();										//Cập nhật giao diện Icon Cart
    		updateNumberOfProductsDisplay();
		})
		.catch(error => {								//Nếu chưa Login
			console.error('Error fetching user ID:', error);
			showErrorToast();
		});
}

//End: JS Object Cuisine

//==================================================================================================================

//Begin: JS Object Entertainment
var intertainmentByCategoryApi = 'http://localhost:8080/intertainment/byCategory';

function startIntertainment(){
  var categorySelect = document.getElementById('category-select');
  var categoryCode = categorySelect.getAttribute('value');
  getIntertainments(renderIntertainments, categoryCode);	
  
  categorySelect.addEventListener('change', function() {
    categoryCode = categorySelect.getAttribute('value');
    getIntertainments(renderIntertainments, categoryCode);
  });											
}
document.addEventListener('DOMContentLoaded', startIntertainment);

// Functions Get category
function getIntertainments(callback, categoryCode = '') {
  fetch(intertainmentByCategoryApi + '?categoryCode=' + categoryCode)
    .then(function(response) {
      return response.json();
    })
    .then(callback);
}

//Initialize Slick for each slider-interService
function initializeSliderIntertainment(sliderIdIntertainment){
  $(sliderIdIntertainment).slick({
    arrows: true,
    prevArrow: "<i class='ti-arrow-circle-left btn-prev-sub btn-main'></i>",
    nextArrow: "<i class='ti-arrow-circle-right btn-next-sub btn-main'></i>",
    dots: true,
    infinite: false,
    pauseOnHover: false
  });
  
  //Kiểm tra slide hiện tại và cập nhật trạng thái hiển thị của các mũi tên
  function updateArrows(sliderId) {
    var currentSlide = $(sliderId).slick('slickCurrentSlide');
    var totalSlides = $(sliderId).slick('getSlick').slideCount;
    $(sliderId).siblings('.btn-prev-sub').toggle(currentSlide !== 0);
    $(sliderId).siblings('.btn-next-sub').toggle(currentSlide !== totalSlides - 1);
  }

  // Sự kiện khi slider khởi tạo và sau khi thay đổi slide
  $(sliderIdIntertainment).on('init afterChange', function (event, slick, currentSlide) {
    var sliderId = '#' + $(this).attr('id');
    updateArrows(sliderId);
  });

  // Sự kiện trước khi thay đổi slide
  $(sliderIdIntertainment).on('beforeChange', function (event, slick, currentSlide, nextSlide) {
    var sliderId = '#' + $(this).attr('id');
    updateArrows(sliderId);
  });

  // Sự kiện khi di chuột vào slider
  $(sliderIdIntertainment).hover(
    function () { 									// Khi chuột vào
      var sliderId = '#' + $(this).attr('id');
      updateArrows(sliderId);
    },
    function () { 									// Khi chuột ra
      $(this).siblings('.btn-prev-sub, .btn-next-sub').hide();
    }
  );

  // Ban đầu ẩn các nút
  $(sliderIdIntertainment).siblings('.btn-prev-sub, .btn-next-sub').hide();
}

//Render dữ liệu ra view
function renderIntertainments(data){							
  var listIntertainmentsBlock = document.querySelector('#slider-entertainment');		
  var intertainments = data.listResult; 						
  var html_intertainment = intertainments.map(function(intertainment){	
	    var imageUrls = intertainment.imageUrls.split(', ');
        var imagesHtml = imageUrls.map(function(url, index) {
            return `<div class="service hotel-image">
                        <img src="${url}" alt="Image ${index + 1}">
                    </div>`;
        }).join('');

	 return `<div class="hotel intertainment-item-${intertainment.id}">
		       <div id="slider-interService-${intertainment.id}" class="slider-cuisine">
				 ${imagesHtml}
		       </div>
		       
		       <div class="hotel-content">
		         <div class="room-content room-city">
		           <span class="room-info truncate-text" id="titleInter-${intertainment.id}">${intertainment.titleInter}</span>
		         </div>
		         <div class="room-content room-properties">
		           <p id="contentInter-${intertainment.id}" class="truncate-text">${intertainment.contentInter}</p>
		           <p id="locationInter-${intertainment.id}" class="truncate-text">${intertainment.locationInter}</p>
		         </div>
		         <div class="btn-reser">
		           <button class="btn-reserve" onclick='addToCartInter(${JSON.stringify(intertainment)})'>Reserve</button>
		         </div>
		       </div>
		     </div>`
  });
  listIntertainmentsBlock.innerHTML = html_intertainment.join('');		//đưa mảng sau khi thay đổi sang dạng chuỗi vào thẻ ul trong html 
  
  // Gọi sự kiện sau khi render dữ liệu
  document.dispatchEvent(new Event('entertainmentRendered'));
  
  // Initialize Slick for each slider-interService
  intertainments.forEach(function(intertainment) {
    initializeSliderIntertainment(`#slider-interService-${intertainment.id}`);
  });
}

//Thêm sản phẩm vào localStorage để thêm vào giỏ hàng
function addToCartInter(intertainment) {
	fetch('/api/userId')												//Lấy userId qua API để kiểm tra Login
		.then(response => {
			if (!response.ok) {
				throw new Error('User not authenticated');
			}
			return response.json();
		})
		.then(() => {													//Nếu Login
			let inter = JSON.parse(localStorage.getItem('inter')) || [];
			let found = inter.find((c) => c.id === intertainment.id);
			if (found) {
				showWarningToast_2();
			} else {
				inter.push(intertainment);
				showSuccessToast_2();
			}
			localStorage.setItem('inter', JSON.stringify(inter));		//Thêm phần tử vào LocalStorage
			calculateNumberOfProducts();								//Cập nhật giao diện Icon Cart
    		updateNumberOfProductsDisplay();
		})
		.catch(error => {												//Nếu chưa Login
			console.error('Error fetching user ID:', error);
			showErrorToast();
		});
}

//End: JS Object Entertainment

//================================================Reset Cart============================================================

// Hiển thị số lượng sản phẩm ra giao diện
document.addEventListener('DOMContentLoaded', function() {
    updateNumberOfProductsDisplay();
});
