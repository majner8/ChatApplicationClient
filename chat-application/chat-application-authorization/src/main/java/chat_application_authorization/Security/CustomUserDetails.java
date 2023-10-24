package chat_application_authorization.Security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import chat_application_commonPart.Logger.Log4j2;

public class CustomUserDetails implements UserDetails{

	
	private Collection<? extends GrantedAuthority> autority;
	private long DatabaseVersion;
	private String userID;
	private int deviceId;
	private long logId;
	
	public int getDeviceId() {
		return deviceId;
	}

	/**Metod create new CustomUserDetails object
	 *  @return null if autority equal null, or userId is not an Integer */
	public static CustomUserDetails createCustomUserDetails(long databaseVersion,Collection<? extends GrantedAuthority> autority, String userId
			,Integer deviceId,Long logId) {
		if(autority==null) {
			Log4j2.log.warn(Log4j2.LogMarker.Security.getMarker(),"Authority collection could not be null");
			return null;
		}
		if(userId==null) {
			Log4j2.log.warn(Log4j2.LogMarker.Security.getMarker(),"userId could not be null");
			return null;
		}
		if(logId==null) {
			Log4j2.log.warn(Log4j2.LogMarker.Security.getMarker(),"logId could not be null");
			return null;
		}
		
		Log4j2.log.debug(Log4j2.LogMarker.Security.getMarker(),"CustomUserDetails was created");
		return new CustomUserDetails(autority,databaseVersion,userId,deviceId,logId);

		
	
		
	}
	
	protected CustomUserDetails(Collection<? extends GrantedAuthority> autority, long databaseVersion, String userId,
		 int deviceId,long logId) {
		this.autority = autority;
		this.DatabaseVersion = databaseVersion;
		this.userID = userId;
		this.deviceId = deviceId;
		this.logId=logId;
	}



	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return this.autority;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	/**Metod return User */
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.userID;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public Collection<? extends GrantedAuthority> getAutority() {
		return autority;
	}


	
	

	public long getDatabaseVersion() {
		return DatabaseVersion;
	}

	public long getLogId() {
		return logId;
	}

	
}
