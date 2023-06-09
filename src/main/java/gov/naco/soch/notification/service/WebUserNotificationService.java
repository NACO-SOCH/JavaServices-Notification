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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import gov.naco.soch.dto.WebUserNotificationDto;
import gov.naco.soch.entity.Facility;
import gov.naco.soch.entity.NotificationEvent;
import gov.naco.soch.entity.NotificationEventRole;
import gov.naco.soch.entity.UserMaster;
import gov.naco.soch.entity.WebUserNotification;
import gov.naco.soch.enums.FacilityTypeEnum;
import gov.naco.soch.exception.ServiceException;
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
import gov.naco.soch.repository.FacilityRepository;
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
	
	@Autowired
	private FacilityRepository facilityRepository;
    
	private static final String ANGLE_BRACKET_OPEN = "[";
	private static final String ANGLE_BRACKET_CLOSED = "]";
	private static final String RECIPIENT_KEY = "recipient";
	
    private static final Logger logger = LoggerFactory.getLogger(WebUserNotificationService.class);
	public boolean saveWebNotification(Map<String, Object> placeholderMap, Long eventId) {
		logger.info("Inside of saveWebNotification : WebUserNotificationService ");
		//Fetch Notification event
		Optional<NotificationEvent> eventOptional = eventRepository.findByEventIdAndIsEnabled(eventId, true);
		if(eventOptional.isPresent()) {
			logger.info("Inside of if(eventOptional.isPresent()) %%%%%%%%%%%%%%%%%%%%");
			NotificationEvent event = eventOptional.get();
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
				if(placeholderMap.containsKey(CommonConstants.WEB_FINAL_URL) && placeholderMap.get(CommonConstants.WEB_FINAL_URL)!=null){
					webUserNotificationDto.setFinalUrl(event.getActionUrl()+placeholderMap.get(CommonConstants.WEB_FINAL_URL).toString());
				}
				else {
					webUserNotificationDto.setFinalUrl(event.getActionUrl());
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
			return true;
		}
		else {
			logger.info("Inside of ELSE : if(event.getIsSpecific())");
			//
			if(placeholderMap.containsKey(CommonConstants.NOTIFICATION_PLACEHOLDER_FACILITY) && placeholderMap.get(CommonConstants.NOTIFICATION_PLACEHOLDER_FACILITY) !=null) {
				logger.info("Inside of specific facility users condition!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			    Long facilityId = Long.parseLong(placeholderMap.get(CommonConstants.NOTIFICATION_PLACEHOLDER_FACILITY).toString());
			    logger.info("Facility Id :"+facilityId);
				Optional<Facility> facilityOptional = facilityRepository.findById(facilityId);
				if(facilityOptional.isPresent()) {
					logger.info("Inside of if(facilityOptional.isPresent())");
					Facility facility = facilityOptional.get();
					Long facilityTypeId = facility.getFacilityType().getId();
					List<NotificationEventRole> eventRoles = eventRoleRepository
							.findEventRolesByEventId(eventId);
					if(eventRoles.isEmpty()||eventRoles==null) {
						return false;
					}
					
					for(NotificationEventRole eventRole : eventRoles) {
						logger.info("Inside of for(NotificationEventRole eventRole : eventRoles) : Scpecific facility change!!!!!");
						Long facilityTypeIdEvent = eventRole.getRole().getFacilityType().getId();
						if(facilityTypeId == facilityTypeIdEvent) {
							logger.info("Inside of if(facilityTypeId == facilityTypeIdEvent)");
							List<UserMaster> users = getRoleBasedUsersOnFacility(facility.getId(),eventRole.getRole().getId(),facilityTypeId);
							if(users!=null) {
								logger.info("Users are fetched on roles !");
								saveNotifications(users,event,placeholderMap,eventId);
							}
						}
						else if (facilityTypeId != facilityTypeIdEvent && facilityTypeIdEvent == 2L) {
							logger.info("Inside of else if (facilityTypeId != facilityTypeIdEvent && facilityTypeIdEvent == 2L)");
							if (facility.getSacsId() != null) {
								List<UserMaster> users = userMasterRepository
										.findUsersByFacilityIdAndRole(facility.getSacsId(), eventRole.getRole().getId());
			                  if(users != null) {
			                	  logger.info("Users are fetched on roles <SACS> !");
			                	  saveNotifications(users,event,placeholderMap,eventId);
			                  }
							}
						}
						else if(facilityTypeId != facilityTypeIdEvent && facilityTypeIdEvent == 1L) {
							logger.info("Inside of else if(facilityTypeId != facilityTypeIdEvent && facilityTypeIdEvent == 1L)");
							List<UserMaster> users = userMasterRepository.findUsersByFacilityTypeIdAndRoleId(facilityTypeIdEvent,
									eventRole.getRole().getId());
							if(users != null) {
			                	  logger.info("Users are fetched on roles <NACO> !");
			                	  saveNotifications(users,event,placeholderMap,eventId);
			                  }
						}
					}
				}
				else {
					logger.info("Else : if(facilityOptional.isPresent())");
					return false;
				}
			}
			
			else {

			//
				logger.info("Inside of (ELSE)   specific facility users condition!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			
			List<NotificationEventRole> eventRoles = eventRoleRepository.findEventRolesByEventId(eventId);
			for(NotificationEventRole role : eventRoles) {
				try {
				logger.info("Role Id from NotificationEventRole role : "+role.getRole().getId());
				logger.info("Event Id from NotificationEventRole role :"+role.getNotificationEvent().getEventId());
				List<UserMaster> userMasters = userMasterRepository.findByRoleId(role.getRole().getId());
				if(userMasters!=null) {
					logger.info("Inside of if(userMasters!=null) {");
					logger.info("Size of user masters :"+userMasters.size());
					saveNotifications(userMasters, event, placeholderMap, eventId);
				}
			}
			catch (Exception e) {
				logger.error("Error in role based users fetched!");
			}
			}
			return true;
		   }
			return false;
		 }
		}
		else {
			logger.info("Inside of FALSE   :  if(eventOptional.isPresent())");
			return false;
		}
	}
	private void saveNotifications(List<UserMaster> users, NotificationEvent event, Map<String, Object> placeholderMap,
			Long eventId) {
		logger.info("Inside of  saveNotifications ())()()()()()()()()()()))))))))))))))))))))))))))))))))))))))))))))()()()()");
		for(UserMaster userMaster : users) {
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
				if(placeholderMap.containsKey(CommonConstants.WEB_FINAL_URL) && placeholderMap.get(CommonConstants.WEB_FINAL_URL)!=null) {
					webUserNotificationDto.setFinalUrl(event.getActionUrl()+placeholderMap.get(CommonConstants.WEB_FINAL_URL).toString());
				}
				else {
					webUserNotificationDto.setFinalUrl(event.getActionUrl());
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
	private List<UserMaster> getRoleBasedUsersOnFacility(Long facilityId, Long roleId, Long facilityTypeId) {
		logger.info("Inside of  getRoleBasedUsersOnFacility(Long id, Long id2, Long facilityTypeId)");
		logger.info("facilityId :"+facilityId+"     roleId :"+roleId+"   FacilityTypeId"+facilityTypeId );
		List<UserMaster> users = userMasterRepository.findUsersByFacilityIdAndFacilityTypeIdAndRoleId(facilityId,
				facilityTypeId, roleId);
		if(!users.isEmpty()) {
			return users;
		}
		else {
			logger.debug("No users on these params");
		return null;
		}
	}
	public Integer getWebNotificationCount(Integer userId) {
		logger.info("Inside of getWebnotificationCountFuntion: WebUserNotificationService ");
		try{
			LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
			if(currentUser != null){
				logger.info("User found: WebUserNotificationService ");
				Integer notificationCount = webUserNotificationRepository.getWebNotificationCount(userId);
				return notificationCount;
			}else{
				logger.info("No User found: WebUserNotificationService ");
				return 0;
			}
		}catch(Exception e){
			   throw new ServiceException("No User Found", null, HttpStatus.BAD_REQUEST);
		}
	}
	
	@SuppressWarnings("unchecked")
	public WebNotificationListDto getAllWebNotification(Integer pageNumber, Integer pageSize,Integer userId) 
	{
		logger.info("Inside of getAllWebNotification : WebUserNotificationService!");
		if (pageNumber == null || pageSize == null) {
			logger.info("Inside of if (pageNumber == null || pageSize == null)");
			pageNumber = 0;
			pageSize = 5 ;
		}
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id").descending());
		
		WebNotificationListDto webNotificationListDto = new WebNotificationListDto();
		List<WebNotificationProjection> webNotificationList = new ArrayList<>();
		Page<WebNotificationProjection> webNotificationListPage = null;
		Optional<List> webNotificationListOptional = null;
		int webNotificationCount = 0;
		logger.info("user Id :"+userId);
		webNotificationCount = webUserNotificationRepository.findCountOfAllWebNotificationsByUser(userId);
		webNotificationListPage = webUserNotificationRepository.findAllWebNotificationsByUserList(userId, pageable);
		webNotificationListOptional = Optional.ofNullable(webNotificationListPage.getContent());
			
		
		if (webNotificationListOptional.isPresent()) {
			logger.info("if (webNotificationListOptional.isPresent()), Notifications are present!!!!!!!!!!!");
			webNotificationList = webNotificationListOptional.get();
		}
		logger.info("Size of webNotificationList from DB :"+webNotificationList.size());
		List<WebUserNotificationDto>  WebUserNotificationDtoList = NotificationMapper.mapWebNotificationListProjectionToWebUserNotificationDtoList(webNotificationList);
		webNotificationListDto.setActualRecordCount(webNotificationCount);
		webNotificationListDto.setWebNotificationList(WebUserNotificationDtoList);
		logger.info("size of WebUserNotificationDtoList is "+WebUserNotificationDtoList.size());
		logger.info("Just before returning the notifications list             :          getAllWebNotification!!!!!!!!!!!!!!!!! ");
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
