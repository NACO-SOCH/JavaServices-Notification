package gov.naco.soch.notification.service;

import java.util.List;
import java.util.Map;

public interface AppAndInAppNotiticationService {
	
	public void sendPushNotificationToBanificiary(List<Map<String,Object>> list) ;
	public void sendPushNotificationToMultipleUser(List<Map<String,Object>> list);
	public void sendPushNotificationToSingleUser(Map<String,Object> map);
	
	

}
