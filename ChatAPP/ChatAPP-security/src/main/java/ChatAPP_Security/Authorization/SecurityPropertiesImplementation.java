package ChatAPP_Security.Authorization;

import java.util.Calendar;

import com.auth0.jwt.algorithms.Algorithm;

public class SecurityPropertiesImplementation implements SecurityProperties {

	@Override
	public Algorithm getjwtTokenDeviceIDAlgorithm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Algorithm getjwtTokenAuthorizationUserAlgorithm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTokenDeviceIdPreflix() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTokenAuthorizationUserPreflix() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTokenDeviceIdHeaderName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTokenAuthorizationUserHederName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Calendar getJwtTokenDeviceIdDuration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Calendar getJwtTokenAuthorizationUserDuration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDeviceIDAuthority() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDeviceId_TokenClaimName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getVersion_TokenClaimName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUserIsActive_TokenClaimName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUserEntity_TokenClaimName() {
		// TODO Auto-generated method stub
		return null;
	}

}
