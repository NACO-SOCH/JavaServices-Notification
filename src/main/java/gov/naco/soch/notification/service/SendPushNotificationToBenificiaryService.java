package gov.naco.soch.notification.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.naco.soch.notification.model.IntentData;
import gov.naco.soch.notification.model.PushDevice;
import gov.naco.soch.repository.PillReminderTimeRepository;
import io.grpc.netty.shaded.io.netty.util.internal.StringUtil;

@Service
public class SendPushNotificationToBenificiaryService {
	
	@Autowired
	private PillReminderTimeRepository pillReminderRepository;
	@Autowired
	private AppAndInAppNotiticationService appAndInAppNotificationService;
	
	public void sendPushNotificationToBanificiary() {
		List<Map<String,Object>> pillReminders=pillReminderRepository.getBeneficiaryPillReminderDetailForReminder();
		pillReminders.forEach(obj->{
			this.sendPushNotification(obj);
		});
		
	}
	
	public void sendPushNotification(Map<String,Object> map) {
		String deviceToken=(String)map.get("device_token");
		if(!StringUtil.isNullOrEmpty(deviceToken)) {
		PushDevice pushDevice =new PushDevice();
		String messsage="Reminder for your "+(String)map.get("regimen_name");
		String eventName="Pill Reminder";
		String eventId=""+(String)map.get("reminder_id");
		String screenName="Pill Reminder";
		String deviceType=(String)map.get("device_type");
		String content=(String)map.get("regimen_source");
		IntentData intentData=new IntentData();
        intentData.setEventID(eventId);
        intentData.setEventName(eventName);
        intentData.setMessage(messsage);
        intentData.setScreenName(screenName);
        intentData.setContent(content);
        pushDevice.setPushRegId(deviceToken);
        pushDevice.setDeviceType(deviceType);
        appAndInAppNotificationService.sendAndSaveNotification(intentData, pushDevice, true);
      
        }
		
	}

}
