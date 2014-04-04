package com.coombu.photobook.service;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.coombu.photobook.model.SecurityUser;
import com.coombu.photobook.util.Constants;

@Service
public class MailServiceImpl implements IMailService {

	Logger log = LoggerFactory.getLogger(MailServiceImpl.class);

	@Autowired
	private transient JavaMailSender mailSender;

	@Autowired
	@Qualifier("registrationMessage")
	private SimpleMailMessage registrationMessage;

	@Autowired
	@Qualifier("forgotPasswordMessage")
	private SimpleMailMessage forgotPasswordMessage;

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public JavaMailSender getMailSender() {
		return mailSender;
	}

	/**
	 * @return the registrationMessage
	 */
	public SimpleMailMessage getRegistrationMessage() {
		return registrationMessage;
	}

	/**
	 * @param registrationMessage the registrationMessage to set
	 */
	public void setRegistrationMessage(SimpleMailMessage registrationMessage) {
		this.registrationMessage = registrationMessage;
	}

	@Override
	@Async
	public boolean sendEmail(String fromAddress, String subject, String body,
			String toAddress) {

		log.debug("Mail service Called!");

		try {
			SimpleMailMessage message = new SimpleMailMessage();
			log.debug("simple message creadted");

			message.setFrom(fromAddress);
			log.debug("from address is fixed");
			message.setTo(toAddress);
			log.debug("to address is fixed");
			message.setSubject(subject);
			log.debug("subject is fixed");
			message.setText(body);
			log.debug("body is fixed");

			log.debug("Sending an email....");
			mailSender.send(message);
			log.debug("Email Send!");

		} catch (Exception e) {
			e.printStackTrace();
			log.debug("Error while sending an Email!");
		}

		return false;
	}

	@Override
	@Async
	public void sendRegistrationMail(SecurityUser user, String eventName,
			String link) {
		MimeMessage message = mailSender.createMimeMessage();
		String subject = "Registration: ";
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setFrom(registrationMessage.getFrom());
			helper.setTo(user.getUserName());
			subject = registrationMessage.getSubject();
			helper.setSubject(subject);
			String text = String.format(registrationMessage.getText(), user
					.getUserProfile().getFirstName(), eventName, link);
			helper.setText(text, true);
			mailSender.send(message);
			log.info("{} was sent to: {}", subject, user.getUserName());
		} catch (Exception e) {
			log.error("{} was not sent to: {}", subject, user.getUserName());
			log.error("error", e);
		}

	}

	@Override
	@Async
	public void sendForgotPasswordToken(SecurityUser user, String tokenUrl) {
		MimeMessage message = mailSender.createMimeMessage();
		String subject = "Coombu Password Recovery: ";
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setFrom(forgotPasswordMessage.getFrom());
			helper.setTo(user.getUserName());
			subject = forgotPasswordMessage.getSubject();
			helper.setSubject(subject);
			String text = String.format(forgotPasswordMessage.getText(),Constants.PASSWORD_EXPIRE_TIME,tokenUrl, user.getUserName());
			helper.setText(text, true);
			mailSender.send(message);
			log.info("{} was sent to: {}", subject, user.getUserName());
		} catch (Exception e) {
			log.error("{} was not sent to: {}", subject, user.getUserName());
			log.error("error", e);
		}

	}

}
