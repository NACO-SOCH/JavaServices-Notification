package gov.naco.soch.notification.service;

import gov.naco.soch.notification.model.SendSmsModel;

public interface MobileSmsSenderService {
	public String sendOtp(SendSmsModel sendSmsModel);
}
