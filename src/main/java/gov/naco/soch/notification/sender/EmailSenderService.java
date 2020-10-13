package gov.naco.soch.notification.sender;

import java.io.File;

import javax.mail.internet.MimeMessage;

import org.apache.commons.codec.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import gov.naco.soch.notification.service.NotificationService;

@Component
public class EmailSenderService {

	@Autowired
	public JavaMailSender emailSender;

	private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

	public void sendEmail(String to, String subject, String text, String senderMail) {
		/**
		 * New code added for accepting HTML templates
		 * 
		 * @author Rishad Basheer
		 */
		try {
			MimeMessage mimeMessage = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, CharEncoding.UTF_8);
			helper.setFrom(senderMail, senderMail);
			helper.setText(text, true); // Use this or above line.
			helper.setTo(to);
			helper.setSubject(subject);
			emailSender.send(mimeMessage);
		} catch (Exception e) {
			logger.error("Exception in sendEmail->", e);
		}

	}
}