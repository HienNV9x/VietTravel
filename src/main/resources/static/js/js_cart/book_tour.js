var bookTour1 = document.getElementById('btn-book-1');
var bookTour2 = document.getElementById('btn-book-2');
var bookTour3 = document.getElementById('btn-book-3');

class Stack {
	constructor() {
		this.items = [];
	}
	
	//Thêm 1 phần tử sau khi Click vào ngăn xếp
	push(element) {
		this.items.push(element);
	}
	
	//Trả về phần tử trên cùng của ngăn xếp mà không loại bỏ nó
	peek() {
		if(this.isEmpty()) {
			return "Stack is empty";
		}
		return this.items[this.items.length-1];
	}
	
	// Kiểm tra xem ngăn xếp có rỗng hay không
	isEmpty() {
		return this.items.length === 0;
	}
	
	//Xóa tất cả các phần tử trong ngăn xếp
	clear() {
		this.items = [];
	}
}

//Sử dụng lớp Stack
let stack = new Stack();

// Thêm các phần tử vào ngăn xếp và cập nhật discount
function bookTour(tourId) {
	stack.push(tourId);
	//console.log(stack.peek());
	localStorage.setItem('discount', stack.peek()); // Lưu phần tử trên cùng trong stack vào localStorage
	//console.log("Front index.html => discount: " + stack.peek());
	if(tourId == 5){
		showSuccess1();
	}else if(tourId == 8){
		showSuccess2();
	}else if(tourId == 10){
		showSuccess3();
	}
}

// Thêm các sự kiện click cho các nút
bookTour1.addEventListener('click', function() {
	fetch('/api/userId')
        .then(response => {
            if (!response.ok) {
                throw new Error('User not authenticated');
            }
            return response.json();
        })
        .then(() => {
            bookTour(5);
        })
        .catch(error => {
            console.error('Error fetching user ID:', error);
            showErrorToast();
        });
});

bookTour2.addEventListener('click', function() {
	fetch('/api/userId')
        .then(response => {
            if (!response.ok) {
                throw new Error('User not authenticated');
            }
            return response.json();
        })
        .then(() => {
            bookTour(8);
        })
        .catch(error => {
            console.error('Error fetching user ID:', error);
            showErrorToast();
        });
});

bookTour3.addEventListener('click', function() {
	fetch('/api/userId')
        .then(response => {
            if (!response.ok) {
                throw new Error('User not authenticated');
            }
            return response.json();
        })
        .then(() => {
            bookTour(10);
        })
        .catch(error => {
            console.error('Error fetching user ID:', error);
            showErrorToast();
        });
});

