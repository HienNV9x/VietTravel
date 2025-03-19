//Lấy userId qua API và đưa vào localStorage
function fetchUserIdAndSave() {
    fetch('/api/userId')
        .then(response => {
            if (!response.ok) {
                if (response.status === 401) {								//User đã đăng nhập
                    resetCart(); 
                    return null;										
                }
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(newUserId => {												//User đã đăng nhập
            if (newUserId !== null) {
                const currentUserId = localStorage.getItem('userId');
                if (currentUserId !== newUserId.toString()) {
                    localStorage.setItem('userId', newUserId); 		
                    resetCart(); 									
                }
            }
        })
        .catch(error => console.error('Error fetching user ID:', error));
}

function resetCart() {														// Xóa hoặc reset giỏ hàng
   localStorage.removeItem('cart');											// Reset mảng sản phẩm trong giỏ hàng
   products = []; 										
   
   localStorage.removeItem('food');
   cuisines = []; 
   
   localStorage.removeItem('inter');
   intertainments = []; 
   
   // Xóa các thuộc tính của Object Move
   localStorage.removeItem('touristCar');
   localStorage.removeItem('car');
   localStorage.removeItem('motorbike');
}

fetchUserIdAndSave();
