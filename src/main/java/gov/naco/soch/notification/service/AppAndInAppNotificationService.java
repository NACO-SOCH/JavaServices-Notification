package gov.naco.soch.notification.service;

import java.util.List;
import java.util.Map;

import gov.naco.soch.entity.PushNotificationEntity;

public interface AppAndInAppNotificationService {
	
	public void sendPushNotificationToBenificiary(List<Map<String,Object>> list) ;
	public void sendPushNotificationToMultipleUser(List<Map<String,Object>> list);
	public void sendPushNotificationToSingleUser(Map<String,Object> map);
	public List<PushNotificationEntity> getNotificationsForBeneficiary(Long beneficiaryId);
	public boolean deleteNotificationForBeneficiary(Long beneficiaryId, Long notificationId);
}
