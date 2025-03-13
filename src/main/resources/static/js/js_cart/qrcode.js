		// Gọi API gửi email PDF
		var emailApi = 'http://localhost:8080/send_email_attachment';			//api gửi mail file pdf thông tin đặt phòng
		function sendEmail() {
		  fetch(emailApi) 									// Gọi đến API gửi email
		    .then(response => {
		      if (response.ok) {
		        //alert("Email has been sent successfully!");
				showSuccessToast_Email_2();
		      } else {
		        //alert("Failed to send email. Please try again later.");
				showErrorToast_Email_3();
		      }
		    })
		    .catch(error => {
		      console.error('Error:', error);
		      //alert("An error occurred. Please try again later.");
			  showErrorToast_Email_2();
		    });
		}

		//Click Button PAY sẽ gửi InvoicePDF
		document.querySelector('#qrcodeSendEmail').addEventListener('click', function(event) {
			sendEmail();
			$.magnificPopup.close();
		});