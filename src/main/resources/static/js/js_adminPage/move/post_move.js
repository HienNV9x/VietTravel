var moveApi = 'http://localhost:8080/move';					//địa chỉ lấy data
	
function startMove(){
  handleCreateForm();										//thực hiện hàm handleCreateForm
}
startMove();

//Tạo dữ liệu mới trong database
function createMove(data, callback){			  	
  var options = {											//tạo thông tin truyền vào fetch
    method: 'POST',						  					//phương thức của fetch là POST
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data)					  			//nội dung POST là data
  };
  fetch(moveApi, options)									//truyền thông tin được tạo ở trên vào API courseApi
    .then(function(response) {					
       return response.json();								//tạo đối tượng dạng json vào db qua API 
    })
    .then(callback);										//thực hiện hàm getCourses(renderCourses); => lấy dữ liệu truyền vào db và render ra giao diện
}

//Lấy dữ liệu từ Form ở HTML để đưa vào function createCuisine()
function handleCreateForm(){
  var createBtn = document.querySelector('#create');		//lấy phần tử button trong html 
  
  createBtn.onclick = function(){							//khi click vào phần tử button thì
     var touristCar = document.querySelector('input[name="touristCar"]').value;			//lấy giá trị phần tử input có tên là name và gán vào biến title
     var car = document.querySelector('input[name="car"]').value;
     var motorbike = document.querySelector('input[name="motorbike"]').value;			//lấy giá trị phần tử input có tên là age và gán vào biến content
	 
     var formData = {										//tạo đối tượng có dạng title, content dùng để truyền vào db
       touristCar: touristCar,
       car: car,
       motorbike: motorbike
     };
	
     createMove(formData, resetForm());
  }
}

//Reset các trường input về giá trị rỗng
function resetForm() {
  document.getElementById('1').value = '';
  document.getElementById('12').value = '';
  document.getElementById('123').value = '';
}