package gov.naco.soch.notification.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.naco.soch.dto.NotificationEventSaveDto;
import gov.naco.soch.entity.NotificationEvent;
import gov.naco.soch.notification.mapper.NotificationMapper;
import gov.naco.soch.notification.sender.EmailSenderService;
import gov.naco.soch.notification.sender.SmsSenderService;
import gov.naco.soch.notification.sender.WhatsAppSenderService;
import gov.naco.soch.projection.NotificationEventProjection;
import gov.naco.soch.projection.NotificationProjection;
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
	@Autowired
	private SmsSenderService smsService;
	@Autowired
	private WhatsAppSenderService whatsAppService;
	@Autowired
	private EmailSenderService emailService;

	private static final String ANGLE_BRACKET_OPEN = "[";
	private static final String ANGLE_BRACKET_CLOSED = "]";
	private static final String RECIPIENT_KEY = "recipient";

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

	public void sendEmail(Map<String, Object> placeholderMap, Long eventId) {
		List<NotificationProjection> notificationDetails = notificationEventRepository.findAllUsersByRoles(eventId);
		List<PlaceholderProjection> placeholdersProjection = getPlaceHoldersForTheEvent(eventId);
		List<String> placeholders = placeholdersProjection.stream().map(PlaceholderProjection::getPlaceholder)
				.collect(Collectors.toList());
		notificationDetails.forEach(detail -> {
			String finalEmailTemplate = replacePlaceHolders(detail.getEmailTemplate(), placeholderMap,
					detail.getRecepient(), placeholders);
			System.out.println(finalEmailTemplate);
			emailService.sendEmail(detail.getEmailId(),detail.getEmailSubject(),finalEmailTemplate);
		});

	}

	public void sendSms(Map<String, Object> placeholderMap, Long eventId) {
		List<NotificationProjection> notificationDetails = notificationEventRepository.findAllUsersByRoles(eventId);
		List<NotificationProjection> smsEnabledNotificationDetails = notificationDetails.stream()
				.filter(x -> x.getSmsEnabled()!=null&& x.getSmsEnabled() == true).collect(Collectors.toList());
		List<PlaceholderProjection> placeholdersProjection = getPlaceHoldersForTheEvent(eventId);
		List<String> placeholders = placeholdersProjection.stream().map(PlaceholderProjection::getPlaceholder)
				.collect(Collectors.toList());
		smsEnabledNotificationDetails.forEach(detail -> {
			String finalSmsTemplate = replacePlaceHolders(detail.getSmsTemplate(), placeholderMap,
					detail.getRecepient(), placeholders);
			System.out.println(finalSmsTemplate);
			//smsService.sendSms(detail.getMobileNumber(),finalSmsTemplate);
		});

	}

	public void sendWhatsapp(Map<String, Object> placeholderMap, Long eventId) {
		List<NotificationProjection> notificationDetails = notificationEventRepository.findAllUsersByRoles(eventId);
		List<NotificationProjection> whatsappEnabledNotificationDetails = notificationDetails.stream()
				.filter(x -> x.getWhatsappEnabled()!=null&&x.getWhatsappEnabled() == true).collect(Collectors.toList());
		List<PlaceholderProjection> placeholdersProjection = getPlaceHoldersForTheEvent(eventId);
		List<String> placeholders = placeholdersProjection.stream().map(PlaceholderProjection::getPlaceholder)
				.collect(Collectors.toList());
		whatsappEnabledNotificationDetails.forEach(detail -> {
			String finalWhatsappTemplate = replacePlaceHolders(detail.getWhatsappTemplate(), placeholderMap,
					detail.getRecepient(), placeholders);
			System.out.println(finalWhatsappTemplate);
			//whatsAppService.sendWhatsApp(detail.getMobileNumber(),finalWhatsappTemplate);
		});

	}

	private String replacePlaceHolders(String template, Map<String, Object> placeholderMap, String recepient,
			List<String> placeholders) {
		Map<String, Object> values = new HashMap<>();
		for (Entry<String, Object> placeholderEntry : placeholderMap.entrySet()) {
			if (placeholders.contains(placeholderEntry.getKey())) {
				values.put(placeholderEntry.getKey(), placeholderEntry.getValue());
			}
		}
		values.put(RECIPIENT_KEY, recepient);
		return StringSubstitutor.replace(template, values, ANGLE_BRACKET_OPEN, ANGLE_BRACKET_CLOSED);

	}

}
