package gov.naco.soch.notification.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import gov.naco.soch.notification.model.IntentData;
import gov.naco.soch.notification.model.NotificationResponse;
import gov.naco.soch.notification.model.NotificationSuccess;
import gov.naco.soch.notification.model.PushDevice;
import gov.naco.soch.notification.model.PushNotificationStatus;
@Service
public class AppAndInAppNotificationServiceImpl implements AppAndInAppNotiticationService{
	private static final Logger log = LoggerFactory.getLogger(AppAndInAppNotificationServiceImpl.class);
	
	@Autowired
	private AsyncPushNotificationService webSync;

	@Override
	public void sendAndSaveNotification(IntentData intentData,PushDevice pushDevice, Boolean persisted) {
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

	

	
	
	
		
	
	
	

}
