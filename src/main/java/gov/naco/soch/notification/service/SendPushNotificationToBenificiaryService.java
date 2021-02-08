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
	private PillReminderTimeRepository pillReminderTimeRepository;
	
	public void sendPushNotificationToBenificiaryPillReminder() {
		List<Map<String,Object>> mapList=pillReminderTimeRepository.getBeneficiaryPillReminderDetailForReminder();
		appAndInAppNotificationService.sendPushNotificationToBenificiary(mapList);
		
	}
	public void sendPushNotificationForCD4Test() {
		List<Map<String,Object>> mapList=pillReminderTimeRepository.sendPushNotificationForCD4Test();
		appAndInAppNotificationService.sendPushNotificationToMultipleUser(mapList);
		
	}
	public void sendPushNotificationForVLTest() {
		List<Map<String,Object>> mapList=pillReminderTimeRepository.sendPushNotificationForVLTest();
		appAndInAppNotificationService.sendPushNotificationToMultipleUser(mapList);
		
	}
	public void sendPushNotificationForHIVReport() {
		List<Map<String,Object>> mapList=pillReminderTimeRepository.sendPushNotificationForHIVReport();
		appAndInAppNotificationService.sendPushNotificationToMultipleUser(mapList);
		
	}
	public void sendPushNotificationForVLAboveThousand() {
		List<Map<String,Object>> mapList=pillReminderTimeRepository.sendPushNotificationForVLAboveThousand();
		appAndInAppNotificationService.sendPushNotificationToMultipleUser(mapList);
		
	}
	public void sendPushNotificationForCD4LT350() {
		List<Map<String,Object>> mapList=pillReminderTimeRepository.sendPushNotificationForCD4LT350();
		appAndInAppNotificationService.sendPushNotificationToMultipleUser(mapList);
		
	}
	public void sendPushNotificationForMonthlyAdhrenceLT80() {
		List<Map<String,Object>> mapList=pillReminderTimeRepository.sendPushNotificationForMonthlyAdhrenceLT80();
		appAndInAppNotificationService.sendPushNotificationToMultipleUser(mapList);		
	}
	public void sendPushNotificationForOIReporting() {
		List<Map<String,Object>> mapList=pillReminderTimeRepository.sendPushNotificationForOIReporting();
		appAndInAppNotificationService.sendPushNotificationToMultipleUser(mapList);			
	}
	public void sendPushNotificationForArtDispensation() {
		List<Map<String,Object>> mapList=pillReminderTimeRepository.sendPushNotificationForArtDispensation();
		appAndInAppNotificationService.sendPushNotificationToMultipleUser(mapList);	
		
	}
	public void sendPushNotificationForCommDistAtTI() {
		List<Map<String,Object>> mapList=pillReminderTimeRepository.sendPushNotificationForCommDistAtTI();
		appAndInAppNotificationService.sendPushNotificationToMultipleUser(mapList);	
		
	}
	public void sendPushNotificationForOstDispensation() {
		List<Map<String,Object>> mapList=pillReminderTimeRepository.sendPushNotificationForOstDispensation();
		appAndInAppNotificationService.sendPushNotificationToMultipleUser(mapList);			
	}
	public void sendPushNotificationForSyphillisTestResult() {
		List<Map<String,Object>> mapList=pillReminderTimeRepository.sendPushNotificationForSyphillisTestResult();
		appAndInAppNotificationService.sendPushNotificationToMultipleUser(mapList);			
	}
	public void sendPushNotificationForStiRtiDiagnosis() {
		List<Map<String,Object>> mapList=pillReminderTimeRepository.sendPushNotificationForStiRtiDiagnosis();
		appAndInAppNotificationService.sendPushNotificationToMultipleUser(mapList);	
		
	}
	
	
	
		
	

}
