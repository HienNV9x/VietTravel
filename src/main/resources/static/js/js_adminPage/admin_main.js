//Lấy tổng income 
var totalRevenueApi = 'http://localhost:8080/revenue/total';
function getTotalRevenue() {
  fetch(totalRevenueApi)
    .then(function(response) {
      return response.json();
    })
    .then(function(totalIncome) {
      renderTotalRevenue(totalIncome);
    });
}
function renderTotalRevenue(totalIncome) {
  var revenueBlock = document.querySelector('#total-revenue');
  revenueBlock.innerHTML = `${totalIncome.toFixed(2)} $`;
} 

//Lấy tổng số bản ghi
var totalRecordsApi = 'http://localhost:8080/revenue/count';
function getTotalRecords() {
  fetch(totalRecordsApi)
    .then(function(response) {
      return response.json();
    })
    .then(function(totalRecords) {
      renderTotalRecords(totalRecords);
    });
}
function renderTotalRecords(totalRecords) {
  var recordsBlock = document.querySelector('#total-records');
  recordsBlock.innerHTML = `${totalRecords}`;
}

//Lấy tổng quantity
var totalQuantityApi = 'http://localhost:8080/revenue/quantity';
function getTotalQuantity() {
  fetch(totalQuantityApi)
    .then(function(response) {
      return response.json();
    })
    .then(function(totalQuantity) {
      renderTotalQuantity(totalQuantity);
    });
}
function renderTotalQuantity(totalQuantity) {
  var quantityBlock = document.querySelector('#total-quantity');
  quantityBlock.innerHTML = `${totalQuantity}`;
} 

//Lấy income theo ngày
function getDailyIncome() {
	var btnDailyIncome = document.querySelector('#btn-daily-income');
	btnDailyIncome.onclick = function(){
		var dateInput = document.querySelector('input[type="date"]').value;
    	var dailyIncomeApi = `http://localhost:8080/revenue/dailyIncome?date=${dateInput}`;
    	fetch(dailyIncomeApi)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(dailyIncome => {
                if (dailyIncome !== null) {
                    document.querySelector('#daily-income').innerHTML = `${dailyIncome.toFixed(2)} $`;
                } else {
                    // Nếu dailyIncome là null, hiển thị thông báo ngày không hợp lệ
                    document.querySelector('#daily-income').innerHTML = `0.00 $`;
                }
            })
            .catch(error => {
                console.error('There has been a problem with your fetch operation:', error);
                // Hiển thị thông báo lỗi hoặc ngày không hợp lệ nếu có lỗi xảy ra
                document.querySelector('#daily-income').innerHTML = `0.00 $`;
            });
	};
}

//Lấy income theo tháng
function getMonthlyIncome() {
    var btnMonthlyIncome = document.querySelector('#btn-monthly-income');
    btnMonthlyIncome.onclick = function(){
        var monthInput = document.querySelector('input[type="month"]').value;
        var year = monthInput.split('-')[0];
        var month = monthInput.split('-')[1];
        var monthlyIncomeApi = `http://localhost:8080/revenue/monthlyIncome?year=${year}&month=${month}`;
        fetch(monthlyIncomeApi)
            .then(response => {
                return response.json();
            })
            .then(monthlyIncome => {
                document.querySelector('#monthly-income').innerHTML = `${monthlyIncome.toFixed(2)} $`;
            })
    };
}

// Cập nhật hàm start để gọi các hàm
function start() {
  getTotalRevenue();
  getTotalRecords();
  getTotalQuantity();
  getDailyIncome(); 
  getMonthlyIncome();
}
start();

//Download file excel revenue
document.getElementById('exportButton').addEventListener('click', function() {
    window.location.href = '/export/revenues/excel';
});
