package gov.naco.soch.notification.sender;

import java.io.File;

import javax.mail.internet.MimeMessage;

import org.apache.commons.codec.CharEncoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailSenderService {

	@Autowired
	public JavaMailSender emailSender;

	public void sendEmail(String to, String subject, String text) {
//		SimpleMailMessage message = new SimpleMailMessage();
//		message.setTo(to);
//		message.setSubject(subject);
//		message.setText(text);
//		emailSender.send(message);
		
/**
 * New code added for accepting HTML templates 
 * @author Rishad Basheer
 */
		try {
			MimeMessage mimeMessage = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, CharEncoding.UTF_8);
			helper.setText(text, true); // Use this or above line.
			helper.setTo(to);
			helper.setSubject(subject);
			//helper.addInline("leftSideImage",new File("C:/Users/u76718/Desktop/Bug_resolution_screenshots/naco2.png"));
			emailSender.send(mimeMessage);
		}
		catch (Exception e) {
			System.out.println("Exception :"+e.getMessage());
		}

	}
}