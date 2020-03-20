package gov.naco.soch.notification.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.naco.soch.notification.service.NotificationService;

@RestController
@RequestMapping("/notification")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class NotificationApiController {

	@Autowired
	NotificationService notificationService;

	private static final Logger logger = LoggerFactory.getLogger(NotificationApiController.class);

	public NotificationApiController() {
	}

	/* API to send email. */
	@GetMapping("/{eventId}/sendEmail")
	public @ResponseBody boolean sendEmail(@RequestBody Map<String, Object> data,
			@PathVariable("eventId") String eventId) {
		logger.debug("Entering into method sendEmail with eventId->{}:data-->{}:", eventId, data);
		return notificationService.sendEmail(data, eventId);
	}

}
