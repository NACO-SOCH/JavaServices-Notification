package gov.naco.soch.notification.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.naco.soch.dto.MiniMasterDto;
import gov.naco.soch.dto.NotificationEventSaveDto;
import gov.naco.soch.entity.MasterNotificationEventType;
import gov.naco.soch.entity.NotificationEvent;
import gov.naco.soch.notification.mapper.NotificationMapper;
import gov.naco.soch.notification.sender.EmailSenderService;
import gov.naco.soch.notification.sender.SmsSenderService;
import gov.naco.soch.notification.sender.WhatsAppSenderService;
import gov.naco.soch.projection.NotificationEventProjection;
import gov.naco.soch.projection.NotificationProjection;
import gov.naco.soch.projection.PlaceholderProjection;
import gov.naco.soch.repository.MasterNotificationEventTypeRepository;
import gov.naco.soch.repository.NotificationEventPlaceholderRepository;
import gov.naco.soch.repository.NotificationEventRepository;
import gov.naco.soch.util.CommonConstants;

@Service
@Transactional
public class NotificationService {
	
	private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
	
	@Autowired
	private NotificationEventRepository notificationEventRepository;
	@Autowired
	private NotificationEventPlaceholderRepository notificationEventPlaceholderRepository;
	
	@Autowired
	private MasterNotificationEventTypeRepository notificationEventTypeRepository;
	
	@Autowired
	private SmsSenderService smsService;
	@Autowired
	private WhatsAppSenderService whatsAppService;
	@Autowired
	private EmailSenderService emailService;

	private static String senderMail = "notification@soch-gov.in";
	private static final String ANGLE_BRACKET_OPEN = "[";
	private static final String ANGLE_BRACKET_CLOSED = "]";
	private static final String RECIPIENT_KEY = "recipient";

	public List<NotificationEventProjection> getEventList() {
		return notificationEventRepository.findAllProjectedByOrderByEventIdAsc();
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
		NotificationEvent event = null;
		Optional<NotificationEvent> eventOpt = notificationEventRepository.findByEventIdAndIsEnabled(eventId, true);
		if (eventOpt.isPresent()) {
			event = eventOpt.get();
		}
		if (event.getIsSpecific() != null && event.getIsSpecific()) {
			sendEmailToSpecificUsers(placeholderMap, event);
		} else {
			senderMail = event.getMasterNotificationEventType().getSenderEmail();
			System.out.println("Sender mail :"+senderMail);
			List<NotificationProjection> notificationDetails = notificationEventRepository.findAllUsersByRoles(eventId);
			List<PlaceholderProjection> placeholdersProjection = getPlaceHoldersForTheEvent(eventId);
			List<String> placeholders = placeholdersProjection.stream().map(PlaceholderProjection::getPlaceholder)
					.collect(Collectors.toList());
			notificationDetails.forEach(detail -> {
				String finalEmailTemplate = replacePlaceHolders(detail.getEmailTemplate(), placeholderMap,
						detail.getRecepient(), placeholders);
				String finalEmailSubject = replacePlaceHolders(detail.getEmailSubject(), placeholderMap,
						detail.getRecepient(), placeholders);
				try {
					if(!StringUtils.isBlank(detail.getEmailId())) {
						emailService.sendEmail(detail.getEmailId(), finalEmailSubject, finalEmailTemplate,senderMail);	
					}
				} catch (Exception e) {
					logger.error("Exception in sendEmail->{}",e);
				}
			});
		}
	}

	public void sendEmailToSpecificUsers(Map<String, Object> placeholderMap, NotificationEvent event) {
        if(event.getMasterNotificationEventType()!=null)
        senderMail = event.getMasterNotificationEventType().getSenderEmail();
		List<PlaceholderProjection> placeholdersProjection = getPlaceHoldersForTheEvent(event.getEventId());
		List<String> placeholders = placeholdersProjection.stream().map(PlaceholderProjection::getPlaceholder)
				.collect(Collectors.toList());
		String finalEmailTemplate = replacePlaceHolders(event.getEmailTemplate(), placeholderMap,
				String.valueOf(placeholderMap.get(CommonConstants.NOTIFICATION_SPECIFIC_RECIPIENT_NAME_PLACEHOLDER)),
				placeholders);
		String finalEmailSubject = replacePlaceHolders(event.getEmailSubject(), placeholderMap,
				String.valueOf(placeholderMap.get(CommonConstants.NOTIFICATION_SPECIFIC_RECIPIENT_NAME_PLACEHOLDER)),
				placeholders);
		List<String> toEmailList = (List<String>) placeholderMap
				.get(CommonConstants.NOTIFICATION_TO_SPECIFIC_EMAILS_PLACEHOLDER);
		System.out.println("Email Ids"+toEmailList);
		toEmailList.forEach(emailId -> {
			emailService.sendEmail(emailId, finalEmailSubject, finalEmailTemplate,senderMail);
		});

	}

	public void sendSms(Map<String, Object> placeholderMap, Long eventId) {
		List<NotificationProjection> notificationDetails = notificationEventRepository.findAllUsersByRoles(eventId);
		List<NotificationProjection> smsEnabledNotificationDetails = notificationDetails.stream()
				.filter(x -> x.getSmsEnabled() != null && x.getSmsEnabled() == true).collect(Collectors.toList());
		List<PlaceholderProjection> placeholdersProjection = getPlaceHoldersForTheEvent(eventId);
		List<String> placeholders = placeholdersProjection.stream().map(PlaceholderProjection::getPlaceholder)
				.collect(Collectors.toList());
		smsEnabledNotificationDetails.forEach(detail -> {
			String finalSmsTemplate = replacePlaceHolders(detail.getSmsTemplate(), placeholderMap,
					detail.getRecepient(), placeholders);
			// smsService.sendSms(detail.getMobileNumber(),finalSmsTemplate);
		});

	}

	public void sendWhatsapp(Map<String, Object> placeholderMap, Long eventId) {
		List<NotificationProjection> notificationDetails = notificationEventRepository.findAllUsersByRoles(eventId);
		List<NotificationProjection> whatsappEnabledNotificationDetails = notificationDetails.stream()
				.filter(x -> x.getWhatsappEnabled() != null && x.getWhatsappEnabled() == true)
				.collect(Collectors.toList());
		List<PlaceholderProjection> placeholdersProjection = getPlaceHoldersForTheEvent(eventId);
		List<String> placeholders = placeholdersProjection.stream().map(PlaceholderProjection::getPlaceholder)
				.collect(Collectors.toList());
		whatsappEnabledNotificationDetails.forEach(detail -> {
			String finalWhatsappTemplate = replacePlaceHolders(detail.getWhatsappTemplate(), placeholderMap,
					detail.getRecepient(), placeholders);
			// whatsAppService.sendWhatsApp(detail.getMobileNumber(),finalWhatsappTemplate);
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

	/**
	 * getEventTypeList : method to fetch notification event type list
	 * @return
	 */
	public List<MiniMasterDto> getEventTypeList() {
		
		List<MasterNotificationEventType> notificationTypes = notificationEventTypeRepository.findAll();
		List<MiniMasterDto> notificationTypesList = new ArrayList<MiniMasterDto>();
		if(notificationTypes!=null) {
		for(MasterNotificationEventType eventType : notificationTypes) {
			MiniMasterDto masterDto = new MiniMasterDto();
			masterDto.setId(eventType.getId());
			masterDto.setName(eventType.getName());
			notificationTypesList.add(masterDto);
		 }
		}
		return notificationTypesList;
	}

}
