package chatAPP_database.User;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import chatAPP_DTO.User.UserDTO.UserAuthPasswordDTO;


@Entity(name=UserAuthEntity.userAuthTableName)
public class UserAuthEntity {
	public final static String userAuthTableName="passwords";
	public final static String userIdEntityColumnName="user_id";
	public final static String passwordEntityColumnName="password";
	public final static String lastChangePasswordEntityColumnName="last_change_password";

	@Id
	@Column(name=UserAuthEntity.userIdEntityColumnName)
	private long userId;
	@Column(name=UserAuthEntity.passwordEntityColumnName)
	private String password;
	@Column(name=UserAuthEntity.lastChangePasswordEntityColumnName)
	private LocalDateTime lastChangePassword;
	
	public UserAuthEntity(UserAuthPasswordDTO user,long userID) {
		this.userId=userID;
		this.password=user.getPassword();
		this.lastChangePassword=user.getLastPasswordChange();
		
	}
	
	public UserAuthEntity() {
		
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public LocalDateTime getLastChangePassword() {
		return lastChangePassword;
	}
	public void setLastChangePassword(LocalDateTime lastChangePassword) {
		this.lastChangePassword = lastChangePassword;
	}
	
	
}
