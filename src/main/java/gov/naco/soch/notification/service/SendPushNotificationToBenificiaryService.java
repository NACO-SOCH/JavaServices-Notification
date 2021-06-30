package gov.naco.soch.notification.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.threeten.bp.LocalDate;

import gov.naco.soch.repository.PillReminderTimeRepository;
import gov.naco.soch.scheduler.NotificationScheduler;

@Service
public class SendPushNotificationToBenificiaryService {
	private static final Logger logger = LoggerFactory.getLogger(SendPushNotificationToBenificiaryService.class);
	
	@Autowired
	private AppAndInAppNotificationService appAndInAppNotificationService;
	@Autowired
	private PillReminderTimeRepository pillReminderTimeRepository;
	
	public void sendPushNotificationToBenificiaryPillReminder() {
		List<Map<String,Object>> mapList=pillReminderTimeRepository.getBeneficiaryPillReminderDetailForReminder();
		appAndInAppNotificationService.sendPushNotificationToBenificiary(mapList);
		
	}
	public void sendPushNotificationForCD4Test() {
		logger.info("Inside sendPushNotificationForCD4Test: CD4 Test");
		List<Map<String,Object>> mapList=pillReminderTimeRepository.sendPushNotificationForCD4Test();
		if(CollectionUtils.isEmpty(mapList)) {
			logger.info("Push Notification Data for CD4 Test is empty for the date: {}", LocalDate.now());
		}else {
			appAndInAppNotificationService.sendPushNotificationToMultipleUser(mapList);
		}
		
	}
	public void sendPushNotificationForVLTest() {
		logger.info("Inside sendPushNotificationForVLTest: VL Test");
		List<Map<String,Object>> mapList=pillReminderTimeRepository.sendPushNotificationForVLTest();
		if(CollectionUtils.isEmpty(mapList)) {
			logger.info("Push Notification Data for VL Test is empty for the date: {}", LocalDate.now());
		}else {
			appAndInAppNotificationService.sendPushNotificationToMultipleUser(mapList);
		}
		
	}
	public void sendPushNotificationForHIVReport() {
		logger.info("Inside sendPushNotificationForHIVReport: HIV Report");
		List<Map<String,Object>> mapList=pillReminderTimeRepository.sendPushNotificationForHIVReport();
		if(CollectionUtils.isEmpty(mapList)) {
			logger.info("Push Notification Data for HIV Report is empty for the date: {}", LocalDate.now());
		}else {
			appAndInAppNotificationService.sendPushNotificationToMultipleUser(mapList);
		}
		
	}
	public void sendPushNotificationForVLAboveThousand() {
		logger.info("Inside sendPushNotificationForVLAboveThousand: VL Above 1000");
		List<Map<String,Object>> mapList=pillReminderTimeRepository.sendPushNotificationForVLAboveThousand();
		if(CollectionUtils.isEmpty(mapList)) {
			logger.info("Push Notification Data for VL Above 1000 is empty for the date: {}", LocalDate.now());
		}else {
			appAndInAppNotificationService.sendPushNotificationToMultipleUser(mapList);
		}
		
	}
	public void sendPushNotificationForCD4LT350() {
		logger.info("Inside sendPushNotificationForCD4LT350: CD4 LT 350");
		List<Map<String,Object>> mapList=pillReminderTimeRepository.sendPushNotificationForCD4LT350();
		if(CollectionUtils.isEmpty(mapList)) {
			logger.info("Push Notification Data for CD4 LT 350 is empty for the date: {}", LocalDate.now());
		}else {
			appAndInAppNotificationService.sendPushNotificationToMultipleUser(mapList);
		}
		
	}
	public void sendPushNotificationForMonthlyAdhrenceLT80() {
		logger.info("Inside sendPushNotificationForMonthlyAdhrenceLT80: Monthly Adhrence LT 80");
		List<Map<String,Object>> mapList=pillReminderTimeRepository.sendPushNotificationForMonthlyAdhrenceLT80();
		if(CollectionUtils.isEmpty(mapList)) {
			logger.info("Push Notification Data for Monthly Adhrence LT 80 is empty for the date: {}", LocalDate.now());
		}else {
			appAndInAppNotificationService.sendPushNotificationToMultipleUser(mapList);
		}	
	}
	public void sendPushNotificationForOIReporting() {
		logger.info("Inside sendPushNotificationForOIReporting: OI Reporting");
		List<Map<String,Object>> mapList=pillReminderTimeRepository.sendPushNotificationForOIReporting();
		if(CollectionUtils.isEmpty(mapList)) {
			logger.info("Push Notification Data for OI Reporting is empty for the date: {}", LocalDate.now());
		}else {
			appAndInAppNotificationService.sendPushNotificationToMultipleUser(mapList);
		}			
	}
	public void sendPushNotificationForArtDispensation() {
		logger.info("Inside sendPushNotificationForArtDispensation: Art Dispensation");
		List<Map<String,Object>> mapList=pillReminderTimeRepository.sendPushNotificationForArtDispensation();
		if(CollectionUtils.isEmpty(mapList)) {
			logger.info("Push Notification Data for Art Dispensation is empty for the date: {}", LocalDate.now());
		}else {
			appAndInAppNotificationService.sendPushNotificationToMultipleUser(mapList);
		}
		
	}
	public void sendPushNotificationForCommDistAtTI() {
		logger.info("Inside sendPushNotificationForCommDistAtTI: Comm Dist At TI");
		List<Map<String,Object>> mapList=pillReminderTimeRepository.sendPushNotificationForCommDistAtTI();
		if(CollectionUtils.isEmpty(mapList)) {
			logger.info("Push Notification Data for Comm Dist At TI is empty for the date: {}", LocalDate.now());
		}else {
			appAndInAppNotificationService.sendPushNotificationToMultipleUser(mapList);
		}
		
	}
	public void sendPushNotificationForOstDispensation() {
		logger.info("Inside sendPushNotificationForOstDispensation: Ost Dispensation");
		List<Map<String,Object>> mapList=pillReminderTimeRepository.sendPushNotificationForOstDispensation();
		if(CollectionUtils.isEmpty(mapList)) {
			logger.info("Push Notification Data for Ost Dispensation is empty for the date: {}", LocalDate.now());
		}else {
			appAndInAppNotificationService.sendPushNotificationToMultipleUser(mapList);
		}		
	}
	public void sendPushNotificationForSyphillisTestResult() {
		logger.info("Inside sendPushNotificationForSyphillisTestResult: Syphillis Test Result");
		List<Map<String,Object>> mapList=pillReminderTimeRepository.sendPushNotificationForSyphillisTestResult();
		if(CollectionUtils.isEmpty(mapList)) {
			logger.info("Push Notification Data for Syphillis Test Result is empty for the date: {}", LocalDate.now());
		}else {
			appAndInAppNotificationService.sendPushNotificationToMultipleUser(mapList);
		}		
	}
	public void sendPushNotificationForStiRtiDiagnosis() {
		logger.info("Inside sendPushNotificationForStiRtiDiagnosis: Sti Rti Diagnosis");
		List<Map<String,Object>> mapList=pillReminderTimeRepository.sendPushNotificationForStiRtiDiagnosis();
		if(CollectionUtils.isEmpty(mapList)) {
			logger.info("Push Notification Data for Sti Rti Diagnosis is empty for the date: {}", LocalDate.now());
		}else {
			appAndInAppNotificationService.sendPushNotificationToMultipleUser(mapList);
		}
		
	}
}
