package gov.naco.soch.notification.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.ApsAlert;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Message.Builder;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.SendResponse;

import gov.naco.soch.notification.model.PushDevice;
import gov.naco.soch.notification.model.PushNotification;
import gov.naco.soch.notification.util.NotificationUtils;


public class PushNotificationService {
	private Logger log = LoggerFactory.getLogger(PushNotificationService.class);
		
		private FirebaseApp firebaseApp;
		
		public PushNotificationService(String firebaseServiceAccountKey) {		
			
			try {
				Resource resource = new ClassPathResource(firebaseServiceAccountKey);
		        InputStream inputStream = resource.getInputStream();
				FirebaseOptions options = new FirebaseOptions.Builder()
						.setCredentials(GoogleCredentials.fromStream(inputStream)).build();
				FirebaseApp serviceCenterFirebaseApp = FirebaseApp.initializeApp(options);
				this.firebaseApp = serviceCenterFirebaseApp;
				log.info("firebaseApp Created");
				
			} catch (FileNotFoundException e) {
			log.error("Error : PushNotificationService" + e);
			} catch (IOException e) {
			log.error("Error : PushNotificationService" + e);
			}
		}

		public void sendPushNotificationToDevice(String title, String message, int type, String target, String targetName, PushDevice device, boolean silent) throws Exception {
			sendPushNotificationToDevice(title, message, type, target, targetName, 1, device, silent);
		}

		public void sendPushNotificationToDevice(String title, String message, int type, String target, String targetName, int badgeCount, PushDevice device, boolean silent) throws Exception {
			Map<String, String> data = new HashMap<String, String>();
			data.put("target", target);
			data.put("targetName", targetName);
			sendPushNotificationToDevice(title, message, type, badgeCount, data, device, silent);
		}

		public void sendPushNotificationToDevice(String title, String message, int type, int badgeCount, Map<String, String> data, PushDevice device, boolean silent) throws Exception {

			PushNotification pushNotification = new PushNotification();
			pushNotification.setTitle(title);
			pushNotification.setMessage(message);
			pushNotification.setType(type);
			pushNotification.setBadgeCount(badgeCount);
			pushNotification.setData(data);

			sendPushNotification(pushNotification, device, silent);
		}
		
		public void sendPushNotificationToTopic(String title, String message, int type, String target, String targetName, String topic, boolean silent) throws Exception {
			sendPushNotificationToTopic(title, message, type, target, targetName, 1, topic, silent);
		}

		public void sendPushNotificationToTopic(String title, String message, int type, String target, String targetName, int badgeCount, String topic, boolean silent) throws Exception {
			Map<String, String> data = new HashMap<String, String>();
			data.put("target", target);
			data.put("targetName", targetName);
			sendPushNotificationToTopic(title, message, type, badgeCount, data, topic, silent);
		}

		public void sendPushNotificationToTopic(String title, String message, int type, int badgeCount, Map<String, String> data, String topic, boolean silent) throws Exception {

			PushNotification pushNotification = new PushNotification();
			pushNotification.setTitle(title);
			pushNotification.setMessage(message);
			pushNotification.setType(type);
			pushNotification.setBadgeCount(badgeCount);
			pushNotification.setData(data);

			sendPushNotificationToTopic(pushNotification, topic, silent);
		}

		public void sendPushNotification(List<PushNotification> pushNotifications, PushDevice device, boolean silent) throws Exception {
			if (pushNotifications != null) {
				for (PushNotification pushNotification : pushNotifications) {
					sendPushNotification(pushNotification, device, silent);
				}
			}
		}

		public void sendPushNotification(PushNotification pushNotification, PushDevice pushDevice, boolean silent) throws Exception {
			if (pushNotification != null && pushDevice != null) {
				if (pushDevice.getPushRegId()!=null) {
					pushNotificationToFCM(pushNotification, pushDevice.getPushRegId(), null, silent,pushDevice.getDeviceType());
					
				}
			}
		}
		
		
		public void sendPushNotificationToTopic(List<PushNotification> pushNotifications, String topic, boolean silent) throws Exception {
			if (pushNotifications != null) {
				for (PushNotification pushNotification : pushNotifications) {
					sendPushNotificationToTopic(pushNotification, topic, silent);
				}
			}
		}

		public void sendPushNotificationToTopic(PushNotification pushNotification, String topic, boolean silent) throws Exception {
			if (pushNotification != null && topic != null && topic.length() > 0) {
				pushNotificationToFCM(pushNotification, null, topic, silent,null);
				
			}
		}
		public void sendPushNotificationToMultipleUserWithSameMessageAndTitle(PushNotification pushNotification ,List<String> tokens) {
			List<List<String>> getPushNotificationTokens=NotificationUtils.getBatches(tokens, 500);
			getPushNotificationTokens.forEach(data->{
				try {
					this.pushNotificationToMultipleFCMWithSameMessageAndTitle(pushNotification,data);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			
		}

		public void pushNotificationToFCM(PushNotification pushNotification, String registrationToken, String topic, boolean silent,String deviceType) throws Exception {
			try {
				log.info("Enter into pushNotificationToFCM(-) of PushNotificationService");
				log.info("firebaseApp-->"+firebaseApp);
				pushNotification.getData().put("type", pushNotification.getType()+"");
				pushNotification.getData().put("title", pushNotification.getTitle());
				pushNotification.getData().put("body", pushNotification.getMessage());
				
				
				Builder builder = null;
				if (silent) {
					if(deviceType!=null&&deviceType.equalsIgnoreCase("android")) {
						log.info("Push-Notification for andriod");
					builder = Message.builder()
					    .setAndroidConfig(AndroidConfig.builder()
					        .setTtl(60 * 60 * 1000L) // 1 hour in milliseconds
					        .setPriority(AndroidConfig.Priority.HIGH)
					        .putAllData(pushNotification.getData())
					        .putData("intentData", pushNotification.getData().get("intentData"))
					        .build());
					}//if
					else if(deviceType!=null&&deviceType.equalsIgnoreCase("ios")) {
						log.info("Push-Notification for ios");
						builder = Message.builder()
						  .setApnsConfig(ApnsConfig.builder()
							        .putHeader("apns-priority", "10")
							        .setAps(Aps.builder()
							            .build())
							        .putAllCustomData(new HashMap<String, Object>(pushNotification.getData()))
							        .build());
					}//else if
					else {
						log.info("Push-Notification for Both");
						builder = Message.builder()
							    .setAndroidConfig(AndroidConfig.builder()
							        .setTtl(60 * 60 * 1000L) // 1 hour in milliseconds
							        .setPriority(AndroidConfig.Priority.HIGH)
							        .putAllData(pushNotification.getData())
							        .putData("intentData", pushNotification.getData().get("intentData"))
							        .build())
							     .setApnsConfig(ApnsConfig.builder()
							        .putHeader("apns-priority", "10")
							        .setAps(Aps.builder()
							            .build())
							        .putAllCustomData(new HashMap<String, Object>(pushNotification.getData()))
							        .build());
					}//else
					
				} else {
					if(deviceType!=null&&deviceType.equalsIgnoreCase("android")) {
						log.info("Push-Notification for Andriod");
					// See documentation on defining a message payload.
					builder = Message.builder()
					    .setAndroidConfig(AndroidConfig.builder()
					        .setTtl(60 * 60 * 1000L) // 1 hour in milliseconds
					        .setPriority(AndroidConfig.Priority.HIGH)
					        .setNotification(AndroidNotification.builder()
					            .setTitle(pushNotification.getTitle())
					            .setBody(pushNotification.getMessage())
					            .setSound("default")
					            .setIcon("ic_launcher_foreground")
					            .build())
					        .putAllData(pushNotification.getData())
					        .build());
					}//if
					else if(deviceType!=null&&deviceType.equalsIgnoreCase("ios")) {
						log.info("Push-Notification for ios"); 
					builder=Message.builder().setApnsConfig(ApnsConfig.builder()
					        .putHeader("apns-priority", "10")
					        .setAps(Aps.builder()
					            .setAlert(ApsAlert.builder()
					                .setTitle(pushNotification.getTitle())
					                .setBody(pushNotification.getMessage())
					                .build())
					            .setSound("default")
					            .setBadge(pushNotification.getBadgeCount() == null ? 0 : pushNotification.getBadgeCount())
					            .build())
					        .putAllCustomData(new HashMap<String, Object>(pushNotification.getData()))
					        .build());
				}//else if
					else {
						log.info(" Push-Notification for Both"); 
						builder = Message.builder()
							    .setAndroidConfig(AndroidConfig.builder()
							        .setTtl(60 * 60 * 1000L) // 1 hour in milliseconds
							        .setPriority(AndroidConfig.Priority.HIGH)
							        .setNotification(AndroidNotification.builder()
							            .setTitle(pushNotification.getTitle())
							            .setBody(pushNotification.getMessage())
							            .setSound("default")
							            .setIcon("ic_launcher_foreground")
							            .build())
							        .putAllData(pushNotification.getData())
							        .build())
							     .setApnsConfig(ApnsConfig.builder()
							        .putHeader("apns-priority", "10")
							        .setAps(Aps.builder()
							            .setAlert(ApsAlert.builder()
							                .setTitle(pushNotification.getTitle())
							                .setBody(pushNotification.getMessage())
							                .build())
							            .setSound("default")
							            .setBadge(pushNotification.getBadgeCount() == null ? 0 : pushNotification.getBadgeCount())
							            .build())
							        .putAllCustomData(new HashMap<String, Object>(pushNotification.getData()))
							        .build());
					  }//else
					
				}
			
				if (topic != null && topic.trim().length() > 0) {
					builder.setTopic(topic);
				} else if (registrationToken != null && registrationToken.trim().length() > 0) {
					log.info(" Push-Notification for Device->"+registrationToken); 
					builder.setToken(registrationToken);
				} else {
					return;
				}

				Message message = builder.build();
				log.info(" Push-Notification Message->"+message); 
				// Send a message to the device corresponding to the provided
				// registration token.
				log.info(" Push-Notification Third Party Api Invoked"); 
				String   response = FirebaseMessaging.getInstance(firebaseApp).send(message);	
				// Response is a message ID string.
				log.info("Successfully sent push message: " + response);
				
			} catch (Exception e) {
				log.info("Successfully sent push message: " + e);
				throw e;
			}
			
			log.info("Exist pushNotificationToFCM(-) of PushNotificationService");
			
		}
		
		public void pushNotificationToMultipleFCMWithSameMessageAndTitle(PushNotification pushNotification,List<String> tokens) throws Exception {
			try {
				log.info("Enter into pushNotificationToFCM(-) of PushNotificationService");
				log.info("firebaseApp-->"+firebaseApp);
				pushNotification.getData().put("title", pushNotification.getTitle());
				pushNotification.getData().put("body", pushNotification.getMessage());
				MulticastMessage message = MulticastMessage.builder()
			    		.setAndroidConfig(AndroidConfig.builder()
						        .setTtl(60 * 60 * 1000L) // 1 hour in milliseconds
						        .setPriority(AndroidConfig.Priority.HIGH)
						        .setNotification(AndroidNotification.builder()
						            .setTitle(pushNotification.getTitle())
						            .setBody(pushNotification.getMessage())
						            .setSound("default")
						            .setIcon("ic_launcher_foreground")
						            .build())
						        .putAllData(pushNotification.getData())
						        .build())
			    		 .setApnsConfig(ApnsConfig.builder()
							        .putHeader("apns-priority", "10")
							        .setAps(Aps.builder()
							            .setAlert(ApsAlert.builder()
							                .setTitle(pushNotification.getTitle())
							                .setBody(pushNotification.getMessage())
							                .build())
							            .setSound("default")
							            .setBadge(pushNotification.getBadgeCount() == null ? 0 : pushNotification.getBadgeCount())
							            .build())
							        .putAllCustomData(new HashMap<String, Object>(pushNotification.getData()))
							        .build()).addAllTokens(tokens).build();
					
				log.info(" Push-Notification Third Party Api Invoked"); 
				BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
				// Response is a message ID string.
				log.info("Successfully sent push message: " + response);
				
			} catch (Exception e) {
				log.info("Successfully sent push message: " + e);
				throw e;
			}
			
			log.info("Exist pushNotificationToFCM(-) of PushNotificationService");
			
		}
		
		public void pushNotificationToMultipleFCMWithDiffMessageAndTitle(List<PushNotification> pushNotifications) throws Exception {
			try {
				log.info("Enter       into pushNotificationToFCM(-) of PushNotificationService");
				log.info("firebaseApp-->"+firebaseApp);
				List<Message> messageBatch=new ArrayList<>();
				pushNotifications.stream().forEach(pushNotification->{
				pushNotification.getData().put("title", pushNotification.getTitle());
				pushNotification.getData().put("body", pushNotification.getMessage());
				Message message = Message.builder()
					    .setAndroidConfig(AndroidConfig.builder()
					        .setTtl(60 * 60 * 1000L) // 1 hour in milliseconds
					        .setPriority(AndroidConfig.Priority.HIGH)
					        .setNotification(AndroidNotification.builder()
					            .setTitle(pushNotification.getTitle())
					            .setBody(pushNotification.getMessage())
					            .setSound("default")
					            .setIcon("ic_launcher_foreground")
					            .build())
					        .putAllData(pushNotification.getData())
					        .build())
					     .setApnsConfig(ApnsConfig.builder()
					        .putHeader("apns-priority", "10")
					        .setAps(Aps.builder()
					            .setAlert(ApsAlert.builder()
					                .setTitle(pushNotification.getTitle())
					                .setBody(pushNotification.getMessage())
					                .build())
					            .setSound("default")
					            .setBadge(pushNotification.getBadgeCount() == null ? 0 : pushNotification.getBadgeCount())
					            .build())
					        .putAllCustomData(new HashMap<String, Object>(pushNotification.getData()))
					        .build()).setToken(pushNotification.getDeviceId()).build();
				messageBatch.add(message);
				 
				});
					
				log.info(" Push-Notification Third Party Api Invoked"); 
				BatchResponse response = FirebaseMessaging.getInstance().sendAll(messageBatch);
				// Response is a message ID string.
				log.info("Response(Failure Count) received from FCM: " + response.getFailureCount());
				log.info("Response(Success Count) received from FCM: " + response.getSuccessCount());
				for(SendResponse sendResponse: response.getResponses()) {
					log.info("Send Response: isSuccessful --> "+ sendResponse.isSuccessful());
					log.info("Send Response: Message Id --> "+ sendResponse.getMessageId());
					if(sendResponse.getException() != null) {
						log.info("Send Response: getException.errorCode --> "+ sendResponse.getException().getErrorCode());
						log.info("Send Response: getException.getLocalizedMessage --> "+ sendResponse.getException().getLocalizedMessage());
					}
				}
				log.info("Successfully sent push message: " + response);
				
			} catch (Exception e) {
				log.error("Exception:  " + e.getLocalizedMessage());
				log.error("Error in sending push notification from FCM: " + e);
				throw e;
			}
			
			log.info("Exit pushNotificationToFCM(-) of PushNotificationService");
			
		}
		
		
		
		
	}