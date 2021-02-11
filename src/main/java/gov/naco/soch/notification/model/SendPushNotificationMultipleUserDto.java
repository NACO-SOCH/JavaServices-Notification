package gov.naco.soch.notification.model;

import java.util.List;



public class SendPushNotificationMultipleUserDto {
	
	
	private List<String> emails;
	private String eventTitle;;
	private String eventMessage;
	private String eventType;
	private String batchCode;
	private String eventUniqueId;
	private String content;
	private String sendBy;
	private Long sendderId;
	private String bactchCode;
	public List<String> getEmails() {
		return emails;
	}
	public void setEmails(List<String> emails) {
		this.emails = emails;
	}
	public String getEventTitle() {
		return eventTitle;
	}
	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}
	public String getEventMessage() {
		return eventMessage;
	}
	public void setEventMessage(String eventMessage) {
		this.eventMessage = eventMessage;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getBatchCode() {
		return batchCode;
	}
	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}
	public String getEventUniqueId() {
		return eventUniqueId;
	}
	public void setEventUniqueId(String eventUniqueId) {
		this.eventUniqueId = eventUniqueId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSendBy() {
		return sendBy;
	}
	public void setSendBy(String sendBy) {
		this.sendBy = sendBy;
	}
	public Long getSendderId() {
		return sendderId;
	}
	public void setSendderId(Long sendderId) {
		this.sendderId = sendderId;
	}
	public String getBactchCode() {
		return bactchCode;
	}
	public void setBactchCode(String bactchCode) {
		this.bactchCode = bactchCode;
	}
	
	

}
