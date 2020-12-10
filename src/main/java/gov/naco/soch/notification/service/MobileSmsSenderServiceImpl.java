package gov.naco.soch.notification.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import gov.naco.soch.notification.model.SendSmsModel;

@Service
public class MobileSmsSenderServiceImpl implements MobileSmsSenderService {
	@Value("${mobileTwilioSid}")
	private String mobileSid;
	@Value("${mobileTwilioAuthId}")
	private String mobileauthId;
	@Value("${mobileTwilioFromMobileNumber}")
	private String mobileTwilioFromMobileNumber;
	
	@Override
	public String sendSms(SendSmsModel sendSmsModel) {
	        Twilio.init(mobileSid, mobileauthId);

	        Message message = Message.creator(new PhoneNumber(sendSmsModel.getMobileNumber()),
	        		new PhoneNumber(mobileTwilioFromMobileNumber), sendSmsModel.getMessageText())
	                .create();
	        System.out.println("here is my id:"+message.getSid());// Unique resource ID created to manage this transaction

		
		return "All Good";
	}

}
