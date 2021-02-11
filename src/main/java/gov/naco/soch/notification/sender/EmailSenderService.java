package gov.naco.soch.notification.sender;

import java.util.List;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.codec.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import gov.naco.soch.entity.NotificationAttachment;

@Component
public class EmailSenderService {

	@Autowired
	public JavaMailSender emailSender;

	private static final Logger logger = LoggerFactory.getLogger(EmailSenderService.class);

	public void sendEmail(String to, String subject, String text, String senderMail,
			List<NotificationAttachment> notificationAttachments) {
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
			if (notificationAttachments != null && !CollectionUtils.isEmpty(notificationAttachments)) {
				notificationAttachments.forEach(notificationAttachment -> {
					byte[] file = notificationAttachment.getAttachment();
					String fileType = notificationAttachment.getAttachmentType();
					String fileName = notificationAttachment.getAttachmentFileName();
					DataSource dataSource = new ByteArrayDataSource(file, fileType);
					try {
						helper.addAttachment(fileName, dataSource);
					} catch (MessagingException e) {
						logger.info("Exception in add attachment ->{}:", e);
						e.printStackTrace();
					}
				});
			}
			logger.info("Inside of sendMail (before send mail) : EmailSenderService !");
			emailSender.send(mimeMessage);
			logger.info("Inside of sendMail (After sent mail) : EmailSenderService !");
		} catch (Exception e) {
			logger.error("Exception in sendEmail->", e);
		}

	}

}