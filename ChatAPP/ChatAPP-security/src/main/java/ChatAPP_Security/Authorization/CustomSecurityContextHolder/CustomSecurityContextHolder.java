package ChatAPP_Security.Authorization.CustomSecurityContextHolder;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;


public class CustomSecurityContextHolder implements SecurityContextHolderStrategy {

	private final ThreadLocal<SecurityContext> threadLocal=new ThreadLocal<SecurityContext>();
	@Override
	public void clearContext() {
		// TODO Auto-generated method stub
		this.threadLocal.remove();
	}

	@Override
	public SecurityContext getContext() {
		// TODO Auto-generated method stub
		return this.threadLocal.get();
	}

	@Override
	public void setContext(SecurityContext context) {
		// TODO Auto-generated method stub
		this.threadLocal.set(context);
	}

	@Override
	public SecurityContext createEmptyContext() {
		// TODO Auto-generated method stub
		this.setContext(new CustomSecurityContext());
		return this.threadLocal.get();
	}

	/**
     * Retrieves the custom security context.
     * @return CustomSecurityContext or null if the context is not of the expected type.
     */
    public static CustomSecurityContext getCustomSecurityContext() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context instanceof CustomSecurityContext) {
            return (CustomSecurityContext) context;
        }
        return null; // or throw an exception based on your error handling strategy
    }
    
	public static class CustomSecurityContext implements SecurityContext{
		private Authentication aut;
		private String deviceID;
		//other deviceId information
		@Override
		public Authentication getAuthentication() {
			// TODO Auto-generated method stub
			return this.aut;
		}

		@Override
		public void setAuthentication(Authentication authentication) {
			// TODO Auto-generated method stub
			this.aut=authentication;
		}



		public String getDeviceID() {
			return deviceID;
		}

		public void setDeviceID(String deviceID) {
			this.deviceID = deviceID;
		}
		//principal have to be CustomUserDetails
		//will be verify during conctruction
		public long getUserID() {
			
			return ((CustomUserDetail)this.aut.getPrincipal()).getUserID();
		}
		public CustomUserDetail getCustomUserDetails() {
			return (CustomUserDetail)this.aut.getPrincipal();
		}
	}
	
}

