package gov.naco.soch.notification.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.naco.soch.dto.NotificationEventSaveDto;
import gov.naco.soch.entity.NotificationEvent;
import gov.naco.soch.notification.mapper.NotificationMapper;
import gov.naco.soch.projection.NotificationEventProjection;
import gov.naco.soch.projection.PlaceholderProjection;
import gov.naco.soch.repository.NotificationEventPlaceholderRepository;
import gov.naco.soch.repository.NotificationEventRepository;


@Service
@Transactional
public class NotificationService {


	@Autowired
	private NotificationEventRepository notificationEventRepository;
	@Autowired
	private NotificationEventPlaceholderRepository notificationEventPlaceholderRepository;
	private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

	

	public List<NotificationEventProjection> getEventList() {
		return notificationEventRepository.findAllProjectedBy();
	}

	public List<PlaceholderProjection> getPlaceHoldersForTheEvent(Long eventId) {
		return notificationEventPlaceholderRepository.findByNotificationEvent_EventId(eventId);

	}

	public void saveEvent(NotificationEventSaveDto notificationEventSaveDto) {
		Optional<NotificationEvent> event_optional = notificationEventRepository
				.findById(notificationEventSaveDto.getEventId());
		if (event_optional.isPresent()) {
			NotificationEvent notificationEvent = NotificationMapper.mapToNotificationEvent(notificationEventSaveDto,
					event_optional.get());
			notificationEventRepository.save(notificationEvent);
		}

	}
	
	/*
	 * Send email service
	 */
	public boolean sendEmail(Map<String, Object> data,
			String eventId) {
		logger.debug("Entering into method sendEmail with eventId->{}:data-->{}:", eventId, data);
		return true;
	}



}
