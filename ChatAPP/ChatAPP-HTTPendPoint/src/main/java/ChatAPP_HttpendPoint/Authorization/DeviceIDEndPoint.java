package ChatAPP_HttpendPoint.Authorization;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public interface DeviceIDEndPoint {

	public static final String deviceIdPath="";

	/**Metod reuturn device ID token, have to be send with every request */
	@GetMapping(deviceIdPath)
	public ResponseEntity<String> getDeviceIDToken(HttpServletRequest request);
}
