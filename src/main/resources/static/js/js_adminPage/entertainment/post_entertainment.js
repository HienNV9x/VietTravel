/*Restful API*/
var intertainmentApi = 'http://localhost:8080/intertainment';		//địa chỉ lấy data
	
function start(){
  handleCreateForm();												//thực hiện hàm handleCreateForm
}
start();

//Tạo dữ liệu mới trong database
function createCourse(data, callback){			  	
  var options = {													//tạo thông tin truyền vào fetch
    method: 'POST',						  							//phương thức của fetch là POST
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data)					  					//nội dung POST là data
  };
  fetch(intertainmentApi, options)									//truyền thông tin được tạo ở trên vào API courseApi
    .then(function(response) {					
       return response.json();										//tạo đối tượng dạng json vào db qua API 
    });
}

// Hàm hiển thị thông báo lên giao diện người dùng
function displayMessage(text, isSuccess) {
    var messageDiv = document.getElementById('message');
    var errorDiv = document.getElementById('error');
    if (isSuccess) {
        if (messageDiv) {
            messageDiv.innerText = "Uploaded information successfully!";
            messageDiv.style.display = 'block';
        }
        if (errorDiv) {
            errorDiv.style.display = 'none';
        }
    } else {
        if (errorDiv) {
            errorDiv.innerText = text;
            errorDiv.style.display = 'block';
        }
        if (messageDiv) {
            messageDiv.style.display = 'none';
        }
    }
}

//Hàm Post File Image vào Folder và Database + Lấy Id bản ghi ảnh vừa Post để Post cùng Cuisine
function uploadImagesAndCreateCourse(formData) {
    var uploadForm = document.getElementById('uploadForm');
    var inputFiles = document.getElementById('input-file');
    var formDataImages = new FormData(uploadForm);    
    fetch(uploadForm.action, {
        method: 'POST',
        body: formDataImages
    })
    .then(response => response.json())  		 					//Giả định rằng phản hồi là JSON
    .then(data => {
        if (data.message.includes("Uploaded information successfully!")) {
            displayMessage(data.message, true);
            // Thêm imageId vào formData
            formData.imageId = data.imageId;
            createCourse(formData, resetForm());
        } else {
            displayMessage("Failed to upload images or not exactly 5 images", false);
        }
        inputFiles.value = '';
    })
    .catch(error => {
        console.error('Failed to upload images or not exactly 5 images', error);
        displayMessage('Failed to upload images or not exactly 5 images', false);
        inputFiles.value = '';
    }); 
}

//Lấy dữ liệu từ Form ở HTML để đưa vào function createCourse()
function handleCreateForm(){ 
  var createBtn = document.querySelector('#create');				//lấy phần tử button trong html
  
  createBtn.onclick = function(event){	
	 event.preventDefault();										//khi click vào phần tử button thì
     var titleInter = document.querySelector('input[name="titleInter"]').value;			//lấy giá trị phần tử input có tên là name và gán vào biến title
     var contentInter = document.querySelector('input[name="contentInter"]').value;
     var locationInter = document.querySelector('input[name="locationInter"]').value;	//lấy giá trị phần tử input có tên là age và gán vào biến content
	 var categoryCode = document.querySelector('select[name="categoryCode"]').value;
	 
     var formData = {												//tạo đối tượng có dạng title, content dùng để truyền vào db
       titleInter: titleInter,
       contentInter: contentInter,
       locationInter: locationInter,
       categoryCode: categoryCode
     };
     
    uploadImagesAndCreateCourse(formData);
  }
}

//Reset các trường input về giá trị rỗng
function resetForm() {
  document.getElementById('1').value = '';
  document.getElementById('12').value = '';
  document.getElementById('123').value = '';
  document.getElementById('1234').value = '';
}