package chat_application_commonPart.Properties;

import java.time.Duration;
import java.time.Instant;

import org.springframework.stereotype.Component;

@Component
public class AuthorizationProperties {
	
	private Duration timeValidity=Duration.ofMinutes(2);
	
	public Duration TokenValidUntil() {
		return Duration.between(Instant.now(), Instant.now().plus(this.timeValidity));
	}
}
