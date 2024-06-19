var moveApi = 'http://localhost:8080/move';						//địa chỉ lấy data
	
function startMove(){
  getMoves(renderMoves);										//thực hiện hàm getCourses. Hàm getCourse sẽ trả về callback hàm renderCourses
}
startMove();

//Functions Get All
function getMoves(callback){					
  fetch(moveApi)												//lấy data từ db ra dưới dạng json
    .then(function(response){					  				//fetch mặc định sử dụng phương thức GET
      return response.json();									//dữ liệu đc chuyển thành 1 mảng các đối tượng json
    })
    .then(callback);											//thực hiện renderCourses
}

//Xóa dữ liệu trong database
function handleDeleteMove(id) {
    var options = {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
        // Không cần gửi body cho DELETE cá nhân
    };
    fetch(moveApi + '/' + id, options)
       .then(function(response) {
           if (!response.ok) {
               throw new Error('Failed to delete');
           }
           var moveItems = document.querySelectorAll('.move-item-' + id);
           moveItems.forEach(function(moveItem) {
               moveItem.remove();
           });
       })
       .catch(function(error) {
           console.error('Error:', error);
       });
}

//Update dữ liệu vào db
function update(id, data, callback) {
	fetch(moveApi + '/' + id, {
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
function renderMoves(data){											//courses là 1 mảng các đối tượng json đc lấy ra từ .then trc đó
  var listMovesBlock = document.querySelector('#list-moves');		//lấy phần tử ul trong html
  var moves = data.listResult; 										// Truy cập vào mảng listResult
  var html_move = moves.map(function(move){							//sd map() để thay đổi giá trị các phần tử trong mảng
  
            //Sử dụng replace để thay thế dấu chấm bằng dấu chấm và thẻ <br>
            var touristCarFormatted = move.touristCar.replace(/\.\s/g, '.<br>');
            var carFormatted = move.car.replace(/\.\s/g, '.<br>');
            var motorbikeFormatted = move.motorbike.replace(/\.\s/g, '.<br>');

	  						return `<tr class="move-item-${move.id}">
                                        <td data-label="Transports Name">
                                            <div class="product-name">
                                                <div class="product-content">
                                                    <div>TOURIST CAR</div>
                                                        <p id="touristCar-${move.id}">${touristCarFormatted}</p>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr class="move-item-${move.id}">
                                        <td data-label="Transports Name">
                                            <div class="product-name">
                                                <div class="product-content">
                                                    <div>CAR</div>
                                                        <p id="car-${move.id}">${carFormatted}</p>
                                                </div>
                                                <div class="product-update">
                                                    <button data-bs-toggle="modal" data-bs-target="#user-login" onclick="handleUpdateMove(${move.id})">
                                                        <svg viewBox="0 0 24 24" width="22" height="22">
                                                            <path
                                                                d="m24,12c0,6.617-5.383,12-12,12-2.704,0-5.298-.939-7.389-2.573l-2.573,2.573v-6.021c0-.527.428-.954.955-.955h6.021l-2.265,2.265c1.516,1.09,3.348,1.711,5.251,1.711,4.963,0,9-4.038,9-9h3ZM12,3c1.911,0,3.741.618,5.255,1.707l-2.269,2.269h5.83c.633,0,1.146-.513,1.146-1.146V0l-2.573,2.573c-2.088-1.635-4.675-2.573-7.389-2.573C5.383,0,0,5.383,0,12h3C3,7.038,7.038,3,12,3Zm5.948,7.305l-1.53.882c.049.265.082.535.082.814s-.034.549-.082.814l1.53.882-1.498,2.6-1.543-.889c-.413.353-.885.631-1.407.817v1.776h-3v-1.776c-.522-.186-.994-.464-1.407-.818l-1.543.889-1.497-2.6,1.53-.881c-.049-.265-.082-.535-.082-.814s.034-.549.082-.814l-1.53-.881,1.497-2.6,1.543.889c.413-.353.885-.632,1.407-.818v-1.776h3v1.776c.522.186.994.464,1.407.817l1.543-.889,1.498,2.6Zm-4.448,1.695c0-.827-.673-1.5-1.5-1.5s-1.5.673-1.5,1.5.673,1.5,1.5,1.5,1.5-.673,1.5-1.5Z" />
                                                        </svg>
                                                    </button>
                                                </div>
                                                <div class="product-delete">
                                                    <button onclick="handleDeleteMove(${move.id})">
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
                                    </tr>
                                    <tr class="move-item-${move.id}">
                                        <td data-label="Transports Name">
                                            <div class="product-name">
                                                <div class="product-content">
                                                    <div>MOTORBIKE</div>
                                                        <p id="motorbike-${move.id}">${motorbikeFormatted}</p>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>`
  });
  listMovesBlock.innerHTML = html_move.join('');						//đưa mảng sau khi thay đổi sang dạng chuỗi vào thẻ ul trong html 
}

//Reset các trường input về giá trị rỗng
function resetForm() {
  document.getElementById('1').value = '';
  document.getElementById('12').value = '';
  document.getElementById('123').value = '';
}

//Lấy dữ liệu theo id từ db truyền vào hàm update
function handleUpdateMove(id) {    
    // Lấy thông tin phòng từ API
    fetch(moveApi + '/' + id)
        .then(response => response.json())
        .then(move => {
            // Thiết lập các giá trị form
            document.querySelector('input[name="touristCar"]').value = move.touristCar;
            document.querySelector('input[name="car"]').value = move.car;
            document.querySelector('input[name="motorbike"]').value = move.motorbike;         
        });
	
	var updateBtn = document.querySelector('#update');
	updateBtn.onclick = function() {
		event.preventDefault();  									// Ngăn chặn hành động mặc định của form
		var touristCar = document.querySelector('input[name="touristCar"]').value;
		var car = document.querySelector('input[name="car"]').value;
		var motorbike = document.querySelector('input[name="motorbike"]').value;
		var formData = {				   							//tạo đối tượng có dạng title, shortDescription, content, price dùng để truyền vào db
       		touristCar: touristCar,
       		car: car,
       		motorbike: motorbike,
     	};
 		update(id, formData, function(){
			 getMoves(renderMoves);
			 resetForm();											//chạy hàm reset các trường input
		});
	}
}

