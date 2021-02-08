package gov.naco.soch.notification.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.naco.soch.repository.PillReminderTimeRepository;

@Service
public class SendPushNotificationToBenificiaryService {
	
	@Autowired
	private AppAndInAppNotiticationService appAndInAppNotificationService;
	@Autowired
	private PillReminderTimeRepository pillReminderTimeRepositiry;
	
	public void sendPushNotificationToBenificiaryPillReminder() {
		List<Map<String,Object>> mapList=pillReminderTimeRepositiry.getBeneficiaryPillReminderDetailForReminder();
		appAndInAppNotificationService.sendPushNotificationToBanificiary(mapList);
		
	}
	
	
	
		
	

}
