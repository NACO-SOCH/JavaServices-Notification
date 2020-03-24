package gov.naco.soch.notification.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.naco.soch.dto.NotificationEventSaveDto;
import gov.naco.soch.notification.service.NotificationService;
import gov.naco.soch.projection.NotificationEventProjection;
import gov.naco.soch.projection.PlaceholderProjection;

@RestController
@RequestMapping("/event")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class NotificationController {
	
	@Autowired
	private NotificationService notificationService;

	private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);
	
	@GetMapping("/list")
	public List<NotificationEventProjection> getEventList()
	{
		logger.debug("Entered getEventList Method ");
		return notificationService.getEventList();
		
	}
	@GetMapping("/placeholders/{eventId}")
	public List<PlaceholderProjection> getPlaceHoldersForTheEvent(@PathVariable Long eventId)
	{
		logger.debug("Entered getPlaceHoldersForTheEvent Method ");
		return notificationService.getPlaceHoldersForTheEvent(eventId);
	}
	
	@PostMapping("/save")
	public boolean saveEvent(@Valid @RequestBody NotificationEventSaveDto notificationEventSaveDto)
	{
		logger.debug("Entered saveEvent Method ");
		notificationService.saveEvent(notificationEventSaveDto);
		return true;
		
	}
	

}
