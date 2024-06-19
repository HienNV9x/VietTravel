function showSuccess1(){
	notification({
		title: "Success!",
		message: "You have successfully booked the Short Tour",
		type: "success",
		duration: 5000
	});
}

function showSuccess2(){
	notification({
		title: "Success!",
		message: "You have successfully booked a long-term Tour",
		type: "success",
		duration: 5000
	});
}

function showSuccess3(){
	notification({
		title: "Success!",
		message: "You have successfully booked the S-Shaped Tour",
		type: "success",
		duration: 5000
	});
}

function showSuccessToast() {
	notification({
		title: "Success!",
		message: "Room added to cart",
		type: "success",
		duration: 5000
	});
}

function showSuccessToast_2() {
	notification({
		title: "Success!",
		message: "Product added to cart",
		type: "success",
		duration: 5000
	});
}

function showWarningToast() {
	notification({
		title: "Warning!",
		message: "The room is already in the cart",
		type: "warning",
		duration: 5000
	});
}

function showWarningToast_2() {
	notification({
		title: "Warning!",
		message: "Products already in the cart",
		type: "warning",
		duration: 5000
	});
}

function showWarningToast_3() {
	notification({
		title: "Warning!",
		message: "Please enter 1 of 10 tourist destinations",
		type: "warning",
		duration: 5000
	});
}

function showErrorToast() {
	notification({
		title: "Error!",
		message: "Please log in to use this feature",
		type: "error",
		duration: 5000
	});
}

function showWarningToastComment() {
	notification({
		title: "Warning!",
		message: "You don't have permission to delete this comment",
		type: "warning",
		duration: 5000
	});
}

function showSuccessToast_Email_1() {
	notification({
		title: "Success!",
		message: "Notification has been sent successfully via Email",
		type: "success",
		duration: 5000
	});
}

function showSuccessToast_Email_2() {
	notification({
		title: "Success!",
		message: "Invoice has been sent successfully via Email",
		type: "success",
		duration: 5000
	});
}

function showErrorToast_Email_1() {
	notification({
		title: "Error!",
		message: "Failed to send Notification. Please try again later",
		type: "error",
		duration: 5000
	});
}

function showErrorToast_Email_2() {
	notification({
		title: "Error!",
		message: "An error occurred. Please try again later",
		type: "error",
		duration: 5000
	});
}

function showErrorToast_Email_3() {
	notification({
		title: "Error!",
		message: "Failed to send email. Please try again later",
		type: "error",
		duration: 5000
	});
}

function showWarningToast_Check() {
	notification({
		title: "Warning!",
		message: "Please check the product information and Payment amount again",
		type: "warning",
		duration: 5000
	});
}

function notification({ title = "", message = "", type = "info", duration = 3000 }) {
  const main = document.getElementById("notification");
  if (main) {
      const notification = document.createElement("div");

      const autoRemoveId = setTimeout(function () {             // Set duration for the slideOut animation to start
          setTimeout(() => main.removeChild(notification), 0);         // Remove the toast after border animation
      }, duration);

      notification.onclick = function (e) {
          if (e.target.closest(".notification__close")) {
              main.removeChild(notification);
              clearTimeout(autoRemoveId);
          }
      };

      const icons = {
          success: "fas fa-check-circle",
          info: "fas fa-info-circle",
          warning: "fas fa-exclamation-circle",
          error: "fas fa-exclamation-circle"
      };
      const colors = {
        success: "#71be34",
        info: "#2f86eb",
        warning: "#ffc021",
        error: "#ff623d"
      };
      const icon = icons[type];
      const color = colors[type];

      notification.classList.add("notification", `notification--${type}`);
      notification.style.animation = `slideInLeft ease .3s`;

      notification.innerHTML = `
          <div class="notification__icon">
              <i class="${icon}"></i>
          </div>
          <div class="notification__body">
              <h3 class="notification__title">${title}</h3>
              <p class="notification__msg">${message}</p>
          </div>
          <div class="notification__close">
              <i class="fas fa-times"></i>
          </div>
      `;

      notification.style.borderLeftColor = color;                            // Setting the left border color
      notification.style.setProperty("--notification-border-bottom-color", color);  // Adding color to border-bottom dynamically

      main.appendChild(notification);
  }
}