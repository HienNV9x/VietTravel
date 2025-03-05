//Click Hidden Button BookTour and Reserve
const buyBtns = document.querySelector('.js-buy-tour')
const buyBtnss = document.querySelector('.js-buy-tour-2')
const buyBtnsss = document.querySelector('.js-buy-tour-3')

const modal = document.querySelector('.js-modal')
const modals = document.querySelector('.js-modal-2')
const modalss = document.querySelector('.js-modal-3')

const modalClose = document.querySelector('.js-modal-close')
const modalClosee = document.querySelector('.js-modal-close-2')
const modalCloseee = document.querySelector('.js-modal-close-3')

const modalContainer = document.querySelector('.js-modal-container')
const modalContainerr = document.querySelector('.js-modal-container-2')
const modalContainerrr = document.querySelector('.js-modal-container-3')

//Hàm hiển thị modal mua vé(thêm class open vào modal)
function showBuyTours() {
    modal.classList.add('open')
}
function showBuyTourss() {
    modals.classList.add('open')
}
function showBuyToursss() {
    modalss.classList.add('open')
}

//Hàm ẩn modal mua vé (gỡ bỏ class open của modal)
function hideBuyTours() {
    modal.classList.remove('open')
}
function hideBuyTourss() {
    modals.classList.remove('open')
}
function hideBuyToursss() {
    modalss.classList.remove('open')
}

buyBtns.addEventListener('click', showBuyTours)
buyBtnss.addEventListener('click', showBuyTourss)
buyBtnsss.addEventListener('click', showBuyToursss)

//Nghe hành vi click vào button close
modalClose.addEventListener('click', hideBuyTours)
modalClosee.addEventListener('click', hideBuyTourss)
modalCloseee.addEventListener('click', hideBuyToursss)

modal.addEventListener('click', hideBuyTours)
modals.addEventListener('click', hideBuyTourss)
modalss.addEventListener('click', hideBuyToursss)

modalContainer.addEventListener('click', function (event) {
    event.stopPropagation()
})
modalContainerr.addEventListener('click', function (event) {
    event.stopPropagation()
})
modalContainerrr.addEventListener('click', function (event) {
    event.stopPropagation()
})

// ---------------------------------------------------------------
//Click display message
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
	localStorage.setItem('discount', stack.peek());             // Lưu phần tử trên cùng trong stack vào localStorage
	if(tourId == 5){
	    hideBuyTours();
		showSuccess1();
	}else if(tourId == 8){
	    hideBuyTourss();
		showSuccess2();
	}else if(tourId == 10){
	    hideBuyToursss();
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

