package gov.naco.soch.notification.model;

import java.time.Instant;
import java.util.Map;




public class PushNotificationStatus {
	
	private String status;
	private Long id;
	private String reason;
	private String deviceId;
	private String deviceType;
	private String content;
	private String title;
	private String message;
	private int type;
	private Map<String, String> data;
	private Integer badgeCount;
	private String heading;
	private String body;
	private String sendBy;
	private Instant sendAt;
	private Long userId;
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof PushNotificationStatus) {
			PushNotificationStatus status=(PushNotificationStatus)obj;
			if(this.getDeviceId().equals(status.getDeviceId())) {
				return true;
			}
		}
		return false;
	}
	@Override
	public int hashCode() {
		return this.deviceId.hashCode();
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getHeading() {
		return heading;
	}
	public void setHeading(String heading) {
		this.heading = heading;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getSendBy() {
		return sendBy;
	}
	public void setSendBy(String sendBy) {
		this.sendBy = sendBy;
	}
	
	public Instant getSendAt() {
		return sendAt;
	}
	public void setSendAt(Instant sendAt) {
		this.sendAt = sendAt;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Map<String, String> getData() {
		return data;
	}
	public void setData(Map<String, String> data) {
		this.data = data;
	}
	public Integer getBadgeCount() {
		return badgeCount;
	}
	public void setBadgeCount(Integer badgeCount) {
		this.badgeCount = badgeCount;
	}
	
	
	
	
	
	

}
