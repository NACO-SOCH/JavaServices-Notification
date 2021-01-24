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
@Service
public class AsyncPushNotificationService {
	private static final Logger log = LoggerFactory.getLogger(AppAndInAppNotificationServiceImpl.class);

	@Autowired
	private PushNotificationService pushNotificationServices;
	
	//@Async
	//@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void sendPushNotification(String message, String title, int type, Map<String, String> targetInfoMap,
			boolean sendpushNotification, String topic, boolean silent, int roleType) throws Exception {
		log.info("Enter into sendPushNotification(-) of WebAsyncService");
		if (sendpushNotification) {
			PushNotification pushMessage = new PushNotification();
			pushMessage.setMessage(message);
			pushMessage.setTitle(title);
			pushMessage.setData(targetInfoMap);
			pushMessage.setType(type);

			pushNotificationServices.sendPushNotificationToTopic(pushMessage, topic, silent);
			log.info("Exist sendPushNotification(-) of WebAsyncService");

		}
	}

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
	
	public void sendPushNotificationToMultipleUser(String message, String title, int type, Map<String, String> targetInfoMap,
			boolean sendpushNotification, boolean silent,List<PushNotificationStatus> status) throws Exception {

		if (sendpushNotification) {
			PushNotification pushMessage = new PushNotification();
			pushMessage.setMessage(message);
			pushMessage.setTitle(title);
			pushMessage.setBadgeCount(1);
			pushMessage.setData(targetInfoMap);
			pushMessage.setType(type);

			
				try {
//					pushNotificationServices.sendMulticastAndHandleErrors(pushMessage,status);
				} catch (Exception e) {
					log.error("Error : sendPushNotification" + e);
					throw e;
				
			}
		}
	}
}


