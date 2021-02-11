package gov.naco.soch.notification.model;




public class NotificationResponse {
	private String code;
    private String result;
    private Object data;
    private String errorCode;
    private String errorMessage;
	@Override
	public String toString() {
		return "NotificationResponse [code=" + code + ", result=" + result + ", data=" + data + ", errorCode="
				+ errorCode + ", errorMessage=" + errorMessage + "]";
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
    
    
	

}
