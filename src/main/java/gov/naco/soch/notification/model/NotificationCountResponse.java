/**
 * 
 */
package gov.naco.soch.notification.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Pranav MS (144958)
 * @email pranav.sasi@ust-global.com
 * @date 2021-Mar-23 11:35:09 am 
 * 
 */
public class NotificationCountResponse {

	private Integer count; 
	private LocalDateTime currentLoginTime;
	
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public LocalDateTime getCurrentLoginTime() {
		return currentLoginTime;
	}
	public void setCurrentLoginTime(LocalDateTime currentLoginTime) {
		this.currentLoginTime = currentLoginTime;
	}
	/**
	 * @param count
	 * @param currentLoginTime
	 */
	public NotificationCountResponse(Integer count, LocalDateTime currentLoginTime) {
		super();
		this.count = count;
		this.currentLoginTime = currentLoginTime;
	}
	
	
}
