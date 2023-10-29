package chat_application_database.AuthorizationEntity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
@Entity(name = LoginActivityEntity.loginActivityEntityName)
public class LoginActivityEntity {

    public static final String loginActivityEntityName = "some_name";  // You need to provide a proper name
    public static final String inetAdressName = "ip_adress";
    public static final String loginTimeName = "login_Time";
    public static final String logoutTimeName = "logout_Time";
    public static final String PrimaryKeyName = "primary_key";
    public static final String deviceIdName = "device";

    @Column(name = inetAdressName)
    private String inetAdress;

    @Column(name = loginTimeName)
    private LocalDateTime loginTime;

    @Column(name = logoutTimeName)
    private LocalDateTime logoutTime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = PrimaryKeyName)
    private int primaryKey;

    @ManyToOne
    @JoinColumn(name = DeviceIdEntity.DeviceIdEntityName)
    private DeviceIdEntity device;

	public String getInetAdress() {
		return inetAdress;
	}

	public void setInetAdress(String inetAdress) {
		this.inetAdress = inetAdress;
	}

	public LocalDateTime getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(LocalDateTime loginTime) {
		this.loginTime = loginTime;
	}

	public LocalDateTime getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(LocalDateTime logoutTime) {
		this.logoutTime = logoutTime;
	}

	public int getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(int primaryKey) {
		this.primaryKey = primaryKey;
	}

	public DeviceIdEntity getDevice() {
		return device;
	}

	public void setDevice(DeviceIdEntity device) {
		this.device = device;
	}

    // ... getters and setters ...
    
    
}

