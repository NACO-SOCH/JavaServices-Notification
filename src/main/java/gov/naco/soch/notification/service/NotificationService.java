package gov.naco.soch.notification.service;

import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class NotificationService {


	private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

	/*
	 * Send email service
	 */
	public boolean sendEmail(Map<String, Object> data,
			String eventId) {
		logger.debug("Entering into method sendEmail with eventId->{}:data-->{}:", eventId, data);
		return true;
	}


}
