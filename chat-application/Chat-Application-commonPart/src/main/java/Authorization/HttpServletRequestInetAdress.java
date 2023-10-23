package Authorization;

import javax.servlet.http.HttpServletRequest;


public interface HttpServletRequestInetAdress {

	default String getInetAdress(HttpServletRequest httpREquest) {
	
		
		return httpREquest.getRemoteAddr(); 
	}
	
}
