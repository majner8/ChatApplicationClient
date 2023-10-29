package chat_application_database.AuthorizationEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;

import org.hibernate.annotations.DynamicUpdate;



@Entity(name=UserEntity.userEntityName)
@DynamicUpdate
public class UserEntity {

	public static final String userEntityName="user_name";
	public static final String emailName="email";
	public static final String country_preflixName="country_preflix";
	public static final String phoneName="phone";
	public static final String userIdName="useruuid";
	public static final String serNameName="sername";
	public static final String lastnameName="lastname";
	public static final String birthdayName="birthday";
	public static final String is_user_activeName="is_user_active";
	public static final String passwordName="password";
	public static final String versionName="version";
	
	@Column(name=emailName)
	private String email;
	@Column(name=country_preflixName)
	private String countryPreflix;
	@Column(name=phoneName)
	private String phone;
	@Id
	@Column(name=userIdName)
	private String userId;
	@Column(name=serNameName)
	private String serName;
	@Column(name=lastnameName)
	private String lastName;
	@Column(name=birthdayName)
	private Date birthDay;
	
	@Column(name=is_user_activeName)
	private boolean isUserActive;
	
	@Column(name="passwordName")
	private String password;
	
	@Version
	private long version;

	 @OneToMany(mappedBy = DeviceIdEntity.userName)
	 private List<DeviceIdEntity> devices = new ArrayList<>();


	public String getEmail() {
		return email;
	}

	public String getCountryPreflix() {
		return countryPreflix;
	}

	public String getPhone() {
		return phone;
	}

	public String getUserId() {
		return userId;
	}

	public String getSerName() {
		return serName;
	}

	public String getLastName() {
		return lastName;
	}

	public Date getBirthDay() {
		return birthDay;
	}

	public boolean isUserActive() {
		return isUserActive;
	}

	public String getPassword() {
		return password;
	}

	public long getVersion() {
		return version;
	}

	public List<DeviceIdEntity> getDevices() {
		return devices;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setCountryPreflix(String countryPreflix) {
		this.countryPreflix = countryPreflix;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setSerName(String serName) {
		this.serName = serName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}

	public void setUserActive(boolean isUserActive) {
		this.isUserActive = isUserActive;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public void setDevices(List<DeviceIdEntity> devices) {
		this.devices = devices;
	}

	
	
	

	
}
