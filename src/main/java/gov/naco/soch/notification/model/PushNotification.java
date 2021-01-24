package gov.naco.soch.notification.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;

import gov.naco.soch.notification.service.JsonObjectParser;

import java.util.Map;


public class PushNotification {
	private long id;
	private long receiverId;
	private String title;
	private String message;
	private int type;
	private Map<String, String> data;
	private Integer badgeCount;
	private String imeis;
	private boolean read;
	private String createdTime;
	private String modifiedTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(long receiverId) {
		this.receiverId = receiverId;
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

	public String getImeis() {
		return imeis;
	}

	public void setImeis(String imeis) {
		this.imeis = imeis;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public String getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(String modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	@JsonIgnore
	public String getDataJson() {
		if (data != null) {
			try {
				return JsonObjectParser.compose(data);
			} catch (Exception e) {
				
			} 
		}
		return null;
	}

	@JsonIgnore
	@SuppressWarnings("unchecked")
	public void setDataJson(String data) {
		if (data != null) {
			try {
				this.data = (Map<String, String>) JsonObjectParser.parse(data, new TypeReference<Map<String, String>>() {});
			} catch (Exception e) {
				
			} 
		}
	}

	@Override
	public String toString() {
		return "PushNotification [id=" + id + ", receiverId=" + receiverId + ", title=" + title + ", message=" + message + ", type=" + type + ", data=" + data + ", badgeCount=" + badgeCount + ", imeis=" + imeis + ", read=" + read + ", createdTime=" + createdTime + ", modifiedTime=" + modifiedTime + "]";
	}

}
