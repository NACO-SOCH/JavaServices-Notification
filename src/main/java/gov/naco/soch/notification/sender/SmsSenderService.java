package gov.naco.soch.notification.sender;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Component
public class SmsSenderService {

	@Value("${twilio.smsnumber}")
	private String sms_number;

	public boolean sendSms(String mobileNumber, String smsTemplate) {

		Message.creator(new PhoneNumber(mobileNumber), new PhoneNumber(sms_number), smsTemplate).create();
		return true;
	}

}
