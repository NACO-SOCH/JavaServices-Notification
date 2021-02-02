package gov.naco.soch.notification.model;

public class SendSmsModel {
	private String mobileNumber;
	private String messageText;
	
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getMessageText() {
		return messageText;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
	@Override
	public String toString() {
		return "SendSmsModel [mobileNumber=" + mobileNumber + ", messageText=" + messageText + "]";
	}
	
	

}
