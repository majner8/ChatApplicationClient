package chat_application_database.AuthorizationEntity;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import chat_application_commonPart.Logger.Log4j2;



// device id will be generated on user by autoIncreament
public interface DeviceIdEntityRepositoryInterface  extends JpaRepository<DeviceIdEntity,Integer> { 	
	/**Metod verify if id exist in database, otherwise return new generated ID */
	default DeviceIdEntity DeviceIdGeneration(UserEntity user,Integer deviceID) {
		if(user==null) {
			Log4j2.log.fatal(Log4j2.LogMarker.Database.getMarker(),"UserEntity cannot be null");
			throw new NullPointerException();
		}
		if(deviceID!=null) {
		Optional<DeviceIdEntity> id=this.findById(deviceID);
		if(id.isPresent()) {
			return id.get();
		}
		}
		DeviceIdEntity idd=new DeviceIdEntity();
		idd.setUser(user);
		this.saveAndFlush(idd);
		return idd;
	}
	
	
}

