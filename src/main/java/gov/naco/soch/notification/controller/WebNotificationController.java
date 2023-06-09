package gov.naco.soch.notification.controller;



import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import gov.naco.soch.dto.WebNotificationListDto;
import gov.naco.soch.dto.WebUserNotificationDto;
import gov.naco.soch.notification.model.NotificationCountResponse;
import gov.naco.soch.notification.service.WebUserNotificationService;
import gov.naco.soch.util.CommonConstants;
/**
 * 
 * @author Rishad Basheer(u76718)
 *
 */
@RestController
@RequestMapping("/webnotification")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class WebNotificationController {
   
	private static final Logger logger = LoggerFactory.getLogger(WebNotificationController.class);
	@Autowired
	private WebUserNotificationService webUserNotificationService;
	@Autowired
	private Environment env;
	@PostMapping("/send/{eventId}")
	//public void saveWebNotification(@RequestBody WebUserNotificationDto webUserNotificationDto) {
		public void saveWebNotification(@RequestBody Map<String, Object> placeholderMap, @PathVariable Long eventId) {
		logger.debug("Entered into saveWebJobNotification Method");
		if(placeholderMap.containsKey("accessKey")) {
		String accessKey = placeholderMap.get("accessKey").toString();
		if (StringUtils.isBlank(accessKey) || !env.getProperty(CommonConstants.PROPERTY_ACCESS_KEY).equals(accessKey)) {
			throw new AccessDeniedException("accessKey is not valid");
		} else {
		logger.info("EVENT ID :"+eventId);
		boolean result = webUserNotificationService.saveWebNotification(placeholderMap,eventId);
		if(result) {
			logger.info("Web user notification is sent!  ");
		 }
		}
		}
		else {
			logger.error("Access key doesn't exist!");
		}
	}
	
	@PostMapping("/job/send/{eventId}")
	//public void saveWebNotification(@RequestBody WebUserNotificationDto webUserNotificationDto) {
		public void saveWebJobNotification(@RequestBody Map<String, Object> placeholderMap, @PathVariable Long eventId) {
		logger.debug("Entered into saveWebJobNotification Method");
		if(placeholderMap.containsKey("accessKey")) {
		String accessKey = placeholderMap.get("accessKey").toString();
		logger.info("Acces Key :"+accessKey);
		if (StringUtils.isBlank(accessKey) || !env.getProperty(CommonConstants.PROPERTY_ACCESS_KEY).equals(accessKey)) {
			throw new AccessDeniedException("accessKey is not valid");
		} else {
		logger.info("EVENT ID :"+eventId);
		boolean result = webUserNotificationService.saveWebNotification(placeholderMap,eventId);
		if(result) {
			logger.info("Web user notification is sent!  ");
		 }
		}
		}
		else {
			logger.error("Access key doesn't exist!");
		}
	}
	
//	@GetMapping("/count")
//	public Integer getNotificationCount(@RequestParam Integer userId) {
//		try {
//			
//		return webUserNotificationService.getWebNotificationCount(userId);
//		}
//		catch (Exception e) {
//			logger.error("Exception :"+e.getMessage());
//		}
//		return null;
//
//	}
	
	@GetMapping("/count")
	public NotificationCountResponse getNotificationCount(@RequestParam Integer userId) {
		NotificationCountResponse notificationCountResponse=null;//=new NotificationCountResponse();
		
		try {
			Integer count=webUserNotificationService.getWebNotificationCount(userId);
			notificationCountResponse=new NotificationCountResponse(count,LocalDateTime.now());
		}
		catch (Exception e) {
			logger.error("Exception :"+e.getMessage());
		}
		return notificationCountResponse;
	}
	

	@GetMapping("/list")
	public @ResponseBody WebNotificationListDto getAllWebNotifications(@RequestParam(required = false) Integer pageNumber,
			@RequestParam(required = false) Integer pageSize, @RequestParam("userId") Integer userId) {
		logger.debug("getAllWebNotifications method called");
		return webUserNotificationService.getAllWebNotification(pageNumber, pageSize , userId);
	}

	@PutMapping("/updateread/{Id}")
	public void updateReadTrue(@PathVariable Long Id) {
		
		logger.debug("updateReadTrue method is invoked");
		 webUserNotificationService.updateRead(Id);
	}
	
	
}
