package Properties;

import java.time.Duration;

import org.springframework.stereotype.Component;

@Component
public class AuthorizationProperties {
	
	private long timeValidity;
	
	public Duration TokenValidUntil() {
		return null;
	}
}
