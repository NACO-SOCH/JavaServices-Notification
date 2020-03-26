package gov.naco.soch.notification.sender;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Component
public class WhatsAppSenderService {

	@Value("${twilio.whatsappnumber}")
	private String whatsapp_number;

	public static final String WHATSAPP_FORMAT = "whatsapp:";

	public boolean sendWhatsApp(String whatsAppNumber, String whatsAppTemplate) {
		Message.creator(new PhoneNumber(WHATSAPP_FORMAT + whatsAppNumber),
				new PhoneNumber(WHATSAPP_FORMAT + whatsapp_number), whatsAppTemplate).create();
		return true;
	}

}
