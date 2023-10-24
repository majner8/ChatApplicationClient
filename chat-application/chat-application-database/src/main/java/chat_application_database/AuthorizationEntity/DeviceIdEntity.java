package chat_application_database.AuthorizationEntity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity(name=DeviceIdEntity.DeviceIdEntityName)
public class DeviceIdEntity {

	public static final String DeviceIdEntityName="device_id";
	public static final String activityName="userActivity";
	public static final String userName="user";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name=DeviceIdEntityName)
	private int deviceID;

	@JoinColumn(name=UserEntity.userIdName)
	@Column(name=userName)
	private UserEntity user;

	
	 @OneToMany(mappedBy = LoginActivityEntity.deviceIdName)
	 @Column(name=activityName)
	 private List<LoginActivityEntity> activity = new ArrayList<>();


	public int getDeviceID() {
		return deviceID;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setDeviceID(int deviceID) {
		this.deviceID = deviceID;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}
	
	
	
}
