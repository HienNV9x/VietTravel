document.getElementById('submitBtn').addEventListener('click', function(e) {
	e.preventDefault();
	let form = document.getElementById('registrationForm');

	let password = form.querySelector('input[name="password"]').value;
	let passwordConfirm = form.querySelector('input[name="passwordConfirm"]').value;
	
	// Kiểm tra sự khớp nhau của mật khẩu
	if (password !== passwordConfirm) {
		let errorElement = form.querySelector('input[name="passwordConfirm"] ~ .has-error');
		if (errorElement) {
			errorElement.textContent = "Passwords do not match";
		}
		return;
	}

	// Nếu mật khẩu khớp nhau thì gửi request lên Server
	let object = {};
	form.querySelectorAll('input, select').forEach(input => {
		object[input.name] = input.value || '';
	});
	let json = JSON.stringify(object);

	fetch('/viettravel/registration', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: json
	})
		.then(response => {
			if (response.ok) {
				window.location.href = "/";
			} else {
				return response.json().then(data => {
					let errors = data.errors;
					document.querySelectorAll('.has-error').forEach(el => el.textContent = '');
					for (let field in errors) {
						let errorElement = document.querySelector(`[name=${field}] ~ .has-error`);
						if (errorElement) {
							errorElement.textContent = errors[field];
						}
					}
				});
			}
		})
		.catch(error => console.error('Error:', error));
});