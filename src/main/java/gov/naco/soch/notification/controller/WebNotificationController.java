package gov.naco.soch.notification.controller;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	
	@GetMapping("/count")
	public Integer getNotificationCount(@RequestParam Integer userId) {
		try {
		return webUserNotificationService.getWebNotificationCount(userId);
		}
		catch (Exception e) {
			logger.error("Exception :"+e.getMessage());
		}
		return null;
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
