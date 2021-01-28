package gov.naco.soch.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import gov.naco.soch.notification.service.NotificationService;
import gov.naco.soch.notification.service.WebUserNotificationService;

@Component
public class NotificationScheduler {

	@Autowired
	private WebUserNotificationService webNotificationService;

	@Autowired
	private NotificationService notificationService;

	@Value("${scheduler.job.notification.cleannotificationattachemnts.enabled}")
	private boolean cleanNotificationAttachemntsEnabled;
	
	private static final Logger logger = LoggerFactory.getLogger(NotificationScheduler.class);

	@Scheduled(cron = "${delete.cron}")
	// @Scheduled(cron = "0 0 0 * * ?")
	// @Scheduled(cron = "0 0/01 * * * ?")
	public void deleteWebUserNotificationsAfterXDays() {
		logger.warn("deleteWebUserNotifications is started!");
		if (webNotificationService.deleteWebUserNotifications()) {
			logger.warn("deleteWebUserNotifications is ended!");
		} else {
			logger.warn("deleteWebUserNotifications is failed");
		}
	}

	/* Job should run in every day midnight 12 AM */
	@Scheduled(cron = "0 0 0 * * ?")
	public void cleanNotificationAttachemnts() {
		if (cleanNotificationAttachemntsEnabled) {
			logger.warn("JOB--> cleanNotificationAttachemnts job started");
			boolean result = notificationService.cleanNotificationAttachemnts();
			logger.debug("Result :", result);
			logger.warn("JOB--> cleanNotificationAttachemnts job ended");
		} else {
			logger.info("JOB-->cleancaptchadata is disabled, so not running");
		}
	}
}
