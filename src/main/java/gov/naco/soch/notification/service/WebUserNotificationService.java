package gov.naco.soch.notification.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import gov.naco.soch.dto.WebUserNotificationDto;
import gov.naco.soch.entity.NotificationEvent;
import gov.naco.soch.entity.NotificationEventRole;
import gov.naco.soch.entity.UserMaster;
import gov.naco.soch.entity.WebUserNotification;
import gov.naco.soch.enums.FacilityTypeEnum;
import gov.naco.soch.mapper.UserMapperUtil;
import gov.naco.soch.dto.LoginResponseDto;
import gov.naco.soch.dto.UserDto;
import gov.naco.soch.dto.UserMasterDto;
import gov.naco.soch.dto.WebNotificationListDto;
import gov.naco.soch.notification.mapper.NotificationMapper;
import gov.naco.soch.notification.mapper.WebUserNotificationMapper;
import gov.naco.soch.projection.NotificationProjection;
import gov.naco.soch.projection.PlaceholderProjection;
import gov.naco.soch.projection.UserListProjection;
import gov.naco.soch.projection.WebNotificationProjection;
import gov.naco.soch.repository.NotificationEventPlaceholderRepository;
import gov.naco.soch.repository.NotificationEventRepository;
import gov.naco.soch.repository.NotificationEventRoleRepository;
import gov.naco.soch.repository.UserMasterRepository;
import gov.naco.soch.repository.WebUserNotificationRepository;
import gov.naco.soch.util.CommonConstants;
import gov.naco.soch.util.UserUtils;
/**
 * 
 * @author Rishad Basheer (U76718)
 *
 */
@Transactional
@Service
public class WebUserNotificationService {
	
	@Autowired
	private WebUserNotificationRepository  webUserNotificationRepository;
	@Autowired
	private NotificationEventRepository eventRepository;
	@Autowired
	private NotificationEventRoleRepository eventRoleRepository;
	@Autowired
	private UserMasterRepository userMasterRepository;
	
	@Autowired
	private NotificationEventPlaceholderRepository notificationEventPlaceholderRepository;
    
	private static final String ANGLE_BRACKET_OPEN = "[";
	private static final String ANGLE_BRACKET_CLOSED = "]";
	private static final String RECIPIENT_KEY = "recipient";
	
    private static final Logger logger = LoggerFactory.getLogger(WebUserNotificationService.class);
	public boolean saveWebNotification(Map<String, Object> placeholderMap, Long eventId) {
		logger.info("Inside of saveWebNotification : WebUserNotificationService ");
		//Fetch Notification event
		NotificationEvent event = eventRepository.findByEventIdAndIsEnabled(eventId, true).get();
		if(event.getIsSpecific()) {
			logger.info("Inside of if(event.getIsSpecific())");
			WebUserNotificationDto webUserNotificationDto = new WebUserNotificationDto();
	
			List<PlaceholderProjection> placeholdersProjection = getPlaceHoldersForTheEvent(eventId);
			List<String> placeholders = placeholdersProjection.stream().map(PlaceholderProjection::getPlaceholder)
					.collect(Collectors.toList());
			logger.info("Web Template (if) :"+event.getWebTemplate());
			String finalWebTemplate = replacePlaceHolders(event.getWebTemplate(), placeholderMap,
					String.valueOf(placeholderMap.get(CommonConstants.NOTIFICATION_SPECIFIC_RECIPIENT_NAME_PLACEHOLDER)),
					placeholders);
	        logger.info("Final Message (if) :"+finalWebTemplate);
			webUserNotificationDto.setFinalMessage(finalWebTemplate);
			if(event.getActionUrl()!=null) {
				if(placeholderMap.get(event.getActionUrl()+CommonConstants.WEB_FINAL_URL)!=null) {
					webUserNotificationDto.setFinalUrl(placeholderMap.get(event.getActionUrl()+CommonConstants.WEB_FINAL_URL).toString());
				}
			}
			webUserNotificationDto.setNotificationId(eventId);
			if(placeholderMap.get(CommonConstants.WEB_USER_ID)!=null) {
				webUserNotificationDto.setUserId(Long.parseLong(placeholderMap.get(CommonConstants.WEB_USER_ID).toString()));
				}
			if(event.getIcon() != null) {
				webUserNotificationDto.setIcon(event.getIcon());
			}
			WebUserNotification webUserNotification = WebUserNotificationMapper.mapDtoToEntity(webUserNotificationDto);
			webUserNotification = webUserNotificationRepository.save(webUserNotification);
			if(webUserNotification!=null) {
			webUserNotificationDto.setId(webUserNotification.getId());
			}	
		}
		else {
			logger.info("Inside of ELSE : if(event.getIsSpecific())");
			List<NotificationEventRole> eventRoles = eventRoleRepository.findEventRolesByEventId(eventId);
			for(NotificationEventRole role : eventRoles) {
				try {
				logger.info("Role Id from NotificationEventRole role : "+role.getRole().getId());
				logger.info("Event Id from NotificationEventRole role :"+role.getNotificationEvent().getEventId());
				List<UserMaster> userMasters = userMasterRepository.findByRoleId(role.getRole().getId());
				if(userMasters!=null) {
					logger.info("Inside of if(userMasters!=null) {");
					logger.info("Size of user masters :"+userMasters.size());
				for(UserMaster userMaster : userMasters) {
					logger.info("Inside for(UserMaster userMaster : userMasters) ");
					WebUserNotificationDto webUserNotificationDto = new WebUserNotificationDto();
					List<PlaceholderProjection> placeholdersProjection = getPlaceHoldersForTheEvent(eventId);
					List<String> placeholders = placeholdersProjection.stream().map(PlaceholderProjection::getPlaceholder)
							.collect(Collectors.toList());
					logger.info("Web Template :"+event.getWebTemplate());
					String finalWebTemplate = replacePlaceHolders(event.getWebTemplate(), placeholderMap,
							userMaster.getFirstname(),
							placeholders);
					logger.info("Final Message  :"+finalWebTemplate);
					webUserNotificationDto.setFinalMessage(finalWebTemplate);
					if(event.getActionUrl()!=null) {
						if(placeholderMap.get(event.getActionUrl()+CommonConstants.WEB_FINAL_URL)!=null) {
							webUserNotificationDto.setFinalUrl(placeholderMap.get(event.getActionUrl()+CommonConstants.WEB_FINAL_URL).toString());
						}
					}
					webUserNotificationDto.setNotificationId(eventId);
					webUserNotificationDto.setUserId(userMaster.getId());
					WebUserNotification webUserNotification = WebUserNotificationMapper.mapDtoToEntity(webUserNotificationDto);
					webUserNotification = webUserNotificationRepository.save(webUserNotification);
					if(webUserNotification!=null) {
						webUserNotificationDto.setId(webUserNotification.getId());
						}
				 }
				}
			}
			catch (Exception e) {
				logger.error("Error in role based users fetched!");
			}
			}
		}
		return true;
	}
	public Integer getWebNotificationCount(Integer userId) {
		logger.info("Inside of getWebnotificationCountFuntion: WebUserNotificationService ");
		Integer notificationCount = webUserNotificationRepository.getWebNotificationCount(userId);
		return notificationCount;
	}
	
	@SuppressWarnings("unchecked")
	public WebNotificationListDto getAllWebNotification(Integer pageNumber, Integer pageSize,Integer userId) 
	{
		if (pageNumber == null || pageSize == null) {
			pageNumber = 0;
			pageSize = 5 ;
		}
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id").descending());
		
		WebNotificationListDto webNotificationListDto = new WebNotificationListDto();
		List<WebNotificationProjection> webNotificationList = new ArrayList<>();
		Page<WebNotificationProjection> webNotificationListPage = null;
		Optional<List> webNotificationListOptional = null;
		int webNotificationCount = 0;
		
		webNotificationCount = webUserNotificationRepository.findCountOfAllWebNotificationsByUser(userId);
		webNotificationListPage = webUserNotificationRepository.findAllWebNotificationsByUserList(userId, pageable);
		webNotificationListOptional = Optional.ofNullable(webNotificationListPage.getContent());
			
		
		if (webNotificationListOptional.isPresent()) {
			webNotificationList = webNotificationListOptional.get();
		}
		List<WebUserNotificationDto>  WebUserNotificationDtoList = NotificationMapper.mapWebNotificationListProjectionToWebUserNotificationDtoList(webNotificationList);
		webNotificationListDto.setActualRecordCount(webNotificationCount);
		webNotificationListDto.setWebNotificationList(WebUserNotificationDtoList);
		return webNotificationListDto;
		
	}
	public boolean deleteWebUserNotifications() {
		logger.info("Inside of deleteWebUserNotifications() : WebUserNotificationService");
		webUserNotificationRepository.deleteAll();
		logger.info("After deleteWebUserNotifications : WebUserNotificationService ");
		return false;
	}
	
	public void updateRead(Long id) {
		try {
			
			 webUserNotificationRepository.updateReadById(id);
			logger.debug("notification with id "+id + " updated ");
			
		}catch (Exception e) {
			logger.error(e.toString());
		}
	}
	
	private String replacePlaceHolders(String template, Map<String, Object> placeholderMap, String recepient,
			List<String> placeholders) {
		Map<String, Object> values = new HashMap<>();
		for (Entry<String, Object> placeholderEntry : placeholderMap.entrySet()) {
			if (placeholders.contains(placeholderEntry.getKey())) {
				logger.info("Place holder key :"+placeholderEntry.getKey()+" Place holder value :"+placeholderEntry.getValue());
				values.put(placeholderEntry.getKey(), placeholderEntry.getValue());
			}
		}
		values.put(RECIPIENT_KEY, recepient);
		return StringSubstitutor.replace(template, values, ANGLE_BRACKET_OPEN, ANGLE_BRACKET_CLOSED);

	}
	public List<PlaceholderProjection> getPlaceHoldersForTheEvent(Long eventId) {
		return notificationEventPlaceholderRepository.findByNotificationEvent_EventId(eventId);

	}

}
