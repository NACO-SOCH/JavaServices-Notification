package gov.naco.soch.notification.controller;

import org.springframework.security.access.AccessDeniedException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.apache.commons.lang3.StringUtils;
import gov.naco.soch.notification.service.NotificationService;
import gov.naco.soch.util.CommonConstants;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class NotificationSenderController {

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private Environment env;

	private static final Logger logger = LoggerFactory.getLogger(NotificationSenderController.class);

	@PostMapping("/sendemail/{eventId}")
	public void sendEmail(@RequestBody Map<String, Object> placeholderMap, @PathVariable Long eventId) {
		logger.debug("Entered sendEmail Method");
		notificationService.sendEmail(placeholderMap, eventId);
		logger.info("After sent mail : NotificationSenderController!");
		//return true;
	}

	@PostMapping("/sendsms/{eventId}")
	public boolean sendSms(@RequestBody Map<String, Object> placeholderMap, @PathVariable Long eventId) {
		logger.debug("Entered sendSms Method");
		notificationService.sendSms(placeholderMap, eventId);
		return true;
	}

	@PostMapping("/sendwhatsapp/{eventId}")
	public boolean sendWhatsapp(@RequestBody Map<String, Object> placeholderMap, @PathVariable Long eventId) {
		logger.debug("Entered sendWhatsapp Method");
		notificationService.sendWhatsapp(placeholderMap, eventId);
		return true;
	}

//API for sending system emails
	@PostMapping("/sendsystememail/{eventId}")
	public boolean sendSystemEmail(@RequestBody Map<String, Object> placeholderMap, @PathVariable Long eventId) {
		logger.debug("Entered sendEmail Method");
		String accessKey = placeholderMap.get("accessKey").toString();
		if (StringUtils.isBlank(accessKey) || !env.getProperty(CommonConstants.PROPERTY_ACCESS_KEY).equals(accessKey)) {
			throw new AccessDeniedException("accessKey is not valid");
		} else {
			notificationService.sendEmail(placeholderMap, eventId);
		}
		return true;
	}

}
