package gov.naco.soch.notification.mapper;

import java.util.HashSet;
import java.util.Set;

import gov.naco.soch.dto.NotificationEventSaveDto;
import gov.naco.soch.entity.NotificationEvent;
import gov.naco.soch.entity.NotificationEventRole;
import gov.naco.soch.entity.Role;

public class NotificationMapper {

	public static NotificationEvent mapToNotificationEvent(NotificationEventSaveDto notificationEventSaveDto,
			NotificationEvent notificationEvent) {

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
		notificationEvent.setIsEnabled(notificationEventSaveDto.getEnable());
		Set<NotificationEventRole> newRoles = new HashSet<>();
		notificationEvent.getNotificationEventRoles().clear();
		notificationEventSaveDto.getRoleIds().forEach(roleId -> {
			Role role = new Role();
			role.setId(roleId);
			NotificationEventRole notificationEventRole = new NotificationEventRole();
			notificationEventRole.setNotificationEvent(notificationEvent);
			notificationEventRole.setRole(role);
			notificationEventRole.setWhatsappEnabled(true);
			notificationEventRole.setEmailEnabled(true);
			notificationEventRole.setSmsEnabled(true);
			newRoles.add(notificationEventRole);
		});
		notificationEvent.getNotificationEventRoles().addAll(newRoles);
		return notificationEvent;

	}

}
