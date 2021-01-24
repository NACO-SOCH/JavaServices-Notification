package gov.naco.soch.notification.service;

import gov.naco.soch.notification.model.IntentData;
import gov.naco.soch.notification.model.PushDevice;

public interface AppAndInAppNotiticationService {
	
	
	public void sendAndSaveNotification(IntentData intentData,PushDevice pushDevice,Boolean value);
	
	

}
