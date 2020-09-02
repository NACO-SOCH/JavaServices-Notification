package gov.naco.soch.notification.sender;

import javax.mail.internet.MimeMessage;

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
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
		helper.setText(text, true); // Use this or above line.
		helper.setTo(to);
		helper.setSubject(subject);
		emailSender.send(mimeMessage);
		}
		catch (Exception e) {
			System.out.println("Exception :"+e.getMessage());
		}

	}
}