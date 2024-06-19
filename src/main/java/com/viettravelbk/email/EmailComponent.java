package com.viettravelbk.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailComponent {
	@Autowired
	private JavaMailSender mailSender;
	
	public void sendEmail() {
		//Use mailSender here...
	}
}
