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
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				sendPushNotification.sendPushNotificationToBenificiaryPillReminder();
			}
		});
	}
	
	@Scheduled(cron = "0 0/15 * * * *")
	public void sendPushNotificationForCD4Test() {
		logger.info("START - sendPushNotificationForCD4Test");
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				sendPushNotification.sendPushNotificationForCD4Test();
			}
		});
	}
	
	@Scheduled(cron = "0 0/15 * * * *")
	public void sendPushNotificationForVLTest() {
		logger.info("START - sendPushNotificationForVLTest");
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				sendPushNotification.sendPushNotificationForVLTest();
			}
		});
	   logger.info("sendPushNotificationForVLTest");	
	}
	
	@Scheduled(cron = "0 0/15 * * * *")
	public void sendPushNotificationForHIVReport() {
		logger.info("START - sendPushNotificationForHIVReport");
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				sendPushNotification.sendPushNotificationForHIVReport();
			}
		});
	}
	
	@Scheduled(cron = "0 0/15 * * * *")
	public void sendPushNotificationForVLAboveThousand() {
		logger.info("START - sendPushNotificationForVLAboveThousand");
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				sendPushNotification.sendPushNotificationForVLAboveThousand();
			}
		});
	}
	
	@Scheduled(cron = "0 0/15 * * * *")
	public void sendPushNotificationForCD4LT350() {
		logger.info("START - sendPushNotificationForCD4LT350");
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				sendPushNotification.sendPushNotificationForCD4LT350();
			}
		});
	}
	
	@Scheduled(cron = "0 0/15 * * * *")
	public void sendPushNotificationForMonthlyAdhrenceLT80() {
		logger.info("START - sendPushNotificationForCD4LT350");
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				sendPushNotification.sendPushNotificationForMonthlyAdhrenceLT80();
			}
		});
	}
	
	@Scheduled(cron = "0 0/15 * * * *")
	public void sendPushNotificationForOIReporting() {
		logger.info("START - sendPushNotificationForOIReporting");
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				sendPushNotification.sendPushNotificationForOIReporting();
			}
		});
	}
	
	@Scheduled(cron = "0 0/15 * * * *")
	public void sendPushNotificationForArtDispensation() {
		logger.info("START - sendPushNotificationForArtDispensation");
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				sendPushNotification.sendPushNotificationForArtDispensation();
			}
		});
	}
	
	@Scheduled(cron = "0 0/15 * * * *")
	public void sendPushNotificationForCommDistAtTI() {
		logger.info("START - sendPushNotificationForCommDistAtTI");
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				sendPushNotification.sendPushNotificationForCommDistAtTI();
			}
		});
	}
	
	@Scheduled(cron = "0 0/15 * * * *")
	public void sendPushNotificationForOstDispensation() {
		logger.info("START - sendPushNotificationForOstDispensation");
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				sendPushNotification.sendPushNotificationForOstDispensation();
			}
		});
	}
	
	@Scheduled(cron = "0 0/15 * * * *")
	public void sendPushNotificationForSyphillisTestResult() {
		logger.info("START - sendPushNotificationForSyphillisTestResult");
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				sendPushNotification.sendPushNotificationForSyphillisTestResult();
			}
		});
	}
	
	@Scheduled(cron = "0 0/15 * * * *")
	public void sendPushNotificationForStiRtiDiagnosis() {
		logger.info("START - sendPushNotificationForStiRtiDiagnosis");
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				sendPushNotification.sendPushNotificationForStiRtiDiagnosis();
			}
		});
	}
}

