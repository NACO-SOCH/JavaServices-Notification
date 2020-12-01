package gov.naco.soch.notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.naco.soch.dto.WebUserNotificationDto;
import gov.naco.soch.entity.WebUserNotification;
import gov.naco.soch.notification.mapper.WebUserNotificationMapper;
import gov.naco.soch.repository.WebUserNotificationRepository;
/**
 * 
 * @author Rishad Basheer (U76718)
 *
 */
@Service
public class WebUserNotificationService {
	
	@Autowired
	private WebUserNotificationRepository  webUserNotificationRepository;
 
	private static final Logger logger = LoggerFactory.getLogger(WebUserNotificationService.class);
	public WebUserNotificationDto saveWebNotification(WebUserNotificationDto webUserNotificationDto) {
		logger.info("Inside of saveWebNotification : WebUserNotificationService ");
		WebUserNotification webUserNotification = WebUserNotificationMapper.mapDtoToEntity(webUserNotificationDto);
		webUserNotification = webUserNotificationRepository.save(webUserNotification);
		if(webUserNotification!=null) {
		webUserNotificationDto.setId(webUserNotification.getId());
		}
		return webUserNotificationDto;
	}
	public Integer getWebNotificationCount(Integer userId) {
		logger.info("Inside of getWebnotificationCountFuntion: WebUserNotificationService ");
		Integer notificationCount = webUserNotificationRepository.getWebNotificationCount(userId);
		return notificationCount;
	}

}
