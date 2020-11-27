package gov.naco.soch.notification.mapper;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.naco.soch.dto.WebUserNotificationDto;
import gov.naco.soch.entity.NotificationEvent;
import gov.naco.soch.entity.UserMaster;
import gov.naco.soch.entity.WebUserNotification;


public class WebUserNotificationMapper {
	private static final Logger logger = LoggerFactory.getLogger(WebUserNotificationMapper.class);
	public static WebUserNotification mapDtoToEntity(WebUserNotificationDto webUserNotificationDto) {
		logger.info("Inside of mapDtoToEntity!");
		WebUserNotification webUserNotification = new WebUserNotification();
		webUserNotification.setFinalMessage(webUserNotificationDto.getFinalMessage());
		webUserNotification.setFinalUrl(webUserNotificationDto.getFinalUrl());
		webUserNotification.setIcon(webUserNotificationDto.getIcon());
		webUserNotification.setNotificationEvent(mapToNotificationEvent(webUserNotificationDto.getNotificationId()));
		webUserNotification.setUserMaster(mapToUserMaster(webUserNotificationDto.getUserId()));
		webUserNotification.setIsRead(false);
		webUserNotification.setCreatedBy(webUserNotificationDto.getUserId());
		webUserNotification.setCreatedTime(LocalDateTime.now());
		webUserNotification.setModifiedBy(webUserNotificationDto.getUserId());
		webUserNotification.setModifiedTime(LocalDateTime.now());
		webUserNotification.setIsActive(true);
		webUserNotification.setIsDelete(false);
		return webUserNotification;
	}
	private static UserMaster mapToUserMaster(Long userId) {
		logger.info("Inside of mapToUserMaster! (mapDtoToEntity)");
		UserMaster master = new UserMaster();
		master.setId(userId);
		return master;
	}
	private static NotificationEvent mapToNotificationEvent(Long notificationId) {
		logger.info("Inside of mapToNotificationEvent! (mapDtoToEntity)");
		NotificationEvent event = new NotificationEvent();
		event.setEventId(notificationId);
		return event;
	}
	
	
	

}
