var courseApi = 'http://localhost:8080/room';	
	
//Lấy id room từ URL khi Click thẻ Room
function getCourseIdFromUrl() {
    const queryParams = new URLSearchParams(window.location.search);
    return queryParams.get('id');
}

//Functions Get Room theo id
function getCourses(callback){	
  const courseId = getCourseIdFromUrl();			
  fetch(`${courseApi}/${courseId}`)									
    .then(function(response){					  	
      return response.json();						
    })
    .then(callback);							
}

//Render dữ liệu ra view
function renderCourses(course){					
  var room_shortDescription = document.getElementById('room_shortDecription');	
  var room_service = document.getElementById('room_service');
  var room_price = document.getElementById('room_price');
  var room_content = document.getElementById('room_content');
  var room_image = document.getElementById('room_image');
  
  room_shortDescription.innerHTML = course.shortDescription;
  room_service.innerHTML = course.roomService;
  room_price.innerHTML = course.price;
  room_content.innerHTML = course.content;
  
  // Render 5 ảnh từ danh sách imageUrls
  if (course.imageUrls && course.imageUrls.length >= 5) {
      for (var i = 0; i < 5; i++) {
          var imgElement = room_image.children[i].querySelector('img');
          if (imgElement) {
              imgElement.src = course.imageUrls[i];
          }
      }
  } else {
      console.error('Not enough images to display');
  }
  
  // Thêm sự kiện click vào button "Book Room"
  var addToCartButton = document.getElementById('addToCartButton');
  addToCartButton.addEventListener('click', function(event) {
    event.preventDefault();	 							// Ngăn chặn hành động mặc định của thẻ <a>
    fetchUserIdAndSave(course); 						// Gọi hàm kiểm tra user và thêm vào giỏ hàng
  });	 
}

function start(){
  getCourses(renderCourses);					   
}
start();

//Thêm sản phẩm vào localStorage để thêm vào giỏ hàng
function addToCart(course) {
  let cart = JSON.parse(localStorage.getItem('cart')) || [];
  let found = cart.find((c) => c.id === course.id);
  if (found) {
    //alert('The room is already in the cart');
	showWarningToast();
  } else {
    cart.push(course);
    localStorage.setItem('cart', JSON.stringify(cart));
    //alert('Room added to cart');
    showSuccessToast();		
    // Tính toán lại số lượng sản phẩm và cập nhật hiển thị
    calculateNumberOfProducts();
    updateNumberOfProductsDisplay();
  }
  localStorage.setItem('cart', JSON.stringify(cart));
  //alert('Product added to cart!');
}

//Lấy userId qua API và đưa vào localStorage
function fetchUserIdAndSave(course) {
    fetch('/api/userId')
        .then(response => {
            if (!response.ok) {
                throw new Error('User not authenticated');
            }
            return response.json();
        })
        .then(newUserId => {
            const currentUserId = localStorage.getItem('userId');
            if (currentUserId !== newUserId.toString()) {
                localStorage.setItem('userId', newUserId); 		// Cập nhật userId mới
                resetCart(); 									// Gọi hàm resetCart để xóa giỏ hàng hiện tại
            }
            addToCart(course); 									// Thực hiện hàm addToCart nếu user đã đăng nhập
        })
        .catch(error => {
            console.error('Error fetching user ID:', error);
            showErrorToast();  
        });
}

function resetCart() {											// Xóa hoặc reset giỏ hàng
    localStorage.removeItem('cart');
    products = []; 												// Reset mảng sản phẩm trong giỏ hàng
}

//==================================================COMMENT============================================================
var courseApiComment = 'http://localhost:8080/comment';	

function startComment() {
	getComment(renderComment);						
	handleCreateComment();									
}
startComment();

//Functions Get theo roomId
function getComment(callback) {
    const roomId = getCourseIdFromUrl(); 						// Lấy roomId từ URL
    fetch(`${courseApiComment}/room/${roomId}`) 				// Lấy comment theo roomId
        .then(function(response) {
            return response.json();
        })
        .then(callback);
}

// Tạo dữ liệu mới trong database
function createComment(data, callback) {
    fetch('/api/userId') 										// Gửi request đến backend để lấy userId
        .then(response => {
            if (!response.ok) {
                throw new Error('User not authenticated');
            }
            return response.json();
        })
        .then(userId => {
			console.log(userId);
            data.userId = userId; 								// Gán userId vào dữ liệu trước khi gửi lên server
            console.log(data.userId);
            var options = {
                method: 'POST',
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(data)
            };
            return fetch(courseApiComment, options); 			// Gửi request tạo comment với userId đã được lấy
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to create comment');
            }
            return response.json();
        })
        .then(callback)
        .catch(error => {
            console.error('Error creating comment:', error);
            showErrorToast();
        });
}

//Xóa dữ liệu trong databse và giao diện dựa trên user đăng dữ liệu
function handleDeleteComment(id) {
    var options = {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
    };
    fetch(courseApiComment + '/' + id, options)
        .then(function(response) {
            if (response.ok) {
                var commentItem = document.querySelector('.comment-item-' + id);
                if (commentItem) {                               
                    commentItem.remove();
                }
            } else {
				showWarningToastComment();
            }
    });
    fetch(courseApiComment, options)							
		.then(function(response) {
			return response.json();
	})
}

//Render dữ liệu ra view
function renderComment(data) {												//comments là 1 mảng các đối tượng json đc lấy ra từ .then trc đó
	var listCommentBlock = document.querySelector('#list-comment');			//lấy phần tử ul trong html
    var reversedData = data.reverse(); 										// Đảo ngược mảng data trước khi render để comment mới nhất được hiển thị đầu tiên
	var htmls = reversedData.map(function(comment) {	
		// Chuyển đổi ngày tháng
        var date = new Date(comment.createdDate); 							// Tạo một đối tượng Date từ chuỗi ngày tháng
        var options = { year: 'numeric', month: 'long', day: 'numeric' }; 	// Định nghĩa các tùy chọn định dạng
        var formattedDate = date.toLocaleDateString('en-US', options); 		// Định dạng ngày tháng theo yêu cầu
        
		          return `<li class="comment-item-${comment.id}">
                              <div class="single-comment-area">
                                  <div class="author-img">
                                      <img src="/img_more/testimonials/user.jpg" alt>
                                  </div>
                                  <div class="comment-content">
                                      <div class="author-name-deg">
                                          <h6 id="createdBy-${comment.id}">${comment.createdBy},</h6>
                                          <span id="createdDate-${comment.id}">${formattedDate}</span>
                                          <span class="close" onclick="handleDeleteComment(${comment.id})">&times</span>
                                      </div>
                                      <p id="text-${comment.id}">${comment.text}</p>
                                  </div>
                              </div>
                           </li>`
	});
	listCommentBlock.innerHTML = htmls.join('');						//đưa mảng sau khi thay đổi sang dạng chuỗi vào thẻ ul trong html 
}

//Nhấn Enter - Lấy dữ liệu từ Form ở HTML để đưa vào function createCourse()
function handleCreateComment() {
	var input = document.querySelector('input[name="text"]'); 			// Lấy input element
	input.addEventListener('keypress', function(e) { 					// Thêm sự kiện keypress
				    
		if (e.key === 'Enter') { 										// Kiểm tra xem phím được nhấn có phải là Enter không		
			var textInput = input.value; 								// Lấy giá trị từ input
			var formData = {
				text: textInput,
				roomId: getCourseIdFromUrl(),  							// Adding roomId to the comment
                //userId: parseInt(userId)      						// Adding userId to the comment
			};
			createComment(formData, function() {
				getComment(renderComment); 								// Gọi lại hàm để hiển thị comment mới
				input.value = "";										// Reset giá trị trong input sau khi post
			}).catch(error => {
                console.error('Error creating comment:', error);		// Giữ nguyên dữ liệu đã nhập nếu có lỗi xảy ra
            });									
		}
	});
}

// Hiển thị số lượng sản phẩm ra giao diện
document.addEventListener('DOMContentLoaded', function() {
    updateNumberOfProductsDisplay();
});



