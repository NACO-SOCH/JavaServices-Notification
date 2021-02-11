//package gov.naco.soch.notification.controller;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import gov.naco.soch.notification.model.SendSmsModel;
//import gov.naco.soch.notification.service.MobileSmsSenderService;
//
//@RestController
//@CrossOrigin(origins = "*", allowedHeaders = "*")
//public class SmsSenderController {
//	private static final Logger log = LoggerFactory.getLogger(SmsSenderController.class);
//	@Autowired
//	MobileSmsSenderService mobileSmsSenderService;
//	@PostMapping("/mobile/general/send-otp")
//	public String sendOtp(@RequestBody SendSmsModel sendSmsModel) {
//		log.info("Entered SmsSenderController.sendOtp() method for mobile");
//		return mobileSmsSenderService.sendOtp(sendSmsModel);
//	}
//}
