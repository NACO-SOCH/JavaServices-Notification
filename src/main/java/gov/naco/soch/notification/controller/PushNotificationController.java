package gov.naco.soch.notification.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import gov.naco.soch.entity.PushNotificationEntity;
import gov.naco.soch.notification.service.AppAndInAppNotificationService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PushNotificationController {
	private static final Logger log = LoggerFactory.getLogger(PushNotificationController.class);
	@Autowired
	AppAndInAppNotificationService service;
	
	@GetMapping("/mobile/pushnotifications/{beneficiaryId}")
	public List<PushNotificationEntity> getNotificationsForBeneficiary(@PathVariable Long beneficiaryId) {
		log.info("Entered PushNotificationController.getNotificationsForBeneficiary method for beneficiary:{}", beneficiaryId);
		return service.getNotificationsForBeneficiary(beneficiaryId);
	}
	
	@DeleteMapping("/mobile/pushnotifications/{beneficiaryId}/{notificationId}")
	public boolean deleteNotificationForBeneficiary(@PathVariable Long beneficiaryId, @PathVariable Long notificationId) {
		log.info("Entered PushNotificationController.deleteNotificationForBeneficiary method for beneficiary:{} and notificationId: {}", beneficiaryId, notificationId);
		return service.deleteNotificationForBeneficiary(beneficiaryId, notificationId);
	}
}
