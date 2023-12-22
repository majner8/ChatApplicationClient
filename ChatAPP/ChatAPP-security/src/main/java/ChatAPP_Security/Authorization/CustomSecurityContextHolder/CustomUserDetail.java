package ChatAPP_Security.Authorization.CustomSecurityContextHolder;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public class CustomUserDetail implements UserDetails{

	protected final long userID;
	protected final long databaseVersion;
	protected final Collection<? extends GrantedAuthority> authority;
	
	public CustomUserDetail(long userID,long databaseVersion,
			Collection<? extends GrantedAuthority> authority) {
		this.authority=authority;
		this.userID=userID;
		this.databaseVersion=databaseVersion;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return this.authority;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
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
	public long getUserID() {
		return this.userID;
	}
	public long getDatabaseVersion() {
		return this.databaseVersion;
	}

	public static class WebSocketCustomUserDetails extends CustomUserDetail{
		private final long userActivityID;
		public WebSocketCustomUserDetails(long userID, long databaseVersion,
				Collection<? extends GrantedAuthority> authority,long userActivityID) {
			super(userID, databaseVersion, authority);
			this.userActivityID=userActivityID;
		}
		public WebSocketCustomUserDetails(CustomUserDetail user,long userActivityID) {
			super(user.getUserID(), user.getDatabaseVersion(), user.getAuthorities());
			this.userActivityID=userActivityID;
		}
		
		public long getUserActivityID() {
			return userActivityID;
		}
		
	}
}

