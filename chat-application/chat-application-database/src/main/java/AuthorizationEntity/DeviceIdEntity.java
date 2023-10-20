package AuthorizationEntity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity(name=DeviceIdEntity.DeviceIdEntityName)
public class DeviceIdEntity {

	public static final String DeviceIdEntityName="";
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="device_id")
	private String deviceID;

	@ManyToOne
	@JoinColumn(name=UserEntity.userIdName)
	@Column(name=UserEntity.userIdName)
	private UserEntity user;

	
	 @OneToMany(mappedBy = DeviceIdEntity.DeviceIdEntityName)
	 private List<LoginActivityEntity> activity = new ArrayList<>();


	public String getDeviceID() {
		return deviceID;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}
	
	
	
}
