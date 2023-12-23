package ChatAPP_Security.Authorization.DeviceID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ChatAPP_Security.Authorization.JwtToken.jwtToken;
import chatAPP_database.Device.deviceIdGenerationRepository;

@Component
public class deviceIDService {

	@Autowired
	private deviceIdGenerationRepository deviceIDRepo;
	@Autowired
	private jwtToken.jwtTokenGenerator jwtTokenGenerator;
	public String generateDeviceID() {
		return this.deviceIDRepo.deviceIdGeneration();
	}
	
	public String generateDeviceJwtToken(String deviceID) {
		return this.jwtTokenGenerator.generateDeviceToken(deviceID);
	}
}
