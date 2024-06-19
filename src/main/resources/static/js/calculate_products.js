function calculateNumberOfProducts() {
    let products = JSON.parse(localStorage.getItem('cart')) || [];
    let numberOfItemsRoom = products.length;

    let cuisines = JSON.parse(localStorage.getItem('food')) || [];
    let numberOfItemsCuisine = cuisines.length;

    let intertainments = JSON.parse(localStorage.getItem('inter')) || [];
    let numberOfItemsInter = intertainments.length;

    let touristCarData = localStorage.getItem('touristCar') || '';
    let carData = localStorage.getItem('car') || '';
    let motorbikeData = localStorage.getItem('motorbike') || '';

    let numberOfItemsMove_1 = touristCarData ? 1 : 0;
    let numberOfItemsMove_2 = carData ? 1 : 0;
    let numberOfItemsMove_3 = motorbikeData ? 1 : 0;

    let quantityProducts = numberOfItemsRoom + numberOfItemsCuisine + numberOfItemsInter + numberOfItemsMove_1 + numberOfItemsMove_2 + numberOfItemsMove_3;

    localStorage.setItem('numberOfProducts', JSON.stringify(quantityProducts));
    return quantityProducts;
}

function updateNumberOfProductsDisplay() {
    let numberOfProducts = document.getElementById('numberOfProducts');
    if (numberOfProducts) {
        numberOfProducts.textContent = JSON.parse(localStorage.getItem('numberOfProducts'));
    }
}

// Gọi hàm calculateNumberOfProducts khi load file
calculateNumberOfProducts();
updateNumberOfProductsDisplay();