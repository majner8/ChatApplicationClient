package chatAPP_database.Device;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;

import chatAPP_CommontPart.Log4j2.Log4j2;
import chatAPP_database.CustomJpaRepository;
import chatAPP_database.Device.DeviceIdEntity;

public interface deviceIdGenerationRepository extends CustomJpaRepository<DeviceIdEntity,String> {

	/**Metod generate deviceId and persist them
	 * Metod has implement mechanism to prevent duplicate id error */
	public default String deviceIdGeneration() {
		boolean finish=false;
		DataIntegrityViolationException ex = null;
		int i=0;
		String id=null;
		do {
			id=UUID.randomUUID().toString();
			try {
				DeviceIdEntity entity=new DeviceIdEntity();
				entity.setDeviceId(id);
				entity.setLastSeen(LocalDate.now(ZoneId.systemDefault()));
				this.save(entity);
			}
			catch(DataIntegrityViolationException e) {
				ex=e;
				Log4j2.log.warn(Log4j2.MarkerLog.Database.getMarker(),"DataIntegrityViolationException occurs, system try generate Id again");
				i++;
				continue;
			}
			finish=true;
		}
		while(i<3&&finish==false);
		if(finish==true) {
			throw ex;
		}
		
		return id;
	}
	
	
}
