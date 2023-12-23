package ChatAPP_Security.Authorization.JwtToken;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import ChatAPP_Security.Authorization.SecurityProperties;
import chatAPP_DTO.Authorization.TokenDTO;
import chatAPP_database.User.UserEntity;
import io.jsonwebtoken.UnsupportedJwtException;

public interface jwtToken {


public interface jwtTokenGenerator {

	public TokenDTO generateAuthorizationToken(
			String deviceID,
			UserEntity userEntity);
	
	/**Metod generate sign device token.
	 * @return sign token, with preflix
	 *  */
	public String generateDeviceToken(
			String deviceID);
		
	
	
	@Component
	public final static class jwtTokengeneratorClass implements jwtToken.jwtTokenGenerator{
		@Autowired
		private SecurityProperties securityProperties;


		public TokenDTO generateAuthorizationToken(
			
				String deviceID,
				UserEntity userEntity) {
			
			Calendar validUntil=this.securityProperties.getJwtTokenAuthorizationUserDuration();
			JWTCreator.Builder jwtBuilder= 
					JWT.create()
					.withSubject(String.valueOf(userEntity.getUserId()))
					.withIssuedAt(new Date())
					.withClaim(this.securityProperties.getDeviceId_TokenClaimName(), deviceID)
					.withClaim(this.securityProperties.getVersion_TokenClaimName(),userEntity.getVersion())
					.withClaim(this.securityProperties.getUserIsActive_TokenClaimName(), userEntity.isUserActive())
					
					.withExpiresAt(validUntil.getTime());

			if(!userEntity.isUserActive()) {
				//add userEntity to finish registration
				//user Entity is just as map
				jwtBuilder.withClaim(this.securityProperties.getUserEntity_TokenClaimName(),userEntity.getValues());
			}
			String jwtToken=jwtBuilder		
					.sign(this.securityProperties.getjwtTokenAuthorizationUserAlgorithm());

			TokenDTO token=new TokenDTO();
			token.setUserActive(userEntity.isUserActive());
			token.setValidUntil(validUntil.getTime());
			token.setToken(jwtToken);
			return token;
		}
		
		public String generateDeviceToken(
				String deviceID) {
			Calendar validUntil=this.securityProperties.getJwtTokenDeviceIdDuration();

			return JWT.create()
					.withSubject(deviceID)
					.withIssuedAt(new Date())
					.withExpiresAt(validUntil.getTime())
					.sign(this.securityProperties.getjwtTokenDeviceIDAlgorithm());
		
		}
		
		
	}

}



public interface jwtTokenValidator {

	public DecodedJWT jwtTokenDeviceIDTokenValidator(HttpServletRequest request);
	
	public DecodedJWT jwtTokenAuthorizationUserTokenValidator(HttpServletRequest request);

	
	

	@Component
	public static  final class jwtTokenValidationClass implements jwtTokenValidator{
		
		
		@Autowired
		private SecurityProperties securityProperties;
		
		private DecodedJWT verifyToken(String headerName, String tokenPreflix, HttpServletRequest request
				,Algorithm tokenAlgo) {
			// TODO Auto-generated method stub
			String rawToken=request.getHeader(headerName);
			if(rawToken==null) {
				throw new UnsupportedJwtException(null);
			}
			if(!rawToken.startsWith(tokenPreflix)) {
				throw new UnsupportedJwtException(null);
			}
			rawToken=rawToken.replaceFirst(tokenPreflix, "");
			JWT.require(tokenAlgo)
			.build()
			.verify(rawToken);				
			return JWT.decode(rawToken);


		}


		@Override
		public DecodedJWT jwtTokenDeviceIDTokenValidator(HttpServletRequest request) {
			// TODO Auto-generated method stub
			return this.verifyToken(this.securityProperties.getTokenDeviceIdHeaderName(), 
					this.securityProperties.getTokenDeviceIdPreflix(), request, 
					this.securityProperties.getjwtTokenDeviceIDAlgorithm());
			
		}

		@Override
		public DecodedJWT jwtTokenAuthorizationUserTokenValidator(HttpServletRequest request) {
			// TODO Auto-generated method stub
			return this.verifyToken(this.securityProperties.getTokenAuthorizationUserHederName(), 
					this.securityProperties.getTokenAuthorizationUserPreflix(), request, 
					this.securityProperties.getjwtTokenAuthorizationUserAlgorithm());
		
		}




		
		
		
	}
	

}


}
