package gov.naco.soch.notification.mapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.naco.soch.dto.MasterDto;
import gov.naco.soch.dto.NotificationEventSaveDto;
import gov.naco.soch.dto.UserMasterDto;
import gov.naco.soch.dto.WebNotificationListDto;
import gov.naco.soch.dto.WebUserNotificationDto;
import gov.naco.soch.entity.MasterNotificationEventType;
import gov.naco.soch.entity.NotificationEvent;
import gov.naco.soch.entity.NotificationEventRole;
import gov.naco.soch.entity.Role;
import gov.naco.soch.projection.UserListProjection;
import gov.naco.soch.projection.WebNotificationProjection;

public class NotificationMapper {

	 private static final Logger logger = LoggerFactory.getLogger(NotificationMapper.class);
	
	public static NotificationEvent mapToNotificationEvent(NotificationEventSaveDto notificationEventSaveDto,
			NotificationEvent notificationEvent) {
		
		if (notificationEventSaveDto.getEventId() != null) {
			notificationEvent.setEventId(notificationEventSaveDto.getEventId());
		}

		if (notificationEventSaveDto.getEmailSubject() != null) {
			notificationEvent.setEmailSubject(notificationEventSaveDto.getEmailSubject());
		}
		if (notificationEventSaveDto.getEmailTemplate() != null) {
			notificationEvent.setEmailTemplate(notificationEventSaveDto.getEmailTemplate());
		}
		if (notificationEventSaveDto.getSmsTemplate() != null) {
			notificationEvent.setSmsTemplate(notificationEventSaveDto.getSmsTemplate());
		}
		if (notificationEventSaveDto.getWhatsAppTemplate() != null) {
			notificationEvent.setWhatsappTemplate(notificationEventSaveDto.getWhatsAppTemplate());
		}
		if(notificationEventSaveDto.getWebTemplate() != null) {
			notificationEvent.setWebTemplate(notificationEventSaveDto.getWebTemplate());
		}
		if(notificationEventSaveDto.getIsEmailEnabled() != null) {
		notificationEvent.setIsEmailEnabled(notificationEventSaveDto.getIsEmailEnabled());
		}
		if(notificationEventSaveDto.getIsWhatsappEnabled() != null) {
			notificationEvent.setIsWhatsappEnabled(notificationEventSaveDto.getIsWhatsappEnabled());
			}
		if(notificationEventSaveDto.getIsSmsEnabled() != null) {
			notificationEvent.setIsSmsEnabled(notificationEventSaveDto.getIsSmsEnabled());
			}
		if(notificationEventSaveDto.getIsWebEnabled() != null) {
			notificationEvent.setIsWebEnabled(notificationEventSaveDto.getIsWebEnabled());
			}
		if(notificationEventSaveDto.getIcon()!=null) {
			notificationEvent.setIcon(notificationEventSaveDto.getIcon());
		}
		
		notificationEvent.setIsEnabled(notificationEventSaveDto.getEnable());
		if(notificationEventSaveDto.getActionUrl() != null) {
		notificationEvent.setActionUrl(notificationEventSaveDto.getActionUrl());
		}
		Set<NotificationEventRole> newRoles = new HashSet<>();
		if(notificationEvent.getNotificationEventRoles()!=null)
		notificationEvent.getNotificationEventRoles().clear();
		notificationEventSaveDto.getRoleIds().forEach(roleId -> {
			Role role = new Role();
			role.setId(roleId);
			NotificationEventRole notificationEventRole = new NotificationEventRole();
			notificationEventRole.setRole(role);
			notificationEventRole.setNotificationEvent(notificationEvent);
			newRoles.add(notificationEventRole);
		});
		notificationEvent.getNotificationEventRoles().addAll(newRoles);
		if(notificationEventSaveDto.getMasterNotificationEventType()!=null) {
	    MasterNotificationEventType eventType = new MasterNotificationEventType();
		MasterDto eventTypeDto = notificationEventSaveDto.getMasterNotificationEventType();
		eventType.setId(eventTypeDto.getId());
		notificationEvent.setMasterNotificationEventType(eventType);
		}
		else
		notificationEvent.setMasterNotificationEventType(null);
		return notificationEvent;

	}
	
	
	public static List<WebUserNotificationDto> mapWebNotificationListProjectionToWebUserNotificationDtoList(List<WebNotificationProjection> WebNotificationProjectionList) {
		logger.info("Inside of mapWebNotificationListProjectionToWebUserNotificationDtoList######");
		List<WebUserNotificationDto> list = new ArrayList<>();
		for (WebNotificationProjection webNotification : WebNotificationProjectionList) {
			logger.info("for (WebNotificationProjection webNotification : WebNotificationProjectionList)");
			WebUserNotificationDto webUserNotificationDto = new WebUserNotificationDto();
			webUserNotificationDto.setId(webNotification.getId());
			webUserNotificationDto.setIcon(webNotification.getIcon());
			webUserNotificationDto.setFinalMessage(webNotification.getFinalMessage());
			webUserNotificationDto.setFinalUrl(webNotification.getFinalUrl());
			webUserNotificationDto.setIsRead(webNotification.getIsRead());
			webUserNotificationDto.setIsDelete(webNotification.getIsDelete());
			webUserNotificationDto.setNoOfDays(webNotification.getNoOfDays());
			list.add(webUserNotificationDto);
		}
		return list;
	}

}
