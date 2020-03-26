package gov.naco.soch.notification.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import gov.naco.soch.notification.service.NotificationService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class NotificationSenderController {

	@Autowired
	private NotificationService notificationService;

	private static final Logger logger = LoggerFactory.getLogger(NotificationSenderController.class);

	@PostMapping("/sendemail/{eventId}")
	public boolean sendEmail(@RequestBody Map<String, Object> placeholderMap, @PathVariable Long eventId) {
		logger.debug("Entered sendEmail Method");
		notificationService.sendEmail(placeholderMap, eventId);
		return true;
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
}
