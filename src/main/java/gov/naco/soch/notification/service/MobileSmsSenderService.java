package gov.naco.soch.notification.service;

import gov.naco.soch.notification.model.SendSmsModel;

public interface MobileSmsSenderService {
	public String sendSms(SendSmsModel sendSmsModel);
}
