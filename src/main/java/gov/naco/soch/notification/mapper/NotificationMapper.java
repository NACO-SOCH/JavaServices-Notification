package gov.naco.soch.notification.mapper;

import java.util.HashSet;
import java.util.Set;

import gov.naco.soch.dto.MasterDto;
import gov.naco.soch.dto.NotificationEventSaveDto;
import gov.naco.soch.entity.MasterNotificationEventType;
import gov.naco.soch.entity.NotificationEvent;
import gov.naco.soch.entity.NotificationEventRole;
import gov.naco.soch.entity.Role;

public class NotificationMapper {

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

}
