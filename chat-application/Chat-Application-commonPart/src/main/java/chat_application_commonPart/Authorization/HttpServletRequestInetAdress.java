package chat_application_commonPart.Authorization;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

@Component
public interface HttpServletRequestInetAdress {

	default String getInetAdress(HttpServletRequest httpREquest) {
	
		
		return httpREquest.getRemoteAddr(); 
	}
	@Component
	public static class justEmptyClassForInterface implements HttpServletRequestInetAdress{
		
	}
}
