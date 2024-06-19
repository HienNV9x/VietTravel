var intertainmentApi = 'http://localhost:8080/intertainment';			//địa chỉ lấy data
var intertainmentByCategoryApi = 'http://localhost:8080/intertainment/byCategory';
	
function startIntertainment(){
  getIntertainments(renderIntertainments);								//thực hiện hàm getCourses. Hàm getCourse sẽ trả về callback hàm renderCourses							//thực hiện hàm handleCreateForm
}
startIntertainment();

//Functions Get All
/*function getIntertainments(callback){					
  fetch(intertainmentApi)												//lấy data từ db ra dưới dạng json
    .then(function(response){					  						//fetch mặc định sử dụng phương thức GET
      return response.json();											//dữ liệu đc chuyển thành 1 mảng các đối tượng json
    })
    .then(callback);													//thực hiện renderCourses
}*/
// Functions Get All
function getIntertainments(callback, categoryCode = '') {
  var url = intertainmentApi;
  if (categoryCode) {
    url = intertainmentByCategoryApi + '?categoryCode=' + categoryCode;
  }

  fetch(url)
    .then(function(response) {
      return response.json();
    })
    .then(callback);
}
// Handle category change
function handleCategoryChange() {
  var categorySelect = document.getElementById('category-select');
  var categoryCode = categorySelect.value;
  getIntertainments(renderIntertainments, categoryCode);
}

//Xóa dữ liệu trong database
function handleDeleteIntertainment(id) {
    var options = {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
        // Không cần gửi body cho DELETE cá nhân
    };
    fetch(intertainmentApi + '/' + id, options)
       .then(function(response) {
           if (!response.ok) {
               throw new Error('Failed to delete');
           }
           var intertainmentItem = document.querySelector('.intertainment-item-' + id);
           if (intertainmentItem) {
               intertainmentItem.remove();
           }
       })
       .catch(function(error) {
           console.error('Error:', error);
       });
}

//Update dữ liệu vào db
function update(id, data, callback) {
	fetch(intertainmentApi + '/' + id, {
		method: 'PUT',
		headers: {
			"Content-Type": "application/json"
		},
		body: JSON.stringify(data)
	})
		.then(function(response) {
			return response.json();
		})
		.then(callback);
}

//Render dữ liệu ra view
function renderIntertainments(data){									//courses là 1 mảng các đối tượng json đc lấy ra từ .then trc đó
  var listIntertainmentsBlock = document.querySelector('#list-intertainments');		//lấy phần tử ul trong html
  var intertainments = data.listResult; 								//Truy cập vào mảng listResult
  var html_intertainment = intertainments.map(function(intertainment){	//sd map() để thay đổi giá trị các phần tử trong mảng

	  return `<tr class="intertainment-item-${intertainment.id}">
                <td data-label="Tour Package">
                  <div class="product-name">
                     <div class="img">
                        <img id="custom-img" src="${intertainment.imageUrl}">
                     </div>
                     <div class="product-content">
                        <p id="titleInter-${intertainment.id}">${intertainment.titleInter}</p>
                        <p id="contentInter-${intertainment.id}">${intertainment.contentInter}</p>
                        <p id="locationInter-${intertainment.id}">${intertainment.locationInter}</p>
                        <p>
                           <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 18 18">
                              <path
                                  d="M8.99939 0C5.40484 0 2.48047 2.92437 2.48047 6.51888C2.48047 10.9798 8.31426 17.5287 8.56264 17.8053C8.79594 18.0651 9.20326 18.0646 9.43613 17.8053C9.68451 17.5287 15.5183 10.9798 15.5183 6.51888C15.5182 2.92437 12.5939 0 8.99939 0ZM8.99939 9.79871C7.19088 9.79871 5.71959 8.32739 5.71959 6.51888C5.71959 4.71037 7.19091 3.23909 8.99939 3.23909C10.8079 3.23909 12.2791 4.71041 12.2791 6.51892C12.2791 8.32743 10.8079 9.79871 8.99939 9.79871Z">
                              </path>
                           </svg> <span id="category-${intertainment.id}">${intertainment.categoryName}</span>
                        </p>
                      </div>
                      <div class="product-update">
                         <button data-bs-toggle="modal" data-bs-target="#user-login" onclick="handleUpdateIntertainment(${intertainment.id})">
                            <svg viewBox="0 0 24 24" width="22" height="22">
                               <path
                                  d="m24,12c0,6.617-5.383,12-12,12-2.704,0-5.298-.939-7.389-2.573l-2.573,2.573v-6.021c0-.527.428-.954.955-.955h6.021l-2.265,2.265c1.516,1.09,3.348,1.711,5.251,1.711,4.963,0,9-4.038,9-9h3ZM12,3c1.911,0,3.741.618,5.255,1.707l-2.269,2.269h5.83c.633,0,1.146-.513,1.146-1.146V0l-2.573,2.573c-2.088-1.635-4.675-2.573-7.389-2.573C5.383,0,0,5.383,0,12h3C3,7.038,7.038,3,12,3Zm5.948,7.305l-1.53.882c.049.265.082.535.082.814s-.034.549-.082.814l1.53.882-1.498,2.6-1.543-.889c-.413.353-.885.631-1.407.817v1.776h-3v-1.776c-.522-.186-.994-.464-1.407-.818l-1.543.889-1.497-2.6,1.53-.881c-.049-.265-.082-.535-.082-.814s.034-.549.082-.814l-1.53-.881,1.497-2.6,1.543.889c.413-.353.885-.632,1.407-.818v-1.776h3v1.776c.522.186.994.464,1.407.817l1.543-.889,1.498,2.6Zm-4.448,1.695c0-.827-.673-1.5-1.5-1.5s-1.5.673-1.5,1.5.673,1.5,1.5,1.5,1.5-.673,1.5-1.5Z" />
                            </svg>
                        </button>
                      </div>
                      <div class="product-delete">
                        <button onclick="handleDeleteIntertainment(${intertainment.id})">
                           <svg viewBox="0 0 512 512" width="22" height="22">
                              <g>
                                 <path
                                     d="M342.635,169.365c-12.493-12.501-32.754-12.507-45.255-0.014c-0.005,0.005-0.01,0.01-0.015,0.014L256,210.752   l-41.365-41.387c-12.501-12.501-32.769-12.501-45.269,0s-12.501,32.769,0,45.269L210.752,256l-41.387,41.365   c-12.501,12.501-12.501,32.769,0,45.269c12.501,12.501,32.769,12.501,45.269,0l0,0L256,301.248l41.365,41.387   c12.501,12.501,32.769,12.501,45.269,0c12.501-12.501,12.501-32.769,0-45.269L301.248,256l41.387-41.365   c12.501-12.493,12.507-32.754,0.014-45.255C342.644,169.375,342.64,169.37,342.635,169.365z" />
                                 <path
                                     d="M256,0C114.615,0,0,114.615,0,256s114.615,256,256,256s256-114.615,256-256C511.847,114.678,397.322,0.153,256,0z M256,448   c-106.039,0-192-85.961-192-192S149.961,64,256,64s192,85.961,192,192C447.882,361.99,361.99,447.882,256,448z" />
                              </g>
                           </svg>
                        </button>
                      </div>
                    </div>
                  </td>
	  		   </tr>`
  });
  listIntertainmentsBlock.innerHTML = html_intertainment.join('');		//đưa mảng sau khi thay đổi sang dạng chuỗi vào thẻ ul trong html 
}

//Reset các trường input về giá trị rỗng
function resetForm() {
  document.getElementById('1').value = '';
  document.getElementById('12').value = '';
  document.getElementById('123').value = '';
  document.getElementById('1234').value = '';
}

// Thêm logic chuyển đổi tên danh mục thành giá trị tương ứng - Sử dụng cho PUT
function convertCategoryNameToValue(name) {
    switch (name) {
        case 'Ha Giang': return 'ha-giang';
        case 'Ha Noi': return 'ha-noi';
        case 'Ha Long': return 'ha-long';
        case 'Ninh Binh': return 'ninh-binh';
        case 'Da Nang': return 'da-nang';
        case 'Hoi An': return 'hoi-an';
        case 'Da Lat': return 'da-lat';
        case 'Ho Chi Minh City': return 'hcm-city';
        case 'Phu Quoc': return 'phu-quoc';
        case 'Ca Mau': return 'ca-mau';            
        default: return '';
    }
}

//Lấy dữ liệu theo id từ db truyền vào hàm update
function handleUpdateIntertainment(id) {    
    // Lấy thông tin phòng từ API
    fetch(intertainmentApi + '/' + id)
        .then(response => response.json())
        .then(intertainment => {
            // Thiết lập các giá trị form
            document.querySelector('input[name="titleInter"]').value = intertainment.titleInter;
            document.querySelector('input[name="contentInter"]').value = intertainment.contentInter;
            document.querySelector('input[name="locationInter"]').value = intertainment.locationInter;         
            const categoryCode = convertCategoryNameToValue(intertainment.categoryName);				// Chuyển đổi từ categoryName sang categoryCode và thiết lập giá trị cho thẻ select
            document.querySelector('select[name="categoryCode"]').value = categoryCode;

        });
	
	var updateBtn = document.querySelector('#update');
	updateBtn.onclick = function() {
		event.preventDefault();  								// Ngăn chặn hành động mặc định của form
		var titleInter = document.querySelector('input[name="titleInter"]').value;
		var contentInter = document.querySelector('input[name="contentInter"]').value;
		var locationInter = document.querySelector('input[name="locationInter"]').value;
		var categoryCode = document.querySelector('select[name="categoryCode"]').value;
		var formData = {				   						//tạo đối tượng có dạng title, shortDescription, content, price dùng để truyền vào db
       		titleInter: titleInter,
       		contentInter: contentInter,
       		locationInter: locationInter,
       		categoryCode: categoryCode,
     	};
 		update(id, formData, function(){
			 getIntertainments(renderIntertainments);
			 resetForm();										//chạy hàm reset các trường input
		});
	}
}

