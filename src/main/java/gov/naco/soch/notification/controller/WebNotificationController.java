package gov.naco.soch.notification.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.naco.soch.dto.WebUserNotificationDto;
import gov.naco.soch.notification.service.WebUserNotificationService;
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
	
	@PostMapping("/save")
	public ResponseEntity<WebUserNotificationDto> saveWebNotification(@RequestBody WebUserNotificationDto webUserNotificationDto) {
		
		WebUserNotificationDto responseDto = webUserNotificationService.saveWebNotification(webUserNotificationDto);
		if(responseDto.getId() != null) {
			return new ResponseEntity<WebUserNotificationDto>(responseDto,HttpStatus.OK);
		}
		responseDto = null;
		return new ResponseEntity<WebUserNotificationDto>(responseDto,HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	
}
