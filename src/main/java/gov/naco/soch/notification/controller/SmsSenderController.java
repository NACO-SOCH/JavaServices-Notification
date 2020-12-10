package gov.naco.soch.notification.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import gov.naco.soch.notification.model.SendSmsModel;
import gov.naco.soch.notification.service.MobileSmsSenderService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SmsSenderController {
	private static final Logger logger = LoggerFactory.getLogger(SmsSenderController.class);
	@Autowired
	MobileSmsSenderService mobileSmsSenderService;
	@PostMapping("/mobile/sendsms")
	public boolean sendSms(@RequestBody SendSmsModel sendSmsModel) {
		logger.debug("Entered sendSms Method for mobile");
		mobileSmsSenderService.sendSms(sendSmsModel);
		return true;
	}
}
