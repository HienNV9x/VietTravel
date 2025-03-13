//-------------------------------------------Begin: JS Render Room----------------------------------------------------

//Lấy dữ liệu sản phẩm từ localStorage và hiển thị
let products = JSON.parse(localStorage.getItem('cart')) || [];
// Tính số lượng phần tử Room
let numberOfItemsRoom = products.length;

// === TRUY CẬP VÀO CÁC THÀNH PHẦN ===
let productsEle = document.querySelector('.products');
let subTotalEl = document.querySelector('.subtotal');
let vatEl = document.querySelector('.vat');
let memberEle = document.querySelector('.member');	
let discountEle = document.querySelector('.discount');
let totalEle = document.querySelector('.totalEle');

// Render và hiển thị dữ liệu Room
function renderUI(arr) {
    productsEle.innerHTML = '';

    // Cập nhật tổng tiền
    updateTotalMoney(arr);

    for (let i = 0; i < arr.length; i++) {
        const p = arr[i];
        // Đảm bảo count được đặt về 0 nếu chưa chọn ngày
        p.count = p.count || 0;
        const totalPrice = p.price * p.count; 					//Tính tổng số tiền cho mỗi phòng
        
        // Sử dụng giá trị ngày từ `products` nếu có, nếu không thì để mặc định là `--/--/----`
        const startDate = p.startDate ? p.startDate : '--/--/----';
        const endDate = p.endDate ? p.endDate : '--/--/----';
        
        productsEle.innerHTML += `
              <tr>
                <td>
                  <div class="thumb_cart">
                    <img src="${p.imageUrl}" alt="Image">
                  </div>
                  <span class="item_cart">${p.title}</span><br>
                  <span class="item_cart_sub">$${p.price}/night</span>
                </td>
                <td>
                  <div class="numbers-row">
                    <div class="inc button_inc" data-id="${p.id}">in</div>
                    <input type="text" step="0" value="${p.count}" id="quantity-${p.id}" class="qty2 form-control" name="quantity_${p.id}" readonly onchange="changeTotalProduct(${p.id}, event)">
                    <div class="dec button_inc" data-id="${p.id}">out</div>
                  </div>
                </td>
                <td>
                  <div class="day-use">
                    <div class="day-start" id="day-start-${p.id}">${startDate}</div>
                    <div class="day-mid">-</div>
                    <div class="day-end" id="day-end-${p.id}">${endDate}</div>
                  </div>
                </td>
                <td>
                  <strong id="quantity-money-${p.id}">$${totalPrice.toFixed(2)}</strong>
                </td>
                <td class="options">
                  <a href="#" onclick="removeItem(${p.id})"><i class=" icon-trash"></i></a>
                </td>
              </tr>
   		 `;
    }
    // Cập nhật tổng tiền sau khi render
    updateTotalMoney();
}

//Cập nhật số lượng sản phẩm trong mảng products và cập nhật trong localStorage và cuối cùng cập nhật UI.
function updateProductInCart(productId, quantity) {		
    // Tìm sản phẩm theo id và cập nhật số lượng
    const productIndex = products.findIndex(p => p.id === productId);
    if (productIndex !== -1) {
        products[productIndex].count = quantity;
        //Cập nhật localStorage
        localStorage.setItem('cart', JSON.stringify(products));
        //Cập nhật UI và tổng tiền sau khi thay đổi số lượng
        renderUI(products);
        updateTotalMoney(products);
    }
}

// Cập nhật số lượng sản phẩm
function updateTotalItem(arr) {
    let total = 0;
    for (let i = 0; i < arr.length; i++) {
        total++;
    }
    return total;
}

// Remove item trong cart
function removeItem(id) {
    for (let i = 0; i < products.length; i++) {
        if (products[i].id == id) {
            products.splice(i, 1);
            break; 											// Dừng vòng lặp khi tìm thấy và xóa sản phẩm
        }
    }
    // Cập nhật lại localStorage sau khi xóa sản phẩm
    localStorage.setItem('cart', JSON.stringify(products)); // Lưu lại dữ liệu mới vào localStorage
    renderUI(products);										// Cập nhật lại UI
}

// Thay đổi số lượng sản phẩm
function changeTotalProduct(id, e) {
    for (let i = 0; i < products.length; i++) {
        if (products[i].id == id) {
            products[i].count = Number(e.target.value);
            break;                  						//Thêm break để thoát khỏi vòng lặp sau khi tìm thấy và cập nhật sản phẩm
        }
    }
    // Cập nhật UI và tổng tiền sau khi thay đổi số lượng
    renderUI(products);             						//Để cập nhật UI
    updateTotalMoney(products);     						//Để cập nhật tổng tiền dựa trên số lượng mới
}

// Cập nhật tổng tiền
var paymentTotalCrypto;
function updateTotalMoney(arr) {
    // Tính tổng tiền cart
    let totalMoney = 0;
    const moneyElements = document.querySelectorAll('[id^="quantity-money-"]');
    moneyElements.forEach(element => {
        totalMoney += parseFloat(element.innerText.replace('$', ''));
    });

    //Cập nhật tiền lên trên giao diện đã làm tròn 2 chữ số thập phân và chuyển về dạng String
    subTotalEl.innerText = "$" + totalMoney.toFixed(2);     //Làm tròn và chuyển thành chuỗi
    
    //Đọc discount từ localStorage
    let discount = localStorage.getItem('discount') || 0;
    console.log("discount fromt LocalStorage: " + discount);
    discountEle.innerText = "↓" + discount + "%";
    
    const pre_tax = totalMoney - (totalMoney*(0.05 + discount/100));        
    const vat = pre_tax * 0.1;
    vatEl.innerText = "$" + vat.toFixed(2);           		//Tính VAT, làm tròn và chuyển thành chuỗi
    const paymentTotal = pre_tax + vat;
    totalEle.innerText = "$" + paymentTotal.toFixed(2);

    const totalCrypto = document.querySelector('#total-crypto');
    const firstPaymentCrypto = document.querySelector('#crypto-payment-first');
    const secondPaymentCrypto = document.querySelector('#crypto-payment-second');
    paymentTotalCrypto = (paymentTotal/2080.90).toFixed(4);
    totalCrypto.innerText = paymentTotalCrypto;
    firstPaymentCrypto.innerText = (paymentTotalCrypto*0.2).toFixed(4);
    secondPaymentCrypto.innerText = (paymentTotalCrypto-paymentTotalCrypto*0.2).toFixed(4);
}

//Payment Crypto Currency
var cryptoPaymentApi = 'http://localhost:8080/api/smartcontract/update-room-rent';
    function createRoomRent(data){
        var options = {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
        },
        body: JSON.stringify(data)
    };
    fetch(cryptoPaymentApi, options)
        .then(response => response.json())
    }
document.querySelector('.confirm-button').addEventListener('click', function(event) {
    event.preventDefault();
    var sender = document.querySelector('input[name="crypto-sender"]').value;
    var formData = {
        roomRent: paymentTotalCrypto,
        renterAddress: sender
    };
    createRoomRent(formData);
    $.magnificPopup.close();
});

//window.onload = renderUI(products);
window.onload = function() {
    renderUI(products);
    attachDatePickers();
};

//End: JS Render Room

//=============================================Begin: JS Chọn lịch ngày In Room - Out Room==============================

  function attachDatePickers() {
    //Lặp qua mỗi phòng để thực hiện các thay đổi linh hoạt
    products.forEach((room) => {
        const roomId = room.id;
        const startDateDiv = document.getElementById(`day-start-${roomId}`);
        const endDateDiv = document.getElementById(`day-end-${roomId}`);
        const quantityInput = document.getElementById(`quantity_${roomId}`);
        const totalPriceElement = document.querySelector(`#quantity-money-${roomId}`);
        
        //Hiển thị ngày đã lưu từ localStorage
        startDateDiv.textContent = room.startDate ? room.startDate : '--/--/----';
        endDateDiv.textContent = room.endDate ? room.endDate : '--/--/----';
        
		function updateDaysCount(quantityInput) {
			const start = startDateDiv.textContent.split("/").reverse().join("-");
			const end = endDateDiv.textContent.split("/").reverse().join("-");
			const startDate = new Date(start);
			const endDate = new Date(end);
			
            //Kiểm tra xem cả ngày bắt đầu và ngày kết thúc có hợp lệ không và ngày kết thúc lớn hơn ngày bắt đầu
            if (isNaN(startDate) || isNaN(endDate) || endDate <= startDate) {
                quantityInput.value = 0;
                totalPriceElement.innerText = `$0.00`;

                //Cập nhật giá trị của p.count trong mảng products
                for (let i = 0; i < products.length; i++) {
                    if (products[i].id == roomId) {
                        products[i].count = 0;
                        products[i].startDate = null; //Thêm
                        products[i].endDate = null;   //Thêm
                        break;
                    }
                }
                updateTotalMoney(); 									// Cập nhật tổng tiền sau khi tính toán
                localStorage.setItem('cart', JSON.stringify(products)); // Cập nhật localStorage
                return;
            }
			
			const timeDiff = endDate - startDate;
			const daysDiff = Math.ceil(timeDiff / (1000 * 60 * 60 * 24));
			quantityInput.value = daysDiff >= 0 ? daysDiff : 0;
			
			// Cập nhật giá trị của p.count trong mảng products
			for (let i = 0; i < products.length; i++) {
				if (products[i].id == roomId) {
					products[i].count = daysDiff >= 0 ? daysDiff : 0;
					products[i].startDate = startDateDiv.textContent; // Thêm
                    products[i].endDate = endDateDiv.textContent;     // Thêm
					break;
				}
			}
			
			// Tính lại totalPrice sau khi đã cập nhật p.count
			const totalPrice = room.price * daysDiff; 					// Tính tổng số tiền cho mỗi phòng
			totalPriceElement.innerText = `$${totalPrice.toFixed(2)}`;
			updateTotalMoney(); 										// Cập nhật tổng tiền sau khi tính toán
			localStorage.setItem('cart', JSON.stringify(products)); 	// Cập nhật localStorage
		}

		// Trong sự kiện onChange của flatpickr
		const startPicker = flatpickr(startDateDiv, {
			enableTime: true,
			dateFormat: "d/m/Y",
			onChange: function(selectedDates, dateStr, instance) {
				startDateDiv.textContent = dateStr;
				const quantityInput = document.getElementById(`quantity-${roomId}`); // Lấy lại quantityInput
				updateDaysCount(quantityInput); 									 // Cập nhật số ngày
			}
		});

		const endPicker = flatpickr(endDateDiv, {
			enableTime: true,
			dateFormat: "d/m/Y",
			onChange: function(selectedDates, dateStr, instance) {
				endDateDiv.textContent = dateStr;
				const quantityInput = document.getElementById(`quantity-${roomId}`); // Lấy lại quantityInput
				updateDaysCount(quantityInput); 									 // Cập nhật số ngày
			}
		});
        
        // Gán sự kiện click cho các nút để mở lịch cho từng phòng
        document.querySelector(`.inc.button_inc[data-id="${roomId}"]`).addEventListener("click", function() {
            startPicker.open();
        });
        
        document.querySelector(`.dec.button_inc[data-id="${roomId}"]`).addEventListener("click", function() {
            endPicker.open();
        });
    });
}

//=========================================End: JS Render Room==========================================================
//===========================================BEGIN CUISINE===================================================================													
//Lấy id object cuisine từ LocalStorage
document.addEventListener('DOMContentLoaded', function () {
    var cuisineId = localStorage.getItem('cuisineId');
    if (cuisineId) {
        fetch(`/cuisine/${cuisineId}`)
            .then(response => response.json())
            .then(cuisine => {
                cuisines.push(cuisine);
                renderUIFOOD(cuisines);
            })
            .catch(error => console.error('Error fetching the cuisine:', error));
    }
});

//Lấy dữ liệu sản phẩm từ localStorage và hiển thị
let cuisines = JSON.parse(localStorage.getItem('food')) || [];
// Tính số lượng phần tử Cuisine
let numberOfItemsCuisine = cuisines.length;

// === TRUY CẬP VÀO CÁC THÀNH PHẦN ===
let cuisinesEle = document.querySelector('.cuisines');

// Lấy thông tin chi tiết của cuisine từ server và Render và hiển thị dữ liệu
function renderUIFOOD(arrFood) {
    cuisinesEle.innerHTML = '';

    for (let i = 0; i < arrFood.length; i++) {
        const pFood = arrFood[i];
        cuisinesEle.innerHTML += `
              <tr>
                <td colspan="3">
                  <div class="thumb_cart">
                    <img src="${pFood.imageUrl}" alt="Image">
                  </div>
                  <span class="item_cart truncate-text">${pFood.titleCuisine}</span>
                  <span class="item_cart_sub truncate-text">${pFood.locationCuisine}</span>
                </td>
                <td>
                  <strong>&#8582;10%</strong>
                </td>
                <td class="options">
                  <a href="#" onclick="removeItemFood(${pFood.id})"><i class=" icon-trash"></i></a>
                </td>
              </tr>
        `;
    }
}

// Remove item trong food
function removeItemFood(id) {
    for (let i = 0; i < cuisines.length; i++) {
        if (cuisines[i].id == id) {
            cuisines.splice(i, 1);
            break; 											// Dừng vòng lặp khi tìm thấy và xóa sản phẩm
        }
    }														// Cập nhật lại localStorage sau khi xóa sản phẩm
    localStorage.setItem('food', JSON.stringify(cuisines)); // Lưu lại dữ liệu mới vào localStorage
    renderUIFOOD(cuisines);									// Cập nhật lại UI
}	
window.onload = renderUIFOOD(cuisines);

//============================================END CUISINE===================================================================													
//============================================BEGIN INTERTAINMENT===================================================================																										

//Lấy id object intertainment từ LocalStorage
document.addEventListener('DOMContentLoaded', function () {
    var intertainmentId = localStorage.getItem('intertainmentId');
    if (intertainmentId) {
        fetch(`/intertainment/${intertainmentId}`)
            .then(response => response.json())
            .then(intertainment => {
                intertainments.push(intertainment);
                renderUIINTER(intertainments);
            })
            .catch(error => console.error('Error fetching the intertainment:', error));
    }
});

//Lấy dữ liệu sản phẩm từ localStorage và hiển thị
let intertainments = JSON.parse(localStorage.getItem('inter')) || [];
// Tính số lượng phần tử Intertainment
let numberOfItemsInter = intertainments.length;

// === TRUY CẬP VÀO CÁC THÀNH PHẦN ===
let intertainmentsEle = document.querySelector('.intertainments');

// Lấy thông tin chi tiết của intertainment từ server và Render hiển thị dữ liệu
function renderUIINTER(arrInter) {
    intertainmentsEle.innerHTML = '';

    for (let i = 0; i < arrInter.length; i++) {
        const pInter = arrInter[i];
        intertainmentsEle.innerHTML += `
              <tr>
                <td colspan="3">
                  <div class="thumb_cart">
                    <img src="${pInter.imageUrl}" alt="Image">
                  </div>
                  <span class="item_cart truncate-text">${pInter.titleInter}</span>
                  <span class="item_cart_sub truncate-text">${pInter.locationInter}</span>
                </td>
                <td>
                  <strong>&#8582;10%</strong>
                </td>
                <td class="options">
                  <a href="#" onclick="removeItemInter(${pInter.id})"><i class=" icon-trash"></i></a>
                </td>
              </tr>
        `;
    }
}

// Remove item trong food
function removeItemInter(id) {
    for (let i = 0; i < intertainments.length; i++) {
        if (intertainments[i].id == id) {
            intertainments.splice(i, 1);
            break; 													// Dừng vòng lặp khi tìm thấy và xóa sản phẩm
        }
    }																// Cập nhật lại localStorage sau khi xóa sản phẩm
    localStorage.setItem('inter', JSON.stringify(intertainments));  // Lưu lại dữ liệu mới vào localStorage
    renderUIINTER(intertainments);									// Cập nhật lại UI
}	
window.onload = renderUIINTER(intertainments);

//=============================================END INTERTAINMENT=======================================================
//=============================================BEGIN MOVE==================================================================

//Lấy dữ liệu sản phẩm từ localStorage và hiển thị
let touristCarData = localStorage.getItem('touristCar') || '';
let carData = localStorage.getItem('car') || '';
let motorbikeData = localStorage.getItem('motorbike') || '';
// Tính số lượng phần tử Move
let numberOfItemsMove_1 = 0;
let numberOfItemsMove_2 = 0;
let numberOfItemsMove_3 = 0;

if(touristCarData != '') {
	numberOfItemsMove_1 = 1
}
if(carData != '') {
	numberOfItemsMove_2 = 1;
}
if(motorbikeData != '') {
	numberOfItemsMove_3 = 1;
}

// Chuyển đổi dữ liệu từ chuỗi sang mảng
let moves = [];
let moveTypes = [];  // Mảng mới để lưu các giá trị đơn giản
if (touristCarData) {
    moves.push({ type: 'touristCar', data: touristCarData, icon: 'fa-bus' });
    moveTypes.push('Tourist Car');  // Lưu giá trị đơn giản
}
if (carData) {
    moves.push({ type: 'car', data: carData, icon: 'fa-car-side' });
    moveTypes.push('Car');  // Lưu giá trị đơn giản
}
if (motorbikeData) {
    moves.push({ type: 'motorbike', data: motorbikeData, icon: 'fa-motorcycle' });
    moveTypes.push('Motorbike');  // Lưu giá trị đơn giản
}

// === TRUY CẬP VÀO CÁC THÀNH PHẦN ===
let movesEle = document.querySelector('.move');

// Lấy thông tin chi tiết của room từ server và Render và hiển thị dữ liệu
function renderUIMOVE(arrMove_item) {
    movesEle.innerHTML = '';

    for (let i = 0; i < arrMove_item.length; i++) {
        const pMove_item = arrMove_item[i];
        movesEle.innerHTML += `
              <tr>
                <td colspan="3">
                  <div class="thumb_cart thumb_cart_move">
                     <i class="tab-icon fas fa-solid ${pMove_item.icon}"></i>
                  </div>
                  <span class="item_cart_move">${pMove_item.data}</span>
                </td>
                <td>
                  <strong>&#8582;5%</strong>
                </td>
                <td class="options">
                  <a href="#" onclick="removeItemMove('${pMove_item.type}')"><i class=" icon-trash"></i></a>
                </td>
              </tr>
        `;
    }
}

// Remove item trong food
function removeItemMove(type) {
    localStorage.removeItem(type);
    moves = moves.filter(move => move.type !== type);
    renderUIMOVE(moves);
}
window.onload = renderUIMOVE(moves);

//==============================================END MOVE================================================================
//==============================================Hover Cart=============================================================

var numberOfProducts = document.getElementById('numberOfProducts');
// Hiển thị số lượng sản phẩm ra giao diện
let quantityProducts = numberOfItemsRoom + numberOfItemsCuisine + numberOfItemsInter + numberOfItemsMove_1 + numberOfItemsMove_2 + numberOfItemsMove_3;
numberOfProducts.textContent = quantityProducts;
localStorage.setItem('numberOfProducts', JSON.stringify(quantityProducts));

//============================================General Processing=======================================================
function updateCart() {
    updateRooms();
    updateCuisine();
    updateIntertainment();
}
//Cập nhật thông tin 3 Object mỗi khi tải trang
function updateRooms() {
    const cartFromLocalStorage = JSON.parse(localStorage.getItem('cart')) || [];	// Lấy giỏ hàng từ localStorage
    const roomIds = cartFromLocalStorage.map(item => item.id);						// Trích xuất roomIds từ giỏ hàng
    //Gửi yêu cầu đến backend để lấy thông tin cập nhật và cập nhật giao diện giỏ hàng.
    return fetch('/api/cart/check', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ roomIds }),
    })
    .then(response => response.json())
    .then(data => {
        // Cập nhật giao diện giỏ hàng ở đây
        const updatedCart = data.map(productFromServer => {
			const productFromLocal = cartFromLocalStorage.find(p => p.id === productFromServer.id);	
			return {...productFromServer, count: productFromLocal ? productFromLocal.count : 1};
		});
		localStorage.setItem('cart', JSON.stringify(updatedCart));
		renderUI(updatedCart);
        attachDatePickers();  											// Reattach date pickers after rendering UI
        
        // Tính số lượng loại thuộc tính category
        const categories = updatedCart.map(room => room.categoryName);  // Sử dụng đúng thuộc tính categoryName
        const uniqueCategories = [...new Set(categories)];
        const numberOfUniqueCategories = uniqueCategories.length;
        
        // Thiết lập biến discount dựa trên numberOfUniqueCategories
        let discount;
        if (numberOfUniqueCategories === 1) {
            discount = 5;
        } else if (numberOfUniqueCategories === 2) {
            discount = 8;
        } else if (numberOfUniqueCategories === 3) {
            discount = 10;
        } else {
            discount = localStorage.getItem('discount') || 0;
        }

        // Cập nhật discount vào LocalStorage
        localStorage.setItem('discount', discount);

        // Hiển thị số lượng loại thuộc tính category (có thể hiện thị ở console hoặc UI)
        console.log('Number of unique categories:', numberOfUniqueCategories);

        return numberOfUniqueCategories;
    })
    .catch((error) => {
        console.error('Error:', error);
    });
}

function updateCuisine() {
    const cuisineFromLocalStorage = JSON.parse(localStorage.getItem('food')) || [];			// Lấy giỏ hàng từ localStorage
    const cuisineIds = cuisineFromLocalStorage.map(item => item.id);						// Trích xuất roomIds từ giỏ hàng
    // Giả sử mã sau đây gửi yêu cầu đến backend để lấy thông tin cập nhật và cập nhật giao diện giỏ hàng.
    return fetch('/api/cuisine/check', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ cuisineIds }),
    })
    .then(response => response.json())
    .then(data => {
        // Cập nhật giao diện giỏ hàng ở đây
        const updatedCuisine = data.map(cuisineFromServer => {
			const cuisineFromLocal = cuisineFromLocalStorage.find(p => p.id === cuisineFromServer.id);	
			return {...cuisineFromServer, count: cuisineFromLocal ? cuisineFromLocal.count : 1};
		});
		localStorage.setItem('food', JSON.stringify(updatedCuisine));
		renderUIFOOD(updatedCuisine);
    })
    .catch((error) => {
        console.error('Error:', error);
    });
}

function updateIntertainment() {
    const intertainmentFromLocalStorage = JSON.parse(localStorage.getItem('inter')) || [];				// Lấy giỏ hàng từ localStorage
    const intertainmentIds = intertainmentFromLocalStorage.map(item => item.id);						// Trích xuất roomIds từ giỏ hàng
    // Giả sử mã sau đây gửi yêu cầu đến backend để lấy thông tin cập nhật và cập nhật giao diện giỏ hàng.
    return fetch('/api/intertainment/check', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ intertainmentIds }),
    })
    .then(response => response.json())
    .then(data => {
        // Cập nhật giao diện giỏ hàng ở đây
        const updatedIntertainment = data.map(intertainmentFromServer => {
			const intertainmentFromLocal = intertainmentFromLocalStorage.find(p => p.id === intertainmentFromServer.id);	
			return {...intertainmentFromServer, count: intertainmentFromLocal ? intertainmentFromLocal.count : 1};
		});
		localStorage.setItem('inter', JSON.stringify(updatedIntertainment));
		renderUIINTER(updatedIntertainment);
    })
    .catch((error) => {
        console.error('Error:', error);
    });
}

// Add this to ensure all updates run when the page is loaded
window.addEventListener('DOMContentLoaded', (event) => {
    updateCart();
});

//===================================================POST DB - EMAIL =================================================

//Lưu thông tin giỏ hàng xuống database
var revenueApi = 'http://localhost:8080/revenue';	        			//địa chỉ lấy data
var emailNotification = 'http://localhost:8080/send_html_email';     	//api thông báo đã đặt phòng
var exportFilePDF = 'http://localhost:8080/export_file_pdf';

//Tạo dữ liệu mới trong database
function createRevenue(data, callback){			  	
  var options = {														//tạo thông tin truyền vào fetch
    method: 'POST',						  								//phương thức của fetch là POST
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data)					  						//nội dung POST là data
  };
  fetch(revenueApi, options)											//truyền thông tin được tạo ở trên vào API revenueApi
    .then(function(response) {					
       return response.json();											//tạo đối tượng dạng json vào db qua API 
    })
    .then(callback);													//thực hiện hàm getRevenue(renderRevenue); => lấy dữ liệu truyền vào db và render ra giao diện
}

// Gọi đến API gửi email HTML thông báo
function sendNotification() {
  return fetch(emailNotification) 									
    .then(response => {
      if (response.ok) {
		showSuccessToast_Email_1();
        //alert("Notification has been sent successfully via Email!");
      } else {
		showErrorToast_Email_1();
        //alert("Failed to send Notification. Please try again later.");
      }
    })
    .catch(error => {
      console.error('Error:', error);
      showErrorToast_Email_2();
      //alert("An error occurred. Please try again later.");
    });
}

//Xuất File PDF xuống Local
function exportPDF(){
	fetch(exportFilePDF)
		.then(response => {})
		.catch(error => {});
}

//Lấy dữ liệu từ Form ở HTML để đưa vào function createRevenue()
function handleRevenueForm(){											//khi click vào phần tử button thì
	 //var income = totalEle.innerText;									//thay .value = .innerText hoặc .textContent vì totalEle là <span> không phải <input>
	 var income = parseFloat(totalEle.innerText.replace('$', '')); 
	 var titles = products.map(product => product.title).join(", ");
     var categories = products.map(product => product.categoryName).join(", ");
     var quantity = products.length;
     var cuisine = cuisines.map(cuisine => cuisine.titleCuisine).join(", ");
     var intertainment = intertainments.map(intertainment => intertainment.titleInter).join(", ");
     var move = moveTypes.join(", ");  									// Sử dụng mảng moveTypes để lưu vào Database
	 
     var formData = {													//tạo đối tượng có dạng title, content dùng để truyền vào db
       title_room: titles, 
       category_room: categories, 
       income: income, 													// Chuyển đổi chuỗi sang số để lưu vào database dưới dạng số.
       quantity: quantity,
       //Thêm các thông tin mới từ các đối tượng khác
       cuisineLocal: cuisine,
       intertainmentLocal: intertainment,
       moveLocal: move
     };
	
     createRevenue(formData, function(){								// Gọi hàm sendNotification sau khi tạo revenue thành công										
	 	sendNotification().then(() => exportPDF());  					// Đảm bảo sendNotification hoàn thành trước exportPDF
	 });																// Gửi Notification sau khi đã tạo Revenue thành công
}

// Thêm sự kiện click cho button CONFIRM
var clickConfirm = false;
document.querySelector('#confirm').addEventListener('click', function(event) {
    event.preventDefault(); 										    // Ngăn chặn hành vi mặc định của form nếu có
	fetch('/api/userId')
        .then(response => {
            if (!response.ok) {
                throw new Error('User not authenticated');
            }
            return response.json();
        })
        .then(() => {
            return Promise.all([
                updateRooms().then(() => {
                   updateTotalMoney();
                }),
                   updateCuisine(),
                   updateIntertainment(),
                   clickConfirm = true,
            ]);
        })
        .then(() => {
            if(quantityProducts == 0){
                showWarningBook_Service();
            } else if(quantityProducts != 0){
                showWarningToast_Check();
                handleRevenueForm();
            }
        })
        .catch(error => {
            console.error('Error fetching user ID:', error);
            showErrorToast();
        });
});

// Thêm sự kiện click cho button CHECK OUT
document.querySelector('#createVNPay').addEventListener('click', function(event) {
    event.preventDefault();
    handlePayment('/viettravel/pay');
    //sendEmail();                                                        //Gửi InvoicePDF
});

/*document.querySelector('#createQRCode').addEventListener('click', function(event) {
    event.preventDefault();
    handlePayment('/viettravel/qrcode');
});*/

document.querySelector('#createQRCode').addEventListener('click', function(event) {
    event.preventDefault();
    fetch('/api/userId')
        .then(response => {
            if(!response.ok) {
                throw new Error('User not authenticated');
            }
            return response.json();
        })
        .then(() => {
            if(clickConfirm && quantityProducts != 0) {
                $.magnificPopup.close();
                setTimeout(() => {
                    $.magnificPopup.open({
                        items: {
                            src: '#qrCodeBox'
                        },
                        type: 'inline',
                        midClick: true
                    });
                }, 300);
            } else {
                showWarningFirt_Confirm();
            }
        })
        .catch(error => {
            console.error('Error fetching user ID:', error);
            showErrorToast();
        });
    //handlePayment('/viettravel/qrcode');
});

document.querySelector('#createCrypto').addEventListener('click', function(event) {
    event.preventDefault();
    fetch('/api/userId')
        .then(response => {
            if (!response.ok) {
                throw new Error('User not authenticated');
            }
            return response.json();
        })
        .then(() => {
            if (clickConfirm && quantityProducts != 0) {
                $.magnificPopup.close();                         // Đóng popup PAY
                setTimeout(() => {                               // Mở popup CRYPTO
                    $.magnificPopup.open({
                        items: {
                            src: '#pay-dialog-crypto'
                        },
                        type: 'inline',
                        midClick: true
                    });
                }, 300);                                         // Thêm độ trễ nhỏ để đảm bảo đóng popup trước khi mở cái mới
            } else {
                showWarningFirt_Confirm();
            }
        })
        .catch(error => {
            console.error('Error fetching user ID:', error);
            showErrorToast();
        });
});

function handlePayment(url) {
    fetch('/api/userId')
        .then(response => {
            if (!response.ok) {
                throw new Error('User not authenticated');
            }
            return response.json();
        })
        .then(() => {
            if(clickConfirm && quantityProducts != 0){
                window.open(url, '_blank');
            } else {
                showWarningFirt_Confirm();
            }
        })
        .catch(error => {
            console.error('Error fetching user ID:', error);
            showErrorToast();
        });
}

// Gọi API gửi email PDF
var emailApi = 'http://localhost:8080/send_email_attachment';			//api gửi mail file pdf thông tin đặt phòng
function sendEmail() {
  fetch(emailApi) 														// Gọi đến API gửi email
    .then(response => {
      if (response.ok) {
		showSuccessToast_Email_2();
      } else {
		showErrorToast_Email_3();
      }
    })
    .catch(error => {
      console.error('Error:', error);
      showErrorToast_Email_2();
    });
}
