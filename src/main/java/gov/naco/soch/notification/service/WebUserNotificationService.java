package gov.naco.soch.notification.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

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
import gov.naco.soch.projection.UserListProjection;
import gov.naco.soch.projection.WebNotificationProjection;
import gov.naco.soch.repository.WebUserNotificationRepository;
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
	
	
	
    private static final Logger logger = LoggerFactory.getLogger(WebUserNotificationService.class);
	public WebUserNotificationDto saveWebNotification(WebUserNotificationDto webUserNotificationDto) {
		logger.info("Inside of saveWebNotification : WebUserNotificationService ");
		WebUserNotification webUserNotification = WebUserNotificationMapper.mapDtoToEntity(webUserNotificationDto);
		webUserNotification = webUserNotificationRepository.save(webUserNotification);
		if(webUserNotification!=null) {
		webUserNotificationDto.setId(webUserNotification.getId());
		}
		return webUserNotificationDto;
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
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id").ascending());
		
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
	
	
}
