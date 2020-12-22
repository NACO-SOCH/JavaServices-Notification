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
import org.springframework.util.CollectionUtils;

import gov.naco.soch.dto.MiniMasterDto;
import gov.naco.soch.dto.NotificationEventSaveDto;
import gov.naco.soch.entity.Facility;
import gov.naco.soch.entity.MasterNotificationEventType;
import gov.naco.soch.entity.NotificationEvent;
import gov.naco.soch.entity.NotificationEventRole;
import gov.naco.soch.entity.UserMaster;
import gov.naco.soch.notification.mapper.NotificationMapper;
import gov.naco.soch.notification.sender.EmailSenderService;
import gov.naco.soch.notification.sender.SmsSenderService;
import gov.naco.soch.notification.sender.WhatsAppSenderService;
import gov.naco.soch.projection.NotificationEventProjection;
import gov.naco.soch.projection.NotificationProjection;
import gov.naco.soch.projection.PlaceholderProjection;
import gov.naco.soch.repository.FacilityRepository;
import gov.naco.soch.repository.MasterNotificationEventTypeRepository;
import gov.naco.soch.repository.NotificationEventPlaceholderRepository;
import gov.naco.soch.repository.NotificationEventRepository;
import gov.naco.soch.repository.NotificationEventRoleRepository;
import gov.naco.soch.repository.UserMasterRepository;
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
	private FacilityRepository facilityRepository;

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private NotificationEventRoleRepository notificationEventRoleRepository;

	@Autowired
	private SmsSenderService smsService;
	@Autowired
	private WhatsAppSenderService whatsAppService;
	@Autowired
	private EmailSenderService emailService;

	private static String senderMail = "soch.notification@naco.gov.in";
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

//	public void sendEmail(Map<String, Object> placeholderMap, Long eventId) {
//		logger.debug("Inside of sendEmail() : NotificationService");
//		NotificationEvent event = null;
//		Optional<NotificationEvent> eventOpt = notificationEventRepository.findByEventIdAndIsEnabled(eventId, true);
//		if (eventOpt.isPresent()) {
//			event = eventOpt.get();
//		}
//		if (event.getIsSpecific() != null && event.getIsSpecific()) {
//			logger.debug("Inside of if (event.getIsSpecific() != null && event.getIsSpecific()) : NotificationService");
//			sendEmailToSpecificUsers(placeholderMap, event);
//		} else {
//			if(event.getMasterNotificationEventType()!=null) {
//				senderMail = event.getMasterNotificationEventType().getSenderEmail();	
//			}
//			List<NotificationProjection> notificationDetails = notificationEventRepository.findAllUsersByRoles(eventId);
//			List<PlaceholderProjection> placeholdersProjection = getPlaceHoldersForTheEvent(eventId);
//			List<String> placeholders = placeholdersProjection.stream().map(PlaceholderProjection::getPlaceholder)
//					.collect(Collectors.toList());
//			notificationDetails.forEach(detail -> {
//				String finalEmailTemplate = replacePlaceHolders(detail.getEmailTemplate(), placeholderMap,
//						detail.getRecepient(), placeholders);
//				String finalEmailSubject = replacePlaceHolders(detail.getEmailSubject(), placeholderMap,
//						detail.getRecepient(), placeholders);
//				try {
//					if(!StringUtils.isBlank(detail.getEmailId())) {
//						logger.info("Going to call emailService.sendEmail with eventId-->{}: detail.getEmailId()-->{}:",eventId, detail.getEmailId());
//						emailService.sendEmail(detail.getEmailId(), finalEmailSubject, finalEmailTemplate,senderMail);	
//						logger.info("Called emailService.sendEmail with eventId-->{}: detail.getEmailId()-->{}:",eventId, detail.getEmailId());
//					}
//				} catch (Exception e) {
//					logger.error("Exception in sendEmail->{}",e);
//				}
//			});
//		}
//	}

	public void sendEmail(Map<String, Object> placeholderMap, Long eventId) {
		logger.debug("Inside of sendEmail() : NotificationService");
		NotificationEvent event = null;
		Optional<NotificationEvent> eventOpt = notificationEventRepository.findByEventIdAndIsEnabled(eventId, true);
		if (eventOpt.isPresent()) {
			event = eventOpt.get();
		if (event.getIsSpecific() != null && event.getIsSpecific()) {
			logger.debug("Inside of if (event.getIsSpecific() != null && event.getIsSpecific()) : NotificationService");
			sendEmailToSpecificUsers(placeholderMap, event);
		} else {
			if (event.getMasterNotificationEventType() != null) {
				senderMail = event.getMasterNotificationEventType().getSenderEmail();
				logger.info("############## SENDER MAIL ID  :"+senderMail);
			}
			List<NotificationProjection> notificationDetails = notificationEventRepository.findAllUsersByRoles(eventId);
			List<PlaceholderProjection> placeholdersProjection = getPlaceHoldersForTheEvent(eventId);
			List<String> placeholders = placeholdersProjection.stream().map(PlaceholderProjection::getPlaceholder)
					.collect(Collectors.toList());

			if (placeholderMap.containsKey(CommonConstants.NOTIFICATION_PLACEHOLDER_FACILITY)
					&& placeholderMap.get(CommonConstants.NOTIFICATION_PLACEHOLDER_FACILITY) != null) {

				Long facilityId = ((Integer) placeholderMap.get(CommonConstants.NOTIFICATION_PLACEHOLDER_FACILITY))
						.longValue();
				Optional<Facility> facilityOpt = facilityRepository.findById(facilityId);
				if (facilityOpt.isPresent()) {
					Facility f1 = facilityOpt.get();
					Long ft1 = f1.getFacilityType().getId();

					List<NotificationEventRole> eventRoles = notificationEventRoleRepository
							.findEventRolesByEventId(eventId);
					if (!CollectionUtils.isEmpty(eventRoles)) {

						Map<String, String> eMailIdsList = new HashMap<>();

						for (NotificationEventRole er : eventRoles) {

							Long ft2 = er.getRole().getFacilityType().getId();

							if (ft1.equals(ft2)) {
								List<UserMaster> users = userMasterRepository
										.findUsersByFacilityIdAndFacilityTypeIdAndRoleId(f1.getId(), ft2,
												er.getRole().getId());
								Map<String, String> eMailIds = users.stream()
										.collect(Collectors.toMap(UserMaster::getFirstname, UserMaster::getEmail));
								if (!CollectionUtils.isEmpty(eMailIds)) {
									eMailIdsList.putAll(eMailIds);
								}
							} else if ((!ft1.equals(ft2)) && ft2.equals(2L)) {
								if (f1.getSacsId() != null) {
									List<UserMaster> users = userMasterRepository
											.findUsersByFacilityIdAndRole(f1.getSacsId(), er.getRole().getId());
									Map<String, String> eMailIds = users.stream()
											.collect(Collectors.toMap(UserMaster::getFirstname, UserMaster::getEmail));
									if (!CollectionUtils.isEmpty(eMailIds)) {
										eMailIdsList.putAll(eMailIds);
									}
								}
							} else if ((!ft1.equals(ft2)) && ft2.equals(1L)) {
								List<UserMaster> users = userMasterRepository.findUsersByFacilityTypeIdAndRoleId(ft2,
										er.getRole().getId());
								Map<String, String> eMailIds = users.stream()
										.collect(Collectors.toMap(UserMaster::getFirstname, UserMaster::getEmail));
								if (!CollectionUtils.isEmpty(eMailIds)) {
									eMailIdsList.putAll(eMailIds);
								}
							}
						}

						if (!CollectionUtils.isEmpty(eMailIdsList)) {

							for (Map.Entry<String, String> entry : eMailIdsList.entrySet()) {
								String finalEmailTemplate = replacePlaceHolders(event.getEmailTemplate(),
										placeholderMap, entry.getKey(), placeholders);
								String finalEmailSubject = replacePlaceHolders(event.getEmailSubject(), placeholderMap,
										entry.getKey(), placeholders);
								try {
									if (!StringUtils.isBlank(entry.getValue())) {
										logger.info(
												"Going to call emailService.sendEmail with eventId-->{}: detail.getEmailId()-->{}:",
												eventId, entry.getValue());
										emailService.sendEmail(entry.getValue(), finalEmailSubject, finalEmailTemplate,
												senderMail);
										logger.info(
												"Called emailService.sendEmail with eventId-->{}: detail.getEmailId()-->{}:",
												eventId, entry.getValue());
									}
								} catch (Exception e) {
									logger.error("Exception in sendEmail->{}", e);
								}
							}
						}
					}
				}
			} else {
				notificationDetails.forEach(detail -> {
					String finalEmailTemplate = replacePlaceHolders(detail.getEmailTemplate(), placeholderMap,
							detail.getRecepient(), placeholders);
					String finalEmailSubject = replacePlaceHolders(detail.getEmailSubject(), placeholderMap,
							detail.getRecepient(), placeholders);
					try {
						if (!StringUtils.isBlank(detail.getEmailId())) {
							logger.info(
									"Going to call emailService.sendEmail with eventId-->{}: detail.getEmailId()-->{}:",
									eventId, detail.getEmailId());
							emailService.sendEmail(detail.getEmailId(), finalEmailSubject, finalEmailTemplate,
									senderMail);
							logger.info("Called emailService.sendEmail with eventId-->{}: detail.getEmailId()-->{}:",
									eventId, detail.getEmailId());
						}
					} catch (Exception e) {
						logger.error("Exception in sendEmail->{}", e);
					}
				});
			}
		 }
		}
	}

	public void sendEmailToSpecificUsers(Map<String, Object> placeholderMap, NotificationEvent event) {
		logger.info(
				"Iniside of sendEmailToSpecificUsers(Map<String, Object> placeholderMap, NotificationEvent event) : NotificationService! ");
		if (event.getMasterNotificationEventType() != null)
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
		logger.info("Email Ids-->{}:", toEmailList);
		toEmailList.forEach(emailId -> {
			logger.info("Going to call SPECIFIC emailService.sendEmail with eventId-->{}: emailId-->{}:",
					event.getEventId(), emailId);
			emailService.sendEmail(emailId, finalEmailSubject, finalEmailTemplate, senderMail);
			logger.info("Called SPECIFIC emailService.sendEmail with eventId-->{}: emailId-->{}:", event.getEventId(),
					emailId);
		});

	}

//	public void sendSms(Map<String, Object> placeholderMap, Long eventId) {
//		List<NotificationProjection> notificationDetails = notificationEventRepository.findAllUsersByRoles(eventId);
//		List<NotificationProjection> smsEnabledNotificationDetails = notificationDetails.stream()
//				.filter(x -> x.getSmsEnabled() != null && x.getSmsEnabled() == true).collect(Collectors.toList());
//		List<PlaceholderProjection> placeholdersProjection = getPlaceHoldersForTheEvent(eventId);
//		List<String> placeholders = placeholdersProjection.stream().map(PlaceholderProjection::getPlaceholder)
//				.collect(Collectors.toList());
//		smsEnabledNotificationDetails.forEach(detail -> {
//			String finalSmsTemplate = replacePlaceHolders(detail.getSmsTemplate(), placeholderMap,
//					detail.getRecepient(), placeholders);
//			smsService.sendSms(detail.getMobileNumber(), finalSmsTemplate);
//		});
//
//	}

	public void sendSms(Map<String, Object> placeholderMap, Long eventId) {
		List<NotificationProjection> notificationDetails = notificationEventRepository.findAllUsersByRoles(eventId);
		List<NotificationProjection> smsEnabledNotificationDetails = notificationDetails.stream()
				.filter(x -> x.getSmsEnabled() != null && x.getSmsEnabled() == true).collect(Collectors.toList());
		List<PlaceholderProjection> placeholdersProjection = getPlaceHoldersForTheEvent(eventId);
		List<String> placeholders = placeholdersProjection.stream().map(PlaceholderProjection::getPlaceholder)
				.collect(Collectors.toList());

		NotificationEvent event = null;
		Optional<NotificationEvent> eventOpt = notificationEventRepository.findByEventIdAndIsEnabled(eventId, true);
		//For specific users
		logger.info("placeholderMap.get(CommonConstants.NOTIFICATION_SPECIFIC_PHONE_NUMBERS_PLACEHOLDER) :"+placeholderMap.get(CommonConstants.NOTIFICATION_SPECIFIC_PHONE_NUMBERS_PLACEHOLDER));
		logger.info("eventOpt.isPresent()  :"+eventOpt.isPresent() );
		logger.info("eventOpt.get().getIsSpecific() :"+eventOpt.get().getIsSpecific());
		if( placeholderMap.get(CommonConstants.NOTIFICATION_SPECIFIC_PHONE_NUMBERS_PLACEHOLDER) != null &&
				eventOpt.isPresent() && eventOpt.get().getIsSpecific() == true) {
			logger.info("****************************** Inside of if(eventOpt.isPresent()) **********************");
			logger.debug("Event place holders :"+eventOpt.get().getNotificationEventPlaceholders());
			sendSMSToSpecificUsers(placeholderMap, eventOpt.get());
			
		}
		//For role users
		else {
		logger.info("##################### Inside of ELSE : if( placeholderMap.get(CommonConstants.NOTIFICATION_SPECIFIC_PHONE_NUMBERS_PLACEHOLDER) != null &&\r\n" + 
				"				eventOpt.isPresent() && eventOpt.get().getIsSpecific() == true) { ##########################");
		if (eventOpt.isPresent() && placeholderMap.containsKey(CommonConstants.NOTIFICATION_PLACEHOLDER_FACILITY)
				&& placeholderMap.get(CommonConstants.NOTIFICATION_PLACEHOLDER_FACILITY) != null) {

			event = eventOpt.get();

			Long facilityId = ((Integer) placeholderMap.get(CommonConstants.NOTIFICATION_PLACEHOLDER_FACILITY))
					.longValue();
			Optional<Facility> facilityOpt = facilityRepository.findById(facilityId);
			if (facilityOpt.isPresent()) {
				Facility f1 = facilityOpt.get();
				Long ft1 = f1.getFacilityType().getId();

				List<NotificationEventRole> eventRoles = notificationEventRoleRepository
						.findEventRolesByEventId(eventId);
				if (!CollectionUtils.isEmpty(eventRoles)) {

					Map<String, String> mobileNumbersList = new HashMap<>();

					for (NotificationEventRole er : eventRoles) {

						Long ft2 = er.getRole().getFacilityType().getId();

						Map<String, String> userMobileNumberList = getEventRoleUserMobileNumber(f1, ft1, er, ft2);
						if (!CollectionUtils.isEmpty(userMobileNumberList)) {
							mobileNumbersList.putAll(userMobileNumberList);
						}
					}

					if (!CollectionUtils.isEmpty(mobileNumbersList)) {

						for (Map.Entry<String, String> entry : mobileNumbersList.entrySet()) {
							
							String finalSmsTemplate = replacePlaceHolders(event.getSmsTemplate(), placeholderMap,
									entry.getKey(), placeholders);
							try {
								if (!StringUtils.isBlank(entry.getValue())) {
									logger.info(
											"Going to call smsService.sendSms with eventId-->{}: detail.getRecepient()-->{}:",
											eventId, entry.getValue());
									smsService.sendSms(entry.getValue(), finalSmsTemplate);
									logger.info(
											"Called smsService.sendSms with eventId-->{}: detail.getRecepient()-->{}:",
											eventId, entry.getValue());
								}
							} catch (Exception e) {
								logger.error("Exception in sendSms->{}", e);
							}
						}
					}
				}
			}
		} else {
			smsEnabledNotificationDetails.forEach(detail -> {
				String finalSmsTemplate = replacePlaceHolders(detail.getSmsTemplate(), placeholderMap,
						detail.getRecepient(), placeholders);
				smsService.sendSms(detail.getMobileNumber(), finalSmsTemplate);
			});
		}
		}
	}

private void sendSMSToSpecificUsers(Map<String, Object> placeholderMap, NotificationEvent event) {
	logger.info("Inside of sendSMSToSpecificUsers(Map<String, Object> placeholderMap, NotificationEvent event) ");
	List<PlaceholderProjection> placeholdersProjection = getPlaceHoldersForTheEvent(event.getEventId());
	List<String> placeholders = placeholdersProjection.stream().map(PlaceholderProjection::getPlaceholder)
			.collect(Collectors.toList());
	String finalSmsTemplate = replacePlaceHolders(event.getSmsTemplate(), placeholderMap,
			String.valueOf(placeholderMap.get(CommonConstants.NOTIFICATION_SPECIFIC_RECIPIENT_NAME_PLACEHOLDER)),
			placeholders);
	List<String> toPhoneNumbers = (List<String>) placeholderMap
			.get(CommonConstants.NOTIFICATION_SPECIFIC_PHONE_NUMBERS_PLACEHOLDER);
	for(String phoneNumber : toPhoneNumbers) {
		logger.info("Going to call smsService.sendSms : phone number :"+phoneNumber+" Message :"+finalSmsTemplate);
		smsService.sendSms(phoneNumber, finalSmsTemplate);
		logger.info("After smsService.sendSms is called!");
	}
}

//	public void sendWhatsapp(Map<String, Object> placeholderMap, Long eventId) {
//		List<NotificationProjection> notificationDetails = notificationEventRepository.findAllUsersByRoles(eventId);
//		List<NotificationProjection> whatsappEnabledNotificationDetails = notificationDetails.stream()
//				.filter(x -> x.getWhatsappEnabled() != null && x.getWhatsappEnabled() == true)
//				.collect(Collectors.toList());
//		List<PlaceholderProjection> placeholdersProjection = getPlaceHoldersForTheEvent(eventId);
//		List<String> placeholders = placeholdersProjection.stream().map(PlaceholderProjection::getPlaceholder)
//				.collect(Collectors.toList());
//		whatsappEnabledNotificationDetails.forEach(detail -> {
//			String finalWhatsappTemplate = replacePlaceHolders(detail.getWhatsappTemplate(), placeholderMap,
//					detail.getRecepient(), placeholders);
//			// whatsAppService.sendWhatsApp(detail.getMobileNumber(),finalWhatsappTemplate);
//		});
//
//	}

	public void sendWhatsapp(Map<String, Object> placeholderMap, Long eventId) {
		List<NotificationProjection> notificationDetails = notificationEventRepository.findAllUsersByRoles(eventId);
		List<NotificationProjection> whatsappEnabledNotificationDetails = notificationDetails.stream()
				.filter(x -> x.getWhatsappEnabled() != null && x.getWhatsappEnabled() == true)
				.collect(Collectors.toList());
		List<PlaceholderProjection> placeholdersProjection = getPlaceHoldersForTheEvent(eventId);
		List<String> placeholders = placeholdersProjection.stream().map(PlaceholderProjection::getPlaceholder)
				.collect(Collectors.toList());

		NotificationEvent event = null;
		Optional<NotificationEvent> eventOpt = notificationEventRepository.findByEventIdAndIsEnabled(eventId, true);
		if (eventOpt.isPresent() && placeholderMap.containsKey(CommonConstants.NOTIFICATION_PLACEHOLDER_FACILITY)
				&& placeholderMap.get(CommonConstants.NOTIFICATION_PLACEHOLDER_FACILITY) != null) {

			event = eventOpt.get();

			Long facilityId = ((Integer) placeholderMap.get(CommonConstants.NOTIFICATION_PLACEHOLDER_FACILITY))
					.longValue();
			Optional<Facility> facilityOpt = facilityRepository.findById(facilityId);
			if (facilityOpt.isPresent()) {
				Facility f1 = facilityOpt.get();
				Long ft1 = f1.getFacilityType().getId();

				List<NotificationEventRole> eventRoles = notificationEventRoleRepository
						.findEventRolesByEventId(eventId);
				if (!CollectionUtils.isEmpty(eventRoles)) {

					Map<String, String> mobileNumbersList = new HashMap<>();

					for (NotificationEventRole er : eventRoles) {

						Long ft2 = er.getRole().getFacilityType().getId();

						Map<String, String> userMobileNumberList = getEventRoleUserMobileNumber(f1, ft1, er, ft2);
						if (!CollectionUtils.isEmpty(userMobileNumberList)) {
							mobileNumbersList.putAll(userMobileNumberList);
						}
					}

					if (!CollectionUtils.isEmpty(mobileNumbersList)) {
						for (Map.Entry<String, String> entry : mobileNumbersList.entrySet()) {

							String finalWhatsappTemplate = replacePlaceHolders(event.getWhatsappTemplate(),
									placeholderMap, entry.getValue(), placeholders);

							try {
								if (!StringUtils.isBlank(entry.getValue())) {
									logger.info(
											"Going to call whatsAppService.sendWhatsApp with eventId-->{}: detail.getRecepient()-->{}:",
											eventId, entry.getValue());
//									whatsAppService.sendWhatsApp(entry.getValue(), finalWhatsappTemplate);
									logger.info(
											"Called whatsAppService.sendWhatsApp with eventId-->{}: detail.getRecepient()-->{}:",
											eventId, entry.getValue());
								}
							} catch (Exception e) {
								logger.error("Exception in sendSms->{}", e);
							}
						}
					}
				}
			} else {
				whatsappEnabledNotificationDetails.forEach(detail -> {
					String finalWhatsappTemplate = replacePlaceHolders(detail.getWhatsappTemplate(), placeholderMap,
							detail.getRecepient(), placeholders);
//					whatsAppService.sendWhatsApp(detail.getMobileNumber(), finalWhatsappTemplate);
				});
			}
		}
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
	 * 
	 * @return
	 */
	public List<MiniMasterDto> getEventTypeList() {

		List<MasterNotificationEventType> notificationTypes = notificationEventTypeRepository.findAll();
		List<MiniMasterDto> notificationTypesList = new ArrayList<MiniMasterDto>();
		if (notificationTypes != null) {
			for (MasterNotificationEventType eventType : notificationTypes) {
				MiniMasterDto masterDto = new MiniMasterDto();
				masterDto.setId(eventType.getId());
				masterDto.setName(eventType.getName());
				notificationTypesList.add(masterDto);
			}
		}
		return notificationTypesList;
	}

	private Map<String, String> getEventRoleUserMobileNumber(Facility f1, Long ft1, NotificationEventRole er,
			Long ft2) {

		if (ft1.equals(ft2)) {
			List<UserMaster> users = userMasterRepository.findUsersByFacilityIdAndFacilityTypeIdAndRoleId(f1.getId(),
					ft2, er.getRole().getId());
			Map<String, String> mobileNumbers = users.stream()
					.collect(Collectors.toMap(UserMaster::getFirstname, UserMaster::getMobileNumber));
			return mobileNumbers;
		} else if ((!ft1.equals(ft2)) && ft2.equals(2L)) {
			if (f1.getSacsId() != null) {
				List<UserMaster> users = userMasterRepository.findUsersByFacilityIdAndRole(f1.getSacsId(),
						er.getRole().getId());
				Map<String, String> mobileNumbers = users.stream()
						.collect(Collectors.toMap(UserMaster::getFirstname, UserMaster::getMobileNumber));
				return mobileNumbers;
			}
		} else if ((!ft1.equals(ft2)) && ft2.equals(1L)) {
			List<UserMaster> users = userMasterRepository.findUsersByFacilityTypeIdAndRoleId(ft2, er.getRole().getId());
			Map<String, String> mobileNumbers = users.stream()
					.collect(Collectors.toMap(UserMaster::getFirstname, UserMaster::getMobileNumber));
			return mobileNumbers;
		}
		return null;
	}
}
