package Security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import Logger.Log4j2;


public class CustomUserDetails implements UserDetails{

	
	private Collection<? extends GrantedAuthority> autority;
	private long DatabaseVersion;
	private int userId;
	private String StringUserID;
	private String deviceId;
	private long logId;
	
	public String getDeviceId() {
		return deviceId;
	}

	/**Metod create new CustomUserDetails object
	 *  @return null if autority equal null, or userId is not an Integer */
	public static CustomUserDetails createCustomUserDetails(long databaseVersion,Collection<? extends GrantedAuthority> autority, String userId
			,String deviceId,Long logId) {
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
		int converUserId;
		try {
		converUserId=Integer.parseInt(userId);
		}
		catch(NumberFormatException e) {
			Log4j2.log.warn(Log4j2.LogMarker.Security.getMarker(),"userId have to be instance of Integer");
			return null;
		}
		Log4j2.log.debug(Log4j2.LogMarker.Security.getMarker(),"CustomUserDetails was created");
		return new CustomUserDetails(autority,databaseVersion,converUserId,deviceId,logId);

		
	
		
	}
	
	protected CustomUserDetails(Collection<? extends GrantedAuthority> autority, long databaseVersion, int userId,
		 String deviceId,long logId) {
		this.autority = autority;
		this.DatabaseVersion = databaseVersion;
		this.userId = userId;
		this.StringUserID=String.valueOf(this.userId);
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
		return this.StringUserID;
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

	public int getUserId() {
		return userId;
	}

	public String getStringUserID() {
		return StringUserID;
	}
	
}
