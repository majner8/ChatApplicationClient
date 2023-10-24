package chat_application_database.AuthorizationEntity;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import chat_application_commonPart.Logger.Log4j2;

public interface LoginActivityEntityInterface extends JpaRepository<LoginActivityEntity,Integer>  	 
{
	
	/**Metod save actual activity time and return Id of activity to log out */
	default Integer savedNewActivity(DeviceIdEntity device,String inetAdress) {
		 LoginActivityEntity en=new LoginActivityEntity();
		 en.setDevice(device);
		 en.setInetAdress(inetAdress);
		 en.setLoginTime(LocalDateTime.now(ZoneOffset.UTC));
		 this.saveAndFlush(en);
		 return en.getPrimaryKey();
	}
	default void Logout(int idOfActivity) {
		Optional<LoginActivityEntity> en=this.findById(idOfActivity);
		if(en.isEmpty()) {
			Log4j2.log.warn(Log4j2.LogMarker.Database.getMarker(),String.format("Cannot process logout Activity, because activity id : %d   did not exist", idOfActivity));
			return;
		}
		
		en.get().setLogoutTime(LocalDateTime.now(ZoneOffset.UTC));
		this.save(en.get());
		return;
		
	}
	
}
