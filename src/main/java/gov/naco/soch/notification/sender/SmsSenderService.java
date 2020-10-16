package gov.naco.soch.notification.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class SmsSenderService {

	// @Value("${twilio.smsnumber}")
	// private String sms_number;

	@Value("${notificationSmsApiEndpoint}")
	private String smsApiEndpoint;

	@Value("${notificationSmsApiUsername}")
	private String smsApiUserName;

	@Value("${notificationSmsApiPin}")
	private String smsApiPin;

	@Value("${notificationSmsApiSignature}")
	private String smsApiSignature;

	private static final Logger logger = LoggerFactory.getLogger(SmsSenderService.class);

	@Autowired
	private RestTemplate restTemplate;

	public boolean sendSms(String mobileNumber, String smsTemplate) {

		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		if (mobileNumber.length() <= 10) {
			mobileNumber = "91" + mobileNumber;
		}
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(smsApiEndpoint)
				.queryParam("username", smsApiUserName).queryParam("pin", smsApiPin).queryParam("message", smsTemplate)
				.queryParam("mnumber", mobileNumber).queryParam("signature", smsApiSignature);
		HttpEntity<?> entity = new HttpEntity<>(headers);
		logger.info("Going to send SMS to mobileNumber-->{}:", mobileNumber);
		restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
		logger.info("Sent SMS to mobileNumber-->{}:", mobileNumber);
		// Message.creator(new PhoneNumber(mobileNumber), new PhoneNumber(sms_number),
		// smsTemplate).create();
		return true;
	}

}
