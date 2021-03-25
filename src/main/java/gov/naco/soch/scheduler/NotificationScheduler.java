package gov.naco.soch.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.threeten.bp.Instant;

import gov.naco.soch.notification.service.SendPushNotificationToBenificiaryService;
import gov.naco.soch.notification.service.NotificationService;
import gov.naco.soch.notification.service.WebUserNotificationService;

@Component
public class NotificationScheduler {

	@Autowired
	private WebUserNotificationService webNotificationService;
	@Autowired
	private SendPushNotificationToBenificiaryService sendPushNotification;
	@Autowired
	@Qualifier("NotificationExecutor")
	private TaskExecutor taskExecutor;

	@Autowired
	private NotificationService notificationService;

	@Value("${scheduler.job.notification.cleannotificationattachemnts.enabled}")
	private boolean cleanNotificationAttachemntsEnabled;
	
	@Value("${scheduler.job.notification.mobileapp.pushnotifications.enabled}")
	private boolean mobileAppPushNotificationsEnabled;
	
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
	@Scheduled(cron = "0 * * * * *")
	public void sendPushNotificationToTheBenificiaryPillReminder() {
		logger.info("START - sendPushNotificationToTheBenificiaryPillReminder");
		if(mobileAppPushNotificationsEnabled) {
			taskExecutor.execute(new Runnable() {
				@Override
				public void run() {
					sendPushNotification.sendPushNotificationToBenificiaryPillReminder();
				}
			});
		}else {
			logger.info("JOB-->sendPushNotificationToTheBenificiaryPillReminder is disabled, so not running");
		}
	}
	
	@Scheduled(cron = "0 0/5 * * * *")
	public void sendPushNotificationForCD4Test() {
		logger.info("START - sendPushNotificationForCD4Test");
		if(mobileAppPushNotificationsEnabled) {
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				sendPushNotification.sendPushNotificationForCD4Test();
			}
		});
		}else {
			logger.info("JOB-->sendPushNotificationForCD4Test is disabled, so not running");
		}
	}
	
	@Scheduled(cron = "0 0/5 * * * *")
	public void sendPushNotificationForVLTest() {
		logger.info("START - sendPushNotificationForVLTest");
		if(mobileAppPushNotificationsEnabled) {
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				sendPushNotification.sendPushNotificationForVLTest();
			}
		});
		}else {
			logger.info("JOB-->sendPushNotificationForVLTest is disabled, so not running");
		}
	}
	
	@Scheduled(cron = "0 0/5 * * * *")
	public void sendPushNotificationForHIVReport() {
		logger.info("START - sendPushNotificationForHIVReport");
		if(mobileAppPushNotificationsEnabled) {
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				sendPushNotification.sendPushNotificationForHIVReport();
			}
		});
		}else {
			logger.info("JOB-->sendPushNotificationForHIVReport is disabled, so not running");
		}
	}
	
	@Scheduled(cron = "0 0/5 * * * *")
	public void sendPushNotificationForVLAboveThousand() {
		logger.info("START - sendPushNotificationForVLAboveThousand");
		if(mobileAppPushNotificationsEnabled) {
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				sendPushNotification.sendPushNotificationForVLAboveThousand();
			}
		});
		}else {
			logger.info("JOB-->sendPushNotificationForVLAboveThousand is disabled, so not running");
		}
	}
	
	@Scheduled(cron = "0 0/5 * * * *")
	public void sendPushNotificationForCD4LT350() {
		logger.info("START - sendPushNotificationForCD4LT350");
		if(mobileAppPushNotificationsEnabled) {
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				sendPushNotification.sendPushNotificationForCD4LT350();
			}
		});
		}else {
			logger.info("JOB-->sendPushNotificationForCD4LT350 is disabled, so not running");
		}
	}
	
	@Scheduled(cron = "0 0/5 * * * *")
	public void sendPushNotificationForMonthlyAdhrenceLT80() {
		logger.info("START - sendPushNotificationForMonthlyAdhrenceLT80");
		if(mobileAppPushNotificationsEnabled) {
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				sendPushNotification.sendPushNotificationForMonthlyAdhrenceLT80();
			}
		});
		}else {
			logger.info("JOB-->sendPushNotificationForMonthlyAdhrenceLT80 is disabled, so not running");
		}
	}
	
	@Scheduled(cron = "0 0/5 * * * *")
	public void sendPushNotificationForOIReporting() {
		logger.info("START - sendPushNotificationForOIReporting");
		if(mobileAppPushNotificationsEnabled) {
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				sendPushNotification.sendPushNotificationForOIReporting();
			}
		});
		}else {
			logger.info("JOB-->sendPushNotificationForOIReporting is disabled, so not running");
		}
	}
	
	@Scheduled(cron = "0 0/5 * * * *")
	public void sendPushNotificationForArtDispensation() {
		logger.info("START - sendPushNotificationForArtDispensation");
		if(mobileAppPushNotificationsEnabled) {
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				sendPushNotification.sendPushNotificationForArtDispensation();
			}
		});
		}else {
			logger.info("JOB-->sendPushNotificationForArtDispensation is disabled, so not running");
		}
	}
	
	@Scheduled(cron = "0 0/5 * * * *")
	public void sendPushNotificationForCommDistAtTI() {
		logger.info("START - sendPushNotificationForCommDistAtTI");
		if(mobileAppPushNotificationsEnabled) {
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				sendPushNotification.sendPushNotificationForCommDistAtTI();
			}
		});
		}else {
			logger.info("JOB-->sendPushNotificationForCommDistAtTI is disabled, so not running");
		}
	}
	
	@Scheduled(cron = "0 0/5 * * * *")
	public void sendPushNotificationForOstDispensation() {
		logger.info("START - sendPushNotificationForOstDispensation");
		if(mobileAppPushNotificationsEnabled) {
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				sendPushNotification.sendPushNotificationForOstDispensation();
			}
		});
		}else {
			logger.info("JOB-->sendPushNotificationForOstDispensation is disabled, so not running");
		}
	}
	
	@Scheduled(cron = "0 0/5 * * * *")
	public void sendPushNotificationForSyphillisTestResult() {
		logger.info("START - sendPushNotificationForSyphillisTestResult");
		if(mobileAppPushNotificationsEnabled) {
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				sendPushNotification.sendPushNotificationForSyphillisTestResult();
			}
		});
		}else {
			logger.info("JOB-->sendPushNotificationForSyphillisTestResult is disabled, so not running");
		}
	}
	
	@Scheduled(cron = "0 0/5 * * * *")
	public void sendPushNotificationForStiRtiDiagnosis() {
		logger.info("START - sendPushNotificationForStiRtiDiagnosis");
		if(mobileAppPushNotificationsEnabled) {
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				sendPushNotification.sendPushNotificationForStiRtiDiagnosis();
			}
		});
		}else {
			logger.info("JOB-->sendPushNotificationForStiRtiDiagnosis is disabled, so not running");
		}
	}
}

