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
	private int deviceID;

	@ManyToOne
	@JoinColumn(name=UserEntity.userIdName)
	@Column(name=UserEntity.userIdName)
	private UserEntity user;

	
	 @OneToMany(mappedBy = DeviceIdEntity.DeviceIdEntityName)
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
