package ChatAPP_HttpendPoint.Authorization;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import ChatAPP_Security.Authorization.DeviceID.deviceIDService;


public class DeviceIdControler implements DeviceIDEndPoint{

	@Autowired
	private  deviceIDService deviceService;
	@Override
	public ResponseEntity<String> getDeviceIDToken(HttpServletRequest request) {
		// TODO Auto-generated method stub
		String deviceID=this.deviceService.generateDeviceID();
		String jwtToken=this.deviceService.generateDeviceJwtToken(deviceID);
		return ResponseEntity.ok(jwtToken);
	}

}
