package chat_application_authorization.jwt;

import java.time.Duration;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import AuthorizationDTO.TokenDTO;
import chat_application_commonPart.Config.SecurityConfiguration;
import chat_application_commonPart.Logger.Log4j2;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public interface JwtTokenInterface {

	/** 
	 *@param Version - null, if user already finish registration, 
	 *			otherwise put database 
	 *		Version in accordance with JPA Optimistic locking
	 *		Handle situation, where user complete registration from other device
	 *@param deviceID -id of device, should not be null,
	 *@return Metod generate token and return them as DTO
	 **/
	default TokenDTO generateToken(String userId,Long Version,int deviceID,boolean isUserActive,Duration tokenValidity,long logId) {
		
		
		Calendar validUntil=Calendar.getInstance();
		validUntil.setTimeZone(TimeZone.getTimeZone(ZoneOffset.UTC));
		validUntil.setTime(new Date());
		
		int minute=(int)Math.floor(tokenValidity.getSeconds()/60);
		validUntil.add(Calendar.MINUTE, minute);
		validUntil.add(Calendar.SECOND, (int)(tokenValidity.getSeconds()-minute*60));

		JWTCreator.Builder jwtBuilder= JWT.create()
				.withSubject(String.valueOf(deviceID))
				.withClaim(SecurityConfiguration.userIsActiveClaimName,String.valueOf(isUserActive) )
				.withClaim(SecurityConfiguration.userIdClaimName, String.valueOf(userId))
				//claim contain logId
				//use to logOutUserInFuture
				.withClaim(SecurityConfiguration.loginIdClaimName, logId)
				.withIssuedAt(new Date())
				.withExpiresAt(validUntil.getTime());
	
		TokenDTO token=new TokenDTO();
		token.setUserActive(isUserActive);
		token.setValidUntil(validUntil.getTime());
		token.setToken(jwtBuilder.sign(Algorithm.HMAC512(SecurityConfiguration.hashTokenPassword)));
		return token;
	}

	
	default DecodedJWT tokenValidation(HttpServletRequest request) {
		Log4j2.log.debug(Log4j2.LogMarker.Security.getMarker(),"Trying verify token");
		String header=request.getHeader(HttpHeaders.AUTHORIZATION); 
		
		if(header==null||header.isEmpty()||!header.startsWith(SecurityConfiguration.jwtTokenPreflix)) {
			Log4j2.log.debug(Log4j2.LogMarker.Security.getMarker(),"Header do not contain claim authorization or do not start with preflix");

			return null;
		 }	
		//remove preflix
		header=header.replace(SecurityConfiguration.jwtTokenPreflix, "");
		
		
		JWT.require(Algorithm.HMAC512(SecurityConfiguration.hashTokenPassword))
        .build()
        .verify(header);
      DecodedJWT jwt=JWT.decode(header);
      String sub=jwt.getSubject();
      Boolean active=jwt.getClaim(SecurityConfiguration.userIsActiveClaimName).asBoolean();
      String userID= jwt.getClaim(SecurityConfiguration.userIdClaimName).asString();
      Long logId=jwt.getClaim(SecurityConfiguration.loginIdClaimName).asLong();
      if(logId==null||sub==null||active==null||userID==null) {
		Log4j2.log.warn(Log4j2.LogMarker.Security.getMarker(),"UnSupportedJwtException, token do not contain appropriate claim");
    	throw new UnsupportedJwtException(null);

      }
      try {
    	  Integer.parseInt(sub);
      }
      catch(NumberFormatException e) {
    	  Log4j2.log.warn(Log4j2.LogMarker.Security.getMarker(),"UnSupportedJwtException, token do not contain appropriate claim");
      	throw new UnsupportedJwtException(null);
 
      }
		Log4j2.log.debug(Log4j2.LogMarker.Security.getMarker(),"Verification of token was sucesfull");

      return jwt;
	}

	@Component
	public static class justEmptyClassForInterface implements JwtTokenInterface{
		
	}
}

