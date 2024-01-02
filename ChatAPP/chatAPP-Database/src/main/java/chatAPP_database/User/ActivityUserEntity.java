package chatAPP_database.User;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
@Entity
public class ActivityUserEntity {

	public static final String userActivityTableName="";
	

	public static final String deviceIDEntityColumnName="";
	public static final String userIDEntityColumnName="";
	public static final String loginEntityColumnName="";
	public static final String logoutEntityColumnName="";

	@Column(name=ActivityUserEntity.deviceIDEntityColumnName)
	private String deviceID;
	@Column(name=ActivityUserEntity.userIDEntityColumnName)
	private long userID;
	@Id
	private long primaryKey;
	@Column(name=ActivityUserEntity.loginEntityColumnName)
	private LocalDateTime login;
	@Column(name=ActivityUserEntity.logoutEntityColumnName)
	private LocalDateTime logout;
	
	public String getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	public long getUserID() {
		return userID;
	}
	public void setUserID(long userID) {
		this.userID = userID;
	}
	public long getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(long primaryKey) {
		this.primaryKey = primaryKey;
	}
	public LocalDateTime getLogin() {
		return login;
	}
	public void setLogin() {
		
	}
	public LocalDateTime getLogout() {
		return logout;
	}
	public void setLogout(LocalDateTime logout) {
		this.logout = logout;
	}
	
}
