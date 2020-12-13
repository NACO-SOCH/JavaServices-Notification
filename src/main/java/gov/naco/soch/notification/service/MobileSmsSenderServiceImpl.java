package gov.naco.soch.notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import gov.naco.soch.notification.model.SendSmsModel;

@Service
public class MobileSmsSenderServiceImpl implements MobileSmsSenderService {
	private static final Logger log = LoggerFactory.getLogger(MobileSmsSenderService.class);
	@Value("${mobileTwilioSid}")
	private String mobileSid;
	@Value("${mobileTwilioAuthId}")
	private String mobileauthId;
	@Value("${mobileTwilioFromMobileNumber}")
	private String mobileTwilioFromMobileNumber;
	
	@Override
	public String sendOtp(SendSmsModel sendSmsModel) {
		try {
			log.info("Entering MobileSmsSenderServiceImpl.sendOtp() method: mobileApp with data {}", sendSmsModel);
	        Twilio.init(mobileSid, mobileauthId);

	        Message message = Message.creator(new PhoneNumber(sendSmsModel.getMobileNumber()),
	        		new PhoneNumber(mobileTwilioFromMobileNumber), sendSmsModel.getMessageText())
	                .create();
	        log.info("Soch-Twilio Message Sid: {}", message.getSid());// Unique resource ID created to manage this transaction
		}catch(Exception ex) {
			log.info("[EXCEPTION] occurred in sendSMS method: {}", ex.getLocalizedMessage());
			return "3000-failure:[" + ex.getLocalizedMessage() + "]";
		}
		log.info("Exiting MobileSmsSenderServiceImpl.sendOtp() method: mobileApp with data {}", sendSmsModel);
		return "2000-success";
	}

}
