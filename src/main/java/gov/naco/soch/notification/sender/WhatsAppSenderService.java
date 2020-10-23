package gov.naco.soch.notification.sender;

import org.springframework.stereotype.Component;

@Component
public class WhatsAppSenderService {

	//@Value("${twilio.whatsappnumber}")
	//private String whatsapp_number;

	public static final String WHATSAPP_FORMAT = "whatsapp:";

	public void sendWhatsApp(String whatsAppNumber, String whatsAppTemplate) {
		/*
		 * Message.creator(new PhoneNumber(WHATSAPP_FORMAT + whatsAppNumber), new
		 * PhoneNumber(WHATSAPP_FORMAT + whatsapp_number), whatsAppTemplate).create();
		 */
	}

}
