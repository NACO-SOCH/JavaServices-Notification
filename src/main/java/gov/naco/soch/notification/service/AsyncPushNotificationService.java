package gov.naco.soch.notification.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.naco.soch.notification.model.PushDevice;
import gov.naco.soch.notification.model.PushNotification;
import gov.naco.soch.notification.model.PushNotificationStatus;
import gov.naco.soch.notification.util.NotificationUtils;
@Service
public class AsyncPushNotificationService {
	private static final Logger log = LoggerFactory.getLogger(AppAndInAppNotificationServiceImpl.class);

	@Autowired
	private PushNotificationService pushNotificationServices;
	
	

	//@Async
	//@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void sendPushNotification(String message, String title, int type, Map<String, String> targetInfoMap,
			boolean sendpushNotification, PushDevice pushDevice, boolean silent) throws Exception {

		if (sendpushNotification) {
			PushNotification pushMessage = new PushNotification();
			pushMessage.setMessage(message);
			pushMessage.setTitle(title);
			pushMessage.setData(targetInfoMap);
			pushMessage.setType(type);

				try {
					pushNotificationServices.sendPushNotification(pushMessage, pushDevice, silent);
				} catch (Exception e) {
					log.error("Error : sendPushNotification" + e);
					throw e;
				}
			
		}

	}
	public void sendPushNotificationToMultipleUserWithDiffMessage(List<PushNotification> pushNotifications) throws Exception {

		List<List<PushNotification>> getPushNotificationTokens=NotificationUtils.getBatches(pushNotifications, 500);
		getPushNotificationTokens.forEach(pushMessages->{
			try {
				pushNotificationServices.pushNotificationToMultipleFCMWithDiffMessageAndTitle(pushMessages);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		});

	}
	
	
	public void sendPushNotificationToMultipleUserWithSameMessage(String title,String message,List<String> tokens,Map<String, String> targetInfoMap) {
		PushNotification pushMessage = new PushNotification();
		pushMessage.setMessage(message);
		pushMessage.setTitle(title);
		pushMessage.setData(targetInfoMap);
		List<List<String>> getPushNotificationTokens=NotificationUtils.getBatches(tokens, 500);
		getPushNotificationTokens.forEach(token->{
			try {
				pushNotificationServices.pushNotificationToMultipleFCMWithSameMessageAndTitle(pushMessage, token);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		});
	}
}


