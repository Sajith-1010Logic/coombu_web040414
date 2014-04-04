package com.coombu.photobook.service;

import com.coombu.photobook.model.SecurityUser;

public interface IMailService {

	public boolean sendEmail(String fromAddress, String subject, String body,
			String toAddress);

	
	public void sendRegistrationMail(SecurityUser user, String eventName, String link);


	public void sendForgotPasswordToken(SecurityUser user, String tokenUrl);
}
