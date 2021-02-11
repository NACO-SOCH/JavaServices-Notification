package gov.naco.soch.notification.model;

public class PushDevice {
	private Long deviceId;
	private Long userId;
	private Integer platformId;
	private String pushRegId;
	private String imei;
	private String createTime;
	private String deviceType;
	private String modifiedTime;
	
	
	public Long getDeviceId() {
		return deviceId;
	}


	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}


	public Long getUserId() {
		return userId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public Integer getPlatformId() {
		return platformId;
	}


	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}


	public String getPushRegId() {
		return pushRegId;
	}


	public void setPushRegId(String pushRegId) {
		this.pushRegId = pushRegId;
	}


	public String getImei() {
		return imei;
	}


	public void setImei(String imei) {
		this.imei = imei;
	}


	public String getCreateTime() {
		return createTime;
	}


	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}


	public String getModifiedTime() {
		return modifiedTime;
	}


	public void setModifiedTime(String modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	
	
	


	public String getDeviceType() {
		return deviceType;
	}


	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}


	@Override
	public String toString() {
		return "PushDevice [deviceId=" + deviceId + ", userId=" + userId + ", platformId=" + platformId + ", pushRegId="
				+ pushRegId + ", imei=" + imei + ", createTime=" + createTime + ", deviceType=" + deviceType
				+ ", modifiedTime=" + modifiedTime + "]";
	}


	

}
