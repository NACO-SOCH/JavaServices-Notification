package gov.naco.soch.notification.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.naco.soch.notification.service.NotificationService;
import gov.naco.soch.util.CommonConstants;

/**
 * Controller class for managing notification attachments
 *
 */
@RestController
@RequestMapping("/attachment")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class NotificationAttachmentController {

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private Environment env;

	private static final Logger logger = LoggerFactory.getLogger(NotificationAttachmentController.class);

	/**
	 * API for cleaning all attachments before 7 days to the current date End point
	 * : /clean Rest Method : GET
	 * 
	 * @param accessKey
	 * @return
	 */
	@GetMapping("/clean")
	public boolean cleanNotificationAttachemnts(@RequestParam("accessKey") String accessKey) {
		if (StringUtils.isBlank(accessKey) || !env.getProperty(CommonConstants.PROPERTY_ACCESS_KEY).equals(accessKey)) {
			throw new AccessDeniedException("accessKey is not valid");
		} else {
			logger.warn("JOB--> cleanNotificationAttachemnts from API started");
			return notificationService.cleanNotificationAttachemnts();
		}
	}

}
