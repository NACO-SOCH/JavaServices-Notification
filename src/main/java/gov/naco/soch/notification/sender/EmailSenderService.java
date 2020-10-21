package gov.naco.soch.notification.sender;

import javax.mail.internet.MimeMessage;

import org.apache.commons.codec.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailSenderService {

	@Autowired
	public JavaMailSender emailSender;

	private static final Logger logger = LoggerFactory.getLogger(EmailSenderService.class);

	public void sendEmail(String to, String subject, String text, String senderMail) {
		/**
		 * New code added for accepting HTML templates
		 * 
		 * @author Rishad Basheer
		 */
		try {
			logger.debug("Inside of sendMail : EmailSenderService !");
			MimeMessage mimeMessage = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, CharEncoding.UTF_8);
			helper.setFrom(senderMail, senderMail);
			helper.setText(text, true); // Use this or above line.
			helper.setTo(to);
			helper.setSubject(subject);
			logger.info("Inside of sendMail (before send mail) : EmailSenderService !");
			emailSender.send(mimeMessage);
			logger.info("Inside of sendMail (After sent mail) : EmailSenderService !");
		} catch (Exception e) {
			logger.error("Exception in sendEmail->", e);
		}

	}
}