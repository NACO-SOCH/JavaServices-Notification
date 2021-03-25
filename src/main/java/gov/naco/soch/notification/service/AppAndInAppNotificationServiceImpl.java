package gov.naco.soch.notification.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import gov.naco.soch.entity.PushNotificationEntity;
import gov.naco.soch.notification.model.IntentData;
import gov.naco.soch.notification.model.NotificationSuccess;
import gov.naco.soch.notification.model.PushDevice;
import gov.naco.soch.notification.model.PushNotification;
import gov.naco.soch.repository.PushNotificationRepository;
import io.grpc.netty.shaded.io.netty.util.internal.StringUtil;
@Service
public class AppAndInAppNotificationServiceImpl implements AppAndInAppNotificationService{
	private static final Logger log = LoggerFactory.getLogger(AppAndInAppNotificationServiceImpl.class);
	
	@Autowired
	private AsyncPushNotificationService webSync;
	@Autowired
	private PushNotificationRepository pushNotificationRepository;
	
	public void sendPushNotificationToBenificiary(List<Map<String,Object>> list) {
		List<PushNotification> listPushNotifications=new ArrayList<>();
		list.forEach(map->{
			String deviceToken=(String)map.get("deviceToken");
			Boolean notificationType=(Boolean)map.get("isNotification");
			if(!StringUtil.isNullOrEmpty(deviceToken)&& notificationType) {
				String messsage=(String)map.get("notificationMessage");
				String eventName=(String)map.get("notificationTitle");
				String deviceType=(String)map.get("deviceOsType");
				IntentData intentData=new IntentData();
		        intentData.setEventName(eventName);
		        intentData.setMessage(messsage);
		        intentData.setContent(""+ (Timestamp)map.get("reminderTime"));
		        intentData.setNotificationId(Long.valueOf((Integer)map.get("reminderId")));
		        Map<String,String> data=new HashMap<>();
		        try {
		        data.put("intentData", new ObjectMapper().writeValueAsString(intentData));
		        }
		        catch(Exception e) {
		        	e.printStackTrace();
		        }
		        PushNotification pushes=new PushNotification();
		        pushes.setDeviceId(deviceToken);
		        pushes.setTitle(eventName);
		        pushes.setMessage(messsage);
		        pushes.setData(data);
		        listPushNotifications.add(pushes);
		        }
			    try {
					webSync.sendPushNotificationToMultipleUserWithDiffMessage(listPushNotifications);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				This below code was commented on 22ndFeb as directed by Adarsh to not save pill reminder push notifications in Db
//			    PushNotificationEntity entity = new PushNotificationEntity();
//			    entity.setBeneficiaryId(Long.valueOf((Integer)map.get("beneficiaryId")));
//			    entity.setNotificationHeader((String)map.get("notificationTitle"));
//			    entity.setNotificationMessage((String)map.get("notificationMessage"));
//			    pushNotificationRepository.save(entity);
		});
		
	}
	
	@Override
	public void sendPushNotificationToSingleUser(Map<String, Object> map) {
		String deviceToken=(String)map.get("deviceToken");
		Boolean notificationType=(Boolean)map.get("isNotification");
		if(!StringUtil.isNullOrEmpty(deviceToken)&& notificationType) {
		PushDevice pushDevice =new PushDevice();
		
		String messsage=(String)map.get("notificationMessage");
		String eventName=(String)map.get("notificationTitle");
		String deviceType=(String)map.get("deviceOsType");
		IntentData intentData=new IntentData();
        intentData.setEventName(eventName);
        intentData.setMessage(messsage);
        pushDevice.setPushRegId(deviceToken);
        pushDevice.setDeviceType(deviceType);
        this.sendAndSaveNotification(intentData, pushDevice, true);
      
        }
		
	}
	
	private void sendAndSaveNotification(IntentData intentData,PushDevice pushDevice, Boolean persisted) {
		log.info("Entering AppAndInAppNotiticationService sendAndSaveNotification::{}::persisted::{}",pushDevice,intentData);
        NotificationSuccess success = null;
        Map<String,String> jsonDataMap=new HashMap<String, String>();
        try {
        ObjectMapper mapper=new ObjectMapper();
        String jsonData=mapper.writeValueAsString(intentData);
        jsonDataMap.put("intentData", jsonData);
        	log.info("invoking webSync.sendPushNotification...........");
        webSync.sendPushNotification(intentData.getMessage(),intentData.getEventName(), 1,jsonDataMap, true,pushDevice ,false);
        log.info("invoked webSync.sendPushNotification...........");
        log.info("Exit AppAndInAppNotiticationService Successfully::{}",success);
        }
        catch(Exception e) {
        	 log.info("Exit AppAndInAppNotiticationService Exception {}",e.getMessage());
        }
	
}

	@Override
	public void sendPushNotificationToMultipleUser(List<Map<String, Object>> list) {
		if(!CollectionUtils.isEmpty(list)) {
			List<String> tokens=new ArrayList<>();
			tokens=list.stream().filter(data->(Boolean)data.get("isNotification")).map(data->(String)data.get("deviceToken")).collect(Collectors.toList());
			Map<String,Object> messageAndTitle=list.get(0);
			String title=(String)messageAndTitle.get("notificationTitle");
			String message=(String)messageAndTitle.get("notificationMessage");
			IntentData intentData=new IntentData();
	        intentData.setEventName(title);
	        intentData.setMessage(message);
	        try {
			Map<String,String> map=new HashMap<>();
			map.put("intentData", new ObjectMapper().writeValueAsString(intentData));
			webSync.sendPushNotificationToMultipleUserWithSameMessage(title,message,tokens,map) ;
	        }
	        catch(Exception e) {
	        	e.printStackTrace();
	        }
	        //Add notifications sent to push_notifications table
	        Boolean notificationTobeSaved=(Boolean)messageAndTitle.get("isNotification");
	        //log.info("Push Notification to be saved: {}", notificationTobeSaved);
	        if(notificationTobeSaved) {
		        PushNotificationEntity entity = new PushNotificationEntity();
			    entity.setBeneficiaryId(Long.valueOf((Integer)messageAndTitle.get("beneficiaryId")));
			    entity.setNotificationHeader(title);
			    entity.setNotificationMessage(message);
			    pushNotificationRepository.save(entity);
	        }
			
		}
		
	}

	@Override
	public List<PushNotificationEntity> getNotificationsForBeneficiary(Long beneficiaryId) {
		return pushNotificationRepository.getNotificationsForBeneficiary(beneficiaryId);
	}

	@Override
	public boolean deleteNotificationForBeneficiary(Long beneficiaryId, Long notificationId) {
		int deleted = pushNotificationRepository.deleteNotificationForBeneficiary(beneficiaryId, notificationId);
		if(deleted == 1) {
			return true;
		}
		return false;
	}


}
