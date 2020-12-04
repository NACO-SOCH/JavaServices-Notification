package gov.naco.soch.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import gov.naco.soch.notification.service.WebUserNotificationService;


@Component
public class NotificationScheduler {

	@Autowired
	private WebUserNotificationService webNotificationService;
	private static final Logger logger = LoggerFactory.getLogger(NotificationScheduler.class);
	
	@Scheduled(cron = "${delete.cron}")
   // @Scheduled(cron = "0 0 0 * * ?")
	//@Scheduled(cron = "0 0/01 * * * ?")
	public void deleteWebUserNotificationsAfterXDays() {
		logger.warn("deleteWebUserNotifications is started!");
		if(webNotificationService.deleteWebUserNotifications()) {
		logger.warn("deleteWebUserNotifications is ended!");
		}
		else {
	   logger.warn("deleteWebUserNotifications is failed");	
		}
	}
}
