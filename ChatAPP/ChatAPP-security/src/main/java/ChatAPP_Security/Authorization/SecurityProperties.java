package ChatAPP_Security.Authorization;

import java.util.Calendar;

import com.auth0.jwt.algorithms.Algorithm;

public interface SecurityProperties {

	public Algorithm getjwtTokenDeviceIDAlgorithm();
	public Algorithm getjwtTokenAuthorizationUserAlgorithm();
	public String getTokenDeviceIdPreflix();
	public String getTokenAuthorizationUserPreflix();
	public String getTokenDeviceIdHeaderName();
	public String getTokenAuthorizationUserHederName();
	public Calendar getJwtTokenDeviceIdDuration();
	public Calendar getJwtTokenAuthorizationUserDuration();
	public String getDeviceIDAuthority();

	
	public String getDeviceId_TokenClaimName();
	public String getVersion_TokenClaimName();
	public String getUserIsActive_TokenClaimName();
	
	public String getUserEntity_TokenClaimName();
	
}

/* 
public static final String userIsActiveRole="Role_active";

public static final String jwtTokenPreflix="Bearer ";

public static final String	userIsActiveClaimName="Role_Active";
public static final String	userIsNotActiveRole="Role_UnActive";	
public static final String	VersionClaimName="version";
public static final String	DeviceIdClaimName="deviceId";
public static final String 	userIdClaimName="userId";
public static final String hashTokenPassword="dsaewaas";

public static final String userEntityClaimName="";
//each login has own id
public static final String loginIdClaimName="LoginId";
*/