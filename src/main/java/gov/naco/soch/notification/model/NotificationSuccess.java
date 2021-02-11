package gov.naco.soch.notification.model;

public class NotificationSuccess{
	private String code;
    private String result;
    private Long notificationId;
    
    public NotificationSuccess() {
		
	}
    
    public NotificationSuccess(String code, String result) {
		super();
		this.code = code;
		this.result = result;
		
	}

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }



	public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

	public Long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}
    
}
