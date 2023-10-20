package AuthorizationEntity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name=LoginActivityEntity.loginActivityEntityName)
public class LoginActivityEntity {

	public static final String loginActivityEntityName="";
	
	public static final String inetAdressName="";
	
	public static final String loginTimeName="";
	
	public static final String logoutTimeName="";
	

	public static final String PrimaryKeyName="";

	@Column(name=inetAdressName)
	private String inetAdress;
	@Column(name=loginTimeName)
	private LocalDateTime loginTime;
	@Column(name=logoutTimeName)
	private LocalDateTime logoutTime;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name=PrimaryKeyName)
	private int primaryKey;

	@ManyToOne
	@JoinColumn(name=DeviceIdEntity.DeviceIdEntityName)
	@Column(name=DeviceIdEntity.DeviceIdEntityName)
	private DeviceIdEntity device;
	
	
	
	public DeviceIdEntity getDevice() {
		return device;
	}

	public void setDevice(DeviceIdEntity device) {
		this.device = device;
	}

	public String getInetAdress() {
		return inetAdress;
	}

	public LocalDateTime getLoginTime() {
		return loginTime;
	}

	public LocalDateTime getLogoutTime() {
		return logoutTime;
	}

	public int getPrimaryKey() {
		return primaryKey;
	}

	public void setInetAdress(String inetAdress) {
		this.inetAdress = inetAdress;
	}

	public void setLoginTime(LocalDateTime loginTime) {
		this.loginTime = loginTime;
	}

	public void setLogoutTime(LocalDateTime logoutTime) {
		this.logoutTime = logoutTime;
	}

	public void setPrimaryKey(int primaryKey) {
		this.primaryKey = primaryKey;
	}

	
	
}
